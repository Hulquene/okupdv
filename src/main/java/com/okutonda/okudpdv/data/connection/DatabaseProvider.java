/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Classe responsável por fornecer conexões reutilizáveis através de um pool
 * (HikariCP). Substitui o ConnectionDatabase antigo.
 *
 * @author Hulquene
 */
public class DatabaseProvider {

    private static HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(ConnectionConfig.getJdbcUrl());
            config.setUsername(ConnectionConfig.getUser());
            config.setPassword(ConnectionConfig.getPassword());
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");

            // ⚙️ Parâmetros de desempenho
            config.setMaximumPoolSize(10);      // Máx. conexões ativas
            config.setMinimumIdle(2);           // Mín. conexões ociosas
            config.setIdleTimeout(30000);       // Fecha conexões inativas após 30s
            config.setConnectionTimeout(10000); // Tempo máx. para obter conexão
            config.setMaxLifetime(1800000);     // Vida útil máx. (30min)
            config.setConnectionTestQuery("SELECT 1");

            dataSource = new HikariDataSource(config);

            System.out.println("[DB] Pool de conexões inicializado com sucesso!");
        } catch (Exception e) {
            System.err.println("[DB] Erro ao inicializar o pool de conexões: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public DatabaseProvider() {
    }

    /**
     * Obtém uma conexão ativa do pool. O método conn.close() devolve a conexão
     * ao pool (não a encerra fisicamente).
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        // Estatísticas atuais do pool
        int total = dataSource.getHikariPoolMXBean().getTotalConnections();
        int active = dataSource.getHikariPoolMXBean().getActiveConnections();
        int idle = dataSource.getHikariPoolMXBean().getIdleConnections();

        System.out.printf(
                "[DB] Conexão #%d obtida do pool. (Ativas: %d | Ociosas: %d | Total: %d)%n",
                conn.hashCode(), active, idle, total
        );

        return conn;
//        return dataSource.getConnection();
    }

    /**
     * Fecha todas as conexões do pool (usar ao encerrar o sistema).
     */
    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("[DB] Pool encerrado.");
        }
    }
}
