/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.okutonda.okudpdv.views.products;

import com.okutonda.okudpdv.controllers.ProductController;
import com.okutonda.okudpdv.data.entities.Product;
import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kenny
 */
public final class JPanelProduct extends javax.swing.JPanel {

    ProductController productController = new ProductController();

    /**
     * Creates new form JPanelProduct
     */
    public JPanelProduct() {
        initComponents();

        // Inicializar componentes de produtos
        inicializarComponentesProdutos();

//        listProducts();
//        loadCombobox();
    }

    /**
     * Inicializa todos os componentes da tela de produtos Esta função deve ser
     * chamada no construtor
     */
    private void inicializarComponentesProdutos() {
        // 1. Inicializar tabela
        inicializarTabelaProdutos();
        // 2. Carregar dados iniciais
        carregarDadosIniciais();
        // 3. Configurar listeners (se necessário)
        configurarListeners();
    }

    /**
     * Inicializa a tabela de produtos com colunas e configurações Cada função é
     * totalmente responsável pela sua tabela
     */
    private void inicializarTabelaProdutos() {
        jTableProducts.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID", "Tipo", "Código", "Descrição", "Preço Venda",
                    "Taxa", "Reason Tax", "Status", "Grupo"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class,
                java.lang.String.class, java.lang.Double.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        // Configurar largura das colunas para melhor visualização
        jTableProducts.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        jTableProducts.getColumnModel().getColumn(1).setPreferredWidth(80);   // Tipo
        jTableProducts.getColumnModel().getColumn(2).setPreferredWidth(100);  // Código
        jTableProducts.getColumnModel().getColumn(3).setPreferredWidth(250);  // Descrição
        jTableProducts.getColumnModel().getColumn(4).setPreferredWidth(90);   // Preço Venda
        jTableProducts.getColumnModel().getColumn(5).setPreferredWidth(120);  // Taxa
        jTableProducts.getColumnModel().getColumn(6).setPreferredWidth(120);  // Reason Tax
        jTableProducts.getColumnModel().getColumn(7).setPreferredWidth(70);   // Status
        jTableProducts.getColumnModel().getColumn(8).setPreferredWidth(150);  // Grupo

        // Configurar renderizador para preços (opcional)
        jTableProducts.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value instanceof BigDecimal) {
                    setText(String.format("%.2f MT", ((BigDecimal) value).doubleValue()));
                }
                return c;
            }
        });

        // Configurar renderizador para status (opcional)
