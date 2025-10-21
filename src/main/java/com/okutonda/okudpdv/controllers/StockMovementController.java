/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.PurchaseItemDao;
import com.okutonda.okudpdv.data.dao.StockMovementDao;
import com.okutonda.okudpdv.data.entities.StockMovement;
import com.okutonda.okudpdv.utilities.UserSession;
import com.okutonda.okudpdv.utilities.UtilDate;

import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author hr
 */
public class StockMovementController {

//    private final StockMovementDao dao;
    private StockMovementDao stockDao;
    private PurchaseItemDao purchaseItemDao;
    UserSession session = UserSession.getInstance();

    public StockMovementController() {
//        this.dao = new StockMovementDao();
        this.stockDao = new StockMovementDao();
        this.purchaseItemDao = new PurchaseItemDao();
    }

    // 📦 Listar movimentos por produto
    public List<StockMovement> listarPorProduto(int productId) {
        return stockDao.listByProduct(productId);
    }

    // 📊 Obter stock atual do produto
    public int getStockAtual(int productId) {
        return stockDao.getStockAtual(productId);
    }

    // 🧾 Listar todos os movimentos (ex: auditoria)
    public List<StockMovement> listarTodos() {
        return stockDao.listAll();
    }

    /**
     * Registra um movimento de stock aplicando as regras de negócio. - Verifica
     * se há stock suficiente em SAÍDA/TRANSFERÊNCIA - Impede novas entradas em
     * compras já completas - Usa transação para garantir consistência
     */
    public boolean registrar(StockMovement movimento) {
        try {
            if (!validarMovimento(movimento)) {
                return false; // ❌ se a validação falhar, não insere
            }

            // ✅ se chegou até aqui, está tudo validado
            return stockDao.add(movimento);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao registar movimento: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean validarMovimento(StockMovement movimento) {
        if (movimento == null) {
            JOptionPane.showMessageDialog(null, "Movimento inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (movimento.getProduct() == null) {
            JOptionPane.showMessageDialog(null, "Produto não definido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (movimento.getQuantity() <= 0) {
            JOptionPane.showMessageDialog(null, "A quantidade deve ser maior que zero!", "Erro", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String tipo = movimento.getType() != null ? movimento.getType().toUpperCase() : "";
        String origem = movimento.getOrigin() != null ? movimento.getOrigin().toUpperCase() : "MANUAL";

        return switch (tipo) {
            case "ENTRADA" ->
                validarEntrada(movimento, origem);
            case "SAIDA" ->
                validarSaida(movimento, origem);
            case "AJUSTE" ->
                validarAjuste(movimento, origem);
            default -> {
                JOptionPane.showMessageDialog(null, "Tipo de movimento desconhecido!", "Erro", JOptionPane.ERROR_MESSAGE);
                yield false;
            }
        };
    }

    private boolean validarEntrada(StockMovement movimento, String origem) {
        switch (origem) {
            case "COMPRA" -> {
                if (!purchaseItemDao.podeDarEntradaDeCompra(movimento)) {
                    JOptionPane.showMessageDialog(null,
                            "⚠️ O produto '" + movimento.getProduct().getDescription()
                            + "' já teve entrada total ou excede o limite da compra.",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }
            case "DEVOLUCAO" -> {
                // Nenhuma validação obrigatória aqui, mas pode vincular à venda original
            }
            case "AJUSTE" -> {
                // Entrada manual — sem restrição
            }
            case "TRANSFERENCIA" -> {
                // Entrada de outro armazém — sem restrição
            }
            default -> {
                // Outras origens manuais
            }
        }
        return true; // ✅ se chegou aqui, pode registrar
    }

    private boolean validarSaida(StockMovement movimento, String origem) {
        int stockAtual = stockDao.getStockAtual(movimento.getProduct().getId());
        int qtd = movimento.getQuantity();

        if (stockAtual < qtd) {
            JOptionPane.showMessageDialog(null,
                    "⚠️ Stock insuficiente para o produto: " + movimento.getProduct().getDescription()
                    + "\nDisponível: " + stockAtual + " | Tentado: " + qtd,
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Todas as saídas são gravadas como negativas
        movimento.setQuantity(-qtd);

        switch (origem) {
            case "VENDA" -> {
                // Saída de venda — sem mais validações
            }
            case "AJUSTE" -> {
                // Saída manual — permitido
            }
            case "TRANSFERENCIA" -> {
                // Saída de armazém origem — permitido
            }
            default -> {
                // Outras saídas genéricas — permitido
            }
        }

        return true; // ✅ se chegou aqui, pode registrar
    }

    private boolean validarAjuste(StockMovement movimento, String origem) {
        // Ajustes podem ser positivos (entrada) ou negativos (saída)
        if (movimento.getQuantity() < 0) {
            int stockAtual = stockDao.getStockAtual(movimento.getProduct().getId());
            if (stockAtual + movimento.getQuantity() < 0) {
                JOptionPane.showMessageDialog(null,
                        "⚠️ Ajuste inválido: o resultado deixaria o stock negativo para o produto "
                        + movimento.getProduct().getDescription(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

//    public boolean registrar(StockMovement movimento) {
//        int qtd = movimento.getQuantity();
//        String tipo = movimento.getType().toUpperCase();
//        String origem = movimento.getOrigin() != null ? movimento.getOrigin().toUpperCase() : "MANUAL";
//
//        try {
//            switch (tipo) {
//
//                // ============================
//                // 🔹 ENTRADA (aumenta stock)
//                // ============================
//                case "ENTRADA" -> {
//                    switch (origem) {
//                        case "COMPRA" -> {
//                            boolean pode = purchaseItemDao.podeDarEntradaDeCompra(movimento);
//                            if (!pode) {
//                                JOptionPane.showMessageDialog(null,
//                                        "⚠️ O produto '" + movimento.getProduct().getDescription()
//                                        + "' já teve entrada total no stock ou excede o que falta da compra.",
//                                        "Aviso", JOptionPane.WARNING_MESSAGE);
//                                return false;
//                            }
//                        }
//                        case "DEVOLUCAO" -> {
//                            // Entrada por devolução (ex: devolução de venda)
//                            // Aqui poderias associar ao documento de origem (nota de crédito)
//                            // Nenhuma restrição de quantidade
//                        }
//                        case "AJUSTE" -> {
//                            // Entrada manual — sem validação
//                        }
//                        case "TRANSFERENCIA" -> {
//                            // Entrada no armazém destino — sem validação
//                        }
//                        default -> {
//                            // Entrada manual ou outro tipo
//                        }
//                    }
//                }
//
//                // ============================
//                // 🔹 SAIDA (diminui stock)
//                // ============================
//                case "SAIDA" -> {
//                    int stockAtual = stockDao.getStockAtual(movimento.getProduct().getId());
//                    if (stockAtual < qtd) {
//                        JOptionPane.showMessageDialog(null,
//                                "Stock insuficiente para o produto: " + movimento.getProduct().getDescription(),
//                                "Erro", JOptionPane.ERROR_MESSAGE);
//                        return false;
//                    }
//
//                    // Saídas sempre gravadas como negativas
//                    movimento.setQuantity(-qtd);
//
//                    switch (origem) {
//                        case "VENDA" -> {
//                            // Saída de uma venda → sem alterar compras
//                        }
//                        case "AJUSTE" -> {
//                            // Saída manual para correção — permitido
//                        }
//                        case "TRANSFERENCIA" -> {
//                            // Saída de armazém origem — permitido
//                        }
//                        default -> {
//                            // Saída genérica — permitido
//                        }
//                    }
//                }
//
//                // ============================
//                // 🔹 AJUSTE (pode ser + ou -)
//                // ============================
//                case "AJUSTE" -> {
//                    // já tratado acima por origem
//                }
//
//                // ============================
//                // 🔹 TRANSFERENCIA (duas etapas)
//                // ============================
//                case "TRANSFERENCIA" -> {
//                    // normalmente já tratada externamente (cria dois movimentos)
//                }
//
//                default -> {
//                    JOptionPane.showMessageDialog(null, "Tipo de movimento desconhecido!", "Erro", JOptionPane.ERROR_MESSAGE);
//                    return false;
//                }
//            }
//
//            // ✅ Se passou por todas as regras → grava via DAO (com transação)
//            return stockDao.add(movimento);
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null,
//                    "Erro ao registar movimento: " + e.getMessage(),
//                    "Erro", JOptionPane.ERROR_MESSAGE);
//            return false;
//        }
//    }
}
