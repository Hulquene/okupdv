package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "reason_taxes")
public class ReasonTaxes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", length = 20)
    private String code;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "reason", length = 100)
    private String reason;

    @Column(name = "standard", length = 50)
    private String standard;

    // Construtores
    public ReasonTaxes() {
    }

    public ReasonTaxes(String code, String description, String reason) {
        this.code = code;
        this.description = description;
        this.reason = reason;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    @Override
    public String toString() {
        return "ReasonTaxes{id=" + id + ", code='" + code + "', reason='" + reason + "'}";
    }
}
