package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.entities.Shift;
import com.okutonda.okudpdv.data.entities.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO responsável pelo acesso à tabela shift.
 *
 * NENHUMA regra de negócio ou sessão é tratada aqui. Apenas operações diretas
 * de leitura/escrita no banco de dados.
 *
 * @author Hulquene
 */
public class ShiftDao extends BaseDao<Shift> {

    public ShiftDao() {
        super();
    }

    public ShiftDao(java.sql.Connection externalConn) {
        super(externalConn);
    }

    // ==========================================================
    // 🔹 Mapeamento ResultSet → Entidade
    // ==========================================================
    private Shift map(ResultSet rs) {
        try {
            Shift s = new Shift();
            s.setId(rs.getInt("id"));
            s.setCode(rs.getString("code"));
            s.setHash(rs.getString("hash"));
            s.setGrantedAmount(rs.getDouble("granted_amount"));
            s.setIncurredAmount(rs.getDouble("incurred_amount"));
            s.setClosingAmount(rs.getDouble("closing_amount"));
            s.setStatus(rs.getString("status"));
            s.setDateOpen(rs.getString("dateOpen"));
            s.setDateClose(rs.getString("dateClose"));

            // Usuário associado
            User u = new User();
            u.setId(rs.getInt("user_id"));
            s.setUser(u);

            return s;
        } catch (SQLException ex) {
            System.err.println("[DB] Erro ao mapear Shift: " + ex.getMessage());
            return null;
        }
    }