//        jTableProducts.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
//            @Override
//            public Component getTableCellRendererComponent(JTable table, Object value,
//                    boolean isSelected, boolean hasFocus, int row, int column) {
//                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//                if (value instanceof Integer) {
//                    int status = (Integer) value;
//                    setText(status == 1 ? "Ativo" : "Inativo");
//                    setForeground(status == 1 ? Color.BLUE : Color.RED);
//                }
//                return c;
//            }
//        });
    }

    /**
     * Carrega dados na tabela de produtos Cada função é totalmente responsável
     * pelo carregamento da sua tabela
     */
    public void loadListProducts(List<Product> list) {
        DefaultTableModel data = (DefaultTableModel) jTableProducts.getModel();
        data.setNumRows(0); // Limpar tabela completamente

        if (list == null || list.isEmpty()) {
            System.out.println("ℹ️ Nenhum produto encontrado para carregar na tabela");
            return;
        }
        
        System.out.println("✅ Tabela de produtos carregada: " + list.size() + " registros");

        for (Product produto : list) {
            data.addRow(new Object[]{
                produto.getId(),
                produto.getType(),
                produto.getCode(),
                produto.getDescription(),
                produto.getPrice(),
                // Mostrar nome da taxa em vez do objeto completo
                (produto.getTaxe() != null ? produto.getTaxe().getName() : ""),
                // Mostrar nome da reason tax em vez do objeto completo
                (produto.getReasonTaxe() != null ? produto.getReasonTaxe().getCode() : ""),
                produto.getStatus(), // Será formatado pelo renderizador
                // Mostrar nome do grupo em vez do objeto completo
                (produto.getGroup() != null ? produto.getGroup().getName() : "")
            });
        }

    }

    /**
     * Lista todos os produtos na tabela Cada função é totalmente responsável
     * pelo seu carregamento
     */
    public void listProducts() {
        try {
            List<Product> list = productController.listAll();
            loadListProducts(list);

            // Opcional: Mostrar estatísticas
            Long totalAtivos = productController.contarProdutosAtivos();
            System.out.println("📊 Estatísticas: " + list.size() + " produtos totais, "
                    + totalAtivos + " ativos");

        } catch (Exception e) {
            System.err.println("❌ Erro ao listar produtos: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                    "Erro ao carregar produtos: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Filtra produtos por texto e carrega na tabela Cada função é totalmente
     * responsável pelo seu filtro
     */
    public void filterListProduct(String txt) {
        try {
            if (txt == null || txt.trim().isEmpty()) {
                // Se texto vazio, carrega todos os produtos
                listProducts();
                return;
            }

            String textoFiltro = txt.trim();
            List<Product> list;

            // Decide qual método de filtro usar baseado no contexto
            if (textoFiltro.length() >= 3) {
                // Filtro avançado para PDV (mais performático)
                list = productController.listForPDV(textoFiltro);
            } else {
                // Filtro geral para textos curtos
                list = productController.filter(textoFiltro);
            }

            loadListProducts(list);

            // Feedback visual do filtro
            if (list.isEmpty()) {
                System.out.println("🔍 Nenhum produto encontrado para: '" + textoFiltro + "'");
            } else {
                System.out.println("🔍 Filtro aplicado: " + list.size()
                        + " produtos encontrados para '" + textoFiltro + "'");
            }

        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar produtos: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                    "Erro ao filtrar produtos: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Filtro avançado por tipo de produto
     */
    public void filterByProductType(String tipo) {
        try {
            List<Product> list;

            if ("product".equalsIgnoreCase(tipo)) {
                list = productController.listProducts();
            } else if ("service".equalsIgnoreCase(tipo)) {
                list = productController.listServices();
            } else {
                list = productController.listAll();
            }

            loadListProducts(list);
            System.out.println("✅ Filtro por tipo '" + tipo + "': " + list.size() + " produtos");

        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar por tipo: " + e.getMessage());
        }
    }

    /**
     * Filtro por status (ativos/inativos)
     */
    public void filterByStatus(boolean apenasAtivos) {
        try {
            List<Product> list;

            if (apenasAtivos) {
                list = productController.findActive();
            } else {
                list = productController.listAll();
            }

            loadListProducts(list);
            System.out.println("✅ Filtro por status '" + (apenasAtivos ? "ativos" : "todos")
                    + "': " + list.size() + " produtos");

        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar por status: " + e.getMessage());
        }
    }

    /**
     * Carrega dados iniciais na interface
     */
    private void carregarDadosIniciais() {
        // Carregar produtos na tabela
        listProducts();
        // Opcional: Carregar estatísticas em labels
//        carregarEstatisticas();
    }

    /**
     * Carrega estatísticas dos produtos
     */
    private void carregarEstatisticas() {
        try {
            Long totalProdutos = productController.contarProdutosAtivos();
            Long comStockMinimo = productController.contarProdutosComStockMinimo();

            // Se tiver labels para mostrar estatísticas:
            // jLabelTotalProdutos.setText(totalProdutos.toString());
            // jLabelAlertaStock.setText(comStockMinimo.toString());
            System.out.println("📊 Estatísticas carregadas: " + totalProdutos
                    + " produtos ativos, " + comStockMinimo + " com stock mínimo");

        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar estatísticas: " + e.getMessage());
        }
    }

    /**
     * Configura listeners para eventos
     */
    private void configurarListeners() {
        // Exemplo: Double-click para editar produto
        jTableProducts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    // Editar produto no double-click
                    editarProdutoSelecionado();
                }
            }
        });
    }

    /**
     * Edita produto selecionado na tabela
     */
    private void editarProdutoSelecionado() {
        int selectedRow = jTableProducts.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(null,
                    "Selecione um produto na tabela!!",
                    "Atenção",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Integer productId = (Integer) jTableProducts.getValueAt(selectedRow, 0);
            abrirDialogoEdicaoProduto(productId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao selecionar o produto!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirDialogoEdicaoProduto(int productId) {
        if (productId <= 0) {
            JOptionPane.showMessageDialog(null,
                    "ID do produto inválido!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialogFormProduct formProd = new JDialogFormProduct(null, true);
        formProd.setProduct(productId);
        formProd.setVisible(true);

        Boolean resp = formProd.getResponse();
        if (Boolean.TRUE.equals(resp)) {
            JOptionPane.showMessageDialog(null,
                    "Produto salvo com sucesso!!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            listProducts(); // Atualiza a lista
        }
    }

//    public void screanListProducts() {
//        jTabbedPaneProduct.setSelectedIndex(0);
//        listProducts();
//    }
//    public void loadCombobox() {
//        List<Supplier> listS = supplierController.get("");
//        jComboBoxSunpplierHistoryInput.removeAllItems();
//        for (Supplier item : listS) {
//            jComboBoxSunpplierHistoryInput.addItem(item);
//        }
//
////        List<ReasonTaxes> listR = reasonTaxeController.get("");
////        jComboBoxReasonTaxeId.removeAllItems();
////        for (ReasonTaxes item : listR) {
////            jComboBoxReasonTaxeId.addItem(item);
////        }
//    }
//    public void loadListProducts(List<Product> list) {
//        DefaultTableModel data = (DefaultTableModel) jTableProducts.getModel();
//        data.setNumRows(0);
//        for (Product c : list) {
//            data.addRow(new Object[]{
//                c.getId(),
//                c.getType(),
//                c.getCode(),
//                //                c.getBarcode(),
//                c.getDescription(),
//                c.getPrice(),
//                //                c.getPurchasePrice(),
//                //                c.getStockTotal(),
//                c.getTaxe(),
//                c.getReasonTaxe(),
//                //                c.getGroupId(),
//                //                c.getSubGroupId(),
//                //                c.getSupplier().getName(),
//                c.getStatus(),
//                c.getGroup()
//            });
//        }
//    }
//
//    public void listProducts() {
//        List<Product> list = productController.listAll();
//        loadListProducts(list);
//    }
//
//    public void filterListProduct(String txt) {
////        ProductDao cDao = new ProductDao();
//        List<Product> list = productController.listForPDV(txt);
//        loadListProducts(list);
//    }
//    public void loadListProductsInventory(List<Product> list) {
//        if (list == null) {
//            list = productController.getForPDV(null);
//        }
//        DefaultTableModel data = (DefaultTableModel) jTableInventory.getModel();
//        data.setNumRows(0);
//        for (Product c : list) {
//            data.addRow(new Object[]{
//                c.getId(),
//                c.getCode(),
//                c.getBarcode(),
//                c.getDescription(),
//                c.getCurrentStock(),
//                c.getPrice(),
//                c.getPurchasePrice(),
//                c.getSupplier().getName(),
//                c.getType()
//            });
//        }
//    }
//    public void listProductsInventory() {
////        List<Product> list = productController.getProducts();
//        List<Product> list = productController.get(null);
//        loadListProductsInventory(list);
//    }
//    public void filterListProductInventory(String txt) {
////        ProductDao cDao = new ProductDao();
//        List<Product> list = productController.get(txt);
//        loadListProductsInventory(list);
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane4 = new javax.swing.JScrollPane();
        jTabbedPaneProduct = new javax.swing.JTabbedPane();
        jPanelSearchSupplier = new javax.swing.JPanel();
        jTextFieldFilterNameTable = new javax.swing.JTextField();
        jButtonAlterSeleted = new javax.swing.JButton();
        jButtonDeleteSelectedTable = new javax.swing.JButton();
        jButtonOpenFormProduct = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableProducts = new javax.swing.JTable();
        jButtonViewSeleted = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButtonGroups = new javax.swing.JButton();

        jTabbedPaneProduct.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPaneProduct.setPreferredSize(new java.awt.Dimension(925, 620));

        jPanelSearchSupplier.setBackground(new java.awt.Color(204, 204, 255));

        jTextFieldFilterNameTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldFilterNameTableActionPerformed(evt);
            }
        });
        jTextFieldFilterNameTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldFilterNameTableKeyReleased(evt);
            }
        });

        jButtonAlterSeleted.setBackground(new java.awt.Color(255, 255, 102));
        jButtonAlterSeleted.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Edit Pencil.png"))); // NOI18N
        jButtonAlterSeleted.setText("Editar");
        jButtonAlterSeleted.setBorderPainted(false);
        jButtonAlterSeleted.setContentAreaFilled(false);
        jButtonAlterSeleted.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonAlterSeleted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlterSeletedActionPerformed(evt);
            }
        });

        jButtonDeleteSelectedTable.setBackground(new java.awt.Color(255, 0, 0));
        jButtonDeleteSelectedTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Trash Can.png"))); // NOI18N
        jButtonDeleteSelectedTable.setText("Excluir");
        jButtonDeleteSelectedTable.setBorderPainted(false);
        jButtonDeleteSelectedTable.setContentAreaFilled(false);
        jButtonDeleteSelectedTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonDeleteSelectedTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteSelectedTableActionPerformed(evt);
            }
        });

        jButtonOpenFormProduct.setBackground(new java.awt.Color(153, 153, 255));
        jButtonOpenFormProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Plus.png"))); // NOI18N
        jButtonOpenFormProduct.setText("Novo Produto");
        jButtonOpenFormProduct.setBorderPainted(false);
        jButtonOpenFormProduct.setContentAreaFilled(false);
        jButtonOpenFormProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonOpenFormProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenFormProductActionPerformed(evt);
            }
        });

        jTableProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Tipo", "Codigo", "Descrição", "Preço", "Imposto", "Razão do Imposto", "Status", "Grupo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
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
        jTableProducts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableProductsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableProducts);

        jButtonViewSeleted.setBackground(new java.awt.Color(102, 255, 102));
        jButtonViewSeleted.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Binoculars.png"))); // NOI18N
        jButtonViewSeleted.setText("Ver");
        jButtonViewSeleted.setToolTipText("Visualizar produto");
        jButtonViewSeleted.setBorderPainted(false);
        jButtonViewSeleted.setContentAreaFilled(false);
        jButtonViewSeleted.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonViewSeleted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewSeletedActionPerformed(evt);
            }
        });

        jLabel2.setText("Procurar Produto");

        jButtonGroups.setBackground(new java.awt.Color(153, 153, 255));
        jButtonGroups.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Puzzle.png"))); // NOI18N
        jButtonGroups.setText("Categoria");
        jButtonGroups.setBorderPainted(false);
        jButtonGroups.setContentAreaFilled(false);
        jButtonGroups.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonGroups.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGroupsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelSearchSupplierLayout = new javax.swing.GroupLayout(jPanelSearchSupplier);
        jPanelSearchSupplier.setLayout(jPanelSearchSupplierLayout);
        jPanelSearchSupplierLayout.setHorizontalGroup(
            jPanelSearchSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSearchSupplierLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSearchSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanelSearchSupplierLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelSearchSupplierLayout.createSequentialGroup()
                        .addComponent(jTextFieldFilterNameTable, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 111, Short.MAX_VALUE)
                        .addComponent(jButtonGroups)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOpenFormProduct)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonViewSeleted)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonAlterSeleted)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDeleteSelectedTable)))
                .addContainerGap())
        );
        jPanelSearchSupplierLayout.setVerticalGroup(
            jPanelSearchSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSearchSupplierLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSearchSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldFilterNameTable, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonOpenFormProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAlterSeleted, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDeleteSelectedTable, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonViewSeleted, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonGroups))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );

        jPanelSearchSupplierLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonGroups, jTextFieldFilterNameTable});

        jTabbedPaneProduct.addTab("Produtos e Serviços", jPanelSearchSupplier);

        jScrollPane4.setViewportView(jTabbedPaneProduct);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 976, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonViewSeletedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewSeletedActionPerformed
        // TODO add your handling code here:

        int id = 0;
        try {
            id = (int) jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 0);
