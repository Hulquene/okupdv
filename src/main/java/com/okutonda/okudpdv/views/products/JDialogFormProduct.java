/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.okutonda.okudpdv.views.products;

import com.okutonda.okudpdv.controllers.GroupsProductController;
import com.okutonda.okudpdv.controllers.ProductController;
import com.okutonda.okudpdv.controllers.ReasonTaxeController;
import com.okutonda.okudpdv.controllers.TaxeController;
import com.okutonda.okudpdv.data.entities.GroupsProduct;
import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.ReasonTaxes;
import com.okutonda.okudpdv.data.entities.Taxes;
import com.okutonda.okudpdv.utilities.Util;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author kenny
 */
public final class JDialogFormProduct extends javax.swing.JDialog {

    ProductController productController = new ProductController();
//    SupplierController supplierController = new SupplierController();
    TaxeController taxeController = new TaxeController();
    ReasonTaxeController reasonTaxeController = new ReasonTaxeController();
    GroupsProductController groupsProductController = new GroupsProductController();
//    WarehouseController warehouseController = new WarehouseController();
    Boolean status = false;

    /**
     * Creates new form JDialogFormProduct
     */
    public JDialogFormProduct(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        loadCombobox();
//        listSupplierCombo();
    }

    public void setFormProduct(Product prod) {
        if (prod != null) {
            jTextFieldId.setText(String.valueOf(prod.getId()));
            jComboBoxType.setSelectedItem(prod.getType());
            jTextFieldCode.setText(prod.getCode());
            jTextFieldBarCode.setText(prod.getBarcode());
            jTextFieldDescription.setText(prod.getDescription());
            jTextFieldPrice.setText(String.valueOf(prod.getPrice()));
            jTextFieldPurchasePrice.setText(String.valueOf(prod.getPurchasePrice()));
//            jTextFieldStockTotal.setText(String.valueOf(prod.getStockTotal()));
//            jComboBoxTaxeId.setSelectedItem(prod.getTaxe().toString());
//            jComboBoxReasonTaxeId.setSelectedItem(prod.getReasonTaxe().toString());
//            jComboBoxReasonTaxeId.setSelectedItem(prod.getReasonTaxe().toString());
//            jComboBoxSupplier.setSelectedItem(prod.getSupplier().getName());
            jComboBoxStatus.setSelectedItem(prod.getStatus());
            if (prod.getTaxe() != null) {
                jLabelTaxeSelected.setText(prod.getTaxe().toString());
            }
            if (prod.getReasonTaxe() != null) {
                jLabelReasonSeleted.setText(prod.getReasonTaxe().toString());
            }
        }
    }

    public Boolean getResponse() {
        return status;
    }

    public void setProduct(int id) {
        loadCombobox();
        Product prod = productController.getById(id);
        setFormProduct(prod);
    }

    public void changeValueTextFildForm(Product cModel) {
        jTextFieldId.setText(Integer.toString(cModel.getId()));
        jTextFieldCode.setText(cModel.getCode());
        jTextFieldBarCode.setText(cModel.getBarcode());
        jTextFieldDescription.setText(cModel.getDescription());
        jComboBoxTaxeId.setSelectedItem(cModel.getTaxe().toString());
        jComboBoxReasonTaxeId.setSelectedItem(cModel.getReasonTaxe().toString());
//        jComboBoxSupplier.setSelectedItem(cModel.getSupplier().getName());
        jComboBoxType.setSelectedItem(cModel.getType());
        jTextFieldBarCode.setText(cModel.getBarcode());
        jTextFieldPrice.setText(cModel.getPrice().toString());
        jTextFieldPurchasePrice.setText(cModel.getPurchasePrice().toString());
//        jComboBoxGroupId.setSelectedIndex(cModel.getGroupId());
//        jComboBoxSubGroupId.setSelectedIndex(cModel.getSubGroupId());
//        jComboBoxSupplier.setSelectedItem(cModel.getSupplier());
        jComboBoxStatus.setSelectedItem(cModel.getStatus());
//        jTextFieldStockTotal.setText(Double.toString(cModel.getStockTotal()));
    }

