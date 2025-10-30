package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.helpers.UtilDate;
import com.okutonda.okudpdv.helpers.UtilSales;
import com.okutonda.okudpdv.helpers.UtilSaft;
import com.okutonda.okudpdv.data.connection.DatabaseProvider;
import com.okutonda.okudpdv.data.dao.*;
import com.okutonda.okudpdv.data.entities.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador principal de Pedidos (Orders)
 *
 * Responsável por criar pedidos completos, registrar pagamentos e movimentar
 * stock em uma transação única.
 *
 * @author Hulquene
 */
public class OrderController {

    private final OrderDao dao;
    private final ProductOrderDao productOrderDao;
    private final PaymentDao paymentDao;
    private final StockMovementDao stockDao;

    public OrderController() {
        this.dao = new OrderDao();
        this.productOrderDao = new ProductOrderDao();
        this.paymentDao = new PaymentDao();
        this.stockDao = new StockMovementDao();
    }

    // ==========================================================
    // 🔹 CONSULTAS BÁSICAS
    // ==========================================================
    public List<Order> getAll() {
        return dao.findAll();
    }

    public Order getById(int id) {
        return dao.findById(id);
    }

    public List<Order> filterDate(LocalDate from, LocalDate to) {
        return dao.filterDate(from, to, "");
    }

    // ==========================================================
    // 🔹 CRIAÇÃO DE PEDIDO COMPLETO
    // ==========================================================
    public Order criarComPagamentos(Order order, List<Payment> pagamentos) throws Exception {

        if (order == null || order.getProducts() == null || order.getProducts().isEmpty()) {
            throw new IllegalArgumentException("Pedido sem produtos.");
        }
        if (pagamentos == null || pagamentos.isEmpty()) {
            throw new IllegalArgumentException("Sem pagamentos associados.");
        }

        // Calcular totais com IVA
        Totais t = calcularTotaisComIva(order.getProducts());
        order.setSubTotal(t.subTotal.doubleValue());
        order.setTotalTaxe(t.tax.doubleValue());
        order.setTotal(t.total.doubleValue());

        // Numerar e gerar hash
        String prefix = UtilSales.getPrefix("order");
        int number = dao.getNextNumber();
        String date = UtilDate.getFormatDataNow();
        int year = UtilDate.getYear();
        String numberOrder = UtilSales.FormatedNumberPrefix2(number, year, prefix);
        String hash = UtilSaft.appGenerateHashInvoice(date, date, numberOrder, String.valueOf(order.getTotal()), "");

        order.setPrefix(prefix);
        order.setNumber(number);
        order.setDatecreate(date);
        order.setHash(hash);
        order.setYear(year);
        order.setStatus(2); // 2 = emitido/pago

        // Transação com DatabaseProvider
        try (Connection conn = DatabaseProvider.getConnection()) {
            conn.setAutoCommit(false);

            OrderDao orderDaoTx = new OrderDao(conn);
            ProductOrderDao itemDaoTx = new ProductOrderDao(conn);
            PaymentDao paymentDaoTx = new PaymentDao(conn);
            StockMovementDao stockDaoTx = new StockMovementDao(conn);

            if (!orderDaoTx.add(order)) {
                throw new RuntimeException("Falha ao gravar cabeçalho do pedido.");
            }

            Order salvo = orderDaoTx.findByNumber(order.getNumber());
            if (salvo == null || salvo.getId() <= 0) {
                throw new RuntimeException("Falha ao recuperar pedido salvo.");
            }

            // Itens e stock
            for (ProductOrder po : order.getProducts()) {
                po.setOrderId(salvo.getId());
                itemDaoTx.add(po);

                StockMovement mov = new StockMovement();
                mov.setProduct(po.getProduct());
                mov.setQuantity(-po.getQty());
                mov.setOrigin("VENDA");
                mov.setType("SAIDA");
                mov.setReason("VENDA " + salvo.getPrefix() + "/" + salvo.getNumber());
                mov.setUser(salvo.getSeller());
                stockDaoTx.add(mov);
            }

            // Pagamentos
            for (Payment p : pagamentos) {
                p.setInvoiceId(salvo.getId());
                p.setInvoiceType(salvo.getPrefix());
                p.setPrefix(salvo.getPrefix());
                p.setNumber(salvo.getNumber());
                p.setClient(salvo.getClient());
                p.setUser(salvo.getSeller());
                if (p.getDate() == null) {
                    p.setDate(date);
                }
                if (p.getCurrency() == null) {
                    p.setCurrency("AOA");
                }
                if (p.getStatus() == null) {
                    p.setStatus(PaymentStatus.SUCCESS);
                }

                paymentDaoTx.add(p);
            }

            conn.commit();
            return orderDaoTx.findById(salvo.getId());

        } catch (SQLException e) {
            throw new Exception("Erro ao processar venda: " + e.getMessage(), e);
        }
    }

