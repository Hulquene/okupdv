package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.WarehouseDao;
import com.okutonda.okudpdv.data.entities.Warehouse;

import java.util.List;

/**
 * Controller responsável pelas regras de negócio dos armazéns (Warehouses).
 *
 * Camada intermediária entre a interface e o DAO. Realiza validações simples e
 * padroniza operações CRUD.
 *
 * @author Hulquene
 */
public class WarehouseController {

    private final WarehouseDao dao;

    public WarehouseController() {
        this.dao = new WarehouseDao();
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public boolean save(Warehouse warehouse) {
        if (warehouse == null) {
            System.err.println("[Controller] Armazém inválido (objeto nulo).");
            return false;
        }

        if (warehouse.getId() <= 0) {
            // Verifica duplicidade
            if (dao.existsByName(warehouse.getName())) {
                System.err.println("[Controller] Já existe um armazém com o nome informado.");
                return false;
            }
            return dao.add(warehouse);
        } else {
            return dao.update(warehouse);
        }
    }

    public boolean delete(int id) {
        if (id <= 0) {
            System.err.println("[Controller] ID inválido para exclusão de armazém.");
            return false;
        }
        return dao.delete(id);
    }

    // ==========================================================
    // 🔹 Consultas
    // ==========================================================
    public Warehouse getById(int id) {
        return dao.findById(id);
    }

    public List<Warehouse> listarTodos() {
        return dao.findAll();
    }

    public List<Warehouse> filtrar(String texto) {
        return dao.filter(texto);
    }

    public List<Warehouse> listarAtivos() {
        return dao.findActive();
    }
}
