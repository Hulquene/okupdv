/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.okutonda.okudpdv.views.clients;

import com.okutonda.okudpdv.controllers.ClientController;
import com.okutonda.okudpdv.controllers.CountryController;
import com.okutonda.okudpdv.dao.ClientDao;
import com.okutonda.okudpdv.models.Clients;
import com.okutonda.okudpdv.utilities.Utilities;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author kenny
 */
public class JDialogFormClient extends javax.swing.JDialog {

    CountryController countryController = new CountryController();
    ClientController clientController = new ClientController();
    Boolean status;
    Clients client;

    /**
     * Creates new form JDialogFormClient
     */
    public JDialogFormClient(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        status = false;
    }

//    public void listComboCountries() {
//
//        List<Countries> list = countryController.get("");
//        jComboBoxCountryClient.removeAllItems();
//        for (Countries item : list) {
//            jComboBoxCountryClient.addItem(item);
//        }
//    }
    public Boolean getResponse() {
        return status;
    }

    public void setFormClient() {
//        User user = userController.getId(id);
        if (this.client != null) {
            jTextFieldIdClient.setText(String.valueOf(client.getId()));
            jTextFieldNifClient.setText(client.getNif());
            jTextFieldNameClient.setText(client.getName());
            jTextFieldEmailClient.setText(client.getEmail());
            jTextFieldPhoneClient.setText(client.getPhone());
//            jComboBoxProfile.setSelectedItem(user.getProfile());
//            jComboBoxCountryClient.setSelectedItem(client.getCountry());
            jTextFieldProvinciaClient.setText(client.getCity());
            jTextFieldAddressClient.setText(client.getAddress());
            jComboBoxStatusClient.setSelectedItem(client.getStatus());
            jComboBoxDefaultClient.setSelectedItem(client.getIsDefault());
            jTextFieldZipCodeClient.setText(client.getZipCode());
            jTextFieldMunicipioClient.setText(client.getState());
        }
    }

    public void setClient(int id) {
        this.client = clientController.getId(id);
//        setFormUser(user);
    }

    public Clients validateClient() {

        System.out.println("jComboBoxDefaultClient " + jComboBoxDefaultClient.getSelectedItem());
        System.out.println("jComboBoxDefaultClient " + jComboBoxDefaultClient.getSelectedItem());

        Clients cModel = new Clients();
        if (jTextFieldNifClient.getText().isEmpty() || jTextFieldNifClient.getText().length() < 9) {
            JOptionPane.showMessageDialog(null, "Campo NIF invalido!! no minimo 9 caracteres");
        } else if (jTextFieldNameClient.getText().isEmpty() || jTextFieldNameClient.getText().length() < 3) {
            JOptionPane.showMessageDialog(null, "Campo Nome invalido!! no minimo 3 caracteres");
        } else if (jTextFieldAddressClient.getText().isEmpty() || jTextFieldAddressClient.getText().length() < 3) {
            JOptionPane.showMessageDialog(null, "Campo Endereço invalido!! no minimo 3 caracteres");
        } else if (jTextFieldProvinciaClient.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Campo Provincia Obrigatorio!!");
        } else if (jTextFieldMunicipioClient.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Campo Municipio invalido!!");
        } else {
            System.out.println("nome:" + jTextFieldNameClient.getText());
            cModel.setNif(jTextFieldNifClient.getText());
            cModel.setName(jTextFieldNameClient.getText());
            cModel.setEmail(jTextFieldEmailClient.getText());
            cModel.setPhone(jTextFieldPhoneClient.getText());
//            cModel.setCountry((Countries) jComboBoxCountryClient.getSelectedItem());
//            cModel.setCity(jTextFieldCityClient.getText());
//            cModel.setState(jTextFieldStateClient.getText());
            cModel.setZipCode(jTextFieldZipCodeClient.getText());
            cModel.setAddress(jTextFieldAddressClient.getText());
            cModel.setCity(jTextFieldProvinciaClient.getText());
            cModel.setState(jTextFieldMunicipioClient.getText());

            String statusCliente = (String) jComboBoxStatusClient.getSelectedItem();
            String defaultClinte = (String) jComboBoxDefaultClient.getSelectedItem();

            if ("ativo".equals(statusCliente)) {
                cModel.setStatus(1);
            } else {
                cModel.setStatus(0);
            }
            if ("sim".equals(defaultClinte)) {
                cModel.setIsDefault(1);
            } else {
                cModel.setIsDefault(0);
            }
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
        jPanelFormClient = new javax.swing.JPanel();
        jTextFieldNifClient = new javax.swing.JTextField();
        jTextFieldNameClient = new javax.swing.JTextField();
        jTextFieldEmailClient = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldAddressClient = new javax.swing.JTextField();
        jTextFieldPhoneClient = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jTextFieldZipCodeClient = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextFieldMunicipioClient = new javax.swing.JTextField();
        jComboBoxDefaultClient = new javax.swing.JComboBox<>();
        jComboBoxStatusClient = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        jTextFieldProvinciaClient = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButtonAddClient = new javax.swing.JButton();
        jButtonClearFormClient = new javax.swing.JButton();
        jTextFieldIdClient = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 650));

