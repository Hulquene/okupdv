package com.okutonda.okudpdv.services;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.dao.PurchaseDao;
import com.okutonda.okudpdv.data.dao.PurchasePaymentDao;
import com.okutonda.okudpdv.data.entities.*;
import com.okutonda.okudpdv.helpers.UserSession;
import com.okutonda.okudpdv.helpers.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;

/**
 * Service layer para gestão de Compras - CORRIGIDO
 */
public class PurchaseService {

    private final PurchaseDao purchaseDao;
    private final PurchasePaymentDao purchasePaymentDao;
    private final UserSession userSession;

    public PurchaseService() {
        this.purchaseDao = new PurchaseDao();
        this.purchasePaymentDao = new PurchasePaymentDao();
        this.userSession = UserSession.getInstance();
    }

    // ==========================================================
    // 🔹 OPERAÇÕES PRINCIPAIS - CORRIGIDAS
    // ==========================================================
    /**
     * Cria uma nova compra com produtos e pagamentos
     */
    public Purchase criarCompraComProdutosEPagamentos(Purchase compra, List<PurchaseItem> itens, List<PurchasePayment> pagamentos) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            System.out.println("🔹 INICIANDO CRIAÇÃO DE COMPRA COM PAGAMENTOS");

            // 1. Validar e preparar dados
            validarCompra(compra);
            prepararDadosCompra(compra);

            // 2. Criar pagamento automático se necessário
            if (pagamentos == null || pagamentos.isEmpty()) {
                System.out.println("💰 Nenhum pagamento fornecido, criando pagamento automático...");
                pagamentos = criarPagamentoAutomatico(compra);
            }

            // 3. Validar pagamentos
            validarPagamentos(pagamentos, compra.getTotal());

            // 4. Aplicar regras de status
            aplicarRegrasStatusCompra(compra, pagamentos);

            // 5. Salvar compra primeiro
            System.out.println("💾 Salvando compra...");
            session.persist(compra);
            session.flush();

            Integer compraId = compra.getId();
            System.out.println("✅ Compra salva - ID: " + compraId);

            // 6. Salvar itens
            if (itens != null && !itens.isEmpty()) {
                System.out.println("📦 Salvando " + itens.size() + " itens...");
                for (PurchaseItem item : itens) {
                    item.setPurchase(compra);

                    if (!validarItemCompra(item)) {
                        throw new RuntimeException("Item de compra inválido: " + item.getProduct().getDescription());
                    }

                    session.persist(item);

                    // Registrar movimento de stock
                    StockMovement mov = new StockMovement();
                    mov.setProduct(item.getProduct());
                    mov.setQuantity(item.getQuantidade());
                    mov.setOrigin("COMPRA");
                    mov.setType("ENTRADA");
                    mov.setReason("COMPRA " + compra.getInvoiceNumber());
                    mov.setUser(compra.getUser());
                    session.persist(mov);
                }
            }

            // 7. Salvar pagamentos
            System.out.println("💳 Salvando " + pagamentos.size() + " pagamentos...");
            for (PurchasePayment p : pagamentos) {
                configurarPagamento(p, compra);
                session.persist(p);
            }

            // 8. Atualizar totais e status
            atualizarTotalPagoCompra(compra, pagamentos);
            atualizarStatusStock(compra);

            tx.commit();

            System.out.println("🎉 Compra criada com sucesso! ID: " + compraId);
            showSuccessMessage("Compra criada com sucesso!\nNúmero: " + compra.getInvoiceNumber());

