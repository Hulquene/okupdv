/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.models.Clients;
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
public class ClientDao {

    private final Connection conn;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public ClientDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public Boolean add(Clients obj) {
        try {
            // 1 passo
            String sql = "INSERT INTO clients (company,nif,phone,email,address,zip_code,status,isdefault,city,state)"
                    + "values(?,?,?,?,?,?,?,?,?,?)";
            // 2 passo
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, obj.getName());
            stmt.setString(2, obj.getNif());
            stmt.setString(3, obj.getPhone());
            stmt.setString(4, obj.getEmail());
            stmt.setString(5, obj.getAddress());
//            stmt.setInt(8, obj.getCountry().getId());
            stmt.setString(6, obj.getZipCode());
//            stmt.setInt(10, obj.getGroupId());
            stmt.setInt(7, obj.getStatus());
            stmt.setInt(8, obj.getIsDefault());
            stmt.setString(9, obj.getCity());
            stmt.setString(10, obj.getState());
            //3 passo
            stmt.execute();
            // 4 passo
            stmt.close();
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao salvar clien" + e.getMessage());
//            JOptionPane.showMessageDialog(null, "Erro ao salvar client: " + e.getMessage());
        }
        return false;
    }

    public Boolean edit(Clients obj, int id) {
        try {
            // 1 passo
            String sql = "UPDATE clients SET company=?,nif=?,phone=?,email=?,address=?,city=?,state=?,zip_code=?,status=?,isdefault=? WHERE id=?";
            // 2 passo
            PreparedStatement ptmt = this.conn.prepareStatement(sql);
            ptmt.setString(1, obj.getName());
            ptmt.setString(2, obj.getNif());
            ptmt.setString(3, obj.getPhone());
            ptmt.setString(4, obj.getEmail());
            ptmt.setString(5, obj.getAddress());
            ptmt.setString(6, obj.getCity());
            ptmt.setString(7, obj.getState());
//            ptmt.setInt(8, obj.getCountry().getId());
            ptmt.setString(8, obj.getZipCode());
            ptmt.setInt(9, obj.getStatus());
            ptmt.setInt(10, obj.getIsDefault());
            ptmt.setInt(11, id);
            //3 passo
            //ptmt.executeQuery();
            ptmt.execute();
            // 4 passo
            ptmt.close();
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao atualizar client" + e.getMessage());
//            JOptionPane.showMessageDialog(null, "Erro ao atualizar client: " + e.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM clients WHERE id =?";
        try {
            // 1 passo
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setInt(1, id);
            ptmt.execute();
            ptmt.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir client: " + e.getMessage());
        }
        return false;
    }

    public Clients searchFromName(String name) {
        try {
            // 1 passo
            String sql = "SELECT * FROM clients WHERE company =?";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, name);
            this.rs = ptmt.executeQuery();
            Clients obj = new Clients();
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

    public Clients getId(int id) {
        try {
            // 1 passo
            String sql = "SELECT * FROM clients WHERE id =?";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setInt(1, id);
            rs = ptmt.executeQuery();
            Clients obj = new Clients();
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

    public Clients searchFromNif(String nif) {
        try {
            // 1 passo
            String sql = "SELECT * FROM clients WHERE nif =?";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, nif);
            rs = ptmt.executeQuery();
            Clients obj = new Clients();
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

    public Clients getClientDefault() {
        try {
            // 1 passo
            String sql = "SELECT * FROM clients WHERE isdefault =1";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            rs = ptmt.executeQuery();
            Clients obj = new Clients();
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Client: " + e.getMessage());
        }
        return null;
    }

    public List<Clients> list(String where) {
        List<Clients> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM clients";
            PreparedStatement ptmt = this.conn.prepareStatement(sql);
            rs = ptmt.executeQuery();
            Clients obj;
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de client: " + e.getMessage());
        }
        return null;
    }

    public List<Clients> filterClient(String txt) {
        List<Clients> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM clients WHERE company LIKE ? OR nif LIKE ? OR city LIKE ?";
            PreparedStatement ptmt = this.conn.prepareStatement(sql);
            ptmt.setString(1, "%" + txt + "%");
            ptmt.setString(2, "%" + txt + "%");
            ptmt.setString(3, "%" + txt + "%");
            rs = ptmt.executeQuery();
            Clients obj;
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de client: " + e.getMessage());
        }
        return null;
    }

    public Clients formatObj(ResultSet rs) {
        try {
            Clients obj = new Clients();
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
            obj.setState(rs.getString("state"));
            obj.setZipCode(rs.getString("zip_code"));
            obj.setStatus(rs.getInt("status"));
            obj.setIsDefault(rs.getInt("isdefault"));
            return obj;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao formatar obj Client: " + e.getMessage());
        }
        return null;
    }
}
