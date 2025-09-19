/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.models.PurchasePayment;
import com.okutonda.okudpdv.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rog
 */
public class PurchasePaymentDao {

    private final Connection conn;

    public PurchasePaymentDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public boolean add(PurchasePayment obj, int purchaseId) {
        String sql = """
            INSERT INTO purchase_payments (purchase_id, valor_pago, data_pagamento, metodo, referencia, observacao, user_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, purchaseId);
            pst.setBigDecimal(2, obj.getValorPago());
            pst.setDate(3, new java.sql.Date(obj.getDataPagamento().getTime()));
            pst.setString(4, obj.getMetodo());
            pst.setString(5, obj.getReferencia());
            pst.setString(6, obj.getObservacao());
            pst.setInt(7, obj.getUser() != null ? obj.getUser().getId() : 0);
            return pst.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir pagamento de compra: " + e.getMessage());
            return false;
        }
    }

    public List<PurchasePayment> listByPurchase(int purchaseId) {
        List<PurchasePayment> list = new ArrayList<>();
        String sql = """
            SELECT pp.*, u.id AS user_id, u.name AS user_name
            FROM purchase_payments pp
            LEFT JOIN users u ON pp.user_id = u.id
            WHERE pp.purchase_id=?
            ORDER BY pp.data_pagamento ASC
        """;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, purchaseId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    PurchasePayment pp = new PurchasePayment();
                    pp.setId(rs.getInt("id"));
                    pp.setValorPago(rs.getBigDecimal("valor_pago"));
                    pp.setDataPagamento(rs.getDate("data_pagamento"));
                    pp.setMetodo(rs.getString("metodo"));
                    pp.setReferencia(rs.getString("referencia"));
                    pp.setObservacao(rs.getString("observacao"));

                    User u = new User();
                    u.setId(rs.getInt("user_id"));
                    u.setName(rs.getString("user_name"));
                    pp.setUser(u);

                    list.add(pp);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar pagamentos da compra: " + e.getMessage());
        }
        return list;
    }
}
