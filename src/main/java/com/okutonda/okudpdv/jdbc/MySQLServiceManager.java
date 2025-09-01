/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    // Método para executar o arquivo SQL usando uma conexão existente
    public static Boolean executeSqlFile(Connection conn) {
        String sqlFilePath = "database/script.sql";
        // Verificar se a conexão é válida
        if (conn == null) {
            System.err.println("A conexão com o banco de dados não pode ser nula.");
            return false;
        }

        // Carregar o arquivo SQL a partir do classpath
        try (InputStream inputStream = MySQLServiceManager.class.getClassLoader().getResourceAsStream(sqlFilePath); BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            if (inputStream == null) {
                System.err.println("Arquivo SQL não encontrado no classpath: " + sqlFilePath);
                return false;
            }

            StringBuilder sql = new StringBuilder();
            String line;

            // Ler o arquivo SQL linha por linha e acumular comandos SQL
            while ((line = br.readLine()) != null) {
                sql.append(line).append("\n");

                // Executar comandos SQL separados por ponto e vírgula
                if (line.trim().endsWith(";")) {
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute(sql.toString());
                        sql.setLength(0); // Limpar o buffer após execução
                    } catch (SQLException e) {
                        System.err.println("Erro ao executar comandos SQL.");
                        e.printStackTrace();
                    }
                }
            }

            // Executar qualquer comando SQL restante
            if (sql.length() > 0) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(sql.toString());
                    System.out.println("Arquivo SQL executado com sucesso.");
                    return true;
                } catch (SQLException e) {
                    System.err.println("Erro ao executar comandos SQL.");
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            System.err.println("Erro ao processar o arquivo SQL.");
            e.printStackTrace();
        }
        return false;
    }

}
