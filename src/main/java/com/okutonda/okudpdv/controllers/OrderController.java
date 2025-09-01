/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.dao.OrderDao;
import com.okutonda.okudpdv.dao.ProductDao;
import com.okutonda.okudpdv.dao.ProductOrderDao;
import com.okutonda.okudpdv.models.Order;
import com.okutonda.okudpdv.models.ProductOrder;
import com.okutonda.okudpdv.utilities.UserSession;
import com.okutonda.okudpdv.utilities.UtilDate;
import com.okutonda.okudpdv.utilities.UtilSaft;
import com.okutonda.okudpdv.utilities.UtilSales;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author kenny
 */
public class OrderController {

    OrderDao dao;
    ProductOrderDao prodOrderDao;
//    ProductDao prodDao;
    UserSession session = UserSession.getInstance();
//    private final String where = "WHERE seller_id =" + session.getUser().getId();
    double subTotal, total;

//    public OrderController(OrderDao dao, ProductOrderDao prodOrderDao) {
//        this.dao = dao;
//        this.prodOrderDao = prodOrderDao;
//        this.prodDao =  ProductDao();
////        session = UserSession.getInstance();
//    }
    public OrderController(OrderDao dao, ProductOrderDao prodOrderDao, ProductDao prodDao, double subTotal, double total) {
        this.dao = dao;
        this.prodOrderDao = prodOrderDao;
//        this.prodDao = prodDao;
        this.subTotal = subTotal;
        this.total = total;
    }

    public OrderController() {
        this.dao = new OrderDao();
        this.prodOrderDao = new ProductOrderDao();
//        this.prodDao = new ProductDao();
    }

    public List<Order> get() {
        if (session.getUser().getProfile().equals("admin") || session.getUser().getProfile().equals("manager")) {
            return dao.list("");
        }
        return dao.list("WHERE seller_id =" + session.getUser().getId());
    }

    public List<Order> filter(String txt) {
        if (session.getUser().getProfile().equals("admin") || session.getUser().getProfile().equals("manager")) {
            return dao.filter(txt, "");
        }
        return dao.filter(txt, "WHERE seller_id =" + session.getUser().getId());
    }

    public List<Order> getOrderSeller(int idSeller) {
        if (session.getUser().getProfile().equals("admin") || session.getUser().getProfile().equals("manager")) {
            return dao.filter("", "WHERE seller_id =" + idSeller);
        }
        return dao.filter("", "WHERE seller_id =" + session.getUser().getId());
    }

    public List<Order> getOrderClient(int idClient) {
        if (session.getUser().getProfile().equals("admin") || session.getUser().getProfile().equals("manager")) {
            return dao.filter("", "WHERE client_id =" + idClient);
        }
        return dao.filter("", "WHERE seller_id =" + session.getUser().getId());
    }

    public List<Order> filterDate(LocalDate dateFrom, LocalDate dateTo) {
        if (session.getUser().getProfile().equals("admin") || session.getUser().getProfile().equals("manager")) {
            return dao.filterDate(dateFrom, dateTo, "");
        }
        return dao.filterDate(dateFrom, dateTo, "and seller_id =" + session.getUser().getId());
    }

    public Order getId(int id) {
        return dao.getId(id);
    }

    public Order add(Order order) {

        String prefix = UtilSales.getPrefix("order");
        int number = this.getNextNumber();
        String date = UtilDate.getFormatDataNow();
//        String numberOrder = prefix + number; 
        String numberOrder = UtilSales.FormatedNumberPrefix2(number, UtilDate.getYear(), prefix);

        String hash = UtilSaft.appGenerateHashInvoice(date, date, numberOrder, String.valueOf(total), "");

        order.setPrefix(prefix);
        order.setNumber(number);
        order.setDatecreate(date);
        order.setHash(hash);
        boolean status = dao.add(order);
//        int totalStock;
//        List<ProductOrder> prodOrderList = order.getProducts();
        System.out.println(order.toString());
        if (status == true) {
            Order responde = dao.getFromNumber(order.getNumber());
//            responde = dao.getId(responde.getId());

            for (ProductOrder prod : order.getProducts()) {
                prodOrderDao.add(prod, responde.getId());
//                totalStock = 0;
//                System.out.println("o id:" + prod.getProduct().getId());
//                System.out.println("stock: "+prod.getProduct().getStockTotal());
////                Product product = prodDao.getId(prod.getProduct().getId());
////                System.out.println("teste2" + product.getStockTotal());

//                totalStock = prod.getProduct().getStockTotal() - prod.getQty();
////                System.out.println(prod.getProduct().getId() + "totalStock: " + totalStock);
//                prodDao.updateStock(prod.getProduct().getId(), totalStock);
            }
//            for (ProductOrder prod : order.getProducts()) {
////                prodOrderDao.add(prod, responde.getId());
//                totalStock = 0;
//                System.out.println("o id:" + prod.getProduct().getId());
//                System.out.println("stock: "+prod.getProduct().getStockTotal());
////                Product product = prodDao.getId(prod.getProduct().getId());
////                System.out.println("teste2" + product.getStockTotal());
//                totalStock = prod.getProduct().getStockTotal() - prod.getQty();
//                System.out.println(prod.getProduct().getId() + "totalStock: " + totalStock);
//                this.prodDao.updateStock(prod.getProduct().getId(), totalStock);
//
//            }
            return responde;
        }
        return null;
    }

    public int getNextNumber() {
        return dao.getNextNumberOrder();
    }

    public Double CalculateSubTotalOrder(List<ProductOrder> listProductOrder) {

//        Double[] totalOrder = [];
//        
//        totalOrder[0] = 0.0;
//        total = subTotal = 0;
//        for (ProductOrder productOrder : listProductOrder) {
//            subTotal = productOrder.getProduct().getPrice() * productOrder.getQty();
//            total += productOrder.getProduct().getPrice() * productOrder.getQty();
//        }
        return null;
    }

    public Double CalculateTotalOrder(List<ProductOrder> listProductOrder) {
        total = subTotal = 0;
        for (ProductOrder productOrder : listProductOrder) {
            subTotal = productOrder.getProduct().getPrice() * productOrder.getQty();
            total += productOrder.getProduct().getPrice() * productOrder.getQty();
        }

        return null;
    }

    public Double CalculateTotalChangeOrder(Order order) {
        return null;
    }

    public Double CalculateTotalValueTaxeOrder(Order order) {
        return null;
    }
}
