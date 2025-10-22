/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.OptionsDao;
import com.okutonda.okudpdv.data.entities.Options;
import java.util.List;

/**
 *
 * @author kenny
 */
public class OptionController {

    private final OptionsDao dao;

    public OptionController() {
        this.dao = new OptionsDao();
    }

    // =========================
    // 🔹 Regras de negócio
    // =========================
    /**
     * Retorna o objeto Options completo pelo nome.
     */
    public Options getOption(String name) {
        return dao.findByName(name);
    }

    /**
     * Retorna o valor da opção (ou string vazia se não existir).
     */
    public String getOptionValue(String name) {
        String value = dao.findValueByName(name);
        return (value != null) ? value : "";
    }

    /**
     * Cria ou atualiza a opção automaticamente. (Se existir → atualiza, se não
     * → insere)
     */
    public boolean saveOption(Options option) {
        return dao.saveOrUpdate(option);
    }

    /**
     * Remove uma opção pelo ID.
     */
    public boolean deleteOption(int id) {
        return dao.delete(id);
    }

    /**
     * Lista todas as opções do sistema.
     */
    public List<Options> getAllOptions() {
        return dao.findAll();
    }
}
