package com.okutonda.okudpdv.data.config;

import com.okutonda.okudpdv.data.connection.ConnectionConfig;
import com.okutonda.okudpdv.data.entities.Options;
import com.okutonda.okudpdv.data.entities.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateConfig {

    private static SessionFactory sessionFactory;

    static {
        initializeSessionFactory();
    }

    private static void initializeSessionFactory() {
        try {
            System.out.println("🔄 [Hibernate] Iniciando configuração...");

            // Configuração programática
            Configuration configuration = new Configuration();

            // Aplicar configurações
            applyHibernateSettings(configuration);

            // Registrar entidades
            registerEntities(configuration);

            // Construir SessionFactory
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            sessionFactory = configuration.buildSessionFactory(registry);

            System.out.println("✅ [Hibernate] SessionFactory criada com sucesso!");

        } catch (Exception e) {
            System.err.println("❌ [Hibernate] Erro ao inicializar SessionFactory: " + e.getMessage());
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    private static void applyHibernateSettings(Configuration configuration) {
        // Configurações básicas do banco (usando suas configurações existentes)
        configuration.setProperty("hibernate.connection.url", ConnectionConfig.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", ConnectionConfig.getUser());
        configuration.setProperty("hibernate.connection.password", ConnectionConfig.getPassword());
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");

        // Dialeto do MySQL
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");

        // 🔥 INTEGRAÇÃO COM SEU HIKARICP EXISTENTE
        configuration.setProperty("hibernate.hikari.maximumPoolSize", "10");
        configuration.setProperty("hibernate.hikari.minimumIdle", "2");
        configuration.setProperty("hibernate.hikari.idleTimeout", "30000");
        configuration.setProperty("hibernate.hikari.connectionTimeout", "10000");
        configuration.setProperty("hibernate.hikari.maxLifetime", "1800000");

        // SQL logging (desenvolvimento)
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.format_sql", "true");
        configuration.setProperty("hibernate.highlight_sql", "true");

        // Estratégia DDL - validate para produção
        configuration.setProperty("hibernate.hbm2ddl.auto", "validate");

        // Codificação
        configuration.setProperty("hibernate.connection.characterEncoding", "UTF-8");
        configuration.setProperty("hibernate.connection.useUnicode", "true");

        // Performance
        configuration.setProperty("hibernate.jdbc.batch_size", "20");
        configuration.setProperty("hibernate.order_inserts", "true");
        configuration.setProperty("hibernate.order_updates", "true");

        System.out.println("✅ [Hibernate] Configurações aplicadas");
    }

    private static void registerEntities(Configuration configuration) {
        try {
            // ✅ Registrar suas entidades JPA
            configuration.addAnnotatedClass(com.okutonda.okudpdv.data.entities.Options.class);
            configuration.addAnnotatedClass(com.okutonda.okudpdv.data.entities.User.class);
            configuration.addAnnotatedClass(com.okutonda.okudpdv.data.entities.Clients.class);
            configuration.addAnnotatedClass(com.okutonda.okudpdv.data.entities.Supplier.class);
            configuration.addAnnotatedClass(com.okutonda.okudpdv.data.entities.GroupsProduct.class);
            configuration.addAnnotatedClass(com.okutonda.okudpdv.data.entities.Product.class);
            configuration.addAnnotatedClass(com.okutonda.okudpdv.data.entities.ProductOrder.class);
            configuration.addAnnotatedClass(com.okutonda.okudpdv.data.entities.Taxes.class);
            configuration.addAnnotatedClass(com.okutonda.okudpdv.data.entities.ReasonTaxes.class);
            configuration.addAnnotatedClass(com.okutonda.okudpdv.data.entities.Warehouse.class);
            configuration.addAnnotatedClass(com.okutonda.okudpdv.data.entities.Payment.class);
            configuration.addAnnotatedClass(com.okutonda.okudpdv.data.entities.PaymentModes.class);
            configuration.addAnnotatedClass(com.okutonda.okudpdv.data.entities.StockMovement.class);
            configuration.addAnnotatedClass(com.okutonda.okudpdv.data.entities.ExportSaftFat.class);

            System.out.println("✅ [Hibernate] Entidades registradas: Options, User, Clients, Supplier");

        } catch (Exception e) {
            System.err.println("❌ [Hibernate] Erro ao registrar entidades: " + e.getMessage());
            throw e;
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            throw new IllegalStateException("SessionFactory não está disponível");
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("✅ [Hibernate] SessionFactory fechada.");
        }
    }

    // Teste de conexão integrado com seu sistema atual
    public static boolean testConnection() {
        try {
            var session = getSessionFactory().openSession();
            var result = session.createNativeQuery("SELECT 1", Integer.class).getSingleResult();
            session.close();

            System.out.println("✅ [Hibernate] Teste de conexão bem-sucedido: " + result);
            return result == 1;

        } catch (Exception e) {
            System.err.println("❌ [Hibernate] Teste de conexão falhou: " + e.getMessage());
            return false;
        }
    }
}

//package com.okutonda.okudpdv.data.config;
//
//import com.okutonda.okudpdv.data.connection.ConnectionConfig;
//import org.hibernate.SessionFactory;
//import org.hibernate.cfg.Configuration;
//import org.hibernate.boot.registry.StandardServiceRegistry;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
//
//import java.util.Map;
//
//public class HibernateConfig {
//
//    private static SessionFactory sessionFactory;
//
//    static {
//        initializeSessionFactory();
//    }
//
//    private static void initializeSessionFactory() {
//        try {
//            System.out.println("🔄 [Hibernate] Iniciando configuração...");
//            
//            // Configuração programática
//            Configuration configuration = new Configuration();
//            
//            // Aplicar configurações
//            applyHibernateSettings(configuration);
//            
//            // Registrar entidades
//            registerEntities(configuration);
//            
//            // Construir SessionFactory
//            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
//                .applySettings(configuration.getProperties())
//                .build();
//            
//            sessionFactory = configuration.buildSessionFactory(registry);
//            
//            System.out.println("✅ [Hibernate] SessionFactory criada com sucesso!");
//            
//        } catch (Exception e) {
//            System.err.println("❌ [Hibernate] Erro ao inicializar SessionFactory: " + e.getMessage());
//            e.printStackTrace();
//            throw new ExceptionInInitializerError(e);
//        }
//    }
//
//    private static void applyHibernateSettings(Configuration configuration) {
//        // Configurações básicas do banco (usando suas configurações existentes)
//        configuration.setProperty("hibernate.connection.url", ConnectionConfig.getJdbcUrl());
//        configuration.setProperty("hibernate.connection.username", ConnectionConfig.getUser());
//        configuration.setProperty("hibernate.connection.password", ConnectionConfig.getPassword());
//        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
//        
//        // Dialeto do MySQL
//        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
//        
//        // 🔥 INTEGRAÇÃO COM SEU HIKARICP EXISTENTE
//        configuration.setProperty("hibernate.hikari.maximumPoolSize", "10");
//        configuration.setProperty("hibernate.hikari.minimumIdle", "2");
//        configuration.setProperty("hibernate.hikari.idleTimeout", "30000");
//        configuration.setProperty("hibernate.hikari.connectionTimeout", "10000");
//        configuration.setProperty("hibernate.hikari.maxLifetime", "1800000");
//        
//        // SQL logging (desenvolvimento)
//        configuration.setProperty("hibernate.show_sql", "true");
//        configuration.setProperty("hibernate.format_sql", "true");
//        configuration.setProperty("hibernate.highlight_sql", "true");
//        
//        // Estratégia DDL - validate para produção
//        configuration.setProperty("hibernate.hbm2ddl.auto", "validate");
//        
//        // Codificação
//        configuration.setProperty("hibernate.connection.characterEncoding", "UTF-8");
//        configuration.setProperty("hibernate.connection.useUnicode", "true");
//        
//        // Performance
//        configuration.setProperty("hibernate.jdbc.batch_size", "20");
//        configuration.setProperty("hibernate.order_inserts", "true");
//        configuration.setProperty("hibernate.order_updates", "true");
//        
//        System.out.println("✅ [Hibernate] Configurações aplicadas");
//    }
//
//    private static void registerEntities(Configuration configuration) {
//        try {
//            // Adicione aqui TODAS as suas entidades
//            // configuration.addAnnotatedClass(com.okutonda.okudpdv.data.entities.User.class);
//            // configuration.addAnnotatedClass(com.okutonda.okudpdv.data.entities.Product.class);
//            
//            // Por enquanto, vamos registrar uma entidade de teste
//            // configuration.addAnnotatedClass(com.okutonda.okudpdv.data.entities.TestEntity.class);
//            
//            System.out.println("✅ [Hibernate] Entidades registradas");
//            
//        } catch (Exception e) {
//            System.err.println("❌ [Hibernate] Erro ao registrar entidades: " + e.getMessage());
//            throw e;
//        }
//    }
//
//    public static SessionFactory getSessionFactory() {
//        if (sessionFactory == null || sessionFactory.isClosed()) {
//            throw new IllegalStateException("SessionFactory não está disponível");
//        }
//        return sessionFactory;
//    }
//
//    public static void shutdown() {
//        if (sessionFactory != null && !sessionFactory.isClosed()) {
//            sessionFactory.close();
//            System.out.println("✅ [Hibernate] SessionFactory fechada.");
//        }
//    }
//    
//    // Teste de conexão integrado com seu sistema atual
//    public static boolean testConnection() {
//        try {
//            var session = getSessionFactory().openSession();
//            var result = session.createNativeQuery("SELECT 1", Integer.class).getSingleResult();
//            session.close();
//            
//            System.out.println("✅ [Hibernate] Teste de conexão bem-sucedido: " + result);
//            return result == 1;
//            
//        } catch (Exception e) {
//            System.err.println("❌ [Hibernate] Teste de conexão falhou: " + e.getMessage());
//            return false;
//        }
//    }
//}
