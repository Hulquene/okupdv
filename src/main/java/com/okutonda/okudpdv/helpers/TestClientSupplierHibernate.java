package com.okutonda.okudpdv.helpers;

import com.okutonda.okudpdv.data.dao.ClientDao;
import com.okutonda.okudpdv.data.dao.SupplierDao;
import com.okutonda.okudpdv.data.entities.Clients;
import com.okutonda.okudpdv.data.entities.Supplier;

public class TestClientSupplierHibernate {

    public static void main(String[] args) {
        try {
            System.out.println("🧪 Testando Client e Supplier Hibernate...");

            testClientDao();
            testSupplierDao();

            System.out.println("✅ Todos os testes passaram!");

        } catch (Exception e) {
            System.err.println("❌ Erro no teste: " + e.getMessage());
            e.printStackTrace();
        } finally {
            com.okutonda.okudpdv.data.config.HibernateUtil.closeSession();
        }
    }

    private static void testClientDao() {
        System.out.println("\n👥 Testando ClientDaoHibernate...");
        ClientDao dao = new ClientDao();

        // Buscar todos
        var allClients = dao.findAll();
        System.out.println("✅ Clients encontrados: " + allClients.size());

        if (!allClients.isEmpty()) {
            // Testar primeiro cliente
            Clients firstClient = allClients.get(0);
            System.out.println("✅ Primeiro client: " + firstClient.getName());

            // Testar busca por ID
            dao.findById(firstClient.getId())
                    .ifPresent(client -> System.out.println("✅ Client por ID: " + client.getName()));

            // Testar filtro
            var filteredClients = dao.filter(firstClient.getName().substring(0, 3));
            System.out.println("✅ Clients filtrados: " + filteredClients.size());
        }

        // Testar cliente padrão
        dao.getDefaultClient()
                .ifPresentOrElse(
                        client -> System.out.println("✅ Client padrão: " + client.getName()),
                        () -> System.out.println("⚠️ Nenhum client padrão definido")
                );
    }

    private static void testSupplierDao() {
        System.out.println("\n🏭 Testando SupplierDaoHibernate...");
        SupplierDao dao = new SupplierDao();

        // Buscar todos
        var allSuppliers = dao.findAll();
        System.out.println("✅ Suppliers encontrados: " + allSuppliers.size());

        if (!allSuppliers.isEmpty()) {
            // Testar primeiro supplier
            Supplier firstSupplier = allSuppliers.get(0);
            System.out.println("✅ Primeiro supplier: " + firstSupplier.getName());

            // Testar busca por ID
            dao.findById(firstSupplier.getId())
                    .ifPresent(supplier -> System.out.println("✅ Supplier por ID: " + supplier.getName()));

            // Testar filtro
            var filteredSuppliers = dao.filter(firstSupplier.getName().substring(0, 3));
            System.out.println("✅ Suppliers filtrados: " + filteredSuppliers.size());
        }

        // Testar suppliers ativos
        var activeSuppliers = dao.findActive();
        System.out.println("✅ Suppliers ativos: " + activeSuppliers.size());
    }
}
