package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.PaymentDao;
import com.okutonda.okudpdv.data.entities.Payment;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller responsável pela lógica de alto nível dos pagamentos.
 *
 * Atua como intermediário entre a interface e o DAO, seguindo o padrão de
 * arquitetura baseado em BaseDao + DatabaseProvider.
 *
 * Fornece métodos para CRUD, filtragem e relatórios.
 *
 * @author …
 */
public class PaymentController {

    private final PaymentDao dao;

    public PaymentController() {
        this.dao = new PaymentDao();
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    /**
     * Adiciona um novo pagamento vinculado a uma fatura/ordem.
     *
     * @param payment objeto Payment preenchido
     * @param invoiceId id da fatura ou ordem associada
     * @return true se inserido com sucesso
     */
    public boolean add(Payment payment, int invoiceId) {
        if (invoiceId <= 0 || payment == null) {
            System.err.println("[PaymentController] Falha ao adicionar: dados inválidos.");
            return false;
        }
        return dao.add(payment, invoiceId);
    }

    /**
     * Atualiza um pagamento existente.
     *
     * @param payment objeto Payment com ID válido
     * @return true se atualizado
     */
    public boolean edit(Payment payment) {
        if (payment == null || payment.getId() <= 0) {
            System.err.println("[PaymentController] Falha ao editar: ID inválido.");
            return false;
        }
        return dao.update(payment);
    }

    /**
     * Exclui um pagamento pelo ID.
     *
     * @param id identificador do pagamento
     * @return true se excluído
     */
    public boolean delete(int id) {
        if (id <= 0) {
            System.err.println("[PaymentController] ID inválido para exclusão.");
            return false;
        }
        return dao.delete(id);
    }

    // ==========================================================
    // 🔹 CONSULTAS
    // ==========================================================
    /**
     * Obtém pagamento por ID.
     */
    public Payment getById(int id) {
        return dao.findById(id);
    }

    /**
     * Obtém pagamento pela referência (ex: código de transação).
     */
    public Payment getByReference(String ref) {
        return dao.findByReference(ref);
    }

    /**
     * Lista todos os pagamentos.
     */
    public List<Payment> getAll() {
        return dao.findAll();
    }

    /**
     * Lista pagamentos com cláusula WHERE customizada.
     */
    public List<Payment> get(String where) {
        return dao.list(where);
    }

    /**
     * Filtra pagamentos por texto (referência, data, prefixo, descrição, etc.).
     */
    public List<Payment> filter(String txt) {
        return dao.filter(txt);
    }

    /**
     * Filtra pagamentos entre duas datas (ex: para relatórios financeiros).
     */
    public List<Payment> filterDate(LocalDate from, LocalDate to) {
        return dao.filterDate(from, to);
    }
}
