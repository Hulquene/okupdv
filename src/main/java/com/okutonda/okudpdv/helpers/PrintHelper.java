/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.helpers;

import com.okutonda.okudpdv.data.dao.OrderDao;
import com.okutonda.okudpdv.data.entities.Invoices;
import com.okutonda.okudpdv.data.entities.Order;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JOptionPane;

/**
 * Utilitário para impressão e formatação de documentos
 *
 * @author kenny
 */
public class PrintHelper {

    /**
     * Obtém o prefixo do documento baseado no tipo
     */
    public static String getDocumentPrefix(String documentType) {
        return "FR"; // Fatura/Recibo
    }

    /**
     * Formata o número do documento com prefixo, ano e número
     */
    public static String formatDocumentNumber(int documentId) {
        OrderDao dao = new OrderDao();
        Order order = dao.findById(documentId).orElse(null);

        if (order != null) {
            return formatDocumentNumber(order.getNumber(), order.getYear(), order.getPrefix());
        }
        return "Documento não encontrado";
    }

    /**
     * Formata o número do documento com os componentes fornecidos
     */
    public static String formatDocumentNumber(int number, int year, String prefix) {
        return String.format("%s %d/%d", prefix, year, number);
    }

    /**
     * Imprime ticket da ordem em impressora térmica (formato 80mm)
     */
    public static void printThermalTicket(Order order) throws PrinterException {
        if (order == null) {
            throw new IllegalArgumentException("Ordem não pode ser nula");
        }

        PrinterJob job = PrinterJob.getPrinterJob();
        FaturaTicket ticket = new FaturaTicket(order);

        // Configurar formato para impressora térmica (80mm)
        PageFormat pageFormat = job.defaultPage();
        Paper paper = createThermalPaper();
        pageFormat.setPaper(paper);

        job.setPrintable(ticket, pageFormat);

        // Selecionar impressora automaticamente ou permitir escolha
        PrintService selectedService = selectPrinter();
        if (selectedService != null) {
            job.setPrintService(selectedService);

            try {
                job.print();
                System.out.println("✅ Ticket impresso com sucesso: " + formatDocumentNumber(order.getNumber(), order.getYear(), order.getPrefix()));
            } catch (PrinterException e) {
                System.err.println("❌ Erro ao imprimir ticket: " + e.getMessage());
                throw e;
            }
        }
    }

