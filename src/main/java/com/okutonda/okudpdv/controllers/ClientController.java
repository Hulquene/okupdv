package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.ClientDao;
import com.okutonda.okudpdv.data.entities.Clients;
import java.util.List;
import java.util.Optional;

/**
 * Controller de Clientes com Hibernate.
 *
 * @author kenny
 */
public class ClientController {

    private final ClientDao dao;

    public ClientController() {
        this.dao = new ClientDao();
    }

    // ===========================
    // 🔹 CRUD e Regras de Negócio
    // ===========================
    /**
     * Cria ou atualiza cliente (id = null/0 → cria, id > 0 → atualiza).
     */
    public Clients save(Clients client, Integer id) {
        try {
            Clients savedClient;

            if (id == null || id == 0) {
                // Validar NIF duplicado antes de criar
                if (client.getNif() != null && !client.getNif().trim().isEmpty()) {
                    Optional<Clients> existing = dao.findByNif(client.getNif());
                    if (existing.isPresent()) {
                        System.err.println("❌ Já existe um cliente com este NIF: " + client.getNif());
                        return null;
                    }
                }

                // Criar novo cliente
                savedClient = dao.save(client);
            } else {
                // Atualizar cliente existente
                client.setId(id);
                savedClient = dao.update(client);
            }

            System.out.println("✅ Cliente salvo: " + savedClient.getName());
            return savedClient;

        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar cliente: " + e.getMessage());
            return null;
        }
    }

    /**
     * Exclui cliente pelo ID.
     */
    public boolean deleteById(Integer id) {
        try {
            dao.delete(id);
            System.out.println("✅ Cliente removido ID: " + id);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erro ao remover cliente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca cliente pelo ID.
     */
    public Clients getById(Integer id) {
        Optional<Clients> clientOpt = dao.findById(id);
        return clientOpt.orElse(null);
    }

    /**
     * Lista todos os clientes.
     */
    public List<Clients> getAll() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar clientes: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Filtra clientes por nome, NIF ou cidade.
     */
    public List<Clients> filter(String text) {
        try {
            return dao.filter(text);
        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar clientes: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Retorna o cliente padrão (isdefault = 1).
     */
    public Clients getDefaultClient() {
        Optional<Clients> clientOpt = dao.getDefaultClient();
        return clientOpt.orElse(null);
    }

    /**
     * Define um cliente como padrão.
     */
    public boolean setDefaultClient(Integer clientId) {
        try {
            boolean success = dao.setDefaultClient(clientId);
            if (success) {
                System.out.println("✅ Cliente definido como padrão: " + clientId);
            }
            return success;
        } catch (Exception e) {
            System.err.println("❌ Erro ao definir cliente padrão: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca cliente pelo nome exato.
     */
    public Clients getByName(String name) {
        Optional<Clients> clientOpt = dao.findByName(name);
        return clientOpt.orElse(null);
    }

    /**
     * Busca cliente pelo NIF.
     */
    public Clients getByNif(String nif) {
        Optional<Clients> clientOpt = dao.findByNif(nif);
        return clientOpt.orElse(null);
    }

    /**
     * Verifica se um NIF já existe (para validação de formulários).
     */
    public boolean nifExists(String nif) {
        if (nif == null || nif.trim().isEmpty()) {
            return false;
        }
        return dao.findByNif(nif).isPresent();
    }

    /**
     * Busca clientes ativos (status = 1).
     */
    public List<Clients> getActiveClients() {
        try {
            List<Clients> allClients = dao.findAll();
            return allClients.stream()
                    .filter(client -> client.getStatus() == 1)
                    .toList();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar clientes ativos: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Ativa/desativa um cliente.
     */
    public boolean toggleClientStatus(Integer id) {
        try {
            Optional<Clients> clientOpt = dao.findById(id);
            if (clientOpt.isPresent()) {
                Clients client = clientOpt.get();
                client.setStatus(client.getStatus() == 1 ? 0 : 1);
                dao.update(client);
                System.out.println("✅ Status do cliente atualizado: " + client.getName());
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("❌ Erro ao alterar status do cliente: " + e.getMessage());
            return false;
        }
    }
}
