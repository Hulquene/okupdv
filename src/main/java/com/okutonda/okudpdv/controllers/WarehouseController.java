package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.WarehouseDao;
import com.okutonda.okudpdv.data.entities.Warehouse;
import java.util.List;
import java.util.Optional;

/**
 * Controller responsável pelas regras de negócio dos armazéns (Warehouses) com
 * Hibernate.
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
    public Warehouse save(Warehouse warehouse) {
        if (warehouse == null) {
            System.err.println("❌ Armazém inválido (objeto nulo).");
            return null;
        }

        try {
            Warehouse savedWarehouse;

            if (warehouse.getId() == null || warehouse.getId() <= 0) {
                // Verifica duplicidade
                if (dao.existsByName(warehouse.getName())) {
                    System.err.println("❌ Já existe um armazém com o nome informado: " + warehouse.getName());
                    return null;
                }

                savedWarehouse = dao.save(warehouse);
            } else {
                savedWarehouse = dao.update(warehouse);
            }

            System.out.println("✅ Armazém salvo: " + savedWarehouse.getName());
            return savedWarehouse;

        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar armazém: " + e.getMessage());
            return null;
        }
    }

    public boolean delete(Integer id) {
        if (id == null || id <= 0) {
            System.err.println("❌ ID inválido para exclusão de armazém.");
            return false;
        }

        try {
            dao.delete(id);
            System.out.println("✅ Armazém removido ID: " + id);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erro ao remover armazém: " + e.getMessage());
            return false;
        }
    }

    // ==========================================================
    // 🔹 Consultas
    // ==========================================================
    public Warehouse getById(Integer id) {
        Optional<Warehouse> warehouseOpt = dao.findById(id);
        return warehouseOpt.orElse(null);
    }

    public List<Warehouse> listarTodos() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar armazéns: " + e.getMessage());
            return List.of();
        }
    }

    public List<Warehouse> filtrar(String texto) {
        try {
            return dao.filter(texto);
        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar armazéns: " + e.getMessage());
            return List.of();
        }
    }

    public List<Warehouse> listarAtivos() {
        try {
            return dao.findActive();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar armazéns ativos: " + e.getMessage());
            return List.of();
        }
    }

    // ==========================================================
    // 🔹 Métodos Adicionais
    // ==========================================================
    /**
     * Ativa/desativa um armazém
     */
    public boolean toggleWarehouseStatus(Integer id) {
        try {
            boolean success = dao.toggleWarehouseStatus(id);
            if (success) {
                System.out.println("✅ Status do armazém atualizado: " + id);
            }
            return success;
        } catch (Exception e) {
            System.err.println("❌ Erro ao alterar status do armazém: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca armazéns por localização
     */
    public List<Warehouse> findByLocation(String location) {
        try {
            return dao.findByLocation(location);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar armazéns por localização: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Verifica se nome já existe
     */
    public boolean nameExists(String name) {
        return dao.existsByName(name);
    }

    /**
     * Valida dados do armazém
     */
    public boolean validateWarehouse(Warehouse warehouse) {
        if (warehouse == null) {
            return false;
        }

        if (warehouse.getName() == null || warehouse.getName().trim().isEmpty()) {
            System.err.println("❌ Nome do armazém é obrigatório");
            return false;
        }

        if (warehouse.getName().length() > 100) {
            System.err.println("❌ Nome muito longo (máximo 100 caracteres)");
            return false;
        }

        if (warehouse.getLocation() != null && warehouse.getLocation().length() > 200) {
            System.err.println("❌ Localização muito longa (máximo 200 caracteres)");
            return false;
        }

        return true;
    }
}
