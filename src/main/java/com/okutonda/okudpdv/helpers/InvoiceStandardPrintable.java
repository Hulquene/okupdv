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
 * Classe para impressão padrão de faturas (formato A4)
 */
public class InvoiceStandardPrintable implements Printable {

    private final Invoices invoice;
    private final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00 €");
    private final Font fontNormal = new Font("Arial", Font.PLAIN, 10);
    private final Font fontBold = new Font("Arial", Font.BOLD, 10);
    private final Font fontTitle = new Font("Arial", Font.BOLD, 14);
    private final Font fontHeader = new Font("Arial", Font.BOLD, 12);

    public InvoiceStandardPrintable(Invoices invoice) {
        this.invoice = invoice;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        int margin = 50;
        int y = margin;
        int lineHeight = 15;
        int pageWidth = (int) pageFormat.getImageableWidth() - (2 * margin);

        // Cabeçalho
        y = printStandardHeader(g2d, y, lineHeight, pageWidth, margin);
        y += lineHeight;
        
        // Informações da fatura
        y = printStandardInvoiceInfo(g2d, y, lineHeight, margin);
        y += lineHeight;
        
        // Cliente
        y = printStandardClientInfo(g2d, y, lineHeight, margin);
        y += lineHeight;
        
        // Produtos
        y = printStandardProducts(g2d, y, lineHeight, pageWidth, margin);
        y += lineHeight;
        
        // Totais
        y = printStandardTotals(g2d, y, lineHeight, pageWidth, margin);
        
        // Rodapé
        printStandardFooter(g2d, y, lineHeight, pageWidth, margin);

        return PAGE_EXISTS;
    }

    private int printStandardHeader(Graphics2D g2d, int y, int lineHeight, int pageWidth, int margin) {
        g2d.setFont(fontTitle);
        String title = "FATURA " + invoice.getPrefix() + "/" + invoice.getNumber();
        g2d.drawString(title, margin + (pageWidth - g2d.getFontMetrics().stringWidth(title)) / 2, y);
        y += lineHeight + 5;
        
        g2d.setFont(fontHeader);
        g2d.drawString("OKU DPDV - Sistema de Gestão", margin, y);
        y += lineHeight;
        
        return y;
    }

    private int printStandardInvoiceInfo(Graphics2D g2d, int y, int lineHeight, int margin) {
        g2d.setFont(fontBold);
        g2d.drawString("INFORMAÇÕES DA FATURA", margin, y);
        y += lineHeight;
        
        g2d.setFont(fontNormal);
        g2d.drawString("Data de Emissão: " + invoice.getIssueDate(), margin, y);
        y += lineHeight;
        
        if (invoice.getDueDate() != null) {
            g2d.drawString("Data de Vencimento: " + invoice.getDueDate(), margin, y);
            y += lineHeight;
        }
        
        g2d.drawString("Status: " + invoice.getStatus(), margin, y);
        y += lineHeight;
        
        return y;
    }

    private int printStandardClientInfo(Graphics2D g2d, int y, int lineHeight, int margin) {
        g2d.setFont(fontBold);
        g2d.drawString("DADOS DO CLIENTE", margin, y);
        y += lineHeight;
        
        g2d.setFont(fontNormal);
        if (invoice.getClient() != null) {
            g2d.drawString("Nome: " + invoice.getClient().getName(), margin, y);
            y += lineHeight;
            
            if (invoice.getClient().getNif() != null) {
                g2d.drawString("NIF: " + invoice.getClient().getNif(), margin, y);
                y += lineHeight;
            }
            
            if (invoice.getClient().getAddress() != null) {
                g2d.drawString("Morada: " + invoice.getClient().getAddress(), margin, y);
                y += lineHeight;
            }
        } else {
            g2d.drawString("Nome: Consumidor Final", margin, y);
            y += lineHeight;
        }
        
        if (invoice.getSeller() != null) {
            g2d.drawString("Vendedor: " + invoice.getSeller().getName(), margin, y);
            y += lineHeight;
        }
        
        return y;
    }

