/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.dao.StockDao;
import com.okutonda.okudpdv.models.Stock;
import java.util.List;

/**
 *
 * @author kenny
 */
public class StockController {
    
    StockDao dao;
//    ProductOrderDao prodOrderDao;

    public StockController() {
        this.dao = new StockDao();
//        this.prodOrderDao = new ProductOrderDao();
    }

    public Boolean add(Stock stock, int id) {
        boolean status = false;
        if (id == 0) {
            status = dao.add(stock);
        } else {
//            status = dao.edit(stock, id);
        }

//        if (status == true) {
//            Stock responde = dao.searchFromName(stock.getName());
////            for (ProductOrder object : order.getProducts()) {
////                prodOrderDao.add(object);
////            }
//            return responde;
//        }
        return status;
    }

    public Stock getId(int id) {
        return dao.getId(id);
    }

    public Boolean deleteId(int id) {
        return dao.delete(id);
    }

    public List<Stock> get(String where) {
        return dao.list(where);
    }
}
