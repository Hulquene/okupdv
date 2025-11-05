package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "status")
    private Integer status = 1;

    @Column(name = "datecreate", length = 20)
    private String datecreate;

    @Column(name = "duedate", length = 20) // ✅ NOVO CAMPO ADICIONADO
    private String duedate;

    @Column(name = "number")
    private Integer number;

    @Column(name = "prefix", length = 10)
    private String prefix;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO; // Valor padrão

    @Column(name = "sub_total", precision = 10, scale = 2)
    private BigDecimal subTotal = BigDecimal.ZERO; // Valor padrão

    @Column(name = "total_taxe", precision = 10, scale = 2)
    private BigDecimal totalTaxe = BigDecimal.ZERO; // Valor padrão

    @Column(name = "pay_total", precision = 10, scale = 2)
    private BigDecimal payTotal = BigDecimal.ZERO; // Valor padrão

    @Column(name = "amount_returned", precision = 10, scale = 2)
    private BigDecimal amountReturned = BigDecimal.ZERO; // Valor padrão

    @Column(name = "hash", length = 255)
    private String hash;

    @Column(name = "year")
    private Integer year;

//    @Column(name = "key", length = 100)
//    private String key;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Clients client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User seller;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductOrder> products = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Construtores
    public Order() {
        this.createdAt = LocalDateTime.now();
    }

    public Order(Clients client, User seller) {
        this();
        this.client = client;
        this.seller = seller;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDatecreate() {
        return datecreate;
    }

    public void setDatecreate(String datecreate) {
        this.datecreate = datecreate;
    }

    public String getDuedate() { // ✅ NOVO GETTER
        return duedate;
    }

    public void setDuedate(String duedate) { // ✅ NOVO SETTER
        this.duedate = duedate;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getTotalTaxe() {
        return totalTaxe;
    }

    public void setTotalTaxe(BigDecimal totalTaxe) {
        this.totalTaxe = totalTaxe;
    }

    public BigDecimal getPayTotal() {
        return payTotal;
    }

    public void setPayTotal(BigDecimal payTotal) {
        this.payTotal = payTotal;
    }

    public BigDecimal getAmountReturned() {
        return amountReturned;
    }

    public void setAmountReturned(BigDecimal amountReturned) {
        this.amountReturned = amountReturned;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

//    public String getKey() {
//        return key;
//    }
//
//    public void setKey(String key) {
//        this.key = key;
//    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public List<ProductOrder> getProducts() {
        return products;
    }

    public void setProducts(List<ProductOrder> products) {
        this.products = products;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Métodos utilitários
    public void addProduct(ProductOrder product) {
        products.add(product);
        product.setOrder(this);
    }

    public void removeProduct(ProductOrder product) {
        products.remove(product);
        product.setOrder(null);
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", number=" + number + ", prefix='" + prefix + "', total=" + total + "}";
    }
}
