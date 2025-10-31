package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "shift")
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", unique = true, length = 50)
    private String code;

    @Column(name = "hash", length = 255)
    private String hash;

    @Column(name = "granted_amount", precision = 10, scale = 2)
    private BigDecimal grantedAmount = BigDecimal.ZERO; // Valor padrão

    @Column(name = "incurred_amount", precision = 10, scale = 2)
    private BigDecimal incurredAmount = BigDecimal.ZERO; // Valor padrão

    @Column(name = "closing_amount", precision = 10, scale = 2)
    private BigDecimal closingAmount = BigDecimal.ZERO; // Valor padrão

    @Column(name = "status", length = 20)
    private String status = "open"; // open, closed, cancelled

    @Column(name = "dateOpen", length = 20)
    private String dateOpen;

    @Column(name = "dateClose", length = 20)
    private String dateClose;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Construtores
    public Shift() {
        this.createdAt = LocalDateTime.now();
    }

    public Shift(User user, BigDecimal grantedAmount) {
        this();
        this.user = user;
        this.grantedAmount = grantedAmount;
        this.status = "open";
        this.dateOpen = java.time.LocalDateTime.now().toString();
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public BigDecimal getGrantedAmount() {
        return grantedAmount;
    }

    public void setGrantedAmount(BigDecimal grantedAmount) {
        this.grantedAmount = grantedAmount;
    }

    public BigDecimal getIncurredAmount() {
        return incurredAmount;
    }

    public void setIncurredAmount(BigDecimal incurredAmount) {
        this.incurredAmount = incurredAmount;
    }

    public BigDecimal getClosingAmount() {
        return closingAmount;
    }

    public void setClosingAmount(BigDecimal closingAmount) {
        this.closingAmount = closingAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateOpen() {
        return dateOpen;
    }

    public void setDateOpen(String dateOpen) {
        this.dateOpen = dateOpen;
    }

    public String getDateClose() {
        return dateClose;
    }

    public void setDateClose(String dateClose) {
        this.dateClose = dateClose;
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
    public BigDecimal getCurrentBalance() {
//        return grantedAmount + incurredAmount;
        BigDecimal total = grantedAmount.add(incurredAmount);
        return total;
    }

    public BigDecimal getDifference() {
        if (closingAmount != null && getCurrentBalance() != null) {
//            return closingAmount - getCurrentBalance();
            BigDecimal total = closingAmount.subtract(getCurrentBalance());
            return total;
        }
        return BigDecimal.ZERO;
    }

    public boolean isOpen() {
        return "open".equals(status);
    }

    public boolean isClosed() {
        return "closed".equals(status);
    }

    @Override
    public String toString() {
        return "Shift{id=" + id + ", code='" + code + "', status='" + status + "', user="
                + (user != null ? user.getName() : "null") + "}";
    }
}
