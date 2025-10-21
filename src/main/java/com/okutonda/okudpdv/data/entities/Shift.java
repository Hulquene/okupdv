/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.entities;

/**
 *
 * @author kenny
 */
public class Shift {

    private int id;
    private User user;
    private String code;
    private String hash;
    private Double grantedAmount;
    private Double incurredAmount;
    private Double closingAmount;
    private String status;
    private String dateClose;
    private String dateOpen;
    private Box box;
    private User manager;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Double getGrantedAmount() {
        return grantedAmount;
    }

    public void setGrantedAmount(Double grantedAmount) {
        this.grantedAmount = grantedAmount;
    }

    public Double getIncurredAmount() {
        return incurredAmount;
    }

    public void setIncurredAmount(Double incurredAmount) {
        this.incurredAmount = incurredAmount;
    }

    public Double getClosingAmount() {
        return closingAmount;
    }

    public void setClosingAmount(Double closingAmount) {
        this.closingAmount = closingAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateClose() {
        return dateClose;
    }

    public void setDateClose(String dateClose) {
        this.dateClose = dateClose;
    }

    public String getDateOpen() {
        return dateOpen;
    }

    public void setDateOpen(String dateOpen) {
        this.dateOpen = dateOpen;
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

}