    private int printStandardProducts(Graphics2D g2d, int y, int lineHeight, int pageWidth, int margin) {
        g2d.setFont(fontBold);
        g2d.drawString("PRODUTOS/SERVIÇOS", margin, y);
        y += lineHeight;
        
        // Cabeçalho da tabela
        int descWidth = pageWidth - 300;
        g2d.drawString("Descrição", margin, y);
        g2d.drawString("Qtd", margin + descWidth + 50, y);
        g2d.drawString("Preço", margin + descWidth + 100, y);
        g2d.drawString("IVA", margin + descWidth + 170, y);
        g2d.drawString("Total", margin + descWidth + 220, y);
        y += lineHeight;
        
        // Linha separadora
        g2d.drawLine(margin, y, margin + pageWidth, y);
        y += 5;
        
        g2d.setFont(fontNormal);
        
        if (invoice.getProducts() != null && !invoice.getProducts().isEmpty()) {
            for (ProductSales ps : invoice.getProducts()) {
                String descricao = ps.getDescription() != null ? ps.getDescription() : "Produto";
                
                // Quebrar descrição se necessário
                if (g2d.getFontMetrics().stringWidth(descricao) > descWidth) {
                    // Implementar quebra de linha se necessário
                }
                
                g2d.drawString(descricao, margin, y);
                g2d.drawString(String.valueOf(ps.getQty()), margin + descWidth + 50, y);
                g2d.drawString(formatCurrency(ps.getPrice()), margin + descWidth + 100, y);
                g2d.drawString(ps.getTaxePercentage() != null ? ps.getTaxePercentage() + "%" : "0%", margin + descWidth + 170, y);
                g2d.drawString(formatCurrency(ps.getTotal()), margin + descWidth + 220, y);
                y += lineHeight;
            }
        } else {
            g2d.drawString("Nenhum item encontrado", margin, y);
            y += lineHeight;
        }
        
        return y;
    }

    private int printStandardTotals(Graphics2D g2d, int y, int lineHeight, int pageWidth, int margin) {
        int startY = y + 10;
        
        g2d.setFont(fontBold);
        g2d.drawString("TOTAIS", margin + pageWidth - 150, startY);
        startY += lineHeight;
        
        g2d.setFont(fontNormal);
        g2d.drawString("Subtotal: " + formatCurrency(invoice.getSubTotal()), margin + pageWidth - 150, startY);
        startY += lineHeight;
        
        g2d.drawString("IVA Total: " + formatCurrency(invoice.getTotalTaxe()), margin + pageWidth - 150, startY);
        startY += lineHeight;
        
        if (invoice.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
            g2d.drawString("Desconto: " + formatCurrency(invoice.getDiscount()), margin + pageWidth - 150, startY);
            startY += lineHeight;
        }
        
        g2d.setFont(fontBold);
        g2d.drawString("TOTAL: " + formatCurrency(invoice.getTotal()), margin + pageWidth - 150, startY);
        startY += lineHeight;
        
        g2d.setFont(fontNormal);
        g2d.drawString("Total Pago: " + formatCurrency(invoice.getPayTotal()), margin + pageWidth - 150, startY);
        startY += lineHeight;
        
        BigDecimal saldo = invoice.getTotal().subtract(invoice.getPayTotal() != null ? invoice.getPayTotal() : BigDecimal.ZERO);
        g2d.drawString("Saldo Pendente: " + formatCurrency(saldo), margin + pageWidth - 150, startY);
        
        return startY;
    }

    private void printStandardFooter(Graphics2D g2d, int y, int lineHeight, int pageWidth, int margin) {
        int footerY = (int) (y + 100);
        
        g2d.setFont(new Font("Arial", Font.ITALIC, 9));
        
        if (invoice.getNote() != null && !invoice.getNote().trim().isEmpty()) {
            g2d.drawString("Observações: " + invoice.getNote(), margin, footerY);
            footerY += lineHeight;
        }
        
        if (invoice.getHash() != null) {
            g2d.drawString("Hash: " + invoice.getHash(), margin, footerY);
            footerY += lineHeight;
        }
        
        g2d.drawString("Documento gerado por OKU DPDV", margin, footerY);
    }

    private String formatCurrency(BigDecimal value) {
        if (value == null) return "0,00 €";
        return currencyFormat.format(value);
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
}