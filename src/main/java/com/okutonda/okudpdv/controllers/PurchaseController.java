package com.okutonda.okudpdv.controllers;

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
 * Controller responsável pelas regras de negócio de Compras.
 *
 * Garante validações, geração de número sequencial e integração com o DAO que
 * executa transações e movimenta stock.
 *
 * @author Hulquene
 */
public class PurchaseController {

    private final PurchaseDao dao;
    private final UserSession session = UserSession.getInstance();

    public PurchaseController() {
        this.dao = new PurchaseDao();
    }

    // ==========================================================
    // 🔹 CRUD OPERATIONS (Novo padrão)
    // ==========================================================
    /**
     * Adiciona uma nova compra ao sistema com todas as validações de negócio.
     * Mantém a lógica original mas adaptada ao novo DAO.
     */
    public Purchase criarCompra(Purchase compra) {
        try {
            // 🔹 1) Validar compra
            if (!validarCompra(compra)) {
                throw new IllegalArgumentException("Dados da compra inválidos");
            }

            // 🔹 2) Validar itens
            if (!compraTemItensValidos(compra)) {
                throw new IllegalArgumentException("Itens da compra inválidos");
            }

            // 🔹 3) Garantir usuário logado
            User usuarioLogado = session.getUser();
            if (usuarioLogado != null) {
                compra.setUser(usuarioLogado);
            }

            // 🔹 4) Calcular totais
            calcularTotaisCompra(compra);

            // 🔹 5) Gerar número de compra sequencial se não informado
            if (compra.getInvoiceNumber() == null || compra.getInvoiceNumber().trim().isEmpty()) {
                Integer nextNumber = dao.getNextInvoiceNumber();
                compra.setInvoiceNumber(gerarNumeroCompra(nextNumber));
            }

            // 🔹 6) Tipo de fatura padrão
            if (compra.getInvoiceType() == null) {
                compra.setInvoiceType(InvoiceType.FT); // Fatura de compra
            }

            // 🔹 7) Datas padrão
            if (compra.getDataCompra() == null) {
                compra.setDataCompra(new java.sql.Date(System.currentTimeMillis()));
            }
            if (compra.getDataVencimento() == null) {
                compra.setDataVencimento(java.sql.Date.valueOf(LocalDate.now().plusDays(30)));
            }

            // 🔹 8) Status padrão
            if (compra.getStatus() == null || compra.getStatus().isEmpty()) {
                compra.setStatus("ABERTO"); // ABERTO | PAGO | CANCELADO
            }

            // 🔹 9) Gravar via DAO
            Purchase compraSalva = dao.save(compra);

            JOptionPane.showMessageDialog(null,
                    "✅ Compra registada com sucesso!\nNúmero: " + compraSalva.getInvoiceNumber(),
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            return compraSalva;

        } catch (Exception e) {
            System.err.println("Erro ao adicionar compra: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                    "❌ Erro ao adicionar compra: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Erro ao criar compra", e);
        }
    }

    /**
     * Método legado - mantido para compatibilidade
     */
    public boolean add(Purchase p) {
        try {
            Purchase compraSalva = criarCompra(p);
            return compraSalva != null && compraSalva.getId() != null;
        } catch (Exception e) {
            return false;
        }
    }

    public Purchase atualizarCompra(Purchase compra) {
        if (compra == null || compra.getId() == null) {
            throw new IllegalArgumentException("Compra inválida para atualização");
        }

        if (!validarCompra(compra)) {
            throw new IllegalArgumentException("Dados da compra inválidos");
        }

        // Recalcula totais antes de atualizar
        calcularTotaisCompra(compra);

        return dao.update(compra);
    }

    public void excluirCompra(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID da compra inválido");
        }

        Purchase compra = dao.findById(id).orElse(null);
        if (compra == null) {
            throw new IllegalArgumentException("Compra não encontrada");
        }

        // Verifica se existem pagamentos associados
        if (compra.getPayments() != null && !compra.getPayments().isEmpty()) {
            throw new IllegalStateException("Não é possível excluir compra com pagamentos associados");
        }

        dao.delete(id);

        JOptionPane.showMessageDialog(null,
                "✅ Compra excluída com sucesso!",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    public Purchase buscarPorId(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        return dao.findById(id).orElse(null);
    }

    public List<Purchase> listarTodas() {
        return dao.findAll();
    }

    // ==========================================================
    // 🔹 MÉTODOS DE BUSCA E FILTRO
    // ==========================================================
    /**
     * Lista todas as compras - método legado
     */
    public List<Purchase> listar() {
        return listarTodas();
    }

    public List<Purchase> filtrar(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return listarTodas();
        }
        return dao.filter(texto.trim());
    }

    public List<Purchase> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de período são obrigatórias");
        }

        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data inicial não pode ser após data final");
        }

