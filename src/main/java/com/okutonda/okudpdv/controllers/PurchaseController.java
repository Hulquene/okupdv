package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.PurchaseDao;
import com.okutonda.okudpdv.data.entities.InvoiceType;
import com.okutonda.okudpdv.data.entities.Purchase;
import com.okutonda.okudpdv.helpers.UserSession;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Controller responsável pelas regras de negócio de Compras.
 *
 * Garante validações, geração de número sequencial e integração com o DAO que
 * executa transações e movimenta stock.
 *
 * @author Hulquene
 */
public class PurchaseController {

    private final PurchaseDao dao;
    private final UserSession session = UserSession.getInstance();

    public PurchaseController() {
        this.dao = new PurchaseDao();
    }

    /**
     * Gera o número formatado da compra (ex: COMP/2025/0001)
     */
    private String gerarNumeroCompra(int sequencia) {
        String ano = String.valueOf(LocalDate.now().getYear());
        return String.format("COMP/%s/%04d", ano, sequencia);
    }

    /**
     * Adiciona uma nova compra ao sistema com todas as validações de negócio.
     */
    public boolean add(Purchase p) {
        try {
            // 🔹 1) Validar fornecedor
            if (p.getSupplier() == null || p.getSupplier().getId() <= 0) {
                JOptionPane.showMessageDialog(null, "Fornecedor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // 🔹 2) Garantir usuário logado
            if (p.getUser() == null || p.getUser().getId() <= 0) {
                p.setUser(session.getUser());
            }

            // 🔹 3) Validar itens
            if (p.getItems() == null || p.getItems().isEmpty()) {
                JOptionPane.showMessageDialog(null, "A compra precisa conter pelo menos um item!", "Erro", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            // 🔹 4) Calcular totais
            BigDecimal total = BigDecimal.ZERO;
            BigDecimal ivaTotal = BigDecimal.ZERO;

            for (var item : p.getItems()) {
                total = total.add(item.getSubtotal());
                if (item.getIva() != null) {
                    ivaTotal = ivaTotal.add(item.getIva());
                }
            }

            p.setTotal(total);
            p.setIvaTotal(ivaTotal);

            // 🔹 5) Gerar número de compra sequencial
            int nextSeq = dao.getNextNumber();
            String numero = gerarNumeroCompra(nextSeq);
            p.setInvoiceNumber(numero);
            p.setInvoiceType(InvoiceType.FT); // tipo fixo: Fatura de compra

            // 🔹 6) Datas padrão
            if (p.getDataCompra() == null) {
                p.setDataCompra(java.sql.Date.valueOf(LocalDate.now()));
            }
            if (p.getDataVencimento() == null) {
                p.setDataVencimento(java.sql.Date.valueOf(LocalDate.now().plusDays(30)));
            }

            // 🔹 7) Status padrão
            if (p.getStatus() == null || p.getStatus().isEmpty()) {
                p.setStatus("ABERTO"); // ABERTO | PAGO | CANCELADO
            }

            // 🔹 8) Gravar via DAO (com transação e movimentação de stock)
            boolean ok = dao.add(p);
            if (ok) {
                JOptionPane.showMessageDialog(null,
                        "✅ Compra registada com sucesso!\nNúmero: " + p.getInvoiceNumber(),
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null,
                        "❌ Falha ao registar a compra. Verifique o log.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return false;
            }

        } catch (Exception e) {
            System.err.println("Erro ao adicionar compra: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                    "Erro ao adicionar compra: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // ==========================================================
    // 🔹 Métodos de consulta
    // ==========================================================
    /**
     * Lista todas as compras
     */
    public List<Purchase> listar() {
        return dao.findAll();
    }

    /**
     * Filtra compras por texto (número, fornecedor, descrição)
     */
    public List<Purchase> filtrar(String texto) {
        return dao.filter(texto);
    }

    /**
     * Busca uma compra específica pelo ID
     */
    public Purchase findById(int id) {
        return dao.findById(id);
    }

    /**
     * Remove uma compra pelo ID
     */
    public boolean remover(int id) {
        return dao.delete(id);
    }
}

///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.okutonda.okudpdv.controllers;
//
//import com.okutonda.okudpdv.data.dao.PurchaseDao;
//import com.okutonda.okudpdv.data.entities.InvoiceType;
//import com.okutonda.okudpdv.data.entities.Purchase;
//import com.okutonda.okudpdv.utilities.UserSession;
//import java.math.BigDecimal;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.List;
//
///**
// *
// * @author kenny
// */
//public class PurchaseController {
//
//    private final PurchaseDao dao;
//    UserSession session = UserSession.getInstance();
//
//    public PurchaseController() {
//        this.dao = new PurchaseDao();
//    }
//
//    /**
//     * Gera número de compra formatado (ex: COMP/2025/0001)
//     */
//    private String gerarNumeroCompra(int sequencia) {
//        String ano = String.valueOf(LocalDate.now().getYear());
//        return String.format("COMP/%s/%04d", ano, sequencia);
//    }
//
//    public boolean add(Purchase p) {
//        try {
//            // 1) Validar fornecedor
//            if (p.getSupplier() == null || p.getSupplier().getId() <= 0) {
//                throw new IllegalArgumentException("Fornecedor inválido");
//            }
//
//            // 1) Validar fornecedor
//            if (p.getUser() == null || p.getUser().getId() <= 0) {
//                p.setUser(session.getUser());
//            }
//
//            // 2) Validar lista de itens
//            if (p.getItems() == null || p.getItems().isEmpty()) {
//                throw new IllegalArgumentException("A compra não contém itens");
//            }
//
//            // 3) Calcular totais
//            BigDecimal total = BigDecimal.ZERO;
//            BigDecimal ivaTotal = BigDecimal.ZERO;
//
//            for (var item : p.getItems()) {
//                total = total.add(item.getSubtotal());
//                if (item.getIva() != null) {
//                    ivaTotal = ivaTotal.add(item.getIva());
//                }
//            }
//
//            p.setTotal(total);
//            p.setIvaTotal(ivaTotal);
//
//            // 4) Gerar número sequencial da compra
//            int nextSeq = dao.getNextNumber(); // precisas implementar no DAO
//            String numero = gerarNumeroCompra(nextSeq);
//            p.setInvoiceNumber(numero);
//            p.setInvoiceType(InvoiceType.FT); // prefixo fixo para compras
//
//            // 5) Validar datas
//            if (p.getDataCompra() == null) {
//                p.setDataCompra(java.sql.Date.valueOf(LocalDate.now()));
//            }
//            if (p.getDataVencimento() == null) {
//                // por default +30 dias
//                p.setDataVencimento(java.sql.Date.valueOf(LocalDate.now().plusDays(30)));
//            }
//
//            // 6) Status inicial
//            if (p.getStatus() == null || p.getStatus().isEmpty()) {
//                p.setStatus("ABERTO"); // ABERTO | PAGO | CANCELADO
//            }
//
//            // 7) Gravar no banco
//            return dao.add(p);
//
//        } catch (SQLException e) {
//            System.out.println("Erro SQL ao adicionar compra: " + e.getMessage());
//            return false;
//        } catch (Exception e) {
//            System.out.println("Erro ao adicionar compra: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public List<Purchase> get() {
//        return dao.list();
//    }
////    PurchaseDao dao;
////
////    public PurchaseController() {
////        this.dao = new PurchaseDao();
////    }
////
//    public Purchase getId(int id) {
//        return dao.getId(id);
//    }
////
//////    public Purchase getFromBarCode(String barcode) {
//////        return dao.searchFromBarCode(barcode);
//////    }
////    public Purchase getName(String description) {
////        return dao.getFromDescription(description);
////    }
////
////    public List<Purchase> get(String where) {
////        return dao.list(where);
////    }
////
////    public List<Purchase> getPurchase() {
////        return dao.list("");
////    }
////
//////    public List<Purchase> getProductsStock() {
//////        return dao.listHistoryStock(" WHERE type ='product'");
//////    }
////////    public List<Purchase> filterProductStock(String txt) {
////////        return dao.filterProduct(txt);
////////    }
//////    public List<Purchase> filterProduct(String txt) {
//////        return dao.filterProduct(txt);
//////    }
//////    public List<Purchase> getServices() {
//////        return dao.list(" WHERE type ='service'");
//////    }
//////    public List<Product> filterService(String txt) {
//////        return dao.filterProduct(txt);
//////    }
////    public List<Purchase> filter(String txt) {
////        return dao.filter(txt);
////    }
////
////    public Boolean add(Purchase pur) {
////        return dao.add(pur);
////    }
////
//////    public boolean updateStock(int prodId, int stock) {
//////        return dao.updateStock(prodId, stock);
//////    }
////    public Boolean deleteId(int id) {
////        return dao.delete(id);
////    }
////
//////    public Double CalculateTotalProduct(Purchase prod, int qtd) {
//////        return prod.getPrice() * qtd;
//////    }
//////
//////    public Double CalculateTotalChangeProduct(Purchase prod) {
//////        return null;
//////    }
//////
//////    public Double CalculateTotalValueTaxeProduct(Purchase prod) {
//////        return null;
//////    }
//}
