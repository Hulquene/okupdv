package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.entities.Payment;
import com.okutonda.okudpdv.data.entities.Clients;
import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.SaftFat;
import com.okutonda.okudpdv.data.entities.ProductSales;
import com.okutonda.okudpdv.data.entities.Order;
import com.okutonda.okudpdv.data.entities.Invoices;
import com.okutonda.okudpdv.data.dao.ProductDao;
import com.okutonda.okudpdv.data.dao.ProductOrderDao;
import com.okutonda.okudpdv.data.dao.ClientDao;
import com.okutonda.okudpdv.data.dao.PaymentDao;
import com.okutonda.okudpdv.data.dao.OrderDao;
import com.okutonda.okudpdv.data.dao.InvoiceDao;
import com.okutonda.okudpdv.data.dao.SaftFatDao;
import com.okutonda.okudpdv.helpers.UserSession;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Locale;

/**
 * Geração do ficheiro SAF-T (AO) atualizado para incluir Orders e Invoices
 */
public class SaftController {

    private final OptionController optionController = new OptionController();
    private final SaftFatDao saftDao = new SaftFatDao();
    private final OrderDao orderDao = new OrderDao();
    private final InvoiceDao invoiceDao = new InvoiceDao();
    private final ProductOrderDao productOrderDao = new ProductOrderDao();
    private final ProductDao productDao = new ProductDao();
    private final ClientDao clientDao = new ClientDao();
    private final PaymentDao paymentDao = new PaymentDao();
    private final UserSession session = UserSession.getInstance();

    // ==========================================================
    // 🔹 CRUD OPERATIONS
    // ==========================================================
    public List<SaftFat> get() {
        return saftDao.getAll();
    }

    public SaftFat getId(long id) {
        return saftDao.findById(id).orElse(null);
    }

    public List<SaftFat> filter(String txt) {
        return saftDao.filter(txt);
    }

    public List<SaftFat> filterCreatedBetween(LocalDate from, LocalDate to) {
        return saftDao.filterByCreatedAt(from, to);
    }

    public List<SaftFat> filterPeriodOverlap(LocalDate from, LocalDate to) {
        return saftDao.filterByPeriod(from, to);
    }

    public List<SaftFat> filterByUser(Integer userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        return saftDao.findByUserId(userId.longValue());
    }

    public List<SaftFat> findByStatus(String status) {
        return saftDao.findByStatus(status);
    }

    public boolean existsForPeriod(LocalDate start, LocalDate end) {
        return saftDao.existsForPeriod(start, end);
    }

    public SaftFat findLastExport() {
        return saftDao.findLastExport().orElse(null);
    }

    public List<SaftFat> findOverlappingExports(LocalDate start, LocalDate end) {
        return saftDao.findOverlappingExports(start, end);
    }

    public void delete(long id) {
        saftDao.delete(id);
    }

    // ==========================================================
    // 🔹 EXPORT OPERATIONS - ATUALIZADO COM UPDATE/SUBSTITUIÇÃO
    // ==========================================================
    public long export(LocalDate start, LocalDate end, Path output) throws Exception {
        String status = "SUCCESS";
        String notes = null;
        SaftFat export;

        try {
            // 🔹 VERIFICA SE JÁ EXISTE EXPORT PARA O PERÍODO E ATUALIZA
            SaftFat existingExport = findExistingExport(start, end);

            // 🔹 SEMPRE GERA O ARQUIVO XML (tanto para novo quanto para atualização)
            generateSaftXml(start, end, output);

            if (existingExport != null) {
                // 🔹 ATUALIZA O REGISTRO EXISTENTE
                System.out.println("🔄 Atualizando export SAF-T existente para o período: " + start + " a " + end);
                existingExport.setFilePath(output.toString());
                existingExport.setStatus(status);
                existingExport.setNotes(notes);
                existingExport.setUser(session.getUser() != null ? session.getUser() : null);

                export = saftDao.update(existingExport);
                System.out.println("✅ Export SAF-T atualizado com ID: " + export.getId());
            } else {
                // 🔹 CRIA NOVO REGISTRO
                Integer exportedBy = (session != null && session.getUser() != null) ? session.getUser().getId() : null;
                export = saftDao.insertExport(start, end, output.toString(), status, notes,
                        exportedBy != null ? session.getUser() : null);

                System.out.println("✅ Export SAF-T criado com ID: " + export.getId());
            }

            return export.getId();

        } catch (Exception e) {
            // 🔹 REGISTRA FALHA MESMO EM CASO DE ERRO
            try {
                SaftFat existingExport = findExistingExport(start, end);
                if (existingExport != null) {
                    existingExport.setStatus("FAILED");
                    existingExport.setNotes(e.getMessage());
                    saftDao.update(existingExport);
                } else {
                    Integer exportedBy = (session != null && session.getUser() != null) ? session.getUser().getId() : null;
                    saftDao.insertExport(start, end, output.toString(), "FAILED", e.getMessage(),
                            exportedBy != null ? session.getUser() : null);
                }
            } catch (Exception ignore) {
            }
            throw new Exception("Erro ao gerar export SAF-T: " + e.getMessage(), e);
        }
    }

