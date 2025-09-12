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
public class SettingsDao {

    private final Connection conn;

    public SettingsDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public String getValue(String key) {
        String sql = "SELECT value FROM settings WHERE `key`=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, key);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        } catch (SQLException ignore) {
        }
        return "";
    }
}
