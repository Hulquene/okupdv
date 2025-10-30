///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.okutonda.okudpdv.jdbc;
//
//import java.sql.*;
//
///**
// *
// * @author hr
// */
//public class DatabaseBootProbe {
//
//    public enum AppState {
//        DB_UNREACHABLE, // servidor/porta inativos, firewall ou host/porta errados
//        DB_NOT_FOUND, // servidor ok, mas a BD (schema) com o nome não existe
//        SCHEMA_MISSING, // BD existe mas tabelas-chave não existem
//        INSTALL_INCOMPLETE, // existe app_meta, mas install_complete != "true"
//        READY                // tudo ok para arrancar o app
//    }
//
//    public static AppState detect() {
//        // Lê envs (usa os mesmos helpers da ConnectionDatabase)
//        String host = ConnectionDatabase.envOr("oku_host", "127.0.0.1");
//        String port = ConnectionDatabase.envOr("oku_port", "3306");
//        String db = ConnectionDatabase.envOr("oku_database", null);
//        String user = ConnectionDatabase.envOr("oku_username", null);
//        String pass = System.getenv("oku_password");
//
//        // 1) Conecta ao servidor SEM selecionar BD (para não falhar se a BD não existir)
//        String urlServer = "jdbc:mysql://" + host + ":" + port + "/"
//                + "?useUnicode=true&characterEncoding=UTF-8"
//                + "&useSSL=false&allowPublicKeyRetrieval=true"
//                + "&serverTimezone=UTC&connectTimeout=5000&socketTimeout=5000";
//
//        try (Connection cServer = DriverManager.getConnection(urlServer, user, pass != null ? pass : "")) {
//            // 2) Verifica se a BD (schema) existe
//            if (!schemaExists(cServer, db)) {
//                return AppState.DB_NOT_FOUND;
//            }
//
//            // 3) Conecta na BD específica
//            String urlDb = "jdbc:mysql://" + host + ":" + port + "/" + db
//                    + "?useUnicode=true&characterEncoding=UTF-8"
//                    + "&useSSL=false&allowPublicKeyRetrieval=true"
//                    + "&serverTimezone=UTC&connectTimeout=5000&socketTimeout=5000";
//
//            try (Connection cDb = DriverManager.getConnection(urlDb, user, pass != null ? pass : "")) {
//                // 4) Verifica tabelas-chave do teu sistema
//                if (!tableExists(cDb, db, "options")) {
//                    return AppState.SCHEMA_MISSING;
//                }
//                // adiciona aqui outras tabelas mínimas se quiseres
//                // if (!tableExists(cDb, db, "users")) return AppState.SCHEMA_MISSING;
//
//                // 5) Verifica flag de instalação
//                String installed = getMeta(cDb, "install_complete");
//                if (!"true".equalsIgnoreCase(installed)) {
//                    return AppState.INSTALL_INCOMPLETE;
//                }
//
//                return AppState.READY;
//            }
//
//        } catch (SQLException e) {
//            String state = e.getSQLState();
//            if (state != null && state.startsWith("08")) {
//                // 08S01 = communications link failure
//                return AppState.DB_UNREACHABLE;
//            }
//            // Sem SQLState ou outro motivo de acesso – considera unreachable
//            return AppState.DB_UNREACHABLE;
//        }
//    }
//
//    private static boolean schemaExists(Connection cServer, String schemaName) throws SQLException {
//        // Requer permissão de leitura no information_schema
//        try (PreparedStatement ps = cServer.prepareStatement(
//                "SELECT 1 FROM information_schema.SCHEMATA WHERE SCHEMA_NAME = ?")) {
//            ps.setString(1, schemaName);
//            try (ResultSet rs = ps.executeQuery()) {
//                return rs.next();
//            }
//        }
//    }
//
//    private static boolean tableExists(Connection c, String schema, String table) throws SQLException {
//        try (PreparedStatement ps = c.prepareStatement(
//                "SELECT 1 FROM information_schema.TABLES WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?")) {
//            ps.setString(1, schema);
//            ps.setString(2, table);
//            try (ResultSet rs = ps.executeQuery()) {
//                return rs.next();
//            }
//        }
//    }
//
//    private static String getMeta(Connection c, String key) throws SQLException {
//        try (PreparedStatement ps = c.prepareStatement(
//                "SELECT value FROM options WHERE `name` = ? LIMIT 1")) {
//            ps.setString(1, key);
//            try (ResultSet rs = ps.executeQuery()) {
//                return rs.next() ? rs.getString(1) : null;
//            }
//        } catch (SQLException e) {
//            // se app_meta não existir, trata como SCHEMA_MISSING ao nível superior
//            return null;
//        }
//    }
//}
