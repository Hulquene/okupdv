/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 *
 * @author kenny
 */
public class Util {

    public static final java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0.00");

    public static String checkNumberType(String input) {
        if (isInteger(input)) {
            return "Integer";
        } else if (isDouble(input)) {
            return "Double";
        } else if (isFloat(input)) {
            return "Float";
        } else {
            return "Not a number";
        }
    }

    public static Boolean checkIsNumber(String input) {
        if (isInteger(input)) {
            return true;
        } else if (isDouble(input)) {
            return true;
        } else if (isFloat(input)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isFloat(String input) {
        try {
            Float.parseFloat(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    // Função para verificar se a string pode ser convertida para double

    public static boolean isValidDouble(String input) {
        // Tenta converter o input diretamente sem alterá-lo
        try {
            Double.valueOf(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Função para converter a string para double se for válida
    public static Double convertToDouble(String input) {
        if (isValidDouble(input)) {
            return Double.valueOf(input);
        } else {
            throw new NumberFormatException("Input is not a valid number.");
        }
    }

    public static BigDecimal convertToBigDecimal(String text) {
        if (text == null || text.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            // Substitui vírgula por ponto para garantir compatibilidade
            text = text.replace(",", ".");
            return new BigDecimal(text).setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    // Função para converter a string para Integer se for válida
    public static Integer convertToInteger(String input) {
        if (isInteger(input)) {
            return Integer.valueOf(input);
        } else {
            throw new NumberFormatException("Input is not a valid integer.");
        }
    }
// Função para gerar o hash de uma string usando o algoritmo SHA-256
// Função para gerar um hash aleatório

    public static String generateRandomHash() {
        try {
            // Cria uma instância de SecureRandom para gerar bytes aleatórios
            SecureRandom secureRandom = new SecureRandom();
            byte[] randomBytes = new byte[32]; // 256 bits
            secureRandom.nextBytes(randomBytes);

            // Cria uma instância de MessageDigest para o algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(randomBytes);

            // Converte os bytes do hash para uma string Base64
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    // Função para gerar um hash a partir de uma entrada e a data/hora atual

    public static String generateHashWithTimestamp(String input) {
        try {
            // Obtém o timestamp atual em milissegundos
            long timestamp = System.currentTimeMillis();

            // Concatena a entrada com o timestamp
            String combinedInput = input + ":" + timestamp;

            // Cria uma instância de MessageDigest para o algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Converte a string combinada para um array de bytes e gera o hash
            byte[] hashBytes = digest.digest(combinedInput.getBytes());

            // Converte os bytes do hash para uma string Base64
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateHash(String input) {
        try {

//            Hashing com Outros Algoritmos
//            :
//Se você quiser usar um algoritmo de hashing diferente
//            , como MD5 ou SHA
//            -1, basta substituir "SHA-256" por "MD5" ou "SHA-1" no método MessageDigest.getInstance.Nota
//            : Embora MD5 e SHA
//            -1 ainda sejam amplamente usados, eles são considerados criptograficamente inseguros para muitas aplicações.Por isso, SHA
//            -256 ou algoritmos mais fortes são recomendados para novas aplicações. 
// Cria uma instância de MessageDigest para o algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Converte a string de entrada para um array de bytes
            byte[] hashBytes = digest.digest(input.getBytes());

            // Converte os bytes do hash para uma string hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateHashUnic(String input) {
        try {
            // Cria uma instância de MessageDigest para o algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Converte a string de entrada para um array de bytes
            byte[] hashBytes = digest.digest(input.getBytes());

            // Converte os bytes do hash para uma string hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static BigDecimal parse(String s) {
        if (s == null) {
            return BigDecimal.ZERO;
        }
        s = s.trim();
        if (s.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // 🔑 remove caracteres “estranhos” (espaços, U+FFFD, etc.)
        s = s.replaceAll("[^0-9,.-]", "");

        // normaliza decimal
        if (s.contains(",") && s.contains(".")) {
            // caso "10.000,50"
            s = s.replace(".", "").replace(",", ".");
        } else if (s.contains(",")) {
            s = s.replace(",", ".");
        }

        try {
            return new BigDecimal(s);
        } catch (Exception e) {
            System.err.println("Falha ao parsear valor: " + s);
            return BigDecimal.ZERO;
        }
    }

    public static String format(BigDecimal v) {
        if (v == null) {
            return "0,00";
        }
        return df.format(v);
    }

}
