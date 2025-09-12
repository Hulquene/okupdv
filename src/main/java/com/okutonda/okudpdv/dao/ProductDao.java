/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.models.GroupsProduct;
import com.okutonda.okudpdv.models.Product;
import com.okutonda.okudpdv.models.ReasonTaxes;
import com.okutonda.okudpdv.models.Supplier;
import com.okutonda.okudpdv.models.Taxes;
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
public class ProductDao {

    private final Connection conn;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public ProductDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public boolean add(Product obj) {
        try {
            // 1 passo
            String sql = "INSERT INTO products (type,code,description,price,tax_id,reason_tax_id,group_id,supplier_id,stock_total,status,barcode,purchase_price)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?)";
            // 2 passo
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, obj.getType());
            stmt.setString(2, obj.getCode());
            stmt.setString(3, obj.getDescription());
            stmt.setDouble(4, obj.getPrice());
            stmt.setInt(5, obj.getTaxe().getId());
            stmt.setInt(6, obj.getReasonTaxe().getId());
            stmt.setInt(7, obj.getGroup().getId());
            stmt.setInt(8, obj.getSupplier().getId());
            stmt.setInt(9, obj.getStockTotal());
            stmt.setInt(10, obj.getStatus());
            stmt.setString(11, obj.getBarcode());
            stmt.setDouble(12, obj.getPurchasePrice());
            //3 passo
            stmt.execute();
            // 4 passo
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao salvar Produto: " + e.getMessage());
        }
        return false;
    }

    public boolean edit(Product obj, int id) {
        try {
            // 1 passo
            String sql = "UPDATE products SET type=?,code=?,description=?,price=?,tax_id=?,reason_tax_id=?,group_id=?,supplier_id=?,stock_total=?,status=?,barcode=?,purchase_price=? WHERE id=?";
            // 2 passo
            pst = this.conn.prepareStatement(sql);
            pst.setString(1, obj.getType());
            pst.setString(2, obj.getCode());
            pst.setString(3, obj.getDescription());
            pst.setDouble(4, obj.getPrice());
            pst.setInt(5, obj.getTaxe().getId());
            pst.setInt(6, obj.getReasonTaxe().getId());
            pst.setInt(7, obj.getGroup().getId());
            pst.setInt(8, obj.getSupplier().getId());
            pst.setInt(9, obj.getStockTotal());
            pst.setInt(10, obj.getStatus());
            pst.setString(11, obj.getBarcode());
            pst.setDouble(12, obj.getPurchasePrice());
            pst.setInt(13, id);
            //3 passo
            //ptmt.executeQuery();
            pst.execute();
            return true;
        } catch (HeadlessException | SQLException e) {
            System.out.println("Erro ao atualizar products: " + e.getMessage());
        }
        return false;
    }

    public boolean updateStock(int id, int stockUpdate) {
        try {
            // 1 passo
            String sql = "UPDATE products SET stock_total=? WHERE id=?";
            // 2 passo
            pst = this.conn.prepareStatement(sql);
            pst.setInt(1, stockUpdate);
            pst.setInt(2, id);
            //3 passo
            //ptmt.executeQuery();
            pst.execute();
            return true;

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar estoque do products: " + e.getMessage());
        }
        return false;
    }

    public Boolean delete(int id) {
        try {
            // 1 passo
            String sql = "DELETE FROM products WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir products: " + e.getMessage());
        }
        return false;
    }

    public Product searchFromBarCode(String barCode) {
        try {
            // 1 passo
            String sql = "SELECT * FROM products WHERE barcode =?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, barCode);
            rs = pst.executeQuery();
            Product obj = new Product();

            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do produto: " + e.getMessage());
        }
        return null;
    }

    public Product getId(int id) {
        try {
            // 1 passo
            String sql = "SELECT * FROM products WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            Product obj = new Product();

            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do produto: " + e.getMessage());
        }
        return null;
    }

    public Product searchFromName(String description) {
        try {
            // 1 passo
            String sql = "SELECT * FROM products WHERE description =?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, description);
            rs = pst.executeQuery();
            Product obj = new Product();
            if (rs.next()) {
                obj = formatObj(rs);
            }
            return obj;
            // 2 passo
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do produto: " + e.getMessage());
        }
        return null;
    }

    public List<Product> list(String where) {
        List<Product> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM products " + where;
            pst = this.conn.prepareStatement(sql);
            rs = pst.executeQuery();
            Product obj;
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de produtos: " + e.getMessage());
        }
        return null;
    }

    public List<Product> listHistoryStock(String where) {
        List<Product> list = new ArrayList<>();
        try {
            String sql = "SELECT "
                    + "p.id,p.type,p.supplier_id,p.group_id,p.tax_id,p.status,p.reason_tax_id,p.code,"
                    + "p.barcode,p.price,p.purchase_price, p.description,p.stock_total,"
                    + "coalesce(sum(po.qty),0) as total_qtd "
                    + "FROM "
                    + "products p "
                    + "LEFT JOIN "
                    + "products_order po ON p.id = po.product_id  "
                    + "GROUP BY "
                    + "p.id,p.description,p.stock_total";

//            String sql = "SELECT * FROM products " + where;
            pst = this.conn.prepareStatement(sql);
            rs = pst.executeQuery();
            Product obj;
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de produtos: " + e.getMessage());
        }
        return null;
    }

    public List<Product> filter(String txt) {
        List<Product> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM products WHERE description LIKE ?  OR type LIKE ? OR barcode LIKE ?  OR price LIKE ?";
//            String sql = "SELECT * FROM products WHERE description LIKE ?";
            pst = this.conn.prepareStatement(sql);
            pst.setString(1, "%" + txt + "%");
            pst.setString(2, "%" + txt + "%");
            pst.setString(3, "%" + txt + "%");
            pst.setString(4, "%" + txt + "%");
            rs = pst.executeQuery();
            Product obj;// = new Product();
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de produtos: " + e.getMessage());
        }
        return null;
    }

    public List<Product> filterProduct(String txt) {
        List<Product> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM products WHERE description LIKE ?  OR type LIKE ? OR barcode LIKE ?  OR price LIKE ? AND type LIKE ?";
//            String sql = "SELECT * FROM products WHERE description LIKE ?";
            pst = this.conn.prepareStatement(sql);
            pst.setString(1, "%" + txt + "%");
            pst.setString(2, "%" + txt + "%");
            pst.setString(3, "%" + txt + "%");
            pst.setString(4, "%" + txt + "%");
            pst.setString(5, "product");

            rs = pst.executeQuery();
            Product obj;// = new Product();
            while (rs.next()) {
                obj = formatObj(rs);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de produtos: " + e.getMessage());
        }
        return null;
    }

    public Product formatObj(ResultSet rs) {
        try {
            Product obj = new Product();
            Supplier supp;
            Taxes tax;
            ReasonTaxes resaonTax;
            GroupsProduct groups;

            SupplierDao sDao = new SupplierDao();
            supp = sDao.getFromId(rs.getInt("supplier_id"));

            GroupsProductDao gDao = new GroupsProductDao();
            groups = gDao.searchFromId(rs.getInt("group_id"));

            TaxeDao tDao = new TaxeDao();
            tax = tDao.searchFromId(rs.getInt("tax_id"));

            ReasonTaxeDao rDao = new ReasonTaxeDao();
            resaonTax = rDao.searchFromId(rs.getInt("reason_tax_id"));

            obj.setId(rs.getInt("id"));
            obj.setType(rs.getString("type"));
            obj.setCode(rs.getString("code"));
            obj.setBarcode(rs.getString("barcode"));
            obj.setDescription(rs.getString("description"));
            obj.setPrice(rs.getDouble("price"));
            obj.setPurchasePrice(rs.getDouble("purchase_price"));
            obj.setGroup(groups);
            obj.setTaxe(tax);
            obj.setReasonTaxe(resaonTax);
            obj.setSupplier(supp);
            obj.setStockTotal(rs.getInt("stock_total"));
            obj.setStatus(rs.getInt("status"));
            return obj;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao formatar obj produtos: " + e.getMessage());
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
