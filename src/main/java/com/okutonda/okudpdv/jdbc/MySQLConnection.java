/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.jdbc;

import com.okutonda.okudpdv.jdbc.connect.DatabaseConnection;

/**
 *
 * @author kenny
 */
public class MySQLConnection extends DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/okudpdv";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    public String filePath = "database/db.sql";

    @Override
    public void loadDriver() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    @Override
    protected String getUrl() {
        return URL;
    }

    @Override
    protected String getUser() {
        return USER;
    }

    @Override
    protected String getPassword() {
        return PASSWORD;
    }
}
