package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.PurchaseItemDao;
import com.okutonda.okudpdv.data.entities.PurchaseItem;
import com.okutonda.okudpdv.data.entities.StockStatus;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller para gestão de itens de compra
 */
public class PurchaseItemController {

    private final PurchaseItemDao dao;

    public PurchaseItemController() {
        this.dao = new PurchaseItemDao();
    }

    // ==========================================================
    // 🔹 CRUD OPERATIONS
    // ==========================================================
    public PurchaseItem adicionarItem(PurchaseItem item, Integer purchaseId) {
        if (!validarItem(item)) {
            throw new IllegalArgumentException("Dados do item inválidos");
        }

        item.setPurchaseId(purchaseId);
        calcularSubtotal(item);

        return dao.save(item);
    }

    public PurchaseItem atualizarItem(PurchaseItem item) {
        if (item == null || item.getId() == null) {
            throw new IllegalArgumentException("Item inválido para atualização");
        }

        if (!validarItem(item)) {
            throw new IllegalArgumentException("Dados do item inválidos");
        }

        calcularSubtotal(item);
        return dao.update(item);
    }

    public void excluirItem(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID do item inválido");
        }

        dao.delete(id);
    }

    public PurchaseItem buscarPorId(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        return dao.findById(id).orElse(null);
    }

    // ==========================================================
    // 🔹 MÉTODOS DE BUSCA
    // ==========================================================
    public List<PurchaseItem> listarPorCompra(Integer purchaseId) {
        if (purchaseId == null || purchaseId <= 0) {
            throw new IllegalArgumentException("ID da compra inválido");
        }
        return dao.findByPurchase(purchaseId);
    }

    public List<PurchaseItem> listarPorProduto(Integer productId) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("ID do produto inválido");
        }
        return dao.findByProduct(productId);
    }

    public List<PurchaseItem> listarPendentesEntrada() {
        return dao.findPendingStockEntry();
    }

    // ==========================================================
    // 🔹 MÉTODOS DE CÁLCULO
    // ==========================================================
    public void calcularSubtotal(PurchaseItem item) {
        if (item != null && item.getPrecoCusto() != null && item.getQuantidade() != null) {
            BigDecimal subtotal = item.getPrecoCusto().multiply(BigDecimal.valueOf(item.getQuantidade()));
            item.setSubtotal(subtotal);
        }
    }

    public void calcularIVA(PurchaseItem item, BigDecimal taxaIVA) {
        if (item != null && item.getSubtotal() != null && taxaIVA != null) {
            BigDecimal iva = item.getSubtotal().multiply(taxaIVA).divide(BigDecimal.valueOf(100));
            item.setIva(iva);
        }
    }

    // ==========================================================
    // 🔹 VALIDAÇÕES DE NEGÓCIO
    // ==========================================================
    public boolean validarItem(PurchaseItem item) {
        if (item == null) {
            return false;
        }

        // Produto obrigatório
        if (item.getProduct() == null || item.getProduct().getId() == null) {
            return false;
        }

        // Quantidade obrigatória e positiva
        if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
            return false;
        }

        // Preço de custo obrigatório e positivo
        if (item.getPrecoCusto() == null || item.getPrecoCusto().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        return true;
    }

    public boolean podeDarEntradaStock(PurchaseItem item, Integer quantidade) {
        if (item == null || quantidade == null || quantidade <= 0) {
            return false;
        }

        Integer quantidadeFaltante = item.getQuantidade() - item.getQuantidadeEntrada();
        return quantidade <= quantidadeFaltante;
    }

    public void atualizarEntradaStock(PurchaseItem item, Integer quantidadeEntrada) {
        if (item == null || quantidadeEntrada == null || quantidadeEntrada < 0) {
            throw new IllegalArgumentException("Parâmetros inválidos para atualização de entrada");
        }

        if (quantidadeEntrada > item.getQuantidade()) {
            throw new IllegalArgumentException("Quantidade de entrada não pode ser maior que a quantidade comprada");
        }

        item.setQuantidadeEntrada(quantidadeEntrada);

        // Atualiza status baseado na quantidade entrada
        if (quantidadeEntrada == 0) {
            item.setEntradaStatus(StockStatus.PENDENTE);
        } else if (quantidadeEntrada < item.getQuantidade()) {
            item.setEntradaStatus(StockStatus.PARCIAL);
        } else {
            item.setEntradaStatus(StockStatus.PROCESSADO);
        }

        dao.update(item);
    }
}
