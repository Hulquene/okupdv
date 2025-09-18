/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.models.Clients;
import com.okutonda.okudpdv.models.Order;
import com.okutonda.okudpdv.models.Payment;
import com.okutonda.okudpdv.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rog
 */
public class FinanceDao {

    private final Connection conn;

    public FinanceDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    /**
     * Lista todas as faturas que ainda têm saldo em aberto (Contas a Receber).
     *
     * @return
     */
    public List<Order> listContasAReceber() {
        List<Order> list = new ArrayList<>();
        String sql = """
            SELECT o.id,
                   o.number,
                   o.prefix,
                   o.total,
                   o.datecreate,
                   o.duedate,
                   o.note,
                   o.client_id,
                   c.company AS client_name,
                   c.nif AS client_nif,
                   u.id AS user_id,
                   u.name AS user_name,
                   (o.total - IFNULL(SUM(p.total), 0)) AS saldo_em_aberto,
                   IFNULL(SUM(p.total), 0) AS total_pago
            FROM orders o
            LEFT JOIN payments p
              ON o.id = p.order_id AND p.status='SUCCESS'
            LEFT JOIN clients c
              ON o.client_id = c.id
            LEFT JOIN users u
              ON o.user_id = u.id
            GROUP BY o.id, c.company, c.nif, u.id, u.name
            HAVING saldo_em_aberto > 0
            ORDER BY o.datecreate ASC
        """;

        try (PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Order obj = new Order();
                obj.setId(rs.getInt("id"));
                obj.setNumber(rs.getInt("number"));
                obj.setPrefix(rs.getString("prefix"));
                obj.setTotal(rs.getDouble("total"));
                obj.setDatecreate(rs.getString("datecreate"));
//                obj.setDuedate(rs.getString("duedate"));
                obj.setNote("Saldo em aberto: " + rs.getDouble("saldo_em_aberto"));
                obj.setPayTotal(rs.getDouble("total_pago"));

                // Cliente
                if (rs.getInt("client_id") > 0) {
                    Clients c = new Clients();
                    c.setId(rs.getInt("client_id"));
                    c.setName(rs.getString("client_name"));
                    c.setNif(rs.getString("client_nif"));
                    obj.setClient(c);
                }

                // Usuário
                if (rs.getInt("user_id") > 0) {
                    User u = new User();
                    u.setId(rs.getInt("user_id"));
                    u.setName(rs.getString("user_name"));
                    obj.setSeller(u);
                }

                list.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar contas a receber: " + e.getMessage());
        }
        return list;
    }

    /**
     * Lista histórico de vendas (todas as faturas pagas no ato ou já
     * liquidadas).
     *
     * @return
     */
    public List<Order> listHistoricoVendas() {
        List<Order> list = new ArrayList<>();
        String sql = """
            SELECT o.id,
                   o.number,
                   o.prefix,
                   o.total,
                   o.datecreate,
                   o.duedate,
                   o.note,
                   o.client_id,
                   c.company AS client_name,
                   c.nif AS client_nif,
                   u.id AS user_id,
                   u.name AS user_name,
                   IFNULL(SUM(p.total), 0) AS total_pago
            FROM orders o
            INNER JOIN payments p
              ON o.id = p.order_id AND p.status='SUCCESS'
            LEFT JOIN clients c
              ON o.client_id = c.id
            LEFT JOIN users u
              ON o.user_id = u.id
            GROUP BY o.id, c.company, c.nif, u.id, u.name
            ORDER BY o.datecreate DESC
        """;

        try (PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Order obj = new Order();
                obj.setId(rs.getInt("id"));
                obj.setNumber(rs.getInt("number"));
                obj.setPrefix(rs.getString("prefix"));
                obj.setTotal(rs.getDouble("total"));
                obj.setDatecreate(rs.getString("datecreate"));
                obj.setDuedate(rs.getString("duedate"));
                obj.setNote(rs.getString("note"));
                obj.setPayTotal(rs.getDouble("total_pago"));

                // Cliente
                if (rs.getInt("client_id") > 0) {
                    Clients c = new Clients();
                    c.setId(rs.getInt("client_id"));
                    c.setName(rs.getString("client_name"));
                    c.setNif(rs.getString("client_nif"));
                    obj.setClient(c);
                }

                // Usuário
                if (rs.getInt("user_id") > 0) {
                    User u = new User();
                    u.setId(rs.getInt("user_id"));
                    u.setName(rs.getString("user_name"));
                    obj.setSeller(u);
                }

                list.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar histórico de vendas: " + e.getMessage());
        }
        return list;
    }

