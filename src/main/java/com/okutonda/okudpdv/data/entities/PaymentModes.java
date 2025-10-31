//package com.okutonda.okudpdv.data.entities;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "payment_modes")
//public class PaymentModes {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @Column(name = "name", nullable = false, length = 50)
//    private String name;
//
//    @Column(name = "description", columnDefinition = "TEXT")
//    private String description;
//
//    @Column(name = "code", length = 20)
//    private String code;
//
//    @Column(name = "status")
//    private Integer status = 1;
//
//    @Column(name = "isDefault")
//    private Integer isDefault = 0;
//
//    // Construtores
//    public PaymentModes() {
//    }
//
//    public PaymentModes(String name, String code, String description) {
//        this.name = name;
//        this.code = code;
//        this.description = description;
//    }
//
//    // Getters e Setters
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
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
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }
//
//    public Integer getIsDefault() {
//        return isDefault;
//    }
//
//    public void setIsDefault(Integer isDefault) {
//        this.isDefault = isDefault;
//    }
//
//    @Override
//    public String toString() {
//        return "PaymentModes{id=" + id + ", name='" + name + "', code='" + code + "'}";
//    }
//}
//
/////*
//// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
//// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
//// */
////package com.okutonda.okudpdv.data.entities;
////
/////**
//// *
//// * @author kenny
//// */
////public class PaymentModes {
////
////    private int id;
////    private String name;
////    private String description;
////    private String code;
////    private int status;
////    private int isDefault;
////
////    public int getId() {
////        return id;
////    }
////
////    public void setId(int id) {
////        this.id = id;
////    }
////
////    public String getName() {
////        return name;
////    }
////
////    public void setName(String name) {
////        this.name = name;
////    }
////
////    public String getDescription() {
////        return description;
////    }
////
////    public void setDescription(String description) {
////        this.description = description;
////    }
////
////    public String getCode() {
////        return code;
////    }
////
////    public void setCode(String code) {
////        this.code = code;
////    }
////
////    public int getStatus() {
////        return status;
////    }
////
////    public void setStatus(int status) {
////        this.status = status;
////    }
////
////    public int getIsDefault() {
////        return isDefault;
////    }
////
////    public void setIsDefault(int isDefault) {
////        this.isDefault = isDefault;
////    }
////
////    @Override
////    public String toString() {
////        return "PaymentModes{" + "id=" + id + ", name=" + name + ", description=" + description + ", code=" + code + ", status=" + status + ", isDefault=" + isDefault + '}';
////    }
////
////}
