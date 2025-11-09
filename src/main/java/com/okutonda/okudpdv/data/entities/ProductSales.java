package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product_sales")
public class ProductSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ✅ Tipo de documento (ORDER, INVOICE, QUOTE, etc.)
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", length = 10, nullable = false)
    private DocumentType documentType = DocumentType.FR;

    // ✅ ID do documento (substitui order_id)
    @Column(name = "document_id", nullable = false)
    private Integer documentId;

    @Column(name = "date", length = 20)
    private String date;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "unit", length = 20)
    private String unit;

    @Column(name = "prod_code", length = 50)
    private String code;

    @Column(name = "taxe_code", length = 20)
    private String taxeCode;

    @Column(name = "taxe_name", length = 100)
    private String taxeName;

    @Column(name = "taxe_percentage", precision = 5, scale = 2)
    private BigDecimal taxePercentage = BigDecimal.ZERO;

    @Column(name = "reason_tax", length = 100)
    private String reasonTax;

    @Column(name = "reason_code", length = 20)
    private String reasonCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    // ✅ CORREÇÃO: Relacionamento com Order (usando document_id e document_type)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "document_id", referencedColumnName = "id", insertable = false, updatable = false),
        @JoinColumn(name = "document_type", referencedColumnName = "prefix", insertable = false, updatable = false)
    })
    private Order order;

    // ✅ CORREÇÃO: Relacionamento com Invoices (usando document_id e document_type)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "document_id", referencedColumnName = "id", insertable = false, updatable = false),
        @JoinColumn(name = "document_type", referencedColumnName = "prefix", insertable = false, updatable = false)
    })
    private Invoices invoice;

    // Construtores
    public ProductSales() {
    }

    // ✅ Construtor para Order
    public ProductSales(Order order, Product product, Integer qty, BigDecimal price) {
        this.documentType = DocumentType.FR;
        this.documentId = order.getId();
        this.product = product;
        this.qty = qty;
        this.price = price;
        this.description = product.getDescription();
        this.code = product.getCode();
        this.date = order.getDatecreate();
        configurarImpostos(product);
    }

    // ✅ Construtor para Invoices
    public ProductSales(Invoices invoice, Product product, Integer qty, BigDecimal price) {
        this.documentType = DocumentType.FT;
        this.documentId = invoice.getId();
        this.product = product;
        this.qty = qty;
        this.price = price;
        this.description = product.getDescription();
        this.code = product.getCode();
        this.date = invoice.getIssueDate();
        configurarImpostos(product);
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType != null ? documentType : DocumentType.FR;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
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
        if (product != null) {
            configurarImpostos(product);
        }
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
        if (order != null) {
            this.documentId = order.getId();
//            this.documentType = DocumentType.fromString(order.getPrefix());
            this.documentType = DocumentType.FR;
            this.date = order.getDatecreate();
        }
    }

    public Invoices getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoices invoice) {
        this.invoice = invoice;
        if (invoice != null) {
            this.documentId = invoice.getId();
            this.documentType = DocumentType.FT;
            this.date = invoice.getIssueDate();
        }
    }

    // Métodos utilitários
    private void configurarImpostos(Product product) {
        if (product.getTaxe() != null) {
            this.taxePercentage = product.getTaxe().getPercetage();
            this.taxeCode = product.getTaxe().getCode();
            this.taxeName = product.getTaxe().getName();
        }

        if (product.getReasonTaxe() != null) {
            this.reasonCode = product.getReasonTaxe().getCode();
            this.reasonTax = product.getReasonTaxe().getReason();
        }
    }

    @Transient
    public BigDecimal getTotal() {
        if (price != null && qty != null) {
            return price.multiply(BigDecimal.valueOf(qty));
        }
        return BigDecimal.ZERO;
    }

    // Métodos para verificar tipo de documento
    public boolean isFromOrder() {
        return DocumentType.FR.equals(documentType);
    }

    public boolean isFromInvoice() {
        return DocumentType.FT.equals(documentType);
    }

    public boolean isFromProforma() {
        return DocumentType.PP.equals(documentType);
    }

    public String getDocumentTypeDescricao() {
        return documentType != null ? documentType.toString() : "Desconhecido";
    }

    public boolean isValid() {
        return documentType != null
                && documentId != null && documentId > 0
                && product != null
                && qty != null && qty > 0
                && price != null && price.compareTo(BigDecimal.ZERO) >= 0;
    }

    @Transient
    public Object getDocument() {
        switch (documentType) {
            case FR:
                return order;
            case FT:
                return invoice;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return "ProductSales{"
                + "id=" + id
                + ", documentType=" + documentType
                + ", documentId=" + documentId
                + ", description='" + description + '\''
                + ", qty=" + qty
                + ", total=" + getTotal()
                + '}';
    }
}
