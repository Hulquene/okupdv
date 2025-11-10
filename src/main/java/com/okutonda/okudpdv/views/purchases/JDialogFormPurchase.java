/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.okutonda.okudpdv.views.purchases;

import com.okutonda.okudpdv.controllers.ProductController;
import com.okutonda.okudpdv.controllers.PurchaseController;
import com.okutonda.okudpdv.controllers.SupplierController;
import com.okutonda.okudpdv.controllers.WarehouseController;
import com.okutonda.okudpdv.data.entities.PaymentStatus;
import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.Purchase;
import com.okutonda.okudpdv.data.entities.PurchaseItem;
import com.okutonda.okudpdv.data.entities.StockStatus;
import com.okutonda.okudpdv.data.entities.Supplier;
//import com.okutonda.okudpdv.ui.TemaCores;
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
//    WarehouseController warehouseController = new WarehouseController();
    Boolean response = false;

    /**
     * Creates new form JDialogFormPurchase
     */
    public JDialogFormPurchase(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
//        applyTheme();

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

//    private void applyTheme() {
////        TemaCleaner.clearBuilderOverrides(getContentPane());
//        // Painel de fundo da janela
////        jPanelSidebar.setBackground(TemaCores.BG_LIGHT);
//
////        TemaUI.aplicarTitulo(jLabelJpanelSelected);
////        jPanelSidebar.setBackground(TemaCores.PRIMARY);
//        // Card do login
////        TemaUI.aplicarPainelHeader(jPanelSidebar, TemaCores.PRIMARY);
//        // Título
////        TemaUI.aplicarTitulo(jLabelNameCompany);
//        jLabelTitle.setForeground(TemaCores.PRIMARY);
//        // Labels
////        jLabel1.setForeground(TemaCores.TEXT_DARK);   // "Email:"
////        jLabel2.setForeground(TemaCores.TEXT_DARK);   // "Senha:"
//        // Campos
////        TemaUI.aplicarCampoTexto(jTextFieldEmail);
////        TemaUI.aplicarCampoTexto(jPasswordFieldPassword);
//        // Botões
////        TemaUI.aplicarBotaoPrimario(jButtonProdAndService);
////        TemaUI.aplicarBotaoPrimario(jButtonInvStoque);
////        TemaUI.aplicarBotaoPrimario(jButtonInventoryPurchases);
////        TemaUI.aplicarBotaoPrimario(jButtonInventoryFisic);
////        TemaUI.aplicarBotaoPrimario(jButtonInventoryReport);
////        TemaUI.aplicarBotaoPrimario(jButtonLogin);
////        jButtonSuport.setForeground(TemaCores.TEXT_GRAY);
////        jButtonAbout.setForeground(TemaCores.TEXT_GRAY);
////        jButtonInstall.setForeground(TemaCores.PRIMARY);
////        jButtonCloseScreen.setForeground(TemaCores.ERROR);
//        // Status de BD (cor dinâmica) — chama depois de testar a conexão
////        updateDbStatusLabel(this.conn != null);
//        // Borda superior/rodapé (opcional)
//        // getRootPane().setBorder(new javax.swing.border.MatteBorder(0, 0, 2, 0, TemaCores.PRIMARY));
//        // Se o GUI Builder deixou cores hardcoded em initComponents,
//        // isso aqui sobrescreve. Se puder, remova as cores fixas no builder.
//    }

    private void loadComboBoxSuppliers() {

        List<Supplier> fornecedores = supplierController.listarTodos();

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

    private void criacaoItem() {
        // Validar produto e quantidade
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

        // Produto selecionado
        Product prod = (Product) jComboBoxListProduto.getSelectedItem();

        // Criar item
        PurchaseItem item = new PurchaseItem();
        item.setProduct(prod);
        item.setQuantidade(qtd);
        item.setPrecoCusto(prod.getPrice());

        // 🔹 CALCULAR SUBTOTAL CORRETAMENTE
        BigDecimal subtotal = prod.getPrice().multiply(BigDecimal.valueOf(qtd));
        item.setSubtotal(subtotal);

        // 🔹 VERIFICAR SE JÁ EXISTE
        boolean jaExiste = false;
        for (PurchaseItem it : itensCompra) {
            if (it.getProduct().getId().equals(prod.getId())) {
                // Já existe → soma quantidade e recalcula subtotal
                int novaQuantidade = it.getQuantidade() + qtd;
                it.setQuantidade(novaQuantidade);
                it.setSubtotal(it.getPrecoCusto().multiply(BigDecimal.valueOf(novaQuantidade)));
                jaExiste = true;
                break;
            }
        }

        // 🔹 SE NÃO EXISTIR, ADICIONA
        if (!jaExiste) {
            itensCompra.add(item);
        }

        // Atualizar tabela e totais
        loadItemsTable();
        atualizarTotais();

        // 🔹 LIMPAR CAMPOS APÓS ADICIONAR
        jTextFieldQtdProduct.setText("1");

    }

    private void finalizarCompra() {
        // Validações
        if (jComboBoxListFornecedor.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor!");
            return;
        }
        if (itensCompra.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione ao menos 1 item!");
            return;
        }

        try {
            Supplier fornecedor = (Supplier) jComboBoxListFornecedor.getSelectedItem();

            Purchase purchase = new Purchase();
            purchase.setSupplier(fornecedor);
            purchase.setDescricao(jTextAreaNote.getText());

            // 🔹 CORREÇÃO CRÍTICA: Associar a Purchase aos itens ANTES de definir a lista
            for (PurchaseItem item : itensCompra) {
                item.setPurchase(purchase); // 🔹 IMPORTANTE: definir a relação bidirecional
            }

            purchase.setItems(new ArrayList<>(itensCompra));

            // 🔹 CALCULAR TOTAIS
            BigDecimal total = new BigDecimal(jTextFieldTotal.getText());
            BigDecimal ivaTotal = new BigDecimal(jTextFieldIvaTotal.getText());
            purchase.setTotal(total);
            purchase.setIvaTotal(ivaTotal);

            // Datas
            java.sql.Date dataCompra = java.sql.Date.valueOf(jFormattedTextFieldDataCompra.getText());
            java.sql.Date dataVenc = java.sql.Date.valueOf(jFormattedTextFieldDataVencimento.getText());
            purchase.setDataCompra(dataCompra);
            purchase.setDataVencimento(dataVenc);

            // Status inicial
            purchase.setStockStatus(StockStatus.PENDENTE);
            purchase.setPaymentStatus(PaymentStatus.PENDENTE);

            // 🔹 DEBUG: Verificar se os itens estão associados
            System.out.println("=== DEBUG FINALIZAR COMPRA ===");
            System.out.println("Fornecedor: " + purchase.getSupplier().getName());
            System.out.println("Total Itens: " + purchase.getItems().size());
            for (PurchaseItem item : purchase.getItems()) {
                System.out.println("Item: " + item.getProduct().getDescription()
                        + " - Purchase: " + (item.getPurchase() != null ? "ASSOCIADO" : "NULO"));
            }

            // 🔹 SALVAR USANDO O NOVO SERVICE
//            System.out.println(purchase);
            response = new PurchaseController().add(purchase);

            if (response) {
                JOptionPane.showMessageDialog(this, "✅ Compra registrada com sucesso!");
                dispose(); // Fecha o dialog
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar compra!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
            e.printStackTrace();
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

        jPanel2 = new javax.swing.JPanel();
        jLabelTitle = new javax.swing.JLabel();
        jComboBoxListFornecedor = new javax.swing.JComboBox();
        jFormattedTextFieldDataVencimento = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jFormattedTextFieldDataCompra = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaNote = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePurchaseItems = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldIvaTotal = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldTotal = new javax.swing.JTextField();
        jButtonFinishPurchase = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldQtdProduct = new javax.swing.JTextField();
        jComboBoxListProduto = new javax.swing.JComboBox();
        jButtonAddProduct = new javax.swing.JButton();
        jFormattedTextFieldDataFabrico = new javax.swing.JFormattedTextField();
        jFormattedTextFieldDataExpiracao = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabelTitle.setText("Formulario de Compra");

        try {
            jFormattedTextFieldDataVencimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("20##-##-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel6.setText("data vencimento");

        jLabel7.setText("Data compra");

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

        jLabel9.setText("Obsevacao");

        jTextAreaNote.setColumns(20);
        jTextAreaNote.setRows(5);
        jScrollPane2.setViewportView(jTextAreaNote);

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

        jLabel5.setText("Imposto");

        jTextFieldIvaTotal.setText("0.0");

        jLabel3.setText("Total");

        jTextFieldTotal.setText("0.0");

        jButtonFinishPurchase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Done.png"))); // NOI18N
        jButtonFinishPurchase.setText("Finalizar");
        jButtonFinishPurchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFinishPurchaseActionPerformed(evt);
            }
        });

        jLabel1.setText("Fornecedor");

        jLabel8.setText("QTD");

        jLabel2.setText("Produto");

        jTextFieldQtdProduct.setText("1");

        jComboBoxListProduto.setToolTipText("");

        jButtonAddProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Plus.png"))); // NOI18N
        jButtonAddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddProductActionPerformed(evt);
            }
        });

        try {
            jFormattedTextFieldDataFabrico.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("20##-##-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jFormattedTextFieldDataExpiracao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("20##-##-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel10.setText("Data de Expiracao");

        jLabel4.setText("Data de Fabrico");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxListProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jTextFieldQtdProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jFormattedTextFieldDataFabrico, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10)
                            .addComponent(jFormattedTextFieldDataExpiracao, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addGap(50, 50, 50))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel1)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jComboBoxListFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jFormattedTextFieldDataCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(12, 12, 12)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jFormattedTextFieldDataVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldIvaTotal, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButtonFinishPurchase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(23, 23, 23))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitle)
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxListFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextFieldDataVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextFieldDataCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(46, 46, 46))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jComboBoxListProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldQtdProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jFormattedTextFieldDataFabrico, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jFormattedTextFieldDataExpiracao, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jButtonAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                        .addGap(28, 28, 28)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextFieldIvaTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonFinishPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(916, 521));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jFormattedTextFieldDataCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextFieldDataCompraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextFieldDataCompraActionPerformed

    private void jButtonAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddProductActionPerformed
        // TODO add your handling code here:
        criacaoItem();
