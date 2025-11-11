package com.okutonda.okudpdv.teste;

import com.okutonda.okudpdv.controllers.*;

public class TestAllControllersHibernate {
    
    public static void main(String[] args) {
        try {
            System.out.println("🧪 Testando TODOS os Controllers Hibernate...");
            
            testGroupsProductController();
            testProductController();
            testProductOrderController();
            testTaxeController();
            testTaxeReasonController();
            testWarehouseController();
            
            System.out.println("✅ Todos os controllers testados com sucesso!");
            
        } catch (Exception e) {
            System.err.println("❌ Erro no teste: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testGroupsProductController() {
        System.out.println("\n📦 Testando GroupsProductControllerHibernate...");
        GroupsProductController controller = new GroupsProductController();
        
        var allGroups = controller.getAll();
        System.out.println("✅ Grupos encontrados: " + allGroups.size());
    }
    
    private static void testProductController() {
        System.out.println("\n🛍️ Testando ProductControllerHibernate...");
        ProductController controller = new ProductController();
        
        var allProducts = controller.listAll();
        System.out.println("✅ Produtos encontrados: " + allProducts.size());
        
        var activeProducts = controller.findActive();
        System.out.println("✅ Produtos ativos: " + activeProducts.size());
    }
    
    private static void testProductOrderController() {
        System.out.println("\n📋 Testando ProductOrderControllerHibernate...");
        ProductOrderController controller = new ProductOrderController();
        
        var allItems = controller.getAll();
        System.out.println("✅ Itens de pedido encontrados: " + allItems.size());
    }
    
    private static void testTaxeController() {
        System.out.println("\n💰 Testando TaxeControllerHibernate...");
        TaxeController controller = new TaxeController();
        
        var allTaxes = controller.listarTodas();
        System.out.println("✅ Impostos encontrados: " + allTaxes.size());
        
        var defaultTax = controller.getDefaultTax();
        System.out.println("✅ Tax padrão: " + (defaultTax != null ? defaultTax.getName() : "Nenhum"));
    }
    
    private static void testTaxeReasonController() {
        System.out.println("\n📄 Testando TaxeReasonControllerHibernate...");
        TaxeReasonController controller = new TaxeReasonController();
        
        var allReasons = controller.listarTodas();
        System.out.println("✅ Razões fiscais encontradas: " + allReasons.size());
    }
    
    private static void testWarehouseController() {
        System.out.println("\n🏭 Testando WarehouseControllerHibernate...");
        WarehouseController controller = new WarehouseController();
        
        var allWarehouses = controller.listarTodos();
        System.out.println("✅ Armazéns encontrados: " + allWarehouses.size());
        
        var activeWarehouses = controller.listarAtivos();
        System.out.println("✅ Armazéns ativos: " + activeWarehouses.size());
    }
}