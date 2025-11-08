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

    // ✅ ATUALIZADO: Usando enum PaymentStatus
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private PaymentStatus status = PaymentStatus.PENDENTE;

    @Column(name = "datecreate", length = 20)
    private String datecreate;

    @Column(name = "duedate", length = 20)
    private String duedate;

    @Column(name = "number")
    private Integer number;

    @Column(name = "prefix", length = 10)
    private String prefix;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "sub_total", precision = 10, scale = 2)
    private BigDecimal subTotal = BigDecimal.ZERO;

    @Column(name = "total_taxe", precision = 10, scale = 2)
    private BigDecimal totalTaxe = BigDecimal.ZERO;

    @Column(name = "pay_total", precision = 10, scale = 2)
    private BigDecimal payTotal = BigDecimal.ZERO;

    @Column(name = "amount_returned", precision = 10, scale = 2)
    private BigDecimal amountReturned = BigDecimal.ZERO;

    @Column(name = "hash", length = 255)
    private String hash;

    @Column(name = "year")
    private Integer year;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Clients client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User seller;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<ProductSales> products = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Construtores
    public Order() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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

    // ✅ ATUALIZADO: Getters e Setters para PaymentStatus
    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status != null ? status : PaymentStatus.PENDENTE;
        this.updatedAt = LocalDateTime.now();
    }

    // ✅ NOVO: Método para compatibilidade com código existente (se necessário)
    public void setStatus(Integer statusCode) {
        if (statusCode != null) {
            switch (statusCode) {
                case 1:
                    this.status = PaymentStatus.PENDENTE;
                    break;
                case 2:
                    this.status = PaymentStatus.PAGO;
                    break;
                case 3:
                    this.status = PaymentStatus.PARCIAL;
                    break;
                case 4:
                    this.status = PaymentStatus.ATRASADO;
                    break;
                case 5:
                    this.status = PaymentStatus.CANCELADO;
                    break;
                default:
                    this.status = PaymentStatus.PENDENTE;
            }
        } else {
            this.status = PaymentStatus.PENDENTE;
        }
        this.updatedAt = LocalDateTime.now();
    }

    // ✅ NOVO: Método para obter código numérico (se necessário)
    public Integer getStatusCode() {
        switch (this.status) {
            case PENDENTE:
                return 1;
            case PAGO:
                return 2;
            case PARCIAL:
                return 3;
            case ATRASADO:
                return 4;
            case CANCELADO:
                return 5;
            default:
                return 1;
        }
    }

    public String getDatecreate() {
        return datecreate;
    }

    public void setDatecreate(String datecreate) {
        this.datecreate = datecreate;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
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

    public List<ProductSales> getProducts() {
        return products;
    }

    public void setProducts(List<ProductSales> products) {
        this.products = products;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ✅ NOVO: Métodos utilitários para status
    public boolean isPendente() {
        return PaymentStatus.PENDENTE.equals(status);
    }

    public boolean isPago() {
        return PaymentStatus.PAGO.equals(status);
    }

    public boolean isParcial() {
        return PaymentStatus.PARCIAL.equals(status);
    }

    public boolean isAtrasado() {
        return PaymentStatus.ATRASADO.equals(status);
    }

    public boolean isCancelado() {
        return PaymentStatus.CANCELADO.equals(status);
    }

    // ✅ NOVO: Método para obter descrição do status
    public String getStatusDescricao() {
        return status != null ? status.getDescricao() : "Desconhecido";
    }

    // ✅ NOVO: Método para calcular saldo pendente
    public BigDecimal getSaldoPendente() {
        return total.subtract(payTotal != null ? payTotal : BigDecimal.ZERO);
    }

    // ✅ NOVO: Método para verificar se está totalmente pago
    public boolean isTotalmentePago() {
        return getSaldoPendente().compareTo(BigDecimal.ZERO) <= 0;
    }

    // Métodos utilitários
    public void addProduct(ProductSales product) {
        products.add(product);
        product.setOrder(this);
    }

    public void removeProduct(ProductSales product) {
        products.remove(product);
        product.setOrder(null);
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", number=" + number + ", prefix='" + prefix + "', total=" + total + ", status=" + status + "}";
    }
}
