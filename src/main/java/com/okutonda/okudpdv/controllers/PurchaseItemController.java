package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.PurchaseItemDao;
import com.okutonda.okudpdv.data.entities.PurchaseItem;
import com.okutonda.okudpdv.data.entities.StockMovement;

import java.util.List;

/**
 * Controller responsável pelas operações de itens de compra.
 *
 * Faz a ponte entre UI e DAO, aplicando validações básicas.
 *
 * @author Hulquene
 */
public class PurchaseItemController {

    private final PurchaseItemDao dao;

    public PurchaseItemController() {
        this.dao = new PurchaseItemDao();
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public boolean add(PurchaseItem item, int purchaseId) {
        if (item == null || purchaseId <= 0) {
            return false;
        }
        return dao.add(item, purchaseId);
    }

    public boolean update(PurchaseItem item) {
        return dao.update(item);
    }

    public boolean delete(int id) {
        return dao.delete(id);
    }

    // ==========================================================
    // 🔹 Consultas
    // ==========================================================
    public List<PurchaseItem> listarTodos() {
        return dao.findAll();
    }

    public List<PurchaseItem> listarPorCompra(int purchaseId) {
        return dao.listByPurchase(purchaseId);
    }

    // ==========================================================
    // 🔹 Regras específicas
    // ==========================================================
    /**
     * Verifica se um movimento de stock (entrada) é válido em relação à compra
     * de origem.
     */
    public boolean podeDarEntrada(StockMovement movimento) {
        return dao.podeDarEntradaDeCompra(movimento);
    }
}
