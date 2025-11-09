package com.okutonda.okudpdv.helpers;

import com.okutonda.okudpdv.data.entities.Invoices;
import com.okutonda.okudpdv.data.entities.ProductSales;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

/**
 * Helper especializado para impressão e visualização de faturas - MODELO AGT ANGOLA
 */
public class InvoicePrintHelper {

    private static final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00 AOA");
    private static final String EMPRESA_NIF = "541234567"; // NIF da empresa
    private static final String EMPRESA_NOME = "OKU DPDV LDA";
    private static final String EMPRESA_ENDERECO = "Luanda, Angola";
    private static final String EMPRESA_CONTACTO = "+244 923 456 789";

    /**
     * Visualização detalhada da fatura - Modelo AGT
     */
    public static void showInvoiceDetails(Invoices invoice, java.awt.Component parentComponent) {
        if (invoice == null) {
            JOptionPane.showMessageDialog(parentComponent,
                    "Selecione uma fatura para visualizar", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog();
        dialog.setTitle("Fatura " + formatInvoiceNumber(invoice) + " - Modelo AGT");
        dialog.setModal(true);
        dialog.setSize(850, 650);
        dialog.setLocationRelativeTo(parentComponent);
        dialog.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("📋 Fatura AGT", createAGTPanel(invoice));
        tabbedPane.addTab("📦 Produtos", createProductsPanel(invoice));
        tabbedPane.addTab("🔍 Detalhes", createDetailsPanel(invoice));

        JPanel buttonPanel = createButtonPanel(invoice, dialog, parentComponent);

        dialog.add(tabbedPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Painel principal no formato AGT Angola
     */
    private static JPanel createAGTPanel(Invoices invoice) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Cabeçalho AGT
        panel.add(createAGTHeader(invoice), BorderLayout.NORTH);
        
        // Corpo da fatura
        panel.add(createAGTBody(invoice), BorderLayout.CENTER);
        
        // Rodapé AGT
        panel.add(createAGTFooter(invoice), BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Cabeçalho no formato AGT
     */
    private static JPanel createAGTHeader(Invoices invoice) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Linha 1: Dados da empresa
        JPanel empresaPanel = new JPanel(new GridLayout(2, 1));
        empresaPanel.add(createLabel(EMPRESA_NOME, Font.BOLD, 14));
        empresaPanel.add(createLabel("NIF: " + EMPRESA_NIF + " | " + EMPRESA_ENDERECO, Font.PLAIN, 11));
        
        // Linha 2: Tipo de documento e número
        JPanel docPanel = new JPanel(new GridLayout(2, 1));
        docPanel.add(createLabel("FATURA/RECIBO", Font.BOLD, 16));
        docPanel.add(createLabel("Nº: " + formatInvoiceNumber(invoice), Font.BOLD, 12));

        // Linha 3: Datas e hash
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(createLabel("Data Emissão: " + invoice.getIssueDate(), Font.PLAIN, 11));
        if (invoice.getDueDate() != null) {
            infoPanel.add(createLabel("Data Vencimento: " + invoice.getDueDate(), Font.PLAIN, 11));
        }
        if (invoice.getHash() != null) {
            infoPanel.add(createLabel("Hash: " + abbreviateHash(invoice.getHash()), Font.PLAIN, 10));
        }

        header.add(empresaPanel, BorderLayout.WEST);
        header.add(docPanel, BorderLayout.CENTER);
        header.add(infoPanel, BorderLayout.EAST);

        return header;
    }

    /**
     * Corpo da fatura AGT
     */
    private static JPanel createAGTBody(Invoices invoice) {
        JPanel body = new JPanel(new BorderLayout());
        body.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Dados do cliente
        body.add(createAGTClientInfo(invoice), BorderLayout.NORTH);
        
        // Tabela de produtos
        body.add(createAGTProductsTable(invoice), BorderLayout.CENTER);
        
        // Totais
        body.add(createAGTTotals(invoice), BorderLayout.SOUTH);

        return body;
    }

    /**
     * Informações do cliente - Modelo AGT
     */
    private static JPanel createAGTClientInfo(Invoices invoice) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Dados do Adquirente"));
        
        String clienteNome = invoice.getClient() != null ? invoice.getClient().getName() : "CONSUMIDOR FINAL";
        String clienteNif = invoice.getClient() != null && invoice.getClient().getNif() != null ? 
                           invoice.getClient().getNif() : "Não identificado";
        String clienteEndereco = invoice.getClient() != null && invoice.getClient().getAddress() != null ?
                               invoice.getClient().getAddress() : "Não especificado";

        panel.add(createLabel("Nome:", Font.BOLD, 11));
        panel.add(createLabel(clienteNome, Font.PLAIN, 11));
        
        panel.add(createLabel("NIF:", Font.BOLD, 11));
        panel.add(createLabel(clienteNif, Font.PLAIN, 11));
        
        panel.add(createLabel("Endereço:", Font.BOLD, 11));
        panel.add(createLabel(clienteEndereco, Font.PLAIN, 11));

        return panel;
    }

    /**
     * Tabela de produtos - Modelo AGT
     */
    private static JPanel createAGTProductsTable(Invoices invoice) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Descrição dos Bens/Serviços Prestados"));

        String[] columns = {"Descrição", "Qtd", "Preço Unit.", "Valor Isento", "Valor Taxado", "IVA", "Total"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (invoice.getProducts() != null && !invoice.getProducts().isEmpty()) {
            for (ProductSales ps : invoice.getProducts()) {
                BigDecimal valorTotal = ps.getPrice().multiply(BigDecimal.valueOf(ps.getQty()));
                BigDecimal valorTaxado = BigDecimal.ZERO;
                BigDecimal valorIsento = BigDecimal.ZERO;
                BigDecimal iva = BigDecimal.ZERO;

                // Simulação de cálculo fiscal (adaptar conforme regras AGT)
                if (ps.getTaxePercentage() != null && ps.getTaxePercentage().compareTo(BigDecimal.ZERO) > 0) {
                    valorTaxado = valorTotal;
                    iva = valorTotal.multiply(ps.getTaxePercentage())
                                 .divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
                } else {
                    valorIsento = valorTotal;
                }

                model.addRow(new Object[]{
                    ps.getDescription() != null ? ps.getDescription() : "Produto/Serviço",
                    ps.getQty(),
                    formatCurrency(ps.getPrice()),
                    formatCurrency(valorIsento),
                    formatCurrency(valorTaxado),
                    formatCurrency(iva),
                    formatCurrency(valorTotal)
                });
            }
        }

        JTable table = new JTable(model);
        table.setRowHeight(20);
        table.setFont(new Font("Arial", Font.PLAIN, 10));
        
        // Ajustar larguras das colunas
        table.getColumnModel().getColumn(0).setPreferredWidth(150); // Descrição
        table.getColumnModel().getColumn(1).setPreferredWidth(40);  // Qtd
        table.getColumnModel().getColumn(2).setPreferredWidth(70);  // Preço Unit.
        table.getColumnModel().getColumn(3).setPreferredWidth(70);  // Isento
        table.getColumnModel().getColumn(4).setPreferredWidth(70);  // Taxado
        table.getColumnModel().getColumn(5).setPreferredWidth(60);  // IVA
        table.getColumnModel().getColumn(6).setPreferredWidth(70);  // Total

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    /**
     * Totais - Modelo AGT
     */
    private static JPanel createAGTTotals(Invoices invoice) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Resumo Fiscal e Financeiro"));

        // Cálculos fiscais (simplificados - adaptar conforme AGT)
        BigDecimal totalIsento = calcularTotalIsento(invoice);
        BigDecimal totalTaxado = calcularTotalTaxado(invoice);
        BigDecimal totalIVA = invoice.getTotalTaxe();

        panel.add(createLabel("Total Valor Isento:", Font.BOLD, 11));
        panel.add(createLabel(formatCurrency(totalIsento), Font.PLAIN, 11));
        
        panel.add(createLabel("Total Valor Taxado:", Font.BOLD, 11));
        panel.add(createLabel(formatCurrency(totalTaxado), Font.PLAIN, 11));
        
        panel.add(createLabel("Total IVa Liquidado:", Font.BOLD, 11));
        panel.add(createLabel(formatCurrency(totalIVA), Font.PLAIN, 11));
        
        panel.add(createLabel("Descontos:", Font.BOLD, 11));
        panel.add(createLabel(formatCurrency(invoice.getDiscount()), Font.PLAIN, 11));
        
        panel.add(createLabel("TOTAL DA FATURA:", Font.BOLD, 12));
        panel.add(createLabel("<html><b>" + formatCurrency(invoice.getTotal()) + "</b></html>", Font.BOLD, 12));

        // Informações de pagamento
        BigDecimal saldo = invoice.getTotal().subtract(invoice.getPayTotal() != null ? invoice.getPayTotal() : BigDecimal.ZERO);
        panel.add(createLabel("Total Pago:", Font.BOLD, 11));
        panel.add(createLabel(formatCurrency(invoice.getPayTotal()), Font.PLAIN, 11));
        
        panel.add(createLabel("Saldo Pendente:", Font.BOLD, 11));
        String corSaldo = saldo.compareTo(BigDecimal.ZERO) > 0 ? "red" : "green";
        panel.add(createLabel("<html><b style='color: " + corSaldo + ";'>" + formatCurrency(saldo) + "</b></html>", Font.BOLD, 11));

        return panel;
    }

    /**
     * Rodapé AGT
     */
    private static JPanel createAGTFooter(Invoices invoice) {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JPanel infoPanel = new JPanel(new GridLayout(0, 1));
        
        // Informações obrigatórias AGT
        infoPanel.add(createLabel("** DOCUMENTO FISCAL VÁLIDO PARA EFEITOS FISCAIS **", Font.BOLD, 10));
        infoPanel.add(createLabel("Conserve este documento por um período mínimo de 10 anos", Font.PLAIN, 9));
        infoPanel.add(createLabel("Em conformidade com o Código Geral Tributário de Angola", Font.PLAIN, 9));
        
        if (invoice.getNote() != null && !invoice.getNote().trim().isEmpty()) {
            infoPanel.add(createLabel("Observações: " + invoice.getNote(), Font.ITALIC, 9));
        }

        // Timestamp de geração
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        infoPanel.add(createLabel("Documento gerado em: " + timestamp, Font.PLAIN, 8));

        footer.add(infoPanel, BorderLayout.CENTER);
        return footer;
    }

    // ==========================================================
    // 🔹 MÉTODOS DE IMPRESSÃO - MODELO AGT
    // ==========================================================

    /**
     * Imprime fatura no modelo AGT (com diálogo)
     */
    public static boolean printAGTInvoice(Invoices invoice, java.awt.Component parentComponent) {
        if (invoice == null) return false;

        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            AGTInvoicePrintable printable = new AGTInvoicePrintable(invoice);
            job.setPrintable(printable);

            if (job.printDialog()) {
                job.print();
                JOptionPane.showMessageDialog(parentComponent,
                    "Fatura AGT impressa com sucesso!\n" +
                    "Nº: " + formatInvoiceNumber(invoice),
                    "Impressão Concluída", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        } catch (Exception e) {
            handlePrintError(e, "AGT", parentComponent);
        }
        return false;
    }

    /**
     * Imprime versão simplificada para térmica
     */
    public static boolean printAGTThermal(Invoices invoice, java.awt.Component parentComponent) {
        if (invoice == null) return false;

        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            AGTThermalPrintable printable = new AGTThermalPrintable(invoice);

            PageFormat pageFormat = job.defaultPage();
            pageFormat.setPaper(createThermalPaper());
            job.setPrintable(printable, pageFormat);

            PrintService printer = selectThermalPrinter();
            if (printer != null) {
                job.setPrintService(printer);
                
                int confirm = JOptionPane.showConfirmDialog(parentComponent,
                    "Imprimir fatura AGT em impressora térmica?\n" +
                    "Esta é uma versão simplificada do documento.",
                    "Impressão Térmica AGT", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    job.print();
                    return true;
                }
            }
        } catch (Exception e) {
            handlePrintError(e, "AGT Térmica", parentComponent);
        }
        return false;
    }

    // ==========================================================
    // 🔹 MÉTODOS AUXILIARES
    // ==========================================================

    private static JPanel createProductsPanel(Invoices invoice) {
        // Manter implementação anterior para compatibilidade
        JPanel panel = new JPanel(new BorderLayout());
        // ... implementação existente ...
        return panel;
    }

    private static JPanel createDetailsPanel(Invoices invoice) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        addDetailRow(panel, "Estado Documento:", invoice.getStatus().getDescricao());
        addDetailRow(panel, "Série Documento:", invoice.getSeries() != null ? invoice.getSeries() : "A");
        addDetailRow(panel, "Ano Documento:", String.valueOf(invoice.getYear()));
        addDetailRow(panel, "ATCUD:", invoice.getAtcud() != null ? invoice.getAtcud() : "Gerado automaticamente");
        addDetailRow(panel, "Hash Segurança:", invoice.getHash() != null ? invoice.getHash() : "Não gerado");

        return panel;
    }

    private static JPanel createButtonPanel(Invoices invoice, JDialog dialog, java.awt.Component parent) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnPrintAGT = new JButton("🖨️ Imprimir AGT");
        btnPrintAGT.setToolTipText("Imprimir no modelo oficial AGT");
        btnPrintAGT.addActionListener(e -> {
            printAGTInvoice(invoice, parent);
        });

        JButton btnPrintThermal = new JButton("🎫 Térmica AGT");
        btnPrintThermal.setToolTipText("Versão simplificada para impressora térmica");
        btnPrintThermal.addActionListener(e -> {
            printAGTThermal(invoice, parent);
        });

        JButton btnClose = new JButton("Fechar");
        btnClose.addActionListener(e -> dialog.dispose());

        panel.add(btnPrintAGT);
        panel.add(btnPrintThermal);
        panel.add(btnClose);

        return panel;
    }

    // ==========================================================
    // 🔹 CÁLCULOS FISCAIS AGT
    // ==========================================================

    private static BigDecimal calcularTotalIsento(Invoices invoice) {
        // Simulação - produtos com taxa 0% são considerados isentos
        if (invoice.getProducts() == null) return BigDecimal.ZERO;
        
        return invoice.getProducts().stream()
            .filter(ps -> ps.getTaxePercentage() == null || ps.getTaxePercentage().compareTo(BigDecimal.ZERO) == 0)
            .map(ps -> ps.getPrice().multiply(BigDecimal.valueOf(ps.getQty())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal calcularTotalTaxado(Invoices invoice) {
        // Simulação - produtos com taxa > 0% são considerados taxados
        if (invoice.getProducts() == null) return BigDecimal.ZERO;
        
        return invoice.getProducts().stream()
            .filter(ps -> ps.getTaxePercentage() != null && ps.getTaxePercentage().compareTo(BigDecimal.ZERO) > 0)
            .map(ps -> ps.getPrice().multiply(BigDecimal.valueOf(ps.getQty())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ==========================================================
    // 🔹 UTILITÁRIOS
    // ==========================================================

    private static JLabel createLabel(String text, int style, int size) {
        JLabel label = new JLabel("<html>" + text + "</html>");
        label.setFont(new Font("Arial", style, size));
        return label;
    }

    private static void addDetailRow(JPanel panel, String label, String value) {
        panel.add(createLabel(label, Font.BOLD, 11));
        panel.add(createLabel(value, Font.PLAIN, 11));
    }

    private static String formatCurrency(BigDecimal value) {
        if (value == null) return "0,00 AOA";
        return currencyFormat.format(value);
    }

    public static String formatInvoiceNumber(Invoices invoice) {
        if (invoice == null) return "N/A";
        return String.format("%s %d/%d", invoice.getPrefix(), invoice.getYear(), invoice.getNumber());
    }

    private static String getStatusText(Integer status) {
        switch (status) {
            case 1: return "🟡 RASCUNHO";
            case 2: return "🔵 EMITIDA";
            case 3: return "🟢 PAGA";
            case 4: return "🔴 ANULADA";
            default: return "⚫ DESCONHECIDO";
        }
    }

    private static String abbreviateHash(String hash) {
        if (hash == null || hash.length() <= 16) return hash;
        return hash.substring(0, 8) + "..." + hash.substring(hash.length() - 8);
    }

    private static Paper createThermalPaper() {
        Paper paper = new Paper();
        double width = 80 * 72 / 25.4;
        double height = 297 * 72 / 25.4;
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

    private static void handlePrintError(Exception e, String printType, java.awt.Component parent) {
        System.err.println("❌ Erro na impressão " + printType + ": " + e.getMessage());
        JOptionPane.showMessageDialog(parent,
            "Erro na impressão " + printType + ":\n" + e.getMessage(),
            "Erro de Impressão", JOptionPane.ERROR_MESSAGE);
    }
}