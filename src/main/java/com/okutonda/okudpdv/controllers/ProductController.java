/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.dao.ProductDao;
import com.okutonda.okudpdv.models.Product;
import java.util.List;

/**
 *
 * @author kenny
 */
public class ProductController {

    ProductDao dao;
//    ProductOrderDao pOrderDao;

    public ProductController() {
        this.dao = new ProductDao();
//        this.pOrderDao = new ProductOrderDao();
    }

    public Product getId(int id) {
        return dao.getId(id);
    }

    public Product getFromBarCode(String barcode) {
        return dao.searchFromBarCode(barcode);
    }

    public Product getName(String description) {
        return dao.searchFromName(description);
    }

    public List<Product> get(String where) {
        return dao.list(where);
    }

    public List<Product> getProducts() {
        return dao.list(" WHERE type ='product'");
    }

    public List<Product> getProductsStock() {
        return dao.listHistoryStock(" WHERE type ='product'");
    }
    public List<Product> filterProductStock(String txt) {
        return dao.filterProduct(txt);
    }

    public List<Product> filterProduct(String txt) {
        return dao.filterProduct(txt);
    }

    public List<Product> getServices() {
        return dao.list(" WHERE type ='service'");
    }

    public List<Product> filterService(String txt) {
        return dao.filterProduct(txt);
    }

    public List<Product> filter(String txt) {
        return dao.filter(txt);
    }

    public Boolean add(Product prod, int id) {
        boolean status;
        if (id == 0) {
            status = dao.add(prod);
        } else {
            status = dao.edit(prod, id);
        }
        return status;
    }

    public boolean updateStock(int prodId, int stock) {
        return dao.updateStock(prodId, stock);
    }

    public Boolean deleteId(int id) {
        return dao.delete(id);
    }

    public Double CalculateTotalProduct(Product prod, int qtd) {
        return prod.getPrice() * qtd;
    }

    public Double CalculateTotalChangeProduct(Product prod) {
        return null;
    }

    public Double CalculateTotalValueTaxeProduct(Product prod) {
        return null;
    }
}
