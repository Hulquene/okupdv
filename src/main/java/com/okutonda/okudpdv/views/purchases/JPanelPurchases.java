/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.okutonda.okudpdv.views.purchases;

import com.okutonda.okudpdv.controllers.PurchaseController;
import com.okutonda.okudpdv.controllers.StockMovementController;
import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.Purchase;
import com.okutonda.okudpdv.views.pdv.JDialogDetailPurchase;
import com.okutonda.okudpdv.views.stock.JDialogFormEntryProdPurchase;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author rog
 */
public class JPanelPurchases extends javax.swing.JPanel {

    PurchaseController purchaseController = new PurchaseController();

    /**
     * Creates new form JPanelPurchases
     */
    public JPanelPurchases() {
        initComponents();

        jTablePurchases.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID", "Fornecedor", "Nº Documento", "Tipo", "Descrição",
                    "Total", "Pago", "Em Aberto", "Data Compra", "Vencimento", "Status"
                }
        ) {
            Class[] types = new Class[]{
                Integer.class, String.class, String.class, String.class,
                String.class, Double.class, Double.class, Double.class,
                String.class, String.class, String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false,
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        listPurchases();
    }

    public void listPurchases() {
        List<Purchase> list = purchaseController.listar();
        loadListPurchases(list);
    }

    public void loadListPurchases(List<Purchase> list) {
        DefaultTableModel data = (DefaultTableModel) jTablePurchases.getModel();
        data.setNumRows(0);

        for (Purchase p : list) {
            double pago = p.getTotal_pago() != null ? p.getTotal_pago().doubleValue() : 0d;
            double emAberto = p.getTotal() != null ? p.getTotal().doubleValue() - pago : 0d;

            data.addRow(new Object[]{
                p.getId(),
                (p.getSupplier() != null ? p.getSupplier().getName() : ""),
                p.getInvoiceNumber(),
                p.getInvoiceType(),
                p.getDescricao(),
                p.getTotal(),
                pago,
                emAberto,
                (p.getDataCompra() != null ? p.getDataCompra().toString() : ""),
                (p.getDataVencimento() != null ? p.getDataVencimento().toString() : ""),
                p.getStatus()
            });
        }
    }

    private void processarEntradaStock(Integer compraId) {
        StockMovementController stockController = new StockMovementController();
        PurchaseController purchaseController = new PurchaseController();

        try {
            // Obtém detalhes da compra usando o PurchaseController
            Purchase compra = purchaseController.buscarPorId(compraId);

            if (compra == null) {
                JOptionPane.showMessageDialog(null, "Compra não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Valida se a compra já não foi processada
            if (compra.getStatus() != null && "PROCESSADA".equals(compra.getStatus())) {
                int resposta = JOptionPane.showConfirmDialog(
                        null,
                        "Esta compra já foi processada anteriormente. Deseja processar novamente?",
                        "Compra Já Processada",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (resposta != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            // Prepara mensagem de confirmação
            StringBuilder mensagem = new StringBuilder();
            mensagem.append("Confirmar entrada de stock para:\n")
                    .append("Compra #").append(compraId).append(" - ").append(compra.getInvoiceNumber())
                    .append("\nFornecedor: ").append(compra.getSupplier().getName())
                    .append("\n\nItens a processar:\n");

            for (var item : compra.getItems()) {
                Product produto = item.getProduct();
                Integer quantidade = item.getQuantidade();
                Integer stockAtual = stockController.getStockAtual(produto.getId());

                mensagem.append("• ").append(produto.getDescription())
                        .append(" - ").append(quantidade).append(" unidades")
                        .append(" (Stock atual: ").append(stockAtual).append(")\n");
            }

            int confirmacao = JOptionPane.showConfirmDialog(
                    null,
                    mensagem.toString(),
                    "Confirmar Entrada de Stock",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirmacao == JOptionPane.YES_OPTION) {
                // Processa a entrada de stock para cada item
                boolean todosProcessados = true;
                List<String> erros = new ArrayList<>();

                for (var item : compra.getItems()) {
                    try {
                        boolean sucesso = stockController.registrarEntradaCompra(
                                item.getProduct(),
                                item.getQuantidade(),
                                compraId
                        );

                        if (!sucesso) {
                            todosProcessados = false;
                            erros.add("Falha ao processar: " + item.getProduct().getDescription());
                        }

                    } catch (Exception e) {
                        todosProcessados = false;
                        erros.add("Erro em " + item.getProduct().getDescription() + ": " + e.getMessage());
                        System.err.println("❌ Erro ao processar item: " + e.getMessage());
                    }
                }

                if (todosProcessados) {
                    // Atualiza o status da compra para processada
                    atualizarStatusCompraProcessada(compraId);

                    JOptionPane.showMessageDialog(null,
                            "Entrada de stock processada com sucesso!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);

                    listPurchases(); // Atualiza a tabela de compras

                } else {
                    StringBuilder erroMsg = new StringBuilder();
                    erroMsg.append("Alguns itens não foram processados:\n");
                    for (String erro : erros) {
                        erroMsg.append("• ").append(erro).append("\n");
                    }

                    JOptionPane.showMessageDialog(null,
                            erroMsg.toString(),
                            "Atenção",
                            JOptionPane.WARNING_MESSAGE);
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Erro ao processar entrada de stock: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                    "Erro ao processar entrada de stock: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

// Método auxiliar para atualizar o status da compra usando o controller
    private void atualizarStatusCompraProcessada(Integer compraId) {
        try {
            PurchaseController purchaseController = new PurchaseController();
            Purchase compra = purchaseController.buscarPorId(compraId);

            if (compra != null) {
                compra.setStatus("PROCESSADA");
                purchaseController.atualizarCompra(compra);
                System.out.println("✅ Status da compra #" + compraId + " atualizado para: PROCESSADA");
            }

        } catch (Exception e) {
            System.err.println("❌ Erro ao atualizar status da compra: " + e.getMessage());
            // Não mostra erro ao usuário pois o stock já foi processado
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePurchases = new javax.swing.JTable();
        jButtonAddPurchase = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButtonEntradaStockCompra = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();

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

        jButtonAddPurchase.setText("Cadastrar Compra");
        jButtonAddPurchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddPurchaseActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText("Pesquisar");

        jLabel2.setText("Fornecedor");

        jButton1.setText("Entrada de Stock");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Ver");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButtonEntradaStockCompra.setText("Entrada de Stock da compra");
        jButtonEntradaStockCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEntradaStockCompraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, Short.MAX_VALUE)
                                .addGap(15, 15, 15)
                                .addComponent(jButtonEntradaStockCompra)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonAddPurchase))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAddPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonEntradaStockCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );

        jTabbedPane1.addTab("Compras", jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 947, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 525, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Pagamentos de compras", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 947, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 525, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Nota de Credito", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 560, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAddPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddPurchaseActionPerformed
        // TODO add your handling code here:
        JDialogFormPurchase jdForm = new JDialogFormPurchase(null, true);
        jdForm.setVisible(true);
        Boolean resp = jdForm.getResponse();
        if (resp == true) {
            JOptionPane.showMessageDialog(null, "Compra feita com sucesso!!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            listPurchases();
        }
    }//GEN-LAST:event_jButtonAddPurchaseActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        JDialogFormEntryProdPurchase formPurchase = new JDialogFormEntryProdPurchase(null, true);
        formPurchase.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        int id = 0;
        try {
            id = (int) jTablePurchases.getValueAt(jTablePurchases.getSelectedRow(), 0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Selecione um Venda na tabela!!", "Atencao", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (id > 0) {
//        
                JDialogDetailPurchase jdOrder = new JDialogDetailPurchase(null, true);
                jdOrder.setOrder(id);
//        jdOrder.setOrder(order);
                jdOrder.setVisible(true);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButtonEntradaStockCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEntradaStockCompraActionPerformed
        // TODO add your handling code here:
        int compraId = 0;
        try {
            compraId = (int) jTablePurchases.getValueAt(jTablePurchases.getSelectedRow(), 0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Selecione uma Compra na tabela!!", "Atencao", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (compraId > 0) {
            processarEntradaStock(compraId);
        }
    }//GEN-LAST:event_jButtonEntradaStockCompraActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonAddPurchase;
    private javax.swing.JButton jButtonEntradaStockCompra;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTablePurchases;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
