/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.okutonda.okudpdv.views.dashboard;

import com.okutonda.okudpdv.controllers.OrderController;

import com.okutonda.okudpdv.controllers.*;
import com.okutonda.okudpdv.data.entities.DocumentType;
import com.okutonda.okudpdv.data.entities.Invoices;
import com.okutonda.okudpdv.data.entities.Order;
import com.okutonda.okudpdv.data.entities.PaymentMode;
import com.okutonda.okudpdv.data.entities.PaymentStatus;
import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.ProductSales;
import com.okutonda.okudpdv.helpers.UserSession;
import java.awt.BasicStroke;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.JPanel;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;

/**
 *
 * @author kenny
 */
public class JPanelDashboard extends javax.swing.JPanel {

    UserSession session;
    private javax.swing.JPanel panelGraficos;
    private javax.swing.JScrollPane scrollPane;
    private OrderController orderController;
    private PaymentController paymentController;
    private InvoiceController invoiceController;
    private ClientController clientController;
    private ProductController productController;
    private StockMovementController stockController;

    /**
     * Creates new form JPanelDashboard
     */
    public JPanelDashboard() {
        initComponents();
        session = UserSession.getInstance();
        // Inicializar controllers
        inicializarControllers();

        // Configurar dashboard com gráficos
        configurarDashboardComGraficos();
    }

    /**
     * Inicializa os controllers
     */
    private void inicializarControllers() {
        try {
            orderController = new OrderController();
            paymentController = new PaymentController();
            invoiceController = new InvoiceController();
            clientController = new ClientController();
            productController = new ProductController();
            stockController = new StockMovementController();
        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar controllers: " + e.getMessage());
        }
    }

