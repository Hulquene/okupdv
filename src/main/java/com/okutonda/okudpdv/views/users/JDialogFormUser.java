/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.okutonda.okudpdv.views.users;

import com.okutonda.okudpdv.controllers.UserController;
import com.okutonda.okudpdv.models.User;
import com.okutonda.okudpdv.utilities.Utilities;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author kenny
 */
public class JDialogFormUser extends javax.swing.JDialog {

    UserController userController = new UserController();
//    CountryController countryController = new CountryController();
    Boolean status;
    User user;

    /**
     * Creates new form JDialogFormUser
     */
    public JDialogFormUser(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        status = false;
    }

    public Boolean getResponse() {
        return status;
    }

    public void setFormUser(User user) {
//        User user = userController.getId(id);
        if (user != null) {
            jTextFieldID.setText(String.valueOf(user.getId()));
            jTextFieldNif.setText(user.getNif());
            jTextFieldName.setText(user.getName());
            jFormattedTextFieldBirthdate.setText(user.getBirthdate());
            jTextFieldEmail.setText(user.getEmail());
            jTextFieldPhone.setText(user.getPhone());
            jComboBoxProfile.setSelectedItem(user.getProfile());
            jTextFieldCountry.setText(user.getCountry());
            jTextFieldCity.setText(user.getCity());
            jTextFieldAddress.setText(user.getAddress());
            jComboBoxStatus.setSelectedItem(user.getStatus());
        } else {
//            JOptionPane.showMessageDialog(null, "Usuario nao encotrado!" + id);
        }
    }

    public void setUser(int id) {
        this.user = userController.getId(id);
//        setFormUser(user);
    }

    public User validateUser() {

        if (jTextFieldNif.getText().isEmpty() || jTextFieldNif.getText().length() < 9 || jTextFieldNif.getText().length() > 15) {
            JOptionPane.showMessageDialog(null, "Campo NIF invalido!! no minimo 9 e no maximo 15 caracteres");
        } else if (jTextFieldName.getText().isEmpty() || jTextFieldName.getText().length() < 3) {
            JOptionPane.showMessageDialog(null, "Campo Name invalido!! no minimo 3 caracteres");
        } else if (jTextFieldEmail.getText().isEmpty() || jTextFieldEmail.getText().length() < 3) {
            JOptionPane.showMessageDialog(null, "Campo Email invalido!! no minimo 3 caracteres");
        } else if (jTextFieldAddress.getText().isEmpty() || jTextFieldAddress.getText().length() < 3) {
            JOptionPane.showMessageDialog(null, "Campo Endereço invalido!!");
//        } else if (jTextFieldPhone.getText().isEmpty() || jTextFieldPhone.getText().length() > 15) {
//            JOptionPane.showMessageDialog(null, "Campo Phone invalido!! no maximo 15 caracteres");
        } else {
            String repitPassword, password;
            password = new String(jPasswordFieldPassword.getPassword());
            repitPassword = new String(jPasswordFieldRepitPassword.getPassword());
            Boolean save = false;
            User cModel = new User();
            if (jTextFieldID.getText().isEmpty()) {
//                System.out.println(""+repitPassword.isEmpty());
//                System.out.println("repitPassword"+repitPassword.isEmpty());
//                System.out.println(""+password.equals(repitPassword));
                if ((repitPassword.isEmpty() || repitPassword.isEmpty()) || !password.equals(repitPassword)) {
                    JOptionPane.showMessageDialog(null, "Verifica a senha para continuar...", "Atenção", JOptionPane.ERROR_MESSAGE);
                } else {
                    save = true;
                }
            } else {
                save = true;
            }

            if (save == true) {
                cModel.setNif(jTextFieldNif.getText());
                cModel.setName(jTextFieldName.getText());
                cModel.setEmail(jTextFieldEmail.getText());
                cModel.setAddress(jTextFieldAddress.getText());
                cModel.setCountry(jTextFieldCountry.getText());
                cModel.setCity(jTextFieldCity.getText());
                cModel.setPhone(jTextFieldPhone.getText());
                cModel.setBirthdate(jFormattedTextFieldBirthdate.getText());
                cModel.setProfile(jComboBoxProfile.getSelectedItem().toString());
                String statusUser = (String) jComboBoxStatus.getSelectedItem();
                if ("ativo".equals(statusUser)) {
                    cModel.setStatus(1);
                } else {
                    cModel.setStatus(0);
                }
                cModel.setPassword(password);
                return cModel;
            }
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
        jLabel2 = new javax.swing.JLabel();
        jTextFieldID = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButtonAdd = new javax.swing.JButton();
        jButtonClearForm = new javax.swing.JButton();
        jPanelFormUser = new javax.swing.JPanel();
        jTextFieldNif = new javax.swing.JTextField();
        jTextFieldName = new javax.swing.JTextField();
        jTextFieldEmail = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldAddress = new javax.swing.JTextField();
        jTextFieldPhone = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jComboBoxProfile = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        jButtonSearchClientForm = new javax.swing.JButton();
        jComboBoxStatus = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        jFormattedTextFieldBirthdate = new javax.swing.JFormattedTextField();
        jPasswordFieldPassword = new javax.swing.JPasswordField();
        jPasswordFieldRepitPassword = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldCity = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldCountry = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 0, 51));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Formulario de Usuario");

