package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.dtos.ProductStockReport;
import com.okutonda.okudpdv.services.ProductService;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Controller atualizado usando ProductService Mantém compatibilidade com código
 * existente
 */
public class ProductController {

    private final ProductService productService;

    public ProductController() {
        this.productService = new ProductService();
    }

    // ==========================================================
    // 🔹 MÉTODOS DE CONSULTA (Compatibilidade)
    // ==========================================================
    public Product getById(Integer id) {
        try {
            return productService.buscarPorId(id);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produto por ID: " + e.getMessage());
            showError("Erro ao buscar produto: " + e.getMessage());
            return null;
        }
    }

    public Product getByBarcode(String barcode) {
        try {
            return productService.buscarPorCodigoBarras(barcode);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produto por código de barras: " + e.getMessage());
            return null;
        }
    }

    public Product getByDescription(String description) {
        try {
            return productService.buscarPorDescricao(description);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produto por descrição: " + e.getMessage());
            return null;
        }
    }

    public List<Product> listAll() {
        try {
            return productService.listarTodos();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar todos os produtos: " + e.getMessage());
            showError("Erro ao buscar produtos: " + e.getMessage());
            return List.of();
        }
    }

    public List<Product> listProducts() {
        try {
            return productService.listarProdutos();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produtos: " + e.getMessage());
            return List.of();
        }
    }

    public List<Product> listServices() {
        try {
            return productService.listarServicos();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar serviços: " + e.getMessage());
            return List.of();
        }
    }

    public List<Product> listForInventory() {
        try {
            return productService.listarParaInventario();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produtos para inventário: " + e.getMessage());
            return List.of();
        }
    }

    public List<Product> listForPDV(String filtro) {
        try {
            return productService.listarParaPDV(filtro);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produtos para PDV: " + e.getMessage());
            return List.of();
        }
    }

    // ==========================================================
    // 🔹 NOVOS MÉTODOS PARA STOCK
    // ==========================================================
    /**
     * Lista produtos com stock abaixo do mínimo
     */
    public List<Product> listarProdutosComStockMinimo() {
        try {
            return productService.listarProdutosComStockMinimo();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produtos com stock mínimo: " + e.getMessage());
            showError("Erro ao buscar alertas de stock: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Lista produtos com stock crítico
     */
    public List<Product> listarProdutosComStockCritico() {
        try {
            return productService.listarProdutosComStockCritico();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produtos com stock crítico: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Lista produtos sem stock
     */
    public List<Product> listarProdutosSemStock() {
        try {
            return productService.listarProdutosSemStock();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produtos sem stock: " + e.getMessage());
            return List.of();
        }
    }

    // ==========================================================
    // 🔹 OPERAÇÕES CRUD
    // ==========================================================
    // No ProductController - JÁ ESTÁ CORRETO assim:
    public Product save(Product product) {
        try {
            return productService.salvar(product);
        } catch (IllegalArgumentException e) {
            // ✅ Erro de validação de negócio - mostrar para usuário
            showError(e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar produto: " + e.getMessage());
            showError("Erro ao salvar produto: " + e.getMessage());
            return null;
        }
    }

    public boolean delete(Integer id) {
        try {
            return productService.excluir(id);
        } catch (Exception e) {
            System.err.println("❌ Erro ao remover produto: " + e.getMessage());
            showError("Erro ao remover produto: " + e.getMessage());
            return false;
        }
    }

    // ==========================================================
    // 🔹 MÉTODOS UTILITÁRIOS
    // ==========================================================
    public BigDecimal calculateTotal(Product prod, Integer qty) {
        try {
            return productService.calcularTotal(prod, qty);
        } catch (Exception e) {
            System.err.println("❌ Erro ao calcular total: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public boolean toggleProductStatus(Integer id) {
        try {
            return productService.alternarStatusProduto(id);
        } catch (Exception e) {
            System.err.println("❌ Erro ao alterar status do produto: " + e.getMessage());
            showError("Erro ao alterar status do produto: " + e.getMessage());
            return false;
        }
    }

    public List<Product> filter(String text) {
        try {
            return productService.filtrar(text);
        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar produtos: " + e.getMessage());
            return List.of();
        }
    }

    public List<Product> findActive() {
        try {
            return productService.listarAtivos();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produtos ativos: " + e.getMessage());
            return List.of();
        }
    }

    public boolean barcodeExists(String barcode) {
        try {
            return productService.verificarCodigoBarrasExiste(barcode);
        } catch (Exception e) {
            System.err.println("❌ Erro ao verificar código de barras: " + e.getMessage());
            return false;
        }
    }

    public boolean codeExists(String code) {
        try {
            return productService.verificarCodigoExiste(code);
        } catch (Exception e) {
            System.err.println("❌ Erro ao verificar código: " + e.getMessage());
            return false;
        }
    }

    // ==========================================================
    // 🔹 RELATÓRIOS (Novos métodos)
    // ==========================================================
    public Long contarProdutosAtivos() {
        try {
            return productService.contarProdutosAtivos();
        } catch (Exception e) {
            System.err.println("❌ Erro ao contar produtos ativos: " + e.getMessage());
            return 0L;
        }
    }

    public Long contarProdutosComStockMinimo() {
        try {
            return productService.contarProdutosComStockMinimo();
        } catch (Exception e) {
            System.err.println("❌ Erro ao contar produtos com stock mínimo: " + e.getMessage());
            return 0L;
        }
    }

    // ==========================================================
    // 🔹 HELPERS DE UI
    // ==========================================================
    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    // NO ProductController
    /**
     * Lista produtos para inventário com relatório completo de stock
     */
    public List<ProductStockReport> listForInventoryWithStockReport() {
        try {
            StockMovementController stockController = new StockMovementController();
            List<Product> produtos = productService.listarParaInventario();

            return stockController.gerarRelatorioParaProdutos(produtos);

        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar relatório de inventário: " + e.getMessage());
            return List.of();
        }
    }
    // ==========================================================
// 🔹 MÉTODOS FALTANTES PARA CONSULTA POR CÓDIGO
// ==========================================================

    /**
     * Busca produto por código interno
     */
    public Product getByCode(String code) {
        try {
            return productService.buscarPorCodigo(code);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produto por código: " + e.getMessage());
            return null;
        }
    }

    /**
     * Busca produto por código ou código de barras
     */
    public Product getByCodeOrBarcode(String code) {
        try {
            return productService.buscarPorCodigoOuCodigoBarras(code);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produto por código/barcode: " + e.getMessage());
            return null;
        }
    }
}
