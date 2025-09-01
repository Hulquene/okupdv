/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.dao.TaxeDao;
import com.okutonda.okudpdv.models.Taxes;
import java.util.List;

/**
 *
 * @author kenny
 */
public class TaxeController {

    TaxeDao dao;
//    ProductOrderDao prodOrderDao;

    public TaxeController() {
        this.dao = new TaxeDao();
    }

    public Taxes getId(int id) {
        return dao.searchFromId(id);
    }

    public Taxes getCode(String code) {
        return dao.searchFromCode(code);
    }

    public List<Taxes> filter(String txt) {
        return dao.filter(txt);
    }
    public List<Taxes> get(String where) {
        return dao.list(where);
    }

    public Boolean add(Taxes tax, int id) {
        boolean status;
        if (id == 0) {
            status = dao.add(tax);
        } else {
            status = dao.edit(tax, id);
        }
        return status;
    }
    
    public Boolean deleteId(int id) {
        return dao.delete(id);
    }
}
