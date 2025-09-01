/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.jdbc.MySQLServiceManager;
import java.sql.Connection;

/**
 *
 * @author kenny
 */
public class Install {
//    private final Connection connection = ConnectionDatabase.getConnect();

    public int installDatabase() {
        if (MySQLServiceManager.createDatabaseMySQL()) {
            Connection connection = ConnectionDatabase.getConnect();
            if (MySQLServiceManager.executeSqlFile(connection)) {
                return 1;
            }
            //executeSqlFile
            return 2;
        }
        //installDatabase
        return 0;
    }
}
