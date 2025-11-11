package com.okutonda.okudpdv.teste;

import com.okutonda.okudpdv.controllers.*;

public class TestFinalControllers {

    public static void main(String[] args) {
        try {
            System.out.println("🧪 Testando ÚLTIMOS Controllers Hibernate...");

            testStockMovementController();
            testPaymentController();

            System.out.println("✅ Todos os controllers finais testados com sucesso!");

        } catch (Exception e) {
            System.err.println("❌ Erro no teste: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testStockMovementController() {
        System.out.println("\n📦 Testando StockMovementControllerHibernate...");
        StockMovementController controller = new StockMovementController();

        var allMovements = controller.listarTodos();
        System.out.println("✅ Movimentos encontrados: " + allMovements.size());

        if (!allMovements.isEmpty()) {
            var stockAtual = controller.getStockAtual(1); // Teste com produto ID 1
            System.out.println("✅ Stock atual do produto 1: " + stockAtual);
        }
    }

    private static void testPaymentController() {
        System.out.println("\n💰 Testando PaymentControllerHibernate...");
        PaymentController controller = new PaymentController();

        var allPayments = controller.getAll();
        System.out.println("✅ Pagamentos encontrados: " + allPayments.size());

        var referenciaUnica = controller.gerarReferenciaUnica();
        System.out.println("✅ Referência única gerada: " + referenciaUnica);
    }

}
