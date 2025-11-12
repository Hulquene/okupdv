package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Column(name = "number", unique = true)
    private Integer number; // 0001, 0002 (apenas número)

    @Column(name = "invoice_number", length = 50)
    private String invoiceNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_type", length = 20)
    private DocumentType invoiceType;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "total", nullable = false, precision = 15, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "iva_total", precision = 15, scale = 2, nullable = false)
    private BigDecimal ivaTotal = BigDecimal.ZERO;

    @Column(name = "total_pago", precision = 15, scale = 2, nullable = false)
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

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PurchaseItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PurchasePayment> payments = new ArrayList<>();

    // 🔹 NOVOS CAMPOS PARA MELHOR CONTROLE
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    // Construtores
    public Purchase() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public Purchase(Supplier supplier, String invoiceNumber) {
        this();
        this.supplier = supplier;
        this.invoiceNumber = invoiceNumber;
        this.dataCompra = new Date();
        this.dataVencimento = java.sql.Date.valueOf(LocalDate.now().plusDays(30));
    }

    public Purchase(Supplier supplier, String invoiceNumber, DocumentType invoiceType) {
        this(supplier, invoiceNumber);
        this.invoiceType = invoiceType;
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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
    
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public DocumentType getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(DocumentType invoiceType) {
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
        this.updatedAt = new Date();
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
        calcularTotais();
        this.updatedAt = new Date();
    }

    public List<PurchasePayment> getPayments() {
        return payments;
    }

    public void setPayments(List<PurchasePayment> payments) {
        this.payments = payments;
        calcularTotalPago();
        this.updatedAt = new Date();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
        this.updatedAt = new Date();
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    // 🔹 MÉTODOS UTILITÁRIOS MELHORADOS
    public void addItem(PurchaseItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        item.setPurchase(this);
        calcularTotais();
        this.updatedAt = new Date();
    }

    public void removeItem(PurchaseItem item) {
        if (items != null) {
            items.remove(item);
            item.setPurchase(null);
            calcularTotais();
            this.updatedAt = new Date();
        }
    }

    public void addPayment(PurchasePayment payment) {
        if (payments == null) {
            payments = new ArrayList<>();
        }
        payments.add(payment);
        payment.setPurchase(this);
        calcularTotalPago();
        atualizarStatusBaseadoEmPagamentos();
        this.updatedAt = new Date();
    }

    public void removePayment(PurchasePayment payment) {
        if (payments != null) {
            payments.remove(payment);
            payment.setPurchase(null);
            calcularTotalPago();
            atualizarStatusBaseadoEmPagamentos();
            this.updatedAt = new Date();
        }
    }

    // 🔹 MÉTODOS DE NEGÓCIO MELHORADOS
    public boolean isStockProcessado() {
        return stockStatus == StockStatus.PROCESSADO || stockStatus == StockStatus.DISPONIVEL;
    }

    public boolean isStockPendente() {
        return stockStatus == StockStatus.PENDENTE;
    }

    public boolean isStockDisponivel() {
        return stockStatus == StockStatus.DISPONIVEL;
    }

    public boolean isPaga() {
        return paymentStatus == PaymentStatus.PAGO;
    }

    public boolean isPendente() {
        return paymentStatus == PaymentStatus.PENDENTE;
    }

    public boolean isParcial() {
        return paymentStatus == PaymentStatus.PARCIAL;
    }

    public boolean isAtrasada() {
        return paymentStatus == PaymentStatus.ATRASADO;
    }

    public boolean isCancelada() {
        return paymentStatus == PaymentStatus.CANCELADO;
    }

    public boolean isAtiva() {
        return Boolean.TRUE.equals(isActive) && !isCancelada();
    }

    // 🔹 NOVOS MÉTODOS DE CÁLCULO
    public void calcularTotais() {
        if (items == null || items.isEmpty()) {
            this.total = BigDecimal.ZERO;
            this.ivaTotal = BigDecimal.ZERO;
            return;
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal ivaTotal = BigDecimal.ZERO;

        for (PurchaseItem item : items) {
            if (item.getSubtotal() != null) {
                subtotal = subtotal.add(item.getSubtotal());
            }
            if (item.getIva() != null) {
                ivaTotal = ivaTotal.add(item.getIva());
            }
        }

        this.total = subtotal.add(ivaTotal);
        this.ivaTotal = ivaTotal;
    }

    public void calcularTotalPago() {
        if (payments == null || payments.isEmpty()) {
            this.total_pago = BigDecimal.ZERO;
            return;
        }

        BigDecimal totalPago = BigDecimal.ZERO;
        for (PurchasePayment payment : payments) {
            if (payment.getValorPago() != null
                    && (payment.getStatus() == PaymentStatus.PAGO || payment.getStatus() == PaymentStatus.PARCIAL)) {
                totalPago = totalPago.add(payment.getValorPago());
            }
        }

        this.total_pago = totalPago;
    }

    // 🔹 MÉTODO PARA ATUALIZAR STATUS AUTOMATICAMENTE
    public void atualizarStatusBaseadoEmPagamentos() {
        calcularTotalPago();

        BigDecimal saldoDevedor = this.total.subtract(this.total_pago);

        if (saldoDevedor.compareTo(BigDecimal.ZERO) <= 0) {
            this.paymentStatus = PaymentStatus.PAGO;
            this.stockStatus = StockStatus.DISPONIVEL;
        } else if (this.total_pago.compareTo(BigDecimal.ZERO) > 0) {
            this.paymentStatus = PaymentStatus.PARCIAL;
            this.stockStatus = StockStatus.DISPONIVEL;
        } else {
            this.paymentStatus = PaymentStatus.PENDENTE;
            this.stockStatus = StockStatus.PENDENTE;
        }

        // Verificar se está atrasada
        if (dataVencimento != null && dataVencimento.before(new Date()) && !isPaga()) {
            this.paymentStatus = PaymentStatus.ATRASADO;
        }

        this.updatedAt = new Date();
    }

    // 🔹 MÉTODOS DE VALIDAÇÃO
    public boolean isValid() {
        return supplier != null
                && invoiceNumber != null && !invoiceNumber.trim().isEmpty()
                && total != null && total.compareTo(BigDecimal.ZERO) >= 0
                && dataCompra != null;
    }

    public boolean temItensValidos() {
        if (items == null || items.isEmpty()) {
            return false;
        }

        for (PurchaseItem item : items) {
            if (!item.isValid()) {
                return false;
            }
        }
        return true;
    }

    // 🔹 MÉTODOS DE CONVENIÊNCIA
    public BigDecimal getSaldoDevedor() {
        return this.total.subtract(this.total_pago != null ? this.total_pago : BigDecimal.ZERO);
    }

    public boolean temSaldoDevedor() {
        return getSaldoDevedor().compareTo(BigDecimal.ZERO) > 0;
    }

    public int getQuantidadeTotalItens() {
        if (items == null) {
            return 0;
        }
        return items.stream()
                .mapToInt(item -> item.getQuantidade() != null ? item.getQuantidade() : 0)
                .sum();
    }

    public int getQuantidadeProdutosDiferentes() {
        if (items == null) {
            return 0;
        }
        return (int) items.stream()
                .map(item -> item.getProduct() != null ? item.getProduct().getId() : null)
                .distinct()
                .count();
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = new Date();
        }
        if (updatedAt == null) {
            updatedAt = new Date();
        }
        if (dataCompra == null) {
            dataCompra = new Date();
        }
        if (dataVencimento == null) {
            dataVencimento = java.sql.Date.valueOf(LocalDate.now().plusDays(30));
        }
        calcularTotais();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
        calcularTotais();
        calcularTotalPago();
    }

    @Override
    public String toString() {
        return String.format("Purchase{id=%d, invoice='%s', supplier=%s, total=%.2f, pago=%.2f, stock=%s, payment=%s}",
                id, invoiceNumber,
                supplier != null ? supplier.getName() : "null",
                total != null ? total.doubleValue() : 0.0,
                total_pago != null ? total_pago.doubleValue() : 0.0,
                stockStatus, paymentStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Purchase)) {
            return false;
        }
        Purchase purchase = (Purchase) o;
        return id != null && id.equals(purchase.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
