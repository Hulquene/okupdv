package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.connection.DatabaseProvider;
import com.okutonda.okudpdv.data.entities.PurchasePayment;
import com.okutonda.okudpdv.data.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pelos pagamentos de compras.
 *
 * Compatível com BaseDao e HikariCP (pool de conexões). Garante fechamento
 * automático das conexões após uso.
 *
 * @author Hulquene
 */
public class PurchasePaymentDao extends BaseDao<PurchasePayment> {

    public PurchasePaymentDao() {
        // usa o pool de conexões
    }

    public PurchasePaymentDao(Connection externalConn) {
        super(externalConn); // permite transações externas
    }

    // ==========================================================
    // 🔹 MAP ResultSet → PurchasePayment
    // ==========================================================
    private PurchasePayment map(ResultSet rs) {
        try {
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

            return pp;
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao mapear PurchasePayment: " + e.getMessage());
            return null;
        }
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    @Override
    public boolean add(PurchasePayment obj) {
        String sql = """
            INSERT INTO purchase_payments
            (purchase_id, valor_pago, data_pagamento, metodo, referencia, observacao, user_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        return executeUpdate(sql,
                obj.getPurchaseId(),
                obj.getValorPago(),
                new java.sql.Date(obj.getDataPagamento().getTime()),
                obj.getMetodo(),
                obj.getReferencia(),
                obj.getObservacao(),
                obj.getUser() != null ? obj.getUser().getId() : 0
        );
    }

    /**
     * Adiciona pagamento vinculado a uma compra
     */
    public boolean add(PurchasePayment obj, int purchaseId) {
        obj.setPurchaseId(purchaseId);
        return add(obj);
    }

    @Override
    public boolean update(PurchasePayment obj) {
        String sql = """
            UPDATE purchase_payments
               SET valor_pago=?, data_pagamento=?, metodo=?, referencia=?, observacao=?, user_id=?
             WHERE id=?
        """;
        return executeUpdate(sql,
                obj.getValorPago(),
                new java.sql.Date(obj.getDataPagamento().getTime()),
                obj.getMetodo(),
                obj.getReferencia(),
                obj.getObservacao(),
                obj.getUser() != null ? obj.getUser().getId() : 0,
                obj.getId()
        );
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM purchase_payments WHERE id=?", id);
    }

    @Override
    public PurchasePayment findById(int id) {
        String sql = """
            SELECT pp.*, u.id AS user_id, u.name AS user_name
            FROM purchase_payments pp
            LEFT JOIN users u ON pp.user_id = u.id
            WHERE pp.id=?
        """;
        return findOne(sql, this::map, id);
    }

    @Override
    public List<PurchasePayment> findAll() {
        String sql = """
            SELECT pp.*, u.id AS user_id, u.name AS user_name
            FROM purchase_payments pp
            LEFT JOIN users u ON pp.user_id = u.id
            ORDER BY pp.data_pagamento ASC
        """;
        return executeQuery(sql, this::map);
    }

    // ==========================================================
    // 🔹 Consultas específicas
    // ==========================================================
    public List<PurchasePayment> listByPurchase(int purchaseId) {
        String sql = """
            SELECT pp.*, u.id AS user_id, u.name AS user_name
            FROM purchase_payments pp
            LEFT JOIN users u ON pp.user_id = u.id
            WHERE pp.purchase_id=?
            ORDER BY pp.data_pagamento ASC
        """;
        return executeQuery(sql, this::map, purchaseId);
    }
}
