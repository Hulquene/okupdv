package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.SupplierDao;
import com.okutonda.okudpdv.data.entities.Supplier;
import java.util.List;
import java.util.Optional;

/**
 * Controller responsável pelas regras de negócio dos Fornecedores (Suppliers)
 * com Hibernate.
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
    /**
     * Cria ou atualiza fornecedor.
     */
    public Supplier save(Supplier supplier) {
        if (supplier == null) {
            System.err.println("❌ Objeto Supplier inválido (nulo).");
            return null;
        }

        try {
            // Valida NIF duplicado para novos fornecedores
            if (supplier.getId() == null && supplier.getNif() != null) {
                if (dao.existsByNif(supplier.getNif())) {
                    System.err.println("❌ Já existe um fornecedor com este NIF: " + supplier.getNif());
                    return null;
                }
            }

            Supplier savedSupplier;

            if (supplier.getId() == null || supplier.getId() <= 0) {
                // Criar novo fornecedor
                savedSupplier = dao.save(supplier);
            } else {
                // Atualizar fornecedor existente
                savedSupplier = dao.update(supplier);
            }

            System.out.println("✅ Fornecedor salvo: " + savedSupplier.getName());
            return savedSupplier;

        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar fornecedor: " + e.getMessage());
            return null;
        }
    }

    /**
     * Exclui fornecedor pelo ID.
     */
    public boolean delete(Integer id) {
        if (id == null || id <= 0) {
            System.err.println("❌ ID inválido para exclusão de fornecedor.");
            return false;
        }

        try {
            dao.delete(id);
            System.out.println("✅ Fornecedor removido ID: " + id);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erro ao remover fornecedor: " + e.getMessage());
            return false;
        }
    }

    // ==========================================================
    // 🔹 Consultas
    // ==========================================================
    public Supplier getById(Integer id) {
        Optional<Supplier> supplierOpt = dao.findById(id);
        return supplierOpt.orElse(null);
    }

    public Supplier getByName(String name) {
        Optional<Supplier> supplierOpt = dao.findByName(name);
        return supplierOpt.orElse(null);
    }

    public Supplier getByNif(String nif) {
        Optional<Supplier> supplierOpt = dao.findByNif(nif);
        return supplierOpt.orElse(null);
    }

    public List<Supplier> listarTodos() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar fornecedores: " + e.getMessage());
            return List.of();
        }
    }

    public List<Supplier> listarAtivos() {
        try {
            return dao.findActive();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar fornecedores ativos: " + e.getMessage());
            return List.of();
        }
    }

    public List<Supplier> filtrar(String texto) {
        try {
            return dao.filter(texto);
        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar fornecedores: " + e.getMessage());
            return List.of();
        }
    }

    // ==========================================================
    // 🔹 Métodos Adicionais
    // ==========================================================
    /**
     * Verifica se um NIF já existe.
     */
    public boolean nifExists(String nif) {
        if (nif == null || nif.trim().isEmpty()) {
            return false;
        }
        return dao.existsByNif(nif);
    }

    /**
     * Busca fornecedores por grupo.
     */
    public List<Supplier> listarPorGrupo(Integer groupId) {
        try {
            return dao.findByGroup(groupId);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar fornecedores por grupo: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Ativa/desativa um fornecedor.
     */
    public boolean toggleSupplierStatus(Integer id) {
        try {
            Optional<Supplier> supplierOpt = dao.findById(id);
            if (supplierOpt.isPresent()) {
                Supplier supplier = supplierOpt.get();
                supplier.setStatus(supplier.getStatus() == 1 ? 0 : 1);
                dao.update(supplier);
                System.out.println("✅ Status do fornecedor atualizado: " + supplier.getName());
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("❌ Erro ao alterar status do fornecedor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Valida dados básicos do fornecedor antes de salvar.
     */
    public boolean validarSupplier(Supplier supplier) {
        if (supplier == null) {
            return false;
        }

        if (supplier.getName() == null || supplier.getName().trim().isEmpty()) {
            System.err.println("❌ Nome do fornecedor é obrigatório");
            return false;
        }

        if (supplier.getNif() != null && supplier.getNif().length() > 25) {
            System.err.println("❌ NIF muito longo (máximo 25 caracteres)");
            return false;
        }

        if (supplier.getEmail() != null && !supplier.getEmail().contains("@")) {
            System.err.println("❌ Email inválido");
            return false;
        }

        return true;
    }

    /**
     * Conta total de fornecedores ativos.
     */
    public long contarFornecedoresAtivos() {
        try {
            return dao.findActive().size();
        } catch (Exception e) {
            System.err.println("❌ Erro ao contar fornecedores ativos: " + e.getMessage());
            return 0;
        }
    }
}
