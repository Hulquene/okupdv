/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.okutonda.okudpdv.views.supplier;

import com.okutonda.okudpdv.controllers.CountryController;
import com.okutonda.okudpdv.controllers.SupplierController;
import com.okutonda.okudpdv.models.Supplier;
import com.okutonda.okudpdv.utilities.UserSession;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kenny
 */
public final class JPanelSupplier extends javax.swing.JPanel {

    SupplierController supplierController = new SupplierController();
    CountryController countryController = new CountryController();
    UserSession session;

    public void listSuppliers() {
//        SupplierDao cDao = new SupplierDao();
        List<Supplier> list = supplierController.get("");
        DefaultTableModel data = (DefaultTableModel) jTableSuppliers.getModel();
        data.setNumRows(0);
        for (Supplier c : list) {
            data.addRow(new Object[]{
                c.getId(),
                c.getNif(),
                c.getName(),
                c.getEmail(),
                c.getPhone(),
                //                c.getCountry(),
                //                c.getCity(),
                //                c.getState(),
                c.getAddress(),
                c.getZipCode(),
                c.getStatus(),});
        }
    }

    public void filterList(String txt) {
//        SupplierDao cDao = new SupplierDao();
        List<Supplier> list = supplierController.filter(txt);
        DefaultTableModel data = (DefaultTableModel) jTableSuppliers.getModel();
        data.setNumRows(0);
        for (Supplier c : list) {
            data.addRow(new Object[]{
                c.getId(),
                c.getNif(),
                c.getName(),
                c.getEmail(),
                c.getPhone(),
                c.getGroupId(),
                //                c.getCountry(),
                //                c.getCity(),
                //                c.getState(),
                c.getAddress(),
                c.getZipCode(),
                c.getStatus(),
                c.getIsDefault()
            });
        }
    }

    public void screanListSupplier() {
        jTabbedPaneSupplier.setSelectedIndex(0);
        listSuppliers();
    }

