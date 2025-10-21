package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.entities.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO responsável pela entidade User. Inclui login, filtro e código de manager.
 *
 * Herda as operações CRUD da BaseDao.
 *
 * @author …
 */
public class UserDao extends BaseDao<User> {

    // 🔹 Função de mapeamento de ResultSet → User
    private User map(ResultSet rs) {
        try {
            User u = new User();
            u.setId(rs.getInt("id"));
            u.setName(rs.getString("name"));
            u.setEmail(rs.getString("email"));
            u.setPhone(rs.getString("phone"));
            u.setNif(rs.getString("nif"));
            u.setAddress(rs.getString("address"));
            u.setCountry(rs.getString("country"));
            u.setCity(rs.getString("city"));
            u.setProfile(rs.getString("profile"));
            u.setBirthdate(rs.getString("birthdate"));
            u.setStatus(rs.getInt("status"));
            u.setPassword(rs.getString("password"));
            u.setCode(rs.getString("code"));
            return u;
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao mapear usuário: " + e.getMessage());
            return null;
        }
    }

    // ======================
    // Implementações CRUD
    // ======================
    @Override
    public boolean add(User u) {
        String sql = """
            INSERT INTO users 
            (name,nif,phone,email,address,city,country,profile,status,birthdate,password,code)
            VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
        """;
        return executeUpdate(sql,
                u.getName(), u.getNif(), u.getPhone(), u.getEmail(), u.getAddress(),
                u.getCity(), u.getCountry(), u.getProfile(), u.getStatus(),
                u.getBirthdate(), u.getPassword(), u.getCode());
    }

    @Override
    public boolean update(User u) {
        String sql = """
            UPDATE users SET name=?,nif=?,phone=?,email=?,address=?,city=?,country=?,
            profile=?,status=?,birthdate=?,password=?,code=? WHERE id=?
        """;
        return executeUpdate(sql,
                u.getName(), u.getNif(), u.getPhone(), u.getEmail(), u.getAddress(),
                u.getCity(), u.getCountry(), u.getProfile(), u.getStatus(),
                u.getBirthdate(), u.getPassword(), u.getCode(), u.getId());
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM users WHERE id=?", id);
    }

    @Override
    public User findById(int id) {
        return findOne("SELECT * FROM users WHERE id=?", this::map, id);
    }

    @Override
    public List<User> findAll() {
        return executeQuery("SELECT * FROM users", this::map);
    }

    // ======================
    // Funções específicas
    // ======================
    /**
     * Faz login pelo email e password.
     */
    public User login(String email, String password) {
        return findOne("SELECT * FROM users WHERE email=? AND password=?", this::map, email, password);
    }

    /**
     * Busca um usuário pelo NIF.
     */
    public User findByNif(String nif) {
        return findOne("SELECT * FROM users WHERE nif=?", this::map, nif);
    }

    /**
     * Busca usuários com nome, email ou endereço semelhantes ao texto.
     */
    public List<User> filter(String text) {
        String sql = "SELECT * FROM users WHERE name LIKE ? OR email LIKE ? OR address LIKE ?";
        String like = "%" + text + "%";
        return executeQuery(sql, this::map, like, like, like);
    }

    /**
     * Valida código de gestor/supervisor.
     */
    public User validateManagerCode(String code) {
        String sql = """
            SELECT * FROM users 
            WHERE code=? AND status=1 AND (profile='admin' OR profile='manager')
        """;
        return findOne(sql, this::map, code);
    }

    /**
     * Atualiza apenas a senha do usuário.
     */
    public boolean updatePassword(int id, String newPassword) {
        return executeUpdate("UPDATE users SET password=? WHERE id=?", newPassword, id);
    }

    /**
     * Atualiza o código do gerente.
     */
    public boolean updateManagerCode(int id, String newCode) {
        return executeUpdate("UPDATE users SET code=? WHERE id=?", newCode, id);
    }
}

