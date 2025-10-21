/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.PaymentDao;
import com.okutonda.okudpdv.data.entities.Payment;
import java.util.List;

/**
 *
 * @author kenny
 */
public class PaymentController {

    PaymentDao dao;
//    ProductOrderDao prodOrderDao;

    public PaymentController() {
        this.dao = new PaymentDao();
//        this.prodOrderDao = new ProductOrderDao();
    }

//    public Boolean add(Payment payment, int id) {
//        boolean status = false;
//        if (id == 0) {
//            status = dao.add(payment, payment.getInvoiceId());
//        } else {
//            status = dao.edit(payment, id);
//        }
//
////        if (status == true) {
////            Payment responde = dao.searchFromName(payment.getName());
//////            for (ProductOrder object : order.getProducts()) {
//////                prodOrderDao.add(object);
//////            }
////            return responde;
////        }
//        return status;
//    }
    public Boolean add(Payment payment, int invoiceId) {
        boolean status = false;
        if (invoiceId > 0) {
            status = dao.add(payment, invoiceId);
        }
        return status;
    }

    public Boolean edit(Payment payment, int id) {
        boolean status = false;
        if (id > 0) {
            status = dao.edit(payment, id);
        }
        return status;
    }

    public Payment getId(int id) {
        return dao.getId(id);
    }

    public Boolean deleteId(int id) {
        return dao.delete(id);
    }

    public List<Payment> get(String where) {
        return dao.list(where);
    }

    public List<Payment> filter(String txt) {
        return dao.filter(txt);
    }
}
