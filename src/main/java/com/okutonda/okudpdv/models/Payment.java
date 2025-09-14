/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.models;

/**
 *
 * @author kenny
 */
public class Payment {

    private int id;
    private String status;
    private String description;
    private Double Total;
    private String date;
    private String dateFinish;
    private Clients client;
    private int invoiceId;
    private String invoiceType;
    private String prefix;
    private int number;
    private PaymentModes paymentMode;
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double Total) {
        this.Total = Total;
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

    public Clients getClient() {
        return client;
    }

    public void setClient(Clients client) {
        this.client = client;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public PaymentModes getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentModes paymentMode) {
        this.paymentMode = paymentMode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Payment{" + "id=" + id + ", status=" + status + ", description=" + description + ", Total=" + Total + ", date=" + date + ", dateFinish=" + dateFinish + ", client=" + client + ", invoiceId=" + invoiceId + ", invoiceType=" + invoiceType + ", prefix=" + prefix + ", number=" + number + ", paymentMode=" + paymentMode + ", user=" + user + '}';
    }

}
