package com.okutonda.okudpdv.helpers;

import com.okutonda.okudpdv.data.connection.DatabaseProvider;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Helper responsável por exportar tabelas da base de dados para ficheiros Excel
 * (.xlsx) e CSV (.csv).
 *
 * Agora usa DatabaseProvider (HikariCP) para conexões automáticas e seguras.
 *
 * ⚙️ Ideal para backups, auditorias e exportações de dados internas.
 *
 * @author Hulquene
 */
public class ExportHelper {

    /**
     * Exporta todas as tabelas da base de dados para ficheiros Excel (.xlsx).
     */
    public void exportAllTablesToExcel() {
        try (Connection conn = DatabaseProvider.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            String[] types = {"TABLE"};
            ResultSet tables = metaData.getTables(null, null, "%", types);

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                exportTableToExcel(conn, tableName);
            }

            System.out.println("✅ Todas as tabelas foram exportadas para Excel.");
        } catch (SQLException ex) {
            Logger.getLogger(ExportHelper.class.getName()).log(Level.SEVERE, "[DB] Erro ao exportar tabelas para Excel", ex);
        }
    }

    /**
     * Exporta todas as tabelas da base de dados para ficheiros CSV (.csv).
     */
    public void exportAllTablesToCSV() {
        try (Connection conn = DatabaseProvider.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            String[] types = {"TABLE"};
            ResultSet tables = metaData.getTables(null, null, "%", types);

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                exportTableToCSV(conn, tableName);
            }

            System.out.println("✅ Todas as tabelas foram exportadas para CSV.");
        } catch (SQLException ex) {
            Logger.getLogger(ExportHelper.class.getName()).log(Level.SEVERE, "[DB] Erro ao exportar tabelas para CSV", ex);
        }
    }

    // ==========================================================
    // 🔹 Exportação para Excel
    // ==========================================================
    private static void exportTableToExcel(Connection conn, String tableName) {
        String excelFilePath = tableName + ".xlsx";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName); Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet(tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Cabeçalho
            Row headerRow = sheet.createRow(0);
            for (int i = 1; i <= columnCount; i++) {
                Cell cell = headerRow.createCell(i - 1);
                cell.setCellValue(rsmd.getColumnName(i));
            }

            // Dados
            int rowIndex = 1;
            while (rs.next()) {
                Row row = sheet.createRow(rowIndex++);
                for (int i = 1; i <= columnCount; i++) {
                    Cell cell = row.createCell(i - 1);
                    cell.setCellValue(rs.getString(i));
                }
            }

            // Gravar ficheiro Excel
            try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
                workbook.write(fileOut);
            }

            System.out.println("📊 Tabela [" + tableName + "] exportada para " + excelFilePath);

        } catch (SQLException | IOException e) {
            System.err.println("[ExportHelper] Erro ao exportar tabela " + tableName + " para Excel: " + e.getMessage());
        }
    }

    // ==========================================================
    // 🔹 Exportação para CSV
    // ==========================================================
    private static void exportTableToCSV(Connection conn, String tableName) {
        String csvFilePath = tableName + ".csv";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName); BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Cabeçalho
            for (int i = 1; i <= columnCount; i++) {
                writer.write(rsmd.getColumnName(i));
                if (i < columnCount) {
                    writer.write(",");
                }
            }
            writer.newLine();

            // Dados
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String val = rs.getString(i);
                    if (val != null) {
                        val = val.replace(",", ";"); // Evita conflito de separadores
                    }
                    writer.write(val == null ? "" : val);
                    if (i < columnCount) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }

            System.out.println("📄 Tabela [" + tableName + "] exportada para " + csvFilePath);

        } catch (SQLException | IOException e) {
            System.err.println("[ExportHelper] Erro ao exportar tabela " + tableName + " para CSV: " + e.getMessage());
        }
    }
}

///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.okutonda.okudpdv.helpers;
//
//import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
//import java.io.BufferedWriter;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DatabaseMetaData;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
///**
// *
// * @author kenny
// */
//public class ExportHelper {
//
//    private final Connection connection = ConnectionDatabase.getConnect();
//
//    public void exportAllTableToExcel() {
//        try {
//            DatabaseMetaData metaData = connection.getMetaData();
//            String[] types = {"TABLE"};
//            ResultSet tables = metaData.getTables(null, null, "%", types);
//
//            while (tables.next()) {
//                String tableName = tables.getString("TABLE_NAME");
//                exportTableToExcel(connection, tableName);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(ExportHelper.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public void exportAllTableToCSV() {
//        try {
//            DatabaseMetaData metaData = connection.getMetaData();
//            String[] types = {"TABLE"};
//            ResultSet tables = metaData.getTables(null, null, "%", types);
//
//            while (tables.next()) {
//                String tableName = tables.getString("TABLE_NAME");
//                exportTableToCSV(connection, tableName);
//            }
//
//            System.out.println("Todas as tabelas foram exportadas.");
//        } catch (SQLException ex) {
//            Logger.getLogger(ExportHelper.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    private static void exportTableToExcel(Connection connection, String tableName) {
//        String excelFilePath = tableName + ".xlsx";
//
//        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName); Workbook workbook = new XSSFWorkbook()) {
//
//            Sheet sheet = workbook.createSheet(tableName);
//            Row headerRow = sheet.createRow(0);
//            ResultSetMetaData rsmd = resultSet.getMetaData();
//            int columnCount = rsmd.getColumnCount();
//
//            // Escreve o cabeçalho do Excel
//            for (int i = 1; i <= columnCount; i++) {
//                Cell cell = headerRow.createCell(i - 1);
//                cell.setCellValue(rsmd.getColumnName(i));
//            }
//
//            // Escreve os dados
//            int rowIndex = 1;
//            while (resultSet.next()) {
//                Row row = sheet.createRow(rowIndex++);
//                for (int i = 1; i <= columnCount; i++) {
//                    Cell cell = row.createCell(i - 1);
//                    cell.setCellValue(resultSet.getString(i));
//                }
//            }
//
//            // Grava o arquivo Excel
//            try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
//                workbook.write(fileOut);
//            }
//
//            System.out.println("Dados da tabela " + tableName + " exportados para " + excelFilePath);
//
//        } catch (SQLException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void exportTableToCSV(Connection connection, String tableName) {
//        String csvFilePath = tableName + ".csv";
//
//        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName); BufferedWriter fileWriter = new BufferedWriter(new FileWriter(csvFilePath))) {
//
//            int columnCount = resultSet.getMetaData().getColumnCount();
//
//            // Escreve o cabeçalho do CSV
//            for (int i = 1; i <= columnCount; i++) {
//                fileWriter.write(resultSet.getMetaData().getColumnName(i));
//                if (i < columnCount) {
//                    fileWriter.write(",");
//                }
//            }
//            fileWriter.newLine();
//
//            // Escreve os dados
//            while (resultSet.next()) {
//                for (int i = 1; i <= columnCount; i++) {
//                    fileWriter.write(resultSet.getString(i));
//                    if (i < columnCount) {
//                        fileWriter.write(",");
//                    }
//                }
//                fileWriter.newLine();
//            }
//
//            System.out.println("Dados da tabela " + tableName + " exportados para " + csvFilePath);
//
//        } catch (SQLException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
