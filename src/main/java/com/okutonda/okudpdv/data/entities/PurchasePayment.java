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
    private BigDecimal valorPago = BigDecimal.ZERO;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_pagamento", nullable = false)
    private Date dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo", length = 50)
    private PaymentMode metodo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private PaymentStatus status = PaymentStatus.PAGO;

    @Column(name = "referencia", length = 100)
    private String referencia;

    @Column(name = "descricao", length = 255)
    private String descricao;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    // Construtores
    public PurchasePayment() {
        this.dataPagamento = new Date();
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public PurchasePayment(BigDecimal valorPago, PaymentMode metodo) {
        this();
        this.valorPago = valorPago;
        this.metodo = metodo;
    }

    public PurchasePayment(BigDecimal valorPago, PaymentMode metodo, PaymentStatus status) {
        this();
        this.valorPago = valorPago;
        this.metodo = metodo;
        this.status = status;
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
        // Método auxiliar para DAO - será usado pelo controller
        if (purchaseId != null) {
            Purchase purchaseRef = new Purchase();
            purchaseRef.setId(purchaseId);
            this.purchase = purchaseRef;
        }
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago != null ? valorPago : BigDecimal.ZERO;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento != null ? dataPagamento : new Date();
    }

    public PaymentMode getMetodo() {
        return metodo;
    }

    public void setMetodo(PaymentMode metodo) {
        this.metodo = metodo;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status != null ? status : PaymentStatus.PAGO;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Métodos utilitários
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Date();
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = new Date();
        }
        if (this.updatedAt == null) {
            this.updatedAt = new Date();
        }
        if (this.dataPagamento == null) {
            this.dataPagamento = new Date();
        }
        if (this.valorPago == null) {
            this.valorPago = BigDecimal.ZERO;
        }
        if (this.status == null) {
            this.status = PaymentStatus.PAGO;
        }
    }

    // Métodos de negócio
    public boolean isPago() {
        return PaymentStatus.PAGO.equals(this.status);
    }

    public boolean isPendente() {
        return PaymentStatus.PENDENTE.equals(this.status);
    }

    public boolean isCancelado() {
        return PaymentStatus.CANCELADO.equals(this.status);
    }

    public boolean isParcial() {
        return PaymentStatus.PARCIAL.equals(this.status);
    }

    public String getStatusDescricao() {
        return status != null ? status.getDescricao() : "";
    }

    public String getMetodoDescricao() {
        return metodo != null ? metodo.getDescricao() : "";
    }

    // Validações
    public boolean isValid() {
        if (valorPago == null || valorPago.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        if (dataPagamento == null) {
            return false;
        }
        if (metodo == null) {
            return false;
        }
        if (status == null) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format(
                "PurchasePayment{id=%d, valor=%.2f, metodo=%s, status=%s, data=%s}",
                id != null ? id : 0,
                valorPago != null ? valorPago.doubleValue() : 0.0,
                metodo != null ? metodo.getCodigo() : "N/A",
                status != null ? status.getDescricao() : "N/A",
                dataPagamento != null ? dataPagamento.toString() : "N/A"
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchasePayment)) {
            return false;
        }
        PurchasePayment that = (PurchasePayment) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
