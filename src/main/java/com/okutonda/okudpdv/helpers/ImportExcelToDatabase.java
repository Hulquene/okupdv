///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
// */
//package com.okutonda.okudpdv.helpers;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
///**
// *
// * @author rog
// */
//public class ImportExcelToDatabase {
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        // TODO code application logic here
//        String excelFilePath = "import.xlsx"; // Substitua pelo caminho do seu arquivo Excel
//        String url = "jdbc:mysql://localhost:3306/testebd"; // Substitua pelo nome do seu banco de dados
//        String user = "root"; // Substitua pelo seu nome de usuário do MySQL
//        String password = ""; // Substitua pela sua senha do MySQL
//
//        try (Connection connection = DriverManager.getConnection(url, user, password)) {
//            importExcelToDatabase(connection, excelFilePath);
//        } catch (SQLException | IOException e) {
//            e.printStackTrace();
//        }
//    }
////    public static void importExcelToDatabase(Connection connection, String excelFilePath) throws IOException, SQLException {
////        try (FileInputStream fileInputStream = new FileInputStream(excelFilePath);
////             Workbook workbook = new XSSFWorkbook(fileInputStream)) {
////
////            // Supomos que os dados estão na primeira planilha
////            Sheet sheet = workbook.getSheetAt(0);
////
////            // Configura a query de inserção
////            String sql = "INSERT INTO sua_tabela (coluna1, coluna2, coluna3) VALUES (?, ?, ?)";
////            try (PreparedStatement statement = connection.prepareStatement(sql)) {
////
////                // Itera sobre as linhas do Excel, começando na segunda linha (índice 1)
////                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
////                    Row row = sheet.getRow(i);
////
////                    if (row != null) {
////                        // Supomos que os dados estão nas três primeiras colunas
////                        String coluna1 = row.getCell(0).getStringCellValue();
////                        String coluna2 = row.getCell(1).getStringCellValue();
////                        double coluna3 = row.getCell(2).getNumericCellValue();
////
////                        // Define os valores dos parâmetros da query
////                        statement.setString(1, coluna1);
////                        statement.setString(2, coluna2);
////                        statement.setDouble(3, coluna3);
////
////                        // Executa a inserção
////                        statement.executeUpdate();
////                    }
////                }
////
////                System.out.println("Dados importados com sucesso!");
////            }
////        }
////    }
//
//    public static void importExcelToDatabase(Connection connection, String excelFilePath) throws IOException, SQLException {
//        try (FileInputStream fileInputStream = new FileInputStream(excelFilePath); Workbook workbook = new XSSFWorkbook(fileInputStream)) {
//
//            Sheet sheet = workbook.getSheetAt(0);
//
//            String sql = "INSERT INTO sua_tabela (coluna1, coluna2, coluna3) VALUES (?, ?, ?)";
//            try (PreparedStatement statement = connection.prepareStatement(sql)) {
//
//                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//                    Row row = sheet.getRow(i);
//
//                    if (row != null) {
//                        String coluna1 = getCellValueAsString(row.getCell(0));
//                        String coluna2 = getCellValueAsString(row.getCell(1));
//                        double coluna3 = getCellValueAsDouble(row.getCell(2));
//
//                        statement.setString(1, coluna1);
//                        statement.setString(2, coluna2);
//                        statement.setDouble(3, coluna3);
//
//                        statement.executeUpdate();
//                    }
//                }
//
//                System.out.println("Dados importados com sucesso!");
//            }
//        }
//    }
//
//    private static String getCellValueAsString(Cell cell) {
//        if (cell == null) {
//            return "";
//        }
//        switch (cell.getCellType()) {
//            case STRING:
//                return cell.getStringCellValue();
//            case NUMERIC:
//                return String.valueOf(cell.getNumericCellValue());
//            case BOOLEAN:
//                return String.valueOf(cell.getBooleanCellValue());
//            default:
//                return "";
//        }
//    }
//
//    private static double getCellValueAsDouble(Cell cell) {
//        if (cell == null) {
//            return 0.0;
//        }
//        switch (cell.getCellType()) {
//            case NUMERIC:
//                return cell.getNumericCellValue();
//            case STRING:
//                try {
//                    return Double.parseDouble(cell.getStringCellValue());
//                } catch (NumberFormatException e) {
//                    throw new IllegalStateException("Cannot convert string to double: " + cell.getStringCellValue());
//                }
//            default:
//                return 0.0;
//        }
//    }
//}
