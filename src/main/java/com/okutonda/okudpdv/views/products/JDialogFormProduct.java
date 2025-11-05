/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.okutonda.okudpdv.views.products;

import com.okutonda.okudpdv.controllers.GroupsProductController;
import com.okutonda.okudpdv.controllers.ProductController;
import com.okutonda.okudpdv.controllers.TaxeReasonController;
import com.okutonda.okudpdv.controllers.TaxeController;
import com.okutonda.okudpdv.data.entities.GroupsProduct;
import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.ProductStatus;
import com.okutonda.okudpdv.data.entities.ProductType;
import com.okutonda.okudpdv.data.entities.ReasonTaxes;
import com.okutonda.okudpdv.data.entities.Taxes;
import com.okutonda.okudpdv.helpers.Util;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author kenny
 */
public final class JDialogFormProduct extends javax.swing.JDialog {

    ProductController productController = new ProductController();
//    SupplierController supplierController = new SupplierController();
    TaxeController taxeController = new TaxeController();
    TaxeReasonController reasonTaxeController = new TaxeReasonController();
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
    }

    public Boolean getResponse() {
        return status;
    }
    private boolean carregandoDados = false;

    public void loadCombobox() {
        // Combos com consulta ao banco
        carregarComboReasonTaxes();
        carregarComboTaxes();
        carregarComboGroups();

        // Combos estáticos
        carregarCombosEstaticos();
    }

    private void carregarComboReasonTaxes() {
        jComboBoxReasonTaxeId.removeAllItems();
        List<ReasonTaxes> listR = reasonTaxeController.listarTodas();
        for (ReasonTaxes item : listR) {
            jComboBoxReasonTaxeId.addItem(item);
        }
    }

    private void carregarComboTaxes() {
        jComboBoxTaxeId.removeAllItems();
        List<Taxes> listT = taxeController.listarTodas();
        for (Taxes item : listT) {
            jComboBoxTaxeId.addItem(item);
        }
    }

    private void carregarComboGroups() {
        jComboBoxGroup.removeAllItems();
        List<GroupsProduct> listG = groupsProductController.getAll();
        System.out.println(listG);
        for (GroupsProduct item : listG) {
            jComboBoxGroup.addItem(item);
        }
    }

    private void carregarCombosEstaticos() {
        carregandoDados = true; // Inicia o carregamento

        // Combo Status
        jComboBoxStatus.removeAllItems();
        jComboBoxStatus.addItem("Ativo");
        jComboBoxStatus.addItem("Inativo");

        // Combo Tipo
        jComboBoxType.removeAllItems();
        jComboBoxType.addItem("Produto");
        jComboBoxType.addItem("Serviço");

        carregandoDados = false; // Finaliza o carregamento
    }

    public void setFormProduct(Product prod) {
        if (prod != null) {
            // TextFields
            jTextFieldId.setText(String.valueOf(prod.getId()));
            jTextFieldCode.setText(prod.getCode());
            jTextFieldBarCode.setText(prod.getBarcode());
            jTextFieldDescription.setText(prod.getDescription());
            jTextFieldPrice.setText(String.valueOf(prod.getPrice()));
            jTextFieldPurchasePrice.setText(String.valueOf(prod.getPurchasePrice()));
            jTextFieldStockMinimo.setText(String.valueOf(prod.getMinStock()));

            // Combos estáticos
            selecionarItemPorString(jComboBoxType, prod.getType());
            // Mapear status numérico para texto
            if (prod.getStatus() != null) {
                ProductStatus statusTexto = (prod.getStatus() == ProductStatus.ACTIVE) ? ProductStatus.ACTIVE : ProductStatus.INACTIVE;
                jComboBoxStatus.setSelectedItem(statusTexto);
            }

            // Combos dinâmicos
            if (prod.getTaxe() != null) {
                selecionarItemComboBox(jComboBoxTaxeId, prod.getTaxe());
            }
            if (prod.getReasonTaxe() != null) {
                selecionarItemComboBox(jComboBoxReasonTaxeId, prod.getReasonTaxe());
            }
            System.out.println("grupo: " + prod.getGroup());
            if (prod.getGroup() != null) {
                selecionarItemComboBox(jComboBoxGroup, prod.getGroup());
            }
        }
    }