        jTextFieldID.setEditable(false);
        jTextFieldID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldIDActionPerformed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ID:");

        jButtonAdd.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Plus.png"))); // NOI18N
        jButtonAdd.setText("Salvar");
        jButtonAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jButtonClearForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Restart.png"))); // NOI18N
        jButtonClearForm.setText("Limpar");
        jButtonClearForm.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonClearForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearFormActionPerformed(evt);
            }
        });

        jPanelFormUser.setBackground(new java.awt.Color(204, 204, 255));

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

        jLabel9.setText("* NIF:");

        jLabel10.setText("* Nome:");

        jLabel11.setText("* Email:");

        jLabel13.setText("Provincia");

        jTextFieldAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldAddressActionPerformed(evt);
            }
        });

        jLabel14.setText("* Telefone:");

        jLabel19.setText("* Estado");

        jComboBoxProfile.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "seller", "manager", "admin" }));

        jLabel20.setText("Grupo");

        jButtonSearchClientForm.setText("Localizar");
        jButtonSearchClientForm.setEnabled(false);
        jButtonSearchClientForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchClientFormActionPerformed(evt);
            }
        });

        jComboBoxStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "inativo", "ativo" }));
        jComboBoxStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxStatusActionPerformed(evt);
            }
        });

        jLabel17.setText("Aniversario");

        try {
            jFormattedTextFieldBirthdate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jPasswordFieldPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldPasswordActionPerformed(evt);
            }
        });

        jLabel3.setText("Senha");

        jLabel4.setText("Repete a senha");

        jTextFieldCity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCityActionPerformed(evt);
            }
        });

        jLabel15.setText("* Endereco:");

        jTextFieldCountry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCountryActionPerformed(evt);
            }
        });

        jLabel16.setText("Pais");

        javax.swing.GroupLayout jPanelFormUserLayout = new javax.swing.GroupLayout(jPanelFormUser);
        jPanelFormUser.setLayout(jPanelFormUserLayout);
        jPanelFormUserLayout.setHorizontalGroup(
            jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormUserLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelFormUserLayout.createSequentialGroup()
                        .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelFormUserLayout.createSequentialGroup()
                                .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldCountry, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(88, 88, 88)
                                .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldPhone)
                                    .addComponent(jLabel20))
                                .addGap(34, 34, 34)
                                .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jFormattedTextFieldBirthdate)
                                    .addGroup(jPanelFormUserLayout.createSequentialGroup()
                                        .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel17)
                                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(jPanelFormUserLayout.createSequentialGroup()
                                .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelFormUserLayout.createSequentialGroup()
                                        .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldAddress))
                                        .addGap(18, 18, 18))
                                    .addGroup(jPanelFormUserLayout.createSequentialGroup()
                                        .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldCity, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanelFormUserLayout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(88, 88, 88)))
                                .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelFormUserLayout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 247, Short.MAX_VALUE))
                                    .addGroup(jPanelFormUserLayout.createSequentialGroup()
                                        .addComponent(jComboBoxProfile, 0, 199, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jComboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanelFormUserLayout.createSequentialGroup()
                                .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addGroup(jPanelFormUserLayout.createSequentialGroup()
                                        .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanelFormUserLayout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel14)
                                            .addComponent(jButtonSearchClientForm))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldNif, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanelFormUserLayout.createSequentialGroup()
                        .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelFormUserLayout.createSequentialGroup()
                                .addComponent(jPasswordFieldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12)
                                .addGap(18, 18, 18)
                                .addComponent(jPasswordFieldRepitPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jPanelFormUserLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jTextFieldAddress, jTextFieldEmail, jTextFieldName});

        jPanelFormUserLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jComboBoxProfile, jTextFieldPhone});

        jPanelFormUserLayout.setVerticalGroup(
            jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormUserLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelFormUserLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldNif, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel17))
                    .addGroup(jPanelFormUserLayout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonSearchClientForm, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(2, 2, 2)
                .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelFormUserLayout.createSequentialGroup()
                        .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextFieldBirthdate, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(jLabel19))
                        .addGap(4, 4, 4)
                        .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(146, 146, 146))
                    .addGroup(jPanelFormUserLayout.createSequentialGroup()
                        .addComponent(jTextFieldCountry, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldCity, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelFormUserLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelFormUserLayout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel12))
                            .addGroup(jPanelFormUserLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelFormUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jPasswordFieldRepitPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPasswordFieldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jLabel3))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jPanelFormUserLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jTextFieldAddress, jTextFieldEmail, jTextFieldName, jTextFieldNif});

        jPanelFormUserLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jComboBoxProfile, jComboBoxStatus, jFormattedTextFieldBirthdate, jTextFieldPhone});

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldID, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(jButtonClearForm)
                .addGap(18, 18, 18)
                .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addComponent(jPanelFormUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldID, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonClearForm, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(jPanelFormUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(816, 608));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldNifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNifActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNifActionPerformed

    private void jTextFieldNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String name = jTextFieldName.getText();
            User cModel;
            cModel = userController.getName(name);
            if (cModel.getName() != null) {
                jTextFieldID.setText(Integer.toString(cModel.getId()));
                jTextFieldNif.setText(cModel.getNif());
                jTextFieldName.setText(cModel.getName());
                jTextFieldEmail.setText(cModel.getEmail());
                jTextFieldCity.setText(cModel.getCity());
                jTextFieldCountry.setText(cModel.getCountry());
                jTextFieldAddress.setText(cModel.getAddress());
                jTextFieldPhone.setText(cModel.getPhone());
                jComboBoxProfile.setSelectedItem(cModel.getProfile());
                jComboBoxStatus.setSelectedItem(cModel.getStatus());
            } else {
                JOptionPane.showMessageDialog(null, "usuario nao encontrado!");
            }

        }
    }//GEN-LAST:event_jTextFieldNameKeyPressed

    private void jTextFieldAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldAddressActionPerformed

    private void jButtonSearchClientFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchClientFormActionPerformed
        // TODO add your handling code here:
        String name = jTextFieldName.getText();
        User cModel = userController.getName(name);
        if (cModel.getName() != null) {
            jTextFieldID.setText(Integer.toString(cModel.getId()));
            jTextFieldNif.setText(cModel.getNif());
            jTextFieldName.setText(cModel.getName());
            jFormattedTextFieldBirthdate.setText(cModel.getBirthdate());
            jTextFieldEmail.setText(cModel.getEmail());
            jTextFieldAddress.setText(cModel.getAddress());
            jTextFieldPhone.setText(cModel.getPhone());
            jComboBoxProfile.setSelectedItem(cModel.getProfile());
            jComboBoxStatus.setSelectedItem(cModel.getStatus());
//            jComboBoxCountry.setSelectedItem(cModel.getCountry());
        } else {
            JOptionPane.showMessageDialog(null, "Cliente nao encontrado!");
        }
    }//GEN-LAST:event_jButtonSearchClientFormActionPerformed

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        // TODO: Passando os dados da interface para o model
//        System.out.println("ennnn");
        User cModel = validateUser();