    /**
     * Busca export existente para o período
     */
    private SaftFat findExistingExport(LocalDate start, LocalDate end) {
        List<SaftFat> existingExports = saftDao.filterByPeriod(start, end);
        return existingExports.isEmpty() ? null : existingExports.get(0);
    }

    /**
     * Gera o SAF-T com estrutura idêntica ao arquivo de exemplo
     */
    private void generateSaftXml(LocalDate start, LocalDate end, Path output) throws Exception {
        XMLOutputFactory xof = XMLOutputFactory.newInstance();

        try (OutputStream os = Files.newOutputStream(output)) {
            XMLStreamWriter w = xof.createXMLStreamWriter(os, "UTF-8");

            w.writeStartDocument("UTF-8", "1.0");
            w.writeStartElement("AuditFile");

            // Escreve header, master files e source documents
            writeHeader(w, start, end);
            writeMasterFiles(w, start, end);
            writeSourceDocuments(w, start, end);

            w.writeEndElement(); // </AuditFile>
            w.writeEndDocument();
            w.flush();
            w.close();
        }
    }

    // ===================== HEADER ATUALIZADO =====================
    private void writeHeader(XMLStreamWriter w, LocalDate start, LocalDate end) throws Exception {
        w.writeStartElement("Header");

        writeTag(w, "AuditFileVersion", "1.01_01");
        writeTag(w, "CompanyID", optionController.getOptionValue("company_nif", "5000767344"));
        writeTag(w, "TaxRegistrationNumber", optionController.getOptionValue("company_nif", "5000767344"));
        writeTag(w, "TaxAccountingBasis", "F"); // F=Faturação
        writeTag(w, "CompanyName", nz(optionController.getOptionValue("company_name"), "Acel Digital"));
        writeTag(w, "BusinessName", nz(optionController.getOptionValue("business_name", "acelfibra.com")));

        // CompanyAddress - Estrutura igual ao exemplo
        w.writeStartElement("CompanyAddress");
        writeTag(w, "AddressDetail", nz(optionController.getOptionValue("company_address_detail", "Avenida Deolinda Rodrigues Nº20")));
        writeTag(w, "City", nz(optionController.getOptionValue("company_city", "Luanda")));
        writeTag(w, "PostalCode", ""); // Vazio como no exemplo
        writeTag(w, "Province", nz(optionController.getOptionValue("company_province", "Luanda")));
        writeTag(w, "Country", "AO");
        w.writeEndElement(); // </CompanyAddress>

        writeTag(w, "FiscalYear", String.valueOf(start.getYear()));
        writeTag(w, "StartDate", start.toString());
        writeTag(w, "EndDate", end.toString());
        writeTag(w, "CurrencyCode", "AOA");
        writeTag(w, "DateCreated", LocalDate.now().toString());
        writeTag(w, "TaxEntity", "AO");
        writeTag(w, "ProductCompanyTaxID", nz(optionController.getOptionValue("company_nif", "5000503053")));
        writeTag(w, "SoftwareValidationNumber", nz(optionController.getOptionValue("software_validation_number", "411/AGT/2023")));
        writeTag(w, "ProductID", nz(optionController.getOptionValue("product_id", "POWERFULLCRM ERP/POWERFULLCRM - ELENGEAFRIKA")));
        writeTag(w, "ProductVersion", nz(optionController.getOptionValue("product_version", "3.1.0")));
        writeTag(w, "Telephone", nz(optionController.getOptionValue("company_phone", "000 000 000")));

        w.writeEndElement(); // </Header>
    }

