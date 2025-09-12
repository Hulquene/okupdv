/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.dao.*;
import com.okutonda.okudpdv.models.*;

import javax.xml.stream.*;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author hr
 */
public class ExportSaftFat {

    private final SaftFatDao saftDao = new SaftFatDao();
    private final SettingsDao settingsDao = new SettingsDao();
    private final OrderDao orderDao = new OrderDao();
    private final ProductOrderDao productOrderDao = new ProductOrderDao();
    private final ProductDao productDao = new ProductDao();
    private final ClientDao clientDao = new ClientDao();

    /**
     * Gera o ficheiro e grava o log da exportação (cada DAO trata a sua
     * conexão).
     */
    public long export(LocalDate start, LocalDate end, Path output) throws Exception {
        try {
            generateSaftXml(start, end, output);
            return saftDao.insertExport(Date.valueOf(start), Date.valueOf(end), output.toString(), "SUCCESS", null);
        } catch (Exception e) {
            saftDao.insertExport(Date.valueOf(start), Date.valueOf(end), output.toString(), "FAILED", e.getMessage());
            throw e;
        }
    }

    /**
     * Geração do SAF-T usando apenas DAOs.
     */
    private void generateSaftXml(LocalDate start, LocalDate end, Path output) throws Exception {
        XMLOutputFactory xof = XMLOutputFactory.newInstance();
        try (OutputStream os = Files.newOutputStream(output)) {
            XMLStreamWriter w = xof.createXMLStreamWriter(os, "UTF-8");

            w.writeStartDocument("UTF-8", "1.0");
            w.writeStartElement("AuditFile"); // adiciona xmlns/xsi quando tiveres o XSD oficial

            writeHeader(w, start, end);
            writeMasterFiles(w, start, end);
            writeSourceDocuments(w, start, end);

            w.writeEndElement(); // </AuditFile>
            w.writeEndDocument();
            w.flush();
            w.close();
        }
    }

    // ===================== HEADER =====================
    private void writeHeader(XMLStreamWriter w, LocalDate start, LocalDate end) throws Exception {
        w.writeStartElement("Header");
        writeTag(w, "AuditFileVersion", "1.04"); // confirmar com AGT
        writeTag(w, "CompanyID", settingsDao.getValue("company_nif"));
        writeTag(w, "TaxRegistrationNumber", settingsDao.getValue("company_nif"));
        writeTag(w, "CompanyName", settingsDao.getValue("company_name"));
        writeTag(w, "BusinessName", settingsDao.getValue("company_name"));
        writeTag(w, "CompanyAddress", settingsDao.getValue("company_address"));
        writeTag(w, "FiscalYear", String.valueOf(start.getYear()));
        writeTag(w, "StartDate", start.toString());
        writeTag(w, "EndDate", end.toString());
        writeTag(w, "CurrencyCode", "AOA");
        writeTag(w, "DateCreated", java.time.LocalDateTime.now().toString());
        writeTag(w, "SoftwareCertificateNumber", "0");
        writeTag(w, "ProductID", "Okudpdv/Okutonda");
        writeTag(w, "ProductVersion", "1.0.0");
        w.writeEndElement();
    }

