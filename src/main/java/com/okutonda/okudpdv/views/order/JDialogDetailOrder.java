/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.okutonda.okudpdv.views.order;

import com.okutonda.okudpdv.controllers.OrderController;
import com.okutonda.okudpdv.data.entities.Order;
import com.okutonda.okudpdv.data.entities.ProductSales;
import com.okutonda.okudpdv.helpers.PrintHelper;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.print.PrinterException;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kenny
 */
public class JDialogDetailOrder extends javax.swing.JDialog {

    Order order;
    OrderController orderController = new OrderController();

    /**
     * Creates new form JDialogDetailOrder
     */
    public JDialogDetailOrder(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        inicializarTabelaProdutosOrder(); // ← Adicione esta linha
    }

    /**
     * Carrega os dados do pedido na interface
     */
    public void setOrder(int id) {
        try {
            System.out.println("id order: " + id);
            this.order = this.orderController.getById(id);

            if (order != null) {
                carregarDadosOrderUI();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Pedido não encontrado!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            System.err.println("Erro ao carregar pedido: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar pedido: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Inicializa a configuração da tabela de produtos do pedido
     */
    private void inicializarTabelaProdutosOrder() {
        // Define o modelo da tabela
        jTableProductsOrder.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID", "Código", "Descrição", "Preço", "Qtd", "Taxa %", "Total"
                }
        ) {
            Class[] types = new Class[]{
                Integer.class, String.class, String.class,
                BigDecimal.class, Integer.class, BigDecimal.class, BigDecimal.class
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
        });

        // Configuração da aparência da tabela
        configurarAparenciaTabelaProdutosOrder();
    }

    /**
     * Configura a aparência da tabela de produtos do pedido
     */
    private void configurarAparenciaTabelaProdutosOrder() {
        // Ajusta a largura das colunas
        jTableProductsOrder.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        jTableProductsOrder.getColumnModel().getColumn(1).setPreferredWidth(80);   // Código
        jTableProductsOrder.getColumnModel().getColumn(2).setPreferredWidth(200);  // Descrição
        jTableProductsOrder.getColumnModel().getColumn(3).setPreferredWidth(80);   // Preço
        jTableProductsOrder.getColumnModel().getColumn(4).setPreferredWidth(60);   // Qtd
        jTableProductsOrder.getColumnModel().getColumn(5).setPreferredWidth(70);   // Taxa %
        jTableProductsOrder.getColumnModel().getColumn(6).setPreferredWidth(90);   // Subtotal

        // Centraliza algumas colunas
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);

        jTableProductsOrder.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        jTableProductsOrder.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Qtd
        jTableProductsOrder.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Taxa %

        // Aplica renderizador para preços e subtotais
        jTableProductsOrder.getColumnModel().getColumn(3).setCellRenderer(new PrecoTableCellRenderer());
        jTableProductsOrder.getColumnModel().getColumn(6).setCellRenderer(new PrecoTableCellRenderer());

        // Aplica renderizador para taxa %
        jTableProductsOrder.getColumnModel().getColumn(5).setCellRenderer(new TaxaTableCellRenderer());
    }

    /**
     * Lista os produtos do pedido na tabela
     */
    public void listProdutsOrder() {
        DefaultTableModel data = (DefaultTableModel) jTableProductsOrder.getModel();
        data.setNumRows(0);

        if (order != null && order.getProducts() != null) {
            for (ProductSales c : order.getProducts()) {
                // Obtém a taxa do produto
                BigDecimal taxaPercent = BigDecimal.ZERO;
                if (c.getProduct() != null && c.getProduct().getTaxe() != null) {
                    taxaPercent = c.getProduct().getTaxe().getPercetage();
                }

                data.addRow(new Object[]{
                    c.getProduct().getId(),
                    c.getCode(),
                    c.getDescription(),
                    c.getPrice(),
                    c.getQty(),
                    taxaPercent, // Taxa %
                    c.getPrice().multiply(BigDecimal.valueOf(c.getQty())) // Subtotal
                });
            }
        }
    }

    /**
     * Renderizador para colunas de taxa %
     */
    private class TaxaTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value instanceof BigDecimal) {
                BigDecimal taxa = (BigDecimal) value;
                setText(String.format("%.1f%%", taxa));
                setHorizontalAlignment(JLabel.CENTER);