            return compra;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao criar compra: " + e.getMessage());
            throw new RuntimeException("Erro ao criar compra: " + e.getMessage(), e);
        }
    }

    /**
     * 🔹 CORRIGIDO: Atualiza compra com pagamentos - trata compras novas e
     * existentes
     */
    /**
     * 🔹 CORRIGIDO: Atualiza compra com pagamentos - com validação do
     * invoiceNumber da interface
     */
    public Purchase atualizarCompraComPagamentos(Purchase compra, List<PurchasePayment> pagamentos) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            System.out.println("=== ATUALIZANDO COMPRA COM PAGAMENTOS ===");
            System.out.println("Compra ID: " + compra.getId());
            System.out.println("Invoice Number: " + compra.getInvoiceNumber());
            System.out.println("Total pagamentos: " + (pagamentos != null ? pagamentos.size() : 0));

            // 🔹 VALIDAÇÃO DO INVOICE_NUMBER DA INTERFACE
            if (compra.getInvoiceNumber() == null || compra.getInvoiceNumber().trim().isEmpty()) {
                // Se veio vazio da interface, gerar automaticamente
                System.out.println("⚠️  InvoiceNumber vazio, gerando automaticamente...");
                String novoNumero = gerarInvoiceNumberAutomatico();
                compra.setInvoiceNumber(novoNumero);
                System.out.println("✅ InvoiceNumber gerado: " + compra.getInvoiceNumber());
            } else {
                // Se veio preenchido, validar se é único
                boolean numeroExiste = verificarInvoiceNumberExistente(compra.getInvoiceNumber(), compra.getId());
                if (numeroExiste) {
                    throw new RuntimeException("Número de fatura já existe: " + compra.getInvoiceNumber()
                            + ". Por favor, use um número diferente.");
                }
                System.out.println("✅ InvoiceNumber válido: " + compra.getInvoiceNumber());
            }

            // 🔹 GERAR NUMBER SEQUENCIAL (sempre automático)
            if (compra.getNumber() == null) {
                Integer nextNumber = purchaseDao.getNextNumber();
                compra.setNumber(nextNumber);
                System.out.println("✅ Number sequencial gerado: " + compra.getNumber());
            }

            // 🔹 VERIFICAR SE É UMA COMPRA NOVA (ID = null) OU EXISTENTE
            Purchase compraAtualizada;

            if (compra.getId() == null) {
                System.out.println("🆕 COMPRA NOVA - Salvando pela primeira vez...");

                // 1. Validar e preparar dados
                validarCompra(compra);
                prepararDadosCompra(compra);

                // 2. Aplicar regras de status
                aplicarRegrasStatusCompra(compra, pagamentos);

                // 3. Salvar compra PRIMEIRO
                session.persist(compra);
                session.flush();

                compraAtualizada = compra;
                System.out.println("✅ Compra salva - ID: " + compraAtualizada.getId());

            } else {
                System.out.println("📝 COMPRA EXISTENTE - Atualizando...");

                // 1. Buscar compra existente
                Purchase compraExistente = session.get(Purchase.class, compra.getId());
                if (compraExistente == null) {
                    throw new RuntimeException("Compra não encontrada com ID: " + compra.getId());
                }

                // 2. Atualizar dados (preservar number sequencial)
                compraExistente.setInvoiceNumber(compra.getInvoiceNumber()); // Mantém o da interface
                compraExistente.setPaymentStatus(compra.getPaymentStatus());
                compraExistente.setStockStatus(compra.getStockStatus());
                compraExistente.setTotal_pago(compra.getTotal_pago());
                // NÃO atualizar compraExistente.setNumber() - manter o sequencial original

                compraAtualizada = compraExistente;

                // 3. Aplicar regras de status
                aplicarRegrasStatusCompra(compraAtualizada, pagamentos);

                // 4. Remover pagamentos antigos
                System.out.println("🗑️ Removendo pagamentos antigos...");
                List<PurchasePayment> pagamentosAntigos = purchasePaymentDao.findByPurchase(compra.getId());
                for (PurchasePayment pagamentoAntigo : pagamentosAntigos) {
                    session.remove(pagamentoAntigo);
                }
            }

            // 🔹 SALVAR NOVOS PAGAMENTOS
            System.out.println("💳 Salvando " + pagamentos.size() + " pagamentos...");
            for (PurchasePayment pagamento : pagamentos) {
                configurarPagamento(pagamento, compraAtualizada);
                session.persist(pagamento);
                System.out.println("✅ Pagamento salvo - " + pagamento.getMetodo() + ": " + pagamento.getValorPago());
            }

            // 🔹 ATUALIZAR TOTAIS E STATUS FINAL
            atualizarTotalPagoCompra(compraAtualizada, pagamentos);
            atualizarStatusStock(compraAtualizada);

            session.update(compraAtualizada);
            tx.commit();

            System.out.println("🎉 Compra atualizada com sucesso! ID: " + compraAtualizada.getId()
                    + ", Número: " + compraAtualizada.getInvoiceNumber()
                    + ", Status: " + compraAtualizada.getPaymentStatus());
            return compraAtualizada;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar compra com pagamentos: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar compra com pagamentos: " + e.getMessage(), e);
        }
    }

    /**
     * 🔹 Gera invoiceNumber automático no formato COMP/2025/0001 - CORRIGIDO
     */
    private String gerarInvoiceNumberAutomatico() {
        Integer nextNumber = purchaseDao.getNextNumber(); // 🔹 CORREÇÃO: usar getNextNumber()
        String ano = String.valueOf(java.time.LocalDate.now().getYear());
        return String.format("COMP/%s/%04d", ano, nextNumber);
    }

    /**
     * 🔹 Verifica se invoiceNumber já existe - CORRIGIDO para Optional
     */
    private boolean verificarInvoiceNumberExistente(String invoiceNumber, Integer compraIdIgnorar) {
        try {
            Optional<Purchase> compraOptional = purchaseDao.findByInvoiceNumber(invoiceNumber);

            if (!compraOptional.isPresent()) {
                return false;
            }

            Purchase compraEncontrada = compraOptional.get();

            // Se estamos atualizando uma compra existente, ignorar ela mesma
            if (compraIdIgnorar != null) {
                return !compraEncontrada.getId().equals(compraIdIgnorar);
            }

            return true;
        } catch (Exception e) {
            System.err.println("❌ Erro ao verificar invoiceNumber: " + e.getMessage());
            return false;
        }
    }

    // ==========================================================
    // 🔹 REGRAS DE NEGÓCIO - CORRIGIDAS
    // ==========================================================
    private void aplicarRegrasStatusCompra(Purchase compra, List<PurchasePayment> pagamentos) {
        BigDecimal totalCompra = compra.getTotal();
        BigDecimal totalPagamentos = calcularTotalPagamentosEfetivos(pagamentos);

        System.out.println("💰 REGRAS DE STATUS - Total Compra: " + totalCompra + ", Total Pagamentos: " + totalPagamentos);

        if (totalPagamentos.compareTo(totalCompra) >= 0) {
            compra.setPaymentStatus(PaymentStatus.PAGO);
            System.out.println("✅ Status: PAGA");
        } else if (totalPagamentos.compareTo(BigDecimal.ZERO) > 0) {
            compra.setPaymentStatus(PaymentStatus.PARCIAL);
            System.out.println("🟡 Status: PARCIAL");
        } else {
            compra.setPaymentStatus(PaymentStatus.PENDENTE);
            System.out.println("🟠 Status: PENDENTE");
        }

        // Verificar atrasos
        if (existemPagamentosAtrasados(pagamentos, compra.getDataVencimento())) {
            System.out.println("⚠️  Aviso: Existem pagamentos em atraso");
            if (!compra.isPaga() && !compra.isParcial()) {
                compra.setPaymentStatus(PaymentStatus.ATRASADO);
            }
        }
    }

    private void atualizarStatusStock(Purchase compra) {
        if (compra.isPaga() || compra.isParcial()) {
            compra.setStockStatus(StockStatus.DISPONIVEL);
        } else {
            compra.setStockStatus(StockStatus.PENDENTE);
        }
    }

    private BigDecimal calcularTotalPagamentosEfetivos(List<PurchasePayment> pagamentos) {
        return pagamentos.stream()
                .filter(p -> p.getStatus() == PaymentStatus.PAGO || p.getStatus() == PaymentStatus.PARCIAL)
                .map(PurchasePayment::getValorPago)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean existemPagamentosAtrasados(List<PurchasePayment> pagamentos, java.util.Date dataVencimento) {
        if (dataVencimento == null) {
            return false;
        }

        try {
            // Data atual
            java.util.Date hoje = new java.util.Date();

            // 1. Verificar se a data de vencimento já passou
            if (dataVencimento.before(hoje)) {
                return true;
            }

            // 2. Verificar se há pagamentos com data posterior ao vencimento
            for (PurchasePayment p : pagamentos) {
                if (p.getDataPagamento() != null) {
                    // Converter java.sql.Date para java.util.Date
                    java.util.Date dataPagamentoUtil = new java.util.Date(p.getDataPagamento().getTime());
                    if (dataPagamentoUtil.after(dataVencimento)) {
                        return true;
                    }
                }
            }

            return false;
        } catch (Exception e) {
            System.err.println("❌ Erro ao verificar pagamentos atrasados: " + e.getMessage());
            return false;
        }
    }

    // ==========================================================
    // 🔹 MÉTODOS AUXILIARES - CORRIGIDOS
    // ==========================================================
    private List<PurchasePayment> criarPagamentoAutomatico(Purchase compra) {
        PurchasePayment pagamento = new PurchasePayment();
        pagamento.setDescricao("Pagamento automático - Compra " + compra.getInvoiceNumber());
        pagamento.setValorPago(compra.getTotal());
        pagamento.setReferencia(gerarReferenciaPagamento(compra));
        pagamento.setMetodo(PaymentMode.NU);
        pagamento.setDataPagamento(new java.sql.Date(System.currentTimeMillis()));
        pagamento.setStatus(PaymentStatus.PAGO);
        return List.of(pagamento);
    }

    private void configurarPagamento(PurchasePayment p, Purchase compra) {
        p.setPurchaseId(compra.getId());

        if (p.getDataPagamento() == null) {
            p.setDataPagamento(new java.sql.Date(System.currentTimeMillis()));
        }
        if (p.getStatus() == null) {
            p.setStatus(PaymentStatus.PAGO);
        }
        if (p.getReferencia() == null) {
            p.setReferencia(gerarReferenciaPagamento(compra));
        }

        User usuarioLogado = userSession.getUser();
        if (usuarioLogado != null) {
            p.setUser(usuarioLogado);
        }
    }

    private String gerarReferenciaPagamento(Purchase compra) {
        return "PAY-COMP-" + compra.getInvoiceNumber() + "-" + System.currentTimeMillis();
    }

    private void validarPagamentos(List<PurchasePayment> pagamentos, BigDecimal totalCompra) {
        if (pagamentos == null || pagamentos.isEmpty()) {
            throw new IllegalArgumentException("Lista de pagamentos não pode ser vazia");
        }

        BigDecimal totalPagamentos = BigDecimal.ZERO;
        for (PurchasePayment p : pagamentos) {
            if (p.getValorPago() == null || p.getValorPago().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Valor do pagamento deve ser maior que zero");
            }
            if (p.getMetodo() == null) {
                throw new IllegalArgumentException("Método de pagamento é obrigatório");
            }
            totalPagamentos = totalPagamentos.add(p.getValorPago());
        }

        if (totalPagamentos.compareTo(totalCompra) < 0) {
            System.out.println("⚠️  Aviso: Total dos pagamentos é menor que o total da compra - Compra ficará como PARCIAL");
        }
    }

    // ==========================================================
    // 🔹 MÉTODOS DE VALIDAÇÃO - CORRIGIDOS
    // ==========================================================
    private void validarCompra(Purchase compra) {
        if (compra == null) {
            throw new IllegalArgumentException("Compra não pode ser nula");
        }
        if (compra.getSupplier() == null || compra.getSupplier().getId() == null) {
            throw new IllegalArgumentException("Fornecedor é obrigatório");
        }
        if (compra.getInvoiceType() == null) {
            throw new IllegalArgumentException("Tipo de fatura é obrigatório");
        }
    }

    private boolean validarItemCompra(PurchaseItem item) {
        return item != null
                && item.getProduct() != null
                && item.getProduct().getId() != null
                && item.getQuantidade() != null
                && item.getQuantidade() > 0
                && item.getPrecoCusto() != null
                && item.getPrecoCusto().compareTo(BigDecimal.ZERO) >= 0;
    }

    private void prepararDadosCompra(Purchase compra) {
        User usuarioLogado = userSession.getUser();
        if (usuarioLogado != null) {
            compra.setUser(usuarioLogado);
        }

        // 🔹 REMOVER - a geração agora é feita em atualizarCompraComPagamentos()
        // if (compra.getInvoiceNumber() == null || compra.getInvoiceNumber().trim().isEmpty()) {
        //     Integer nextNumber = obterProximoNumeroFatura();
        //     compra.setInvoiceNumber(gerarNumeroCompra(nextNumber));
        // }
        if (compra.getInvoiceType() == null) {
            compra.setInvoiceType(DocumentType.FT);
        }

        if (compra.getDataCompra() == null) {
            compra.setDataCompra(new java.sql.Date(System.currentTimeMillis()));
        }
        if (compra.getDataVencimento() == null) {
            // Criar java.util.Date para 30 dias no futuro
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.add(java.util.Calendar.DAY_OF_MONTH, 30);
            compra.setDataVencimento(cal.getTime());
        }

        if (compra.getPaymentStatus() == null) {
            compra.setPaymentStatus(PaymentStatus.PENDENTE);
        }
        if (compra.getStockStatus() == null) {
            compra.setStockStatus(StockStatus.PENDENTE);
        }

        calcularTotaisCompra(compra);
    }

    // ==========================================================
    // 🔹 MÉTODOS PÚBLICOS - MANTIDOS
    // ==========================================================
    public Purchase criarCompra(Purchase compra) {
        return criarCompraComProdutosEPagamentos(compra, compra.getItems(), compra.getPayments());
    }

    public Purchase atualizarCompra(Purchase compra) {
        try {
            validarCompra(compra);
            calcularTotaisCompra(compra);
            return purchaseDao.update(compra);
        } catch (Exception e) {
            handleError("Erro ao atualizar compra", e);
            throw new RuntimeException("Erro ao atualizar compra", e);
        }
    }

    public void excluirCompra(Integer id) {
        try {
            Purchase compra = buscarPorId(id);
            if (compra == null) {
                throw new IllegalArgumentException("Compra não encontrada");
            }
            validarExclusaoCompra(compra);
            purchaseDao.delete(id);
            showSuccessMessage("Compra excluída com sucesso!");
        } catch (Exception e) {
            handleError("Erro ao excluir compra", e);
            throw new RuntimeException("Erro ao excluir compra", e);
        }
    }

    private void validarExclusaoCompra(Purchase compra) {
        if (compra.getPayments() != null && !compra.getPayments().isEmpty()) {
            throw new IllegalStateException("Não é possível excluir compra com pagamentos associados");
        }
    }

    public Purchase buscarPorId(Integer id) {
        return purchaseDao.findById(id).orElse(null);
    }

    public List<Purchase> listarTodas() {
        return purchaseDao.findAll();
    }

    public List<Purchase> filtrar(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return listarTodas();
        }
        return purchaseDao.filter(texto.trim());
    }

    public List<Purchase> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        validarPeriodo(dataInicio, dataFim);
        return purchaseDao.filterByDate(dataInicio, dataFim);
    }

    public List<Purchase> listarPorFornecedor(Integer fornecedorId) {
        if (fornecedorId == null || fornecedorId <= 0) {
            throw new IllegalArgumentException("ID do fornecedor inválido");
        }
        return purchaseDao.findBySupplier(fornecedorId);
    }

    public List<Purchase> listarContasAPagar() {
        List<Purchase> todas = purchaseDao.findAll();
        return todas.stream()
                .filter(p -> PaymentStatus.PENDENTE.equals(p.getPaymentStatus())
                || PaymentStatus.PARCIAL.equals(p.getPaymentStatus()))
                .toList();
    }

    public void calcularTotaisCompra(Purchase compra) {
        if (compra == null || compra.getItems() == null) {
            return;
        }

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalIVA = BigDecimal.ZERO;

        for (PurchaseItem item : compra.getItems()) {
            if (item.getSubtotal() != null) {
                total = total.add(item.getSubtotal());
            }
            if (item.getIva() != null) {
                totalIVA = totalIVA.add(item.getIva());
            }
        }

        compra.setTotal(total);
        compra.setIvaTotal(totalIVA);
    }

    public BigDecimal calcularTotalCompras(List<Purchase> compras) {
        if (compras == null || compras.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return compras.stream()
                .map(Purchase::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        validarPeriodo(dataInicio, dataFim);
        Double total = purchaseDao.calculateTotalByPeriod(dataInicio, dataFim);
        return BigDecimal.valueOf(total != null ? total : 0.0);
    }

    public BigDecimal calcularSaldoDevedor(Purchase compra) {
        if (compra == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal totalPago = compra.getTotal_pago() != null ? compra.getTotal_pago() : BigDecimal.ZERO;
        return compra.getTotal().subtract(totalPago);
    }

    public Integer obterProximoNumeroFatura() {
        return purchaseDao.getNextNumber();
    }

    private void validarPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de período são obrigatórias");
        }
        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data inicial não pode ser após data final");
        }
    }

    private String gerarNumeroCompra(int sequencia) {
        String ano = String.valueOf(java.time.LocalDate.now().getYear());
        return String.format("COMP/%s/%04d", ano, sequencia);
    }

    private void atualizarTotalPagoCompra(Purchase compra, List<PurchasePayment> pagamentos) {
        BigDecimal totalPago = calcularTotalPagamentosEfetivos(pagamentos);
        compra.setTotal_pago(totalPago);
        System.out.println("💰 Total pago atualizado: " + totalPago);
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleError(String message, Exception e) {
        System.err.println(message + ": " + e.getMessage());
        JOptionPane.showMessageDialog(null, message + ": " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
