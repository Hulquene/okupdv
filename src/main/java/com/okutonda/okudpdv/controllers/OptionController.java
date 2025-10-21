/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.OptionsDao;
import com.okutonda.okudpdv.data.entities.Options;

/**
 *
 * @author kenny
 */
public class OptionController {

    OptionsDao dao;
//    ProductOrderDao prodOrderDao;

    public OptionController() {
        this.dao = new OptionsDao();
    }

    public Options getOptions(String name) {
        Options op = dao.searchFromName(name);
//        System.out.println("valor: "+op);
        if (op == null) {
        } else {
            return op;
        }
        return null;
    }

    public String getValueOptions(String name) {
        Options op = dao.searchFromName(name);
        if (op != null) {
            return op.getValue();
        }
        return "";
    }

    public Boolean add(Options options) {
        Options opt = getOptions(options.getName());
        if (opt != null) {
            return dao.edit(options);
        } else {
            return dao.add(options);
        }
    }

    public Boolean save(Options options) {
        return dao.add(options);
    }
//    public Boolean deleteId(int id) {
//        return dao.delete(id);
//    }
}
