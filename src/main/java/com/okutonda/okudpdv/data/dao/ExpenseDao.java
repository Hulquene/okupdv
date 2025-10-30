package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.entities.Expense;
import com.okutonda.okudpdv.data.entities.Supplier;
import com.okutonda.okudpdv.data.entities.User;

import java.sql.*;
import java.util.List;

/**
 * DAO responsável pela gestão das despesas.
 * 
 * Compatível com o padrão BaseDao e HikariCP (DatabaseProvider).
 * Todas as conexões são automaticamente devolvidas ao pool.
 * 
 * @author Hulquene
 */
public class ExpenseDao extends BaseDao<Expense> {

    public ExpenseDao() {
        super(); // usa conexão automática via pool
    }

    public ExpenseDao(Connection externalConn) {
        super(externalConn); // para uso em transações externas
    }

    // ==========================================================
    // 🔹 Mapeamento ResultSet → Entidade Expense
    // ==========================================================
    private Expense map(ResultSet rs) {
        try {
            Expense e = new Expense();
            e.setId(rs.getInt("id"));
            e.setDescription(rs.getString("description"));
            e.setTotal(rs.getBigDecimal("total"));
            e.setPrefix(rs.getString("prefix"));
            e.setNumber(rs.getInt("number"));
            e.setDate(rs.getString("date"));
            e.setMode(rs.getString("mode"));
            e.setReference(rs.getString("reference"));
            e.setNotes(rs.getString("notes"));
            e.setCurrency(rs.getString("currency"));

            // 🔹 Relacionamentos
            int supplierId = rs.getInt("supplier_id");
            if (supplierId > 0) {
                Supplier s = new Supplier();
                s.setId(supplierId);
                s.setName(rs.getString("supplier_name"));
                e.setSupplier(s);
            }

            int userId = rs.getInt("user_id");
            if (userId > 0) {
                User u = new User();
                u.setId(userId);
                u.setName(rs.getString("user_name"));
                e.setUser(u);
            }

            return e;
        } catch (SQLException ex) {
            System.err.println("[DB] Erro ao mapear Expense: " + ex.getMessage());
            return null;
        }
    }

    // ==========================================================
    // 🔹 CRUD BÁSICO
    // ==========================================================
    @Override
    public boolean add(Expense e) {
        String sql = """
            INSERT INTO expenses
            (description, total, prefix, number, date, mode, reference, notes, currency, supplier_id, user_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        return executeUpdate(sql,
                e.getDescription(),
                e.getTotal(),
                e.getPrefix(),
                e.getNumber(),
                e.getDate(),
                e.getMode(),
                e.getReference(),
                e.getNotes(),
                e.getCurrency(),
                e.getSupplier() != null ? e.getSupplier().getId() : 0,
                e.getUser() != null ? e.getUser().getId() : 0
        );
    }

    @Override
    public boolean update(Expense e) {
        String sql = """
            UPDATE expenses
               SET description=?, total=?, mode=?, reference=?, notes=?, currency=?, supplier_id=?, user_id=?
             WHERE id=?
        """;
        return executeUpdate(sql,
                e.getDescription(),
                e.getTotal(),
                e.getMode(),
                e.getReference(),
                e.getNotes(),
                e.getCurrency(),
                e.getSupplier() != null ? e.getSupplier().getId() : 0,
                e.getUser() != null ? e.getUser().getId() : 0,
                e.getId()
        );
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM expenses WHERE id=?", id);
    }

    @Override
    public Expense findById(int id) {
        String sql = """
            SELECT e.*, 
                   s.id AS supplier_id, s.name AS supplier_name,
                   u.id AS user_id, u.name AS user_name
              FROM expenses e
         LEFT JOIN suppliers s ON e.supplier_id = s.id
         LEFT JOIN users u ON e.user_id = u.id
             WHERE e.id=?
        """;
        return findOne(sql, this::map, id);
    }

    @Override
    public List<Expense> findAll() {
        String sql = """
            SELECT e.*, 
                   s.id AS supplier_id, s.name AS supplier_name,
                   u.id AS user_id, u.name AS user_name
              FROM expenses e
         LEFT JOIN suppliers s ON e.supplier_id = s.id
         LEFT JOIN users u ON e.user_id = u.id
          ORDER BY e.date DESC
        """;
        return executeQuery(sql, this::map);
    }

    // ==========================================================
    // 🔹 CONSULTAS PERSONALIZADAS
    // ==========================================================
    /**
     * Lista despesas entre duas datas específicas.
     */
    public List<Expense> listDespesas(String dateFrom, String dateTo) {
        String sql = """
            SELECT e.*, 
                   s.id AS supplier_id, s.name AS supplier_name,
                   u.id AS user_id, u.name AS user_name
              FROM expenses e
         LEFT JOIN suppliers s ON e.supplier_id = s.id
         LEFT JOIN users u ON e.user_id = u.id
             WHERE DATE(e.date) BETWEEN ? AND ?
          ORDER BY e.date ASC
        """;
        return executeQuery(sql, this::map, dateFrom, dateTo);
    }

    /**
     * Busca despesas com filtro textual (descrição ou referência).
     */
    public List<Expense> filter(String text) {
        String like = "%" + text + "%";
        String sql = """
            SELECT e.*, 
                   s.id AS supplier_id, s.name AS supplier_name,
                   u.id AS user_id, u.name AS user_name
              FROM expenses e
         LEFT JOIN suppliers s ON e.supplier_id = s.id
         LEFT JOIN users u ON e.user_id = u.id
             WHERE e.description LIKE ? OR e.reference LIKE ?
          ORDER BY e.date DESC
        """;
        return executeQuery(sql, this::map, like, like);
    }
}
