package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.services.InvoiceService;
import com.okutonda.okudpdv.data.entities.Invoices;
import com.okutonda.okudpdv.data.entities.Clients;
import com.okutonda.okudpdv.data.entities.Payment;
import com.okutonda.okudpdv.data.entities.ProductSales;
import com.okutonda.okudpdv.data.entities.User;
import com.okutonda.okudpdv.helpers.UserSession;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para gestão de Faturas (Invoices)
 */
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final UserSession userSession;

    public InvoiceController() {
        this.invoiceService = new InvoiceService();
        this.userSession = UserSession.getInstance();
    }

    // ==========================================================
    // 🔹 OPERAÇÕES CRUD
    // ==========================================================
    /**
     * Cria uma fatura com produtos e pagamentos
     */
    public Invoices criarFaturaComProdutosEPagamentos(Invoices fatura, List<ProductSales> produtos, List<Payment> pagamentos) {
        return invoiceService.criarFaturaComProdutosEPagamentos(fatura, produtos, pagamentos);
    }

    /**
     * Cria uma fatura completa com produtos e pagamento automático
     */
    public Invoices criarFaturaCompleta(Clients cliente, String prefixo, List<ProductSales> produtos,
            String observacoes, BigDecimal desconto, List<Payment> pagamentos) {
        try {
            // 1. Criar a fatura
            Invoices fatura = new Invoices();
            fatura.setClient(cliente);
            fatura.setPrefix(prefixo);
            fatura.setNote(observacoes);
            fatura.setDiscount(desconto != null ? desconto : BigDecimal.ZERO);
            fatura.setIssueDate(LocalDate.now().toString());
            fatura.setDueDate(LocalDate.now().plusDays(30).toString());

            // Vendedor
            User usuarioLogado = userSession.getUser();
            if (usuarioLogado != null) {
                fatura.setSeller(usuarioLogado);
            }

            // 2. Calcular totais
            calcularTotaisFatura(fatura, produtos);

            // 3. Criar fatura com pagamentos
            return invoiceService.criarFaturaComProdutosEPagamentos(fatura, produtos, pagamentos);

        } catch (Exception e) {
            System.err.println("❌ Erro ao criar fatura completa: " + e.getMessage());
            throw new RuntimeException("Erro ao criar fatura: " + e.getMessage(), e);
        }
    }

    /**
     * Método de compatibilidade (mantém o antigo sem pagamentos)
     */
    public Invoices criarFaturaComProdutos(Invoices fatura, List<ProductSales> produtos) {
        // Chama o novo método sem pagamentos (será criado automaticamente)
        return criarFaturaComProdutosEPagamentos(fatura, produtos, null);
    }

    public Invoices atualizarFatura(Invoices fatura) {
        return invoiceService.atualizarFatura(fatura);
    }

    public void excluirFatura(Integer id) {
        invoiceService.excluirFatura(id);
    }

    public Invoices buscarPorId(Integer id) {
        return invoiceService.buscarPorId(id);
    }

    public List<Invoices> listarTodas() {
        return invoiceService.listarTodas();
    }

    // ==========================================================
    // 🔹 GESTÃO DE STATUS
    // ==========================================================
//    public Invoices emitirFatura(Integer id) {
//        return invoiceService.emitirFatura(id);
//    }

    public Invoices marcarComoPaga(Integer id) {
        return invoiceService.marcarComoPaga(id);
    }

    public Invoices anularFatura(Integer id) {
        return invoiceService.anularFatura(id);
    }

    // ==========================================================
    // 🔹 CONSULTAS E FILTROS
    // ==========================================================
    public List<Invoices> filtrar(String texto) {
        return invoiceService.filtrar(texto);
    }

    public List<Invoices> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return invoiceService.listarPorPeriodo(dataInicio, dataFim);
    }

    public List<Invoices> listarPorCliente(Integer clienteId) {
        return invoiceService.listarPorCliente(clienteId);
    }

    public List<Invoices> listarPorVendedor(Integer vendedorId) {
        if (!isAdminOrManager() && !vendedorId.equals(getUsuarioLogadoId())) {
            System.err.println("❌ Acesso não autorizado para ver faturas de outros vendedores");
            return List.of();
        }
        return invoiceService.listarPorVendedor(vendedorId);
    }

    public List<Invoices> listarPorPrefixo(String prefixo) {
        return invoiceService.listarPorPrefixo(prefixo);
    }

    public List<Invoices> listarPendentes() {
        return invoiceService.listarPendentes();
    }

    public List<Invoices> listarEmitidas() {
        return invoiceService.listarEmitidas();
    }

    public List<Invoices> listarPagas() {
        return invoiceService.listarPagas();
    }

    public List<Invoices> listarAnuladas() {
        return invoiceService.listarAnuladas();
    }

    public List<Invoices> listarComVencimentoProximo(LocalDate data) {
        return invoiceService.listarComVencimentoProximo(data);
    }

    // ==========================================================
    // 🔹 CÁLCULOS E RELATÓRIOS
    // ==========================================================
    public BigDecimal calcularTotalFaturas(List<Invoices> faturas) {
        return invoiceService.calcularTotalFaturas(faturas);
    }

    public BigDecimal calcularTotalPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return invoiceService.calcularTotalPorPeriodo(dataInicio, dataFim);
    }

    public Integer obterProximoNumero(String prefixo) {
        return invoiceService.obterProximoNumero(prefixo);
    }

    public InvoiceService.EstatisticasFaturas calcularEstatisticas(LocalDate from, LocalDate to) {
        return invoiceService.calcularEstatisticas(from, to);
    }

    // ==========================================================
    // 🔹 MÉTODOS AUXILIARES
    // ==========================================================
    private boolean isAdminOrManager() {
        User user = userSession.getUser();
        return user != null
                && ("admin".equals(user.getProfile()) || "manager".equals(user.getProfile()));
    }

    private Integer getUsuarioLogadoId() {
        User user = userSession.getUser();
        return user != null ? user.getId() : null;
    }

    /**
     * Verifica se o usuário tem permissão para modificar a fatura
     */
    public boolean podeModificarFatura(Invoices fatura) {
        if (fatura == null) {
            return false;
        }

        User usuarioLogado = userSession.getUser();
        if (usuarioLogado == null) {
            return false;
        }

        if (isAdminOrManager()) {
            return true;
        }

        return fatura.getSeller() != null
                && fatura.getSeller().getId().equals(usuarioLogado.getId())
                && fatura.isPendente();
    }

    /**
     * Obtém faturas do vendedor logado
     */
    public List<Invoices> listarMinhasFaturas() {
        User usuarioLogado = userSession.getUser();
        if (usuarioLogado == null) {
            return List.of();
        }

        return invoiceService.listarPorVendedor(usuarioLogado.getId());
    }

    /**
     * Cria uma fatura completa com produtos (BASEADO NO ORDER CONTROLLER)
     */
