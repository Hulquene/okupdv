package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.connection.DatabaseProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * DAO responsável por consultas e relatórios personalizados.
 *
 * Usa conexão direta via DatabaseProvider (HikariCP), ideal para relatórios e
 * consultas agregadas que não pertencem a um único módulo.
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

        try (Connection conn = DatabaseProvider.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, dateFrom);
            pst.setString(2, dateTo);

            try (ResultSet rs = pst.executeQuery()) {
                List<Map<String, Object>> list = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("produto", rs.getString("produto"));
                    row.put("quantidade", rs.getDouble("quantidade"));
                    row.put("total", rs.getDouble("total"));
                    list.add(row);
                }
                return list;
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao buscar relatório de vendas por produto: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * Retorna total de vendas agrupado por vendedor.
     */
    public List<Map<String, Object>> getSalesBySeller(String dateFrom, String dateTo) {
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

        try (Connection conn = DatabaseProvider.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, dateFrom);
            pst.setString(2, dateTo);

            try (ResultSet rs = pst.executeQuery()) {
                List<Map<String, Object>> list = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("vendedor", rs.getString("vendedor"));
                    row.put("total_faturas", rs.getInt("total_faturas"));
                    row.put("total_vendas", rs.getDouble("total_vendas"));
                    list.add(row);
                }
                return list;
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao buscar relatório de vendas por vendedor: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * Retorna resumo de turno (Shift): total recebido, despesas, saldo.
     */
    public Map<String, Object> getShiftSummary(int shiftId) {
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

        try (Connection conn = DatabaseProvider.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, shiftId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("id", rs.getInt("id"));
                    map.put("codigo", rs.getString("code"));
                    map.put("operador", rs.getString("operador"));
                    map.put("data_abertura", rs.getString("dateOpen"));
                    map.put("data_fecho", rs.getString("dateClose"));
                    map.put("montante_inicial", rs.getDouble("granted_amount"));
                    map.put("montante_incorrido", rs.getDouble("incurred_amount"));
                    map.put("montante_final", rs.getDouble("closing_amount"));
                    map.put("status", rs.getString("status"));
                    return map;
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao buscar resumo do turno: " + e.getMessage());
        }
        return Collections.emptyMap();
    }

    /**
     * Retorna lista de turnos encerrados com totais agregados.
     */
    public List<Map<String, Object>> listClosedShifts(String dateFrom, String dateTo) {
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

        try (Connection conn = DatabaseProvider.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, dateFrom);
            pst.setString(2, dateTo);

            try (ResultSet rs = pst.executeQuery()) {
                List<Map<String, Object>> list = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("codigo_turno", rs.getString("code"));
                    row.put("operador", rs.getString("operador"));
                    row.put("abertura", rs.getString("dateOpen"));
                    row.put("fecho", rs.getString("dateClose"));
                    row.put("total", rs.getDouble("closing_amount"));
                    list.add(row);
                }
                return list;
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao buscar histórico de turnos: " + e.getMessage());
        }
        return Collections.emptyList();
    }
}
