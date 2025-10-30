package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.ProductOrder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO responsável por manipular os itens (products_order) de cada pedido.
 *
 * Compatível com o novo BaseDao e conexões transacionais. Agora o orderId vem
 * de dentro do próprio objeto ProductOrder.
 *
 * @author …
 */
public class ProductOrderDao extends BaseDao<ProductOrder> {

    // ✅ Construtor padrão (usa conexão do pool automaticamente)
    public ProductOrderDao() {
        // não precisa chamar super(), ele já existe por padrão
    }

    // ✅ Construtor alternativo (usa conexão externa — transação)
    public ProductOrderDao(java.sql.Connection externalConn) {
        super(externalConn);
    }

    // ==========================================================
    // 🔹 MAPEAMENTO RESULTSET → OBJETO
    // ==========================================================
    private ProductOrder map(ResultSet rs) {
        try {
            ProductOrder obj = new ProductOrder();
            ProductDao productDao = new ProductDao();

            obj.setId(rs.getInt("id"));
            obj.setOrderId(rs.getInt("order_id"));
            obj.setDate(rs.getString("date"));
            obj.setDescription(rs.getString("description"));
            obj.setQty(rs.getInt("qty"));
            obj.setPrice(rs.getBigDecimal("price"));
            obj.setUnit(rs.getString("unit"));
            obj.setCode(rs.getString("prod_code"));
            obj.setTaxeCode(rs.getString("taxe_code"));
            obj.setTaxeName(rs.getString("taxe_name"));
            obj.setTaxePercentage(rs.getBigDecimal("taxe_percentage"));
            obj.setReasonTax(rs.getString("reason_tax"));
            obj.setReasonCode(rs.getString("reason_code"));
            obj.setProduct(productDao.findById(rs.getInt("product_id")));

            return obj;
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao mapear ProductOrder: " + e.getMessage());
            return null;
        }
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    @Override
    public boolean add(ProductOrder obj) {
        if (obj == null || obj.getOrderId() <= 0) {
            System.err.println("[DB] orderId inválido em ProductOrder: " + obj);
            return false;
        }

        String sql = """
            INSERT INTO products_order (
                order_id, product_id, description, qty, price, unit,
                prod_code, taxe_code, taxe_name, taxe_percentage,
                reason_tax, reason_code
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        return executeUpdate(sql,
                obj.getOrderId(),
                obj.getProduct().getId(),
                obj.getDescription(),
                obj.getQty(),
                obj.getPrice(),
                obj.getUnit(),
                obj.getCode(),
                obj.getTaxeCode(),
                obj.getTaxeName(),
                obj.getTaxePercentage(),
                obj.getReasonTax(),
                obj.getReasonCode()
        );
    }

    @Override
    public boolean update(ProductOrder obj) {
        String sql = """
            UPDATE products_order
               SET order_id=?, product_id=?, description=?, qty=?, price=?, unit=?,
                   prod_code=?, taxe_code=?, taxe_name=?, taxe_percentage=?,
                   reason_tax=?, reason_code=?
             WHERE id=?
        """;
        return executeUpdate(sql,
                obj.getOrderId(),
                obj.getProduct().getId(),
                obj.getDescription(),
                obj.getQty(),
                obj.getPrice(),
                obj.getUnit(),
                obj.getCode(),
                obj.getTaxeCode(),
                obj.getTaxeName(),
                obj.getTaxePercentage(),
                obj.getReasonTax(),
                obj.getReasonCode(),
                obj.getId()
        );
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM products_order WHERE id=?", id);
    }

    @Override
    public ProductOrder findById(int id) {
        return findOne("SELECT * FROM products_order WHERE id=?", this::map, id);
    }

    @Override
    public List<ProductOrder> findAll() {
        return executeQuery("SELECT * FROM products_order", this::map);
    }

    // ==========================================================
    // 🔹 CONSULTAS ESPECÍFICAS
    // ==========================================================
    /**
     * Lista todos os itens de um pedido
     */
    public List<ProductOrder> listProductFromOrderId(int orderId) {
        String sql = "SELECT * FROM products_order WHERE order_id=?";
        return executeQuery(sql, this::map, orderId);
    }

    /**
     * Lista com cláusula WHERE opcional
     */
    public List<ProductOrder> list(String where) {
        String sql = "SELECT * FROM products_order " + (where != null ? where : "");
        return executeQuery(sql, this::map);
    }

    /**
     * Filtro genérico por texto na descrição
     */
    public List<ProductOrder> filter(String txt) {
        String sql = "SELECT * FROM products_order WHERE description LIKE ?";
        return executeQuery(sql, this::map, "%" + txt + "%");
    }
}
