package com.okutonda.okudpdv.helpers;

import com.okutonda.okudpdv.data.entities.Order;
import com.okutonda.okudpdv.data.entities.ProductSales;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.*;
import java.awt.*;

/**
 * Helper especializado para impressão térmica de pedidos (orders)
 */
public class OrderPrintHelper {

    private static final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00 AOA");
    private static final String EMPRESA_NOME = "OKU DPDV LDA";
    private static final String EMPRESA_NIF = "541234567";
    private static final String EMPRESA_ENDERECO = "Luanda, Angola";
    private static final String EMPRESA_CONTACTO = "+244 923 456 789";


    /**
     * Mostra preview do pedido em formato térmico usando Swing
     */
    public static void showOrderPreview(Order order) {
        JDialog previewDialog = new JDialog();
        previewDialog.setTitle("Visualizar Pedido - " + formatOrderNumber(order));
        previewDialog.setModal(true);
        previewDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        previewDialog.setSize(320, 600);
        previewDialog.setLocationRelativeTo(null);
        previewDialog.setResizable(false);

        // Painel principal com estilo térmico
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Fonte monoespaçada para estilo térmico
        Font fontNormal = new Font("Monospaced", Font.PLAIN, 11);
        Font fontBold = new Font("Monospaced", Font.BOLD, 11);
        Font fontTitle = new Font("Monospaced", Font.BOLD, 12);
        Font fontSmall = new Font("Monospaced", Font.PLAIN, 10);

        // Cabeçalho
        mainPanel.add(createCenteredLabel(EMPRESA_NOME, fontTitle));
        mainPanel.add(createCenteredLabel("NIF: " + EMPRESA_NIF, fontNormal));
        mainPanel.add(createCenteredLabel(EMPRESA_ENDERECO, fontNormal));
        mainPanel.add(createCenteredLabel("Tel: " + EMPRESA_CONTACTO, fontNormal));
        
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(createSeparator());
        mainPanel.add(createCenteredLabel("PEDIDO/RECIBO", fontTitle));
        mainPanel.add(createSeparator());

        // Informações do Pedido
        mainPanel.add(createSectionTitle("INFORMAÇÕES DO PEDIDO", fontBold));
        mainPanel.add(createInfoLine("PEDIDO: " + formatOrderNumber(order), fontNormal));
        mainPanel.add(createInfoLine("Data: " + order.getDatecreate(), fontNormal));
        mainPanel.add(createInfoLine("Status: " + getStatusDescricao(order.getStatus()), fontNormal));
        if (order.getSeller() != null) {
            mainPanel.add(createInfoLine("Vendedor: " + order.getSeller().getName(), fontNormal));
        }

        mainPanel.add(createSeparator());

        // Cliente
        mainPanel.add(createSectionTitle("CLIENTE", fontBold));
        String clienteNome = order.getClient() != null ? order.getClient().getName() : "CONSUMIDOR FINAL";
        mainPanel.add(createInfoLine(clienteNome, fontNormal));
        if (order.getClient() != null && order.getClient().getNif() != null) {
            mainPanel.add(createInfoLine("NIF: " + order.getClient().getNif(), fontNormal));
        }

        mainPanel.add(createSeparator());

        // Produtos
        mainPanel.add(createSectionTitle("PRODUTOS/SERVIÇOS", fontBold));
        
        // Cabeçalho da tabela de produtos
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setMaximumSize(new Dimension(280, 20));
        
        JLabel descLabel = new JLabel("Descrição");
        descLabel.setFont(fontBold);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel valoresLabel = new JLabel("Qtd  Preço   Total");
        valoresLabel.setFont(fontBold);
        valoresLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        headerPanel.add(descLabel, BorderLayout.WEST);
        headerPanel.add(valoresLabel, BorderLayout.EAST);
        mainPanel.add(headerPanel);
        
        mainPanel.add(createSeparator());
        System.out.println("order.getProducts():" + order.getProducts());
        // Itens dos produtos
        if (order.getProducts() != null && !order.getProducts().isEmpty()) {
            for (ProductSales ps : order.getProducts()) {
                String descricao = ps.getDescription() != null ? ps.getDescription() : "Produto";
                if (descricao.length() > 25) {
                    descricao = descricao.substring(0, 22) + "...";
                }
                
                JPanel produtoPanel = new JPanel(new BorderLayout());
                produtoPanel.setBackground(Color.WHITE);
                produtoPanel.setMaximumSize(new Dimension(280, 20));
                
                JLabel descProduto = new JLabel(descricao);
                descProduto.setFont(fontNormal);
                
                String valores = String.format("%-3d %-6s %-6s", 
                    ps.getQty(),
                    formatCurrencyCompact(ps.getPrice()),
                    formatCurrencyCompact(ps.getTotal()));
                
                JLabel valoresProduto = new JLabel(valores);
                valoresProduto.setFont(fontNormal);
                
                produtoPanel.add(descProduto, BorderLayout.WEST);
                produtoPanel.add(valoresProduto, BorderLayout.EAST);
                mainPanel.add(produtoPanel);
            }
        } else {
            mainPanel.add(createInfoLine("Nenhum item", fontNormal));
        }

        mainPanel.add(createSeparator());

        // Totais
        mainPanel.add(createSectionTitle("RESUMO FINANCEIRO", fontBold));
        mainPanel.add(createTotalLine("Subtotal:", order.getSubTotal(), fontNormal));
        mainPanel.add(createTotalLine("IVA:", order.getTotalTaxe(), fontNormal));
        mainPanel.add(createTotalLine("TOTAL:", order.getTotal(), fontBold));

        mainPanel.add(createSeparator());

        // Pagamento
        mainPanel.add(createSectionTitle("INFORMAÇÕES PAGAMENTO", fontBold));
        mainPanel.add(createTotalLine("Total Pago:", order.getPayTotal(), fontNormal));
        
        BigDecimal saldo = order.getTotal().subtract(order.getPayTotal() != null ? order.getPayTotal() : BigDecimal.ZERO);
        mainPanel.add(createTotalLine("Saldo:", saldo, fontNormal));
        mainPanel.add(createInfoLine("Status: " + getPaymentStatus(order), fontNormal));

        mainPanel.add(createSeparator());

        // Rodapé
        mainPanel.add(createCenteredLabel("** DOCUMENTO COMERCIAL **", fontSmall));
        mainPanel.add(createCenteredLabel("Obrigado pela preferência!", fontSmall));
        
        if (order.getHash() != null) {
            mainPanel.add(createCenteredLabel("H: " + abbreviateHash(order.getHash()), fontSmall));
        }
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));
        mainPanel.add(createCenteredLabel(timestamp, fontSmall));

        // Botões
        JPanel buttonPanel = new JPanel();
        JButton printButton = new JButton("Imprimir Térmica");
        printButton.addActionListener(e -> {
            printThermalOrder(order, previewDialog);
            previewDialog.dispose();
        });

        JButton closeButton = new JButton("Fechar");
        closeButton.addActionListener(e -> previewDialog.dispose());

        buttonPanel.add(printButton);
        buttonPanel.add(closeButton);

        // Scroll pane para o conteúdo
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        previewDialog.setLayout(new BorderLayout());
        previewDialog.add(scrollPane, BorderLayout.CENTER);
        previewDialog.add(buttonPanel, BorderLayout.SOUTH);

        previewDialog.setVisible(true);
    }

    // ==========================================================
    // 🔹 MÉTODOS AUXILIARES PARA CRIAÇÃO DE COMPONENTES
    // ==========================================================

    private static JLabel createCenteredLabel(String text, Font font) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(font);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setMaximumSize(new Dimension(280, 20));
        return label;
    }

    private static JLabel createSectionTitle(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setMaximumSize(new Dimension(280, 20));
        return label;
    }

    private static JLabel createInfoLine(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setMaximumSize(new Dimension(280, 20));
        return label;
    }

    private static JPanel createTotalLine(String label, BigDecimal value, Font font) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(280, 20));
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(font);
        
        JLabel valueComp = new JLabel(formatCurrencyCompact(value));
        valueComp.setFont(font);
        
        panel.add(labelComp, BorderLayout.WEST);
        panel.add(valueComp, BorderLayout.EAST);
        
        return panel;
    }

    private static JSeparator createSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(280, 5));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        return separator;
    }

    /**
     * Imprime pedido em impressora térmica (80mm) - SEM diálogo
     */
    public static boolean printThermalOrder(Order order, java.awt.Component parentComponent) {
        if (order == null) {
            JOptionPane.showMessageDialog(parentComponent,
                    "Selecione um pedido para imprimir", "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            OrderThermalPrintable printable = new OrderThermalPrintable(order);

            PageFormat pageFormat = job.defaultPage();
            pageFormat.setPaper(createThermalPaper());
            job.setPrintable(printable, pageFormat);

            PrintService printer = selectThermalPrinter();
            if (printer != null) {
                job.setPrintService(printer);
                
                // Confirmação rápida
                int confirm = JOptionPane.showConfirmDialog(parentComponent,
                        "Imprimir pedido em impressora térmica?\n" +
                        "Nº: " + formatOrderNumber(order) + "\n" +
                        "Cliente: " + (order.getClient() != null ? order.getClient().getName() : "Consumidor Final"),
                        "Confirmação de Impressão",
                        JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    job.print();
                    System.out.println("✅ Pedido impresso em térmica: " + formatOrderNumber(order));
                    return true;
                }
            } else {
                JOptionPane.showMessageDialog(parentComponent,
                        "Nenhuma impressora térmica encontrada!",
                        "Impressora Não Encontrada", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            handlePrintError(e, "térmica", parentComponent);
        }
        return false;
    }

    /**
     * Imprime pedido com diálogo de seleção de impressora
     */
    public static boolean printOrderWithDialog(Order order, java.awt.Component parentComponent) {
        if (order == null) {
            JOptionPane.showMessageDialog(parentComponent,
                    "Selecione um pedido para imprimir", "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            OrderStandardPrintable printable = new OrderStandardPrintable(order);
            job.setPrintable(printable);

            if (job.printDialog()) {
                job.print();
                JOptionPane.showMessageDialog(parentComponent,
                        "Pedido enviado para impressão com sucesso!\n" +
                        "Nº: " + formatOrderNumber(order),
                        "Impressão Concluída", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        } catch (Exception e) {
            handlePrintError(e, "padrão", parentComponent);
        }
        return false;
    }

    // ==========================================================
    // 🔹 MÉTODOS AUXILIARES
    // ==========================================================

    private static Paper createThermalPaper() {
        Paper paper = new Paper();
        double width = 80 * 72 / 25.4; // 80mm para pontos
        double height = 297 * 72 / 25.4; // Altura A4
        paper.setSize(width, height);
        paper.setImageableArea(5, 5, width - 10, height - 10);
        return paper;
    }

    private static PrintService selectThermalPrinter() {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        String[] thermalKeywords = {"thermal", "termica", "ticket", "cupom", "80mm", "58mm", "POS"};
        
        for (PrintService service : services) {
            String name = service.getName().toLowerCase();
            for (String keyword : thermalKeywords) {
                if (name.contains(keyword.toLowerCase())) {
                    return service;
                }
            }
        }
        return services.length > 0 ? services[0] : null;
    }

    public static String formatOrderNumber(Order order) {
        if (order == null) return "N/A";
        return order.getPrefix() + "/" + order.getNumber();
    }

    private static String formatCurrencyCompact(BigDecimal value) {
        if (value == null) return "0,00";
        String formatted = currencyFormat.format(value);
        return formatted.replace(" AOA", "");
    }

    private static String getStatusDescricao(com.okutonda.okudpdv.data.entities.PaymentStatus status) {
        if (status == null) return "DESCONHECIDO";
        switch (status) {
            case PENDENTE: return "PENDENTE";
            case PAGO: return "PAGO";
            case PARCIAL: return "PARCIAL";
            case ATRASADO: return "ATRASADO";
            case CANCELADO: return "CANCELADO";
            default: return "DESCONHECIDO";
        }
    }

    private static String getPaymentStatus(Order order) {
        BigDecimal saldo = order.getTotal().subtract(order.getPayTotal() != null ? order.getPayTotal() : BigDecimal.ZERO);
        if (saldo.compareTo(BigDecimal.ZERO) == 0) return "PAGO";
        if (saldo.compareTo(order.getTotal()) == 0) return "PENDENTE";
        return "PARCIAL";
    }

    private static String abbreviateHash(String hash) {
        if (hash == null || hash.length() <= 12) return hash;
        return hash.substring(0, 6) + "..." + hash.substring(hash.length() - 6);
    }

    private static void handlePrintError(Exception e, String printType, java.awt.Component parent) {
        System.err.println("❌ Erro na impressão " + printType + ": " + e.getMessage());
        JOptionPane.showMessageDialog(parent,
                "Erro na impressão " + printType + ":\n" + e.getMessage(),
                "Erro de Impressão", JOptionPane.ERROR_MESSAGE);
    }
}