/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.models.Warehouse;
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
public class WarehouseDao {

    private final Connection conn;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public WarehouseDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public boolean add(Warehouse obj) {
        try {
            // 1 passo
            String sql = "INSERT INTO warehouse (name,address)"
                    + "values(?,?)";
            // 2 passo
            pst = this.conn.prepareStatement(sql);
            pst.setString(1, obj.getName());
            pst.setString(2, obj.getAddress());
            pst.execute();
            // 4 passo
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao salvar Warehouse: " + e.getMessage());
        }
        return false;
    }
    
    
    public boolean edit(Warehouse obj, int id) {
        try {
            // 1 passo
            String sql = "UPDATE warehouse SET name=?,address=? WHERE id=?";
            // 2 passo
            pst = this.conn.prepareStatement(sql);
            pst.setString(1, obj.getName());
            pst.setString(2, obj.getAddress());
            pst.setInt(3, id);
            //3 passo
            //ptmt.executeQuery();
            pst.execute();
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao atualizar Warehouse: " + e.getMessage());
        }
        return false;
    }

    public List<Warehouse> list(String where) {
        List<Warehouse> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM warehouse";
            pst = this.conn.prepareStatement(sql);
            rs = pst.executeQuery();
            Warehouse obj;
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de Warehouse: " + e.getMessage());
        }
        return null;
    }
    
    public Warehouse searchFromId(int id) {
        try {
            // 1 passo
            String sql = "SELECT * FROM warehouse WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            Warehouse obj = null;
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Warehouse: " + e.getMessage());
        }
        return null;
    }
     public List<Warehouse> filter(String txt) {
        List<Warehouse> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM warehouse WHERE name LIKE ?  OR address LIKE ?";
            pst = this.conn.prepareStatement(sql);
            pst.setString(1, "%" + txt + "%");
            pst.setString(2, "%" + txt + "%");
            rs = pst.executeQuery();
            Warehouse obj;// = new Product();
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de warehouse: " + e.getMessage());
        }
        return null;
    }
    public Boolean delete(int id) {
        try {
            // 1 passo
            String sql = "DELETE FROM warehouse WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir Warehouse: " + e.getMessage());
        }
        return false;
    }
    
    
    public Warehouse formatObj(ResultSet rs) {
        try {
            Warehouse obj = new Warehouse();
            obj.setId(rs.getInt("id"));
            obj.setName(rs.getString("name"));
            obj.setAddress(rs.getString("address"));
            return obj;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao formatar obj Warehouse: " + e.getMessage());
        }
        return null;
    }
}
