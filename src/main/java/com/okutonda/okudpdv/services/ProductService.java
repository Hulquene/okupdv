package com.okutonda.okudpdv.services;

import com.okutonda.okudpdv.data.dao.ProductDao;
import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.ProductStatus;
import com.okutonda.okudpdv.data.entities.ProductType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service responsável pela lógica de negócio dos produtos
 */
public class ProductService {

    private final ProductDao productDao;

    public ProductService() {
        this.productDao = new ProductDao();
    }

    // ==========================================================
    // 🔹 CONSULTAS BÁSICAS
    // ==========================================================
    public Product buscarPorId(Integer id) {
        return productDao.findById(id).orElse(null);
    }

    public Product buscarPorCodigoBarras(String barcode) {
        return productDao.findByBarcode(barcode).orElse(null);
    }

    public Product buscarPorDescricao(String description) {
        return productDao.findByDescription(description).orElse(null);
    }

    public List<Product> listarTodos() {
        try {
//            List<Product> p = productDao.findAll();
//            System.out.println("listarTodos:" + p);
            return productDao.findAll();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar todos os produtos: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar produtos", e);
        }
    }

    public List<Product> listarProdutos() {
        try {
            return productDao.findByType(ProductType.PRODUCT);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produtos: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar produtos", e);
        }
    }

    public List<Product> listarServicos() {
        try {
            return productDao.findByType(ProductType.SERVICE);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar serviços: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar serviços", e);
        }
    }

    // ==========================================================
    // 🔹 CONSULTAS PARA INVENTÁRIO E PDV
    // ==========================================================
    public List<Product> listarParaInventario() {
        try {
            return productDao.findActive();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produtos para inventário: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar produtos para inventário", e);
        }
    }

    public List<Product> listarParaPDV(String filtro) {
        try {
            return productDao.findForPDV(filtro);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produtos para PDV: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar produtos para PDV", e);
        }
    }

    public List<Product> listarAtivos() {
        try {
            return productDao.findActive();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produtos ativos: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar produtos ativos", e);
        }
    }

    // ==========================================================
    // 🔹 CONSULTAS ESPECÍFICAS PARA STOCK
    // ==========================================================
    /**
     * Lista produtos com stock abaixo do mínimo
     */
    public List<Product> listarProdutosComStockMinimo() {
        try {
            // Primeiro busca todos os produtos ativos
            List<Product> produtosAtivos = productDao.findActive();

            // Filtra os que têm stock atual menor ou igual ao stock mínimo
            return produtosAtivos.stream()
                    .filter(produto -> {
                        Integer stockAtual = produto.getCurrentStock() != null ? produto.getCurrentStock() : 0;
                        Integer stockMinimo = produto.getMinStock() != null ? produto.getMinStock() : 0;
                        return stockAtual <= stockMinimo;
                    })
                    .toList();

        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produtos com stock mínimo: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar produtos com stock mínimo", e);
        }
    }

    /**
     * Lista produtos com stock crítico (abaixo de 50% do mínimo)
     */
    public List<Product> listarProdutosComStockCritico() {
        try {
            List<Product> produtosAtivos = productDao.findActive();

            return produtosAtivos.stream()
                    .filter(produto -> {
                        Integer stockAtual = produto.getCurrentStock() != null ? produto.getCurrentStock() : 0;
                        Integer stockMinimo = produto.getMinStock() != null ? produto.getMinStock() : 0;

                        if (stockMinimo == 0) {
                            return false; // Ignora produtos sem stock mínimo definido
                        }
                        double percentual = (double) stockAtual / stockMinimo;
                        return percentual <= 0.5; // 50% ou menos do stock mínimo
                    })
                    .toList();

        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produtos com stock crítico: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar produtos com stock crítico", e);
        }
    }

    /**
     * Lista produtos sem stock (stock = 0)
     */
    public List<Product> listarProdutosSemStock() {
        try {
            List<Product> produtosAtivos = productDao.findActive();

            return produtosAtivos.stream()
                    .filter(produto -> {
                        Integer stockAtual = produto.getCurrentStock() != null ? produto.getCurrentStock() : 0;
                        return stockAtual == 0;
                    })
                    .toList();

        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produtos sem stock: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar produtos sem stock", e);
        }
    }

