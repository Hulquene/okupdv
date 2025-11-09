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
 * Classe para impressão padrão de pedidos (formato A4)
 */
public class OrderStandardPrintable implements Printable {

    private final Order order;
    private final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00 AOA");
    private final Font fontNormal = new Font("Arial", Font.PLAIN, 10);
    private final Font fontBold = new Font("Arial", Font.BOLD, 10);
    private final Font fontTitle = new Font("Arial", Font.BOLD, 14);
    private final Font fontHeader = new Font("Arial", Font.BOLD, 12);

    public OrderStandardPrintable(Order order) {
        this.order = order;
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

        // Informações do pedido
        y = printStandardOrderInfo(g2d, y, lineHeight, margin);
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
        String title = "PEDIDO/RECIBO " + order.getPrefix() + "/" + order.getNumber();
        g2d.drawString(title, margin + (pageWidth - g2d.getFontMetrics().stringWidth(title)) / 2, y);
        y += lineHeight + 5;

        g2d.setFont(fontHeader);
        g2d.drawString("OKU DPDV - Sistema de Gestão", margin, y);
        y += lineHeight;

        g2d.setFont(fontNormal);
        g2d.drawString("NIF: 541234567 - Luanda, Angola", margin, y);
        y += lineHeight;
        g2d.drawString("Tel: +244 923 456 789", margin, y);
        y += lineHeight;

        return y;
    }

    private int printStandardOrderInfo(Graphics2D g2d, int y, int lineHeight, int margin) {
        g2d.setFont(fontBold);
        g2d.drawString("INFORMAÇÕES DO PEDIDO", margin, y);
        y += lineHeight;

        g2d.setFont(fontNormal);
        g2d.drawString("Número: " + order.getPrefix() + "/" + order.getNumber(), margin, y);
        y += lineHeight;

        g2d.drawString("Data: " + order.getDatecreate(), margin, y);
        y += lineHeight;

        g2d.drawString("Status: " + getStatusText(order.getStatus()), margin, y);
        y += lineHeight;

        if (order.getSeller() != null) {
            g2d.drawString("Vendedor: " + order.getSeller().getName(), margin, y);
            y += lineHeight;
        }

        return y;
    }

    private int printStandardClientInfo(Graphics2D g2d, int y, int lineHeight, int margin) {
        g2d.setFont(fontBold);
        g2d.drawString("DADOS DO CLIENTE", margin, y);
        y += lineHeight;

        g2d.setFont(fontNormal);
        String clienteNome = order.getClient() != null ? order.getClient().getName() : "CONSUMIDOR FINAL";
        g2d.drawString("Nome: " + clienteNome, margin, y);
        y += lineHeight;

        String clienteNif = order.getClient() != null && order.getClient().getNif() != null
                ? order.getClient().getNif() : "N/I";
        g2d.drawString("NIF: " + clienteNif, margin, y);
        y += lineHeight;

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
        g2d.drawString("Total", margin + descWidth + 170, y);
        y += lineHeight;

        // Linha separadora
        g2d.drawLine(margin, y, margin + pageWidth, y);
        y += 5;

        g2d.setFont(fontNormal);

        if (order.getProducts() != null && !order.getProducts().isEmpty()) {
            for (ProductSales ps : order.getProducts()) {
                String descricao = ps.getDescription() != null ? ps.getDescription() : "Produto";

                // Quebrar descrição se necessário
                if (g2d.getFontMetrics().stringWidth(descricao) > descWidth) {
                    // Implementar quebra de linha se necessário
                }

                g2d.drawString(descricao, margin, y);
                g2d.drawString(String.valueOf(ps.getQty()), margin + descWidth + 50, y);
                g2d.drawString(formatCurrency(ps.getPrice()), margin + descWidth + 100, y);
                g2d.drawString(formatCurrency(ps.getTotal()), margin + descWidth + 170, y);
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
        g2d.drawString("RESUMO FINANCEIRO", margin + pageWidth - 200, startY);
        startY += lineHeight;

        g2d.setFont(fontNormal);
        g2d.drawString("Subtotal: " + formatCurrency(order.getSubTotal()), margin + pageWidth - 200, startY);
        startY += lineHeight;

        g2d.drawString("IVA: " + formatCurrency(order.getTotalTaxe()), margin + pageWidth - 200, startY);
        startY += lineHeight;

        g2d.setFont(fontBold);
        g2d.drawString("TOTAL: " + formatCurrency(order.getTotal()), margin + pageWidth - 200, startY);
        startY += lineHeight;

        g2d.setFont(fontNormal);
        g2d.drawString("Total Pago: " + formatCurrency(order.getPayTotal()), margin + pageWidth - 200, startY);
        startY += lineHeight;

        BigDecimal saldo = order.getTotal().subtract(order.getPayTotal() != null ? order.getPayTotal() : BigDecimal.ZERO);
        g2d.drawString("Saldo: " + formatCurrency(saldo), margin + pageWidth - 200, startY);
        startY += lineHeight;

        g2d.drawString("Status Pagamento: " + getPaymentStatus(order), margin + pageWidth - 200, startY);

        return startY;
    }

    private void printStandardFooter(Graphics2D g2d, int y, int lineHeight, int pageWidth, int margin) {
        int footerY = (int) (y + 100);

        g2d.setFont(new Font("Arial", Font.ITALIC, 9));

        g2d.drawString("** DOCUMENTO COMERCIAL **", margin + (pageWidth - g2d.getFontMetrics().stringWidth("** DOCUMENTO COMERCIAL **")) / 2, footerY);
        footerY += lineHeight;

        g2d.drawString("Obrigado pela preferência!", margin + (pageWidth - g2d.getFontMetrics().stringWidth("Obrigado pela preferência!")) / 2, footerY);
        footerY += lineHeight;

        if (order.getHash() != null) {
            g2d.drawString("Hash: " + order.getHash(), margin, footerY);
            footerY += lineHeight;
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        g2d.drawString("Emitido em: " + timestamp, margin, footerY);
    }

    private String formatCurrency(BigDecimal value) {
        if (value == null) {
            return "0,00 AOA";
        }
        return currencyFormat.format(value);
    }

    private String getStatusText(com.okutonda.okudpdv.data.entities.PaymentStatus status) {
        if (status == null) {
            return "DESCONHECIDO";
        }
        switch (status) {
            case PENDENTE:
                return "PENDENTE";
            case PAGO:
                return "PAGO";
            case PARCIAL:
                return "PARCIAL";
            case ATRASADO:
                return "ATRASADO";
            case CANCELADO:
                return "CANCELADO";
            default:
                return "DESCONHECIDO";
        }
    }

    private String getPaymentStatus(Order order) {
        BigDecimal saldo = order.getTotal().subtract(order.getPayTotal() != null ? order.getPayTotal() : BigDecimal.ZERO);
        if (saldo.compareTo(BigDecimal.ZERO) == 0) {
            return "PAGO";
        }
        if (saldo.compareTo(order.getTotal()) == 0) {
            return "PENDENTE";
        }
        return "PARCIAL";
    }
}
