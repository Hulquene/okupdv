//package com.okutonda.okudpdv.data.dao;
//
//import com.okutonda.okudpdv.data.entities.ExpenseCategory;
//import java.sql.*;
//import java.util.List;
//
///**
// * DAO responsável pelas categorias de despesas.
// *
// * Totalmente compatível com BaseDao e HikariCP (DatabaseProvider). Fecha
// * automaticamente conexões após uso.
// *
// * @author Hulquene
// */
//public class ExpenseCategoryDao extends BaseDao<ExpenseCategory> {
//
//    public ExpenseCategoryDao() {
//        super(); // usa o pool de conexões automaticamente
//    }
//
//    public ExpenseCategoryDao(Connection externalConn) {
//        super(externalConn); // permite uso transacional externo
//    }
//
//    // ==========================================================
//    // 🔹 Mapeamento ResultSet → Entidade
//    // ==========================================================
//    private ExpenseCategory map(ResultSet rs) {
//        try {
//            ExpenseCategory c = new ExpenseCategory();
//            c.setId(rs.getInt("id"));
//            c.setName(rs.getString("name"));
//            c.setDescription(rs.getString("description"));
//            return c;
//        } catch (SQLException e) {
//            System.err.println("[DB] Erro ao mapear ExpenseCategory: " + e.getMessage());
//            return null;
//        }
//    }
//
//    // ==========================================================
//    // 🔹 CRUD
//    // ==========================================================
//    @Override
//    public boolean add(ExpenseCategory category) {
//        String sql = "INSERT INTO expense_categories (name, description) VALUES (?, ?)";
//        return executeUpdate(sql, category.getName(), category.getDescription());
//    }
//
//    @Override
//    public boolean update(ExpenseCategory category) {
//        String sql = "UPDATE expense_categories SET name=?, description=? WHERE id=?";
//        return executeUpdate(sql, category.getName(), category.getDescription(), category.getId());
//    }
//
//    @Override
//    public boolean delete(int id) {
//        String sql = "DELETE FROM expense_categories WHERE id=?";
//        return executeUpdate(sql, id);
//    }
//
//    @Override
//    public ExpenseCategory findById(int id) {
//        String sql = "SELECT * FROM expense_categories WHERE id=?";
//        return findOne(sql, this::map, id);
//    }
//
//    @Override
//    public List<ExpenseCategory> findAll() {
//        String sql = "SELECT * FROM expense_categories ORDER BY name ASC";
//        return executeQuery(sql, this::map);
//    }
//
//    // ==========================================================
//    // 🔹 Consultas adicionais
//    // ==========================================================
//    public List<ExpenseCategory> filterByName(String namePart) {
//        String sql = "SELECT * FROM expense_categories WHERE name LIKE ? ORDER BY name ASC";
//        return executeQuery(sql, this::map, "%" + namePart + "%");
//    }
//}
