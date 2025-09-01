/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.dao.ReasonTaxeDao;
import com.okutonda.okudpdv.models.ReasonTaxes;
import java.util.List;

/**
 *
 * @author kenny
 */
public class ReasonTaxeController {

    ReasonTaxeDao dao;
//    ProductOrderDao prodOrderDao;

    public ReasonTaxeController() {
        this.dao = new ReasonTaxeDao();
    }

    public ReasonTaxes getId(int id) {
        return dao.searchFromId(id);
    }

    public ReasonTaxes getCode(String code) {
        return dao.searchFromCode(code);
    }

    public List<ReasonTaxes> get(String where) {
        return dao.list(where);
    }

}
