/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.data.entities.Clients;
import com.okutonda.okudpdv.data.entities.Expense;
import com.okutonda.okudpdv.data.entities.ExpenseCategory;
import com.okutonda.okudpdv.data.entities.InvoiceType;
import com.okutonda.okudpdv.data.entities.Order;
import com.okutonda.okudpdv.data.entities.Payment;
import com.okutonda.okudpdv.data.entities.Purchase;
import com.okutonda.okudpdv.data.entities.Supplier;
import com.okutonda.okudpdv.data.entities.User;

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

    public List<Purchase> listContasAPagar() {
        List<Purchase> list = new ArrayList<>();
        String sql = """
        SELECT p.id,
               p.invoice_number,
               p.invoice_type,
               p.descricao,
               p.total,
               p.iva_total,
               p.data_compra,
               p.data_vencimento,
               p.status,
               s.id   AS supplier_id,
               s.name AS supplier_name,
               (p.total - IFNULL(SUM(pp.valor_pago), 0)) AS saldo_em_aberto,
               IFNULL(SUM(pp.valor_pago), 0) AS total_pago
        FROM purchases p
        LEFT JOIN purchase_payments pp ON p.id = pp.purchase_id
        LEFT JOIN suppliers s ON p.supplier_id = s.id
        GROUP BY p.id, p.invoice_number, p.invoice_type, p.descricao, p.total, p.iva_total,
                 p.data_compra, p.data_vencimento, p.status, s.id, s.name
        HAVING saldo_em_aberto > 0
        ORDER BY p.data_vencimento ASC
    """;

        try (PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Purchase obj = new Purchase();
                // ENUMs
                String invoice_typeStr = rs.getString("invoice_type");
                
                obj.setId(rs.getInt("id"));
                obj.setInvoiceNumber(rs.getString("invoice_number"));
//                obj.setInvoiceType(rs.getString("invoice_type"));
                obj.setDescricao(rs.getString("descricao"));
                obj.setTotal(rs.getBigDecimal("total"));
                obj.setIvaTotal(rs.getBigDecimal("iva_total"));

                // Datas (converte de SQL Date → java.util.Date)
                obj.setDataCompra(rs.getDate("data_compra"));
                obj.setDataVencimento(rs.getDate("data_vencimento"));

                obj.setStatus(rs.getString("status"));
                obj.setPayTotal(rs.getBigDecimal("total_pago"));
                obj.setNote("Saldo em aberto: " + rs.getBigDecimal("saldo_em_aberto"));

                if (invoice_typeStr != null) {
                    obj.setInvoiceType(InvoiceType.valueOf(invoice_typeStr));
                }
                // fornecedor
                if (rs.getInt("supplier_id") > 0) {
                    Supplier s = new Supplier();
                    s.setId(rs.getInt("supplier_id"));
                    s.setName(rs.getString("supplier_name"));
                    obj.setSupplier(s);
                }

                list.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar contas a pagar: " + e.getMessage());
        }
        return list;
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
                    obj.setPaymentMode(com.okutonda.okudpdv.data.entities.PaymentMode.valueOf(rs.getString("mode")));
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
                    obj.setPaymentMode(com.okutonda.okudpdv.data.entities.PaymentMode.valueOf(rs.getString("mode")));
                    obj.setReference(rs.getString("reference"));

                    // Cliente
                    com.okutonda.okudpdv.data.entities.Clients c = new com.okutonda.okudpdv.data.entities.Clients();
                    c.setId(rs.getInt("client_id"));
                    c.setName(rs.getString("client_name"));
                    obj.setClient(c);

                    // Usuário
                    com.okutonda.okudpdv.data.entities.User u = new com.okutonda.okudpdv.data.entities.User();
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

    public List<Expense> listDespesas(String dateFrom, String dateTo) {
        List<Expense> list = new ArrayList<>();
        String sql = """
        SELECT e.id,
               e.description,
               e.total,
               e.date,
               e.dateFinish,
               e.mode,
               e.reference,
               e.currency,
               e.notes,
               s.id   AS supplier_id,
               s.name AS supplier_name,
               u.id   AS user_id,
               u.name AS user_name,
               c.id   AS category_id,
               c.name AS category_name
        FROM expenses e
        LEFT JOIN suppliers s ON e.supplier_id = s.id
        LEFT JOIN users u     ON e.user_id = u.id
        LEFT JOIN expense_categories c ON e.category_id = c.id
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
                    obj.setDate(rs.getString("date"));
                    obj.setDateFinish(rs.getString("dateFinish"));
                    obj.setMode(rs.getString("mode"));
                    obj.setReference(rs.getString("reference"));
                    obj.setCurrency(rs.getString("currency"));
                    obj.setNotes(rs.getString("notes"));

                    // fornecedor
                    if (rs.getInt("supplier_id") > 0) {
                        Supplier supplier = new Supplier();
                        supplier.setId(rs.getInt("supplier_id"));
                        supplier.setName(rs.getString("supplier_name"));
                        obj.setSupplier(supplier);
                    }

                    // usuário
                    if (rs.getInt("user_id") > 0) {
                        User u = new User();
                        u.setId(rs.getInt("user_id"));
                        u.setName(rs.getString("user_name"));
                        obj.setUser(u);
                    }

                    // categoria
                    if (rs.getInt("category_id") > 0) {
                        ExpenseCategory c = new ExpenseCategory();
                        c.setId(rs.getInt("category_id"));
                        c.setName(rs.getString("category_name"));
                        obj.setCategory(c);
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
