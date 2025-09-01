/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.dao.PaymentModeDao;
import com.okutonda.okudpdv.models.PaymentModes;
import java.util.List;

/**
 *
 * @author kenny
 */
public class PaymentModeController {

    PaymentModeDao dao;

    public PaymentModeController() {
        this.dao = new PaymentModeDao();
    }

    public Boolean add(PaymentModes paymentModes, int id) {
        boolean status = false;
        if (id == 0) {
            status = dao.add(paymentModes);
        } else {
            status = dao.edit(paymentModes, id);
        }
        return status;
//        if (status == true) {
//            PaymentModes responde = dao.searchFromName(paymentModes.getName());
////            for (ProductOrder object : order.getProducts()) {
////                prodOrderDao.add(object);
////            }
//            return responde;
//        }
//        return null;
    }

    public PaymentModes getId(int id) {
        return dao.getId(id);
    }

    public Boolean deleteId(int id) {
        return dao.delete(id);
    }

    public List<PaymentModes> get(String where) {
        return dao.list(where);
    }
}
