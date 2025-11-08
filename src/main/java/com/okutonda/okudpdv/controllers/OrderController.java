package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.OrderDao;
import com.okutonda.okudpdv.data.dao.PaymentDao;
import com.okutonda.okudpdv.data.dao.ProductOrderDao;
import com.okutonda.okudpdv.data.dao.StockMovementDao;
import com.okutonda.okudpdv.data.entities.*;
import com.okutonda.okudpdv.helpers.UserSession;
import com.okutonda.okudpdv.helpers.UtilDate;
import com.okutonda.okudpdv.helpers.PrintHelper;
import com.okutonda.okudpdv.helpers.UtilSaft;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controlador principal de Pedidos (Orders) com Hibernate
 *
 * Responsável por criar pedidos completos, registrar pagamentos e movimentar
 * stock em uma transação única.
 *
 * @author Hulquene
 */
public class OrderController {

    private final OrderDao orderDao;
    private final ProductOrderDao productOrderDao;
    private final PaymentDao paymentDao;
    private final StockMovementDao stockMovementDao;
    private final UserSession session = UserSession.getInstance();

    public OrderController() {
        this.orderDao = new OrderDao();
        this.productOrderDao = new ProductOrderDao();
        this.paymentDao = new PaymentDao();
        this.stockMovementDao = new StockMovementDao();
    }

