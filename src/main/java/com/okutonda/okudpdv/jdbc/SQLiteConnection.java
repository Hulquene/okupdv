/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.jdbc;

import com.okutonda.okudpdv.jdbc.connect.DatabaseConnection;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author kenny
 */
public class SQLiteConnection extends DatabaseConnection {

    private static final String URL = "jdbc:sqlite:" + System.getProperty("user.home") + "/seu_database.db";
    public String filePath = "database/db.sql";

    @Override
    public void loadDriver() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
    }

    @Override
    protected String getUrl() {
        return URL;
    }

    @Override
    protected String getUser() {
        return null;  // Não utilizado para SQLite
    }

    @Override
    protected String getPassword() {
        return null;  // Não utilizado para SQLite
    }

}
