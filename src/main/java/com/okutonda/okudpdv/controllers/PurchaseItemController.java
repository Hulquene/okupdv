/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.dao.PurchaseItemDao;
import com.okutonda.okudpdv.models.PurchaseItem;

import java.util.List;

/**
 *
 * @author rog
 */
public class PurchaseItemController {

    private final PurchaseItemDao dao;

    public PurchaseItemController() {
        this.dao = new PurchaseItemDao();
    }

    public boolean add(PurchaseItem item, int purchaseId) {
        return dao.add(item, purchaseId);
    }

    public List<PurchaseItem> getByPurchase(int purchaseId) {
        return dao.listByPurchase(purchaseId);
    }
}
