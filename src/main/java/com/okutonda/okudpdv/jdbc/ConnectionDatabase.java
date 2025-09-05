///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.okutonda.okudpdv.jdbc;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import javax.swing.JOptionPane;
//
///**
// *
// * @author kenny
// */
//public class ConnectionDatabase {
//
//    public static Connection connection;
//
//    // Método para criar ou recuperar uma conexão com o banco de dados
//    public static Connection getConnect() {
////        final String url = "jdbc:mysql://localhost:3306/okudpdv";
////        final String url = "jdbc:mysql://localhost:3306/oku";
//
//        String dbHost = System.getenv("oku_host");
//        String dbUsername = System.getenv("oku_username");
//        String dbPassword = "";//System.getenv("oku_password");
//        String dbPort = System.getenv("oku_port");
//        String dbDatabase = System.getenv("oku_database");
//        final String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbDatabase;
////        final String username = "root";
////        final String password = "";
//
//        try {
//            if (dbUsername == null || dbPassword == null || dbHost == null || dbPort == null) {
//                JOptionPane.showMessageDialog(null, "Uma ou mais variáveis de ambiente não estão definidas.", "Atenção", JOptionPane.ERROR_MESSAGE);
////                System.err.println("Uma ou mais variáveis de ambiente não estão definidas.");
//            } else if (dbDatabase == null) {
//                JOptionPane.showMessageDialog(null, "Banco de dados, a variáveis de ambiente não esta definida.", "Atenção", JOptionPane.ERROR_MESSAGE);
//            } else {
////                System.out.println("DB_USER: " + dbUsername);
////                System.out.println("DB_PASSWORD: " + dbPassword);
////                System.out.println("DB_NAME: " + dbDatabase);
//
//                if (connection == null) {
//                    connection = DriverManager.getConnection(url, dbUsername, dbPassword);
//                    System.out.println("Criou conexao, Conectado!!");
//                    return connection;
//                }
////            System.out.println("Recuperou a conexao");
//                return connection;
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Error connect database" + e.getMessage());
////            JOptionPane.showMessageDialog(null, "Error connect database: " + e.getMessage());
//        }
//        return null;
//    }
//
//    // Método para fechar a conexão com o banco de dados
//    public void disconnect() {
//        if (connection != null) {
//            try {
//                connection.close();
//            } catch (SQLException e) {
//                JOptionPane.showMessageDialog(null, "Error close connected database: " + e.getMessage());
//            }
//        }
//    }
//
//    private static String envOr(String key, String def) {
//        String v = System.getenv(key);
//        v = (v == null) ? null : v.trim();
//        if (isBlank(v)) {
//            return def;
//        }
//        return v;
//    }
//
//    private static boolean isBlank(String s) {
//        return s == null || s.trim().isEmpty();
//    }
//
//    private static void safeClose(AutoCloseable c) {
//        try {
//            if (c != null) {
//                c.close();
//            }
//        } catch (Exception ignore) {
//        }
//    }
//
//    private static void logSQLException(SQLException e) {
//        SQLException curr = e;
//        while (curr != null) {
//            System.err.println("  Message   : " + curr.getMessage());
//            System.err.println("  SQLState  : " + curr.getSQLState());
//            System.err.println("  ErrorCode : " + curr.getErrorCode());
//            curr = curr.getNextException();
//        }
//        e.printStackTrace(System.err);
//    }
//}
package com.okutonda.okudpdv.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;

public class ConnectionDatabase {

    private static volatile Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Driver MySQL não encontrado (mysql-connector-j).\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static synchronized Connection getConnect() {
        if (connection != null) {
            try {
                if (!connection.isClosed() && connection.isValid(3)) {
                    return connection;
                }
            } catch (SQLException ignore) {
            }
            safeClose(connection);
            connection = null;
        }

        String host = envOr("oku_host", "127.0.0.1");
        String port = envOr("oku_port", "3306");
        String db = envOr("oku_database", null);
        String user = envOr("oku_username", null);
        String pass = System.getenv("oku_password"); // pode ser null/vazia

        StringBuilder missing = new StringBuilder();
        if (isBlank(host)) {
            missing.append("oku_host ");
        }
        if (isBlank(port)) {
            missing.append("oku_port ");
        }
        if (isBlank(db)) {
            missing.append("oku_database ");
        }
        if (isBlank(user)) {
            missing.append("oku_username ");
        }

        if (missing.length() > 0) {
            JOptionPane.showMessageDialog(null,
                    "Variáveis de ambiente em falta: " + missing.toString().trim(),
                    "Atenção", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        final String url = "jdbc:mysql://" + host + ":" + port + "/" + db
                + "?useUnicode=true&characterEncoding=UTF-8"
                + "&useSSL=false&allowPublicKeyRetrieval=true"
                + "&serverTimezone=UTC"
                + "&connectTimeout=5000&socketTimeout=5000";

        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", pass != null ? pass : "");

        int attempts = 0;
        SQLException last = null;

        while (attempts < 3) {
            attempts++;
            try {
                System.out.println("[DB] A ligar (" + attempts + "/3): " + url + " user=" + user);
                Connection c = DriverManager.getConnection(url, props);
                if (c.isValid(3)) {
                    System.out.println("[DB] Ligado com sucesso!");
                    connection = c;
                    return connection;
                }
                safeClose(c);
                System.err.println("[DB] Conexão obtida mas inválida.");
            } catch (SQLException e) {
                last = e;
                String sqlState = e.getSQLState();
                if (sqlState != null) {
                    if (sqlState.startsWith("08")) {
                        System.err.println("[DB] Falha de comunicação/servidor indisponível: " + e.getMessage());
                    } else if ("28000".equals(sqlState)) {
                        System.err.println("[DB] Acesso negado (credenciais/host): " + e.getMessage());
                        break;
                    }
                } else {
                    System.err.println("[DB] Erro JDBC: " + e.getMessage());
                }
                try {
                    Thread.sleep(600);
                } catch (InterruptedException ignored) {
                }
            }
        }

        if (last != null) {
            logSQLException(last);
            JOptionPane.showMessageDialog(null,
                    "Erro ao ligar à base de dados:\n" + last.getMessage(),
                    "Erro de ligação", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public static synchronized void disconnect() {
        safeClose(connection);
        connection = null;
    }

    static String envOr(String key, String def) {
        String v = System.getenv(key);
        v = (v == null) ? null : v.trim();
        if (isBlank(v)) {
            return def;
        }
        return v;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static void safeClose(AutoCloseable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception ignore) {
        }
    }

    private static void logSQLException(SQLException e) {
        SQLException curr = e;
        while (curr != null) {
            System.err.println("  Message   : " + curr.getMessage());
            System.err.println("  SQLState  : " + curr.getSQLState());
            System.err.println("  ErrorCode : " + curr.getErrorCode());
            curr = curr.getNextException();
        }
        e.printStackTrace(System.err);
    }
}
