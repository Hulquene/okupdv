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

    private final GroupsProductDao dao;

    public GroupsProductController() {
        this.dao = new GroupsProductDao();
    }

    // ======================
    // 🔹 CRUD
    // ======================
    /**
     * Cria ou atualiza grupo (id = 0 → cria, id > 0 → atualiza).
     */
    public boolean save(GroupsProduct group, int id) {
        if (id == 0) {
            return dao.add(group);
        } else {
            group.setId(id);
            return dao.update(group);
        }
    }

    /**
     * Exclui grupo pelo ID.
     */
    public boolean deleteById(int id) {
        return dao.delete(id);
    }

    /**
     * Busca grupo pelo ID.
     */
    public GroupsProduct getById(int id) {
        return dao.findById(id);
    }

    /**
     * Lista todos os grupos.
     */
    public List<GroupsProduct> getAll() {
        return dao.findAll();
    }

    // ======================
    // 🔹 Extras
    // ======================
    /**
     * Busca grupo pelo código.
     */
    public GroupsProduct getByCode(String code) {
        return dao.findByCode(code);
    }

    /**
     * Filtra grupos por nome ou código.
     */
    public List<GroupsProduct> filter(String text) {
        return dao.filter(text);
    }
}
