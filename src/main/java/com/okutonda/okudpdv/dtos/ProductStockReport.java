/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.dtos;

/**
 *
 * @author hr
 */// Arquivo: ProductStockReport.java
import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.ProductStatus;
import com.okutonda.okudpdv.data.entities.ProductType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para relatório de stock de produtos Utilizado para transferência de dados
 * entre service e view
 */
public class ProductStockReport {

    private Integer productId;
    private String code;
    private String barcode;
    private String description;
    private Integer currentStock;
    private Integer minStock;
    private BigDecimal price;
    private BigDecimal purchasePrice;
    private String groupName;
    private ProductType productType; // ATUALIZADO: De String para ProductType
    private Integer totalEntradas;
    private Integer totalSaidas;
    private LocalDateTime ultimaMovimentacao;
    private String statusStock;
    private BigDecimal valorTotalStock;
    private ProductStatus status; // ATUALIZADO: De Integer para ProductStatus

    // Construtor a partir de Product + métricas
    public ProductStockReport(Product product, Integer stockAtual, Integer totalEntradas,
            Integer totalSaidas, LocalDateTime ultimaMovimentacao) {
        this.productId = product.getId();
        this.code = product.getCode();
        this.barcode = product.getBarcode();
        this.description = product.getDescription();
        this.currentStock = stockAtual;
        this.minStock = product.getMinStock();
        this.price = product.getPrice();
        this.purchasePrice = product.getPurchasePrice();
        this.groupName = product.getGroup() != null ? product.getGroup().getName() : "";
        this.productType = product.getType();
        this.totalEntradas = totalEntradas;
        this.totalSaidas = totalSaidas;
        this.ultimaMovimentacao = ultimaMovimentacao;
        this.statusStock = calcularStatusStock(stockAtual, product.getMinStock());
        this.valorTotalStock = calcularValorTotalStock(product.getPurchasePrice(), stockAtual);
        this.status = product.getStatus();
    }

    private String calcularStatusStock(Integer stockAtual, Integer stockMinimo) {
        if (stockMinimo == null) {
            stockMinimo = 0;
        }

        if (stockAtual <= 0) {
            return "SEM STOCK";
        }
        if (stockAtual <= stockMinimo) {
            return "STOCK BAIXO";
        }
        if (stockAtual <= stockMinimo * 2) {
            return "STOCK MÉDIO";
        }
        return "STOCK NORMAL";
    }

    private BigDecimal calcularValorTotalStock(BigDecimal precoCompra, Integer stockAtual) {
        if (precoCompra == null || stockAtual == null) {
            return BigDecimal.ZERO;
        }
        return precoCompra.multiply(BigDecimal.valueOf(stockAtual));
    }

    // Getters e Setters
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public Integer getMinStock() {
        return minStock;
    }

    public void setMinStock(Integer minStock) {
        this.minStock = minStock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Integer getTotalEntradas() {
        return totalEntradas;
    }

    public void setTotalEntradas(Integer totalEntradas) {
        this.totalEntradas = totalEntradas;
    }

    public Integer getTotalSaidas() {
        return totalSaidas;
    }

    public void setTotalSaidas(Integer totalSaidas) {
        this.totalSaidas = totalSaidas;
    }

    public LocalDateTime getUltimaMovimentacao() {
        return ultimaMovimentacao;
    }

    public void setUltimaMovimentacao(LocalDateTime ultimaMovimentacao) {
        this.ultimaMovimentacao = ultimaMovimentacao;
    }

    public String getStatusStock() {
        return statusStock;
    }

    public void setStatusStock(String statusStock) {
        this.statusStock = statusStock;
    }

    public BigDecimal getValorTotalStock() {
        return valorTotalStock;
    }

    public void setValorTotalStock(BigDecimal valorTotalStock) {
        this.valorTotalStock = valorTotalStock;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

}
