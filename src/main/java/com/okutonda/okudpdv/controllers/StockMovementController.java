package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.StockMovementDao;
import com.okutonda.okudpdv.data.entities.StockMovement;
import com.okutonda.okudpdv.helpers.UserSession;

import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;

/**
 * Controller responsável por validar e orquestrar movimentos de stock com Hibernate.
 *
 * Aplica as regras de negócio (compras, vendas, ajustes, transferências) e
 * garante consistência via transações do Hibernate.
 *
 * @author Hulquene
 */
public class StockMovementController {

    private final StockMovementDao stockDao;
    private final UserSession session = UserSession.getInstance();

    public StockMovementController() {
        this.stockDao = new StockMovementDao();
    }

    // ==========================================================
    // 🔹 Consultas
    // ==========================================================
    public List<StockMovement> listarPorProduto(Integer productId) {
        try {
            return stockDao.findByProductId(productId);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar movimentos por produto: " + e.getMessage());
            showError("Erro ao buscar movimentos de stock");
            return List.of();
        }
    }

    public Integer getStockAtual(Integer productId) {
        try {
            return stockDao.getCurrentStock(productId);
        } catch (Exception e) {
            System.err.println("❌ Erro ao calcular stock atual: " + e.getMessage());
            return 0;
        }
    }

    public List<StockMovement> listarTodos() {
        try {
            return stockDao.findAll();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar todos os movimentos: " + e.getMessage());
            showError("Erro ao buscar movimentos de stock");
            return List.of();
        }
    }

    public List<StockMovement> listarPorTipo(String tipo) {
        try {
            return stockDao.findByType(tipo);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar movimentos por tipo: " + e.getMessage());
            return List.of();
        }
    }

    // ==========================================================
    // 🔹 Registro com validações
    // ==========================================================
    /**
     * Registra um movimento de stock aplicando regras de negócio.
     */
    public StockMovement registrar(StockMovement movimento) {
        try {
            if (!validarMovimento(movimento)) {
                return null; // ❌ Falha de validação
            }

            // Define o usuário da sessão se não estiver definido
            if (movimento.getUser() == null && session.getUser() != null) {
                movimento.setUser(session.getUser());
            }

            // Executa inserção
            StockMovement savedMovement = stockDao.save(movimento);
            
            if (savedMovement != null) {
                System.out.println("✅ Movimento de stock registrado: " + 
                    movimento.getProduct().getDescription() + " - " + movimento.getQuantity());
                showSuccess("Movimento de stock registrado com sucesso!");
            }
            
            return savedMovement;

        } catch (Exception e) {
            System.err.println("❌ Erro ao registrar movimento: " + e.getMessage());
            showError("Erro ao registar movimento: " + e.getMessage());
            return null;
        }
    }

    /**
     * Registra múltiplos movimentos em lote
     */
    public boolean registrarEmLote(List<StockMovement> movimentos) {
        if (movimentos == null || movimentos.isEmpty()) {
            showWarning("Lista de movimentos vazia");
            return false;
        }

        try {
            // Valida todos os movimentos primeiro
            for (StockMovement movimento : movimentos) {
                if (!validarMovimento(movimento)) {
                    return false;
                }
                
                // Define o usuário da sessão se não estiver definido
                if (movimento.getUser() == null && session.getUser() != null) {
                    movimento.setUser(session.getUser());
                }
            }

            // Salva em lote (usando transação interna do DAO)
            for (StockMovement movimento : movimentos) {
                stockDao.save(movimento);
            }
            
            System.out.println("✅ " + movimentos.size() + " movimentos registrados em lote");
            showSuccess(movimentos.size() + " movimentos registrados com sucesso!");
            return true;

        } catch (Exception e) {
            System.err.println("❌ Erro ao registrar movimentos em lote: " + e.getMessage());
            showError("Erro ao registrar movimentos em lote: " + e.getMessage());
            return false;
        }
    }

