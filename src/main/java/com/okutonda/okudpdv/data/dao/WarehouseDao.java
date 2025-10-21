/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.data.entities.Warehouse;
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

    public WarehouseDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public boolean add(Warehouse obj) {
        try {
            String sql = "INSERT INTO warehouses (name, location, description, status) VALUES (?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, obj.getName());
            pst.setString(2, obj.getLocation());
            pst.setString(3, obj.getDescription());
            pst.setInt(4, obj.getStatus());
            pst.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar Warehouse: " + e.getMessage());
        }
        return false;
    }

    public boolean edit(Warehouse obj, int id) {
        try {
            String sql = "UPDATE warehouses SET name=?, location=?, description=?, status=? WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, obj.getName());
            pst.setString(2, obj.getLocation());
            pst.setString(3, obj.getDescription());
            pst.setInt(4, obj.getStatus());
            pst.setInt(5, id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar Warehouse: " + e.getMessage());
        }
        return false;
    }

    public List<Warehouse> list(String where) {
        List<Warehouse> list = new ArrayList<>();
        String sql = "SELECT * FROM warehouses " + (where == null ? "" : where);
        try (PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(formatObj(rs));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar Warehouses: " + e.getMessage());
        }
        return list;
    }

    public Warehouse searchFromId(int id) {
        try {
            String sql = "SELECT * FROM warehouses WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return formatObj(rs);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar Warehouse: " + e.getMessage());
        }
        return null;
    }

    public List<Warehouse> filter(String txt) {
        List<Warehouse> list = new ArrayList<>();
        String sql = "SELECT * FROM warehouses WHERE name LIKE ? OR location LIKE ? OR description LIKE ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            String like = "%" + txt + "%";
            pst.setString(1, like);
            pst.setString(2, like);
            pst.setString(3, like);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(formatObj(rs));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao filtrar Warehouses: " + e.getMessage());
        }
        return list;
    }

    public Boolean delete(int id) {
        try {
            String sql = "DELETE FROM warehouses WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir Warehouse: " + e.getMessage());
        }
        return false;
    }

    private Warehouse formatObj(ResultSet rs) throws SQLException {
        Warehouse obj = new Warehouse();
        obj.setId(rs.getInt("id"));
        obj.setName(rs.getString("name"));
        obj.setLocation(rs.getString("location"));
        obj.setDescription(rs.getString("description"));
        obj.setStatus(rs.getInt("status"));
        obj.setCreatedAt(rs.getTimestamp("created_at"));
        obj.setUpdatedAt(rs.getTimestamp("updated_at"));
        return obj;
    }
}
