package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "purchase_payments")
public class PurchasePayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    @Column(name = "valor_pago", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorPago;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_pagamento", nullable = false)
    private Date dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo", length = 50)
    private PaymentMode metodo;

    @Column(name = "referencia", length = 100)
    private String referencia;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Construtores
    public PurchasePayment() {
        this.dataPagamento = new Date();
    }

    public PurchasePayment(BigDecimal valorPago, PaymentMode metodo) {
        this();
        this.valorPago = valorPago;
        this.metodo = metodo;
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

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public PaymentMode getMetodo() {
        return metodo;
    }

    public void setMetodo(PaymentMode metodo) {
        this.metodo = metodo;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "PurchasePayment{id=" + id + ", valor=" + valorPago + ", data=" + dataPagamento + "}";
    }
}
