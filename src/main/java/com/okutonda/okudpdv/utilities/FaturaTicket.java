/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.utilities;

import com.okutonda.okudpdv.data.entities.Order;
import com.okutonda.okudpdv.data.entities.ProductOrder;
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
import java.util.List;

/**
 *
 * @author kenny
 */
public class FaturaTicket implements Printable {

//    private String nomeCliente;
//    private String data;
//    private double total;
//    private List<Item> itens;
    private final Order order;

//    public FaturaTicket(Order order) {
////        this.nomeCliente = nomeCliente;
////        this.data = data;
////        this.total = total;
////        this.itens = itens;
//        this.order = order;
//    }
//
//    @Override
//    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
//        if (page > 0) {
//            return NO_SUCH_PAGE;
//        }
//
//        Graphics2D g2d = (Graphics2D) g;
//        g2d.translate(pf.getImageableX(), pf.getImageableY());
//
//        int y = 10; // posição inicial Y
//
//        // Título
//        g2d.setFont(new Font("Serif", Font.BOLD, 14));
//        g2d.drawString("Fatura", 10, y);
//        y += 20;
//
//        // Detalhes do Cliente
//        g2d.setFont(new Font("Serif", Font.PLAIN, 10));
//        g2d.drawString("Nome: " + order.getClient().getName(), 10, y);
//        y += 15;
//        g2d.drawString("Data: " + order.getDatecreate(), 10, y);
//        y += 20;
//
//        // Cabeçalho dos itens
//        g2d.drawString("Item", 10, y);
//        g2d.drawString("Qtd", 60, y);
//        g2d.drawString("Preço", 100, y);
//        y += 10;
//
//        // Linha horizontal
//        g2d.drawLine(10, y, 180, y);
//        y += 10;
//
//        // Itens da fatura
//        for (ProductOrder item : order.getProducts()) {
//            g2d.drawString(item.getDescription(), 10, y);
//            g2d.drawString(String.valueOf(item.getQty()), 60, y);
//            g2d.drawString("R$ " + String.format("%.2f", item.getPrice()), 100, y);
//            y += 15;
//        }
//
//        // Linha horizontal
//        g2d.drawLine(10, y, 180, y);
//        y += 10;
//
//        // Total
//        g2d.drawString("Total: R$ " + String.format("%.2f", order.getTotal()), 10, y);
//        y += 20;
//
//        return PAGE_EXISTS;
//    }
//    private String nomeCliente;
//    private String dataFatura;
//    private double total;
//    private List<Item> itens;
    // Informações adicionais
    private String nomeEmpresa;
    private String enderecoEmpresa;
    private String telefoneEmpresa;
    private String numeroFatura;
    private String nomeVendedor;

    public FaturaTicket(Order order) {
        this.order = order;
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
        g2d.drawString("nomeEmpresa", 10, y);
        y += 15;
        g2d.setFont(new Font("Serif", Font.PLAIN, 10));
        g2d.drawString("enderecoEmpresa", 10, y);
        y += 15;
        g2d.drawString("Tel: " + "telefoneEmpresa", 10, y);
        y += 25;

        // Informações da Fatura
        g2d.setFont(new Font("Serif", Font.PLAIN, 10));
        g2d.drawString("Fatura Nº: " + order.getPrefix() + order.getNumber(), 10, y);
        y += 15;
        g2d.drawString("Data: " + order.getDatecreate(), 10, y);
        y += 15;
        g2d.drawString("Vendedor: " + order.getSeller().getName(), 10, y);
        y += 25;

        // Linha horizontal
        g2d.drawLine(10, y, (int) width - 10, y);
        y += 10;

        // Título da Fatura
        g2d.setFont(new Font("Serif", Font.BOLD, 12));
        g2d.drawString("Fatura", 10, y);
        y += 20;

        // Cabeçalho dos Itens
        g2d.setFont(new Font("Serif", Font.PLAIN, 10));
        g2d.drawString("Item", 10, y);
        g2d.drawString("Qtd", 60, y);
        g2d.drawString("Unidade", 100, y);
        g2d.drawString("Preço", 140, y);
        g2d.drawString("Imposto", 180, y);
        y += 10;

        // Linha horizontal
        g2d.drawLine(10, y, (int) width - 10, y);
        y += 10;

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalImposto = BigDecimal.ZERO;

        // Itens da Fatura
        for (ProductOrder item : order.getProducts()) {

//            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(line.getQty()));
            BigDecimal precoTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQty()));
//            BigDecimal precoTotal = item.getQty() * item.getPrice();

//            BigDecimal imposto = precoTotal * 7 / 100;
            BigDecimal imposto = precoTotal
                    .multiply(BigDecimal.valueOf(7)) // multiplica por 7
                    .divide(BigDecimal.valueOf(100));   // divide por 100
//            double imposto = precoTotal * item.getTaxaImposto() / 100;
//            subtotal += precoTotal;
//            totalImposto += imposto;
            subtotal = subtotal.add(precoTotal);
            totalImposto = totalImposto.add(imposto);