    // ==========================================================
    // 🔹 CRUD básico
    // ==========================================================
    @Override
    public boolean add(Shift s) {
        String sql = """
            INSERT INTO shift (hash, code, granted_amount, incurred_amount, closing_amount, status, user_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        return executeUpdate(sql,
                s.getHash(),
                s.getCode(),
                s.getGrantedAmount(),
                s.getIncurredAmount(),
                s.getClosingAmount(),
                s.getStatus(),
                s.getUser().getId());
    }

    @Override
    public boolean update(Shift s) {
        String sql = """
            UPDATE shift 
               SET incurred_amount=?, closing_amount=?, status=?, dateClose=? 
             WHERE id=?
        """;
        return executeUpdate(sql,
                s.getIncurredAmount(),
                s.getClosingAmount(),
                s.getStatus(),
                s.getDateClose(),
                s.getId());
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM shift WHERE id=?", id);
    }

    @Override
    public Shift findById(int id) {
        return findOne("SELECT * FROM shift WHERE id=?", this::map, id);
    }

    @Override
    public List<Shift> findAll() {
        return executeQuery("SELECT * FROM shift ORDER BY id DESC", this::map);
    }

    // ==========================================================
    // 🔹 Consultas específicas (somente SQL)
    // ==========================================================
    public Shift findByCode(String code) {
        return findOne("SELECT * FROM shift WHERE code=?", this::map, code);
    }

    public Shift findByHash(String hash) {
        return findOne("SELECT * FROM shift WHERE hash=?", this::map, hash);
    }

    public Shift findLastOpenShiftByUser(int userId) {
        String sql = """
            SELECT * FROM shift 
             WHERE user_id=? AND status='open'
          ORDER BY id DESC LIMIT 1
        """;
        return findOne(sql, this::map, userId);
    }

    public List<Shift> filter(String text) {
        String like = "%" + text + "%";
        String sql = """
            SELECT * FROM shift 
             WHERE code LIKE ? OR status LIKE ? OR closing_amount LIKE ?
          ORDER BY id DESC
        """;
        return executeQuery(sql, this::map, like, like, like);
    }

    public boolean updateIncurredAmount(double newValue, int id) {
        return executeUpdate("UPDATE shift SET incurred_amount=? WHERE id=?", newValue, id);
    }

    public boolean updateClosingData(double closingAmount, String status, String dateClose, int id) {
        return executeUpdate("""
            UPDATE shift SET closing_amount=?, status=?, dateClose=? WHERE id=?
        """, closingAmount, status, dateClose, id);
    }
}

///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.okutonda.okudpdv.data.dao;
//
//import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
//import com.okutonda.okudpdv.data.entities.Shift;
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
//public class ShiftDao {
//
//    private final Connection conn;
//    PreparedStatement pst = null;
//    ResultSet rs = null;
//
//    public ShiftDao() {
//        this.conn = ConnectionDatabase.getConnect();
//    }
//
//    public boolean add(Shift obj) {
//        try {
//            // 1 passo
//            String sql = "INSERT INTO shift (hash,code,granted_amount,incurred_amount,closing_amount,status,user_id)"
//                    + "values(?,?,?,?,?,?,?)";
//            // 2 passo
//            pst = this.conn.prepareStatement(sql);
//            pst.setString(1, obj.getHash());
//            pst.setString(2, obj.getCode());
//            pst.setDouble(3, obj.getGrantedAmount());
//            pst.setDouble(4, obj.getIncurredAmount());
//            pst.setDouble(5, obj.getClosingAmount());
//            pst.setString(6, obj.getStatus());
//            pst.setInt(7, obj.getUser().getId());
//            //3 passo
//            pst.execute();
//            // 4 passo
//            return true;
//        } catch (HeadlessException | SQLException e) {
//            System.out.println("Erro ao salvar Shift: " + e.getMessage());
//        }
//        return false;
//    }
//
//    public boolean edit(Shift obj, int id) {
//        try {
//            // 1 passo
//            String sql = "UPDATE shift SET incurred_amount=?,closing_amount=?,status=?WHERE id=?";
//            // 2 passo
//            pst = this.conn.prepareStatement(sql);
////            pst.setString(1, obj.getHash());
////            pst.setString(2, obj.getCode());
////            pst.setDouble(1, obj.getGrantedAmount());
//            pst.setDouble(1, obj.getIncurredAmount());
//            pst.setDouble(2, obj.getClosingAmount());
//            pst.setString(3, obj.getStatus());
//            pst.setInt(4, id);
//            //3 passo
//            //ptmt.executeQuery();
//            pst.execute();
//            return true;
//        } catch (HeadlessException | SQLException e) {
//            System.out.println("Erro ao atualizar Shift: " + e.getMessage());
//        }
//        return false;
//    }
//
//    public boolean updateIncurredAmount(Double value, int id) {
//        try {
//            // 1 passo
//            String sql = "UPDATE shift SET incurred_amount=? WHERE id=?";
//            // 2 passo
//            pst = this.conn.prepareStatement(sql);
//            pst.setDouble(1, value);
//            pst.setInt(2, id);
//            pst.execute();
//            return true;
//        } catch (HeadlessException | SQLException e) {
//            System.out.println("Erro ao atualizar Shift: " + e.getMessage());
//        }
//        return false;
//    }
//
//    public boolean closeShift(Shift shift) {
//        try {
//            // 1 passo
//            String sql = "UPDATE shift SET closing_amount=?,status=?,dateClose=? WHERE id=?";
//            // 2 passo
//            pst = this.conn.prepareStatement(sql);
//            pst.setDouble(1, shift.getClosingAmount());
//            pst.setString(2, shift.getStatus());
//            pst.setString(3, shift.getDateClose());
//            pst.setInt(4, shift.getId());
//            //3 passo
//            //ptmt.executeQuery();
//            pst.execute();
//            return true;
//        } catch (HeadlessException | SQLException e) {
//            System.out.println("Erro ao atualizar Shift: " + e.getMessage());
//        }
//        return false;
//    }
//
//    public Shift searchFromId(int id) {
//        try {
//            // 1 passo
//            String sql = "SELECT * FROM shift WHERE id =?";
//            pst = conn.prepareStatement(sql);
//            pst.setInt(1, id);
//            rs = pst.executeQuery();
//            Shift obj = null;
//            if (rs.next()) {
//                obj = formatObj(rs);
//            }
//            return obj;
//            // 2 passo
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Shift: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public Shift getLastShiftUser(int id) {
//        try {
//            // 1 passo
//            String sql = "SELECT * FROM shift WHERE user_id =? and status=1";
//            pst = conn.prepareStatement(sql);
//            pst.setInt(1, id);
//            rs = pst.executeQuery();
//            Shift obj = null;
//            if (rs.next()) {
//                obj = formatObj(rs);
//            }
//            return obj;
//            // 2 passo
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Shift: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public Shift getFromCode(String code) {
//        try {
//            // 1 passo
//            String sql = "SELECT * FROM shift WHERE code =?";
//            pst = conn.prepareStatement(sql);
//            pst.setString(1, code);
//            rs = pst.executeQuery();
//            Shift obj = null;
//            if (rs.next()) {
//                obj = formatObj(rs);
//            }
//            return obj;
//            // 2 passo
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Shift: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public Shift getFromHash(String hash) {
//        try {
//            // 1 passo
//            String sql = "SELECT * FROM shift WHERE hash =?";
//            pst = conn.prepareStatement(sql);
//            pst.setString(1, hash);
//            rs = pst.executeQuery();
//            Shift obj = null;
//            if (rs.next()) {
//                obj = formatObj(rs);
//            }
//            return obj;
//            // 2 passo
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Shift: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public List<Shift> list(String where) {
//        List<Shift> list = new ArrayList<>();
//        try {
//            String sql = "SELECT * FROM shift";
//            pst = this.conn.prepareStatement(sql);
//            rs = pst.executeQuery();
//            Shift obj;
//            while (rs.next()) {
//                obj = formatObj(rs);
//                list.add(obj);
//            }
//            return list;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de Shift: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public List<Shift> filter(String txt) {
//        List<Shift> list = new ArrayList<>();
//        try {
//            String sql = "SELECT * FROM shift WHERE name LIKE ?  OR code LIKE ? OR closing_amount LIKE ?";
////            String sql = "SELECT * FROM products WHERE description LIKE ?";
//            pst = this.conn.prepareStatement(sql);
//            pst.setString(1, "%" + txt + "%");
//            pst.setString(2, "%" + txt + "%");
//            pst.setString(3, "%" + txt + "%");
//            rs = pst.executeQuery();
//            Shift obj;// = new Product();
//            while (rs.next()) {
//                obj = formatObj(rs);
//                list.add(obj);
//            }
//            return list;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de Shift: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public Boolean delete(int id) {
//        try {
//            // 1 passo
//            String sql = "DELETE FROM shift WHERE id =?";
//            pst = conn.prepareStatement(sql);
//            pst.setInt(1, id);
//            pst.execute();
//            return true;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao excluir Shift: " + e.getMessage());
//        }
//        return false;
//    }
//
//    public Shift formatObj(ResultSet rs) {
//        try {
//            Shift obj = new Shift();
//            User user;
//            UserDao uDao = new UserDao();
//            user = uDao.findById(rs.getInt("user_id"));
//            obj.setId(rs.getInt("id"));
//            obj.setCode(rs.getString("code"));
//            obj.setHash(rs.getString("hash"));
//            obj.setGrantedAmount(rs.getDouble("granted_amount"));
//            obj.setIncurredAmount(rs.getDouble("incurred_amount"));
//            obj.setClosingAmount(rs.getDouble("closing_amount"));
//            obj.setStatus(rs.getString("status"));
//            obj.setDateClose(rs.getString("dateClose"));
//            obj.setDateOpen(rs.getString("dateOpen"));
//            obj.setUser(user);
//            return obj;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao formatar obj Shift: " + e.getMessage());
//        }
//        return null;
//    }
//}
