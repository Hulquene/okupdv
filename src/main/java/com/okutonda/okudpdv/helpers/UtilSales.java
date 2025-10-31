/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.helpers;

import com.okutonda.okudpdv.data.dao.OrderDao;
import com.okutonda.okudpdv.data.entities.Order;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

/**
 *
 * @author kenny
 */
public class UtilSales {

    public static String getPrefix(String type) {
        return "FR";
    }

    public static String FormatedNumberPrefix(int id, String type) {
        String format = "";
        OrderDao dao = new OrderDao();
        Order obj = dao.findById(id).orElse(null);
        if (obj != null) {
            format = obj.getPrefix() + " " + obj.getYear() + "/" + obj.getNumber();
        }
        return format;
    }

    public static String FormatedNumberPrefix2(int number, int year, String prefix) {
        return prefix + " " + year + "/" + number;
    }

    public static void PrintOrderTicket(Order order) throws PrinterException {

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new FaturaTicket(order));

        PageFormat pf = job.defaultPage();
        Paper paper = new Paper();
        double width = 80 * 72 / 25.4; // 80mm to points
        double height = 200 * 72 / 25.4; // 200mm to points (adjust as needed)
        paper.setSize(width, height);
        paper.setImageableArea(0, 0, width, height);
        pf.setPaper(paper);

        job.setPrintable(new FaturaTicket(order), pf);

        // Procurar impressoras disponíveis
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        if (printServices.length == 0) {
            System.out.println("Nenhuma impressora encontrada.");
            return;
        }

        // Listar impressoras disponíveis
        System.out.println("Selecione uma impressora:");
        for (int i = 0; i < printServices.length; i++) {
            System.out.println((i + 1) + ": " + printServices[i].getName());
        }

//        String value = JOptionPane.showInputDialog(null,"Selecione a impressora:");
        System.out.println("imprimir");
        // Selecionar impressora
        int selectedPrinterIndex = 2; // Aqui você pode implementar lógica para selecionar a impressora desejada
        PrintService selectedService = printServices[selectedPrinterIndex];
        job.setPrintService(selectedService);

        try {
            job.print();
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    public static void print(Order order) {
//          String nomeCliente = "João Silva";
//        String data = "07/08/2024";
//        double total = 200.00;

//        List<Item> itens = new ArrayList<>();
//        itens.add(new Item("Item 1", 2, 50.00));
//        itens.add(new Item("Item 2", 1, 100.00));
//        PrinterJob job = PrinterJob.getPrinterJob();
//        job.setPrintable(new FaturaTicket(order));
//
//        PageFormat pf = job.defaultPage();
//        Paper paper = new Paper();
//        double width = 80 * 72 / 25.4; // 80mm to points
//        double height = 200 * 72 / 25.4; // 200mm to points (adjust as needed)
//        paper.setSize(width, height);
//        paper.setImageableArea(0, 0, width, height);
//        pf.setPaper(paper);
//
//        job.setPrintable(new FaturaTicket(order), pf);
//
//        boolean doPrint = job.printDialog();
//        if (doPrint) {
//            try {
//                job.print();
//            } catch (PrinterException e) {
//                e.printStackTrace();
//            }
//        }
// Criando uma instância da FaturaTicket
        FaturaTicket fatura = new FaturaTicket(order);

        // Configurando o trabalho de impressão
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(fatura);

        // Exibindo a caixa de diálogo padrão de impressão
        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
            } catch (PrinterException e) {
                System.err.println("Erro ao imprimir: " + e.getMessage());
            }
        }
    }
}
