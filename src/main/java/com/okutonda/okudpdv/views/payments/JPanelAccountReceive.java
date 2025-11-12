/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.okutonda.okudpdv.views.payments;

import com.okutonda.okudpdv.controllers.FinanceController;
import com.okutonda.okudpdv.data.entities.Order;
import com.okutonda.okudpdv.ui.TemaUI;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hr
 */
public class JPanelAccountReceive extends javax.swing.JPanel {

    private FinanceController financeController = new FinanceController();
    private DefaultTableModel tableModel;
    private List<Order> currentOrders;

    /**
     * Creates new form JPanelAccountReceive
     */
    public JPanelAccountReceive() {
        initComponents();
        applyTheme();
        initTableModel();
        setupDateFields();
        listContasReceber();
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
//        TemaUI.aplicarBotaoPrimario(jButton4);
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
                    "Cliente", "Nº Fatura", "Emissão", "Vencimento",
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
        jTableFinanceReceive.setModel(tableModel);
    }

    /**
     * Configura as datas iniciais
     */
    private void setupDateFields() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        jFormattedTextField5.setText(firstDayOfMonth.format(formatter));
        jFormattedTextField6.setText(today.format(formatter));
    }

    /**
     * Lista contas a receber
     */
    public void listContasReceber() {
        currentOrders = financeController.getContasAReceber();
        loadListReceber(currentOrders);
    }

    /**
     * Carrega dados na tabela
     */
    public void loadListReceber(List<Order> list) {
        tableModel.setRowCount(0);

        LocalDate hoje = LocalDate.now();

        for (Order o : list) {
            BigDecimal total = o.getTotal() != null ? o.getTotal() : BigDecimal.ZERO;
            BigDecimal pago = o.getPayTotal() != null ? o.getPayTotal() : BigDecimal.ZERO;
            BigDecimal emAberto = total.subtract(pago);

            LocalDate emissao = LocalDate.parse(o.getDatecreate().substring(0, 10));
            LocalDate vencimento = emissao.plusDays(30); // simulação
            int diasAtraso = hoje.isAfter(vencimento)
                    ? (int) ChronoUnit.DAYS.between(vencimento, hoje)
                    : 0;

            String status;
            if (emAberto.compareTo(BigDecimal.ZERO) <= 0) {
                status = "Pago";
            } else if (pago.compareTo(BigDecimal.ZERO) > 0 && emAberto.compareTo(BigDecimal.ZERO) > 0) {
                status = "Parcialmente paga";
            } else if (diasAtraso > 0) {
                status = "Vencida";
            } else {
                status = "Em aberto";
            }

            tableModel.addRow(new Object[]{
                (o.getClient() != null ? o.getClient().getName() : ""),
                o.getPrefix() + "-" + o.getNumber(),
                emissao.toString(),
                vencimento.toString(),
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
        jTableFinanceReceive.getColumnModel().getColumn(0).setPreferredWidth(150); // Cliente
        jTableFinanceReceive.getColumnModel().getColumn(1).setPreferredWidth(100); // Nº Fatura
        jTableFinanceReceive.getColumnModel().getColumn(2).setPreferredWidth(80);  // Emissão
        jTableFinanceReceive.getColumnModel().getColumn(3).setPreferredWidth(80);  // Vencimento
        jTableFinanceReceive.getColumnModel().getColumn(4).setPreferredWidth(80);  // Valor Total
        jTableFinanceReceive.getColumnModel().getColumn(5).setPreferredWidth(80);  // Pago
        jTableFinanceReceive.getColumnModel().getColumn(6).setPreferredWidth(80);  // Em Aberto
        jTableFinanceReceive.getColumnModel().getColumn(7).setPreferredWidth(100); // Status
        jTableFinanceReceive.getColumnModel().getColumn(8).setPreferredWidth(60);  // Dias Atraso
    }

    /**
     * Filtra contas a receber
     */
    public void filterContasReceber() {
        if (!validarDatas()) {
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateFrom = LocalDate.parse(jFormattedTextField5.getText(), formatter);
            LocalDate dateTo = LocalDate.parse(jFormattedTextField6.getText(), formatter);

            // Filtro por status
            String statusSelecionado = (String) jComboBox3.getSelectedItem();
            List<Order> filteredList = currentOrders.stream()
                    .filter(o -> filtrarPorStatus(o, statusSelecionado))
                    .filter(o -> filtrarPorData(o, dateFrom, dateTo))
                    .toList();

            loadListReceber(filteredList);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao filtrar contas a receber: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Filtra por status
     */
    private boolean filtrarPorStatus(Order order, String status) {
        if ("Todos".equals(status)) {
            return true;
        }

        BigDecimal total = order.getTotal() != null ? order.getTotal() : BigDecimal.ZERO;
        BigDecimal pago = order.getPayTotal() != null ? order.getPayTotal() : BigDecimal.ZERO;
        BigDecimal emAberto = total.subtract(pago);

        LocalDate hoje = LocalDate.now();
        LocalDate emissao = LocalDate.parse(order.getDatecreate().substring(0, 10));
        LocalDate vencimento = emissao.plusDays(30);
        boolean vencida = hoje.isAfter(vencimento);

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
    private boolean filtrarPorData(Order order, LocalDate dateFrom, LocalDate dateTo) {
        try {
            LocalDate dataEmissao = LocalDate.parse(order.getDatecreate().substring(0, 10));
            return !dataEmissao.isBefore(dateFrom) && !dataEmissao.isAfter(dateTo);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida as datas
     */
    private boolean validarDatas() {
        String dataInicio = jFormattedTextField5.getText();
        String dataFim = jFormattedTextField6.getText();

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
     * Obtém o pedido selecionado
     */
    private Order getOrderSelecionada() {
        int selectedRow = jTableFinanceReceive.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma conta da tabela",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }

        int modelRow = jTableFinanceReceive.convertRowIndexToModel(selectedRow);
        String numeroFatura = (String) tableModel.getValueAt(modelRow, 1);

        return currentOrders.stream()
                .filter(o -> (o.getPrefix() + "-" + o.getNumber()).equals(numeroFatura))
                .findFirst()
                .orElse(null);
    }

    /**
     * Processa recebimento da conta
     */
    private void receberConta() {
        Order order = getOrderSelecionada();
        if (order == null) {
            return;
        }

        BigDecimal total = order.getTotal() != null ? order.getTotal() : BigDecimal.ZERO;
        BigDecimal pago = order.getPayTotal() != null ? order.getPayTotal() : BigDecimal.ZERO;
        BigDecimal saldo = total.subtract(pago);

        if (saldo.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Esta conta já está totalmente recebida",
                    "Informação",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // TODO: Implementar diálogo de recebimento
        JOptionPane.showMessageDialog(this,
                "Funcionalidade de recebimento será implementada em breve\n"
                + "Conta: " + order.getPrefix() + "/" + order.getNumber() + "\n"
                + "Saldo: " + saldo + " AOA",
                "Recebimento",
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableFinanceReceive = new javax.swing.JTable();
        jFormattedTextField5 = new javax.swing.JFormattedTextField();
        jFormattedTextField6 = new javax.swing.JFormattedTextField();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jComboBoxPaymentFromClient = new javax.swing.JComboBox();
        jLabel19 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        jTableFinanceReceive.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Descricao", "Total", "Data de Final", "Cliente", "Estado do pagamento"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTableFinanceReceive);

        try {
            jFormattedTextField5.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFormattedTextField5KeyPressed(evt);
            }
        });

        try {
            jFormattedTextField6.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFormattedTextField6KeyPressed(evt);
            }
        });

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "pagas", "pedentes", "parcial", "vencidas" }));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });
        jComboBox3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBox3KeyPressed(evt);
            }
        });

        jLabel14.setText("Da data");

        jLabel15.setText("Ate a data");

        jLabel19.setText("Por Cliente");

        jLabel28.setText("Status");

        jButton1.setText("Receber conta");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(63, 63, 63))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jFormattedTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jComboBoxPaymentFromClient, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(jButton1)))
                        .addGap(0, 319, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane4)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel28)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxPaymentFromClient, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        receberConta();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3KeyPressed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
        filterContasReceber();
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jFormattedTextField5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField5KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filterContasReceber();
        }
    }//GEN-LAST:event_jFormattedTextField5KeyPressed

    private void jFormattedTextField6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField6KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filterContasReceber();
        }
    }//GEN-LAST:event_jFormattedTextField6KeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox jComboBoxPaymentFromClient;
    private javax.swing.JFormattedTextField jFormattedTextField5;
    private javax.swing.JFormattedTextField jFormattedTextField6;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTableFinanceReceive;
    // End of variables declaration//GEN-END:variables
}
