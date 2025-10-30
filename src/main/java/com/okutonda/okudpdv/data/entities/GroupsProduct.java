package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "groups_product")
public class GroupsProduct {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "code", length = 50)
    private String code;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    // Construtores
    public GroupsProduct() {}
    
    public GroupsProduct(String name, String code) {
        this.name = name;
        this.code = code;
    }
    
    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    @Override
    public String toString() {
        return "GroupsProduct{id=" + id + ", name='" + name + "', code='" + code + "'}";
    }
}