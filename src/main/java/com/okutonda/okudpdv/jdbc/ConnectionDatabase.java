/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author kenny
 */
public class ConnectionDatabase {

    public static Connection connection;

    // Método para criar ou recuperar uma conexão com o banco de dados
    public static Connection getConnect() {
//        final String url = "jdbc:mysql://localhost:3306/okudpdv";
//        final String url = "jdbc:mysql://localhost:3306/oku";

        String dbHost = System.getenv("oku_host");
        String dbUsername = System.getenv("oku_username");
        String dbPassword = "";//System.getenv("oku_password");
        String dbPort = System.getenv("oku_port");
        String dbDatabase = System.getenv("oku_database");
        final String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbDatabase;
//        final String username = "root";
//        final String password = "";

        try {
            if (dbUsername == null || dbPassword == null || dbHost == null || dbPort == null) {
                JOptionPane.showMessageDialog(null, "Uma ou mais variáveis de ambiente não estão definidas.", "Atenção", JOptionPane.ERROR_MESSAGE);
//                System.err.println("Uma ou mais variáveis de ambiente não estão definidas.");
            } else if (dbDatabase == null) {
                JOptionPane.showMessageDialog(null, "Banco de dados, a variáveis de ambiente não esta definida.", "Atenção", JOptionPane.ERROR_MESSAGE);
            } else {
//                System.out.println("DB_USER: " + dbUsername);
//                System.out.println("DB_PASSWORD: " + dbPassword);
//                System.out.println("DB_NAME: " + dbDatabase);

                if (connection == null) {
                    connection = DriverManager.getConnection(url, dbUsername, dbPassword);
                    System.out.println("Criou conexao, Conectado!!");
                    return connection;
                }
//            System.out.println("Recuperou a conexao");
                return connection;
            }

        } catch (SQLException e) {
            System.out.println("Error connect database" + e.getMessage());
//            JOptionPane.showMessageDialog(null, "Error connect database: " + e.getMessage());
        }
        return null;
    }

    // Método para fechar a conexão com o banco de dados
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error close connected database: " + e.getMessage());
            }
        }
    }

}
