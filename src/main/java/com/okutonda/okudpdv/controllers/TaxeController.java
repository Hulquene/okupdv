package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.TaxeDao;
import com.okutonda.okudpdv.data.entities.Taxes;
import java.util.List;
import java.util.Optional;

/**
 * Controller responsável pelas regras de negócio dos impostos (Taxes) com
 * Hibernate.
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
    public Taxes save(Taxes tax) {
        if (tax == null) {
            System.err.println("❌ Objeto Tax inválido (nulo).");
            return null;
        }

        try {
            Taxes savedTax;

            if (tax.getId() == null || tax.getId() <= 0) {
                // Validar código duplicado
                if (tax.getCode() != null && !tax.getCode().trim().isEmpty()) {
                    if (dao.codeExists(tax.getCode())) {
                        System.err.println("❌ Já existe um imposto com este código: " + tax.getCode());
                        return null;
                    }
                }

                savedTax = dao.save(tax);
            } else {
                savedTax = dao.update(tax);
            }

            System.out.println("✅ Imposto salvo: " + savedTax.getName());
            return savedTax;

        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar imposto: " + e.getMessage());
            return null;
        }
    }

    public boolean delete(Integer id) {
        if (id == null || id <= 0) {
            System.err.println("❌ ID inválido para exclusão de imposto.");
            return false;
        }

        try {
            dao.delete(id);
            System.out.println("✅ Imposto removido ID: " + id);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erro ao remover imposto: " + e.getMessage());
            return false;
        }
    }

    // ==========================================================
    // 🔹 Consultas
    // ==========================================================
    public Taxes getById(Integer id) {
        Optional<Taxes> taxOpt = dao.findById(id);
        return taxOpt.orElse(null);
    }

    public Taxes getByCode(String code) {
        Optional<Taxes> taxOpt = dao.findByCode(code);
        return taxOpt.orElse(null);
    }

    public List<Taxes> listarTodas() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar impostos: " + e.getMessage());
            return List.of();
        }
    }

    public List<Taxes> filtrar(String termo) {
        try {
            return dao.filter(termo);
        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar impostos: " + e.getMessage());
            return List.of();
        }
    }

    public Taxes getDefaultTax() {
        Optional<Taxes> taxOpt = dao.findDefaultTax();
        return taxOpt.orElse(null);
    }

    /**
     * Define um imposto como padrão
     */
    public boolean setDefaultTax(Integer taxId) {
        try {
            boolean success = dao.setDefaultTax(taxId);
            if (success) {
                System.out.println("✅ Imposto padrão definido: " + taxId);
            }
            return success;
        } catch (Exception e) {
            System.err.println("❌ Erro ao definir imposto padrão: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica se código já existe
     */
    public boolean codeExists(String code) {
        return dao.codeExists(code);
    }

    /**
     * Busca impostos ativos
     */
    public List<Taxes> findActive() {
        try {
            return dao.findActive();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar impostos ativos: " + e.getMessage());
            return List.of();
        }
    }
}
