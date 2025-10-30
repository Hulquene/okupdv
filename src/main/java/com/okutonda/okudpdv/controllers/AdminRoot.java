/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.entities.Options;
import com.okutonda.okudpdv.helpers.CompanySession;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author kenny
 */
public class AdminRoot {

    CompanySession companySession = CompanySession.getInstance();
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private final String encryptionKeyBase64 = "aXdrbH5JPzdg8hM5AbZYylF4uN7FfWZ9FgYoK/X9JrE="; // Substitua pela chave Base64 gerada
    OptionController optionController = new OptionController();
//    ProductOrderDao prodOrderDao;

    public AdminRoot() {
    }

    public String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        byte[] keyBytes = Base64.getDecoder().decode(encryptionKeyBase64);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        // Gerar IV e inicializar o cipher
        byte[] iv = new byte[cipher.getBlockSize()];
        new java.security.SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(data.getBytes());

        // Codificar os dados criptografados e o IV em Base64
        return Base64.getEncoder().encodeToString(encrypted) + "::" + Base64.getEncoder().encodeToString(iv);
    }

    public String decrypt(String encryptedData) throws Exception {
        String[] parts = encryptedData.split("::");
        byte[] encryptedBytes = Base64.getDecoder().decode(parts[0]);
        byte[] iv = Base64.getDecoder().decode(parts[1]);

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        byte[] keyBytes = Base64.getDecoder().decode(encryptionKeyBase64);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decrypted = cipher.doFinal(encryptedBytes);

        return new String(decrypted);
    }

//    public Boolean validateLicence(String client, String encryptedData) {
//        try {
////            String encryptionKeyBase64 = "aXdrbH5JPzdg8hM5AbZYylF4uN7FfWZ9FgYoK/X9JrE="; // Substitua pela chave Base64 gerada
////            String data = "1234567";
//
//            // Criptografar
////            String encryptedData = encrypt(data);
////            System.out.println("Encrypted Data: " + encryptedData);
////            JOptionPane.showMessageDialog(null, "encryptedData!!" + encryptedData);
//            // Descriptografar
//            String decryptedData = decrypt(encryptedData);
//            System.out.println("Decrypted Data: " + decryptedData);
//            JOptionPane.showMessageDialog(null, "decryptedData!!" + decryptedData);
//
////            JOptionPane.showMessageDialog(null, "decryptedData:\n" + decryptedData + "\n encryptedData:\n" + encryptedData);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }
    /**
     * Compara a data fornecida com a data atual e fornece informações sobre o
     * status da data.
     *
     * @param dataStr A data fornecida no formato "dd/MM/yyyy".
     * @return
     */
    public Boolean compararData(String dataStr) {
        // Definir o formato da data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Obter a data atual
        LocalDate dataAtual = LocalDate.now();

        // Converter a string fornecida para um objeto LocalDate
        LocalDate dataFornecida = LocalDate.parse(dataStr, formatter);

        // Comparar a data fornecida com a data atual
        if (dataFornecida.isBefore(dataAtual)) {
            System.out.println("A data fornecida (" + dataStr + ") é anterior à data atual.");
//            return 2;
        } else if (dataFornecida.isEqual(dataAtual)) {
            System.out.println("A data fornecida (" + dataStr + ") é hoje.");
//            return 2;
        } else {
            // Calcular o tempo restante até a data fornecida
            Period periodoRestante = Period.between(dataAtual, dataFornecida);
            long mesesRestantes = periodoRestante.toTotalMonths();
            long diasRestantes = periodoRestante.getDays();

            companySession.setDateKeyLicence(dataStr);
            companySession.setTime("Tempo restante até a data fornecida: " + mesesRestantes + " meses e " + diasRestantes + " dias.");

            System.out.println("A data fornecida (" + dataStr + ") é futura.");
            System.out.println("Tempo restante até a data fornecida: "
                    + mesesRestantes + " meses e " + diasRestantes + " dias.");
            return true;
        }
        return false;
    }

    /**
     * Separa uma string em três partes com base no delimitador ":".
     *
     * @param input A string de entrada no formato "parte1:parte2:parte3".
     * @return Um array de strings contendo as três partes separadas, ou null se
     * a entrada for inválida.
     */
    public static String[] separarString(String input) {
        // Usar o método split para dividir a string pelo delimitador ":"
        String[] partes = input.split(":");

        // Verificar se a divisão resultou em exatamente três partes
        if (partes.length == 3) {
            return partes;
        } else {
            System.out.println("Entrada inválida. A string deve conter exatamente três partes separadas por ':'.");
            return null;
        }
    }

    public Boolean saveLicence(String key) {
        if (validateLicence(key) == true) {
            Options obj = new Options("companyKeyLicence", key, "1");
            return optionController.saveOption(obj);
        }
        return false;
    }

    public Boolean validateLicence(String encryptedData) {
        try {
            String decryptedData = decrypt(encryptedData);
//            System.out.println("Decrypted Data: " + decryptedData);
            String[] partes = separarString(decryptedData);

            // Verificando se a separação foi bem-sucedida e exibindo as partes
            if (partes != null) {
                System.out.println("Parte 1: " + partes[0]);
                System.out.println("Parte 2: " + partes[1]);
                System.out.println("Parte 3: " + partes[2]);

//                if (!companySession.getKeyLicence().equals(decryptedData)) {
                if (companySession.getNif().equals(partes[0])) {
                    // Chamando a função para comparar a data formato "30/12/2024";
                    if (compararData(partes[2]) == true) {

                        return true;
                    }
                }
//                }
            }
//            JOptionPane.showMessageDialog(null, "decryptedData!!" + decryptedData);
//            JOptionPane.showMessageDialog(null, "decryptedData:\n" + decryptedData + "\n encryptedData:\n" + encryptedData);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

//    public Options getOptions(String name) {
//        Options op = optionDao.searchFromName(name);
////        System.out.println("valor: " + op);
//        if (op == null) {
//        } else {
//            return op;
//        }
//        return null;
//    }
//    public Options getLicence() {
//        return getOptions("companyKeyLicence");
//    }
    public Boolean getStatusLicence() {
        String value = optionController.getOptionValue("companyKeyLicence");
        Boolean status = true;
//        if (!value.isEmpty()) {
//            status = validateLicence(value);
//            System.out.println("Licence:" + value);
//        }
        return status;
    }

    public String getStatusSoftware() {
        return optionController.getOptionValue("softwareStatus");
    }
}
