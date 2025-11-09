/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.okutonda.okudpdv.views.payments;

import com.okutonda.okudpdv.controllers.FinanceController;
import com.okutonda.okudpdv.controllers.PurchasePaymentController;
import com.okutonda.okudpdv.data.entities.Purchase;
import com.okutonda.okudpdv.ui.TemaUI;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hr
 */
public class JPanelAccountPay extends javax.swing.JPanel {

    private FinanceController financeController = new FinanceController();
    private PurchasePaymentController purchasePaymentController = new PurchasePaymentController();
    private DefaultTableModel tableModel;
    private List<Purchase> currentPurchases;

    /**
     * Creates new form JPanelAccountPay
     */
    public JPanelAccountPay() {
        initComponents();
        applyTheme();
        initTableModel();
        setupDateFields();
        listContasPagar();
    }

    private void applyTheme() {
//        TemaCleaner.clearBuilderOverrides(getContentPane());
        // Painel de fundo da janela
//        jPanelSidebar.setBackground(TemaCores.BG_LIGHT);

//        TemaUI.aplicarTitulo(jLabelJpanelSelected);
//        jPanelSidebar.setBackground(TemaCores.PRIMARY);
        // Card do login
//        TemaUI.aplicarPainelHeader(jPanelSidebar, TemaCores.PRIMARY);
        // Título
//        TemaUI.aplicarTitulo(jLabelNameCompany);
//        jLabelNameCompany.setForeground(TemaCores.PRIMARY);
        // Labels
//        jLabel1.setForeground(TemaCores.TEXT_DARK);   // "Email:"
//        jLabel2.setForeground(TemaCores.TEXT_DARK);   // "Senha:"
        // Campos
//        TemaUI.aplicarCampoTexto(jTextFieldEmail);
//        TemaUI.aplicarCampoTexto(jPasswordFieldPassword);
        // Botões
//        TemaUI.aplicarBotaoPrimario(jButton1);
//        TemaUI.aplicarBotaoPrimario(jButton2);
//        TemaUI.aplicarBotaoPrimario(jButton3);
        TemaUI.aplicarBotaoPrimario(jButton4);
//        TemaUI.aplicarBotaoPrimario(jButtonInventoryReport);

//        TemaUI.aplicarBotaoPrimario(jButtonLogin);
//        jButtonSuport.setForeground(TemaCores.TEXT_GRAY);
//        jButtonAbout.setForeground(TemaCores.TEXT_GRAY);
//        jButtonInstall.setForeground(TemaCores.PRIMARY);
//        jButtonCloseScreen.setForeground(TemaCores.ERROR);
        // Status de BD (cor dinâmica) — chama depois de testar a conexão
//        updateDbStatusLabel(this.conn != null);
        // Borda superior/rodapé (opcional)
        // getRootPane().setBorder(new javax.swing.border.MatteBorder(0, 0, 2, 0, TemaCores.PRIMARY));
        // Se o GUI Builder deixou cores hardcoded em initComponents,
        // isso aqui sobrescreve. Se puder, remova as cores fixas no builder.
    }

