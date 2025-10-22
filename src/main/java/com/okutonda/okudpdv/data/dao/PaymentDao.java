package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.entities.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;

/**
 * DAO responsável pela gestão de pagamentos.
 *
 * Herda BaseDao<Payment> e usa o pool de conexões. Compatível com transações
 * externas.
 *
 * @author …
 */
public class PaymentDao extends BaseDao<Payment> {

    // ==========================================================
    // 🔹 Mapeamento SQL → Objeto Payment
    // ==========================================================
    private Payment map(ResultSet rs) {
        try {
            Payment p = new Payment();

            p.setId(rs.getInt("id"));
            p.setDescription(rs.getString("description"));
            p.setTotal(rs.getBigDecimal("total"));
            p.setPrefix(rs.getString("prefix"));
            p.setNumber(rs.getInt("number"));
            p.setDate(rs.getString("date"));
            p.setDateFinish(rs.getString("dateFinish"));
            p.setInvoiceId(rs.getInt("order_id"));
            p.setInvoiceType(rs.getString("order_type"));
            p.setReference(rs.getString("reference"));
            p.setCurrency(rs.getString("currency"));

            // enums seguros
            String modeStr = rs.getString("mode");
            String statusStr = rs.getString("status");

            if (modeStr != null) {
                try {
                    p.setPaymentMode(PaymentMode.valueOf(modeStr.toUpperCase()));
                } catch (Exception ignored) {
                }
            }
            if (statusStr != null) {
                try {
                    p.setStatus(PaymentStatus.valueOf(statusStr.toUpperCase()));
                } catch (Exception ignored) {
                }
            }

            // relações
            try {
                ClientDao cDao = new ClientDao();
                UserDao uDao = new UserDao();
                p.setClient(cDao.findById(rs.getInt("clientId")));
                p.setUser(uDao.findById(rs.getInt("userId")));
            } catch (Exception e) {
                System.err.println("[DB] Aviso: falha ao carregar relações de Payment → " + e.getMessage());
            }

            return p;
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao mapear Payment: " + e.getMessage());
            return null;
        }
    }

    // ==========================================================
    // 🔹 CRUD (GenericDao)
    // ==========================================================
    @Override
    public boolean add(Payment p) {
        String sql = """
            INSERT INTO payments
            (description,total,prefix,number,`date`,dateFinish,status,
             clientId,userId,order_id,order_type,mode,reference,currency)
            VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)
        """;
        return executeUpdate(sql,
                safe(p.getDescription()),
                safeBD(p.getTotal()),
                safe(p.getPrefix()),
                p.getNumber(),
                safe(p.getDate()),
                nullable(p.getDateFinish()),
                safeEnum(p.getStatus(), PaymentStatus.SUCCESS),
                (p.getClient() != null ? p.getClient().getId() : 0),
                (p.getUser() != null ? p.getUser().getId() : 0),
                (p.getInvoiceId() != 0 ? p.getInvoiceId() : 0),
                safe(p.getInvoiceType(), "ORDER"),
                safeEnum(p.getPaymentMode(), PaymentMode.NUMERARIO),
                safe(p.getReference()),
                safe(p.getCurrency(), "AOA"));
    }

    /**
     * Permite adicionar pagamento vinculado a uma fatura específica
     */
    public boolean add(Payment p, int orderId) {
        p.setInvoiceId(orderId);
        return add(p);
    }

    @Override
    public boolean update(Payment p) {
        String sql = """
            UPDATE payments 
            SET dateFinish=?, status=?, reference=?, description=?, currency=? 
            WHERE id=?
        """;
        return executeUpdate(sql,
                nullable(p.getDateFinish()),
                safeEnum(p.getStatus(), PaymentStatus.SUCCESS),
                safe(p.getReference()),
                safe(p.getDescription()),
                safe(p.getCurrency()),
                p.getId());
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM payments WHERE id=?", id);
    }

