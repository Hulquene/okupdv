/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.GroupsProductDao;
import com.okutonda.okudpdv.data.entities.GroupsProduct;
import java.util.List;

/**
 *
 * @author kenny
 */
public class GroupsProductController {

    GroupsProductDao dao;
//    ProductOrderDao prodOrderDao;

    public GroupsProductController() {
        this.dao = new GroupsProductDao();
    }

    public GroupsProduct getId(int id) {
        return dao.searchFromId(id);
    }

    public GroupsProduct getCode(String code) {
        return dao.searchFromCode(code);
    }

    public List<GroupsProduct> filter(String txt) {
        return dao.filter(txt);
    }

    public List<GroupsProduct> get(String where) {
        return dao.list(where);
    }

    public Boolean add(GroupsProduct group, int id) {
        boolean status;
        if (id == 0) {
            status = dao.add(group);
        } else {
            status = dao.edit(group, id);
        }
        return status;
    }

    public Boolean deleteId(int id) {
        return dao.delete(id);
    }
}
