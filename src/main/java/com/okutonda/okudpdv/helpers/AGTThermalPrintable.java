package com.okutonda.okudpdv.helpers;

import com.okutonda.okudpdv.data.entities.Invoices;
import com.okutonda.okudpdv.data.entities.ProductSales;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe para impressão térmica simplificada no modelo AGT Angola (80mm)
 */
public class AGTThermalPrintable implements Printable {

    private final Invoices invoice;
    private final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00 AOA");
    private final Font fontNormal = new Font("Monospaced", Font.PLAIN, 8);
    private final Font fontBold = new Font("Monospaced", Font.BOLD, 8);
    private final Font fontTitle = new Font("Monospaced", Font.BOLD, 9);
    private final Font fontSmall = new Font("Monospaced", Font.PLAIN, 7);

    // Largura máxima para papel 80mm
    private static final int MAX_WIDTH = 42;

    public AGTThermalPrintable(Invoices invoice) {
        this.invoice = invoice;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        int y = 0;
        int lineHeight = 10;

        // Cabeçalho
        y = printThermalHeader(g2d, y, lineHeight);
        y = printSeparator(g2d, y, lineHeight);

        // Documento
        y = printThermalDocumentInfo(g2d, y, lineHeight);
        y = printSeparator(g2d, y, lineHeight);

        // Cliente
        y = printThermalClientInfo(g2d, y, lineHeight);
        y = printSeparator(g2d, y, lineHeight);

        // Produtos
        y = printThermalProducts(g2d, y, lineHeight);
        y = printSeparator(g2d, y, lineHeight);

        // Totais
        y = printThermalTotals(g2d, y, lineHeight);
        y = printSeparator(g2d, y, lineHeight);

        // Rodapé
        y = printThermalFooter(g2d, y, lineHeight);

        return PAGE_EXISTS;
    }

    private int printThermalHeader(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontTitle);
        g2d.drawString(centerText("FATURA AGT"), 0, y);
        y += lineHeight;

        g2d.setFont(fontBold);
        g2d.drawString(centerText("OKU DPDV LDA"), 0, y);
        y += lineHeight;

        g2d.setFont(fontNormal);
        g2d.drawString(centerText("NIF: 541234567"), 0, y);
        y += lineHeight;

        g2d.drawString(centerText("Luanda, Angola"), 0, y);
        y += lineHeight + 2;

