//package com.okutonda.okudpdv.data.dao;
//
//import com.okutonda.okudpdv.data.connection.DatabaseProvider;
//import com.okutonda.okudpdv.data.entities.Product;
//import com.okutonda.okudpdv.data.entities.PurchaseItem;
//import com.okutonda.okudpdv.data.entities.StockMovement;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * DAO responsável pelos itens de compra.
// *
// * Compatível com o novo padrão BaseDao + DatabaseProvider. Contém lógica de
// * validação de entrada de stock (compra parcial).
// *
// * @author Hulquene
// */
//public class PurchaseItemDao extends BaseDao<PurchaseItem> {
//
//    public PurchaseItemDao() {
//         // não precisa chamar super(), ele já existe por padrão
//    }
//
//    public PurchaseItemDao(Connection externalConn) {
//        super(externalConn);
//    }
//
//    // ==========================================================
//    // 🔹 MAP ResultSet → PurchaseItem
//    // ==========================================================
//    private PurchaseItem map(ResultSet rs) {
//        try {
//            PurchaseItem obj = new PurchaseItem();
//            obj.setId(rs.getInt("id"));
//            obj.setPurchaseId(rs.getInt("purchase_id"));
//            obj.setQuantidade(rs.getInt("quantidade"));
//            obj.setPrecoCusto(rs.getBigDecimal("preco_custo"));
//            obj.setIva(rs.getBigDecimal("iva"));
//            obj.setSubtotal(rs.getBigDecimal("subtotal"));
//            obj.setQuantidadeEntrada(rs.getInt("quantidade_entrada"));
//            obj.setEntradaStatus(rs.getString("entrada_status"));
//
//            Product prod = new Product();
//            prod.setId(rs.getInt("product_id"));
//            prod.setDescription(rs.getString("product_name"));
//            obj.setProduct(prod);
//
//            return obj;
//        } catch (SQLException e) {
//            System.err.println("[DB] Erro ao mapear PurchaseItem: " + e.getMessage());
//            return null;
//        }
//    }
//
//    // ==========================================================
//    // 🔹 CRUD básico
//    // ==========================================================
//    @Override
//    public boolean add(PurchaseItem item) {
//        String sql = """
//            INSERT INTO purchase_items
//            (purchase_id, product_id, quantidade, preco_custo, iva, subtotal, quantidade_entrada, entrada_status)
//            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
//        """;
//        return executeUpdate(sql,
//                item.getPurchaseId(),
//                item.getProduct().getId(),
//                item.getQuantidade(),
//                item.getPrecoCusto(),
//                item.getIva(),
//                item.getSubtotal(),
//                0, // quantidade_entrada inicial
//                "nao_iniciado" // estado inicial
//        );
//    }
//
//    /**
//     * Adiciona item dentro de uma compra já conhecida
//     */
//    public boolean add(PurchaseItem obj, int purchaseId) {
//        obj.setPurchaseId(purchaseId);
//        return add(obj);
//    }
//
//    @Override
//    public boolean update(PurchaseItem obj) {
//        String sql = """
//            UPDATE purchase_items
//               SET quantidade=?, preco_custo=?, iva=?, subtotal=?,
//                   quantidade_entrada=?, entrada_status=?
//             WHERE id=?
//        """;
//        return executeUpdate(sql,
//                obj.getQuantidade(),
//                obj.getPrecoCusto(),
//                obj.getIva(),
//                obj.getSubtotal(),
//                obj.getQuantidadeEntrada(),
//                obj.getEntradaStatus(),
//                obj.getId());
//    }
//
//    @Override
//    public boolean delete(int id) {
//        return executeUpdate("DELETE FROM purchase_items WHERE id=?", id);
//    }
//
//    @Override
//    public PurchaseItem findById(int id) {
//        String sql = """
//            SELECT i.*, p.description AS product_name
//            FROM purchase_items i
//            INNER JOIN products p ON p.id = i.product_id
//            WHERE i.id = ?
//        """;
//        return findOne(sql, this::map, id);
//    }
//
//    @Override
//    public List<PurchaseItem> findAll() {
//        String sql = """
//            SELECT i.*, p.description AS product_name
//            FROM purchase_items i
//            INNER JOIN products p ON p.id = i.product_id
//            ORDER BY i.id ASC
//        """;
//        return executeQuery(sql, this::map);
//    }
//
//    // ==========================================================
//    // 🔹 Lógica de negócio
//    // ==========================================================
//    /**
//     * Verifica se é possível dar entrada no stock a partir de uma compra.
//     * Impede que o movimento ultrapasse a quantidade comprada.
//     */
//    public boolean podeDarEntradaDeCompra(StockMovement movimento) {
//        String sql = """
//            SELECT quantidade, quantidade_entrada
//            FROM purchase_items
//            WHERE purchase_id = ? AND product_id = ?
//        """;
//
//        try (Connection conn = DatabaseProvider.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
//
//            pst.setInt(1, movimento.getReferenceId());
//            pst.setInt(2, movimento.getProduct().getId());
//
//            try (ResultSet rs = pst.executeQuery()) {
//                if (rs.next()) {
//                    int total = rs.getInt("quantidade");
//                    int entrada = rs.getInt("quantidade_entrada");
//                    int faltante = total - entrada;
//
//                    if (faltante <= 0) {
//                        System.out.printf("⚠ Produto já teve entrada total (%d/%d)%n", entrada, total);
//                        return false;
//                    }
//                    if (movimento.getQuantity() > faltante) {
//                        System.out.printf("⚠ Quantidade (%d) excede faltante (%d)%n",
//                                movimento.getQuantity(), faltante);
//                        return false;
//                    }
//                    return true; // ✅ pode dar entrada
//                } else {
//                    System.out.println("⚠ Item de compra não encontrado para produto="
//                            + movimento.getProduct().getId() + " | compra=" + movimento.getReferenceId());
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("[DB] Erro ao verificar entrada de compra: " + e.getMessage());
//        }
//        return false;
//    }
//
//    // ==========================================================
//    // 🔹 Consultas auxiliares
//    // ==========================================================
//    public List<PurchaseItem> listByPurchase(int purchaseId) {
//        String sql = """
//            SELECT i.*, p.description AS product_name
//            FROM purchase_items i
//            INNER JOIN products p ON i.product_id = p.id
//            WHERE i.purchase_id = ?
//            ORDER BY i.id ASC
//        """;
//        return executeQuery(sql, this::map, purchaseId);
//    }
//}
//
/////*
//// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
//// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
//// */
////package com.okutonda.okudpdv.data.dao;
////
////import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
////import com.okutonda.okudpdv.data.entities.PurchaseItem;
////import com.okutonda.okudpdv.data.entities.Product;
////import com.okutonda.okudpdv.data.entities.StockMovement;
////
////import java.sql.*;
////import java.util.ArrayList;
////import java.util.List;
////
/////**
//// *
//// * @author rog
//// */
////public class PurchaseItemDao {
////
////    private final Connection conn;
////
////    public PurchaseItemDao() {
////        this.conn = ConnectionDatabase.getConnect();
////    }
////
////    public boolean add(PurchaseItem obj, int purchaseId) {
////        String sql = """
////        INSERT INTO purchase_items
////        (purchase_id, product_id, quantidade, preco_custo, iva, subtotal, quantidade_entrada, entrada_status)
////        VALUES (?,?,?,?,?,?,?,?)
////    """;
////
////        try (PreparedStatement pst = conn.prepareStatement(sql)) {
////            pst.setInt(1, purchaseId);
////            pst.setInt(2, obj.getProduct().getId());
////            pst.setInt(3, obj.getQuantidade());
////            pst.setBigDecimal(4, obj.getPrecoCusto());
////            pst.setBigDecimal(5, obj.getIva());
////            pst.setBigDecimal(6, obj.getSubtotal());
////            pst.setInt(7, 0); // ✅ nenhuma entrada no momento da criação
////            pst.setString(8, "nao_iniciado"); // ✅ estado inicial
////            return pst.executeUpdate() == 1;
////
////        } catch (SQLException e) {
////            System.out.println("Erro ao adicionar item de compra: " + e.getMessage());
////            return false;
////        }
////    }
////
////    public boolean podeDarEntradaDeCompra(StockMovement movimento) {
////        String sql = """
////        SELECT quantidade, quantidade_entrada
////        FROM purchase_items
////        WHERE purchase_id = ? AND product_id = ?
////    """;
////
////        try (PreparedStatement pst = conn.prepareStatement(sql)) {
////            pst.setInt(1, movimento.getReferenceId());       // ID da compra
////            pst.setInt(2, movimento.getProduct().getId());   // ID do produto
////
////            try (ResultSet rs = pst.executeQuery()) {
////                if (rs.next()) {
////                    int quantidadeTotal = rs.getInt("quantidade");
////                    int quantidadeEntrada = rs.getInt("quantidade_entrada");
////
////                    int faltante = quantidadeTotal - quantidadeEntrada;
////
////                    if (faltante <= 0) {
////                        // 🛑 Já teve entrada total — não pode dar entrada de novo
////                        System.out.println("⚠ Produto já teve entrada total ("
////                                + quantidadeEntrada + "/" + quantidadeTotal + ")");
////                        return false;
////                    }
////
////                    if (movimento.getQuantity() > faltante) {
////                        // 🛑 Está tentando dar entrada além do que falta
////                        System.out.println("⚠ Quantidade informada (" + movimento.getQuantity()
////                                + ") excede o que falta (" + faltante + ")");
////                        return false;
////                    }
////
////                    // ✅ Pode dar entrada normalmente
////                    return true;
////                } else {
////                    // 🛑 Item não encontrado (produto não pertence à compra)
////                    System.out.println("⚠ Item de compra não encontrado: produto="
////                            + movimento.getProduct().getId()
////                            + ", compra=" + movimento.getReferenceId());
////                }
////            }
////
////        } catch (SQLException e) {
////            System.err.println("Erro ao verificar entrada de compra: " + e.getMessage());
////        }
////
////        return false; // segurança: se algo falhar, bloqueia entrada
////    }
////
////    public List<PurchaseItem> listByPurchase(int purchaseId) {
////        List<PurchaseItem> list = new ArrayList<>();
////
////        String sql = """
////        SELECT i.*, p.description AS product_name
////        FROM purchase_items i
////        INNER JOIN products p ON i.product_id = p.id
////        WHERE i.purchase_id = ?
////        ORDER BY i.id ASC
////    """;
////
////        try (PreparedStatement pst = conn.prepareStatement(sql)) {
////            pst.setInt(1, purchaseId);
////
////            try (ResultSet rs = pst.executeQuery()) {
////                while (rs.next()) {
////                    PurchaseItem obj = new PurchaseItem();
////
////                    obj.setId(rs.getInt("id"));
////                    obj.setPurchaseId(rs.getInt("purchase_id"));
////                    obj.setQuantidade(rs.getInt("quantidade"));
////                    obj.setPrecoCusto(rs.getBigDecimal("preco_custo"));
////                    obj.setIva(rs.getBigDecimal("iva"));
////                    obj.setSubtotal(rs.getBigDecimal("subtotal"));
////
////                    // ✅ novos campos de controle de entrada
////                    obj.setQuantidadeEntrada(rs.getInt("quantidade_entrada"));
////                    obj.setEntradaStatus(rs.getString("entrada_status"));
////
////                    // Produto associado
////                    Product prod = new Product();
////                    prod.setId(rs.getInt("product_id"));
////                    prod.setDescription(rs.getString("product_name"));
////                    obj.setProduct(prod);
////
////                    list.add(obj);
////                }
////            }
////        } catch (SQLException e) {
////            System.out.println("Erro ao listar itens da compra: " + e.getMessage());
////        }
////
////        return list;
////    }
////
////}
