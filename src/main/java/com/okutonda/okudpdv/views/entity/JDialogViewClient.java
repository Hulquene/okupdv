/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.okutonda.okudpdv.views.entity;

import com.okutonda.okudpdv.controllers.ClientController;
import com.okutonda.okudpdv.controllers.InvoiceController;
import com.okutonda.okudpdv.controllers.PaymentController;
import com.okutonda.okudpdv.data.entities.Clients;
import com.okutonda.okudpdv.data.entities.Invoices;
import com.okutonda.okudpdv.data.entities.Payment;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author rog
 */
public class JDialogViewClient extends javax.swing.JDialog {

    private final ClientController clientController = new ClientController();
    private final InvoiceController invoiceController = new InvoiceController();
    private final PaymentController paymentController = new PaymentController();
    private final DecimalFormat df = new DecimalFormat("#,##0.00");
    private Clients client;

    /**
     * Creates new form JDialogViewClient
     */
    public JDialogViewClient(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
        configurarTabelas();
    }

    private void configurarTabelas() {
        configurarTabelaFaturas();
        configurarTabelaPagamentos();
    }

    private void configurarTabelaFaturas() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Número", "Data", "Total", "Pago", "Status", "Vencimento"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTableFaturas.setModel(model);

        // Ajustar largura das colunas
        jTableFaturas.getColumnModel().getColumn(0).setPreferredWidth(100);
        jTableFaturas.getColumnModel().getColumn(1).setPreferredWidth(80);
        jTableFaturas.getColumnModel().getColumn(2).setPreferredWidth(90);
        jTableFaturas.getColumnModel().getColumn(3).setPreferredWidth(90);
        jTableFaturas.getColumnModel().getColumn(4).setPreferredWidth(80);
        jTableFaturas.getColumnModel().getColumn(5).setPreferredWidth(80);
    }

    private void configurarTabelaPagamentos() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Data", "Valor", "Método", "Status", "Referência", "Fatura"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTablePagamentos.setModel(model);

        // Ajustar largura das colunas
        jTablePagamentos.getColumnModel().getColumn(0).setPreferredWidth(80);
        jTablePagamentos.getColumnModel().getColumn(1).setPreferredWidth(80);
        jTablePagamentos.getColumnModel().getColumn(2).setPreferredWidth(100);
        jTablePagamentos.getColumnModel().getColumn(3).setPreferredWidth(80);
        jTablePagamentos.getColumnModel().getColumn(4).setPreferredWidth(120);
        jTablePagamentos.getColumnModel().getColumn(5).setPreferredWidth(100);
    }

    public void setData(int id) {
        try {
            this.client = clientController.getById(id);
            if (this.client != null) {
                setForm();
                setTitle("Visualizar Cliente - " + client.getName());
                carregarFaturas();
                carregarPagamentos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Cliente não encontrado!",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar cliente: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void setForm() {
        if (this.client != null) {
            // Dados básicos
            jLabelId.setText(String.valueOf(client.getId()));
            jLabelNif.setText(client.getNif() != null ? client.getNif() : "N/A");
            jLabelNome.setText(client.getName());
            jLabelEmail.setText(client.getEmail() != null ? client.getEmail() : "N/A");
            jLabelTelefone.setText(client.getPhone() != null ? client.getPhone() : "N/A");

            // Endereço
            jLabelEndereco.setText(client.getAddress() != null ? client.getAddress() : "N/A");
            jLabelCidade.setText(client.getCity() != null ? client.getCity() : "N/A");
            jLabelProvincia.setText(client.getState() != null ? client.getState() : "N/A");
            jLabelZipCode.setText(client.getZipCode() != null ? client.getZipCode() : "N/A");

            // Status
            String status = client.getStatus() == 1 ? "Ativo" : "Inativo";
            jLabelStatus.setText(status);
            jLabelStatus.setForeground(client.getStatus() == 1
                    ? new java.awt.Color(0, 153, 0) : new java.awt.Color(204, 0, 0));

            // Cliente padrão
            String padrao = client.getIsDefault() == 1 ? "Sim" : "Não";
            jLabelPadrao.setText(padrao);
        }
    }

    private void carregarFaturas() {
        try {
            if (client == null || client.getId() == null) {
                return;
            }

            List<Invoices> faturas = invoiceController.listarPorCliente(client.getId());
            DefaultTableModel model = (DefaultTableModel) jTableFaturas.getModel();
            model.setRowCount(0);

            BigDecimal totalFaturado = BigDecimal.ZERO;
            BigDecimal totalPago = BigDecimal.ZERO;

            for (Invoices fatura : faturas) {
                String numeroFatura = fatura.getPrefix() + "/" + fatura.getNumber();
                BigDecimal pago = fatura.getPayTotal() != null ? fatura.getPayTotal() : BigDecimal.ZERO;
                String status = fatura.getStatus() != null ? fatura.getStatus().getDescricao() : "PENDENTE";

                model.addRow(new Object[]{
                    numeroFatura,
                    fatura.getIssueDate(),
                    df.format(fatura.getTotal()),
                    df.format(pago),
                    status,
                    fatura.getDueDate()
                });

                totalFaturado = totalFaturado.add(fatura.getTotal());
                totalPago = totalPago.add(pago);
            }

            jLabelTotalFaturas.setText(
                    String.format("Total Faturas: %d | Faturado: %s AOA | Pago: %s AOA",
                            faturas.size(), df.format(totalFaturado), df.format(totalPago))
            );

        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar faturas: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar faturas: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarPagamentos() {
        try {
            if (client == null || client.getId() == null) {
                return;
            }

            // Carrega pagamentos das faturas deste cliente
            List<Invoices> faturas = invoiceController.listarPorCliente(client.getId());
            DefaultTableModel model = (DefaultTableModel) jTablePagamentos.getModel();
            model.setRowCount(0);

            int totalPagamentos = 0;
            BigDecimal totalPago = BigDecimal.ZERO;

            for (Invoices fatura : faturas) {
                List<Payment> pagamentos = paymentController.getByInvoiceId(fatura.getId());

                for (Payment pagamento : pagamentos) {
                    String numeroFatura = fatura.getPrefix() + "/" + fatura.getNumber();

                    model.addRow(new Object[]{
                        pagamento.getDate(),
                        df.format(pagamento.getTotal()),
                        pagamento.getPaymentMode() != null ? pagamento.getPaymentMode().getDescricao() : "N/A",
                        pagamento.getStatus() != null ? pagamento.getStatus().getDescricao() : "N/A",
                        pagamento.getReference() != null ? pagamento.getReference() : "N/A",
                        numeroFatura
                    });

                    totalPagamentos++;
                    if (pagamento.getTotal() != null) {
                        totalPago = totalPago.add(pagamento.getTotal());
                    }
                }
            }

            jLabelTotalPagamentos.setText(
                    String.format("Total Pagamentos: %d | Total Pago: %s AOA",
                            totalPagamentos, df.format(totalPago))
            );

        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar pagamentos: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar pagamentos: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableFR = new javax.swing.JTable();
        jLabelTotalFaturas2 = new javax.swing.JLabel();
        jLabelTotalFaturasRecibo = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabelTotalFaturas = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableFaturas = new javax.swing.JTable();
        jLabelTotalFaturas1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabelTotalPagamentos = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTablePagamentos = new javax.swing.JTable();
        jLabelTotalPagamentos1 = new javax.swing.JLabel();
        jLabelId = new javax.swing.JLabel();
        jLabelNif = new javax.swing.JLabel();
        jLabelNome = new javax.swing.JLabel();
        jLabelEmail = new javax.swing.JLabel();
        jLabelTelefone = new javax.swing.JLabel();
        jLabelProvincia = new javax.swing.JLabel();
        jLabelCidade = new javax.swing.JLabel();
        jLabelEndereco = new javax.swing.JLabel();
        jLabelZipCode = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabelStatus = new javax.swing.JLabel();
        jLabelGrupo = new javax.swing.JLabel();
        jLabelPadrao = new javax.swing.JLabel();
        jButtonFechar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaObservacoes = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTableFR.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(jTableFR);

        jLabelTotalFaturas2.setText("Total de Faturas Recibo");

        jLabelTotalFaturasRecibo.setText("jLabel1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 888, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabelTotalFaturasRecibo)
                        .addGap(280, 280, 280)
                        .addComponent(jLabelTotalFaturas2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTotalFaturasRecibo)
                    .addComponent(jLabelTotalFaturas2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Faturas Recibo", jPanel1);

        jLabelTotalFaturas.setText("jLabel1");

        jTableFaturas.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTableFaturas);

        jLabelTotalFaturas1.setText("Total de Faturas");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabelTotalFaturas)
                .addGap(109, 109, 109)
                .addComponent(jLabelTotalFaturas1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 888, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTotalFaturas)
                    .addComponent(jLabelTotalFaturas1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(142, 142, 142))
        );

        jTabbedPane1.addTab("Faturas Recorente", jPanel2);

        jLabelTotalPagamentos.setText("jLabel3");

        jTablePagamentos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTablePagamentos);

        jLabelTotalPagamentos1.setText("Total de Pagamentos");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 888, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabelTotalPagamentos)
                .addGap(110, 110, 110)
                .addComponent(jLabelTotalPagamentos1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTotalPagamentos)
                    .addComponent(jLabelTotalPagamentos1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(131, 131, 131))
        );

        jTabbedPane1.addTab("Pagamentos", jPanel3);

        jLabelId.setText("jLabel4");

        jLabelNif.setText("jLabel4");

        jLabelNome.setText("jLabel5");

        jLabelEmail.setText("jLabel5");

        jLabelTelefone.setText("jLabel4");

        jLabelProvincia.setText("jLabel4");

        jLabelCidade.setText("jLabel4");

        jLabelEndereco.setText("jLabel4");

        jLabelZipCode.setText("jLabel4");

        jLabel13.setText("Endereço");

        jLabel10.setText("Informações adicionais");

        jLabelStatus.setText("jLabel4");

        jLabelGrupo.setText("jLabel4");

        jLabelPadrao.setText("jLabel4");

        jButtonFechar.setText("Fechar");
        jButtonFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFecharActionPerformed(evt);
            }
        });

        jTextAreaObservacoes.setColumns(20);
        jTextAreaObservacoes.setRows(5);
        jScrollPane3.setViewportView(jTextAreaObservacoes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelNif)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelEndereco))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabelId)
                                        .addGap(100, 100, 100))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabelEmail)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabelCidade, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelNome)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelZipCode))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabelTelefone)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelProvincia)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(92, 92, 92)
                                .addComponent(jLabel10))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(135, 135, 135)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabelPadrao)
                                    .addComponent(jLabelGrupo)
                                    .addComponent(jLabelStatus))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonFechar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelId)
                            .addComponent(jLabel13)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelNif)
                            .addComponent(jLabelEndereco)
                            .addComponent(jLabelGrupo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelZipCode)
                            .addComponent(jLabelNome)
                            .addComponent(jLabelPadrao))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelCidade)
                                    .addComponent(jLabelEmail))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelProvincia)
                                    .addComponent(jLabelTelefone)))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabelStatus))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonFechar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setSize(new java.awt.Dimension(916, 418));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFecharActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButtonFecharActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JDialogViewClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogViewClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogViewClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogViewClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogViewClient dialog = new JDialogViewClient(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonFechar;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabelCidade;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelEndereco;
    private javax.swing.JLabel jLabelGrupo;
    private javax.swing.JLabel jLabelId;
    private javax.swing.JLabel jLabelNif;
    private javax.swing.JLabel jLabelNome;
    private javax.swing.JLabel jLabelPadrao;
    private javax.swing.JLabel jLabelProvincia;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JLabel jLabelTelefone;
    private javax.swing.JLabel jLabelTotalFaturas;
    private javax.swing.JLabel jLabelTotalFaturas1;
    private javax.swing.JLabel jLabelTotalFaturas2;
    private javax.swing.JLabel jLabelTotalFaturasRecibo;
    private javax.swing.JLabel jLabelTotalPagamentos;
    private javax.swing.JLabel jLabelTotalPagamentos1;
    private javax.swing.JLabel jLabelZipCode;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableFR;
    private javax.swing.JTable jTableFaturas;
    private javax.swing.JTable jTablePagamentos;
    private javax.swing.JTextArea jTextAreaObservacoes;
    // End of variables declaration//GEN-END:variables
}
