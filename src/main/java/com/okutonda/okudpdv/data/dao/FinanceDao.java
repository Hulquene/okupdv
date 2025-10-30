package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.connection.DatabaseProvider;
import static com.okutonda.okudpdv.data.connection.DatabaseProvider.getConnection;
import com.okutonda.okudpdv.data.entities.*;
import java.sql.*;
import java.util.*;

/**
 * DAO de relatórios e controle financeiro: - Contas a Pagar - Contas a Receber
 * - Fluxo de Caixa - Receitas e Despesas
 *
 * Herda diretamente de DatabaseProvider para usar o pool de conexões
 * (HikariCP). Não implementa GenericDao, pois é um DAO analítico, não CRUD.
 *
 * @author Hulquene
 */
public class FinanceDao extends DatabaseProvider {

    public FinanceDao() {
        super();
    }

    // ==========================================================
    // 🔹 CONTAS A PAGAR
    // ==========================================================
    public List<Purchase> listContasAPagar() {
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
             GROUP BY p.id, p.invoice_number, p.invoice_type, p.descricao,
                      p.total, p.iva_total, p.data_compra, p.data_vencimento,
                      p.status, s.id, s.name
             HAVING saldo_em_aberto > 0
             ORDER BY p.data_vencimento ASC
        """;

        List<Purchase> list = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Purchase p = new Purchase();
                p.setId(rs.getInt("id"));
                p.setInvoiceNumber(rs.getString("invoice_number"));
                p.setDescricao(rs.getString("descricao"));
                p.setTotal(rs.getBigDecimal("total"));
                p.setIvaTotal(rs.getBigDecimal("iva_total"));
                p.setDataCompra(rs.getDate("data_compra"));
                p.setDataVencimento(rs.getDate("data_vencimento"));
                p.setStatus(rs.getString("status"));
                p.setPayTotal(rs.getBigDecimal("total_pago"));
                p.setNote("Saldo em aberto: " + rs.getBigDecimal("saldo_em_aberto"));

                String tipo = rs.getString("invoice_type");
                if (tipo != null) {
                    p.setInvoiceType(InvoiceType.valueOf(tipo));
                }

                Supplier s = new Supplier();
                s.setId(rs.getInt("supplier_id"));
                s.setName(rs.getString("supplier_name"));
                p.setSupplier(s);

                list.add(p);
            }

        } catch (SQLException e) {
            System.err.println("[DB] Erro ao listar contas a pagar: " + e.getMessage());
        }
        return list;
    }

    // ==========================================================
    // 🔹 CONTAS A RECEBER
    // ==========================================================
    public List<Order> listContasAReceber() {
        String sql = """
            SELECT o.id,
                   o.number,
                   o.prefix,
                   o.total,
                   o.datecreate,
                   o.duedate,
                   c.id AS client_id,
                   c.company AS client_name,
                   c.nif AS client_nif,
                   u.id AS user_id,
                   u.name AS user_name,
                   (o.total - IFNULL(SUM(p.total), 0)) AS saldo_em_aberto,
                   IFNULL(SUM(p.total), 0) AS total_pago
              FROM orders o
              LEFT JOIN payments p ON o.id = p.order_id AND p.status='SUCCESS'
              LEFT JOIN clients c ON o.client_id = c.id
              LEFT JOIN users u ON o.user_id = u.id
             GROUP BY o.id, c.company, c.nif, u.id, u.name
             HAVING saldo_em_aberto > 0
             ORDER BY o.datecreate ASC
        """;

        List<Order> list = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setNumber(rs.getInt("number"));
                o.setPrefix(rs.getString("prefix"));
                o.setTotal(rs.getDouble("total"));
                o.setDatecreate(rs.getString("datecreate"));
                o.setDuedate(rs.getString("duedate"));
                o.setPayTotal(rs.getDouble("total_pago"));
                o.setNote("Saldo em aberto: " + rs.getDouble("saldo_em_aberto"));

                Clients c = new Clients();
                c.setId(rs.getInt("client_id"));
                c.setName(rs.getString("client_name"));
                c.setNif(rs.getString("client_nif"));
                o.setClient(c);

                User u = new User();
                u.setId(rs.getInt("user_id"));
                u.setName(rs.getString("user_name"));
                o.setSeller(u);

                list.add(o);
            }

        } catch (SQLException e) {
            System.err.println("[DB] Erro ao listar contas a receber: " + e.getMessage());
        }
        return list;
    }

    // ==========================================================
    // 🔹 HISTÓRICO DE VENDAS
    // ==========================================================
    public List<Order> listHistoricoVendas() {
        String sql = """
            SELECT o.id,
                   o.number,
                   o.prefix,
                   o.total,
                   o.datecreate,
                   o.duedate,
                   o.note,
                   c.id AS client_id,
                   c.company AS client_name,
                   u.id AS user_id,
                   u.name AS user_name,
                   IFNULL(SUM(p.total), 0) AS total_pago
              FROM orders o
              INNER JOIN payments p ON o.id = p.order_id AND p.status='SUCCESS'
              LEFT JOIN clients c ON o.client_id = c.id
              LEFT JOIN users u ON o.user_id = u.id
             GROUP BY o.id, c.company, u.id
             ORDER BY o.datecreate DESC
        """;

        List<Order> list = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setNumber(rs.getInt("number"));
                o.setPrefix(rs.getString("prefix"));
                o.setTotal(rs.getDouble("total"));
                o.setDatecreate(rs.getString("datecreate"));
                o.setDuedate(rs.getString("duedate"));
                o.setNote(rs.getString("note"));
                o.setPayTotal(rs.getDouble("total_pago"));

                Clients c = new Clients();
                c.setId(rs.getInt("client_id"));
                c.setName(rs.getString("client_name"));
                o.setClient(c);

                User u = new User();
                u.setId(rs.getInt("user_id"));
                u.setName(rs.getString("user_name"));
                o.setSeller(u);

                list.add(o);
            }

        } catch (SQLException e) {
            System.err.println("[DB] Erro ao listar histórico de vendas: " + e.getMessage());
        }
        return list;
    }

    // ==========================================================
    // 🔹 FLUXO DE CAIXA
    // ==========================================================
    public List<Payment> listFluxoCaixa(String dateFrom, String dateTo) {
        String sql = """
            SELECT DATE(p.date) AS dia, p.mode, SUM(p.total) AS valor
              FROM payments p
             WHERE p.status='SUCCESS' AND DATE(p.date) BETWEEN ? AND ?
             GROUP BY DATE(p.date), p.mode
             ORDER BY dia ASC
        """;

        List<Payment> list = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, dateFrom);
            pst.setString(2, dateTo);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Payment p = new Payment();
                    p.setDate(rs.getString("dia"));
                    p.setPaymentMode(PaymentMode.valueOf(rs.getString("mode")));
                    p.setTotal(rs.getBigDecimal("valor"));
                    list.add(p);
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB] Erro ao listar fluxo de caixa: " + e.getMessage());
        }
        return list;
    }

    // ==========================================================
    // 🔹 RECEITAS
    // ==========================================================
    public List<Payment> listReceitas(String dateFrom, String dateTo) {
        String sql = """
            SELECT p.id, p.description, p.total, p.date, p.mode, p.reference,
                   c.id AS client_id, c.company AS client_name,
                   u.id AS user_id, u.name AS user_name
              FROM payments p
              LEFT JOIN clients c ON p.clientId = c.id
              LEFT JOIN users u ON p.userId = u.id
             WHERE p.status='SUCCESS'
               AND DATE(p.date) BETWEEN ? AND ?
             ORDER BY p.date ASC
        """;

        List<Payment> list = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, dateFrom);
            pst.setString(2, dateTo);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Payment p = new Payment();
                    p.setId(rs.getInt("id"));
                    p.setDescription(rs.getString("description"));
                    p.setTotal(rs.getBigDecimal("total"));
                    p.setDate(rs.getString("date"));
                    p.setPaymentMode(PaymentMode.valueOf(rs.getString("mode")));
                    p.setReference(rs.getString("reference"));

                    Clients c = new Clients();
                    c.setId(rs.getInt("client_id"));
                    c.setName(rs.getString("client_name"));
                    p.setClient(c);

                    User u = new User();
                    u.setId(rs.getInt("user_id"));
                    u.setName(rs.getString("user_name"));
                    p.setUser(u);

                    list.add(p);
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB] Erro ao listar receitas: " + e.getMessage());
        }
        return list;
    }

    public double getTotalReceitas(String dateFrom, String dateTo) {
        String sql = """
            SELECT SUM(p.total) AS valor
              FROM payments p
             WHERE p.status='SUCCESS'
               AND DATE(p.date) BETWEEN ? AND ?
        """;

        try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, dateFrom);
            pst.setString(2, dateTo);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("valor");
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB] Erro ao calcular total de receitas: " + e.getMessage());
        }
        return 0d;
    }

    // ==========================================================
    // 🔹 DESPESAS
    // ==========================================================
    public List<Expense> listDespesas(String dateFrom, String dateTo) {
        String sql = """
            SELECT e.id, e.description, e.total, e.date, e.dateFinish, e.mode, e.reference,
                   e.currency, e.notes,
                   s.id AS supplier_id, s.name AS supplier_name,
                   u.id AS user_id, u.name AS user_name,
                   c.id AS category_id, c.name AS category_name
              FROM expenses e
              LEFT JOIN suppliers s ON e.supplier_id = s.id
              LEFT JOIN users u ON e.user_id = u.id
              LEFT JOIN expense_categories c ON e.category_id = c.id
             WHERE DATE(e.date) BETWEEN ? AND ?
             ORDER BY e.date ASC
        """;

        List<Expense> list = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, dateFrom);
            pst.setString(2, dateTo);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Expense e = new Expense();
                    e.setId(rs.getInt("id"));
                    e.setDescription(rs.getString("description"));
                    e.setTotal(rs.getBigDecimal("total"));
                    e.setDate(rs.getString("date"));
                    e.setDateFinish(rs.getString("dateFinish"));
                    e.setMode(rs.getString("mode"));
                    e.setReference(rs.getString("reference"));
                    e.setCurrency(rs.getString("currency"));
                    e.setNotes(rs.getString("notes"));

                    Supplier s = new Supplier();
                    s.setId(rs.getInt("supplier_id"));
                    s.setName(rs.getString("supplier_name"));
                    e.setSupplier(s);

                    User u = new User();
                    u.setId(rs.getInt("user_id"));
                    u.setName(rs.getString("user_name"));
                    e.setUser(u);

                    ExpenseCategory c = new ExpenseCategory();
                    c.setId(rs.getInt("category_id"));
                    c.setName(rs.getString("category_name"));
                    e.setCategory(c);

                    list.add(e);
                }
            }

        } catch (SQLException ex) {
            System.err.println("[DB] Erro ao listar despesas: " + ex.getMessage());
        }
        return list;
    }
}