    // ===================== MASTERFILES ATUALIZADO =====================
    private void writeMasterFiles(XMLStreamWriter w, LocalDate start, LocalDate end) throws Exception {
        w.writeStartElement("MasterFiles");

        // CLIENTES - Todos os clientes do sistema (como no exemplo)
        List<Clients> allClients = clientDao.findAll();
        for (Clients c : allClients) {
            if (c == null) {
                continue;
            }

            w.writeStartElement("Customer");
            writeTag(w, "CustomerID", String.valueOf(c.getId()));
            writeTag(w, "AccountID", "Desconhecido"); // Como no exemplo
            writeTag(w, "CustomerTaxID", nz(c.getNif(), "999999999")); // 999999999 como padrão
            writeTag(w, "CompanyName", formatCustomerName(nz(c.getName())));

            // BillingAddress - Estrutura igual ao exemplo
            w.writeStartElement("BillingAddress");
            writeTag(w, "AddressDetail", "Desconhecido");
            writeTag(w, "City", "Desconhecido");
            writeTag(w, "PostalCode", "Desconhecido");
            writeTag(w, "Province", "Desconhecido");
            writeTag(w, "Country", "AO");
            w.writeEndElement(); // </BillingAddress>

            // ShipToAddress - Estrutura igual ao exemplo
            w.writeStartElement("ShipToAddress");
            writeTag(w, "AddressDetail", "Desconhecido");
            writeTag(w, "City", "Desconhecido");
            writeTag(w, "PostalCode", "Desconhecido");
            writeTag(w, "Country", "AO");
            w.writeEndElement(); // </ShipToAddress>

            writeTag(w, "SelfBillingIndicator", "0");
            w.writeEndElement(); // </Customer>
        }

        // PRODUTOS - Todos os produtos do sistema
        List<Product> allProducts = productDao.findAll();
        for (Product p : allProducts) {
            if (p == null) {
                continue;
            }

            w.writeStartElement("Product");
            writeTag(w, "ProductType", "P"); // P=Produto, S=Serviço
            writeTag(w, "ProductCode", nz(p.getCode(), String.valueOf(p.getId())));
            writeTag(w, "ProductGroup", ""); // Vazio como no exemplo
            writeTag(w, "ProductDescription", nz(p.getDescription(), p.getDescription()));
            writeTag(w, "ProductNumberCode", nz(p.getCode(), String.valueOf(p.getId())));
            w.writeEndElement(); // </Product>
        }

        // TABELA DE IMPOSTOS - Simplificada conforme exemplo
        writeTaxTable(w);

        w.writeEndElement(); // </MasterFiles>
    }

    // TaxTable simplificada
    private void writeTaxTable(XMLStreamWriter w) throws Exception {
        w.writeStartElement("TaxTable");

        // Entradas básicas de IVA para Angola
        writeTaxEntry(w, "IVA", "NOR", "IVA Normal 14%", "14.00");
        writeTaxEntry(w, "IVA", "INT", "IVA Intermédio 7%", "7.00");
        writeTaxEntry(w, "IVA", "RED", "IVA Reduzido 5%", "5.00");
        writeTaxEntry(w, "IVA", "ISE", "Isento de IVA", "0.00");
        writeTaxEntry(w, "IVA", "OUT", "Outras Taxas", "0.00");

        w.writeEndElement(); // </TaxTable>
    }

    private void writeTaxEntry(XMLStreamWriter w, String type, String code, String desc, String perc) throws Exception {
        w.writeStartElement("TaxTableEntry");
        writeTag(w, "TaxType", type);
        writeTag(w, "TaxCountryRegion", "AO");
        writeTag(w, "TaxCode", code);
        writeTag(w, "Description", desc);
        writeTag(w, "TaxPercentage", perc);
        w.writeEndElement(); // </TaxTableEntry>
    }

