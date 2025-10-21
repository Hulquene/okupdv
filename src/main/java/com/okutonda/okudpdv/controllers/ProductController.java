/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.ProductDao;
import com.okutonda.okudpdv.data.entities.Product;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author kenny
 */
public class ProductController {

    private final ProductDao dao;

    public ProductController() {
        this.dao = new ProductDao();
    }

    /**
     * Buscar por ID
     */
    public Product getId(int id) {
        return dao.getId(id);
    }

    /**
     * Buscar por código de barras
     */
    public Product getFromBarCode(String barcode) {
        return dao.searchFromBarCode(barcode);
    }

    /**
     * Buscar por nome
     */
    public Product getName(String description) {
        return dao.searchFromName(description);
    }

    /**
     * Lista genérica (com ou sem WHERE)
     */
    public List<Product> get(String where) {
        return dao.listWithStock(where == null ? "" : where);
    }

    /**
     * Apenas produtos (sem serviços)
     */
    public List<Product> getProducts() {
        return dao.listWithStock(" WHERE p.type ='product'");
    }

    /**
     * Apenas serviços
     */
    public List<Product> getServices() {
        return dao.listWithStock(" WHERE p.type ='service'");
    }

    /**
     * Inventário (para o menu de stock)
     */
    public List<Product> getForInventory() {
        return dao.listForInventory();
    }

    /**
     * Produtos para PDV (somente ativos, com stock disponível)
     */
    public List<Product> getForPDV(String filtro) {
        return dao.listForPDV(filtro);
    }

    /**
     * Filtro geral
     */
//    public List<Product> filter(String txt, String and) {
//        and = (and == null || and.isEmpty()) ? "" : and;
//        return dao.filter(txt, and);
//    }
    /**
     * Adicionar ou editar
     */
    public Boolean add(Product prod, int id) {
        if (id == 0) {
            return dao.add(prod);
        } else {
            return dao.edit(prod, id);
        }
    }

    /**
     * Apagar
     */
    public Boolean deleteId(int id) {
        return dao.delete(id);
    }

    /**
     * Calcular total de um produto × quantidade
     */
    public BigDecimal calculateTotalProduct(Product prod, int qtd) {
        if (prod == null || prod.getPrice() == null) {
            return BigDecimal.ZERO;
        }
        return prod.getPrice().multiply(BigDecimal.valueOf(qtd));
    }

    // Futuro: podes implementar descontos, impostos ou trocos
    public Double CalculateTotalChangeProduct(Product prod) {
        return null;
    }

    public Double CalculateTotalValueTaxeProduct(Product prod) {
        return null;
    }
}
