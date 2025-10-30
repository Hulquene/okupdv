package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.TaxeDao;
import com.okutonda.okudpdv.data.entities.Taxes;

import java.util.List;

/**
 * Controller responsável pelas regras de negócio dos impostos (Taxes).
 *
 * Aplica validações simples e delega persistência ao DAO. Mantém
 * compatibilidade com as camadas fiscais e SAFT.
 *
 * @author Hulquene
 */
public class TaxeController {

    private final TaxeDao dao;

    public TaxeController() {
        this.dao = new TaxeDao();
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public boolean save(Taxes tax) {
        if (tax == null) {
            System.err.println("[Controller] Objeto Tax inválido (nulo).");
            return false;
        }

        if (tax.getId() <= 0) {
            return dao.add(tax);
        } else {
            return dao.update(tax);
        }
    }

    public boolean delete(int id) {
        if (id <= 0) {
            System.err.println("[Controller] ID inválido para exclusão de imposto.");
            return false;
        }
        return dao.delete(id);
    }

    // ==========================================================
    // 🔹 Consultas
    // ==========================================================
    public Taxes getById(int id) {
        return dao.findById(id);
    }

    public Taxes getByCode(String code) {
        return dao.findByCode(code);
    }

    public List<Taxes> listarTodas() {
        return dao.findAll();
    }

    public List<Taxes> filtrar(String termo) {
        return dao.filter(termo);
    }

    public Taxes getDefaultTax() {
        return dao.findDefaultTax();
    }
}
