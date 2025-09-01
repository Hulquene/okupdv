/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.dao;

import com.mysql.cj.xdevapi.Client;
import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.models.Clients;
import com.okutonda.okudpdv.models.Payment;
import com.okutonda.okudpdv.models.PaymentModes;
import com.okutonda.okudpdv.models.User;
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
public class PaymentDao {

    private final Connection conn;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public PaymentDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public Boolean add(Payment obj, int invoiceId) {
        try {
            // 1 passo
            String sql = "INSERT INTO payment (description,total,prefix,number,date,dateFinish,status,clientId,userId,invoiceId,invoiceType,paymentModeId)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?)";
            // 2 passo
            this.pst = this.conn.prepareStatement(sql);
            this.pst.setString(1, obj.getDescription());
            this.pst.setDouble(2, obj.getTotal());
            this.pst.setString(3, obj.getPrefix());
            this.pst.setInt(4, obj.getNumber());
            this.pst.setString(5, obj.getDate());
            this.pst.setString(6, obj.getDateFinish());
            this.pst.setString(7, obj.getStatus());
            this.pst.setInt(8, obj.getClient().getId());
            this.pst.setInt(9, obj.getUser().getId());
            this.pst.setInt(8, obj.getInvoiceId());
            this.pst.setString(10, obj.getInvoiceType());
            this.pst.setInt(11, obj.getPaymentMode().getId());
            //3 passo
            this.pst.execute();
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao salvar Payment" + e.getMessage());
//            JOptionPane.showMessageDialog(null, "Erro ao salvar client: " + e.getMessage());
        }
        return false;
    }

//    public Boolean edit(Payment obj, int id) {
//        try {
//            // 1 passo
//            String sql = "UPDATE payment SET company=?,nif=?,phone=?,email=?,address=?,city=?,state=?,country=?,zip_code=?,status=?,isdefault=? WHERE id=?";
//            // 2 passo
//            this.pst = this.conn.prepareStatement(sql);
//            this.pst.setString(1, obj.getDescription());
//            this.pst.setDouble(2, obj.getTotal());
//            this.pst.setString(3, obj.getPrefix());
//            this.pst.setInt(4, obj.getNumber());
//            this.pst.setString(5, obj.getDate());
//            this.pst.setString(6, obj.getDateFinish());
//            this.pst.setString(7, obj.getStatus());
//            this.pst.setInt(8, obj.getClient().getId());
//            this.pst.setInt(9, obj.getUser().getId());
//            this.pst.setInt(8, obj.getInvoiceId());
//            this.pst.setString(10, obj.getInvoiceType());
//            this.pst.setInt(11, obj.getPaymentMode().getId());
//
//            this.pst.setInt(12, id);
//            this.pst.execute();
//            return true;
//        } catch (HeadlessException | SQLException e) {
//            System.out.println("Erro ao atualizar Payment" + e.getMessage());
////            JOptionPane.showMessageDialog(null, "Erro ao atualizar client: " + e.getMessage());
//        }
//        return false;
//    }
    public Boolean edit(Payment obj, int id) {
        try {
            // 1 passo
            String sql = "UPDATE payment SET dateFinish=?,status=? WHERE id=?";
            // 2 passo
            this.pst = this.conn.prepareStatement(sql);
            this.pst.setString(1, obj.getDateFinish());
            this.pst.setString(2, obj.getStatus());
            this.pst.setInt(3, id);

            this.pst.execute();
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao atualizar Payment" + e.getMessage());
//            JOptionPane.showMessageDialog(null, "Erro ao atualizar client: " + e.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM payment WHERE id =?";
        try {
            // 1 passo
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setInt(1, id);
            ptmt.execute();
            ptmt.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir payment: " + e.getMessage());
        }
        return false;
    }

    public Payment searchFromName(String name) {
        try {
            // 1 passo
            String sql = "SELECT * FROM payment WHERE company =?";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, name);
            this.rs = ptmt.executeQuery();
            Payment obj = new Payment();
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
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Payment: " + e.getMessage());
        }
        return null;
    }

    public Payment getId(int id) {
        try {
            // 1 passo
            String sql = "SELECT * FROM payment WHERE id =?";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setInt(1, id);
            rs = ptmt.executeQuery();
            Payment obj = new Payment();
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
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do payment: " + e.getMessage());
        }
        return null;
    }

    public Payment searchFromNif(String nif) {
        try {
            // 1 passo
            String sql = "SELECT * FROM payment WHERE nif =?";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, nif);
            rs = ptmt.executeQuery();
            Payment obj = new Payment();
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
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do payment: " + e.getMessage());
        }
        return null;
    }

//    public Payment getClientDefault() {
//        try {
//            // 1 passo
//            String sql = "SELECT * FROM payment WHERE isdefault =1";
//            PreparedStatement ptmt = conn.prepareStatement(sql);
//            rs = ptmt.executeQuery();
//            Payment obj = new Payment();
//            if (rs.next()) {
//                obj = formatObj(rs);
//            }
//            return obj;
//            // 2 passo
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Client: " + e.getMessage());
//        }
//        return null;
//    }
    public List<Payment> list(String where) {
        List<Payment> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM payment";
            PreparedStatement ptmt = this.conn.prepareStatement(sql);
            rs = ptmt.executeQuery();
            Payment obj;
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de payment: " + e.getMessage());
        }
        return null;
    }

    public List<Payment> filter(String txt) {
        List<Payment> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM payment WHERE description LIKE ? OR date LIKE ? OR prefix LIKE ?";
            PreparedStatement ptmt = this.conn.prepareStatement(sql);
            ptmt.setString(1, "%" + txt + "%");
            ptmt.setString(2, "%" + txt + "%");
            ptmt.setString(3, "%" + txt + "%");
            rs = ptmt.executeQuery();
            Payment obj;
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

    public Payment formatObj(ResultSet rs) {
        try {
            Payment obj = new Payment();

            User user;
            UserDao userDao = new UserDao();
            user = userDao.getId(rs.getInt("userId"));

            Clients client;
            ClientDao clientDao = new ClientDao();
            client = clientDao.getId(rs.getInt("clientId"));

            PaymentModes paymentMode;
            PaymentModeDao paymMDao = new PaymentModeDao();
            paymentMode = paymMDao.getId(rs.getInt("paymentModeId"));

            obj.setId(rs.getInt("id"));
            obj.setDescription(rs.getString("description"));
            obj.setTotal(rs.getDouble("total"));
            obj.setPrefix(rs.getString("prefix"));
            obj.setNumber(rs.getInt("number"));
            obj.setDate(rs.getString("date"));
            obj.setDateFinish(rs.getString("dateFinish"));
            obj.setStatus(rs.getString("status"));
            obj.setInvoiceId(rs.getInt("invoiceId"));
            obj.setUser(user);
            obj.setClient(client);
            obj.setPaymentMode(paymentMode);

            return obj;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao formatar obj Client: " + e.getMessage());
        }
        return null;
    }
}
