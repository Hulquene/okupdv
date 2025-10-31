/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.entities.Payment;
import com.okutonda.okudpdv.data.entities.Clients;
import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.ExportSaftFat;
import com.okutonda.okudpdv.data.entities.ProductOrder;
import com.okutonda.okudpdv.data.entities.Order;
import com.okutonda.okudpdv.data.dao.ProductDao;
import com.okutonda.okudpdv.data.dao.ProductOrderDao;
import com.okutonda.okudpdv.data.dao.SettingsDao;
import com.okutonda.okudpdv.data.dao.ClientDao;
import com.okutonda.okudpdv.data.dao.PaymentDao;
import com.okutonda.okudpdv.data.dao.OrderDao;
import com.okutonda.okudpdv.data.dao.SaftFatDao;
import com.okutonda.okudpdv.helpers.UserSession;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Locale;

/**
 * Geração do ficheiro SAF-T (AO) com: - Header - MasterFiles (Customers,
 * Products, TaxTable) - SourceDocuments (SalesInvoices, WorkingDocuments,
 * Payments)
 */
/**
 *
 * @author hr
 */
public class SaftFatController {

    // DAOs
    private final SaftFatDao saftDao = new SaftFatDao();
    private final SettingsDao settingsDao = new SettingsDao();
    private final OrderDao orderDao = new OrderDao();
    private final ProductOrderDao productOrderDao = new ProductOrderDao();
    private final ProductDao productDao = new ProductDao();
    private final ClientDao clientDao = new ClientDao();
    private final PaymentDao paymentDao = new PaymentDao();

    private final UserSession session = UserSession.getInstance();

    // ==========================================================
    // 🔹 CRUD OPERATIONS - Updated to match SaftFatDao
    // ==========================================================
    public List<ExportSaftFat> get() {
        return saftDao.getAll();
    }

    public ExportSaftFat getId(long id) {
        return saftDao.findById(id).orElse(null);
    }

    public List<ExportSaftFat> filter(String txt) {
        return saftDao.filter(txt);
    }

    public List<ExportSaftFat> filterCreatedBetween(LocalDate from, LocalDate to) {
        return saftDao.filterByCreatedAt(from, to);
    }

    public List<ExportSaftFat> filterPeriodOverlap(LocalDate from, LocalDate to) {
        return saftDao.filterByPeriod(from, to);
    }

    public List<ExportSaftFat> filterByUser(Integer userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        return saftDao.findByUserId(userId.longValue());
    }

    public List<ExportSaftFat> findByStatus(String status) {
        return saftDao.findByStatus(status);
    }

    public boolean existsForPeriod(LocalDate start, LocalDate end) {
        return saftDao.existsForPeriod(start, end);
    }

    public ExportSaftFat findLastExport() {
        return saftDao.findLastExport().orElse(null);
    }

    public List<ExportSaftFat> findOverlappingExports(LocalDate start, LocalDate end) {
        return saftDao.findOverlappingExports(start, end);
    }

    public void delete(long id) {
        saftDao.delete(id);
    }

    // ==========================================================
    // 🔹 EXPORT OPERATIONS
    // ==========================================================
    /**
     * Ponto de entrada: gera XML e regista no BD (saft_exports).
     */
    public long export(LocalDate start, LocalDate end, Path output) throws Exception {
        String status = "SUCCESS";
        String notes = null;
        ExportSaftFat export;

        try {
            // Verifica se já existe export para este período
            if (saftDao.existsForPeriod(start, end)) {
                throw new Exception("Já existe um export SAF-T para o período " + start + " a " + end);
            }

            // Gera o ficheiro XML
            generateSaftXml(start, end, output);

            // Regista no banco de dados
            Integer exportedBy = (session != null && session.getUser() != null) ? session.getUser().getId() : null;
            export = saftDao.insertExport(start, end, output.toString(), status, notes,
                    exportedBy != null ? session.getUser() : null);

            System.out.println("✅ Export SAF-T criado com ID: " + export.getId());
            return export.getId();

        } catch (Exception e) {
            // Tenta registar a falha no banco de dados
            try {
                Integer exportedBy = (session != null && session.getUser() != null) ? session.getUser().getId() : null;
                saftDao.insertExport(start, end, output.toString(), "FAILED", e.getMessage(),
                        exportedBy != null ? session.getUser() : null);
            } catch (Exception ignore) {
                /* se falhar o log, ignora */
            }
            throw new Exception("Erro ao gerar export SAF-T: " + e.getMessage(), e);
        }
    }

    /**
     * Gera o SAF-T via StAX.
     */
    private void generateSaftXml(LocalDate start, LocalDate end, Path output) throws Exception {
        XMLOutputFactory xof = XMLOutputFactory.newInstance();

        try (OutputStream os = Files.newOutputStream(output)) {
            XMLStreamWriter w = xof.createXMLStreamWriter(os, "UTF-8");

            w.writeStartDocument("UTF-8", "1.0");
            w.writeStartElement("AuditFile");
            // Adiciona namespace se necessário
            // w.writeDefaultNamespace("urn:OECD:StandardAuditFile-Tax:AO_1.01");

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
        writeTag(w, "AuditFileVersion", "1.01");
        writeTag(w, "CompanyID", settingsDao.getValue("company_nif"));
        writeTag(w, "TaxRegistrationNumber", settingsDao.getValue("company_nif"));
        writeTag(w, "TaxAccountingBasis", settingsDao.getValue("tax_accounting_basis", "F")); // F=Faturação
        writeTag(w, "CompanyName", nz(settingsDao.getValue("company_name")));
        writeTag(w, "BusinessName", nz(settingsDao.getValue("company_name")));

        // CompanyAddress
        w.writeStartElement("CompanyAddress");
        writeTag(w, "AddressDetail", nz(settingsDao.getValue("company_address_detail", settingsDao.getValue("company_address"))));
        writeTag(w, "City", nz(settingsDao.getValue("company_city")));
        writeTag(w, "PostalCode", nz(settingsDao.getValue("company_postal_code")));
        writeTag(w, "Province", nz(settingsDao.getValue("company_province", settingsDao.getValue("company_state"))));
        writeTag(w, "Country", nz(settingsDao.getValue("company_country_code", "AO")));
        w.writeEndElement(); // </CompanyAddress>

        writeTag(w, "FiscalYear", String.valueOf(start.getYear()));
        writeTag(w, "StartDate", start.toString());
        writeTag(w, "EndDate", end.toString());
        writeTag(w, "CurrencyCode", nz(settingsDao.getValue("currency_code", "AOA")));
        writeTag(w, "DateCreated", java.time.LocalDate.now().toString());
        writeTag(w, "TaxEntity", nz(settingsDao.getValue("tax_entity", "AO")));
        writeTag(w, "ProductCompanyTaxID", nz(settingsDao.getValue("company_nif")));
        writeTag(w, "SoftwareValidationNumber", nz(settingsDao.getValue("software_validation_number")));
        writeTag(w, "ProductID", nz(settingsDao.getValue("product_id", "Okudpdv/Okutonda")));
        writeTag(w, "ProductVersion", nz(settingsDao.getValue("product_version", "1.0.0")));
        writeTag(w, "Telephone", nz(settingsDao.getValue("company_phone")));
        writeTag(w, "FileType", nz(settingsDao.getValue("file_type", "N"))); // N=Normal
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
            Clients c = clientDao.findById(cid);
            if (c == null) {
                continue;
            }

            w.writeStartElement("Customer");
            writeTag(w, "CustomerID", String.valueOf(c.getId()));
            writeTag(w, "AccountID", nz(c.getName(), "Desconhecido"));
            writeTag(w, "CustomerTaxID", nz(c.getNif()));
            writeTag(w, "CompanyName", nz(c.getName()));

            // BillingAddress
            w.writeStartElement("BillingAddress");
            writeTag(w, "AddressDetail", nz(c.getAddress(), "Desconhecido"));
            writeTag(w, "City", nz(c.getCity(), "Desconhecido"));
            writeTag(w, "PostalCode", nz(c.getZipCode()));
            writeTag(w, "Province", nz(c.getState()));
            writeTag(w, "Country", nz(settingsDao.getValue("company_country_code", "AO")));
            w.writeEndElement();

            // ShipToAddress (opcional – replico billing)
            w.writeStartElement("ShipToAddress");
            writeTag(w, "AddressDetail", nz(c.getAddress(), "Desconhecido"));
            writeTag(w, "City", nz(c.getCity(), "Desconhecido"));
            writeTag(w, "PostalCode", nz(c.getZipCode()));
            writeTag(w, "Country", nz(settingsDao.getValue("company_country_code", "AO")));
            w.writeEndElement();

            writeTag(w, "SelfBillingIndicator", "0");
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
            Product p = productDao.findById(pid);
            if (p == null) {
                continue;
            }

            w.writeStartElement("Product");
            writeTag(w, "ProductType", "P");
            writeTag(w, "ProductCode", nz(p.getCode(), String.valueOf(p.getId())));
            writeTag(w, "ProductGroup", "");
            writeTag(w, "ProductDescription", nz(p.getDescription(), p.getDescription()));
            writeTag(w, "ProductNumberCode", nz(p.getCode(), String.valueOf(p.getId())));
            w.writeEndElement();
        }

        // TABELA DE IMPOSTOS
        writeTaxTable(w);

        w.writeEndElement(); // </MasterFiles>
    }