        return dao.filterByDate(dataInicio, dataFim);
    }

    public List<Purchase> listarPorFornecedor(Integer fornecedorId) {
        if (fornecedorId == null || fornecedorId <= 0) {
            throw new IllegalArgumentException("ID do fornecedor inválido");
        }
        return dao.findBySupplier(fornecedorId);
    }

    public List<Purchase> listarPorTipoFatura(InvoiceType tipoFatura) {
        if (tipoFatura == null) {
            throw new IllegalArgumentException("Tipo de fatura é obrigatório");
        }
        return dao.findByInvoiceType(tipoFatura);
    }

    public List<Purchase> listarPorStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status é obrigatório");
        }
        return dao.findByStatus(status.trim());
    }

    public List<Purchase> listarContasAPagar() {
        // Filtra compras com status ABERTO ou PARCIAL
        List<Purchase> todas = dao.findAll();
        return todas.stream()
                .filter(p -> "ABERTO".equals(p.getStatus()) || "PARCIAL".equals(p.getStatus()))
                .toList();
    }

    // ==========================================================
    // 🔹 MÉTODOS UTILITÁRIOS
    // ==========================================================
    /**
     * Gera o número formatado da compra (ex: COMP/2025/0001)
     */
    private String gerarNumeroCompra(int sequencia) {
        String ano = String.valueOf(LocalDate.now().getYear());
        return String.format("COMP/%s/%04d", ano, sequencia);
    }

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
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de período são obrigatórias");
        }

        Double total = dao.calculateTotalByPeriod(dataInicio, dataFim);
        return BigDecimal.valueOf(total);
    }

    public BigDecimal calcularSaldoDevedor(Purchase compra) {
        if (compra == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalPago = compra.getTotal_pago() != null ? compra.getTotal_pago() : BigDecimal.ZERO;
        return compra.getTotal().subtract(totalPago);
    }

    // ==========================================================
    // 🔹 VALIDAÇÕES DE NEGÓCIO
    // ==========================================================
    public boolean validarCompra(Purchase compra) {
        if (compra == null) {
            return false;
        }

        // Fornecedor obrigatório
        if (compra.getSupplier() == null || compra.getSupplier().getId() == null) {
            return false;
        }

        // Número da fatura obrigatório (será gerado se não informado)
        // if (compra.getInvoiceNumber() == null || compra.getInvoiceNumber().trim().isEmpty()) {
        //     return false;
        // }
        // Tipo de fatura obrigatório
        if (compra.getInvoiceType() == null) {
            return false;
        }

        // Data de compra obrigatória (será definida se não informada)
        // if (compra.getDataCompra() == null) {
        //     return false;
        // }
        // Data de vencimento obrigatória (será definida se não informada)
        // if (compra.getDataVencimento() == null) {
        //     return false;
        // }
        return true;
    }

    public boolean compraTemItensValidos(Purchase compra) {
        if (compra == null || compra.getItems() == null || compra.getItems().isEmpty()) {
            return false;
        }

        for (PurchaseItem item : compra.getItems()) {
            if (item.getProduct() == null || item.getProduct().getId() == null) {
                return false;
            }
            if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                return false;
            }
            if (item.getPrecoCusto() == null || item.getPrecoCusto().compareTo(BigDecimal.ZERO) <= 0) {
                return false;
            }
        }

        return true;
    }

    public Integer obterProximoNumeroFatura() {
        return dao.getNextInvoiceNumber();
    }

    /**
     * Remove uma compra pelo ID - método legado
     */
    public boolean remover(int id) {
        try {
            excluirCompra(id);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Erro ao excluir compra: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Busca uma compra específica pelo ID - método legado
     */
    public Purchase findById(int id) {
        return buscarPorId(id);
    }
}
