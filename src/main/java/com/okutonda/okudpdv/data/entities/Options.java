package com.okutonda.okudpdv.data.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "options")
public class Options {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "value", columnDefinition = "TEXT")
    private String value;

    @Column(name = "status", length = 20)
    private String status;

    // Construtores
    public Options() {
    }

    public Options(String name, String value, String status) {
        this.name = name;
        this.value = value;
        this.status = status;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Options{id=" + id + ", name='" + name + "', value='" + value + "'}";
    }
}

///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.okutonda.okudpdv.data.entities;
//
///**
// *
// * @author kenny
// */
//public class Options {
//
//    private int id;
//    private String name;
//    private String value;
//    private String status;
//
//    public Options(String name, String value, String status) {
//        this.name = name;
//        this.value = value;
//        this.status = status;
//    }
//
//    public Options() {
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getValue() {
//        return value;
//    }
//
//    public void setValue(String value) {
//        this.value = value;
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
//}