//        // validar produto e quantidade
//        if (jComboBoxListProduto.getSelectedItem() == null) {
//            JOptionPane.showMessageDialog(this, "Selecione um produto!");
//            return;
//        }
//        int qtd;
//        try {
//            qtd = Integer.parseInt(jTextFieldQtdProduct.getText());
//            if (qtd <= 0) {
//                throw new NumberFormatException();
//            }
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(this, "Quantidade inválida!");
//            return;
//        }
//
//        // produto selecionado
//        Product prod = (Product) jComboBoxListProduto.getSelectedItem();
//
//        // criar item
//        PurchaseItem item = new PurchaseItem();
//        item.setProduct(prod);
//        item.setQuantidade(qtd);
//        item.setPrecoCusto(prod.getPrice());
//        item.setSubtotal(prod.getPrice().multiply(BigDecimal.valueOf(qtd)));
//
//// 🔹 Verificar se já existe esse produto na lista
//        boolean jaExiste = false;
//        for (PurchaseItem it : itensCompra) {
//            if (it.getProduct().getId() == prod.getId()) {
//                // já existe → soma quantidade e recalcula subtotal
//                it.setQuantidade(it.getQuantidade() + qtd);
//                it.setSubtotal(it.getPrecoCusto().multiply(BigDecimal.valueOf(it.getQuantidade())));
//                jaExiste = true;
//                break;
//            }
//        }
//
//// 🔹 Se não existir, adiciona normalmente
//        if (!jaExiste) {
//            itensCompra.add(item);
//        }
//
//// atualizar tabela
//        loadItemsTable();
//        atualizarTotais();
    }//GEN-LAST:event_jButtonAddProductActionPerformed

    private void jButtonFinishPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFinishPurchaseActionPerformed
        // TODO add your handling code here:
        finalizarCompra();