    // ==========================================================
    // 🔹 CONSULTAS BÁSICAS
    // ==========================================================
    public List<Order> getAll() {
        try {
            // Se for admin/manager, mostra todos, senão apenas os do vendedor
            if (isAdminOrManager()) {
                return orderDao.findAll();
            } else {
                return orderDao.findBySellerId(session.getUser().getId());
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar pedidos: " + e.getMessage());
            return List.of();
        }
    }

    public Order getById(Integer id) {
        Optional<Order> orderOpt = orderDao.findById(id);
        return orderOpt.orElse(null);
    }

    public List<Order> filterByDate(LocalDate from, LocalDate to) {
        try {
            // Filtro por data com controle de acesso
            List<Order> allOrders = orderDao.filterByDate(from, to);
            if (isAdminOrManager()) {
                return allOrders;
            } else {
                return allOrders.stream()
                        .filter(order -> order.getSeller() != null
                        && order.getSeller().getId().equals(session.getUser().getId()))
                        .toList();
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar pedidos por data: " + e.getMessage());
            return List.of();
        }
    }

    public List<Order> filter(String text) {
        try {
            List<Order> filtered = orderDao.filter(text);
            if (isAdminOrManager()) {
                return filtered;
            } else {
                return filtered.stream()
                        .filter(order -> order.getSeller() != null
                        && order.getSeller().getId().equals(session.getUser().getId()))
                        .toList();
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar pedidos: " + e.getMessage());
            return List.of();
        }
    }

    // ==========================================================
    // 🔹 CRIAÇÃO DE PEDIDO COMPLETO
    // ==========================================================
    public Order criarComPagamentos(Order order, List<Payment> pagamentos) {
        if (order == null || order.getProducts() == null || order.getProducts().isEmpty()) {
            System.err.println("❌ Pedido sem produtos.");
            return null;
        }
        if (pagamentos == null || pagamentos.isEmpty()) {
            System.err.println("❌ Sem pagamentos associados.");
            return null;
        }

        try {
            // Calcular totais com IVA
            Totais t = calcularTotaisComIva(order.getProducts());
            order.setSubTotal(t.subTotal);
            order.setTotalTaxe(t.tax);
            order.setTotal(t.total);

            // Numerar e gerar hash
            String prefix = DocumentType.FR.getPrefix();
            Integer number = orderDao.getNextNumber();
            String date = UtilDate.getFormatDataNow();
            Integer year = UtilDate.getYear();
            String numberOrder = PrintHelper.formatDocumentNumber(number, year, prefix);
            String hash = UtilSaft.appGenerateHashInvoice(date, date, numberOrder,
                    String.valueOf(order.getTotal()), "");

            order.setPrefix(prefix);
            order.setNumber(number);
            order.setDatecreate(date);
            order.setHash(hash);
            order.setYear(year);
            order.setStatus(PaymentStatus.PAGO);

            // Define o vendedor se não estiver definido
            if (order.getSeller() == null && session.getUser() != null) {
                order.setSeller(session.getUser());
            }

            // ✅ PASSO 1: Salva o pedido principal PRIMEIRO
            Order savedOrder = orderDao.save(order);
            if (savedOrder == null) {
                throw new RuntimeException("Falha ao gravar cabeçalho do pedido.");
            }

            System.out.println("✅ Order salvo - ID: " + savedOrder.getId() + ", Número: " + savedOrder.getNumber());

            // ✅ PASSO 2: AGORA salva os ProductSales com documentId CORRETO
            for (ProductSales po : order.getProducts()) {
                // ✅ CORREÇÃO: documentId deve ser o NÚMERO do documento, não o ID
                po.setDocumentId(savedOrder.getNumber()); // ✅ Usa savedOrder.getNumber() que já está salvo
                po.setDocumentType(DocumentType.FR);
                po.setOrder(savedOrder);

                // ✅ DEBUG: Verificar dados antes de salvar
                System.out.println("🔍 ProductSales - DocumentId: " + po.getDocumentId()
                        + ", DocumentType: " + po.getDocumentType()
                        + ", Valid: " + po.isValid());

                if (!po.isValid()) {
                    throw new RuntimeException("Item de venda inválido: " + po.getDescription());
                }

                // Salva o item
                productOrderDao.save(po);
                System.out.println("✅ ProductSales salvo - DocumentId: " + po.getDocumentId());

                // Registra movimento de stock (saída)
                StockMovement mov = new StockMovement();
                mov.setProduct(po.getProduct());
                mov.setQuantity(-po.getQty());
                mov.setOrigin("VENDA");
                mov.setType("SAIDA");
                mov.setReason("VENDA " + savedOrder.getPrefix() + "/" + savedOrder.getNumber());
                mov.setUser(savedOrder.getSeller());
                stockMovementDao.save(mov);
            }

            // ✅ PASSO 3: Salva os pagamentos
            for (Payment p : pagamentos) {
                p.setInvoiceId(savedOrder.getId());
                p.setInvoiceType(DocumentType.FR);
                p.setPrefix(savedOrder.getPrefix());
                p.setNumber(savedOrder.getNumber());
                p.setClient(savedOrder.getClient());
                p.setUser(savedOrder.getSeller());

                if (p.getDate() == null) {
                    p.setDate(date);
                }
                if (p.getCurrency() == null) {
                    p.setCurrency("AOA");
                }
                if (p.getStatus() == null) {
                    p.setStatus(PaymentStatus.PAGO);
                }

                paymentDao.save(p);
            }

            System.out.println("✅ Pedido criado com sucesso: " + savedOrder.getPrefix() + "/" + savedOrder.getNumber());
            return savedOrder;

        } catch (Exception e) {
            System.err.println("❌ Erro ao processar venda: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // ==========================================================
    // 🔹 Métodos Auxiliares
    // ==========================================================
    /**
     * Obtém o próximo número de pedido
     */
    public Integer getNextNumber() {
        return orderDao.getNextNumber();
    }

    /**
     * Busca pedidos por cliente
     */
    public List<Order> getByClientId(Integer clientId) {
        try {
            List<Order> orders = orderDao.findByClientId(clientId);
            if (isAdminOrManager()) {
                return orders;
            } else {
                return orders.stream()
                        .filter(order -> order.getSeller() != null
                        && order.getSeller().getId().equals(session.getUser().getId()))
                        .toList();
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar pedidos por cliente: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Busca pedidos por vendedor
     */
    public List<Order> getBySellerId(Integer sellerId) {
        try {
            // Apenas admin/manager pode ver pedidos de outros vendedores
            if (!isAdminOrManager() && !sellerId.equals(session.getUser().getId())) {
                System.err.println("❌ Acesso não autorizado para ver pedidos de outros vendedores");
                return List.of();
            }
            return orderDao.findBySellerId(sellerId);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar pedidos por vendedor: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Altera status do pedido
     */
    public boolean alterarStatus(Integer orderId, Integer novoStatus) {
        try {
            Optional<Order> orderOpt = orderDao.findById(orderId);
            if (orderOpt.isPresent()) {
                Order order = orderOpt.get();

                // Verifica permissão
                if (!isAdminOrManager()
                        && !order.getSeller().getId().equals(session.getUser().getId())) {
                    System.err.println("❌ Acesso não autorizado para alterar este pedido");
                    return false;
                }

                order.setStatus(novoStatus);
                orderDao.update(order);
                System.out.println("✅ Status do pedido atualizado: " + orderId + " -> " + novoStatus);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("❌ Erro ao alterar status do pedido: " + e.getMessage());
            return false;
        }
    }

    /**
     * Calcula estatísticas de vendas
     */
    public EstatisticasVendas calcularEstatisticas(LocalDate from, LocalDate to) {
        try {
            Double totalVendas = orderDao.calculateTotalSalesByPeriod(from, to);
            Long pedidosConcluidos = orderDao.countByStatus(2);
            Long pedidosPendentes = orderDao.countByStatus(1);

            return new EstatisticasVendas(totalVendas, pedidosConcluidos, pedidosPendentes);
        } catch (Exception e) {
            System.err.println("❌ Erro ao calcular estatísticas: " + e.getMessage());
            return new EstatisticasVendas(0.0, 0L, 0L);
        }
    }

    // ==========================================================
    // 🔹 Classes Internas
    // ==========================================================
    private static class Totais {

        final BigDecimal subTotal;
        final BigDecimal tax;
        final BigDecimal total;

        Totais(BigDecimal s, BigDecimal i, BigDecimal t) {
            this.subTotal = s;
            this.tax = i;
            this.total = t;
        }
    }

    public static class EstatisticasVendas {

        public final Double totalVendas;
        public final Long pedidosConcluidos;
        public final Long pedidosPendentes;

        public EstatisticasVendas(Double totalVendas, Long pedidosConcluidos, Long pedidosPendentes) {
            this.totalVendas = totalVendas;
            this.pedidosConcluidos = pedidosConcluidos;
            this.pedidosPendentes = pedidosPendentes;
        }
    }

    private Totais calcularTotaisComIva(List<ProductSales> itens) {
        BigDecimal sub = BigDecimal.ZERO, iva = BigDecimal.ZERO, tot = BigDecimal.ZERO;
        for (ProductSales po : itens) {
            if (po == null || po.getProduct() == null) {
                continue;
            }
            BigDecimal qty = BigDecimal.valueOf(po.getQty());
            BigDecimal price = po.getPrice() != null ? po.getPrice() : BigDecimal.ZERO;
            BigDecimal gross = price.multiply(qty);

            BigDecimal perc = po.getTaxePercentage() == null ? BigDecimal.ZERO : po.getTaxePercentage();
            BigDecimal lineTax = BigDecimal.ZERO, lineNet = gross;

            if (perc.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal base = new BigDecimal("100").add(perc);
                lineTax = gross.multiply(perc).divide(base, 2, RoundingMode.HALF_UP);
                lineNet = gross.subtract(lineTax);
            }

            sub = sub.add(lineNet);
            iva = iva.add(lineTax);
            tot = tot.add(gross);
        }
        return new Totais(sub, iva, tot);
    }

    private boolean isAdminOrManager() {
        User user = session.getUser();
        return user != null
                && ("admin".equals(user.getProfile()) || "manager".equals(user.getProfile()));
    }
}