//    public Invoices criarFaturaCompleta(Clients cliente, String prefixo, List<ProductSales> produtos,
//            String observacoes, BigDecimal desconto) {
//        try {
//            // 1. Criar a fatura
//            Invoices fatura = new Invoices();
//            fatura.setClient(cliente);
//            fatura.setPrefix(prefixo);
//            fatura.setNote(observacoes);
//            fatura.setDiscount(desconto != null ? desconto : BigDecimal.ZERO);
//            fatura.setIssueDate(LocalDate.now().toString());
//            fatura.setDueDate(LocalDate.now().plusDays(30).toString());
//
//            // Vendedor
//            User usuarioLogado = userSession.getUser();
//            if (usuarioLogado != null) {
//                fatura.setSeller(usuarioLogado);
//            }
//
//            // 2. Calcular totais
//            calcularTotaisFatura(fatura, produtos);
//
//            // 3. Usar método baseado no OrderController
//            return invoiceService.criarFaturaComProdutos(fatura, produtos);
//
//        } catch (Exception e) {
//            System.err.println("❌ Erro ao criar fatura completa: " + e.getMessage());
//            throw new RuntimeException("Erro ao criar fatura: " + e.getMessage(), e);
//        }
//    }

    /**
     * Método simplificado para criar faturas básicas
     */
//    public Invoices criarFaturaBasica(Clients cliente, String prefixo, BigDecimal total) {
//        Invoices fatura = new Invoices();
//        fatura.setClient(cliente);
//        fatura.setPrefix(prefixo);
//        fatura.setTotal(total);
//        fatura.setSubTotal(total);
//        fatura.setPayTotal(BigDecimal.ZERO);
//        fatura.setIssueDate(LocalDate.now().toString());
//        fatura.setDueDate(LocalDate.now().plusDays(30).toString());
//
//        User usuarioLogado = userSession.getUser();
//        if (usuarioLogado != null) {
//            fatura.setSeller(usuarioLogado);
//        }
//
//        return invoiceService.criarFaturaComProdutos(fatura, null);
//    }

    /**
     * Calcula totais da fatura baseado nos produtos
     */
    private void calcularTotaisFatura(Invoices fatura, List<ProductSales> produtos) {
        if (produtos == null || produtos.isEmpty()) {
            fatura.setSubTotal(BigDecimal.ZERO);
            fatura.setTotalTaxe(BigDecimal.ZERO);
            fatura.setTotal(BigDecimal.ZERO);
            return;
        }

        BigDecimal subTotal = BigDecimal.ZERO;
        BigDecimal totalImpostos = BigDecimal.ZERO;

        for (ProductSales ps : produtos) {
            BigDecimal totalItem = ps.getPrice().multiply(BigDecimal.valueOf(ps.getQty()));
            subTotal = subTotal.add(totalItem);

            if (ps.getTaxePercentage() != null && ps.getTaxePercentage().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal impostoItem = totalItem.multiply(ps.getTaxePercentage())
                        .divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
                totalImpostos = totalImpostos.add(impostoItem);
            }
        }

        BigDecimal desconto = fatura.getDiscount() != null ? fatura.getDiscount() : BigDecimal.ZERO;
        BigDecimal total = subTotal.add(totalImpostos).subtract(desconto);

        fatura.setSubTotal(subTotal);
        fatura.setTotalTaxe(totalImpostos);
        fatura.setTotal(total);
        fatura.setPayTotal(BigDecimal.ZERO);
    }
}