    // ===================== SOURCE DOCUMENTS ATUALIZADO =====================
    private void writeSourceDocuments(XMLStreamWriter w, LocalDate start, LocalDate end) throws Exception {
        w.writeStartElement("SourceDocuments");

        // SalesInvoices - Faturas do período (Orders + Invoices)
        writeSalesInvoices(w, start, end);

        // WorkingDocuments - Documentos de trabalho (se aplicável)
        writeWorkingDocuments(w, start, end);

        // Payments - Pagamentos do período
        writePayments(w, start, end);

        w.writeEndElement(); // </SourceDocuments>
    }

    // -------- SalesInvoices ATUALIZADO COM ORDERS E INVOICES --------
    private void writeSalesInvoices(XMLStreamWriter w, LocalDate start, LocalDate end) throws Exception {
        w.writeStartElement("SalesInvoices");

        // 🔹 BUSCA ORDERS E INVOICES DO PERÍODO
        List<Order> orders = orderDao.filterByDate(start, end);
        List<Invoices> invoices = invoiceDao.filterByDate(start, end);

        BigDecimal totalCredit = BigDecimal.ZERO;
        BigDecimal totalDebit = BigDecimal.ZERO;
        int numberOfEntries = 0;

        // 🔹 PROCESSAR ORDERS
        for (Order o : orders) {
            if (o == null) {
                continue;
            }
            writeOrderAsInvoice(w, o);

            BigDecimal grossTotal = bd(o.getTotal());
            if ("NC".equalsIgnoreCase(inferInvoiceType(o.getPrefix()))) {
                totalDebit = totalDebit.add(grossTotal);
            } else {
                totalCredit = totalCredit.add(grossTotal);
            }
            numberOfEntries++;
        }

        // 🔹 PROCESSAR INVOICES
        for (Invoices inv : invoices) {
            if (inv == null) {
                continue;
            }
            writeInvoiceAsInvoice(w, inv);

            BigDecimal grossTotal = bd(inv.getTotal());
            if ("NC".equalsIgnoreCase(inferInvoiceType(inv.getPrefix()))) {
                totalDebit = totalDebit.add(grossTotal);
            } else {
                totalCredit = totalCredit.add(grossTotal);
            }
            numberOfEntries++;
        }

        // Totais gerais
        writeTag(w, "NumberOfEntries", String.valueOf(numberOfEntries));
        writeTag(w, "TotalDebit", totalDebit.toString());
        writeTag(w, "TotalCredit", totalCredit.toString());

        w.writeEndElement(); // </SalesInvoices>
    }

    private void writeOrderAsInvoice(XMLStreamWriter w, Order o) throws Exception {
        String invoiceNo = (o.getPrefix() != null ? o.getPrefix() : "")
                + (o.getNumber() != null ? String.valueOf(o.getNumber()) : "");
        String invoiceDate = safeDate10(o.getDatecreate());

        w.writeStartElement("Invoice");
        writeTag(w, "InvoiceNo", invoiceNo);
        writeTag(w, "InvoiceDate", invoiceDate);
        writeTag(w, "InvoiceType", inferInvoiceType(o.getPrefix()));

        // DocumentStatus
        w.writeStartElement("DocumentStatus");
        writeTag(w, "InvoiceStatus", "N"); // N=Normal
        writeTag(w, "InvoiceStatusDate", invoiceDate);
        writeTag(w, "SourceID", o.getSeller() != null ? nz(o.getSeller().getName()) : "Sistema");
        w.writeEndElement(); // </DocumentStatus>

        writeTag(w, "SourceID", o.getSeller() != null ? nz(o.getSeller().getName()) : "Sistema");
        writeTag(w, "CustomerID", String.valueOf(o.getClient() != null ? o.getClient().getId() : 0));

        // Lines
        List<ProductSales> lines = productOrderDao.findByOrderId(o.getId());
        writeInvoiceLines(w, lines, "Order");

        // DocumentTotals
        w.writeStartElement("DocumentTotals");
        writeTag(w, "TaxPayable", decimal(nzD(o.getTotalTaxe())));
        writeTag(w, "NetTotal", decimal(nzD(o.getSubTotal())));
        writeTag(w, "GrossTotal", decimal(nzD(o.getTotal())));
        w.writeEndElement(); // </DocumentTotals>

        w.writeEndElement(); // </Invoice>
    }

