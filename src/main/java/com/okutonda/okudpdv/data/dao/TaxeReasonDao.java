package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.entities.ReasonTaxes;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO responsável pelas razões de isenção e códigos fiscais (ReasonTaxes).
 *
 * Compatível com BaseDao e HikariCP (DatabaseProvider). Oferece consultas por
 * ID, código e listagem geral.
 *
 * @author Hulquene
 */
public class TaxeReasonDao extends BaseDao<ReasonTaxes> {

    public TaxeReasonDao() {
        super(); // Usa o pool de conexões (DatabaseProvider)
    }

    public TaxeReasonDao(java.sql.Connection externalConn) {
        super(externalConn); // Permite uso transacional externo
    }

    // ==========================================================
    // 🔹 Mapeamento ResultSet → Entidade
    // ==========================================================
    private ReasonTaxes map(ResultSet rs) {
        try {
            ReasonTaxes obj = new ReasonTaxes();
            obj.setId(rs.getInt("id"));
            obj.setCode(rs.getString("code"));
            obj.setDescription(rs.getString("description"));
            obj.setReason(rs.getString("reason"));
            obj.setStandard(rs.getString("standard"));
            return obj;
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao mapear ReasonTaxes: " + e.getMessage());
            return null;
        }
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    @Override
    public boolean add(ReasonTaxes obj) {
        String sql = """
            INSERT INTO reason_taxes (code, description, reason, standard)
            VALUES (?, ?, ?, ?)
        """;
        return executeUpdate(sql, obj.getCode(), obj.getDescription(), obj.getReason(), obj.getStandard());
    }

    @Override
    public boolean update(ReasonTaxes obj) {
        String sql = """
            UPDATE reason_taxes
               SET code=?, description=?, reason=?, standard=?
             WHERE id=?
        """;
        return executeUpdate(sql,
                obj.getCode(),
                obj.getDescription(),
                obj.getReason(),
                obj.getStandard(),
                obj.getId());
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM reason_taxes WHERE id=?", id);
    }

    @Override
    public ReasonTaxes findById(int id) {
        String sql = "SELECT * FROM reason_taxes WHERE id=?";
        return findOne(sql, this::map, id);
    }

    @Override
    public List<ReasonTaxes> findAll() {
        String sql = "SELECT * FROM reason_taxes ORDER BY code ASC";
        return executeQuery(sql, this::map);
    }

    // ==========================================================
    // 🔹 Consultas personalizadas
    // ==========================================================
    /**
     * Busca uma razão fiscal pelo código (ex: M02, M04, etc.)
     */
    public ReasonTaxes searchFromCode(String code) {
        String sql = "SELECT * FROM reason_taxes WHERE code=?";
        return findOne(sql, this::map, code);
    }

    /**
     * Busca razões fiscais que contenham o texto informado.
     */
    public List<ReasonTaxes> searchByDescription(String text) {
        String like = "%" + text + "%";
        String sql = "SELECT * FROM reason_taxes WHERE description LIKE ? OR reason LIKE ? ORDER BY code ASC";
        return executeQuery(sql, this::map, like, like);
    }
}
