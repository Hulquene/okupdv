/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.connection;

import java.sql.*;

/**
 * Classe de diagnóstico responsável por verificar se o banco está acessível, se
 * o schema existe e se o sistema está instalado corretamente.
 *
 * @author Hulquene
 */
public class DatabaseBootProbe {

    public enum AppState {
        DB_UNREACHABLE, // servidor inativo ou porta errada
        DB_NOT_FOUND, // servidor ativo mas BD inexistente
        SCHEMA_MISSING, // BD existe, mas tabelas principais ausentes
        INSTALL_INCOMPLETE, // BD incompleta (instalação não finalizada)
        READY             // tudo OK para inicializar o sistema
    }

    private DatabaseBootProbe() {
    }

    public static AppState detect() {
        String urlServer = "jdbc:mysql://" + ConnectionConfig.getHost() + ":" + ConnectionConfig.getPort() + "/"
                + "?useUnicode=true&characterEncoding=UTF-8"
                + "&useSSL=false&allowPublicKeyRetrieval=true"
                + "&serverTimezone=UTC&connectTimeout=5000&socketTimeout=5000";

        try (Connection cServer = DriverManager.getConnection(urlServer, ConnectionConfig.getUser(), ConnectionConfig.getPassword())) {

            if (!schemaExists(cServer, ConnectionConfig.getDatabase())) {
                return AppState.DB_NOT_FOUND;
            }

            // Conecta à BD específica
            try (Connection cDb = DriverManager.getConnection(ConnectionConfig.getJdbcUrl(),
                    ConnectionConfig.getUser(), ConnectionConfig.getPassword())) {

                // Verifica se tabela "options" existe
                if (!tableExists(cDb, ConnectionConfig.getDatabase(), "options")) {
                    return AppState.SCHEMA_MISSING;
                }

                // Verifica flag de instalação
                String installed = getMeta(cDb, "install_complete");
                if (!"true".equalsIgnoreCase(installed)) {
                    return AppState.INSTALL_INCOMPLETE;
                }

                return AppState.READY;
            }

        } catch (SQLException e) {
            String state = e.getSQLState();
            if (state != null && state.startsWith("08")) {
                return AppState.DB_UNREACHABLE;
            }
            return AppState.DB_UNREACHABLE;
        }
    }

    private static boolean schemaExists(Connection conn, String schema) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT 1 FROM information_schema.SCHEMATA WHERE SCHEMA_NAME = ?")) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static boolean tableExists(Connection conn, String schema, String table) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT 1 FROM information_schema.TABLES WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?")) {
            ps.setString(1, schema);
            ps.setString(2, table);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static String getMeta(Connection conn, String key) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT value FROM options WHERE name = ? LIMIT 1")) {
            ps.setString(1, key);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            return null;
        }
    }
}
