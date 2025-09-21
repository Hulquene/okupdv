/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.dao.StockMovementDao;
import com.okutonda.okudpdv.models.StockMovement;
import com.okutonda.okudpdv.utilities.UserSession;
import com.okutonda.okudpdv.utilities.UtilDate;

import java.util.List;

/**
 *
 * @author hr
 */
public class StockMovementController {

    private final StockMovementDao dao;
    UserSession session = UserSession.getInstance();

    public StockMovementController() {
        this.dao = new StockMovementDao();
    }

    // Listar movimentos por produto
    public List<StockMovement> listarPorProduto(int productId) {
        return dao.listByProduct(productId);
    }

    // Obter stock atual
    public int getStockAtual(int productId) {
        return dao.getStockAtual(productId);
    }

    // Listar todos os movimentos (ex: auditoria)
    public List<StockMovement> listarTodos() {
        return dao.listAll();
    }

    // Registrar movimento com regra de negócio
    public boolean registrar(StockMovement movimento) {
        int qtd = movimento.getQuantity();

        switch (movimento.getType().toUpperCase()) {
            case "SAIDA":
                int atual = dao.getStockAtual(movimento.getProduct().getId());
                if (atual < qtd) {
                    throw new IllegalArgumentException("Stock insuficiente para o produto!");
                }
                movimento.setQuantity(-qtd); // saída negativa
                break;

            case "TRANSFERENCIA":
                // saída no armazém origem
                int stockOrigem = dao.getStockAtual(movimento.getProduct().getId());
                if (stockOrigem < qtd) {
                    throw new IllegalArgumentException("Stock insuficiente no armazém origem!");
                }
                // aqui podes gravar dois movimentos: saída (origem) + entrada (destino)
                break;

            case "ENTRADA":
            case "AJUSTE":
                // só regista
                break;
        }

        return dao.add(movimento);
    }
}
