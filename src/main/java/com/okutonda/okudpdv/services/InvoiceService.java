package com.okutonda.okudpdv.services;

import com.okutonda.okudpdv.data.dao.InvoiceDao;
import com.okutonda.okudpdv.data.entities.Invoices;
import com.okutonda.okudpdv.data.entities.Clients;
import com.okutonda.okudpdv.data.entities.User;
import com.okutonda.okudpdv.helpers.UserSession;
import com.okutonda.okudpdv.helpers.UtilDate;
import com.okutonda.okudpdv.helpers.PrintHelper;
import com.okutonda.okudpdv.helpers.UtilSaft;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;

/**
 * Service layer para gestão de Faturas (Invoices)
 */
public class InvoiceService {

    private final InvoiceDao invoiceDao;
    private final UserSession userSession;

    public InvoiceService() {
        this.invoiceDao = new InvoiceDao();
        this.userSession = UserSession.getInstance();
    }

    // ==========================================================
    // 🔹 OPERAÇÕES PRINCIPAIS
    // ==========================================================
    /**
     * Cria uma nova fatura
     */
    public Invoices criarFatura(Invoices fatura) {
        try {
            validarFatura(fatura);
            prepararDadosFatura(fatura);

            Invoices faturaSalva = invoiceDao.save(fatura);
            showSuccessMessage("Fatura criada com sucesso!\nNúmero: "
                    + faturaSalva.getPrefix() + "/" + faturaSalva.getNumber());
            return faturaSalva;

        } catch (Exception e) {
            handleError("Erro ao criar fatura", e);
            throw new RuntimeException("Erro ao criar fatura", e);
        }
    }

    /**
     * Emite uma fatura (muda status para emitida)
     */
    public Invoices emitirFatura(Integer id) {
        try {
            Invoices fatura = buscarPorId(id);
            if (fatura == null) {
                throw new IllegalArgumentException("Fatura não encontrada");
            }

            if (fatura.isEmitida()) {
                throw new IllegalStateException("Fatura já está emitida");
            }

            if (fatura.isAnulada()) {
                throw new IllegalStateException("Fatura anulada não pode ser emitida");
            }

            fatura.setStatus(2); // Emitida
            fatura.setHash(gerarHashFatura(fatura));

            return invoiceDao.update(fatura);

        } catch (Exception e) {
            handleError("Erro ao emitir fatura", e);
            throw new RuntimeException("Erro ao emitir fatura", e);
        }
    }

    /**
     * Marca fatura como paga
     */
    public Invoices marcarComoPaga(Integer id) {
        try {
            Invoices fatura = buscarPorId(id);
            if (fatura == null) {
                throw new IllegalArgumentException("Fatura não encontrada");
            }

            if (!fatura.isEmitida()) {
                throw new IllegalStateException("Apenas faturas emitidas podem ser marcadas como pagas");
            }

            fatura.setStatus(3); // Paga
            return invoiceDao.update(fatura);

        } catch (Exception e) {
            handleError("Erro ao marcar fatura como paga", e);
            throw new RuntimeException("Erro ao marcar fatura como paga", e);
        }
    }

    /**
     * Anula uma fatura
     */
    public Invoices anularFatura(Integer id) {
        try {
            Invoices fatura = buscarPorId(id);
            if (fatura == null) {
                throw new IllegalArgumentException("Fatura não encontrada");
            }

            if (fatura.isPaga()) {
                throw new IllegalStateException("Fatura paga não pode ser anulada");
            }

            fatura.setStatus(4); // Anulada
            return invoiceDao.update(fatura);

        } catch (Exception e) {
            handleError("Erro ao anular fatura", e);
            throw new RuntimeException("Erro ao anular fatura", e);
        }
    }

