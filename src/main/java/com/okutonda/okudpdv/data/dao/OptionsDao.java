package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.entities.Options;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO responsável por gerir as opções de configuração do sistema.
 *
 * Usa a infraestrutura genérica BaseDao + pool de conexões.
 *
 * @author …
 */
public class OptionsDao extends BaseDao<Options> {

    // 🔹 Mapeamento de ResultSet → objeto Options
    private Options map(ResultSet rs) {
        try {
            Options obj = new Options();
            obj.setId(rs.getInt("id"));
            obj.setName(rs.getString("name"));
            obj.setValue(rs.getString("value"));
            obj.setStatus(rs.getString("status"));
            return obj;
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao mapear Options: " + e.getMessage());
            return null;
        }
    }

    // ==================================
    // 🔹 Implementação CRUD padrão
    // ==================================
    @Override
    public boolean add(Options obj) {
        String sql = "INSERT INTO options (name, value, status) VALUES (?, ?, ?)";
        return executeUpdate(sql, obj.getName(), obj.getValue(), obj.getStatus());
    }

    @Override
    public boolean update(Options obj) {
        String sql = "UPDATE options SET value=?, status=? WHERE name=?";
        return executeUpdate(sql, obj.getValue(), obj.getStatus(), obj.getName());
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM options WHERE id=?", id);
    }

    @Override
    public Options findById(int id) {
        return findOne("SELECT * FROM options WHERE id=?", this::map, id);
    }

    @Override
    public List<Options> findAll() {
        return executeQuery("SELECT * FROM options", this::map);
    }

    // ==================================
    // 🔹 Métodos específicos do módulo
    // ==================================
    /**
     * Busca uma opção pelo nome
     */
    public Options findByName(String name) {
        return findOne("SELECT * FROM options WHERE name=?", this::map, name);
    }

    /**
     * Retorna o valor da opção (ou null se não existir)
     */
    public String findValueByName(String name) {
        Options opt = findByName(name);
        return (opt != null) ? opt.getValue() : null;
    }

    /**
     * Cria ou atualiza automaticamente (upsert)
     */
    public boolean saveOrUpdate(Options opt) {
        Options existing = findByName(opt.getName());
        if (existing != null) {
            return update(opt);
        } else {
            return add(opt);
        }
    }
}
