/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.entities;

/**
 *
 * @author kenny
 */
public class Countries {

    private int id;
    private String iso2;
    private String iso3;
    private String short_name;
    private String long_name;
    private String un_member;
    private String numcode;
    private String calling_code;
    private String cctld;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public String getUn_member() {
        return un_member;
    }

    public void setUn_member(String un_member) {
        this.un_member = un_member;
    }

    public String getNumcode() {
        return numcode;
    }

    public void setNumcode(String numcode) {
        this.numcode = numcode;
    }

    public String getCalling_code() {
        return calling_code;
    }

    public void setCalling_code(String calling_code) {
        this.calling_code = calling_code;
    }

    public String getCctld() {
        return cctld;
    }

    public void setCctld(String cctld) {
        this.cctld = cctld;
    }

    @Override
    public String toString() {
        return this.getShort_name();
    }

}
