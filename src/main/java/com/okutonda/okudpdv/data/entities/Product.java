package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type", length = 20)
    private String type;

    @Column(name = "code", length = 50)
    private String code;

    @Column(name = "barcode", length = 100)
    private String barcode;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "purchase_price", precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private GroupsProduct group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_id")
    private Taxes taxe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reason_tax_id")
    private ReasonTaxes reasonTaxe;

    @Column(name = "status")
    private Integer status = 1;

    @Column(name = "min_stock")
    private Integer minStock = 0;

    @Transient
    private Integer currentStock;

    // Construtores
    public Product() {
    }

    public Product(String description, BigDecimal price, String type) {
        this.description = description;
        this.price = price;
        this.type = type;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public GroupsProduct getGroup() {
        return group;
    }

    public void setGroup(GroupsProduct group) {
        this.group = group;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMinStock() {
        return minStock;
    }

    public void setMinStock(Integer minStock) {
        this.minStock = minStock;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    @Override
    public String toString() {
        return "Product{id=" + id + ", description='" + description + "', price=" + price + "}";
    }
}
