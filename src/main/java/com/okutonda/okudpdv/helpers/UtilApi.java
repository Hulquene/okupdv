/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.helpers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 *
 * @author kenny
 */
public class UtilApi {

    public String get() {
        try {
            // Cria um cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Cria uma requisição GET com cabeçalho
            HttpRequest request = HttpRequest.newBuilder()
                    //                    .uri(new URI("https://jsonplaceholder.typicode.com/posts/1"))
                    .uri(new URI("http://localhost:8080/pessoa"))
                    .header("User-Agent", "Java HttpClient") // Adiciona um cabeçalho
                    .header("Accept", "application/json") // Cabeçalho de exemplo adicional
                    .GET()
                    .build();

            // Envia a requisição e obtém a resposta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // Imprime o corpo da resposta
            System.out.println(response.body());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getLicence() {
        try {
            // Cria um cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Cria uma requisição GET com cabeçalho
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://jsonplaceholder.typicode.com/posts/1"))
                    .header("User-Agent", "Java HttpClient") // Adiciona um cabeçalho
                    .header("Accept", "application/json") // Cabeçalho de exemplo adicional
                    .GET()
                    .build();

            // Envia a requisição e obtém a resposta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Imprime o corpo da resposta
            System.out.println(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
