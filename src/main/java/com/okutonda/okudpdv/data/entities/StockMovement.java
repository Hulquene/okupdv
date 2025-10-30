package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movements")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "type", length = 20)
    private String type; // ENTRADA, SAIDA, AJUSTE

    @Column(name = "origin", length = 50)
    private String origin = "MANUAL";

    @Column(name = "reference_id")
    private Integer referenceId;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "reason", length = 100)
    private String reason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Construtores
    public StockMovement() {
        this.createdAt = LocalDateTime.now();
    }

    public StockMovement(Product product, User user, Integer quantity, String type) {
        this();
        this.product = product;
        this.user = user;
        this.quantity = quantity;
        this.type = type;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "StockMovement{id=" + id + ", product=" + (product != null ? product.getDescription() : "null")
                + ", quantity=" + quantity + ", type='" + type + "'}";
    }
}