    public void roles() {
        switch (session.getUser().getProfile()) {
            case "admin":
                break;
            case "user":
                this.jButtonOpenFormSupplier.setEnabled(false);
                this.jButtonAlterSeleted.setEnabled(false);
                this.jButtonDeleteSelected.setEnabled(false);
                break;
            case "seller":
                this.jButtonOpenFormSupplier.setEnabled(false);
                this.jButtonAlterSeleted.setEnabled(false);
                this.jButtonDeleteSelected.setEnabled(false);
                break;
            case "manager":
                this.jButtonOpenFormSupplier.setEnabled(false);
                this.jButtonAlterSeleted.setEnabled(false);
                this.jButtonDeleteSelected.setEnabled(false);
                break;
            default:
                break;
        }
    }

//    public Supplier validateSupllier() {
//
//        if (jTextFieldNif.getText().isEmpty() || jTextFieldNif.getText().length() < 9 || jTextFieldNif.getText().length() > 15) {
//            JOptionPane.showMessageDialog(null, "Campo NIF invalido!! no minimo 9 e no maximo 15 caracteres");
//        } else if (jTextFieldName.getText().isEmpty() || jTextFieldName.getText().length() < 3) {
//            JOptionPane.showMessageDialog(null, "Campo Name invalido!! no minimo 3 caracteres");
//        } else if (jTextFieldEmail.getText().isEmpty() || jTextFieldEmail.getText().length() < 3) {
//            JOptionPane.showMessageDialog(null, "Campo Email invalido!! no minimo 3 caracteres");
//        } else if (jTextFieldAddress.getText().isEmpty() || jTextFieldAddress.getText().length() < 3) {
//            JOptionPane.showMessageDialog(null, "Campo Endereço invalido!!");
//        } else if (jTextFieldPhone.getText().isEmpty() || jTextFieldPhone.getText().length() > 15) {
//            JOptionPane.showMessageDialog(null, "Campo Phone invalido!! no maximo 15 caracteres");
//        } else {
//            Supplier cModel = new Supplier();
//            cModel.setNif(jTextFieldNif.getText());
//            cModel.setName(jTextFieldName.getText());
//            cModel.setEmail(jTextFieldEmail.getText());
//            cModel.setAddress(jTextFieldAddress.getText());
//            cModel.setPhone(jTextFieldPhone.getText());
//            cModel.setCountry((Countries) jComboBoxCountry.getSelectedItem());
//            cModel.setCity(jTextFieldCity.getText());
//            cModel.setState(jTextFieldState.getText());
//            cModel.setZipCode(jTextFieldZipCode.getText());
//            cModel.setStatus(jComboBoxStatus.getSelectedIndex());
//            return cModel;
//        }
//        return null;
//    }
//    public void listComboCountries() {
//
//        List<Countries> list = countryController.get("");
//        jComboBoxCountry.removeAllItems();
//        for (Countries item : list) {
//            jComboBoxCountry.addItem(item);
//        }
//    }
    /**
     * Creates new form JPanelSupplier
     */
    public JPanelSupplier() {
        initComponents();
        session = UserSession.getInstance();
        listSuppliers();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPaneSupplier = new javax.swing.JTabbedPane();
        jPanelSearchSupplier = new javax.swing.JPanel();
        jTextFieldFilterNameTable = new javax.swing.JTextField();
        jButtonAlterSeleted = new javax.swing.JButton();
        jButtonDeleteSelected = new javax.swing.JButton();
        jButtonOpenFormSupplier = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableSuppliers = new javax.swing.JTable();
        jButtonViewSupplierSelected = new javax.swing.JButton();

        jPanelSearchSupplier.setBackground(new java.awt.Color(204, 204, 255));

        jTextFieldFilterNameTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldFilterNameTableKeyReleased(evt);
            }
        });

        jButtonAlterSeleted.setBackground(new java.awt.Color(255, 255, 153));
        jButtonAlterSeleted.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Edit Pencil.png"))); // NOI18N
        jButtonAlterSeleted.setText("Alterar");
        jButtonAlterSeleted.setContentAreaFilled(false);
        jButtonAlterSeleted.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonAlterSeleted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlterSeletedActionPerformed(evt);
            }
        });

        jButtonDeleteSelected.setBackground(new java.awt.Color(255, 51, 51));
        jButtonDeleteSelected.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Trash Can.png"))); // NOI18N
        jButtonDeleteSelected.setText("Excluir");
        jButtonDeleteSelected.setContentAreaFilled(false);
        jButtonDeleteSelected.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonDeleteSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteSelectedActionPerformed(evt);
            }
        });

        jButtonOpenFormSupplier.setBackground(new java.awt.Color(153, 153, 255));
        jButtonOpenFormSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Plus.png"))); // NOI18N
        jButtonOpenFormSupplier.setText("Adicionar");
        jButtonOpenFormSupplier.setContentAreaFilled(false);
        jButtonOpenFormSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonOpenFormSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenFormSupplierActionPerformed(evt);
            }
        });

        jTableSuppliers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NIF", "Nome", "Email", "Telefone", "Endereço", "Zip", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableSuppliers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableSuppliersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableSuppliers);

        jButtonViewSupplierSelected.setBackground(new java.awt.Color(153, 255, 153));
        jButtonViewSupplierSelected.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Binoculars.png"))); // NOI18N
        jButtonViewSupplierSelected.setText("Ver");
        jButtonViewSupplierSelected.setContentAreaFilled(false);
        jButtonViewSupplierSelected.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonViewSupplierSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewSupplierSelectedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelSearchSupplierLayout = new javax.swing.GroupLayout(jPanelSearchSupplier);
        jPanelSearchSupplier.setLayout(jPanelSearchSupplierLayout);
        jPanelSearchSupplierLayout.setHorizontalGroup(
            jPanelSearchSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSearchSupplierLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSearchSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanelSearchSupplierLayout.createSequentialGroup()
                        .addComponent(jTextFieldFilterNameTable, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 304, Short.MAX_VALUE)
                        .addComponent(jButtonOpenFormSupplier)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonViewSupplierSelected)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonAlterSeleted)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDeleteSelected)))
                .addContainerGap())
        );
        jPanelSearchSupplierLayout.setVerticalGroup(
            jPanelSearchSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSearchSupplierLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanelSearchSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldFilterNameTable, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonOpenFormSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAlterSeleted, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDeleteSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonViewSupplierSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
                .addGap(8, 8, 8))
        );

        jPanelSearchSupplierLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonAlterSeleted, jButtonDeleteSelected, jButtonOpenFormSupplier, jButtonViewSupplierSelected, jTextFieldFilterNameTable});

        jTabbedPaneSupplier.addTab("Fornecedores", jPanelSearchSupplier);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPaneSupplier)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneSupplier)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonViewSupplierSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewSupplierSelectedActionPerformed
        // TODO add your handling code here:
        int id = Integer.parseInt(jTableSuppliers.getValueAt(jTableSuppliers.getSelectedRow(), 0).toString());
        Supplier supplier = supplierController.getId(id);
        JOptionPane.showMessageDialog(null, "supplier :" + supplier.getName() + "\n NIF:" + supplier.getNif() + "\n Email:" + supplier.getEmail() + "\n Endereço:" + supplier.getAddress());
    }//GEN-LAST:event_jButtonViewSupplierSelectedActionPerformed

    private void jTableSuppliersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableSuppliersMouseClicked
        // TODO add your handling code here:
        //        jTabbedPaneSupplier.setSelectedIndex(1);
        //        jTextFieldId.setText(jTableSuppliers.getValueAt(jTableSuppliers.getSelectedRow(), 0).toString());
        //        jTextFieldNif.setText(jTableSuppliers.getValueAt(jTableSuppliers.getSelectedRow(), 1).toString());
        //        jTextFieldName.setText(jTableSuppliers.getValueAt(jTableSuppliers.getSelectedRow(), 2).toString());
        //        jTextFieldEmail.setText(jTableSuppliers.getValueAt(jTableSuppliers.getSelectedRow(), 3).toString());
        //        jTextFieldPhone.setText(jTableSuppliers.getValueAt(jTableSuppliers.getSelectedRow(), 4).toString());
        //        jComboBoxCountry.setSelectedItem(jTableSuppliers.getValueAt(jTableSuppliers.getSelectedRow(), 5));
        //        jTextFieldCity.setText(jTableSuppliers.getValueAt(jTableSuppliers.getSelectedRow(), 6).toString());
        //        jTextFieldState.setText(jTableSuppliers.getValueAt(jTableSuppliers.getSelectedRow(), 7).toString());
        //        jTextFieldAddress.setText(jTableSuppliers.getValueAt(jTableSuppliers.getSelectedRow(), 8).toString());
        //        jTextFieldZipCode.setText(jTableSuppliers.getValueAt(jTableSuppliers.getSelectedRow(), 9).toString());
        //        jComboBoxStatus.setSelectedIndex((int) jTableSuppliers.getValueAt(jTableSuppliers.getSelectedRow(), 10));
    }//GEN-LAST:event_jTableSuppliersMouseClicked

    private void jButtonOpenFormSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenFormSupplierActionPerformed
        // TODO add your handling code here:
        //        jTabbedPaneSupplier.setSelectedIndex(1);
        //        Utilities helpUtil = new Utilities();
        //        helpUtil.clearScreen(jPanelFormSupplier);
        JDialogFormSupplier formSupplier = new JDialogFormSupplier(null, true);
        formSupplier.setVisible(true);
        Boolean resp = formSupplier.getResponse();
        if (resp == true) {
            JOptionPane.showMessageDialog(null, "Usuario salvo com sucesso!!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            listSuppliers();
        }
    }//GEN-LAST:event_jButtonOpenFormSupplierActionPerformed

    private void jButtonDeleteSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteSelectedActionPerformed
        // TODO add your handling code here:
        int id = 0;
        try {
            id = (int) jTableSuppliers.getValueAt(jTableSuppliers.getSelectedRow(), 0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Selecione um Fornecedor na tabela!!", "Atencao", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (id > 0) {
                Supplier client = supplierController.getId(id);
                int sair = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja Deletar," + client.getName() + "?", "Atenção", JOptionPane.YES_NO_OPTION);
                if (sair == JOptionPane.YES_OPTION) {
                    if (supplierController.deleteId(id)) {
                        JOptionPane.showMessageDialog(null, "suppliers excluido com Sucesso!!");
                        listSuppliers();
                    }
                }
            }
        }

