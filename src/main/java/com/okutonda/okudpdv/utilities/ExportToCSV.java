package com.okutonda.okudpdv.utilities;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author rog
 */
public class ExportToCSV {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String url = "jdbc:mysql://localhost:3306/testebd";
        String user =  "root";
        String password = "";

       try (Connection connection = DriverManager.getConnection(url, user, password)) {

            DatabaseMetaData metaData = connection.getMetaData();
            String[] types = {"TABLE"};
            ResultSet tables = metaData.getTables(null, null, "%", types);

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                exportTableToCSV(connection, tableName);
            }

            System.out.println("Todas as tabelas foram exportadas.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void exportTableToCSV(Connection connection, String tableName) {
        String csvFilePath = tableName + ".csv";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
             BufferedWriter fileWriter = new BufferedWriter(new FileWriter(csvFilePath))) {

            int columnCount = resultSet.getMetaData().getColumnCount();

            // Escreve o cabeçalho do CSV
            for (int i = 1; i <= columnCount; i++) {
                fileWriter.write(resultSet.getMetaData().getColumnName(i));
                if (i < columnCount) {
                    fileWriter.write(",");
                }
            }
            fileWriter.newLine();

            // Escreve os dados
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    fileWriter.write(resultSet.getString(i));
                    if (i < columnCount) {
                        fileWriter.write(",");
                    }
                }
                fileWriter.newLine();
            }

            System.out.println("Dados da tabela " + tableName + " exportados para " + csvFilePath);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
