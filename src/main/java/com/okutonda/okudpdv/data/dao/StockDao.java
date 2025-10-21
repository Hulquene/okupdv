/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.data.entities.Purchase;
import com.okutonda.okudpdv.data.entities.Stock;
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
public class StockDao {

    private final Connection conn;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private String table = "stocks";

    public StockDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public boolean add(Stock obj) {
        try {
            // 1 passo
            String sql = "INSERT INTO " + table + " (type,qty,description,purchase_id,user_id)"
                    + "values(?,?,?,?,?)";
            // 2 passo
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, obj.getType());
            stmt.setInt(2, obj.getQty());
            stmt.setString(3, obj.getDescription());
            stmt.setInt(4, obj.getPurchase().getId());
            stmt.setInt(5, obj.getUser().getId());
            //3 passo
            stmt.execute();
            // 4 passo
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao salvar stock: " + e.getMessage());
        }
        return false;
    }

//    public boolean edit(Stock obj, int id) {
//        try {
//            // 1 passo
//            String sql = "UPDATE stock SET type=?,code=?,description=?,price=?,tax_id=?,reason_tax_id=?,group_id=?,supplier_id=?,stock_total=?,status=?,barcode=?,purchase_price=? WHERE id=?";
//            // 2 passo
//            pst = this.conn.prepareStatement(sql);
//            pst.setString(1, obj.getType());
//            pst.setString(2, obj.getCode());
//            pst.setString(3, obj.getDescription());
//            pst.setDouble(4, obj.getPrice());
//            pst.setInt(5, obj.getTaxe().getId());
//            pst.setInt(13, id);
//            //3 passo
//            //ptmt.executeQuery();
//            pst.execute();
//            return true;
//        } catch (HeadlessException | SQLException e) {
//            System.out.println("Erro ao atualizar products: " + e.getMessage());
//        }
//        return false;
//    }

//    public boolean updateStock(int id, int stockUpdate) {
//        try {
//            // 1 passo
//            String sql = "UPDATE products SET stock_total=? WHERE id=?";
//            // 2 passo
//            pst = this.conn.prepareStatement(sql);
//            pst.setInt(1, stockUpdate);
//            pst.setInt(2, id);
//            //3 passo
//            //ptmt.executeQuery();
//            pst.execute();
//            return true;
//
//        } catch (HeadlessException | SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao atualizar estoque do products: " + e.getMessage());
//        }
//        return false;
//    }

    public Boolean delete(int id) {
        try {
            // 1 passo
            String sql = "DELETE FROM " + table + " WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir stock: " + e.getMessage());
        }
        return false;
    }

//    public Stock searchFromBarCode(String barCode) {
//        try {
//            // 1 passo
//            String sql = "SELECT * FROM stock WHERE barcode =?";
//            pst = conn.prepareStatement(sql);
//            pst.setString(1, barCode);
//            rs = pst.executeQuery();
//            Stock obj = new Stock();
//
//            if (rs.next()) {
//                obj = formatObj(rs);
//            }
//            return obj;
//            // 2 passo
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do stock: " + e.getMessage());
//        }
//        return null;
//    }

    public Stock getId(int id) {
        try {
            // 1 passo
            String sql = "SELECT * FROM " + table + " WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            Stock obj = new Stock();

            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do stock: " + e.getMessage());
        }
        return null;
    }