    /**
     * Atualiza uma fatura
     */
    public Invoices atualizarFatura(Invoices fatura) {
        try {
            validarFatura(fatura);
            return invoiceDao.update(fatura);

        } catch (Exception e) {
            handleError("Erro ao atualizar fatura", e);
            throw new RuntimeException("Erro ao atualizar fatura", e);
        }
    }

    /**
     * Exclui uma fatura
     */
    public void excluirFatura(Integer id) {
        try {
            Invoices fatura = buscarPorId(id);
            if (fatura == null) {
                throw new IllegalArgumentException("Fatura não encontrada");
            }

            if (fatura.isEmitida() || fatura.isPaga()) {
                throw new IllegalStateException("Não é possível excluir faturas emitidas ou pagas");
            }

            invoiceDao.delete(id);
            showSuccessMessage("Fatura excluída com sucesso!");

        } catch (Exception e) {
            handleError("Erro ao excluir fatura", e);
            throw new RuntimeException("Erro ao excluir fatura", e);
        }
    }

    // ==========================================================
    // 🔹 CONSULTAS
    // ==========================================================
    public Invoices buscarPorId(Integer id) {
        return invoiceDao.findById(id).orElse(null);
    }

    public List<Invoices> listarTodas() {
        return invoiceDao.findAll();
    }

