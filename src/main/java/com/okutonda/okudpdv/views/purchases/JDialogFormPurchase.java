/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.okutonda.okudpdv.views.purchases;

import com.okutonda.okudpdv.controllers.ProductController;
import com.okutonda.okudpdv.controllers.PurchaseController;
import com.okutonda.okudpdv.controllers.SupplierController;
import com.okutonda.okudpdv.controllers.WarehouseController;
import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.Purchase;
import com.okutonda.okudpdv.data.entities.PurchaseItem;
import com.okutonda.okudpdv.data.entities.Supplier;
import com.okutonda.okudpdv.ui.TemaCores;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;

/**
 *
 * @author rog
 */
public class JDialogFormPurchase extends javax.swing.JDialog {

    private List<PurchaseItem> itensCompra = new ArrayList<>();

    ProductController productController = new ProductController();
    SupplierController supplierController = new SupplierController();
    WarehouseController warehouseController = new WarehouseController();
    Boolean response = false;

    /**
     * Creates new form JDialogFormPurchase
     */
    public JDialogFormPurchase(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        applyTheme();

        loadComboBoxSuppliers();
        loadComboBoxProdutosCompra();
//        loadComboBoxWarehouse();

        // mesmo formatter/behavior nos dois campos
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        jFormattedTextFieldDataCompra.setFormatterFactory(
                new DefaultFormatterFactory(new DateFormatter(sdf)));
        jFormattedTextFieldDataCompra.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);

        jFormattedTextFieldDataVencimento.setFormatterFactory(
                new DefaultFormatterFactory(new DateFormatter(sdf)));
        jFormattedTextFieldDataVencimento.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);

// 1) data de compra = hoje
        Date hoje = new Date();
        jFormattedTextFieldDataCompra.setValue(hoje);

// 2) vencimento = compra + 1 mês (usando java.time)
        ZoneId zone = ZoneId.systemDefault();
        LocalDate base = hoje.toInstant().atZone(zone).toLocalDate();
        Date venc = Date.from(base.plusMonths(1).atStartOfDay(zone).toInstant());
        jFormattedTextFieldDataVencimento.setValue(venc);

    }

    public Boolean getResponse() {
        return response;
    }

    private void applyTheme() {
//        TemaCleaner.clearBuilderOverrides(getContentPane());
        // Painel de fundo da janela
//        jPanelSidebar.setBackground(TemaCores.BG_LIGHT);

//        TemaUI.aplicarTitulo(jLabelJpanelSelected);
//        jPanelSidebar.setBackground(TemaCores.PRIMARY);
        // Card do login
//        TemaUI.aplicarPainelHeader(jPanelSidebar, TemaCores.PRIMARY);
        // Título
//        TemaUI.aplicarTitulo(jLabelNameCompany);
        jLabelTitle.setForeground(TemaCores.PRIMARY);
        // Labels
//        jLabel1.setForeground(TemaCores.TEXT_DARK);   // "Email:"
//        jLabel2.setForeground(TemaCores.TEXT_DARK);   // "Senha:"
        // Campos
//        TemaUI.aplicarCampoTexto(jTextFieldEmail);
//        TemaUI.aplicarCampoTexto(jPasswordFieldPassword);
        // Botões
//        TemaUI.aplicarBotaoPrimario(jButtonProdAndService);
//        TemaUI.aplicarBotaoPrimario(jButtonInvStoque);
//        TemaUI.aplicarBotaoPrimario(jButtonInventoryPurchases);
//        TemaUI.aplicarBotaoPrimario(jButtonInventoryFisic);
//        TemaUI.aplicarBotaoPrimario(jButtonInventoryReport);
//        TemaUI.aplicarBotaoPrimario(jButtonLogin);
//        jButtonSuport.setForeground(TemaCores.TEXT_GRAY);
//        jButtonAbout.setForeground(TemaCores.TEXT_GRAY);
//        jButtonInstall.setForeground(TemaCores.PRIMARY);
//        jButtonCloseScreen.setForeground(TemaCores.ERROR);
        // Status de BD (cor dinâmica) — chama depois de testar a conexão
//        updateDbStatusLabel(this.conn != null);
        // Borda superior/rodapé (opcional)
        // getRootPane().setBorder(new javax.swing.border.MatteBorder(0, 0, 2, 0, TemaCores.PRIMARY));
        // Se o GUI Builder deixou cores hardcoded em initComponents,
        // isso aqui sobrescreve. Se puder, remova as cores fixas no builder.
    }

    private void loadComboBoxSuppliers() {

        List<Supplier> fornecedores = supplierController.get("");

        for (Supplier s : fornecedores) {
            jComboBoxListFornecedor.addItem(s);
        }
    }

