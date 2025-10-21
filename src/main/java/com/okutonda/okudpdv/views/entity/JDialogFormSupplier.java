/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.okutonda.okudpdv.views.entity;

import com.okutonda.okudpdv.controllers.CountryController;
import com.okutonda.okudpdv.controllers.SupplierController;
import com.okutonda.okudpdv.data.dao.SupplierDao;
import com.okutonda.okudpdv.data.entities.Countries;
import com.okutonda.okudpdv.data.entities.Supplier;
import com.okutonda.okudpdv.utilities.UserSession;
import com.okutonda.okudpdv.utilities.Utilities;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author kenny
 */
public class JDialogFormSupplier extends javax.swing.JDialog {

    SupplierController supplierController = new SupplierController();
    CountryController countryController = new CountryController();
    UserSession session;
    Boolean status;
    Supplier supplier;

    /**
     * Creates new form JDialogFormSupplier
     */
    public JDialogFormSupplier(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        session = UserSession.getInstance();
        status = false;
    }

    public Boolean getResponse() {
        return status;
    }

    public void listComboCountries() {

        List<Countries> list = countryController.get("");
        jComboBoxCountry.removeAllItems();
        for (Countries item : list) {
            jComboBoxCountry.addItem(item.getLong_name());
        }
    }

    public void setFormSupplier() {
//        User user = userController.getId(id);
        if (supplier != null) {
            jTextFieldId.setText(String.valueOf(supplier.getId()));
            jTextFieldNif.setText(supplier.getNif());
            jTextFieldName.setText(supplier.getName());
            jTextFieldEmail.setText(supplier.getEmail());
            jTextFieldPhone.setText(supplier.getPhone());
            jComboBoxCountry.setSelectedItem(supplier.getCountry());
            jTextFieldCity.setText(supplier.getCity());
            jTextFieldAddress.setText(supplier.getAddress());
            jComboBoxStatus.setSelectedItem(supplier.getStatus());
//            jTextFieldState.setText(supplier.getState());
            jTextFieldZipCode.setText(supplier.getZipCode());
        } 
    }

    public void setSupplier(int id) {
        this.supplier = supplierController.getId(id);
//        setFormUser(user);
    }

