/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.models.Expense;
import com.okutonda.okudpdv.models.Supplier;
import com.okutonda.okudpdv.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rog
 */
public class ExpenseDao {

    private final Connection conn;

    public ExpenseDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public List<Expense> listDespesas(String dateFrom, String dateTo) {
        List<Expense> list = new ArrayList<>();
        String sql = """
            SELECT e.id,
                   e.description,
                   e.total,
                   e.prefix,
                   e.number,
                   e.date,
                   e.mode,
                   e.reference,
                   e.notes,
                   e.currency,
                   s.id   AS supplier_id,
                   s.name AS supplier_name,
                   u.id   AS user_id,
                   u.name AS user_name
            FROM expenses e
            LEFT JOIN suppliers s ON e.supplier_id = s.id
            LEFT JOIN users u     ON e.user_id = u.id
            WHERE DATE(e.date) BETWEEN ? AND ?
            ORDER BY e.date ASC
        """;

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, dateFrom);
            pst.setString(2, dateTo);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Expense obj = new Expense();
                    obj.setId(rs.getInt("id"));
                    obj.setDescription(rs.getString("description"));
                    obj.setTotal(rs.getBigDecimal("total"));
                    obj.setPrefix(rs.getString("prefix"));
                    obj.setNumber(rs.getInt("number"));
                    obj.setDate(rs.getString("date"));
                    obj.setMode(rs.getString("mode"));
                    obj.setReference(rs.getString("reference"));
                    obj.setNotes(rs.getString("notes"));
                    obj.setCurrency(rs.getString("currency"));

                    // Relacionamentos
                    if (rs.getInt("supplier_id") > 0) {
                        Supplier s = new Supplier();
                        s.setId(rs.getInt("supplier_id"));
                        s.setName(rs.getString("supplier_name"));
                        obj.setSupplier(s);
                    }
                    if (rs.getInt("user_id") > 0) {
                        User u = new User();
                        u.setId(rs.getInt("user_id"));
                        u.setName(rs.getString("user_name"));
                        obj.setUser(u);
                    }

                    list.add(obj);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar despesas: " + e.getMessage());
        }
        return list;
    }
}
