/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.models.Product;
import com.okutonda.okudpdv.models.ProductOrder;
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
public class ProductOrderDao {

    private final Connection conn;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public ProductOrderDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public boolean add(ProductOrder obj, int orderId) {
        try {
            // 1 passo
            String sql = "INSERT INTO products_order (order_id,product_id,description,qty,price,unit,prod_code,taxe_code,taxe_name,taxe_percentage,reason_tax,reason_code)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?)";
            // 2 passo
            pst = this.conn.prepareStatement(sql);
            pst.setInt(1, orderId);
            pst.setInt(2, obj.getProduct().getId());
//            ptmt.setString(3, obj.getDate());
            pst.setString(3, obj.getDescription());
            pst.setInt(4, obj.getQty());
            pst.setDouble(5, obj.getPrice());
            pst.setString(6, obj.getUnit());
            pst.setString(7, obj.getCode());
            pst.setString(8, obj.getTaxeCode());
            pst.setString(9, obj.getTaxeName());
            pst.setDouble(10, obj.getTaxePercentage());
            pst.setString(11, obj.getReasonTax());
            pst.setString(12, obj.getReasonCode());
            //3 passo
            pst.execute();
            return true;
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar products_order: " + e.getMessage());
        }
        return false;
    }

    public boolean edit(ProductOrder obj) {
        try {
            // 1 passo
            String sql = "UPDATE products_order SET status=?,datecreate=?,number=?,prefix=?,total=?,sub_total=?,pay_total=?,amount_returned=?,hash=?,client_id=?,seller_id=? WHERE id=?";
            // 2 passo
            pst = this.conn.prepareStatement(sql);
            pst.setInt(1, obj.getOrderId());
            pst.setInt(2, obj.getProduct().getId());
            pst.setString(3, obj.getDate());
            pst.setString(4, obj.getDescription());
            pst.setInt(5, obj.getQty());
            pst.setDouble(6, obj.getPrice());
            pst.setString(7, obj.getUnit());
            pst.setString(8, obj.getCode());
            pst.setString(9, obj.getTaxeCode());
            pst.setString(10, obj.getTaxeName());
            pst.setDouble(11, obj.getTaxePercentage());
            pst.setString(12, obj.getReasonTax());
            pst.setString(13, obj.getReasonCode());
            //3 passo
            //ptmt.executeQuery();
            pst.execute();
            return true;

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar products_order: " + e.getMessage());
        }
        return false;
    }

    public void delete(int id) {
        try {
            // 1 passo
            String sql = "DELETE FROM products_order WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.execute();
            JOptionPane.showMessageDialog(null, "products_order excluido com Sucesso!!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir products_order: " + e.getMessage());
        }
    }

    public ProductOrder searchFromId(int id) {
        try {
            // 1 passo
            String sql = "SELECT * FROM products_order WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            ProductOrder obj = new ProductOrder();
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Order: " + e.getMessage());
        }
        return null;
    }

    public List<ProductOrder> listProductFromOrderId(int id) {

        List<ProductOrder> list = new ArrayList<>();
        try {
            // 1 passo
            String sql = "SELECT * FROM products_order WHERE order_id=?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            ProductOrder obj;
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Order: " + e.getMessage());
        }
        return null;
    }

    public List<ProductOrder> list(String where) {
        List<ProductOrder> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM products_order " + where;
            pst = this.conn.prepareStatement(sql);
            rs = pst.executeQuery();
            ProductOrder obj;// = new Order();
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de products_order: " + e.getMessage());
        }
        return null;
    }

    public List<ProductOrder> filter(String txt) {
        List<ProductOrder> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM products_order WHERE description LIKE ?";
            pst = this.conn.prepareStatement(sql);
            pst.setString(1, "%" + txt + "%");
            rs = pst.executeQuery();
            ProductOrder obj;// = new Order();
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de products_order: " + e.getMessage());
        }
        return null;
    }

//    public int getNextNumberOrder() {
//        List<ProductOrder> list = new ArrayList<>();
//        try {
//            int nextNumber = 1;
//            String sql = "SELECT max(number) number FROM products_order";
//            PreparedStatement ptmt = this.conn.prepareStatement(sql);
//            ResultSet rs = ptmt.executeQuery();
//
//            ProductOrder obj = new ProductOrder();
//            while (rs.next()) {
//                obj.setNumber(rs.getInt("number"));
//                nextNumber = obj.getNumber() + 1;
//            }
//            return nextNumber;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao pegar o proximo id: " + e.getMessage());
//        }
//        return 0;
//    }
    public ProductOrder formatObj(ResultSet rs) {
        try {
            ProductOrder obj = new ProductOrder();
            Product product;// = new Client();
            ProductDao productDao = new ProductDao();
            product = productDao.getId(rs.getInt("product_id"));
//            Order order;// = new Order();
//            OrderDao orderDao = new OrderDao();
//            order = orderDao.searchFromId(rs.getInt("order_id"));
            obj.setId(rs.getInt("id"));
            obj.setProduct(product);
            obj.setOrderId(rs.getInt("order_id"));
            obj.setDate(rs.getString("date"));
            obj.setDescription(rs.getString("description"));
            obj.setQty(rs.getInt("qty"));
            obj.setPrice(rs.getDouble("price"));
            obj.setUnit(rs.getString("unit"));
            obj.setCode(rs.getString("prod_code"));
            obj.setTaxeName(rs.getString("taxe_name"));
            obj.setTaxePercentage(rs.getDouble("taxe_percentage"));
            obj.setTaxeCode(rs.getString("taxe_code"));
            return obj;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao formatar obj products_order: " + e.getMessage());
        }
        return null;
    }
}
