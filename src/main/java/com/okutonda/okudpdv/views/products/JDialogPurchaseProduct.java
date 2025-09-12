/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.okutonda.okudpdv.views.products;

import com.okutonda.okudpdv.controllers.ProductController;
import com.okutonda.okudpdv.controllers.PurchaseController;
import com.okutonda.okudpdv.controllers.SupplierController;
import com.okutonda.okudpdv.models.Product;
import com.okutonda.okudpdv.models.Purchase;
import com.okutonda.okudpdv.models.Supplier;
import com.okutonda.okudpdv.models.User;
import com.okutonda.okudpdv.utilities.UserSession;
import com.okutonda.okudpdv.utilities.Util;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author kenny
 */
public class JDialogPurchaseProduct extends javax.swing.JDialog {

    ProductController productController = new ProductController();
    SupplierController supplierController = new SupplierController();
    PurchaseController purchaseController = new PurchaseController();
    UserSession session;
    Boolean status = false;
    Product prod;
    Double pricePurchase = 0.0;
    Double priceSale = 0.0;

    /**
     * Creates new form JDialogPurchaseProduct
     */
    public JDialogPurchaseProduct(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        session = UserSession.getInstance();
    }

    public Boolean getResponse() {
        return status;
    }

    public void setProduct(int id) {
        this.prod = productController.getId(id);
    }

    public void setFormProduct() {
        if (this.prod != null) {
            jLabelNameProduct.setText(this.prod.getDescription());
//            jTextFieldId.setText(String.valueOf(prod.getId()));
//            jComboBoxType.setSelectedItem(prod.getType());
//            jTextFieldCode.setText(prod.getCode());
//            jTextFieldBarCode.setText(prod.getBarcode());
//            jTextFieldDescription.setText(prod.getDescription());
//            jTextFieldPrice.setText(String.valueOf(prod.getPrice()));
//            jTextFieldPurchasePrice.setText(String.valueOf(prod.getPurchasePrice()));
//            jTextFieldStockTotal.setText(String.valueOf(prod.getStockTotal()));
//            jComboBoxTaxeId.setSelectedItem(prod.getTaxe());
//            jComboBoxReasonTaxeId.setSelectedItem(prod.getReasonTaxe());
//            jComboBoxReasonTaxeId.setSelectedItem(prod.getReasonTaxe());
//            jComboBoxSupplier.setSelectedItem(prod.getSupplier());
//            jComboBoxStatus.setSelectedIndex(Integer.parseInt(prod.getStatus()));
        }
    }

    public void loadCombobox() {
        List<Supplier> listS = supplierController.get("");
        jComboBoxSupplier.removeAllItems();
        for (Supplier item : listS) {
            jComboBoxSupplier.addItem(item);
        }

//        List<Warehouse> listW = warehouseController.get("");
//        jComboBoxWarehause.removeAllItems();
//        for (Warehouse item : listW) {
//            jComboBoxWarehause.addItem(item);
//        }
//
//        List<ReasonTaxes> listR = reasonTaxeController.get("");
//        jComboBoxReasonTaxeId.removeAllItems();
//        for (ReasonTaxes item : listR) {
//            jComboBoxReasonTaxeId.addItem(item);
//        }
    }

