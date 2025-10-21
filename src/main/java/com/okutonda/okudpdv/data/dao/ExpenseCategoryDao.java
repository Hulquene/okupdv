/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.data.entities.ExpenseCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rog
 */
public class ExpenseCategoryDao {

    private final Connection conn;

    public ExpenseCategoryDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public void insert(ExpenseCategory category) {
        String sql = "INSERT INTO expense_categories (name, description) VALUES (?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, category.getName());
            pst.setString(2, category.getDescription());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao inserir categoria: " + e.getMessage());
        }
    }

    public void update(ExpenseCategory category) {
        String sql = "UPDATE expense_categories SET name=?, description=? WHERE id=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, category.getName());
            pst.setString(2, category.getDescription());
            pst.setInt(3, category.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar categoria: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM expense_categories WHERE id=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao apagar categoria: " + e.getMessage());
        }
    }

    public List<ExpenseCategory> getAll() {
        List<ExpenseCategory> list = new ArrayList<>();
        String sql = "SELECT * FROM expense_categories ORDER BY name ASC";
        try (PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                ExpenseCategory c = new ExpenseCategory();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
                list.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar categorias: " + e.getMessage());
        }
        return list;
    }
}
