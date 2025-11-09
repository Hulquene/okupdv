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
 * Classe para impressão térmica de pedidos (formato 80mm)
 */
public class OrderThermalPrintable implements Printable {

    private final Order order;
    private final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00 AOA");
    private final Font fontNormal = new Font("Monospaced", Font.PLAIN, 8);
    private final Font fontBold = new Font("Monospaced", Font.BOLD, 8);
    private final Font fontTitle = new Font("Monospaced", Font.BOLD, 9);
    private final Font fontSmall = new Font("Monospaced", Font.PLAIN, 7);

    // Largura máxima para papel 80mm
    private static final int MAX_WIDTH = 42;

    public OrderThermalPrintable(Order order) {
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

        // Cabeçalho
        y = printHeader(g2d, y, lineHeight);
        y = printSeparator(g2d, y, lineHeight);
        
        // Informações do pedido
        y = printOrderInfo(g2d, y, lineHeight);
        y = printSeparator(g2d, y, lineHeight);
        
        // Cliente
        y = printClientInfo(g2d, y, lineHeight);
        y = printSeparator(g2d, y, lineHeight);
        
        // Produtos
        y = printProducts(g2d, y, lineHeight);
        y = printSeparator(g2d, y, lineHeight);
        
        // Totais
        y = printTotals(g2d, y, lineHeight);
        y = printSeparator(g2d, y, lineHeight);
        
        // Pagamento
        y = printPaymentInfo(g2d, y, lineHeight);
        y = printSeparator(g2d, y, lineHeight);
        
        // Rodapé
        printFooter(g2d, y, lineHeight);

        return PAGE_EXISTS;
    }

    private int printHeader(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontTitle);
        g2d.drawString(centerText("PEDIDO/RECIBO"), 0, y);
        y += lineHeight;
        
        g2d.setFont(fontBold);
        g2d.drawString(centerText("OKU DPDV LDA"), 0, y);
        y += lineHeight;
        
        g2d.setFont(fontNormal);
        g2d.drawString(centerText("NIF: 541234567"), 0, y);
        y += lineHeight;
        
        g2d.drawString(centerText("Luanda, Angola"), 0, y);
        y += lineHeight;
        
        g2d.drawString(centerText("Tel: +244 923 456 789"), 0, y);
        y += lineHeight + 2;
        
        return y;
    }

    private int printOrderInfo(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontBold);
        g2d.drawString("PEDIDO: " + order.getPrefix() + "/" + order.getNumber(), 0, y);
        y += lineHeight;
        
        g2d.setFont(fontNormal);
        g2d.drawString("Data: " + order.getDatecreate(), 0, y);
        y += lineHeight;
        
        g2d.drawString("Status: " + getStatusAbreviado(order.getStatus()), 0, y);
        y += lineHeight;
        
        if (order.getSeller() != null) {
            g2d.drawString("Vendedor: " + order.getSeller().getName(), 0, y);
            y += lineHeight;
        }
        
        return y;
    }

    private int printClientInfo(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontBold);
        g2d.drawString("CLIENTE", 0, y);
        y += lineHeight;
        
        g2d.setFont(fontNormal);
        String clienteNome = order.getClient() != null ? order.getClient().getName() : "CONSUMIDOR FINAL";
        if (clienteNome.length() > MAX_WIDTH) {
            clienteNome = clienteNome.substring(0, MAX_WIDTH - 3) + "...";
        }
        g2d.drawString(clienteNome, 0, y);
        y += lineHeight;
        
        String clienteNif = order.getClient() != null && order.getClient().getNif() != null ? 
                           order.getClient().getNif() : "N/I";
        g2d.drawString("NIF: " + clienteNif, 0, y);
        y += lineHeight;
        
        return y;
    }

    private int printProducts(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontBold);
        g2d.drawString("PRODUTOS/SERVICOS", 0, y);
        y += lineHeight;
        
        // Cabeçalho
        g2d.drawString("Descricao", 0, y);
        g2d.drawString("Qtd  Preco   Total", MAX_WIDTH - 18, y);
        y += lineHeight;
        
        g2d.setFont(fontNormal);
        
        if (order.getProducts() != null && !order.getProducts().isEmpty()) {
            for (ProductSales ps : order.getProducts()) {
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
                
                if (y > 200) break; // Limite da página
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
        g2d.drawString(String.format("%-15s %8s", "Subtotal:", formatCurrencyCompact(order.getSubTotal())), 0, y);
        y += lineHeight;
        
        g2d.drawString(String.format("%-15s %8s", "IVA:", formatCurrencyCompact(order.getTotalTaxe())), 0, y);
        y += lineHeight;
        
//        if (order.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
//            g2d.drawString(String.format("%-15s %8s", "Desconto:", formatCurrencyCompact(order.getDiscount())), 0, y);
//            y += lineHeight;
//        }
        
        g2d.setFont(fontBold);
        g2d.drawString(String.format("%-15s %8s", "TOTAL:", formatCurrencyCompact(order.getTotal())), 0, y);
        y += lineHeight;
        
        return y;
    }

    private int printPaymentInfo(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontBold);
        g2d.drawString("INFORMAÇÕES PAGAMENTO", 0, y);
        y += lineHeight;
        
        g2d.setFont(fontNormal);
        g2d.drawString(String.format("%-15s %8s", "Total Pago:", formatCurrencyCompact(order.getPayTotal())), 0, y);
        y += lineHeight;
        
        BigDecimal saldo = order.getTotal().subtract(order.getPayTotal() != null ? order.getPayTotal() : BigDecimal.ZERO);
        g2d.drawString(String.format("%-15s %8s", "Saldo:", formatCurrencyCompact(saldo)), 0, y);
        y += lineHeight;
        
        g2d.drawString("Status: " + getPaymentStatus(order), 0, y);
        y += lineHeight;
        
        return y;
    }

    private int printFooter(Graphics2D g2d, int y, int lineHeight) {
        g2d.setFont(fontSmall);
        g2d.drawString(centerText("** DOCUMENTO COMERCIAL **"), 0, y);
        y += lineHeight;
        
        g2d.drawString(centerText("Obrigado pela preferência!"), 0, y);
        y += lineHeight;
        
        if (order.getHash() != null) {
            g2d.drawString(centerText("H: " + abbreviateHash(order.getHash())), 0, y);
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
        if (value == null) return "0,00";
        String formatted = currencyFormat.format(value);
        return formatted.replace(" AOA", "");
    }

    private String getStatusAbreviado(com.okutonda.okudpdv.data.entities.PaymentStatus status) {
        if (status == null) return "DESC";
        switch (status) {
            case PENDENTE: return "PEND";
            case PAGO: return "PAGO";
            case PARCIAL: return "PARC";
            case ATRASADO: return "ATRA";
            case CANCELADO: return "CANC";
            default: return "DESC";
        }
    }

    private String getPaymentStatus(Order order) {
        BigDecimal saldo = order.getTotal().subtract(order.getPayTotal() != null ? order.getPayTotal() : BigDecimal.ZERO);
        if (saldo.compareTo(BigDecimal.ZERO) == 0) return "PAGO";
        if (saldo.compareTo(order.getTotal()) == 0) return "PENDENTE";
        return "PARCIAL";
    }

    private String abbreviateHash(String hash) {
        if (hash == null || hash.length() <= 12) return hash;
        return hash.substring(0, 6) + "..." + hash.substring(hash.length() - 6);
    }
}