/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.helpers;

import com.okutonda.okudpdv.data.entities.Invoices;
import com.okutonda.okudpdv.data.entities.ProductSales;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.math.BigDecimal;

/**
 * Classe para impressão de tickets de Faturas (Invoices)
 *
 * @author kenny
 */
public class FaturaTicketInvoice implements Printable {

    private final Invoices invoice;

    public FaturaTicketInvoice(Invoices invoice) {
        this.invoice = invoice;
    }

    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
        if (page > 0) {
            return NO_SUCH_PAGE;
        }

        // Configurar tamanho da folha para 80mm de largura
        Paper paper = new Paper();
        double width = 80 * 2.83465; // 80mm em pontos (1mm = 2.83465 pontos)
        double height = pf.getHeight(); // altura conforme necessário
        paper.setSize(width, height);
        paper.setImageableArea(0, 0, width, height);
        pf.setPaper(paper);

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        int y = 10; // posição inicial Y

        // Cabeçalho da Empresa
        g2d.setFont(new Font("Serif", Font.BOLD, 14));
        g2d.drawString("MINHA EMPRESA", 10, y);
        y += 15;
        g2d.setFont(new Font("Serif", Font.PLAIN, 10));
        g2d.drawString("Endereço da Empresa", 10, y);
        y += 15;
        g2d.drawString("Tel: (XX) XXXX-XXXX", 10, y);
        y += 25;

        // Informações da Fatura
        g2d.setFont(new Font("Serif", Font.PLAIN, 10));
        g2d.drawString("Fatura Nº: " + invoice.getPrefix() + "/" + invoice.getNumber(), 10, y);
        y += 15;
        g2d.drawString("Data: " + invoice.getIssueDate(), 10, y);
        y += 15;

        if (invoice.getSeller() != null) {
            g2d.drawString("Vendedor: " + invoice.getSeller().getName(), 10, y);
            y += 15;
        }

        if (invoice.getClient() != null) {
            g2d.drawString("Cliente: " + invoice.getClient().getName(), 10, y);
            y += 15;
        }

        y += 10;

        // Linha horizontal
        g2d.drawLine(10, y, (int) width - 10, y);
        y += 10;

        // Título da Fatura
        g2d.setFont(new Font("Serif", Font.BOLD, 12));
        g2d.drawString("FATURA - " + invoice.getPrefix(), 10, y);
        y += 20;

        // Cabeçalho dos Itens
        g2d.setFont(new Font("Serif", Font.PLAIN, 10));
        g2d.drawString("Descrição", 10, y);
        g2d.drawString("Qtd", 120, y);
        g2d.drawString("Preço", 150, y);
        g2d.drawString("Total", 200, y);
        y += 10;

        // Linha horizontal
        g2d.drawLine(10, y, (int) width - 10, y);
        y += 10;

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalImposto = BigDecimal.ZERO;

        // Itens da Fatura (se houver)
        if (invoice.getProducts() != null && !invoice.getProducts().isEmpty()) {
            for (ProductSales item : invoice.getProducts()) {
                BigDecimal precoTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQty()));
                subtotal = subtotal.add(precoTotal);

                // Calcular imposto se houver
                if (item.getTaxePercentage() != null && item.getTaxePercentage().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal imposto = precoTotal.multiply(item.getTaxePercentage())
                            .divide(BigDecimal.valueOf(100));
                    totalImposto = totalImposto.add(imposto);
                }

                // Quebrar descrição longa
                String descricao = item.getDescription();
                if (descricao.length() > 20) {
                    descricao = descricao.substring(0, 20) + "...";
                }

                g2d.drawString(descricao, 10, y);
                g2d.drawString(String.valueOf(item.getQty()), 120, y);
                g2d.drawString("R$ " + String.format("%.2f", item.getPrice()), 150, y);
                g2d.drawString("R$ " + String.format("%.2f", precoTotal), 200, y);
                y += 15;
            }
        } else {
            // Se não há itens, mostrar mensagem
            g2d.drawString("*** SEM ITENS ***", 10, y);
            y += 15;
        }

        // Linha horizontal
        g2d.drawLine(10, y, (int) width - 10, y);
        y += 10;

        // Subtotal
        g2d.drawString("Subtotal: R$ " + String.format("%.2f", subtotal), 10, y);
        y += 15;

        // Total de Impostos
        if (totalImposto.compareTo(BigDecimal.ZERO) > 0) {
            g2d.drawString("Impostos: R$ " + String.format("%.2f", totalImposto), 10, y);
            y += 15;
        }

        // Desconto
        if (invoice.getDiscount() != null && invoice.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
            g2d.drawString("Desconto: R$ " + String.format("%.2f", invoice.getDiscount()), 10, y);
            y += 15;
        }

        // Total
        g2d.setFont(new Font("Serif", Font.BOLD, 12));
        BigDecimal totalGeral = invoice.getTotal();
        g2d.drawString("TOTAL: R$ " + String.format("%.2f", totalGeral), 10, y);
        y += 20;

        // Status da Fatura
        g2d.setFont(new Font("Serif", Font.PLAIN, 10));
        g2d.drawString("Status: " + getStatusDescricao(invoice.getStatus()), 10, y);
        y += 15;

        // Valor Pago
        if (invoice.getPayTotal() != null && invoice.getPayTotal().compareTo(BigDecimal.ZERO) > 0) {
            g2d.drawString("Valor Pago: R$ " + String.format("%.2f", invoice.getPayTotal()), 10, y);
            y += 15;

            // Saldo Pendente
            BigDecimal saldoPendente = totalGeral.subtract(invoice.getPayTotal());
            if (saldoPendente.compareTo(BigDecimal.ZERO) > 0) {
                g2d.drawString("Saldo Pendente: R$ " + String.format("%.2f", saldoPendente), 10, y);
                y += 15;
            }
        }

        // Linha horizontal
        g2d.drawLine(10, y, (int) width - 10, y);
        y += 10;

        // Observações
        if (invoice.getNote() != null && !invoice.getNote().trim().isEmpty()) {
            g2d.drawString("Observações:", 10, y);
            y += 15;

            String observacao = invoice.getNote();
            if (observacao.length() > 30) {
                // Quebrar observação longa
                String[] palavras = observacao.split(" ");
                StringBuilder linha = new StringBuilder();
                for (String palavra : palavras) {
                    if (linha.length() + palavra.length() + 1 > 30) {
                        g2d.drawString(linha.toString(), 10, y);
                        y += 15;
                        linha = new StringBuilder();
                    }
                    linha.append(palavra).append(" ");
                }
                if (linha.length() > 0) {
                    g2d.drawString(linha.toString(), 10, y);
                    y += 15;
                }
            } else {
                g2d.drawString(observacao, 10, y);
                y += 15;
            }
        }

        // Rodapé
        g2d.setFont(new Font("Serif", Font.PLAIN, 8));
        g2d.drawString("Obrigado pela preferência!", 10, y);
        y += 10;
        g2d.drawString("Documento gerado em: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), 10, y);

        return PAGE_EXISTS;
    }

    private String getStatusDescricao(Integer status) {
        if (status == null) {
            return "Desconhecido";
        }
        switch (status) {
            case 1:
                return "Pendente";
            case 2:
                return "Emitida";
            case 3:
                return "Paga";
            case 4:
                return "Anulada";
            default:
                return "Desconhecido";
        }
    }
}
