/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.dao.PurchasePaymentDao;
import com.okutonda.okudpdv.models.PurchasePayment;

import java.util.List;

/**
 *
 * @author rog
 */
public class PurchasePaymentController {

    private final PurchasePaymentDao dao;

    public PurchasePaymentController() {
        this.dao = new PurchasePaymentDao();
    }

    public boolean add(PurchasePayment p, int purchaseId) {
        return dao.add(p, purchaseId);
    }

    public List<PurchasePayment> getByPurchase(int purchaseId) {
        return dao.listByPurchase(purchaseId);
    }

    public List<PurchasePayment> listByPurchase(int purchaseId) {
        return dao.listByPurchase(purchaseId);
    }
}
