/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.models.Product;
import com.okutonda.okudpdv.models.Purchase;
import com.okutonda.okudpdv.models.Supplier;
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
public class PurchaseDao {

    private final Connection conn;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private String table = "purchases";

    public PurchaseDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public boolean add(Purchase obj) {
        try {
            // 1 passo
            String sql = "INSERT INTO " + table + " (date,product_id,description,total,price_purchase,price_sale,user_id,supplier_id,qty,status,status_payment)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?)";
            // 2 passo
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, obj.getDate());
            stmt.setInt(2, obj.getProduct().getId());
            stmt.setString(3, obj.getDescription());
            stmt.setDouble(4, obj.getTotal());
            stmt.setDouble(5, obj.getPricePurchase());
            stmt.setDouble(6, obj.getPriceSale());
            stmt.setInt(7, obj.getUser().getId());
            stmt.setInt(8, obj.getSupplier().getId());
            stmt.setInt(9, obj.getQty());
            stmt.setString(10, obj.getStatus());
            stmt.setString(11, obj.getStatusPayment());
            //3 passo
            stmt.execute();
            // 4 passo
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao salvar Compra: " + e.getMessage());
        }
        return false;
    }

//    public boolean edit(Purchase obj, int id) {
//        try {
//            // 1 passo
//            String sql = "UPDATE products SET type=?,code=?,description=?,price=?,tax_id=?,reason_tax_id=?,group_id=?,supplier_id=?,stock_total=?,status=?,barcode=?,purchase_price=? WHERE id=?";
//            // 2 passo
//            pst = this.conn.prepareStatement(sql);
//            pst.setString(1, obj.getDate());
//            pst.setString(2, obj.getProduct().get);
//            pst.setString(3, obj.getDescription());
//            pst.setDouble(4, obj.getPrice());
//            pst.setInt(5, obj.getTaxe().getId());
//            pst.setInt(6, obj.getReasonTaxe().getId());
//            pst.setInt(7, obj.getGroup().getId());
//            pst.setInt(8, obj.getSupplier().getId());
//            pst.setInt(9, obj.getStockTotal());
//            pst.setString(10, obj.getStatus());
//            pst.setString(11, obj.getBarcode());
//            pst.setDouble(12, obj.getPurchasePrice());
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
    public Boolean delete(int id) {
        try {
            // 1 passo
            String sql = "DELETE FROM " + table + " WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir compra: " + e.getMessage());
        }
        return false;
    }

    public Purchase getFromProdId(int idProd) {
        try {
            // 1 passo
            String sql = "SELECT * FROM " + table + " WHERE product_id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, idProd);
            rs = pst.executeQuery();
            Purchase obj = new Purchase();

            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta da compra: " + e.getMessage());
        }
        return null;
    }

    public Purchase getId(int id) {
        try {
            // 1 passo
            String sql = "SELECT * FROM " + table + " WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            Purchase obj = new Purchase();

            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Compra: " + e.getMessage());
        }
        return null;
    }

    public Purchase getFromDescription(String description) {
        try {
            // 1 passo
            String sql = "SELECT * FROM " + table + " WHERE description =?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, description);
            rs = pst.executeQuery();
            Purchase obj = new Purchase();
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Compra: " + e.getMessage());
        }
        return null;
    }

    public List<Purchase> list(String where) {
        List<Purchase> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM " + table + " " + where;
            pst = this.conn.prepareStatement(sql);
            rs = pst.executeQuery();
            Purchase obj;
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de Compra: " + e.getMessage());
        }
        return null;
    }

//    public List<Purchase> listHistoryStock(String where) {
//        List<Purchase> list = new ArrayList<>();
//        try {
////            String sql = "SELECT "
////                    + "p.supplier_id,p.group_id,p.tax_id,p.status,p.reason_tax_id,pcode,"
////                    + "p.barcode,p.price,p.purchase_price, p.description,p.stock_total,"
////                    + "coalesce(sum(po.qty),0) as total_qtd "
////                    + "FROM "
////                    + "products p "
////                    + "LEFT JOIN "
////                    + "products_order po ON p.id = po.product_id  "
////                    + "GROUP BY "
////                    + "p.id,p.description,p.stock_total";
//
//            String sql = "SELECT * FROM products " + where;
//            pst = this.conn.prepareStatement(sql);
//            rs = pst.executeQuery();
//            Product obj;
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
    public List<Purchase> filter(String txt) {
        List<Purchase> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM " + table + " WHERE price_purchase LIKE ?  OR description LIKE ? OR date LIKE ?  OR total LIKE ?";
//            String sql = "SELECT * FROM products WHERE description LIKE ?";
            pst = this.conn.prepareStatement(sql);
            pst.setString(1, "%" + txt + "%");
            pst.setString(2, "%" + txt + "%");
            pst.setString(3, "%" + txt + "%");
            pst.setString(4, "%" + txt + "%");
            rs = pst.executeQuery();
            Purchase obj;// = new Product();
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de Compra: " + e.getMessage());
        }
        return null;
    }

//    public List<Purchase> filterProduct(String txt) {
//        List<Purchase> list = new ArrayList<>();
//        try {
//            String sql = "SELECT * FROM purchase WHERE description LIKE ?  OR type LIKE ? OR barcode LIKE ?  OR price LIKE ? AND type LIKE ?";
////            String sql = "SELECT * FROM products WHERE description LIKE ?";
//            pst = this.conn.prepareStatement(sql);
//            pst.setString(1, "%" + txt + "%");
//            pst.setString(2, "%" + txt + "%");
//            pst.setString(3, "%" + txt + "%");
//            pst.setString(4, "%" + txt + "%");
//            pst.setString(5, "product");
//
//            rs = pst.executeQuery();
//            Purchase obj;// = new Product();
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
    public Purchase formatObj(ResultSet rs) {
        try {
            Purchase obj = new Purchase();
            Supplier supp;
            User user;
            Product prod;

            SupplierDao sDao = new SupplierDao();
            supp = sDao.getFromId(rs.getInt("supplier_id"));
            UserDao uDao = new UserDao();
            user = uDao.getId(rs.getInt("user_id"));
            ProductDao pDao = new ProductDao();
            prod = pDao.getId(rs.getInt("product_id"));
            obj.setId(rs.getInt("id"));
            obj.setDate(rs.getString("date"));
            obj.setDescription(rs.getString("description"));
            obj.setTotal(rs.getDouble("total"));
            obj.setPricePurchase(rs.getDouble("price_purchase"));
            obj.setPriceSale(rs.getDouble("price_sale"));
            obj.setUser(user);
            obj.setProduct(prod);
            obj.setSupplier(supp);
            obj.setQty(rs.getInt("qty"));
            obj.setStatus(rs.getString("status"));
            obj.setStatusPayment(rs.getString("status_payment"));
            return obj;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao formatar obj Compra: " + e.getMessage());
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
//    public int getQtdStock(int id) {
//        try {
//            int qtdStock = 0;
//            String sql = "SELECT stock_total FROM products WHERE id=?";
//            PreparedStatement ptmt = this.conn.prepareStatement(sql);
//            ptmt.setInt(1, id);
//            ResultSet rs = ptmt.executeQuery();
//            if (rs.next()) {
//                qtdStock = rs.getInt("stock_total");
//            }
//            return qtdStock;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao pegar stock_total de produtos: " + e.getMessage());
//        }
//        return 0;
//    }
}
