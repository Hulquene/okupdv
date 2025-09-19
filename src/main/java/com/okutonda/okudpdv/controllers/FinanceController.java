/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.dao.FinanceDao;
import com.okutonda.okudpdv.models.Expense;
import com.okutonda.okudpdv.models.Order;
import com.okutonda.okudpdv.models.Payment;
import com.okutonda.okudpdv.models.Purchase;
import com.okutonda.okudpdv.utilities.UserSession;

import java.util.List;

/**
 *
 * @author rog
 */
public class FinanceController {

    private final FinanceDao dao;
    private final UserSession session = UserSession.getInstance();

    public FinanceController() {
        this.dao = new FinanceDao();
    }

    // Contas a receber (considerando perfil do usuário logado)
    public List<Order> getContasAReceber() {
        if (session.getUser().getProfile().equals("admin") || session.getUser().getProfile().equals("manager")) {
            return dao.listContasAReceber();
        }
        // no futuro podes adaptar para filtrar só as faturas do vendedor logado
        return dao.listContasAReceber();
    }
    // Contas a pagar
// Contas a pagar

    public List<Purchase> getContasAPagar() {
        if (session.getUser().getProfile().equals("admin") || session.getUser().getProfile().equals("manager")) {
            return dao.listContasAPagar();
        }
        // no futuro: filtrar compras por usuário responsável
        return dao.listContasAPagar();
    }

    // Histórico de vendas
    public List<Order> getHistoricoVendas() {
        return dao.listHistoricoVendas();
    }

    // Fluxo de caixa num intervalo de datas
    public List<Payment> getFluxoCaixa(String dateFrom, String dateTo) {
        return dao.listFluxoCaixa(dateFrom, dateTo);
    }

    // Total de receitas no período
    public double getTotalReceitas(String dateFrom, String dateTo) {
        return dao.getTotalReceitas(dateFrom, dateTo);
    }

    public List<Payment> getReceitas(String dateFrom, String dateTo) {
        return dao.listReceitas(dateFrom, dateTo);
    }

    public List<Expense> getDespesas(String dateFrom, String dateTo) {
        return dao.listDespesas(dateFrom, dateTo);
    }

}
