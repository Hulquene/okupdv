/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.utilities;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 *
 * @author kenny
 */
public class UtilSaft {

    public static String encrypt(String data, String privateKeyPEM) throws Exception {
        // Remover cabeçalhos e rodapés do PEM
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        // Decodificar o PEM para obter o byte array da chave privada
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyPEM);

        // Converter bytes para chave privada usando KeyFactory
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        // Configurar o objeto Signature com o algoritmo SHA1 com RSA
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes("UTF-8"));

        // Assinar os dados
        byte[] signedData = signature.sign();

        // Codificar a assinatura em Base64
        return Base64.getEncoder().encodeToString(signedData);
    }

    public static String appGenerateHashInvoice(String invoiceDate, String systemEntryDate, String invoiceNo, String grossTotal,
            String previousHash) {

        String privateKeyPEM;
        String stringToEncrypt;

        if (previousHash == null || previousHash.isEmpty()) {
            stringToEncrypt = String.join(";", invoiceDate, systemEntryDate, invoiceNo, grossTotal);
        } else {
            stringToEncrypt = String.join(";", invoiceDate, systemEntryDate, invoiceNo, grossTotal, previousHash);
        }
        return stringToEncrypt;
//        return encrypt(stringToEncrypt, privateKeyPEM);
    }

    public static int opensslVerifySignature(String data, String signatureBase64, String publicKeyPEM) {
        try {
            // Remover cabeçalhos e rodapés do PEM
            publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            // Decodificar o PEM para obter o byte array da chave pública
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyPEM);

            // Converter bytes para chave pública usando KeyFactory
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // Decodificar a assinatura base64
            byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);

            // Configurar o objeto Signature com o algoritmo SHA1 com RSA
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(publicKey);
            signature.update(data.getBytes("UTF-8"));

            // Verificar a assinatura
            boolean isValid = signature.verify(signatureBytes);

            return isValid ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Retorna -1 em caso de erro
        }
    }
}
