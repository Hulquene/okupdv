/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.ExpenseCategoryDao;
import com.okutonda.okudpdv.data.entities.ExpenseCategory;

import java.util.List;

/**
 *
 * @author rog
 */
public class ExpenseCategoryController {

    private final ExpenseCategoryDao dao;

    public ExpenseCategoryController() {
        this.dao = new ExpenseCategoryDao();
    }

    public void save(ExpenseCategory c) {
        if (c.getId() == null) {
            dao.insert(c);
        } else {
            dao.update(c);
        }
    }

    public void delete(int id) {
        dao.delete(id);
    }

    public List<ExpenseCategory> getAll() {
        return dao.getAll();
    }
}