// Método auxiliar para selecionar item por string nos combos estáticos
    private void selecionarItemPorString(JComboBox<String> comboBox, ProductType valor) {
        if (valor != null) {
            for (int i = 0; i < comboBox.getItemCount(); i++) {
                if (valor.equals(comboBox.getItemAt(i))) {
                    comboBox.setSelectedIndex(i);
                    return;
                }
            }
        }
    }

// Método auxiliar para selecionar item por objeto nos combos dinâmicos
    private void selecionarItemComboBox(JComboBox comboBox, Object objetoParaSelecionar) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            Object item = comboBox.getItemAt(i);

            // Tenta comparar por igualdade direta
            if (item.equals(objetoParaSelecionar)) {
                comboBox.setSelectedIndex(i);
                return;
            }

            // Tenta comparar por ID se disponível
            if (temMesmoId(item, objetoParaSelecionar)) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
    }

// Verifica se dois objetos têm o mesmo ID
    private boolean temMesmoId(Object obj1, Object obj2) {
        try {
            Method getId1 = obj1.getClass().getMethod("getId");
            Method getId2 = obj2.getClass().getMethod("getId");

            Object id1 = getId1.invoke(obj1);
            Object id2 = getId2.invoke(obj2);

            return id1 != null && id1.equals(id2);
        } catch (Exception e) {
            return false;
        }
    }

    public void setProduct(int id) {
        Product prod = productController.getById(id);
        setFormProduct(prod);
    }

