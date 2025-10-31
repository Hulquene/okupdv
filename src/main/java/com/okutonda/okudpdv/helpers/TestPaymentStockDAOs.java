package com.okutonda.okudpdv.helpers;

import com.okutonda.okudpdv.data.dao.PaymentDao;
import com.okutonda.okudpdv.data.dao.StockMovementDao;

public class TestPaymentStockDAOs {

    public static void main(String[] args) {
        try {
            System.out.println("🧪 Testando DAOs de Payment e Stock...");

            testPaymentDao();
            testStockMovementDao();

            System.out.println("✅ Todos os testes passaram!");

        } catch (Exception e) {
            System.err.println("❌ Erro no teste: " + e.getMessage());
            e.printStackTrace();
        } finally {
            com.okutonda.okudpdv.data.config.HibernateUtil.closeSession();
        }
    }

    private static void testPaymentDao() {
        System.out.println("\n💰 Testando PaymentDaoHibernate...");
        PaymentDao dao = new PaymentDao();

        var allPayments = dao.findAll();
        System.out.println("✅ Payments encontrados: " + allPayments.size());

        if (!allPayments.isEmpty()) {
            var firstPayment = allPayments.get(0);
            System.out.println("✅ Primeiro payment: " + firstPayment.getReference() + " - " + firstPayment.getTotal());
        }
    }


    private static void testStockMovementDao() {
        System.out.println("\n📦 Testando StockMovementDaoHibernate...");
        StockMovementDao dao = new StockMovementDao();

        var allMovements = dao.findAll();
        System.out.println("✅ StockMovements encontrados: " + allMovements.size());

        if (!allMovements.isEmpty()) {
            var firstMovement = allMovements.get(0);
            System.out.println("✅ Primeiro movimento: " + firstMovement.getType() + " - " + firstMovement.getQuantity());
        }
    }
}
