package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.SupplierDao;
import com.okutonda.okudpdv.data.entities.Supplier;
import java.util.List;

/**
 * Controller responsável pelas regras de negócio dos Fornecedores (Suppliers).
 *
 * Aplica validações, previne duplicidades e faz ligação entre interface e DAO.
 *
 * @author Hulquene
 */
public class SupplierController {

    private final SupplierDao dao;

    public SupplierController() {
        this.dao = new SupplierDao();
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public boolean save(Supplier supplier) {
        if (supplier == null) {
            System.err.println("[Controller] Objeto Supplier inválido (nulo).");
            return false;
        }

        // Valida NIF duplicado
        if ((supplier.getId() == 0)
                && dao.existsByNif(supplier.getNif())) {
            System.err.println("[Controller] Já existe um fornecedor com este NIF.");
            return false;
        }

        if (supplier.getId() <= 0) {
            return dao.add(supplier);
        } else {
            return dao.update(supplier);
        }
    }

    public boolean delete(int id) {
        if (id <= 0) {
            System.err.println("[Controller] ID inválido para exclusão de fornecedor.");
            return false;
        }
        return dao.delete(id);
    }

    // ==========================================================
    // 🔹 Consultas
    // ==========================================================
    public Supplier getById(int id) {
        return dao.findById(id);
    }

    public Supplier getByName(String name) {
        return dao.findByName(name);
    }

    public Supplier getByNif(String nif) {
        return dao.findByNif(nif);
    }

    public List<Supplier> listarTodos() {
        List<Supplier> lista = dao.findAll();
        dao.close();
        return lista;
    }

    public List<Supplier> listarAtivos() {
        List<Supplier> lista = dao.findActive();
        dao.close();
        return lista;
//        return dao.findActive();
    }

    public List<Supplier> filtrar(String texto) {
        return dao.filter(texto);
    }
}