    public Product validateProduct() {
        Product cModel = new Product();

        if (jTextFieldCode.getText().isEmpty() || jTextFieldCode.getText().length() < 3) {
            JOptionPane.showMessageDialog(null, "Campo Codigo invalido!! no minimo 3 caracteres");
        } else if (jTextFieldDescription.getText().isEmpty() || jTextFieldDescription.getText().length() < 3) {
            JOptionPane.showMessageDialog(null, "Campo Descrição invalido!! no minimo 3 caracteres");
        } else if (jTextFieldBarCode.getText().isEmpty() || jTextFieldBarCode.getText().length() < 9) {
            JOptionPane.showMessageDialog(null, "Campo Codigo de Barra invalido!! no minimo 9 caracteres");
        } else if (Util.isValidDouble(jTextFieldPrice.getText()) == false) {
            JOptionPane.showMessageDialog(null, "Campo Preço invalido!! Insira um numero preço");
        } else if (Util.isValidDouble(jTextFieldPurchasePrice.getText()) == false) {
            JOptionPane.showMessageDialog(null, "Campo Preço de Compra invalido!! Insira um numero compra");
        } else if (jTextFieldStockTotal.getText().isEmpty() || Util.isInteger(jTextFieldStockTotal.getText()) == false) {
            JOptionPane.showMessageDialog(null, "Campo Total de Estoque invalido!! Insira um numero");
        } else if (jComboBoxReasonTaxeId.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Campo Reason Taxe invalido!! Selecione");
        } else if (jComboBoxTaxeId.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Campo Taxe invalido!! Selecione");
        } else {
            cModel.setCode(jTextFieldCode.getText());
            cModel.setType(jComboBoxType.getSelectedItem().toString());
            cModel.setDescription(jTextFieldDescription.getText());
            cModel.setBarcode(jTextFieldBarCode.getText());
            cModel.setTaxe((Taxes) jComboBoxTaxeId.getSelectedItem());
            cModel.setReasonTaxe((ReasonTaxes) jComboBoxReasonTaxeId.getSelectedItem());
            cModel.setPrice(new BigDecimal(jTextFieldPrice.getText().replace(",", ".")));
            cModel.setPurchasePrice(new BigDecimal(jTextFieldPurchasePrice.getText().replace(",", ".")));
            cModel.setGroup((GroupsProduct) jComboBoxGroup.getSelectedItem());
            String statusProd = (String) jComboBoxStatus.getSelectedItem();
            if ("ativo".equals(statusProd)) {
                cModel.setStatus(1);
            } else {
                cModel.setStatus(0);
            }
            return cModel;
        }
        return null;
    }

