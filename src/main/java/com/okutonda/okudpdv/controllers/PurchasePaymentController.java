package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.PurchasePaymentDao;
import com.okutonda.okudpdv.data.entities.PurchasePayment;
import com.okutonda.okudpdv.data.entities.PaymentMode;
import com.okutonda.okudpdv.data.entities.User;
import com.okutonda.okudpdv.helpers.UserSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller para gestão de pagamentos de compras
 */
public class PurchasePaymentController {

    private final PurchasePaymentDao dao;
    private final UserSession session = UserSession.getInstance();

    public PurchasePaymentController() {
        this.dao = new PurchasePaymentDao();
    }

    // ==========================================================
    // 🔹 CRUD OPERATIONS
    // ==========================================================
    public PurchasePayment registrarPagamento(PurchasePayment pagamento, Integer purchaseId) {
        if (!validarPagamento(pagamento)) {
            throw new IllegalArgumentException("Dados do pagamento inválidos");
        }

        // Define usuário logado
        User usuarioLogado = session.getUser();
        if (usuarioLogado != null) {
            pagamento.setUser(usuarioLogado);
        }

        pagamento.setPurchaseId(purchaseId);
        return dao.save(pagamento);
    }

    public PurchasePayment atualizarPagamento(PurchasePayment pagamento) {
        if (pagamento == null || pagamento.getId() == null) {
            throw new IllegalArgumentException("Pagamento inválido para atualização");
        }

        if (!validarPagamento(pagamento)) {
            throw new IllegalArgumentException("Dados do pagamento inválidos");
        }

        return dao.update(pagamento);
    }

    public void excluirPagamento(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID do pagamento inválido");
        }

        dao.delete(id);
    }

    public PurchasePayment buscarPorId(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        return dao.findById(id).orElse(null);
    }

    // ==========================================================
    // 🔹 MÉTODOS DE BUSCA
    // ==========================================================
    public List<PurchasePayment> listarPorCompra(Integer purchaseId) {
        if (purchaseId == null || purchaseId <= 0) {
            throw new IllegalArgumentException("ID da compra inválido");
        }
        return dao.findByPurchase(purchaseId);
    }

    public List<PurchasePayment> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de período são obrigatórias");
        }

        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data inicial não pode ser após data final");
        }

        return dao.filterByDate(dataInicio, dataFim);
    }

    public List<PurchasePayment> listarPorModoPagamento(PaymentMode modoPagamento) {
        if (modoPagamento == null) {
            throw new IllegalArgumentException("Modo de pagamento é obrigatório");
        }
        return dao.findByPaymentMode(modoPagamento);
    }

    // ==========================================================
    // 🔹 CÁLCULOS E RELATÓRIOS
    // ==========================================================
    public BigDecimal calcularTotalPago(Integer purchaseId) {
        if (purchaseId == null || purchaseId <= 0) {
            return BigDecimal.ZERO;
        }

        List<PurchasePayment> pagamentos = dao.findByPurchase(purchaseId);
        return pagamentos.stream()
                .map(PurchasePayment::getValorPago)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularSaldoDevedor(BigDecimal totalCompra, Integer purchaseId) {
        if (totalCompra == null || purchaseId == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalPago = calcularTotalPago(purchaseId);
        return totalCompra.subtract(totalPago);
    }

    public BigDecimal calcularTotalPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de período são obrigatórias");
        }

        Double total = dao.calculateTotalByPeriod(dataInicio, dataFim);
        return BigDecimal.valueOf(total);
    }

    // ==========================================================
    // 🔹 VALIDAÇÕES DE NEGÓCIO
    // ==========================================================
    public boolean validarPagamento(PurchasePayment pagamento) {
        if (pagamento == null) {
            return false;
        }

        // Valor pago obrigatório e positivo
        if (pagamento.getValorPago() == null || pagamento.getValorPago().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        // Data de pagamento obrigatória
        if (pagamento.getDataPagamento() == null) {
            return false;
        }

        // Método de pagamento obrigatório
        if (pagamento.getMetodo() == null) {
            return false;
        }

        return true;
    }

    public boolean pagamentoExcedeSaldo(BigDecimal valorPagamento, BigDecimal totalCompra, Integer purchaseId) {
        if (valorPagamento == null || totalCompra == null || purchaseId == null) {
            return true;
        }

        BigDecimal saldoDevedor = calcularSaldoDevedor(totalCompra, purchaseId);
        return valorPagamento.compareTo(saldoDevedor) > 0;
    }
    // 🔹 NOVO MÉTODO: Listar todos os pagamentos

    public List<PurchasePayment> listarTodos() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            System.err.println("❌ Erro ao listar todos os pagamentos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 🔹 NOVO MÉTODO: Converter string para PaymentMode
    public PaymentMode converterParaPaymentMode(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            return null;
        }

        for (PaymentMode mode : PaymentMode.getActiveModes()) {
            if (mode.getDescricao().equals(descricao)) {
                return mode;
            }
        }

        return null;
    }

}
