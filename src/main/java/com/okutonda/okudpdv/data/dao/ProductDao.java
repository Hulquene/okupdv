/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.Supplier;
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

    public ProductDao(Connection externalConn) { // para transação
        this.conn = externalConn;
    }

    /**
     * Inserir produto
     */
    public boolean add(Product obj) {
         System.out.println("Tax ID: " + obj.getId());
        System.out.println("Tax ID: " + obj.getTaxe().getId());
        System.out.println("ReasonTax ID: " + obj.getReasonTaxe().getId());
        System.out.println("Group ID: " + obj.getGroup().getId());
        System.out.println(obj.print());
        
        String sql = """
        INSERT INTO products (
            type, code, description, price, tax_id, reason_tax_id, group_id,
            status, barcode, purchase_price, min_stock
        )
        VALUES (?,?,?,?,?,?,?,?,?,?,?)
    """;

        try {
            pst = this.conn.prepareStatement(sql);
            // Preenche os parâmetros na mesma ordem das colunas
            pst.setString(1, obj.getType());                  // type
            pst.setString(2, obj.getCode());                  // code
            pst.setString(3, obj.getDescription());           // description
            pst.setBigDecimal(4, obj.getPrice());             // price
            pst.setInt(5, obj.getTaxe().getId());             // tax_id
            pst.setInt(6, obj.getReasonTaxe().getId());       // reason_tax_id
            pst.setInt(7, obj.getGroup().getId());            // group_id
            pst.setInt(8, obj.getStatus());                   // status
            pst.setString(9, obj.getBarcode());               // barcode
            pst.setBigDecimal(10, obj.getPurchasePrice());    // purchase_price
            pst.setInt(11, obj.getMinStock());                // min_stock

            pst.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao salvar Produto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Atualizar produto
     */
    public boolean edit(Product obj, int id) {
        try {
            String sql = """
                UPDATE products
                   SET type=?, code=?, description=?, price=?, tax_id=?, reason_tax_id=?,
                       group_id=?, status=?, barcode=?, purchase_price=?, min_stock=?
                 WHERE id=?
            """;

            pst = this.conn.prepareStatement(sql);
            pst.setString(1, obj.getType());
            pst.setString(2, obj.getCode());
            pst.setString(3, obj.getDescription());
            pst.setBigDecimal(4, obj.getPrice());
            pst.setInt(5, obj.getTaxe().getId());
            pst.setInt(6, obj.getReasonTaxe().getId());
            pst.setInt(7, obj.getGroup().getId());
            pst.setInt(8, obj.getStatus());
            pst.setString(9, obj.getBarcode());
            pst.setBigDecimal(10, obj.getPurchasePrice());
            pst.setInt(11, obj.getMinStock());
            pst.setInt(12, id);

            pst.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar produto: " + e.getMessage());
        }
        return false;
    }

    /**
     * Apagar produto
     */
    public Boolean delete(int id) {
        try {
            String sql = "DELETE FROM products WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir produto: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lista produtos com stock calculado
     */
    public List<Product> listWithStock(String where) {
        List<Product> list = new ArrayList<>();
        String sql = """
            SELECT p.*,
                   COALESCE(SUM(sm.quantity),0) AS current_stock
              FROM products p
         LEFT JOIN stock_movements sm ON sm.product_id = p.id
        """ + (where != null ? where : "") + """
          GROUP BY p.id
        """;

        try (PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Product obj = formatObj(rs);
                obj.setCurrentStock(rs.getInt("current_stock"));
                list.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos com stock: " + e.getMessage());
        }
        return list;
    }

    /**
     * Stock atual de um produto
     */
    public int getCurrentStock(int productId) {
        String sql = "SELECT IFNULL(SUM(quantity),0) AS stock_atual FROM stock_movements WHERE product_id=?";
        try (PreparedStatement pst = this.conn.prepareStatement(sql)) {
            pst.setInt(1, productId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("stock_atual");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao calcular stock atual: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Inventário (para tela de stock)
     */
    public List<Product> listForInventory() {
        List<Product> list = new ArrayList<>();
        String sql = """
        SELECT 
            p.id,
            p.code,
            p.barcode,
            p.description,
            IFNULL(SUM(sm.quantity), 0) AS current_stock,
            p.min_stock,
            p.price,
            p.purchase_price,
            p.type
        FROM products p
        LEFT JOIN stock_movements sm ON sm.product_id = p.id
        WHERE p.status = 1
        GROUP BY p.id, p.code, p.barcode, p.description,
                 p.min_stock, p.price, p.purchase_price, p.type
    """;

        try (PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Product prod = new Product();
                prod.setId(rs.getInt("id"));
                prod.setCode(rs.getString("code"));
                prod.setBarcode(rs.getString("barcode"));
                prod.setDescription(rs.getString("description"));
                prod.setCurrentStock(rs.getInt("current_stock"));
                prod.setMinStock(rs.getInt("min_stock"));
                prod.setPrice(rs.getBigDecimal("price"));
                prod.setPurchasePrice(rs.getBigDecimal("purchase_price"));
                prod.setType(rs.getString("type")); // agora funciona, porque está no SELECT
                list.add(prod);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar inventário: " + e.getMessage());
        }
        return list;
    }

    /**
     * Produtos para PDV
     */
    public List<Product> listForPDV(String filtro) {
        List<Product> list = new ArrayList<>();

        String baseSql = """
            SELECT p.*, COALESCE(SUM(sm.quantity),0) AS current_stock
              FROM products p
         LEFT JOIN stock_movements sm ON sm.product_id = p.id
             WHERE p.status=1
               AND p.type='product'
        """;

        if (filtro != null && !filtro.trim().isEmpty()) {
            baseSql += " AND (p.description LIKE ? OR p.barcode LIKE ? OR p.code LIKE ?) ";
        }

        baseSql += " GROUP BY p.id HAVING current_stock > 0";

        try (PreparedStatement pst = conn.prepareStatement(baseSql)) {
            if (filtro != null && !filtro.trim().isEmpty()) {
                String like = "%" + filtro + "%";
                pst.setString(1, like);
                pst.setString(2, like);
                pst.setString(3, like);
            }

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Product obj = formatObj(rs);
                obj.setCurrentStock(rs.getInt("current_stock"));
                list.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos para PDV: " + e.getMessage());
        }
        return list;
    }

    /**
     * Busca por código de barras
     */
    public Product searchFromBarCode(String barCode) {
        try {
            String sql = "SELECT * FROM products WHERE barcode =?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, barCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                return formatObj(rs);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do produto: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca por ID
     */
    public Product getId(int id) {
        try {
            String sql = "SELECT * FROM products WHERE id =?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                return formatObj(rs);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar produto por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca por nome
     */
    public Product searchFromName(String description) {
        try {
            String sql = "SELECT * FROM products WHERE description =?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, description);
            rs = pst.executeQuery();
            if (rs.next()) {
                return formatObj(rs);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar produto por nome: " + e.getMessage());
        }
        return null;
    }

    /**
     * Método utilitário para criar objeto Product
     */
    public Product formatObj(ResultSet rs) {
        try {
            Product obj = new Product();

            SupplierDao sDao = new SupplierDao();
            GroupsProductDao gDao = new GroupsProductDao();
            TaxeDao tDao = new TaxeDao();
            ReasonTaxeDao rDao = new ReasonTaxeDao();

            obj.setId(rs.getInt("id"));
            obj.setType(rs.getString("type"));
            obj.setCode(rs.getString("code"));
            obj.setBarcode(rs.getString("barcode"));
            obj.setDescription(rs.getString("description"));
            obj.setPrice(rs.getBigDecimal("price"));
            obj.setPurchasePrice(rs.getBigDecimal("purchase_price"));
            obj.setGroup(gDao.searchFromId(rs.getInt("group_id")));
            obj.setTaxe(tDao.searchFromId(rs.getInt("tax_id")));
            obj.setReasonTaxe(rDao.searchFromId(rs.getInt("reason_tax_id")));
            obj.setStatus(rs.getInt("status"));
            obj.setMinStock(rs.getInt("min_stock"));

            // calcula o stock atual
            obj.setCurrentStock(getCurrentStock(obj.getId()));
            return obj;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao formatar produto: " + e.getMessage());
        }
        return null;
    }
}
