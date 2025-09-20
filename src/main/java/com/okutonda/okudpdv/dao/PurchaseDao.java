/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.models.Purchase;
import com.okutonda.okudpdv.models.PurchaseItem;
import com.okutonda.okudpdv.models.Supplier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kenny
 */
public class PurchaseDao {

    private final Connection conn;

    public PurchaseDao() {
        this.conn = ConnectionDatabase.getConnect();
    }

    public boolean add(Purchase purchase) {
        String sqlPurchase = """
            INSERT INTO purchases
            (supplier_id, invoice_number, invoice_type, descricao, total, iva_total, data_compra, data_vencimento, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pst = conn.prepareStatement(sqlPurchase, Statement.RETURN_GENERATED_KEYS)) {
            // Inserir dados da compra
            pst.setInt(1, purchase.getSupplier().getId());
            pst.setString(2, purchase.getInvoiceNumber());
            pst.setString(3, purchase.getInvoiceType());
            pst.setString(4, purchase.getDescricao());
            pst.setBigDecimal(5, purchase.getTotal());
            pst.setBigDecimal(6, purchase.getIvaTotal());
            pst.setDate(7, new java.sql.Date(purchase.getDataCompra().getTime()));
            pst.setDate(8, new java.sql.Date(purchase.getDataVencimento().getTime()));
            pst.setString(9, purchase.getStatus());

            int affected = pst.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Erro ao inserir compra.");
            }

            // Pegar o ID da compra
            int purchaseId;
            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    purchaseId = rs.getInt(1);
                } else {
                    throw new SQLException("Erro ao obter ID da compra.");
                }
            }

            // Inserir itens
            if (purchase.getItems() != null) {
                String sqlItem = """
                    INSERT INTO purchase_items
                    (purchase_id, product_id, quantidade, preco_custo, iva, subtotal)
                    VALUES (?, ?, ?, ?, ?, ?)
                """;

                try (PreparedStatement pstItem = conn.prepareStatement(sqlItem)) {
                    for (PurchaseItem item : purchase.getItems()) {
                        pstItem.setInt(1, purchaseId);
                        pstItem.setInt(2, item.getProduct().getId());
                        pstItem.setInt(3, item.getQuantidade());
                        pstItem.setBigDecimal(4, item.getPrecoCusto());
                        pstItem.setBigDecimal(5, item.getIva());
                        pstItem.setBigDecimal(6, item.getSubtotal());
                        pstItem.addBatch();

                        // Atualizar stock do produto
                        atualizarStock(item.getProduct().getId(), item.getQuantidade());
                    }
                    pstItem.executeBatch();
                }
            }

            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar compra: " + e.getMessage());
            return false;
        }
    }

    private void atualizarStock(int productId, int qtd) throws SQLException {
        String sql = "UPDATE products SET stock = stock + ? WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, qtd);
            pst.setInt(2, productId);
            pst.executeUpdate();
        }
    }

    public List<Purchase> list() {
        List<Purchase> list = new ArrayList<>();
        String sql = "SELECT p.*, s.company AS supplier_name FROM purchases p INNER JOIN suppliers s ON p.supplier_id = s.id ORDER BY p.data_compra DESC";
        try (PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Purchase obj = new Purchase();
                obj.setId(rs.getInt("id"));
                obj.setInvoiceNumber(rs.getString("invoice_number"));
                obj.setInvoiceType(rs.getString("invoice_type"));
                obj.setDescricao(rs.getString("descricao"));
                obj.setTotal(rs.getBigDecimal("total"));
                obj.setIvaTotal(rs.getBigDecimal("iva_total"));
                obj.setDataCompra(rs.getDate("data_compra"));
                obj.setDataVencimento(rs.getDate("data_vencimento"));
                obj.setStatus(rs.getString("status"));

                Supplier supplier = new Supplier();
                supplier.setId(rs.getInt("supplier_id"));
                supplier.setName(rs.getString("supplier_name"));
                obj.setSupplier(supplier);

                list.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar compras: " + e.getMessage());
        }
        return list;
    }
//    private final Connection conn;
//    PreparedStatement pst = null;
//    ResultSet rs = null;
//    private String table = "purchases";
//
//    public PurchaseDao() {
//        this.conn = ConnectionDatabase.getConnect();
//    }
//
//    public boolean add(Purchase obj) {
//        try {
//            // 1 passo
//            String sql = "INSERT INTO " + table + " (date,product_id,description,total,price_purchase,price_sale,user_id,supplier_id,qty,status,status_payment)"
//                    + "values(?,?,?,?,?,?,?,?,?,?,?)";
//            // 2 passo
//            PreparedStatement stmt = this.conn.prepareStatement(sql);
//            stmt.setString(1, obj.getDate());
//            stmt.setInt(2, obj.getProduct().getId());
//            stmt.setString(3, obj.getDescription());
//            stmt.setDouble(4, obj.getTotal());
//            stmt.setDouble(5, obj.getPricePurchase());
//            stmt.setDouble(6, obj.getPriceSale());
//            stmt.setInt(7, obj.getUser().getId());
//            stmt.setInt(8, obj.getSupplier().getId());
//            stmt.setInt(9, obj.getQty());
//            stmt.setString(10, obj.getStatus());
//            stmt.setString(11, obj.getStatusPayment());
//            //3 passo
//            stmt.execute();
//            // 4 passo
//            return true;
//        } catch (HeadlessException | SQLException e) {
//            System.out.println("Erro ao salvar Compra: " + e.getMessage());
//        }
//        return false;
//    }
//
////    public boolean edit(Purchase obj, int id) {
////        try {
////            // 1 passo
////            String sql = "UPDATE products SET type=?,code=?,description=?,price=?,tax_id=?,reason_tax_id=?,group_id=?,supplier_id=?,stock_total=?,status=?,barcode=?,purchase_price=? WHERE id=?";
////            // 2 passo
////            pst = this.conn.prepareStatement(sql);
////            pst.setString(1, obj.getDate());
////            pst.setString(2, obj.getProduct().get);
////            pst.setString(3, obj.getDescription());
////            pst.setDouble(4, obj.getPrice());
////            pst.setInt(5, obj.getTaxe().getId());
////            pst.setInt(6, obj.getReasonTaxe().getId());
////            pst.setInt(7, obj.getGroup().getId());
////            pst.setInt(8, obj.getSupplier().getId());
////            pst.setInt(9, obj.getStockTotal());
////            pst.setString(10, obj.getStatus());
////            pst.setString(11, obj.getBarcode());
////            pst.setDouble(12, obj.getPurchasePrice());
////            pst.setInt(13, id);
////            //3 passo
////            //ptmt.executeQuery();
////            pst.execute();
////            return true;
////        } catch (HeadlessException | SQLException e) {
////            System.out.println("Erro ao atualizar products: " + e.getMessage());
////        }
////        return false;
////    }
//    public Boolean delete(int id) {
//        try {
//            // 1 passo
//            String sql = "DELETE FROM " + table + " WHERE id =?";
//            pst = conn.prepareStatement(sql);
//            pst.setInt(1, id);
//            pst.execute();
//            return true;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao excluir compra: " + e.getMessage());
//        }
//        return false;
//    }
//
//    public Purchase getFromProdId(int idProd) {
//        try {
//            // 1 passo
//            String sql = "SELECT * FROM " + table + " WHERE product_id =?";
//            pst = conn.prepareStatement(sql);
//            pst.setInt(1, idProd);
//            rs = pst.executeQuery();
//            Purchase obj = new Purchase();
//
//            if (rs.next()) {
//                obj = formatObj(rs);
//            }
//            return obj;
//            // 2 passo
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta da compra: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public Purchase getId(int id) {
//        try {
//            // 1 passo
//            String sql = "SELECT * FROM " + table + " WHERE id =?";
//            pst = conn.prepareStatement(sql);
//            pst.setInt(1, id);
//            rs = pst.executeQuery();
//            Purchase obj = new Purchase();
//
//            if (rs.next()) {
//                obj = formatObj(rs);
//            }
//            return obj;
//            // 2 passo
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Compra: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public Purchase getFromDescription(String description) {
//        try {
//            // 1 passo
//            String sql = "SELECT * FROM " + table + " WHERE description =?";
//            pst = conn.prepareStatement(sql);
//            pst.setString(1, description);
//            rs = pst.executeQuery();
//            Purchase obj = new Purchase();
//            if (rs.next()) {
//                obj = formatObj(rs);
//            }
//            return obj;
//            // 2 passo
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Compra: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public List<Purchase> list(String where) {
//        List<Purchase> list = new ArrayList<>();
//        try {
//            String sql = "SELECT * FROM " + table + " " + where;
//            pst = this.conn.prepareStatement(sql);
//            rs = pst.executeQuery();
//            Purchase obj;
//            while (rs.next()) {
//                obj = formatObj(rs);
//                list.add(obj);
//            }
//            return list;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de Compra: " + e.getMessage());
//        }
//        return null;
//    }
//
////    public List<Purchase> listHistoryStock(String where) {
////        List<Purchase> list = new ArrayList<>();
////        try {
//////            String sql = "SELECT "
//////                    + "p.supplier_id,p.group_id,p.tax_id,p.status,p.reason_tax_id,pcode,"
//////                    + "p.barcode,p.price,p.purchase_price, p.description,p.stock_total,"
//////                    + "coalesce(sum(po.qty),0) as total_qtd "
//////                    + "FROM "
//////                    + "products p "
//////                    + "LEFT JOIN "
//////                    + "products_order po ON p.id = po.product_id  "
//////                    + "GROUP BY "
//////                    + "p.id,p.description,p.stock_total";
////
////            String sql = "SELECT * FROM products " + where;
////            pst = this.conn.prepareStatement(sql);
////            rs = pst.executeQuery();
////            Product obj;
////            while (rs.next()) {
////                obj = formatObj(rs);
////                list.add(obj);
////            }
////            return list;
////        } catch (SQLException e) {
////            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de produtos: " + e.getMessage());
////        }
////        return null;
////    }
//    public List<Purchase> filter(String txt) {
//        List<Purchase> list = new ArrayList<>();
//        try {
//            String sql = "SELECT * FROM " + table + " WHERE price_purchase LIKE ?  OR description LIKE ? OR date LIKE ?  OR total LIKE ?";
////            String sql = "SELECT * FROM products WHERE description LIKE ?";
//            pst = this.conn.prepareStatement(sql);
//            pst.setString(1, "%" + txt + "%");
//            pst.setString(2, "%" + txt + "%");
//            pst.setString(3, "%" + txt + "%");
//            pst.setString(4, "%" + txt + "%");
//            rs = pst.executeQuery();
//            Purchase obj;// = new Product();
//            while (rs.next()) {
//                obj = formatObj(rs);
//                list.add(obj);
//            }
//            return list;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de Compra: " + e.getMessage());
//        }
//        return null;
//    }
//
////    public List<Purchase> filterProduct(String txt) {
////        List<Purchase> list = new ArrayList<>();
////        try {
////            String sql = "SELECT * FROM purchase WHERE description LIKE ?  OR type LIKE ? OR barcode LIKE ?  OR price LIKE ? AND type LIKE ?";
//////            String sql = "SELECT * FROM products WHERE description LIKE ?";
////            pst = this.conn.prepareStatement(sql);
////            pst.setString(1, "%" + txt + "%");
////            pst.setString(2, "%" + txt + "%");
////            pst.setString(3, "%" + txt + "%");
////            pst.setString(4, "%" + txt + "%");
////            pst.setString(5, "product");
////
////            rs = pst.executeQuery();
////            Purchase obj;// = new Product();
////            while (rs.next()) {
////                obj = formatObj(rs);
////                list.add(obj);
////            }
////            return list;
////        } catch (SQLException e) {
////            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de produtos: " + e.getMessage());
////        }
////        return null;
////    }
//    public Purchase formatObj(ResultSet rs) {
//        try {
//            Purchase obj = new Purchase();
//            Supplier supp;
//            User user;
//            Product prod;
//
//            SupplierDao sDao = new SupplierDao();
//            supp = sDao.getFromId(rs.getInt("supplier_id"));
//            UserDao uDao = new UserDao();
//            user = uDao.getId(rs.getInt("user_id"));
//            ProductDao pDao = new ProductDao();
//            prod = pDao.getId(rs.getInt("product_id"));
//            obj.setId(rs.getInt("id"));
//            obj.setDate(rs.getString("date"));
//            obj.setDescription(rs.getString("description"));
//            obj.setTotal(rs.getDouble("total"));
//            obj.setPricePurchase(rs.getDouble("price_purchase"));
//            obj.setPriceSale(rs.getDouble("price_sale"));
//            obj.setUser(user);
//            obj.setProduct(prod);
//            obj.setSupplier(supp);
//            obj.setQty(rs.getInt("qty"));
//            obj.setStatus(rs.getString("status"));
//            obj.setStatusPayment(rs.getString("status_payment"));
//            return obj;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao formatar obj Compra: " + e.getMessage());
//        }
//        return null;
//    }
//
////    public List<Product> listProduct(ResultSet rs) {
////        while (rs.next()) {
////            Product obj = new Product();
////            obj.setId(rs.getInt("id"));
////            obj.setType(rs.getString("type"));
////            obj.setCode(rs.getString("code"));
////            obj.setDescription(rs.getString("description"));
////            obj.setLongDescription(rs.getString("long_description"));
////            obj.setPrice(rs.getDouble("price"));
////            obj.setPurchasePrice(rs.getDouble("purchase_price"));
////            obj.setTaxeId(rs.getInt("taxe_id"));
////            obj.setReasonTaxeId(rs.getInt("reason_taxe_id"));
////            obj.setGroupId(rs.getInt("group_id"));
////            obj.setSubGroupId(rs.getInt("sub_group_id"));
////            obj.setUnitId(rs.getInt("unit_id"));
////            obj.setStockTotal(rs.getInt("stock_total"));
////            obj.setStockMin(rs.getInt("stock_value_min"));
////            obj.setStockMax(rs.getInt("stock_value_max"));
////            obj.setStatus(rs.getInt("status"));
////            list.add(obj);
////        }
////        return list;
////    }
////    public int getQtdStock(int id) {
////        try {
////            int qtdStock = 0;
////            String sql = "SELECT stock_total FROM products WHERE id=?";
////            PreparedStatement ptmt = this.conn.prepareStatement(sql);
////            ptmt.setInt(1, id);
////            ResultSet rs = ptmt.executeQuery();
////            if (rs.next()) {
////                qtdStock = rs.getInt("stock_total");
////            }
////            return qtdStock;
////        } catch (SQLException e) {
////            JOptionPane.showMessageDialog(null, "Erro ao pegar stock_total de produtos: " + e.getMessage());
////        }
////        return 0;
////    }
}
