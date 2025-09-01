/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.dao.CountryDao;
import com.okutonda.okudpdv.models.Countries;
import java.util.List;

/**
 *
 * @author kenny
 */
public class CountryController {
    
    CountryDao dao;
//    ProductOrderDao prodOrderDao;

    public CountryController() {
        this.dao = new CountryDao();
//        this.prodOrderDao = new ProductOrderDao();
    }

    public Countries getId(int id) {
        return dao.searchFromId(id);
    }

    public List<Countries> get(String where) {
        return dao.list(where);
    }

    public List<Countries> filter(String txt) {
        return dao.filter(txt);
    }
}
