/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.okutonda.okudpdv.views.products;

import com.okutonda.okudpdv.controllers.GroupsProductController;
import com.okutonda.okudpdv.controllers.ProductController;
import com.okutonda.okudpdv.controllers.ReasonTaxeController;
import com.okutonda.okudpdv.controllers.SupplierController;
import com.okutonda.okudpdv.controllers.TaxeController;
import com.okutonda.okudpdv.controllers.WarehouseController;
import com.okutonda.okudpdv.models.GroupsProduct;
import com.okutonda.okudpdv.models.Product;
import com.okutonda.okudpdv.models.ReasonTaxes;
import com.okutonda.okudpdv.models.Supplier;
import com.okutonda.okudpdv.models.Taxes;
import com.okutonda.okudpdv.models.Warehouse;
import com.okutonda.okudpdv.utilities.Util;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author kenny
 */
public final class JDialogFormProduct extends javax.swing.JDialog {

    ProductController productController = new ProductController();
    SupplierController supplierController = new SupplierController();
    TaxeController taxeController = new TaxeController();
    ReasonTaxeController reasonTaxeController = new ReasonTaxeController();
    GroupsProductController groupsProductController = new GroupsProductController();
    WarehouseController warehouseController = new WarehouseController();
    Boolean status = false;
    /**
     * Creates new form JDialogFormProduct
     */
    public JDialogFormProduct(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
//        loadCombobox();
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
            jTextFieldStockTotal.setText(String.valueOf(prod.getStockTotal()));
            jComboBoxTaxeId.setSelectedItem(prod.getTaxe().toString());
            jComboBoxReasonTaxeId.setSelectedItem(prod.getReasonTaxe().toString());
//            jComboBoxReasonTaxeId.setSelectedItem(prod.getReasonTaxe().toString());
            jComboBoxSupplier.setSelectedItem(prod.getSupplier().getName());
            jComboBoxStatus.setSelectedItem(prod.getStatus());
            System.out.println("taxe: " + prod.getTaxe().toString());
        }
    }

    public Boolean getResponse() {
        return status;
    }

    public void setProduct(int id) {
        loadCombobox();
        Product prod = productController.getId(id);
        setFormProduct(prod);
    }

    public void changeValueTextFildForm(Product cModel) {
        jTextFieldId.setText(Integer.toString(cModel.getId()));
        jTextFieldCode.setText(cModel.getCode());
        jTextFieldBarCode.setText(cModel.getBarcode());
        jTextFieldDescription.setText(cModel.getDescription());
        jComboBoxTaxeId.setSelectedItem(cModel.getTaxe().toString());
        jComboBoxReasonTaxeId.setSelectedItem(cModel.getReasonTaxe().toString());
        jComboBoxSupplier.setSelectedItem(cModel.getSupplier().getName());
        jComboBoxType.setSelectedItem(cModel.getType());
        jTextFieldBarCode.setText(cModel.getBarcode());
        jTextFieldPrice.setText(cModel.getPrice().toString());
        jTextFieldPurchasePrice.setText(cModel.getPurchasePrice().toString());
//        jComboBoxGroupId.setSelectedIndex(cModel.getGroupId());
//        jComboBoxSubGroupId.setSelectedIndex(cModel.getSubGroupId());
        jComboBoxSupplier.setSelectedItem(cModel.getSupplier());
        jComboBoxStatus.setSelectedItem(cModel.getStatus());
        jTextFieldStockTotal.setText(Double.toString(cModel.getStockTotal()));
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
            cModel.setPrice(Util.convertToDouble(jTextFieldPrice.getText()));
            cModel.setPurchasePrice(Util.convertToDouble(jTextFieldPurchasePrice.getText()));
            cModel.setGroup((GroupsProduct) jComboBoxGroup.getSelectedItem());
            cModel.setSupplier((Supplier) jComboBoxSupplier.getSelectedItem());
            cModel.setStockTotal(Util.convertToInteger(jTextFieldStockTotal.getText()));
            cModel.setStatus(jComboBoxStatus.getSelectedItem().toString());
            return cModel;
        }

        return null;
    }

    public void loadCombobox() {
        List<Supplier> listS = supplierController.get("");
        jComboBoxSupplier.removeAllItems();
        for (Supplier item : listS) {
            jComboBoxSupplier.addItem(item);
        }

        List<Warehouse> listW = warehouseController.get("");
        jComboBoxWarehause.removeAllItems();
        for (Warehouse item : listW) {
            jComboBoxWarehause.addItem(item);
        }

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

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jButtonCancelFormAdd = new javax.swing.JButton();
        jButtonClearForm = new javax.swing.JButton();
        jButtonAdd = new javax.swing.JButton();
        jTextFieldId = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanelFormProduct = new javax.swing.JPanel();
        jTextFieldCode = new javax.swing.JTextField();
        jTextFieldDescription = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
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
        jComboBoxSupplier = new javax.swing.JComboBox();
        jTextFieldPrice = new javax.swing.JTextField();
        jTextFieldStockTotal = new javax.swing.JTextField();
        jTextFieldPurchasePrice = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxWarehause = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 0, 51));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Formulario Produtos e serviço");

        jButtonCancelFormAdd.setText("Cancelar");

        jButtonClearForm.setText("Limpar");

        jButtonAdd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonAdd.setText("Salvar");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jTextFieldId.setEditable(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ID:");

        jPanelFormProduct.setBackground(new java.awt.Color(204, 204, 255));

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

        jLabel9.setText("Tipo");

        jLabel10.setText("Codigo:");

        jLabel11.setText("Descricao");

        jLabel13.setText("Preço");

        jLabel14.setText("Fornecedor");

        jLabel19.setText("Status");

        jLabel15.setText("Taxas");

        jLabel16.setText("Codigo de Barra");

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

        jLabel21.setText("Estoque");

        jComboBoxReasonTaxeId.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jComboBoxReasonTaxeIdAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
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

        jTextFieldStockTotal.setText("0");

        jLabel2.setText("Preço de Compra");

        jComboBoxWarehause.setEnabled(false);

        jLabel4.setText("Armazem");

        javax.swing.GroupLayout jPanelFormProductLayout = new javax.swing.GroupLayout(jPanelFormProduct);
        jPanelFormProduct.setLayout(jPanelFormProductLayout);
        jPanelFormProductLayout.setHorizontalGroup(
            jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormProductLayout.createSequentialGroup()
                .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelFormProductLayout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(jLabel12))
                    .addGroup(jPanelFormProductLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelFormProductLayout.createSequentialGroup()
                                        .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jTextFieldPurchasePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jComboBoxReasonTaxeId, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(7, 7, 7))
                                    .addGroup(jPanelFormProductLayout.createSequentialGroup()
                                        .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelFormProductLayout.createSequentialGroup()
                                                    .addGap(2, 2, 2)
                                                    .addComponent(jTextFieldPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel20)
                                                    .addComponent(jComboBoxTaxeId, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(jPanelFormProductLayout.createSequentialGroup()
                                                .addGap(5, 5, 5)
                                                .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanelFormProductLayout.createSequentialGroup()
                                                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(153, 153, 153)
                                                        .addComponent(jLabel18))
                                                    .addComponent(jComboBoxGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(162, 162, 162)))
                                .addGroup(jPanelFormProductLayout.createSequentialGroup()
                                    .addGap(7, 7, 7)
                                    .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanelFormProductLayout.createSequentialGroup()
                                            .addComponent(jTextFieldDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jButtonSearchForm, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanelFormProductLayout.createSequentialGroup()
                                            .addGap(6, 6, 6)
                                            .addComponent(jLabel13)))))
                            .addComponent(jComboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelFormProductLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel11)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldCode, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanelFormProductLayout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addComponent(jLabel14))
                                .addComponent(jTextFieldBarCode)
                                .addComponent(jComboBoxSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanelFormProductLayout.createSequentialGroup()
                                    .addComponent(jLabel19)
                                    .addGap(99, 99, 99)
                                    .addComponent(jLabel21)))
                            .addGroup(jPanelFormProductLayout.createSequentialGroup()
                                .addComponent(jComboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldStockTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel4)
                            .addComponent(jComboBoxWarehause, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel16))))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanelFormProductLayout.setVerticalGroup(
            jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelFormProductLayout.createSequentialGroup()
                .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelFormProductLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonSearchForm, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelFormProductLayout.createSequentialGroup()
                                .addGap(162, 162, 162)
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBoxGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelFormProductLayout.createSequentialGroup()
                                .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel13))
                                .addGap(17, 17, 17)
                                .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextFieldPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldPurchasePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jComboBoxTaxeId, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxReasonTaxeId, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(74, 74, 74))))
                    .addGroup(jPanelFormProductLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldCode, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldBarCode, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jLabel21))
                        .addGap(15, 15, 15)
                        .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldStockTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBoxWarehause, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addGap(29, 29, 29))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanelFormProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldId, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonClearForm)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonCancelFormAdd)))
                .addGap(16, 16, 16))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancelFormAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonClearForm)
                    .addComponent(jButtonAdd)
                    .addComponent(jTextFieldId, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanelFormProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(107, 107, 107))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonAdd, jButtonCancelFormAdd, jButtonClearForm});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setSize(new java.awt.Dimension(853, 548));
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
            cModel = productController.getName(description);
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
        List<GroupsProduct> list = groupsProductController.get("");
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

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        // TODO add your handling code here:
        Product cModel = validateProduct();
        if (cModel != null) {
            int id = jTextFieldId.getText().isEmpty() == true ? 0 : Integer.parseInt(jTextFieldId.getText());
//            boolean response;
            if (id == 0) {
                status = productController.add(cModel, 0);
//                if (response) {
//                    JOptionPane.showMessageDialog(null, "products salvo com Sucesso!!");
//                    screanListProducts();
//                }
            } else {
                status = productController.add(cModel, id);
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
    private javax.swing.JComboBox jComboBoxSupplier;
    private javax.swing.JComboBox jComboBoxTaxeId;
    private javax.swing.JComboBox<String> jComboBoxType;
    private javax.swing.JComboBox jComboBoxWarehause;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
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
