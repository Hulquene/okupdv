package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.TaxeReasonDao;
import com.okutonda.okudpdv.data.entities.ReasonTaxes;
import java.util.List;
import java.util.Optional;

/**
 * Controller responsável pela gestão de razões fiscais com Hibernate.
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
    public ReasonTaxes save(ReasonTaxes reason) {
        if (reason == null) {
            System.err.println("❌ Razão fiscal inválida (objeto nulo).");
            return null;
        }

        try {
            ReasonTaxes savedReason;

            if (reason.getId() == null || reason.getId() <= 0) {
                // Validar código duplicado
                if (reason.getCode() != null && !reason.getCode().trim().isEmpty()) {
                    if (dao.codeExists(reason.getCode())) {
                        System.err.println("❌ Já existe uma razão fiscal com este código: " + reason.getCode());
                        return null;
                    }
                }

                savedReason = dao.save(reason);
            } else {
                savedReason = dao.update(reason);
            }

            System.out.println("✅ Razão fiscal salva: " + savedReason.getCode());
            return savedReason;

        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar razão fiscal: " + e.getMessage());
            return null;
        }
    }

    public boolean delete(Integer id) {
        if (id == null || id <= 0) {
            System.err.println("❌ ID inválido para exclusão.");
            return false;
        }

        try {
            dao.delete(id);
            System.out.println("✅ Razão fiscal removida ID: " + id);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erro ao remover razão fiscal: " + e.getMessage());
            return false;
        }
    }

    // ==========================================================
    // 🔹 Consultas
    // ==========================================================
    public ReasonTaxes getById(Integer id) {
        Optional<ReasonTaxes> reasonOpt = dao.findById(id);
        return reasonOpt.orElse(null);
    }

    public ReasonTaxes getByCode(String code) {
        Optional<ReasonTaxes> reasonOpt = dao.findByCode(code);
        return reasonOpt.orElse(null);
    }

    public List<ReasonTaxes> listarTodas() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar razões fiscais: " + e.getMessage());
            return List.of();
        }
    }

    public List<ReasonTaxes> pesquisar(String termo) {
        try {
            return dao.searchByDescription(termo);
        } catch (Exception e) {
            System.err.println("❌ Erro ao pesquisar razões fiscais: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Filtra razões fiscais por texto
     */
    public List<ReasonTaxes> filter(String text) {
        try {
            return dao.filter(text);
        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar razões fiscais: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Busca razões fiscais por padrão
     */
    public List<ReasonTaxes> findByStandard(String standard) {
        try {
            return dao.findByStandard(standard);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar razões fiscais por padrão: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Verifica se código já existe
     */
    public boolean codeExists(String code) {
        return dao.codeExists(code);
    }
}
