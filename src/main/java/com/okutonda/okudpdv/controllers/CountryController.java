package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.CountryDao;
import com.okutonda.okudpdv.data.entities.Countries;
import java.util.List;
import java.util.Optional;

/**
 * Controller para gestão de países
 */
public class CountryController {

    private final CountryDao dao;

    public CountryController() {
        this.dao = new CountryDao();
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public Countries save(Countries country) {
        if (country == null) {
            System.err.println("❌ País não pode ser nulo");
            return null;
        }

        try {
            // Validações
            if (!validarCountry(country)) {
                return null;
            }

            // CORREÇÃO: Verifica se é novo (id = 0) ou existente (id > 0)
            if (country.getId() == 0) {
                return dao.save(country);
            } else {
                return dao.update(country);
            }

        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar país: " + e.getMessage());
            return null;
        }
    }

    public boolean delete(Integer id) {
        if (id == null || id <= 0) {
            System.err.println("❌ ID inválido para exclusão");
            return false;
        }

        try {
            dao.delete(id);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erro ao excluir país: " + e.getMessage());
            return false;
        }
    }

    // ==========================================================
    // 🔹 Consultas
    // ==========================================================
    public Countries getById(Integer id) {
        Optional<Countries> country = dao.findById(id);
        return country.orElse(null);
    }

    public List<Countries> getAll() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar países: " + e.getMessage());
            return List.of();
        }
    }

    public Countries getByIso2(String iso2) {
        Optional<Countries> country = dao.findByIso2(iso2);
        return country.orElse(null);
    }

    public Countries getByIso3(String iso3) {
        Optional<Countries> country = dao.findByIso3(iso3);
        return country.orElse(null);
    }

    public Countries getByName(String name) {
        Optional<Countries> country = dao.findByName(name);
        return country.orElse(null);
    }

    public List<Countries> filter(String text) {
        try {
            return dao.filter(text);
        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar países: " + e.getMessage());
            return List.of();
        }
    }

    public List<Countries> getCommonCountries() {
        try {
            return dao.findCommonCountries();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar países comuns: " + e.getMessage());
            return List.of();
        }
    }

    public List<Countries> getUnMemberCountries() {
        try {
            return dao.findUnMemberCountries();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar países da ONU: " + e.getMessage());
            return List.of();
        }
    }

    // ==========================================================
    // 🔹 Validações
    // ==========================================================
    private boolean validarCountry(Countries country) {
        if (country.getIso2() == null || country.getIso2().length() != 2) {
            System.err.println("❌ ISO2 deve ter 2 caracteres");
            return false;
        }

        if (country.getIso3() == null || country.getIso3().length() != 3) {
            System.err.println("❌ ISO3 deve ter 3 caracteres");
            return false;
        }

        if (country.getLong_name() == null || country.getLong_name().trim().isEmpty()) {
            System.err.println("❌ Nome longo é obrigatório");
            return false;
        }

        // CORREÇÃO: Verificar duplicatas apenas para novos registros (id = 0)
        if (country.getId() == 0) {
            if (dao.existsByIso2(country.getIso2())) {
                System.err.println("❌ ISO2 já existe: " + country.getIso2());
                return false;
            }
            if (dao.existsByIso3(country.getIso3())) {
                System.err.println("❌ ISO3 já existe: " + country.getIso3());
                return false;
            }
        }

        return true;
    }

    /**
     * Verifica se um país existe pelo ISO2
     */
    public boolean existsByIso2(String iso2) {
        return dao.existsByIso2(iso2);
    }

    /**
     * Busca país por código de telefone
     */
    public Countries getByCallingCode(String callingCode) {
        List<Countries> countries = dao.findByCallingCode(callingCode);
        return countries.isEmpty() ? null : countries.get(0);
    }
}
