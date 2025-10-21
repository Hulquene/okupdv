/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.StockMovement;
import com.okutonda.okudpdv.data.entities.User;

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
        String sqlInsert = """
        INSERT INTO stock_movements
        (product_id, warehouse_id, user_id, quantity, type, origin, reference_id, notes, reason)
        VALUES (?,?,?,?,?,?,?,?,?)
    """;

        PreparedStatement pstInsert = null;

        try {
            // 🔒 Inicia transação manual
            conn.setAutoCommit(false);

            pstInsert = conn.prepareStatement(sqlInsert);
            pstInsert.setInt(1, movimento.getProduct().getId());
            pstInsert.setInt(2, movimento.getWarehouse() != null ? movimento.getWarehouse().getId() : 1);
            pstInsert.setInt(3, movimento.getUser().getId());
            pstInsert.setInt(4, movimento.getQuantity());
            pstInsert.setString(5, movimento.getType());
            pstInsert.setString(6, movimento.getOrigin() != null ? movimento.getOrigin() : "MANUAL");
            if (movimento.getReferenceId() != null) {
                pstInsert.setInt(7, movimento.getReferenceId());
            } else {
                pstInsert.setNull(7, java.sql.Types.INTEGER);
            }
            pstInsert.setString(8, movimento.getNotes() != null ? movimento.getNotes() : "");
            pstInsert.setString(9, movimento.getReason() != null ? movimento.getReason() : "");

            pstInsert.executeUpdate();

            // ✅ Se veio de uma compra, atualiza o item correspondente
            if ("COMPRA".equalsIgnoreCase(movimento.getOrigin())) {
                atualizarEntradaItemCompra(movimento);
            }

            // 💾 Tudo certo → confirma
            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Erro ao registar movimento de stock: " + e.getMessage());
            try {
                conn.rollback(); // ⛔ Reverte tudo
                System.err.println("⚠ Transação revertida devido a erro.");
            } catch (SQLException ex) {
                System.err.println("Erro ao reverter transação: " + ex.getMessage());
            }
        } finally {
            try {
                if (pstInsert != null) {
                    pstInsert.close();
                }
                conn.setAutoCommit(true); // 🔁 volta ao modo normal
            } catch (SQLException e) {
                System.err.println("Erro ao restaurar autocommit: " + e.getMessage());
            }
        }

        return false;
    }

    private void atualizarEntradaItemCompra(StockMovement movimento) {
        try {
            // 1️⃣ Obter a quantidade atual de entrada do item
            String sqlSelect = "SELECT quantidade, quantidade_entrada "
                    + "FROM purchase_items "
                    + "WHERE purchase_id = ? AND product_id = ?";
            PreparedStatement pst1 = conn.prepareStatement(sqlSelect);
            pst1.setInt(1, movimento.getReferenceId());
            pst1.setInt(2, movimento.getProduct().getId());
            ResultSet rs = pst1.executeQuery();

            if (rs.next()) {
                int quantidadeTotal = rs.getInt("quantidade");
                int quantidadeEntradaAtual = rs.getInt("quantidade_entrada");

                int novaQuantidadeEntrada = quantidadeEntradaAtual + movimento.getQuantity();

                String novoStatus;
                if (novaQuantidadeEntrada >= quantidadeTotal) {
                    novaQuantidadeEntrada = quantidadeTotal;
                    novoStatus = "completo";
                } else if (novaQuantidadeEntrada > 0) {
                    novoStatus = "parcial";
                } else {
                    novoStatus = "nao_iniciado";
                }

                // 2️⃣ Atualizar o item
                String sqlUpdate = "UPDATE purchase_items "
                        + "SET quantidade_entrada = ?, entrada_status = ? "
                        + "WHERE purchase_id = ? AND product_id = ?";
                PreparedStatement pst2 = conn.prepareStatement(sqlUpdate);
                pst2.setInt(1, novaQuantidadeEntrada);
                pst2.setString(2, novoStatus);
                pst2.setInt(3, movimento.getReferenceId());
                pst2.setInt(4, movimento.getProduct().getId());
                pst2.executeUpdate();

                System.out.println("🔄 Item de compra atualizado → produto: "
                        + movimento.getProduct().getDescription()
                        + " (" + novaQuantidadeEntrada + "/" + quantidadeTotal + ")");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar entrada do item de compra: " + e.getMessage());
        }
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
            System.out.println("Erro ao carregar lista movimentos de stock: " + e.getMessage());
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
