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

/**
 * Classe para impressão de faturas em impressoras térmicas (80mm)
 */
public class InvoiceThermalPrintable implements Printable {

    private final Invoices invoice;
    private final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00 €");
    private final Font fontNormal = new Font("Monospaced", Font.PLAIN, 9);
    private final Font fontBold = new Font("Monospaced", Font.BOLD, 9);
    private final Font fontTitle = new Font("Monospaced", Font.BOLD, 10);
    private final Font fontSmall = new Font("Monospaced", Font.PLAIN, 8);

    // Largura máxima para papel 80mm (em caracteres)
    private static final int MAX_WIDTH = 42;

    public InvoiceThermalPrintable(Invoices invoice) {
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
        int lineHeight = 12;

        // Cabeçalho da empresa
        y = printHeader(g2d, y, lineHeight);
        
        // Linha separadora
        y = printSeparator(g2d, y, lineHeight);
        
        // Dados da fatura
        y = printInvoiceInfo(g2d, y, lineHeight);
        
        // Linha separadora
        y = printSeparator(g2d, y, lineHeight);
        
        // Dados do cliente
        y = printClientInfo(g2d, y, lineHeight);
        
        // Linha separadora
        y = printSeparator(g2d, y, lineHeight);
        
        // Produtos
        y = printProducts(g2d, y, lineHeight);
        
        // Linha separadora
        y = printSeparator(g2d, y, lineHeight);
        
        // Totais
        y = printTotals(g2d, y, lineHeight);
        
        // Linha separadora
        y = printSeparator(g2d, y, lineHeight);
        
        // Rodapé
        y = printFooter(g2d, y, lineHeight);

        return PAGE_EXISTS;
    }

    private int printHeader(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontTitle);
        g2d.drawString(centerText("OKU DPDV"), 0, y);
        y += lineHeight;
        
        g2d.setFont(fontNormal);
        g2d.drawString(centerText("Sistema de Gestão"), 0, y);
        y += lineHeight;
        
        g2d.drawString(centerText("Tel: +244 123 456 789"), 0, y);
        y += lineHeight;
        
        g2d.drawString(centerText("NIF: 123456789"), 0, y);
        y += lineHeight + 5;
        
        return y;
    }

    private int printInvoiceInfo(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontBold);
        g2d.drawString("FATURA: " + invoice.getPrefix() + "/" + invoice.getNumber(), 0, y);
        y += lineHeight;
        
        g2d.setFont(fontNormal);
        g2d.drawString("Data: " + invoice.getIssueDate(), 0, y);
        y += lineHeight;
        
        if (invoice.getDueDate() != null) {
            g2d.drawString("Venc: " + invoice.getDueDate(), 0, y);
            y += lineHeight;
        }
        
        g2d.drawString("Status: " + invoice.getStatus(), 0, y);
        y += lineHeight;
        
        return y;
    }

    private int printClientInfo(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontBold);
        g2d.drawString("CLIENTE", 0, y);
        y += lineHeight;
        
        g2d.setFont(fontNormal);
        if (invoice.getClient() != null) {
            String clienteNome = invoice.getClient().getName();
            if (clienteNome.length() > MAX_WIDTH) {
                clienteNome = clienteNome.substring(0, MAX_WIDTH - 3) + "...";
            }
            g2d.drawString(clienteNome, 0, y);
            y += lineHeight;
            
            if (invoice.getClient().getNif() != null) {
                g2d.drawString("NIF: " + invoice.getClient().getNif(), 0, y);
                y += lineHeight;
            }
        } else {
            g2d.drawString("Consumidor Final", 0, y);
            y += lineHeight;
        }
        
        return y;
    }

    private int printProducts(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontBold);
        g2d.drawString("PRODUTOS/SERVIÇOS", 0, y);
        y += lineHeight;
        
        // Cabeçalho da tabela
        g2d.drawString("Descrição", 0, y);
        g2d.drawString("Qtd  Preço   Total", MAX_WIDTH - 18, y);
        y += lineHeight;
        
        g2d.setFont(fontNormal);
        
        if (invoice.getProducts() != null && !invoice.getProducts().isEmpty()) {
            for (ProductSales ps : invoice.getProducts()) {
                // Descrição (quebrada se necessário)
                String descricao = ps.getDescription() != null ? ps.getDescription() : "Produto";
                if (descricao.length() > 25) {
                    descricao = descricao.substring(0, 22) + "...";
                }
                
                g2d.drawString(descricao, 0, y);
                y += lineHeight;
                
                // Quantidade, preço e total
                String linhaValores = String.format("%-3d %-7s %-7s", 
                    ps.getQty(),
                    formatCurrency(ps.getPrice()),
                    formatCurrency(ps.getTotal()));
                
                g2d.drawString(linhaValores, MAX_WIDTH - 18, y);
                y += lineHeight;
                
                // Verificar se precisa de nova página
                if (y > 250) {
                    g2d.drawString("... mais itens ...", 0, y);
                    y += lineHeight;
                    break;
                }
            }
        } else {
            g2d.drawString("Nenhum item", 0, y);
            y += lineHeight;
        }
        
        return y;
    }

    private int printTotals(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontBold);
        g2d.drawString("RESUMO FINANCEIRO", 0, y);
        y += lineHeight;
        
        g2d.setFont(fontNormal);
        g2d.drawString(String.format("%-15s %10s", "Subtotal:", formatCurrency(invoice.getSubTotal())), 0, y);
        y += lineHeight;
        
        g2d.drawString(String.format("%-15s %10s", "IVA:", formatCurrency(invoice.getTotalTaxe())), 0, y);
        y += lineHeight;
        
        if (invoice.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
            g2d.drawString(String.format("%-15s %10s", "Desconto:", formatCurrency(invoice.getDiscount())), 0, y);
            y += lineHeight;
        }
        
        g2d.setFont(fontBold);
        g2d.drawString(String.format("%-15s %10s", "TOTAL:", formatCurrency(invoice.getTotal())), 0, y);
        y += lineHeight;
        
        g2d.setFont(fontNormal);
        g2d.drawString(String.format("%-15s %10s", "Pago:", formatCurrency(invoice.getPayTotal())), 0, y);
        y += lineHeight;
        
        BigDecimal saldo = invoice.getTotal().subtract(invoice.getPayTotal() != null ? invoice.getPayTotal() : BigDecimal.ZERO);
        g2d.drawString(String.format("%-15s %10s", "Saldo:", formatCurrency(saldo)), 0, y);
        y += lineHeight;
        
        return y;
    }

    private int printFooter(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontSmall);
        g2d.drawString(centerText("Obrigado pela preferência!"), 0, y);
        y += lineHeight;
        
        g2d.drawString(centerText("Documento gerado por OKU DPDV"), 0, y);
        y += lineHeight;
        
        if (invoice.getHash() != null) {
            g2d.drawString(centerText("Hash: " + abbreviateHash(invoice.getHash())), 0, y);
            y += lineHeight;
        }
        
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

    private String formatCurrency(BigDecimal value) {
        if (value == null) return "0,00€";
        String formatted = currencyFormat.format(value);
        // Remover espaço para caber melhor no ticket
        return formatted.replace(" €", "€");
    }

    private String getStatusText(Integer status) {
        switch (status) {
            case 1: return "PENDENTE";
            case 2: return "EMITIDA";
            case 3: return "PAGA";
            case 4: return "ANULADA";
            default: return "DESCONHECIDO";
        }
    }

    private String abbreviateHash(String hash) {
        if (hash == null || hash.length() <= 16) return hash;
        return hash.substring(0, 8) + "..." + hash.substring(hash.length() - 8);
    }
}