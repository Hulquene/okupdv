package com.okutonda.okudpdv.services;

import com.okutonda.okudpdv.data.dao.PurchaseDao;
import com.okutonda.okudpdv.data.entities.InvoiceType;
import com.okutonda.okudpdv.data.entities.Purchase;
import com.okutonda.okudpdv.data.entities.PurchaseItem;
import com.okutonda.okudpdv.data.entities.User;
import com.okutonda.okudpdv.helpers.UserSession;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;

/**
 * Service layer para gestão de Compras Separa a lógica de negócio do controller
 */
public class PurchaseService {

    private final PurchaseDao purchaseDao;
    private final UserSession userSession;

    public PurchaseService() {
        this.purchaseDao = new PurchaseDao();
        this.userSession = UserSession.getInstance();
    }

    // ==========================================================
    // 🔹 OPERAÇÕES PRINCIPAIS
    // ==========================================================
    /**
     * Cria uma nova compra com todas as validações
     */
    public Purchase criarCompra(Purchase compra) {
        try {
            // Validações
            validarCompra(compra);
            validarItensCompra(compra);

            // Preparar dados
            prepararDadosCompra(compra);

            // Calcular totais
            calcularTotaisCompra(compra);

            // Salvar
            Purchase compraSalva = purchaseDao.save(compra);

            showSuccessMessage("Compra registada com sucesso!\nNúmero: " + compraSalva.getInvoiceNumber());
            return compraSalva;

        } catch (Exception e) {
            handleError("Erro ao criar compra", e);
            throw new RuntimeException("Erro ao criar compra", e);
        }
    }

    /**
     * Atualiza uma compra existente
     */
    public Purchase atualizarCompra(Purchase compra) {
        try {
            validarCompra(compra);
            calcularTotaisCompra(compra);

            return purchaseDao.update(compra);

        } catch (Exception e) {
            handleError("Erro ao atualizar compra", e);
            throw new RuntimeException("Erro ao atualizar compra", e);
        }
    }

    /**
     * Exclui uma compra
     */
    public void excluirCompra(Integer id) {
        try {
            Purchase compra = buscarPorId(id);
            if (compra == null) {
                throw new IllegalArgumentException("Compra não encontrada");
            }

            validarExclusaoCompra(compra);
            purchaseDao.delete(id);

            showSuccessMessage("Compra excluída com sucesso!");

        } catch (Exception e) {
            handleError("Erro ao excluir compra", e);
            throw new RuntimeException("Erro ao excluir compra", e);
        }
    }

    // ==========================================================
    // 🔹 CONSULTAS
    // ==========================================================
    public Purchase buscarPorId(Integer id) {
        return purchaseDao.findById(id).orElse(null);
    }

    public List<Purchase> listarTodas() {
        return purchaseDao.findAll();
    }

    public List<Purchase> filtrar(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return listarTodas();
        }
        return purchaseDao.filter(texto.trim());
    }

    public List<Purchase> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        validarPeriodo(dataInicio, dataFim);
        return purchaseDao.filterByDate(dataInicio, dataFim);
    }

    public List<Purchase> listarPorFornecedor(Integer fornecedorId) {
        if (fornecedorId == null || fornecedorId <= 0) {
            throw new IllegalArgumentException("ID do fornecedor inválido");
        }
        return purchaseDao.findBySupplier(fornecedorId);
    }

    public List<Purchase> listarContasAPagar() {
        List<Purchase> todas = purchaseDao.findAll();
        return todas.stream()
                .filter(p -> "ABERTO".equals(p.getStatus()) || "PARCIAL".equals(p.getStatus()))
                .toList();
    }

    // ==========================================================
    // 🔹 CÁLCULOS E ESTATÍSTICAS
    // ==========================================================
    public void calcularTotaisCompra(Purchase compra) {
        if (compra == null || compra.getItems() == null) {
            return;
        }

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalIVA = BigDecimal.ZERO;

        for (PurchaseItem item : compra.getItems()) {
            if (item.getSubtotal() != null) {
                total = total.add(item.getSubtotal());
            }
            if (item.getIva() != null) {
                totalIVA = totalIVA.add(item.getIva());
            }
        }

        compra.setTotal(total);
        compra.setIvaTotal(totalIVA);
    }

    public BigDecimal calcularTotalCompras(List<Purchase> compras) {
        if (compras == null || compras.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return compras.stream()
                .map(Purchase::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        validarPeriodo(dataInicio, dataFim);
        Double total = purchaseDao.calculateTotalByPeriod(dataInicio, dataFim);
        return BigDecimal.valueOf(total != null ? total : 0.0);
    }

    public BigDecimal calcularSaldoDevedor(Purchase compra) {
        if (compra == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalPago = compra.getTotal_pago() != null ? compra.getTotal_pago() : BigDecimal.ZERO;
        return compra.getTotal().subtract(totalPago);
    }

    public Integer obterProximoNumeroFatura() {
        return purchaseDao.getNextInvoiceNumber();
    }

    // ==========================================================
    // 🔹 VALIDAÇÕES
    // ==========================================================
    private void validarCompra(Purchase compra) {
        if (compra == null) {
            throw new IllegalArgumentException("Compra não pode ser nula");
        }

        if (compra.getSupplier() == null || compra.getSupplier().getId() == null) {
            throw new IllegalArgumentException("Fornecedor é obrigatório");
        }

        if (compra.getInvoiceType() == null) {
            throw new IllegalArgumentException("Tipo de fatura é obrigatório");
        }
    }

    private void validarItensCompra(Purchase compra) {
        if (compra.getItems() == null || compra.getItems().isEmpty()) {
            throw new IllegalArgumentException("Compra deve ter pelo menos um item");
        }

        for (PurchaseItem item : compra.getItems()) {
            if (item.getProduct() == null || item.getProduct().getId() == null) {
                throw new IllegalArgumentException("Produto do item é obrigatório");
            }

            if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                throw new IllegalArgumentException("Quantidade do item deve ser maior que zero");
            }

            if (item.getPrecoCusto() == null || item.getPrecoCusto().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Preço de custo do item é inválido");
            }
        }
    }

    private void validarExclusaoCompra(Purchase compra) {
        if (compra.getPayments() != null && !compra.getPayments().isEmpty()) {
            throw new IllegalStateException("Não é possível excluir compra com pagamentos associados");
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
    private void prepararDadosCompra(Purchase compra) {
        // Usuário logado
        User usuarioLogado = userSession.getUser();
        if (usuarioLogado != null) {
            compra.setUser(usuarioLogado);
        }

        // Número sequencial
        if (compra.getInvoiceNumber() == null || compra.getInvoiceNumber().trim().isEmpty()) {
            Integer nextNumber = obterProximoNumeroFatura();
            compra.setInvoiceNumber(gerarNumeroCompra(nextNumber));
        }

        // Tipo de fatura padrão
        if (compra.getInvoiceType() == null) {
            compra.setInvoiceType(InvoiceType.FT);
        }

        // Datas padrão
        if (compra.getDataCompra() == null) {
            compra.setDataCompra(new java.sql.Date(System.currentTimeMillis()));
        }
        if (compra.getDataVencimento() == null) {
            compra.setDataVencimento(java.sql.Date.valueOf(LocalDate.now().plusDays(30)));
        }

        // Status padrão
        if (compra.getStatus() == null || compra.getStatus().isEmpty()) {
            compra.setStatus("ABERTO");
        }
    }

    private String gerarNumeroCompra(int sequencia) {
        String ano = String.valueOf(LocalDate.now().getYear());
        return String.format("COMP/%s/%04d", ano, sequencia);
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
    
}
