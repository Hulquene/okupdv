package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.entities.GroupsProduct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO responsável pela gestão dos grupos de produtos.
 * 
 * Herda BaseDao<GroupsProduct> e usa o pool de conexões.
 * 
 * @author …
 */
public class GroupsProductDao extends BaseDao<GroupsProduct> {
// ✅ Construtor padrão (usa conexão do pool automaticamente)

    public GroupsProductDao() {
        // não precisa chamar super(), ele já existe por padrão
    }

    // ✅ Construtor alternativo (usa conexão externa — transação)
    public GroupsProductDao(java.sql.Connection externalConn) {
        super(externalConn);
    }
    // 🔹 Função de mapeamento SQL → objeto
    private GroupsProduct map(ResultSet rs) {
        try {
            GroupsProduct obj = new GroupsProduct();
            obj.setId(rs.getInt("id"));
            obj.setCode(rs.getString("code"));
            obj.setName(rs.getString("name"));
            return obj;
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao mapear GroupsProduct: " + e.getMessage());
            return null;
        }
    }

    // ========================
    // 🔹 CRUD (GenericDao)
    // ========================

    @Override
    public boolean add(GroupsProduct obj) {
        String sql = "INSERT INTO groups_product (name, code) VALUES (?, ?)";
        return executeUpdate(sql, obj.getName(), obj.getCode());
    }

    @Override
    public boolean update(GroupsProduct obj) {
        String sql = "UPDATE groups_product SET name=?, code=? WHERE id=?";
        return executeUpdate(sql, obj.getName(), obj.getCode(), obj.getId());
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM groups_product WHERE id=?", id);
    }

    @Override
    public GroupsProduct findById(int id) {
        return findOne("SELECT * FROM groups_product WHERE id=?", this::map, id);
    }

    @Override
    public List<GroupsProduct> findAll() {
        return executeQuery("SELECT * FROM groups_product", this::map);
    }

    // ========================
    // 🔹 Métodos específicos
    // ========================

    /** Busca grupo pelo código */
    public GroupsProduct findByCode(String code) {
        return findOne("SELECT * FROM groups_product WHERE code=?", this::map, code);
    }

    /** Filtra grupos pelo nome ou código */
    public List<GroupsProduct> filter(String txt) {
        String like = "%" + txt + "%";
        String sql = "SELECT * FROM groups_product WHERE name LIKE ? OR code LIKE ?";
        return executeQuery(sql, this::map, like, like);
    }
}
