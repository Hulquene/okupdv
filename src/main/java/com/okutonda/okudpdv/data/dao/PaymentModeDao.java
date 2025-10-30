package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.entities.PaymentModes;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO responsável pela gestão dos modos de pagamento.
 *
 * Segue o padrão BaseDao<PaymentModes> e usa o DatabaseProvider.
 *
 * @author …
 */
public class PaymentModeDao extends BaseDao<PaymentModes> {
    // ✅ Construtor padrão (usa conexão do pool automaticamente)

    public PaymentModeDao() {
        // não precisa chamar super(), ele já existe por padrão
    }

    // ✅ Construtor alternativo (usa conexão externa — transação)
    public PaymentModeDao(java.sql.Connection externalConn) {
        super(externalConn);
    }

    // ==========================================================
    // 🔹 Mapeamento SQL → Objeto
    // ==========================================================
    private PaymentModes map(ResultSet rs) {
        try {
            PaymentModes obj = new PaymentModes();
            obj.setId(rs.getInt("id"));
            obj.setName(rs.getString("name"));
            obj.setDescription(rs.getString("description"));
            obj.setCode(rs.getString("code"));
            obj.setStatus(rs.getInt("status"));
            obj.setIsDefault(rs.getInt("isDefault"));
            return obj;
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao mapear PaymentModes: " + e.getMessage());
            return null;
        }
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    @Override
    public boolean add(PaymentModes obj) {
        String sql = """
            INSERT INTO payment_modes (name, description, code, status, isDefault)
            VALUES (?, ?, ?, ?, ?)
        """;
        return executeUpdate(sql, obj.getName(), obj.getDescription(), obj.getCode(),
                obj.getStatus(), obj.getIsDefault());
    }

    @Override
    public boolean update(PaymentModes obj) {
        String sql = """
            UPDATE payment_modes
            SET name=?, description=?, code=?, status=?, isDefault=?
            WHERE id=?
        """;
        return executeUpdate(sql, obj.getName(), obj.getDescription(),
                obj.getCode(), obj.getStatus(), obj.getIsDefault(), obj.getId());
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM payment_modes WHERE id=?", id);
    }

    @Override
    public PaymentModes findById(int id) {
        return findOne("SELECT * FROM payment_modes WHERE id=?", this::map, id);
    }

    @Override
    public List<PaymentModes> findAll() {
        return executeQuery("SELECT * FROM payment_modes", this::map);
    }

    // ==========================================================
    // 🔹 MÉTODOS ESPECÍFICOS
    // ==========================================================
    /**
     * Busca modo de pagamento pelo nome
     */
    public PaymentModes findByName(String name) {
        return findOne("SELECT * FROM payment_modes WHERE name=?", this::map, name);
    }

    /**
     * Busca modo de pagamento pelo código
     */
    public PaymentModes findByCode(String code) {
        return findOne("SELECT * FROM payment_modes WHERE code=?", this::map, code);
    }

    /**
     * Retorna o modo de pagamento padrão (isDefault = 1)
     */
    public PaymentModes findDefault() {
        return findOne("SELECT * FROM payment_modes WHERE isDefault=1", this::map);
    }

    /**
     * Filtra modos de pagamento por nome, código ou descrição
     */
    public List<PaymentModes> filter(String txt) {
        String like = "%" + txt + "%";
        String sql = """
            SELECT * FROM payment_modes
            WHERE name LIKE ? OR code LIKE ? OR description LIKE ?
        """;
        return executeQuery(sql, this::map, like, like, like);
    }
}
