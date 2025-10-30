package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.ExpenseCategoryDao;
import com.okutonda.okudpdv.data.entities.ExpenseCategory;

import java.util.List;

/**
 * Controller responsável pela lógica de negócio das categorias de despesas.
 *
 * Intermedia a interface com o DAO e aplica validações simples.
 *
 * @author Hulquene
 */
public class ExpenseCategoryController {

    private final ExpenseCategoryDao dao;

    public ExpenseCategoryController() {
        this.dao = new ExpenseCategoryDao();
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public boolean save(ExpenseCategory c) {
        if (c == null) {
            System.err.println("[Controller] Categoria inválida (nula).");
            return false;
        }

        if (c.getId() == null || c.getId() <= 0) {
            return dao.add(c);
        } else {
            return dao.update(c);
        }
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
    public List<ExpenseCategory> getAll() {
        return dao.findAll();
    }

    public ExpenseCategory getById(int id) {
        return dao.findById(id);
    }

    public List<ExpenseCategory> searchByName(String namePart) {
        return dao.filterByName(namePart);
    }
}
