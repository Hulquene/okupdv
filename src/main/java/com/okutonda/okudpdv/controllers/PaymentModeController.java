package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.PaymentModeDao;
import com.okutonda.okudpdv.data.entities.PaymentModes;
import java.util.List;

/**
 * Controller responsável pela lógica de negócio dos modos de pagamento.
 *
 * Orquestra operações de CRUD e filtros, utilizando o PaymentModeDao.
 *
 * Totalmente compatível com o novo padrão BaseDao + DatabaseProvider.
 *
 * @author …
 */
public class PaymentModeController {

    private final PaymentModeDao dao;

    public PaymentModeController() {
        this.dao = new PaymentModeDao();
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    /**
     * Adiciona ou atualiza um modo de pagamento
     */
    public boolean save(PaymentModes mode) {
        if (mode == null) {
            System.err.println("[PaymentModeController] Objeto inválido.");
            return false;
        }
        if (mode.getId() > 0) {
            return dao.update(mode);
        }
        return dao.add(mode);
    }

    /**
     * Remove modo de pagamento pelo ID
     */
    public boolean delete(int id) {
        if (id <= 0) {
            System.err.println("[PaymentModeController] ID inválido para exclusão.");
            return false;
        }
        return dao.delete(id);
    }

    // ==========================================================
    // 🔹 CONSULTAS
    // ==========================================================
    /**
     * Obtém modo de pagamento por ID
     */
    public PaymentModes getById(int id) {
        return dao.findById(id);
    }

    /**
     * Obtém modo de pagamento pelo nome
     */
    public PaymentModes getByName(String name) {
        return dao.findByName(name);
    }

    /**
     * Obtém modo de pagamento pelo código
     */
    public PaymentModes getByCode(String code) {
        return dao.findByCode(code);
    }

    /**
     * Obtém modo de pagamento padrão (isDefault=1)
     */
    public PaymentModes getDefault() {
        return dao.findDefault();
    }

    /**
     * Lista todos os modos de pagamento
     */
    public List<PaymentModes> getAll() {
        return dao.findAll();
    }

    /**
     * Filtra modos de pagamento por texto
     */
    public List<PaymentModes> filter(String txt) {
        return dao.filter(txt);
    }
}
