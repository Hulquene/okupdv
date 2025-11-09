package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
public class Invoices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private PaymentStatus status = PaymentStatus.PENDENTE;

    @Column(name = "issue_date", length = 20, nullable = false)
    private String issueDate;

    @Column(name = "due_date", length = 20)
    private String dueDate;

    @Column(name = "number", nullable = false)
    private Integer number;

    @Column(name = "prefix", length = 10, nullable = false)
    private String prefix;

    @Column(name = "series", length = 10)
    private String series;

    @Column(name = "total", precision = 15, scale = 2, nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "sub_total", precision = 15, scale = 2, nullable = false)
    private BigDecimal subTotal = BigDecimal.ZERO;

    @Column(name = "total_taxe", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalTaxe = BigDecimal.ZERO;

    @Column(name = "discount", precision = 10, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name = "pay_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal payTotal = BigDecimal.ZERO;

    @Column(name = "amount_returned", precision = 10, scale = 2)
    private BigDecimal amountReturned = BigDecimal.ZERO;

    @Column(name = "hash", length = 255)
    private String hash;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "atcud", length = 100)
    private String atcud;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    // Relacionamentos
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Clients client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User seller;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY) // SEM CASCADE!
    private List<ProductSales> products = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Construtores
    public Invoices() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Invoices(Clients client, User seller, String prefix) {
        this();
        this.client = client;
        this.seller = seller;
        this.prefix = prefix != null ? prefix : "FT";
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status != null ? status : PaymentStatus.PENDENTE;
        this.updatedAt = LocalDateTime.now();
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
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
        this.prefix = prefix != null ? prefix : "FT";
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal != null ? subTotal : BigDecimal.ZERO;
    }

    public BigDecimal getTotalTaxe() {
        return totalTaxe;
    }

    public void setTotalTaxe(BigDecimal totalTaxe) {
        this.totalTaxe = totalTaxe != null ? totalTaxe : BigDecimal.ZERO;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount != null ? discount : BigDecimal.ZERO;
    }

    public BigDecimal getPayTotal() {
        return payTotal;
    }

    public void setPayTotal(BigDecimal payTotal) {
        this.payTotal = payTotal != null ? payTotal : BigDecimal.ZERO;
    }

    public BigDecimal getAmountReturned() {
        return amountReturned;
    }

    public void setAmountReturned(BigDecimal amountReturned) {
        this.amountReturned = amountReturned != null ? amountReturned : BigDecimal.ZERO;
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

    public String getAtcud() {
        return atcud;
    }

    public void setAtcud(String atcud) {
        this.atcud = atcud;
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

    // ✅ NOVO: Getter e Setter para products
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

    // Métodos de negócio
    public Boolean isPendente() {
        return status == PaymentStatus.PENDENTE;
    }

//    public PaymentStatus isEmitida() {
//        return status == PaymentStatus.;
//    }
    public Boolean isPaga() {
        return status == PaymentStatus.PAGO;
    }

    public Boolean isAnulada() {
        return status == PaymentStatus.CANCELADO;
    }

    public BigDecimal getSaldoPendente() {
        return total.subtract(payTotal != null ? payTotal : BigDecimal.ZERO);
    }

    public boolean isPagaTotalmente() {
        return getSaldoPendente().compareTo(BigDecimal.ZERO) <= 0;
    }

    // ✅ NOVO: Métodos para verificar tipo pelo prefixo
    public boolean isFatura() {
        return "FT".equals(prefix) || "FR".equals(prefix);
    }

    public boolean isNotaCredito() {
        return "NC".equals(prefix);
    }

    public boolean isNotaDebito() {
        return "ND".equals(prefix);
    }

    public boolean isRecibo() {
        return "RC".equals(prefix);
    }

    public boolean isProforma() {
        return "PP".equals(prefix);
    }

    public boolean isOrcamento() {
        return "OR".equals(prefix);
    }

    // Método para validar dados básicos
    public boolean isValid() {
        return prefix != null && !prefix.trim().isEmpty()
                && issueDate != null && !issueDate.trim().isEmpty()
                && number != null && number > 0
                && total != null && total.compareTo(BigDecimal.ZERO) >= 0
                && client != null
                && seller != null;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Invoice{"
                + "id=" + id
                + ", prefix=" + prefix
                + ", number=" + number
                + ", total=" + total
                + ", status=" + status
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoices)) {
            return false;
        }
        Invoices invoice = (Invoices) o;
        return id != null && id.equals(invoice.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
