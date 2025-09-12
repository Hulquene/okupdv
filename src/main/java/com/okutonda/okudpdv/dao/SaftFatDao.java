/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import java.sql.*;

/**
 *
 * @author hr
 */
public class SaftFatDao {

    private final Connection conn;

    public SaftFatDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public long insertExport(Date start, Date end, String path, String status, String notes) throws SQLException {
        String sql = "INSERT INTO saft_exports (period_start, period_end, file_path, status, notes) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, start);
            ps.setDate(2, end);
            ps.setString(3, path);
            ps.setString(4, status);
            ps.setString(5, notes);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return -1;
    }
}
