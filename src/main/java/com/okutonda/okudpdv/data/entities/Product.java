package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20)
    private ProductType type = ProductType.PRODUCT;

    @Column(name = "code", length = 50)
    private String code;

    @Column(name = "barcode", length = 100)
    private String barcode;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "purchase_price", precision = 10, scale = 2)
    private BigDecimal purchasePrice = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private GroupsProduct group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_id")
    private Taxes taxe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reason_tax_id")
    private ReasonTaxes reasonTaxe;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private ProductStatus status = ProductStatus.ACTIVE;

    @Column(name = "min_stock")
    private Integer minStock = 0;

    @Transient
    private Integer currentStock;

    // Construtores
    public Product() {
    }

    public Product(String description, BigDecimal price, ProductType type) {
        this.description = description;
        this.price = price;
        this.type = type;
    }

    // Método de conveniência para compatibilidade
    public Product(String description, BigDecimal price, String type) {
        this.description = description;
        this.price = price;
        this.type = ProductType.fromCode(type);
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    // Getter/Setter para compatibilidade com código legado (String)
    public String getTypeString() {
        return type != null ? type.getCode() : ProductType.PRODUCT.getCode();
    }

    public void setTypeString(String type) {
        this.type = ProductType.fromCode(type);
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

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    // Getter/Setter para compatibilidade com código legado (Integer)
    public Integer getStatusInteger() {
        return status != null ? status.getCode() : ProductStatus.ACTIVE.getCode();
    }

    public void setStatusInteger(Integer status) {
        this.status = ProductStatus.fromCode(status);
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

    // Métodos utilitários
    public boolean isActive() {
        return status != null && status.isActive();
    }

    public boolean isProduct() {
        return type == ProductType.PRODUCT;
    }

    public boolean isService() {
        return type == ProductType.SERVICE;
    }

    @Override
    public String toString() {
        return "Product{id=" + id + ", description='" + description + "', price=" + price
                + ", type=" + (type != null ? type.getDescription() : "N/A")
                + ", status=" + (status != null ? status.getDescription() : "N/A") + "}";
    }
}
