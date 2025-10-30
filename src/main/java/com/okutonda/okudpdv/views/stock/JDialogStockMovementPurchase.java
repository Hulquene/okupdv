/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.okutonda.okudpdv.views.stock;

import com.okutonda.okudpdv.controllers.ProductController;
import com.okutonda.okudpdv.controllers.PurchaseController;
import com.okutonda.okudpdv.controllers.StockMovementController;
import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.Purchase;
import com.okutonda.okudpdv.data.entities.PurchaseItem;
import com.okutonda.okudpdv.data.entities.StockMovement;
import com.okutonda.okudpdv.data.entities.Warehouse;
import com.okutonda.okudpdv.helpers.UserSession;
import com.okutonda.okudpdv.helpers.Util;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hr
 */
public class JDialogStockMovementPurchase extends javax.swing.JDialog {

    PurchaseController purchaseController = new PurchaseController();
    Boolean response = false;
    final String origem = "COMPRA";
    private int produtoSelecionadoId = -1;

    /**
     * Creates new form JDialogStockMovementPurchase
     */
    public JDialogStockMovementPurchase(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        configurarTabelaMovimentos();
        listPurchase();
    }

    public Boolean getResponse() {
        return response;
    }

    private void configurarTabelaMovimentos() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID Produto", "Descrição", "Quantidade", "Armazém",
                    "Origem", "Tipo", "Referência", "Notas"
                }
        ) {
            Class[] types = new Class[]{
                Integer.class, String.class, Integer.class, Integer.class,
                String.class, String.class, String.class, String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, true, false, true, true, true, true
            };

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };

        jTableProductsFromMovimento.setModel(model);
    }

    public void listPurchase() {
        List<Purchase> list = purchaseController.listar(); // busca todas as compras ou compras abertas
        loadListPurchase(list);
    }

    public void loadListPurchase(List<Purchase> list) {
        DefaultTableModel model = (DefaultTableModel) jTableListProduct.getModel();
        model.setRowCount(0); // limpa tabela

        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma compra encontrada para entrada de estoque.");
            return;
        }

        for (Purchase p : list) {
            if (p.getItems() != null) {
                for (PurchaseItem item : p.getItems()) {

                    System.out.println(item);

                    int quantidade = item.getQuantidade() != null ? item.getQuantidade() : 0;
                    int entrada = item.getQuantidadeEntrada() != null ? item.getQuantidadeEntrada() : 0;
                    int faltante = Math.max(quantidade - entrada, 0);
                    String status = item.getEntradaStatus() != null ? item.getEntradaStatus() : "nao_iniciado";

                    // ✅ Só lista se ainda houver quantidade pendente
                    if (faltante > 0) {
                        model.addRow(new Object[]{
                            item.getProduct().getId(), // ID Produto
                            item.getProduct().getDescription(), // Descrição
                            //                            p.getInvoiceNumber(), // Nº Fatura
                            quantidade, // Qtd total da compra
                            entrada, // Qtd já lançada
                            faltante, // Qtd restante
                            status.toUpperCase(), // Estado (NAO_INICIADO / PARCIAL)
                            item.getPrecoCusto(), // Preço de custo
                            p.getId(), // ID da compra
                        });
                    }
                }
            }
        }
    }

