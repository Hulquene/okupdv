package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.ProductOrderDao;
import com.okutonda.okudpdv.data.entities.ProductOrder;
import java.util.List;
import java.util.Optional;

/**
 * Controller para os itens de pedidos (products_order) com Hibernate.
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
    public ProductOrder getById(Integer id) {
        Optional<ProductOrder> itemOpt = dao.findById(id);
        return itemOpt.orElse(null);
    }

    /**
     * Lista todos os itens de um pedido
     */
    public List<ProductOrder> getByOrderId(Integer orderId) {
        try {
            return dao.findByOrderId(orderId);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar itens do pedido: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Adiciona novo item (usa orderId do próprio objeto)
     */
    public ProductOrder add(ProductOrder item) {
        if (item == null || item.getOrderId() == null || item.getOrderId() <= 0) {
            System.err.println("❌ ProductOrder inválido — orderId ausente.");
            return null;
        }

        try {
            ProductOrder savedItem = dao.save(item);
            System.out.println("✅ Item adicionado ao pedido: " + savedItem.getOrderId());
            return savedItem;
        } catch (Exception e) {
            System.err.println("❌ Erro ao adicionar item: " + e.getMessage());
            return null;
        }
    }

    /**
     * Atualiza item existente
     */
    public ProductOrder edit(ProductOrder item) {
        if (item == null || item.getId() == null || item.getId() <= 0) {
            System.err.println("❌ ID inválido para atualização de ProductOrder.");
            return null;
        }

        try {
            ProductOrder updatedItem = dao.update(item);
            System.out.println("✅ Item atualizado: " + updatedItem.getId());
            return updatedItem;
        } catch (Exception e) {
            System.err.println("❌ Erro ao atualizar item: " + e.getMessage());
            return null;
        }
    }

    /**
     * Remove item
     */
    public boolean delete(Integer id) {
        try {
            dao.delete(id);
            System.out.println("✅ Item removido ID: " + id);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erro ao remover item: " + e.getMessage());
            return false;
        }
    }

    /**
     * Remove todos os itens de um pedido
     */
    public boolean deleteByOrderId(Integer orderId) {
        try {
            boolean success = dao.deleteByOrderId(orderId);
            if (success) {
                System.out.println("✅ Todos os itens removidos do pedido: " + orderId);
            }
            return success;
        } catch (Exception e) {
            System.err.println("❌ Erro ao remover itens do pedido: " + e.getMessage());
            return false;
        }
    }

    /**
     * Filtro por texto
     */
    public List<ProductOrder> filter(String text) {
        try {
            return dao.filter(text);
        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar itens: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Salva múltiplos itens em lote
     */
    public boolean saveBatch(List<ProductOrder> items) {
        if (items == null || items.isEmpty()) {
            System.err.println("❌ Lista de itens vazia");
            return false;
        }

        try {
            dao.saveBatch(items);
            System.out.println("✅ " + items.size() + " itens salvos em lote");
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar itens em lote: " + e.getMessage());
            return false;
        }
    }

    /**
     * Calcula o total de um pedido
     */
    public Double calculateOrderTotal(Integer orderId) {
        try {
            return dao.calculateOrderTotal(orderId);
        } catch (Exception e) {
            System.err.println("❌ Erro ao calcular total do pedido: " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * Lista todos os itens
     */
    public List<ProductOrder> getAll() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar itens: " + e.getMessage());
            return List.of();
        }
    }
}