    /**
     * Configura o dashboard com múltiplos gráficos
     */
    private void configurarDashboardComGraficos() {
        try {
            // Mudar layout do painel principal
            this.setLayout(new BorderLayout());

            // Criar painel principal para gráficos com GridLayout para 2 colunas
            panelGraficos = new javax.swing.JPanel();
            panelGraficos.setLayout(new java.awt.GridLayout(0, 2, 15, 15)); // 0 linhas = automático, 2 colunas
            panelGraficos.setBackground(new java.awt.Color(240, 240, 240));
            panelGraficos.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

            // Adicionar gráficos (agora podemos adicionar mais de 4)
            adicionarGraficoVendasPorTipo();
            adicionarGraficoPagamentosPercentagem();
            adicionarGraficoTotalFaturasFTFR();
            adicionarGraficoClientes();
            adicionarGraficoStock(); // 5º gráfico
            adicionarGraficoVendasMensais(); // 6º gráfico
            adicionarGraficoTopProdutos(); // 7º gráfico
            adicionarGraficoEstatisticasPagamentos(); // 8º gráfico

            // Criar JScrollPane para conter os gráficos
            scrollPane = new javax.swing.JScrollPane(panelGraficos);
            scrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Scroll mais suave

            // Adicionar scrollPane ao painel principal
            this.add(scrollPane, BorderLayout.CENTER);

            // Atualizar interface
            this.revalidate();
            this.repaint();

        } catch (Exception e) {
            System.err.println("❌ Erro ao configurar dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gráfico 1: Vendas por Mês separadas por FT e FR
     */
    private void adicionarGraficoVendasPorTipo() {
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

//            boolean dadosReaisDisponiveis = false;
            // Tentar obter dados reais
            try {
                LocalDate hoje = LocalDate.now();

                // Calcular vendas dos últimos 6 meses
                for (int i = 5; i >= 0; i--) {
                    LocalDate mes = hoje.minusMonths(i);
                    LocalDate inicio = mes.withDayOfMonth(1);
                    LocalDate fim = mes.withDayOfMonth(mes.lengthOfMonth());

                    String nomeMes = getNomeMes(mes.getMonthValue());

                    // CORREÇÃO: Usar os métodos corrigidos
                    BigDecimal vendasFT = calcularVendasPorTipo("FT", inicio, fim);
                    BigDecimal vendasFR = calcularVendasPorTipo("FR", inicio, fim);

                    System.out.println("vendasFT: " + vendasFT + ", vendasFR: " + vendasFR);

                    // CORREÇÃO: Converter BigDecimal para double para o gráfico
                    dataset.addValue(vendasFT.doubleValue(), "Fatura (FT)", nomeMes);
                    dataset.addValue(vendasFR.doubleValue(), "Recibo (FR)", nomeMes);

//                    if (vendasFT.compareTo(BigDecimal.ZERO) > 0 || vendasFR.compareTo(BigDecimal.ZERO) > 0) {
//                        dadosReaisDisponiveis = true;
//                    }
                }
            } catch (Exception e) {
                System.err.println("❌ Erro ao obter dados reais de vendas: " + e.getMessage());
            }

            // Se não houver dados reais, usar dados de exemplo
//            if (!dadosReaisDisponiveis) {
//                System.out.println("⚠️ Usando dados de exemplo para gráfico de vendas");
//                dataset.addValue(120000, "Fatura (FT)", "Jan");
//                dataset.addValue(80000, "Recibo (FR)", "Jan");
//                dataset.addValue(150000, "Fatura (FT)", "Fev");
//                dataset.addValue(90000, "Recibo (FR)", "Fev");
//                dataset.addValue(180000, "Fatura (FT)", "Mar");
//                dataset.addValue(100000, "Recibo (FR)", "Mar");
//                dataset.addValue(160000, "Fatura (FT)", "Abr");
//                dataset.addValue(80000, "Recibo (FR)", "Abr");
//                dataset.addValue(200000, "Fatura (FT)", "Mai");
//                dataset.addValue(120000, "Recibo (FR)", "Mai");
//                dataset.addValue(220000, "Fatura (FT)", "Jun");
//                dataset.addValue(140000, "Recibo (FR)", "Jun");
//            }
            JFreeChart chart = ChartFactory.createBarChart(
                    "Vendas Mensais por Tipo de Documento",
                    "Mês",
                    "Valor (AOA)",
                    dataset
            );
            chart.setBackgroundPaint(java.awt.Color.WHITE);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(400, 300));
            panelGraficos.add(criarPainelGrafico(chartPanel, "💰 Vendas FT/FR"));

        } catch (Exception e) {
            System.err.println("❌ Erro ao criar gráfico de vendas: " + e.getMessage());
            panelGraficos.add(criarPainelErro("Erro em Vendas"));
        }
    }

    /**
     * Gráfico 2: Percentagem de Métodos de Pagamento
     */
    private void adicionarGraficoPagamentosPercentagem() {
        try {
            DefaultPieDataset dataset = new DefaultPieDataset();

//            boolean dadosReaisDisponiveis = false;
            // Tentar obter dados reais do PaymentController
            try {
                LocalDate hoje = LocalDate.now();
                LocalDate inicioMes = hoje.withDayOfMonth(1);

                Map<PaymentMode, Double> estatisticas = paymentController.getEstatisticasPorModo(inicioMes, hoje);

                if (estatisticas != null && !estatisticas.isEmpty()) {
                    double total = estatisticas.values().stream().mapToDouble(Double::doubleValue).sum();

                    for (Map.Entry<PaymentMode, Double> entry : estatisticas.entrySet()) {
                        if (entry.getValue() > 0) {
                            double percentagem = (entry.getValue() / total) * 100;
                            dataset.setValue(entry.getKey().getDescricao() + " (" + String.format("%.1f", percentagem) + "%)",
                                    entry.getValue());
//                            dadosReaisDisponiveis = true;
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("❌ Erro ao obter dados reais de pagamentos: " + e.getMessage());
            }

            // Se não houver dados reais, usar dados de exemplo com percentagens
//            if (!dadosReaisDisponiveis) {
//                dataset.setValue("Numerário (45.0%)", 45000.0);
//                dataset.setValue("Multicaixa (25.0%)", 25000.0);
//                dataset.setValue("Cartão Crédito (15.0%)", 15000.0);
//                dataset.setValue("Transferência (10.0%)", 10000.0);
//                dataset.setValue("Outros (5.0%)", 5000.0);
//            }
            JFreeChart chart = ChartFactory.createPieChart(
                    "Métodos de Pagamento - Percentagem",
                    dataset,
                    true, true, false
            );
            chart.setBackgroundPaint(java.awt.Color.WHITE);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(400, 300));
            panelGraficos.add(criarPainelGrafico(chartPanel, "💳 Pagamentos (%)"));

        } catch (Exception e) {
            System.err.println("❌ Erro ao criar gráfico de pagamentos: " + e.getMessage());
            panelGraficos.add(criarPainelErro("Erro em Pagamentos"));
        }
    }

    /**
     * Gráfico 3: Total de Faturas FT vs FR
     */
    private void adicionarGraficoTotalFaturasFTFR() {
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

//            boolean dadosReaisDisponiveis = false;
            // Tentar obter dados reais
            try {
                LocalDate hoje = LocalDate.now();
                LocalDate inicioAno = LocalDate.of(hoje.getYear(), 1, 1);

                // Simular contagem de documentos
                long totalFT = contarDocumentosPorTipo("FT", inicioAno, hoje);
                long totalFR = contarDocumentosPorTipo("FR", inicioAno, hoje);
                long totalGeral = totalFT + totalFR;

                dataset.addValue(totalFT, "Documentos", "Faturas (FT)");
                dataset.addValue(totalFR, "Documentos", "Recibos (FR)");
                dataset.addValue(totalGeral, "Documentos", "Total Geral");

//                if (totalFT > 0 || totalFR > 0) {
//                    dadosReaisDisponiveis = true;
//                }
            } catch (Exception e) {
                System.err.println("❌ Erro ao obter dados reais de faturas: " + e.getMessage());
            }

            // Se não houver dados reais, usar dados de exemplo
//            if (!dadosReaisDisponiveis) {
//                dataset.addValue(45, "Documentos", "Faturas (FT)");
//                dataset.addValue(78, "Documentos", "Recibos (FR)");
//                dataset.addValue(123, "Documentos", "Total Geral");
//            }
            JFreeChart chart = ChartFactory.createBarChart(
                    "Total de Documentos - FT vs FR",
                    "Tipo de Documento",
                    "Quantidade",
                    dataset
            );
            chart.setBackgroundPaint(java.awt.Color.WHITE);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(400, 300));
            panelGraficos.add(criarPainelGrafico(chartPanel, "📊 Documentos FT/FR"));

        } catch (Exception e) {
            System.err.println("❌ Erro ao criar gráfico de documentos: " + e.getMessage());
            panelGraficos.add(criarPainelErro("Erro em Documentos"));
        }
    }

    /**
     * Gráfico 4: Estatísticas de Clientes
     */
    private void adicionarGraficoClientes() {
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

//            boolean dadosReaisDisponiveis = false;
            // Tentar obter dados reais do ClientController
            try {
                var clientes = clientController.getAll();

                if (clientes != null && !clientes.isEmpty()) {
                    long ativos = clientes.stream().filter(c -> c.getStatus() == 1).count();
                    long inativos = clientes.size() - ativos;

                    dataset.addValue(ativos, "Clientes", "Ativos");
                    dataset.addValue(inativos, "Clientes", "Inativos");
                    dataset.addValue(clientes.size(), "Clientes", "Total");
//                    dadosReaisDisponiveis = true;
                }
            } catch (Exception e) {
                System.err.println("❌ Erro ao obter dados reais de clientes: " + e.getMessage());
            }

            // Se não houver dados reais, usar dados de exemplo
//            if (!dadosReaisDisponiveis) {
//                dataset.addValue(150, "Clientes", "Ativos");
//                dataset.addValue(25, "Clientes", "Inativos");
//                dataset.addValue(175, "Clientes", "Total");
//            }
            JFreeChart chart = ChartFactory.createBarChart(
                    "Estatísticas de Clientes",
                    "Status",
                    "Quantidade",
                    dataset
            );
            chart.setBackgroundPaint(java.awt.Color.WHITE);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(400, 300));
            panelGraficos.add(criarPainelGrafico(chartPanel, "👥 Clientes"));

        } catch (Exception e) {
            System.err.println("❌ Erro ao criar gráfico de clientes: " + e.getMessage());
            panelGraficos.add(criarPainelErro("Erro em Clientes"));
        }
    }

    /**
     * Gráfico 5: Situação de Stock
     */
    private void adicionarGraficoStock() {
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            // Obter produtos com stock mínimo (mais críticos)
            List<Product> produtosComStockMinimo = productController.listarProdutosComStockMinimo();

            // Se não houver produtos críticos, pegar alguns produtos ativos
            if (produtosComStockMinimo.isEmpty()) {
                List<Product> produtosAtivos = productController.findActive();
                produtosComStockMinimo = produtosAtivos.stream()
                        .limit(5)
                        .collect(Collectors.toList());
            } else {
                // Limitar aos 5 mais críticos
                produtosComStockMinimo = produtosComStockMinimo.stream()
                        .limit(5)
                        .collect(Collectors.toList());
            }

            // Adicionar dados reais ao dataset com tratamento de null
            for (Product produto : produtosComStockMinimo) {
                // Usar StockMovementController para obter stock atual de forma confiável
                Integer stockAtual = stockController.getStockAtual(produto.getId());

                // Se ainda for null, usar 0 como fallback
                if (stockAtual == null) {
                    stockAtual = 0;
                }

                // Obter stock mínimo com fallback
                Integer stockMinimo = produto.getMinStock();
                if (stockMinimo == null) {
                    stockMinimo = 0;
                }

                dataset.addValue(
                        stockAtual.doubleValue(),
                        "Stock",
                        produto.getDescription()
                );
            }

            JFreeChart chart = ChartFactory.createBarChart(
                    "Top 5 - Stock em Armazém (Situação Crítica)",
                    "Produto",
                    "Quantidade",
                    dataset
            );

            // Personalizar cores baseado no nível de stock
            CategoryPlot plot = chart.getCategoryPlot();
            BarRenderer renderer = (BarRenderer) plot.getRenderer();

            for (int i = 0; i < produtosComStockMinimo.size(); i++) {
                Product produto = produtosComStockMinimo.get(i);

                // Obter valores com tratamento de null
                Integer stockAtual = stockController.getStockAtual(produto.getId());
                Integer stockMinimo = produto.getMinStock();

                double stock = stockAtual != null ? stockAtual.doubleValue() : 0.0;
                double minStock = stockMinimo != null ? stockMinimo.doubleValue() : 0.0;

                // Definir cores baseadas no nível de stock
                if (minStock > 0) {
                    // Vermelho para stock abaixo do mínimo, amarelo para próximo do mínimo, verde para ok
                    if (stock < minStock) {
                        renderer.setSeriesPaint(i, Color.RED);
                    } else if (stock <= minStock * 1.5) {
                        renderer.setSeriesPaint(i, Color.ORANGE);
                    } else {
                        renderer.setSeriesPaint(i, Color.GREEN);
                    }
                } else {
                    // Se não há stock mínimo definido, usar azul
                    renderer.setSeriesPaint(i, Color.BLUE);
                }
            }

            chart.setBackgroundPaint(java.awt.Color.WHITE);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(400, 300));
            panelGraficos.add(criarPainelGrafico(chartPanel, "📦 Stock"));

        } catch (Exception e) {
            System.err.println("❌ Erro ao criar gráfico de stock: " + e.getMessage());
            e.printStackTrace(); // Para debug
            panelGraficos.add(criarPainelErro("Erro em Stock"));
        }
    }

    /**
     * Gráfico 6: Vendas Mensais Consolidado
     */
    private void adicionarGraficoVendasMensais() {
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

//            OrderController orderController = new OrderController();
//            InvoiceController invoiceController = new InvoiceController();
            // Obter data atual e calcular últimos 6 meses
            LocalDate hoje = LocalDate.now();
            String[] meses = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun"};

            for (int i = 5; i >= 0; i--) {
                LocalDate mesAtual = hoje.minusMonths(i);
                LocalDate inicioMes = mesAtual.withDayOfMonth(1);
                LocalDate fimMes = mesAtual.withDayOfMonth(mesAtual.lengthOfMonth());

                // Calcular vendas do mês (Orders + Invoices)
                BigDecimal totalVendasMes = BigDecimal.ZERO;

                // Vendas de Orders
                List<Order> ordersMes = orderController.filterByDate(inicioMes, fimMes);
                for (Order order : ordersMes) {
                    if (order.getTotal() != null) {
                        totalVendasMes = totalVendasMes.add(order.getTotal());
                    }
                }

                // Vendas de Invoices
                List<Invoices> invoicesMes = invoiceController.listarPorPeriodo(inicioMes, fimMes);
                for (Invoices invoice : invoicesMes) {
                    if (invoice.getTotal() != null && "EMITIDA".equals(invoice.getStatus())) {
                        totalVendasMes = totalVendasMes.add(invoice.getTotal());
                    }
                }

                dataset.addValue(
                        totalVendasMes.doubleValue(),
                        "Vendas",
                        meses[5 - i] // Ajustar índice para os últimos 6 meses
                );
            }

            JFreeChart chart = ChartFactory.createLineChart(
                    "Evolução de Vendas Mensais - Últimos 6 Meses",
                    "Mês",
                    "Valor (AOA)",
                    dataset
            );

            // Personalizar a linha do gráfico
            CategoryPlot plot = chart.getCategoryPlot();
            LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, new Color(0, 100, 0)); // Verde escuro
            renderer.setSeriesStroke(0, new BasicStroke(2.5f));
            renderer.setSeriesShapesVisible(0, true);

            chart.setBackgroundPaint(java.awt.Color.WHITE);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(400, 300));
            panelGraficos.add(criarPainelGrafico(chartPanel, "📈 Vendas Mensais"));

        } catch (Exception e) {
            System.err.println("❌ Erro ao criar gráfico de vendas mensais: " + e.getMessage());
            panelGraficos.add(criarPainelErro("Erro em Vendas Mensais"));
        }
    }

    /**
     * Gráfico 7: Top Produtos Mais Vendidos
     */
    private void adicionarGraficoTopProdutos() {
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            ProductOrderController productOrderController = new ProductOrderController();
            OrderController orderController = new OrderController();
            InvoiceController invoiceController = new InvoiceController();

            // Obter últimos 30 dias de vendas
            LocalDate dataFim = LocalDate.now();
            LocalDate dataInicio = dataFim.minusDays(30);

            // Mapa para agrupar vendas por produto
            Map<String, Integer> vendasPorProduto = new HashMap<>();

            // 🔹 PROCESSAR VENDAS DE ORDERS (PDV)
            List<Order> ordersPeriodo = orderController.filterByDate(dataInicio, dataFim);
            System.out.println("📊 Orders encontrados: " + ordersPeriodo.size());

            for (Order order : ordersPeriodo) {
                // Verificar se o order tem status pago ou é válido
                if (order.getStatus() != null && order.getStatus().equals(PaymentStatus.PAGO)) {
                    List<ProductSales> itensVenda = productOrderController.getByOrderId(order.getId());

                    for (ProductSales item : itensVenda) {
                        if (item != null && item.getQty() > 0) {
                            String nomeProduto = item.getDescription();
                            if (nomeProduto == null || nomeProduto.trim().isEmpty()) {
                                nomeProduto = "Produto Sem Nome";
                            }
                            int quantidade = item.getQty();
                            vendasPorProduto.merge(nomeProduto, quantidade, Integer::sum);
                        }
                    }
                }
            }

            // 🔹 PROCESSAR VENDAS DE INVOICES (FATURAS)
            List<Invoices> invoicesPeriodo = invoiceController.listarPorPeriodo(dataInicio, dataFim);
            System.out.println("📊 Invoices encontrados: " + invoicesPeriodo.size());

            for (Invoices invoice : invoicesPeriodo) {
                // Considerar apenas faturas emitidas ou pagas
                if (invoice != null || invoice.isPaga()) {
                    // Obter produtos da fatura (assumindo que há um método para isso)
                    // Se não houver, você precisará implementar ProductInvoiceController similar ao ProductOrderController
                    try {
                        // Tentar obter produtos da fatura - ajuste conforme sua implementação
                        if (invoice.getProducts() != null) {
                            for (ProductSales item : invoice.getProducts()) {
                                if (item != null && item.getQty() > 0) {
                                    String nomeProduto = item.getDescription();
                                    if (nomeProduto == null || nomeProduto.trim().isEmpty()) {
                                        nomeProduto = "Produto Sem Nome";
                                    }
                                    int quantidade = item.getQty();
                                    vendasPorProduto.merge(nomeProduto, quantidade, Integer::sum);
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("⚠️ Erro ao processar produtos da fatura: " + e.getMessage());
                    }
                }
            }

            System.out.println("📊 Total de produtos distintos vendidos: " + vendasPorProduto.size());

            // Ordenar por quantidade vendida (decrescente) e pegar top 5
            List<Map.Entry<String, Integer>> topProdutos = vendasPorProduto.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(5)
                    .collect(Collectors.toList());

            // Se não houver dados, usar dados de exemplo com aviso
            if (topProdutos.isEmpty()) {
                System.out.println("⚠️ Nenhum dado de venda encontrado, usando dados de exemplo");

                // Dados de exemplo educativos
                dataset.addValue(45, "Vendas", "Caderno Universitário");
                dataset.addValue(38, "Vendas", "Caneta Esferográfica");
                dataset.addValue(29, "Vendas", "Borracha Branca");
                dataset.addValue(21, "Vendas", "Lápis HB");
                dataset.addValue(18, "Vendas", "Régua 30cm");
            } else {
                // Adicionar dados reais ao dataset
                for (Map.Entry<String, Integer> entry : topProdutos) {
                    dataset.addValue(entry.getValue(), "Vendas",
                            entry.getKey().length() > 20
                            ? entry.getKey().substring(0, 20) + "..."
                            : entry.getKey()
                    );
                }
            }

            JFreeChart chart = ChartFactory.createBarChart(
                    "Top 5 - Produtos Mais Vendidos (Últimos 30 Dias)",
                    "Produto",
                    "Quantidade Vendida",
                    dataset
            );

            // Personalizar cores e aparência
            CategoryPlot plot = chart.getCategoryPlot();
            BarRenderer renderer = (BarRenderer) plot.getRenderer();

            // Gradiente de cores para o ranking
            Color[] coresRanking = {
                new Color(220, 20, 60), // Vermelho - 1º lugar
                new Color(255, 140, 0), // Laranja - 2º lugar
                new Color(255, 215, 0), // Dourado - 3º lugar
                new Color(65, 105, 225), // Azul real - 4º lugar
                new Color(46, 139, 87) // Verde - 5º lugar
            };

            for (int i = 0; i < Math.min(topProdutos.size(), 5); i++) {
                renderer.setSeriesPaint(i, coresRanking[i]);
            }

            // Melhorar a aparência do gráfico
            plot.setBackgroundPaint(Color.WHITE);
            plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
            plot.setOutlinePaint(Color.GRAY);

            // Configurar as barras
            renderer.setItemMargin(0.1);
            renderer.setMaximumBarWidth(0.1);

            // Rotacionar labels do eixo X para melhor legibilidade
            CategoryAxis domainAxis = plot.getDomainAxis();
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

            chart.setBackgroundPaint(java.awt.Color.WHITE);

            // Adicionar subtítulo se usando dados de exemplo
            if (topProdutos.isEmpty()) {
                chart.addSubtitle(new TextTitle("Dados de exemplo - Sem vendas reais no período"));
            }

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(450, 350)); // Ligeiramente maior para melhor visualização
            chartPanel.setMouseWheelEnabled(true);

            panelGraficos.add(criarPainelGrafico(chartPanel, "🏆 Top Produtos"));

        } catch (Exception e) {
            System.err.println("❌ Erro ao criar gráfico de top produtos: " + e.getMessage());
            e.printStackTrace(); // Para debug
            panelGraficos.add(criarPainelErro("Erro em Top Produtos"));
        }
    }

    /**
     * Gráfico 8: Estatísticas de Pagamentos
     */
    private void adicionarGraficoEstatisticasPagamentos() {
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            // Dados de exemplo para estatísticas de pagamentos
            dataset.addValue(45, "Pagamentos", "Pagos");
            dataset.addValue(12, "Pagamentos", "Pendentes");
            dataset.addValue(3, "Pagamentos", "Atrasados");
            dataset.addValue(60, "Pagamentos", "Total");

            JFreeChart chart = ChartFactory.createBarChart(
                    "Estatísticas de Pagamentos",
                    "Status",
                    "Quantidade",
                    dataset
            );
            chart.setBackgroundPaint(java.awt.Color.WHITE);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(400, 300));
            panelGraficos.add(criarPainelGrafico(chartPanel, "💵 Estatísticas Pagos"));

        } catch (Exception e) {
            System.err.println("❌ Erro ao criar gráfico de estatísticas: " + e.getMessage());
            panelGraficos.add(criarPainelErro("Erro em Estatísticas"));
        }
    }

    /**
     * Método auxiliar para calcular vendas por tipo - ATUALIZADO
     */
    private BigDecimal calcularVendasPorTipo(String tipoDocumento, LocalDate inicio, LocalDate fim) {
        try {
            if (DocumentType.FT.getPrefix().equals(tipoDocumento)) {
                // Para Faturas (FT) - Faturas
                return calcularVendasFT(inicio, fim);

            } else if (DocumentType.FR.getPrefix().equals(tipoDocumento)) {
                // Para Recibos (FR) - Orders
                return calcularVendasFR(inicio, fim);
            }
            return BigDecimal.ZERO;
        } catch (Exception e) {
            System.err.println("❌ Erro ao calcular vendas para " + tipoDocumento + ": " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    /**
     * Método simplificado para calcular vendas FT (Faturas)
     */
    private BigDecimal calcularVendasFT(LocalDate inicio, LocalDate fim) {
        try {
            // 🔹 Fallback: cálculo manual
            List<Invoices> faturas = invoiceController.listarPorPeriodo(inicio, fim);
            BigDecimal total = BigDecimal.ZERO;
            if (faturas != null) {
                for (Invoices fatura : faturas) {
                    // Incluir apenas faturas pagas (Status 3)
                    if (fatura.getTotal() != null
                            && fatura.getStatus() != null
                            && fatura.getStatus() == PaymentStatus.PAGO) {
                        total = total.add(fatura.getTotal());
                    }
                }
            }
            return total;

        } catch (Exception e) {
            System.err.println("❌ Erro ao calcular vendas FT: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    /**
     * Método alternativo para calcular vendas FR (Orders)
     */
    private BigDecimal calcularVendasFR(LocalDate inicio, LocalDate fim) {
        try {
            // Se o OrderController não tiver o método calcularEstatisticas, usar este:
            List<Order> orders = orderController.filterByDate(inicio, fim);
            BigDecimal total = BigDecimal.ZERO;

            for (Order order : orders) {
                if (order.getTotal() != null && order.getStatus() == PaymentStatus.PAGO) {
                    total = total.add(order.getTotal());
                }
            }
            return total;

        } catch (Exception e) {
            System.err.println("❌ Erro ao calcular vendas FR: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    /**
     * Método auxiliar para contar documentos por tipo
     */
    private long contarDocumentosPorTipo(String tipoDocumento, LocalDate inicio, LocalDate fim) {
        try {
            // Substitua por sua lógica real de busca no banco
            return (long) (Math.random() * 50 + 10); // Entre 10 e 60
        } catch (Exception e) {
            System.err.println("❌ Erro ao contar documentos para " + tipoDocumento + ": " + e.getMessage());
            return 0L;
        }
    }

    /**
     * Cria um painel organizado para cada gráfico
     */
    private JPanel criarPainelGrafico(ChartPanel chartPanel, String titulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(java.awt.Color.WHITE);
        panel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(titulo),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.add(chartPanel, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Cria painel de erro quando gráfico falha
     */
    private JPanel criarPainelErro(String titulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(java.awt.Color.WHITE);
        panel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(titulo),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        javax.swing.JLabel lblErro = new javax.swing.JLabel("Dados não disponíveis");
        lblErro.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblErro.setForeground(java.awt.Color.RED);
        panel.add(lblErro, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Retorna nome do mês em português
     */
    private String getNomeMes(int mes) {
        String[] meses = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
            "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        return meses[mes - 1];
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setMinimumSize(new java.awt.Dimension(900, 600));
        setPreferredSize(new java.awt.Dimension(900, 600));
        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                formAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 900, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_formAncestorAdded
        // TODO add your handling code here:

    }//GEN-LAST:event_formAncestorAdded


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
