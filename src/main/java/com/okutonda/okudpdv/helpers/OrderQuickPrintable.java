package com.okutonda.okudpdv.helpers;

import com.okutonda.okudpdv.data.entities.Order;
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
 * Classe para impressão rápida de tickets (versão simplificada)
 */
public class OrderQuickPrintable implements Printable {

    private final Order order;
    private final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00 AOA");
    private final Font fontNormal = new Font("Monospaced", Font.PLAIN, 9);
    private final Font fontBold = new Font("Monospaced", Font.BOLD, 9);
    private final Font fontSmall = new Font("Monospaced", Font.PLAIN, 8);

    private static final int MAX_WIDTH = 42;

    public OrderQuickPrintable(Order order) {
        this.order = order;
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

        // Cabeçalho rápido
        y = printQuickHeader(g2d, y, lineHeight);
        y += 2;
        
        // Informações essenciais
        y = printQuickInfo(g2d, y, lineHeight);
        y += 2;
        
        // Produtos (apenas nomes e quantidades)
        y = printQuickProducts(g2d, y, lineHeight);
        y += 2;
        
        // Total
        y = printQuickTotal(g2d, y, lineHeight);
        y += 2;
        
        // Rodapé rápido
        printQuickFooter(g2d, y, lineHeight);

        return PAGE_EXISTS;
    }

    private int printQuickHeader(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontBold);
        g2d.drawString(centerText("COMPROVANTE"), 0, y);
        y += lineHeight;
        
        g2d.setFont(fontNormal);
        g2d.drawString(centerText("OKU DPDV"), 0, y);
        y += lineHeight;
        
        return y;
    }

    private int printQuickInfo(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontBold);
        g2d.drawString("PED: " + order.getPrefix() + "/" + order.getNumber(), 0, y);
        y += lineHeight;
        
        g2d.setFont(fontNormal);
        g2d.drawString("Data: " + order.getDatecreate(), 0, y);
        y += lineHeight;
        
        String cliente = order.getClient() != null ? order.getClient().getName() : "Consumidor";
        if (cliente.length() > 25) {
            cliente = cliente.substring(0, 22) + "...";
        }
        g2d.drawString("Cliente: " + cliente, 0, y);
        y += lineHeight;
        
        return y;
    }

    private int printQuickProducts(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontBold);
        g2d.drawString("ITENS:", 0, y);
        y += lineHeight;
        
        g2d.setFont(fontNormal);
        
        if (order.getProducts() != null && !order.getProducts().isEmpty()) {
            for (ProductSales ps : order.getProducts()) {
                String descricao = ps.getDescription() != null ? ps.getDescription() : "Item";
                if (descricao.length() > 20) {
                    descricao = descricao.substring(0, 17) + "...";
                }
                
                String linha = String.format("%-20s %2dx", descricao, ps.getQty());
                g2d.drawString(linha, 0, y);
                y += lineHeight;
                
                if (y > 180) break; // Limite para ticket rápido
            }
        }
        
        return y;
    }

    private int printQuickTotal(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontBold);
        g2d.drawString("TOTAL: " + formatCurrencyCompact(order.getTotal()), 0, y);
        y += lineHeight;
        
        return y;
    }

    private void printQuickFooter(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontSmall);
        g2d.drawString(centerText("Obrigado!"), 0, y);
        y += lineHeight;
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));
        g2d.drawString(centerText(timestamp), 0, y);
    }

    private String centerText(String text) {
        int spaces = (MAX_WIDTH - text.length()) / 2;
        if (spaces > 0) {
            return String.format("%" + spaces + "s%s", "", text);
        }
        return text;
    }

    private String formatCurrencyCompact(BigDecimal value) {
        if (value == null) return "0,00";
        String formatted = currencyFormat.format(value);
        return formatted.replace(" AOA", "");
    }
}