//        System.out.println("ennnn" + cModel.getName());
        if (cModel != null) {
            int id = jTextFieldID.getText().isEmpty() == true ? 0 : Integer.parseInt(jTextFieldID.getText());
            status = false;
            if (id > 0) {
                userController.add(cModel, id);
                status = true;
                System.out.println("atualizar");
            } else {
                userController.add(cModel, 0);

                System.out.println("Adicionar");
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
        helpUtil.clearScreen(jPanelFormUser);
    }//GEN-LAST:event_jButtonClearFormActionPerformed

    private void jTextFieldIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldIDActionPerformed

    private void jComboBoxStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxStatusActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
//        System.out.println("ssss");
//        listComboCountries();
        this.setFormUser(this.user);
    }//GEN-LAST:event_formWindowActivated

    private void jPasswordFieldPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordFieldPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordFieldPasswordActionPerformed

    private void jTextFieldCityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldCityActionPerformed

    private void jTextFieldCountryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCountryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldCountryActionPerformed

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
            java.util.logging.Logger.getLogger(JDialogFormUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogFormUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogFormUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogFormUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogFormUser dialog = new JDialogFormUser(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonSearchClientForm;
    private javax.swing.JComboBox<String> jComboBoxProfile;
    private javax.swing.JComboBox<String> jComboBoxStatus;
    private javax.swing.JFormattedTextField jFormattedTextFieldBirthdate;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelFormUser;
    private javax.swing.JPasswordField jPasswordFieldPassword;
    private javax.swing.JPasswordField jPasswordFieldRepitPassword;
    private javax.swing.JTextField jTextFieldAddress;
    private javax.swing.JTextField jTextFieldCity;
    private javax.swing.JTextField jTextFieldCountry;
    private javax.swing.JTextField jTextFieldEmail;
    private javax.swing.JTextField jTextFieldID;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldNif;
    private javax.swing.JTextField jTextFieldPhone;
    // End of variables declaration//GEN-END:variables
}
