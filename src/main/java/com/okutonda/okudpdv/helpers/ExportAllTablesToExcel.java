/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.okutonda.okudpdv.helpers;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
/**
 *
 * @author rog
 */
public class ExportAllTablesToExcel {

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
                exportTableToExcel(connection, tableName);
            }

            System.out.println("Todas as tabelas foram exportadas.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void exportTableToExcel(Connection connection, String tableName) {
        String excelFilePath = tableName + ".xlsx";

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName); Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet(tableName);
            Row headerRow = sheet.createRow(0);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Escreve o cabeçalho do Excel
            for (int i = 1; i <= columnCount; i++) {
                Cell cell = headerRow.createCell(i - 1);
                cell.setCellValue(rsmd.getColumnName(i));
            }

            // Escreve os dados
            int rowIndex = 1;
            while (resultSet.next()) {
                Row row = sheet.createRow(rowIndex++);
                for (int i = 1; i <= columnCount; i++) {
                    Cell cell = row.createCell(i - 1);
                    cell.setCellValue(resultSet.getString(i));
                }
            }

            // Grava o arquivo Excel
            try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
                workbook.write(fileOut);
            }

            System.out.println("Dados da tabela " + tableName + " exportados para " + excelFilePath);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
