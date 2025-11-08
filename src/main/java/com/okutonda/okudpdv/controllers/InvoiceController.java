package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.services.InvoiceService;
import com.okutonda.okudpdv.data.entities.Invoices;
import com.okutonda.okudpdv.data.entities.Clients;
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
    public Invoices criarFatura(Invoices fatura) {
        return invoiceService.criarFatura(fatura);
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
    public Invoices emitirFatura(Integer id) {
        return invoiceService.emitirFatura(id);
    }

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
        // Controle de acesso: vendedores só veem suas próprias faturas
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
     * Cria uma nova fatura com dados básicos
     */
    public Invoices criarFaturaBasica(Clients cliente, String prefixo, BigDecimal total) {
        Invoices fatura = new Invoices();
        fatura.setClient(cliente);
        fatura.setPrefix(prefixo);
        fatura.setTotal(total);
        fatura.setSubTotal(total);
        fatura.setPayTotal(BigDecimal.ZERO);
        fatura.setIssueDate(LocalDate.now().toString());
        fatura.setDueDate(LocalDate.now().plusDays(30).toString());

        // Vendedor padrão é o usuário logado
        User usuarioLogado = userSession.getUser();
        if (usuarioLogado != null) {
            fatura.setSeller(usuarioLogado);
        }

        return criarFatura(fatura);
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

        // Admin/manager pode modificar qualquer fatura
        if (isAdminOrManager()) {
            return true;
        }

        // Vendedor só pode modificar suas próprias faturas pendentes
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
}