//            System.out.println("jTableUsers id:" + value);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Selecione um Products na tabela!!", "Atencao", JOptionPane.ERROR_MESSAGE);
        } finally {

//            int id = Integer.parseInt(jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 0).toString());
            if (id > 0) {
                Product prod = productController.getById(id);
                JOptionPane.showMessageDialog(null, "Produto :" + prod.getDescription() + "\n Codigo de barra:" + prod.getBarcode() + "\n Preço:" + prod.getPrice() + "\n Status:" + prod.getStatus());

//                JDialogFormProduct formProd = new JDialogFormProduct(null, true);
//                formProd.setProduct(value);
//                formProd.setVisible(true);
//                Boolean resp = formProd.getResponse();
//                if (resp == true) {
//                    JOptionPane.showMessageDialog(null, "FormProduct salvo com sucesso!!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
//                    listProducts();
//                }
            }
        }
    }//GEN-LAST:event_jButtonViewSeletedActionPerformed

    private void jTableProductsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableProductsMouseClicked
        // TODO add your handling code here:
        //        jTabbedPaneProduct.setSelectedIndex(1);
        //        jTextFieldId.setText(jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 0).toString());
        //        jComboBoxType.setSelectedItem(jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 1));
        //        jTextFieldCode.setText(jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 2).toString());
        //        jTextFieldBarCode.setText(jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 3).toString());
        //        jTextFieldDescription.setText(jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 4).toString());
        //        jTextFieldPrice.setText(jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 5).toString());
        //        jTextFieldPurchasePrice.setText(jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 6).toString());
        //        jTextFieldStockTotal.setText(jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 7).toString());
        //        jComboBoxTaxeId.setSelectedItem(jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 8));
        //        jComboBoxReasonTaxeId.setSelectedItem(jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 9));
        //        jComboBoxReasonTaxeId.setSelectedItem(jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 10));
        //        jComboBoxSupplier.setSelectedIndex((int) jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 11));
        //        jComboBoxGroup.setSelectedIndex((int) jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 12));
    }//GEN-LAST:event_jTableProductsMouseClicked

    private void jButtonOpenFormProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenFormProductActionPerformed
        // TODO add your handling code here:
        JDialogFormProduct formProd = new JDialogFormProduct(null, true);
        //        formProd.setFormProduct(prod);
        formProd.setVisible(true);
        Boolean resp = formProd.getResponse();
        if (resp == true) {
            JOptionPane.showMessageDialog(null, "Products salvo com sucesso!!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            listProducts();
        }

        //        jTabbedPaneProduct.setSelectedIndex(1);
        //        Utilities helpUtil = new Utilities();
        //        helpUtil.clearScreen(jPanelFormSupplier);
    }//GEN-LAST:event_jButtonOpenFormProductActionPerformed

    private void jButtonDeleteSelectedTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteSelectedTableActionPerformed
        // TODO add your handling code here:
        int id = 0;
        try {
//             int id = Integer.parseInt(jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 0).toString());
            id = (int) jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 0);
            System.out.println("jTableProducts id:" + id);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Selecione um Products na tabela!!", "Atencao", JOptionPane.ERROR_MESSAGE);
        } finally {

            Product prod = productController.getById(id);
            //        JOptionPane.showMessageDialog(null, "Cliente :" + client.getName());
            int sair = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja Deletar," + prod.getDescription() + "?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (sair == JOptionPane.YES_OPTION) {
                if (productController.delete(id)) {
                    JOptionPane.showMessageDialog(null, "products excluido com Sucesso!!");
                    listProducts();
                }
            }
        }
    }//GEN-LAST:event_jButtonDeleteSelectedTableActionPerformed

    private void jButtonAlterSeletedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlterSeletedActionPerformed
        // TODO add your handling code here:
        editarProdutoSelecionado();
    }//GEN-LAST:event_jButtonAlterSeletedActionPerformed

    private void jTextFieldFilterNameTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldFilterNameTableKeyReleased
        // TODO add your handling code here:
        String txt = jTextFieldFilterNameTable.getText();
        if (!txt.isEmpty()) {
            filterListProduct(txt);
        } else {
            listProducts();
        }
    }//GEN-LAST:event_jTextFieldFilterNameTableKeyReleased

    private void jTextFieldFilterNameTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldFilterNameTableActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldFilterNameTableActionPerformed

    private void jButtonGroupsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGroupsActionPerformed
        // TODO add your handling code here:
        JDialogCategoryProduct jGroup = new JDialogCategoryProduct(null, true);
        jGroup.setVisible(true);
    }//GEN-LAST:event_jButtonGroupsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAlterSeleted;
    private javax.swing.JButton jButtonDeleteSelectedTable;
    private javax.swing.JButton jButtonGroups;
    private javax.swing.JButton jButtonOpenFormProduct;
    private javax.swing.JButton jButtonViewSeleted;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanelSearchSupplier;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPaneProduct;
    private javax.swing.JTable jTableProducts;
    private javax.swing.JTextField jTextFieldFilterNameTable;
    // End of variables declaration//GEN-END:variables
}
