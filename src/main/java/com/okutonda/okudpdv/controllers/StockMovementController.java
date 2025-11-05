package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.StockMovementDao;
import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.StockMovement;
import com.okutonda.okudpdv.data.entities.Warehouse;
import com.okutonda.okudpdv.dtos.ProductStockReport;
import com.okutonda.okudpdv.helpers.UserSession;
import com.okutonda.okudpdv.services.StockMovementService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Controller responsável por validar e orquestrar movimentos de stock Inclui
 * operações CRUD + relatórios + métricas
 */
public class StockMovementController {

    private final StockMovementService stockService;
    private final UserSession session = UserSession.getInstance();

    public StockMovementController() {
        this.stockService = new StockMovementService();
    }

    // ==========================================================
    // 🔹 CONSULTAS BÁSICAS
    // ==========================================================
    public List<StockMovement> listarPorProduto(Integer productId) {
        try {
            return stockService.getHistoricoProduto(productId);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar movimentos por produto: " + e.getMessage());
            showError("Erro ao buscar movimentos de stock");
            return new ArrayList<>();
        }
    }

    public Integer getStockAtual(Integer productId) {
        try {
            return stockService.getStockAtual(productId);
        } catch (Exception e) {
            System.err.println("❌ Erro ao calcular stock atual: " + e.getMessage());
            return 0;
        }
    }

    public List<StockMovement> listarTodos() {
        try {
            // Busca movimentos dos últimos 30 dias por padrão
            LocalDateTime inicio = LocalDateTime.now().minusDays(30);
            LocalDateTime fim = LocalDateTime.now();
            return stockService.getMovimentosPorPeriodo(inicio, fim);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar todos os movimentos: " + e.getMessage());
            showError("Erro ao buscar movimentos de stock");
            return new ArrayList<>();
        }
    }

