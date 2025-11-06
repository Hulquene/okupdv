package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.PaymentDao;
import com.okutonda.okudpdv.data.entities.Payment;
import com.okutonda.okudpdv.data.entities.PaymentMode;
import com.okutonda.okudpdv.data.entities.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controller responsável pela lógica de alto nível dos pagamentos com
 * Hibernate.
 *
 * Atua como intermediário entre a interface e o DAO. Fornece métodos para CRUD,
 * filtragem e relatórios.
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
     * Adiciona um novo pagamento.
     */
    public Payment add(Payment payment) {
        if (payment == null) {
            System.err.println("❌ Falha ao adicionar: payment é nulo.");
            return null;
        }

        try {
            // Validações básicas
            if (!validarPayment(payment)) {
                return null;
            }

            Payment savedPayment = dao.save(payment);
            System.out.println("✅ Pagamento adicionado: " + savedPayment.getReference());
            return savedPayment;

        } catch (Exception e) {
            System.err.println("❌ Erro ao adicionar pagamento: " + e.getMessage());
            return null;
        }
    }

    /**
     * Adiciona um novo pagamento vinculado a uma fatura/ordem.
     */
    public Payment add(Payment payment, Integer invoiceId) {
        if (invoiceId == null || invoiceId <= 0) {
            System.err.println("❌ Falha ao adicionar: invoiceId inválido.");
            return null;
        }

        payment.setInvoiceId(invoiceId);
        return add(payment);
    }

    /**
     * Atualiza um pagamento existente.
     */
    public Payment edit(Payment payment) {
        if (payment == null || payment.getId() == null || payment.getId() <= 0) {
            System.err.println("❌ Falha ao editar: ID inválido.");
            return null;
        }

        try {
            Payment updatedPayment = dao.update(payment);
            System.out.println("✅ Pagamento atualizado: " + updatedPayment.getReference());
            return updatedPayment;

        } catch (Exception e) {
            System.err.println("❌ Erro ao atualizar pagamento: " + e.getMessage());
            return null;
        }
    }

    /**
     * Exclui um pagamento pelo ID.
     */
    public boolean delete(Integer id) {
        if (id == null || id <= 0) {
            System.err.println("❌ ID inválido para exclusão.");
            return false;
        }

        try {
            dao.delete(id);
            System.out.println("✅ Pagamento removido ID: " + id);
            return true;

        } catch (Exception e) {
            System.err.println("❌ Erro ao remover pagamento: " + e.getMessage());
            return false;
        }
    }

    // ==========================================================
    // 🔹 CONSULTAS
    // ==========================================================
    public Payment getById(Integer id) {
        Optional<Payment> paymentOpt = dao.findById(id);
        return paymentOpt.orElse(null);
    }

    public Payment getByReference(String ref) {
        Optional<Payment> paymentOpt = dao.findByReference(ref);
        return paymentOpt.orElse(null);
    }

    public List<Payment> getAll() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar pagamentos: " + e.getMessage());
            return List.of();
        }
    }

    public List<Payment> filter(String txt) {
        try {
            return dao.filter(txt);
        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar pagamentos: " + e.getMessage());
            return List.of();
        }
    }

    public List<Payment> filterByDate(LocalDate from, LocalDate to) {
        try {
            return dao.filterByDate(from, to);
        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar pagamentos por data: " + e.getMessage());
            return List.of();
        }
    }

    // ==========================================================
    // 🔹 Métodos Específicos
    // ==========================================================
    /**
     * Busca pagamentos por fatura/ordem
     */
    public List<Payment> getByInvoiceId(Integer invoiceId) {
        try {
            return dao.findByInvoiceId(invoiceId);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar pagamentos por invoiceId: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Busca pagamentos por status
     */
    public List<Payment> getByStatus(PaymentStatus status) {
        try {
            return dao.findByStatus(status);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar pagamentos por status: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Busca pagamentos por modo de pagamento (ATUALIZADO)
     */
    public List<Payment> getByPaymentMode(PaymentMode paymentMode) {
        try {
            return dao.findByPaymentMode(paymentMode);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar pagamentos por modo: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Busca pagamentos por código do modo de pagamento (NOVO MÉTODO)
     */
    public List<Payment> getByPaymentMode(String codigoModo) {
        try {
            PaymentMode paymentMode = PaymentMode.fromCodigo(codigoModo);
            return dao.findByPaymentMode(paymentMode);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar pagamentos por código do modo: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Calcula o total de pagamentos em um período
     */
    public Double calcularTotalPorPeriodo(LocalDate from, LocalDate to) {
        try {
            return dao.calculateTotalByPeriod(from, to);
        } catch (Exception e) {
            System.err.println("❌ Erro ao calcular total de pagamentos: " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * Calcula o total de pagamentos por modo de pagamento em um período (NOVO
     * MÉTODO)
     */
    public Double calcularTotalPorModoEPeriodo(PaymentMode paymentMode, LocalDate from, LocalDate to) {
        try {
            List<Payment> pagamentos = dao.findByPaymentMode(paymentMode);
            return pagamentos.stream()
                    .filter(p -> isDataNoPeriodo(p.getDate(), from, to))
                    .filter(p -> p.getStatus() == PaymentStatus.PAGO)
                    .mapToDouble(p -> p.getTotal().doubleValue())
                    .sum();
        } catch (Exception e) {
            System.err.println("❌ Erro ao calcular total por modo e período: " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * Altera o status de um pagamento
     */
    public boolean alterarStatus(Integer paymentId, PaymentStatus novoStatus) {
        try {
            Optional<Payment> paymentOpt = dao.findById(paymentId);
            if (paymentOpt.isPresent()) {
                Payment payment = paymentOpt.get();
                payment.setStatus(novoStatus);
                dao.update(payment);
                System.out.println("✅ Status do pagamento atualizado: " + paymentId + " -> " + novoStatus);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("❌ Erro ao alterar status do pagamento: " + e.getMessage());
            return false;
        }
    }

    /**
     * Altera o modo de pagamento (NOVO MÉTODO)
     */
    public boolean alterarModoPagamento(Integer paymentId, PaymentMode novoModo) {
        try {
            Optional<Payment> paymentOpt = dao.findById(paymentId);
            if (paymentOpt.isPresent()) {
                Payment payment = paymentOpt.get();
                payment.setPaymentMode(novoModo);
                dao.update(payment);
                System.out.println("✅ Modo de pagamento atualizado: " + paymentId + " -> " + novoModo);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("❌ Erro ao alterar modo de pagamento: " + e.getMessage());
            return false;
        }
    }

    // ==========================================================
    // 🔹 Validações
    // ==========================================================
    private boolean validarPayment(Payment payment) {
        if (payment == null) {
            return false;
        }

        if (payment.getTotal() == null || payment.getTotal().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            System.err.println("❌ Total do pagamento deve ser maior que zero");
            return false;
        }

        if (payment.getReference() == null || payment.getReference().trim().isEmpty()) {
            System.err.println("❌ Referência do pagamento é obrigatória");
            return false;
        }

        if (payment.getDate() == null || payment.getDate().trim().isEmpty()) {
            System.err.println("❌ Data do pagamento é obrigatória");
            return false;
        }

        if (payment.getPaymentMode() == null) {
            System.err.println("❌ Modo de pagamento é obrigatório");
            return false;
        }

        if (payment.getStatus() == null) {
            payment.setStatus(PaymentStatus.PAGO);
        }

        return true;
    }

    /**
     * Verifica se uma referência já existe
     */
    public boolean referenciaExiste(String referencia) {
        return getByReference(referencia) != null;
    }

    /**
     * Gera uma referência única para pagamento
     */
    public String gerarReferenciaUnica() {
        String base = "PAY-" + System.currentTimeMillis();
        int counter = 1;
        String referencia = base;

        while (referenciaExiste(referencia)) {
            referencia = base + "-" + counter;
            counter++;
        }

        return referencia;
    }

    /**
     * Valida se um código de modo de pagamento é válido (NOVO MÉTODO)
     */
    public boolean validarCodigoModoPagamento(String codigo) {
        return PaymentMode.isValidCodigo(codigo);
    }

    /**
     * Obtém todos os modos de pagamento ativos (NOVO MÉTODO)
     */
    public PaymentMode[] getModosPagamentoAtivos() {
        return PaymentMode.getActiveModes();
    }

    /**
     * Obtém os modos de pagamento mais comuns (NOVO MÉTODO)
     */
    public PaymentMode[] getModosPagamentoComuns() {
        return PaymentMode.getCommonModes();
    }

    /**
     * Converte string para PaymentMode (NOVO MÉTODO)
     */
    public PaymentMode converterParaPaymentMode(String valor) {
        return PaymentMode.fromString(valor);
    }

    /**
     * Converte código para PaymentMode (NOVO MÉTODO)
     */
    public PaymentMode converterCodigoParaPaymentMode(String codigo) {
        return PaymentMode.fromCodigo(codigo);
    }

    // ==========================================================
    // 🔹 Utilitários
    // ==========================================================
    private boolean isDataNoPeriodo(String dataString, LocalDate from, LocalDate to) {
        try {
            if (dataString == null || dataString.trim().isEmpty()) {
                return false;
            }
            LocalDate data = LocalDate.parse(dataString.substring(0, 10));
            return !data.isBefore(from) && !data.isAfter(to);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Cria um pagamento rápido com validação (NOVO MÉTODO)
     */
    public Payment criarPagamentoRapido(String descricao, BigDecimal total, String referencia, PaymentMode modo) {
        Payment payment = new Payment();
        payment.setDescription(descricao);
        payment.setTotal(total);
        payment.setReference(referencia);
        payment.setPaymentMode(modo);
        payment.setDate(java.time.LocalDate.now().toString());
        payment.setStatus(PaymentStatus.PAGO);

        return add(payment);
    }

    /**
     * Obtém estatísticas de pagamentos por modo (NOVO MÉTODO)
     */
    public java.util.Map<PaymentMode, Double> getEstatisticasPorModo(LocalDate from, LocalDate to) {
        java.util.Map<PaymentMode, Double> estatisticas = new java.util.HashMap<>();

        for (PaymentMode modo : PaymentMode.getActiveModes()) {
            Double total = calcularTotalPorModoEPeriodo(modo, from, to);
            estatisticas.put(modo, total);
        }

        return estatisticas;
    }
}