    public void loadCombobox() {
//        List<Supplier> listS = supplierController.get("");
//        jComboBoxSupplier.removeAllItems();
//        for (Supplier item : listS) {
//            jComboBoxSupplier.addItem(item);
//        }
//
//        List<Warehouse> listW = warehouseController.get("");
//        jComboBoxWarehause.removeAllItems();
//        for (Warehouse item : listW) {
//            jComboBoxWarehause.addItem(item);
//        }

        List<ReasonTaxes> listR = reasonTaxeController.get("");
        jComboBoxReasonTaxeId.removeAllItems();
        for (ReasonTaxes item : listR) {
            jComboBoxReasonTaxeId.addItem(item);
        }

        List<Taxes> list = taxeController.get("");
        jComboBoxTaxeId.removeAllItems();
        for (Taxes item : list) {
            jComboBoxTaxeId.addItem(item);
        }
        jComboBoxTaxeId.setSelectedItem("IVA 14: 14.0");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelFormProduct = new javax.swing.JPanel();
        jTextFieldCode = new javax.swing.JTextField();
        jTextFieldDescription = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jTextFieldBarCode = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jComboBoxType = new javax.swing.JComboBox<>();
        jComboBoxGroup = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        jComboBoxTaxeId = new javax.swing.JComboBox();
        jButtonSearchForm = new javax.swing.JButton();
        jComboBoxStatus = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        jComboBoxReasonTaxeId = new javax.swing.JComboBox();
        jTextFieldPrice = new javax.swing.JTextField();
        jTextFieldStockTotal = new javax.swing.JTextField();
        jTextFieldPurchasePrice = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabelTaxeSelected = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabelReasonSeleted = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButtonClearForm = new javax.swing.JButton();
        jButtonAdd = new javax.swing.JButton();
        jTextFieldId = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButtonCancelFormAdd = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jTextFieldCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCodeActionPerformed(evt);
            }
        });

        jTextFieldDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldDescriptionKeyPressed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Tipo");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Codigo:");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Descricao");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Preço");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setText("Status");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Taxas");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Codigo de Barra");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setText("Razão:");

        jComboBoxType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "product", "service" }));
        jComboBoxType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxTypeItemStateChanged(evt);
            }
        });
        jComboBoxType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTypeActionPerformed(evt);
            }
        });

        jComboBoxGroup.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jComboBoxGroupAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setText("Grupo");

        jComboBoxTaxeId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "selecione" }));
        jComboBoxTaxeId.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jComboBoxTaxeIdAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jButtonSearchForm.setText("Localizar");
        jButtonSearchForm.setEnabled(false);
        jButtonSearchForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchFormActionPerformed(evt);
            }
        });

        jComboBoxStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "inativo", "ativo" }));
        jComboBoxStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxStatusActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setText("Estoque minimo");

        jComboBoxReasonTaxeId.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jComboBoxReasonTaxeIdAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jTextFieldStockTotal.setText("0");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Preço de Compra");

        jLabelTaxeSelected.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelTaxeSelected.setText("Vazio");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setText("Taxas selecionada");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setText("Razão selecionada");

        jLabelReasonSeleted.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelReasonSeleted.setText("Vazio");

        jLabel5.setForeground(new java.awt.Color(204, 0, 0));
        jLabel5.setText("Imposto deve estar incluso no preco unitario");

        jButtonClearForm.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonClearForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Restart.png"))); // NOI18N
        jButtonClearForm.setText("Limpar");
        jButtonClearForm.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jButtonAdd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Plus.png"))); // NOI18N
        jButtonAdd.setText("Salvar");
        jButtonAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jTextFieldId.setEditable(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("ID:");

        jButtonCancelFormAdd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonCancelFormAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Cancel.png"))); // NOI18N
        jButtonCancelFormAdd.setText("Cancelar");
        jButtonCancelFormAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel3.setText("Formulario Produtos e serviço");

        javax.swing.GroupLayout jPanelFormProductLayout = new javax.swing.GroupLayout(jPanelFormProduct);
        jPanelFormProduct.setLayout(jPanelFormProductLayout);
        jPanelFormProductLayout.setHorizontalGroup(
            jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormProductLayout.createSequentialGroup()
                .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelFormProductLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonClearForm)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonCancelFormAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12))
                    .addGroup(jPanelFormProductLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelFormProductLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanelFormProductLayout.createSequentialGroup()
                                        .addComponent(jTextFieldDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButtonSearchForm, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanelFormProductLayout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(142, 142, 142)
                                        .addComponent(jLabel10)
                                        .addGap(208, 208, 208)))
                                .addGroup(jPanelFormProductLayout.createSequentialGroup()
                                    .addComponent(jComboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jTextFieldCode))
                                .addComponent(jComboBoxTaxeId, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelFormProductLayout.createSequentialGroup()
                                    .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanelFormProductLayout.createSequentialGroup()
                                            .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel13)
                                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(174, 174, 174))
                                        .addGroup(jPanelFormProductLayout.createSequentialGroup()
                                            .addComponent(jTextFieldPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                    .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2)
                                        .addComponent(jTextFieldPurchasePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanelFormProductLayout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addComponent(jLabel11))
                                .addGroup(jPanelFormProductLayout.createSequentialGroup()
                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabelTaxeSelected, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel20)
                            .addComponent(jLabel16)
                            .addComponent(jLabel18)
                            .addComponent(jLabel23)
                            .addComponent(jLabelReasonSeleted, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxGroup, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldBarCode)
                            .addComponent(jComboBoxReasonTaxeId, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanelFormProductLayout.createSequentialGroup()
                                .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21)
                                    .addComponent(jTextFieldStockTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)))))
                    .addGroup(jPanelFormProductLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldId, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(108, Short.MAX_VALUE))
        );
        jPanelFormProductLayout.setVerticalGroup(
            jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelFormProductLayout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldId, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelFormProductLayout.createSequentialGroup()
                        .addComponent(jComboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonSearchForm, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel2)
                            .addComponent(jLabel19)
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldPurchasePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldStockTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxTaxeId, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxReasonTaxeId, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelFormProductLayout.createSequentialGroup()
                        .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldBarCode, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCode, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel20)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23)
                    .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelTaxeSelected)))
                .addGap(0, 0, 0)
                .addComponent(jLabelReasonSeleted)
                .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelFormProductLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel12))
                    .addGroup(jPanelFormProductLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonAdd)
                            .addComponent(jButtonClearForm)
                            .addComponent(jButtonCancelFormAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jPanelFormProductLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonAdd, jButtonCancelFormAdd, jButtonClearForm});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelFormProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelFormProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(878, 524));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldCodeActionPerformed

    private void jTextFieldDescriptionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDescriptionKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String description = jTextFieldDescription.getText();
            Product cModel;
            cModel = productController.getByDescription(description);
            if (cModel.getDescription() != null) {
                changeValueTextFildForm(cModel);
            } else {
                JOptionPane.showMessageDialog(null, "Product nao encontrado!");
            }
        }
    }//GEN-LAST:event_jTextFieldDescriptionKeyPressed

    private void jComboBoxTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxTypeActionPerformed

    private void jComboBoxGroupAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jComboBoxGroupAncestorAdded
        // TODO add your handling code here:
        List<GroupsProduct> list = groupsProductController.getAll();
        jComboBoxGroup.removeAllItems();
        for (GroupsProduct item : list) {
            jComboBoxGroup.addItem(item);
        }
    }//GEN-LAST:event_jComboBoxGroupAncestorAdded

    private void jComboBoxTaxeIdAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jComboBoxTaxeIdAncestorAdded
        // TODO add your handling code here:

    }//GEN-LAST:event_jComboBoxTaxeIdAncestorAdded

    private void jButtonSearchFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchFormActionPerformed
        // TODO add your handling code here:
