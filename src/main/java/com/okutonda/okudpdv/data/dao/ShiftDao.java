/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.data.entities.Shift;
import com.okutonda.okudpdv.data.entities.User;
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
public class ShiftDao {

    private final Connection conn;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public ShiftDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public boolean add(Shift obj) {
        try {
            // 1 passo
            String sql = "INSERT INTO shift (hash,code,granted_amount,incurred_amount,closing_amount,status,user_id)"
                    + "values(?,?,?,?,?,?,?)";
            // 2 passo
            pst = this.conn.prepareStatement(sql);
            pst.setString(1, obj.getHash());
            pst.setString(2, obj.getCode());
            pst.setDouble(3, obj.getGrantedAmount());
            pst.setDouble(4, obj.getIncurredAmount());
            pst.setDouble(5, obj.getClosingAmount());
            pst.setString(6, obj.getStatus());
            pst.setInt(7, obj.getUser().getId());
            //3 passo
            pst.execute();
            // 4 passo
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao salvar Shift: " + e.getMessage());
        }
        return false;
    }

    public boolean edit(Shift obj, int id) {
        try {
            // 1 passo
            String sql = "UPDATE shift SET incurred_amount=?,closing_amount=?,status=?WHERE id=?";
            // 2 passo
            pst = this.conn.prepareStatement(sql);
//            pst.setString(1, obj.getHash());
//            pst.setString(2, obj.getCode());
//            pst.setDouble(1, obj.getGrantedAmount());
            pst.setDouble(1, obj.getIncurredAmount());
            pst.setDouble(2, obj.getClosingAmount());
            pst.setString(3, obj.getStatus());
            pst.setInt(4, id);
            //3 passo
            //ptmt.executeQuery();
            pst.execute();
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao atualizar Shift: " + e.getMessage());
        }
        return false;
    }

    public boolean updateIncurredAmount(Double value, int id) {
        try {
            // 1 passo
            String sql = "UPDATE shift SET incurred_amount=? WHERE id=?";
            // 2 passo
            pst = this.conn.prepareStatement(sql);
            pst.setDouble(1, value);
            pst.setInt(2, id);
            pst.execute();
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao atualizar Shift: " + e.getMessage());
        }
        return false;
    }

    public boolean closeShift(Shift shift) {
        try {
            // 1 passo
            String sql = "UPDATE shift SET closing_amount=?,status=?,dateClose=? WHERE id=?";
            // 2 passo
            pst = this.conn.prepareStatement(sql);
            pst.setDouble(1, shift.getClosingAmount());
            pst.setString(2, shift.getStatus());
            pst.setString(3, shift.getDateClose());
            pst.setInt(4, shift.getId());
            //3 passo
            //ptmt.executeQuery();
            pst.execute();
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao atualizar Shift: " + e.getMessage());
        }
        return false;
    }

    public Shift searchFromId(int id) {
        try {
            // 1 passo
            String sql = "SELECT * FROM shift WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            Shift obj = null;
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Shift: " + e.getMessage());
        }
        return null;
    }

    public Shift getLastShiftUser(int id) {
        try {
            // 1 passo
            String sql = "SELECT * FROM shift WHERE user_id =? and status=1";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            Shift obj = null;
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Shift: " + e.getMessage());
        }
        return null;
    }

    public Shift getFromCode(String code) {
        try {
            // 1 passo
            String sql = "SELECT * FROM shift WHERE code =?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, code);
            rs = pst.executeQuery();
            Shift obj = null;
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Shift: " + e.getMessage());
        }
        return null;
    }

    public Shift getFromHash(String hash) {
        try {
            // 1 passo
            String sql = "SELECT * FROM shift WHERE hash =?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, hash);
            rs = pst.executeQuery();
            Shift obj = null;
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Shift: " + e.getMessage());
        }
        return null;
    }

    public List<Shift> list(String where) {
        List<Shift> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM shift";
            pst = this.conn.prepareStatement(sql);
            rs = pst.executeQuery();
            Shift obj;
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de Shift: " + e.getMessage());
        }
        return null;
    }

    public List<Shift> filter(String txt) {
        List<Shift> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM shift WHERE name LIKE ?  OR code LIKE ? OR closing_amount LIKE ?";
//            String sql = "SELECT * FROM products WHERE description LIKE ?";
            pst = this.conn.prepareStatement(sql);
            pst.setString(1, "%" + txt + "%");
            pst.setString(2, "%" + txt + "%");
            pst.setString(3, "%" + txt + "%");
            rs = pst.executeQuery();
            Shift obj;// = new Product();
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de Shift: " + e.getMessage());
        }
        return null;
    }

    public Boolean delete(int id) {
        try {
            // 1 passo
            String sql = "DELETE FROM shift WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir Shift: " + e.getMessage());
        }
        return false;
    }

    public Shift formatObj(ResultSet rs) {
        try {
            Shift obj = new Shift();
            User user;
            UserDao uDao = new UserDao();
            user = uDao.findById(rs.getInt("user_id"));
            obj.setId(rs.getInt("id"));
            obj.setCode(rs.getString("code"));
            obj.setHash(rs.getString("hash"));
            obj.setGrantedAmount(rs.getDouble("granted_amount"));
            obj.setIncurredAmount(rs.getDouble("incurred_amount"));
            obj.setClosingAmount(rs.getDouble("closing_amount"));
            obj.setStatus(rs.getString("status"));
            obj.setDateClose(rs.getString("dateClose"));
            obj.setDateOpen(rs.getString("dateOpen"));
            obj.setUser(user);
            return obj;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao formatar obj Shift: " + e.getMessage());
        }
        return null;
    }
}
