/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.okutonda.okudpdv.views.entity;

import com.okutonda.okudpdv.controllers.PurchaseController;
import com.okutonda.okudpdv.controllers.PurchasePaymentController;
import com.okutonda.okudpdv.controllers.SupplierController;
import com.okutonda.okudpdv.data.entities.Purchase;
import com.okutonda.okudpdv.data.entities.PurchasePayment;
import com.okutonda.okudpdv.data.entities.Supplier;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hr
 */
public class JDialogViewSupplier extends javax.swing.JDialog {

    private final SupplierController suppController = new SupplierController();
    private final PurchaseController purchaseController = new PurchaseController();
    private final PurchasePaymentController paymentController = new PurchasePaymentController();
    private final DecimalFormat df = new DecimalFormat("#,##0.00");
    private Supplier supplier;

    /**
     * Creates new form JDialogViewSupplier
     */
    public JDialogViewSupplier(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
        configurarTabelas();
    }

    private void configurarTabelas() {
        configurarTabelaCompras();
        configurarTabelaPagamentos();
    }

    private void configurarTabelaCompras() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Número", "Data", "Total", "Status", "Saldo Devedor"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTablePurchases.setModel(model);

        // Ajustar largura das colunas
        jTablePurchases.getColumnModel().getColumn(0).setPreferredWidth(100);
        jTablePurchases.getColumnModel().getColumn(1).setPreferredWidth(80);
        jTablePurchases.getColumnModel().getColumn(2).setPreferredWidth(90);
        jTablePurchases.getColumnModel().getColumn(3).setPreferredWidth(80);
        jTablePurchases.getColumnModel().getColumn(4).setPreferredWidth(90);
    }

    private void configurarTabelaPagamentos() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Data", "Valor", "Método", "Status", "Referência"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTablePurchasePayments.setModel(model);

        // Ajustar largura das colunas
        jTablePurchasePayments.getColumnModel().getColumn(0).setPreferredWidth(80);
        jTablePurchasePayments.getColumnModel().getColumn(1).setPreferredWidth(80);
        jTablePurchasePayments.getColumnModel().getColumn(2).setPreferredWidth(100);
        jTablePurchasePayments.getColumnModel().getColumn(3).setPreferredWidth(80);
        jTablePurchasePayments.getColumnModel().getColumn(4).setPreferredWidth(120);
    }

    public void setData(int id) {
        try {
            this.supplier = suppController.getById(id);
            if (this.supplier != null) {
                setForm();
                setTitle("Visualizar Fornecedor - " + supplier.getName());
                carregarCompras();
                carregarPagamentos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Fornecedor não encontrado!",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar fornecedor: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void setForm() {
        if (this.supplier != null) {
            // Dados básicos
            jLabelId.setText(String.valueOf(supplier.getId()));
            jLabelNif.setText(supplier.getNif() != null ? supplier.getNif() : "N/A");
            jLabelNome.setText(supplier.getName());
            jLabelEmail.setText(supplier.getEmail() != null ? supplier.getEmail() : "N/A");
            jLabelTelefone.setText(supplier.getPhone() != null ? supplier.getPhone() : "N/A");

            // Endereço
            jLabelEndereco.setText(supplier.getAddress() != null ? supplier.getAddress() : "N/A");
            jLabelCidade.setText(supplier.getCity() != null ? supplier.getCity() : "N/A");
            jLabelZipCode.setText(supplier.getZipCode() != null ? supplier.getZipCode() : "N/A");

            // Status
            String status = supplier.getStatus() == 1 ? "Ativo" : "Inativo";
            jLabelStatus.setText(status);
            jLabelStatus.setForeground(supplier.getStatus() == 1
                    ? new java.awt.Color(0, 153, 0) : new java.awt.Color(204, 0, 0));

            // Informações adicionais
            jLabelGrupo.setText(supplier.getGroupId() != null ? supplier.getGroupId().toString() : "N/A");
            jLabelPadrao.setText(supplier.getIsDefault() == 1 ? "Sim" : "Não");
        }
    }

    private void carregarCompras() {
        try {
            if (supplier == null || supplier.getId() == null) {
                return;
            }

            List<Purchase> compras = purchaseController.listarPorFornecedor(supplier.getId());
            DefaultTableModel model = (DefaultTableModel) jTablePurchases.getModel();
            model.setRowCount(0);

            for (Purchase compra : compras) {
                BigDecimal saldoDevedor = purchaseController.calcularSaldoDevedor(compra);
                String status = saldoDevedor.compareTo(BigDecimal.ZERO) == 0 ? "PAGA" : "PENDENTE";

                model.addRow(new Object[]{
                    compra.getInvoiceNumber(),
                    compra.getDataCompra(),
                    df.format(compra.getTotal()),
                    status,
                    df.format(saldoDevedor)
                });
            }

            jLabelTotalCompras.setText("Total de Compras: " + compras.size());

        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar compras: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar compras: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarPagamentos() {
        try {
            if (supplier == null || supplier.getId() == null) {
                return;
            }

            // Carrega pagamentos das compras deste fornecedor
            List<Purchase> compras = purchaseController.listarPorFornecedor(supplier.getId());
            DefaultTableModel model = (DefaultTableModel) jTablePurchasePayments.getModel();
            model.setRowCount(0);

            int totalPagamentos = 0;
            BigDecimal totalPago = BigDecimal.ZERO;

            for (Purchase compra : compras) {
                List<PurchasePayment> pagamentos = paymentController.listarPorCompra(compra.getId());

                for (PurchasePayment pagamento : pagamentos) {
                    model.addRow(new Object[]{
                        pagamento.getDataPagamento(),
                        df.format(pagamento.getValorPago()),
                        pagamento.getMetodo() != null ? pagamento.getMetodo().getDescricao() : "N/A",
                        pagamento.getStatus() != null ? pagamento.getStatus().getDescricao() : "N/A",
                        pagamento.getReferencia() != null ? pagamento.getReferencia() : "N/A"
                    });

                    totalPagamentos++;
                    if (pagamento.getValorPago() != null) {
                        totalPago = totalPago.add(pagamento.getValorPago());
                    }
                }
            }

            jLabelTotalPagamentos.setText("Total Pagamentos: " + totalPagamentos + " | Total Pago: " + df.format(totalPago) + " AOA");

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
        jPanel2 = new javax.swing.JPanel();
        jLabelTotalCompras = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePurchases = new javax.swing.JTable();
        jLabelTotalCompras1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabelTotalPagamentos = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTablePurchasePayments = new javax.swing.JTable();
        jLabelTotalPagamentos1 = new javax.swing.JLabel();
        jButtonFechar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaObservacoes = new javax.swing.JTextArea();
        jLabelNif = new javax.swing.JLabel();
        jLabelNome = new javax.swing.JLabel();
        jLabelEmail = new javax.swing.JLabel();
        jLabelTelefone = new javax.swing.JLabel();
        jLabelCidade = new javax.swing.JLabel();
        jLabelProvincia = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabelPadrao = new javax.swing.JLabel();
        jLabelGrupo = new javax.swing.JLabel();
        jLabelId = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabelEndereco = new javax.swing.JLabel();
        jLabelStatus = new javax.swing.JLabel();
        jLabelZipCode = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabelTotalCompras.setText("jLabel1");

        jTablePurchases.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTablePurchases);

        jLabelTotalCompras1.setText("Total de Compras");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 882, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelTotalCompras)
                        .addGap(92, 92, 92)
                        .addComponent(jLabelTotalCompras1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTotalCompras)
                    .addComponent(jLabelTotalCompras1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103))
        );

        jTabbedPane1.addTab("Faturas", jPanel2);

        jLabelTotalPagamentos.setText("jLabel3");

        jTablePurchasePayments.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTablePurchasePayments);

        jLabelTotalPagamentos1.setText("Total de pagamentos");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 882, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabelTotalPagamentos)
                        .addGap(139, 139, 139)
                        .addComponent(jLabelTotalPagamentos1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTotalPagamentos)
                    .addComponent(jLabelTotalPagamentos1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Pagamentos", jPanel3);

        jButtonFechar.setText("Fechar");
        jButtonFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFecharActionPerformed(evt);
            }
        });

        jTextAreaObservacoes.setColumns(20);
        jTextAreaObservacoes.setRows(5);
        jScrollPane3.setViewportView(jTextAreaObservacoes);

        jLabelNif.setText("jLabel4");

        jLabelNome.setText("jLabel5");

        jLabelEmail.setText("jLabel5");

        jLabelTelefone.setText("jLabel4");

        jLabelCidade.setText("jLabel4");

        jLabelProvincia.setText("jLabel4");

        jLabel10.setText("Informações adicionais");

        jLabelPadrao.setText("jLabel4");

        jLabelGrupo.setText("jLabel4");

        jLabelId.setText("jLabel4");

        jLabel13.setText("Endereço");

        jLabelEndereco.setText("jLabel4");

        jLabelStatus.setText("jLabel4");

        jLabelZipCode.setText("jLabel4");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelId)
                                .addGap(149, 149, 149))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelNif)
                                    .addComponent(jLabelNome)
                                    .addComponent(jLabelEmail)
                                    .addComponent(jLabelTelefone))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabelZipCode)
                                            .addComponent(jLabelEndereco)
                                            .addComponent(jLabelCidade)
                                            .addComponent(jLabelProvincia))))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(92, 92, 92)
                                .addComponent(jLabel10))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(128, 128, 128)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabelPadrao)
                                    .addComponent(jLabelGrupo)
                                    .addComponent(jLabelStatus))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonFechar, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(14, 14, 14))
                    .addComponent(jTabbedPane1)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelId)
                    .addComponent(jLabel13)
                    .addComponent(jLabel10)
                    .addComponent(jButtonFechar))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelEndereco)
                            .addComponent(jLabelNif))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelNome)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelEmail)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelTelefone))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelZipCode)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelCidade)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelProvincia))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelGrupo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelPadrao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelStatus))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
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
            java.util.logging.Logger.getLogger(JDialogViewSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogViewSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogViewSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogViewSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogViewSupplier dialog = new JDialogViewSupplier(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabelTotalCompras;
    private javax.swing.JLabel jLabelTotalCompras1;
    private javax.swing.JLabel jLabelTotalPagamentos;
    private javax.swing.JLabel jLabelTotalPagamentos1;
    private javax.swing.JLabel jLabelZipCode;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTablePurchasePayments;
    private javax.swing.JTable jTablePurchases;
    private javax.swing.JTextArea jTextAreaObservacoes;
    // End of variables declaration//GEN-END:variables
}