                // Destaca taxas diferentes de zero
                if (taxa.compareTo(BigDecimal.ZERO) > 0) {
                    setForeground(new Color(178, 34, 34)); // Vermelho escuro para taxas
                    setFont(getFont().deriveFont(Font.BOLD));
                }
            }
            return c;
        }
    }

    /**
     * Carrega os dados do pedido na interface gráfica
     */
    private void carregarDadosOrderUI() {
        try {
            // Informações do cabeçalho
            jLabelNumberOrder.setText(PrintHelper.formatDocumentNumber(order.getNumber(), order.getYear(), order.getPrefix()));
            jLabelDate.setText(order.getDatecreate() != null ? order.getDatecreate() : "");

            // Vendedor
            if (order.getSeller() != null) {
                jLabelSeller.setText(order.getSeller().getName());
            } else {
                jLabelSeller.setText("N/A");
            }

            // Cliente
            if (order.getClient() != null) {
                jLabelClientName.setText(order.getClient().getName());
                jLabelClientNif.setText(order.getClient().getNif() != null ? order.getClient().getNif() : "");
            } else {
                jLabelClientName.setText("Consumidor Final");
                jLabelClientNif.setText("");
            }

            // Observações
            jTextPaneNote.setText(order.getNote() != null ? order.getNote() : "");

            // Totais
            jLabelSubTotalOrder.setText(order.getSubTotal() != null ? formatarMoeda(order.getSubTotal()) : "0.00 AOA");
            jLabelTaxOrder.setText(order.getTotalTaxe() != null ? formatarMoeda(order.getTotalTaxe()) : "0.00 AOA");
            jLabelTotalOrder.setText(order.getTotal() != null ? formatarMoeda(order.getTotal()) : "0.00 AOA");

            // Lista produtos
            listProdutsOrder();

        } catch (Exception e) {
            System.err.println("Erro ao carregar dados do pedido na UI: " + e.getMessage());
            throw new RuntimeException("Erro ao carregar dados do pedido", e);
        }
    }

    /**
     * Formata valor monetário para exibição
     */
    private String formatarMoeda(BigDecimal valor) {
        if (valor == null) {
            return "0.00 AOA";
        }
        return String.format("%,.2f AOA", valor);
    }

    /**
     * Renderizador para colunas de preço
     */
    private class PrecoTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value instanceof BigDecimal) {
                BigDecimal preco = (BigDecimal) value;
                setText(String.format("%,.2f", preco));
                setHorizontalAlignment(JLabel.RIGHT);
            }
            return c;
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableProductsOrder = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabelClientName = new javax.swing.JLabel();
        jLabelClientNif = new javax.swing.JLabel();
        jLabelDate = new javax.swing.JLabel();
        jLabelNumberOrder = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabelSeller = new javax.swing.JLabel();
        jLabelSubTotalOrder = new javax.swing.JLabel();
        jLabelTotalOrder = new javax.swing.JLabel();
        jLabelTaxOrder = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPaneNote = new javax.swing.JTextPane();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Detalhe da Fatura");

        jTableProductsOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Codigo", "Produto", "Preço", "QTD", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class
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
        jScrollPane1.setViewportView(jTableProductsOrder);
        if (jTableProductsOrder.getColumnModel().getColumnCount() > 0) {
            jTableProductsOrder.getColumnModel().getColumn(0).setResizable(false);
            jTableProductsOrder.getColumnModel().getColumn(2).setResizable(false);
        }

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Nota:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Total");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Imposto");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Subtotal");

        jLabelClientName.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelClientName.setText("Cliente");

        jLabelClientNif.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelClientNif.setText("NIF");

        jLabelDate.setText("Data");

        jLabelNumberOrder.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabelNumberOrder.setText("N da Fatura");

        jButton1.setText("Imprimir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setText("Pagamento");

        jLabelSeller.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelSeller.setText("seller");

        jLabelSubTotalOrder.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelSubTotalOrder.setText("jLabel6");

        jLabelTotalOrder.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelTotalOrder.setText("jLabel7");

        jLabelTaxOrder.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelTaxOrder.setText("jLabel11");

        jScrollPane3.setViewportView(jTextPaneNote);

        jLabel6.setText("Nome Cliente:");

        jLabel7.setText("NIF Cliente:");

        jLabel8.setText("Nome do Vendedor:");

        jLabel9.setText("Data da Fatura:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(43, 43, 43)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelTaxOrder)
                                    .addComponent(jLabelTotalOrder)
                                    .addComponent(jLabelSubTotalOrder)))
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(17, 17, 17))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabelNumberOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(20, 20, 20))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelClientNif, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelClientName, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelSeller)
                            .addComponent(jLabelDate, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(225, 225, 225))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 448, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabelNumberOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelClientName)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelClientNif)
                            .addComponent(jLabel7)))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabelSeller)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelDate)
                            .addComponent(jLabel2)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabelSubTotalOrder))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelTaxOrder)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelTotalOrder)
                            .addComponent(jLabel3))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(104, 104, 104))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 570, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(651, 578));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:

        jLabelNumberOrder.setText(PrintHelper.formatDocumentNumber(order.getNumber(), order.getYear(), order.getPrefix()));
        jLabelDate.setText(order.getDatecreate());
        jLabelSeller.setText(order.getSeller().getName());
        jLabelClientName.setText(order.getClient().getName());
        jLabelClientNif.setText(order.getClient().getNif());
        jTextPaneNote.setText(order.getNote());

        jLabelSubTotalOrder.setText(order.getSubTotal().toString());
        jLabelTaxOrder.setText(order.getTotalTaxe().toString());
        jLabelTotalOrder.setText(order.getTotal().toString());

        listProdutsOrder();
    }//GEN-LAST:event_formWindowActivated

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // TODO add your handling code here:
            PrintHelper.printThermalTicket(this.order);
        } catch (PrinterException ex) {
            Logger.getLogger(JDialogDetailOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(JDialogDetailOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogDetailOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogDetailOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogDetailOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogDetailOrder dialog = new JDialogDetailOrder(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelClientName;
    private javax.swing.JLabel jLabelClientNif;
    private javax.swing.JLabel jLabelDate;
    private javax.swing.JLabel jLabelNumberOrder;
    private javax.swing.JLabel jLabelSeller;
    private javax.swing.JLabel jLabelSubTotalOrder;
    private javax.swing.JLabel jLabelTaxOrder;
    private javax.swing.JLabel jLabelTotalOrder;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTableProductsOrder;
    private javax.swing.JTextPane jTextPaneNote;
    // End of variables declaration//GEN-END:variables
}
