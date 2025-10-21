/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.ReportDao;
import com.okutonda.okudpdv.utilities.UserSession;
import com.okutonda.okudpdv.utilities.UtillFiles;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author kenny
 */
public class ReportController {

    ReportDao dao;
    UserSession session = UserSession.getInstance();

    public ReportController() {
        this.dao = new ReportDao();
    }

    public Boolean salesProductsArrayListToExcell(List<List<String>> data) {

//        List<List<String>> data = new ArrayList<>();
//        data.add(List.of("ID", "Data", "Descricao", "Produto", "QTD", "Preço", "Imposto", "Ttal"));
//        data.add(List.of("Alice", "30", "São Paulo"));
//        data.add(List.of("Bob", "25", "Rio de Janeiro"));
//        data.add(List.of("Carol", "35", "Belo Horizonte"));
//        data.add(List.of("Carol", "35", "Belo Horizonte"));
//        for (ProductOrder prod : listProd) {
//            data.add(List.of(String.valueOf(prod.getId()), String.valueOf(prod.getDate()), "", prod.getDescription(), String.valueOf(prod.getQty()), prod.getPrice().toString(), prod.getTaxeCode(), ""));
//        }
        String filePath = "dados.xlsx";
        try {
            UtillFiles.convertArrayListToExcel(data, filePath);
            System.out.println("Arquivo Excel criado com sucesso.");
            return true;
        } catch (IOException e) {
        }
        return false;
    }

    public Boolean salesProductsSellerArrayListToExcell(List<List<String>> data) {

//        List<List<String>> data = new ArrayList<>();
//        data.add(List.of("ID", "Data", "Descricao", "Vendedor", "Produto", "QTD", "Preço", "Imposto", "Ttal"));
//        for (ProductOrder prod : listProd) {
//            data.add(List.of(String.valueOf(prod.getId()), String.valueOf(prod.getDate()), "", "", prod.getDescription(), String.valueOf(prod.getQty()), prod.getPrice().toString(), prod.getTaxeCode(), ""));
//        }
        String filePath = "dados.xlsx";
        try {
            UtillFiles.convertArrayListToExcel(data, filePath);
            System.out.println("Arquivo Excel criado com sucesso.");
            return true;
        } catch (IOException e) {
        }
        return false;
    }

    public Boolean salesOrderSellerArrayListToExcell(List<List<String>> data) {

//        List<List<String>> data = new ArrayList<>();
//        data.add(List.of("ID", "Vendedor", "Fatura", "Data", "SubTotal", "Total"));
//        for (Order order : listOrders) {
//            data.add(List.of(String.valueOf(order.getId()), "", order.getPrefix(), order.getDatecreate(), String.valueOf(order.getSubTotal()), String.valueOf(order.getTotal())));
//        }
        String filePath = "dados.xlsx";
        try {
            UtillFiles.convertArrayListToExcel(data, filePath);
            System.out.println("Arquivo Excel criado com sucesso.");
            return true;
        } catch (IOException e) {
        }
        return false;
    }

    public Boolean salesOrderArrayListToExcell(List<List<String>> data) {

//        List<List<String>> data = new ArrayList<>();
//        data.add(List.of("ID", "Fatura", "Data", "SubTotal", "Total"));
//        for (Order order : listOrders) {
//            data.add(List.of(String.valueOf(order.getId()),order.getPrefix(), order.getDatecreate(), String.valueOf(order.getSubTotal()), String.valueOf(order.getTotal())));
//        }
        String filePath = "dados.xlsx";
        try {
            UtillFiles.convertArrayListToExcel(data, filePath);
            System.out.println("Arquivo Excel criado com sucesso.");
            return true;
        } catch (IOException e) {
        }
        return false;
    }

    public Boolean historyShiftArrayListToExcell(List<List<String>> data) {

//        List<List<String>> data = new ArrayList<>();
//        data.add(List.of("ID", "Fatura", "Data", "SubTotal", "Total"));
//        for (Order order : listOrders) {
//            data.add(List.of(String.valueOf(order.getId()),order.getPrefix(), order.getDatecreate(), String.valueOf(order.getSubTotal()), String.valueOf(order.getTotal())));
//        }
        String filePath = "dados.xlsx";
        try {
            UtillFiles.convertArrayListToExcel(data, filePath);
            System.out.println("Arquivo Excel criado com sucesso.");
            return true;
        } catch (IOException e) {
        }
        return false;
    }

}
