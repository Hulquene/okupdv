package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.GroupsProductDao;
import com.okutonda.okudpdv.data.entities.GroupsProduct;
import java.util.List;
import java.util.Optional;

/**
 * Controller de Grupos de Produtos com Hibernate.
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
     * Cria ou atualiza grupo (id = null/0 → cria, id > 0 → atualiza).
     */
    public GroupsProduct save(GroupsProduct group, Integer id) {
        try {
            GroupsProduct savedGroup;

            if (id == null || id == 0) {
                // Validar código duplicado antes de criar
                if (group.getCode() != null && !group.getCode().trim().isEmpty()) {
                    Optional<GroupsProduct> existing = dao.findByCode(group.getCode());
                    if (existing.isPresent()) {
                        System.err.println("❌ Já existe um grupo com este código: " + group.getCode());
                        return null;
                    }
                }

                // Criar novo grupo
                savedGroup = dao.save(group);
            } else {
                // Atualizar grupo existente
                group.setId(id);
                savedGroup = dao.update(group);
            }

            System.out.println("✅ Grupo salvo: " + savedGroup.getName());
            return savedGroup;

        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar grupo: " + e.getMessage());
            return null;
        }
    }

    /**
     * Exclui grupo pelo ID.
     */
    public boolean deleteById(Integer id) {
        try {
            dao.delete(id);
            System.out.println("✅ Grupo removido ID: " + id);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erro ao remover grupo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca grupo pelo ID.
     */
    public GroupsProduct getById(Integer id) {
        Optional<GroupsProduct> groupOpt = dao.findById(id);
        return groupOpt.orElse(null);
    }

    /**
     * Lista todos os grupos.
     */
    public List<GroupsProduct> getAll() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar grupos: " + e.getMessage());
            return List.of();
        }
    }

    // ======================
    // 🔹 Extras
    // ======================
    /**
     * Busca grupo pelo código.
     */
    public GroupsProduct getByCode(String code) {
        Optional<GroupsProduct> groupOpt = dao.findByCode(code);
        return groupOpt.orElse(null);
    }

    /**
     * Filtra grupos por nome ou código.
     */
    public List<GroupsProduct> filter(String text) {
        try {
            return dao.filter(text);
        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar grupos: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Verifica se um código já existe.
     */
    public boolean codeExists(String code) {
        return dao.findByCode(code).isPresent();
    }

    /**
     * Valida dados do grupo antes de salvar.
     */
    public boolean validateGroup(GroupsProduct group) {
        if (group == null) {
            return false;
        }

        if (group.getName() == null || group.getName().trim().isEmpty()) {
            System.err.println("❌ Nome do grupo é obrigatório");
            return false;
        }

        if (group.getCode() != null && group.getCode().length() > 50) {
            System.err.println("❌ Código muito longo (máximo 50 caracteres)");
            return false;
        }

        return true;
    }
}
