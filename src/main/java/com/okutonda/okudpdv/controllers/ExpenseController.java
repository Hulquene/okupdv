/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.dao.ExpenseDao;
import com.okutonda.okudpdv.models.Expense;

import java.util.List;

/**
 *
 * @author rog
 */
public class ExpenseController {

    private final ExpenseDao dao;

    public ExpenseController() {
        this.dao = new ExpenseDao();
    }

    public List<Expense> getDespesas(String dateFrom, String dateTo) {
        return dao.listDespesas(dateFrom, dateTo);
    }
}
