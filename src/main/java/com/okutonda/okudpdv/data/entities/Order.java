package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;
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

    @Column(name = "total")
    private Double total = 0.0;

    @Column(name = "sub_total")
    private Double subTotal = 0.0;

    @Column(name = "total_taxe")
    private Double totalTaxe = 0.0;

    @Column(name = "pay_total")
    private Double payTotal = 0.0;

    @Column(name = "amount_returned")
    private Double amountReturned = 0.0;

    @Column(name = "hash", length = 255)
    private String hash;

    @Column(name = "year")
    private Integer year;

    @Column(name = "key", length = 100)
    private String key;

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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getTotalTaxe() {
        return totalTaxe;
    }

    public void setTotalTaxe(Double totalTaxe) {
        this.totalTaxe = totalTaxe;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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
