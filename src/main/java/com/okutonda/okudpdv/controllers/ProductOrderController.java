package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.ProductOrderDao;
import com.okutonda.okudpdv.data.entities.ProductOrder;
import java.util.List;

/**
 * Controller para os itens de pedidos (products_order).
 *
 * Agora usa o orderId interno do objeto ProductOrder.
 *
 * @author …
 */
public class ProductOrderController {

    private final ProductOrderDao dao;

    public ProductOrderController() {
        this.dao = new ProductOrderDao();
    }

    /**
     * Obtém um item pelo ID
     */
    public ProductOrder getById(int id) {
        return dao.findById(id);
    }

    /**
     * Lista todos os itens de um pedido
     */
    public List<ProductOrder> getByOrderId(int orderId) {
        return dao.listProductFromOrderId(orderId);
    }

    /**
     * Lista com cláusula WHERE
     */
    public List<ProductOrder> list(String where) {
        return dao.list(where);
    }

    /**
     * Adiciona novo item (usa orderId do próprio objeto)
     */
    public boolean add(ProductOrder item) {
        if (item == null || item.getOrderId() <= 0) {
            System.err.println("[Controller] ProductOrder inválido — orderId ausente.");
            return false;
        }
        return dao.add(item);
    }

    /**
     * Atualiza item existente
     */
    public boolean edit(ProductOrder item) {
        if (item == null || item.getId() <= 0) {
            System.err.println("[Controller] ID inválido para atualização de ProductOrder.");
            return false;
        }
        return dao.update(item);
    }

    /**
     * Remove item
     */
    public boolean delete(int id) {
        return dao.delete(id);
    }

    /**
     * Filtro por texto
     */
    public List<ProductOrder> filter(String text) {
        return dao.filter(text);
    }
}
