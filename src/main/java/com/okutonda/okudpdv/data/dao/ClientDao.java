package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.entities.Clients;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO para gestão de Clientes.
 *
 * Herda a estrutura genérica de BaseDao, usando o pool de conexões via
 * DatabaseProvider.
 *
 * @author …
 */
public class ClientDao extends BaseDao<Clients> {

    // 🔹 Mapeia o resultado SQL → objeto Clients
    private Clients map(ResultSet rs) {
        try {
            Clients obj = new Clients();
            obj.setId(rs.getInt("id"));
            obj.setNif(rs.getString("nif"));
            obj.setName(rs.getString("company"));
            obj.setEmail(rs.getString("email"));
            obj.setPhone(rs.getString("phone"));
            obj.setAddress(rs.getString("address"));
            obj.setCity(rs.getString("city"));
            obj.setState(rs.getString("state"));
            obj.setZipCode(rs.getString("zip_code"));
            obj.setStatus(rs.getInt("status"));
            obj.setIsDefault(rs.getInt("isdefault"));
            return obj;
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao mapear cliente: " + e.getMessage());
            return null;
        }
    }

    // ====================================
    // 🔹 Implementações CRUD (GenericDao)
    // ====================================
    @Override
    public boolean add(Clients obj) {
        String sql = """
            INSERT INTO clients (company,nif,phone,email,address,zip_code,status,isdefault,city,state)
            VALUES (?,?,?,?,?,?,?,?,?,?)
        """;
        return executeUpdate(sql,
                obj.getName(), obj.getNif(), obj.getPhone(), obj.getEmail(), obj.getAddress(),
                obj.getZipCode(), obj.getStatus(), obj.getIsDefault(), obj.getCity(), obj.getState());
    }

    @Override
    public boolean update(Clients obj) {
        String sql = """
            UPDATE clients SET company=?,nif=?,phone=?,email=?,address=?,city=?,state=?,zip_code=?,status=?,isdefault=? 
            WHERE id=?
        """;
        return executeUpdate(sql,
                obj.getName(), obj.getNif(), obj.getPhone(), obj.getEmail(),
                obj.getAddress(), obj.getCity(), obj.getState(),
                obj.getZipCode(), obj.getStatus(), obj.getIsDefault(), obj.getId());
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM clients WHERE id=?", id);
    }

    @Override
    public Clients findById(int id) {
        return findOne("SELECT * FROM clients WHERE id=?", this::map, id);
    }

    @Override
    public List<Clients> findAll() {
        return executeQuery("SELECT * FROM clients", this::map);
    }

    // ====================================
    // 🔹 Métodos específicos do módulo
    // ====================================
    /**
     * Busca cliente por nome exato
     */
    public Clients findByName(String name) {
        return findOne("SELECT * FROM clients WHERE company=?", this::map, name);
    }

    /**
     * Busca cliente por NIF
     */
    public Clients findByNif(String nif) {
        return findOne("SELECT * FROM clients WHERE nif=?", this::map, nif);
    }

    /**
     * Retorna o cliente marcado como padrão (isdefault = 1)
     */
    public Clients getDefaultClient() {
        return findOne("SELECT * FROM clients WHERE isdefault=1", this::map);
    }

    /**
     * Filtra clientes por nome, NIF ou cidade
     */
    public List<Clients> filter(String txt) {
        String like = "%" + txt + "%";
        String sql = "SELECT * FROM clients WHERE company LIKE ? OR nif LIKE ? OR city LIKE ?";
        return executeQuery(sql, this::map, like, like, like);
    }
}
