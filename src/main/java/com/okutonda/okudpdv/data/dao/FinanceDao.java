package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.connection.DatabaseProvider;
import static com.okutonda.okudpdv.data.connection.DatabaseProvider.getConnection;
import com.okutonda.okudpdv.data.entities.*;
import java.sql.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * DAO de relatórios e controle financeiro: - Contas a Pagar - Contas a Receber
 * - Fluxo de Caixa - Receitas e Despesas - Relatórios Consolidados
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
                p.setTotal_pago(rs.getBigDecimal("total_pago"));

                String tipo = rs.getString("invoice_type");
                if (tipo != null) {
                    try {
                        p.setInvoiceType(InvoiceType.valueOf(tipo));
                    } catch (IllegalArgumentException e) {
                        p.setInvoiceType(InvoiceType.FT);
                    }
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

    /**
     * Calcula o total de contas a pagar
     */
    public BigDecimal getTotalContasAPagar() {
        String sql = """
            SELECT SUM(p.total - IFNULL(SUM(pp.valor_pago), 0)) AS total
              FROM purchases p
              LEFT JOIN purchase_payments pp ON p.id = pp.purchase_id
             GROUP BY p.id
            HAVING (p.total - IFNULL(SUM(pp.valor_pago), 0)) > 0
        """;

        try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            BigDecimal total = BigDecimal.ZERO;
            while (rs.next()) {
                BigDecimal valor = rs.getBigDecimal("total");
                if (valor != null) {
                    total = total.add(valor);
                }
            }
            return total;

        } catch (SQLException e) {
            System.err.println("[DB] Erro ao calcular total de contas a pagar: " + e.getMessage());
            return BigDecimal.ZERO;
        }
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
             GROUP BY o.id, o.number, o.prefix, o.total, o.datecreate, o.duedate,
                      c.id, c.company, c.nif, u.id, u.name
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

    /**
     * Calcula o total de contas a receber
     */
    public BigDecimal getTotalContasAReceber() {
        String sql = """
            SELECT SUM(o.total - IFNULL(SUM(p.total), 0)) AS total
              FROM orders o
              LEFT JOIN payments p ON o.id = p.order_id AND p.status='SUCCESS'
             GROUP BY o.id
            HAVING (o.total - IFNULL(SUM(p.total), 0)) > 0
        """;

        try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            BigDecimal total = BigDecimal.ZERO;
            while (rs.next()) {
                BigDecimal valor = rs.getBigDecimal("total");
                if (valor != null) {
                    total = total.add(valor);
                }
            }
            return total;

        } catch (SQLException e) {
            System.err.println("[DB] Erro ao calcular total de contas a receber: " + e.getMessage());
            return BigDecimal.ZERO;
        }
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
              LEFT JOIN payments p ON o.id = p.order_id AND p.status='SUCCESS'
              LEFT JOIN clients c ON o.client_id = c.id
              LEFT JOIN users u ON o.user_id = u.id
             GROUP BY o.id, o.number, o.prefix, o.total, o.datecreate, o.duedate, o.note,
                      c.id, c.company, u.id, u.name
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

    /**
     * Lista vendas por período específico
     */
    public List<Order> listVendasPorPeriodo(String dateFrom, String dateTo) {
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
              LEFT JOIN payments p ON o.id = p.order_id AND p.status='SUCCESS'
              LEFT JOIN clients c ON o.client_id = c.id
              LEFT JOIN users u ON o.user_id = u.id
             WHERE DATE(o.datecreate) BETWEEN ? AND ?
             GROUP BY o.id, o.number, o.prefix, o.total, o.datecreate, o.duedate, o.note,
                      c.id, c.company, u.id, u.name
             ORDER BY o.datecreate DESC
        """;

        List<Order> list = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, dateFrom);
            pst.setString(2, dateTo);

            try (ResultSet rs = pst.executeQuery()) {
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
            }

        } catch (SQLException e) {
            System.err.println("[DB] Erro ao listar vendas por período: " + e.getMessage());
        }
        return list;
    }

    /**
     * Calcula total de vendas por período
     */
    public BigDecimal getTotalVendasPeriodo(String dateFrom, String dateTo) {
        String sql = """
            SELECT SUM(o.total) AS total
              FROM orders o
             WHERE DATE(o.datecreate) BETWEEN ? AND ?
        """;

        try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, dateFrom);
            pst.setString(2, dateTo);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    BigDecimal total = rs.getBigDecimal("total");
                    return total != null ? total : BigDecimal.ZERO;
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB] Erro ao calcular total de vendas por período: " + e.getMessage());
        }
        return BigDecimal.ZERO;
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

                    // Para Payment - AGORA FUNCIONA
                    String mode = rs.getString("mode");
                    if (mode != null) {
                        p.setPaymentMode(PaymentMode.fromCodigo(mode));
                    } else {
                        p.setPaymentMode(PaymentMode.OU);
                    }

                    p.setTotal(rs.getBigDecimal("valor"));
                    list.add(p);
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB] Erro ao listar fluxo de caixa: " + e.getMessage());
        }
        return list;
    }

    /**
     * Calcula total do fluxo de caixa por período
     */
    public BigDecimal getTotalFluxoCaixa(String dateFrom, String dateTo) {
        String sql = """
            SELECT SUM(p.total) AS total
              FROM payments p
             WHERE p.status='SUCCESS' AND DATE(p.date) BETWEEN ? AND ?
        """;

        try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, dateFrom);
            pst.setString(2, dateTo);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    BigDecimal total = rs.getBigDecimal("total");
                    return total != null ? total : BigDecimal.ZERO;
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB] Erro ao calcular total do fluxo de caixa: " + e.getMessage());
        }
        return BigDecimal.ZERO;
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

                    // No FinanceDao - CORRETO
                    String mode = rs.getString("mode");
                    if (mode != null) {
                        p.setPaymentMode(PaymentMode.fromCodigo(mode));
                    } else {
                        p.setPaymentMode(PaymentMode.OU);
                    }

                    p.setReference(rs.getString("reference"));

                    // Cliente
                    int clientId = rs.getInt("client_id");
                    if (clientId > 0) {
                        Clients c = new Clients();
                        c.setId(clientId);
                        c.setName(rs.getString("client_name"));
                        p.setClient(c);
                    }

                    // Usuário
                    int userId = rs.getInt("user_id");
                    if (userId > 0) {
                        User u = new User();
                        u.setId(userId);
                        u.setName(rs.getString("user_name"));
                        p.setUser(u);
                    }

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

    /**
     * Versão BigDecimal do total de receitas
     */
    public BigDecimal getTotalReceitasBigDecimal(String dateFrom, String dateTo) {
        String sql = """
            SELECT SUM(p.total) AS total
              FROM payments p
             WHERE p.status='SUCCESS'
               AND DATE(p.date) BETWEEN ? AND ?
        """;

        try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, dateFrom);
            pst.setString(2, dateTo);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    BigDecimal total = rs.getBigDecimal("total");
                    return total != null ? total : BigDecimal.ZERO;
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB] Erro ao calcular total de receitas (BigDecimal): " + e.getMessage());
        }
        return BigDecimal.ZERO;
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

                    // Para Expense - AGORA FUNCIONA
                    String mode = rs.getString("mode");
                    if (mode != null) {
                        e.setPaymentMode(PaymentMode.fromCodigo(mode));
                    } else {
                        e.setPaymentMode(PaymentMode.OU);
                    }

                    e.setReference(rs.getString("reference"));
                    e.setCurrency(rs.getString("currency"));
                    e.setNotes(rs.getString("notes"));

                    // Fornecedor
                    int supplierId = rs.getInt("supplier_id");
                    if (supplierId > 0) {
                        Supplier s = new Supplier();
                        s.setId(supplierId);
                        s.setName(rs.getString("supplier_name"));
                        e.setSupplier(s);
                    }

                    // Usuário
                    int userId = rs.getInt("user_id");
                    if (userId > 0) {
                        User u = new User();
                        u.setId(userId);
                        u.setName(rs.getString("user_name"));
                        e.setUser(u);
                    }

                    // Categoria
                    int categoryId = rs.getInt("category_id");
                    if (categoryId > 0) {
                        ExpenseCategory c = new ExpenseCategory();
                        c.setId(categoryId);
                        c.setName(rs.getString("category_name"));
                        e.setCategory(c);
                    }

                    list.add(e);
                }
            }

        } catch (SQLException ex) {
            System.err.println("[DB] Erro ao listar despesas: " + ex.getMessage());
        }
        return list;
    }

    /**
     * Calcula total de despesas por período
     */
    public BigDecimal getTotalDespesas(String dateFrom, String dateTo) {
        String sql = """
            SELECT SUM(e.total) AS total
              FROM expenses e
             WHERE DATE(e.date) BETWEEN ? AND ?
        """;

        try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, dateFrom);
            pst.setString(2, dateTo);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    BigDecimal total = rs.getBigDecimal("total");
                    return total != null ? total : BigDecimal.ZERO;
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB] Erro ao calcular total de despesas: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    // ==========================================================
    // 🔹 RELATÓRIOS CONSOLIDADOS
    // ==========================================================
    /**
     * Retorna um mapa com todos os totais para relatório consolidado
     */
    public Map<String, BigDecimal> getTotaisConsolidados(String dateFrom, String dateTo) {
        Map<String, BigDecimal> totais = new HashMap<>();

        try {
            totais.put("receitas", getTotalReceitasBigDecimal(dateFrom, dateTo));
            totais.put("despesas", getTotalDespesas(dateFrom, dateTo));
            totais.put("vendas", getTotalVendasPeriodo(dateFrom, dateTo));
            totais.put("contas_receber", getTotalContasAReceber());
            totais.put("contas_pagar", getTotalContasAPagar());

            // Calcula saldo
            BigDecimal saldo = totais.get("receitas").subtract(totais.get("despesas"));
            totais.put("saldo", saldo);

        } catch (Exception e) {
            System.err.println("[DB] Erro ao calcular totais consolidados: " + e.getMessage());
        }

        return totais;
    }
}
