//package com.okutonda.okudpdv.data.dao;
//
//import com.okutonda.okudpdv.data.entities.Box;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.List;
//
///**
// * DAO responsável pela gestão das caixas (Box).
// *
// * Usa o padrão BaseDao para operações CRUD genéricas, com conexões geridas pelo
// * pool DatabaseProvider.
// *
// * Nenhuma lógica de negócio deve estar aqui — apenas SQL.
// *
// * @author Hulquene
// */
//public class BoxDao extends BaseDao<Box> {
//
//    public BoxDao() {
//        super();
//    }
//
//    public BoxDao(java.sql.Connection externalConn) {
//        super(externalConn);
//    }
//
//    // ==========================================================
//    // 🔹 Mapeamento ResultSet → Entidade Box
//    // ==========================================================
//    private Box map(ResultSet rs) {
//        try {
//            Box b = new Box();
//            b.setId(rs.getInt("id"));
//            b.setName(rs.getString("name"));
//            b.setStatus(rs.getString("status"));
//            // Campos futuros: total, data, user, etc.
//            return b;
//        } catch (SQLException ex) {
//            System.err.println("[DB] Erro ao mapear Box: " + ex.getMessage());
//            return null;
//        }
//    }
//
//    // ==========================================================
//    // 🔹 CRUD BÁSICO (BaseDao)
//    // ==========================================================
//    @Override
//    public boolean add(Box b) {
//        String sql = "INSERT INTO box (name, status) VALUES (?, ?)";
//        return executeUpdate(sql, b.getName(), b.getStatus());
//    }
//
//    @Override
//    public boolean update(Box b) {
//        String sql = "UPDATE box SET name=?, status=? WHERE id=?";
//        return executeUpdate(sql, b.getName(), b.getStatus(), b.getId());
//    }
//
//    @Override
//    public boolean delete(int id) {
//        return executeUpdate("DELETE FROM box WHERE id=?", id);
//    }
//
//    @Override
//    public Box findById(int id) {
//        return findOne("SELECT * FROM box WHERE id=?", this::map, id);
//    }
//
//    @Override
//    public List<Box> findAll() {
//        return executeQuery("SELECT * FROM box ORDER BY id DESC", this::map);
//    }
//
//    // ==========================================================
//    // 🔹 MÉTODOS PERSONALIZADOS
//    // ==========================================================
//    /**
//     * Pesquisa caixas por texto (nome ou status).
//     */
//    public List<Box> filter(String txt) {
//        String like = "%" + txt + "%";
//        String sql = """
//            SELECT * FROM box 
//             WHERE name LIKE ? OR status LIKE ?
//          ORDER BY name ASC
//        """;
//        return executeQuery(sql, this::map, like, like);
//    }
//
//    /**
//     * Retorna caixa padrão (status='open' ou isdefault=1)
//     */
//    public Box getDefaultBox() {
//        String sql = "SELECT * FROM box WHERE status='open' LIMIT 1";
//        return findOne(sql, this::map);
//    }
//}
//
/////*
//// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
//// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
//// */
////package com.okutonda.okudpdv.data.dao;
////
////import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
////import com.okutonda.okudpdv.data.entities.Box;
////import java.awt.HeadlessException;
////import java.sql.Connection;
////import java.sql.PreparedStatement;
////import java.sql.ResultSet;
////import java.sql.SQLException;
////import java.util.ArrayList;
////import java.util.List;
////import javax.swing.JOptionPane;
////
/////**
//// *
//// * @author kenny
//// */
////public class BoxDao {
////
////    private final Connection conn;
////    PreparedStatement pst = null;
////    ResultSet rs = null;
////
////    public BoxDao() {
////        this.conn = ConnectionDatabase.getConnect();
////    }
////
////    public boolean add(Box obj) {
////        try {
////            // 1 passo
////            String sql = "INSERT INTO box (name)"
////                    + "values(?)";
////            // 2 passo
////            pst = this.conn.prepareStatement(sql);
////            pst.setString(1, obj.getName());
//////            pst.setDouble(2, obj.getTotal());
//////            pst.setString(3, obj.getDateOpen());
//////            pst.setString(4, obj.getStatus());
//////            pst.setDouble(5, obj.getUser().getId());
////            //3 passo
////            pst.execute();
////            // 4 passo
////            return true;
////        } catch (HeadlessException | SQLException e) {
////            System.out.println("Erro ao salvar Shift: " + e.getMessage());
////        }
////        return false;
////    }
////
////    public boolean edit(Box obj, int id) {
////        try {
////            // 1 passo
////            String sql = "UPDATE box SET name=? WHERE id=?";
////            // 2 passo
////            pst = this.conn.prepareStatement(sql);
//////            pst.setString(1, obj.getHash());
//////            pst.setString(2, obj.getCode());
//////            pst.setDouble(1, obj.getGrantedAmount());
////            pst.setString(1, obj.getName());
//////            pst.setString(2, obj.getStatus());
////            pst.setInt(2, id);
////            //3 passo
////            //ptmt.executeQuery();
////            pst.execute();
////            return true;
////        } catch (HeadlessException | SQLException e) {
////            System.out.println("Erro ao atualizar Box: " + e.getMessage());
////        }
////        return false;
////    }
////    public boolean edit1(Box obj, int id) {
////        try {
////            // 1 passo
////            String sql = "UPDATE box SET name=?,status=? WHERE id=?";
////            // 2 passo
////            pst = this.conn.prepareStatement(sql);
//////            pst.setString(1, obj.getHash());
//////            pst.setString(2, obj.getCode());
//////            pst.setDouble(1, obj.getGrantedAmount());
////            pst.setString(1, obj.getName());
////            pst.setString(2, obj.getStatus());
////            pst.setInt(3, id);
////            //3 passo
////            //ptmt.executeQuery();
////            pst.execute();
////            return true;
////        } catch (HeadlessException | SQLException e) {
////            System.out.println("Erro ao atualizar Box: " + e.getMessage());
////        }
////        return false;
////    }
////
//////    public boolean updateTotal(Double value, int id) {
//////        try {
//////            // 1 passo
//////            String sql = "UPDATE box SET incurred_amount=? WHERE id=?";
//////            // 2 passo
//////            pst = this.conn.prepareStatement(sql);
//////            pst.setDouble(1, value);
//////            pst.setInt(2, id);
//////            pst.execute();
//////            return true;
//////        } catch (HeadlessException | SQLException e) {
//////            System.out.println("Erro ao atualizar Shift: " + e.getMessage());
//////        }
//////        return false;
//////    }
////
//////    public boolean closeBox(Box shift) {
//////        try {
//////            // 1 passo
//////            String sql = "UPDATE shift SET status=?,dateClose=? WHERE id=?";
//////            // 2 passo
//////            pst = this.conn.prepareStatement(sql);
//////            pst.setString(1, shift.getStatus());
//////            pst.setString(2, shift.getDateOpen());
////////            pst.setString(3, shift.getDateClose());
//////            pst.setInt(4, shift.getId());
//////            //3 passo
//////            //ptmt.executeQuery();
//////            pst.execute();
//////            return true;
//////        } catch (HeadlessException | SQLException e) {
//////            System.out.println("Erro ao atualizar Shift: " + e.getMessage());
//////        }
//////        return false;
//////    }
////
////    public Box getId(int id) {
////        try {
////            // 1 passo
////            String sql = "SELECT * FROM box WHERE id =?";
////            pst = conn.prepareStatement(sql);
////            pst.setInt(1, id);
////            rs = pst.executeQuery();
////            Box obj = null;
////            if (rs.next()) {
////                obj = formatObj(rs);
////            }
////            return obj;
////            // 2 passo
////        } catch (SQLException e) {
////            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Shift: " + e.getMessage());
////        }
////        return null;
////    }
////
//////    public Shift getLastShiftUser(int id) {
//////        try {
//////            // 1 passo
//////            String sql = "SELECT * FROM shift WHERE user_id =? and status=1";
//////            pst = conn.prepareStatement(sql);
//////            pst.setInt(1, id);
//////            rs = pst.executeQuery();
//////            Shift obj = null;
//////            if (rs.next()) {
//////                obj = formatObj(rs);
//////            }
//////            return obj;
//////            // 2 passo
//////        } catch (SQLException e) {
//////            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Shift: " + e.getMessage());
//////        }
//////        return null;
//////    }
////
//////    public Shift getFromCode(String code) {
//////        try {
//////            // 1 passo
//////            String sql = "SELECT * FROM shift WHERE code =?";
//////            pst = conn.prepareStatement(sql);
//////            pst.setString(1, code);
//////            rs = pst.executeQuery();
//////            Shift obj = null;
//////            if (rs.next()) {
//////                obj = formatObj(rs);
//////            }
//////            return obj;
//////            // 2 passo
//////        } catch (SQLException e) {
//////            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Shift: " + e.getMessage());
//////        }
//////        return null;
//////    }
////
//////    public Shift getFromHash(String hash) {
//////        try {
//////            // 1 passo
//////            String sql = "SELECT * FROM shift WHERE hash =?";
//////            pst = conn.prepareStatement(sql);
//////            pst.setString(1, hash);
//////            rs = pst.executeQuery();
//////            Shift obj = null;
//////            if (rs.next()) {
//////                obj = formatObj(rs);
//////            }
//////            return obj;
//////            // 2 passo
//////        } catch (SQLException e) {
//////            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Shift: " + e.getMessage());
//////        }
//////        return null;
//////    }
////
////    public List<Box> list(String where) {
////        List<Box> list = new ArrayList<>();
////        try {
////            String sql = "SELECT * FROM box";
////            pst = this.conn.prepareStatement(sql);
////            rs = pst.executeQuery();
////            Box obj;
////            while (rs.next()) {
////                obj = formatObj(rs);
////                list.add(obj);
////            }
////            return list;
////        } catch (SQLException e) {
////            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de Caixa: " + e.getMessage());
////        }
////        return null;
////    }
////
////    public List<Box> filter(String txt) {
////        List<Box> list = new ArrayList<>();
////        try {
////            String sql = "SELECT * FROM box WHERE name LIKE ?  OR status LIKE ? ";
//////            String sql = "SELECT * FROM products WHERE description LIKE ?";
////            pst = this.conn.prepareStatement(sql);
////            pst.setString(1, "%" + txt + "%");
////            pst.setString(2, "%" + txt + "%");
////            rs = pst.executeQuery();
////            Box obj;// = new Product();
////            while (rs.next()) {
////                obj = formatObj(rs);
////                list.add(obj);
////            }
////            return list;
////        } catch (SQLException e) {
////            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de Caixa: " + e.getMessage());
////        }
////        return null;
////    }
////
////    public Boolean delete(int id) {
////        try {
////            // 1 passo
////            String sql = "DELETE FROM box WHERE id =?";
////            pst = conn.prepareStatement(sql);
////            pst.setInt(1, id);
////            pst.execute();
////            return true;
////        } catch (SQLException e) {
////            JOptionPane.showMessageDialog(null, "Erro ao excluir Caixa: " + e.getMessage());
////        }
////        return false;
////    }
////
////    public Box formatObj(ResultSet rs) {
////        try {
////            Box obj = new Box();
//////            User user;
//////            UserDao uDao = new UserDao();
//////            user = uDao.getId(rs.getInt("user_id"));
////            
////            obj.setId(rs.getInt("id"));
////            obj.setName(rs.getString("name"));
//////            obj.setTotal(rs.getDouble("total"));
////            obj.setStatus(rs.getString("status"));
//////            obj.setDateOpen(rs.getString("dateOpen"));
//////            obj.setUser(user);
////            return obj;
////        } catch (SQLException e) {
////            JOptionPane.showMessageDialog(null, "Erro ao formatar obj Caixa: " + e.getMessage());
////        }
////        return null;
////    }
////}
