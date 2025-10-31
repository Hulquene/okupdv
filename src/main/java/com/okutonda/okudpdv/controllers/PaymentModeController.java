//package com.okutonda.okudpdv.controllers;
//
//import com.okutonda.okudpdv.data.dao.PaymentModeDao;
//import com.okutonda.okudpdv.data.entities.PaymentModes;
//import java.util.List;
//import java.util.Optional;
//
///**
// * Controller responsável pela lógica de negócio dos modos de pagamento com
// * Hibernate.
// *
// * Orquestra operações de CRUD e filtros.
// *
// * @author …
// */
//public class PaymentModeController {
//
//    private final PaymentModeDao dao;
//
//    public PaymentModeController() {
//        this.dao = new PaymentModeDao();
//    }
//
//    // ==========================================================
//    // 🔹 CRUD
//    // ==========================================================
//    /**
//     * Adiciona ou atualiza um modo de pagamento
//     */
//    public PaymentModes save(PaymentModes mode) {
//        if (mode == null) {
//            System.err.println("❌ Objeto inválido.");
//            return null;
//        }
//
//        try {
//            // Validações
//            if (!validarPaymentMode(mode)) {
//                return null;
//            }
//
//            PaymentModes savedMode;
//
//            if (mode.getId() == null || mode.getId() <= 0) {
//                // Validar duplicados antes de criar
//                if (mode.getName() != null && dao.nameExists(mode.getName())) {
//                    System.err.println("❌ Já existe um modo de pagamento com este nome: " + mode.getName());
//                    return null;
//                }
//
//                if (mode.getCode() != null && dao.codeExists(mode.getCode())) {
//                    System.err.println("❌ Já existe um modo de pagamento com este código: " + mode.getCode());
//                    return null;
//                }
//
//                savedMode = dao.save(mode);
//            } else {
//                savedMode = dao.update(mode);
//            }
//
//            System.out.println("✅ PaymentMode salvo: " + savedMode.getName());
//            return savedMode;
//
//        } catch (Exception e) {
//            System.err.println("❌ Erro ao salvar modo de pagamento: " + e.getMessage());
//            return null;
//        }
//    }
//
//    /**
//     * Remove modo de pagamento pelo ID
//     */
//    public boolean delete(Integer id) {
//        if (id == null || id <= 0) {
//            System.err.println("❌ ID inválido para exclusão.");
//            return false;
//        }
//
//        try {
//            // Verifica se é o modo padrão antes de excluir
//            Optional<PaymentModes> modeOpt = dao.findById(id);
//            if (modeOpt.isPresent() && modeOpt.get().getIsDefault() == 1) {
//                System.err.println("❌ Não é possível excluir o modo de pagamento padrão");
//                return false;
//            }
//
//            dao.delete(id);
//            System.out.println("✅ PaymentMode removido ID: " + id);
//            return true;
//
//        } catch (Exception e) {
//            System.err.println("❌ Erro ao remover modo de pagamento: " + e.getMessage());
//            return false;
//        }
//    }
//
//    // ==========================================================
//    // 🔹 CONSULTAS
//    // ==========================================================
//    public PaymentModes getById(Integer id) {
//        Optional<PaymentModes> modeOpt = dao.findById(id);
//        return modeOpt.orElse(null);
//    }
//
//    public PaymentModes getByName(String name) {
//        Optional<PaymentModes> modeOpt = dao.findByName(name);
//        return modeOpt.orElse(null);
//    }
//
//    public PaymentModes getByCode(String code) {
//        Optional<PaymentModes> modeOpt = dao.findByCode(code);
//        return modeOpt.orElse(null);
//    }
//
//    public PaymentModes getDefault() {
//        Optional<PaymentModes> modeOpt = dao.findDefault();
//        return modeOpt.orElse(null);
//    }
//
//    public List<PaymentModes> getAll() {
//        try {
//            return dao.findAll();
//        } catch (Exception e) {
//            System.err.println("❌ Erro ao buscar modos de pagamento: " + e.getMessage());
//            return List.of();
//        }
//    }
//
//    public List<PaymentModes> filter(String txt) {
//        try {
//            return dao.filter(txt);
//        } catch (Exception e) {
//            System.err.println("❌ Erro ao filtrar modos de pagamento: " + e.getMessage());
//            return List.of();
//        }
//    }
//
//    // ==========================================================
//    // 🔹 Métodos Específicos
//    // ==========================================================
//    /**
//     * Define um modo de pagamento como padrão
//     */
//    public boolean setDefaultPaymentMode(Integer modeId) {
//        try {
//            boolean success = dao.setDefaultPaymentMode(modeId);
//            if (success) {
//                System.out.println("✅ Modo de pagamento padrão definido: " + modeId);
//            }
//            return success;
//        } catch (Exception e) {
//            System.err.println("❌ Erro ao definir modo de pagamento padrão: " + e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * Busca apenas modos de pagamento ativos
//     */
//    public List<PaymentModes> getActiveModes() {
//        try {
//            return dao.findActive();
//        } catch (Exception e) {
//            System.err.println("❌ Erro ao buscar modos de pagamento ativos: " + e.getMessage());
//            return List.of();
//        }
//    }
//
//    /**
//     * Ativa/desativa um modo de pagamento
//     */
//    public boolean togglePaymentModeStatus(Integer id) {
//        try {
//            Optional<PaymentModes> modeOpt = dao.findById(id);
//            if (modeOpt.isPresent()) {
//                PaymentModes mode = modeOpt.get();
//                mode.setStatus(mode.getStatus() == 1 ? 0 : 1);
//                dao.update(mode);
//                System.out.println("✅ Status do PaymentMode atualizado: " + mode.getName());
//                return true;
//            }
//            return false;
//        } catch (Exception e) {
//            System.err.println("❌ Erro ao alterar status do modo de pagamento: " + e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * Verifica se nome já existe
//     */
//    public boolean nomeExiste(String nome) {
//        return dao.nameExists(nome);
//    }
//
//    /**
//     * Verifica se código já existe
//     */
//    public boolean codigoExiste(String codigo) {
//        return dao.codeExists(codigo);
//    }
//
//    // ==========================================================
//    // 🔹 Validações
//    // ==========================================================
//    private boolean validarPaymentMode(PaymentModes mode) {
//        if (mode == null) {
//            return false;
//        }
//
//        if (mode.getName() == null || mode.getName().trim().isEmpty()) {
//            System.err.println("❌ Nome do modo de pagamento é obrigatório");
//            return false;
//        }
//
//        if (mode.getName().length() > 50) {
//            System.err.println("❌ Nome muito longo (máximo 50 caracteres)");
//            return false;
//        }
//
//        if (mode.getCode() != null && mode.getCode().length() > 20) {
//            System.err.println("❌ Código muito longo (máximo 20 caracteres)");
//            return false;
//        }
//
//        return true;
//    }
//
//    /**
//     * Valida se um modo de pagamento pode ser excluído
//     */
//    public boolean podeExcluir(Integer id) {
//        Optional<PaymentModes> modeOpt = dao.findById(id);
//        if (modeOpt.isPresent()) {
//            PaymentModes mode = modeOpt.get();
//            return mode.getIsDefault() != 1; // Não pode excluir o padrão
//        }
//        return false;
//    }
//}
