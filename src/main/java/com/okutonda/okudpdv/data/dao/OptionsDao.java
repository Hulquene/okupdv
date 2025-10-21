/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.data.entities.Options;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author kenny
 */
public class OptionsDao {
    private final Connection conn;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public OptionsDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public boolean add(Options obj) {
        try {
            // 1 passo
            String sql = "INSERT INTO options (name,value,status)"
                    + "values(?,?,?)";
            // 2 passo
            pst = this.conn.prepareStatement(sql);
            pst.setString(1, obj.getName());
            pst.setString(2, obj.getValue());
            pst.setString(3, obj.getStatus());
            //3 passo
            pst.execute();
            // 4 passo
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao salvar options: " + e.getMessage());
        }
        return false;
    }

    public boolean edit(Options obj) {
        try {
            // 1 passo
            String sql = "UPDATE options SET value=?,status=?  WHERE name=?";
            // 2 passo
            pst = this.conn.prepareStatement(sql);
//            pst.setString(1, obj.getName());
            pst.setString(1, obj.getValue());
            pst.setString(2, obj.getStatus());
            pst.setString(3, obj.getName());
            //3 passo
            //ptmt.executeQuery();
            pst.execute();
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao atualizar options: " + e.getMessage());
        }
        return false;
    }
    
    public Options searchFromName(String name) {
        try {
            // 1 passo
            String sql = "SELECT * FROM options WHERE name =?";
            pst = this.conn.prepareStatement(sql);
            pst.setString(1, name);
            rs = pst.executeQuery();
            Options obj = null;
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do options: " + e.getMessage());
        }
        return null;
    }

    public Options formatObj(ResultSet rs) {
        try {
            Options obj = new Options();
            obj.setId(rs.getInt("id"));
            obj.setValue(rs.getString("value"));
            obj.setName(rs.getString("name"));
            obj.setStatus(rs.getString("status"));
            return obj;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao formatar obj options: " + e.getMessage());
        }
        return null;
    }
}
