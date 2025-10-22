/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.okutonda.okudpdv.views.entity;

import com.okutonda.okudpdv.controllers.ClientController;
import com.okutonda.okudpdv.controllers.SupplierController;
import com.okutonda.okudpdv.data.dao.ClientDao;
import com.okutonda.okudpdv.data.entities.Clients;
import com.okutonda.okudpdv.data.entities.Supplier;
import com.okutonda.okudpdv.ui.TemaCleaner;
import com.okutonda.okudpdv.ui.TemaCores;
import com.okutonda.okudpdv.utilities.UserSession;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kenny
 */
public final class JPanelEntity extends javax.swing.JPanel {

//    CountryController countryController = new CountryController();
    ClientController clientController = new ClientController();
    SupplierController supplierController = new SupplierController();
    UserSession session;

    /**
     * Creates new form JPanelClient
     */
    public JPanelEntity() {
        initComponents();
        applyTheme();
        session = UserSession.getInstance();
        listClients();
        listSuppliers();
    }

    private void applyTheme() {
        TemaCleaner.clearBuilderOverrides(jPanelClient);
        // Painel de fundo da janela
        jPanelClient.setBackground(TemaCores.BG_LIGHT);
    }

    public void listClients() {
        ClientDao cDao = new ClientDao();
        List<Clients> list = cDao.findAll();
//        jTableClients.setModel(new DefaultTableModel);
        DefaultTableModel data = (DefaultTableModel) jTableClients.getModel();
//        data.setM
        data.setNumRows(0);
        for (Clients c : list) {
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
                c.getStatus(),
                c.getIsDefault()
            });
        }
    }

    public void filterListClients(String txt) {
        ClientDao cDao = new ClientDao();
        List<Clients> list = cDao.filter(txt);
        DefaultTableModel data = (DefaultTableModel) jTableClients.getModel();
        data.setNumRows(0);
        for (Clients c : list) {
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
                c.getStatus(),
                c.getIsDefault()
            });
        }
    }

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

    public void filterSupplierList(String txt) {
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

//    public Clients validateClient() {
//
//        Clients cModel = new Clients();
//        if (jTextFieldNifClient.getText().isEmpty() || jTextFieldNifClient.getText().length() < 9) {
//            JOptionPane.showMessageDialog(null, "Campo NIF invalido!! no minimo 9 caracteres");
//        } else if (jTextFieldNameClient.getText().isEmpty() || jTextFieldNameClient.getText().length() < 3) {
//            JOptionPane.showMessageDialog(null, "Campo Nome invalido!! no minimo 3 caracteres");
//        } else if (jTextFieldAddressClient.getText().isEmpty() || jTextFieldAddressClient.getText().length() < 3) {
//            JOptionPane.showMessageDialog(null, "Campo Endereço invalido!! no minimo 3 caracteres");
//        } else {
//            System.out.println("nome:" + jTextFieldNameClient.getText());
//            cModel.setNif(jTextFieldNifClient.getText());
//            cModel.setName(jTextFieldNameClient.getText());
//            cModel.setEmail(jTextFieldEmailClient.getText());
//            cModel.setAddress(jTextFieldAddressClient.getText());
//            cModel.setPhone(jTextFieldPhoneClient.getText());
//            cModel.setCountry((Countries) jComboBoxCountryClient.getSelectedItem());
//            cModel.setCity(jTextFieldCityClient.getText());
//            cModel.setState(jTextFieldStateClient.getText());
//            cModel.setZipCode(jTextFieldZipCodeClient.getText());
//            cModel.setStatus(jComboBoxDefaultClient.getSelectedIndex());
//            cModel.setIsDefault(jComboBoxDefaultClient.getSelectedIndex());
//            return cModel;
//        }
//
//        return null;
//    }
    public void screanListClient() {
        jTabbedPaneClient.setSelectedIndex(0);
        listClients();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPaneClient = new javax.swing.JTabbedPane();
        jPanelClient = new javax.swing.JPanel();
        jButtonFilterClientNameTable = new javax.swing.JButton();
        jTextFieldFilterClientNameTable = new javax.swing.JTextField();
        jButtonAlterClientSeletedTable = new javax.swing.JButton();
        jButtonDeleteClientSelectedTable = new javax.swing.JButton();
        jButtonCreateNewClient = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableClients = new javax.swing.JTable();
        jButtonCreateViewClient = new javax.swing.JButton();
        jPanelSearchSupplier = new javax.swing.JPanel();
        jTextFieldFilterNameTable = new javax.swing.JTextField();
        jButtonAlterSeleted = new javax.swing.JButton();
        jButtonDeleteSelected = new javax.swing.JButton();
        jButtonOpenFormSupplier = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableSuppliers = new javax.swing.JTable();
        jButtonViewSupplierSelected = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(900, 600));
        setPreferredSize(new java.awt.Dimension(900, 600));

        jTabbedPaneClient.setBackground(new java.awt.Color(255, 255, 255));

        jPanelClient.setBackground(new java.awt.Color(204, 204, 255));
        jPanelClient.setPreferredSize(new java.awt.Dimension(900, 600));

        jButtonFilterClientNameTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Search.png"))); // NOI18N
        jButtonFilterClientNameTable.setText("Pesquisar");
        jButtonFilterClientNameTable.setContentAreaFilled(false);
        jButtonFilterClientNameTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonFilterClientNameTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFilterClientNameTableActionPerformed(evt);
            }
        });

        jTextFieldFilterClientNameTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldFilterClientNameTableKeyReleased(evt);
            }
        });

        jButtonAlterClientSeletedTable.setBackground(new java.awt.Color(255, 255, 102));
        jButtonAlterClientSeletedTable.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonAlterClientSeletedTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Edit Pencil.png"))); // NOI18N
        jButtonAlterClientSeletedTable.setText("Editar");
        jButtonAlterClientSeletedTable.setContentAreaFilled(false);
        jButtonAlterClientSeletedTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonAlterClientSeletedTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlterClientSeletedTableActionPerformed(evt);
            }
        });

        jButtonDeleteClientSelectedTable.setBackground(new java.awt.Color(255, 51, 51));
        jButtonDeleteClientSelectedTable.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonDeleteClientSelectedTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Trash Can.png"))); // NOI18N
        jButtonDeleteClientSelectedTable.setText("Excluir");
        jButtonDeleteClientSelectedTable.setToolTipText("Apagar Cliente");
        jButtonDeleteClientSelectedTable.setContentAreaFilled(false);
        jButtonDeleteClientSelectedTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonDeleteClientSelectedTable.setSelected(true);
        jButtonDeleteClientSelectedTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteClientSelectedTableActionPerformed(evt);
            }
        });

        jButtonCreateNewClient.setBackground(new java.awt.Color(153, 153, 255));
        jButtonCreateNewClient.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonCreateNewClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Plus.png"))); // NOI18N
        jButtonCreateNewClient.setText("Adicionar");
        jButtonCreateNewClient.setToolTipText("Cadastrar Cliente");
        jButtonCreateNewClient.setContentAreaFilled(false);
        jButtonCreateNewClient.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonCreateNewClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCreateNewClientActionPerformed(evt);
            }
        });

        jTableClients.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NIF", "Nome", "Email", "Telefone", "Endereço", "Zip", "Status", "Padrão"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableClients.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableClientsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableClients);

        jButtonCreateViewClient.setBackground(new java.awt.Color(102, 255, 102));
        jButtonCreateViewClient.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonCreateViewClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Binoculars.png"))); // NOI18N
        jButtonCreateViewClient.setText("Ver");
        jButtonCreateViewClient.setContentAreaFilled(false);
        jButtonCreateViewClient.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonCreateViewClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCreateViewClientActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelClientLayout = new javax.swing.GroupLayout(jPanelClient);
        jPanelClient.setLayout(jPanelClientLayout);
        jPanelClientLayout.setHorizontalGroup(
            jPanelClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelClientLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelClientLayout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(jPanelClientLayout.createSequentialGroup()
                        .addComponent(jTextFieldFilterClientNameTable, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonFilterClientNameTable)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addComponent(jButtonCreateNewClient)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonCreateViewClient)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonAlterClientSeletedTable)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDeleteClientSelectedTable, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8))))
        );
        jPanelClientLayout.setVerticalGroup(
            jPanelClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelClientLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanelClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldFilterClientNameTable, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonFilterClientNameTable, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCreateNewClient, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAlterClientSeletedTable, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDeleteClientSelectedTable, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCreateViewClient, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPaneClient.addTab("Clientes", jPanelClient);

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
        jScrollPane2.setViewportView(jTableSuppliers);

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
                    .addComponent(jScrollPane2)
                    .addGroup(jPanelSearchSupplierLayout.createSequentialGroup()
                        .addComponent(jTextFieldFilterNameTable, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 171, Short.MAX_VALUE)
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
                .addGap(8, 8, 8))
        );

        jTabbedPaneClient.addTab("Fornecedores", jPanelSearchSupplier);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jTabbedPaneClient)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneClient, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
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
            filterSupplierList(txt);
        } else {
            listSuppliers();
        }
    }//GEN-LAST:event_jTextFieldFilterNameTableKeyReleased

    private void jButtonCreateViewClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCreateViewClientActionPerformed
        // TODO add your handling code here:
        int id = 0;
        try {
            id = (int) jTableClients.getValueAt(jTableClients.getSelectedRow(), 0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Selecione um cliente na tabela!!", "Atencao", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (id > 0) {
                JDialogViewClient viewClient = new JDialogViewClient(null, true);
                viewClient.setClient(id);
                viewClient.setVisible(true);
                //                Boolean resp = viewClient.getResponse();
                //                Clients client = clientController.getId(id);
                //                JOptionPane.showMessageDialog(null, "Cliente :" + client.getName() + "\n NIF:" + client.getNif() + "\n Email:" + client.getEmail() + "\n Endereço:" + client.getAddress());
            }
        }
        //        int id = Integer.parseInt(jTableClients.getValueAt(jTableClients.getSelectedRow(), 0).toString());
        //        Clients client = clientController.getId(id);
        //        JOptionPane.showMessageDialog(null, "Cliente :" + client.getName() + "\n NIF:" + client.getNif() + "\n Email:" + client.getEmail() + "\n Endereço:" + client.getAddress());
    }//GEN-LAST:event_jButtonCreateViewClientActionPerformed

    private void jTableClientsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableClientsMouseClicked
        // TODO add your handling code here:
        //        jTabbedPaneClient.setSelectedIndex(1);
        //        jTextFieldIdClient.setText(jTableClients.getValueAt(jTableClients.getSelectedRow(), 0).toString());
        //        jTextFieldNifClient.setText(jTableClients.getValueAt(jTableClients.getSelectedRow(), 1).toString());
        //        jTextFieldNameClient.setText(jTableClients.getValueAt(jTableClients.getSelectedRow(), 2).toString());
        //        jTextFieldEmailClient.setText(jTableClients.getValueAt(jTableClients.getSelectedRow(), 3).toString());
        //        jTextFieldPhoneClient.setText(jTableClients.getValueAt(jTableClients.getSelectedRow(), 4).toString());
        //        jComboBoxCountryClient.setSelectedItem(jTableClients.getValueAt(jTableClients.getSelectedRow(), 5));
        //        jTextFieldCityClient.setText(jTableClients.getValueAt(jTableClients.getSelectedRow(), 6).toString());
        //        jTextFieldStateClient.setText(jTableClients.getValueAt(jTableClients.getSelectedRow(), 7).toString());
        //        jTextFieldAddressClient.setText(jTableClients.getValueAt(jTableClients.getSelectedRow(), 8).toString());
        //        jTextFieldZipCodeClient.setText(jTableClients.getValueAt(jTableClients.getSelectedRow(), 9).toString());
        //        jComboBoxStatusClient.setSelectedIndex((int) jTableClients.getValueAt(jTableClients.getSelectedRow(), 10));
    }//GEN-LAST:event_jTableClientsMouseClicked

    private void jButtonCreateNewClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCreateNewClientActionPerformed
        // TODO add your handling code here:
        //        jTabbedPaneClient.setSelectedIndex(1);

        JDialogFormClient formClient = new JDialogFormClient(null, true);
        formClient.setVisible(true);
        Boolean resp = formClient.getResponse();
        if (resp == true) {
            JOptionPane.showMessageDialog(null, "Usuario salvo com sucesso!!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            listClients();
        }
    }//GEN-LAST:event_jButtonCreateNewClientActionPerformed

    private void jButtonDeleteClientSelectedTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteClientSelectedTableActionPerformed
        // TODO add your handling code here:
        System.out.println("teste");
        int id = 0;
        try {
            id = (int) jTableClients.getValueAt(jTableClients.getSelectedRow(), 0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Selecione um cliente na tabela!!", "Atencao", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (id > 0) {
                Clients client = clientController.getById(id);
                int sair = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja Deletar," + client.getName() + "?", "Atenção", JOptionPane.YES_NO_OPTION);
                if (sair == JOptionPane.YES_OPTION) {
                    if (clientController.deleteById(id)) {
                        JOptionPane.showMessageDialog(null, "Client excluido com Sucesso!!");
                        listClients();
                    }
                }
            }
        }

        //        int id = Integer.parseInt(jTableClients.getValueAt(jTableClients.getSelectedRow(), 0).toString());
        //        Clients client = clientController.getId(id);
        //        //        JOptionPane.showMessageDialog(null, "Cliente :" + client.getName());
        //        int sair = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja Deletar," + client.getName() + "?", "Atenção", JOptionPane.YES_NO_OPTION);
        //        if (sair == JOptionPane.YES_OPTION) {
        //            if (clientController.deleteId(id)) {
        //                JOptionPane.showMessageDialog(null, "Client excluido com Sucesso!!");
        //                listClients();
        //            }
        //        }
    }//GEN-LAST:event_jButtonDeleteClientSelectedTableActionPerformed

    private void jButtonAlterClientSeletedTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlterClientSeletedTableActionPerformed
        // TODO add your handling code here:
        int value = 0;
        try {
            value = (int) jTableClients.getValueAt(jTableClients.getSelectedRow(), 0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Selecione um cliente na tabela!!", "Atencao", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (value > 0) {
                JDialogFormClient formClient = new JDialogFormClient(null, true);
                formClient.setClient(value);
                formClient.setVisible(true);
                Boolean resp = formClient.getResponse();
                if (resp == true) {
                    JOptionPane.showMessageDialog(null, "Cliente salvo com sucesso!!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    listClients();
                }
            }
        }
    }//GEN-LAST:event_jButtonAlterClientSeletedTableActionPerformed

    private void jTextFieldFilterClientNameTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldFilterClientNameTableKeyReleased
        // TODO add your handling code here:
        String txt = jTextFieldFilterClientNameTable.getText();
        if (!txt.isEmpty()) {
            filterListClients(txt);
        } else {
            listClients();
        }
    }//GEN-LAST:event_jTextFieldFilterClientNameTableKeyReleased

    private void jButtonFilterClientNameTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFilterClientNameTableActionPerformed
        // TODO add your handling code here:
        String txt = jTextFieldFilterClientNameTable.getText();
        if (!txt.isEmpty()) {
            filterListClients(txt);
        } else {
            listClients();
        }
    }//GEN-LAST:event_jButtonFilterClientNameTableActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton jButtonAlterClientSeletedTable;
    public javax.swing.JButton jButtonAlterSeleted;
    private javax.swing.JButton jButtonCreateNewClient;
    private javax.swing.JButton jButtonCreateViewClient;
    public javax.swing.JButton jButtonDeleteClientSelectedTable;
    public javax.swing.JButton jButtonDeleteSelected;
    private javax.swing.JButton jButtonFilterClientNameTable;
    private javax.swing.JButton jButtonOpenFormSupplier;
    private javax.swing.JButton jButtonViewSupplierSelected;
    private javax.swing.JPanel jPanelClient;
    private javax.swing.JPanel jPanelSearchSupplier;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPaneClient;
    private javax.swing.JTable jTableClients;
    private javax.swing.JTable jTableSuppliers;
    private javax.swing.JTextField jTextFieldFilterClientNameTable;
    private javax.swing.JTextField jTextFieldFilterNameTable;
    // End of variables declaration//GEN-END:variables
}