//    public Stock searchFromName(String description) {
//        try {
//            // 1 passo
//            String sql = "SELECT * FROM stock WHERE description =?";
//            pst = conn.prepareStatement(sql);
//            pst.setString(1, description);
//            rs = pst.executeQuery();
//            Stock obj = new Stock();
//            if (rs.next()) {
//                obj = formatObj(rs);
//            }
//            return obj;
//            // 2 passo
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do produto: " + e.getMessage());
//        }
//        return null;
//    }

    public List<Stock> list(String where) {
        List<Stock> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM " + table + " " + where;
            pst = this.conn.prepareStatement(sql);
            rs = pst.executeQuery();
            Stock obj;
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de Stock: " + e.getMessage());
        }
        return null;
    }

    public List<Stock> listHistoryStock(String where) {
        List<Stock> list = new ArrayList<>();
        try {
//            String sql = "SELECT "
//                    + "p.supplier_id,p.group_id,p.tax_id,p.status,p.reason_tax_id,pcode,"
//                    + "p.barcode,p.price,p.purchase_price, p.description,p.stock_total,"
//                    + "coalesce(sum(po.qty),0) as total_qtd "
//                    + "FROM "
//                    + "products p "
//                    + "LEFT JOIN "
//                    + "products_order po ON p.id = po.product_id  "
//                    + "GROUP BY "
//                    + "p.id,p.description,p.stock_total";

            String sql = "SELECT * FROM stock " + where;
            pst = this.conn.prepareStatement(sql);
            rs = pst.executeQuery();
            Stock obj;
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de Stock: " + e.getMessage());
        }
        return null;
    }

    public List<Stock> filter(String txt) {
        List<Stock> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM " + table + " WHERE description LIKE ?  OR type LIKE ? OR created_at LIKE ?";
//            String sql = "SELECT * FROM products WHERE description LIKE ?";
            pst = this.conn.prepareStatement(sql);
            pst.setString(1, "%" + txt + "%");
            pst.setString(2, "%" + txt + "%");
            pst.setString(3, "%" + txt + "%");
            rs = pst.executeQuery();
            Stock obj;// = new Product();
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de stock: " + e.getMessage());
        }
        return null;
    }

//    public List<Product> filterProduct(String txt) {
//        List<Product> list = new ArrayList<>();
//        try {
//            String sql = "SELECT * FROM products WHERE description LIKE ?  OR type LIKE ? OR barcode LIKE ?  OR price LIKE ? AND type LIKE ?";
////            String sql = "SELECT * FROM products WHERE description LIKE ?";
//            pst = this.conn.prepareStatement(sql);
//            pst.setString(1, "%" + txt + "%");
//            pst.setString(2, "%" + txt + "%");
//            pst.setString(3, "%" + txt + "%");
//            pst.setString(4, "%" + txt + "%");
//            pst.setString(5, "product");
//
//            rs = pst.executeQuery();
//            Product obj;// = new Product();
//            while (rs.next()) {
//                obj = formatObj(rs);
//                list.add(obj);
//            }
//            return list;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de produtos: " + e.getMessage());
//        }
//        return null;
//    }
    public Stock formatObj(ResultSet rs) {
        try {
            Stock obj = new Stock();
            Purchase pur;
            PurchaseDao pDao = new PurchaseDao();
            pur = null;//pDao.getId(rs.getInt("purchase_id"));
            
            User user;
            UserDao uDao = new UserDao();
            user = uDao.getId(rs.getInt("user_id"));

            obj.setId(rs.getInt("id"));
            obj.setType(rs.getString("type"));
            obj.setDescription(rs.getString("description"));
            obj.setPurchase(pur);
            obj.setUser(user);
            obj.setQty(rs.getInt("qty"));
            return obj;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao formatar obj Stock: " + e.getMessage());
        }
        return null;
    }

//    public List<Product> listProduct(ResultSet rs) {
//        while (rs.next()) {
//            Product obj = new Product();
//            obj.setId(rs.getInt("id"));
//            obj.setType(rs.getString("type"));
//            obj.setCode(rs.getString("code"));
//            obj.setDescription(rs.getString("description"));
//            obj.setLongDescription(rs.getString("long_description"));
//            obj.setPrice(rs.getDouble("price"));
//            obj.setPurchasePrice(rs.getDouble("purchase_price"));
//            obj.setTaxeId(rs.getInt("taxe_id"));
//            obj.setReasonTaxeId(rs.getInt("reason_taxe_id"));
//            obj.setGroupId(rs.getInt("group_id"));
//            obj.setSubGroupId(rs.getInt("sub_group_id"));
//            obj.setUnitId(rs.getInt("unit_id"));
//            obj.setStockTotal(rs.getInt("stock_total"));
//            obj.setStockMin(rs.getInt("stock_value_min"));
//            obj.setStockMax(rs.getInt("stock_value_max"));
//            obj.setStatus(rs.getInt("status"));
//            list.add(obj);
//        }
//        return list;
//    }
    public int getQtdStock(int id) {
        try {
            int qtdStock = 0;
            String sql = "SELECT stock_total FROM products WHERE id=?";
            PreparedStatement ptmt = this.conn.prepareStatement(sql);
            ptmt.setInt(1, id);
            ResultSet rs = ptmt.executeQuery();
            if (rs.next()) {
                qtdStock = rs.getInt("stock_total");
            }
            return qtdStock;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar stock_total de produtos: " + e.getMessage());
        }
        return 0;
    }

}
