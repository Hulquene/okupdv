/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.models;

import java.util.Date;

/**
 *
 * @author hr
 */
public class StockMovement {
    private int id;
    private Product product;
    private int quantity; // positivo = entrada, negativo = saída
    private String type;  // IN, OUT, AJUSTE, TRANSFERENCIA
    private String reason; // motivo (VENDA, COMPRA, DEVOLUCAO, AJUSTE MANUAL, etc.)
    private User user;
    private Date createdAt;

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "StockMovement{" +
                "id=" + id +
                ", product=" + (product != null ? product.getDescription() : "null") +
                ", quantity=" + quantity +
                ", type='" + type + '\'' +
                ", reason='" + reason + '\'' +
                ", user=" + (user != null ? user.getName() : "null") +
                ", createdAt=" + createdAt +
                '}';
    }
}