//    private void loadComboBoxWarehouse() {
//        List<Warehouse> warehouse = warehouseController.get("");
//        for (Warehouse s : warehouse) {
//            jComboBoxWarehouse.addItem(s);
//        }
//    }
    private void loadComboBoxProdutosCompra() {
        jComboBoxListProduto.removeAllItems(); // limpa primeiro

        List<Product> produtos = productController.listProducts();
        for (Product p : produtos) {
            jComboBoxListProduto.addItem(p);
        }
    }

    private void loadItemsTable() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Produto", "Qtd", "Preço", "IVA", "Subtotal"}
        );
        for (PurchaseItem it : itensCompra) {
            model.addRow(new Object[]{
                it.getProduct().getDescription(),
                it.getQuantidade(),
                it.getPrecoCusto(),
                it.getIva(),
                it.getSubtotal()
            });
        }
        jTablePurchaseItems.setModel(model);
    }

    private void atualizarTotais() {
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal ivaTotal = BigDecimal.ZERO;

        for (PurchaseItem it : itensCompra) {
            total = total.add(it.getSubtotal());
            ivaTotal = ivaTotal.add(it.getIva() != null ? it.getIva() : BigDecimal.ZERO);
        }

        jTextFieldTotal.setText(total.toString());
        jTextFieldIvaTotal.setText(ivaTotal.toString());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBoxListFornecedor = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePurchaseItems = new javax.swing.JTable();
        jTextFieldTotal = new javax.swing.JTextField();
        jButtonFinishPurchase = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabelTitle = new javax.swing.JLabel();
        jFormattedTextFieldDataCompra = new javax.swing.JFormattedTextField();
        jFormattedTextFieldDataVencimento = new javax.swing.JFormattedTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaNote = new javax.swing.JTextArea();
        jTextFieldIvaTotal = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jFormattedTextFieldDataFabrico = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        jFormattedTextFieldDataExpiracao = new javax.swing.JFormattedTextField();
        jComboBoxListProduto = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldQtdProduct = new javax.swing.JTextField();
        jButtonAddProduct = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Fornecedor");

        jTablePurchaseItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTablePurchaseItems);

        jTextFieldTotal.setText("0.0");

        jButtonFinishPurchase.setText("Finalizar");
        jButtonFinishPurchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFinishPurchaseActionPerformed(evt);
            }
        });

        jLabel3.setText("Total");

        jLabelTitle.setText("Formulario de Compra");

        try {
            jFormattedTextFieldDataCompra.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("20##-##-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextFieldDataCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextFieldDataCompraActionPerformed(evt);
            }
        });

        try {
            jFormattedTextFieldDataVencimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("20##-##-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jTextAreaNote.setColumns(20);
        jTextAreaNote.setRows(5);
        jScrollPane2.setViewportView(jTextAreaNote);

        jTextFieldIvaTotal.setText("0.0");

        jLabel5.setText("Iva Total");

        jLabel6.setText("data vencimento");

        jLabel7.setText("Data compra");

        jLabel9.setText("Obsevacao");

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));

        jLabel4.setText("Data de Fabrico");

        try {
            jFormattedTextFieldDataFabrico.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("20##-##-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel10.setText("Data de Expiracao");

        try {
            jFormattedTextFieldDataExpiracao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("20##-##-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jComboBoxListProduto.setToolTipText("");

        jLabel2.setText("Produto");

        jLabel8.setText("QTD");

        jTextFieldQtdProduct.setText("1");

        jButtonAddProduct.setText("Add");
        jButtonAddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddProductActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jFormattedTextFieldDataFabrico))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jFormattedTextFieldDataExpiracao, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel2))
                            .addComponent(jComboBoxListProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextFieldQtdProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxListProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldQtdProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextFieldDataFabrico, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextFieldDataExpiracao, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel1))
                            .addComponent(jComboBoxListFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldIvaTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonFinishPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6))
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jFormattedTextFieldDataCompra)
                                    .addGap(40, 40, 40)
                                    .addComponent(jFormattedTextFieldDataVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 14, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabelTitle)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxListFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jFormattedTextFieldDataVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextFieldDataCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldIvaTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonFinishPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35))
        );

        setSize(new java.awt.Dimension(783, 568));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jFormattedTextFieldDataCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextFieldDataCompraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextFieldDataCompraActionPerformed

    private void jButtonAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddProductActionPerformed
        // TODO add your handling code here:
        // validar produto e quantidade
        if (jComboBoxListProduto.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto!");
            return;
        }
        int qtd;
        try {
            qtd = Integer.parseInt(jTextFieldQtdProduct.getText());
            if (qtd <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida!");
            return;
        }

        // produto selecionado
        Product prod = (Product) jComboBoxListProduto.getSelectedItem();

        // criar item
        PurchaseItem item = new PurchaseItem();
        item.setProduct(prod);
        item.setQuantidade(qtd);
        item.setPrecoCusto(prod.getPrice());
        item.setSubtotal(prod.getPrice().multiply(BigDecimal.valueOf(qtd)));

// 🔹 Verificar se já existe esse produto na lista
        boolean jaExiste = false;
        for (PurchaseItem it : itensCompra) {
            if (it.getProduct().getId() == prod.getId()) {
                // já existe → soma quantidade e recalcula subtotal
                it.setQuantidade(it.getQuantidade() + qtd);
                it.setSubtotal(it.getPrecoCusto().multiply(BigDecimal.valueOf(it.getQuantidade())));
                jaExiste = true;
                break;
            }
        }

// 🔹 Se não existir, adiciona normalmente
        if (!jaExiste) {
            itensCompra.add(item);
        }

// atualizar tabela
        loadItemsTable();
        atualizarTotais();
    }//GEN-LAST:event_jButtonAddProductActionPerformed

    private void jButtonFinishPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFinishPurchaseActionPerformed
        // TODO add your handling code here:
        // validações
        if (jComboBoxListFornecedor.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor!");
            return;
        }
        if (itensCompra.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione ao menos 1 item!");
            return;
        }

        Supplier fornecedor = (Supplier) jComboBoxListFornecedor.getSelectedItem();

        Purchase purchase = new Purchase();
        purchase.setSupplier(fornecedor);
        purchase.setDescricao(jTextAreaNote.getText());
        purchase.setTotal(new BigDecimal(jTextFieldTotal.getText()));
        purchase.setIvaTotal(new BigDecimal(jTextFieldIvaTotal.getText()));
        purchase.setItems(itensCompra);

        try {
            java.sql.Date dataCompra = java.sql.Date.valueOf(jFormattedTextFieldDataCompra.getText());
            java.sql.Date dataVenc = java.sql.Date.valueOf(jFormattedTextFieldDataVencimento.getText());
            purchase.setDataCompra(dataCompra);
            purchase.setDataVencimento(dataVenc);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Datas inválidas!");
            return;
        }

        // status inicial
        purchase.setStatus("aberto");

        // salvar na BD via controller
        response = new PurchaseController().add(purchase);
        if (response) {
//            JOptionPane.showMessageDialog(this, "Compra salva com sucesso!");
            dispose(); // fecha o dialog
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao salvar compra!");
        }
    }//GEN-LAST:event_jButtonFinishPurchaseActionPerformed

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
            java.util.logging.Logger.getLogger(JDialogFormPurchase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogFormPurchase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogFormPurchase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogFormPurchase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogFormPurchase dialog = new JDialogFormPurchase(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonAddProduct;
    private javax.swing.JButton jButtonFinishPurchase;
    private javax.swing.JComboBox jComboBoxListFornecedor;
    private javax.swing.JComboBox jComboBoxListProduto;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataCompra;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataExpiracao;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataFabrico;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataVencimento;
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
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTablePurchaseItems;
    private javax.swing.JTextArea jTextAreaNote;
    private javax.swing.JTextField jTextFieldIvaTotal;
    private javax.swing.JTextField jTextFieldQtdProduct;
    private javax.swing.JTextField jTextFieldTotal;
    // End of variables declaration//GEN-END:variables
}
