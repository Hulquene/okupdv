/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
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
public class UserDao {

    private final Connection conn;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public UserDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public Boolean add(User obj) {
        try {
            // 1 passo
            String sql = "INSERT INTO users (name,nif,phone,email,address,city,country,profile,status,birthdate,password)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?)";
            // 2 passo
            PreparedStatement ptmt = this.conn.prepareStatement(sql);
            ptmt.setString(1, obj.getName());
            ptmt.setString(2, obj.getNif());
            ptmt.setString(3, obj.getPhone());
            ptmt.setString(4, obj.getEmail());
            ptmt.setString(5, obj.getAddress());
            ptmt.setString(6, obj.getCity());
            ptmt.setString(7, obj.getCountry());
            ptmt.setString(8, obj.getProfile());
            ptmt.setInt(9, obj.getStatus());
            ptmt.setString(10, obj.getBirthdate());
            ptmt.setString(11, obj.getPassword());
            //3 passo
            ptmt.execute();
            // 4 passo
            return true;

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar Usuario: " + e.getMessage());
        }
        return false;
    }

    public Boolean edit(User obj, int id) {
        try {
            // 1 passo
            String sql = "UPDATE users SET name=?,nif=?,phone=?,email=?,address=?,city=?,country=?,profile=?,status=?,birthdate=? WHERE id=?";
            // 2 passo
            PreparedStatement ptmt = this.conn.prepareStatement(sql);
            ptmt.setString(1, obj.getName());
            ptmt.setString(2, obj.getNif());
            ptmt.setString(3, obj.getPhone());
            ptmt.setString(4, obj.getEmail());
            ptmt.setString(5, obj.getAddress());
            ptmt.setString(6, obj.getCity());
            ptmt.setString(7, obj.getCountry());
            ptmt.setString(8, obj.getProfile());
            ptmt.setInt(9, obj.getStatus());
            ptmt.setString(10, obj.getBirthdate());
            ptmt.setInt(11, id);
            //3 passo
            //ptmt.executeQuery();
            ptmt.execute();
            return true;
//            JOptionPane.showMessageDialog(null, "Usuario atualizado com Sucesso!!");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Usuario: " + e.getMessage());
        }
        return false;
    }

    public Boolean updatePassword(String newPassword, int id) {
        try {
            // 1 passo
            String sql = "UPDATE users SET password=? WHERE id=?";
            // 2 passo
            PreparedStatement ptmt = this.conn.prepareStatement(sql);
            ptmt.setString(1, newPassword);
            ptmt.setInt(2, id);
            //3 passo
            //ptmt.executeQuery();
            ptmt.execute();
            return true;
//            JOptionPane.showMessageDialog(null, "Usuario atualizado com Sucesso!!");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Password Usuario: " + e.getMessage());
        }
        return false;
    }

    public Boolean updateCodeManager(String code, int id) {
        try {
            // 1 passo
            String sql = "UPDATE users SET code=? WHERE id=?";
            // 2 passo
            PreparedStatement ptmt = this.conn.prepareStatement(sql);
            ptmt.setString(1, code);
            ptmt.setInt(2, id);
            //3 passo
            //ptmt.executeQuery();
            ptmt.execute();
            return true;
//            JOptionPane.showMessageDialog(null, "Usuario atualizado com Sucesso!!");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar code Usuario: " + e.getMessage());
        }
        return false;
    }

    public User searchFromNif(String nif) {
        try {
            // 1 passo
            String sql = "SELECT * FROM users WHERE nif =?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, nif);
            rs = pst.executeQuery();
            User obj = new User();
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do User: " + e.getMessage());
        }
        return null;
    }

    public User searchFromName(String name) {
        try {
            // 1 passo
            String sql = "SELECT * FROM users WHERE name =?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, name);
            rs = pst.executeQuery();
            User obj = new User();
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Usuario: " + e.getMessage());
        }
        return null;
    }

    public User getId(int id) {
        try {
            // 1 passo
            String sql = "SELECT * FROM users WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            User obj = new User();
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Usuario: " + e.getMessage());
        }
        return null;
    }

    public User validateManagerCode(String code) {
        try {
            // 1 passo
            String sql = "SELECT * FROM users WHERE code =? AND status =? AND (profile =? OR profile =?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, code);
            pst.setInt(2, 1);
            pst.setString(3, "admin");
            pst.setString(4, "manager");
            rs = pst.executeQuery();
            User obj = new User();
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Supervisor: " + e.getMessage());
        }
        return null;
    }

    public List<User> list(String where) {
        List<User> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM users";
            pst = this.conn.prepareStatement(sql);
            rs = pst.executeQuery();
            User obj;// = new User();
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de Usuario: " + e.getMessage());
        }
        return null;
    }

    public List<User> filter(String txt) {
        List<User> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM users WHERE name LIKE ?  OR email LIKE ? OR address LIKE ?";
            pst = this.conn.prepareStatement(sql);
            pst.setString(1, "%" + txt + "%");
            pst.setString(2, "%" + txt + "%");
            pst.setString(3, "%" + txt + "%");
            rs = pst.executeQuery();
            User obj;// = new User();
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de Usuario: " + e.getMessage());
        }
        return null;
    }

    public User login(String email, String password) {
        try {
//            int id = 0;
            // 1 passo
            String sql = "SELECT * FROM users WHERE email =? and password=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, email);
            pst.setString(2, password);
            rs = pst.executeQuery();
            User user;
            if (rs.next()) {
                user = formatObj(rs);
                return user;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro login " + e.getMessage());
        }
        return null;
    }

    public Boolean delete(int id) {
        try {
            // 1 passo
            String sql = "DELETE FROM users WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir Usuario: " + e.getMessage());
        }
        return false;
    }

    public User formatObj(ResultSet rs) {
        try {
            User obj = new User();
//            CountryDao sDao = new CountryDao();
//            sDao.searchFromId(rs.getInt("country"));

            obj.setId(rs.getInt("id"));
            obj.setNif(rs.getString("nif"));
            obj.setName(rs.getString("name"));
            obj.setEmail(rs.getString("email"));
            obj.setPhone(rs.getString("phone"));
            obj.setAddress(rs.getString("address"));
            obj.setCountry(rs.getString("country"));
            obj.setCity(rs.getString("city"));
            obj.setProfile(rs.getString("profile"));
            obj.setBirthdate(rs.getString("birthdate"));
            obj.setStatus(rs.getInt("status"));

            return obj;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao formatar obj Client: " + e.getMessage());
        }
        return null;
    }
}
