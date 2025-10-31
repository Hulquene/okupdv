package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.ExpenseDao;
import com.okutonda.okudpdv.data.entities.Expense;
import com.okutonda.okudpdv.data.entities.PaymentMode;
import com.okutonda.okudpdv.data.entities.User;
import com.okutonda.okudpdv.helpers.UserSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller para gestão de despesas
 */
public class ExpenseController {

    private final ExpenseDao dao;
    private final UserSession session = UserSession.getInstance();

    public ExpenseController() {
        this.dao = new ExpenseDao();
    }

    // ==========================================================
    // 🔹 CRUD OPERATIONS
    // ==========================================================
    public Expense criarDespesa(Expense despesa) {
        if (!validarDespesa(despesa)) {
            throw new IllegalArgumentException("Dados da despesa inválidos");
        }

        // Define usuário logado
        User usuarioLogado = session.getUser();
        if (usuarioLogado != null) {
            despesa.setUser(usuarioLogado);
        }

        // Define número sequencial se não informado
        if (despesa.getNumber() == null || despesa.getNumber() == 0) {
            despesa.setNumber(dao.getNextNumber());
        }

        return dao.save(despesa);
    }

    public Expense atualizarDespesa(Expense despesa) {
        if (despesa == null || despesa.getId() == null) {
            throw new IllegalArgumentException("Despesa inválida para atualização");
        }

        if (!validarDespesa(despesa)) {
            throw new IllegalArgumentException("Dados da despesa inválidos");
        }

        return dao.update(despesa);
    }

    public void excluirDespesa(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID da despesa inválido");
        }

        Expense despesa = dao.findById(id).orElse(null);
        if (despesa == null) {
            throw new IllegalArgumentException("Despesa não encontrada");
        }

        dao.delete(id);
    }

    public Expense buscarPorId(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        return dao.findById(id).orElse(null);
    }

    public List<Expense> listarTodas() {
        return dao.findAll();
    }

    // ==========================================================
    // 🔹 MÉTODOS DE BUSCA E FILTRO
    // ==========================================================
    public List<Expense> filtrar(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return listarTodas();
        }
        return dao.filter(texto.trim());
    }

    public List<Expense> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de período são obrigatórias");
        }

        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data inicial não pode ser após data final");
        }

        return dao.filterByDate(dataInicio, dataFim);
    }

    public List<Expense> listarPorCategoria(Integer categoriaId) {
        if (categoriaId == null || categoriaId <= 0) {
            throw new IllegalArgumentException("ID da categoria inválido");
        }
        return dao.findByCategory(categoriaId);
    }

    public List<Expense> listarPorFornecedor(Integer fornecedorId) {
        if (fornecedorId == null || fornecedorId <= 0) {
            throw new IllegalArgumentException("ID do fornecedor inválido");
        }
        return dao.findBySupplier(fornecedorId);
    }

    public List<Expense> listarPorModoPagamento(PaymentMode modoPagamento) {
        if (modoPagamento == null) {
            throw new IllegalArgumentException("Modo de pagamento é obrigatório");
        }
        return dao.findByPaymentMode(modoPagamento);
    }

    public List<Expense> listarPorStatus(Integer status) {
        if (status == null) {
            throw new IllegalArgumentException("Status é obrigatório");
        }
        return dao.findByStatus(status);
    }

    // ==========================================================
    // 🔹 CÁLCULOS E RELATÓRIOS
    // ==========================================================
    public BigDecimal calcularTotalDespesas(List<Expense> despesas) {
        if (despesas == null || despesas.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return despesas.stream()
                .map(Expense::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de período são obrigatórias");
        }

        Double total = dao.calculateTotalByPeriod(dataInicio, dataFim);
        return BigDecimal.valueOf(total);
    }

    public BigDecimal calcularTotalPorCategoria(Integer categoriaId) {
        if (categoriaId == null || categoriaId <= 0) {
            throw new IllegalArgumentException("ID da categoria inválido");
        }

        List<Expense> despesas = dao.findByCategory(categoriaId);
        return calcularTotalDespesas(despesas);
    }

    // ==========================================================
    // 🔹 VALIDAÇÕES DE NEGÓCIO
    // ==========================================================
    public boolean validarDespesa(Expense despesa) {
        if (despesa == null) {
            return false;
        }

        // Descrição obrigatória
        if (despesa.getDescription() == null || despesa.getDescription().trim().isEmpty()) {
            return false;
        }

        // Total obrigatório e positivo
        if (despesa.getTotal() == null || despesa.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        // Data obrigatória
        if (despesa.getDate() == null || despesa.getDate().trim().isEmpty()) {
            return false;
        }

        // Modo de pagamento obrigatório
        if (despesa.getMode() == null) {
            return false;
        }

        return true;
    }

    public Integer obterProximoNumero() {
        return dao.getNextNumber();
    }
}
