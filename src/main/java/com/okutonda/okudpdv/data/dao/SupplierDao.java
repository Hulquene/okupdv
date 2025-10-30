package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.entities.Supplier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO responsável pela gestão de Fornecedores (Suppliers).
 *
 * Compatível com BaseDao + HikariCP. Fornece CRUD completo e consultas
 * personalizadas (por NIF, nome, etc).
 *
 * @author Hulquene
 */
public class SupplierDao extends BaseDao<Supplier> {

    public SupplierDao() {
        super(); // Usa o pool de conexões via DatabaseProvider
    }

    public SupplierDao(java.sql.Connection externalConn) {
        super(externalConn); // Suporte a transações externas
    }

    // ==========================================================
    // 🔹 Mapeamento ResultSet → Entidade
    // ==========================================================
    private Supplier map(ResultSet rs) {
        try {
            Supplier s = new Supplier();
            s.setId(rs.getInt("id"));
            s.setName(rs.getString("company"));
            s.setNif(rs.getString("nif"));
            s.setPhone(rs.getString("phone"));
            s.setEmail(rs.getString("email"));
            s.setAddress(rs.getString("address"));
            s.setCity(rs.getString("city"));
            s.setZipCode(rs.getString("zip_code"));
            s.setGroupId(rs.getInt("group_id"));
            s.setStatus(rs.getInt("status"));
            s.setIsDefault(rs.getInt("isdefault"));
            return s;
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao mapear Supplier: " + e.getMessage());
            return null;
        }
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    @Override
    public boolean add(Supplier s) {
        String sql = """
            INSERT INTO suppliers (company, nif, phone, email, address, city, zip_code, group_id, status, isdefault)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        return executeUpdate(sql,
                s.getName(),
                s.getNif(),
                s.getPhone(),
                s.getEmail(),
                s.getAddress(),
                s.getCity(),
                s.getZipCode(),
                s.getGroupId(),
                s.getStatus(),
                s.getIsDefault());
    }

    @Override
    public boolean update(Supplier s) {
        String sql = """
            UPDATE suppliers
               SET company=?, nif=?, phone=?, email=?, address=?, city=?, zip_code=?, group_id=?, status=?, isdefault=?
             WHERE id=?
        """;
        return executeUpdate(sql,
                s.getName(),
                s.getNif(),
                s.getPhone(),
                s.getEmail(),
                s.getAddress(),
                s.getCity(),
                s.getZipCode(),
                s.getGroupId(),
                s.getStatus(),
                s.getIsDefault(),
                s.getId());
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM suppliers WHERE id=?", id);
    }

    @Override
    public Supplier findById(int id) {
        String sql = "SELECT * FROM suppliers WHERE id=?";
        return findOne(sql, this::map, id);
    }

    @Override
    public List<Supplier> findAll() {
        String sql = "SELECT * FROM suppliers ORDER BY company ASC";
        return executeQuery(sql, this::map);
    }

    // ==========================================================
    // 🔹 Consultas adicionais
    // ==========================================================
    public Supplier findByName(String name) {
        String sql = "SELECT * FROM suppliers WHERE company = ?";
        return findOne(sql, this::map, name);
    }

    public Supplier findByNif(String nif) {
        String sql = "SELECT * FROM suppliers WHERE nif = ?";
        return findOne(sql, this::map, nif);
    }

    public List<Supplier> filter(String text) {
        String like = "%" + text + "%";
        String sql = """
            SELECT * FROM suppliers
             WHERE company LIKE ? OR nif LIKE ? OR city LIKE ? OR email LIKE ?
          ORDER BY company ASC
        """;
        return executeQuery(sql, this::map, like, like, like, like);
    }

    public boolean existsByNif(String nif) {
        String sql = "SELECT COUNT(*) FROM suppliers WHERE nif = ?";
        int count = executeScalarInt(sql, nif);
        return count > 0;
    }

    public List<Supplier> findActive() {
        String sql = "SELECT * FROM suppliers WHERE status = 1 ORDER BY company ASC";
        return executeQuery(sql, this::map);
    }
}