// Na sua View - validação completa de campos obrigatórios
    private boolean validarCamposUI() {
        // 1️⃣ Validação do Código
        if (jTextFieldCode.getText().trim().isEmpty()) {
            showError("Código é obrigatório!");
            jTextFieldCode.requestFocus();
            return false;
        }
        if (jTextFieldCode.getText().trim().length() < 3) {
            showError("Código deve ter pelo menos 3 caracteres");
            jTextFieldCode.requestFocus();
            return false;
        }

        // 2️⃣ Validação da Descrição
        if (jTextFieldDescription.getText().trim().isEmpty()) {
            showError("Descrição é obrigatória!");
            jTextFieldDescription.requestFocus();
            return false;
        }
        if (jTextFieldDescription.getText().trim().length() < 3) {
            showError("Descrição deve ter pelo menos 3 caracteres");
            jTextFieldDescription.requestFocus();
            return false;
        }

        // 3️⃣ Validação do Código de Barras
        if (jTextFieldBarCode.getText().trim().isEmpty()) {
            showError("Código de Barras é obrigatório!");
            jTextFieldBarCode.requestFocus();
            return false;
        }
        if (jTextFieldBarCode.getText().trim().length() < 9) {
            showError("Código de Barras deve ter pelo menos 9 caracteres");
            jTextFieldBarCode.requestFocus();
            return false;
        }

        // 4️⃣ Validação do Preço de Venda
        if (jTextFieldPrice.getText().trim().isEmpty()) {
            showError("Preço de Venda é obrigatório!");
            jTextFieldPrice.requestFocus();
            return false;
        }
        if (!Util.isValidDouble(jTextFieldPrice.getText())) {
            showError("Preço de Venda deve ser um número válido\nEx: 150.00 ou 150,00");
            jTextFieldPrice.requestFocus();
            return false;
        }
        try {
            BigDecimal preco = new BigDecimal(jTextFieldPrice.getText().replace(",", "."));
            if (preco.compareTo(BigDecimal.ZERO) < 0) {
                showError("Preço de Venda não pode ser negativo");
                jTextFieldPrice.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Formato de Preço de Venda inválido");
            jTextFieldPrice.requestFocus();
            return false;
        }

        // 5️⃣ Validação do Preço de Compra
        if (jTextFieldPurchasePrice.getText().trim().isEmpty()) {
            showError("Preço de Compra é obrigatório!");
            jTextFieldPurchasePrice.requestFocus();
            return false;
        }
        if (!Util.isValidDouble(jTextFieldPurchasePrice.getText())) {
            showError("Preço de Compra deve ser um número válido\nEx: 100.00 ou 100,00");
            jTextFieldPurchasePrice.requestFocus();
            return false;
        }
        try {
            BigDecimal precoCompra = new BigDecimal(jTextFieldPurchasePrice.getText().replace(",", "."));
            if (precoCompra.compareTo(BigDecimal.ZERO) < 0) {
                showError("Preço de Compra não pode ser negativo");
                jTextFieldPurchasePrice.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Formato de Preço de Compra inválido");
            jTextFieldPurchasePrice.requestFocus();
            return false;
        }

        // 6️⃣ Validação do Stock Mínimo
        if (jTextFieldStockMinimo.getText().trim().isEmpty()) {
            showError("Stock Mínimo é obrigatório!");
            jTextFieldStockMinimo.requestFocus();
            return false;
        }
        if (!Util.isInteger(jTextFieldStockMinimo.getText())) {
            showError("Stock Mínimo deve ser um número inteiro\nEx: 10, 50, 100");
            jTextFieldStockMinimo.requestFocus();
            return false;
        }
        try {
            int stockMinimo = Integer.parseInt(jTextFieldStockMinimo.getText().trim());
            if (stockMinimo < 0) {
                showError("Stock Mínimo não pode ser negativo");
                jTextFieldStockMinimo.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Formato de Stock Mínimo inválido");
            jTextFieldStockMinimo.requestFocus();
            return false;
        }

        // 7️⃣ Validação do Tipo
        if (jComboBoxType.getSelectedItem() == null) {
            showError("Tipo do produto é obrigatório!");
            jComboBoxType.requestFocus();
            return false;
        }

        // 8️⃣ Validação da Taxa
        if (jComboBoxTaxeId.getSelectedItem() == null) {
            showError("Taxa é obrigatória!");
            jComboBoxTaxeId.requestFocus();
            return false;
        }

        // 9️⃣ Validação da Reason Tax
        if (jComboBoxReasonTaxeId.getSelectedItem() == null) {
            showError("Reason Tax é obrigatória!");
            jComboBoxReasonTaxeId.requestFocus();
            return false;
        }

        // 🔟 Validação do Status
        if (jComboBoxStatus.getSelectedItem() == null) {
            showError("Status é obrigatório!");
            jComboBoxStatus.requestFocus();
            return false;
        }

        // ✅ Todas as validações passaram
        return true;
    }

// Método auxiliar para mostrar erros
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro de Validação", JOptionPane.ERROR_MESSAGE);
    }

// Método auxiliar para mostrar sucesso
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    // Função simplificada para criar/salvar produto

    private void salvarProduto() {
        // 1️⃣ PRIMEIRO: Validação completa da UI
        if (!validarCamposUI()) {
            status = false;
            return; // Para aqui se houver erro de validação
        }

        try {
            // 2️⃣ Criar objeto Product a partir dos campos
            Product produto = criarProdutoFromCampos();

            // 3️⃣ Chamar Controller (que vai fazer validação de negócio)
            Product produtoSalvo = productController.save(produto);

            // 4️⃣ Verificar resultado
            if (produtoSalvo != null && produtoSalvo.getId() != null) {
                showSuccess(produto.getId() == null
                        ? "Produto criado com sucesso!"
                        : "Produto atualizado com sucesso!");
                status = true;
                this.dispose(); // Fechar diálogo
            }
            // Se retornar null, o Controller já mostrou o erro

        } catch (NumberFormatException e) {
            showError("Erro na conversão de números: " + e.getMessage());
        } catch (Exception e) {
            showError("Erro inesperado: " + e.getMessage());
            System.err.println("❌ Erro ao salvar produto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Product criarProdutoFromCampos() {
        Product produto = new Product();

        // Campos obrigatórios (já validados)
        produto.setCode(jTextFieldCode.getText().trim());
        produto.setDescription(jTextFieldDescription.getText().trim());
        produto.setBarcode(jTextFieldBarCode.getText().trim());
        produto.setPrice(new BigDecimal(jTextFieldPrice.getText().replace(",", ".")));
        produto.setPurchasePrice(new BigDecimal(jTextFieldPurchasePrice.getText().replace(",", ".")));
        produto.setMinStock(Integer.parseInt(jTextFieldStockMinimo.getText().trim()));
        
        String tipoSelecionado = jComboBoxType.getSelectedItem().toString();
        produto.setType(ProductType.fromDescription(tipoSelecionado));
//        produto.setType(jComboBoxType.getSelectedItem().toString());
        produto.setTaxe((Taxes) jComboBoxTaxeId.getSelectedItem());
        produto.setReasonTaxe((ReasonTaxes) jComboBoxReasonTaxeId.getSelectedItem());

        // Campos opcionais
        if (jComboBoxGroup.getSelectedItem() != null) {
            produto.setGroup((GroupsProduct) jComboBoxGroup.getSelectedItem());
        }

        // Status
        String status = (String) jComboBoxStatus.getSelectedItem();
        produto.setStatus(ProductStatus.ACTIVE.equals(status) ? ProductStatus.ACTIVE : ProductStatus.INACTIVE);
//        produto.setStatus("Ativo".equals(status) ? 1 : 0);

        // Se é edição, setar ID
        if (!jTextFieldId.getText().isEmpty()) {
            produto.setId(Integer.parseInt(jTextFieldId.getText()));
        }

        return produto;
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
        jTextFieldStockMinimo = new javax.swing.JTextField();
        jTextFieldPurchasePrice = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButtonClearForm = new javax.swing.JButton();
        jButtonSalvarProduto = new javax.swing.JButton();
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

        jTextFieldStockMinimo.setText("0");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Preço de Compra");

        jLabel5.setForeground(new java.awt.Color(204, 0, 0));
        jLabel5.setText("Imposto deve estar incluso no preco unitario");

        jButtonClearForm.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonClearForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Restart.png"))); // NOI18N
        jButtonClearForm.setText("Limpar");
        jButtonClearForm.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jButtonSalvarProduto.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonSalvarProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Plus.png"))); // NOI18N
        jButtonSalvarProduto.setText("Salvar");
        jButtonSalvarProduto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonSalvarProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalvarProdutoActionPerformed(evt);
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
            .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelFormProductLayout.createSequentialGroup()
                    .addGap(347, 347, 347)
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
                                .addComponent(jLabel11)))
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(26, 26, 26)
                    .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel20)
                        .addComponent(jLabel16)
                        .addComponent(jLabel18)
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
                                .addComponent(jTextFieldStockMinimo, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)))))
                .addGroup(jPanelFormProductLayout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(jLabel1)
                    .addGap(18, 18, 18)
                    .addComponent(jTextFieldId, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanelFormProductLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonSalvarProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonClearForm)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonCancelFormAdd)
                .addContainerGap())
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
                            .addComponent(jTextFieldStockMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addGap(18, 18, 18)
                .addGroup(jPanelFormProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSalvarProduto)
                    .addComponent(jButtonClearForm)
                    .addComponent(jButtonCancelFormAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jLabel12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelFormProductLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonCancelFormAdd, jButtonClearForm, jButtonSalvarProduto});

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

        setSize(new java.awt.Dimension(788, 470));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldCodeActionPerformed

    private void jTextFieldDescriptionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDescriptionKeyPressed
        // TODO add your handling code here:
