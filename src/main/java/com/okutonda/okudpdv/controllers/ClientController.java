/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.ClientDao;
import com.okutonda.okudpdv.data.entities.Clients;
import java.util.List;

/**
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
     * Cria ou atualiza cliente (id = 0 → cria, id > 0 → atualiza).
     */
    public Clients save(Clients client, int id) {
        boolean status;

        if (id == 0) {
            status = dao.add(client);
        } else {
            client.setId(id);
            status = dao.update(client);
        }

        if (status) {
            return dao.findByNif(client.getNif());
        }
        return null;
    }

    /**
     * Exclui cliente pelo ID.
     */
    public boolean deleteById(int id) {
        return dao.delete(id);
    }

    /**
     * Busca cliente pelo ID.
     */
    public Clients getById(int id) {
        Clients cliente = dao.findById(id);
        dao.close();
        return cliente;
    }

    /**
     * Lista todos os clientes.
     */
    public List<Clients> getAll() {
        List<Clients> lista = dao.findAll();
        dao.close();
        return lista;
    }

    /**
     * Filtra clientes por nome, NIF ou cidade.
     */
    public List<Clients> filter(String text) {
        return dao.filter(text);
    }

    /**
     * Retorna o cliente padrão (isdefault = 1).
     */
    public Clients getDefaultClient() {
        return dao.getDefaultClient();
    }

    /**
     * Busca cliente pelo nome exato.
     */
    public Clients getByName(String name) {
        return dao.findByName(name);
    }

    /**
     * Busca cliente pelo NIF.
     */
    public Clients getByNif(String nif) {
        return dao.findByNif(nif);
    }
}
