package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "prefix", length = 10)
    private String prefix;

    @Column(name = "number")
    private Integer number;

    @Column(name = "date", length = 20)
    private String date;

    @Column(name = "dateFinish", length = 20)
    private String dateFinish;

    @Column(name = "order_id")
    private Integer invoiceId;

    @Column(name = "order_type", length = 20)
    private String invoiceType = "ORDER";

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "currency", length = 10)
    private String currency = "AOA";

    @Enumerated(EnumType.STRING)
    @Column(name = "mode", length = 20)
    private PaymentMode paymentMode = PaymentMode.NU;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private PaymentStatus status = PaymentStatus.PENDENTE; // CORREÇÃO: Usando PaymentStatus externo

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId")
    private Clients client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Construtores
    public Payment() {
        this.createdAt = LocalDateTime.now();
    }

    public Payment(String description, BigDecimal total, PaymentMode paymentMode) {
        this();
        this.description = description;
        this.total = total;
        this.paymentMode = paymentMode;
    }

    public Payment(String description, BigDecimal total, PaymentMode paymentMode, PaymentStatus status) {
        this(description, total, paymentMode);
        this.status = status;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(String dateFinish) {
        this.dateFinish = dateFinish;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public Clients getClient() {
        return client;
    }

    public void setClient(Clients client) {
        this.client = client;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Métodos utilitários
    public boolean isPago() {
        return status == PaymentStatus.PAGO;
    }

    public boolean isPendente() {
        return status == PaymentStatus.PENDENTE;
    }

    public boolean isParcial() {
        return status == PaymentStatus.PARCIAL;
    }

    public boolean isAtrasado() {
        return status == PaymentStatus.ATRASADO;
    }

    public boolean isCancelado() {
        return status == PaymentStatus.CANCELADO;
    }

    // Método para obter descrição do status
    public String getStatusDescricao() {
        return status != null ? status.getDescricao() : "Desconhecido";
    }

    // Método para obter descrição do modo de pagamento
    public String getPaymentModeDescricao() {
        return paymentMode != null ? paymentMode.getDescricao() : "Desconhecido";
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", total=" + total +
                ", reference='" + reference + '\'' +
                ", status=" + getStatusDescricao() +
                ", paymentMode=" + getPaymentModeDescricao() +
                '}';
    }
}