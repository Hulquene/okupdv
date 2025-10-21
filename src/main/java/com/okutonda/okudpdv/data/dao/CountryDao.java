/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.data.entities.Countries;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author kenny
 */
public class CountryDao {

    private final Connection conn;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public CountryDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public Countries searchFromId(int id) {
        try {
            // 1 passo
            String sql = "SELECT * FROM countries WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            Countries obj = null;
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do countries: " + e.getMessage());
        }
        return null;
    }

    public List<Countries> list(String where) {
        List<Countries> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM countries";
            PreparedStatement ptmt = this.conn.prepareStatement(sql);
            rs = ptmt.executeQuery();
            Countries obj;
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de countries: " + e.getMessage());
        }
        return null;
    }

    public List<Countries> filter(String txt) {
        List<Countries> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM countries WHERE short_name LIKE ? OR long_name LIKE ? OR iso2 LIKE ?";
            PreparedStatement ptmt = this.conn.prepareStatement(sql);
            ptmt.setString(1, "%" + txt + "%");
            ptmt.setString(2, "%" + txt + "%");
            ptmt.setString(3, "%" + txt + "%");
            rs = ptmt.executeQuery();
            Countries obj;
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de countries: " + e.getMessage());
        }
        return null;
    }

    public Countries formatObj(ResultSet rs) {
        try {
            Countries obj = new Countries();
            obj.setId(rs.getInt("id"));
            obj.setIso2(rs.getString("iso2"));
            obj.setIso3(rs.getString("iso3"));
            obj.setShort_name(rs.getString("short_name"));
            obj.setLong_name(rs.getString("long_name"));
            obj.setUn_member(rs.getString("un_member"));
            obj.setNumcode(rs.getString("numcode"));
            obj.setCalling_code(rs.getString("calling_code"));
            obj.setCctld(rs.getString("cctld"));
            return obj;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao formatar obj Countries: " + e.getMessage());
        }
        return null;
    }
}
