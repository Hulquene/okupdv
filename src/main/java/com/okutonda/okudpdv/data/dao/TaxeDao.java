/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.data.entities.Taxes;
import java.awt.HeadlessException;
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
public class TaxeDao {

    private final Connection conn;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public TaxeDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public boolean add(Taxes obj) {
        try {
            // 1 passo
            String sql = "INSERT INTO taxes (name,code,percentage,isdefault)"
                    + "values(?,?,?,?)";
            // 2 passo
             pst = this.conn.prepareStatement(sql);
            pst.setString(1, obj.getName());
            pst.setString(2, obj.getCode());
            pst.setBigDecimal(3, obj.getPercetage());
            pst.setInt(4, obj.getIsDefault());
            //3 passo
            pst.execute();
            // 4 passo
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao salvar Taxes: " + e.getMessage());
        }
        return false;
    }
    
    public boolean edit(Taxes obj, int id) {
        try {
            // 1 passo
            String sql = "UPDATE taxes SET name=?,code=?,percentage=?,isdefault=? WHERE id=?";
            // 2 passo
             pst = this.conn.prepareStatement(sql);
            pst.setString(1, obj.getName());
            pst.setString(2, obj.getCode());
            pst.setBigDecimal(3, obj.getPercetage());
            pst.setInt(4, obj.getIsDefault());
            pst.setInt(5, id);
            //3 passo
            //ptmt.executeQuery();
            pst.execute();
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao atualizar taxes: " + e.getMessage());
        }
        return false;
    }

    public Taxes searchFromId(int id) {
        try {
            // 1 passo
            String sql = "SELECT * FROM taxes WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            Taxes obj = null;
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Taxes: " + e.getMessage());
        }
        return null;
    }

    public Taxes searchFromCode(String code) {
        try {
            // 1 passo
            String sql = "SELECT * FROM taxes WHERE code =?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, code);
            rs = pst.executeQuery();
            Taxes obj = null;
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Taxes: " + e.getMessage());
        }
        return null;
    }

    public List<Taxes> list(String where) {
        List<Taxes> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM taxes";
            PreparedStatement ptmt = this.conn.prepareStatement(sql);
            rs = ptmt.executeQuery();
            Taxes obj;
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de Taxes: " + e.getMessage());
        }
        return null;
    }
    
    public List<Taxes> filter(String txt) {
        List<Taxes> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM taxes WHERE name LIKE ?  OR code LIKE ? OR percentage LIKE ?";
//            String sql = "SELECT * FROM products WHERE description LIKE ?";
             pst = this.conn.prepareStatement(sql);
            pst.setString(1, "%" + txt + "%");
            pst.setString(2, "%" + txt + "%");
            pst.setString(3, "%" + txt + "%");
            rs = pst.executeQuery();
            Taxes obj;// = new Product();
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de produtos: " + e.getMessage());
        }
        return null;
    }

    public Boolean delete(int id) {
        try {
            // 1 passo
            String sql = "DELETE FROM taxes WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir Taxes: " + e.getMessage());
        }
        return false;
    }

    public Taxes formatObj(ResultSet rs) {
        try {
            Taxes obj = new Taxes();
            obj.setId(rs.getInt("id"));
            obj.setCode(rs.getString("code"));
            obj.setName(rs.getString("name"));
            obj.setPercetage(rs.getBigDecimal("percentage"));
            obj.setIsDefault(rs.getInt("isdefault"));
            return obj;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao formatar obj taxes: " + e.getMessage());
        }
        return null;
    }
}
