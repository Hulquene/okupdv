package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.*;
import org.hibernate.Session;
import jakarta.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * DAO de relatórios e controle financeiro usando Hibernate - Contas a Pagar -
 * Contas a Receber - Fluxo de Caixa - Receitas e Despesas - Relatórios
 * Consolidados
 *
 * @author Hulquene
 */
public class FinanceDao {

    // ==========================================================
    // 🔹 CONTAS A PAGAR
    // ==========================================================
    public List<Purchase> listContasAPagar() {
        Session session = HibernateUtil.getCurrentSession();
        try {
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

            List<Object[]> results = session.createNativeQuery(sql, Object[].class).getResultList();
            List<Purchase> list = new ArrayList<>();

            for (Object[] row : results) {
                Purchase p = new Purchase();
                p.setId((Integer) row[0]);
                p.setInvoiceNumber((String) row[1]);
                p.setDescricao((String) row[3]);
                p.setTotal((BigDecimal) row[4]);
                p.setIvaTotal((BigDecimal) row[5]);
                p.setDataCompra((java.sql.Date) row[6]);
                p.setDataVencimento((java.sql.Date) row[7]);
                p.setStatus((String) row[8]);
                p.setTotal_pago((BigDecimal) row[11]);

                String tipo = (String) row[2];
                if (tipo != null) {
                    try {
                        p.setInvoiceType(InvoiceType.valueOf(tipo));
                    } catch (IllegalArgumentException e) {
                        p.setInvoiceType(InvoiceType.FT);
                    }
                }

                Supplier s = new Supplier();
                s.setId((Integer) row[9]);
                s.setName((String) row[10]);
                p.setSupplier(s);

                list.add(p);
            }

            return list;

        } catch (Exception e) {
            System.err.println("Erro ao listar contas a pagar: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Calcula o total de contas a pagar
     */
    public BigDecimal getTotalContasAPagar() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            String sql = """
                SELECT SUM(p.total - IFNULL(SUM(pp.valor_pago), 0)) AS total
                  FROM purchases p
                  LEFT JOIN purchase_payments pp ON p.id = pp.purchase_id
                 GROUP BY p.id
                HAVING (p.total - IFNULL(SUM(pp.valor_pago), 0)) > 0
            """;

            List<BigDecimal> results = session.createNativeQuery(sql, BigDecimal.class).getResultList();
            BigDecimal total = BigDecimal.ZERO;
            for (BigDecimal valor : results) {
                if (valor != null) {
                    total = total.add(valor);
                }
            }
            return total;

        } catch (Exception e) {
            System.err.println("Erro ao calcular total de contas a pagar: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    // ==========================================================
    // 🔹 CONTAS A RECEBER
    // ==========================================================
    public List<com.okutonda.okudpdv.data.entities.Order> listContasAReceber() {
        Session session = HibernateUtil.getCurrentSession();
        try {
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

            List<Object[]> results = session.createNativeQuery(sql, Object[].class).getResultList();
            List<com.okutonda.okudpdv.data.entities.Order> list = new ArrayList<>();

            for (Object[] row : results) {
                com.okutonda.okudpdv.data.entities.Order o = new com.okutonda.okudpdv.data.entities.Order();
                o.setId((Integer) row[0]);
                o.setNumber((Integer) row[1]);
                o.setPrefix((String) row[2]);
                o.setTotal((BigDecimal) row[3]);
                o.setDatecreate((String) row[4]);
                o.setDuedate((String) row[5]);
                o.setPayTotal((BigDecimal) row[11]);

                Clients c = new Clients();
                c.setId((Integer) row[6]);
                c.setName((String) row[7]);
                c.setNif((String) row[8]);
                o.setClient(c);

                User u = new User();
                u.setId((Integer) row[9]);
                u.setName((String) row[10]);
                o.setSeller(u);

                list.add(o);
            }

            return list;

        } catch (Exception e) {
            System.err.println("Erro ao listar contas a receber: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Calcula o total de contas a receber
     */
    public BigDecimal getTotalContasAReceber() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            String sql = """
                SELECT SUM(o.total - IFNULL(SUM(p.total), 0)) AS total
                  FROM orders o
                  LEFT JOIN payments p ON o.id = p.order_id AND p.status='SUCCESS'
                 GROUP BY o.id
                HAVING (o.total - IFNULL(SUM(p.total), 0)) > 0
            """;

            List<BigDecimal> results = session.createNativeQuery(sql, BigDecimal.class).getResultList();
            BigDecimal total = BigDecimal.ZERO;
            for (BigDecimal valor : results) {
                if (valor != null) {
                    total = total.add(valor);
                }
            }
            return total;

        } catch (Exception e) {
            System.err.println("Erro ao calcular total de contas a receber: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    // ==========================================================
    // 🔹 HISTÓRICO DE VENDAS
    // ==========================================================
    public List<com.okutonda.okudpdv.data.entities.Order> listHistoricoVendas() {
        Session session = HibernateUtil.getCurrentSession();
        try {
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

            List<Object[]> results = session.createNativeQuery(sql, Object[].class).getResultList();
            List<com.okutonda.okudpdv.data.entities.Order> list = new ArrayList<>();

            for (Object[] row : results) {
                com.okutonda.okudpdv.data.entities.Order o = new com.okutonda.okudpdv.data.entities.Order();
                o.setId((Integer) row[0]);
                o.setNumber((Integer) row[1]);
                o.setPrefix((String) row[2]);
                o.setTotal((BigDecimal) row[3]);
                o.setDatecreate((String) row[4]);
                o.setDuedate((String) row[5]);
                o.setNote((String) row[6]);
                o.setPayTotal((BigDecimal) row[11]);

                Clients c = new Clients();
                c.setId((Integer) row[7]);
                c.setName((String) row[8]);
                o.setClient(c);

                User u = new User();
                u.setId((Integer) row[9]);
                u.setName((String) row[10]);
                o.setSeller(u);

                list.add(o);
            }

            return list;

        } catch (Exception e) {
            System.err.println("Erro ao listar histórico de vendas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Lista vendas por período específico
     */
    public List<com.okutonda.okudpdv.data.entities.Order> listVendasPorPeriodo(String dateFrom, String dateTo) {
        Session session = HibernateUtil.getCurrentSession();
        try {
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

            List<Object[]> results = session.createNativeQuery(sql, Object[].class)
                    .setParameter(1, dateFrom)
                    .setParameter(2, dateTo)
                    .getResultList();

            List<com.okutonda.okudpdv.data.entities.Order> list = new ArrayList<>();

            for (Object[] row : results) {
                com.okutonda.okudpdv.data.entities.Order o = new com.okutonda.okudpdv.data.entities.Order();
                o.setId((Integer) row[0]);
                o.setNumber((Integer) row[1]);
                o.setPrefix((String) row[2]);
                o.setTotal((BigDecimal) row[3]);
                o.setDatecreate((String) row[4]);
                o.setDuedate((String) row[5]);
                o.setNote((String) row[6]);
                o.setPayTotal((BigDecimal) row[11]);

                Clients c = new Clients();
                c.setId((Integer) row[7]);
                c.setName((String) row[8]);
                o.setClient(c);

                User u = new User();
                u.setId((Integer) row[9]);
                u.setName((String) row[10]);
                o.setSeller(u);

                list.add(o);
            }

            return list;

        } catch (Exception e) {
            System.err.println("Erro ao listar vendas por período: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Calcula total de vendas por período
     */
    public BigDecimal getTotalVendasPeriodo(String dateFrom, String dateTo) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            String sql = """
                SELECT SUM(o.total) AS total
                  FROM orders o
                 WHERE DATE(o.datecreate) BETWEEN ? AND ?
            """;

            BigDecimal total = (BigDecimal) session.createNativeQuery(sql)
                    .setParameter(1, dateFrom)
                    .setParameter(2, dateTo)
                    .uniqueResult();

            return total != null ? total : BigDecimal.ZERO;

        } catch (Exception e) {
            System.err.println("Erro ao calcular total de vendas por período: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    // ==========================================================
    // 🔹 FLUXO DE CAIXA
    // ==========================================================
    public List<Payment> listFluxoCaixa(String dateFrom, String dateTo) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            String sql = """
                SELECT DATE(p.date) AS dia, p.mode, SUM(p.total) AS valor
                  FROM payments p
                 WHERE p.status='SUCCESS' AND DATE(p.date) BETWEEN ? AND ?
                 GROUP BY DATE(p.date), p.mode
                 ORDER BY dia ASC
            """;

            List<Object[]> results = session.createNativeQuery(sql, Object[].class)
                    .setParameter(1, dateFrom)
                    .setParameter(2, dateTo)
                    .getResultList();

            List<Payment> list = new ArrayList<>();

            for (Object[] row : results) {
                Payment p = new Payment();
                p.setDate((String) row[0]);

                String mode = (String) row[1];
                if (mode != null) {
                    p.setPaymentMode(PaymentMode.fromCodigo(mode));
                } else {
                    p.setPaymentMode(PaymentMode.OU);
                }

                p.setTotal((BigDecimal) row[2]);
                list.add(p);
            }

            return list;

        } catch (Exception e) {
            System.err.println("Erro ao listar fluxo de caixa: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Calcula total do fluxo de caixa por período
     */
    public BigDecimal getTotalFluxoCaixa(String dateFrom, String dateTo) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            String sql = """
                SELECT SUM(p.total) AS total
                  FROM payments p
                 WHERE p.status='SUCCESS' AND DATE(p.date) BETWEEN ? AND ?
            """;

            BigDecimal total = (BigDecimal) session.createNativeQuery(sql)
                    .setParameter(1, dateFrom)
                    .setParameter(2, dateTo)
                    .uniqueResult();

            return total != null ? total : BigDecimal.ZERO;

        } catch (Exception e) {
            System.err.println("Erro ao calcular total do fluxo de caixa: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    // ==========================================================
    // 🔹 RECEITAS
    // ==========================================================
    public List<Payment> listReceitas(String dateFrom, String dateTo) {
        Session session = HibernateUtil.getCurrentSession();
        try {
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

            List<Object[]> results = session.createNativeQuery(sql, Object[].class)
                    .setParameter(1, dateFrom)
                    .setParameter(2, dateTo)
                    .getResultList();

            List<Payment> list = new ArrayList<>();

            for (Object[] row : results) {
                Payment p = new Payment();
                p.setId((Integer) row[0]);
                p.setDescription((String) row[1]);
                p.setTotal((BigDecimal) row[2]);
                p.setDate((String) row[3]);

                String mode = (String) row[4];
                if (mode != null) {
                    p.setPaymentMode(PaymentMode.fromCodigo(mode));
                } else {
                    p.setPaymentMode(PaymentMode.OU);
                }

                p.setReference((String) row[5]);

                // Cliente
                int clientId = (Integer) row[6];
                if (clientId > 0) {
                    Clients c = new Clients();
                    c.setId(clientId);
                    c.setName((String) row[7]);
                    p.setClient(c);
                }

                // Usuário
                int userId = (Integer) row[8];
                if (userId > 0) {
                    User u = new User();
                    u.setId(userId);
                    u.setName((String) row[9]);
                    p.setUser(u);
                }

                list.add(p);
            }

            return list;

        } catch (Exception e) {
            System.err.println("Erro ao listar receitas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public double getTotalReceitas(String dateFrom, String dateTo) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            String sql = """
                SELECT SUM(p.total) AS valor
                  FROM payments p
                 WHERE p.status='SUCCESS'
                   AND DATE(p.date) BETWEEN ? AND ?
            """;

            Double valor = (Double) session.createNativeQuery(sql)
                    .setParameter(1, dateFrom)
                    .setParameter(2, dateTo)
                    .uniqueResult();

            return valor != null ? valor : 0d;

        } catch (Exception e) {
            System.err.println("Erro ao calcular total de receitas: " + e.getMessage());
            return 0d;
        }
    }

    /**
     * Versão BigDecimal do total de receitas
     */
    public BigDecimal getTotalReceitasBigDecimal(String dateFrom, String dateTo) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            String sql = """
                SELECT SUM(p.total) AS total
                  FROM payments p
                 WHERE p.status='SUCCESS'
                   AND DATE(p.date) BETWEEN ? AND ?
            """;

            BigDecimal total = (BigDecimal) session.createNativeQuery(sql)
                    .setParameter(1, dateFrom)
                    .setParameter(2, dateTo)
                    .uniqueResult();

            return total != null ? total : BigDecimal.ZERO;

        } catch (Exception e) {
            System.err.println("Erro ao calcular total de receitas (BigDecimal): " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    // ==========================================================
    // 🔹 DESPESAS
    // ==========================================================
    public List<Expense> listDespesas(String dateFrom, String dateTo) {
        Session session = HibernateUtil.getCurrentSession();
        try {
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

            List<Object[]> results = session.createNativeQuery(sql, Object[].class)
                    .setParameter(1, dateFrom)
                    .setParameter(2, dateTo)
                    .getResultList();

            List<Expense> list = new ArrayList<>();

            for (Object[] row : results) {
                Expense e = new Expense();
                e.setId((Integer) row[0]);
                e.setDescription((String) row[1]);
                e.setTotal((BigDecimal) row[2]);
                e.setDate((String) row[3]);
                e.setDateFinish((String) row[4]);

                String mode = (String) row[5];
                if (mode != null) {
                    e.setPaymentMode(PaymentMode.fromCodigo(mode));
                } else {
                    e.setPaymentMode(PaymentMode.OU);
                }

                e.setReference((String) row[6]);
                e.setCurrency((String) row[7]);
                e.setNotes((String) row[8]);

                // Fornecedor
                int supplierId = (Integer) row[9];
                if (supplierId > 0) {
                    Supplier s = new Supplier();
                    s.setId(supplierId);
                    s.setName((String) row[10]);
                    e.setSupplier(s);
                }

                // Usuário
                int userId = (Integer) row[11];
                if (userId > 0) {
                    User u = new User();
                    u.setId(userId);
                    u.setName((String) row[12]);
                    e.setUser(u);
                }

                // Categoria
                int categoryId = (Integer) row[13];
                if (categoryId > 0) {
                    ExpenseCategory c = new ExpenseCategory();
                    c.setId(categoryId);
                    c.setName((String) row[14]);
                    e.setCategory(c);
                }

                list.add(e);
            }

            return list;

        } catch (Exception ex) {
            System.err.println("Erro ao listar despesas: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Calcula total de despesas por período
     */
    public BigDecimal getTotalDespesas(String dateFrom, String dateTo) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            String sql = """
                SELECT SUM(e.total) AS total
                  FROM expenses e
                 WHERE DATE(e.date) BETWEEN ? AND ?
            """;

            BigDecimal total = (BigDecimal) session.createNativeQuery(sql)
                    .setParameter(1, dateFrom)
                    .setParameter(2, dateTo)
                    .uniqueResult();

            return total != null ? total : BigDecimal.ZERO;

        } catch (Exception e) {
            System.err.println("Erro ao calcular total de despesas: " + e.getMessage());
            return BigDecimal.ZERO;
        }
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
            System.err.println("Erro ao calcular totais consolidados: " + e.getMessage());
        }

        return totais;
    }
}
