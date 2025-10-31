package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.ExpenseCategoryDao;
import com.okutonda.okudpdv.data.dao.ExpenseDao;
import com.okutonda.okudpdv.data.entities.Expense;
import com.okutonda.okudpdv.data.entities.ExpenseCategory;
import java.util.List;
import java.util.Optional;

/**
 * Controller para gestão de categorias de despesas
 */
public class ExpenseCategoryController {

    private final ExpenseCategoryDao dao;

    public ExpenseCategoryController() {
        this.dao = new ExpenseCategoryDao();
    }

    // ==========================================================
    // 🔹 CRUD OPERATIONS
    // ==========================================================
    public ExpenseCategory criarCategoria(ExpenseCategory categoria) {
        if (!validarCategoria(categoria)) {
            throw new IllegalArgumentException("Dados da categoria inválidos");
        }

        if (dao.existsByName(categoria.getName())) {
            throw new IllegalArgumentException("Já existe uma categoria com este nome");
        }

        return dao.save(categoria);
    }

    public ExpenseCategory atualizarCategoria(ExpenseCategory categoria) {
        if (categoria == null || categoria.getId() == null) {
            throw new IllegalArgumentException("Categoria inválida para atualização");
        }

        if (!validarCategoria(categoria)) {
            throw new IllegalArgumentException("Dados da categoria inválidos");
        }

        // Verifica se o nome já existe em outra categoria
        Optional<ExpenseCategory> existing = dao.findByName(categoria.getName());
        if (existing.isPresent() && !existing.get().getId().equals(categoria.getId())) {
            throw new IllegalArgumentException("Já existe outra categoria com este nome");
        }

        return dao.update(categoria);
    }

    public void excluirCategoria(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID da categoria inválido");
        }

        // Verifica se existem despesas usando esta categoria
        ExpenseDao expenseDao = new ExpenseDao();
        List<Expense> despesas = expenseDao.findByCategory(id);
        if (!despesas.isEmpty()) {
            throw new IllegalStateException("Não é possível excluir categoria com despesas associadas");
        }

        dao.delete(id);
    }

    public ExpenseCategory buscarPorId(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        return dao.findById(id).orElse(null);
    }

    public List<ExpenseCategory> listarTodas() {
        return dao.findAll();
    }

    public List<ExpenseCategory> listarAtivas() {
        return dao.findByStatus(1);
    }

    // ==========================================================
    // 🔹 MÉTODOS DE BUSCA E FILTRO
    // ==========================================================
    public List<ExpenseCategory> filtrar(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return listarTodas();
        }
        return dao.filter(texto.trim());
    }

    public ExpenseCategory buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return null;
        }
        return dao.findByName(nome.trim()).orElse(null);
    }

    // ==========================================================
    // 🔹 VALIDAÇÕES DE NEGÓCIO
    // ==========================================================
    public boolean validarCategoria(ExpenseCategory categoria) {
        if (categoria == null) {
            return false;
        }

        if (categoria.getName() == null || categoria.getName().trim().isEmpty()) {
            return false;
        }

        if (categoria.getName().length() > 100) {
            return false;
        }

        if (categoria.getDescription() != null && categoria.getDescription().length() > 500) {
            return false;
        }

        return true;
    }

    public boolean categoriaExiste(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }
        return dao.existsByName(nome.trim());
    }

    public boolean podeExcluir(Integer categoriaId) {
        if (categoriaId == null || categoriaId <= 0) {
            return false;
        }

        ExpenseDao expenseDao = new ExpenseDao();
        List<Expense> despesas = expenseDao.findByCategory(categoriaId);
        return despesas.isEmpty();
    }
}
