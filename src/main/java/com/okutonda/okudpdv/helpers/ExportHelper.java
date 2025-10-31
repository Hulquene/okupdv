package com.okutonda.okudpdv.helpers;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import org.hibernate.Session;
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
 * Agora usa Hibernate para conexões automáticas e seguras.
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
        Session session = HibernateUtil.getCurrentSession();
        try {
            session.doWork(connection -> {
                try {
                    DatabaseMetaData metaData = connection.getMetaData();
                    String[] types = {"TABLE"};
                    ResultSet tables = metaData.getTables(null, null, "%", types);

                    while (tables.next()) {
                        String tableName = tables.getString("TABLE_NAME");
                        exportTableToExcel(connection, tableName);
                    }

                    System.out.println("✅ Todas as tabelas foram exportadas para Excel.");
                } catch (SQLException ex) {
                    Logger.getLogger(ExportHelper.class.getName()).log(Level.SEVERE, "[DB] Erro ao exportar tabelas para Excel", ex);
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(ExportHelper.class.getName()).log(Level.SEVERE, "[DB] Erro ao exportar tabelas para Excel", ex);
        }
    }

    /**
     * Exporta todas as tabelas da base de dados para ficheiros CSV (.csv).
     */
    public void exportAllTablesToCSV() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            session.doWork(connection -> {
                try {
                    DatabaseMetaData metaData = connection.getMetaData();
                    String[] types = {"TABLE"};
                    ResultSet tables = metaData.getTables(null, null, "%", types);

                    while (tables.next()) {
                        String tableName = tables.getString("TABLE_NAME");
                        exportTableToCSV(connection, tableName);
                    }

                    System.out.println("✅ Todas as tabelas foram exportadas para CSV.");
                } catch (SQLException ex) {
                    Logger.getLogger(ExportHelper.class.getName()).log(Level.SEVERE, "[DB] Erro ao exportar tabelas para CSV", ex);
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(ExportHelper.class.getName()).log(Level.SEVERE, "[DB] Erro ao exportar tabelas para CSV", ex);
        }
    }

    /**
     * Exporta uma tabela específica para Excel
     */
    public void exportTableToExcel(String tableName) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            session.doWork(connection -> {
                exportTableToExcel(connection, tableName);
            });
        } catch (Exception ex) {
            Logger.getLogger(ExportHelper.class.getName()).log(Level.SEVERE, "[DB] Erro ao exportar tabela " + tableName + " para Excel", ex);
        }
    }

    /**
     * Exporta uma tabela específica para CSV
     */
    public void exportTableToCSV(String tableName) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            session.doWork(connection -> {
                exportTableToCSV(connection, tableName);
            });
        } catch (Exception ex) {
            Logger.getLogger(ExportHelper.class.getName()).log(Level.SEVERE, "[DB] Erro ao exportar tabela " + tableName + " para CSV", ex);
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

            // Estilo para cabeçalho
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Cabeçalho
            Row headerRow = sheet.createRow(0);
            for (int i = 1; i <= columnCount; i++) {
                Cell cell = headerRow.createCell(i - 1);
                cell.setCellValue(rsmd.getColumnName(i));
                cell.setCellStyle(headerStyle);

                // Auto-size column
                sheet.autoSizeColumn(i - 1);
            }

            // Dados
            int rowIndex = 1;
            while (rs.next()) {
                Row row = sheet.createRow(rowIndex++);
                for (int i = 1; i <= columnCount; i++) {
                    Cell cell = row.createCell(i - 1);
                    String value = rs.getString(i);
                    if (value != null) {
                        // Tenta converter para número se possível
                        try {
                            if (value.matches("-?\\d+(\\.\\d+)?")) {
                                cell.setCellValue(Double.parseDouble(value));
                            } else {
                                cell.setCellValue(value);
                            }
                        } catch (NumberFormatException e) {
                            cell.setCellValue(value);
                        }
                    }
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
                writer.write(escapeCsvValue(rsmd.getColumnName(i)));
                if (i < columnCount) {
                    writer.write(",");
                }
            }
            writer.newLine();

            // Dados
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    writer.write(escapeCsvValue(value));
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

    /**
     * Escapa valores CSV para evitar problemas com vírgulas e aspas
     */
    private static String escapeCsvValue(String value) {
        if (value == null) {
            return "";
        }

        // Se contém vírgula, nova linha ou aspas, coloca entre aspas
        if (value.contains(",") || value.contains("\n") || value.contains("\"") || value.contains("\r")) {
            // Escapa aspas duplas
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }

        return value;
    }

    /**
     * Exporta dados de uma consulta customizada para Excel
     */
    public void exportQueryToExcel(String query, String fileName, String sheetName) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            session.doWork(connection -> {
                exportQueryToExcel(connection, query, fileName, sheetName);
            });
        } catch (Exception ex) {
            Logger.getLogger(ExportHelper.class.getName()).log(Level.SEVERE, "[DB] Erro ao exportar consulta para Excel", ex);
        }
    }

    private static void exportQueryToExcel(Connection conn, String query, String fileName, String sheetName) {
        String excelFilePath = fileName + ".xlsx";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query); Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet(sheetName);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Estilo para cabeçalho
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Cabeçalho
            Row headerRow = sheet.createRow(0);
            for (int i = 1; i <= columnCount; i++) {
                Cell cell = headerRow.createCell(i - 1);
                cell.setCellValue(rsmd.getColumnName(i));
                cell.setCellStyle(headerStyle);
                sheet.autoSizeColumn(i - 1);
            }

            // Dados
            int rowIndex = 1;
            while (rs.next()) {
                Row row = sheet.createRow(rowIndex++);
                for (int i = 1; i <= columnCount; i++) {
                    Cell cell = row.createCell(i - 1);
                    String value = rs.getString(i);
                    if (value != null) {
                        cell.setCellValue(value);
                    }
                }
            }

            // Gravar ficheiro Excel
            try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
                workbook.write(fileOut);
            }

            System.out.println("📊 Consulta exportada para " + excelFilePath);

        } catch (SQLException | IOException e) {
            System.err.println("[ExportHelper] Erro ao exportar consulta para Excel: " + e.getMessage());
        }
    }

    /**
     * Exporta dados de uma consulta customizada para CSV
     */
    public void exportQueryToCSV(String query, String fileName) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            session.doWork(connection -> {
                exportQueryToCSV(connection, query, fileName);
            });
        } catch (Exception ex) {
            Logger.getLogger(ExportHelper.class.getName()).log(Level.SEVERE, "[DB] Erro ao exportar consulta para CSV", ex);
        }
    }

    private static void exportQueryToCSV(Connection conn, String query, String fileName) {
        String csvFilePath = fileName + ".csv";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query); BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Cabeçalho
            for (int i = 1; i <= columnCount; i++) {
                writer.write(escapeCsvValue(rsmd.getColumnName(i)));
                if (i < columnCount) {
                    writer.write(",");
                }
            }
            writer.newLine();

            // Dados
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    writer.write(escapeCsvValue(value));
                    if (i < columnCount) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }

            System.out.println("📄 Consulta exportada para " + csvFilePath);

        } catch (SQLException | IOException e) {
            System.err.println("[ExportHelper] Erro ao exportar consulta para CSV: " + e.getMessage());
        }
    }
}
