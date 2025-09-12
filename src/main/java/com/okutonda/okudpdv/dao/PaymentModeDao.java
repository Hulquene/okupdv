/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.models.PaymentModes;
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
public class PaymentModeDao {

    private final Connection conn;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public PaymentModeDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public Boolean add(PaymentModes obj) {
        try {
            // 1 passo
            String sql = "INSERT INTO payment_modes (name,description,code,status,isDefault)"
                    + "values(?,?,?,?,?)";
            // 2 passo
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, obj.getName());
            stmt.setString(2, obj.getDescription());
            stmt.setString(3, obj.getCode());
            stmt.setInt(4, obj.getStatus());
            stmt.setInt(5, obj.getIsDefault());
            //3 passo
            stmt.execute();
            // 4 passo
            stmt.close();
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao salvar payment_modes" + e.getMessage());
//            JOptionPane.showMessageDialog(null, "Erro ao salvar client: " + e.getMessage());
        }
        return false;
    }

    public Boolean edit(PaymentModes obj, int id) {
        try {
            // 1 passo
            String sql = "UPDATE payment_modes SET name=?,description=?,code=?,status=?,isDefault=? WHERE id=?";
            // 2 passo
            pst = this.conn.prepareStatement(sql);
            pst.setString(1, obj.getName());
            pst.setString(2, obj.getDescription());
            pst.setString(3, obj.getCode());
            pst.setInt(4, obj.getStatus());
            pst.setInt(5, obj.getIsDefault());
            pst.setInt(6, id);
            //3 passo
            //ptmt.executeQuery();
            pst.execute();
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao atualizar payment_modes" + e.getMessage());
//            JOptionPane.showMessageDialog(null, "Erro ao atualizar client: " + e.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM payment_modes WHERE id =?";
        try {
            // 1 passo
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.execute();
            pst.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir payment_modes: " + e.getMessage());
        }
        return false;
    }

    public PaymentModes searchFromName(String name) {
        try {
            // 1 passo
            String sql = "SELECT * FROM payment_modes WHERE company =?";
            this.pst = conn.prepareStatement(sql);
            this.pst.setString(1, name);
            this.rs = this.pst.executeQuery();
            PaymentModes obj = new PaymentModes();
            if (rs.next()) {
                obj = formatObj(rs);
            }
//            if (rs.next()) {
//                obj.setId(rs.getInt("id"));
//                obj.setNif(rs.getString("nif"));
//                obj.setName(rs.getString("company"));
//                obj.setEmail(rs.getString("email"));
//                obj.setPhone(rs.getString("phone"));
//                obj.setAddress(rs.getString("address"));
//                obj.setCountry(rs.getInt("country"));
//                obj.setCity(rs.getString("city"));
//                obj.setState(rs.getString("state"));
//                obj.setZipCode(rs.getString("zip_code"));
//                obj.setStatus(rs.getInt("status"));
//                obj.setIsDefault(rs.getInt("isdefault"));
//            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do payment_modes: " + e.getMessage());
        }
        return null;
    }

    public PaymentModes getId(int id) {
        try {
            // 1 passo
            String sql = "SELECT * FROM payment_modes WHERE id =?";
            this.pst = conn.prepareStatement(sql);
            this.pst.setInt(1, id);
            rs = this.pst.executeQuery();
            PaymentModes obj = new PaymentModes();
            if (rs.next()) {
                obj = formatObj(rs);
            }
//            if (rs.next()) {
//                obj.setId(rs.getInt("id"));
//                obj.setNif(rs.getString("nif"));
//                obj.setName(rs.getString("company"));
//                obj.setEmail(rs.getString("email"));
//                obj.setPhone(rs.getString("phone"));
//                obj.setAddress(rs.getString("address"));
//                obj.setCountry(rs.getInt("country"));
//                obj.setCity(rs.getString("city"));
//                obj.setState(rs.getString("state"));
//                obj.setZipCode(rs.getString("zip_code"));
//                obj.setStatus(rs.getInt("status"));
//                obj.setIsDefault(rs.getInt("isdefault"));
//            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do payment_modes: " + e.getMessage());
        }
        return null;
    }

    public PaymentModes searchFromNif(String nif) {
        try {
            // 1 passo
            String sql = "SELECT * FROM payment_modes WHERE nif =?";
            this.pst = conn.prepareStatement(sql);
            this.pst.setString(1, nif);
            rs = this.pst.executeQuery();
            PaymentModes obj = new PaymentModes();
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do payment_modes: " + e.getMessage());
        }
        return null;
    }

    public PaymentModes getClientDefault() {
        try {
            // 1 passo
            String sql = "SELECT * FROM payment_modes WHERE isdefault =1";
            this.pst = conn.prepareStatement(sql);
            rs = this.pst.executeQuery();
            PaymentModes obj = new PaymentModes();
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do payment_modes: " + e.getMessage());
        }
        return null;
    }

    public List<PaymentModes> list(String where) {
        List<PaymentModes> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM payment_modes";
            this.pst = this.conn.prepareStatement(sql);
            rs = this.pst.executeQuery();
            PaymentModes obj;
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de payment_modes: " + e.getMessage());
        }
        return null;
    }

    public List<PaymentModes> filterClient(String txt) {
        List<PaymentModes> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM payment_modes WHERE company LIKE ? OR nif LIKE ? OR city LIKE ?";
            this.pst = this.conn.prepareStatement(sql);
            this.pst.setString(1, "%" + txt + "%");
            this.pst.setString(2, "%" + txt + "%");
            this.pst.setString(3, "%" + txt + "%");
            rs = this.pst.executeQuery();
            PaymentModes obj;
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de payment_modes: " + e.getMessage());
        }
        return null;
    }

    public PaymentModes formatObj(ResultSet rs) {
        try {
            PaymentModes obj = new PaymentModes();
//            Countries country;
//            CountryDao sDao = new CountryDao();
//            country = sDao.searchFromId(rs.getInt("country"));
            obj.setId(rs.getInt("id"));
            obj.setName(rs.getString("name"));
            obj.setDescription(rs.getString("description"));
            obj.setCode(rs.getString("code"));
            obj.setStatus(rs.getInt("status"));
            obj.setIsDefault(rs.getInt("isDefault"));
            return obj;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao formatar obj payment_modes: " + e.getMessage());
        }
        return null;
    }
}
