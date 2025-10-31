package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "purchase_items")
public class PurchaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "preco_custo", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoCusto = BigDecimal.ZERO; // Valor padrão

    @Column(name = "iva", precision = 10, scale = 2)
    private BigDecimal iva = BigDecimal.ZERO;

    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO; // Valor padrão

    @Column(name = "quantidade_entrada")
    private Integer quantidadeEntrada = 0;

    @Column(name = "entrada_status", length = 20)
    private String entradaStatus = "NAO_INICIADO";

    // Construtores
    public PurchaseItem() {
    }

    public PurchaseItem(Product product, Integer quantidade, BigDecimal precoCusto) {
        this.product = product;
        this.quantidade = quantidade;
        this.precoCusto = precoCusto;
        calcularSubtotal();
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public Integer getPurchaseId() {
        return purchase != null ? purchase.getId() : null;
    }

    public void setPurchaseId(Integer purchaseId) {
        // Método auxiliar para DAO
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Integer getQuantidadeEntrada() {
        return quantidadeEntrada;
    }

    public void setQuantidadeEntrada(Integer quantidadeEntrada) {
        this.quantidadeEntrada = quantidadeEntrada;
    }

    public String getEntradaStatus() {
        return entradaStatus;
    }

    public void setEntradaStatus(String entradaStatus) {
        this.entradaStatus = entradaStatus;
    }

    // Métodos utilitários
    public void calcularSubtotal() {
        if (precoCusto != null && quantidade != null) {
            this.subtotal = precoCusto.multiply(BigDecimal.valueOf(quantidade));
        }
    }

    public void calcularIVA(BigDecimal taxaIVA) {
        if (subtotal != null && taxaIVA != null) {
            this.iva = subtotal.multiply(taxaIVA).divide(BigDecimal.valueOf(100));
        }
    }

    @Override
    public String toString() {
        return "PurchaseItem{id=" + id + ", product=" + (product != null ? product.getDescription() : "null")
                + ", quantidade=" + quantidade + "}";
    }
}