    // TaxTable com entradas base – se tiveres tabela própria, lê de lá.
    private void writeTaxTable(XMLStreamWriter w) throws Exception {
        w.writeStartElement("TaxTable");
        writeTaxEntry(w, "IVA", "AO", "NOR", "IVA 14%", "14.00");
        writeTaxEntry(w, "IVA", "AO", "INT", "IVA 7%", "7.00");
        writeTaxEntry(w, "IVA", "AO", "RED", "IVA 5%", "5.00");
        writeTaxEntry(w, "IVA", "AO", "ISE", "Isento - Art. 12.º b) CIVA", "0.00");
        writeTaxEntry(w, "IVA", "AO", "NS", "IVA 0% - Exportação", "0.00");
        writeTaxEntry(w, "IVA", "AO", "OUT", "Outros", "0.00");
        w.writeEndElement();
    }

    private void writeTaxEntry(XMLStreamWriter w, String type, String region, String code, String desc, String perc) throws Exception {
        w.writeStartElement("TaxTableEntry");
        writeTag(w, "TaxType", type);
        writeTag(w, "TaxCountryRegion", region);
        writeTag(w, "TaxCode", code);
        writeTag(w, "Description", desc);
        writeTag(w, "TaxPercentage", perc);
        w.writeEndElement();
    }

    // ===================== SOURCE DOCUMENTS =====================
    private void writeSourceDocuments(XMLStreamWriter w, LocalDate start, LocalDate end) throws Exception {
        w.writeStartElement("SourceDocuments");

        writeSalesInvoices(w, start, end);     // Faturas
        writePayments(w, start, end);          // Recibos

        w.writeEndElement(); // </SourceDocuments>
    }

    // -------- SalesInvoices --------
    private void writeSalesInvoices(XMLStreamWriter w, LocalDate start, LocalDate end) throws Exception {
        w.writeStartElement("SalesInvoices");

        BigDecimal totalCredit = BigDecimal.ZERO; // FT/FR/ND
        BigDecimal totalDebit = BigDecimal.ZERO; // NC

        List<Order> orders = orderDao.filterDate(start, end, "");
        for (Order o : orders) {
            String invoiceNo = (o.getPrefix() == null ? "" : o.getPrefix()) + (o.getPrefix() == null ? "" : " ") + o.getNumber();
            String invoiceDate = safeDate10(o.getDatecreate());
            String periodMM = month2(invoiceDate);
            String invoiceType = inferInvoiceType(o.getPrefix()); // FT/FR/NC/ND
            String sourceId = o.getSeller() != null ? nz(o.getSeller().getName(), "Desconhecido") : "Desconhecido";

            w.writeStartElement("Invoice");
            writeTag(w, "InvoiceNo", nz(invoiceNo));
            writeTag(w, "ATCUD", nz(settingsDao.getValue("atcud")));

            // DocumentStatus
            w.writeStartElement("DocumentStatus");
            writeTag(w, "InvoiceStatus", "N"); // Normal
            writeTag(w, "InvoiceStatusDate", dateTimeFrom(o.getDatecreate()));
            writeTag(w, "Reason", "");
            writeTag(w, "SourceID", sourceId);
            writeTag(w, "SourceBilling", "P");
            w.writeEndElement();

            writeTag(w, "Hash", nz(o.getHash()));
            writeTag(w, "HashControl", "1");
            writeTag(w, "Period", stripLeadingZero(periodMM));
            writeTag(w, "InvoiceDate", invoiceDate);
            writeTag(w, "InvoiceType", invoiceType);

            // SpecialRegimes
            w.writeStartElement("SpecialRegimes");
            writeTag(w, "SelfBillingIndicator", "0");
            writeTag(w, "CashVATSchemeIndicator", "0");
            writeTag(w, "ThirdPartiesBillingIndicator", "0");
            w.writeEndElement();

            writeTag(w, "SourceID", sourceId);
            writeTag(w, "SystemEntryDate", dateTimeFrom(o.getDatecreate()));
            writeTag(w, "CustomerID", String.valueOf(o.getClient() != null ? o.getClient().getId() : 0));

            // ShipFrom mínimo
            w.writeStartElement("ShipFrom");
            writeTag(w, "DeliveryDate", invoiceDate);
            w.writeStartElement("Address");
            writeTag(w, "AddressDetail", "Desconhecido");
            writeTag(w, "City", "Desconhecido");
            writeTag(w, "PostalCode", "Desconhecido");
            writeTag(w, "Country", "Desconhecido");
            w.writeEndElement();
            w.writeEndElement();

            // Linhas
            List<ProductOrder> lines = productOrderDao.listProductFromOrderId(o.getId());
            writeInvoiceLines(w, lines, invoiceDate);

            // Totais
            BigDecimal gross = bd(o.getTotal());
            BigDecimal net = bd(o.getSubTotal());
            BigDecimal tax = gross.subtract(net).max(BigDecimal.ZERO);

            w.writeStartElement("DocumentTotals");
            writeTag(w, "TaxPayable", tax.toString());
            writeTag(w, "NetTotal", net.toString());
            writeTag(w, "GrossTotal", gross.toString());

            // (opcional) bloco Payment padrão
            w.writeStartElement("Payment");
            writeTag(w, "PaymentMechanism", "");
            writeTag(w, "PaymentAmount", "0");
            writeTag(w, "PaymentDate", "");
            w.writeEndElement();

            w.writeEndElement(); // </DocumentTotals>

            w.writeEndElement(); // </Invoice>

            if ("NC".equalsIgnoreCase(invoiceType)) {
                totalDebit = totalDebit.add(gross);
            } else {
                totalCredit = totalCredit.add(gross);
            }
        }

        writeTag(w, "NumberOfEntries", String.valueOf(orders.size()));
        writeTag(w, "TotalDebit", totalDebit.toString());
        writeTag(w, "TotalCredit", totalCredit.toString());

        w.writeEndElement(); // </SalesInvoices>
    }

