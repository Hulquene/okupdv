/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.dao.SupplierDao;
import com.okutonda.okudpdv.models.Supplier;
import java.util.List;

/**
 *
 * @author kenny
 */
public class SupplierController {

    SupplierDao dao;
//    ProductOrderDao prodOrderDao;

    public SupplierController() {
        this.dao = new SupplierDao();
//        this.prodOrderDao = new ProductOrderDao();
    }

    public Supplier add(Supplier supplier, int id) {
        boolean status;
        if (id == 0) {
            status = dao.add(supplier);
        } else {
            status = dao.edit(supplier, id);
        }
        if (status == true) {
            Supplier responde = dao.searchFromName(supplier.getNif());
            return responde;
        }
        return null;
    }

    public Supplier getId(int id) {
        return dao.getFromId(id);
    }

    public Boolean deleteId(int id) {
        return dao.delete(id);
    }

    public List<Supplier> get(String where) {
        return dao.list(where);
    }

    public List<Supplier> filter(String txt) {
        return dao.filter(txt);
    }
}