            g2d.drawString(item.getDescription(), 10, y);
            g2d.drawString(String.valueOf(item.getQty()), 60, y);
            g2d.drawString("Unidade", 100, y);
//            g2d.drawString(item.getUnit(), 100, y);
            g2d.drawString("R$ " + String.format("%.2f", item.getPrice()), 140, y);
            g2d.drawString("R$ " + String.format("%.2f", imposto), 180, y);
            y += 15;

            // Razão para isenção de imposto
//            if (item.getTaxaImposto() == 0) {
//                g2d.drawString("Isento: " + item.getRazaoIsencao(), 10, y);
//                y += 15;
//            }
        }

        // Linha horizontal
        g2d.drawLine(10, y, (int) width - 10, y);
        y += 10;

        // Subtotal
        g2d.drawString("Subtotal: R$ " + String.format("%.2f", subtotal), 10, y);
        y += 15;

        // Total de Impostos
        g2d.drawString("Total de Impostos: R$ " + String.format("%.2f", totalImposto), 10, y);
        y += 15;

        // Total
        BigDecimal totalGeral = subtotal.add(totalImposto);
        g2d.setFont(new Font("Serif", Font.BOLD, 12));
        g2d.drawString("Total: R$ " + String.format("%.2f", totalGeral.doubleValue()), 10, y);
//        g2d.drawString("Total: R$ " + String.format("%.2f", subtotal + totalImposto), 10, y);
        y += 20;

        // Linha horizontal
        g2d.drawLine(10, y, (int) width - 10, y);
        y += 10;

        // Cabeçalho da Tabela de Razões de Isenção
        g2d.setFont(new Font("Serif", Font.BOLD, 10));
        g2d.drawString("Razões de Isenção de Imposto", 10, y);
        y += 15;

        // Cabeçalho da Tabela de Razões de Isenção
        g2d.setFont(new Font("Serif", Font.PLAIN, 10));
        g2d.drawString("Item", 10, y);
        g2d.drawString("Razão de Isenção", 60, y);
        y += 10;

        // Linha horizontal
        g2d.drawLine(10, y, (int) width - 10, y);
        y += 10;

        // Razões de Isenção
        for (ProductOrder item : order.getProducts()) {
            if (item.getTaxePercentage() == BigDecimal.ZERO) {
                g2d.drawString(item.getDescription(), 10, y);
                g2d.drawString(item.getReasonTax(), 60, y);
                y += 15;
            }
        }

        // Linha horizontal
        g2d.drawLine(10, y, (int) width - 10, y);
        y += 10;

        // Rodapé
        g2d.setFont(new Font("Serif", Font.PLAIN, 10));
        g2d.drawString("Obrigado pela preferência!", 10, y);
        y += 15;
        g2d.drawString("Visite nosso site: www.exemplo.com", 10, y);
        y += 10;
        g2d.drawString("Siga-nos nas redes sociais", 10, y);

        return PAGE_EXISTS;
    }
}
