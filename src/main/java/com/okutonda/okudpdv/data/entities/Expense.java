package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO; // Valor padrão

    @Column(name = "prefix", length = 10)
    private String prefix;

    @Column(name = "number")
    private Integer number;

    @Column(name = "date", length = 20)
    private String date;

    @Column(name = "dateFinish", length = 20)
    private String dateFinish;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode", length = 50)
    private PaymentMode mode = PaymentMode.NU; // Padrão: Numerário

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "currency", length = 10)
    private String currency = "AOA";

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "status")
    private Integer status = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ExpenseCategory category;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Construtores
    public Expense() {
        this.createdAt = LocalDateTime.now();
    }

    public Expense(String description, BigDecimal total, String date) {
        this();
        this.description = description;
        this.total = total;
        this.date = date;
    }

    public Expense(String description, BigDecimal total, String date, PaymentMode mode) {
        this(description, total, date);
        this.mode = mode;
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

    public PaymentMode getMode() {
        return mode;
    }

    public void setMode(PaymentMode mode) {
        this.mode = mode;
    }

    // Método de conveniência para compatibilidade com o FinanceDao
    public PaymentMode getPaymentMode() {
        return this.mode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.mode = paymentMode;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Métodos utilitários
    public boolean isActive() {
        return status != null && status == 1;
    }

    public void activate() {
        this.status = 1;
    }

    public void deactivate() {
        this.status = 0;
    }

    @Override
    public String toString() {
        return "Expense{id=" + id + ", description='" + description + "', total=" + total + ", mode=" + mode + "}";
    }

    /**
     * Método para converter string do banco para PaymentMode
     */
    public static PaymentMode parsePaymentMode(String modeString) {
        if (modeString == null || modeString.trim().isEmpty()) {
            return PaymentMode.NU;
        }
        return PaymentMode.fromCodigo(modeString);
    }
}
