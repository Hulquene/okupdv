package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.ProductDao;
import com.okutonda.okudpdv.data.entities.Product;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller responsável pela lógica de alto nível dos produtos.
 *
 * Intermedia o acesso entre UI e ProductDao (CRUD, filtros, PDV, inventário).
 *
 * @author …
 */
public class ProductController {

    private final ProductDao dao;

    public ProductController() {
        this.dao = new ProductDao();
    }

    // ==========================================================
    // 🔹 CONSULTAS
    // ==========================================================
    public Product getById(int id) {
        return dao.findById(id);
    }

    public Product getByBarcode(String barcode) {
        return dao.findByBarcode(barcode);
    }

    public Product getByDescription(String description) {
        return dao.findByDescription(description);
    }

    public List<Product> listAll() {
        return dao.findAll();
    }

    public List<Product> list(String where) {
        return dao.listWithStock(where);
    }

    public List<Product> listProducts() {
        return dao.listWithStock(" WHERE p.type='product'");
    }

    public List<Product> listServices() {
        return dao.listWithStock(" WHERE p.type='service'");
    }

    public List<Product> listForInventory() {
        return dao.listForInventory();
    }

    public List<Product> listForPDV(String filtro) {
        return dao.listForPDV(filtro);
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public boolean save(Product product) {
        if (product == null) {
            System.err.println("[ProductController] Produto inválido.");
            return false;
        }
        if (product.getId() > 0) {
            return dao.update(product);
        }
        return dao.add(product);
    }

    public boolean delete(int id) {
        return dao.delete(id);
    }

    // ==========================================================
    // 🔹 UTILITÁRIOS
    // ==========================================================
    /**
     * Calcula total de produto × quantidade
     */
    public BigDecimal calculateTotal(Product prod, int qty) {
        if (prod == null || prod.getPrice() == null) {
            return BigDecimal.ZERO;
        }
        return prod.getPrice().multiply(BigDecimal.valueOf(qty));
    }

    /**
     * Obtém stock atual de um produto
     */
    public int getCurrentStock(int productId) {
        return dao.getCurrentStock(productId);
    }
}
