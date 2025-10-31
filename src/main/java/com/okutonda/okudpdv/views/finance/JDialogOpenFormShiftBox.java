/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.okutonda.okudpdv.views.finance;

import com.okutonda.okudpdv.controllers.ShiftController;
import com.okutonda.okudpdv.controllers.UserController;
import com.okutonda.okudpdv.data.entities.Shift;
import com.okutonda.okudpdv.data.entities.User;
import com.okutonda.okudpdv.helpers.UserSession;
import com.okutonda.okudpdv.helpers.Util;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author kenny
 */
public class JDialogOpenFormShiftBox extends javax.swing.JDialog {

//    BoxController boxController = new BoxController();
    ShiftController shiftController = new ShiftController();
    //UserSession session = UserSession.getInstance();
    UserController userController = new UserController();
    Boolean status = false;

    /**
     * Creates new form JDialogOpenBox
     */
    public JDialogOpenFormShiftBox(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

//    public void setFormProduct(Product prod) {
//        if (prod != null) {
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
//        }
//    }
//    public Boolean getResponse() {
//        return status;
//    }
//
//
//    public Boolean ValidateManager() {
//
//        String codeManager = new String(jPasswordFieldManagerPassword.getPassword());
//        String nameManager = (String) jComboBoxSelectedManager.getSelectedItem();
//        return !codeManager.isEmpty() && !nameManager.isEmpty();
//    }
//
//    public Shift validateShift() {
//        Shift cModel = new Shift();
//
//        if (jTextFieldShiftValueOpen.getText().isEmpty() && Util.isDouble(jTextFieldShiftValueOpen.getText())) {
//            JOptionPane.showMessageDialog(null, "Valor invalido!", "Atencao", JOptionPane.ERROR_MESSAGE);
//        } else if (jComboBoxSelectedUser.getSelectedItem() == null) {
//            JOptionPane.showMessageDialog(null, "Campo User invalido!! Selecione");
//        } else if (jComboBoxSeletecBox.getSelectedItem() == null) {
//            JOptionPane.showMessageDialog(null, "Campo Caixa invalido!! Selecione");
//        } else if (jComboBoxSelectedManager.getSelectedItem() == null) {
//            JOptionPane.showMessageDialog(null, "Campo Gestor invalido!! Selecione");
//        } else {
//            if (ValidateManager() == false) {
//                JOptionPane.showMessageDialog(null, "Prencha o formulario para continuar...", "Atenção", JOptionPane.ERROR_MESSAGE);
//            } else {
//
//                cModel.setBox((Box) jComboBoxSeletecBox.getSelectedItem());
//                cModel.setGrantedAmount(Double.valueOf(jTextFieldShiftValueOpen.getText()));
//                cModel.setIncurredAmount(0.0);
//                cModel.setClosingAmount(0.0);
//                cModel.setUser((User) jComboBoxSelectedUser.getSelectedItem());
//                cModel.setManager((User) jComboBoxSelectedManager.getSelectedItem());
//                return cModel;
////                Boolean resp = shiftController.add(cModel, 0);
////                if (resp == true) {
////                    this.dispose();
////                }
//            }
//        }
//        return null;
//    }
//
//    public void loadCombobox() {
//        List<Box> listS = boxController.findAll();
//        jComboBoxSeletecBox.removeAllItems();
//        for (Box item : listS) {
//            jComboBoxSeletecBox.addItem(item.getName());
//        }
//
//        List<User> listW = userController.getAll();
//        jComboBoxSelectedUser.removeAllItems();
//        jComboBoxSelectedManager.removeAllItems();
//        for (User item : listW) {
//            jComboBoxSelectedUser.addItem(item.getName());
//            jComboBoxSelectedManager.addItem(item.getName());
//        }
////        List<ReasonTaxes> listR = reasonTaxeController.get("");
////        jComboBoxReasonTaxeId.removeAllItems();
////        for (ReasonTaxes item : listR) {
////            jComboBoxReasonTaxeId.addItem(item);
////        }
//    }
    public Boolean getResponse() {
        return status;
    }

    public Boolean ValidateManager() {
        String codeManager = new String(jPasswordFieldManagerPassword.getPassword());
        String nameManager = (String) jComboBoxSelectedManager.getSelectedItem();
        return !codeManager.isEmpty() && !nameManager.isEmpty();
    }

    public Shift validateShift() {
        Shift cModel = new Shift();

        // Validação do valor de abertura
        if (jTextFieldShiftValueOpen.getText().isEmpty() || !Util.isDouble(jTextFieldShiftValueOpen.getText())) {
            JOptionPane.showMessageDialog(null, "Valor de abertura inválido!", "Atenção", JOptionPane.ERROR_MESSAGE);
            return null;
        } // Validação do usuário
        else if (jComboBoxSelectedUser.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Selecione um usuário!", "Atenção", JOptionPane.ERROR_MESSAGE);
            return null;
        } // Validação do gestor
        else if (jComboBoxSelectedManager.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Selecione um gestor!", "Atenção", JOptionPane.ERROR_MESSAGE);
            return null;
        } // Validação das credenciais do gestor
        else if (!validarSenhaGestor()) {
            JOptionPane.showMessageDialog(null, "Senha do gestor incorreta!", "Atenção", JOptionPane.ERROR_MESSAGE);
            return null;
        } else {
            try {
                // Configura o turno
                Double valorAbertura = Double.valueOf(jTextFieldShiftValueOpen.getText());

                cModel.setGrantedAmount(valorAbertura);
                cModel.setIncurredAmount(0.0);
                cModel.setClosingAmount(0.0);
                cModel.setStatus("open");

                // Obtém usuário selecionado
                User usuarioSelecionado = (User) jComboBoxSelectedUser.getSelectedItem();
                cModel.setUser(usuarioSelecionado);

                // Gera código e hash do turno
                cModel.setCode(gerarCodigoTurno());
                cModel.setHash(gerarHashTurno());
                cModel.setDateOpen(java.time.LocalDateTime.now().toString());

                return cModel;

            } catch (Exception e) {
                System.err.println("❌ Erro ao validar turno: " + e.getMessage());
                JOptionPane.showMessageDialog(null, "Erro ao configurar turno: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
    }

    /**
     * Valida a senha do gestor selecionado
     */
    private boolean validarSenhaGestor() {
        try {
            String senhaInserida = new String(jPasswordFieldManagerPassword.getPassword());
            User gestorSelecionado = (User) jComboBoxSelectedManager.getSelectedItem();

            if (gestorSelecionado == null || senhaInserida.isEmpty()) {
                return false;
            }

            // Usa o UserController para validar a senha
            UserController userController = new UserController();
            return userController.validarSenha(gestorSelecionado.getId(), senhaInserida);

        } catch (Exception e) {
            System.err.println("❌ Erro ao validar senha do gestor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gera um código único para o turno
     */
    private String gerarCodigoTurno() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return "SHIFT_" + timestamp.substring(timestamp.length() - 6);
    }

    /**
     * Gera hash único para o turno
     */
    private String gerarHashTurno() {
        User usuario = (User) jComboBoxSelectedUser.getSelectedItem();
        String userInfo = usuario != null ? usuario.getId().toString() : "unknown";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return "shift_" + userInfo + "_" + timestamp;
    }

    public void loadCombobox() {
        // Carrega usuários para seleção
        List<User> listUsers = userController.getAll();
        jComboBoxSelectedUser.removeAllItems();
        jComboBoxSelectedManager.removeAllItems();

        for (User user : listUsers) {
            jComboBoxSelectedUser.addItem(user);
            jComboBoxSelectedManager.addItem(user);
        }

        System.out.println("✅ Combobox carregados: " + listUsers.size() + " usuários");
    }

    /**
     * Método para abrir o turno validado
     */
    public boolean abrirTurnoValidado() {
        try {
            Shift turnoValidado = validateShift();

            if (turnoValidado == null) {
                return false; // Validação falhou
            }

            // Usa o ShiftController para abrir o turno
            ShiftController shiftController = new ShiftController();
            Shift turnoAberto = shiftController.abrirTurno(turnoValidado.getGrantedAmount());

            if (turnoAberto != null) {
                JOptionPane.showMessageDialog(null,
                        "✅ Turno aberto com sucesso!\n"
                        + "Código: " + turnoAberto.getCode() + "\n"
                        + "Usuário: " + turnoAberto.getUser().getName() + "\n"
                        + "Valor abertura: " + turnoAberto.getGrantedAmount() + " AOA",
                        "Turno Aberto", JOptionPane.INFORMATION_MESSAGE);

                this.status = true;
                return true;
            }

            return false;

        } catch (Exception e) {
            System.err.println("❌ Erro ao abrir turno: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                    "Erro ao abrir turno: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxSeletecBox = new javax.swing.JComboBox();
        jTextFieldShiftValueOpen = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboBoxSelectedManager = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jButtonSave = new javax.swing.JButton();
        jPasswordFieldManagerPassword = new javax.swing.JPasswordField();
        jComboBoxSelectedUser = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Abertura de Caixa");

        jLabel2.setText("Selecione o Caixa");

        jLabel3.setText("Valor de entrada");

        jLabel4.setText("Codigo do gerente");

        jLabel5.setText("Selecione o Gerente");

        jButtonSave.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonSave.setText("Abrir");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jLabel6.setText("Selecione o Vendedor");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jComboBoxSeletecBox, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBoxSelectedManager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBoxSelectedUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordFieldManagerPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldShiftValueOpen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6))
                .addContainerGap(61, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jComboBoxSelectedManager, jComboBoxSelectedUser, jComboBoxSeletecBox, jPasswordFieldManagerPassword, jTextFieldShiftValueOpen});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxSelectedUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxSeletecBox, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldShiftValueOpen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxSelectedManager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordFieldManagerPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(jButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(82, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jComboBoxSelectedManager, jComboBoxSelectedUser, jComboBoxSeletecBox, jPasswordFieldManagerPassword, jTextFieldShiftValueOpen});

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

        setSize(new java.awt.Dimension(616, 409));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        // TODO add your handling code here:
//        Shift shift = validateShift();
//        if (shift != null) {
////            shift.setUser(session.getUser());
//            Boolean resp = shiftController.openShift(shift);
//            if (resp == true) {
//                dispose();
//            }
//        } else {
//            JOptionPane.showMessageDialog(null, "Valor invalido!", "Atencao", JOptionPane.ERROR_MESSAGE);
//        }
        boolean sucesso = abrirTurnoValidado();
        if (sucesso) {
            this.dispose();
        }
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        loadCombobox();
    }//GEN-LAST:event_formWindowActivated

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
            java.util.logging.Logger.getLogger(JDialogOpenFormShiftBox.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogOpenFormShiftBox.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogOpenFormShiftBox.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogOpenFormShiftBox.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogOpenFormShiftBox dialog = new JDialogOpenFormShiftBox(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox jComboBoxSelectedManager;
    private javax.swing.JComboBox jComboBoxSelectedUser;
    private javax.swing.JComboBox jComboBoxSeletecBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordFieldManagerPassword;
    private javax.swing.JTextField jTextFieldShiftValueOpen;
    // End of variables declaration//GEN-END:variables
}
