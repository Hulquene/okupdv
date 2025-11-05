// Arquivo: StockReportService.java - VERSÃO CORRIGIDA
package com.okutonda.okudpdv.services;

import com.okutonda.okudpdv.data.dao.StockMovementDao;
import com.okutonda.okudpdv.data.dao.ProductDao; // ✅ IMPORT CORRETO
import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.StockMovement;
import com.okutonda.okudpdv.dtos.ProductStockReport;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service especializado em gerar relatórios de stock usando DTOs USA ProductDao
 * em vez de StockMovementDao para buscar produtos
 */
public class StockReportService {

    private final StockMovementDao stockMovementDao;
    private final StockMovementService stockMovementService;
    private final ProductDao productDao; // ✅ USA ProductDao

    public StockReportService() {
        this.stockMovementDao = new StockMovementDao();
        this.stockMovementService = new StockMovementService();
        this.productDao = new ProductDao(); // ✅ Inicializa ProductDao
    }

    /**
     * Gera relatório completo de stock para todos os produtos ✅ CORRIGIDO: Usa
     * productDao.findAllForStockReport()
     */
    public List<ProductStockReport> gerarRelatorioStockCompleto() {
        try {
            List<Product> produtos = productDao.findAllForStockReport(); // ✅ CORRETO
            List<ProductStockReport> relatorio = new ArrayList<>();

            for (Product produto : produtos) {
                ProductStockReport report = criarRelatorioProduto(produto);
                relatorio.add(report);
            }

            return relatorio;

        } catch (Exception e) {
            System.err.println("❌ Erro ao gerar relatório completo de stock: " + e.getMessage());
            throw new RuntimeException("Erro ao gerar relatório de stock", e);
        }
    }

    /**
     * Gera relatório de stock com filtro ✅ CORRIGIDO: Usa
     * productDao.findForStockReport()
     */
    public List<ProductStockReport> gerarRelatorioStockFiltrado(String filtro) {
        try {
            List<Product> produtos;

            if (filtro == null || filtro.trim().isEmpty()) {
                produtos = productDao.findAllForStockReport(); // ✅ CORRETO
            } else {
                produtos = productDao.findForStockReport(filtro.trim()); // ✅ CORRETO
            }

            List<ProductStockReport> relatorio = new ArrayList<>();
            for (Product produto : produtos) {
                ProductStockReport report = criarRelatorioProduto(produto);
                relatorio.add(report);
            }

            return relatorio;

        } catch (Exception e) {
            System.err.println("❌ Erro ao gerar relatório filtrado: " + e.getMessage());
            throw new RuntimeException("Erro ao gerar relatório filtrado", e);
        }
    }

    /**
     * Gera relatório de produtos com stock baixo ✅ CORRIGIDO: Usa productDao +
     * lógica de stock
     */
    public List<ProductStockReport> gerarRelatorioStockBaixo() {
        try {
            List<Product> produtos = productDao.findAllForStockReport(); // ✅ CORRETO
            List<ProductStockReport> relatorio = new ArrayList<>();

            for (Product produto : produtos) {
                Integer stockAtual = stockMovementDao.getStockAtual(produto.getId());
                Integer minStock = produto.getMinStock();

                if (minStock == null) {
                    minStock = 0;
                }
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
     * Gera relatório para um produto específico ✅ CORRIGIDO: Usa
     * productDao.findByIdWithRelations()
     */
    public ProductStockReport gerarRelatorioProdutoEspecifico(Integer productId) {
        try {
            // ✅ CORRETO: Usa ProductDao para buscar produto
            Product produto = productDao.findByIdWithRelations(productId);
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
     * Cria o DTO ProductStockReport a partir de uma Entity Product ✅ MANTIDO:
     * Reutiliza StockMovementDao para métricas de stock
     */
    private ProductStockReport criarRelatorioProduto(Product produto) {
        try {
            Integer stockAtual = stockMovementDao.getStockAtual(produto.getId());
            Integer totalEntradas = stockMovementDao.getTotalEntradas(produto.getId());
            Integer totalSaidas = stockMovementDao.getTotalSaidas(produto.getId());
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

    /**
     * Obtém a última movimentação de um produto ✅ MANTIDO: Usa StockMovementDao
     * (que é a responsabilidade correta)
     */
    private LocalDateTime getUltimaMovimentacao(Integer productId) {
        try {
            List<StockMovement> movimentos = stockMovementDao.findByProductId(productId);

            if (movimentos != null && !movimentos.isEmpty()) {
                return movimentos.get(0).getCreatedAt(); // Já está ordenado por data decrescente
            }

            return null;

        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar última movimentação: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtém estatísticas gerais do stock
     */
    public EstatisticasStock obterEstatisticasStock() {
        try {
            List<ProductStockReport> relatorioCompleto = gerarRelatorioStockCompleto();

            long totalProdutos = relatorioCompleto.size();
            long semStock = relatorioCompleto.stream()
                    .filter(r -> r.getCurrentStock() <= 0)
                    .count();
            long stockBaixo = relatorioCompleto.stream()
                    .filter(r -> {
                        Integer minStock = r.getMinStock();
                        if (minStock == null) {
                            minStock = 0;
                        }
                        return r.getCurrentStock() > 0 && r.getCurrentStock() <= minStock;
                    })
                    .count();
            long stockNormal = totalProdutos - semStock - stockBaixo;

            return new EstatisticasStock(totalProdutos, semStock, stockBaixo, stockNormal);

        } catch (Exception e) {
            System.err.println("❌ Erro ao obter estatísticas: " + e.getMessage());
            return new EstatisticasStock(0, 0, 0, 0);
        }
    }

    /**
     * Classe interna para estatísticas
     */
    public static class EstatisticasStock {

        private final long totalProdutos;
        private final long semStock;
        private final long stockBaixo;
        private final long stockNormal;

        public EstatisticasStock(long totalProdutos, long semStock, long stockBaixo, long stockNormal) {
            this.totalProdutos = totalProdutos;
            this.semStock = semStock;
            this.stockBaixo = stockBaixo;
            this.stockNormal = stockNormal;
        }

        // Getters
        public long getTotalProdutos() {
            return totalProdutos;
        }

        public long getSemStock() {
            return semStock;
        }

        public long getStockBaixo() {
            return stockBaixo;
        }

        public long getStockNormal() {
            return stockNormal;
        }

        public double getPercentualSemStock() {
            return totalProdutos > 0 ? (semStock * 100.0) / totalProdutos : 0;
        }

        public double getPercentualStockBaixo() {
            return totalProdutos > 0 ? (stockBaixo * 100.0) / totalProdutos : 0;
        }
    }
}