    public List<StockMovement> listarPorTipo(String tipo) {
        try {
            return stockService.getMovimentosPorTipo(tipo);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar movimentos por tipo: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<StockMovement> listarPorOrigem(String origem) {
        try {
            return stockService.getMovimentosPorOrigem(origem);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar movimentos por origem: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<StockMovement> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        try {
            return stockService.getMovimentosPorPeriodo(inicio, fim);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar movimentos por período: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<StockMovement> listarPorReferencia(Integer referenceId) {
        try {
            return stockService.getMovimentosPorReferencia(referenceId);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar movimentos por referência: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ==========================================================
    // 🔹 OPERAÇÕES DE STOCK
    // ==========================================================
    /**
     * Registra entrada por compra
     */
    public boolean registrarEntradaCompra(Product product, Integer quantidade, Integer purchaseId) {
        try {
            StockMovement movimento = stockService.registrarEntradaCompra(
                    product, quantidade, purchaseId, "Entrada por compra #" + purchaseId
            );

            if (movimento != null) {
                System.out.println("✅ Entrada por compra registrada: " + product.getDescription());
                showSuccess("Entrada de stock por compra registrada com sucesso!");
                return true;
            }
            return false;

        } catch (StockMovementService.StockInsuficienteException e) {
            showError(e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("❌ Erro ao registrar entrada por compra: " + e.getMessage());
            showError("Erro ao registrar entrada por compra: " + e.getMessage());
            return false;
        }
    }

    /**
     * Registra saída por venda
     */
    public boolean registrarSaidaVenda(Product product, Integer quantidade, Integer saleId) {
        try {
            StockMovement movimento = stockService.registrarSaidaVenda(
                    product, quantidade, saleId, "Saída por venda #" + saleId
            );

            if (movimento != null) {
                System.out.println("✅ Saída por venda registrada: " + product.getDescription());
                showSuccess("Saída de stock por venda registrada com sucesso!");
                return true;
            }
            return false;

        } catch (StockMovementService.StockInsuficienteException e) {
            showError(e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("❌ Erro ao registrar saída por venda: " + e.getMessage());
            showError("Erro ao registrar saída por venda: " + e.getMessage());
            return false;
        }
    }

    /**
     * Registra ajuste manual
     */
    public boolean registrarAjuste(Product product, Integer quantidade, String motivo) {
        try {
            StockMovement movimento = stockService.registrarAjuste(
                    product, quantidade, motivo, "Ajuste manual: " + motivo
            );

            if (movimento != null) {
                System.out.println("✅ Ajuste registrado: " + product.getDescription() + " - " + quantidade);
                showSuccess("Ajuste de stock registrado com sucesso!");
                return true;
            }
            return false;

        } catch (StockMovementService.StockInsuficienteException e) {
            showError(e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("❌ Erro ao registrar ajuste: " + e.getMessage());
            showError("Erro ao registrar ajuste: " + e.getMessage());
            return false;
        }
    }

    /**
     * Ajusta stock para valor específico
     */
    public boolean ajustarParaValor(Product product, Integer novoStock, String motivo) {
        try {
            // Implementação simplificada - calcula a diferença e faz ajuste
            Integer stockAtual = getStockAtual(product.getId());
            Integer diferenca = novoStock - stockAtual;

            StockMovement movimento = stockService.registrarAjuste(
                    product, diferenca, motivo,
                    "Ajuste para " + novoStock + " unidades (era " + stockAtual + ")"
            );

            if (movimento != null) {
                System.out.println("✅ Stock ajustado para " + novoStock + " (era " + stockAtual + ")");
                showSuccess("Stock ajustado para " + novoStock + " unidades!");
                return true;
            }
            return false;

        } catch (StockMovementService.StockInsuficienteException e) {
            showError(e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("❌ Erro ao ajustar stock: " + e.getMessage());
            showError("Erro ao ajustar stock: " + e.getMessage());
            return false;
        }
    }

    // ==========================================================
    // 🔹 RELATÓRIOS E MÉTRICAS
    // ==========================================================
    /**
     * Obtém relatório completo de stock
     */
    public List<ProductStockReport> getRelatorioStockCompleto() {
        try {
            return stockService.gerarRelatorioStockCompleto();
        } catch (Exception e) {
            System.err.println("❌ Erro ao obter relatório de stock: " + e.getMessage());
            showError("Erro ao carregar relatório de stock: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Obtém produtos com stock baixo
     */
    public List<ProductStockReport> getProdutosComStockBaixo() {
        try {
            return stockService.gerarRelatorioStockBaixo();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produtos com stock baixo: " + e.getMessage());
            showError("Erro ao buscar produtos com stock baixo: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Obtém relatório para um produto específico
     */
    public ProductStockReport getRelatorioProdutoEspecifico(Integer productId) {
        try {
            return stockService.gerarRelatorioProdutoEspecifico(productId);
        } catch (Exception e) {
            System.err.println("❌ Erro ao gerar relatório do produto: " + e.getMessage());
            showError("Erro ao gerar relatório do produto: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gera relatório completo de stock usando ProductStockReport
     */
    public List<ProductStockReport> getRelatorioStockCompletoReport() {
        try {
            return stockService.gerarRelatorioStockCompleto();
        } catch (Exception e) {
            System.err.println("❌ Erro ao obter relatório de stock: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Gera relatório de stock para um produto específico
     */
    public ProductStockReport gerarRelatorioProduto(Product produto) {
        try {
            return stockService.gerarRelatorioProdutoEspecifico(produto.getId());
        } catch (Exception e) {
            System.err.println("❌ Erro ao gerar relatório do produto: " + e.getMessage());
            return criarRelatorioProdutoFallback(produto);
        }
    }

    /**
     * Gera relatório de stock para uma lista de produtos específica
     */
    public List<ProductStockReport> gerarRelatorioParaProdutos(List<Product> produtos) {
        try {
            List<ProductStockReport> relatorio = new ArrayList<>();

            for (Product produto : produtos) {
                ProductStockReport report = criarRelatorioProduto(produto);
                relatorio.add(report);
            }

            return relatorio;

        } catch (Exception e) {
            System.err.println("❌ Erro ao gerar relatório para produtos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
// ==========================================================
// 🔹 MÉTODOS AUXILIARES PRIVADOS
// ==========================================================

    private ProductStockReport criarRelatorioProduto(Product produto) {
        try {
            Integer stockAtual = getStockAtual(produto.getId());
            Integer totalEntradas = getTotalEntradas(produto.getId());
            Integer totalSaidas = getTotalSaidas(produto.getId());
            LocalDateTime ultimaMovimentacao = getUltimaMovimentacao(produto.getId());

            return new ProductStockReport(
                    produto,
                    stockAtual,
                    totalEntradas,
                    totalSaidas,
                    ultimaMovimentacao
            );
        } catch (Exception e) {
            System.err.println("❌ Erro ao criar relatório para produto " + produto.getId() + ": " + e.getMessage());
            return criarRelatorioProdutoFallback(produto);
        }
    }

    private ProductStockReport criarRelatorioProdutoFallback(Product produto) {
        return new ProductStockReport(
                produto,
                0, // stockAtual
                0, // totalEntradas  
                0, // totalSaidas
                null // ultimaMovimentacao
        );
    }

    /**
     * Obtém estatísticas de stock
     */
    public StockMovementDao.EstatisticasStock getEstatisticasStock() {
        try {
            return stockService.obterEstatisticasStock();
        } catch (Exception e) {
            System.err.println("❌ Erro ao obter estatísticas: " + e.getMessage());
            return new StockMovementDao.EstatisticasStock(0, 0, 0, 0);
        }
    }

    /**
     * Obtém total de entradas de um produto
     */
    public Integer getTotalEntradas(Integer productId) {
        try {
            return stockService.getTotalEntradas(productId);
        } catch (Exception e) {
            System.err.println("❌ Erro ao obter total de entradas: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Obtém total de saídas de um produto
     */
    public Integer getTotalSaidas(Integer productId) {
        try {
            return stockService.getTotalSaidas(productId);
        } catch (Exception e) {
            System.err.println("❌ Erro ao obter total de saídas: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Obtém última movimentação de um produto
     */
    public LocalDateTime getUltimaMovimentacao(Integer productId) {
        try {
            return stockService.getUltimaMovimentacao(productId);
        } catch (Exception e) {
            System.err.println("❌ Erro ao obter última movimentação: " + e.getMessage());
            return null;
        }
    }

    // ==========================================================
    // 🔹 MÉTODOS DE VALIDAÇÃO
    // ==========================================================
    /**
     * Verifica se há stock suficiente
     */
    public boolean verificarStockSuficiente(Integer productId, Integer quantidade) {
        try {
            Integer stockAtual = getStockAtual(productId);
            return stockAtual >= quantidade;
        } catch (Exception e) {
            System.err.println("❌ Erro ao verificar stock: " + e.getMessage());
            return false;
        }
    }

    /**
     * Calcula o saldo atual de um produto
     */
    public Integer calcularSaldoProduto(Integer productId) {
        return getStockAtual(productId);
    }

    // ==========================================================
    // 🔹 MÉTODOS COMPATIBILIDADE (LEGADO)
    // ==========================================================
    /**
     * Registra um movimento de stock genérico (método legado)
     */
    public StockMovement registrar(StockMovement movimento) {
        try {
            if (!validarMovimento(movimento)) {
                return null;
            }

            // Converte para os métodos específicos do service
            return switch (movimento.getType().toUpperCase()) {
                case "ENTRADA" -> {
                    StockMovement result = stockService.registrarEntradaCompra(
                            movimento.getProduct(),
                            movimento.getQuantity(),
                            movimento.getReferenceId(),
                            movimento.getNotes()
                    );
                    yield result;
                }
                case "SAIDA" -> {
                    StockMovement result = stockService.registrarSaidaVenda(
                            movimento.getProduct(),
                            Math.abs(movimento.getQuantity()),
                            movimento.getReferenceId(),
                            movimento.getNotes()
                    );
                    yield result;
                }
                case "AJUSTE" -> {
                    StockMovement result = stockService.registrarAjuste(
                            movimento.getProduct(),
                            movimento.getQuantity(),
                            movimento.getReason(),
                            movimento.getNotes()
                    );
                    yield result;
                }
                default -> {
                    showError("Tipo de movimento não suportado: " + movimento.getType());
                    yield null;
                }
            };

        } catch (StockMovementService.StockInsuficienteException e) {
            showError(e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("❌ Erro ao registrar movimento: " + e.getMessage());
            showError("Erro ao registar movimento: " + e.getMessage());
            return null;
        }
    }

    /**
     * Registra múltiplos movimentos em lote (método legado)
     */
    public boolean registrarEmLote(List<StockMovement> movimentos) {
        if (movimentos == null || movimentos.isEmpty()) {
            showWarning("Lista de movimentos vazia");
            return false;
        }

        try {
            int sucessos = 0;
            for (StockMovement movimento : movimentos) {
                StockMovement resultado = registrar(movimento);
                if (resultado != null) {
                    sucessos++;
                }
            }

            if (sucessos == movimentos.size()) {
                System.out.println("✅ " + sucessos + " movimentos registrados em lote");
                showSuccess(sucessos + " movimentos registrados com sucesso!");
                return true;
            } else {
                showWarning(sucessos + " de " + movimentos.size() + " movimentos registrados");
                return sucessos > 0;
            }

        } catch (Exception e) {
            System.err.println("❌ Erro ao registrar movimentos em lote: " + e.getMessage());
            showError("Erro ao registrar movimentos em lote: " + e.getMessage());
            return false;
        }
    }

    /**
     * Valida movimento (método legado)
     */
    private boolean validarMovimento(StockMovement movimento) {
        if (movimento == null) {
            showError("Movimento inválido!");
            return false;
        }

        if (movimento.getProduct() == null) {
            showError("Produto não definido!");
            return false;
        }

        if (movimento.getQuantity() == null || movimento.getQuantity() == 0) {
            showWarning("A quantidade deve ser diferente de zero!");
            return false;
        }

        if (movimento.getType() == null) {
            showError("Tipo de movimento não definido!");
            return false;
        }

        return true;
    }

    // ==========================================================
    // 🔹 HELPERS DE UI
    // ==========================================================
    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(null, message, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Remove um movimento de stock (se implementado no service)
     */
    public boolean removerMovimento(Integer movimentoId) {
        try {
            // Esta funcionalidade precisaria ser implementada no Service
            showWarning("Funcionalidade de remoção não implementada no Service");
            return false;
        } catch (Exception e) {
            System.err.println("❌ Erro ao remover movimento: " + e.getMessage());
            showError("Erro ao remover movimento: " + e.getMessage());
            return false;
        }
    }
}
