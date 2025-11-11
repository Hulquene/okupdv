package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_items")
public class PurchaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "preco_custo", nullable = false, precision = 15, scale = 2)
    private BigDecimal precoCusto = BigDecimal.ZERO;

    @Column(name = "iva_percentual", precision = 5, scale = 2)
    private BigDecimal ivaPercentual = BigDecimal.ZERO;

    @Column(name = "iva_valor", precision = 15, scale = 2)
    private BigDecimal iva = BigDecimal.ZERO;

    @Column(name = "subtotal", precision = 15, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "total", precision = 15, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "quantidade_entrada")
    private Integer quantidadeEntrada = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "entrada_status", length = 20)
    private StockStatus entradaStatus = StockStatus.PENDENTE;

    @Column(name = "lote", length = 50)
    private String lote;

    @Column(name = "validade")
    private java.sql.Date validade;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    // Construtores
    public PurchaseItem() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public PurchaseItem(Product product, Integer quantidade, BigDecimal precoCusto) {
        this();
        this.product = product;
        this.quantidade = quantidade;
        this.precoCusto = precoCusto;
        calcularTotais();
    }

    public PurchaseItem(Product product, Integer quantidade, BigDecimal precoCusto, BigDecimal ivaPercentual) {
        this(product, quantidade, precoCusto);
        this.ivaPercentual = ivaPercentual;
        calcularTotais();
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getPurchaseId() {
        return purchase != null ? purchase.getId() : null;
    }

    public void setPurchaseId(Integer purchaseId) {
        // Método auxiliar para DAO - não implementa lógica
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        calcularTotais();
        this.updatedAt = LocalDateTime.now();
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
        calcularTotais();
        this.updatedAt = LocalDateTime.now();
    }

    public BigDecimal getIvaPercentual() {
        return ivaPercentual;
    }

    public void setIvaPercentual(BigDecimal ivaPercentual) {
        this.ivaPercentual = ivaPercentual;
        calcularTotais();
        this.updatedAt = LocalDateTime.now();
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
        this.updatedAt = LocalDateTime.now();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
        this.updatedAt = LocalDateTime.now();
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getQuantidadeEntrada() {
        return quantidadeEntrada;
    }

    public void setQuantidadeEntrada(Integer quantidadeEntrada) {
        this.quantidadeEntrada = quantidadeEntrada;
        atualizarStatusEntrada();
        this.updatedAt = LocalDateTime.now();
    }

    public StockStatus getEntradaStatus() {
        return entradaStatus;
    }

    public void setEntradaStatus(StockStatus entradaStatus) {
        this.entradaStatus = entradaStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
        this.updatedAt = LocalDateTime.now();
    }

    public java.sql.Date getValidade() {
        return validade;
    }

    public void setValidade(java.sql.Date validade) {
        this.validade = validade;
        this.updatedAt = LocalDateTime.now();
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

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
        this.updatedAt = LocalDateTime.now();
    }

    // 🔹 MÉTODOS DE NEGÓCIO MELHORADOS
    public void calcularTotais() {
        // Calcular subtotal (quantidade × preço)
        if (precoCusto != null && quantidade != null) {
            this.subtotal = precoCusto.multiply(BigDecimal.valueOf(quantidade));
        } else {
            this.subtotal = BigDecimal.ZERO;
        }

        // Calcular IVA se percentual estiver definido
        if (ivaPercentual != null && ivaPercentual.compareTo(BigDecimal.ZERO) > 0) {
            this.iva = subtotal.multiply(ivaPercentual).divide(BigDecimal.valueOf(100));
        } else {
            this.iva = BigDecimal.ZERO;
        }

        // Calcular total (subtotal + IVA)
        this.total = subtotal.add(iva);
    }

    public void calcularIVA(BigDecimal taxaIVA) {
        if (taxaIVA != null) {
            this.ivaPercentual = taxaIVA;
            calcularTotais();
        }
    }

    public void atualizarStatusEntrada() {
        if (quantidadeEntrada == null) {
            quantidadeEntrada = 0;
        }
        if (quantidade == null) {
            quantidade = 0;
        }

        if (quantidadeEntrada.equals(0)) {
            this.entradaStatus = StockStatus.PENDENTE;
        } else if (quantidadeEntrada.equals(quantidade)) {
            this.entradaStatus = StockStatus.DISPONIVEL;
        } else if (quantidadeEntrada < quantidade) {
            this.entradaStatus = StockStatus.PARCIAL;
        } else {
            this.entradaStatus = StockStatus.PROCESSADO;
        }
    }

    public void adicionarEntrada(Integer quantidadeEntrada) {
        if (quantidadeEntrada != null && quantidadeEntrada > 0) {
            this.quantidadeEntrada += quantidadeEntrada;
            atualizarStatusEntrada();
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void removerEntrada(Integer quantidadeEntrada) {
        if (quantidadeEntrada != null && quantidadeEntrada > 0) {
            this.quantidadeEntrada = Math.max(0, this.quantidadeEntrada - quantidadeEntrada);
            atualizarStatusEntrada();
            this.updatedAt = LocalDateTime.now();
        }
    }

    // 🔹 MÉTODOS DE VALIDAÇÃO
    public boolean isValid() {
        return product != null
                && product.getId() != null
                && quantidade != null
                && quantidade > 0
                && precoCusto != null
                && precoCusto.compareTo(BigDecimal.ZERO) >= 0;
    }

    public boolean isEntradaCompleta() {
        return quantidadeEntrada != null
                && quantidade != null
                && quantidadeEntrada >= quantidade;
    }

    public boolean isEntradaParcial() {
        return quantidadeEntrada != null
                && quantidade != null
                && quantidadeEntrada > 0
                && quantidadeEntrada < quantidade;
    }

    public boolean isEntradaPendente() {
        return quantidadeEntrada == null || quantidadeEntrada == 0;
    }

    // 🔹 MÉTODOS DE CONVENIÊNCIA
    public Integer getQuantidadePendente() {
        if (quantidade == null || quantidadeEntrada == null) {
            return 0;
        }
        return Math.max(0, quantidade - quantidadeEntrada);
    }

    public BigDecimal getValorEntrada() {
        if (precoCusto == null || quantidadeEntrada == null) {
            return BigDecimal.ZERO;
        }
        return precoCusto.multiply(BigDecimal.valueOf(quantidadeEntrada));
    }

    public BigDecimal getValorPendente() {
        if (precoCusto == null || getQuantidadePendente() == null) {
            return BigDecimal.ZERO;
        }
        return precoCusto.multiply(BigDecimal.valueOf(getQuantidadePendente()));
    }

    public String getDescricaoProduto() {
        return product != null ? product.getDescription() : "Produto não definido";
    }

    public String getCodigoProduto() {
        return product != null ? product.getCode() : "N/A";
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (quantidadeEntrada == null) {
            quantidadeEntrada = 0;
        }
        if (entradaStatus == null) {
            entradaStatus = StockStatus.PENDENTE;
        }
        calcularTotais();
        atualizarStatusEntrada();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calcularTotais();
        atualizarStatusEntrada();
    }

    @Override
    public String toString() {
        return String.format("PurchaseItem{id=%d, product=%s, quantidade=%d, preco=%.2f, total=%.2f, entrada=%d/%d}",
                id,
                product != null ? product.getDescription() : "null",
                quantidade != null ? quantidade : 0,
                precoCusto != null ? precoCusto.doubleValue() : 0.0,
                total != null ? total.doubleValue() : 0.0,
                quantidadeEntrada != null ? quantidadeEntrada : 0,
                quantidade != null ? quantidade : 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseItem)) {
            return false;
        }
        PurchaseItem that = (PurchaseItem) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
