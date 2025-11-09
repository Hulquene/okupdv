package com.okutonda.okudpdv.helpers;

import com.okutonda.okudpdv.data.entities.Invoices;
import com.okutonda.okudpdv.data.entities.PaymentStatus;
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
 * Classe para impressão de faturas no modelo oficial AGT Angola (Formato A4)
 */
public class AGTInvoicePrintable implements Printable {

    private final Invoices invoice;
    private final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00 AOA");
    private final Font fontNormal = new Font("Arial", Font.PLAIN, 9);
    private final Font fontBold = new Font("Arial", Font.BOLD, 9);
    private final Font fontTitle = new Font("Arial", Font.BOLD, 14);
    private final Font fontHeader = new Font("Arial", Font.BOLD, 11);
    private final Font fontSmall = new Font("Arial", Font.PLAIN, 8);

    // Dados da empresa (configuráveis)
    private final String EMPRESA_NOME = "OKU DPDV LDA";
    private final String EMPRESA_NIF = "541234567";
    private final String EMPRESA_ENDERECO = "Luanda, Angola";
    private final String EMPRESA_CONTACTO = "+244 923 456 789";
    private final String EMPRESA_ATIVIDADE = "COMÉRCIO E PRESTAÇÃO DE SERVIÇOS";

    public AGTInvoicePrintable(Invoices invoice) {
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
        int lineHeight = 12;
        int pageWidth = (int) pageFormat.getImageableWidth() - (2 * margin);

        // Cabeçalho AGT
        y = printAGTHeader(g2d, y, lineHeight, pageWidth, margin);
        y += lineHeight + 5;

        // Dados do documento
        y = printDocumentInfo(g2d, y, lineHeight, margin);
        y += lineHeight + 5;

        // Dados do cliente
        y = printClientInfo(g2d, y, lineHeight, margin);
        y += lineHeight + 5;

        // Tabela de produtos
        y = printProductsTable(g2d, y, lineHeight, pageWidth, margin);
        y += lineHeight + 5;

        // Resumo fiscal
        y = printFiscalSummary(g2d, y, lineHeight, pageWidth, margin);
        y += lineHeight + 5;

        // Rodapé AGT
        printAGTFooter(g2d, y, lineHeight, pageWidth, margin);

        return PAGE_EXISTS;
    }

    private int printAGTHeader(Graphics2D g2d, int y, int lineHeight, int pageWidth, int margin) {
        // Linha superior - Dados da empresa
        g2d.setFont(fontTitle);
        String title = "FATURA/RECIBO - MODELO OFICIAL AGT";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, margin + (pageWidth - titleWidth) / 2, y);
        y += lineHeight + 5;

        g2d.setFont(fontHeader);
        g2d.drawString(EMPRESA_NOME, margin, y);
        y += lineHeight;

        g2d.setFont(fontNormal);
        g2d.drawString("NIF: " + EMPRESA_NIF, margin, y);
        g2d.drawString("Atividade: " + EMPRESA_ATIVIDADE, margin + 200, y);
        y += lineHeight;

        g2d.drawString("Endereço: " + EMPRESA_ENDERECO, margin, y);
        g2d.drawString("Contacto: " + EMPRESA_CONTACTO, margin + 200, y);
        y += lineHeight;

        // Linha separadora
        g2d.drawLine(margin, y, margin + pageWidth, y);
        y += lineHeight + 5;

