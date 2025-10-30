package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.entities.Warehouse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO responsável pela gestão de Armazéns (Warehouses).
 *
 * Compatível com BaseDao + HikariCP. Fornece métodos de CRUD e consultas
 * filtradas.
 *
 * @author Hulquene
 */
public class WarehouseDao extends BaseDao<Warehouse> {

    public WarehouseDao() {
        super(); // utiliza pool de conexões via DatabaseProvider
    }

    public WarehouseDao(java.sql.Connection externalConn) {
        super(externalConn); // permite uso transacional
    }

    // ==========================================================
    // 🔹 Mapeamento ResultSet → Entidade
    // ==========================================================
    private Warehouse map(ResultSet rs) {
        try {
            Warehouse w = new Warehouse();
            w.setId(rs.getInt("id"));
            w.setName(rs.getString("name"));
            w.setLocation(rs.getString("location"));
            w.setDescription(rs.getString("description"));
            w.setStatus(rs.getInt("status"));
            w.setCreatedAt(rs.getTimestamp("created_at"));
            w.setUpdatedAt(rs.getTimestamp("updated_at"));
            return w;
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao mapear Warehouse: " + e.getMessage());
            return null;
        }
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    @Override
    public boolean add(Warehouse w) {
        String sql = """
            INSERT INTO warehouses (name, location, description, status)
            VALUES (?, ?, ?, ?)
        """;
        return executeUpdate(sql,
                w.getName(),
                w.getLocation(),
                w.getDescription(),
                w.getStatus());
    }

    @Override
    public boolean update(Warehouse w) {
        String sql = """
            UPDATE warehouses
               SET name=?, location=?, description=?, status=?
             WHERE id=?
        """;
        return executeUpdate(sql,
                w.getName(),
                w.getLocation(),
                w.getDescription(),
                w.getStatus(),
                w.getId());
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM warehouses WHERE id=?", id);
    }

    @Override
    public Warehouse findById(int id) {
        String sql = "SELECT * FROM warehouses WHERE id=?";
        return findOne(sql, this::map, id);
    }

    @Override
    public List<Warehouse> findAll() {
        String sql = "SELECT * FROM warehouses ORDER BY name ASC";
        return executeQuery(sql, this::map);
    }

    // ==========================================================
    // 🔹 Consultas adicionais
    // ==========================================================
    public List<Warehouse> filter(String txt) {
        String like = "%" + txt + "%";
        String sql = """
            SELECT * FROM warehouses
             WHERE name LIKE ? OR location LIKE ? OR description LIKE ?
          ORDER BY name ASC
        """;
        return executeQuery(sql, this::map, like, like, like);
    }

    public List<Warehouse> findActive() {
        String sql = "SELECT * FROM warehouses WHERE status = 1 ORDER BY name ASC";
        return executeQuery(sql, this::map);
    }

    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM warehouses WHERE name = ?";
        int count = executeScalarInt(sql, name);
        return count > 0;
    }
}
