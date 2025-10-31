package com.okutonda.okudpdv.helpers;

import com.okutonda.okudpdv.data.dao.TaxeDaoHibernate;
import com.okutonda.okudpdv.data.dao.TaxeReasonDaoHibernate;
import com.okutonda.okudpdv.data.dao.WarehouseDaoHibernate;
import com.okutonda.okudpdv.data.entities.Taxes;
import com.okutonda.okudpdv.data.entities.ReasonTaxes;
import com.okutonda.okudpdv.data.entities.Warehouse;

public class TestTaxWarehouseDAOs {
    
    public static void main(String[] args) {
        try {
            System.out.println("🧪 Testando DAOs de Taxes, ReasonTaxes e Warehouse...");
            
            testTaxeDao();
            testTaxeReasonDao();
            testWarehouseDao();
            
            System.out.println("✅ Todos os testes passaram!");
            
        } catch (Exception e) {
            System.err.println("❌ Erro no teste: " + e.getMessage());
            e.printStackTrace();
        } finally {
            com.okutonda.okudpdv.data.config.HibernateUtil.closeSession();
        }
    }
    
    private static void testTaxeDao() {
        System.out.println("\n💰 Testando TaxeDaoHibernate...");
        TaxeDaoHibernate dao = new TaxeDaoHibernate();
        
        // Buscar todos
        var allTaxes = dao.findAll();
        System.out.println("✅ Taxes encontrados: " + allTaxes.size());
        
        if (!allTaxes.isEmpty()) {
            Taxes firstTax = allTaxes.get(0);
            System.out.println("✅ Primeiro tax: " + firstTax.getName() + " - " + firstTax.getPercentage() + "%");
            
            // Testar busca por ID
            dao.findById(firstTax.getId())
               .ifPresent(tax -> System.out.println("✅ Tax por ID: " + tax.getName()));
            
            // Testar tax padrão
            dao.findDefaultTax()
               .ifPresentOrElse(
                   tax -> System.out.println("✅ Tax padrão: " + tax.getName()),
                   () -> System.out.println("⚠️ Nenhum tax padrão definido")
               );
        }
    }
    
    private static void testTaxeReasonDao() {
        System.out.println("\n📋 Testando TaxeReasonDaoHibernate...");
        TaxeReasonDaoHibernate dao = new TaxeReasonDaoHibernate();
        
        // Buscar todos
        var allReasons = dao.findAll();
        System.out.println("✅ ReasonTaxes encontrados: " + allReasons.size());
        
        if (!allReasons.isEmpty()) {
            ReasonTaxes firstReason = allReasons.get(0);
            System.out.println("✅ Primeira reason: " + firstReason.getCode() + " - " + firstReason.getReason());
            
            // Testar busca por código
            dao.findByCode(firstReason.getCode())
               .ifPresent(reason -> System.out.println("✅ Reason por código: " + reason.getReason()));
        }
    }
    
    private static void testWarehouseDao() {
        System.out.println("\n🏭 Testando WarehouseDaoHibernate...");
        WarehouseDaoHibernate dao = new WarehouseDaoHibernate();
        
        // Buscar todos
        var allWarehouses = dao.findAll();
        System.out.println("✅ Warehouses encontrados: " + allWarehouses.size());
        
        if (!allWarehouses.isEmpty()) {
            Warehouse firstWarehouse = allWarehouses.get(0);
            System.out.println("✅ Primeiro warehouse: " + firstWarehouse.getName() + " - " + firstWarehouse.getLocation());
            
            // Testar busca por ID
            dao.findById(firstWarehouse.getId())
               .ifPresent(warehouse -> System.out.println("✅ Warehouse por ID: " + warehouse.getName()));
            
            // Testar warehouses ativos
            var activeWarehouses = dao.findActive();
            System.out.println("✅ Warehouses ativos: " + activeWarehouses.size());
        }
    }
}