/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Classe utilitária global com funções para números, datas, strings e
 * criptografia
 *
 * @author OKUTONDA
 */
public class Util {

    // ==========================================================
    // 🔹 CONFIGURAÇÕES GLOBAIS
    // ==========================================================
    public static final Locale DEFAULT_LOCALE = new Locale("pt", "AO");
    public static final DecimalFormat DF = createDecimalFormat();

    // Formatos de data
    public static final String DATE_FORMAT_DD_MM_YYYY = "dd/MM/yyyy";
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_DD_MM_YYYY_HH_MM = "dd/MM/yyyy HH:mm";
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_HH_MM = "HH:mm";

    // Padrões regex
    private static final Pattern INTEGER_PATTERN = Pattern.compile("^-?\\d+$");
    private static final Pattern DECIMAL_PATTERN = Pattern.compile("^-?\\d+([.,]\\d+)?$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern NIF_PATTERN = Pattern.compile("^\\d{9}$"); // Padrão básico para Angola

    // ==========================================================
    // 🔹 CONSTRUTOR PRIVADO (CLASSE UTILITÁRIA)
    // ==========================================================
    private Util() {
        throw new IllegalStateException("Classe utilitária - não pode ser instanciada");
    }

    // ==========================================================
    // 🔹 FORMATADOR DE NÚMEROS
    // ==========================================================
    private static DecimalFormat createDecimalFormat() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(DEFAULT_LOCALE);
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat df = new DecimalFormat("#,##0.00", symbols);
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df;
    }

    // ==========================================================
    // 🔹 VALIDAÇÃO DE NÚMEROS (MELHORADAS)
    // ==========================================================
    /**
     * Verifica se uma string é um número inteiro válido
     */
    public static boolean isInteger(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        return INTEGER_PATTERN.matcher(input.trim()).matches();
    }

    /**
     * Verifica se uma string é um número decimal válido
     */
    public static boolean isDecimal(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        return DECIMAL_PATTERN.matcher(input.trim()).matches();
    }

    /**
     * Verifica se uma string é um número válido (inteiro ou decimal)
     */
    public static boolean isNumber(String input) {
        return isInteger(input) || isDecimal(input);
    }

