/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.models;

import java.util.List;

/**
 *
 * @author kenny
 */
public class Order {

    private int id;
    private int status;
    private int number;
    private String prefix;
    private Clients client;
    private List<ProductOrder> products;
    private User seller;
    private Double total;
    private Double subTotal;
    private Double payTotal;
    private Double amountReturned;
    private String datecreate;
    private String duedate;  
    private int year;
    private String hash;
    private String note;
    private Double totalTaxe;
    private String key;

    public List<ProductOrder> getProducts() {
        return products;
    }

    public void setProducts(List<ProductOrder> products) {
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Clients getClient() {
        return client;
    }

    public void setClient(Clients client) {
        this.client = client;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getDatecreate() {
        return datecreate;
    }

    public void setDatecreate(String datecreate) {
        this.datecreate = datecreate;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getPayTotal() {
        return payTotal;
    }

    public void setPayTotal(Double payTotal) {
        this.payTotal = payTotal;
    }

    public Double getAmountReturned() {
        return amountReturned;
    }

    public void setAmountReturned(Double amountReturned) {
        this.amountReturned = amountReturned;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Double getTotalTaxe() {
        return totalTaxe;
    }

    public void setTotalTaxe(Double totalTaxe) {
        this.totalTaxe = totalTaxe;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", status=" + status + ", number=" + number + ", prefix=" + prefix + ", client=" + client + ", products=" + products + ", seller=" + seller + ", total=" + total + ", subTotal=" + subTotal + ", payTotal=" + payTotal + ", amountReturned=" + amountReturned + ", datecreate=" + datecreate + ", year=" + year + ", hash=" + hash + ", note=" + note + ", totalTaxe=" + totalTaxe + ", key=" + key + '}';
    }

}
