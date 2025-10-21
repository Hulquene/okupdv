/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.data.entities.ExportSaftFat;
import com.okutonda.okudpdv.data.entities.User;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author hr
 */
public class SaftFatDao {

    private final Connection conn;

    public SaftFatDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    // Mantém o método antigo: redireciona para o overload novo passando null em exported_by
    public long insertExport(Date start, Date end, String path, String status, String notes) throws SQLException {
        return insertExport(start, end, path, status, notes, null);
    }

    // NOVO: overload com exported_by
    public long insertExport(Date start, Date end, String path, String status, String notes, Integer exportedBy) throws SQLException {
        String sql = "INSERT INTO saft_exports (period_start, period_end, file_path, status, notes, exported_by) "
                + "VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, start);
            ps.setDate(2, end);
            ps.setString(3, path);
            ps.setString(4, status);
            ps.setString(5, notes);
            if (exportedBy == null) {
                ps.setNull(6, Types.INTEGER);
            } else {
                ps.setInt(6, exportedBy);
            }
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return -1;
    }

    // ====== GET ALL ======
    public List<ExportSaftFat> getAll() {
        List<ExportSaftFat> list = new ArrayList<>();
        String sql = """
            SELECT id, period_start, period_end, file_path, status, notes,
                   created_at, exported_by
            FROM saft_exports
            ORDER BY created_at DESC, id DESC
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar saft_exports: " + e.getMessage());
        }
        return list;
    }

    // ====== GET BY ID ======
    public ExportSaftFat getId(long id) {
        String sql = """
            SELECT id, period_start, period_end, file_path, status, notes,
                   created_at, exported_by
            FROM saft_exports WHERE id=?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar saft_export: " + e.getMessage());
        }
        return null;
    }

    // ====== FILTER por TEXTO (file_path, status, notes, e nome do utilizador via JOIN) ======
    public List<ExportSaftFat> filter(String txt) {
        List<ExportSaftFat> list = new ArrayList<>();
        String like = "%" + (txt == null ? "" : txt.trim()) + "%";
        String sql = """
            SELECT se.id, se.period_start, se.period_end, se.file_path, se.status, se.notes,
                   se.created_at, se.exported_by
            FROM saft_exports se
            LEFT JOIN users u ON u.id = se.exported_by
            WHERE COALESCE(se.file_path,'') LIKE ?
               OR COALESCE(se.status,'') LIKE ?
               OR COALESCE(se.notes,'') LIKE ?
               OR COALESCE(u.name,'') LIKE ?
            ORDER BY se.created_at DESC, se.id DESC
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao filtrar saft_exports: " + e.getMessage());
        }
        return list;
    }

    // ====== FILTER por CREATED_AT (intervalo do registo/log) ======
    public List<ExportSaftFat> filterByCreatedAt(LocalDate from, LocalDate to) {
        List<ExportSaftFat> list = new ArrayList<>();
        String sql = """
            SELECT id, period_start, period_end, file_path, status, notes,
                   created_at, exported_by
            FROM saft_exports
            WHERE DATE(created_at) BETWEEN ? AND ?
            ORDER BY created_at DESC, id DESC
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao filtrar por created_at: " + e.getMessage());
        }
        return list;
    }

    // ====== FILTER por PERÍODO SAF-T (interseção) ======
    public List<ExportSaftFat> filterByPeriod(LocalDate from, LocalDate to) {
        List<ExportSaftFat> list = new ArrayList<>();
        String sql = """
            SELECT id, period_start, period_end, file_path, status, notes,
                   created_at, exported_by
            FROM saft_exports
            WHERE period_start <= ? AND period_end >= ?
            ORDER BY period_start DESC, id DESC
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(to));     // start <= to
            ps.setDate(2, Date.valueOf(from));   // end   >= from
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao filtrar por período: " + e.getMessage());
        }
        return list;
    }

    /**
     * (Opcional) Filtrar por usuário específico
     */
    public List<ExportSaftFat> filterByUser(Integer userId) {
        List<ExportSaftFat> list = new ArrayList<>();
        String sql = """
            SELECT id, period_start, period_end, file_path, status, notes,
                   created_at, exported_by
            FROM saft_exports
            WHERE exported_by = ?
            ORDER BY created_at DESC, id DESC
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (userId == null) {
                ps.setNull(1, Types.INTEGER);
            } else {
                ps.setInt(1, userId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao filtrar por usuário: " + e.getMessage());
        }
        return list;
    }
    // ====== MAP ResultSet → Model ======

    private ExportSaftFat map(ResultSet rs) throws SQLException {
        ExportSaftFat m = new ExportSaftFat();
        m.setId(rs.getLong("id"));

        Date ps = rs.getDate("period_start");
        Date pe = rs.getDate("period_end");
        Timestamp ca = rs.getTimestamp("created_at");

        m.setPeriodStart(ps != null ? ps.toLocalDate() : null);
        m.setPeriodEnd(pe != null ? pe.toLocalDate() : null);
        m.setFilePath(rs.getString("file_path"));
        m.setStatus(rs.getString("status"));
        m.setNotes(rs.getString("notes"));
        m.setCreatedAt(ca != null ? ca.toLocalDateTime() : null);

        int exportedBy = rs.getInt("exported_by");
        if (!rs.wasNull()) {
            // carrega o utilizador via UserDao
            UserDao udao = new UserDao();
            User u = udao.getId(exportedBy);
            m.setUser(u);
        } else {
            m.setUser(null);
        }
        return m;
    }
}