    /**
     * Verifica se uma string é um double válido
     */
    public static boolean isDouble(String input) {
        if (!isDecimal(input)) {
            return false;
        }
        try {
            Double.parseDouble(normalizeDecimal(input));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Verifica se uma string é um float válido
     */
    public static boolean isFloat(String input) {
        if (!isDecimal(input)) {
            return false;
        }
        try {
            Float.parseFloat(normalizeDecimal(input));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Determina o tipo de número
     */
    public static String getNumberType(String input) {
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

    // ==========================================================
    // 🔹 CONVERSÃO DE NÚMEROS (MELHORADAS)
    // ==========================================================
    /**
     * Normaliza string decimal para formato padrão (ponto como separador)
     */
    private static String normalizeDecimal(String input) {
        if (input == null) {
            return "0";
        }
        String normalized = input.trim().replace(".", "").replace(",", ".");
        return normalized.isEmpty() ? "0" : normalized;
    }

    /**
     * Converte string para Integer
     */
    public static Integer toInteger(String input) {
        if (!isInteger(input)) {
            throw new NumberFormatException("Não é um inteiro válido: " + input);
        }
        return Integer.valueOf(input.trim());
    }

    /**
     * Converte string para Integer com valor padrão
     */
    public static Integer toInteger(String input, Integer defaultValue) {
        try {
            return toInteger(input);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Converte string para Double
     */
    public static Double toDouble(String input) {
        if (!isDouble(input)) {
            throw new NumberFormatException("Não é um double válido: " + input);
        }
        return Double.valueOf(normalizeDecimal(input));
    }

    /**
     * Converte string para Double com valor padrão
     */
    public static Double toDouble(String input, Double defaultValue) {
        try {
            return toDouble(input);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Converte string para BigDecimal
     */
    public static BigDecimal toBigDecimal(String input) {
        if (input == null || input.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            String normalized = normalizeDecimal(input.trim());
            return new BigDecimal(normalized).setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    /**
     * Converte string para BigDecimal com valor padrão
     */
    public static BigDecimal toBigDecimal(String input, BigDecimal defaultValue) {
        try {
            return toBigDecimal(input);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    // ==========================================================
    // 🔹 FORMATAÇÃO DE NÚMEROS (MELHORADAS)
    // ==========================================================
    /**
     * Formata BigDecimal para string no formato monetário
     */
    public static String formatCurrency(BigDecimal value) {
        if (value == null) {
            return "0,00";
        }
        return DF.format(value);
    }

    /**
     * Formata Double para string no formato monetário
     */
    public static String formatCurrency(Double value) {
        if (value == null) {
            return "0,00";
        }
        return DF.format(value);
    }

    /**
     * Formata Integer para string no formato monetário
     */
    public static String formatCurrency(Integer value) {
        if (value == null) {
            return "0,00";
        }
        return DF.format(value);
    }

    /**
     * Formata número para string com casas decimais
     */
    public static String formatNumber(Number value, int decimals) {
        if (value == null) {
            value = 0;
        }
        DecimalFormat customFormat = new DecimalFormat("#,##0." + "0".repeat(decimals));
        customFormat.setDecimalFormatSymbols(DF.getDecimalFormatSymbols());
        return customFormat.format(value);
    }

    // ==========================================================
    // 🔹 OPERAÇÕES COM DATAS (NOVAS)
    // ==========================================================
    /**
     * Obtém a data atual
     */
    public static LocalDate currentDate() {
        return LocalDate.now();
    }

    /**
     * Obtém a data e hora atual
     */
    public static LocalDateTime currentDateTime() {
        return LocalDateTime.now();
    }

    /**
     * Formata LocalDate para string
     */
    public static String formatDate(LocalDate date, String format) {
        if (date == null) {
            return "";
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, DEFAULT_LOCALE);
            return date.format(formatter);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Formata LocalDateTime para string
     */
    public static String formatDateTime(LocalDateTime dateTime, String format) {
        if (dateTime == null) {
            return "";
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, DEFAULT_LOCALE);
            return dateTime.format(formatter);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Converte string para LocalDate
     */
    public static LocalDate parseDate(String dateString, String format) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, DEFAULT_LOCALE);
            return LocalDate.parse(dateString.trim(), formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Converte string para LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeString, String format) {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, DEFAULT_LOCALE);
            return LocalDateTime.parse(dateTimeString.trim(), formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Data atual formatada (dd/MM/yyyy)
     */
    public static String currentDateFormatted() {
        return formatDate(currentDate(), DATE_FORMAT_DD_MM_YYYY);
    }

    /**
     * Data e hora atual formatada (dd/MM/yyyy HH:mm)
     */
    public static String currentDateTimeFormatted() {
        return formatDateTime(currentDateTime(), DATE_FORMAT_DD_MM_YYYY_HH_MM);
    }

    /**
     * Adiciona dias a uma data
     */
    public static LocalDate addDays(LocalDate date, int days) {
        if (date == null) {
            return null;
        }
        return date.plusDays(days);
    }

    /**
     * Adiciona meses a uma data
     */
    public static LocalDate addMonths(LocalDate date, int months) {
        if (date == null) {
            return null;
        }
        return date.plusMonths(months);
    }

    /**
     * Diferença em dias entre duas datas
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(start, end);
    }

    /**
     * Verifica se uma data está dentro de um intervalo
     */
    public static boolean isDateInRange(LocalDate date, LocalDate start, LocalDate end) {
        if (date == null || start == null || end == null) {
            return false;
        }
        return !date.isBefore(start) && !date.isAfter(end);
    }
    // ==========================================================
// 🔹 MÉTODOS DE DATA ADICIONAIS (PARA COMPATIBILIDADE)
// ==========================================================

    /**
     * Obtém o ano atual
     */
    public static Integer getYear() {
        return LocalDate.now().getYear();
    }

    /**
     * Obtém o mês atual (1-12)
     */
    public static Integer getMonth() {
        return LocalDate.now().getMonthValue();
    }

    /**
     * Obtém o dia atual do mês (1-31)
     */
    public static Integer getDay() {
        return LocalDate.now().getDayOfMonth();
    }

    /**
     * Obtém a data atual formatada (para compatibilidade)
     */
//    public static String getFormatDataNow() {
//        return currentDateFormatted();
//    }
    /**
     * Obtém data e hora atual formatada (para compatibilidade)
     */
    public static String getFormatDateTimeNow() {
        return currentDateTimeFormatted();
    }

    /**
     * Obtém LocalDate atual (para compatibilidade)
     */
    public static LocalDate getLocalDateNow() {
        return currentDate();
    }

    /**
     * Obtém LocalDateTime atual (para compatibilidade)
     */
    public static LocalDateTime getLocalDateTimeNow() {
        return currentDateTime();
    }

    /**
     * Formata data no formato dd/MM/yyyy (para compatibilidade)
     */
    public static String formatDate(LocalDate date) {
        return formatDate(date, DATE_FORMAT_DD_MM_YYYY);
    }

    /**
     * Formata data e hora no formato dd/MM/yyyy HH:mm (para compatibilidade)
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return formatDateTime(dateTime, DATE_FORMAT_DD_MM_YYYY_HH_MM);
    }
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    // Método para obter data atual
    public static Date getDataAtual() {
        return new Date();
    }

    // Método para formatar data como string
    public static String getDataFormatada() {
        return DATE_FORMAT.format(new Date());
    }

    // Método para formatar data específica
    public static String formatarData(Date data) {
        if (data == null) {
            return "";
        }
        return DATE_FORMAT.format(data);
    }

    // Método para data e hora
    public static String getDataHoraFormatada() {
        return DATETIME_FORMAT.format(new Date());
    }

    // Método para converter string para Date
    public static Date parseData(String dataStr) {
        try {
            return DATE_FORMAT.parse(dataStr);
        } catch (Exception e) {
            return new Date(); // Retorna data atual em caso de erro
        }
    }

    // ==========================================================
    // 🔹 VALIDAÇÃO DE STRINGS (NOVAS)
    // ==========================================================
    /**
     * Verifica se string é vazia ou nula
     */
    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    /**
     * Verifica se string não é vazia
     */
    public static boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    /**
     * Verifica se email é válido
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Verifica se NIF é válido (formato básico)
     */
    public static boolean isValidNIF(String nif) {
        if (isEmpty(nif)) {
            return false;
        }
        return NIF_PATTERN.matcher(nif.trim()).matches();
    }

    /**
     * Limita string ao tamanho máximo
     */
    public static String limitString(String text, int maxLength) {
        if (isEmpty(text)) {
            return "";
        }
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    /**
     * Capitaliza primeira letra
     */
    public static String capitalize(String text) {
        if (isEmpty(text)) {
            return "";
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    // ==========================================================
    // 🔹 CRIPTOGRAFIA E HASH (MELHORADAS)
    // ==========================================================
    /**
     * Gera hash SHA-256 de uma string
     */
    public static String generateHash(String input) {
        if (isEmpty(input)) {
            return "";
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());

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
            throw new RuntimeException("Algoritmo SHA-256 não disponível", e);
        }
    }

    /**
     * Gera hash único com timestamp
     */
    public static String generateUniqueHash(String input) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String combined = input + ":" + timestamp + ":" + generateRandomString(8);
        return generateHash(combined);
    }

    /**
     * Gera hash aleatório
     */
    public static String generateRandomHash() {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] randomBytes = new byte[32];
            secureRandom.nextBytes(randomBytes);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(randomBytes);

            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo SHA-256 não disponível", e);
        }
    }

    /**
     * Gera string aleatória
     */
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }

        return sb.toString();
    }

    // ==========================================================
    // 🔹 UTILITÁRIOS DIVERSOS (NOVOS)
    // ==========================================================
    /**
     * Remove caracteres não numéricos
     */
    public static String keepOnlyNumbers(String text) {
        if (isEmpty(text)) {
            return "";
        }
        return text.replaceAll("\\D", "");
    }

    /**
     * Formata número de telefone
     */
    public static String formatPhoneNumber(String phone) {
        if (isEmpty(phone)) {
            return "";
        }
        String numbers = keepOnlyNumbers(phone);

        if (numbers.length() == 9) {
            return numbers.replaceFirst("(\\d{3})(\\d{3})(\\d{3})", "$1 $2 $3");
        }
        return numbers;
    }

    /**
     * Verifica se objeto é numérico
     */
    public static boolean isNumeric(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj instanceof Number
                || (obj instanceof String && isNumber((String) obj));
    }

    /**
     * Converte objeto para BigDecimal seguro
     */
    public static BigDecimal toBigDecimal(Object obj) {
        if (obj == null) {
            return BigDecimal.ZERO;
        }

        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        if (obj instanceof Number) {
            return BigDecimal.valueOf(((Number) obj).doubleValue());
        }
        if (obj instanceof String) {
            return toBigDecimal((String) obj);
        }

        return BigDecimal.ZERO;
    }

    /**
     * Arredonda valor para 2 casas decimais
     */
    public static BigDecimal round(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula percentagem
     */
    public static BigDecimal calculatePercentage(BigDecimal value, BigDecimal percentage) {
        if (value == null || percentage == null) {
            return BigDecimal.ZERO;
        }
        return value.multiply(percentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    /**
     * Verifica se dois valores são iguais (com tolerância para decimais)
     */
    public static boolean equals(BigDecimal value1, BigDecimal value2) {
        if (value1 == null && value2 == null) {
            return true;
        }
        if (value1 == null || value2 == null) {
            return false;
        }
        return value1.compareTo(value2) == 0;
    }

    /**
     * Retorna o maior valor entre dois
     */
    public static BigDecimal max(BigDecimal value1, BigDecimal value2) {
        if (value1 == null) {
            return value2;
        }
        if (value2 == null) {
            return value1;
        }
        return value1.compareTo(value2) > 0 ? value1 : value2;
    }

    /**
     * Retorna o menor valor entre dois
     */
    public static BigDecimal min(BigDecimal value1, BigDecimal value2) {
        if (value1 == null) {
            return value2;
        }
        if (value2 == null) {
            return value1;
        }
        return value1.compareTo(value2) < 0 ? value1 : value2;
    }

    // ==========================================================
    // 🔹 MÉTODOS DE DEPURAÇÃO (NOVOS)
    // ==========================================================
    /**
     * Log formatado para debug
     */
    public static void debug(String message) {
        System.out.println("🔹 [DEBUG] " + message);
    }

    /**
     * Log de erro formatado
     */
    public static void error(String message, Exception e) {
        System.err.println("❌ [ERROR] " + message);
        if (e != null) {
            e.printStackTrace();
        }
    }

    /**
     * Log de informação formatado
     */
    public static void info(String message) {
        System.out.println("ℹ️ [INFO] " + message);
    }

    /**
     * Mede tempo de execução de uma tarefa
     */
    public static long measureTime(Runnable task) {
        long start = System.currentTimeMillis();
        task.run();
        long end = System.currentTimeMillis();
        return end - start;
    }
}
