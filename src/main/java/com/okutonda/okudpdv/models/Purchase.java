/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author kenny
 */
public class Purchase {

    private Integer id;
    private Supplier supplier;
    private String invoiceNumber;
    private String invoiceType; // FT, FR, NC, ND
    private String descricao;
    private BigDecimal total;
    private BigDecimal ivaTotal;
    private Date dataCompra;
    private Date dataVencimento;
    private String status; // aberto, parcial, pago, atrasado

    private List<PurchaseItem> items;
    private List<PurchasePayment> payments;

    private BigDecimal total_pago;
    private String note;

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

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getPayTotal() {
        return total_pago;
    }

    public void setPayTotal(BigDecimal total_pago) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
//    private int id;
//    private String description;
//    private Product product;
//    private Double Total;
//    private Double priceSale;
//    private Double pricePurchase;
//    private int qty;
//    private String status;
//    private String statusPayment;
//    private String date;
//    private User user;
//    private Supplier supplier;
//    private int stockTotal;
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
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
//    public Product getProduct() {
//        return product;
//    }
//
//    public void setProduct(Product product) {
//        this.product = product;
//    }
//
//    public Double getTotal() {
//        return Total;
//    }
//
//    public void setTotal(Double Total) {
//        this.Total = Total;
//    }
//
//    public Double getPriceSale() {
//        return priceSale;
//    }
//
//    public void setPriceSale(Double priceSale) {
//        this.priceSale = priceSale;
//    }
//
//    public Double getPricePurchase() {
//        return pricePurchase;
//    }
//
//    public void setPricePurchase(Double pricePurchase) {
//        this.pricePurchase = pricePurchase;
//    }
//
//    public int getQty() {
//        return qty;
//    }
//
//    public void setQty(int qty) {
//        this.qty = qty;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getStatusPayment() {
//        return statusPayment;
//    }
//
//    public void setStatusPayment(String statusPayment) {
//        this.statusPayment = statusPayment;
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
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public Supplier getSupplier() {
//        return supplier;
//    }
//
//    public void setSupplier(Supplier supplier) {
//        this.supplier = supplier;
//    }
//
//    public int getStockTotal() {
//        return stockTotal;
//    }
//
//    public void setStockTotal(int stockTotal) {
//        this.stockTotal = stockTotal;
//    }
//    

}
