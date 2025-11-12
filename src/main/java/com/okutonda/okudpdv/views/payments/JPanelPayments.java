/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.okutonda.okudpdv.views.payments;

import com.okutonda.okudpdv.controllers.PaymentController;
import com.okutonda.okudpdv.controllers.PurchasePaymentController;
import com.okutonda.okudpdv.data.entities.Payment;
import com.okutonda.okudpdv.data.entities.PaymentMode;
import com.okutonda.okudpdv.data.entities.PaymentStatus;
import com.okutonda.okudpdv.data.entities.Purchase;
import com.okutonda.okudpdv.data.entities.PurchasePayment;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hr
 */
public class JPanelPayments extends javax.swing.JPanel {

    private PaymentController paymentController = new PaymentController();
    private PurchasePaymentController purchasePaymentController = new PurchasePaymentController();
    private DefaultTableModel tableModelSales;
    private DefaultTableModel tableModelPurchase;

    /**
     * Creates new form JPanelPayments
     */
    public JPanelPayments() {
        initComponents();
        initTableModels();
        setupDateFields();
        loadPaymentModes();
        listPaymentsSales();
        listPaymentsPurchase();
    }

    /**
     * Inicializa os modelos da tabela
     */
    private void initTableModels() {
        // Modelo para vendas
        tableModelSales = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID", "Referência", "Data", "Descrição", "Total", "Modo Pagamento", "Status", "Cliente"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class,
                java.lang.String.class, java.lang.Double.class, java.lang.String.class,
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        jTablePaymentsSales.setModel(tableModelSales);

        // Modelo para compras (similar)
        tableModelPurchase = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID", "Referência", "Data", "Descrição", "Total", "Modo Pagamento", "Status", "Fornecedor"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class,
                java.lang.String.class, java.lang.Double.class, java.lang.String.class,
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        jTablePaymentsPurchase.setModel(tableModelPurchase);
    }

    /**
     * Configura as datas iniciais
     */
    private void setupDateFields() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Vendas
        jFormattedTextFieldFilterDaDataSales.setText(firstDayOfMonth.format(formatter));
        jFormattedTextFieldFilterAteDataSales.setText(today.format(formatter));

