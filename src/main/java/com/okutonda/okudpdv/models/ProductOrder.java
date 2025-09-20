/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.models;

import java.math.BigDecimal;

/**
 *
 * @author kenny
 */
public class ProductOrder {

    private int id;
    private int orderId;
    private Product product;
    private String date;
    private String description;
    private int qty;
    private BigDecimal price;
    private String unit;
    private String code;
//    private String group_name;
//    private String group_code;
    private String taxeCode;
    private String taxeName;
    private BigDecimal taxePercentage;
    private String reasonTax;
    private String reasonCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTaxeCode() {
        return taxeCode;
    }

    public void setTaxeCode(String taxeCode) {
        this.taxeCode = taxeCode;
    }

    public String getTaxeName() {
        return taxeName;
    }

    public void setTaxeName(String taxeCame) {
        this.taxeName = taxeCame;
    }

    public BigDecimal getTaxePercentage() {
        return taxePercentage;
    }

    public void setTaxePercentage(BigDecimal taxePercentage) {
        this.taxePercentage = taxePercentage;
    }

    public String getReasonTax() {
        return reasonTax;
    }

    public void setReasonTax(String reasonTax) {
        this.reasonTax = reasonTax;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    @Override
    public String toString() {
        return "ProductOrder{" + "id=" + id + ", orderId=" + orderId + ", product=" + product + ", date=" + date + ", description=" + description + ", qty=" + qty + ", price=" + price + ", unit=" + unit + ", code=" + code + ", taxeCode=" + taxeCode + ", taxeName=" + taxeName + ", taxePercentage=" + taxePercentage + ", reasonTax=" + reasonTax + ", reasonCode=" + reasonCode + '}';
    }
    
}
