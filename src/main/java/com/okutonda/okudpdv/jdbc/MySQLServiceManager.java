/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author kenny
 */
public class MySQLServiceManager {

    // Função para criar uma base de dados MySQL
    public static Boolean createDatabaseMySQL() {

        String dbHost = System.getenv("oku_host");
        String dbUsername = System.getenv("oku_username");
        String dbPassword = ""; //System.getenv("oku_password");
        String dbPort = System.getenv("oku_port");
        String dbDatabase = System.getenv("oku_database");
        final String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/";

//        String url = "jdbc:mysql://localhost:3306/";
//        String user = "root"; // Substitua pelo seu usuário
//        String password = ""; // Substitua pela sua senha
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword); Statement statement = conn.createStatement()) {

            String sql = "CREATE DATABASE IF NOT EXISTS " + dbDatabase;
            statement.executeUpdate(sql);
            System.out.println("Base de dados " + dbDatabase + " criada com sucesso!");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean executeSqlFile(Connection conn) {
        final String sqlFilePath = "database/script.sql";
        if (conn == null) {
            System.err.println("A conexão com o banco de dados não pode ser nula.");
            return false;
        }

        try (InputStream in = MySQLServiceManager.class.getClassLoader().getResourceAsStream(sqlFilePath)) {
            if (in == null) {
                System.err.println("Arquivo SQL não encontrado no classpath: " + sqlFilePath);
                return false;
            }

            // Leia SEMPRE em UTF-8
            try (Reader reader = new InputStreamReader(in, java.nio.charset.StandardCharsets.UTF_8)) {
                StringBuilder stmt = new StringBuilder();

                boolean inSingleQuote = false; // '
                boolean inDoubleQuote = false; // " (raro em MySQL, exceto ANSI_QUOTES)
                boolean inBacktick = false; // `
                boolean inLineComment = false; // -- ou #
                boolean inBlockComment = false; // /* ... */

                int prev = -1;
                int ch;
                while ((ch = reader.read()) != -1) {
                    char c = (char) ch;
                    char p = (char) prev;

                    // Comentário de linha ativo?
                    if (inLineComment) {
                        if (c == '\n' || c == '\r') {
                            inLineComment = false;
                        }
                        prev = ch;
                        continue;
                    }

                    // Comentário de bloco ativo?
                    if (inBlockComment) {
                        if (p == '*' && c == '/') {
                            inBlockComment = false;
                        }
                        prev = ch;
                        continue;
                    }

                    // Detectar início de comentários (quando não está dentro de aspas/crase)
                    if (!inSingleQuote && !inDoubleQuote && !inBacktick) {
                        // "--" comentário de linha
                        if (p == '-' && c == '-') {
                            // remove o '-' anterior já escrito
                            if (stmt.length() > 0) {
                                stmt.setLength(stmt.length() - 1);
                            }
                            inLineComment = true;
                            prev = ch;
                            continue;
                        }
                        // "#"
                        if (c == '#') {
                            inLineComment = true;
                            prev = ch;
                            continue;
                        }
                        // "/* ... */" comentário de bloco
                        if (p == '/' && c == '*') {
                            if (stmt.length() > 0) {
                                stmt.setLength(stmt.length() - 1); // remove a '/'
                            }
                            inBlockComment = true;
                            prev = ch;
                            continue;
                        }
                    }

                    // Troca de estados de citação
                    if (!inDoubleQuote && !inBacktick && c == '\'') {
                        // alterna ' se não for escape \'
                        if (!(inSingleQuote && p == '\\')) {
                            inSingleQuote = !inSingleQuote;
                        }
                    } else if (!inSingleQuote && !inBacktick && c == '"') {
                        // opcional (ANSI_QUOTES). Não atrapalha.
                        if (!(inDoubleQuote && p == '\\')) {
                            inDoubleQuote = !inDoubleQuote;
                        }
                    } else if (!inSingleQuote && !inDoubleQuote && c == '`') {
                        inBacktick = !inBacktick;
                    }

                    // Fechamento de statement: ';' somente fora de aspas/crase/comentário
                    if (c == ';' && !inSingleQuote && !inDoubleQuote && !inBacktick) {
                        String sql = stmt.toString().trim();
                        if (!sql.isEmpty()) {
                            try (Statement s = conn.createStatement()) {
                                s.execute(sql);
                            } catch (SQLException e) {
                                System.err.println("❌ Erro ao executar SQL:\n" + sql);
                                e.printStackTrace();
                            }
                        }
                        stmt.setLength(0);
                    } else {
                        stmt.append(c);
                    }

                    prev = ch;
                }

                // Último statement (sem ';' final)
                String rest = stmt.toString().trim();
                if (!rest.isEmpty()) {
                    try (Statement s = conn.createStatement()) {
                        s.execute(rest);
                    } catch (SQLException e) {
                        System.err.println("❌ Erro ao executar SQL (final):\n" + rest);
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("Arquivo SQL executado com sucesso.");
            return true;

        } catch (IOException e) {
            System.err.println("Erro ao processar o arquivo SQL.");
            e.printStackTrace();
            return false;
        }
    }

    // Método para executar o arquivo SQL usando uma conexão existente
//    public static Boolean executeSqlFile(Connection conn) {
//        String sqlFilePath = "database/script.sql";
//        // Verificar se a conexão é válida
//        if (conn == null) {
//            System.err.println("A conexão com o banco de dados não pode ser nula.");
//            return false;
//        }
//
//        // Carregar o arquivo SQL a partir do classpath
//        try (InputStream inputStream = MySQLServiceManager.class.getClassLoader().getResourceAsStream(sqlFilePath); BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
//
//            if (inputStream == null) {
//                System.err.println("Arquivo SQL não encontrado no classpath: " + sqlFilePath);
//                return false;
//            }
//
//            StringBuilder sql = new StringBuilder();
//            String line;
//
//            // Ler o arquivo SQL linha por linha e acumular comandos SQL
////            while ((line = br.readLine()) != null) {
////                sql.append(line).append("\n");
////
////                // Executar comandos SQL separados por ponto e vírgula
////                if (line.trim().endsWith(";")) {
////                    try (Statement stmt = conn.createStatement()) {
////                        stmt.execute(sql.toString());
////                        sql.setLength(0); // Limpar o buffer após execução
////                    } catch (SQLException e) {
////                        System.err.println("Erro ao executar comandos SQL.");
////                        e.printStackTrace();
////                    }
////                }
////            }
//            while ((line = br.readLine()) != null) {
//                line = line.trim();
//
//                // Ignorar linhas de comentário ou vazias
//                if (line.startsWith("--") || line.startsWith("/*") || line.startsWith("*") || line.isEmpty()) {
//                    continue;
//                }
//
//                // Remover comentários inline (tudo depois de "--" ou "#")
//                if (line.contains("--")) {
//                    line = line.substring(0, line.indexOf("--")).trim();
//                }
//                if (line.contains("#")) {
//                    line = line.substring(0, line.indexOf("#")).trim();
//                }
//
//                // Remover comentários em blocos (/* ... */) dentro da linha
//                line = line.replaceAll("/\\*.*?\\*/", "").trim();
//
//                if (line.isEmpty()) {
//                    continue;
//                }
//
//                sql.append(line).append(" ");
//
//                if (line.endsWith(";")) {
//                    String[] comandos = sql.toString().split(";");
//                    for (String comando : comandos) {
//                        String c = comando.trim();
//                        if (!c.isEmpty()) {
//                            try (Statement stmt = conn.createStatement()) {
//                                stmt.execute(c);
//                            } catch (SQLException e) {
//                                System.err.println("❌ Erro ao executar SQL:\n" + c);
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                    sql.setLength(0); // limpar buffer
//                }
//            }
//
//            // Executar qualquer comando SQL restante
//            if (sql.length() > 0) {
//                try (Statement stmt = conn.createStatement()) {
//                    stmt.execute(sql.toString());
//                    System.out.println("Arquivo SQL executado com sucesso.");
//                    return true;
//                } catch (SQLException e) {
//                    System.err.println("Erro ao executar comandos SQL.");
//                    e.printStackTrace();
//                }
//            }
//
//        } catch (IOException e) {
//            System.err.println("Erro ao processar o arquivo SQL.");
//            e.printStackTrace();
//        }
//        return false;
//    }
}
