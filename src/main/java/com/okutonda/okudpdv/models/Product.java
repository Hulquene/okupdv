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
public class Product {

    private int id;
    private String type;
    private String code;
    private String description;
    private String longDescription;
    private BigDecimal price;
    private BigDecimal purchasePrice;
    private Taxes taxe;
    private ReasonTaxes reasonTaxe;
    private GroupsProduct group;
    private String barcode;
    private Supplier supplier;
    private int minStock;   // novo: mínimo em vez de stock_total
    private int status;

    // 🔹 campo somente leitura, calculado em runtime (não persistido)
    private int currentStock;

    // getters/setters...
    public int getMinStock() {
        return minStock;
    }

    public void setMinStock(int minStock) {
        this.minStock = minStock;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Taxes getTaxe() {
        return taxe;
    }

    public void setTaxe(Taxes taxe) {
        this.taxe = taxe;
    }

    public ReasonTaxes getReasonTaxe() {
        return reasonTaxe;
    }

    public void setReasonTaxe(ReasonTaxes reasonTaxe) {
        this.reasonTaxe = reasonTaxe;
    }

    public GroupsProduct getGroup() {
        return group;
    }

    public void setGroup(GroupsProduct group) {
        this.group = group;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Product{"
                + "id=" + id
                + ", type=" + type
                + ", code=" + code
                + ", description=" + description
                + ", price=" + price
                + ", purchasePrice=" + purchasePrice
                + ", currentStock=" + currentStock
                + ", minStock=" + minStock
                + ", status=" + status + '}';
    }

}