    public Purchase validatePurchase() {
        Purchase cModel = new Purchase();

        if (jTextFieldQty.getText().isEmpty() || Util.isInteger(jTextFieldQty.getText()) == false) {
            JOptionPane.showMessageDialog(null, "Campo Quatidade invalido!! Insira um numero inteiro");
        } else if (Util.isValidDouble(jTextFieldTotalPurchase.getText()) == false) {
            JOptionPane.showMessageDialog(null, "Campo Total invalido!! Insira um valor valido");
        } else if (Util.isValidDouble(jTextFieldPriceUnit.getText()) == false) {
            JOptionPane.showMessageDialog(null, "Campo Preço de Compra invalido!! Insira um valor compra");
        } else if (Util.isValidDouble(jTextFieldPriceSale.getText()) == false) {
            JOptionPane.showMessageDialog(null, "Campo Preço de venda invalido!! Insira um preço");
//        } else if (Util.isValidDouble(jTextFieldPorcetege.getText()) == false) {
//            JOptionPane.showMessageDialog(null, "Campo Percentagem invalido!! Insira um numero compra");
        } else if (jComboBoxSupplier.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Campo Fornecedor invalido!! Selecione um");
        } else {
            cModel.setDescription("compra " + jLabelNameProduct.getText());
//            cModel.setDate(jFormattedTextField1.getText());
            cModel.setDate("2025-09-10 21:03:52");
            cModel.setQty(Integer.parseInt(jTextFieldQty.getText()));

            cModel.setPriceSale(Util.convertToDouble(jTextFieldPriceSale.getText()));
            cModel.setPricePurchase(Util.convertToDouble(jTextFieldPriceUnit.getText()));
            cModel.setTotal(Util.convertToDouble(jTextFieldTotalPurchase.getText()));
            cModel.setStatus(Integer.toString(jComboBoxStatus.getSelectedIndex()));
            cModel.setStatusPayment(Integer.toString(jComboBoxStatus.getSelectedIndex()));
            cModel.setUser((User) session.getUser());
            cModel.setProduct(this.prod);
            cModel.setSupplier((Supplier) jComboBoxSupplier.getSelectedItem());
            return cModel;
        }

        return null;
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
        jLabelNameProduct = new javax.swing.JLabel();
        jTextFieldQty = new javax.swing.JTextField();
        jTextFieldTotalPurchase = new javax.swing.JTextField();
        jTextFieldPriceUnit = new javax.swing.JTextField();
        jTextFieldPriceSale = new javax.swing.JTextField();
        jTextFieldPorcetege = new javax.swing.JTextField();
        jLabelProductId1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButtonSave = new javax.swing.JButton();
        jComboBoxSupplier = new javax.swing.JComboBox();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabelProductId = new javax.swing.JLabel();
        jComboBoxStatus = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        jLabelNameProduct.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabelNameProduct.setForeground(new java.awt.Color(255, 255, 255));
        jLabelNameProduct.setText("Produto");

        jTextFieldTotalPurchase.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldTotalPurchaseKeyReleased(evt);
            }
        });

        jTextFieldPorcetege.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldPorcetegeKeyReleased(evt);
            }
        });

        jLabelProductId1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabelProductId1.setForeground(new java.awt.Color(0, 0, 51));
        jLabelProductId1.setText("Comprar produto:");

        jLabel1.setText("Quantidade");

        jLabel2.setText("Lucro %");

        jLabel3.setText("Data de Pagamento");

        jLabel4.setText("Total Compra");

        jLabel5.setText("Preço Unit Produto");

        jLabel6.setText("Fornecedor");

        jLabel7.setText("Preço da Venda");

        jButtonSave.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonSave.setText("Guardar");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jComboBoxSupplier.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jComboBoxSupplierAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jComboBoxSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSupplierActionPerformed(evt);
            }
        });

        try {
            jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##-##-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabelProductId.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabelProductId.setForeground(new java.awt.Color(255, 255, 255));
        jLabelProductId.setText("Produto");

        jComboBoxStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Inativo", "Ativo" }));
        jComboBoxStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxStatusActionPerformed(evt);
            }
        });

        jLabel8.setText("Status do Pagamento");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldQty, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldPorcetege, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel2))
                            .addComponent(jLabel3)
                            .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jComboBoxStatus, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextFieldTotalPurchase, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxSupplier, javax.swing.GroupLayout.Alignment.LEADING, 0, 207, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextFieldPriceSale, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextFieldPriceUnit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButtonSave, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel5)))
                            .addComponent(jLabel8))
                        .addGap(0, 33, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabelProductId1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelNameProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelProductId, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jFormattedTextField1, jTextFieldPorcetege, jTextFieldPriceSale, jTextFieldPriceUnit, jTextFieldQty, jTextFieldTotalPurchase});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNameProduct)
                    .addComponent(jLabelProductId1)
                    .addComponent(jLabelProductId))
                .addGap(49, 49, 49)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(8, 8, 8))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldQty, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldTotalPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabel2))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldPorcetege, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextFieldPriceUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextFieldPriceSale, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(jButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jFormattedTextField1, jTextFieldPorcetege, jTextFieldQty});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(648, 408));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        loadCombobox();
        setFormProduct();

    }//GEN-LAST:event_formWindowActivated

    private void jComboBoxSupplierAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jComboBoxSupplierAncestorAdded
        // TODO add your handling code here:
        //        List<Supplier> list = supplierController.get("");
        //        jComboBoxSupplier.removeAllItems();
        //        for (Supplier item : list) {
        //            jComboBoxSupplier.addItem(item);
        //        }
    }//GEN-LAST:event_jComboBoxSupplierAncestorAdded

    private void jComboBoxSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSupplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxSupplierActionPerformed

    private void jTextFieldTotalPurchaseKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldTotalPurchaseKeyReleased
        // TODO add your handling code here:
        Double total = Double.valueOf(jTextFieldTotalPurchase.getText());
        int qty = Integer.parseInt(jTextFieldQty.getText());
        this.pricePurchase = total / qty;
        jTextFieldPriceUnit.setText(pricePurchase.toString());

    }//GEN-LAST:event_jTextFieldTotalPurchaseKeyReleased

    private void jTextFieldPorcetegeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPorcetegeKeyReleased
        // TODO add your handling code here:
        if (Util.checkIsNumber(jTextFieldPorcetege.getText())) {
            Double per = Double.valueOf(jTextFieldPorcetege.getText());
            this.priceSale = this.pricePurchase + (this.pricePurchase * (per / 100));
            jTextFieldPriceSale.setText(this.priceSale.toString());
        } else {
            JOptionPane.showMessageDialog(null, "Valor invalido!! insira um numero valido");
        }
    }//GEN-LAST:event_jTextFieldPorcetegeKeyReleased

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        // TODO add your handling code here:
        Purchase cModel = validatePurchase();
        if (cModel != null) {
            status = purchaseController.add(cModel);
            if (status == true) {
                this.dispose();
            }
        }
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jComboBoxStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxStatusActionPerformed

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
            java.util.logging.Logger.getLogger(JDialogPurchaseProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogPurchaseProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogPurchaseProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogPurchaseProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogPurchaseProduct dialog = new JDialogPurchaseProduct(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonSave;
    private javax.swing.JComboBox<String> jComboBoxStatus;
    private javax.swing.JComboBox jComboBoxSupplier;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabelNameProduct;
    private javax.swing.JLabel jLabelProductId;
    private javax.swing.JLabel jLabelProductId1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextFieldPorcetege;
    private javax.swing.JTextField jTextFieldPriceSale;
    private javax.swing.JTextField jTextFieldPriceUnit;
    private javax.swing.JTextField jTextFieldQty;
    private javax.swing.JTextField jTextFieldTotalPurchase;
    // End of variables declaration//GEN-END:variables
}