    @Override
    public Payment findById(int id) {
        return findOne("SELECT * FROM payments WHERE id=?", this::map, id);
    }

    @Override
    public List<Payment> findAll() {
        return executeQuery("SELECT * FROM payments ORDER BY date DESC", this::map);
    }

    // ==========================================================
    // 🔹 Métodos específicos
    // ==========================================================
    /**
     * Busca pagamento pela referência
     */
    public Payment findByReference(String ref) {
        return findOne("SELECT * FROM payments WHERE reference=?", this::map, ref);
    }

    /**
     * Lista pagamentos com condição customizada
     */
    public List<Payment> list(String where) {
        String sql = "SELECT * FROM payments " + (where != null ? where : "");
        return executeQuery(sql, this::map);
    }

    /**
     * Filtra por texto (prefix, referência, data, descrição)
     */
    public List<Payment> filter(String txt) {
        String like = "%" + txt + "%";
        String sql = """
            SELECT * FROM payments 
            WHERE description LIKE ? OR `date` LIKE ? OR prefix LIKE ? OR reference LIKE ?
        """;
        return executeQuery(sql, this::map, like, like, like, like);
    }

    /**
     * Filtra pagamentos entre duas datas
     */
    public List<Payment> filterDate(LocalDate from, LocalDate to) {
        String sql = """
            SELECT * FROM payments 
            WHERE DATE(`date`) BETWEEN ? AND ? 
            ORDER BY `date` ASC, id ASC
        """;
        return executeQuery(sql, this::map, Date.valueOf(from), Date.valueOf(to));
    }

    // ==========================================================
    // 🔹 Helpers seguros
    // ==========================================================
    private static String safe(String s) {
        return (s == null) ? "" : s;
    }

    private static String safe(String s, String def) {
        return (s == null || s.isEmpty()) ? def : s;
    }

    private static String safeEnum(Enum<?> e, Enum<?> def) {
        return (e != null) ? e.name() : def.name();
    }

    private static BigDecimal safeBD(BigDecimal b) {
        return (b == null) ? BigDecimal.ZERO : b;
    }

    private static Object nullable(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }
}

