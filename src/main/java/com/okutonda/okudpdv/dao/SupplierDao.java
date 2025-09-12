/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.models.Countries;
import com.okutonda.okudpdv.models.Supplier;
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
public class SupplierDao {

    private final Connection conn;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public SupplierDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public Boolean add(Supplier obj) {
        try {
            // 1 passo
            String sql = "INSERT INTO suppliers (company,nif,phone,email,address,city,zip_code,group_id,status,isdefault)"
                    + "values(?,?,?,?,?,?,?,?,?,?)";
            // 2 passo
            pst = this.conn.prepareStatement(sql);
            pst.setString(1, obj.getName());
            pst.setString(2, obj.getNif());
            pst.setString(3, obj.getPhone());
            pst.setString(4, obj.getEmail());
            pst.setString(5, obj.getAddress());
            pst.setString(6, obj.getCity());
//            pst.setString(7, obj.getState());
//            pst.setInt(8, obj.getCountry().getId());
            pst.setString(7, obj.getZipCode());
            pst.setInt(8, obj.getGroupId());
            pst.setInt(9, obj.getStatus());
            pst.setInt(10, obj.getIsDefault());
            //3 passo
            pst.execute();
            // 4 passo
            pst.close();
            return true;
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar Supplier: " + e.getMessage());
        }
        return false;
    }

    public Boolean edit(Supplier obj, int id) {
        try {
            // 1 passo
            String sql = "UPDATE suppliers SET company=?,nif=?,phone=?,email=?,address=?,city=?,zip_code=?,group_id=?,status=?,isdefault=? WHERE id=?";
            // 2 passo
            pst = this.conn.prepareStatement(sql);
            pst.setString(1, obj.getName());
            pst.setString(2, obj.getNif());
            pst.setString(3, obj.getPhone());
            pst.setString(4, obj.getEmail());
            pst.setString(5, obj.getAddress());
            pst.setString(6, obj.getCity());
//            pst.setString(7, obj.getState());
//            pst.setInt(8, obj.getCountry().getId());
            pst.setString(7, obj.getZipCode());
            pst.setInt(8, obj.getGroupId());
            pst.setInt(9, obj.getStatus());
            pst.setInt(10, obj.getIsDefault());
            pst.setInt(11, id);

            pst.execute();
            // 4 passo
            pst.close();
            return true;
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar suppliers: " + e.getMessage());
        }
        return false;
    }

    public Boolean delete(int id) {
        try {
            // 1 passo
            String sql = "DELETE FROM suppliers WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.execute();
            pst.close();

            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir suppliers: " + e.getMessage());
        }
        return false;
    }

    public Supplier searchFromName(String name) {
        try {
            // 1 passo
            String sql = "SELECT * FROM suppliers WHERE company =?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, name);
            rs = pst.executeQuery();
            Supplier obj = new Supplier();
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do suppliers: " + e.getMessage());
        }
        return null;
    }

    public Supplier getFromId(int id) {
        try {
            // 1 passo
            String sql = "SELECT * FROM suppliers WHERE id=?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            Supplier obj = new Supplier();
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do suppliers: " + e.getMessage());
        }
        return null;
    }

    public Supplier searchFromNif(String nif) {
        try {
            // 1 passo
            String sql = "SELECT * FROM clients WHERE nif =?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, nif);
            rs = pst.executeQuery();
            Supplier obj = new Supplier();
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do client: " + e.getMessage());
        }
        return null;
    }

    public List<Supplier> list(String where) {
        List<Supplier> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM suppliers";
            pst = this.conn.prepareStatement(sql);
            rs = pst.executeQuery();
            Supplier obj;
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de suppliers: " + e.getMessage());
        }
        return null;
    }

    public List<Supplier> filter(String txt) {
        List<Supplier> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM suppliers WHERE company LIKE ? OR nif LIKE ? OR city LIKE ?  OR email LIKE ?";
            pst = this.conn.prepareStatement(sql);
            pst.setString(1, "%" + txt + "%");
            pst.setString(2, "%" + txt + "%");
            pst.setString(3, "%" + txt + "%");
            pst.setString(4, "%" + txt + "%");

            rs = pst.executeQuery();
            Supplier obj;
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de suppliers: " + e.getMessage());
        }
        return null;
    }

    public Supplier formatObj(ResultSet rs) {

        try {
            Supplier obj = new Supplier();
//            Countries country;
//            CountryDao sDao = new CountryDao();
//            country = sDao.searchFromId(rs.getInt("country"));

            obj.setId(rs.getInt("id"));
            obj.setNif(rs.getString("nif"));
            obj.setName(rs.getString("company"));
            obj.setEmail(rs.getString("email"));
            obj.setPhone(rs.getString("phone"));
            obj.setAddress(rs.getString("address"));
//            obj.setCountry(country);
            obj.setCity(rs.getString("city"));
//            obj.setState(rs.getString("state"));
            obj.setZipCode(rs.getString("zip_code"));
            obj.setGroupId(rs.getInt("group_id"));
            obj.setStatus(rs.getInt("status"));
            obj.setIsDefault(rs.getInt("isdefault"));
            return obj;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao formatar obj Supplier: " + e.getMessage());
        }
        return null;
    }
}