        return y;
    }

    private int printThermalDocumentInfo(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontBold);
        g2d.drawString("DOC: " + invoice.getPrefix() + "/" + invoice.getNumber(), 0, y);
        y += lineHeight;

        g2d.setFont(fontNormal);
        g2d.drawString("Data: " + invoice.getIssueDate(), 0, y);
        y += lineHeight;

        g2d.drawString("Estado: " + invoice.getStatus(), 0, y);
        y += lineHeight;

        return y;
    }

    private int printThermalClientInfo(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontBold);
        g2d.drawString("CLIENTE", 0, y);
        y += lineHeight;

        g2d.setFont(fontNormal);
        String clienteNome = invoice.getClient() != null ? invoice.getClient().getName() : "CONSUMIDOR FINAL";
        if (clienteNome.length() > MAX_WIDTH) {
            clienteNome = clienteNome.substring(0, MAX_WIDTH - 3) + "...";
        }
        g2d.drawString(clienteNome, 0, y);
        y += lineHeight;

        String clienteNif = invoice.getClient() != null && invoice.getClient().getNif() != null
                ? invoice.getClient().getNif() : "N/I";
        g2d.drawString("NIF: " + clienteNif, 0, y);
        y += lineHeight;

        return y;
    }

    private int printThermalProducts(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontBold);
        g2d.drawString("PRODUTOS/SERVICOS", 0, y);
        y += lineHeight;

        // Cabeçalho
        g2d.drawString("Descricao", 0, y);
        g2d.drawString("Qtd  Preco   Total", MAX_WIDTH - 18, y);
        y += lineHeight;

        g2d.setFont(fontNormal);

        if (invoice.getProducts() != null && !invoice.getProducts().isEmpty()) {
            for (ProductSales ps : invoice.getProducts()) {
                // Descrição
                String descricao = ps.getDescription() != null ? ps.getDescription() : "Item";
                if (descricao.length() > 25) {
                    descricao = descricao.substring(0, 22) + "...";
                }
                g2d.drawString(descricao, 0, y);
                y += lineHeight;

                // Valores
                String valores = String.format("%-3d %-6s %-6s",
                        ps.getQty(),
                        formatCurrencyCompact(ps.getPrice()),
                        formatCurrencyCompact(ps.getTotal()));
                g2d.drawString(valores, MAX_WIDTH - 18, y);
                y += lineHeight;

                if (y > 200) {
                    break; // Limite da página
                }
            }
        }

        return y;
    }

    private int printThermalTotals(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontBold);
        g2d.drawString("RESUMO", 0, y);
        y += lineHeight;

        g2d.setFont(fontNormal);
        g2d.drawString(String.format("%-15s %8s", "Subtotal:", formatCurrencyCompact(invoice.getSubTotal())), 0, y);
        y += lineHeight;

        g2d.drawString(String.format("%-15s %8s", "IVA:", formatCurrencyCompact(invoice.getTotalTaxe())), 0, y);
        y += lineHeight;

        if (invoice.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
            g2d.drawString(String.format("%-15s %8s", "Desconto:", formatCurrencyCompact(invoice.getDiscount())), 0, y);
            y += lineHeight;
        }

        g2d.setFont(fontBold);
        g2d.drawString(String.format("%-15s %8s", "TOTAL:", formatCurrencyCompact(invoice.getTotal())), 0, y);
        y += lineHeight;

        BigDecimal saldo = invoice.getTotal().subtract(invoice.getPayTotal() != null ? invoice.getPayTotal() : BigDecimal.ZERO);
        g2d.setFont(fontNormal);
        g2d.drawString(String.format("%-15s %8s", "Saldo:", formatCurrencyCompact(saldo)), 0, y);
        y += lineHeight;

        return y;
    }

    private int printThermalFooter(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontSmall);
        g2d.drawString(centerText("** DOCUMENTO FISCAL **"), 0, y);
        y += lineHeight;

        g2d.drawString(centerText("Valido para efeitos fiscais"), 0, y);
        y += lineHeight;

        g2d.drawString(centerText("Conserve por 10 anos"), 0, y);
        y += lineHeight;

        if (invoice.getHash() != null) {
            g2d.drawString(centerText("H: " + abbreviateHash(invoice.getHash())), 0, y);
            y += lineHeight;
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));
        g2d.drawString(centerText(timestamp), 0, y);

        return y;
    }

    private int printSeparator(Graphics2D g2d, int y, int lineHeight) {
        g2d.drawLine(0, y + 2, MAX_WIDTH * 3, y + 2);
        return y + lineHeight;
    }

    private String centerText(String text) {
        int spaces = (MAX_WIDTH - text.length()) / 2;
        if (spaces > 0) {
            return String.format("%" + spaces + "s%s", "", text);
        }
        return text;
    }

    private String formatCurrencyCompact(BigDecimal value) {
        if (value == null) {
            return "0,00";
        }
        String formatted = currencyFormat.format(value);
        return formatted.replace(" AOA", "");
    }

    private String getStatusAbreviado(Integer status) {
        switch (status) {
            case 1:
                return "RASC";
            case 2:
                return "NORM";
            case 3:
                return "PAGA";
            case 4:
                return "ANUL";
            default:
                return "DESC";
        }
    }

    private String abbreviateHash(String hash) {
        if (hash == null || hash.length() <= 12) {
            return hash;
        }
        return hash.substring(0, 6) + "..." + hash.substring(hash.length() - 6);
    }
}