    // ==========================================================
    // 🔹 OPERAÇÕES CRUD
    // ==========================================================
    // No ProductService - JÁ ESTÁ CORRETO assim:
    public Product salvar(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }

        try {
            // ✅ Validações de negócio
            validarProduto(product);

            Product produtoSalvo;

            if (product.getId() == null || product.getId() <= 0) {
                // ✅ Valida duplicados antes de criar
                validarDuplicados(product);
                produtoSalvo = productDao.save(product);
            } else {
                produtoSalvo = productDao.update(product);
            }

            System.out.println("✅ Produto salvo: " + produtoSalvo.getDescription());
            return produtoSalvo;

        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar produto: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar produto: " + e.getMessage(), e);
        }
    }

    public boolean excluir(Integer id) {
        try {
            productDao.delete(id);
            System.out.println("✅ Produto removido ID: " + id);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erro ao remover produto: " + e.getMessage());
            throw new RuntimeException("Erro ao remover produto", e);
        }
    }

    // ==========================================================
    // 🔹 VALIDAÇÕES
    // ==========================================================
    private void validarProduto(Product product) {
        if (product.getDescription() == null || product.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição do produto é obrigatória");
        }

        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preço do produto deve ser maior ou igual a zero");
        }
    }

    private void validarDuplicados(Product product) {
        if (product.getBarcode() != null && !product.getBarcode().trim().isEmpty()) {
            if (productDao.barcodeExists(product.getBarcode())) {
                throw new IllegalArgumentException("Já existe um produto com este código de barras: " + product.getBarcode());
            }
        }

        if (product.getCode() != null && !product.getCode().trim().isEmpty()) {
            if (productDao.codeExists(product.getCode())) {
                throw new IllegalArgumentException("Já existe um produto com este código: " + product.getCode());
            }
        }
    }

    // ==========================================================
    // 🔹 UTILITÁRIOS
    // ==========================================================
    public BigDecimal calcularTotal(Product produto, Integer quantidade) {
        if (produto == null || produto.getPrice() == null || quantidade == null) {
            return BigDecimal.ZERO;
        }
        return produto.getPrice().multiply(BigDecimal.valueOf(quantidade));
    }

    public boolean alternarStatusProduto(Integer id) {
        try {
            Optional<Product> produtoOpt = productDao.findById(id);
            if (produtoOpt.isPresent()) {
                Product produto = produtoOpt.get();
                produto.setStatus(produto.getStatus() == ProductStatus.ACTIVE ? ProductStatus.INACTIVE : ProductStatus.ACTIVE);
                productDao.update(produto);
                System.out.println("✅ Status do produto atualizado: " + produto.getDescription());
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("❌ Erro ao alterar status do produto: " + e.getMessage());
            throw new RuntimeException("Erro ao alterar status do produto", e);
        }
    }

    public List<Product> filtrar(String texto) {
        try {
            return productDao.filter(texto);
        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar produtos: " + e.getMessage());
            throw new RuntimeException("Erro ao filtrar produtos", e);
        }
    }

    public boolean verificarCodigoBarrasExiste(String barcode) {
        return productDao.findByBarcode(barcode).isPresent();
    }

    public boolean verificarCodigoExiste(String code) {
        return productDao.codeExists(code);
    }

    // ==========================================================
    // 🔹 RELATÓRIOS E ESTATÍSTICAS
    // ==========================================================
    /**
     * Conta total de produtos ativos
     */
    public Long contarProdutosAtivos() {
        try {
            List<Product> produtosAtivos = productDao.findActive();
            return (long) produtosAtivos.size();
        } catch (Exception e) {
            System.err.println("❌ Erro ao contar produtos ativos: " + e.getMessage());
            return 0L;
        }
    }

    /**
     * Conta produtos com stock mínimo
     */
    public Long contarProdutosComStockMinimo() {
        try {
            List<Product> produtosComAlerta = listarProdutosComStockMinimo();
            return (long) produtosComAlerta.size();
        } catch (Exception e) {
            System.err.println("❌ Erro ao contar produtos com stock mínimo: " + e.getMessage());
            return 0L;
        }
    }

}