    // ==========================================================
    // 🔹 Cálculo de totais
    // ==========================================================
    private static class Totais {

        final BigDecimal subTotal;
        final BigDecimal tax;
        final BigDecimal total;

        Totais(BigDecimal s, BigDecimal i, BigDecimal t) {
            this.subTotal = s;
            this.tax = i;
            this.total = t;
        }
    }

    private Totais calcularTotaisComIva(List<ProductOrder> itens) {
        BigDecimal sub = BigDecimal.ZERO, iva = BigDecimal.ZERO, tot = BigDecimal.ZERO;
        for (ProductOrder po : itens) {
            if (po == null || po.getProduct() == null) {
                continue;
            }
            BigDecimal qty = BigDecimal.valueOf(po.getQty());
            BigDecimal price = po.getPrice() != null ? po.getPrice() : BigDecimal.ZERO;
            BigDecimal gross = price.multiply(qty);

            BigDecimal perc = po.getTaxePercentage() == null ? BigDecimal.ZERO : po.getTaxePercentage();
            BigDecimal lineTax = BigDecimal.ZERO, lineNet = gross;

            if (perc.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal base = new BigDecimal("100").add(perc);
                lineTax = gross.multiply(perc).divide(base, 2, RoundingMode.HALF_UP);
                lineNet = gross.subtract(lineTax);
            }

            sub = sub.add(lineNet);
            iva = iva.add(lineTax);
            tot = tot.add(gross);
        }
        return new Totais(sub, iva, tot);
    }
}