//        String name = jTextFieldDescription.getText();
//        Product cModel;
//        cModel = productController.getName(name);
//        if (cModel.getDescription() != null) {
//            changeValueTextFildForm(cModel);
//        } else {
//            JOptionPane.showMessageDialog(null, "Supplier nao encontrado!");
//        }
    }//GEN-LAST:event_jButtonSearchFormActionPerformed

    private void jComboBoxStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxStatusActionPerformed

    private void jComboBoxReasonTaxeIdAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jComboBoxReasonTaxeIdAncestorAdded
        // TODO add your handling code here:
//        List<ReasonTaxes> list = reasonTaxeController.get("");
//        jComboBoxReasonTaxeId.removeAllItems();
//        for (ReasonTaxes item : list) {
//            jComboBoxReasonTaxeId.addItem(item);
//        }
    }//GEN-LAST:event_jComboBoxReasonTaxeIdAncestorAdded

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        // TODO add your handling code here:
        Product cModel = validateProduct();
        if (cModel != null) {
            int id = jTextFieldId.getText().isEmpty() == true ? 0 : Integer.parseInt(jTextFieldId.getText());
//            boolean response;
            if (id == 0) {
                status = productController.save(cModel);
//                if (response) {
//                    JOptionPane.showMessageDialog(null, "products salvo com Sucesso!!");
//                    screanListProducts();
//                }
            } else {
                status = productController.save(cModel);
//                if (response) {
//                    JOptionPane.showMessageDialog(null, "products Atualizado com Sucesso!!");
//                    screanListProducts();
//                }
            }
//            if (status == true) {
//                JOptionPane.showMessageDialog(null, "products Atualizado com Sucesso!!");
            this.dispose();
//                screanListProducts();
//            }
        }
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
//        loadCombobox();
    }//GEN-LAST:event_formWindowActivated

    private void jComboBoxTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxTypeItemStateChanged
        // TODO add your handling code here:
        String type = jComboBoxType.getSelectedItem().toString();
        if (type.equals("product")) {
            jTextFieldStockTotal.setEnabled(true);
        } else {
            jTextFieldStockTotal.setEnabled(false);
        }
//        JOptionPane.showMessageDialog(null, "O tipo selecionado: " + type);
    }//GEN-LAST:event_jComboBoxTypeItemStateChanged

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
            java.util.logging.Logger.getLogger(JDialogFormProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogFormProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogFormProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogFormProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogFormProduct dialog = new JDialogFormProduct(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonCancelFormAdd;
    private javax.swing.JButton jButtonClearForm;
    private javax.swing.JButton jButtonSearchForm;
    private javax.swing.JComboBox jComboBoxGroup;
    private javax.swing.JComboBox jComboBoxReasonTaxeId;
    private javax.swing.JComboBox<String> jComboBoxStatus;
    private javax.swing.JComboBox jComboBoxTaxeId;
    private javax.swing.JComboBox<String> jComboBoxType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelReasonSeleted;
    private javax.swing.JLabel jLabelTaxeSelected;
    private javax.swing.JPanel jPanelFormProduct;
    private javax.swing.JTextField jTextFieldBarCode;
    private javax.swing.JTextField jTextFieldCode;
    private javax.swing.JTextField jTextFieldDescription;
    private javax.swing.JTextField jTextFieldId;
    private javax.swing.JTextField jTextFieldPrice;
    private javax.swing.JTextField jTextFieldPurchasePrice;
    private javax.swing.JTextField jTextFieldStockTotal;
    // End of variables declaration//GEN-END:variables
}