    private void writeInvoiceAsInvoice(XMLStreamWriter w, Invoices inv) throws Exception {
        String invoiceNo = (inv.getPrefix() != null ? inv.getPrefix() : "")
                + (inv.getNumber() != null ? String.valueOf(inv.getNumber()) : "");
        String invoiceDate = safeDate10(inv.getIssueDate());

        w.writeStartElement("Invoice");
        writeTag(w, "InvoiceNo", invoiceNo);
        writeTag(w, "InvoiceDate", invoiceDate);
        writeTag(w, "InvoiceType", inferInvoiceType(inv.getPrefix()));

        // DocumentStatus
        w.writeStartElement("DocumentStatus");
        writeTag(w, "InvoiceStatus", "N"); // N=Normal
        writeTag(w, "InvoiceStatusDate", invoiceDate);
        writeTag(w, "SourceID", inv.getSeller() != null ? nz(inv.getSeller().getName()) : "Sistema");
        w.writeEndElement(); // </DocumentStatus>

        writeTag(w, "SourceID", inv.getSeller() != null ? nz(inv.getSeller().getName()) : "Sistema");
        writeTag(w, "CustomerID", String.valueOf(inv.getClient() != null ? inv.getClient().getId() : 0));

        // Lines (simplificado - assumindo que Invoice tem produtos também)
        writeInvoiceLines(w, new ArrayList<>(), "Invoice");

        // DocumentTotals
        w.writeStartElement("DocumentTotals");
        writeTag(w, "TaxPayable", decimal(nzD(inv.getTotalTaxe())));
        writeTag(w, "NetTotal", decimal(nzD(inv.getSubTotal())));
        writeTag(w, "GrossTotal", decimal(nzD(inv.getTotal())));
        w.writeEndElement(); // </DocumentTotals>

        w.writeEndElement(); // </Invoice>
    }

    private void writeInvoiceLines(XMLStreamWriter w, List<ProductSales> lines, String source) throws Exception {
        if (lines == null || lines.isEmpty()) {
            // Linha vazia para manter a estrutura
            w.writeStartElement("Line");
            writeTag(w, "LineNumber", "1");
            writeTag(w, "ProductCode", "0");
            writeTag(w, "ProductDescription", "Documento " + source);
            writeTag(w, "Quantity", "1.00");
            writeTag(w, "UnitPrice", "0.00");
            writeTag(w, "CreditAmount", "0.00");
            w.writeEndElement(); // </Line>
            return;
        }

        int lineNo = 0;
        for (ProductSales line : lines) {
            lineNo++;
            w.writeStartElement("Line");
            writeTag(w, "LineNumber", String.valueOf(lineNo));
            writeTag(w, "ProductCode", line.getProduct() != null
                    ? String.valueOf(line.getProduct().getId()) : "0");
            writeTag(w, "ProductDescription", nz(line.getDescription()));
            writeTag(w, "Quantity", decimal(line.getQty()));
            writeTag(w, "UnitPrice", decimal(nzD(line.getPrice())));

            BigDecimal lineAmount = BigDecimal.valueOf(nzD(line.getPrice()))
                    .multiply(BigDecimal.valueOf(line.getQty()));
            writeTag(w, "CreditAmount", lineAmount.toString());

            w.writeEndElement(); // </Line>
        }
    }

    // -------- WorkingDocuments (simplificado) --------
    private void writeWorkingDocuments(XMLStreamWriter w, LocalDate start, LocalDate end) throws Exception {
        w.writeStartElement("WorkingDocuments");
        // Implementação simplificada - pode ser expandida conforme necessidade
        writeTag(w, "NumberOfEntries", "0");
        writeTag(w, "TotalDebit", "0.00");
        writeTag(w, "TotalCredit", "0.00");
        w.writeEndElement(); // </WorkingDocuments>
    }