    /**
     * Fluxo de caixa consolidado por dia e forma de pagamento.
     *
     * @param dateFrom
     * @param dateTo
     * @return
     */
    public List<Payment> listFluxoCaixa(String dateFrom, String dateTo) {
        List<Payment> list = new ArrayList<>();
        String sql = """
            SELECT DATE(p.date) AS dia,
                   p.mode,
                   SUM(p.total) AS valor
            FROM payments p
            WHERE p.status='SUCCESS'
              AND DATE(p.date) BETWEEN ? AND ?
            GROUP BY DATE(p.date), p.mode
            ORDER BY dia ASC
        """;

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, dateFrom);
            pst.setString(2, dateTo);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Payment obj = new Payment();
                    obj.setDate(rs.getString("dia"));
                    obj.setPaymentMode(com.okutonda.okudpdv.models.PaymentMode.valueOf(rs.getString("mode")));
                    obj.setTotal(rs.getBigDecimal("valor"));
                    list.add(obj);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar fluxo de caixa: " + e.getMessage());
        }
        return list;
    }

    public List<Payment> listReceitas(String dateFrom, String dateTo) {
        List<Payment> list = new ArrayList<>();
        String sql = """
        SELECT p.id,
               p.description,
               p.total,
               p.date,
               p.mode,
               p.reference,
               c.id   AS client_id,
               c.company AS client_name,
               u.id   AS user_id,
               u.name AS user_name
        FROM payments p
        LEFT JOIN clients c ON p.clientId = c.id
        LEFT JOIN users u   ON p.userId = u.id
        WHERE p.status='SUCCESS'
          AND DATE(p.date) BETWEEN ? AND ?
        ORDER BY p.date ASC
    """;

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, dateFrom);
            pst.setString(2, dateTo);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Payment obj = new Payment();
                    obj.setId(rs.getInt("id"));
                    obj.setDescription(rs.getString("description"));
                    obj.setTotal(rs.getBigDecimal("total"));
                    obj.setDate(rs.getString("date"));
                    obj.setPaymentMode(com.okutonda.okudpdv.models.PaymentMode.valueOf(rs.getString("mode")));
                    obj.setReference(rs.getString("reference"));

                    // Cliente
                    com.okutonda.okudpdv.models.Clients c = new com.okutonda.okudpdv.models.Clients();
                    c.setId(rs.getInt("client_id"));
                    c.setName(rs.getString("client_name"));
                    obj.setClient(c);

                    // Usuário
                    com.okutonda.okudpdv.models.User u = new com.okutonda.okudpdv.models.User();
                    u.setId(rs.getInt("user_id"));
                    u.setName(rs.getString("user_name"));
                    obj.setUser(u);

                    list.add(obj);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar receitas: " + e.getMessage());
        }
        return list;
    }

    /**
     * Total de receitas no período (somatório de todos os pagamentos).
     *
     * @param dateFrom
     * @param dateTo
     * @return
     */
    public double getTotalReceitas(String dateFrom, String dateTo) {
        String sql = """
            SELECT SUM(p.total) AS valor
            FROM payments p
            WHERE p.status='SUCCESS'
              AND DATE(p.date) BETWEEN ? AND ?
        """;

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, dateFrom);
            pst.setString(2, dateTo);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("valor");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao calcular total de receitas: " + e.getMessage());
        }
        return 0d;
    }
}
