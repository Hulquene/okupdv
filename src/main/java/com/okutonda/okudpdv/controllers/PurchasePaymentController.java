package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.PurchasePaymentDao;
import com.okutonda.okudpdv.data.entities.PurchasePayment;

import java.util.List;

/**
 * Controller responsável pela lógica de pagamentos de compras.
 *
 * Intermedia operações entre a interface (UI) e o DAO. Aplica validações
 * simples e mantém consistência com o modelo de negócios.
 *
 * @author Hulquene
 */
public class PurchasePaymentController {

    private final PurchasePaymentDao dao;

    public PurchasePaymentController() {
        this.dao = new PurchasePaymentDao();
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public boolean add(PurchasePayment p, int purchaseId) {
        if (p == null || purchaseId <= 0) {
            System.err.println("[Controller] Pagamento inválido.");
            return false;
        }
        return dao.add(p, purchaseId);
    }

    public boolean edit(PurchasePayment p) {
        if (p == null || p.getId() <= 0) {
            return false;
        }
        return dao.update(p);
    }

    public boolean delete(int id) {
        return dao.delete(id);
    }

    // ==========================================================
    // 🔹 Consultas
    // ==========================================================
    public List<PurchasePayment> listarPorCompra(int purchaseId) {
        return dao.listByPurchase(purchaseId);
    }

    public PurchasePayment getById(int id) {
        return dao.findById(id);
    }

    public List<PurchasePayment> listarTodos() {
        return dao.findAll();
    }
}
