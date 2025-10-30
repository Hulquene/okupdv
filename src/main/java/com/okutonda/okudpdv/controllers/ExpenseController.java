package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.ExpenseDao;
import com.okutonda.okudpdv.data.entities.Expense;

import java.util.List;

/**
 * Controller responsável pelas regras de negócio de despesas.
 * 
 * Atua como camada intermediária entre a UI e o DAO.
 * Aplica validações simples antes de persistir ou consultar dados.
 * 
 * @author Hulquene
 */
public class ExpenseController {

    private final ExpenseDao dao;

    public ExpenseController() {
        this.dao = new ExpenseDao();
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public boolean add(Expense e) {
        if (e == null) {
            System.err.println("[Controller] Despesa inválida.");
            return false;
        }
        return dao.add(e);
    }

    public boolean edit(Expense e) {
        if (e == null || e.getId() <= 0) {
            System.err.println("[Controller] Despesa inválida para atualização.");
            return false;
        }
        return dao.update(e);
    }

    public boolean delete(int id) {
        if (id <= 0) {
            System.err.println("[Controller] ID inválido para exclusão.");
            return false;
        }
        return dao.delete(id);
    }

    // ==========================================================
    // 🔹 Consultas
    // ==========================================================
    public List<Expense> listarTodas() {
        return dao.findAll();
    }

    public List<Expense> getDespesas(String dateFrom, String dateTo) {
        return dao.listDespesas(dateFrom, dateTo);
    }

    public List<Expense> filtrar(String texto) {
        return dao.filter(texto);
    }

    public Expense getById(int id) {
        return dao.findById(id);
    }
}