        return y;
    }

    private int printDocumentInfo(Graphics2D g2d, int y, int lineHeight, int margin) {
        g2d.setFont(fontHeader);
        g2d.drawString("INFORMAÇÕES DO DOCUMENTO FISCAL", margin, y);
        y += lineHeight;

        g2d.setFont(fontNormal);
        g2d.drawString("Nº Documento: " + formatInvoiceNumber(invoice), margin, y);
        g2d.drawString("Série: " + (invoice.getSeries() != null ? invoice.getSeries() : "A"), margin + 200, y);
        y += lineHeight;

        g2d.drawString("Data Emissão: " + invoice.getIssueDate(), margin, y);
        if (invoice.getDueDate() != null) {
            g2d.drawString("Data Vencimento: " + invoice.getDueDate(), margin + 200, y);
        }
        y += lineHeight;

        g2d.drawString("Estado: " + invoice.getStatus().getDescricao(), margin, y);
        if (invoice.getAtcud() != null) {
            g2d.drawString("ATCUD: " + invoice.getAtcud(), margin + 200, y);
        }
        y += lineHeight;

        if (invoice.getHash() != null) {
            g2d.drawString("Hash: " + invoice.getHash(), margin, y);
            y += lineHeight;
        }

        return y;
    }

    private int printClientInfo(Graphics2D g2d, int y, int lineHeight, int margin) {
        g2d.setFont(fontHeader);
        g2d.drawString("DADOS DO ADQUIRENTE", margin, y);
        y += lineHeight;

        g2d.setFont(fontNormal);
        String clienteNome = invoice.getClient() != null ? invoice.getClient().getName() : "CONSUMIDOR FINAL";
        String clienteNif = invoice.getClient() != null && invoice.getClient().getNif() != null
                ? invoice.getClient().getNif() : "NÃO IDENTIFICADO";

        g2d.drawString("Nome: " + clienteNome, margin, y);
        y += lineHeight;

        g2d.drawString("NIF: " + clienteNif, margin, y);

        if (invoice.getClient() != null && invoice.getClient().getAddress() != null) {
            g2d.drawString("Endereço: " + invoice.getClient().getAddress(), margin + 200, y);
        }
        y += lineHeight;

        if (invoice.getSeller() != null) {
            g2d.drawString("Vendedor: " + invoice.getSeller().getName(), margin, y);
            y += lineHeight;
        }

        return y;
    }

    private int printProductsTable(Graphics2D g2d, int y, int lineHeight, int pageWidth, int margin) {
        g2d.setFont(fontHeader);
        g2d.drawString("DESCRIÇÃO DOS BENS/SERVIÇOS PRESTADOS", margin, y);
        y += lineHeight;

        // Cabeçalho da tabela
        int[] columnWidths = {180, 40, 70, 70, 70, 60, 70}; // Larguras das colunas
        int currentX = margin;

        g2d.setFont(fontBold);
        g2d.drawString("Descrição", currentX, y);
        currentX += columnWidths[0];

        g2d.drawString("Qtd", currentX, y);
        currentX += columnWidths[1];

        g2d.drawString("Preço Unit.", currentX, y);
        currentX += columnWidths[2];

        g2d.drawString("Valor Isento", currentX, y);
        currentX += columnWidths[3];

        g2d.drawString("Valor Taxado", currentX, y);
        currentX += columnWidths[4];

        g2d.drawString("IVA", currentX, y);
        currentX += columnWidths[5];

        g2d.drawString("Total", currentX, y);
        y += lineHeight;

        // Linha separadora
        g2d.drawLine(margin, y, margin + pageWidth, y);
        y += 5;

        g2d.setFont(fontNormal);

        if (invoice.getProducts() != null && !invoice.getProducts().isEmpty()) {
            for (ProductSales ps : invoice.getProducts()) {
                currentX = margin;

                // Descrição
                String descricao = ps.getDescription() != null ? ps.getDescription() : "Produto/Serviço";
                if (descricao.length() > 25) {
                    descricao = descricao.substring(0, 22) + "...";
                }
                g2d.drawString(descricao, currentX, y);
                currentX += columnWidths[0];

                // Quantidade
                g2d.drawString(String.valueOf(ps.getQty()), currentX, y);
                currentX += columnWidths[1];

                // Preço Unitário
                g2d.drawString(formatCurrency(ps.getPrice()), currentX, y);
                currentX += columnWidths[2];

                // Cálculos fiscais
                BigDecimal valorTotal = ps.getPrice().multiply(BigDecimal.valueOf(ps.getQty()));
                BigDecimal valorIsento = BigDecimal.ZERO;
                BigDecimal valorTaxado = BigDecimal.ZERO;
                BigDecimal iva = BigDecimal.ZERO;

                if (ps.getTaxePercentage() != null && ps.getTaxePercentage().compareTo(BigDecimal.ZERO) > 0) {
                    valorTaxado = valorTotal;
                    iva = valorTotal.multiply(ps.getTaxePercentage())
                            .divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
                } else {
                    valorIsento = valorTotal;
                }

                // Valor Isento
                g2d.drawString(formatCurrency(valorIsento), currentX, y);
                currentX += columnWidths[3];

                // Valor Taxado
                g2d.drawString(formatCurrency(valorTaxado), currentX, y);
                currentX += columnWidths[4];

                // IVA
                g2d.drawString(formatCurrency(iva), currentX, y);
                currentX += columnWidths[5];

                // Total
                g2d.drawString(formatCurrency(valorTotal), currentX, y);

                y += lineHeight;

                // Verificar fim da página
                if (y > 650) {
                    g2d.drawString("... continua na próxima página ...", margin, y);
                    break;
                }
            }
        } else {
            g2d.drawString("Nenhum item registado", margin, y);
            y += lineHeight;
        }

        return y;
    }

    private int printFiscalSummary(Graphics2D g2d, int y, int lineHeight, int pageWidth, int margin) {
        g2d.setFont(fontHeader);
        g2d.drawString("RESUMO FISCAL E FINANCEIRO", margin, y);
        y += lineHeight;

        // Cálculos fiscais
        BigDecimal totalIsento = calcularTotalIsento();
        BigDecimal totalTaxado = calcularTotalTaxado();
        BigDecimal totalIVA = invoice.getTotalTaxe();
        BigDecimal saldo = invoice.getTotal().subtract(invoice.getPayTotal() != null ? invoice.getPayTotal() : BigDecimal.ZERO);

        int rightColumn = margin + pageWidth - 150;

        g2d.setFont(fontNormal);
        g2d.drawString("Total Valor Isento:", rightColumn, y);
        g2d.drawString(formatCurrency(totalIsento), rightColumn + 100, y);
        y += lineHeight;

        g2d.drawString("Total Valor Taxado:", rightColumn, y);
        g2d.drawString(formatCurrency(totalTaxado), rightColumn + 100, y);
        y += lineHeight;

        g2d.drawString("Total IVA Liquidado:", rightColumn, y);
        g2d.drawString(formatCurrency(totalIVA), rightColumn + 100, y);
        y += lineHeight;

        if (invoice.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
            g2d.drawString("Descontos:", rightColumn, y);
            g2d.drawString(formatCurrency(invoice.getDiscount()), rightColumn + 100, y);
            y += lineHeight;
        }

        g2d.setFont(fontBold);
        g2d.drawString("TOTAL DA FATURA:", rightColumn, y);
        g2d.drawString(formatCurrency(invoice.getTotal()), rightColumn + 100, y);
        y += lineHeight;

        g2d.setFont(fontNormal);
        g2d.drawString("Total Pago:", rightColumn, y);
        g2d.drawString(formatCurrency(invoice.getPayTotal()), rightColumn + 100, y);
        y += lineHeight;

        g2d.drawString("Saldo Pendente:", rightColumn, y);
        g2d.drawString(formatCurrency(saldo), rightColumn + 100, y);
        y += lineHeight;

        return y;
    }

    private void printAGTFooter(Graphics2D g2d, int y, int lineHeight, int pageWidth, int margin) {
        int footerY = 700; // Posição fixa no rodapé

        g2d.setFont(fontSmall);

        // Linha separadora
        g2d.drawLine(margin, footerY, margin + pageWidth, footerY);
        footerY += lineHeight;

        // Informações obrigatórias AGT
        g2d.drawString("*** DOCUMENTO FISCAL VÁLIDO PARA EFEITOS FISCAIS ***",
                margin + (pageWidth - g2d.getFontMetrics().stringWidth("*** DOCUMENTO FISCAL VÁLIDO PARA EFEITOS FISCAIS ***")) / 2,
                footerY);
        footerY += lineHeight;

        g2d.drawString("Conserve este documento por um período mínimo de 10 anos", margin, footerY);
        footerY += lineHeight;

        g2d.drawString("Em conformidade com o Código Geral Tributário de Angola - Lei nº 5/22", margin, footerY);
        footerY += lineHeight;

        // Observações
        if (invoice.getNote() != null && !invoice.getNote().trim().isEmpty()) {
            g2d.drawString("Observações: " + invoice.getNote(), margin, footerY);
            footerY += lineHeight;
        }

        // Timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        g2d.drawString("Documento gerado em: " + timestamp, margin, footerY);
    }

    // ==========================================================
    // 🔹 CÁLCULOS FISCAIS
    // ==========================================================
    private BigDecimal calcularTotalIsento() {
        if (invoice.getProducts() == null) {
            return BigDecimal.ZERO;
        }

        return invoice.getProducts().stream()
                .filter(ps -> ps.getTaxePercentage() == null || ps.getTaxePercentage().compareTo(BigDecimal.ZERO) == 0)
                .map(ps -> ps.getPrice().multiply(BigDecimal.valueOf(ps.getQty())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularTotalTaxado() {
        if (invoice.getProducts() == null) {
            return BigDecimal.ZERO;
        }

        return invoice.getProducts().stream()
                .filter(ps -> ps.getTaxePercentage() != null && ps.getTaxePercentage().compareTo(BigDecimal.ZERO) > 0)
                .map(ps -> ps.getPrice().multiply(BigDecimal.valueOf(ps.getQty())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ==========================================================
    // 🔹 UTILITÁRIOS
    // ==========================================================
    private String formatCurrency(BigDecimal value) {
        if (value == null) {
            return "0,00 AOA";
        }
        return currencyFormat.format(value);
    }

    private String formatInvoiceNumber(Invoices invoice) {
        return String.format("%s %d/%d", invoice.getPrefix(), invoice.getYear(), invoice.getNumber());
    }

//    private String getStatusDescricao(PaymentStatus status) {
//        switch (status) {
//            case 1:
//                return "RASCUNHO";
//            case 2:
//                return "NORMAL";
//            case 3:
//                return "REGULARIZADA";
//            case 4:
//                return "ANULADA";
//            default:
//                return "DESCONHECIDO";
//        }
//    }
}
