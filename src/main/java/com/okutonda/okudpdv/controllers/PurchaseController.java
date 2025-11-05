package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.entities.InvoiceType;
import com.okutonda.okudpdv.data.entities.Purchase;
import com.okutonda.okudpdv.services.PurchaseService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Controller atualizado usando PurchaseService Mantém compatibilidade com
 * código existente
 */
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController() {
        this.purchaseService = new PurchaseService();
    }

    // ==========================================================
    // 🔹 MÉTODOS PRINCIPAIS (Compatibilidade)
    // ==========================================================
    public boolean add(Purchase p) {
        try {
            System.out.println("=== INICIANDO ADIÇÃO DE COMPRA ===");
            System.out.println("Purchase recebido: " + p);
            p.setInvoiceType(InvoiceType.FT);
            Purchase compraSalva = purchaseService.criarCompra(p);
            boolean sucesso = compraSalva != null && compraSalva.getId() != null;

            System.out.println("Resultado: " + (sucesso ? "SUCESSO" : "FALHA"));
            return sucesso;

        } catch (Exception e) {
            System.err.println("Erro no método add: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Purchase criarCompra(Purchase compra) {
        return purchaseService.criarCompra(compra);
    }

    public Purchase atualizarCompra(Purchase compra) {
        return purchaseService.atualizarCompra(compra);
    }

    public void excluirCompra(Integer id) {
        purchaseService.excluirCompra(id);
    }

    public Purchase buscarPorId(Integer id) {
        return purchaseService.buscarPorId(id);
    }

    public List<Purchase> listarTodas() {
        return purchaseService.listarTodas();
    }

    // ==========================================================
    // 🔹 MÉTODOS DE BUSCA (Compatibilidade)
    // ==========================================================
    public List<Purchase> listar() {
        return purchaseService.listarTodas();
    }

    public List<Purchase> filtrar(String texto) {
        return purchaseService.filtrar(texto);
    }

    public List<Purchase> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return purchaseService.listarPorPeriodo(dataInicio, dataFim);
    }

    public List<Purchase> listarPorFornecedor(Integer fornecedorId) {
        return purchaseService.listarPorFornecedor(fornecedorId);
    }

    public List<Purchase> listarPorTipoFatura(InvoiceType tipoFatura) {
        // Implementar no service se necessário
        return List.of();
    }

    public List<Purchase> listarPorStatus(String status) {
        // Implementar no service se necessário  
        return List.of();
    }

    public List<Purchase> listarContasAPagar() {
        return purchaseService.listarContasAPagar();
    }

    // ==========================================================
    // 🔹 MÉTODOS UTILITÁRIOS (Compatibilidade)
    // ==========================================================
    public void calcularTotaisCompra(Purchase compra) {
        purchaseService.calcularTotaisCompra(compra);
    }

    public BigDecimal calcularTotalCompras(List<Purchase> compras) {
        return purchaseService.calcularTotalCompras(compras);
    }

    public BigDecimal calcularTotalPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return purchaseService.calcularTotalPorPeriodo(dataInicio, dataFim);
    }

    public BigDecimal calcularSaldoDevedor(Purchase compra) {
        return purchaseService.calcularSaldoDevedor(compra);
    }

    public Integer obterProximoNumeroFatura() {
        return purchaseService.obterProximoNumeroFatura();
    }

    // ==========================================================
    // 🔹 MÉTODOS LEGADO (Mantidos para compatibilidade)
    // ==========================================================
    public boolean remover(int id) {
        try {
            purchaseService.excluirCompra(id);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Erro ao excluir compra: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public Purchase findById(int id) {
        return purchaseService.buscarPorId(id);
    }

    // ==========================================================
    // 🔹 VALIDAÇÕES (Mantidas por enquanto)
    // ==========================================================
    public boolean validarCompra(Purchase compra) {
        try {
            if (compra == null) {
                return false;
            }
            if (compra.getSupplier() == null || compra.getSupplier().getId() == null) {
                return false;
            }
            if (compra.getInvoiceType() == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean compraTemItensValidos(Purchase compra) {
        try {
            if (compra.getItems() == null || compra.getItems().isEmpty()) {
                return false;
            }

            for (var item : compra.getItems()) {
                if (item.getProduct() == null || item.getProduct().getId() == null) {
                    return false;
                }
                if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                    return false;
                }
                if (item.getPrecoCusto() == null || item.getPrecoCusto().compareTo(BigDecimal.ZERO) < 0) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
