///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.okutonda.okudpdv.jdbc.connect;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
///**
// *
// * @author kenny
// */
//public abstract class DatabaseConnection {
//
//    protected Connection connection;
//
//    // Método abstrato para carregar o driver específico
//    public abstract void loadDriver() throws ClassNotFoundException;
//
//    // Método para criar uma conexão com o banco de dados
//    public Connection connect() throws SQLException, ClassNotFoundException {
//        if (connection == null || connection.isClosed()) {
//            loadDriver();
//            connection = DriverManager.getConnection(getUrl(), getUser(), getPassword());
//        }
//        return connection;
//    }
//
//    // Método para fechar a conexão com o banco de dados
//    public void disconnect() {
//        if (connection != null) {
//            try {
//                connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    // Métodos abstratos para obter URL, usuário e senha
//    protected abstract String getUrl();
//
//    protected abstract String getUser();
//
//    protected abstract String getPassword();
//
//    
//    // Método para obter a conexão atual
//    public Connection getConnection() {
//        return connection;
//    }
//}