//        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
//            String description = jTextFieldDescription.getText();
//            Product cModel;
//            cModel = productController.getByDescription(description);
//            if (cModel.getDescription() != null) {
//                changeValueTextFildForm(cModel);
//            } else {
//                JOptionPane.showMessageDialog(null, "Product nao encontrado!");
//            }
//        }
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

    private void jButtonSalvarProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalvarProdutoActionPerformed
        // TODO add your handling code here:
        salvarProduto();
    }//GEN-LAST:event_jButtonSalvarProdutoActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
//        loadCombobox();
    }//GEN-LAST:event_formWindowActivated

    private void jComboBoxTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxTypeItemStateChanged
        // TODO add your handling code here:
        if (carregandoDados) {
            return;
        }

        if (evt.getStateChange() == ItemEvent.SELECTED) {
            Object selectedItem = jComboBoxType.getSelectedItem();

            // VERIFICAÇÃO CRÍTICA: nunca use toString() sem verificar null
            if (selectedItem != null) {
                String type = selectedItem.toString();
                if ("Produto".equals(type)) {
                    jTextFieldStockMinimo.setEnabled(true);
                    jTextFieldBarCode.setEnabled(true);
                } else {
                    jTextFieldStockMinimo.setEnabled(false);
                    jTextFieldBarCode.setEnabled(false);
                }
            }
        }
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
    private javax.swing.JButton jButtonCancelFormAdd;
    private javax.swing.JButton jButtonClearForm;
    private javax.swing.JButton jButtonSalvarProduto;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanelFormProduct;
    private javax.swing.JTextField jTextFieldBarCode;
    private javax.swing.JTextField jTextFieldCode;
    private javax.swing.JTextField jTextFieldDescription;
    private javax.swing.JTextField jTextFieldId;
    private javax.swing.JTextField jTextFieldPrice;
    private javax.swing.JTextField jTextFieldPurchasePrice;
    private javax.swing.JTextField jTextFieldStockMinimo;
    // End of variables declaration//GEN-END:variables
}
