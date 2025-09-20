/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.dao.StockMovementDao;
import com.okutonda.okudpdv.models.StockMovement;

import java.util.List;

/**
 *
 * @author hr
 */
public class StockMovementController {

    private final StockMovementDao dao;

    public StockMovementController() {
        this.dao = new StockMovementDao();
    }

    // Registrar movimento
    public boolean registrar(StockMovement movimento) {
        return dao.add(movimento);
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
    // Registrar movimento

}
