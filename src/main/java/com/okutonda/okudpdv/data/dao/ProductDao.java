package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.entities.Product;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO responsável pela gestão de produtos.
 *
 * Segue o padrão BaseDao<Product> com suporte ao novo DatabaseProvider. Inclui
 * métodos especializados de inventário e PDV.
 *
 * @author …
 */
public class ProductDao extends BaseDao<Product> {

    // ==========================================================
    // 🔹 MAPEAMENTO SQL → OBJETO
    // ==========================================================
    private Product map(ResultSet rs) {
        try {
            Product obj = new Product();

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
            obj.setGroup(gDao.findById(rs.getInt("group_id")));
            obj.setTaxe(tDao.searchFromId(rs.getInt("tax_id")));
            obj.setReasonTaxe(rDao.searchFromId(rs.getInt("reason_tax_id")));
            obj.setStatus(rs.getInt("status"));
            obj.setMinStock(rs.getInt("min_stock"));
            return obj;
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao mapear Product: " + e.getMessage());
            return null;
        }
    }

    // ==========================================================
    // 🔹 CRUD (GENÉRICO)
    // ==========================================================
    @Override
    public boolean add(Product obj) {
        String sql = """
            INSERT INTO products (
                type, code, description, price, tax_id, reason_tax_id, group_id,
                status, barcode, purchase_price, min_stock
            )
            VALUES (?,?,?,?,?,?,?,?,?,?,?)
        """;
        return executeUpdate(sql,
                obj.getType(),
                obj.getCode(),
                obj.getDescription(),
                obj.getPrice(),
                obj.getTaxe().getId(),
                obj.getReasonTaxe().getId(),
                obj.getGroup().getId(),
                obj.getStatus(),
                obj.getBarcode(),
                obj.getPurchasePrice(),
                obj.getMinStock()
        );
    }

    @Override
    public boolean update(Product obj) {
        String sql = """
            UPDATE products
               SET type=?, code=?, description=?, price=?, tax_id=?, reason_tax_id=?,
                   group_id=?, status=?, barcode=?, purchase_price=?, min_stock=?
             WHERE id=?
        """;
        return executeUpdate(sql,
                obj.getType(),
                obj.getCode(),
                obj.getDescription(),
                obj.getPrice(),
                obj.getTaxe().getId(),
                obj.getReasonTaxe().getId(),
                obj.getGroup().getId(),
                obj.getStatus(),
                obj.getBarcode(),
                obj.getPurchasePrice(),
                obj.getMinStock(),
                obj.getId()
        );
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM products WHERE id=?", id);
    }

    @Override
    public Product findById(int id) {
        return findOne("SELECT * FROM products WHERE id=?", this::map, id);
    }

    @Override
    public List<Product> findAll() {
        return executeQuery("SELECT * FROM products", this::map);
    }

    // ==========================================================
    // 🔹 MÉTODOS ESPECÍFICOS
    // ==========================================================
    /**
     * Busca produto pelo código de barras
     */
    public Product findByBarcode(String barcode) {
        return findOne("SELECT * FROM products WHERE barcode=?", this::map, barcode);
    }

    /**
     * Busca produto pela descrição
     */
    public Product findByDescription(String desc) {
        return findOne("SELECT * FROM products WHERE description=?", this::map, desc);
    }

    /**
     * Lista produtos com stock calculado
     */
    public List<Product> listWithStock(String where) {
        String sql = """
            SELECT p.*, COALESCE(SUM(sm.quantity),0) AS current_stock
              FROM products p
         LEFT JOIN stock_movements sm ON sm.product_id = p.id
        """ + (where != null ? where : "") + """
          GROUP BY p.id
        """;
        return executeQuery(sql, rs -> {
            Product p = map(rs);
            try {
                p.setCurrentStock(rs.getInt("current_stock"));
            } catch (SQLException e) {
                System.err.println("[DB] Erro ao definir current_stock: " + e.getMessage());
            }
            return p;
        });
    }

    /**
     * Retorna o stock atual de um produto
     */
    public int getCurrentStock(int productId) {
        String sql = "SELECT IFNULL(SUM(quantity),0) AS stock_atual FROM stock_movements WHERE product_id=?";
        return executeScalarInt(sql, productId);
    }

    /**
     * Lista simplificada de produtos para inventário
     */
    public List<Product> listForInventory() {
        String sql = """
            SELECT 
                p.id, p.code, p.barcode, p.description,
                IFNULL(SUM(sm.quantity),0) AS current_stock,
                p.min_stock, p.price, p.purchase_price, p.type
              FROM products p
         LEFT JOIN stock_movements sm ON sm.product_id = p.id
             WHERE p.status=1
          GROUP BY p.id, p.code, p.barcode, p.description,
                   p.min_stock, p.price, p.purchase_price, p.type
        """;
        return executeQuery(sql, rs -> {
            Product p = new Product();
            try {
                p.setId(rs.getInt("id"));
                p.setCode(rs.getString("code"));
                p.setBarcode(rs.getString("barcode"));
                p.setDescription(rs.getString("description"));
                p.setCurrentStock(rs.getInt("current_stock"));
                p.setMinStock(rs.getInt("min_stock"));
                p.setPrice(rs.getBigDecimal("price"));
                p.setPurchasePrice(rs.getBigDecimal("purchase_price"));
                p.setType(rs.getString("type"));
            } catch (SQLException e) {
                System.err.println("[DB] Erro ao mapear inventário: " + e.getMessage());
            }
            return p;
        });
    }

    /**
     * Lista produtos disponíveis para o PDV
     */
    public List<Product> listForPDV(String filtro) {
        StringBuilder sql = new StringBuilder("""
            SELECT p.*, COALESCE(SUM(sm.quantity),0) AS current_stock
              FROM products p
         LEFT JOIN stock_movements sm ON sm.product_id = p.id
             WHERE p.status=1 AND p.type='product'
        """);
        if (filtro != null && !filtro.trim().isEmpty()) {
            sql.append(" AND (p.description LIKE ? OR p.barcode LIKE ? OR p.code LIKE ?) ");
        }
        sql.append(" GROUP BY p.id HAVING current_stock > 0");

        if (filtro == null || filtro.trim().isEmpty()) {
            return executeQuery(sql.toString(), this::map);
        }

        String like = "%" + filtro + "%";
        return executeQuery(sql.toString(), this::map, like, like, like);
    }
}
