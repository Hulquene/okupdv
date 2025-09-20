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

    private final WarehouseDao dao;

    public WarehouseController() {
        this.dao = new WarehouseDao();
    }

    /**
     * Adicionar ou editar
     */
    public Boolean add(Warehouse warehouse, int id) {
        if (id == 0) {
            return dao.add(warehouse);
        } else {
            return dao.edit(warehouse, id);
        }
    }

    /**
     * Buscar por ID
     */
    public Warehouse getId(int id) {
        return dao.searchFromId(id);
    }

    /**
     * Listar com filtro LIKE
     */
    public List<Warehouse> filter(String txt) {
        return dao.filter(txt);
    }

    /**
     * Excluir
     */
    public Boolean deleteId(int id) {
        return dao.delete(id);
    }

    /**
     * Listar todos (com ou sem WHERE)
     */
    public List<Warehouse> get(String where) {
        return dao.list(where);
    }
}