///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.okutonda.okudpdv.controllers;
//
//import com.okutonda.okudpdv.data.dao.OrderDao;
//import com.okutonda.okudpdv.data.dao.PaymentDao;
//import com.okutonda.okudpdv.data.dao.ProductDao;
//import com.okutonda.okudpdv.data.dao.ProductOrderDao;
//import com.okutonda.okudpdv.data.dao.StockMovementDao;
//import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
//import com.okutonda.okudpdv.data.entities.Order;
//import com.okutonda.okudpdv.data.entities.Payment;
//import com.okutonda.okudpdv.data.entities.PaymentStatus;
//import com.okutonda.okudpdv.data.entities.ProductOrder;
//import com.okutonda.okudpdv.data.entities.StockMovement;
//import com.okutonda.okudpdv.utilities.UserSession;
//import com.okutonda.okudpdv.utilities.UtilDate;
//import com.okutonda.okudpdv.utilities.UtilSaft;
//import com.okutonda.okudpdv.utilities.UtilSales;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.List;
//
///**
// *
// * @author kenny
// */
//public class OrderController {
//
//    OrderDao dao;
//    ProductOrderDao prodOrderDao;
////    ProductDao prodDao;
//    UserSession session = UserSession.getInstance();
////    private final String where = "WHERE seller_id =" + session.getUser().getId();
//    double subTotal, total;
//
////    public OrderController(OrderDao dao, ProductOrderDao prodOrderDao) {
////        this.dao = dao;
////        this.prodOrderDao = prodOrderDao;
////        this.prodDao =  ProductDao();
//////        session = UserSession.getInstance();
////    }
//    public OrderController(OrderDao dao, ProductOrderDao prodOrderDao, ProductDao prodDao, double subTotal, double total) {
//        this.dao = dao;
//        this.prodOrderDao = prodOrderDao;
////        this.prodDao = prodDao;
//        this.subTotal = subTotal;
//        this.total = total;
//    }
//
//    public OrderController() {
//        this.dao = new OrderDao();
//        this.prodOrderDao = new ProductOrderDao();
////        this.prodDao = new ProductDao();
//    }
//
//    public List<Order> get() {
//        if (session.getUser().getProfile().equals("admin") || session.getUser().getProfile().equals("manager")) {
//            return dao.list("");
//        }
//        return dao.list("WHERE seller_id =" + session.getUser().getId());
//    }
//
//    public List<Order> filter(String txt) {
//        if (session.getUser().getProfile().equals("admin") || session.getUser().getProfile().equals("manager")) {
//            return dao.filter(txt, "");
//        }
//        return dao.filter(txt, "WHERE seller_id =" + session.getUser().getId());
//    }
//
//    public List<Order> getOrderSeller(int idSeller) {
//        if (session.getUser().getProfile().equals("admin") || session.getUser().getProfile().equals("manager")) {
//            return dao.filter("", "WHERE seller_id =" + idSeller);
//        }
//        return dao.filter("", "WHERE seller_id =" + session.getUser().getId());
//    }
//
//    public List<Order> getOrderClient(int idClient) {
//        if (session.getUser().getProfile().equals("admin") || session.getUser().getProfile().equals("manager")) {
//            return dao.filter("", "WHERE client_id =" + idClient);
//        }
//        return dao.filter("", "WHERE seller_id =" + session.getUser().getId());
//    }
//
//    public List<Order> filterDate(LocalDate dateFrom, LocalDate dateTo) {
//        if (session.getUser().getProfile().equals("admin") || session.getUser().getProfile().equals("manager")) {
//            return dao.filterDate(dateFrom, dateTo, "");
//        }
//        return dao.filterDate(dateFrom, dateTo, "and seller_id =" + session.getUser().getId());
//    }
//
//    public Order getId(int id) {
//        return dao.getId(id);
//    }
//
//    public Order add(Order order) {
//
//        String prefix = UtilSales.getPrefix("order");
//        int number = this.getNextNumber();
//        String date = UtilDate.getFormatDataNow();
////        String numberOrder = prefix + number; 
//        String numberOrder = UtilSales.FormatedNumberPrefix2(number, UtilDate.getYear(), prefix);
//
//        String hash = UtilSaft.appGenerateHashInvoice(date, date, numberOrder, String.valueOf(total), "");
//
//        order.setPrefix(prefix);
//        order.setNumber(number);
//        order.setDatecreate(date);
//        order.setHash(hash);
//        boolean status = dao.add(order);
////        int totalStock;
////        List<ProductOrder> prodOrderList = order.getProducts();
//        System.out.println(order.toString());
//        if (status == true) {
//            Order responde = dao.getFromNumber(order.getNumber());
////            responde = dao.getId(responde.getId());
//
//            for (ProductOrder prod : order.getProducts()) {
//                prodOrderDao.add(prod, responde.getId());
////                totalStock = 0;
////                System.out.println("o id:" + prod.getProduct().getId());
////                System.out.println("stock: "+prod.getProduct().getStockTotal());
//////                Product product = prodDao.getId(prod.getProduct().getId());
//////                System.out.println("teste2" + product.getStockTotal());
//
////                totalStock = prod.getProduct().getStockTotal() - prod.getQty();
//////                System.out.println(prod.getProduct().getId() + "totalStock: " + totalStock);
////                prodDao.updateStock(prod.getProduct().getId(), totalStock);
//            }
////            for (ProductOrder prod : order.getProducts()) {
//////                prodOrderDao.add(prod, responde.getId());
////                totalStock = 0;
////                System.out.println("o id:" + prod.getProduct().getId());
////                System.out.println("stock: "+prod.getProduct().getStockTotal());
//////                Product product = prodDao.getId(prod.getProduct().getId());
//////                System.out.println("teste2" + product.getStockTotal());
////                totalStock = prod.getProduct().getStockTotal() - prod.getQty();
////                System.out.println(prod.getProduct().getId() + "totalStock: " + totalStock);
////                this.prodDao.updateStock(prod.getProduct().getId(), totalStock);
////
////            }
//            return responde;
//        }
//        return null;
//    }
//
//    public int getNextNumber() {
//        return dao.getNextNumberOrder();
//    }
//
//    public Double CalculateSubTotalOrder(List<ProductOrder> listProductOrder) {
//
////        Double[] totalOrder = [];
////        
////        totalOrder[0] = 0.0;
////        total = subTotal = 0;
////        for (ProductOrder productOrder : listProductOrder) {
////            subTotal = productOrder.getProduct().getPrice() * productOrder.getQty();
////            total += productOrder.getProduct().getPrice() * productOrder.getQty();
////        }
//        return null;
//    }
//
////    public Double CalculateTotalOrder(List<ProductOrder> listProductOrder) {
////        total = subTotal = 0;
////        for (ProductOrder productOrder : listProductOrder) {
////            subTotal = productOrder.getProduct().getPrice() * productOrder.getQty();
////            total += productOrder.getProduct().getPrice() * productOrder.getQty();
////        }
////
////        return null;
////    }
//    public BigDecimal calculateTotalOrder(List<ProductOrder> listProductOrder) {
//        BigDecimal subTotal = BigDecimal.ZERO;
//        BigDecimal total = BigDecimal.ZERO;
//
//        for (ProductOrder productOrder : listProductOrder) {
//            if (productOrder.getProduct() != null && productOrder.getProduct().getPrice() != null) {
//                BigDecimal price = productOrder.getProduct().getPrice();
//                BigDecimal qty = BigDecimal.valueOf(productOrder.getQty());
//
//                BigDecimal lineTotal = price.multiply(qty);
//
//                subTotal = subTotal.add(lineTotal);
//                total = total.add(lineTotal); // se fores aplicar IVA ou descontos, mete aqui
//            }
//        }
//
//        // podes guardar subTotal/total como atributos do objeto se precisares
//        return total;
//    }
//
//    public Double CalculateTotalChangeOrder(Order order) {
//        return null;
//    }
//
//    public Double CalculateTotalValueTaxeOrder(Order order) {
//        return null;
//    }
//
//    public Order criarEFinalizarComPagamentos(Order order, List<Payment> pagamentos) throws Exception {
//        // 0) validações básicas
//        if (order == null || order.getProducts() == null || order.getProducts().isEmpty()) {
//            throw new IllegalArgumentException("Pedido sem itens.");
//        }
//        if (pagamentos == null || pagamentos.isEmpty()) {
//            throw new IllegalArgumentException("Sem pagamentos.");
//        }
//
//        // 1) Recalcular totais no servidor (preço COM IVA embutido)
//        Totais t = calcularTotaisComIvaEmbutido(order.getProducts());
//        order.setSubTotal(t.subTotal.doubleValue());
//        order.setTotalTaxe(t.tax.doubleValue());
//        order.setTotal(t.total.doubleValue());
//
//        // 2) Numeracao/Hash/Meta
//        String prefix = UtilSales.getPrefix("order");
//        int number = this.getNextNumber();
//        String date = UtilDate.getFormatDataNow();
//        int year = UtilDate.getYear();
//        String numberOrder = UtilSales.FormatedNumberPrefix2(number, year, prefix);
//
//        // Usa total (com IVA) no hash
//        String hash = UtilSaft.appGenerateHashInvoice(date, date, numberOrder, String.valueOf(order.getTotal()), "");
//
//        order.setPrefix(prefix);
//        order.setNumber(number);
//        order.setDatecreate(date);
//        order.setHash(hash);
//        order.setYear(year);
//        order.setStatus(2); // exemplo: 2 = emitido/pago
//
//        // (se tiveres chave única fiscal)
//        // order.setKey(saftService.gerarChave(numberOrder, date));  // se existir
//        // 3) Transação única (usar MESMA connection para todos os DAOs)
//        Connection conn = null;
//        boolean sucesso = false;
//        try {
//            conn = ConnectionDatabase.getConnect(); 
//            conn.setAutoCommit(false);
//
//            OrderDao orderDaoTx = new OrderDao(conn);
//            ProductOrderDao itemDaoTx = new ProductOrderDao(conn);
//            PaymentDao paymentDaoTx = new PaymentDao(conn);
//            ProductDao productDaoTx = new ProductDao(conn); // se fores baixar stock
//
//            // 3.1) Inserir cabeçalho
//            boolean ok = orderDaoTx.add(order);
//            if (!ok) {
//                throw new RuntimeException("Falha ao gravar Order.");
//            }
//
//            // 3.2) Obter o ID gravado (podes usar getGeneratedKeys; aqui uso o teu getFromNumber)
//            Order salvo = orderDaoTx.getFromNumber(order.getNumber());
//            if (salvo == null || salvo.getId() <= 0) {
//                throw new RuntimeException("Falha ao recuperar Order gravado.");
//            }
//
////            // 3.3) Inserir itens + baixa de stock
////            for (ProductOrder po : order.getProducts()) {
////                itemDaoTx.add(po, salvo.getId());
////                // baixa de stock (se aplicável)
////                int novoStock = po.getProduct().getStockTotal() - po.getQty();
////                productDaoTx.updateStock(po.getProduct().getId(), Math.max(novoStock, 0));
////            }
//// 3.3) Inserir itens + baixa de stock
//            for (ProductOrder po : order.getProducts()) {
//                // 1) Registar item da fatura
//                itemDaoTx.add(po, salvo.getId());
//
//                // 2) Registar movimento de stock (saída)
//                StockMovement movimento = new StockMovement();
//                movimento.setProduct(po.getProduct());
//                movimento.setQuantity(-po.getQty()); // saída = negativo
//                movimento.setOrigin("VENDA");
//                movimento.setType("SAIDA");
//                movimento.setReason("VENDA " + salvo.getPrefix() + "/" + salvo.getNumber());
//                movimento.setUser(order.getSeller());
//
//                StockMovementDao stockDao = new StockMovementDao(conn);
//                stockDao.add(movimento);
//            }
//
//            // 3.4) Inserir pagamentos
//            for (Payment p : pagamentos) {
//                p.setInvoiceId(salvo.getId());
//                p.setInvoiceType(salvo.getPrefix());
//                p.setPrefix(salvo.getPrefix());
//                p.setNumber(salvo.getNumber());
//                p.setClient(salvo.getClient());
//                p.setUser(salvo.getSeller());
//                if (p.getDate() == null) {
//                    p.setDate(date);
//                }
//                if (p.getCurrency() == null) {
//                    p.setCurrency("AOA");
//                }
//                if (p.getStatus() == null) {
//                    p.setStatus(PaymentStatus.SUCCESS);
//                }
//
//                paymentDaoTx.add(p, salvo.getId());
//            }
//
//            conn.commit();// ✅ confirma tudo
//            sucesso = true;
//            return orderDaoTx.getId(salvo.getId());
//        } catch (Exception e) {
//            System.err.println("Erro ao processar venda: " + e.getMessage());
//            if (conn != null) {
//                try {
//                    if (!sucesso) { // 🚧 só tenta rollback se não comitou ainda
//                        conn.rollback();
//                        System.err.println("Transação revertida.");
//                    }
//                } catch (SQLException ex) {
//                    System.err.println("Erro ao reverter: " + ex.getMessage());
//                }
//            }
//            throw e;
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.setAutoCommit(true); // ✅ volta ao normal
////                    conn.close(); // ✅ fecha conexão
//                } catch (SQLException e) {
//                    System.err.println("Erro ao fechar conexão: " + e.getMessage());
//                }
//            }
//        }
//    }
//
//    /**
//     * Totais com PREÇO COM IVA embutido
//     */
//    private static class Totais {
//
//        final BigDecimal subTotal; // líquido
//        final BigDecimal tax;      // IVA
//        final BigDecimal total;    // bruto
//
//        Totais(BigDecimal s, BigDecimal i, BigDecimal t) {
//            this.subTotal = s;
//            this.tax = i;
//            this.total = t;
//        }
//    }
//
//    private Totais calcularTotaisComIvaEmbutido(List<ProductOrder> itens) {
//        BigDecimal sub = BigDecimal.ZERO, iva = BigDecimal.ZERO, tot = BigDecimal.ZERO;
//        for (ProductOrder po : itens) {
//            if (po == null || po.getProduct() == null) {
//                continue;
//            }
//            BigDecimal qty = bd(po.getQty());
//            BigDecimal price = bd(po.getPrice()); // preço unitário COM IVA
//            BigDecimal gross = price.multiply(qty);
//
//            BigDecimal perc = po.getTaxePercentage() == null ? BigDecimal.ZERO : new BigDecimal(po.getTaxePercentage().toString());
//            BigDecimal lineTax, lineNet;
//            if (perc.compareTo(BigDecimal.ZERO) > 0) {
//                BigDecimal base = new BigDecimal("100").add(perc);
//                lineTax = gross.multiply(perc).divide(base, 2, RoundingMode.HALF_UP);
//                lineNet = gross.subtract(lineTax);
//            } else {
//                lineTax = BigDecimal.ZERO;
//                lineNet = gross;
//            }
//            sub = sub.add(lineNet);
//            iva = iva.add(lineTax);
//            tot = tot.add(gross);
//        }
//        return new Totais(
//                sub.setScale(2, RoundingMode.HALF_UP),
//                iva.setScale(2, RoundingMode.HALF_UP),
//                tot.setScale(2, RoundingMode.HALF_UP)
//        );
//    }
//
//    private static BigDecimal bd(Number n) {
//        return (n == null) ? BigDecimal.ZERO : new BigDecimal(n.toString());
//    }
//
//}