        jPanelFormClient.setBackground(new java.awt.Color(255, 255, 255));
        jPanelFormClient.setPreferredSize(new java.awt.Dimension(750, 600));

        jTextFieldNifClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNifClientActionPerformed(evt);
            }
        });

        jTextFieldNameClient.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldNameClientKeyPressed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("* NIF:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("* Nome:");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Email:");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setText("* Endereco:");

        jTextFieldAddressClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldAddressClientActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Telefone:");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel19.setText("* Status");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setText(" zip_code :");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setText("Municipio");

        jTextFieldMunicipioClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldMunicipioClientActionPerformed(evt);
            }
        });

        jComboBoxDefaultClient.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "nao", "sim" }));
        jComboBoxDefaultClient.setEnabled(false);
        jComboBoxDefaultClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxDefaultClientActionPerformed(evt);
            }
        });

        jComboBoxStatusClient.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "inativo", "ativo" }));
        jComboBoxStatusClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxStatusClientActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel21.setText("* Padrão");

        jTextFieldProvinciaClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldProvinciaClientActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel20.setText("Provincia");

        javax.swing.GroupLayout jPanelFormClientLayout = new javax.swing.GroupLayout(jPanelFormClient);
        jPanelFormClient.setLayout(jPanelFormClientLayout);
        jPanelFormClientLayout.setHorizontalGroup(
            jPanelFormClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormClientLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanelFormClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelFormClientLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel11))
                    .addGroup(jPanelFormClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel10)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldNameClient, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                        .addGroup(jPanelFormClientLayout.createSequentialGroup()
                            .addGap(61, 61, 61)
                            .addComponent(jLabel12))
                        .addComponent(jTextFieldNifClient))
                    .addComponent(jTextFieldEmailClient, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jTextFieldAddressClient, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(67, 67, 67)
                .addGroup(jPanelFormClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldPhoneClient, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel17)
                    .addComponent(jTextFieldZipCodeClient, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelFormClientLayout.createSequentialGroup()
                        .addGroup(jPanelFormClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxDefaultClient, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelFormClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jComboBoxStatusClient, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel18)
                    .addComponent(jTextFieldMunicipioClient, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldProvinciaClient, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanelFormClientLayout.setVerticalGroup(
            jPanelFormClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormClientLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelFormClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelFormClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelFormClientLayout.createSequentialGroup()
                        .addComponent(jTextFieldNifClient, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldNameClient, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11))
                    .addGroup(jPanelFormClientLayout.createSequentialGroup()
                        .addComponent(jTextFieldPhoneClient, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldZipCodeClient, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelFormClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldEmailClient, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldProvinciaClient, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelFormClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel18))
                .addGap(4, 4, 4)
                .addGroup(jPanelFormClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldAddressClient, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldMunicipioClient, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanelFormClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelFormClientLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxDefaultClient, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(25, Short.MAX_VALUE))
                    .addGroup(jPanelFormClientLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxStatusClient, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Formulario de Cliente");

        jButtonAddClient.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonAddClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Plus.png"))); // NOI18N
        jButtonAddClient.setText("Salvar");
        jButtonAddClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddClientActionPerformed(evt);
            }
        });

        jButtonClearFormClient.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonClearFormClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Restart.png"))); // NOI18N
        jButtonClearFormClient.setText("Limpar");
        jButtonClearFormClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearFormClientActionPerformed(evt);
            }
        });

        jTextFieldIdClient.setEditable(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ID:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelFormClient, javax.swing.GroupLayout.PREFERRED_SIZE, 725, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(91, 91, 91)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldIdClient, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonAddClient, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonClearFormClient, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jButtonAddClient)
                    .addComponent(jButtonClearFormClient)
                    .addComponent(jTextFieldIdClient, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jPanelFormClient, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonAddClient, jTextFieldIdClient});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(778, 470));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldNifClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNifClientActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNifClientActionPerformed

    private void jTextFieldNameClientKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNameClientKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String name = jTextFieldNameClient.getText();
            Clients cModel;
            // cModel = new Clients();
            ClientDao cDao = new ClientDao();
            cModel = cDao.searchFromName(name);
            if (cModel.getName() != null) {
                jTextFieldIdClient.setText(Integer.toString(cModel.getId()));
                jTextFieldNifClient.setText(cModel.getNif());
                jTextFieldNameClient.setText(cModel.getName());
                jTextFieldEmailClient.setText(cModel.getEmail());
                jTextFieldAddressClient.setText(cModel.getAddress());
                jTextFieldPhoneClient.setText(cModel.getPhone());
                jTextFieldZipCodeClient.setText(cModel.getZipCode());
//                jTextFieldStateClient.setText(cModel.getState());
                jComboBoxDefaultClient.setSelectedItem(cModel.getStatus());
//                jComboBoxCountryClient.setSelectedItem(cModel.getCountry());
            } else {
                JOptionPane.showMessageDialog(null, "Cliente nao encontrado!");
            }
        }
    }//GEN-LAST:event_jTextFieldNameClientKeyPressed

    private void jTextFieldAddressClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldAddressClientActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldAddressClientActionPerformed

    private void jTextFieldMunicipioClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldMunicipioClientActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldMunicipioClientActionPerformed

    private void jComboBoxDefaultClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxDefaultClientActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxDefaultClientActionPerformed

    private void jButtonAddClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddClientActionPerformed
        // TODO: Passando os dados da interface para o model
        Clients cModel = validateClient();
        if (cModel != null) {
            int id = jTextFieldIdClient.getText().isEmpty() == true ? 0 : Integer.parseInt(jTextFieldIdClient.getText());
            status = false;
            if (id > 0) {
                clientController.add(cModel, id);
                status = true;
                System.out.println("atualizar");
            } else {
                clientController.add(cModel, 0);

                System.out.println("Adicionar");
                status = true;
            }
            this.dispose();
//            screanListUser();
//            Utilities helpUtil = new Utilities();
//            helpUtil.clearScreen(jPanelFormUser);
        }
    }//GEN-LAST:event_jButtonAddClientActionPerformed

    private void jButtonClearFormClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearFormClientActionPerformed
        // TODO add your handling code here:
        Utilities helpUtil = new Utilities();
        helpUtil.clearScreen(jPanelFormClient);
    }//GEN-LAST:event_jButtonClearFormClientActionPerformed

    private void jComboBoxStatusClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxStatusClientActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxStatusClientActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
//        listComboCountries();
        setFormClient();
    }//GEN-LAST:event_formWindowActivated

    private void jTextFieldProvinciaClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldProvinciaClientActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldProvinciaClientActionPerformed

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
            java.util.logging.Logger.getLogger(JDialogFormClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogFormClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogFormClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogFormClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogFormClient dialog = new JDialogFormClient(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonAddClient;
    private javax.swing.JButton jButtonClearFormClient;
    private javax.swing.JComboBox<String> jComboBoxDefaultClient;
    private javax.swing.JComboBox<String> jComboBoxStatusClient;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelFormClient;
    private javax.swing.JTextField jTextFieldAddressClient;
    private javax.swing.JTextField jTextFieldEmailClient;
    private javax.swing.JTextField jTextFieldIdClient;
    private javax.swing.JTextField jTextFieldMunicipioClient;
    private javax.swing.JTextField jTextFieldNameClient;
    private javax.swing.JTextField jTextFieldNifClient;
    private javax.swing.JTextField jTextFieldPhoneClient;
    private javax.swing.JTextField jTextFieldProvinciaClient;
    private javax.swing.JTextField jTextFieldZipCodeClient;
    // End of variables declaration//GEN-END:variables
}
