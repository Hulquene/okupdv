/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.dao.ClientDao;
import com.okutonda.okudpdv.models.Clients;
import java.util.List;

/**
 *
 * @author kenny
 */
public class ClientController {

    ClientDao dao;
//    ProductOrderDao prodOrderDao;

    public ClientController() {
        this.dao = new ClientDao();
//        this.prodOrderDao = new ProductOrderDao();
    }

    public Clients add(Clients client, int id) {
        boolean status;
        if (id == 0) {
            status = dao.add(client);
        } else {
            status = dao.edit(client, id);
        }

        if (status == true) {
            Clients responde = dao.searchFromName(client.getName());
//            for (ProductOrder object : order.getProducts()) {
//                prodOrderDao.add(object);
//            }
            return responde;
        }
        return null;
    }

    public Clients getId(int id) {
        return dao.getId(id);
    }

    public Boolean deleteId(int id) {
        return dao.delete(id);
    }

    public List<Clients> get(String where) {
        return dao.list(where);
    }
}
