/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.dao.WarehouseDao;
import com.okutonda.okudpdv.models.Warehouse;
import java.util.List;

/**
 *
 * @author kenny
 */
public class WarehouseController {

    WarehouseDao dao;
//    ProductOrderDao prodOrderDao;

    public WarehouseController() {
        this.dao = new WarehouseDao();
//        this.prodOrderDao = new ProductOrderDao();
    }

    public Boolean add(Warehouse warehouse, int id) {
        boolean status;
        if (id == 0) {
            status = dao.add(warehouse);
        } else {
            status = dao.edit(warehouse, id);
        }
        return status;
    }

    public Warehouse getId(int id) {
        return dao.searchFromId(id);
    }

    public List<Warehouse> filter(String txt) {
        return dao.filter(txt);
    }

    public Boolean deleteId(int id) {
        return dao.delete(id);
    }

    public List<Warehouse> get(String where) {
        return dao.list(where);
    }
}
