/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.models.Product;
import com.okutonda.okudpdv.models.StockMovement;
import com.okutonda.okudpdv.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hr
 */
public class StockMovementDao {

    private final Connection conn;

    public StockMovementDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public StockMovementDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Regista um movimento de stock (entrada/saída/ajuste).
     */
    public boolean add(StockMovement movimento) {
        try {
            String sql = """
            INSERT INTO stock_movements
            (product_id, warehouse_id, user_id, quantity, type, origin, reference_id, notes, reason)
            VALUES (?,?,?,?,?,?,?,?,?)
        """;
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, movimento.getProduct().getId());
            pst.setInt(2, movimento.getWarehouse() != null ? movimento.getWarehouse().getId() : 1); // default 1
            pst.setInt(3, movimento.getUser().getId());
            pst.setInt(4, movimento.getQuantity());
            pst.setString(5, movimento.getType());
            pst.setString(6, movimento.getOrigin() != null ? movimento.getOrigin() : "MANUAL");
            if (movimento.getReferenceId() != null) {
                pst.setInt(7, movimento.getReferenceId());
            } else {
                pst.setNull(7, java.sql.Types.INTEGER);
            }
            pst.setString(8, movimento.getNotes() != null ? movimento.getNotes() : "");
            pst.setString(9, movimento.getReason() != null ? movimento.getReason() : "");

            pst.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao registar movimento de stock: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lista todos os movimentos de um produto.
     */
    public List<StockMovement> listByProduct(int productId) {
        List<StockMovement> list = new ArrayList<>();
        String sql = """
            SELECT sm.*, u.name AS user_name, p.description AS product_name
            FROM stock_movements sm
            LEFT JOIN users u ON sm.user_id = u.id
            LEFT JOIN products p ON sm.product_id = p.id
            WHERE sm.product_id=?
            ORDER BY sm.created_at DESC
        """;

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, productId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                StockMovement m = new StockMovement();
                Product prod = new Product();
                prod.setId(rs.getInt("product_id"));
                prod.setDescription(rs.getString("product_name"));

                User u = new User();
                u.setId(rs.getInt("user_id"));
                u.setName(rs.getString("user_name"));

                m.setId(rs.getInt("id"));
                m.setProduct(prod);
                m.setQuantity(rs.getInt("quantity"));
                m.setType(rs.getString("type"));
                m.setReason(rs.getString("reason"));
                m.setUser(u);
                m.setCreatedAt(rs.getTimestamp("created_at"));

                list.add(m);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar movimentos de stock: " + e.getMessage());
        }
        return list;
    }

    /**
     * Lista todos os movimentos de stock (auditoria completa).
     */
    public List<StockMovement> listAll() {
        List<StockMovement> list = new ArrayList<>();
        String sql = """
            SELECT sm.*, u.name AS user_name, p.description AS product_name
            FROM stock_movements sm
            LEFT JOIN users u ON sm.user_id = u.id
            LEFT JOIN products p ON sm.product_id = p.id
            ORDER BY sm.created_at DESC
        """;

        try (PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                StockMovement m = new StockMovement();
                Product prod = new Product();
                prod.setId(rs.getInt("product_id"));
                prod.setDescription(rs.getString("product_name"));

                User u = new User();
                u.setId(rs.getInt("user_id"));
                u.setName(rs.getString("user_name"));

                m.setId(rs.getInt("id"));
                m.setProduct(prod);
                m.setQuantity(rs.getInt("quantity"));
                m.setType(rs.getString("type"));
                m.setReason(rs.getString("reason"));
                m.setUser(u);
                m.setCreatedAt(rs.getTimestamp("created_at"));

                list.add(m);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar movimentos de stock: " + e.getMessage());
        }
        return list;
    }

    /**
     * Calcula o stock atual de um produto.
     */
    public int getStockAtual(int productId) {
        String sql = "SELECT IFNULL(SUM(quantity),0) AS stock_atual FROM stock_movements WHERE product_id=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, productId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("stock_atual");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao calcular stock atual: " + e.getMessage());
        }
        return 0;
    }
}
