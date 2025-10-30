//package com.okutonda.okudpdv.jdbc;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.Properties;
//import javax.swing.JOptionPane;
//
//public class ConnectionDatabase {
//
//    private static volatile Connection connection;
//
//    static {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            JOptionPane.showMessageDialog(null,
//                    "Driver MySQL não encontrado (mysql-connector-j).\n" + e.getMessage(),
//                    "Erro", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    public static synchronized Connection getConnect() {
//        if (connection != null) {
//            try {
//                if (!connection.isClosed() && connection.isValid(3)) {
//                    return connection;
//                }
//            } catch (SQLException ignore) {
//            }
//            safeClose(connection);
//            connection = null;
//        }
//
//        String host = envOr("oku_host", "127.0.0.1");
//        String port = envOr("oku_port", "3306");
//        String db = envOr("oku_database", null);
//        String user = envOr("oku_username", null);
//        String pass = System.getenv("oku_password"); // pode ser null/vazia
//
//        StringBuilder missing = new StringBuilder();
//        if (isBlank(host)) {
//            missing.append("oku_host ");
//        }
//        if (isBlank(port)) {
//            missing.append("oku_port ");
//        }
//        if (isBlank(db)) {
//            missing.append("oku_database ");
//        }
//        if (isBlank(user)) {
//            missing.append("oku_username ");
//        }
//
//        if (missing.length() > 0) {
//            JOptionPane.showMessageDialog(null,
//                    "Variáveis de ambiente em falta: " + missing.toString().trim(),
//                    "Atenção", JOptionPane.ERROR_MESSAGE);
//            return null;
//        }
//
//        final String url = "jdbc:mysql://" + host + ":" + port + "/" + db
//                + "?useUnicode=true&characterEncoding=UTF-8"
//                + "&useSSL=false&allowPublicKeyRetrieval=true"
//                + "&serverTimezone=UTC"
//                + "&connectTimeout=5000&socketTimeout=5000";
//
//        Properties props = new Properties();
//        props.setProperty("user", user);
//        props.setProperty("password", pass != null ? pass : "");
//
//        int attempts = 0;
//        SQLException last = null;
//
//        while (attempts < 3) {
//            attempts++;
//            try {
//                System.out.println("[DB] A ligar (" + attempts + "/3): " + url + " user=" + user);
//                Connection c = DriverManager.getConnection(url, props);
//                if (c.isValid(3)) {
//                    System.out.println("[DB] Ligado com sucesso!");
//                    connection = c;
//                    return connection;
//                }
//                safeClose(c);
//                System.err.println("[DB] Conexão obtida mas inválida.");
//            } catch (SQLException e) {
//                last = e;
//                String sqlState = e.getSQLState();
//                if (sqlState != null) {
//                    if (sqlState.startsWith("08")) {
//                        System.err.println("[DB] Falha de comunicação/servidor indisponível: " + e.getMessage());
//                    } else if ("28000".equals(sqlState)) {
//                        System.err.println("[DB] Acesso negado (credenciais/host): " + e.getMessage());
//                        break;
//                    }
//                } else {
//                    System.err.println("[DB] Erro JDBC: " + e.getMessage());
//                }
//                try {
//                    Thread.sleep(600);
//                } catch (InterruptedException ignored) {
//                }
//            }
//        }
//
//        if (last != null) {
//            logSQLException(last);
//            JOptionPane.showMessageDialog(null,
//                    "Erro ao ligar à base de dados:\n" + last.getMessage(),
//                    "Erro de ligação", JOptionPane.ERROR_MESSAGE);
//        }
//        return null;
//    }
//
//    public static synchronized void disconnect() {
//        safeClose(connection);
//        connection = null;
//    }
//
//    static String envOr(String key, String def) {
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
//
//
//
//
//
//
//
////
////package com.okutonda.okudpdv.jdbc;
////
////import java.sql.Connection;
////import java.sql.DriverManager;
////import java.sql.SQLException;
////import java.util.Properties;
////import javax.swing.JOptionPane;
////
////public class ConnectionDatabase {
////
////    // 🔹 Conexão global (para compatibilidade)
////    private static volatile Connection connection;
////
////    static {
////        try {
////            Class.forName("com.mysql.cj.jdbc.Driver");
////        } catch (ClassNotFoundException e) {
////            JOptionPane.showMessageDialog(null,
////                    "Driver MySQL não encontrado (mysql-connector-j).\n" + e.getMessage(),
////                    "Erro", JOptionPane.ERROR_MESSAGE);
////        }
////    }
////
////    // ============================================================
////    // 🔹 1️⃣ Modo compatível (usado em partes antigas da app)
////    // ============================================================
////    public static synchronized Connection getConnect() {
////        try {
////            if (connection != null && !connection.isClosed() && connection.isValid(3)) {
////                return connection;
////            }
////        } catch (SQLException ignore) {
////        }
////        safeClose(connection);
////        connection = createNewConnection(true);
////        return connection;
////    }
////
////    // ============================================================
////    // 🔹 2️⃣ Novo modo transacional (para commits e rollbacks)
////    // ============================================================
////    public static Connection getNewConnection() {
////        return createNewConnection(false);
////    }
////
////    // ============================================================
////    // 🔹 3️⃣ Fecha manualmente a conexão global (se necessário)
////    // ============================================================
////    public static synchronized void disconnect() {
////        safeClose(connection);
////        connection = null;
////    }
////
////    // ============================================================
////    // 🔹 4️⃣ Função central de criação de conexão
////    // ============================================================
////    private static Connection createNewConnection(boolean isGlobal) {
////        String host = envOr("oku_host", "127.0.0.1");
////        String port = envOr("oku_port", "3306");
////        String db = envOr("oku_database", "okutonda");
////        String user = envOr("oku_username", "root");
////        String pass = System.getenv("oku_password");
////
////        final String url = "jdbc:mysql://" + host + ":" + port + "/" + db
////                + "?useUnicode=true&characterEncoding=UTF-8"
////                + "&useSSL=false&allowPublicKeyRetrieval=true"
////                + "&serverTimezone=UTC"
////                + "&connectTimeout=5000&socketTimeout=5000";
////
////        Properties props = new Properties();
////        props.setProperty("user", user);
////        props.setProperty("password", pass != null ? pass : "");
////
////        try {
////            Connection c = DriverManager.getConnection(url, props);
////
////            // ⚠️ A conexão transacional começa SEM autocommit
//////            c.setAutoCommit(isGlobal); // global = true → mantém autocommit
////              c.setAutoCommit(isGlobal ? true : false);
////            System.out.println("[DB] " + (isGlobal ? "Conexão global" : "Nova conexão transacional") + " estabelecida!");
////            return c;
////
////        } catch (SQLException e) {
////            JOptionPane.showMessageDialog(null,
////                    "Erro ao conectar ao banco de dados:\n" + e.getMessage(),
////                    "Erro de ligação", JOptionPane.ERROR_MESSAGE);
////            throw new RuntimeException("Falha ao conectar ao banco de dados", e);
////        }
////    }
////
////    // ============================================================
////    // 🔹 5️⃣ Utilitários auxiliares
////    // ============================================================
////    static String envOr(String key, String def) {
////        String v = System.getenv(key);
////        v = (v == null) ? null : v.trim();
////        return isBlank(v) ? def : v;
////    }
////
////    private static boolean isBlank(String s) {
////        return s == null || s.trim().isEmpty();
////    }
////
////    private static void safeClose(AutoCloseable c) {
////        try {
////            if (c != null) {
////                c.close();
////            }
////        } catch (Exception ignore) {
////        }
////    }
////}
