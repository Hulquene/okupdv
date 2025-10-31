package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import org.hibernate.Session;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import java.util.*;

/**
 * DAO responsável por consultas e relatórios personalizados.
 *
 * Usa Hibernate para relatórios e consultas agregadas que não pertencem a um
 * único módulo.
 *
 * Nenhuma regra de negócio deve existir aqui.
 *
 * @author Hulquene
 */
public class ReportDao {

    /**
     * Retorna total de vendas agrupado por produto.
     */
    public List<Map<String, Object>> getSalesByProduct(String dateFrom, String dateTo) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            String sql = """
                SELECT 
                    p.description AS produto,
                    SUM(po.qty) AS quantidade,
                    SUM(po.total) AS total
                  FROM product_order po
                  JOIN products p ON po.product_id = p.id
                  JOIN orders o ON po.order_id = o.id
                 WHERE DATE(o.datecreate) BETWEEN ? AND ?
              GROUP BY p.id, p.description
              ORDER BY total DESC
            """;

            List<Tuple> results = session.createNativeQuery(sql, Tuple.class)
                    .setParameter(1, dateFrom)
                    .setParameter(2, dateTo)
                    .getResultList();

            List<Map<String, Object>> list = new ArrayList<>();
            for (Tuple tuple : results) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("produto", tuple.get("produto"));
                row.put("quantidade", tuple.get("quantidade"));
                row.put("total", tuple.get("total"));
                list.add(row);
            }
            return list;

        } catch (Exception e) {
            System.err.println("Erro ao buscar relatório de vendas por produto: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Retorna total de vendas agrupado por vendedor.
     */
    public List<Map<String, Object>> getSalesBySeller(String dateFrom, String dateTo) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            String sql = """
                SELECT 
                    u.name AS vendedor,
                    COUNT(o.id) AS total_faturas,
                    SUM(o.total) AS total_vendas
                  FROM orders o
                  JOIN users u ON o.user_id = u.id
                 WHERE DATE(o.datecreate) BETWEEN ? AND ?
              GROUP BY u.id, u.name
              ORDER BY total_vendas DESC
            """;

            List<Tuple> results = session.createNativeQuery(sql, Tuple.class)
                    .setParameter(1, dateFrom)
                    .setParameter(2, dateTo)
                    .getResultList();

            List<Map<String, Object>> list = new ArrayList<>();
            for (Tuple tuple : results) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("vendedor", tuple.get("vendedor"));
                row.put("total_faturas", tuple.get("total_faturas"));
                row.put("total_vendas", tuple.get("total_vendas"));
                list.add(row);
            }
            return list;

        } catch (Exception e) {
            System.err.println("Erro ao buscar relatório de vendas por vendedor: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Retorna resumo de turno (Shift): total recebido, despesas, saldo.
     */
    public Map<String, Object> getShiftSummary(int shiftId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            String sql = """
                SELECT 
                    s.id,
                    s.code,
                    s.dateOpen,
                    s.dateClose,
                    s.granted_amount,
                    s.incurred_amount,
                    s.closing_amount,
                    s.status,
                    u.name AS operador
                  FROM shift s
                  JOIN users u ON s.user_id = u.id
                 WHERE s.id = ?
            """;

            Tuple result = (Tuple) session.createNativeQuery(sql, Tuple.class)
                    .setParameter(1, shiftId)
                    .uniqueResult();

            if (result != null) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", result.get("id"));
                map.put("codigo", result.get("code"));
                map.put("operador", result.get("operador"));
                map.put("data_abertura", result.get("dateOpen"));
                map.put("data_fecho", result.get("dateClose"));
                map.put("montante_inicial", result.get("granted_amount"));
                map.put("montante_incorrido", result.get("incurred_amount"));
                map.put("montante_final", result.get("closing_amount"));
                map.put("status", result.get("status"));
                return map;
            }
            return Collections.emptyMap();

        } catch (Exception e) {
            System.err.println("Erro ao buscar resumo do turno: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    /**
     * Retorna lista de turnos encerrados com totais agregados.
     */
    public List<Map<String, Object>> listClosedShifts(String dateFrom, String dateTo) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            String sql = """
                SELECT 
                    s.code,
                    u.name AS operador,
                    s.dateOpen,
                    s.dateClose,
                    s.closing_amount
                  FROM shift s
                  JOIN users u ON s.user_id = u.id
                 WHERE s.status='close'
                   AND DATE(s.dateClose) BETWEEN ? AND ?
              ORDER BY s.dateClose DESC
            """;

            List<Tuple> results = session.createNativeQuery(sql, Tuple.class)
                    .setParameter(1, dateFrom)
                    .setParameter(2, dateTo)
                    .getResultList();

            List<Map<String, Object>> list = new ArrayList<>();
            for (Tuple tuple : results) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("codigo_turno", tuple.get("code"));
                row.put("operador", tuple.get("operador"));
                row.put("abertura", tuple.get("dateOpen"));
                row.put("fecho", tuple.get("dateClose"));
                row.put("total", tuple.get("closing_amount"));
                list.add(row);
            }
            return list;

        } catch (Exception e) {
            System.err.println("Erro ao buscar histórico de turnos: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Método adicional: Relatório de produtos mais vendidos
     */
    public List<Map<String, Object>> getTopProducts(String dateFrom, String dateTo, int limit) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            String sql = """
                SELECT 
                    p.description AS produto,
                    SUM(po.qty) AS quantidade,
                    SUM(po.total) AS total_vendido
                  FROM product_order po
                  JOIN products p ON po.product_id = p.id
                  JOIN orders o ON po.order_id = o.id
                 WHERE DATE(o.datecreate) BETWEEN ? AND ?
              GROUP BY p.id, p.description
              ORDER BY quantidade DESC
                 LIMIT ?
            """;

            List<Tuple> results = session.createNativeQuery(sql, Tuple.class)
                    .setParameter(1, dateFrom)
                    .setParameter(2, dateTo)
                    .setParameter(3, limit)
                    .getResultList();

            List<Map<String, Object>> list = new ArrayList<>();
            for (Tuple tuple : results) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("produto", tuple.get("produto"));
                row.put("quantidade", tuple.get("quantidade"));
                row.put("total_vendido", tuple.get("total_vendido"));
                list.add(row);
            }
            return list;

        } catch (Exception e) {
            System.err.println("Erro ao buscar top produtos: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Método adicional: Relatório de clientes que mais compram
     */
    public List<Map<String, Object>> getTopClients(String dateFrom, String dateTo, int limit) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            String sql = """
                SELECT 
                    c.company AS cliente,
                    COUNT(o.id) AS total_compras,
                    SUM(o.total) AS total_gasto
                  FROM orders o
                  JOIN clients c ON o.client_id = c.id
                 WHERE DATE(o.datecreate) BETWEEN ? AND ?
              GROUP BY c.id, c.company
              ORDER BY total_gasto DESC
                 LIMIT ?
            """;

            List<Tuple> results = session.createNativeQuery(sql, Tuple.class)
                    .setParameter(1, dateFrom)
                    .setParameter(2, dateTo)
                    .setParameter(3, limit)
                    .getResultList();

            List<Map<String, Object>> list = new ArrayList<>();
            for (Tuple tuple : results) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("cliente", tuple.get("cliente"));
                row.put("total_compras", tuple.get("total_compras"));
                row.put("total_gasto", tuple.get("total_gasto"));
                list.add(row);
            }
            return list;

        } catch (Exception e) {
            System.err.println("Erro ao buscar top clientes: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Método adicional: Relatório diário de vendas
     */
    public List<Map<String, Object>> getDailySales(String dateFrom, String dateTo) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            String sql = """
                SELECT 
                    DATE(o.datecreate) AS data,
                    COUNT(o.id) AS total_pedidos,
                    SUM(o.total) AS total_vendas
                  FROM orders o
                 WHERE DATE(o.datecreate) BETWEEN ? AND ?
              GROUP BY DATE(o.datecreate)
              ORDER BY data DESC
            """;

            List<Tuple> results = session.createNativeQuery(sql, Tuple.class)
                    .setParameter(1, dateFrom)
                    .setParameter(2, dateTo)
                    .getResultList();

            List<Map<String, Object>> list = new ArrayList<>();
            for (Tuple tuple : results) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("data", tuple.get("data"));
                row.put("total_pedidos", tuple.get("total_pedidos"));
                row.put("total_vendas", tuple.get("total_vendas"));
                list.add(row);
            }
            return list;

        } catch (Exception e) {
            System.err.println("Erro ao buscar relatório diário de vendas: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