    // ===================== MASTERFILES =====================
    private void writeMasterFiles(XMLStreamWriter w, LocalDate start, LocalDate end) throws Exception {
        w.writeStartElement("MasterFiles");

        // ORDERS no período
        List<Order> orders = orderDao.filterDate(start, end, "");
        // CLIENTES distintos presentes nas vendas
        Set<Integer> customerIds = orders.stream()
                .map(o -> o.getClient() != null ? o.getClient().getId() : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for (Integer cid : customerIds) {
            Clients c = clientDao.getId(cid);
            if (c == null) {
                continue;
            }
            w.writeStartElement("Customer");
            writeTag(w, "CustomerID", String.valueOf(c.getId()));
            writeTag(w, "AccountID", String.valueOf(c.getId()));
            writeTag(w, "CustomerTaxID", nz(c.getNif()));
            writeTag(w, "CompanyName", nz(c.getName()));
            // Se o XSD exigir estrutura de endereço, depois quebramos em sub-tags:
            writeTag(w, "BillingAddress", nz(c.getAddress()));
            w.writeEndElement();
        }

        // PRODUTOS distintos presentes nas vendas
        Set<Integer> productIds = new LinkedHashSet<>();
        for (Order o : orders) {
            List<ProductOrder> lines = productOrderDao.listProductFromOrderId(o.getId());
            if (lines == null) {
                continue;
            }
            for (ProductOrder l : lines) {
                if (l.getProduct() != null) {
                    productIds.add(l.getProduct().getId());
                }
            }
        }
        
        for (Integer pid : productIds) {
            Product p = productDao.getId(pid);
            if (p == null) {
                continue;
            }
            w.writeStartElement("Product");
            writeTag(w, "ProductType", "P");
            writeTag(w, "ProductCode", nz(p.getCode()));
            writeTag(w, "ProductGroup", "");
            writeTag(w, "ProductDescription", nz(p.getDescription()));
//            writeTag(w, "UnitOfMeasure", nz(p.getUnit));
            writeTag(w, "UnitPrice", String.valueOf(p.getPrice() != null ? p.getPrice() : 0));
            w.writeEndElement();
        }

        w.writeEndElement(); // </MasterFiles>
    }

    // ===================== SOURCE DOCUMENTS =====================
    private void writeSourceDocuments(XMLStreamWriter w, LocalDate start, LocalDate end) throws Exception {
        w.writeStartElement("SourceDocuments");

        // -------- VENDAS (SalesInvoices) --------
        w.writeStartElement("SalesInvoices");
        BigDecimal totalCredit = BigDecimal.ZERO;

        List<Order> orders = orderDao.filterDate(start, end, "");
        for (Order o : orders) {
            String invoiceNo = (o.getPrefix() == null ? "" : o.getPrefix() + " ") + o.getNumber();
            int customerId = (o.getClient() != null ? o.getClient().getId() : 0);

            w.writeStartElement("Invoice");
            writeTag(w, "InvoiceNo", invoiceNo);
            writeTag(w, "InvoiceDate", safeDate10(o.getDatecreate()));
            writeTag(w, "CustomerID", String.valueOf(customerId));
            writeTag(w, "InvoiceType", inferInvoiceType(o.getPrefix())); // FT/FR/NC/ND (fallback FR)

            // Linhas
            List<ProductOrder> lines = productOrderDao.listProductFromOrderId(o.getId());
            writeInvoiceLines(w, lines);

            // Totais (BigDecimal para precisão)
            BigDecimal gross = bd(o.getTotal());
            BigDecimal net = bd(o.getSubTotal());
            BigDecimal tax = gross.subtract(net).max(BigDecimal.ZERO);

            writeTag(w, "GrossTotal", gross.toString());
            writeTag(w, "NetTotal", net.toString());
            writeTag(w, "TaxPayable", tax.toString());

            w.writeEndElement(); // </Invoice>

            totalCredit = totalCredit.add(gross);
        }

        writeTag(w, "NumberOfEntries", String.valueOf(orders.size()));
        writeTag(w, "TotalCredit", totalCredit.toString());
        writeTag(w, "TotalDebit", "0");

        w.writeEndElement(); // </SalesInvoices>

        // (Se precisares, adicionar aqui Compras/Payments/MovementOfGoods conforme o guia AGT)
        w.writeEndElement(); // </SourceDocuments>
    }

    private void writeInvoiceLines(XMLStreamWriter w, List<ProductOrder> lines) throws Exception {
        if (lines == null) {
            return;
        }
        for (ProductOrder line : lines) {
            w.writeStartElement("Line");

            int pid = (line.getProduct() != null ? line.getProduct().getId() : 0);
            writeTag(w, "ProductCode", String.valueOf(pid));
            writeTag(w, "ProductDescription", nz(line.getDescription()));
            writeTag(w, "Quantity", String.valueOf(line.getQty()));
            writeTag(w, "UnitPrice", String.valueOf(nzD(line.getPrice())));

            // Impostos
            w.writeStartElement("Tax");
            String taxType = (nz(line.getTaxeName()).isEmpty() ? "IVA" : line.getTaxeName());
            writeTag(w, "TaxType", taxType);                 // IVA / ISE / NS …
            writeTag(w, "TaxCode", nz(line.getTaxeCode()));  // M00, M02, M10 …
            writeTag(w, "TaxPercentage", String.valueOf(nzD(line.getTaxePercentage())));
            w.writeEndElement(); // </Tax>

            BigDecimal credit = BigDecimal.valueOf(nzD(line.getPrice()))
                    .multiply(BigDecimal.valueOf(line.getQty()));
            writeTag(w, "CreditAmount", credit.toString());

            w.writeEndElement(); // </Line>
        }
    }

    // ===================== HELPERS =====================
    private static void writeTag(XMLStreamWriter w, String name, String value) throws XMLStreamException {
        w.writeStartElement(name);
        w.writeCharacters(value == null ? "" : value);
        w.writeEndElement();
    }

    private static String nz(String s) {
        return s == null ? "" : s;
    }

    private static double nzD(Double d) {
        return d == null ? 0.0 : d;
    }

    private static BigDecimal bd(Object o) {
        if (o == null) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(o.toString());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private static String safeDate10(String s) {
        if (s == null) {
            return "";
        }
        return s.length() >= 10 ? s.substring(0, 10) : s; // pega "YYYY-MM-DD"
    }

    private static String inferInvoiceType(String prefix) {
        if (prefix == null) {
            return "FR";
        }
        String p = prefix.trim().toUpperCase();
        if (p.contains("FT")) {
            return "FT";
        }
        if (p.contains("FR")) {
            return "FR";
        }
        if (p.contains("NC")) {
            return "NC";
        }
        if (p.contains("ND")) {
            return "ND";
        }
        return "FR";
    }
}