        // Compras
        jFormattedTextField1.setText(firstDayOfMonth.format(formatter));
        jFormattedTextField2.setText(today.format(formatter));
    }

    /**
     * Carrega os modos de pagamento nos combobox
     */
    private void loadPaymentModes() {
        jComboBoxFilterTipoDocumentSales.removeAllItems();
        jComboBoxTipoDocument.removeAllItems();

        // Adiciona opção "Todos"
        jComboBoxFilterTipoDocumentSales.addItem("Todos");
        jComboBoxTipoDocument.addItem("Todos");

        // Adiciona os modos de pagamento
        for (PaymentMode mode : paymentController.getModosPagamentoAtivos()) {
            jComboBoxFilterTipoDocumentSales.addItem(mode.getDescricao());
            jComboBoxTipoDocument.addItem(mode.getDescricao());
        }
    }

    /**
     * Lista pagamentos de vendas na tabela
     */
    public void listPaymentsSales() {
        List<Payment> list = paymentController.getAll();
        listTableSales(list);
    }

    /**
     * Preenche a tabela de vendas com dados
     */
    public void listTableSales(List<Payment> list) {
        tableModelSales.setRowCount(0);

        for (Payment payment : list) {
            tableModelSales.addRow(new Object[]{
                payment.getId(),
                //                payment.getReference(),
                payment.getInvoiceType(),
                payment.getDate(),
                payment.getDescription(),
                payment.getTotal(),
                payment.getPaymentMode() != null ? payment.getPaymentMode().getDescricao() : "N/A",
                getStatusText(payment.getStatus()),
                payment.getClient() != null ? payment.getClient().getName() : "N/A"
            });
        }

        adjustTableColumnsSales();
    }

    /**
     * Ajusta as colunas da tabela de vendas
     */
    private void adjustTableColumnsSales() {
        jTablePaymentsSales.getColumnModel().getColumn(0).setPreferredWidth(40);   // ID
        jTablePaymentsSales.getColumnModel().getColumn(1).setPreferredWidth(100);  // Referência
        jTablePaymentsSales.getColumnModel().getColumn(2).setPreferredWidth(80);   // Data
        jTablePaymentsSales.getColumnModel().getColumn(3).setPreferredWidth(150);  // Descrição
        jTablePaymentsSales.getColumnModel().getColumn(4).setPreferredWidth(80);   // Total
        jTablePaymentsSales.getColumnModel().getColumn(5).setPreferredWidth(100);  // Modo Pagamento
        jTablePaymentsSales.getColumnModel().getColumn(6).setPreferredWidth(80);   // Status
        jTablePaymentsSales.getColumnModel().getColumn(7).setPreferredWidth(120);  // Cliente
    }

    /**
     * Filtra pagamentos de vendas
     */
    public void filterPaymentsSales() {
        if (!validarDatasSales()) {
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateFrom = LocalDate.parse(jFormattedTextFieldFilterDaDataSales.getText(), formatter);
            LocalDate dateTo = LocalDate.parse(jFormattedTextFieldFilterAteDataSales.getText(), formatter);

            // Filtro por modo de pagamento
            String modoSelecionado = (String) jComboBoxFilterTipoDocumentSales.getSelectedItem();
            PaymentMode paymentMode = null;
            if (!"Todos".equals(modoSelecionado)) {
                paymentMode = paymentController.converterParaPaymentMode(modoSelecionado);
            }

            List<Payment> filteredList;
            if (paymentMode != null) {
                filteredList = paymentController.getByPaymentMode(paymentMode);
                // Filtra adicionalmente por data
                filteredList = filteredList.stream()
                        .filter(p -> isDataNoPeriodo(p.getDate(), dateFrom, dateTo))
                        .toList();
            } else {
                filteredList = paymentController.filterByDate(dateFrom, dateTo);
            }

            listTableSales(filteredList);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao filtrar pagamentos: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Valida as datas do filtro de vendas
     */
    private boolean validarDatasSales() {
        String dataInicio = jFormattedTextFieldFilterDaDataSales.getText();
        String dataFim = jFormattedTextFieldFilterAteDataSales.getText();

        if (dataInicio == null || dataInicio.trim().isEmpty()
                || dataFim == null || dataFim.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha ambas as datas para filtrar",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateFrom = LocalDate.parse(dataInicio, formatter);
            LocalDate dateTo = LocalDate.parse(dataFim, formatter);

            if (dateFrom.isAfter(dateTo)) {
                JOptionPane.showMessageDialog(this,
                        "Data de início não pode ser maior que data de fim",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }

            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Formato de data inválido. Use DD/MM/AAAA",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Obtém o pagamento selecionado na tabela de vendas
     */
    private Payment getPaymentSelecionadoSales() {
        int selectedRow = jTablePaymentsSales.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um pagamento da tabela",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }

        int modelRow = jTablePaymentsSales.convertRowIndexToModel(selectedRow);
        Integer paymentId = (Integer) tableModelSales.getValueAt(modelRow, 0);

        return paymentController.getById(paymentId);
    }

    /**
     * Visualiza pagamento de venda
     */
    private void visualizarPaymentSales() {
        Payment payment = getPaymentSelecionadoSales();
        if (payment == null) {
            return;
        }

        // TODO: Implementar visualização detalhada do pagamento
        JOptionPane.showMessageDialog(this,
                "Visualizando pagamento: " + payment.getReference()
                + "\nValor: " + payment.getTotal()
                + "\nModo: " + (payment.getPaymentMode() != null ? payment.getPaymentMode().getDescricao() : "N/A"),
                "Detalhes do Pagamento",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Imprime pagamento de venda
     */
    private void imprimirPaymentSales() {
        Payment payment = getPaymentSelecionadoSales();
        if (payment == null) {
            return;
        }

        // TODO: Implementar impressão do pagamento
        JOptionPane.showMessageDialog(this,
                "Funcionalidade de impressão será implementada em breve\n"
                + "Pagamento: " + payment.getReference(),
                "Impressão",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Converte status para texto
     */
    private String getStatusText(PaymentStatus status) {
        if (status == null) {
            return "DESCONHECIDO";
        }

        switch (status) {
            case PENDENTE:
                return "PENDENTE";
            case PAGO:
                return "PAGO";
            case CANCELADO:
                return "CANCELADO";
            default:
                return "DESCONHECIDO";
        }
    }

    /**
     * Verifica se data está no período
     */
    private boolean isDataNoPeriodo(String dataString, LocalDate from, LocalDate to) {
        try {
            if (dataString == null || dataString.trim().isEmpty()) {
                return false;
            }
            LocalDate data = LocalDate.parse(dataString.substring(0, 10));
            return !data.isBefore(from) && !data.isAfter(to);
        } catch (Exception e) {
            return false;
        }
    }

    // ==========================================================
    // MÉTODOS PARA COMPRAS (estrutura similar)
    // ==========================================================
    /**
     * Lista pagamentos de compras na tabela - IMPLEMENTADO
     */
    public void listPaymentsPurchase() {
        try {
            List<PurchasePayment> list = purchasePaymentController.listarTodos();
            listTablePurchase(list);
        } catch (Exception e) {
            System.err.println("❌ Erro ao listar pagamentos de compras: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar pagamentos de compras: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Preenche a tabela de compras com dados - IMPLEMENTADO
     */
    public void listTablePurchase(List<PurchasePayment> list) {
        tableModelPurchase.setRowCount(0);

        for (PurchasePayment payment : list) {
            tableModelPurchase.addRow(new Object[]{
                payment.getId(),
                payment.getReferencia(),
                payment.getDataPagamento(),
                payment.getDescricao(),
                payment.getValorPago(),
                payment.getMetodoDescricao(),
                getStatusText(payment.getStatus()),
                getFornecedorNome(payment.getPurchase())
            });
        }

        adjustTableColumnsPurchase();
    }

    /**
     * Ajusta as colunas da tabela de compras - IMPLEMENTADO
     */
    private void adjustTableColumnsPurchase() {
        jTablePaymentsPurchase.getColumnModel().getColumn(0).setPreferredWidth(40);   // ID
        jTablePaymentsPurchase.getColumnModel().getColumn(1).setPreferredWidth(100);  // Referência
        jTablePaymentsPurchase.getColumnModel().getColumn(2).setPreferredWidth(80);   // Data
        jTablePaymentsPurchase.getColumnModel().getColumn(3).setPreferredWidth(150);  // Descrição
        jTablePaymentsPurchase.getColumnModel().getColumn(4).setPreferredWidth(80);   // Total
        jTablePaymentsPurchase.getColumnModel().getColumn(5).setPreferredWidth(100);  // Modo Pagamento
        jTablePaymentsPurchase.getColumnModel().getColumn(6).setPreferredWidth(80);   // Status
        jTablePaymentsPurchase.getColumnModel().getColumn(7).setPreferredWidth(120);  // Fornecedor
    }

    /**
     * Filtra pagamentos de compras - IMPLEMENTADO
     */
    public void filterPaymentsPurchase() {
        if (!validarDatasPurchase()) {
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateFrom = LocalDate.parse(jFormattedTextField1.getText(), formatter);
            LocalDate dateTo = LocalDate.parse(jFormattedTextField2.getText(), formatter);

            // Filtro por modo de pagamento
            String modoSelecionado = (String) jComboBoxTipoDocument.getSelectedItem();
            PaymentMode paymentMode = null;
            if (!"Todos".equals(modoSelecionado)) {
                paymentMode = purchasePaymentController.converterParaPaymentMode(modoSelecionado);
            }

            List<PurchasePayment> filteredList;
            if (paymentMode != null) {
                filteredList = purchasePaymentController.listarPorModoPagamento(paymentMode);
                // Filtra adicionalmente por data
                filteredList = filteredList.stream()
                        .filter(p -> isDataNoPeriodo(p.getDataPagamento().toString(), dateFrom, dateTo))
                        .toList();
            } else {
                filteredList = purchasePaymentController.listarPorPeriodo(dateFrom, dateTo);
            }

            listTablePurchase(filteredList);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao filtrar pagamentos de compras: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Valida as datas do filtro de compras - IMPLEMENTADO
     */
    private boolean validarDatasPurchase() {
        String dataInicio = jFormattedTextField1.getText();
        String dataFim = jFormattedTextField2.getText();

        if (dataInicio == null || dataInicio.trim().isEmpty()
                || dataFim == null || dataFim.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha ambas as datas para filtrar",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateFrom = LocalDate.parse(dataInicio, formatter);
            LocalDate dateTo = LocalDate.parse(dataFim, formatter);

            if (dateFrom.isAfter(dateTo)) {
                JOptionPane.showMessageDialog(this,
                        "Data de início não pode ser maior que data de fim",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }

            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Formato de data inválido. Use DD/MM/AAAA",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Obtém o nome do fornecedor - IMPLEMENTADO
     */
    private String getFornecedorNome(Purchase purchase) {
        if (purchase == null || purchase.getSupplier() == null) {
            return "N/A";
        }
        return purchase.getSupplier().getName();
    }

    /**
     * Obtém o pagamento selecionado na tabela de compras - IMPLEMENTADO
     */
    private PurchasePayment getPaymentSelecionadoPurchase() {
        int selectedRow = jTablePaymentsPurchase.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um pagamento da tabela de compras",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }

        int modelRow = jTablePaymentsPurchase.convertRowIndexToModel(selectedRow);
        Integer paymentId = (Integer) tableModelPurchase.getValueAt(modelRow, 0);

        return purchasePaymentController.buscarPorId(paymentId);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jFormattedTextFieldFilterDaDataSales = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        jFormattedTextFieldFilterAteDataSales = new javax.swing.JFormattedTextField();
        jComboBoxFilterTipoDocumentSales = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTablePaymentsSales = new javax.swing.JTable();
        jButtonFilterSales = new javax.swing.JButton();
        jButtonViewPaymentSales = new javax.swing.JButton();
        jButtonPrintPaymentsSales = new javax.swing.JButton();
        jComboBoxFilterClienteSales = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePaymentsPurchase = new javax.swing.JTable();
        jComboBoxTipoDocument = new javax.swing.JComboBox<>();
        jButtonViewPaymentPurchase = new javax.swing.JButton();
        jButtonPrintPaymentsPurchase = new javax.swing.JButton();
        jButtonFilterPurchasePayment = new javax.swing.JButton();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        jLabel3.setText("Da data");

        jFormattedTextFieldFilterDaDataSales.setText("jFormattedTextField1");
        jFormattedTextFieldFilterDaDataSales.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFormattedTextFieldFilterDaDataSalesKeyPressed(evt);
            }
        });

        jLabel4.setText("Ate a data");

        jFormattedTextFieldFilterAteDataSales.setText("jFormattedTextField1");
        jFormattedTextFieldFilterAteDataSales.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFormattedTextFieldFilterAteDataSalesKeyPressed(evt);
            }
        });

        jComboBoxFilterTipoDocumentSales.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTablePaymentsSales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTablePaymentsSales);

        jButtonFilterSales.setText("Filtrar");
        jButtonFilterSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFilterSalesActionPerformed(evt);
            }
        });

        jButtonViewPaymentSales.setText("Visualizar");
        jButtonViewPaymentSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewPaymentSalesActionPerformed(evt);
            }
        });

        jButtonPrintPaymentsSales.setText("Imprimir");
        jButtonPrintPaymentsSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrintPaymentsSalesActionPerformed(evt);
            }
        });

        jComboBoxFilterClienteSales.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setText("Tipo de Documento");

        jLabel6.setText("Cliente");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jFormattedTextFieldFilterDaDataSales, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextFieldFilterAteDataSales, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxFilterTipoDocumentSales, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jComboBoxFilterClienteSales, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonFilterSales)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 277, Short.MAX_VALUE)
                                .addComponent(jButtonViewPaymentSales)
                                .addGap(37, 37, 37)
                                .addComponent(jButtonPrintPaymentsSales)
                                .addGap(18, 18, 18))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jFormattedTextFieldFilterAteDataSales, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonViewPaymentSales, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonPrintPaymentsSales, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonFilterSales, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jFormattedTextFieldFilterDaDataSales, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBoxFilterTipoDocumentSales, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBoxFilterClienteSales, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Pagamentos de Vendas", jPanel1);

        jTablePaymentsPurchase.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTablePaymentsPurchase);

        jComboBoxTipoDocument.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButtonViewPaymentPurchase.setText("Visualizar");
        jButtonViewPaymentPurchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewPaymentPurchaseActionPerformed(evt);
            }
        });

        jButtonPrintPaymentsPurchase.setText("Imprimir");
        jButtonPrintPaymentsPurchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrintPaymentsPurchaseActionPerformed(evt);
            }
        });

        jButtonFilterPurchasePayment.setText("Filtrar");
        jButtonFilterPurchasePayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFilterPurchasePaymentActionPerformed(evt);
            }
        });

        jFormattedTextField1.setText("jFormattedTextField1");

        jFormattedTextField2.setText("jFormattedTextField1");

        jLabel1.setText("Da data");

        jLabel2.setText("Ate a data");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBoxTipoDocument, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 179, Short.MAX_VALUE)
                                .addComponent(jButtonFilterPurchasePayment)
                                .addGap(162, 162, 162)
                                .addComponent(jButtonViewPaymentPurchase)
                                .addGap(37, 37, 37)
                                .addComponent(jButtonPrintPaymentsPurchase)
                                .addGap(18, 18, 18))))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jFormattedTextField2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonViewPaymentPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonPrintPaymentsPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonFilterPurchasePayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBoxTipoDocument, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Pagamentos Compras", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonFilterSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFilterSalesActionPerformed
        // TODO add your handling code here:
        filterPaymentsSales();
    }//GEN-LAST:event_jButtonFilterSalesActionPerformed

    private void jButtonViewPaymentSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewPaymentSalesActionPerformed
        // TODO add your handling code here:
        visualizarPaymentSales();
    }//GEN-LAST:event_jButtonViewPaymentSalesActionPerformed

    private void jButtonPrintPaymentsSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrintPaymentsSalesActionPerformed
        // TODO add your handling code here:
        imprimirPaymentSales();
    }//GEN-LAST:event_jButtonPrintPaymentsSalesActionPerformed

    private void jFormattedTextFieldFilterDaDataSalesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextFieldFilterDaDataSalesKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filterPaymentsSales();
        }
    }//GEN-LAST:event_jFormattedTextFieldFilterDaDataSalesKeyPressed

    private void jFormattedTextFieldFilterAteDataSalesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextFieldFilterAteDataSalesKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filterPaymentsSales();
        }
    }//GEN-LAST:event_jFormattedTextFieldFilterAteDataSalesKeyPressed

    private void jButtonViewPaymentPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewPaymentPurchaseActionPerformed
        // TODO add your handling code here:
        PurchasePayment payment = getPaymentSelecionadoPurchase();
        if (payment == null) {
            return;
        }

        // Visualização detalhada do pagamento de compra
        String detalhes = String.format(
                "Detalhes do Pagamento (Compra):\n"
                + "Referência: %s\n"
                + "Valor: %.2f AOA\n"
                + "Data: %s\n"
                + "Modo: %s\n"
                + "Status: %s\n"
                + "Fornecedor: %s\n"
                + "Descrição: %s",
                payment.getReferencia(),
                payment.getValorPago().doubleValue(),
                payment.getDataPagamento(),
                payment.getMetodoDescricao(),
                payment.getStatusDescricao(),
                getFornecedorNome(payment.getPurchase()),
                payment.getDescricao() != null ? payment.getDescricao() : "N/A"
        );

        JOptionPane.showMessageDialog(this,
                detalhes,
                "Detalhes do Pagamento - Compra",
                JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButtonViewPaymentPurchaseActionPerformed

    private void jButtonPrintPaymentsPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrintPaymentsPurchaseActionPerformed
        // TODO add your handling code here:
        PurchasePayment payment = getPaymentSelecionadoPurchase();
        if (payment == null) {
            return;
        }

        // TODO: Implementar impressão do pagamento de compra
        JOptionPane.showMessageDialog(this,
                "Funcionalidade de impressão será implementada em breve\n"
                + "Pagamento (Compra): " + payment.getReferencia(),
                "Impressão - Compra",
                JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButtonPrintPaymentsPurchaseActionPerformed

    private void jButtonFilterPurchasePaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFilterPurchasePaymentActionPerformed
        // TODO add your handling code here:
        filterPaymentsPurchase();
    }//GEN-LAST:event_jButtonFilterPurchasePaymentActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonFilterPurchasePayment;
    private javax.swing.JButton jButtonFilterSales;
    private javax.swing.JButton jButtonPrintPaymentsPurchase;
    private javax.swing.JButton jButtonPrintPaymentsSales;
    private javax.swing.JButton jButtonViewPaymentPurchase;
    private javax.swing.JButton jButtonViewPaymentSales;
    private javax.swing.JComboBox<String> jComboBoxFilterClienteSales;
    private javax.swing.JComboBox<String> jComboBoxFilterTipoDocumentSales;
    private javax.swing.JComboBox<String> jComboBoxTipoDocument;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JFormattedTextField jFormattedTextFieldFilterAteDataSales;
    private javax.swing.JFormattedTextField jFormattedTextFieldFilterDaDataSales;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTablePaymentsPurchase;
    private javax.swing.JTable jTablePaymentsSales;
    // End of variables declaration//GEN-END:variables
}
