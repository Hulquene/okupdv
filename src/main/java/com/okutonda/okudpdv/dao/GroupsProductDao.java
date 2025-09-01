/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.models.GroupsProduct;
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
public class GroupsProductDao {

    private final Connection conn;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public GroupsProductDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public boolean add(GroupsProduct obj) {
        try {
            // 1 passo
            String sql = "INSERT INTO groups_product (name,code)"
                    + "values(?,?)";
            // 2 passo
            pst = this.conn.prepareStatement(sql);
            pst.setString(1, obj.getName());
            pst.setString(2, obj.getCode());
            //3 passo
            pst.execute();
            // 4 passo
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao salvar Group: " + e.getMessage());
        }
        return false;
    }

    public boolean edit(GroupsProduct obj, int id) {
        try {
            // 1 passo
            String sql = "UPDATE groups_product SET name=?,code=? WHERE id=?";
            // 2 passo
            pst = this.conn.prepareStatement(sql);
            pst.setString(1, obj.getName());
            pst.setString(2, obj.getCode());
            pst.setInt(3, id);
            //3 passo
            //ptmt.executeQuery();
            pst.execute();
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao atualizar groups_product: " + e.getMessage());
        }
        return false;
    }

    public GroupsProduct searchFromId(int id) {
        try {
            // 1 passo
            String sql = "SELECT * FROM groups_product WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            GroupsProduct obj = null;
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do groups_product: " + e.getMessage());
        }
        return null;
    }

    public GroupsProduct searchFromCode(String code) {
        try {
            // 1 passo
            String sql = "SELECT * FROM groups_product WHERE code =?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, code);
            rs = pst.executeQuery();
            GroupsProduct obj = null;
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do groups_product: " + e.getMessage());
        }
        return null;
    }

    public List<GroupsProduct> list(String where) {
        List<GroupsProduct> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM groups_product";
            PreparedStatement ptmt = this.conn.prepareStatement(sql);
            rs = ptmt.executeQuery();
            GroupsProduct obj;
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de groups_product: " + e.getMessage());
        }
        return null;
    }

    public List<GroupsProduct> filter(String txt) {
        List<GroupsProduct> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM groups_product WHERE name LIKE ?  OR code LIKE ?";
//            String sql = "SELECT * FROM products WHERE description LIKE ?";
            pst = this.conn.prepareStatement(sql);
            pst.setString(1, "%" + txt + "%");
            pst.setString(2, "%" + txt + "%");
            rs = pst.executeQuery();
            GroupsProduct obj;// = new Product();
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de groups_product: " + e.getMessage());
        }
        return null;
    }

    public Boolean delete(int id) {
        try {
            // 1 passo
            String sql = "DELETE FROM groups_product WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir groups_product: " + e.getMessage());
        }
        return false;
    }

    public GroupsProduct formatObj(ResultSet rs) {
        try {
            GroupsProduct obj = new GroupsProduct();
            obj.setId(rs.getInt("id"));
            obj.setCode(rs.getString("code"));
            obj.setName(rs.getString("name"));
            return obj;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao formatar obj groups_product: " + e.getMessage());
        }
        return null;
    }
}
