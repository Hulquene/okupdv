/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.utilities;

/**
 *
 * @author kenny
 */
public class CompanySession {

    private static CompanySession instance;
    private String name;
    private String nif;
    private String address;
    private String email;
    private String phone;
    private String entity;
    private String keyLicence;
    private String dateKeyLicence;
    private String keyClient;
    private String time;

    public CompanySession() {
        // Construtor privado para evitar instanciamento externo
    }

    public static CompanySession getInstance() {
        if (instance == null) {
            instance = new CompanySession();
        }
        return instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getKeyLicence() {
        return keyLicence;
    }

    public void setKeyLicence(String keyLicence) {
        this.keyLicence = keyLicence;
    }

    public String getDateKeyLicence() {
        return dateKeyLicence;
    }

    public void setDateKeyLicence(String dateKeyLicence) {
        this.dateKeyLicence = dateKeyLicence;
    }

    public String getKeyClient() {
        return keyClient;
    }

    public void setKeyClient(String keyClient) {
        this.keyClient = keyClient;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning of this object is not allowed");
    }
}