    /**
     * Imprime documento com diálogo de seleção de impressora
     */
    public static boolean printWithDialog(Order order) {
        if (order == null) {
            JOptionPane.showMessageDialog(null, "Ordem não pode ser nula", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        FaturaTicket ticket = new FaturaTicket(order);
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(ticket);

        // Exibir diálogo de impressão
        boolean confirmPrint = job.printDialog();
        if (confirmPrint) {
            try {
                job.print();
                System.out.println("✅ Documento impresso com sucesso: " + formatDocumentNumber(order.getNumber(), order.getYear(), order.getPrefix()));
                return true;
            } catch (PrinterException e) {
                System.err.println("❌ Erro ao imprimir documento: " + e.getMessage());
                JOptionPane.showMessageDialog(null,
                        "Erro ao imprimir: " + e.getMessage(),
                        "Erro de Impressão",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }

    /**
     * Cria papel no formato térmico (80mm)
     */
    private static Paper createThermalPaper() {
        Paper paper = new Paper();
        double width = 80 * 72 / 25.4; // 80mm para pontos
        double height = 297 * 72 / 25.4; // Altura A4 para garantir espaço suficiente
        paper.setSize(width, height);
        paper.setImageableArea(5, 5, width - 10, height - 10); // Margens
        return paper;
    }

    /**
     * Seleciona a impressora térmica automaticamente ou permite escolha
     */
    private static PrintService selectPrinter() {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

        if (printServices.length == 0) {
            System.err.println("❌ Nenhuma impressora encontrada.");
            return null;
        }

        // Tentar encontrar impressora térmica automaticamente
        PrintService thermalPrinter = findThermalPrinter(printServices);
        if (thermalPrinter != null) {
            System.out.println("✅ Impressora térmica selecionada: " + thermalPrinter.getName());
            return thermalPrinter;
        }

        // Se não encontrar, usar primeira impressora disponível
        System.out.println("ℹ️  Usando primeira impressora disponível: " + printServices[0].getName());
        return printServices[0];
    }

    /**
     * Encontra impressora térmica pelo nome
     */
    private static PrintService findThermalPrinter(PrintService[] services) {
        // Palavras-chave comuns em impressoras térmicas
        String[] thermalKeywords = {"thermal", "termica", "ticket", "cupom", "80mm", "58mm", "POS"};

        for (PrintService service : services) {
            String serviceName = service.getName().toLowerCase();

            for (String keyword : thermalKeywords) {
                if (serviceName.contains(keyword.toLowerCase())) {
                    return service;
                }
            }
        }
        return null;
    }

    /**
     * Verifica se há impressoras disponíveis
     */
    public static boolean hasAvailablePrinters() {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        return printServices.length > 0;
    }

    /**
     * Lista todas as impressoras disponíveis
     */
    public static void listAvailablePrinters() {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

        if (printServices.length == 0) {
            System.out.println("Nenhuma impressora disponível.");
            return;
        }

        System.out.println("📋 Impressoras disponíveis:");
        for (int i = 0; i < printServices.length; i++) {
            System.out.println((i + 1) + ". " + printServices[i].getName());
        }
    }

    /**
     * Imprime ticket da fatura em impressora térmica (formato 80mm)
     */
    /**
     * Imprime ticket da fatura em impressora térmica (formato 80mm)
     */
    public static void printThermalInvoice(Invoices invoice) throws PrinterException {
        if (invoice == null) {
            throw new IllegalArgumentException("Fatura não pode ser nula");
        }

        PrinterJob job = PrinterJob.getPrinterJob();
        FaturaTicketInvoice ticket = new FaturaTicketInvoice(invoice); // ✅ Usa a nova classe

        // Configurar formato para impressora térmica (80mm)
        PageFormat pageFormat = job.defaultPage();
        Paper paper = createThermalPaper();
        pageFormat.setPaper(paper);

        job.setPrintable(ticket, pageFormat);

        // Selecionar impressora automaticamente ou permitir escolha
        PrintService selectedService = selectPrinter();
        if (selectedService != null) {
            job.setPrintService(selectedService);

            try {
                job.print();
                System.out.println("✅ Fatura impressa com sucesso: " + formatDocumentNumber(invoice.getNumber(), invoice.getYear(), invoice.getPrefix()));
            } catch (PrinterException e) {
                System.err.println("❌ Erro ao imprimir fatura: " + e.getMessage());
                throw e;
            }
        }
    }

    /**
     * Imprime fatura com diálogo de seleção de impressora
     */
    public static boolean printInvoiceWithDialog(Invoices invoice) {
        if (invoice == null) {
            JOptionPane.showMessageDialog(null, "Fatura não pode ser nula", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        FaturaTicketInvoice ticket = new FaturaTicketInvoice(invoice); // ✅ Usa a nova classe
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(ticket);

        // Exibir diálogo de impressão
        boolean confirmPrint = job.printDialog();
        if (confirmPrint) {
            try {
                job.print();
                System.out.println("✅ Fatura impressa com sucesso: " + formatDocumentNumber(invoice.getNumber(), invoice.getYear(), invoice.getPrefix()));
                return true;
            } catch (PrinterException e) {
                System.err.println("❌ Erro ao imprimir fatura: " + e.getMessage());
                JOptionPane.showMessageDialog(null,
                        "Erro ao imprimir: " + e.getMessage(),
                        "Erro de Impressão",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }

    /**
     * Formata o número do documento de invoice
     */
    public static String formatInvoiceNumber(Invoices invoice) {
        if (invoice == null) {
            return "N/A";
        }
        return formatDocumentNumber(invoice.getNumber(), invoice.getYear(), invoice.getPrefix());
    }
}

//// Imprimir automaticamente em impressora térmica
//PrintHelper.printThermalTicket(order);
//
//// Imprimir com diálogo de seleção
//boolean success = PrintHelper.printWithDialog(order);
//
//// Verificar impressoras
//if (PrintHelper.hasAvailablePrinters()) {
//    PrintHelper.listAvailablePrinters();
//}
