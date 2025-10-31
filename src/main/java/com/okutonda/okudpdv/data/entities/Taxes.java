package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "taxes")
public class Taxes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "percentage", precision = 5, scale = 2)
    private BigDecimal percentage = BigDecimal.ZERO; // Valor padrão

    @Column(name = "isdefault")
    private Integer isDefault = 0;

    // Construtores
    public Taxes() {
    }

    public Taxes(String name, String code, BigDecimal percentage) {
        this.name = name;
        this.code = code;
        this.percentage = percentage;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    // Método de conveniência para compatibilidade
    public BigDecimal getPercetage() {
        return percentage;
    }

    public void setPercetage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "Taxes{id=" + id + ", name='" + name + "', percentage=" + percentage + "}";
    }
}