    /**
     * Inicializa o modelo da tabela
     */
    private void initTableModel() {
        tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Fornecedor", "Nº Documento", "Data Compra", "Vencimento",
                    "Valor Total", "Pago", "Em Aberto", "Status", "Dias Atraso"
                }
        ) {
            Class[] types = new Class[]{
                String.class, String.class, String.class, String.class,
                Double.class, Double.class, Double.class, String.class, Integer.class
            };
            boolean[] canEdit = new boolean[]{false, false, false, false, false, false, false, false, false};

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        jTableFinancePayable.setModel(tableModel);
    }

    /**
     * Configura as datas iniciais
     */
    private void setupDateFields() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        jFormattedTextField3.setText(firstDayOfMonth.format(formatter));
        jFormattedTextField4.setText(today.format(formatter));
    }

    /**
     * Lista contas a pagar
     */
    public void listContasPagar() {
        currentPurchases = financeController.getContasAPagar();
        loadListPagar(currentPurchases);
    }

    /**
     * Carrega dados na tabela
     */
    public void loadListPagar(List<Purchase> list) {
        tableModel.setRowCount(0);

        LocalDate hoje = LocalDate.now();

        for (Purchase p : list) {
            BigDecimal total = p.getTotal() != null ? p.getTotal() : BigDecimal.ZERO;
            BigDecimal pago = p.getTotal_pago() != null ? p.getTotal_pago() : BigDecimal.ZERO;
            BigDecimal emAberto = total.subtract(pago);

            // Status
            String status;
            if (emAberto.compareTo(BigDecimal.ZERO) <= 0) {
                status = "Pago";
            } else if (pago.compareTo(BigDecimal.ZERO) > 0 && emAberto.compareTo(BigDecimal.ZERO) > 0) {
                status = "Parcial";
            } else {
                status = "Em aberto";
            }

            // Vencimento e dias em atraso
            int diasAtraso = 0;
            if (p.getDataVencimento() != null) {
                LocalDate vencimento = p.getDataVencimento().toInstant()
                        .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                if (hoje.isAfter(vencimento)) {
                    diasAtraso = (int) java.time.temporal.ChronoUnit.DAYS.between(vencimento, hoje);
                    if (emAberto.compareTo(BigDecimal.ZERO) > 0) {
                        status = "Vencida";
                    }
                }
            }

            tableModel.addRow(new Object[]{
                (p.getSupplier() != null ? p.getSupplier().getName() : ""),
                p.getInvoiceNumber(),
                p.getDataCompra(),
                p.getDataVencimento(),
                total.doubleValue(),
                pago.doubleValue(),
                emAberto.doubleValue(),
                status,
                diasAtraso
            });
        }

        adjustTableColumns();
    }

    /**
     * Ajusta as colunas da tabela
     */
    private void adjustTableColumns() {
        jTableFinancePayable.getColumnModel().getColumn(0).setPreferredWidth(150); // Fornecedor
        jTableFinancePayable.getColumnModel().getColumn(1).setPreferredWidth(100); // Nº Documento
        jTableFinancePayable.getColumnModel().getColumn(2).setPreferredWidth(80);  // Data Compra
        jTableFinancePayable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Vencimento
        jTableFinancePayable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Valor Total
        jTableFinancePayable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Pago
        jTableFinancePayable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Em Aberto
        jTableFinancePayable.getColumnModel().getColumn(7).setPreferredWidth(80);  // Status
        jTableFinancePayable.getColumnModel().getColumn(8).setPreferredWidth(60);  // Dias Atraso
    }

    /**
     * Filtra contas a pagar
     */
    public void filterContasPagar() {
        if (!validarDatas()) {
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateFrom = LocalDate.parse(jFormattedTextField3.getText(), formatter);
            LocalDate dateTo = LocalDate.parse(jFormattedTextField4.getText(), formatter);

            // Filtro por status
            String statusSelecionado = (String) jComboBox2.getSelectedItem();
            List<Purchase> filteredList = currentPurchases.stream()
                    .filter(p -> filtrarPorStatus(p, statusSelecionado))
                    .filter(p -> filtrarPorData(p, dateFrom, dateTo))
                    .toList();

            loadListPagar(filteredList);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao filtrar contas a pagar: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Filtra por status
     */
    private boolean filtrarPorStatus(Purchase purchase, String status) {
        if ("Todos".equals(status)) {
            return true;
        }

        BigDecimal total = purchase.getTotal() != null ? purchase.getTotal() : BigDecimal.ZERO;
        BigDecimal pago = purchase.getTotal_pago() != null ? purchase.getTotal_pago() : BigDecimal.ZERO;
        BigDecimal emAberto = total.subtract(pago);

        LocalDate hoje = LocalDate.now();
        boolean vencida = purchase.getDataVencimento() != null
                && hoje.isAfter(purchase.getDataVencimento().toInstant()
                        .atZone(java.time.ZoneId.systemDefault()).toLocalDate());

        switch (status) {
            case "pagas":
                return emAberto.compareTo(BigDecimal.ZERO) <= 0;
            case "pedentes":
                return emAberto.compareTo(BigDecimal.ZERO) > 0 && !vencida;
            case "parcial":
                return pago.compareTo(BigDecimal.ZERO) > 0 && emAberto.compareTo(BigDecimal.ZERO) > 0;
            case "vencidas":
                return vencida && emAberto.compareTo(BigDecimal.ZERO) > 0;
            default:
                return true;
        }
    }

    /**
     * Filtra por data
     */
    private boolean filtrarPorData(Purchase purchase, LocalDate dateFrom, LocalDate dateTo) {
        try {
            if (purchase.getDataCompra() == null) {
                return false;
            }
            LocalDate dataCompra = purchase.getDataCompra().toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            return !dataCompra.isBefore(dateFrom) && !dataCompra.isAfter(dateTo);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida as datas
     */
    private boolean validarDatas() {
        String dataInicio = jFormattedTextField3.getText();
        String dataFim = jFormattedTextField4.getText();

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
     * Obtém a compra selecionada
     */
    private Purchase getPurchaseSelecionada() {
        int selectedRow = jTableFinancePayable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma conta da tabela",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }

        int modelRow = jTableFinancePayable.convertRowIndexToModel(selectedRow);
        String numeroDocumento = (String) tableModel.getValueAt(modelRow, 1);

        return currentPurchases.stream()
                .filter(p -> p.getInvoiceNumber().equals(numeroDocumento))
                .findFirst()
                .orElse(null);
    }

    /**
     * Processa pagamento da conta
     */
    private void pagarConta() {
        Purchase purchase = getPurchaseSelecionada();
        if (purchase == null) {
            return;
        }

        BigDecimal total = purchase.getTotal() != null ? purchase.getTotal() : BigDecimal.ZERO;
        BigDecimal pago = purchase.getTotal_pago() != null ? purchase.getTotal_pago() : BigDecimal.ZERO;
        BigDecimal saldo = total.subtract(pago);

        if (saldo.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Esta conta já está totalmente paga",
                    "Informação",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // TODO: Implementar diálogo de pagamento
        JOptionPane.showMessageDialog(this,
                "Funcionalidade de pagamento será implementada em breve\n"
                + "Conta: " + purchase.getInvoiceNumber() + "\n"
                + "Saldo: " + saldo + " AOA",
                "Pagamento",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableFinancePayable = new javax.swing.JTable();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jFormattedTextField4 = new javax.swing.JFormattedTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jComboBoxPaymentFromClient1 = new javax.swing.JComboBox();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();

        jTableFinancePayable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Descrição", "Produto", "Qtd", "Total", "Vencimento", "Comprador", "Fornecedor", "Pagamento", "Data Fabrico", "Data expiração"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTableFinancePayable);

        try {
            jFormattedTextField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFormattedTextField3KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField3KeyReleased(evt);
            }
        });

        try {
            jFormattedTextField4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFormattedTextField4KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField4KeyReleased(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "pagas", "pedentes", "parcial", "vencidas" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel5.setText("Da data");

        jLabel6.setText("Ate a data");

        jLabel29.setText("Fornecedor");

        jLabel30.setText("Status");

        jButton4.setText("Pagar conta");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jComboBoxPaymentFromClient1, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton4)))
                        .addGap(0, 385, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel30)
                        .addComponent(jLabel29))
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxPaymentFromClient1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1118, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 603, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        pagarConta();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
        filterContasPagar();
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jFormattedTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField3KeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_jFormattedTextField3KeyReleased

    private void jFormattedTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField4KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField4KeyReleased

    private void jFormattedTextField3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField3KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filterContasPagar();
        }
    }//GEN-LAST:event_jFormattedTextField3KeyPressed

    private void jFormattedTextField4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField4KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filterContasPagar();
        }
    }//GEN-LAST:event_jFormattedTextField4KeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox jComboBoxPaymentFromClient1;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JFormattedTextField jFormattedTextField4;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTableFinancePayable;
    // End of variables declaration//GEN-END:variables
}
