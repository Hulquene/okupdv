package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;
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
    private Double grantedAmount = 0.0;

    @Column(name = "incurred_amount", precision = 10, scale = 2)
    private Double incurredAmount = 0.0;

    @Column(name = "closing_amount", precision = 10, scale = 2)
    private Double closingAmount = 0.0;

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

    public Shift(User user, Double grantedAmount) {
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

    public Double getGrantedAmount() {
        return grantedAmount;
    }

    public void setGrantedAmount(Double grantedAmount) {
        this.grantedAmount = grantedAmount;
    }

    public Double getIncurredAmount() {
        return incurredAmount;
    }

    public void setIncurredAmount(Double incurredAmount) {
        this.incurredAmount = incurredAmount;
    }

    public Double getClosingAmount() {
        return closingAmount;
    }

    public void setClosingAmount(Double closingAmount) {
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
    public Double getCurrentBalance() {
        return grantedAmount + incurredAmount;
    }

    public Double getDifference() {
        if (closingAmount != null && getCurrentBalance() != null) {
            return closingAmount - getCurrentBalance();
        }
        return 0.0;
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
