/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.dao.PurchaseDao;
import com.okutonda.okudpdv.models.Purchase;
import java.util.List;

/**
 *
 * @author kenny
 */
public class PurchaseController {

    PurchaseDao dao;

    public PurchaseController() {
        this.dao = new PurchaseDao();
    }

    public Purchase getId(int id) {
        return dao.getId(id);
    }

//    public Purchase getFromBarCode(String barcode) {
//        return dao.searchFromBarCode(barcode);
//    }
    public Purchase getName(String description) {
        return dao.getFromDescription(description);
    }

    public List<Purchase> get(String where) {
        return dao.list(where);
    }

    public List<Purchase> getPurchase() {
        return dao.list("");
    }

//    public List<Purchase> getProductsStock() {
//        return dao.listHistoryStock(" WHERE type ='product'");
//    }
////    public List<Purchase> filterProductStock(String txt) {
////        return dao.filterProduct(txt);
////    }
//    public List<Purchase> filterProduct(String txt) {
//        return dao.filterProduct(txt);
//    }
//    public List<Purchase> getServices() {
//        return dao.list(" WHERE type ='service'");
//    }
//    public List<Product> filterService(String txt) {
//        return dao.filterProduct(txt);
//    }
    public List<Purchase> filter(String txt) {
        return dao.filter(txt);
    }

    public Boolean add(Purchase pur) {
        return dao.add(pur);
    }

//    public boolean updateStock(int prodId, int stock) {
//        return dao.updateStock(prodId, stock);
//    }
    public Boolean deleteId(int id) {
        return dao.delete(id);
    }

//    public Double CalculateTotalProduct(Purchase prod, int qtd) {
//        return prod.getPrice() * qtd;
//    }
//
//    public Double CalculateTotalChangeProduct(Purchase prod) {
//        return null;
//    }
//
//    public Double CalculateTotalValueTaxeProduct(Purchase prod) {
//        return null;
//    }
}
