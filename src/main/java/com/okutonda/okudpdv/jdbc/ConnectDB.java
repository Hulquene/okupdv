/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author kenny
 */
public class ConnectDB {

//    private static final String url = "jdbc:sqlite:okudpdv.db";
//    final private String url = "jdbc:mysql://localhost:3306/okudpdv";
//    final private String username = "root";
//    final private String password = "";
    //CONEXAO REMOTA
//    final private String url = "jdbc:mysql://172.20.83.71:3306/okudpdv";
//    final private String username = "kenny";
//    final private String password = "1234567890";

//    public Connection connect() {
//        try {
//
//   // Carregar o driver JDBC do SQLite
//            Class.forName("org.sqlite.JDBC");
////           
//            return DriverManager.getConnection(url);
//        } catch (SQLException e) {
//            System.out.println("Error connect database" + e.getMessage());
//            JOptionPane.showMessageDialog(null, "Error connect database: " + e.getMessage());
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//    public Connection connect() {
//        try {
//            return DriverManager.getConnection(url, username, password);
//        } catch (SQLException e) {
//            System.out.println("Error connect database" + e.getMessage());
//            JOptionPane.showMessageDialog(null, "Error connect database: " + e.getMessage());
//        }
//        return null;
//    }

    public Boolean createDatabaseSQLite() {
        String dbResourcePath = "database/seu_database.db";

        // Diretório oculto do usuário
//        String userHome = System.getProperty("user.home");
        Path dbFilePath = Paths.get("", "okudpdv.db");

        // Copiar o banco de dados do diretório de recursos para a pasta do usuário
        ClassLoader classLoader = ConnectDB.class.getClassLoader();
        try (InputStream dbStream = classLoader.getResourceAsStream(dbResourcePath)) {
            if (dbStream == null) {
                throw new IllegalArgumentException("Ficheiro de banco de dados não encontrado: " + dbResourcePath);
//                JOptionPane.showMessageDialog(null, "Ficheiro de banco de dados não encontrado:");
            }
            Files.copy(dbStream, dbFilePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Banco de dados copiado com sucesso para: " + dbFilePath);
            JOptionPane.showMessageDialog(null, "Banco de dados copiado com sucesso para: " + dbFilePath);
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao copiar o banco de dados para: " + dbFilePath);
            JOptionPane.showMessageDialog(null, "Erro ao copiar o banco de dados para: " + dbFilePath);
            e.printStackTrace();
        }
        return false;
    }
}
