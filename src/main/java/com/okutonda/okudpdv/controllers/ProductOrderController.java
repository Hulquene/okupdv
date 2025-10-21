/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.ProductOrderDao;
import com.okutonda.okudpdv.data.entities.ProductOrder;
import java.util.List;

/**
 *
 * @author kenny
 */
public class ProductOrderController {

    ProductOrderDao dao;
//    ProductDao prodDao;
//    ProductOrderDao pOrderDao;

    public ProductOrderController() {
        this.dao = new ProductOrderDao();
//        this.prodDao = new ProductDao();
//        this.pOrderDao = new ProductOrderDao();
    }

    public ProductOrder getId(int id) {
        return dao.searchFromId(id);
    }

    public List<ProductOrder> getOrderId(int idOrder) {
        return dao.listProductFromOrderId(idOrder);
    }

    public List<ProductOrder> get(String where) {
        return dao.list(where);
    }

    public boolean add(ProductOrder prod, int orderId) {
        Boolean status = dao.add(prod, orderId);
        if (status == true) {
//            System.out.println("teste"+prod.getQty());
//            Product product = prodDao.getId(orderId);
//            System.out.println("teste2"+product.getStockTotal());
//            int total = product.getStockTotal() - prod.getQty();
//            System.out.println("total"+total);
//            prodDao.updateStock(orderId, total);
            return true;
        }
        return status;
    }
}
