package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products_order")
public class ProductOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "date", length = 20)
    private String date;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price = BigDecimal.ZERO; // Valor padrão

    @Column(name = "unit", length = 20)
    private String unit;

    @Column(name = "prod_code", length = 50)
    private String code;

    @Column(name = "taxe_code", length = 20)
    private String taxeCode;

    @Column(name = "taxe_name", length = 100)
    private String taxeName;

    @Column(name = "taxe_percentage", precision = 5, scale = 2)
    private BigDecimal taxePercentage = BigDecimal.ZERO; // Valor padrão

    @Column(name = "reason_tax", length = 100)
    private String reasonTax;

    @Column(name = "reason_code", length = 20)
    private String reasonCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    // 🔥 ADICIONE ESTE RELACIONAMENTO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    // Construtores
    public ProductOrder() {
    }

    public ProductOrder(Integer orderId, Product product, Integer qty, BigDecimal price) {
        this.orderId = orderId;
        this.product = product;
        this.qty = qty;
        this.price = price;
        this.description = product.getDescription();
        this.code = product.getCode();
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
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

    public void setTaxeName(String taxeName) {
        this.taxeName = taxeName;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // 🔥 ADICIONE ESTES GETTERS E SETTERS
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
        if (order != null) {
            this.orderId = order.getId();
        }
    }

    // Método utilitário para calcular total do item
    @Transient
    public BigDecimal getTotal() {
        if (price != null && qty != null) {
            return price.multiply(BigDecimal.valueOf(qty));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String toString() {
        return "ProductOrder{id=" + id + ", orderId=" + orderId + ", description='" + description + "'}";
    }
}

//package com.okutonda.okudpdv.data.entities;
//
//import jakarta.persistence.*;
//import java.math.BigDecimal;
//
//@Entity
//@Table(name = "products_order")
//public class ProductOrder {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @Column(name = "order_id")
//    private Integer orderId;
//
//    @Column(name = "date", length = 20)
//    private String date;
//
//    @Column(name = "description", columnDefinition = "TEXT")
//    private String description;
//
//    @Column(name = "qty")
//    private Integer qty;
//
//    @Column(name = "price", precision = 10, scale = 2)
//    private BigDecimal price;
//
//    @Column(name = "unit", length = 20)
//    private String unit;
//
//    @Column(name = "prod_code", length = 50)
//    private String code;
//
//    @Column(name = "taxe_code", length = 20)
//    private String taxeCode;
//
//    @Column(name = "taxe_name", length = 100)
//    private String taxeName;
//
//    @Column(name = "taxe_percentage", precision = 5, scale = 2)
//    private BigDecimal taxePercentage;
//
//    @Column(name = "reason_tax", length = 100)
//    private String reasonTax;
//
//    @Column(name = "reason_code", length = 20)
//    private String reasonCode;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id")
//    private Product product;
//
//    // Construtores
//    public ProductOrder() {
//    }
//
//    public ProductOrder(Integer orderId, Product product, Integer qty, BigDecimal price) {
//        this.orderId = orderId;
//        this.product = product;
//        this.qty = qty;
//        this.price = price;
//        this.description = product.getDescription();
//        this.code = product.getCode();
//    }
//
//    // Getters e Setters
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public Integer getOrderId() {
//        return orderId;
//    }
//
//    public void setOrderId(Integer orderId) {
//        this.orderId = orderId;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public Integer getQty() {
//        return qty;
//    }
//
//    public void setQty(Integer qty) {
//        this.qty = qty;
//    }
//
//    public BigDecimal getPrice() {
//        return price;
//    }
//
//    public void setPrice(BigDecimal price) {
//        this.price = price;
//    }
//
//    public String getUnit() {
//        return unit;
//    }
//
//    public void setUnit(String unit) {
//        this.unit = unit;
//    }
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public String getTaxeCode() {
//        return taxeCode;
//    }
//
//    public void setTaxeCode(String taxeCode) {
//        this.taxeCode = taxeCode;
//    }
//
//    public String getTaxeName() {
//        return taxeName;
//    }
//
//    public void setTaxeName(String taxeName) {
//        this.taxeName = taxeName;
//    }
//
//    public BigDecimal getTaxePercentage() {
//        return taxePercentage;
//    }
//
//    public void setTaxePercentage(BigDecimal taxePercentage) {
//        this.taxePercentage = taxePercentage;
//    }
//
//    public String getReasonTax() {
//        return reasonTax;
//    }
//
//    public void setReasonTax(String reasonTax) {
//        this.reasonTax = reasonTax;
//    }
//
//    public String getReasonCode() {
//        return reasonCode;
//    }
//
//    public void setReasonCode(String reasonCode) {
//        this.reasonCode = reasonCode;
//    }
//
//    public Product getProduct() {
//        return product;
//    }
//
//    public void setProduct(Product product) {
//        this.product = product;
//    }
//
//    // Método utilitário para calcular total do item
//    @Transient
//    public BigDecimal getTotal() {
//        if (price != null && qty != null) {
//            return price.multiply(BigDecimal.valueOf(qty));
//        }
//        return BigDecimal.ZERO;
//    }
//
//    @Override
//    public String toString() {
//        return "ProductOrder{id=" + id + ", orderId=" + orderId + ", description='" + description + "'}";
//    }
//}
