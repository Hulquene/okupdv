package com.okutonda.okudpdv.services;

import com.okutonda.okudpdv.data.dao.ProductDao;
import com.okutonda.okudpdv.data.dao.StockMovementDao;
import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.StockMovement;
import com.okutonda.okudpdv.data.entities.Warehouse;
import com.okutonda.okudpdv.dtos.ProductStockReport;
import com.okutonda.okudpdv.helpers.UserSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsável por TODA a lógica de negócio de movimentação de stock
 * Inclui operações CRUD + relatórios + métricas
 */
public class StockMovementService {

    private final StockMovementDao stockMovementDao;
    private final UserSession userSession;

    public StockMovementService() {
        this.stockMovementDao = new StockMovementDao();
        this.userSession = UserSession.getInstance();
    }

    // ==========================================================
    // 🔹 OPERAÇÕES BÁSICAS DE STOCK
    // ==========================================================
    public StockMovement registrarEntradaCompra(Product product, Integer quantidade,
            Integer purchaseId, String notes) {
        StockMovement movimento = criarMovimentoBase(product, Math.abs(quantidade), "ENTRADA");
        movimento.setOrigin("COMPRA");
        movimento.setReferenceId(purchaseId);
        movimento.setNotes(notes != null ? notes : "Entrada por compra #" + purchaseId);

        return stockMovementDao.save(movimento);
    }

    public StockMovement registrarSaidaVenda(Product product, Integer quantidade,
            Integer saleId, String notes) {
        // Valida se há stock suficiente
        if (!validarStockSuficiente(product.getId(), quantidade)) {
            throw new StockInsuficienteException(
                    "Stock insuficiente para " + product.getDescription()
                    + ". Disponível: " + getStockAtual(product.getId())
                    + ", Requerido: " + quantidade
            );
        }

        StockMovement movimento = criarMovimentoBase(product, -Math.abs(quantidade), "SAIDA");
        movimento.setOrigin("VENDA");
        movimento.setReferenceId(saleId);
        movimento.setNotes(notes != null ? notes : "Saída por venda #" + saleId);

        return stockMovementDao.save(movimento);
    }

    public StockMovement registrarAjuste(Product product, Integer quantidade,
            String motivo, String notes) {
        // Para ajustes negativos, valida se não deixa stock negativo
        if (quantidade < 0) {
            Integer quantidadeAbsoluta = Math.abs(quantidade);
            if (!validarStockSuficiente(product.getId(), quantidadeAbsoluta)) {
                throw new StockInsuficienteException(
                        "Ajuste inválido: deixaria stock negativo para " + product.getDescription()
                        + ". Disponível: " + getStockAtual(product.getId())
                        + ", Tentado: " + quantidadeAbsoluta
                );
            }
        }

        StockMovement movimento = criarMovimentoBase(product, quantidade, "AJUSTE");
        movimento.setOrigin("MANUAL");
        movimento.setReason(motivo);
        movimento.setNotes(notes != null ? notes : "Ajuste manual: " + motivo);

        return stockMovementDao.save(movimento);
    }

    // ==========================================================
    // 🔹 CONSULTAS E RELATÓRIOS
    // ==========================================================
    public Integer getStockAtual(Integer productId) {
        return stockMovementDao.getStockAtual(productId);
    }

    public Integer getTotalEntradas(Integer productId) {
        return stockMovementDao.getTotalEntradas(productId);
    }

    public Integer getTotalSaidas(Integer productId) {
        return stockMovementDao.getTotalSaidas(productId);
    }

    public LocalDateTime getUltimaMovimentacao(Integer productId) {
        return stockMovementDao.getUltimaMovimentacao(productId);
    }

    public List<StockMovement> getHistoricoProduto(Integer productId) {
        return stockMovementDao.findByProductId(productId);
    }

