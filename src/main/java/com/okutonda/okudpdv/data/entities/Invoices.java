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
    private String prefix = "FT"; // ✅ EXCLUSIVO: FT

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

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY)
    private List<ProductSales> products = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Construtores
    public Invoices() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.prefix = "FT"; // ✅ SEMPRE FT
    }

    public Invoices(Clients client, User seller) {
        this();
        this.client = client;
        this.seller = seller;
        // ✅ Prefix é sempre "FT", não precisa de parâmetro
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
        return "FT"; // ✅ SEMPRE RETORNA FT
    }

    public void setPrefix(String prefix) {
        // ✅ IGNORA qualquer tentativa de mudar o prefix - sempre será FT
        this.prefix = "FT";
        this.updatedAt = LocalDateTime.now();
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

    // ✅ MÉTODOS ESPECÍFICOS PARA FATURAS (FT)
    public String getNumeroFormatado() {
        if (number == null || year == null) {
            return "N/A";
        }
        return String.format("FT %d/%d", year, number);
    }

    public String getDescricaoCompleta() {
        return String.format("%s - Fatura - %s", 
            getNumeroFormatado(),
            client != null ? client.getName() : "Cliente não definido"
        );
    }

    // Métodos de negócio específicos para faturas
    public Boolean isPendente() {
        return status == PaymentStatus.PENDENTE;
    }

    public Boolean isParcial() {
        return status == PaymentStatus.PARCIAL;
    }

    public Boolean isPaga() {
        return status == PaymentStatus.PAGO;
    }

    public Boolean isAtrasada() {
        return status == PaymentStatus.ATRASADO;
    }

    public Boolean isCancelada() {
        return status == PaymentStatus.CANCELADO;
    }

    public BigDecimal getSaldoPendente() {
        return total.subtract(payTotal != null ? payTotal : BigDecimal.ZERO);
    }

    public boolean isPagaTotalmente() {
        return getSaldoPendente().compareTo(BigDecimal.ZERO) <= 0;
    }

    public boolean temSaldoPendente() {
        return getSaldoPendente().compareTo(BigDecimal.ZERO) > 0;
    }

    // ✅ MÉTODOS EXCLUSIVOS PARA FATURAS
    public boolean isFatura() {
        return true; // ✅ SEMPRE verdadeiro - esta entidade é exclusiva para faturas
    }

    public boolean isDocumentoFiscal() {
        return true; // ✅ FT é sempre documento fiscal
    }

    public boolean isDocumentoValidoParaSAFT() {
        return true; // ✅ FT é sempre válido para SAF-T
    }

    // ✅ NOVO: Método para obter descrição do tipo de documento
    public String getDocumentTypeDescricao() {
        return "Fatura";
    }

    // ✅ NOVO: Método para verificar se pode ser editada
    public boolean podeSerEditada() {
        return isPendente() || isParcial();
    }

    // ✅ NOVO: Método para verificar se pode ser cancelada
    public boolean podeSerCancelada() {
        return !isCancelada() && !isPaga();
    }

    // ✅ NOVO: Método para verificar se está vencida
    public boolean isVencida() {
        if (dueDate == null) return false;
        
        try {
            LocalDateTime vencimento = LocalDateTime.parse(dueDate + "T00:00:00");
            return LocalDateTime.now().isAfter(vencimento);
        } catch (Exception e) {
            return false;
        }
    }

    // ✅ NOVO: Método para calcular dias em atraso
    public Integer getDiasAtraso() {
        if (dueDate == null || !isVencida()) return 0;
        
        try {
            LocalDateTime vencimento = LocalDateTime.parse(dueDate + "T00:00:00");
            return (int) java.time.Duration.between(vencimento, LocalDateTime.now()).toDays();
        } catch (Exception e) {
            return 0;
        }
    }

    // Método para validar dados básicos de fatura
    public boolean isValid() {
        return "FT".equals(prefix) // ✅ SEMPRE deve ser FT
                && issueDate != null && !issueDate.trim().isEmpty()
                && number != null && number > 0
                && total != null && total.compareTo(BigDecimal.ZERO) >= 0
                && client != null
                && seller != null;
    }

    // ✅ NOVO: Método para validar para emissão
    public boolean isValidParaEmissao() {
        return isValid() 
                && isPendente() 
                && total.compareTo(BigDecimal.ZERO) > 0
                && products != null && !products.isEmpty();
    }

    // ✅ NOVO: Método específico para faturas - cálculo de IVA
    public BigDecimal calcularIVA() {
        // Supondo que o IVA é 14% em Angola
        BigDecimal taxaIVA = new BigDecimal("0.14");
        return subTotal.multiply(taxaIVA).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    // ✅ NOVO: Método para verificar se fatura está dentro do prazo
    public boolean isDentroDoPrazo() {
        if (dueDate == null) return true;
        
        try {
            LocalDateTime vencimento = LocalDateTime.parse(dueDate + "T23:59:59");
            return LocalDateTime.now().isBefore(vencimento);
        } catch (Exception e) {
            return true;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return getNumeroFormatado(); // ✅ Retorna "FT 2024/123"
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Invoices)) return false;
        Invoices invoice = (Invoices) o;
        return id != null && id.equals(invoice.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}