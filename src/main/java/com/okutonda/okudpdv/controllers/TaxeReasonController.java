package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.TaxeReasonDao;
import com.okutonda.okudpdv.data.entities.ReasonTaxes;

import java.util.List;

/**
 * Controller responsável pela gestão de razões fiscais (isenção, regime,
 * etc.).
 *
 * Atua como camada intermediária entre a interface e o DAO. Aplica validações e
 * fornece métodos de consulta simplificados.
 *
 * @author Hulquene
 */
public class TaxeReasonController {

    private final TaxeReasonDao dao;

    public TaxeReasonController() {
        this.dao = new TaxeReasonDao();
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public boolean save(ReasonTaxes reason) {
        if (reason == null) {
            System.err.println("[Controller] Razão fiscal inválida (objeto nulo).");
            return false;
        }
        if (reason.getId() > 0) {
            return dao.update(reason);
        } else {
            return dao.add(reason);
        }
    }

    public boolean delete(int id) {
        if (id <= 0) {
            System.err.println("[Controller] ID inválido para exclusão.");
            return false;
        }
        return dao.delete(id);
    }

    // ==========================================================
    // 🔹 Consultas
    // ==========================================================
    public ReasonTaxes getById(int id) {
        return dao.findById(id);
    }

    public ReasonTaxes getByCode(String code) {
        return dao.searchFromCode(code);
    }

    public List<ReasonTaxes> listarTodas() {
        return dao.findAll();
    }

    public List<ReasonTaxes> pesquisar(String termo) {
        return dao.searchByDescription(termo);
    }
}
