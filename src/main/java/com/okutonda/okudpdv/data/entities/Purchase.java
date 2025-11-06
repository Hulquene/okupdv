package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "invoice_number", nullable = false, length = 50)
    private String invoiceNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_type", length = 20)
    private InvoiceType invoiceType;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "iva_total", precision = 10, scale = 2)
    private BigDecimal ivaTotal = BigDecimal.ZERO;

    @Column(name = "total_pago", precision = 10, scale = 2)
    private BigDecimal total_pago = BigDecimal.ZERO;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_compra")
    private Date dataCompra;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_vencimento")
    private Date dataVencimento;

    // STATUS DE STOCK - controle de entrada no stock
    @Enumerated(EnumType.STRING)
    @Column(name = "stock_status", length = 20)
    private StockStatus stockStatus = StockStatus.PENDENTE;

    // STATUS DE PAGAMENTO - controle financeiro
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 20)
    private PaymentStatus paymentStatus = PaymentStatus.PENDENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PurchaseItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PurchasePayment> payments = new ArrayList<>();

    // Construtores
    public Purchase() {
    }

    public Purchase(Supplier supplier, String invoiceNumber) {
        this.supplier = supplier;
        this.invoiceNumber = invoiceNumber;
        this.dataCompra = new Date();
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getIvaTotal() {
        return ivaTotal;
    }

    public void setIvaTotal(BigDecimal ivaTotal) {
        this.ivaTotal = ivaTotal;
    }

    public BigDecimal getTotal_pago() {
        return total_pago;
    }

    public void setTotal_pago(BigDecimal total_pago) {
        this.total_pago = total_pago;
    }

    public Date getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(Date dataCompra) {
        this.dataCompra = dataCompra;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public StockStatus getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(StockStatus stockStatus) {
        this.stockStatus = stockStatus;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PurchaseItem> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItem> items) {
        this.items = items;
    }

    public List<PurchasePayment> getPayments() {
        return payments;
    }

    public void setPayments(List<PurchasePayment> payments) {
        this.payments = payments;
    }

    // Métodos utilitários
    public void addItem(PurchaseItem item) {
        items.add(item);
        item.setPurchase(this);
    }

    public void removeItem(PurchaseItem item) {
        items.remove(item);
        item.setPurchase(null);
    }

    public void addPayment(PurchasePayment payment) {
        payments.add(payment);
        payment.setPurchase(this);
    }

    public void removePayment(PurchasePayment payment) {
        payments.remove(payment);
        payment.setPurchase(null);
    }

    // Métodos de negócio
    public boolean isStockProcessado() {
        return stockStatus == StockStatus.PROCESSADO;
    }

    public boolean isStockPendente() {
        return stockStatus == StockStatus.PENDENTE;
    }

    public boolean isPaymentPago() {
        return paymentStatus == PaymentStatus.PAGO;
    }

    public boolean isPaymentPendente() {
        return paymentStatus == PaymentStatus.PENDENTE;
    }

    public boolean isPaymentParcial() {
        return paymentStatus == PaymentStatus.PARCIAL;
    }

    @Override
    public String toString() {
        return "Purchase{id=" + id + ", invoice='" + invoiceNumber + "', total=" + total
                + ", stock=" + stockStatus + ", payment=" + paymentStatus + "}";
    }
}
