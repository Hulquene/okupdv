package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.CountryDao;
import com.okutonda.okudpdv.data.entities.Countries;

import java.util.List;

/**
 * Controller responsável pelas regras de negócio dos países (Countries).
 *
 * Atua como camada intermediária entre a interface (UI) e o DAO. Aplica
 * validações simples e padroniza as consultas.
 *
 * @author Hulquene
 */
public class CountryController {

    private final CountryDao dao;

    public CountryController() {
        this.dao = new CountryDao();
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public boolean save(Countries country) {
        if (country == null) {
            System.err.println("[Controller] País inválido (objeto nulo).");
            return false;
        }

        if (country.getId() <= 0) {
            return dao.add(country);
        } else {
            return dao.update(country);
        }
    }

    public boolean delete(int id) {
        if (id <= 0) {
            System.err.println("[Controller] ID inválido para exclusão de país.");
            return false;
        }
        return dao.delete(id);
    }

    // ==========================================================
    // 🔹 Consultas
    // ==========================================================
    public Countries getById(int id) {
        return dao.findById(id);
    }

    public List<Countries> listarTodos() {
        return dao.findAll();
    }

    public List<Countries> filtrar(String texto) {
        return dao.filter(texto);
    }

    public Countries getByIso2(String iso2) {
        return dao.findByIso2(iso2);
    }

    public Countries getByName(String nome) {
        return dao.findByName(nome);
    }
}
