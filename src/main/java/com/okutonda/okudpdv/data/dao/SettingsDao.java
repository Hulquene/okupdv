//package com.okutonda.okudpdv.data.dao;
//
//import com.okutonda.okudpdv.data.connection.DatabaseProvider;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
///**
// * DAO responsável por obter valores da tabela settings.
// *
// * Usa DatabaseProvider (HikariCP) diretamente, sem dependência do
// * ConnectionDatabase. Todas as conexões são encerradas automaticamente
// * (try-with-resources).
// *
// * ⚙️ Tabela esperada: settings(id INT, `key` VARCHAR, `value` TEXT)
// *
// * @author Hulquene
// */
//public class SettingsDao {
//
//    /**
//     * Retorna o valor de uma chave, ou "" caso não exista.
//     */
//    public String getValue(String key) {
//        String sql = "SELECT value FROM settings WHERE `key`=?";
//        try (Connection conn = DatabaseProvider.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setString(1, key);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    return rs.getString("value");
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("[DB] Erro ao buscar setting (" + key + "): " + e.getMessage());
//        }
//        return "";
//    }
//
//    /**
//     * Retorna o valor de uma chave, com valor padrão (fallback).
//     */
//    public String getValue(String key, String defaultValue) {
//        String v = getValue(key);
//        if (v == null) {
//            v = "";
//        }
//        v = v.trim();
//        return v.isEmpty() ? defaultValue : v;
//    }
//
//    /**
//     * Helper tipado: int com fallback.
//     */
//    public int getInt(String key, int defaultValue) {
//        String v = getValue(key);
//        try {
//            return Integer.parseInt(v.trim());
//        } catch (Exception e) {
//            return defaultValue;
//        }
//    }
//
//    /**
//     * Helper tipado: double com fallback.
//     */
//    public double getDouble(String key, double defaultValue) {
//        String v = getValue(key);
//        try {
//            return Double.parseDouble(v.trim().replace(',', '.'));
//        } catch (Exception e) {
//            return defaultValue;
//        }
//    }
//
//    /**
//     * Helper tipado: boolean com fallback. Aceita 1/0, true/false, sim/não,
//     * yes/no.
//     */
//    public boolean getBoolean(String key, boolean defaultValue) {
//        String v = getValue(key);
//        if (v == null) {
//            return defaultValue;
//        }
//
//        v = v.trim().toLowerCase();
//        if (v.equals("1") || v.equals("true") || v.equals("sim") || v.equals("yes")) {
//            return true;
//        }
//        if (v.equals("0") || v.equals("false") || v.equals("não") || v.equals("nao") || v.equals("no")) {
//            return false;
//        }
//
//        return defaultValue;
//    }
//}
//
/////*
//// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
//// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
//// */
////package com.okutonda.okudpdv.data.dao;
////
////import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
////import java.sql.*;
////
/////**
//// *
//// * @author hr
//// */
////public class SettingsDao {
////
////    private final Connection conn;
////
////    public SettingsDao() {
////        this.conn = ConnectionDatabase.getConnect();
////    }
////
////    public String getValue(String key) {
////        String sql = "SELECT value FROM settings WHERE `key`=?";
////        try (PreparedStatement ps = conn.prepareStatement(sql)) {
////            ps.setString(1, key);
////            try (ResultSet rs = ps.executeQuery()) {
////                if (rs.next()) {
////                    return rs.getString(1);
////                }
////            }
////        } catch (SQLException ignore) {
////        }
////        return "";
////    }
////
////    /**
////     * NOVO: com valor padrão. Usa o getValue acima e faz fallback.
////     */
////    public String getValue(String key, String defaultValue) {
////        String v = getValue(key);
////        if (v == null) {
////            v = "";
////        }
////        v = v.trim();
////        return v.isEmpty() ? defaultValue : v;
////    }
////
////    /**
////     * (Opcional) Helper tipado: int com fallback.
////     */
////    public int getInt(String key, int defaultValue) {
////        String v = getValue(key);
////        try {
////            return Integer.parseInt(v.trim());
////        } catch (Exception e) {
////            return defaultValue;
////        }
////    }
////
////    /**
////     * (Opcional) Helper tipado: double com fallback.
////     */
////    public double getDouble(String key, double defaultValue) {
////        String v = getValue(key);
////        try {
////            return Double.parseDouble(v.trim().replace(',', '.'));
////        } catch (Exception e) {
////            return defaultValue;
////        }
////    }
////
////    /**
////     * (Opcional) Helper tipado: boolean com fallback. Aceita 1/0, true/false,
////     * sim/não.
////     */
////    public boolean getBoolean(String key, boolean defaultValue) {
////        String v = getValue(key);
////        if (v == null) {
////            return defaultValue;
////        }
////        v = v.trim().toLowerCase();
////        if (v.equals("1") || v.equals("true") || v.equals("sim") || v.equals("yes")) {
////            return true;
////        }
////        if (v.equals("0") || v.equals("false") || v.equals("não") || v.equals("nao") || v.equals("no")) {
////            return false;
////        }
////        return defaultValue;
////    }
////}