    public List<Invoices> filtrar(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return listarTodas();
        }
        return invoiceDao.filter(texto.trim());
    }

    public List<Invoices> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        validarPeriodo(dataInicio, dataFim);
        return invoiceDao.filterByDate(dataInicio, dataFim);
    }

    public List<Invoices> listarPorCliente(Integer clienteId) {
        if (clienteId == null || clienteId <= 0) {
            throw new IllegalArgumentException("ID do cliente inválido");
        }
        return invoiceDao.findByClientId(clienteId);
    }

    public List<Invoices> listarPorVendedor(Integer vendedorId) {
        if (vendedorId == null || vendedorId <= 0) {
            throw new IllegalArgumentException("ID do vendedor inválido");
        }
        return invoiceDao.findBySellerId(vendedorId);
    }

    public List<Invoices> listarPorPrefixo(String prefixo) {
        if (prefixo == null || prefixo.trim().isEmpty()) {
            throw new IllegalArgumentException("Prefixo inválido");
        }
        return invoiceDao.findByPrefix(prefixo.trim());
    }

    public List<Invoices> listarPendentes() {
        return invoiceDao.findPendentes();
    }

    public List<Invoices> listarEmitidas() {
        return invoiceDao.findEmitidas();
    }

    public List<Invoices> listarPagas() {
        return invoiceDao.findPagas();
    }

    public List<Invoices> listarAnuladas() {
        return invoiceDao.findAnuladas();
    }

    public List<Invoices> listarComVencimentoProximo(LocalDate data) {
        return invoiceDao.findComVencimentoProximo(data);
    }

    // ==========================================================
    // 🔹 CÁLCULOS E ESTATÍSTICAS
    // ==========================================================
    public BigDecimal calcularTotalFaturas(List<Invoices> faturas) {
        if (faturas == null || faturas.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return faturas.stream()
                .map(Invoices::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        validarPeriodo(dataInicio, dataFim);
        Double total = invoiceDao.calculateTotalSalesByPeriod(dataInicio, dataFim);
        return BigDecimal.valueOf(total != null ? total : 0.0);
    }

    public Integer obterProximoNumero(String prefixo) {
        return invoiceDao.getNextNumber(prefixo);
    }

    public EstatisticasFaturas calcularEstatisticas(LocalDate from, LocalDate to) {
        try {
            Double totalVendas = invoiceDao.calculateTotalSalesByPeriod(from, to);
            Long faturasEmitidas = invoiceDao.countByStatus(2);
            Long faturasPagas = invoiceDao.countByStatus(3);
            Long faturasPendentes = invoiceDao.countByStatus(1);

            return new EstatisticasFaturas(
                    BigDecimal.valueOf(totalVendas != null ? totalVendas : 0.0),
                    faturasEmitidas, faturasPagas, faturasPendentes
            );
        } catch (Exception e) {
            System.err.println("❌ Erro ao calcular estatísticas: " + e.getMessage());
            return new EstatisticasFaturas(BigDecimal.ZERO, 0L, 0L, 0L);
        }
    }

    // ==========================================================
    // 🔹 VALIDAÇÕES
    // ==========================================================
    private void validarFatura(Invoices fatura) {
        if (fatura == null) {
            throw new IllegalArgumentException("Fatura não pode ser nula");
        }

        if (fatura.getClient() == null || fatura.getClient().getId() == null) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }

        if (fatura.getSeller() == null || fatura.getSeller().getId() == null) {
            throw new IllegalArgumentException("Vendedor é obrigatório");
        }

        if (fatura.getPrefix() == null || fatura.getPrefix().trim().isEmpty()) {
            throw new IllegalArgumentException("Prefixo é obrigatório");
        }

        if (fatura.getIssueDate() == null || fatura.getIssueDate().trim().isEmpty()) {
            throw new IllegalArgumentException("Data de emissão é obrigatória");
        }

        if (fatura.getTotal() == null || fatura.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total da fatura é inválido");
        }
    }

    private void validarPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de período são obrigatórias");
        }

        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data inicial não pode ser após data final");
        }
    }

    // ==========================================================
    // 🔹 PREPARAÇÃO DE DADOS
    // ==========================================================
    private void prepararDadosFatura(Invoices fatura) {
        // Usuário logado
        User usuarioLogado = userSession.getUser();
        if (usuarioLogado != null && fatura.getSeller() == null) {
            fatura.setSeller(usuarioLogado);
        }

        // Número sequencial
        if (fatura.getNumber() == null) {
            Integer nextNumber = obterProximoNumero(fatura.getPrefix());
            fatura.setNumber(nextNumber);
        }

        // Data de emissão padrão
        if (fatura.getIssueDate() == null) {
            fatura.setIssueDate(UtilDate.getFormatDataNow());
        }

        // Ano atual
        if (fatura.getYear() == null) {
            fatura.setYear(UtilDate.getYear());
        }

        // Status padrão
        if (fatura.getStatus() == null) {
            fatura.setStatus(1); // Pendente
        }

        // Hash se for emitida
        if (fatura.isEmitida() && fatura.getHash() == null) {
            fatura.setHash(gerarHashFatura(fatura));
        }
    }

    private String gerarHashFatura(Invoices fatura) {
        String numeroFatura = PrintHelper.formatDocumentNumber(
                fatura.getNumber(), fatura.getYear(), fatura.getPrefix()
        );
        return UtilSaft.appGenerateHashInvoice(
                fatura.getIssueDate(),
                fatura.getDueDate() != null ? fatura.getDueDate() : fatura.getIssueDate(),
                numeroFatura,
                String.valueOf(fatura.getTotal()),
                ""
        );
    }

    // ==========================================================
    // 🔹 UTILITÁRIOS
    // ==========================================================
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleError(String message, Exception e) {
        System.err.println(message + ": " + e.getMessage());
        JOptionPane.showMessageDialog(null, message + ": " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
    }

    // ==========================================================
    // 🔹 CLASSE INTERNA PARA ESTATÍSTICAS
    // ==========================================================
    public static class EstatisticasFaturas {

        public final BigDecimal totalVendas;
        public final Long faturasEmitidas;
        public final Long faturasPagas;
        public final Long faturasPendentes;

        public EstatisticasFaturas(BigDecimal totalVendas, Long faturasEmitidas,
                Long faturasPagas, Long faturasPendentes) {
            this.totalVendas = totalVendas;
            this.faturasEmitidas = faturasEmitidas;
            this.faturasPagas = faturasPagas;
            this.faturasPendentes = faturasPendentes;
        }
    }
}
