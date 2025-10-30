package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.entities.Taxes;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO responsável pela gestão dos impostos (Taxes).
 *
 * Compatível com BaseDao + HikariCP. Oferece operações CRUD e consultas por
 * código, nome e percentagem.
 *
 * @author Hulquene
 */
public class TaxeDao extends BaseDao<Taxes> {

    public TaxeDao() {
        super(); // usa o pool de conexões
    }

    public TaxeDao(java.sql.Connection externalConn) {
        super(externalConn); // permite uso em transações
    }

    // ==========================================================
    // 🔹 Mapeamento ResultSet → Entidade
    // ==========================================================
    private Taxes map(ResultSet rs) {
        try {
            Taxes t = new Taxes();
            t.setId(rs.getInt("id"));
            t.setCode(rs.getString("code"));
            t.setName(rs.getString("name"));
            t.setPercetage(rs.getBigDecimal("percentage"));
            t.setIsDefault(rs.getInt("isdefault"));
            return t;
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao mapear Taxes: " + e.getMessage());
            return null;
        }
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    @Override
    public boolean add(Taxes obj) {
        String sql = """
            INSERT INTO taxes (name, code, percentage, isdefault)
            VALUES (?, ?, ?, ?)
        """;
        return executeUpdate(sql,
                obj.getName(),
                obj.getCode(),
                obj.getPercetage(),
                obj.getIsDefault());
    }

    @Override
    public boolean update(Taxes obj) {
        String sql = """
            UPDATE taxes
               SET name=?, code=?, percentage=?, isdefault=?
             WHERE id=?
        """;
        return executeUpdate(sql,
                obj.getName(),
                obj.getCode(),
                obj.getPercetage(),
                obj.getIsDefault(),
                obj.getId());
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM taxes WHERE id=?", id);
    }

    @Override
    public Taxes findById(int id) {
        String sql = "SELECT * FROM taxes WHERE id=?";
        return findOne(sql, this::map, id);
    }

    @Override
    public List<Taxes> findAll() {
        String sql = "SELECT * FROM taxes ORDER BY name ASC";
        return executeQuery(sql, this::map);
    }

    // ==========================================================
    // 🔹 Consultas personalizadas
    // ==========================================================
    public Taxes findByCode(String code) {
        String sql = "SELECT * FROM taxes WHERE code=?";
        return findOne(sql, this::map, code);
    }

    public List<Taxes> filter(String text) {
        String like = "%" + text + "%";
        String sql = """
            SELECT * FROM taxes
             WHERE name LIKE ? OR code LIKE ? OR percentage LIKE ?
          ORDER BY name ASC
        """;
        return executeQuery(sql, this::map, like, like, like);
    }

    public Taxes findDefaultTax() {
        String sql = "SELECT * FROM taxes WHERE isdefault = 1 LIMIT 1";
        return findOne(sql, this::map);
    }
}
