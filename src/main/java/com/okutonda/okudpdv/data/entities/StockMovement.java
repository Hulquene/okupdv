/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.entities;

import java.util.Date;

/**
 *
 * @author hr
 */
public class StockMovement {

    private int id;
    private Product product;
    private Warehouse warehouse;  // novo campo
    private User user;
    private int quantity;
    private String type;       // ENTRADA, SAIDA, AJUSTE, TRANSFERENCIA
    private String origin;     // COMPRA, VENDA, DEVOLUCAO, MANUAL...
    private Integer referenceId;
    private String notes;
    private String reason;
    private Date createdAt;
    private Date updatedAt;

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
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

    @Override
    public String toString() {
        return "StockMovement{"
                + "id=" + id
                + ", product=" + (product != null ? product.getDescription() : "null")
                + ", warehouse=" + (warehouse != null ? warehouse.getName() : "null")
                + ", quantity=" + quantity
                + ", type='" + type + '\''
                + ", origin='" + origin + '\''
                + ", reason='" + reason + '\''
                + ", notes='" + notes + '\''
                + ", referenceId=" + referenceId
                + ", user=" + (user != null ? user.getName() : "null")
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + '}';
    }
}
