package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.FinanceDao;
import com.okutonda.okudpdv.data.entities.*;
import com.okutonda.okudpdv.helpers.UserSession;
import java.util.List;

/**
 * Controller de gestão financeira: - Contas a Pagar / Receber - Fluxo de Caixa
 * - Receitas e Despesas
 *
 * Responsável por aplicar lógica de negócio sobre o DAO.
 *
 * @author Hulquene
 */
public class FinanceController {

    private final FinanceDao dao;
    private final UserSession session = UserSession.getInstance();

    public FinanceController() {
        this.dao = new FinanceDao();
    }

    // ==========================================================
    // 🔹 CONTAS A RECEBER
    // ==========================================================
    public List<Order> getContasAReceber() {
        // Futuro: filtrar por vendedor logado se perfil != admin
        return dao.listContasAReceber();
    }

    // ==========================================================
    // 🔹 CONTAS A PAGAR
    // ==========================================================
    public List<Purchase> getContasAPagar() {
        return dao.listContasAPagar();
    }

    // ==========================================================
    // 🔹 HISTÓRICO DE VENDAS
    // ==========================================================
    public List<Order> getHistoricoVendas() {
        return dao.listHistoricoVendas();
    }

    // ==========================================================
    // 🔹 FLUXO DE CAIXA / RECEITAS / DESPESAS
    // ==========================================================
    public List<Payment> getFluxoCaixa(String from, String to) {
        return dao.listFluxoCaixa(from, to);
    }

    public List<Payment> getReceitas(String from, String to) {
        return dao.listReceitas(from, to);
    }

    public double getTotalReceitas(String from, String to) {
        return dao.getTotalReceitas(from, to);
    }

    public List<Expense> getDespesas(String from, String to) {
        return dao.listDespesas(from, to);
    }
}