//        int id = Integer.parseInt(jTableSuppliers.getValueAt(jTableSuppliers.getSelectedRow(), 0).toString());
//        Supplier client = supplierController.getId(id);
//        //        JOptionPane.showMessageDialog(null, "Cliente :" + client.getName());
//        int sair = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja Deletar," + client.getName() + "?", "Atenção", JOptionPane.YES_NO_OPTION);
//        if (sair == JOptionPane.YES_OPTION) {
//            if (supplierController.deleteId(id)) {
//                JOptionPane.showMessageDialog(null, "suppliers excluido com Sucesso!!");
//                listSuppliers();
//            }
//        }
    }//GEN-LAST:event_jButtonDeleteSelectedActionPerformed

    private void jButtonAlterSeletedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlterSeletedActionPerformed
        // TODO add your handling code here:
        int value = 0;
        try {
            value = (int) jTableSuppliers.getValueAt(jTableSuppliers.getSelectedRow(), 0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Selecione um Fornecedor na tabela!!", "Atencao", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (value > 0) {
                JDialogFormSupplier formUser = new JDialogFormSupplier(null, true);
                formUser.setSupplier(value);
                formUser.setVisible(true);
                Boolean resp = formUser.getResponse();
                if (resp == true) {
                    JOptionPane.showMessageDialog(null, "Usuario salvo com sucesso!!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    listSuppliers();
                }
            }
        }
    }//GEN-LAST:event_jButtonAlterSeletedActionPerformed

    private void jTextFieldFilterNameTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldFilterNameTableKeyReleased
        // TODO add your handling code here:
        String txt = jTextFieldFilterNameTable.getText();
        if (!txt.isEmpty()) {
            filterList(txt);
        } else {
            listSuppliers();
        }
    }//GEN-LAST:event_jTextFieldFilterNameTableKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton jButtonAlterSeleted;
    public javax.swing.JButton jButtonDeleteSelected;
    private javax.swing.JButton jButtonOpenFormSupplier;
    private javax.swing.JButton jButtonViewSupplierSelected;
    private javax.swing.JPanel jPanelSearchSupplier;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPaneSupplier;
    private javax.swing.JTable jTableSuppliers;
    private javax.swing.JTextField jTextFieldFilterNameTable;
    // End of variables declaration//GEN-END:variables
}
