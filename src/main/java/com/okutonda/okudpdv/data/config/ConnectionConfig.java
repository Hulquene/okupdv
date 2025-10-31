/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.config;

/**
 * Centraliza a configuração do banco de dados (variáveis de ambiente, host,
 * porta, etc.)
 *
 * @author Hulquene
 */
public class ConnectionConfig {

    private ConnectionConfig() {
    }

    public static String getHost() {
        return envOr("oku_host", "127.0.0.1");
    }

    public static String getPort() {
        return envOr("oku_port", "3306");
    }

    public static String getDatabase() {
        return envOr("oku_database", "okudpdv");
    }

    public static String getUser() {
        return envOr("oku_username", "root");
    }

    public static String getPassword() {
        String pass = System.getenv("oku_password");
        return pass != null ? pass.trim() : "";
    }

    public static String getJdbcUrl() {
        return "jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabase()
                + "?useUnicode=true&characterEncoding=UTF-8"
                + "&useSSL=false&allowPublicKeyRetrieval=true"
                + "&serverTimezone=UTC"
                + "&connectTimeout=5000&socketTimeout=5000";
    }

    private static String envOr(String key, String def) {
        String v = System.getenv(key);
        return (v == null || v.trim().isEmpty()) ? def : v.trim();
    }
}