    public List<StockMovement> getMovimentosPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return stockMovementDao.findByPeriodo(inicio, fim);
    }

    public List<StockMovement> getMovimentosPorTipo(String tipo) {
        return stockMovementDao.findByType(tipo);
    }

    public List<StockMovement> getMovimentosPorOrigem(String origem) {
        return stockMovementDao.findByOrigin(origem);
    }

    public List<StockMovement> getMovimentosPorReferencia(Integer referenceId) {
        return stockMovementDao.findByReferenceId(referenceId);
    }

    // ==========================================================
    // 🔹 RELATÓRIOS COM DTOs
    // ==========================================================
    /**
     * Gera relatório completo de stock para todos os produtos
     */
    public List<ProductStockReport> gerarRelatorioStockCompleto() {
        try {
            ProductDao productDao = new ProductDao();
            List<Product> produtos = productDao.findAllWithDetails();
            List<ProductStockReport> relatorio = new ArrayList<>();
System.out.println("findAllWithDetails" + produtos);
            for (Product produto : produtos) {
                ProductStockReport report = criarRelatorioProduto(produto);
                relatorio.add(report);
            }
System.out.println("repo" + relatorio);
            return relatorio;

        } catch (Exception e) {
            System.err.println("❌ Erro ao gerar relatório completo de stock: " + e.getMessage());
            throw new RuntimeException("Erro ao gerar relatório de stock", e);
        }
    }

    /**
     * Gera relatório de produtos com stock baixo
     */
    public List<ProductStockReport> gerarRelatorioStockBaixo() {
        try {
            ProductDao productDao = new ProductDao();
            List<Product> produtos = productDao.findAllWithDetails();
            List<ProductStockReport> relatorio = new ArrayList<>();

            for (Product produto : produtos) {
                Integer stockAtual = getStockAtual(produto.getId());
                Integer minStock = produto.getMinStock() != null ? produto.getMinStock() : 0;

                if (stockAtual <= minStock) {
                    ProductStockReport report = criarRelatorioProduto(produto);
                    relatorio.add(report);
                }
            }

            return relatorio;

        } catch (Exception e) {
            System.err.println("❌ Erro ao gerar relatório de stock baixo: " + e.getMessage());
            throw new RuntimeException("Erro ao gerar relatório de stock baixo", e);
        }
    }

    /**
     * Gera relatório para um produto específico
     */
    public ProductStockReport gerarRelatorioProdutoEspecifico(Integer productId) {
        try {
            ProductDao productDao = new ProductDao();
            Product produto = productDao.findByIdWithRelations(productId);
            System.out.println("pro: "+produto);
            if (produto == null) {
                throw new RuntimeException("Produto não encontrado: " + productId);
            }

            return criarRelatorioProduto(produto);

        } catch (Exception e) {
            System.err.println("❌ Erro ao gerar relatório do produto: " + e.getMessage());
            throw new RuntimeException("Erro ao gerar relatório do produto", e);
        }
    }

    /**
     * Obtém estatísticas gerais do stock
     */
    public StockMovementDao.EstatisticasStock obterEstatisticasStock() {
        return stockMovementDao.getEstatisticasStock();
    }

    // ==========================================================
    // 🔹 MÉTODOS PRIVADOS AUXILIARES
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
            // Retorna relatório com valores padrão em caso de erro
            return new ProductStockReport(
                    produto,
                    0,
                    0,
                    0,
                    null
            );
        }
    }

    private StockMovement criarMovimentoBase(Product product, Integer quantidade, String tipo) {
        StockMovement movimento = new StockMovement();
        movimento.setProduct(product);
        movimento.setQuantity(quantidade);
        movimento.setType(tipo);
        movimento.setUser(userSession.getUser());
        movimento.setCreatedAt(LocalDateTime.now());
        return movimento;
    }

    private boolean validarStockSuficiente(Integer productId, Integer quantidadeRequerida) {
        return stockMovementDao.hasStockSuficiente(productId, quantidadeRequerida);
    }

    // ==========================================================
    // 🔹 EXCEÇÕES PERSONALIZADAS
    // ==========================================================
    public static class StockInsuficienteException extends RuntimeException {

        public StockInsuficienteException(String message) {
            super(message);
        }
    }

    public static class MovimentoInvalidoException extends RuntimeException {

        public MovimentoInvalidoException(String message) {
            super(message);
        }
    }
}