//    public void filterListPurchase(String txt) {
////        List<Purchase> list = purchaseController.filter(txt);
////        loadListPurchase(list);
//    }
    private void carregarTabelaOrigem(String origem) {
        DefaultTableModel model = (DefaultTableModel) jTableListProduct.getModel();
        model.setRowCount(0);

        List<?> lista;

        switch (origem.toUpperCase()) {
            case "COMPRA":
                lista = purchaseController.listar(); // busca todas as compras ou compras abertas
                for (Purchase p : (List<Purchase>) lista) {
                    if (p.getItems() != null) {  // protege contra null
                        for (PurchaseItem item : p.getItems()) {
                            model.addRow(new Object[]{
                                item.getProduct().getId(),
                                item.getProduct().getDescription(),
                                item.getPrecoCusto(),
                                item.getQuantidade(),});
                        }
                    }
                }
                break;

            case "VENDA":
                // buscar os pedidos/vendas
                // orderController.get(...) e adicionar ao model
                break;

            case "MANUAL":
            case "AJUSTE":
            case "TRANSFERENCIA":
                // listar produtos ativos
                ProductController pc = new ProductController();
                List<Product> produtos = pc.listProducts();
                for (Product prod : produtos) {
                    model.addRow(new Object[]{
                        prod.getId(),
                        prod.getDescription(),
                        prod.getPrice()
                    });
                }
                break;
        }
    }

    private void processarMovimento() {
        try {
            DefaultTableModel model = (DefaultTableModel) jTableProductsFromMovimento.getModel();
            int totalRows = model.getRowCount();

            if (totalRows == 0) {
                JOptionPane.showMessageDialog(this,
                        "Nenhum produto foi adicionado ao movimento!",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            StockMovementController controller = new StockMovementController();

            for (int i = 0; i < totalRows; i++) {
                // === Ler dados da linha da tabela ===
                int productId = (int) model.getValueAt(i, 0);   // ID Produto
                String descricao = (String) model.getValueAt(i, 1); // Descrição
                int quantidade = (int) model.getValueAt(i, 2);   // Quantidade
                int warehouseId = (int) model.getValueAt(i, 3);   // Armazém origem
                String origem = (String) model.getValueAt(i, 4); // Origem (COMPRA, VENDA…)
                String tipo = (String) model.getValueAt(i, 5); // Tipo (ENTRADA, SAIDA…)
                String referencia = (String) model.getValueAt(i, 6); // ID do documento ligado
                String notas = (String) model.getValueAt(i, 7); // Notas / motivo

                // === Construir objetos ===
                Product produto = new Product();
                produto.setId(productId);
                produto.setDescription(descricao);

                Warehouse warehouse = new Warehouse();
                warehouse.setId(warehouseId);

                StockMovement movimento = new StockMovement();
                movimento.setProduct(produto);
                movimento.setWarehouse(warehouse);
                movimento.setQuantity(quantidade);
                movimento.setType(tipo != null ? tipo.toUpperCase() : "ENTRADA");
                movimento.setOrigin(origem != null ? origem.toUpperCase() : "MANUAL");
                movimento.setNotes(notas != null ? notas : "");
                movimento.setReferenceId(
                        (referencia != null && !referencia.isEmpty())
                        ? Integer.parseInt(referencia)
                        : null
                );

                // Pega user logado pela sessão
                movimento.setUser(UserSession.getInstance().getUser());

                // === Enviar para controller ===
                response = controller.registrar(movimento);
            }

            if (response) {
                JOptionPane.showMessageDialog(this,
                        "Movimento registado com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // Limpa a tabela depois de salvar
                model.setRowCount(0);
                dispose(); // fecha o dialog
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro de formatação numérica (quantidade ou referência inválida)!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao processar movimento: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void removerProdutoDaTabela() {
        try {
            int row = jTableProductsFromMovimento.getSelectedRow();
            if (row >= 0) {
                DefaultTableModel model = (DefaultTableModel) jTableProductsFromMovimento.getModel();
                model.removeRow(row);
                JOptionPane.showMessageDialog(this, "Produto removido da lista!", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma linha para remover!", "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao remover produto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherCamposProdutoSelecionado() {
        int row = jTableListProduct.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) jTableListProduct.getModel();

        // Pega os valores da linha
        int productId = (int) model.getValueAt(row, 0);        // Coluna ID
        String descricao = (String) model.getValueAt(row, 1);  // Coluna Descrição
//        double preco = (double) model.getValueAt(row, 2);      // Coluna Preço
        int quantidade = (int) model.getValueAt(row, 4);       // Coluna Quantidade

        int ref = (int) model.getValueAt(row, 7);       // Coluna Quantidade

        // Aqui o preço pode vir como BigDecimal
        Object precoObj = model.getValueAt(row, 6);
        String precoStr = precoObj != null ? precoObj.toString() : "0.00";

        // Preenche os fields
        jLabelIDProdSelected.setText(String.valueOf(productId));
        jTextFieldNameProdSelected.setText(descricao);
        jTextFieldQtdProdSelected.setText(String.valueOf(quantidade));
        jTextFieldPriceCustoProd.setText(String.valueOf(precoStr));
        jTextFieldReference.setText(String.valueOf(ref));

        // Se tiver validade / expiração na coluna
//        Object validade = model.getValueAt(row, 6);
//        if (validade != null) {
//            jFormattedTextFieldDateValidateProd.setText(validade.toString());
//        }
    }

    private void adicionarProdutoNaTabela() {
        try {
            // 1. Ler os campos
            int productId = Integer.parseInt(jLabelIDProdSelected.getText().trim());
            String nomeProduto = jTextFieldNameProdSelected.getText().trim();
            String qtdStr = jTextFieldQtdProdSelected.getText().trim();
            String precoCusto = jTextFieldPriceCustoProd.getText().trim();
            String referencia = jTextFieldReference.getText().trim();
//            String notas = jTextAreaNoteOrReason.getText().trim();

            int quantidade = Integer.parseInt(qtdStr);

            // Para já vamos supor que já tens o ID do produto (podes ter via hidden field ou objeto selecionado)
//            int productId = obterProdutoSelecionadoId(); // 👉 método auxiliar que vais implementar
            // Warehouse (armazém de origem)
            int warehouseId = jComboBoxWarehouse.getSelectedIndex() + 1; // exemplo simples

            // Tipo e origem
            String tipo =  "ENTRADA";
//            String tipo = jComboBoxType.getSelectedItem().toString();
//            String origem = jComboBoxOrigin.getSelectedItem().toString();

            // 2. Validar dados mínimos
            if (productId <= 0 || nomeProduto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um produto válido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "Informe uma quantidade válida!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. Adicionar linha à tabela
            DefaultTableModel model = (DefaultTableModel) jTableProductsFromMovimento.getModel();
            model.addRow(new Object[]{
                productId,
                nomeProduto,
                quantidade,
                warehouseId,
                this.origem,
                tipo,
                referencia
//                notas
            });

            // 4. Limpar campos após adicionar
            jTextFieldNameProdSelected.setText("");
            jTextFieldQtdProdSelected.setText("1");
            jTextFieldPriceCustoProd.setText("");
            jTextFieldReference.setText("");
            jTextAreaNoteOrReason.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar produto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int obterProdutoSelecionadoId() {
        return produtoSelecionadoId;
    }

// Quando selecionares um produto da lista de origem:
    private void selecionarProduto(int id, String descricao) {
        produtoSelecionadoId = id;
        jTextFieldNameProdSelected.setText(descricao);
    }

    private void jComboBoxOriginActionPerformed(java.awt.event.ActionEvent evt) {
//        String origem = jComboBoxOrigin.getSelectedItem().toString();
//        carregarTabelaOrigem(origem);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableListProduct = new javax.swing.JTable();
        jButtonSaveMovimento = new javax.swing.JButton();
        jTextFieldSearchProd = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableProductsFromMovimento = new javax.swing.JTable();
        jComboBoxWarehouse = new javax.swing.JComboBox<>();
        jTextFieldNameProdSelected = new javax.swing.JTextField();
        jTextFieldQtdProdSelected = new javax.swing.JTextField();
        jButtonAddProdToList = new javax.swing.JButton();
        jComboBoxType = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaNoteOrReason = new javax.swing.JTextArea();
        jLabel12 = new javax.swing.JLabel();
        jLabelNameUser = new javax.swing.JLabel();
        jLabelDate = new javax.swing.JLabel();
        jTextFieldPriceCustoProd = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jFormattedTextFieldDateValidateProd = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldReference = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jButtonRemoveProdList = new javax.swing.JButton();
        jLabelIDProdSelected = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setText("Movimento de Estoque Compras");

        jTableListProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Producto", "Qtd", "Entrada", "Faltante", "Status", "Preço", "Refer."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableListProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableListProductMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableListProduct);

        jButtonSaveMovimento.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonSaveMovimento.setText("Salvar");
        jButtonSaveMovimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveMovimentoActionPerformed(evt);
            }
        });

        jTextFieldSearchProd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSearchProdKeyReleased(evt);
            }
        });

        jLabel5.setText("Pesquisar por descricao e preço");

        jTableProductsFromMovimento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7"
            }
        ));
        jScrollPane2.setViewportView(jTableProductsFromMovimento);

        jComboBoxWarehouse.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextFieldNameProdSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNameProdSelectedActionPerformed(evt);
            }
        });

        jTextFieldQtdProdSelected.setText("1");
        jTextFieldQtdProdSelected.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldQtdProdSelectedKeyReleased(evt);
            }
        });

        jButtonAddProdToList.setText("Add");
        jButtonAddProdToList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddProdToListActionPerformed(evt);
            }
        });

        jComboBoxType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Entrada", "Saída", "Ajuste" }));

        jLabel6.setText("Nome e codigo do produto");

        jLabel7.setText("QTD");

        jLabel8.setText("Armazem");

        jLabel10.setText("Tipo");

        jLabel11.setText("Origem: Compra");

        jTextAreaNoteOrReason.setColumns(20);
        jTextAreaNoteOrReason.setRows(5);
        jScrollPane3.setViewportView(jTextAreaNoteOrReason);

        jLabel12.setText("Motivo do movimento (reason)");

        jLabelNameUser.setText("Utilizador");

        jLabelDate.setText("Data/Hora");

        jLabel2.setText("Preço Unitário");

        try {
            jFormattedTextFieldDateValidateProd.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("20##-##-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel3.setText("Data de expiração");

        jLabel13.setText("Referência ao documento de origem");

        jButtonRemoveProdList.setText("Remover");
        jButtonRemoveProdList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveProdListActionPerformed(evt);
            }
        });

        jLabelIDProdSelected.setText("ID");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jComboBoxWarehouse, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(jTextFieldReference, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jTextFieldSearchProd, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldNameProdSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabelIDProdSelected, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel6)))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7)
                                .addGap(42, 42, 42)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldPriceCustoProd, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButtonAddProdToList)
                                    .addComponent(jFormattedTextFieldDateValidateProd, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3)))
                            .addComponent(jScrollPane1)
                            .addComponent(jComboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 47, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addGap(285, 285, 285))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jButtonSaveMovimento, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButtonRemoveProdList)))
                                        .addGap(21, 21, 21))))
                            .addComponent(jScrollPane2)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel11))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel8)
                                .addGap(162, 162, 162)
                                .addComponent(jTextFieldQtdProdSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(142, 142, 142)
                        .addComponent(jLabelDate, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(105, 105, 105)
                        .addComponent(jLabelNameUser, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel11)
                            .addComponent(jLabelDate)
                            .addComponent(jLabelNameUser))
                        .addGap(22, 22, 22)
                        .addComponent(jLabel10)
                        .addGap(0, 0, 0)
                        .addComponent(jComboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonSaveMovimento, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel2)
                    .addComponent(jLabelIDProdSelected)
                    .addComponent(jLabel3)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextFieldNameProdSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldQtdProdSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldPriceCustoProd, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jFormattedTextFieldDateValidateProd, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(3, 3, 3)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jComboBoxWarehouse, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldReference, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonAddProdToList, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jButtonRemoveProdList, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldSearchProd, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

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

        setSize(new java.awt.Dimension(1049, 558));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTableListProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableListProductMouseClicked
        // TODO add your handling code here:
        preencherCamposProdutoSelecionado();
    }//GEN-LAST:event_jTableListProductMouseClicked

    private void jButtonSaveMovimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveMovimentoActionPerformed
        // TODO add your handling code here:
        processarMovimento();
    }//GEN-LAST:event_jButtonSaveMovimentoActionPerformed

    private void jTextFieldSearchProdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearchProdKeyReleased
        // TODO add your handling code here:
        String txt = jTextFieldSearchProd.getText();
        if (!txt.isEmpty()) {
            //            filterListPurchase(txt);
        } else {
            //            listPurchase();
        }
    }//GEN-LAST:event_jTextFieldSearchProdKeyReleased

    private void jTextFieldNameProdSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNameProdSelectedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNameProdSelectedActionPerformed

    private void jButtonAddProdToListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddProdToListActionPerformed
        // TODO add your handling code here:
        adicionarProdutoNaTabela();
    }//GEN-LAST:event_jButtonAddProdToListActionPerformed

    private void jButtonRemoveProdListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveProdListActionPerformed
        // TODO add your handling code here:
        removerProdutoDaTabela();
    }//GEN-LAST:event_jButtonRemoveProdListActionPerformed

    private void jTextFieldQtdProdSelectedKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldQtdProdSelectedKeyReleased
        // TODO add your handling code here:
        String texto = jTextFieldQtdProdSelected.getText().trim();
        int quantidade = 1; // valor padrão

        // 🔹 1️⃣ Se o campo estiver vazio → não forçar nada (permite o utilizador digitar)
        if (texto.isEmpty()) {
            return;
        }

        // 🔹 2️⃣ Verifica se é número válido
        if (!Util.checkIsNumber(texto)) {
            // opcional: alerta visual sem forçar "1"
//            jTextFieldQtdProdSelected.setBackground(Color.decode("#f8d7da")); // vermelho leve
            jTextFieldQtdProdSelected.setText(String.valueOf("1"));
            return;
        } else {
//            jTextFieldQtdProdSelected.setBackground(Color.WHITE);
        }

        try {
            quantidade = Integer.parseInt(texto);

            // 🔹 3️⃣ Garante que nunca seja menor que 1
            if (quantidade < 1) {
                quantidade = 1;
            }

        } catch (NumberFormatException e) {
            quantidade = 1; // fallback de segurança
        }

        // 🔹 4️⃣ Atualiza o campo com o valor ajustado (só se for válido)
        jTextFieldQtdProdSelected.setText(String.valueOf(quantidade));
    }//GEN-LAST:event_jTextFieldQtdProdSelectedKeyReleased

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
            java.util.logging.Logger.getLogger(JDialogStockMovementPurchase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogStockMovementPurchase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogStockMovementPurchase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogStockMovementPurchase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogStockMovementPurchase dialog = new JDialogStockMovementPurchase(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonAddProdToList;
    private javax.swing.JButton jButtonRemoveProdList;
    private javax.swing.JButton jButtonSaveMovimento;
    private javax.swing.JComboBox<String> jComboBoxType;
    private javax.swing.JComboBox<String> jComboBoxWarehouse;
    private javax.swing.JFormattedTextField jFormattedTextFieldDateValidateProd;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabelDate;
    private javax.swing.JLabel jLabelIDProdSelected;
    private javax.swing.JLabel jLabelNameUser;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTableListProduct;
    private javax.swing.JTable jTableProductsFromMovimento;
    private javax.swing.JTextArea jTextAreaNoteOrReason;
    private javax.swing.JTextField jTextFieldNameProdSelected;
    private javax.swing.JTextField jTextFieldPriceCustoProd;
    private javax.swing.JTextField jTextFieldQtdProdSelected;
    private javax.swing.JTextField jTextFieldReference;
    private javax.swing.JTextField jTextFieldSearchProd;
    // End of variables declaration//GEN-END:variables
}