    public Supplier validateSupllier() {

        if (jTextFieldNif.getText().isEmpty() || jTextFieldNif.getText().length() < 9 || jTextFieldNif.getText().length() > 15) {
            JOptionPane.showMessageDialog(null, "Campo NIF invalido!! no minimo 9 e no maximo 15 caracteres");
        } else if (jTextFieldName.getText().isEmpty() || jTextFieldName.getText().length() < 3) {
            JOptionPane.showMessageDialog(null, "Campo Name invalido!! no minimo 3 caracteres");
//        } else if (jTextFieldEmail.getText().isEmpty() || jTextFieldEmail.getText().length() < 3) {
//            JOptionPane.showMessageDialog(null, "Campo Email invalido!! no minimo 3 caracteres");
        } else if (jTextFieldAddress.getText().isEmpty() || jTextFieldAddress.getText().length() < 3) {
            JOptionPane.showMessageDialog(null, "Campo Endereço invalido!!");
        } else if (jTextFieldPhone.getText().isEmpty() || jTextFieldPhone.getText().length() > 15) {
            JOptionPane.showMessageDialog(null, "Campo Phone invalido!! no maximo 15 caracteres");
        } else {
            Supplier cModel = new Supplier();
            cModel.setNif(jTextFieldNif.getText());
            cModel.setName(jTextFieldName.getText());
            cModel.setEmail(jTextFieldEmail.getText());
            cModel.setAddress(jTextFieldAddress.getText());
            cModel.setPhone(jTextFieldPhone.getText());
//            cModel.setCountry((Countries) jComboBoxCountry.getSelectedItem());
            cModel.setCity(jTextFieldCity.getText());
//            cModel.setState(jTextFieldState.getText());
            cModel.setZipCode(jTextFieldZipCode.getText());
//            cModel.setStatus((String) jComboBoxStatus.getSelectedItem());
            cModel.setIsDefault(0);

            String statusSupp = (String) jComboBoxStatus.getSelectedItem();
//            String defaultSupp = (String) jComboBoxDefaultClient.getSelectedItem();

            if ("ativo".equals(statusSupp)) {
                cModel.setStatus(1);
            } else {
                cModel.setStatus(0);
            }

//            if ("sim".equals(defaultSupp)) {
//                cModel.setIsDefault(1);
//            } else {
//                cModel.setIsDefault(0);
//            }
            return cModel;
        }
        return null;
    }

//    public void listComboCountries() {
//
//        List<Countries> list = countryController.get("");
//        jComboBoxCountry.removeAllItems();
//        for (Countries item : list) {
//            jComboBoxCountry.addItem(item);
//        }
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanelFormSupplier = new javax.swing.JPanel();
        jTextFieldNif = new javax.swing.JTextField();
        jTextFieldName = new javax.swing.JTextField();
        jTextFieldEmail = new javax.swing.JTextField();
        jLabelNifClient = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldAddress = new javax.swing.JTextField();
        jTextFieldPhone = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jTextFieldZipCode = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jComboBoxStatus = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jComboBoxCountry = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jTextFieldCity = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldId = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jButtonAdd = new javax.swing.JButton();
        jButtonClearForm = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 0, 51));

        jPanelFormSupplier.setBackground(new java.awt.Color(204, 204, 255));

        jTextFieldNif.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNifActionPerformed(evt);
            }
        });

        jTextFieldName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldNameKeyPressed(evt);
            }
        });

        jLabelNifClient.setText("* NIF:");

        jLabel10.setText("* Nome:");

        jLabel11.setText("Email:");

        jLabel13.setText("Provincia");

        jTextFieldAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldAddressActionPerformed(evt);
            }
        });

        jLabel14.setText("* Telefone:");

        jLabel19.setText("* Status");

        jLabel17.setText(" zip_code :");

        jComboBoxStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "inativo", "ativo" }));
        jComboBoxStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxStatusActionPerformed(evt);
            }
        });

        jLabel15.setText("Pais");

        jComboBoxCountry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxCountryActionPerformed(evt);
            }
        });

        jLabel16.setText("* Endereco:");

        jTextFieldCity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCityActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelFormSupplierLayout = new javax.swing.GroupLayout(jPanelFormSupplier);
        jPanelFormSupplier.setLayout(jPanelFormSupplierLayout);
        jPanelFormSupplierLayout.setHorizontalGroup(
            jPanelFormSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormSupplierLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelFormSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelFormSupplierLayout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jLabel12))
                    .addComponent(jLabel13)
                    .addComponent(jLabel10)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jTextFieldName, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                    .addComponent(jComboBoxCountry, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldCity, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                    .addComponent(jTextFieldAddress)
                    .addComponent(jTextFieldZipCode, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanelFormSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelFormSupplierLayout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(jPanelFormSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel14)
                            .addComponent(jLabelNifClient, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelFormSupplierLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanelFormSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldNif, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19))))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanelFormSupplierLayout.setVerticalGroup(
            jPanelFormSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormSupplierLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanelFormSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNifClient)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelFormSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldNif, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelFormSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelFormSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxCountry, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelFormSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelFormSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCity, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelFormSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel19))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldZipCode, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanelFormSupplierLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jComboBoxStatus, jTextFieldAddress, jTextFieldEmail, jTextFieldName, jTextFieldNif, jTextFieldPhone, jTextFieldZipCode});

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Parc Formulario Fornecedor");

        jTextFieldId.setEditable(false);

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("ID");

        jButtonAdd.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Plus.png"))); // NOI18N
        jButtonAdd.setText("Salvar");
        jButtonAdd.setBorderPainted(false);
        jButtonAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jButtonClearForm.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonClearForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Restart.png"))); // NOI18N
        jButtonClearForm.setText("Limpar");
        jButtonClearForm.setBorderPainted(false);
        jButtonClearForm.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonClearForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearFormActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jPanelFormSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldId, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonClearForm, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldId, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonClearForm, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanelFormSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(141, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setSize(new java.awt.Dimension(778, 501));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldNifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNifActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNifActionPerformed

    private void jTextFieldNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String name = jTextFieldName.getText();
            Supplier cModel;
            SupplierDao cDao = new SupplierDao();
            cModel = cDao.searchFromName(name);
            if (cModel.getName() != null) {
                jTextFieldId.setText(Integer.toString(cModel.getId()));
                jTextFieldNif.setText(cModel.getNif());
                jTextFieldName.setText(cModel.getName());
                jTextFieldEmail.setText(cModel.getEmail());
//                jComboBoxCountry.setSelectedItem(cModel.getCountry());
                jTextFieldPhone.setText(cModel.getPhone());
//                jTextFieldState.setText(cModel.getState());
                jTextFieldAddress.setText(cModel.getAddress());
                jTextFieldZipCode.setText(cModel.getZipCode());
                jComboBoxStatus.setSelectedItem(cModel.getStatus());
            } else {
                JOptionPane.showMessageDialog(null, "Cliente nao encontrado!");
            }

        }
    }//GEN-LAST:event_jTextFieldNameKeyPressed

    private void jTextFieldAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldAddressActionPerformed

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        // TODO: Passando os dados da interface para o model
        Supplier cModel = validateSupllier();
        if (cModel != null) {
            int id = jTextFieldId.getText().isEmpty() == true ? 0 : Integer.parseInt(jTextFieldId.getText());
            status = false;
            if (id > 0) {
                supplierController.add(cModel, id);
                status = true;
            } else {
                supplierController.add(cModel, 0);
                status = true;
            }
            this.dispose();
//            screanListUser();
//            Utilities helpUtil = new Utilities();
//            helpUtil.clearScreen(jPanelFormUser);
        }


    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonClearFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearFormActionPerformed
        // TODO add your handling code here:
        Utilities helpUtil = new Utilities();
        helpUtil.clearScreen(jPanelFormSupplier);
    }//GEN-LAST:event_jButtonClearFormActionPerformed

    private void jComboBoxStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxStatusActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
//        listComboCountries();
        this.setFormSupplier();
    }//GEN-LAST:event_formWindowActivated

    private void jComboBoxCountryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxCountryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxCountryActionPerformed

    private void jTextFieldCityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldCityActionPerformed

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
            java.util.logging.Logger.getLogger(JDialogFormSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogFormSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogFormSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogFormSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogFormSupplier dialog = new JDialogFormSupplier(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonClearForm;
    private javax.swing.JComboBox<String> jComboBoxCountry;
    private javax.swing.JComboBox<String> jComboBoxStatus;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabelNifClient;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelFormSupplier;
    private javax.swing.JTextField jTextFieldAddress;
    private javax.swing.JTextField jTextFieldCity;
    private javax.swing.JTextField jTextFieldEmail;
    private javax.swing.JTextField jTextFieldId;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldNif;
    private javax.swing.JTextField jTextFieldPhone;
    private javax.swing.JTextField jTextFieldZipCode;
    // End of variables declaration//GEN-END:variables
}
