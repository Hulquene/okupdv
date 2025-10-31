package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.OptionsDao;
import com.okutonda.okudpdv.data.entities.Options;
import java.util.List;
import java.util.Optional;

/**
 * Controlador de Opções com Hibernate.
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
        Optional<Options> optionOpt = dao.findByName(name);
        return optionOpt.orElse(null);
    }

    /**
     * Retorna o valor da opção (ou string vazia se não existir).
     */
    public String getOptionValue(String name) {
        String value = dao.findValueByName(name);
        return (value != null) ? value : "";
    }

    /**
     * Retorna o valor da opção como boolean.
     */
    public boolean getOptionValueAsBoolean(String name) {
        String value = getOptionValue(name);
        return "true".equalsIgnoreCase(value) || "1".equals(value);
    }

    /**
     * Retorna o valor da opção como inteiro.
     */
    public int getOptionValueAsInt(String name, int defaultValue) {
        try {
            String value = getOptionValue(name);
            return value.isEmpty() ? defaultValue : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Cria ou atualiza a opção automaticamente. (Se existir → atualiza, se não
     * → insere)
     */
    public boolean saveOption(Options option) {
        try {
            dao.saveOrUpdate(option);
            System.out.println("✅ Option salva: " + option.getName());
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar option: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método conveniente para salvar opção por nome e valor.
     */
    public boolean saveOption(String name, String value, String status) {
        Options option = new Options(name, value, status);
        return saveOption(option);
    }

    /**
     * Remove uma opção pelo ID.
     */
    public boolean deleteOption(Integer id) {
        try {
            dao.delete(id);
            System.out.println("✅ Option removida ID: " + id);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erro ao remover option: " + e.getMessage());
            return false;
        }
    }

    /**
     * Remove uma opção pelo nome.
     */
    public boolean deleteOptionByName(String name) {
        Optional<Options> optionOpt = dao.findByName(name);
        if (optionOpt.isPresent()) {
            return deleteOption(optionOpt.get().getId());
        }
        return false;
    }

    /**
     * Lista todas as opções do sistema.
     */
    public List<Options> getAllOptions() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar options: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Verifica se a instalação está completa.
     */
    public boolean isInstallationComplete() {
        return getOptionValueAsBoolean("install_complete");
    }

    /**
     * Marca a instalação como completa.
     */
    public boolean markInstallationComplete() {
        return saveOption("install_complete", "true", "active");
    }
}

///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.okutonda.okudpdv.controllers;
//
//import com.okutonda.okudpdv.data.dao.OptionsDao;
//import com.okutonda.okudpdv.data.entities.Options;
//import java.util.List;
//
///**
// *
// * @author kenny
// */
//public class OptionController {
//
//    private final OptionsDao dao;
//
//    public OptionController() {
//        this.dao = new OptionsDao();
//    }
//
//    // =========================
//    // 🔹 Regras de negócio
//    // =========================
//    /**
//     * Retorna o objeto Options completo pelo nome.
//     */
//    public Options getOption(String name) {
//        return dao.findByName(name);
//    }
//
//    /**
//     * Retorna o valor da opção (ou string vazia se não existir).
//     */
//    public String getOptionValue(String name) {
//        String value = dao.findValueByName(name);
//        return (value != null) ? value : "";
//    }
//
//    /**
//     * Cria ou atualiza a opção automaticamente. (Se existir → atualiza, se não
//     * → insere)
//     */
//    public boolean saveOption(Options option) {
//        return dao.saveOrUpdate(option);
//    }
//
//    /**
//     * Remove uma opção pelo ID.
//     */
//    public boolean deleteOption(int id) {
//        return dao.delete(id);
//    }
//
//    /**
//     * Lista todas as opções do sistema.
//     */
//    public List<Options> getAllOptions() {
//        return dao.findAll();
//    }
//}
