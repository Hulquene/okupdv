/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.models.Clients;
import com.okutonda.okudpdv.models.Payment;
import com.okutonda.okudpdv.models.PaymentMode;
import com.okutonda.okudpdv.models.PaymentStatus;
import com.okutonda.okudpdv.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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

    public PaymentDao(Connection externalConn) { // para transação
        this.conn = externalConn;
    }

    public boolean add(Payment p, int orderId) {
        final String sql = """
        INSERT INTO payments
        (description,total,prefix,number,`date`,dateFinish,status,
         clientId,userId,order_id,order_type,mode,reference,currency)
        VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // 1) Campos básicos
            ps.setString(1, p.getDescription() != null ? p.getDescription() : "");
            ps.setBigDecimal(2, p.getTotal() != null ? p.getTotal() : java.math.BigDecimal.ZERO);
            ps.setString(3, p.getPrefix() != null ? p.getPrefix() : "");
            ps.setInt(4, p.getNumber());
            ps.setString(5, p.getDate()); // String ISO/datetime que já estás a usar

            // 2) dateFinish pode ser nulo
            if (p.getDateFinish() != null && !p.getDateFinish().isEmpty()) {
                ps.setString(6, p.getDateFinish());
            } else {
                ps.setNull(6, java.sql.Types.TIMESTAMP);
            }

            // 3) ENUMs no BD (guardar name() do enum em MAIÚSCULAS)
            ps.setString(7, p.getStatus() != null ? p.getStatus().name() : "SUCCESS");

            // 4) relacionamentos / vínculo
            ps.setInt(8, (p.getClient() != null && p.getClient().getId() > 0) ? p.getClient().getId() : 0);
            ps.setInt(9, (p.getUser() != null && p.getUser().getId() > 0) ? p.getUser().getId() : 0);
            ps.setInt(10, orderId);

            // 5) tipo do documento
//            ps.setString(11, p.getInvoiceType() != null ? p.getInvoiceType() : "ORDER");
            ps.setString(11, "ORDER");
            // 6) modo de pagamento (ENUM)
            ps.setString(12, p.getPaymentMode() != null ? p.getPaymentMode().name() : "NUMERARIO");

            // 7) extras
            ps.setString(13, p.getReference());
            ps.setString(14, p.getCurrency() != null ? p.getCurrency() : "AOA");

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar pagamento: " + e.getMessage());
            return false;
        }
    }

    public Boolean edit(Payment obj, int id) {
        String sql = "UPDATE payments SET dateFinish=?, status=?, reference=?, description=?, currency=? WHERE id=?";
        try (PreparedStatement pst = this.conn.prepareStatement(sql)) {
            pst.setString(1, obj.getDateFinish());
            pst.setString(2, obj.getStatus() != null ? obj.getStatus().name() : null); // enum -> STRING
            pst.setString(3, obj.getReference());
            pst.setString(4, obj.getDescription());
            pst.setString(5, obj.getCurrency());
            pst.setInt(6, id);
            return pst.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar Payment: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM payments WHERE id=?";
        try (PreparedStatement ptmt = conn.prepareStatement(sql)) {
            ptmt.setInt(1, id);
            return ptmt.executeUpdate() == 1;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir payment: " + e.getMessage());
            return false;
        }
    }

    public List<Payment> filterDate(LocalDate from, LocalDate to) {
        List<Payment> list = new ArrayList<>();
        String sql
                = "SELECT * FROM payments "
                + "WHERE DATE(`date`) BETWEEN ? AND ? "
                + "ORDER BY `date` ASC, id ASC";
        try (PreparedStatement ptmt = this.conn.prepareStatement(sql)) {
            ptmt.setDate(1, java.sql.Date.valueOf(from));
            ptmt.setDate(2, java.sql.Date.valueOf(to));
            try (ResultSet rs = ptmt.executeQuery()) {
                while (rs.next()) {
                    Payment obj = formatObj(rs);
                    if (obj != null) {
                        list.add(obj);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao filtrar payment por data: " + e.getMessage());
        }
        return list;
    }

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
    public Payment getId(int id) {
        String sql = "SELECT * FROM payments WHERE id=?";
        try (PreparedStatement ptmt = conn.prepareStatement(sql)) {
            ptmt.setInt(1, id);
            try (ResultSet rs = ptmt.executeQuery()) {
                if (rs.next()) {
                    return formatObj(rs);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar payment: " + e.getMessage());
        }
        return null;
    }

    public Payment findByReference(String reference) {
        String sql = "SELECT * FROM payments WHERE reference=?";
        try (PreparedStatement ptmt = conn.prepareStatement(sql)) {
            ptmt.setString(1, reference);
            try (ResultSet rs = ptmt.executeQuery()) {
                if (rs.next()) {
                    return formatObj(rs);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar payment por referência: " + e.getMessage());
        }
        return null;
    }

    public List<Payment> list(String where) {
        List<Payment> list = new ArrayList<>();
        String w = (where != null && !where.trim().isEmpty()) ? " " + where : "";
        String sql = "SELECT * FROM payments" + w;
        try (PreparedStatement ptmt = this.conn.prepareStatement(sql); ResultSet rs = ptmt.executeQuery()) {
            while (rs.next()) {
                list.add(formatObj(rs));
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar payment: " + e.getMessage());
            return null;
        }
    }

    public List<Payment> filter(String txt) {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE description LIKE ? OR `date` LIKE ? OR prefix LIKE ? OR reference LIKE ?";
        try (PreparedStatement ptmt = this.conn.prepareStatement(sql)) {
            String like = "%" + txt + "%";
            ptmt.setString(1, like);
            ptmt.setString(2, like);
            ptmt.setString(3, like);
            ptmt.setString(4, like);
            try (ResultSet rs = ptmt.executeQuery()) {
                while (rs.next()) {
                    list.add(formatObj(rs));
                }
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao filtrar payment: " + e.getMessage());
            return null;
        }
    }

    public Payment formatObj(ResultSet rs) {
        try {
            Payment obj = new Payment();

            // relacionamentos (usa teus DAOs atuais)
            User user = new UserDao().getId(rs.getInt("userId"));
            Clients client = new ClientDao().getId(rs.getInt("clientId"));

            // ENUMs
            String modeStr = rs.getString("mode");     // coluna VARCHAR("NUMERARIO", ...)
            String statusStr = rs.getString("status"); // coluna VARCHAR("SUCCESS", ...)

            obj.setId(rs.getInt("id"));
            obj.setDescription(rs.getString("description"));
            obj.setTotal(rs.getBigDecimal("total"));          // BigDecimal
            obj.setPrefix(rs.getString("prefix"));
            obj.setNumber(rs.getInt("number"));
            obj.setDate(rs.getString("date"));
            obj.setDateFinish(rs.getString("dateFinish"));
            obj.setInvoiceId(rs.getInt("order_id"));
            obj.setReference(rs.getString("reference"));
            obj.setCurrency(rs.getString("currency"));

            if (statusStr != null) {
                obj.setStatus(PaymentStatus.valueOf(statusStr));
            }
            if (modeStr != null) {
                obj.setPaymentMode(PaymentMode.valueOf(modeStr));
            }

            obj.setUser(user);
            obj.setClient(client);

            return obj;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao formatar Payment: " + e.getMessage());
            return null;
        }
    }
}
