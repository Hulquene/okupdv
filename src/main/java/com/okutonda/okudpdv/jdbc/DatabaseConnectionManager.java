///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
// */
//package com.okutonda.okudpdv.jdbc;
//
//import com.okutonda.okudpdv.jdbc.connect.DatabaseConnection;
//import java.sql.Connection;
//import java.sql.SQLException;
//
///**
// *
// * @author kenny
// */
//public class DatabaseConnectionManager {
//
//    private DatabaseConnection connection;
//
//    public void setConnection(DatabaseConnection connection) {
//        this.connection = connection;
//    }
//
//    public Connection connect() throws SQLException, ClassNotFoundException {
//        if (connection == null) {
//            throw new IllegalStateException("Conexão não inicializada.");
//        }
//        return connection.connect();
//    }
//
//    public void disconnect() {
//        if (connection != null) {
//            connection.disconnect();
//        }
//    }
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        // TODO code application logic here
//        try {
//            DatabaseConnectionManager manager = new DatabaseConnectionManager();
//            
//            // Conexão com MySQL
//            manager.setConnection(new MySQLConnection());
//            Connection mysqlConn = manager.connect();
//            System.out.println("Conectado ao MySQL com sucesso!");
//            manager.disconnect();
//
//            // Conexão com SQLite
//            manager.setConnection(new SQLiteConnection());
//            Connection sqliteConn = manager.connect();
//            System.out.println("Conectado ao SQLite com sucesso!");
//            manager.disconnect();
//
//
//
//
//
//        } catch (SQLException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
////        try {
////            DatabaseConnectionManager manager = new DatabaseConnectionManager();
////
////            // Conexão com SQLite
////            manager.setConnection(new SQLiteConnection());
////
////            // Conectar ao banco de dados
////            manager.connect();
////            System.out.println("Conectado ao SQLite com sucesso!");
////
////            // Executar o arquivo SQL
//////            if (manager.getConnection() instanceof SQLiteConnection) {
//////                ((SQLiteConnection) manager.getConnection()).create();
//////            }
////
////            // Desconectar do banco de dados
////            manager.disconnect();
////
////        } catch (SQLException | ClassNotFoundException e) {
////            e.printStackTrace();
////        }
//    }
//
//    public Connection getConnection() {
//        return connection != null ? connection.getConnection() : null;
//    }
//}
