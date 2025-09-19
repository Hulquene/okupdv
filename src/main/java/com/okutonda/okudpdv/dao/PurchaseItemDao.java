/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.models.PurchaseItem;
import com.okutonda.okudpdv.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rog
 */
public class PurchaseItemDao {

    private final Connection conn;

    public PurchaseItemDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public boolean add(PurchaseItem obj, int purchaseId) {
        String sql = """
            INSERT INTO purchase_items
            (purchase_id, product_id, quantidade, preco_custo, iva, subtotal)
            VALUES (?,?,?,?,?,?)
        """;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, purchaseId);
            pst.setInt(2, obj.getProduct().getId());
            pst.setInt(3, obj.getQuantidade());
            pst.setBigDecimal(4, obj.getPrecoCusto());
            pst.setBigDecimal(5, obj.getIva());
            pst.setBigDecimal(6, obj.getSubtotal());
            return pst.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Erro ao adicionar item de compra: " + e.getMessage());
            return false;
        }
    }

    public List<PurchaseItem> listByPurchase(int purchaseId) {
        List<PurchaseItem> list = new ArrayList<>();
        String sql = """
            SELECT i.*, p.name AS product_name
            FROM purchase_items i
            INNER JOIN products p ON i.product_id = p.id
            WHERE i.purchase_id = ?
        """;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, purchaseId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    PurchaseItem obj = new PurchaseItem();
                    obj.setId(rs.getInt("id"));
                    obj.setPurchaseId(rs.getInt("purchase_id"));
                    obj.setQuantidade(rs.getInt("quantidade"));
                    obj.setPrecoCusto(rs.getBigDecimal("preco_custo"));
                    obj.setIva(rs.getBigDecimal("iva"));
                    obj.setSubtotal(rs.getBigDecimal("subtotal"));

                    Product prod = new Product();
                    prod.setId(rs.getInt("product_id"));
                    prod.setDescription(rs.getString("product_name"));
                    obj.setProduct(prod);

                    list.add(obj);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar itens da compra: " + e.getMessage());
        }
        return list;
    }
}