///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.okutonda.okudpdv.data.dao;
//
//import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
//import com.okutonda.okudpdv.data.entities.User;
//import java.awt.HeadlessException;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import javax.swing.JOptionPane;
//
///**
// *
// * @author kenny
// */
//public class UserDao {
//
//    private final Connection conn;
//    PreparedStatement pst = null;
//    ResultSet rs = null;
//
//    public UserDao() {
//        this.conn = ConnectionDatabase.getConnect();
//    }
//
//    public Boolean add(User obj) {
//        try {
//            // 1 passo
//            String sql = "INSERT INTO users (name,nif,phone,email,address,city,country,profile,status,birthdate,password)"
//                    + "values(?,?,?,?,?,?,?,?,?,?,?)";
//            // 2 passo
//            PreparedStatement ptmt = this.conn.prepareStatement(sql);
//            ptmt.setString(1, obj.getName());
//            ptmt.setString(2, obj.getNif());
//            ptmt.setString(3, obj.getPhone());
//            ptmt.setString(4, obj.getEmail());
//            ptmt.setString(5, obj.getAddress());
//            ptmt.setString(6, obj.getCity());
//            ptmt.setString(7, obj.getCountry());
//            ptmt.setString(8, obj.getProfile());
//            ptmt.setInt(9, obj.getStatus());
//            ptmt.setString(10, obj.getBirthdate());
//            ptmt.setString(11, obj.getPassword());
//            //3 passo
//            ptmt.execute();
//            // 4 passo
//            return true;
//
//        } catch (HeadlessException | SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao salvar Usuario: " + e.getMessage());
//        }
//        return false;
//    }
//
//    public Boolean edit(User obj, int id) {
//        try {
//            // 1 passo
//            String sql = "UPDATE users SET name=?,nif=?,phone=?,email=?,address=?,city=?,country=?,profile=?,status=?,birthdate=? WHERE id=?";
//            // 2 passo
//            PreparedStatement ptmt = this.conn.prepareStatement(sql);
//            ptmt.setString(1, obj.getName());
//            ptmt.setString(2, obj.getNif());
//            ptmt.setString(3, obj.getPhone());
//            ptmt.setString(4, obj.getEmail());
//            ptmt.setString(5, obj.getAddress());
//            ptmt.setString(6, obj.getCity());
//            ptmt.setString(7, obj.getCountry());
//            ptmt.setString(8, obj.getProfile());
//            ptmt.setInt(9, obj.getStatus());
//            ptmt.setString(10, obj.getBirthdate());
//            ptmt.setInt(11, id);
//            //3 passo
//            //ptmt.executeQuery();
//            ptmt.execute();
//            return true;
////            JOptionPane.showMessageDialog(null, "Usuario atualizado com Sucesso!!");
//        } catch (HeadlessException | SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao atualizar Usuario: " + e.getMessage());
//        }
//        return false;
//    }
//
//    public Boolean updatePassword(String newPassword, int id) {
//        try {
//            // 1 passo
//            String sql = "UPDATE users SET password=? WHERE id=?";
//            // 2 passo
//            PreparedStatement ptmt = this.conn.prepareStatement(sql);
//            ptmt.setString(1, newPassword);
//            ptmt.setInt(2, id);
//            //3 passo
//            //ptmt.executeQuery();
//            ptmt.execute();
//            return true;
////            JOptionPane.showMessageDialog(null, "Usuario atualizado com Sucesso!!");
//        } catch (HeadlessException | SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao atualizar Password Usuario: " + e.getMessage());
//        }
//        return false;
//    }
//
//    public Boolean updateCodeManager(String code, int id) {
//        try {
//            // 1 passo
//            String sql = "UPDATE users SET code=? WHERE id=?";
//            // 2 passo
//            PreparedStatement ptmt = this.conn.prepareStatement(sql);
//            ptmt.setString(1, code);
//            ptmt.setInt(2, id);
//            //3 passo
//            //ptmt.executeQuery();
//            ptmt.execute();
//            return true;
////            JOptionPane.showMessageDialog(null, "Usuario atualizado com Sucesso!!");
//        } catch (HeadlessException | SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao atualizar code Usuario: " + e.getMessage());
//        }
//        return false;
//    }
//
//    public User searchFromNif(String nif) {
//        try {
//            // 1 passo
//            String sql = "SELECT * FROM users WHERE nif =?";
//            pst = conn.prepareStatement(sql);
//            pst.setString(1, nif);
//            rs = pst.executeQuery();
//            User obj = new User();
//            if (rs.next()) {
//                obj = formatObj(rs);
//            }
//            return obj;
//            // 2 passo
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do User: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public User searchFromName(String name) {
//        try {
//            // 1 passo
//            String sql = "SELECT * FROM users WHERE name =?";
//            pst = conn.prepareStatement(sql);
//            pst.setString(1, name);
//            rs = pst.executeQuery();
//            User obj = new User();
//            if (rs.next()) {
//                obj = formatObj(rs);
//            }
//            return obj;
//            // 2 passo
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Usuario: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public User getId(int id) {
//        try {
//            // 1 passo
//            String sql = "SELECT * FROM users WHERE id =?";
//            pst = conn.prepareStatement(sql);
//            pst.setInt(1, id);
//            rs = pst.executeQuery();
//            User obj = new User();
//            if (rs.next()) {
//                obj = formatObj(rs);
//            }
//            return obj;
//            // 2 passo
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Usuario: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public User validateManagerCode(String code) {
//        try {
//            // 1 passo
//            String sql = "SELECT * FROM users WHERE code =? AND status =? AND (profile =? OR profile =?)";
//            pst = conn.prepareStatement(sql);
//            pst.setString(1, code);
//            pst.setInt(2, 1);
//            pst.setString(3, "admin");
//            pst.setString(4, "manager");
//            rs = pst.executeQuery();
//            User obj = new User();
//            if (rs.next()) {
//                obj = formatObj(rs);
//            }
//            return obj;
//            // 2 passo
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Supervisor: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public List<User> list(String where) {
//        List<User> list = new ArrayList<>();
//        try {
//            String sql = "SELECT * FROM users";
//            pst = this.conn.prepareStatement(sql);
//            rs = pst.executeQuery();
//            User obj;// = new User();
//            while (rs.next()) {
//                obj = formatObj(rs);
//                list.add(obj);
//            }
//            return list;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de Usuario: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public List<User> filter(String txt) {
//        List<User> list = new ArrayList<>();
//        try {
//            String sql = "SELECT * FROM users WHERE name LIKE ?  OR email LIKE ? OR address LIKE ?";
//            pst = this.conn.prepareStatement(sql);
//            pst.setString(1, "%" + txt + "%");
//            pst.setString(2, "%" + txt + "%");
//            pst.setString(3, "%" + txt + "%");
//            rs = pst.executeQuery();
//            User obj;// = new User();
//            while (rs.next()) {
//                obj = formatObj(rs);
//                list.add(obj);
//            }
//            return list;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de Usuario: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public User login(String email, String password) {
//        try {
////            int id = 0;
//            // 1 passo
//            String sql = "SELECT * FROM users WHERE email =? and password=?";
//            pst = conn.prepareStatement(sql);
//            pst.setString(1, email);
//            pst.setString(2, password);
//            rs = pst.executeQuery();
//            User user;
//            if (rs.next()) {
//                user = formatObj(rs);
//                return user;
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro login " + e.getMessage());
//        }
//        return null;
//    }
//
//    public Boolean delete(int id) {
//        try {
//            // 1 passo
//            String sql = "DELETE FROM users WHERE id =?";
//            pst = conn.prepareStatement(sql);
//            pst.setInt(1, id);
//            pst.execute();
//            return true;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao excluir Usuario: " + e.getMessage());
//        }
//        return false;
//    }
//
//    public User formatObj(ResultSet rs) {
//        try {
//            User obj = new User();
////            CountryDao sDao = new CountryDao();
////            sDao.searchFromId(rs.getInt("country"));
//
//            obj.setId(rs.getInt("id"));
//            obj.setNif(rs.getString("nif"));
//            obj.setName(rs.getString("name"));
//            obj.setEmail(rs.getString("email"));
//            obj.setPhone(rs.getString("phone"));
//            obj.setAddress(rs.getString("address"));
//            obj.setCountry(rs.getString("country"));
//            obj.setCity(rs.getString("city"));
//            obj.setProfile(rs.getString("profile"));
//            obj.setBirthdate(rs.getString("birthdate"));
//            obj.setStatus(rs.getInt("status"));
//
//            return obj;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao formatar obj Client: " + e.getMessage());
//        }
//        return null;
//    }
//}
