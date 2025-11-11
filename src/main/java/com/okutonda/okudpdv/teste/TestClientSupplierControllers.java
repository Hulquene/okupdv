package com.okutonda.okudpdv.teste;

import com.okutonda.okudpdv.controllers.ClientController;
import com.okutonda.okudpdv.controllers.SupplierController;
import com.okutonda.okudpdv.data.entities.Clients;
import com.okutonda.okudpdv.data.entities.Supplier;

public class TestClientSupplierControllers {
    
    public static void main(String[] args) {
        try {
            System.out.println("🧪 Testando Controllers de Client e Supplier...");
            
            testClientController();
            testSupplierController();
            
            System.out.println("✅ Todos os testes dos controllers passaram!");
            
        } catch (Exception e) {
            System.err.println("❌ Erro no teste: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testClientController() {
        System.out.println("\n👥 Testando ClientControllerHibernate...");
        ClientController controller = new ClientController();
        
        // Buscar todos os clientes
        var allClients = controller.getAll();
        System.out.println("✅ Clientes encontrados: " + allClients.size());
        
        if (!allClients.isEmpty()) {
            Clients firstClient = allClients.get(0);
            System.out.println("✅ Primeiro cliente: " + firstClient.getName());
            
            // Testar busca por ID
            Clients clientById = controller.getById(firstClient.getId());
            System.out.println("✅ Cliente por ID: " + (clientById != null ? clientById.getName() : "null"));
            
            // Testar filtro
            var filteredClients = controller.filter(firstClient.getName().substring(0, 3));
            System.out.println("✅ Clientes filtrados: " + filteredClients.size());
            
            // Testar cliente padrão
            Clients defaultClient = controller.getDefaultClient();
            if (defaultClient != null) {
                System.out.println("✅ Cliente padrão: " + defaultClient.getName());
            }
        }
        
        // Testar validação de NIF
        boolean nifExists = controller.nifExists("123456789");
        System.out.println("✅ NIF existe: " + nifExists);
    }
    
    private static void testSupplierController() {
        System.out.println("\n🏭 Testando SupplierControllerHibernate...");
        SupplierController controller = new SupplierController();
        
        // Buscar todos os fornecedores
        var allSuppliers = controller.listarTodos();
        System.out.println("✅ Fornecedores encontrados: " + allSuppliers.size());
        
        if (!allSuppliers.isEmpty()) {
            Supplier firstSupplier = allSuppliers.get(0);
            System.out.println("✅ Primeiro fornecedor: " + firstSupplier.getName());
            
            // Testar busca por ID
            Supplier supplierById = controller.getById(firstSupplier.getId());
            System.out.println("✅ Fornecedor por ID: " + (supplierById != null ? supplierById.getName() : "null"));
            
            // Testar filtro
            var filteredSuppliers = controller.filtrar(firstSupplier.getName().substring(0, 3));
            System.out.println("✅ Fornecedores filtrados: " + filteredSuppliers.size());
        }
        
        // Testar fornecedores ativos
        var activeSuppliers = controller.listarAtivos();
        System.out.println("✅ Fornecedores ativos: " + activeSuppliers.size());
        
        // Testar validação de NIF
        boolean nifExists = controller.nifExists("987654321");
        System.out.println("✅ NIF existe: " + nifExists);
        
        // Testar contagem
        long activeCount = controller.contarFornecedoresAtivos();
        System.out.println("✅ Total de fornecedores ativos: " + activeCount);
    }
}