///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.okutonda.okudpdv.data.dao;
//
//import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
//import com.okutonda.okudpdv.data.entities.Clients;
//import com.okutonda.okudpdv.data.entities.Payment;
//import com.okutonda.okudpdv.data.entities.PaymentMode;
//import com.okutonda.okudpdv.data.entities.PaymentStatus;
//import com.okutonda.okudpdv.data.entities.User;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import javax.swing.JOptionPane;
//
///**
// *
// * @author kenny
// */
//public class PaymentDao {
//
//    private final Connection conn;
//    PreparedStatement pst = null;
//    ResultSet rs = null;
//
//    public PaymentDao() {
//        this.conn = ConnectionDatabase.getConnect();
//    }
//
//    public PaymentDao(Connection externalConn) { // para transação
//        this.conn = externalConn;
//    }
//
//    public boolean add(Payment p, int orderId) {
//        final String sql = """
//        INSERT INTO payments
//        (description,total,prefix,number,`date`,dateFinish,status,
//         clientId,userId,order_id,order_type,mode,reference,currency)
//        VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)
//    """;
//
//        try (PreparedStatement ps = conn.prepareStatement(sql)) {
//            // 1) Campos básicos
//            ps.setString(1, p.getDescription() != null ? p.getDescription() : "");
//            ps.setBigDecimal(2, p.getTotal() != null ? p.getTotal() : java.math.BigDecimal.ZERO);
//            ps.setString(3, p.getPrefix() != null ? p.getPrefix() : "");
//            ps.setInt(4, p.getNumber());
//            ps.setString(5, p.getDate()); // String ISO/datetime que já estás a usar
//
//            // 2) dateFinish pode ser nulo
//            if (p.getDateFinish() != null && !p.getDateFinish().isEmpty()) {
//                ps.setString(6, p.getDateFinish());
//            } else {
//                ps.setNull(6, java.sql.Types.TIMESTAMP);
//            }
//
//            // 3) ENUMs no BD (guardar name() do enum em MAIÚSCULAS)
//            ps.setString(7, p.getStatus() != null ? p.getStatus().name() : "SUCCESS");
//
//            // 4) relacionamentos / vínculo
//            ps.setInt(8, (p.getClient() != null && p.getClient().getId() > 0) ? p.getClient().getId() : 0);
//            ps.setInt(9, (p.getUser() != null && p.getUser().getId() > 0) ? p.getUser().getId() : 0);
//            ps.setInt(10, orderId);
//
//            // 5) tipo do documento
////            ps.setString(11, p.getInvoiceType() != null ? p.getInvoiceType() : "ORDER");
//            ps.setString(11, "ORDER");
//            // 6) modo de pagamento (ENUM)
//            ps.setString(12, p.getPaymentMode() != null ? p.getPaymentMode().name() : "NUMERARIO");
//
//            // 7) extras
//            ps.setString(13, p.getReference());
//            ps.setString(14, p.getCurrency() != null ? p.getCurrency() : "AOA");
//
//            return ps.executeUpdate() == 1;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao salvar pagamento: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public Boolean edit(Payment obj, int id) {
//        String sql = "UPDATE payments SET dateFinish=?, status=?, reference=?, description=?, currency=? WHERE id=?";
//        try (PreparedStatement pst = this.conn.prepareStatement(sql)) {
//            pst.setString(1, obj.getDateFinish());
//            pst.setString(2, obj.getStatus() != null ? obj.getStatus().name() : null); // enum -> STRING
//            pst.setString(3, obj.getReference());
//            pst.setString(4, obj.getDescription());
//            pst.setString(5, obj.getCurrency());
//            pst.setInt(6, id);
//            return pst.executeUpdate() == 1;
//        } catch (SQLException e) {
//            System.out.println("Erro ao atualizar Payment: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public boolean delete(int id) {
//        String sql = "DELETE FROM payments WHERE id=?";
//        try (PreparedStatement ptmt = conn.prepareStatement(sql)) {
//            ptmt.setInt(1, id);
//            return ptmt.executeUpdate() == 1;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao excluir payment: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public List<Payment> filterDate(LocalDate from, LocalDate to) {
//        List<Payment> list = new ArrayList<>();
//        String sql
//                = "SELECT * FROM payments "
//                + "WHERE DATE(`date`) BETWEEN ? AND ? "
//                + "ORDER BY `date` ASC, id ASC";
//        try (PreparedStatement ptmt = this.conn.prepareStatement(sql)) {
//            ptmt.setDate(1, java.sql.Date.valueOf(from));
//            ptmt.setDate(2, java.sql.Date.valueOf(to));
//            try (ResultSet rs = ptmt.executeQuery()) {
//                while (rs.next()) {
//                    Payment obj = formatObj(rs);
//                    if (obj != null) {
//                        list.add(obj);
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao filtrar payment por data: " + e.getMessage());
//        }
//        return list;
//    }
//
////    public List<Payment> filterDate(LocalDate from, LocalDate to) {
////        List<Payment> list = new ArrayList<>();
////        String sql
////                = "SELECT * FROM payments "
////                + "WHERE DATE(`date`) BETWEEN ? AND ? "
////                + "ORDER BY `date` ASC, id ASC";
////        try (PreparedStatement ptmt = this.conn.prepareStatement(sql)) {
////            ptmt.setDate(1, java.sql.Date.valueOf(from));
////            ptmt.setDate(2, java.sql.Date.valueOf(to));
////            try (ResultSet rs = ptmt.executeQuery()) {
////                while (rs.next()) {
////                    Payment obj = formatObj(rs);
////                    if (obj != null) {
////                        list.add(obj);
////                    }
////                }
////            }
////        } catch (SQLException e) {
////            JOptionPane.showMessageDialog(null, "Erro ao filtrar payment por data: " + e.getMessage());
////        }
////        return list;
////    }
//    public Payment getId(int id) {
//        String sql = "SELECT * FROM payments WHERE id=?";
//        try (PreparedStatement ptmt = conn.prepareStatement(sql)) {
//            ptmt.setInt(1, id);
//            try (ResultSet rs = ptmt.executeQuery()) {
//                if (rs.next()) {
//                    return formatObj(rs);
//                }
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao consultar payment: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public Payment findByReference(String reference) {
//        String sql = "SELECT * FROM payments WHERE reference=?";
//        try (PreparedStatement ptmt = conn.prepareStatement(sql)) {
//            ptmt.setString(1, reference);
//            try (ResultSet rs = ptmt.executeQuery()) {
//                if (rs.next()) {
//                    return formatObj(rs);
//                }
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao consultar payment por referência: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public List<Payment> list(String where) {
//        List<Payment> list = new ArrayList<>();
//        String w = (where != null && !where.trim().isEmpty()) ? " " + where : "";
//        String sql = "SELECT * FROM payments" + w;
//        try (PreparedStatement ptmt = this.conn.prepareStatement(sql); ResultSet rs = ptmt.executeQuery()) {
//            while (rs.next()) {
//                list.add(formatObj(rs));
//            }
//            return list;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao listar payment: " + e.getMessage());
//            return null;
//        }
//    }
//
//    public List<Payment> filter(String txt) {
//        List<Payment> list = new ArrayList<>();
//        String sql = "SELECT * FROM payments WHERE description LIKE ? OR `date` LIKE ? OR prefix LIKE ? OR reference LIKE ?";
//        try (PreparedStatement ptmt = this.conn.prepareStatement(sql)) {
//            String like = "%" + txt + "%";
//            ptmt.setString(1, like);
//            ptmt.setString(2, like);
//            ptmt.setString(3, like);
//            ptmt.setString(4, like);
//            try (ResultSet rs = ptmt.executeQuery()) {
//                while (rs.next()) {
//                    list.add(formatObj(rs));
//                }
//            }
//            return list;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao filtrar payment: " + e.getMessage());
//            return null;
//        }
//    }
//
//    public Payment formatObj(ResultSet rs) {
//        try {
//            Payment obj = new Payment();
//
//            // relacionamentos (usa teus DAOs atuais)
//            User user = new UserDao().findById(rs.getInt("userId"));
//            Clients client = new ClientDao().findById(rs.getInt("clientId"));
//
//            // ENUMs
//            String modeStr = rs.getString("mode");     // coluna VARCHAR("NUMERARIO", ...)
//            String statusStr = rs.getString("status"); // coluna VARCHAR("SUCCESS", ...)
//
//            obj.setId(rs.getInt("id"));
//            obj.setDescription(rs.getString("description"));
//            obj.setTotal(rs.getBigDecimal("total"));          // BigDecimal
//            obj.setPrefix(rs.getString("prefix"));
//            obj.setNumber(rs.getInt("number"));
//            obj.setDate(rs.getString("date"));
//            obj.setDateFinish(rs.getString("dateFinish"));
//            obj.setInvoiceId(rs.getInt("order_id"));
//            obj.setReference(rs.getString("reference"));
//            obj.setCurrency(rs.getString("currency"));
//
//            if (statusStr != null) {
//                obj.setStatus(PaymentStatus.valueOf(statusStr));
//            }
//            if (modeStr != null) {
//                obj.setPaymentMode(PaymentMode.valueOf(modeStr));
//            }
//
//            obj.setUser(user);
//            obj.setClient(client);
//
//            return obj;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao formatar Payment: " + e.getMessage());
//            return null;
//        }
//    }
//}