//        // validações
//        if (jComboBoxListFornecedor.getSelectedItem() == null) {
//            JOptionPane.showMessageDialog(this, "Selecione um fornecedor!");
//            return;
//        }
//        if (itensCompra.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Adicione ao menos 1 item!");
//            return;
//        }
//
//        Supplier fornecedor = (Supplier) jComboBoxListFornecedor.getSelectedItem();
//
//        Purchase purchase = new Purchase();
//        purchase.setSupplier(fornecedor);
//        purchase.setDescricao(jTextAreaNote.getText());
//        purchase.setTotal(new BigDecimal(jTextFieldTotal.getText()));
//        purchase.setIvaTotal(new BigDecimal(jTextFieldIvaTotal.getText()));
//        purchase.setItems(itensCompra);
//
//        try {
//            java.sql.Date dataCompra = java.sql.Date.valueOf(jFormattedTextFieldDataCompra.getText());
//            java.sql.Date dataVenc = java.sql.Date.valueOf(jFormattedTextFieldDataVencimento.getText());
//            purchase.setDataCompra(dataCompra);
//            purchase.setDataVencimento(dataVenc);
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Datas inválidas!");
//            return;
//        }
//
//        // status inicial
//        purchase.setStatus("aberto");
//
//        // salvar na BD via controller
//        response = new PurchaseController().add(purchase);
//        if (response) {
////            JOptionPane.showMessageDialog(this, "Compra salva com sucesso!");
//            dispose(); // fecha o dialog
//        } else {
//            JOptionPane.showMessageDialog(this, "Erro ao salvar compra!");
//        }
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTablePurchaseItems;
    private javax.swing.JTextArea jTextAreaNote;
    private javax.swing.JTextField jTextFieldIvaTotal;
    private javax.swing.JTextField jTextFieldQtdProduct;
    private javax.swing.JTextField jTextFieldTotal;
    // End of variables declaration//GEN-END:variables
}
