/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.models;

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
    private Double price;
    private Double purchasePrice;
    private Taxes taxe;
    private ReasonTaxes reasonTaxe;
    private GroupsProduct group;
    private String barcode;
    private Supplier supplier;
    private int stockTotal;
//    private int stockMin;
    private int status;

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
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

    public int getStockTotal() {
        return stockTotal;
    }

    public void setStockTotal(int stockTotal) {
        this.stockTotal = stockTotal;
    }

//    public int getStockMin() {
//        return stockMin;
//    }
//
//    public void setStockMin(int stockMin) {
//        this.stockMin = stockMin;
//    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
