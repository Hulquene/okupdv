package com.okutonda.okudpdv.services;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.dao.InvoiceDao;
import com.okutonda.okudpdv.data.dao.PaymentDao;
import com.okutonda.okudpdv.data.entities.DocumentType;
import com.okutonda.okudpdv.data.entities.Invoices;
import com.okutonda.okudpdv.data.entities.Payment;
import com.okutonda.okudpdv.data.entities.PaymentMode;
import com.okutonda.okudpdv.data.entities.PaymentStatus;
import com.okutonda.okudpdv.data.entities.ProductSales;
import com.okutonda.okudpdv.data.entities.StockMovement;
import com.okutonda.okudpdv.data.entities.User;
import com.okutonda.okudpdv.helpers.UserSession;
import com.okutonda.okudpdv.helpers.PrintHelper;
import com.okutonda.okudpdv.helpers.Util;
import com.okutonda.okudpdv.helpers.UtilSaft;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Service layer para gestão de Faturas (Invoices)
 */
public class InvoiceService {

    private final InvoiceDao invoiceDao;
    private final PaymentDao paymentDao;

    private final UserSession userSession;

    public InvoiceService() {
        this.invoiceDao = new InvoiceDao();
        this.paymentDao = new PaymentDao();
        this.userSession = UserSession.getInstance();
    }

    // ==========================================================
    // 🔹 OPERAÇÕES PRINCIPAIS - ATUALIZADAS COM REGRAS DE NEGÓCIO
    // ==========================================================
    /**
     * Cria uma nova fatura com produtos e pagamentos e aplica regras de status
     */
    public Invoices criarFaturaComProdutosEPagamentos(Invoices fatura, List<ProductSales> produtos, List<Payment> pagamentos) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            System.out.println("🔹 INICIANDO CRIAÇÃO DE FATURA COM PAGAMENTOS");

            // 1. Validar dados básicos
            validarFatura(fatura);
            prepararDadosFatura(fatura);

            // 2. Se não veio com pagamentos, criar um pagamento automático
            if (pagamentos == null || pagamentos.isEmpty()) {
                System.out.println("💰 Nenhum pagamento fornecido, criando pagamento automático...");
                pagamentos = criarPagamentoAutomatico(fatura);
            }

            // 3. Validar pagamentos
            validarPagamentos(pagamentos, fatura.getTotal());

            // 4. 🔹 APLICAR REGRAS DE STATUS BASEADO NOS PAGAMENTOS
            aplicarRegrasStatusFatura(fatura, pagamentos);

            // 5. Salvar fatura PRIMEIRO (para obter ID)
            System.out.println("💾 Salvando fatura...");
            session.persist(fatura);
            session.flush(); // 🔥 CRÍTICO: Força INSERT para obter ID

            Integer faturaId = fatura.getId();
            System.out.println("✅ Fatura salva - ID: " + faturaId + ", Número: " + fatura.getNumber() + ", Status: " + fatura.getStatus());

            // 6. Salvar produtos SEPARADAMENTE
            if (produtos != null && !produtos.isEmpty()) {
                System.out.println("📦 Salvando " + produtos.size() + " produtos...");

                for (ProductSales ps : produtos) {
                    ps.setDocumentId(faturaId);
                    ps.setInvoice(fatura);

                    System.out.println("🔍 ProductSales - DocumentId: " + ps.getDocumentId()
                            + ", ProductId: " + ps.getProduct().getId()
                            + ", Qty: " + ps.getQty());

                    if (!ps.isValid()) {
                        throw new RuntimeException("Item de venda inválido: " + ps.getDescription());
                    }

                    session.persist(ps);
                    System.out.println("✅ ProductSales salvo - DocumentId: " + ps.getDocumentId());

                    // Registra movimento de stock (saída) - igual ao OrderController
                    StockMovement mov = new StockMovement();
                    mov.setProduct(ps.getProduct());
                    mov.setQuantity(-ps.getQty());
                    mov.setOrigin("VENDA");
                    mov.setType("SAIDA");
                    mov.setReason("VENDA " + fatura.getPrefix() + "/" + fatura.getNumber());
                    mov.setUser(fatura.getSeller());
                    session.persist(mov);
                    System.out.println("✅ Movimento de stock registrado");
                }
            } else {
                System.out.println("ℹ️ Nenhum produto para salvar");
            }

