package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.connection.DatabaseProvider;
import com.okutonda.okudpdv.jdbc.MySQLServiceManager;
import java.sql.Connection;

/**
 * Classe responsável pela instalação e inicialização do banco de dados do
 * sistema Okudpdv.
 *
 * Substitui o uso de ConnectionDatabase por DatabaseProvider (pool HikariCP).
 *
 * @author Hulquene
 */
public class Install {

    /**
     * Executa o processo de instalação do banco de dados.
     *
     * Fluxo: 1️⃣ Cria a base de dados (via MySQLServiceManager) 2️⃣ Executa o
     * ficheiro .sql com estrutura inicial 3️⃣ Retorna código de status: - 1 =
     * sucesso total - 2 = erro ao executar script SQL - 0 = erro ao criar base
     * de dados
     */
    public int installDatabase() {
        try {
            if (MySQLServiceManager.createDatabaseMySQL()) {
                try (Connection conn = DatabaseProvider.getConnection()) {
                    boolean ok = MySQLServiceManager.executeSqlFile(conn);
                    return ok ? 1 : 2;
                } catch (Exception e) {
                    System.err.println("[INSTALL] Erro ao executar SQL: " + e.getMessage());
                    return 2;
                }
            }
        } catch (Exception e) {
            System.err.println("[INSTALL] Erro ao criar base de dados: " + e.getMessage());
        }
        return 0;
    }
}

///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.okutonda.okudpdv.controllers;
//
//import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
//import com.okutonda.okudpdv.jdbc.MySQLServiceManager;
//import java.sql.Connection;
//
///**
// *
// * @author kenny
// */
//public class Install {
////    private final Connection connection = ConnectionDatabase.getConnect();
//
//    public int installDatabase() {
//        if (MySQLServiceManager.createDatabaseMySQL()) {
//            Connection connection = ConnectionDatabase.getConnect();
//            if (MySQLServiceManager.executeSqlFile(connection)) {
//                return 1;
//            }
//            //executeSqlFile
//            return 2;
//        }
//        //installDatabase
//        return 0;
//    }
//}
