package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.ProductDao;
import com.okutonda.okudpdv.data.entities.Product;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Controller responsável pela lógica de alto nível dos produtos com Hibernate.
 *
 * Intermedia o acesso entre UI e ProductDaoHibernate (CRUD, filtros, PDV,
 * inventário).
 *
 * @author …
 */
public class ProductController {

    private final ProductDao dao;

    public ProductController() {
        this.dao = new ProductDao();
    }

    // ==========================================================
    // 🔹 CONSULTAS
    // ==========================================================
    public Product getById(Integer id) {
        Optional<Product> productOpt = dao.findById(id);
        return productOpt.orElse(null);
    }

    public Product getByBarcode(String barcode) {
        Optional<Product> productOpt = dao.findByBarcode(barcode);
        return productOpt.orElse(null);
    }

    public Product getByDescription(String description) {
        Optional<Product> productOpt = dao.findByDescription(description);
        return productOpt.orElse(null);
    }

    public List<Product> listAll() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produtos: " + e.getMessage());
            return List.of();
        }
    }

    public List<Product> listProducts() {
        try {
            return dao.findByType("product");
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produtos: " + e.getMessage());
            return List.of();
        }
    }

    public List<Product> listServices() {
        try {
            return dao.findByType("service");
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar serviços: " + e.getMessage());
            return List.of();
        }
    }

    public List<Product> listForInventory() {
        try {
            // Nota: Implementação simplificada - você pode adaptar conforme necessidade
            return dao.findActive();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produtos para inventário: " + e.getMessage());
            return List.of();
        }
    }

    public List<Product> listForPDV(String filtro) {
        try {
            return dao.findForPDV(filtro);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produtos para PDV: " + e.getMessage());
            return List.of();
        }
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public Product save(Product product) {
        if (product == null) {
            System.err.println("❌ Produto inválido.");
            return null;
        }

        try {
            Product savedProduct;

            if (product.getId() == null || product.getId() <= 0) {
                // Validar duplicados antes de criar
                if (product.getBarcode() != null && !product.getBarcode().trim().isEmpty()) {
                    if (dao.barcodeExists(product.getBarcode())) {
                        System.err.println("❌ Já existe um produto com este código de barras: " + product.getBarcode());
                        return null;
                    }
                }

                if (product.getCode() != null && !product.getCode().trim().isEmpty()) {
                    if (dao.codeExists(product.getCode())) {
                        System.err.println("❌ Já existe um produto com este código: " + product.getCode());
                        return null;
                    }
                }

                savedProduct = dao.save(product);
            } else {
                savedProduct = dao.update(product);
            }

            System.out.println("✅ Produto salvo: " + savedProduct.getDescription());
            return savedProduct;

        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar produto: " + e.getMessage());
            return null;
        }
    }

    public boolean delete(Integer id) {
        try {
            dao.delete(id);
            System.out.println("✅ Produto removido ID: " + id);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erro ao remover produto: " + e.getMessage());
            return false;
        }
    }

    // ==========================================================
    // 🔹 UTILITÁRIOS
    // ==========================================================
    /**
     * Calcula total de produto × quantidade
     */
    public BigDecimal calculateTotal(Product prod, Integer qty) {
        if (prod == null || prod.getPrice() == null || qty == null) {
            return BigDecimal.ZERO;
        }
        return prod.getPrice().multiply(BigDecimal.valueOf(qty));
    }

    /**
     * Ativa/desativa um produto
     */
    public boolean toggleProductStatus(Integer id) {
        try {
            Optional<Product> productOpt = dao.findById(id);
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                product.setStatus(product.getStatus() == 1 ? 0 : 1);
                dao.update(product);
                System.out.println("✅ Status do produto atualizado: " + product.getDescription());
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("❌ Erro ao alterar status do produto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Filtra produtos por texto
     */
    public List<Product> filter(String text) {
        try {
            return dao.filter(text);
        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar produtos: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Busca produtos ativos
     */
    public List<Product> findActive() {
        try {
            return dao.findActive();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar produtos ativos: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Verifica se código de barras já existe
     */
    public boolean barcodeExists(String barcode) {
        return dao.findByBarcode(barcode).isPresent();
    }

    /**
     * Verifica se código de produto já existe
     */
    public boolean codeExists(String code) {
        return dao.codeExists(code);
    }
}