            // 7. Salvar os pagamentos
            System.out.println("💳 Salvando " + pagamentos.size() + " pagamentos...");
            for (Payment p : pagamentos) {
                configurarPagamento(p, fatura);
                session.persist(p);
                System.out.println("✅ Pagamento salvo - " + p.getPaymentMode() + ": " + p.getTotal() + ", Status: " + p.getStatus());
            }

            // 8. 🔹 ATUALIZAR TOTAL PAGO NA FATURA
            atualizarTotalPagoFatura(fatura, pagamentos);

            tx.commit();

            System.out.println("🎉 Fatura criada com sucesso! ID: " + faturaId + ", Status Final: " + fatura.getStatus());
            showSuccessMessage("Fatura criada com sucesso!\nNúmero: "
                    + fatura.getPrefix() + "/" + fatura.getNumber() + "\nStatus: " + fatura.getStatus().getDescricao());

            return fatura;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao criar fatura com pagamentos: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar fatura: " + e.getMessage(), e);
        }
    }

    /**
     * 🔹 REGRAS DE NEGÓCIO: Aplica status da fatura baseado nos pagamentos
     */
    private void aplicarRegrasStatusFatura(Invoices fatura, List<Payment> pagamentos) {
        BigDecimal totalFatura = fatura.getTotal();
        BigDecimal totalPagamentos = calcularTotalPagamentosEfetivos(pagamentos);

        System.out.println("💰 REGRAS DE STATUS - Total Fatura: " + totalFatura + ", Total Pagamentos: " + totalPagamentos);

        // 🔹 REGRA 1: Se total pago >= total fatura → FATURA PAGA
        if (totalPagamentos.compareTo(totalFatura) >= 0) {
            fatura.setStatus(PaymentStatus.PAGO);
            System.out.println("✅ Status: PAGA (Total pago >= Total fatura)");
        } // 🔹 REGRA 2: Se total pago > 0 mas < total fatura → FATURA PARCIAL
        else if (totalPagamentos.compareTo(BigDecimal.ZERO) > 0 && totalPagamentos.compareTo(totalFatura) < 0) {
            fatura.setStatus(PaymentStatus.PARCIAL);
            System.out.println("🟡 Status: PARCIAL (Total pago: " + totalPagamentos + " < Total fatura: " + totalFatura + ")");
        } // 🔹 REGRA 3: Se total pago = 0 → FATURA PENDENTE
        else {
            fatura.setStatus(PaymentStatus.PENDENTE);
            System.out.println("🟠 Status: PENDENTE (Nenhum pagamento efetivo)");
        }

        // 🔹 REGRA 4: Verificar se há pagamentos em atraso
        if (existemPagamentosAtrasados(pagamentos, fatura.getDueDate())) {
            System.out.println("⚠️  Aviso: Existem pagamentos em atraso");
            // Se a fatura não está paga e tem pagamentos atrasados, marcar como atrasada
            if (!fatura.isPaga() && !fatura.isParcial()) {
                fatura.setStatus(PaymentStatus.ATRASADO);
                System.out.println("🔴 Status atualizado para: ATRASADO");
            }
        }
    }

    /**
     * 🔹 Calcula total dos pagamentos EFETIVOS (apenas pagos)
     */
    private BigDecimal calcularTotalPagamentosEfetivos(List<Payment> pagamentos) {
        return pagamentos.stream()
                .filter(p -> p.getStatus() == PaymentStatus.PAGO || p.getStatus() == PaymentStatus.PARCIAL)
                .map(Payment::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 🔹 Verifica se existem pagamentos em atraso
     */
    private boolean existemPagamentosAtrasados(List<Payment> pagamentos, String dataVencimento) {
        if (dataVencimento == null) {
            return false;
        }

        try {
            LocalDate vencimento = LocalDate.parse(dataVencimento.substring(0, 10));
            LocalDate hoje = LocalDate.now();

            // Se a data de vencimento já passou
            if (vencimento.isBefore(hoje)) {
                return true;
            }

            // Verificar se há pagamentos com data posterior ao vencimento
            for (Payment p : pagamentos) {
                if (p.getDate() != null && p.getDate().length() >= 10) {
                    LocalDate dataPagamento = LocalDate.parse(p.getDate().substring(0, 10));
                    if (dataPagamento.isAfter(vencimento)) {
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

    /**
     * 🔹 Atualiza o total pago na fatura
     */
    private void atualizarTotalPagoFatura(Invoices fatura, List<Payment> pagamentos) {
        BigDecimal totalPago = calcularTotalPagamentosEfetivos(pagamentos);
        fatura.setPayTotal(totalPago);
        System.out.println("💰 Total pago atualizado: " + totalPago);
    }

    // ==========================================================
    // 🔹 MÉTODOS PARA ATUALIZAÇÃO DE STATUS EM TEMPO REAL
    // ==========================================================
    /**
     * Atualiza o status da fatura baseado nos pagamentos atuais
     */
    public Invoices atualizarStatusFatura(Integer faturaId) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Invoices fatura = buscarPorId(faturaId);
            if (fatura == null) {
                throw new IllegalArgumentException("Fatura não encontrada: " + faturaId);
            }

            // Buscar pagamentos atuais da fatura
            List<Payment> pagamentos = paymentDao.findByInvoiceId(faturaId);

            // Aplicar regras de status
            aplicarRegrasStatusFatura(fatura, pagamentos);

            // Atualizar total pago
            atualizarTotalPagoFatura(fatura, pagamentos);

            // Salvar alterações
            session.update(fatura);
            tx.commit();

            System.out.println("🔄 Status da fatura " + faturaId + " atualizado para: " + fatura.getStatus());
            return fatura;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar status da fatura: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar status da fatura", e);
        }
    }

    /**
     * Adiciona um pagamento a uma fatura existente e atualiza status
     */
    public Payment adicionarPagamentoAFatura(Integer faturaId, Payment pagamento) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Invoices fatura = buscarPorId(faturaId);
            if (fatura == null) {
                throw new IllegalArgumentException("Fatura não encontrada: " + faturaId);
            }

            // Configurar pagamento
            configurarPagamento(pagamento, fatura);

            // Salvar pagamento
            session.persist(pagamento);

            // Buscar todos os pagamentos da fatura (incluindo o novo)
            List<Payment> todosPagamentos = paymentDao.findByInvoiceId(faturaId);
            todosPagamentos.add(pagamento);

            // Atualizar status da fatura
            aplicarRegrasStatusFatura(fatura, todosPagamentos);
            atualizarTotalPagoFatura(fatura, todosPagamentos);

            session.update(fatura);
            tx.commit();

            System.out.println("✅ Pagamento adicionado à fatura " + faturaId + ". Novo status: " + fatura.getStatus());
            return pagamento;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao adicionar pagamento à fatura: " + e.getMessage());
            throw new RuntimeException("Erro ao adicionar pagamento à fatura", e);
        }
    }

    // ==========================================================
    // 🔹 MÉTODOS EXISTENTES (com pequenas melhorias)
    // ==========================================================
    /**
     * Cria pagamento automático quando não são fornecidos pagamentos
     */
    private List<Payment> criarPagamentoAutomatico(Invoices fatura) {
        Payment pagamento = new Payment();

        // Configurar pagamento automático (dinheiro como padrão)
        pagamento.setDescription("Pagamento automático - Fatura " + fatura.getPrefix() + "/" + fatura.getNumber());
        pagamento.setTotal(fatura.getTotal());
        pagamento.setReference(gerarReferenciaPagamento(fatura));
        pagamento.setPaymentMode(PaymentMode.NU); // Numerário como padrão
        pagamento.setDate(Util.currentDateFormatted());
        pagamento.setCurrency("AOA");
        pagamento.setStatus(PaymentStatus.PAGO);

        return List.of(pagamento);
    }

    /**
     * Configura os dados do pagamento para a fatura
     */
    private void configurarPagamento(Payment p, Invoices fatura) {
        p.setInvoiceId(fatura.getId());
        p.setInvoiceType(DocumentType.FT); // FT para faturas
        p.setPrefix(fatura.getPrefix());
        p.setNumber(fatura.getNumber());
        p.setClient(fatura.getClient());
        p.setUser(fatura.getSeller());

        // Valores padrão se não definidos
        if (p.getDate() == null) {
            p.setDate(Util.currentDateFormatted());
        }
        if (p.getCurrency() == null) {
            p.setCurrency("AOA");
        }
        if (p.getStatus() == null) {
            p.setStatus(PaymentStatus.PAGO);
        }
        if (p.getReference() == null) {
            p.setReference(gerarReferenciaPagamento(fatura));
        }
    }

    /**
     * Gera referência única para pagamento
     */
    private String gerarReferenciaPagamento(Invoices fatura) {
        return "PAY-FT-" + fatura.getPrefix() + "-" + fatura.getNumber() + "-" + System.currentTimeMillis();
    }

    /**
     * Valida os pagamentos
     */
    private void validarPagamentos(List<Payment> pagamentos, BigDecimal totalFatura) {
        if (pagamentos == null || pagamentos.isEmpty()) {
            throw new IllegalArgumentException("Lista de pagamentos não pode ser vazia");
        }

        BigDecimal totalPagamentos = BigDecimal.ZERO;
        for (Payment p : pagamentos) {
            if (p.getTotal() == null || p.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Total do pagamento deve ser maior que zero");
            }
            if (p.getPaymentMode() == null) {
                throw new IllegalArgumentException("Modo de pagamento é obrigatório");
            }
            totalPagamentos = totalPagamentos.add(p.getTotal());
        }

        // 🔹 ATUALIZAÇÃO: Permitir pagamento parcial
        // Não lançar exceção se total for menor, apenas avisar
        if (totalPagamentos.compareTo(totalFatura) < 0) {
            System.out.println("⚠️  Aviso: Total dos pagamentos (" + totalPagamentos + ") é menor que o total da fatura (" + totalFatura + ") - Fatura ficará como PARCIAL");
        }
    }

    // ==========================================================
    // 🔹 OPERAÇÕES PRINCIPAIS
    // ==========================================================
    /**
     * Marca fatura como paga
     */
    public Invoices marcarComoPaga(Integer id) {
        try {
            Invoices fatura = buscarPorId(id);
            if (fatura == null) {
                throw new IllegalArgumentException("Fatura não encontrada");
            }

            if (!fatura.isPendente()) {
                throw new IllegalStateException("Apenas faturas pedentes podem ser marcadas como pagas");
            }

            fatura.setStatus(PaymentStatus.PAGO); // Paga
            return invoiceDao.update(fatura);

        } catch (Exception e) {
            handleError("Erro ao marcar fatura como paga", e);
            throw new RuntimeException("Erro ao marcar fatura como paga", e);
        }
    }

    /**
     * Anula uma fatura
     */
    public Invoices anularFatura(Integer id) {
        try {
            Invoices fatura = buscarPorId(id);
            if (fatura == null) {
                throw new IllegalArgumentException("Fatura não encontrada");
            }

            if (fatura.isPaga()) {
                throw new IllegalStateException("Fatura paga não pode ser anulada");
            }

            fatura.setStatus(PaymentStatus.CANCELADO); // Anulada
            return invoiceDao.update(fatura);

        } catch (Exception e) {
            handleError("Erro ao anular fatura", e);
            throw new RuntimeException("Erro ao anular fatura", e);
        }
    }

    /**
     * Atualiza uma fatura
     */
    public Invoices atualizarFatura(Invoices fatura) {
        try {
            validarFatura(fatura);
            return invoiceDao.update(fatura);

        } catch (Exception e) {
            handleError("Erro ao atualizar fatura", e);
            throw new RuntimeException("Erro ao atualizar fatura", e);
        }
    }

    /**
     * Exclui uma fatura
     */
    public void excluirFatura(Integer id) {
        try {
            Invoices fatura = buscarPorId(id);
            if (fatura == null) {
                throw new IllegalArgumentException("Fatura não encontrada");
            }

            if (fatura.isPendente() || fatura.isPaga()) {
                throw new IllegalStateException("Não é possível excluir faturas pedente ou pagas");
            }

            invoiceDao.delete(id);
            showSuccessMessage("Fatura excluída com sucesso!");

        } catch (Exception e) {
            handleError("Erro ao excluir fatura", e);
            throw new RuntimeException("Erro ao excluir fatura", e);
        }
    }

    // ==========================================================
    // 🔹 CONSULTAS
    // ==========================================================
    public Invoices buscarPorId(Integer id) {
        return invoiceDao.findById(id).orElse(null);
    }

    public List<Invoices> listarTodas() {
        return invoiceDao.findAll();
    }

    public List<Invoices> filtrar(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return listarTodas();
        }
        return invoiceDao.filter(texto.trim());
    }

    public List<Invoices> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        validarPeriodo(dataInicio, dataFim);
        return invoiceDao.filterByDate(dataInicio, dataFim);
    }

    public List<Invoices> listarPorCliente(Integer clienteId) {
        if (clienteId == null || clienteId <= 0) {
            throw new IllegalArgumentException("ID do cliente inválido");
        }
        return invoiceDao.findByClientId(clienteId);
    }

    public List<Invoices> listarPorVendedor(Integer vendedorId) {
        if (vendedorId == null || vendedorId <= 0) {
            throw new IllegalArgumentException("ID do vendedor inválido");
        }
        return invoiceDao.findBySellerId(vendedorId);
    }

    public List<Invoices> listarPorPrefixo(String prefixo) {
        if (prefixo == null || prefixo.trim().isEmpty()) {
            throw new IllegalArgumentException("Prefixo inválido");
        }
        return invoiceDao.findByPrefix(prefixo.trim());
    }

    public List<Invoices> listarPendentes() {
        return invoiceDao.findPendentes();
    }

    public List<Invoices> listarEmitidas() {
        return invoiceDao.findEmitidas();
    }

    public List<Invoices> listarPagas() {
        return invoiceDao.findPagas();
    }

    public List<Invoices> listarAnuladas() {
        return invoiceDao.findAnuladas();
    }

    public List<Invoices> listarComVencimentoProximo(LocalDate data) {
        return invoiceDao.findComVencimentoProximo(data);
    }

    // ==========================================================
    // 🔹 CÁLCULOS E ESTATÍSTICAS
    // ==========================================================
    public BigDecimal calcularTotalFaturas(List<Invoices> faturas) {
        if (faturas == null || faturas.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return faturas.stream()
                .map(Invoices::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        validarPeriodo(dataInicio, dataFim);
        return invoiceDao.calculateTotalSalesByPeriod(dataInicio, dataFim);
    }

    public Integer obterProximoNumero(String prefixo) {
        return invoiceDao.getNextNumber(prefixo);
    }

    public EstatisticasFaturas calcularEstatisticas(LocalDate from, LocalDate to) {
        try {
            BigDecimal totalVendas = invoiceDao.calculateTotalSalesByPeriod(from, to);
            Long faturasEmitidas = invoiceDao.countFaturasEmitidas();
            Long faturasPagas = invoiceDao.countByStatus(PaymentStatus.PAGO);
            Long faturasPendentes = invoiceDao.countByStatus(PaymentStatus.PENDENTE);

            return new EstatisticasFaturas(
                    totalVendas,
                    faturasEmitidas, faturasPagas, faturasPendentes
            );
        } catch (Exception e) {
            System.err.println("❌ Erro ao calcular estatísticas: " + e.getMessage());
            return new EstatisticasFaturas(BigDecimal.ZERO, 0L, 0L, 0L);
        }
    }

    // ==========================================================
    // 🔹 VALIDAÇÕES
    // ==========================================================
    private void validarFatura(Invoices fatura) {
        if (fatura == null) {
            throw new IllegalArgumentException("Fatura não pode ser nula");
        }

        if (fatura.getClient() == null || fatura.getClient().getId() == null) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }

        if (fatura.getSeller() == null || fatura.getSeller().getId() == null) {
            throw new IllegalArgumentException("Vendedor é obrigatório");
        }

        if (fatura.getPrefix() == null || fatura.getPrefix().trim().isEmpty()) {
            throw new IllegalArgumentException("Prefixo é obrigatório");
        }

        if (fatura.getIssueDate() == null || fatura.getIssueDate().trim().isEmpty()) {
            throw new IllegalArgumentException("Data de emissão é obrigatória");
        }

        if (fatura.getTotal() == null || fatura.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total da fatura é inválido");
        }
    }

    private void validarPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de período são obrigatórias");
        }

        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data inicial não pode ser após data final");
        }
    }

    // ==========================================================
    // 🔹 PREPARAÇÃO DE DADOS
    // ==========================================================
    private void prepararDadosFatura(Invoices fatura) {
        // Usuário logado
        User usuarioLogado = userSession.getUser();
        if (usuarioLogado != null && fatura.getSeller() == null) {
            fatura.setSeller(usuarioLogado);
        }

        // Número sequencial
        if (fatura.getNumber() == null) {
            Integer nextNumber = obterProximoNumero(fatura.getPrefix());
            fatura.setNumber(nextNumber);
        }

        // Data de emissão padrão
        if (fatura.getIssueDate() == null) {
            fatura.setIssueDate(Util.currentDateFormatted());
        }

        // Ano atual
        if (fatura.getYear() == null) {
            fatura.setYear(Util.getYear());
        }

        // Status padrão
        if (fatura.getStatus() == null) {
            fatura.setStatus(PaymentStatus.PENDENTE); // Pendente
        }

        // Hash se for emitida
        if (fatura != null && fatura.getHash() == null) {
            fatura.setHash(gerarHashFatura(fatura));
        }
    }

    private String gerarHashFatura(Invoices fatura) {
        String numeroFatura = PrintHelper.formatDocumentNumber(
                fatura.getNumber(), fatura.getYear(), fatura.getPrefix()
        );
        return UtilSaft.appGenerateHashInvoice(
                fatura.getIssueDate(),
                fatura.getDueDate() != null ? fatura.getDueDate() : fatura.getIssueDate(),
                numeroFatura,
                String.valueOf(fatura.getTotal()),
                ""
        );
    }

    // ==========================================================
    // 🔹 UTILITÁRIOS
    // ==========================================================
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleError(String message, Exception e) {
        System.err.println(message + ": " + e.getMessage());
        JOptionPane.showMessageDialog(null, message + ": " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
    }

    // ==========================================================
    // 🔹 CLASSE INTERNA PARA ESTATÍSTICAS
    // ==========================================================
    public static class EstatisticasFaturas {

        public final BigDecimal totalVendas;
        public final Long faturasEmitidas;
        public final Long faturasPagas;
        public final Long faturasPendentes;

        public EstatisticasFaturas(BigDecimal totalVendas, Long faturasEmitidas,
                Long faturasPagas, Long faturasPendentes) {
            this.totalVendas = totalVendas;
            this.faturasEmitidas = faturasEmitidas;
            this.faturasPagas = faturasPagas;
            this.faturasPendentes = faturasPendentes;
        }
    }
}
