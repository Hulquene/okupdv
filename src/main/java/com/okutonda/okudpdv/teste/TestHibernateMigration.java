package com.okutonda.okudpdv.teste;

import com.okutonda.okudpdv.data.config.HibernateConfig;
import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.dao.OptionsDao;
import com.okutonda.okudpdv.data.dao.UserDao;
import com.okutonda.okudpdv.data.dao.UserDao;
import com.okutonda.okudpdv.data.entities.Options;
import com.okutonda.okudpdv.data.entities.User;

public class TestHibernateMigration {

    public static void main(String[] args) {
        try {
            System.out.println("🧪 Testando migração para Hibernate...");

            // Testar conexão
            boolean connected = HibernateConfig.testConnection();
            if (!connected) {
                System.err.println("❌ Falha na conexão Hibernate");
                return;
            }

            // Testar Options
            testOptions();

            // Testar Users
            testUsers();

            System.out.println("✅ Todos os testes passaram!");

        } catch (Exception e) {
            System.err.println("❌ Erro no teste: " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession();
        }
    }

    private static void testOptions() {
        System.out.println("\n📋 Testando Options...");
        OptionsDao dao = new OptionsDao();

        // Buscar todas
        var allOptions = dao.findAll();
        System.out.println("✅ Options encontradas: " + allOptions.size());

        // Buscar por nome
        dao.findByName("install_complete")
                .ifPresentOrElse(
                        opt -> System.out.println("✅ Option encontrada: " + opt.getValue()),
                        () -> System.out.println("⚠️ Option não encontrada")
                );
    }

    private static void testUsers() {
        System.out.println("\n👥 Testando Users...");
        UserDao dao = new UserDao();

        // Buscar todos
        var allUsers = dao.findAll();
        System.out.println("✅ Users encontrados: " + allUsers.size());

        if (!allUsers.isEmpty()) {
            // Testar primeiro usuário
            User firstUser = allUsers.get(0);
            System.out.println("✅ Primeiro user: " + firstUser.getName() + " - " + firstUser.getEmail());

            // Testar busca por ID
            dao.findById(firstUser.getId())
                    .ifPresent(user -> System.out.println("✅ User por ID: " + user.getName()));
        }

        // Testar filtro
        var filteredUsers = dao.filter("admin");
        System.out.println("✅ Users filtrados: " + filteredUsers.size());
    }
}