    private void writeInvoiceLines(XMLStreamWriter w, List<ProductOrder> lines, String invoiceDate) throws Exception {
        if (lines == null) {
            return;
        }
        int lineNo = 0;

        for (ProductOrder line : lines) {
            lineNo++;
            w.writeStartElement("Line");

            writeTag(w, "LineNumber", String.valueOf(lineNo));
            int pid = (line.getProduct() != null ? line.getProduct().getId() : 0);
            writeTag(w, "ProductCode", String.valueOf(pid));
            writeTag(w, "ProductDescription", nz(line.getDescription()));
            writeTag(w, "Quantity", decimal(line.getQty()));
            writeTag(w, "UnitOfMeasure", nz(line.getUnit(), "Unid"));
            writeTag(w, "UnitPrice", decimal(nzD(line.getPrice())));

            BigDecimal credit = BigDecimal.valueOf(nzD(line.getPrice()))
                    .multiply(BigDecimal.valueOf(line.getQty()));
            writeTag(w, "CreditAmount", credit.toString());

            writeTag(w, "TaxPointDate", invoiceDate);

            // Tax
            w.writeStartElement("Tax");
            writeTag(w, "TaxType", nz(line.getTaxeName(), "IVA"));
            writeTag(w, "TaxCountryRegion", "AO");
            writeTag(w, "TaxCode", mapTaxCode(line.getTaxeCode(), line.getTaxePercentage(), line.getTaxeName()));
            writeTag(w, "TaxPercentage", decimal(nzD(line.getTaxePercentage())));
            w.writeEndElement(); // </Tax>

            w.writeEndElement(); // </Line>
        }
    }

