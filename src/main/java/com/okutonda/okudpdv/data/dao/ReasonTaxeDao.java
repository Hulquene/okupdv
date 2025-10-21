/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.data.entities.ReasonTaxes;
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
public class ReasonTaxeDao {
    
    private final Connection conn;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public ReasonTaxeDao() {
        this.conn = ConnectionDatabase.getConnect();
    }
    
    public ReasonTaxes searchFromId(int id) {
        try {
            // 1 passo
            String sql = "SELECT * FROM reason_taxes WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            ReasonTaxes obj = null;
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do ReasonTaxes: " + e.getMessage());
        }
        return null;
    }
    public ReasonTaxes searchFromCode(String code) {
        try {
            // 1 passo
            String sql = "SELECT * FROM reason_taxes WHERE code =?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, code);
            rs = pst.executeQuery();
            ReasonTaxes obj = null;
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do ReasonTaxes: " + e.getMessage());
        }
        return null;
    }

    public List<ReasonTaxes> list(String where) {
        List<ReasonTaxes> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM reason_taxes";
            PreparedStatement ptmt = this.conn.prepareStatement(sql);
            rs = ptmt.executeQuery();
            ReasonTaxes obj;
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de ReasonTaxes: " + e.getMessage());
        }
        return null;
    }

    public ReasonTaxes formatObj(ResultSet rs) {
        try {
            ReasonTaxes obj = new ReasonTaxes();
            obj.setId(rs.getInt("id"));
            obj.setCode(rs.getString("code"));
            obj.setDescription(rs.getString("description"));
            obj.setReason(rs.getString("reason"));
            obj.setStandard(rs.getString("standard"));
            return obj;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao formatar obj Countries: " + e.getMessage());
        }
        return null;
    }
}
