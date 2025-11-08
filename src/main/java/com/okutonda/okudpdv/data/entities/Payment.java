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

    @Column(name = "total", precision = 10, scale = 2, nullable = false)
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

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", length = 20)
    private DocumentType invoiceType = DocumentType.FT; // CORREÇÃO: Usando enum DocumentType

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "currency", length = 10, nullable = false)
    private String currency = "AOA";

    @Enumerated(EnumType.STRING)
    @Column(name = "mode", length = 20, nullable = false)
    private PaymentMode paymentMode = PaymentMode.NU;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private PaymentStatus status = PaymentStatus.PENDENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId")
    private Clients client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Construtores
    public Payment() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Payment(String description, BigDecimal total, PaymentMode paymentMode) {
        this();
        this.description = description;
        this.total = total != null ? total : BigDecimal.ZERO;
        this.paymentMode = paymentMode;
    }

    public Payment(String description, BigDecimal total, PaymentMode paymentMode, PaymentStatus status) {
        this(description, total, paymentMode);
        this.status = status != null ? status : PaymentStatus.PENDENTE;
    }

    public Payment(String description, BigDecimal total, PaymentMode paymentMode,
            PaymentStatus status, Clients client, User user) {
        this(description, total, paymentMode, status);
        this.client = client;
        this.user = user;
    }

    public Payment(String description, BigDecimal total, PaymentMode paymentMode,
            PaymentStatus status, Clients client, User user, DocumentType invoiceType) {
        this(description, total, paymentMode, status, client, user);
        this.invoiceType = invoiceType != null ? invoiceType : DocumentType.FT;
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
        this.total = total != null ? total : BigDecimal.ZERO;
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

    public DocumentType getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(DocumentType invoiceType) {
        this.invoiceType = invoiceType != null ? invoiceType : DocumentType.FT;
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
        if (currency != null && !currency.trim().isEmpty()) {
            this.currency = currency;
        }
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode != null ? paymentMode : PaymentMode.NU;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status != null ? status : PaymentStatus.PENDENTE;
        this.updatedAt = LocalDateTime.now();
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Métodos utilitários
    public boolean isPago() {
        return PaymentStatus.PAGO.equals(status);
    }

    public boolean isPendente() {
        return PaymentStatus.PENDENTE.equals(status);
    }

    public boolean isParcial() {
        return PaymentStatus.PARCIAL.equals(status);
    }

    public boolean isAtrasado() {
        return PaymentStatus.ATRASADO.equals(status);
    }

    public boolean isCancelado() {
        return PaymentStatus.CANCELADO.equals(status);
    }

    // Método para verificar se o pagamento está ativo
    public boolean isAtivo() {
        return !PaymentStatus.CANCELADO.equals(status);
    }

    // Método para verificar se é um documento fiscal
    public boolean isDocumentoFiscal() {
        return invoiceType != null
                && (invoiceType == DocumentType.FT
                || invoiceType == DocumentType.FR
                || invoiceType == DocumentType.NC
                || invoiceType == DocumentType.ND
                || invoiceType == DocumentType.RC);
    }

    // Método para obter descrição do status
    public String getStatusDescricao() {
        return status != null ? status.getDescricao() : "Desconhecido";
    }

    // Método para obter descrição do modo de pagamento
    public String getPaymentModeDescricao() {
        return paymentMode != null ? paymentMode.getDescricao() : "Desconhecido";
    }

    // Método para obter descrição do tipo de fatura
    public String getInvoiceTypeDescricao() {
        return invoiceType != null ? invoiceType.toString() : "Desconhecido";
    }

    // Método para marcar como pago
    public void marcarComoPago() {
        this.status = PaymentStatus.PAGO;
        this.updatedAt = LocalDateTime.now();
    }

    // Método para marcar como cancelado
    public void marcarComoCancelado() {
        this.status = PaymentStatus.CANCELADO;
        this.updatedAt = LocalDateTime.now();
    }

    // Método para validar dados básicos
    public boolean isValid() {
        return total != null
                && total.compareTo(BigDecimal.ZERO) >= 0
                && paymentMode != null
                && status != null
                && user != null
                && invoiceType != null;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Payment{"
                + "id=" + id
                + ", total=" + total
                + ", reference='" + reference + '\''
                + ", invoiceType=" + getInvoiceTypeDescricao()
                + ", status=" + getStatusDescricao()
                + ", paymentMode=" + getPaymentModeDescricao()
                + ", createdAt=" + createdAt
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        Payment payment = (Payment) o;
        return id != null && id.equals(payment.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