    // ==========================================================
    // 🔹 Validações
    // ==========================================================
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

        if (movimento.getUser() == null && session.getUser() == null) {
            showError("Usuário não definido para o movimento!");
            return false;
        }

        String tipo = movimento.getType() != null ? movimento.getType().toUpperCase() : "";
        String origem = movimento.getOrigin() != null ? movimento.getOrigin().toUpperCase() : "MANUAL";

        return switch (tipo) {
            case "ENTRADA" -> validarEntrada(movimento, origem);
            case "SAIDA" -> validarSaida(movimento, origem);
            case "AJUSTE" -> validarAjuste(movimento, origem);
            default -> {
                showError("Tipo de movimento desconhecido: " + tipo);
                yield false;
            }
        };
    }

    private boolean validarEntrada(StockMovement movimento, String origem) {
        // Entradas são sempre positivas
        if (movimento.getQuantity() < 0) {
            movimento.setQuantity(Math.abs(movimento.getQuantity()));
        }
        
        // Validações específicas para compras
        if ("COMPRA".equalsIgnoreCase(origem)) {
            // Aqui você pode adicionar validações específicas para compras
            // Por exemplo, verificar se a quantidade não excede o pedido de compra
            System.out.println("🔍 Validação de entrada por compra: " + movimento.getProduct().getDescription());
        }
        
        return true;
    }

    private boolean validarSaida(StockMovement movimento, String origem) {
        // Para saídas, garante que a quantidade seja negativa
        if (movimento.getQuantity() > 0) {
            movimento.setQuantity(-movimento.getQuantity());
        }
        
        Integer stockAtual = getStockAtual(movimento.getProduct().getId());
        Integer qtd = Math.abs(movimento.getQuantity());

        if (stockAtual < qtd) {
            showError(
                "Stock insuficiente para o produto: " + movimento.getProduct().getDescription() +
                "\nDisponível: " + stockAtual + " | Tentado: " + qtd
            );
            return false;
        }

        return true;
    }

    private boolean validarAjuste(StockMovement movimento, String origem) {
        // Ajuste pode ser positivo (entrada) ou negativo (saída)
        if (movimento.getQuantity() < 0) {
            Integer stockAtual = getStockAtual(movimento.getProduct().getId());
            if (stockAtual + movimento.getQuantity() < 0) {
                showError(
                    "Ajuste inválido: o resultado deixaria o stock negativo para o produto " +
                    movimento.getProduct().getDescription()
                );
                return false;
            }
        }
        return true;
    }

    // ==========================================================
    // 🔹 Métodos Utilitários
    // ==========================================================
    
    /**
     * Calcula o saldo atual de um produto
     */
    public Integer calcularSaldoProduto(Integer productId) {
        return getStockAtual(productId);
    }

    /**
     * Busca movimentos por período (implementação básica)
     */
    public List<StockMovement> listarPorPeriodo(String dataInicio, String dataFim) {
        try {
            // Implementação simplificada - você pode adaptar conforme necessidade
            List<StockMovement> todos = stockDao.findAll();
            return todos.stream()
                .filter(mov -> isBetweenDates(mov.getCreatedAt().toString(), dataInicio, dataFim))
                .toList();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar movimentos por período: " + e.getMessage());
            return List.of();
        }
    }

    private boolean isBetweenDates(String data, String inicio, String fim) {
        // Implementação simplificada de comparação de datas
        try {
            return data.compareTo(inicio) >= 0 && data.compareTo(fim) <= 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Remove um movimento de stock
     */
    public boolean removerMovimento(Integer movimentoId) {
        try {
            stockDao.delete(movimentoId);
            System.out.println("✅ Movimento removido: " + movimentoId);
            showSuccess("Movimento de stock removido com sucesso!");
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erro ao remover movimento: " + e.getMessage());
            showError("Erro ao remover movimento: " + e.getMessage());
            return false;
        }
    }

    // ==========================================================
    // 🔹 Helpers de UI
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
}