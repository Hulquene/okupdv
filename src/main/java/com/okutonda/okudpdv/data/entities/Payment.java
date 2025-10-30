package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "prefix", length = 10)
    private String prefix;

    @Column(name = "number")
    private Integer number;

    @Column(name = "date", length = 20)
    private String date;

    @Column(name = "dateFinish", length = 20)
    private String dateFinish;

    @Column(name = "order_id")
    private Integer invoiceId;

    @Column(name = "order_type", length = 20)
    private String invoiceType = "ORDER";

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "currency", length = 10)
    private String currency = "AOA";

    @Enumerated(EnumType.STRING)
    @Column(name = "mode", length = 20)
    private PaymentMode paymentMode = PaymentMode.NUMERARIO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private PaymentStatus status = PaymentStatus.SUCCESS;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId")
    private Clients client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Construtores
    public Payment() {
        this.createdAt = LocalDateTime.now();
    }

    public Payment(String description, BigDecimal total, PaymentMode paymentMode) {
        this();
        this.description = description;
        this.total = total;
        this.paymentMode = paymentMode;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(String dateFinish) {
        this.dateFinish = dateFinish;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public Clients getClient() {
        return client;
    }

    public void setClient(Clients client) {
        this.client = client;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Payment{id=" + id + ", total=" + total + ", reference='" + reference + "'}";
    }

    public enum PaymentMode {
        NUMERARIO, TRANSFERENCIA, CARTAO, CHEQUE, MULTICAIXA, OUTRO
    }

    public enum PaymentStatus {
        PENDING, SUCCESS, FAILED, CANCELLED, REFUNDED
    }
}

///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.okutonda.okudpdv.data.entities;
//
//import java.math.BigDecimal;
//
///**
// *
// * @author kenny
// */
//public class Payment {
//
//    private int id;
//    private PaymentStatus status;     // SUCCESS ou FAILED
//    private String description;       // Observações / detalhe do pagamento
//    private BigDecimal total;         // valor pago
//    private String date;              // data criação
//    private String dateFinish;        // data liquidação/finalização
//    private Clients client;           // cliente associado
//    private int invoiceId;            // id da fatura
//    private String invoiceType;       // tipo da fatura (FT, FR, NC…)
//    private String prefix;            // prefixo da fatura
//    private int number;               // número da fatura
//    private PaymentMode paymentMode;  // NUMERARIO, MULTICAIXA, TRANSFERENCIA, OUTROS
//    private User user;                // operador/caixa que registrou
//    private String reference;         // referência NSU/IBAN/comprovativo (quando aplicável)
//    private String currency = "AOA";  // moeda (default Kwanza)
//
//    // getters/setters
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public PaymentStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(PaymentStatus status) {
//        this.status = status;
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
//    public BigDecimal getTotal() {
//        return total;
//    }
//
//    public void setTotal(BigDecimal total) {
//        this.total = total;
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
//    public String getDateFinish() {
//        return dateFinish;
//    }
//
//    public void setDateFinish(String dateFinish) {
//        this.dateFinish = dateFinish;
//    }
//
//    public Clients getClient() {
//        return client;
//    }
//
//    public void setClient(Clients client) {
//        this.client = client;
//    }
//
//    public int getInvoiceId() {
//        return invoiceId;
//    }
//
//    public void setInvoiceId(int invoiceId) {
//        this.invoiceId = invoiceId;
//    }
//
//    public String getInvoiceType() {
//        return invoiceType;
//    }
//
//    public void setInvoiceType(String invoiceType) {
//        this.invoiceType = invoiceType;
//    }
//
//    public String getPrefix() {
//        return prefix;
//    }
//
//    public void setPrefix(String prefix) {
//        this.prefix = prefix;
//    }
//
//    public int getNumber() {
//        return number;
//    }
//
//    public void setNumber(int number) {
//        this.number = number;
//    }
//
//    public PaymentMode getPaymentMode() {
//        return paymentMode;
//    }
//
//    public void setPaymentMode(PaymentMode paymentMode) {
//        this.paymentMode = paymentMode;
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
//    public String getReference() {
//        return reference;
//    }
//
//    public void setReference(String reference) {
//        this.reference = reference;
//    }
//
//    public String getCurrency() {
//        return currency;
//    }
//
//    public void setCurrency(String currency) {
//        this.currency = currency;
//    }
//
//    @Override
//    public String toString() {
//        return "Payment{"
//                + "id=" + id
//                + ", status=" + status
//                + ", description='" + description + '\''
//                + ", total=" + total
//                + ", date='" + date + '\''
//                + ", dateFinish='" + dateFinish + '\''
//                + ", client=" + client
//                + ", invoiceId=" + invoiceId
//                + ", invoiceType='" + invoiceType + '\''
//                + ", prefix='" + prefix + '\''
//                + ", number=" + number
//                + ", paymentMode=" + paymentMode
//                + ", user=" + user
//                + ", reference='" + reference + '\''
//                + ", currency='" + currency + '\''
//                + '}';
//    }
//}