    // -------- Payments --------
    private void writePayments(XMLStreamWriter w, LocalDate start, LocalDate end) throws Exception {
        w.writeStartElement("Payments");

        var payments = paymentDao.list(""); // se adicionares filterDate, usa-o aqui
        // filtra por período (date: "YYYY-MM-DD[...]")
        payments.removeIf(p -> {
            String d = p.getDate();
            if (d == null || d.length() < 10) {
                return true;
            }
            LocalDate ld = LocalDate.parse(d.substring(0, 10));
            return ld.isBefore(start) || ld.isAfter(end);
        });

        BigDecimal totalCredit = BigDecimal.ZERO;
        BigDecimal totalDebit = BigDecimal.ZERO;
        int entries = 0;

        for (Payment p : payments) {
            entries++;

            String ref = (p.getPrefix() == null ? "" : p.getPrefix() + " ") + p.getNumber();
            String pDate = safeDate10(p.getDate());
            String periodMM = stripLeadingZero(month2(pDate));
            String sourceId = p.getUser() != null ? nz(p.getUser().getName(), "Desconhecido") : "Desconhecido";
            String mech = "";

            // documento de origem (fatura) para o SourceDocumentID
            String originNo = "";
            String invType = nz(p.getInvoiceType());
            if (p.getInvoiceId() > 0) {
                Order inv = orderDao.findById(p.getInvoiceId());
                if (inv != null) {
                    originNo = (inv.getPrefix() == null ? "" : inv.getPrefix() + " ") + inv.getNumber();
                    if (invType.isEmpty()) {
                        invType = inferInvoiceType(inv.getPrefix());
                    }
                }
            }

            w.writeStartElement("Payment");
            writeTag(w, "PaymentRefNo", ref);
            writeTag(w, "Period", periodMM);
            writeTag(w, "TransactionDate", pDate);
            writeTag(w, "PaymentType", "RC");
            writeTag(w, "Description", nz(p.getDescription()));
            writeTag(w, "SystemID", sourceId);
            writeTag(w, "SourceID", sourceId);
            writeTag(w, "SystemEntryDate", dateTimeFrom(p.getDateFinish() != null && !p.getDateFinish().isEmpty() ? p.getDateFinish() : p.getDate()));
            writeTag(w, "CustomerID", String.valueOf(p.getClient() != null ? p.getClient().getId() : 0));

            // Linha do recibo
            w.writeStartElement("Line");
            writeTag(w, "LineNumber", "1");

            w.writeStartElement("SourceDocumentID");
            writeTag(w, "OriginatingON", originNo);
            writeTag(w, "InvoiceType", inferInvoiceType(invType));
            writeTag(w, "Description", "Liquidação");
            w.writeEndElement(); // </SourceDocumentID>

            BigDecimal amt = p.getTotal();
            writeTag(w, "CreditAmount", amt.toString());

            w.writeStartElement("Tax"); // pagamentos não têm IVA
            writeTag(w, "TaxType", "IVA");
            writeTag(w, "TaxCountryRegion", "AO");
            writeTag(w, "TaxCode", "OUT");
            writeTag(w, "TaxPercentage", "0.00");
            w.writeEndElement(); // </Tax>

            w.writeEndElement(); // </Line>

            // Totais do recibo
            w.writeStartElement("DocumentTotals");
            writeTag(w, "TaxPayable", "0.00");
            writeTag(w, "NetTotal", amt.toString());
            writeTag(w, "GrossTotal", amt.toString());

            w.writeStartElement("PaymentMethod");
            writeTag(w, "PaymentMechanism", mech);
            writeTag(w, "PaymentAmount", amt.toString());
            writeTag(w, "PaymentDate", pDate);
            w.writeEndElement(); // </PaymentMethod>

            w.writeEndElement(); // </DocumentTotals>

            w.writeEndElement(); // </Payment>

            totalCredit = totalCredit.add(amt);
        }

        writeTag(w, "NumberOfEntries", String.valueOf(entries));
        writeTag(w, "TotalDebit", totalDebit.toString());
        writeTag(w, "TotalCredit", totalCredit.toString());

        w.writeEndElement(); // </Payments>
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

    private static String nz(String s, String def) {
        return (s == null || s.isEmpty()) ? def : s;
    }

    // Mantém em BigDecimal para operações financeiras
    private static BigDecimal nzBD(BigDecimal d) {
        return d == null ? BigDecimal.ZERO : d;
    }

    // Só usa double para exibir ou cálculos não críticos
    private static double nzD(BigDecimal d) {
        return d == null ? 0.0 : d.doubleValue();
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
        return s.length() >= 10 ? s.substring(0, 10) : s; // "YYYY-MM-DD"
    }

    private static String dateTimeFrom(String s) {
        if (s == null || s.isEmpty()) {
            return java.time.LocalDateTime.now().toString();
        }
        String base = s.replace(' ', 'T');
        if (base.length() == 10) {
            base += "T00:00:00";
        }
        return base;
    }

    private static String month2(String yyyyMMdd) {
        if (yyyyMMdd == null || yyyyMMdd.length() < 7) {
            return "01";
        }
        return yyyyMMdd.substring(5, 7);
    }

    private static String stripLeadingZero(String mm) {
        if (mm == null) {
            return "";
        }
        return mm.startsWith("0") ? mm.substring(1) : mm;
    }

    private static String decimal(int v) {
        return String.format(Locale.US, "%d.00", v);
    }

    private static String decimal(double v) {
        return String.format(Locale.US, "%.2f", v);
    }

    // FT/FR/NC/ND a partir do prefixo; ajusta às tuas séries
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

    // Mapeamento simples do TaxCode para Angola
    private static String mapTaxCode(String code, BigDecimal perc, String name) {
        BigDecimal p = perc == null ? BigDecimal.ZERO : perc;
        if (approx(p, 14)) {
            return "NOR";
        }
        if (approx(p, 7)) {
            return "INT";
        }
        if (approx(p, 5)) {
            return "RED";
        }
        if (approx(p, 0)) {
            String n = (name == null ? "" : name.toUpperCase());
            String c = (code == null ? "" : code.toUpperCase());
            if (n.contains("ISENTO") || c.contains("ISE")) {
                return "ISE";
            }
            if (n.contains("EXPORT") || c.contains("NS")) {
                return "NS";
            }
            return "OUT";
        }
        if (code != null) {
            String c = code.toUpperCase();
            if (c.startsWith("NOR")) {
                return "NOR";
            }
            if (c.startsWith("INT")) {
                return "INT";
            }
            if (c.startsWith("RED")) {
                return "RED";
            }
            if (c.startsWith("ISE")) {
                return "ISE";
            }
            if (c.startsWith("NS")) {
                return "NS";
            }
        }
        return "OUT";
    }

    private static boolean approx(BigDecimal a, double b) {
        if (a == null) {
            a = BigDecimal.ZERO;
        }
        BigDecimal bdB = BigDecimal.valueOf(b);

        BigDecimal diff = a.subtract(bdB).abs();
        return diff.compareTo(new BigDecimal("0.001")) < 0;
    }
}

///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.okutonda.okudpdv.controllers;
//
//import com.okutonda.okudpdv.data.entities.Payment;
//import com.okutonda.okudpdv.data.entities.Clients;
//import com.okutonda.okudpdv.data.entities.Product;
//import com.okutonda.okudpdv.data.entities.ExportSaftFat;
//import com.okutonda.okudpdv.data.entities.ProductOrder;
//import com.okutonda.okudpdv.data.entities.Order;
//import com.okutonda.okudpdv.data.dao.ProductDao;
//import com.okutonda.okudpdv.data.dao.ProductOrderDao;
//import com.okutonda.okudpdv.data.dao.SettingsDao;
//import com.okutonda.okudpdv.data.dao.ClientDao;
//import com.okutonda.okudpdv.data.dao.PaymentDao;
//import com.okutonda.okudpdv.data.dao.OrderDao;
//import com.okutonda.okudpdv.data.dao.SaftFatDao;
//import com.okutonda.okudpdv.helpers.UserSession;
//
//import javax.xml.stream.XMLOutputFactory;
//import javax.xml.stream.XMLStreamException;
//import javax.xml.stream.XMLStreamWriter;
//import java.io.OutputStream;
//import java.math.BigDecimal;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.sql.Date;
//import java.time.LocalDate;
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.Locale;
//
///**
// * Geração do ficheiro SAF-T (AO) com: - Header - MasterFiles (Customers,
// * Products, TaxTable) - SourceDocuments (SalesInvoices, WorkingDocuments,
// * Payments)
// */
///**
// *
// * @author hr
// */
//public class SaftFatController {
//
//    // DAOs
//    private final SaftFatDao saftDao = new SaftFatDao();
//    private final SettingsDao settingsDao = new SettingsDao();
//    private final OrderDao orderDao = new OrderDao();
//    private final ProductOrderDao productOrderDao = new ProductOrderDao();
//    private final ProductDao productDao = new ProductDao();
//    private final ClientDao clientDao = new ClientDao();
//    private final PaymentDao paymentDao = new PaymentDao();
////    private final PaymentModeDao paymentModeDao = new PaymentModeDao();
//    // Working Documents (model/dao mínimos que partilhei contigo)
////    private final WorkDocumentDao workDocDao = new WorkDocumentDao();
////    private final WorkDocumentLineDao workDocLineDao = new WorkDocumentLineDao();
//
//    private final UserSession session = UserSession.getInstance();
//
//    public List<ExportSaftFat> get() {
//        return saftDao.getAll();
//    }
//
//    public ExportSaftFat getId(long id) {
//        return saftDao.getId(id);
//    }
//
//    public List<ExportSaftFat> filter(String txt) {
//        return saftDao.filter(txt);
//    }
//
//    public List<ExportSaftFat> filterCreatedBetween(LocalDate from, LocalDate to) {
//        return saftDao.filterByCreatedAt(from, to);
//    }
//
//    public List<ExportSaftFat> filterPeriodOverlap(LocalDate from, LocalDate to) {
//        return saftDao.filterByPeriod(from, to);
//    }
//
//    public List<ExportSaftFat> filterByUser(Integer userId) {
//        return saftDao.filterByUser(userId);
//    }
//
//    /**
//     * Ponto de entrada: gera XML e regista no BD (saft_exports).
//     */
//    public long export(LocalDate start, LocalDate end, Path output) throws Exception {
//        String status = "SUCCESS";
//        String notes = null;
//        long exportId;
//
//        try {
//            generateSaftXml(start, end, output);
//
//            Integer exportedBy = (session != null && session.getUser() != null) ? session.getUser().getId() : null;
//            // Requer overload no SaftFatDao com exported_by
//            exportId = saftDao.insertExport(Date.valueOf(start), Date.valueOf(end), output.toString(), status, notes, exportedBy);
//            return exportId;
//
//        } catch (Exception e) {
//            try {
//                Integer exportedBy = (session != null && session.getUser() != null) ? session.getUser().getId() : null;
//                saftDao.insertExport(Date.valueOf(start), Date.valueOf(end), output.toString(), "FAILED", e.getMessage(), exportedBy);
//            } catch (Exception ignore) {
//                /* se falhar o log, ignora */ }
//            throw e;
//        }
//    }
//
//    /**
//     * Gera o SAF-T via StAX.
//     */
//    private void generateSaftXml(LocalDate start, LocalDate end, Path output) throws Exception {
//        XMLOutputFactory xof = XMLOutputFactory.newInstance();
//
//        try (OutputStream os = Files.newOutputStream(output)) {
//            XMLStreamWriter w = xof.createXMLStreamWriter(os, "UTF-8");
//
//            w.writeStartDocument("UTF-8", "1.0");
//            // Podes adicionar o namespace/XSD oficial quando definires:
//            // w.writeStartElement("AuditFile");
//            // w.writeDefaultNamespace("urn:OECD:StandardAuditFile-Tax:AO_1.01");
//            w.writeStartElement("AuditFile");
//
//            writeHeader(w, start, end);
//            writeMasterFiles(w, start, end);
//            writeSourceDocuments(w, start, end);
//
//            w.writeEndElement(); // </AuditFile>
//            w.writeEndDocument();
//            w.flush();
//            w.close();
//        }
//    }
//
//    // ===================== HEADER =====================
//    private void writeHeader(XMLStreamWriter w, LocalDate start, LocalDate end) throws Exception {
//        w.writeStartElement("Header");
//        writeTag(w, "AuditFileVersion", "1.01");
//        writeTag(w, "CompanyID", settingsDao.getValue("company_nif"));
//        writeTag(w, "TaxRegistrationNumber", settingsDao.getValue("company_nif"));
//        writeTag(w, "TaxAccountingBasis", settingsDao.getValue("tax_accounting_basis", "F")); // F=Faturação
//        writeTag(w, "CompanyName", nz(settingsDao.getValue("company_name")));
//        writeTag(w, "BusinessName", nz(settingsDao.getValue("company_name")));
//
//        // CompanyAddress
//        w.writeStartElement("CompanyAddress");
//        writeTag(w, "AddressDetail", nz(settingsDao.getValue("company_address_detail", settingsDao.getValue("company_address"))));
//        writeTag(w, "City", nz(settingsDao.getValue("company_city")));
//        writeTag(w, "PostalCode", nz(settingsDao.getValue("company_postal_code")));
//        writeTag(w, "Province", nz(settingsDao.getValue("company_province", settingsDao.getValue("company_state"))));
//        writeTag(w, "Country", nz(settingsDao.getValue("company_country_code", "AO")));
//        w.writeEndElement(); // </CompanyAddress>
//
//        writeTag(w, "FiscalYear", String.valueOf(start.getYear()));
//        writeTag(w, "StartDate", start.toString());
//        writeTag(w, "EndDate", end.toString());
//        writeTag(w, "CurrencyCode", nz(settingsDao.getValue("currency_code", "AOA")));
//        writeTag(w, "DateCreated", java.time.LocalDate.now().toString());
//        writeTag(w, "TaxEntity", nz(settingsDao.getValue("tax_entity", "AO")));
//        writeTag(w, "ProductCompanyTaxID", nz(settingsDao.getValue("company_nif")));
//        writeTag(w, "SoftwareValidationNumber", nz(settingsDao.getValue("software_validation_number")));
//        writeTag(w, "ProductID", nz(settingsDao.getValue("product_id", "Okudpdv/Okutonda")));
//        writeTag(w, "ProductVersion", nz(settingsDao.getValue("product_version", "1.0.0")));
//        writeTag(w, "Telephone", nz(settingsDao.getValue("company_phone")));
//        writeTag(w, "FileType", nz(settingsDao.getValue("file_type", "N"))); // N=Normal
//        w.writeEndElement();
//    }
//
//    // ===================== MASTERFILES =====================
//    private void writeMasterFiles(XMLStreamWriter w, LocalDate start, LocalDate end) throws Exception {
//        w.writeStartElement("MasterFiles");
//
//        // ORDERS no período
//        List<Order> orders = orderDao.filterDate(start, end, "");
//
//        // CLIENTES distintos presentes nas vendas
//        Set<Integer> customerIds = orders.stream()
//                .map(o -> o.getClient() != null ? o.getClient().getId() : null)
//                .filter(Objects::nonNull)
//                .collect(Collectors.toCollection(LinkedHashSet::new));
//
//        for (Integer cid : customerIds) {
//            Clients c = clientDao.findById(cid);
//            if (c == null) {
//                continue;
//            }
//
//            w.writeStartElement("Customer");
//            writeTag(w, "CustomerID", String.valueOf(c.getId()));
//            writeTag(w, "AccountID", nz(c.getName(), "Desconhecido"));
//            writeTag(w, "CustomerTaxID", nz(c.getNif()));
//            writeTag(w, "CompanyName", nz(c.getName()));
//
//            // BillingAddress
//            w.writeStartElement("BillingAddress");
//            writeTag(w, "AddressDetail", nz(c.getAddress(), "Desconhecido"));
//            writeTag(w, "City", nz(c.getCity(), "Desconhecido"));
//            writeTag(w, "PostalCode", nz(c.getZipCode()));
//            writeTag(w, "Province", nz(c.getState()));
//            writeTag(w, "Country", nz(settingsDao.getValue("company_country_code", "AO")));
//            w.writeEndElement();
//
//            // ShipToAddress (opcional – replico billing)
//            w.writeStartElement("ShipToAddress");
//            writeTag(w, "AddressDetail", nz(c.getAddress(), "Desconhecido"));
//            writeTag(w, "City", nz(c.getCity(), "Desconhecido"));
//            writeTag(w, "PostalCode", nz(c.getZipCode()));
//            writeTag(w, "Country", nz(settingsDao.getValue("company_country_code", "AO")));
//            w.writeEndElement();
//
//            writeTag(w, "SelfBillingIndicator", "0");
//            w.writeEndElement();
//        }
//
//        // PRODUTOS distintos presentes nas vendas
//        Set<Integer> productIds = new LinkedHashSet<>();
//        for (Order o : orders) {
//            List<ProductOrder> lines = productOrderDao.listProductFromOrderId(o.getId());
//            if (lines == null) {
//                continue;
//            }
//            for (ProductOrder l : lines) {
//                if (l.getProduct() != null) {
//                    productIds.add(l.getProduct().getId());
//                }
//            }
//        }
//        for (Integer pid : productIds) {
//            Product p = productDao.findById(pid);
//            if (p == null) {
//                continue;
//            }
//
//            w.writeStartElement("Product");
//            writeTag(w, "ProductType", "P");
//            writeTag(w, "ProductCode", nz(p.getCode(), String.valueOf(p.getId())));
//            writeTag(w, "ProductGroup", "");
//            writeTag(w, "ProductDescription", nz(p.getDescription(), p.getDescription()));
//            writeTag(w, "ProductNumberCode", nz(p.getCode(), String.valueOf(p.getId())));
//            w.writeEndElement();
//        }
//
//        // TABELA DE IMPOSTOS
//        writeTaxTable(w);
//
//        w.writeEndElement(); // </MasterFiles>
//    }
//
//    // TaxTable com entradas base – se tiveres tabela própria, lê de lá.
//    private void writeTaxTable(XMLStreamWriter w) throws Exception {
//        w.writeStartElement("TaxTable");
//        writeTaxEntry(w, "IVA", "AO", "NOR", "IVA 14%", "14.00");
//        writeTaxEntry(w, "IVA", "AO", "INT", "IVA 7%", "7.00");
//        writeTaxEntry(w, "IVA", "AO", "RED", "IVA 5%", "5.00");
//        writeTaxEntry(w, "IVA", "AO", "ISE", "Isento - Art. 12.º b) CIVA", "0.00");
//        writeTaxEntry(w, "IVA", "AO", "NS", "IVA 0% - Exportação", "0.00");
//        writeTaxEntry(w, "IVA", "AO", "OUT", "Outros", "0.00");
//        w.writeEndElement();
//    }
//
//    private void writeTaxEntry(XMLStreamWriter w, String type, String region, String code, String desc, String perc) throws Exception {
//        w.writeStartElement("TaxTableEntry");
//        writeTag(w, "TaxType", type);
//        writeTag(w, "TaxCountryRegion", region);
//        writeTag(w, "TaxCode", code);
//        writeTag(w, "Description", desc);
//        writeTag(w, "TaxPercentage", perc);
//        w.writeEndElement();
//    }
//
//    // ===================== SOURCE DOCUMENTS =====================
//    private void writeSourceDocuments(XMLStreamWriter w, LocalDate start, LocalDate end) throws Exception {
//        w.writeStartElement("SourceDocuments");
//
//        writeSalesInvoices(w, start, end);     // Faturas
////        writeWorkingDocuments(w, start, end);  // Documentos de trabalho
//        writePayments(w, start, end);          // Recibos
//
//        w.writeEndElement(); // </SourceDocuments>
//    }
//
//    // -------- SalesInvoices --------
//    private void writeSalesInvoices(XMLStreamWriter w, LocalDate start, LocalDate end) throws Exception {
//        w.writeStartElement("SalesInvoices");
//
//        BigDecimal totalCredit = BigDecimal.ZERO; // FT/FR/ND
//        BigDecimal totalDebit = BigDecimal.ZERO; // NC
//
//        List<Order> orders = orderDao.filterDate(start, end, "");
//        for (Order o : orders) {
//            String invoiceNo = (o.getPrefix() == null ? "" : o.getPrefix()) + (o.getPrefix() == null ? "" : " ") + o.getNumber();
//            String invoiceDate = safeDate10(o.getDatecreate());
//            String periodMM = month2(invoiceDate);
//            String invoiceType = inferInvoiceType(o.getPrefix()); // FT/FR/NC/ND
//            String sourceId = o.getSeller() != null ? nz(o.getSeller().getName(), "Desconhecido") : "Desconhecido";
//
//            w.writeStartElement("Invoice");
//            writeTag(w, "InvoiceNo", nz(invoiceNo));
//            writeTag(w, "ATCUD", nz(settingsDao.getValue("atcud")));
//
//            // DocumentStatus
//            w.writeStartElement("DocumentStatus");
//            writeTag(w, "InvoiceStatus", "N"); // Normal
//            writeTag(w, "InvoiceStatusDate", dateTimeFrom(o.getDatecreate()));
//            writeTag(w, "Reason", "");
//            writeTag(w, "SourceID", sourceId);
//            writeTag(w, "SourceBilling", "P");
//            w.writeEndElement();
//
//            writeTag(w, "Hash", nz(o.getHash()));
//            writeTag(w, "HashControl", "1");
//            writeTag(w, "Period", stripLeadingZero(periodMM));
//            writeTag(w, "InvoiceDate", invoiceDate);
//            writeTag(w, "InvoiceType", invoiceType);
//
//            // SpecialRegimes
//            w.writeStartElement("SpecialRegimes");
//            writeTag(w, "SelfBillingIndicator", "0");
//            writeTag(w, "CashVATSchemeIndicator", "0");
//            writeTag(w, "ThirdPartiesBillingIndicator", "0");
//            w.writeEndElement();
//
//            writeTag(w, "SourceID", sourceId);
//            writeTag(w, "SystemEntryDate", dateTimeFrom(o.getDatecreate()));
//            writeTag(w, "CustomerID", String.valueOf(o.getClient() != null ? o.getClient().getId() : 0));
//
//            // ShipFrom mínimo
//            w.writeStartElement("ShipFrom");
//            writeTag(w, "DeliveryDate", invoiceDate);
//            w.writeStartElement("Address");
//            writeTag(w, "AddressDetail", "Desconhecido");
//            writeTag(w, "City", "Desconhecido");
//            writeTag(w, "PostalCode", "Desconhecido");
//            writeTag(w, "Country", "Desconhecido");
//            w.writeEndElement();
//            w.writeEndElement();
//
//            // Linhas
//            List<ProductOrder> lines = productOrderDao.listProductFromOrderId(o.getId());
//            writeInvoiceLines(w, lines, invoiceDate);
//
//            // Totais
//            BigDecimal gross = bd(o.getTotal());
//            BigDecimal net = bd(o.getSubTotal());
//            BigDecimal tax = gross.subtract(net).max(BigDecimal.ZERO);
//
//            w.writeStartElement("DocumentTotals");
//            writeTag(w, "TaxPayable", tax.toString());
//            writeTag(w, "NetTotal", net.toString());
//            writeTag(w, "GrossTotal", gross.toString());
//
//            // (opcional) bloco Payment padrão
//            w.writeStartElement("Payment");
//            writeTag(w, "PaymentMechanism", "");
//            writeTag(w, "PaymentAmount", "0");
//            writeTag(w, "PaymentDate", "");
//            w.writeEndElement();
//
//            w.writeEndElement(); // </DocumentTotals>
//
//            w.writeEndElement(); // </Invoice>
//
//            if ("NC".equalsIgnoreCase(invoiceType)) {
//                totalDebit = totalDebit.add(gross);
//            } else {
//                totalCredit = totalCredit.add(gross);
//            }
//        }
//
//        writeTag(w, "NumberOfEntries", String.valueOf(orders.size()));
//        writeTag(w, "TotalDebit", totalDebit.toString());
//        writeTag(w, "TotalCredit", totalCredit.toString());
//
//        w.writeEndElement(); // </SalesInvoices>
//    }
//
//    private void writeInvoiceLines(XMLStreamWriter w, List<ProductOrder> lines, String invoiceDate) throws Exception {
//        if (lines == null) {
//            return;
//        }
//        int lineNo = 0;
//
//        for (ProductOrder line : lines) {
//            lineNo++;
//            w.writeStartElement("Line");
//
//            writeTag(w, "LineNumber", String.valueOf(lineNo));
//            int pid = (line.getProduct() != null ? line.getProduct().getId() : 0);
//            writeTag(w, "ProductCode", String.valueOf(pid));
//            writeTag(w, "ProductDescription", nz(line.getDescription()));
//            writeTag(w, "Quantity", decimal(line.getQty()));
//            writeTag(w, "UnitOfMeasure", nz(line.getUnit(), "Unid"));
//            writeTag(w, "UnitPrice", decimal(nzD(line.getPrice())));
//
//            BigDecimal credit = BigDecimal.valueOf(nzD(line.getPrice()))
//                    .multiply(BigDecimal.valueOf(line.getQty()));
//            writeTag(w, "CreditAmount", credit.toString());
//
//            writeTag(w, "TaxPointDate", invoiceDate);
//
//            // Tax
//            w.writeStartElement("Tax");
//            writeTag(w, "TaxType", nz(line.getTaxeName(), "IVA"));
//            writeTag(w, "TaxCountryRegion", "AO");
//            writeTag(w, "TaxCode", mapTaxCode(line.getTaxeCode(), line.getTaxePercentage(), line.getTaxeName()));
//            writeTag(w, "TaxPercentage", decimal(nzD(line.getTaxePercentage())));
//            w.writeEndElement(); // </Tax>
//
//            w.writeEndElement(); // </Line>
//        }
//    }
//
//    // -------- WorkingDocuments --------
////    private void writeWorkingDocuments(XMLStreamWriter w, LocalDate start, LocalDate end) throws Exception {
////        w.writeStartElement("WorkingDocuments");
////
////        var docs = workDocDao.filterDate(start, end, "");
////        BigDecimal totalCredit = BigDecimal.ZERO;
////        BigDecimal totalDebit = BigDecimal.ZERO;
////
////        for (WorkDocument d : docs) {
////            String docNo = (d.getPrefix() == null ? "" : d.getPrefix() + " ") + d.getNumber();
////            String docDate = safeDate10(d.getDate());
////            String periodMM = stripLeadingZero(month2(docDate));
////            String workType = inferWorkType(d.getPrefix()); // PP/NE/FO...
////            String sourceId = d.getSeller() != null ? nz(d.getSeller().getName(), "Desconhecido") : "Desconhecido";
////
////            w.writeStartElement("WorkDocument");
////            writeTag(w, "DocumentNumber", docNo);
////            writeTag(w, "ATCUD", nz(settingsDao.getValue("atcud")));
////
////            // DocumentStatus
////            w.writeStartElement("DocumentStatus");
////            writeTag(w, "WorkStatus", "N");
////            writeTag(w, "WorkStatusDate", dateTimeFrom(d.getDate()));
////            writeTag(w, "SourceID", sourceId);
////            writeTag(w, "SourceBilling", "P");
////            w.writeEndElement();
////
////            writeTag(w, "Hash", nz(d.getHash()));
////            writeTag(w, "Period", periodMM);
////            writeTag(w, "WorkDate", docDate);
////            writeTag(w, "WorkType", workType);
////            writeTag(w, "SourceID", sourceId);
////            writeTag(w, "SystemEntryDate", dateTimeFrom(d.getDate()));
////            writeTag(w, "CustomerID", String.valueOf(d.getClient() != null ? d.getClient().getId() : 0));
////
////            // Linhas
////            var lines = workDocLineDao.listByWorkDocumentId(d.getId());
////            int ln = 0;
////            BigDecimal gross = BigDecimal.ZERO;
////            BigDecimal net = BigDecimal.ZERO;
////
////            for (WorkDocumentLine line : lines) {
////                ln++;
////                w.writeStartElement("Line");
////                writeTag(w, "LineNumber", String.valueOf(ln));
////
////                int pid = line.getProduct() != null ? line.getProduct().getId() : 0;
////                writeTag(w, "ProductCode", String.valueOf(pid));
////                writeTag(w, "ProductDescription", nz(line.getDescription()));
////                writeTag(w, "Quantity", decimal(line.getQty()));
////                writeTag(w, "UnitOfMeasure", nz(line.getUnit(), "Unid"));
////                writeTag(w, "UnitPrice", decimal(nzD(line.getPrice())));
////
////                BigDecimal credit = BigDecimal.valueOf(nzD(line.getPrice()))
////                        .multiply(BigDecimal.valueOf(line.getQty()));
////                writeTag(w, "CreditAmount", credit.toString());
////                writeTag(w, "TaxPointDate", docDate);
////
////                w.writeStartElement("Tax");
////                writeTag(w, "TaxType", nz(line.getTaxeName(), "IVA"));
////                writeTag(w, "TaxCountryRegion", "AO");
////                writeTag(w, "TaxCode", mapTaxCode(line.getTaxeCode(), line.getTaxePercentage(), line.getTaxeName()));
////                writeTag(w, "TaxPercentage", decimal(nzD(line.getTaxePercentage())));
////                w.writeEndElement(); // </Tax>
////
////                w.writeEndElement(); // </Line>
////
////                gross = gross.add(credit);
////                BigDecimal lineTax = credit.multiply(BigDecimal.valueOf(nzD(line.getTaxePercentage()) / 100.0));
////                net = net.add(credit.subtract(lineTax));
////            }
////
////            BigDecimal taxPayable = gross.subtract(net).max(BigDecimal.ZERO);
////            w.writeStartElement("DocumentTotals");
////            writeTag(w, "TaxPayable", taxPayable.toString());
////            writeTag(w, "NetTotal", net.toString());
////            writeTag(w, "GrossTotal", gross.toString());
////            w.writeEndElement(); // </DocumentTotals>
////
////            w.writeEndElement(); // </WorkDocument>
////
////            // Regra simples: documentos de trabalho contam em crédito
////            totalCredit = totalCredit.add(gross);
////        }
////
////        writeTag(w, "NumberOfEntries", String.valueOf(docs.size()));
////        writeTag(w, "TotalDebit", totalDebit.toString());
////        writeTag(w, "TotalCredit", totalCredit.toString());
////
////        w.writeEndElement(); // </WorkingDocuments>
////    }
//    // -------- Payments --------
//    private void writePayments(XMLStreamWriter w, LocalDate start, LocalDate end) throws Exception {
//        w.writeStartElement("Payments");
//
//        var payments = paymentDao.list(""); // se adicionares filterDate, usa-o aqui
//        // filtra por período (date: "YYYY-MM-DD[...]")
//        payments.removeIf(p -> {
//            String d = p.getDate();
//            if (d == null || d.length() < 10) {
//                return true;
//            }
//            LocalDate ld = LocalDate.parse(d.substring(0, 10));
//            return ld.isBefore(start) || ld.isAfter(end);
//        });
//
//        BigDecimal totalCredit = BigDecimal.ZERO;
//        BigDecimal totalDebit = BigDecimal.ZERO;
//        int entries = 0;
//
//        for (Payment p : payments) {
//            entries++;
//
//            String ref = (p.getPrefix() == null ? "" : p.getPrefix() + " ") + p.getNumber();
//            String pDate = safeDate10(p.getDate());
//            String periodMM = stripLeadingZero(month2(pDate));
//            String sourceId = p.getUser() != null ? nz(p.getUser().getName(), "Desconhecido") : "Desconhecido";
////            String mech = mapPaymentMechanism(p.getPaymentMode());
//            String mech = "";
//
//            // documento de origem (fatura) para o SourceDocumentID
//            String originNo = "";
//            String invType = nz(p.getInvoiceType());
//            if (p.getInvoiceId() > 0) {
//                Order inv = orderDao.findById(p.getInvoiceId());
//                if (inv != null) {
//                    originNo = (inv.getPrefix() == null ? "" : inv.getPrefix() + " ") + inv.getNumber();
//                    if (invType.isEmpty()) {
//                        invType = inferInvoiceType(inv.getPrefix());
//                    }
//                }
//            }
//
//            w.writeStartElement("Payment");
//            writeTag(w, "PaymentRefNo", ref);
//            writeTag(w, "Period", periodMM);
//            writeTag(w, "TransactionDate", pDate);
//            writeTag(w, "PaymentType", "RC");
//            writeTag(w, "Description", nz(p.getDescription()));
//            writeTag(w, "SystemID", sourceId);
//            writeTag(w, "SourceID", sourceId);
//            writeTag(w, "SystemEntryDate", dateTimeFrom(p.getDateFinish() != null && !p.getDateFinish().isEmpty() ? p.getDateFinish() : p.getDate()));
//            writeTag(w, "CustomerID", String.valueOf(p.getClient() != null ? p.getClient().getId() : 0));
//
//            // Linha do recibo
//            w.writeStartElement("Line");
//            writeTag(w, "LineNumber", "1");
//
//            w.writeStartElement("SourceDocumentID");
//            writeTag(w, "OriginatingON", originNo);
//            writeTag(w, "InvoiceType", inferInvoiceType(invType));
//            writeTag(w, "Description", "Liquidação");
//            w.writeEndElement(); // </SourceDocumentID>
//
//            BigDecimal amt = p.getTotal();
//            writeTag(w, "CreditAmount", amt.toString());
//
//            w.writeStartElement("Tax"); // pagamentos não têm IVA
//            writeTag(w, "TaxType", "IVA");
//            writeTag(w, "TaxCountryRegion", "AO");
//            writeTag(w, "TaxCode", "OUT");
//            writeTag(w, "TaxPercentage", "0.00");
//            w.writeEndElement(); // </Tax>
//
//            w.writeEndElement(); // </Line>
//
//            // Totais do recibo
//            w.writeStartElement("DocumentTotals");
//            writeTag(w, "TaxPayable", "0.00");
//            writeTag(w, "NetTotal", amt.toString());
//            writeTag(w, "GrossTotal", amt.toString());
//
//            w.writeStartElement("PaymentMethod");
//            writeTag(w, "PaymentMechanism", mech);
//            writeTag(w, "PaymentAmount", amt.toString());
//            writeTag(w, "PaymentDate", pDate);
//            w.writeEndElement(); // </PaymentMethod>
//
//            w.writeEndElement(); // </DocumentTotals>
//
//            w.writeEndElement(); // </Payment>
//
//            totalCredit = totalCredit.add(amt);
//        }
//
//        writeTag(w, "NumberOfEntries", String.valueOf(entries));
//        writeTag(w, "TotalDebit", totalDebit.toString());
//        writeTag(w, "TotalCredit", totalCredit.toString());
//
//        w.writeEndElement(); // </Payments>
//    }
//
//    // ===================== HELPERS =====================
//    private static void writeTag(XMLStreamWriter w, String name, String value) throws XMLStreamException {
//        w.writeStartElement(name);
//        w.writeCharacters(value == null ? "" : value);
//        w.writeEndElement();
//    }
//
//    private static String nz(String s) {
//        return s == null ? "" : s;
//    }
//
//    private static String nz(String s, String def) {
//        return (s == null || s.isEmpty()) ? def : s;
//    }
//
//    // Mantém em BigDecimal para operações financeiras
//    private static BigDecimal nzBD(BigDecimal d) {
//        return d == null ? BigDecimal.ZERO : d;
//    }
//
//// Só usa double para exibir ou cálculos não críticos
//    private static double nzD(BigDecimal d) {
//        return d == null ? 0.0 : d.doubleValue();
//    }
//
//    private static BigDecimal bd(Object o) {
//        if (o == null) {
//            return BigDecimal.ZERO;
//        }
//        try {
//            return new BigDecimal(o.toString());
//        } catch (Exception e) {
//            return BigDecimal.ZERO;
//        }
//    }
//
//    private static String safeDate10(String s) {
//        if (s == null) {
//            return "";
//        }
//        return s.length() >= 10 ? s.substring(0, 10) : s; // "YYYY-MM-DD"
//    }
//
//    private static String dateTimeFrom(String s) {
//        if (s == null || s.isEmpty()) {
//            return java.time.LocalDateTime.now().toString();
//        }
//        String base = s.replace(' ', 'T');
//        if (base.length() == 10) {
//            base += "T00:00:00";
//        }
//        return base;
//    }
//
//    private static String month2(String yyyyMMdd) {
//        if (yyyyMMdd == null || yyyyMMdd.length() < 7) {
//            return "01";
//        }
//        return yyyyMMdd.substring(5, 7);
//    }
//
//    private static String stripLeadingZero(String mm) {
//        if (mm == null) {
//            return "";
//        }
//        return mm.startsWith("0") ? mm.substring(1) : mm;
//    }
//
//    private static String decimal(int v) {
//        return String.format(Locale.US, "%d.00", v);
//    }
//
//    private static String decimal(double v) {
//        return String.format(Locale.US, "%.2f", v);
//    }
//
//    // FT/FR/NC/ND a partir do prefixo; ajusta às tuas séries
//    private static String inferInvoiceType(String prefix) {
//        if (prefix == null) {
//            return "FR";
//        }
//        String p = prefix.trim().toUpperCase();
//        if (p.contains("FT")) {
//            return "FT";
//        }
//        if (p.contains("FR")) {
//            return "FR";
//        }
//        if (p.contains("NC")) {
//            return "NC";
//        }
//        if (p.contains("ND")) {
//            return "ND";
//        }
//        return "FR";
//    }
//
//    // PP/NE/FO… (ajusta às tuas séries de documentos de trabalho)
//    private static String inferWorkType(String prefix) {
//        if (prefix == null) {
//            return "PP";
//        }
//        String p = prefix.trim().toUpperCase();
//        if (p.contains("NE")) {
//            return "NE";
//        }
//        if (p.contains("FO")) {
//            return "FO";
//        }
//        return "PP";
//    }
//
//    // Mapeamento simples do TaxCode para Angola
//    private static String mapTaxCode(String code, BigDecimal perc, String name) {
//        BigDecimal p = perc == null ? BigDecimal.ZERO : perc;
//        if (approx(p, 14)) {
//            return "NOR";
//        }
//        if (approx(p, 7)) {
//            return "INT";
//        }
//        if (approx(p, 5)) {
//            return "RED";
//        }
//        if (approx(p, 0)) {
//            String n = (name == null ? "" : name.toUpperCase());
//            String c = (code == null ? "" : code.toUpperCase());
//            if (n.contains("ISENTO") || c.contains("ISE")) {
//                return "ISE";
//            }
//            if (n.contains("EXPORT") || c.contains("NS")) {
//                return "NS";
//            }
//            return "OUT";
//        }
//        if (code != null) {
//            String c = code.toUpperCase();
//            if (c.startsWith("NOR")) {
//                return "NOR";
//            }
//            if (c.startsWith("INT")) {
//                return "INT";
//            }
//            if (c.startsWith("RED")) {
//                return "RED";
//            }
//            if (c.startsWith("ISE")) {
//                return "ISE";
//            }
//            if (c.startsWith("NS")) {
//                return "NS";
//            }
//        }
//        return "OUT";
//    }
//
//    private static boolean approx(BigDecimal a, double b) {
//        if (a == null) {
//            a = BigDecimal.ZERO;
//        }
//        BigDecimal bdB = BigDecimal.valueOf(b);
//
//        BigDecimal diff = a.subtract(bdB).abs();
//        return diff.compareTo(new BigDecimal("0.001")) < 0;
//    }
//
//}
