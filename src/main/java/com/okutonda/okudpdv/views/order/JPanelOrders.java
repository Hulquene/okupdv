/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.okutonda.okudpdv.views.order;

import com.okutonda.okudpdv.controllers.OrderController;
import com.okutonda.okudpdv.data.entities.Order;
import com.okutonda.okudpdv.data.entities.PaymentStatus;
import com.okutonda.okudpdv.helpers.OrderPrintHelper;
import com.okutonda.okudpdv.helpers.PrintHelper;
import com.okutonda.okudpdv.views.sales.JDialogGenerateNoteCredit;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hr
 */
public class JPanelOrders extends javax.swing.JPanel {

    OrderController orderController = new OrderController();
    private DefaultTableModel tableModel;

    /**
     * Creates new form JPanelOrders
     */
    public JPanelOrders() {
        initComponents();
//        listOrder();
        initTableModel();
        listOrder();
        setupDateFields();
    }

    /**
     * Inicializa o modelo da tabela
     */
    private void initTableModel() {
        tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID", "Pedido", "Data", "Total", "Cliente", "Vendedor", "Status"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class,
                java.lang.Double.class, java.lang.String.class, java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false
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
        jTableOrders.setModel(tableModel);
    }

    /**
     * Configura as datas iniciais nos campos de data
     */
    private void setupDateFields() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        jFormattedTextFieldDateStart.setText(firstDayOfMonth.format(formatter));
        jFormattedTextFieldDateFinish.setText(today.format(formatter));
    }

    public void listTable(List<Order> list) {
        tableModel.setRowCount(0);

        for (Order order : list) {
            tableModel.addRow(new Object[]{
                order.getId(),
                PrintHelper.formatDocumentNumber(order.getNumber(), order.getYear(), order.getPrefix()),
                order.getDatecreate(),
                order.getTotal(),
                order.getClient() != null ? order.getClient().getName() : "CONSUMIDOR FINAL",
                order.getSeller() != null ? order.getSeller().getName() : "N/A",
                getStatusText(order.getStatus())
            });
        }

        // Ajusta as colunas após carregar os dados
        adjustTableColumns();
    }

    /**
     * Ajusta o tamanho das colunas da tabela
     */
    private void adjustTableColumns() {
        // Ajusta as larguras das colunas
        jTableOrders.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
        jTableOrders.getColumnModel().getColumn(1).setPreferredWidth(100); // Pedido
        jTableOrders.getColumnModel().getColumn(2).setPreferredWidth(80);  // Data
        jTableOrders.getColumnModel().getColumn(3).setPreferredWidth(80);  // Total
        jTableOrders.getColumnModel().getColumn(4).setPreferredWidth(150); // Cliente
        jTableOrders.getColumnModel().getColumn(5).setPreferredWidth(120); // Vendedor
        jTableOrders.getColumnModel().getColumn(6).setPreferredWidth(80);  // Status
    }

    public void listOrder() {
        List<Order> list = orderController.getAll();
        listTable(list);
    }

    public void filterListOrder(String txt) {
        List<Order> list = orderController.filter(txt);
        listTable(list);
    }

    public void filterListOrderFromDate(LocalDate dateFrom, LocalDate dateTo) {
        List<Order> list = orderController.filterByDate(dateFrom, dateTo);
        listTable(list);
    }

    private Order getOrderSelecionada() {
        int selectedRow = jTableOrders.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um Pedido da tabela",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }

        int modelRow = jTableOrders.convertRowIndexToModel(selectedRow);
        Integer orderId = (Integer) tableModel.getValueAt(modelRow, 0);

        return orderController.getById(orderId);
    }

    private void visualizarOrder() {
        Order order = getOrderSelecionada();
        if (order == null) {
            return;
        }

        OrderPrintHelper.showOrderPreview(order);
    }

    private void imprimirOrder() {
        Order order = getOrderSelecionada();
        if (order == null) {
            return;
        }

        // Imprimir pedido
        OrderPrintHelper.printOrderWithDialog(order, this);
    }

    private void apagarOrder() {
        Order order = getOrderSelecionada();
        if (order == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja apagar o pedido "
                + order.getPrefix() + "/" + order.getNumber() + "?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // TODO: Implementar exclusão do pedido
            JOptionPane.showMessageDialog(this,
                    "Funcionalidade de exclusão será implementada em breve",
                    "Informação",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Converte o status para texto legível
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
            case PARCIAL:
                return "PARCIAL";
            case ATRASADO:
                return "ATRASADO";
            case CANCELADO:
                return "CANCELADO";
            default:
                return "DESCONHECIDO";
        }
    }

    /**
     * Valida as datas antes de filtrar
     */
    private boolean validarDatas() {
        String dataInicio = jFormattedTextFieldDateStart.getText();
        String dataFim = jFormattedTextFieldDateFinish.getText();

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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelFR = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableOrders = new javax.swing.JTable();
        jButtonViewOrder = new javax.swing.JButton();
        jButtonPrintOrder = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButtonFilterOrder = new javax.swing.JButton();
        jFormattedTextFieldDateFinish = new javax.swing.JFormattedTextField();
        jFormattedTextFieldDateStart = new javax.swing.JFormattedTextField();
        jButtonGenerateNoteCreditFR = new javax.swing.JButton();

        jTableOrders.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Fatura", "Data", "Total", "Cliente", "Seller", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableOrders);

        jButtonViewOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Binoculars.png"))); // NOI18N
        jButtonViewOrder.setText("Ver");
        jButtonViewOrder.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonViewOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewOrderActionPerformed(evt);
            }
        });

        jButtonPrintOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/printer_8139457.png"))); // NOI18N
        jButtonPrintOrder.setText("Imprimir");
        jButtonPrintOrder.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonPrintOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrintOrderActionPerformed(evt);
            }
        });

        jLabel1.setText("Data Inicio:");

        jLabel2.setText("Data Fim:");

        jButtonFilterOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Search.png"))); // NOI18N
        jButtonFilterOrder.setText("Filtrar");
        jButtonFilterOrder.setBorderPainted(false);
        jButtonFilterOrder.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonFilterOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFilterOrderActionPerformed(evt);
            }
        });

        try {
            jFormattedTextFieldDateFinish.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/20##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jFormattedTextFieldDateStart.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/20##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jButtonGenerateNoteCreditFR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Document.png"))); // NOI18N
        jButtonGenerateNoteCreditFR.setText("Gerar Nota de Credito");
        jButtonGenerateNoteCreditFR.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonGenerateNoteCreditFR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenerateNoteCreditFRActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelFRLayout = new javax.swing.GroupLayout(jPanelFR);
        jPanelFR.setLayout(jPanelFRLayout);
        jPanelFRLayout.setHorizontalGroup(
            jPanelFRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFRLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelFRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 939, Short.MAX_VALUE)
                    .addGroup(jPanelFRLayout.createSequentialGroup()
                        .addGroup(jPanelFRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextFieldDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelFRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanelFRLayout.createSequentialGroup()
                                .addComponent(jFormattedTextFieldDateFinish, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonFilterOrder)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonGenerateNoteCreditFR)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonViewOrder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonPrintOrder)))
                .addContainerGap())
        );
        jPanelFRLayout.setVerticalGroup(
            jPanelFRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelFRLayout.createSequentialGroup()
                .addGroup(jPanelFRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelFRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextFieldDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextFieldDateFinish, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonFilterOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonGenerateNoteCreditFR, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonViewOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonPrintOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelFR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelFR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonViewOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewOrderActionPerformed
        // TODO add your handling code here:
        visualizarOrder();
    }//GEN-LAST:event_jButtonViewOrderActionPerformed

    private void jButtonPrintOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrintOrderActionPerformed
        // TODO add your handling code here:
        imprimirOrder();
    }//GEN-LAST:event_jButtonPrintOrderActionPerformed

    private void jButtonFilterOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFilterOrderActionPerformed
        // TODO add your handling code here:
        if (!validarDatas()) {
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateFrom = LocalDate.parse(jFormattedTextFieldDateStart.getText(), formatter);
            LocalDate dateTo = LocalDate.parse(jFormattedTextFieldDateFinish.getText(), formatter);
            filterListOrderFromDate(dateFrom, dateTo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao processar datas: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonFilterOrderActionPerformed

    private void jButtonGenerateNoteCreditFRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenerateNoteCreditFRActionPerformed
        // TODO add your handling code here:
        Order order = getOrderSelecionada();
        if (order == null) {
            return;
        }

        JDialogGenerateNoteCredit jdNoteCredit = new JDialogGenerateNoteCredit(null, true);
        jdNoteCredit.setNoteCredit(order.getId(), "FR");
        jdNoteCredit.setVisible(true);
    }//GEN-LAST:event_jButtonGenerateNoteCreditFRActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonFilterOrder;
    public javax.swing.JButton jButtonGenerateNoteCreditFR;
    private javax.swing.JButton jButtonPrintOrder;
    private javax.swing.JButton jButtonViewOrder;
    private javax.swing.JFormattedTextField jFormattedTextFieldDateFinish;
    private javax.swing.JFormattedTextField jFormattedTextFieldDateStart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanelFR;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableOrders;
    // End of variables declaration//GEN-END:variables
}