    // -------- Payments ATUALIZADO --------
    private void writePayments(XMLStreamWriter w, LocalDate start, LocalDate end) throws Exception {
        w.writeStartElement("Payments");

        List<Payment> payments = paymentDao.findAll();
        BigDecimal totalCredit = BigDecimal.ZERO;
        BigDecimal totalDebit = BigDecimal.ZERO;
        int numberOfEntries = 0;

        // Filtra pagamentos por período
        List<Payment> filteredPayments = payments.stream()
                .filter(p -> isDateInPeriod(p.getDate(), start, end))
                .collect(Collectors.toList());

        for (Payment p : filteredPayments) {
            if (p == null) {
                continue;
            }

            w.writeStartElement("Payment");
            writeTag(w, "PaymentRefNo", nz(p.getReference()));
            writeTag(w, "TransactionDate", safeDate10(p.getDate()));
            writeTag(w, "PaymentType", "RG"); // RG=Recibo
            writeTag(w, "Description", nz(p.getDescription()));
            writeTag(w, "SourceID", p.getUser() != null ? nz(p.getUser().getName()) : "Sistema");

            // Line
            w.writeStartElement("Line");
            writeTag(w, "LineNumber", "1");
            writeTag(w, "CreditAmount", decimal(nzD(p.getTotal())));
            w.writeEndElement(); // </Line>

            // DocumentTotals
            w.writeStartElement("DocumentTotals");
            writeTag(w, "NetTotal", decimal(nzD(p.getTotal())));
            writeTag(w, "GrossTotal", decimal(nzD(p.getTotal())));
            w.writeEndElement(); // </DocumentTotals>

            w.writeEndElement(); // </Payment>

            totalCredit = totalCredit.add(bd(p.getTotal()));
            numberOfEntries++;
        }

        writeTag(w, "NumberOfEntries", String.valueOf(numberOfEntries));
        writeTag(w, "TotalDebit", totalDebit.toString());
        writeTag(w, "TotalCredit", totalCredit.toString());

        w.writeEndElement(); // </Payments>
    }

    // ===================== HELPERS ATUALIZADOS =====================
    private static void writeTag(XMLStreamWriter w, String name, String value) throws XMLStreamException {
        w.writeStartElement(name);
        w.writeCharacters(value == null ? "" : value);
        w.writeEndElement();
    }

    private static String nz(String s) {
        return s == null ? "" : s.trim();
    }

    private static String nz(String s, String def) {
        return (s == null || s.trim().isEmpty()) ? def : s.trim();
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

    private static double nzD(BigDecimal d) {
        return d == null ? 0.0 : d.doubleValue();
    }

    private static String safeDate10(String s) {
        if (s == null) {
            return LocalDate.now().toString();
        }
        return s.length() >= 10 ? s.substring(0, 10) : s;
    }

    private static String decimal(int v) {
        return String.format(Locale.US, "%d.00", v);
    }

    private static String decimal(double v) {
        return String.format(Locale.US, "%.2f", v);
    }

    private static String decimal(BigDecimal v) {
        return v != null ? String.format(Locale.US, "%.2f", v.doubleValue()) : "0.00";
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

    private static boolean isDateInPeriod(String dateStr, LocalDate start, LocalDate end) {
        if (dateStr == null || dateStr.length() < 10) {
            return false;
        }
        try {
            LocalDate date = LocalDate.parse(dateStr.substring(0, 10));
            return !date.isBefore(start) && !date.isAfter(end);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Formata o nome do cliente conforme exemplo (mantém espaços e formatação)
     */
    private static String formatCustomerName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "CLIENTE DESCONHECIDO";
        }

        // Remove espaços extras mas mantém a formatação básica
        String formatted = name.trim().replaceAll("\\s+", " ");

        // Adiciona espaço no início se necessário (como no exemplo)
        if (!formatted.startsWith(" ")) {
            formatted = " " + formatted;
        }

        return formatted;
    }
}
