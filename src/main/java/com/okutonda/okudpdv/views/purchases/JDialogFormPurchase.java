/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.okutonda.okudpdv.views.purchases;

import com.okutonda.okudpdv.controllers.ProductController;
import com.okutonda.okudpdv.controllers.PurchaseController;
import com.okutonda.okudpdv.controllers.SupplierController;
import com.okutonda.okudpdv.controllers.WarehouseController;
import com.okutonda.okudpdv.data.entities.DocumentType;
import com.okutonda.okudpdv.data.entities.PaymentMode;
import com.okutonda.okudpdv.data.entities.PaymentStatus;
import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.Purchase;
import com.okutonda.okudpdv.data.entities.PurchaseItem;
import com.okutonda.okudpdv.data.entities.PurchasePayment;
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
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;

/**
 *
 * @author rog
 */
public class JDialogFormPurchase extends javax.swing.JDialog {

    private List<PurchaseItem> itensCompra = new ArrayList<>();
    private DefaultTableModel tableModelItensCompra;
    private DefaultTableModel tableModelListaProdutos;
    private TableRowSorter<DefaultTableModel> rowSorter;

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
        inicializarComponentes();
        carregarDadosIniciais();
    }

    public Boolean getResponse() {
        return response;
    }

    /**
     * Inicializa componentes da interface
     */
    private void inicializarComponentes() {
        // Inicializar tabelas
        inicializarTabelaItensCompra();
        inicializarTabelaListaProdutos();

        // Configurar datas
        configurarDatasPadrao();

        // Carregar combobox
        carregarCombobox();

        // Configurar filtro de produtos
        configurarFiltroProdutos();
    }

    /**
     * Inicializa a tabela de itens da compra
     */
    private void inicializarTabelaItensCompra() {
        String[] colunas = {"ID", "Produto", "Qtd", "Preço", "Subtotal"};
        tableModelItensCompra = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return Integer.class;
                    case 1:
                        return String.class;
                    case 2:
                        return Integer.class;
                    case 3:
                        return BigDecimal.class;
                    case 4:
                        return BigDecimal.class;
                    default:
                        return Object.class;
                }
            }
        };

        jTablePurchaseItems.setModel(tableModelItensCompra);
    }

    /**
     * Inicializa a tabela de lista de produtos
     */
    private void inicializarTabelaListaProdutos() {
        String[] colunas = {"Código", "Descrição", "Preço"};
        tableModelListaProdutos = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        jTableListProd.setModel(tableModelListaProdutos);
    }

    /**
     * Configura filtro para a tabela de produtos
     */
    private void configurarFiltroProdutos() {
        rowSorter = new TableRowSorter<>(tableModelListaProdutos);
        jTableListProd.setRowSorter(rowSorter);
    }

    /**
     * Filtra produtos na tabela
     */
    private void filtrarProdutos() {
        String textoFiltro = jTextFieldFilterListProdu.getText().trim();

        if (textoFiltro.length() == 0) {
            rowSorter.setRowFilter(null);
        } else {
            try {
                RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("(?i)" + textoFiltro, 0, 1);
                rowSorter.setRowFilter(rf);
            } catch (Exception e) {
                System.err.println("Erro ao filtrar produtos: " + e.getMessage());
            }
        }
    }

    /**
     * Configura datas padrão - CORRIGIDO
     */
    private void configurarDatasPadrao() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            // Data de compra = hoje
            Date hoje = new Date();
            jFormattedTextFieldDataCompra.setText(sdf.format(hoje));

            // Vencimento = hoje + 30 dias
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(hoje);
            cal.add(java.util.Calendar.DAY_OF_MONTH, 30);
            Date vencimento = cal.getTime();
            jFormattedTextFieldDataVencimento.setText(sdf.format(vencimento));

        } catch (Exception e) {
            System.err.println("Erro ao configurar datas: " + e.getMessage());
        }
    }

    /**
     * Carrega combobox com dados
     */
    private void carregarCombobox() {
        try {
            // Carregar fornecedores
            carregarFornecedores();

            // Carregar produtos na tabela
            carregarProdutosNaTabela();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar dados: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carrega dados iniciais
     */
    private void carregarDadosIniciais() {
        // Nada específico por enquanto
    }

    /**
     * Carrega lista de fornecedores
     */
    private void carregarFornecedores() {
        try {
            jComboBoxListFornecedor.removeAllItems();
            List<Supplier> fornecedores = supplierController.listarTodos();

            for (Supplier fornecedor : fornecedores) {
                jComboBoxListFornecedor.addItem(fornecedor);
            }

            if (jComboBoxListFornecedor.getItemCount() > 0) {
                jComboBoxListFornecedor.setSelectedIndex(0);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar fornecedores: " + e.getMessage());
        }
    }

    /**
     * Carrega produtos na tabela de lista
     */
    private void carregarProdutosNaTabela() {
        try {
            tableModelListaProdutos.setRowCount(0);
            List<Product> produtos = productController.listProducts();

            for (Product produto : produtos) {
                tableModelListaProdutos.addRow(new Object[]{
                    produto.getCode(),
                    produto.getDescription(),
                    produto.getPrice()
                });
            }

        } catch (Exception e) {
            System.err.println("Erro ao carregar produtos: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar produtos: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Preenche campos do produto quando selecionado na tabela
     */
    private void preencherCamposProdutoSelecionado() {
        int selectedRow = jTableListProd.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        try {
            int modelRow = jTableListProd.convertRowIndexToModel(selectedRow);

            String codigo = (String) tableModelListaProdutos.getValueAt(modelRow, 0);
            String descricao = (String) tableModelListaProdutos.getValueAt(modelRow, 1);
            BigDecimal preco = (BigDecimal) tableModelListaProdutos.getValueAt(modelRow, 2);

            jTextFieldCodeProd.setText(codigo);
            jTextFieldNameProdSeletec.setText(descricao);

            // Focar no campo de quantidade
            jTextFieldQtdProduct.requestFocus();
            jTextFieldQtdProduct.selectAll();

        } catch (Exception e) {
            System.err.println("Erro ao preencher campos do produto: " + e.getMessage());
        }
    }

    /**
     * Seleciona produto da tabela
     */
    private void selecionarProduto() {
        int selectedRow = jTableListProd.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um produto da lista primeiro",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        preencherCamposProdutoSelecionado();
    }

    /**
     * Adiciona produto selecionado à compra
     */
    private void adicionarProduto() {
        try {
            // Verificar se há produto selecionado
            if (jTextFieldCodeProd.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Selecione um produto da lista primeiro",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Obter código do produto selecionado
            String codigoProduto = jTextFieldCodeProd.getText().trim();
            Product produto = productController.getByCode(codigoProduto);

            if (produto == null) {
                JOptionPane.showMessageDialog(this,
                        "Produto não encontrado: " + codigoProduto,
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obter quantidade
            int quantidade = 1;
            try {
                String qtdTexto = jTextFieldQtdProduct.getText().trim();
                if (!qtdTexto.isEmpty()) {
                    quantidade = Integer.parseInt(qtdTexto);
                    if (quantidade <= 0) {
                        JOptionPane.showMessageDialog(this,
                                "Quantidade deve ser maior que zero",
                                "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Quantidade inválida. Use um número inteiro.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar se já existe na compra
            PurchaseItem itemExistente = encontrarItemNaCompra(produto.getId());

            if (itemExistente != null) {
                // Produto já existe - incrementar quantidade
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Produto '" + produto.getDescription() + "' já está na compra.\n"
                        + "Deseja incrementar a quantidade em " + quantidade + " unidades?",
                        "Produto Duplicado",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    int novaQuantidade = itemExistente.getQuantidade() + quantidade;
                    itemExistente.setQuantidade(novaQuantidade);
                    itemExistente.setSubtotal(itemExistente.getPrecoCusto().multiply(BigDecimal.valueOf(novaQuantidade)));

                    atualizarTabelaItensCompra();
                    atualizarTotais();
                    limparCamposProduto();

                    JOptionPane.showMessageDialog(this,
                            "Quantidade atualizada com sucesso! Nova quantidade: " + novaQuantidade,
                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    return;
                } else {
                    return;
                }
            }

            // Produto novo - adicionar à compra
            PurchaseItem item = new PurchaseItem();
            item.setProduct(produto);
            item.setQuantidade(quantidade);
            item.setPrecoCusto(produto.getPrice());

            // Calcular subtotal
            BigDecimal subtotal = produto.getPrice().multiply(BigDecimal.valueOf(quantidade));
            item.setSubtotal(subtotal);

            // Adicionar à lista
            itensCompra.add(item);

            // Atualizar interface
            atualizarTabelaItensCompra();
            atualizarTotais();
            limparCamposProduto();

            JOptionPane.showMessageDialog(this,
                    "Produto adicionado com sucesso! Quantidade: " + quantidade,
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao adicionar produto: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Remove produto selecionado da compra
     */
    private void removerProduto() {
        int selectedRow = jTablePurchaseItems.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um item para remover",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int modelRow = jTablePurchaseItems.convertRowIndexToModel(selectedRow);
            Integer productId = (Integer) tableModelItensCompra.getValueAt(modelRow, 0);

            // Remover da lista
            itensCompra.removeIf(item -> item.getProduct().getId().equals(productId));

            // Atualizar tabela e totais
            atualizarTabelaItensCompra();
            atualizarTotais();

            JOptionPane.showMessageDialog(this,
                    "Item removido com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao remover item: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Atualiza a tabela de itens da compra
     */
    private void atualizarTabelaItensCompra() {
        tableModelItensCompra.setRowCount(0);

        for (PurchaseItem item : itensCompra) {
            tableModelItensCompra.addRow(new Object[]{
                item.getProduct().getId(),
                item.getProduct().getDescription(),
                item.getQuantidade(),
                item.getPrecoCusto(),
                item.getSubtotal()
            });
        }
    }

    /**
     * Calcula totais da compra
     */
    private void atualizarTotais() {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal ivaTotal = BigDecimal.ZERO;

        for (PurchaseItem item : itensCompra) {
            if (item.getSubtotal() != null) {
                subtotal = subtotal.add(item.getSubtotal());
            }
            if (item.getIva() != null) {
                ivaTotal = ivaTotal.add(item.getIva());
            }
        }

        BigDecimal total = subtotal.add(ivaTotal);

        // Atualizar campos
        jTextFieldTotal.setText(total.toString());
        jTextFieldIvaTotal.setText(ivaTotal.toString());
    }

    /**
     * Finaliza a compra com opção de pagamento - CORRIGIDO
     */
    /**
 * Finaliza a compra com opção de pagamento - CORRIGIDO
 */
private void finalizarCompra(boolean abrirPagamento) {
    try {
        // Validar dados básicos
        if (!validarDadosCompra()) {
            return;
        }

        // 🔹 VALIDAR INVOICE NUMBER (se preenchido)
        String invoiceNumber = jTextFieldInvoiceNumber.getText().trim();
        if (!invoiceNumber.isEmpty()) {
            // Verificar se já existe (opcional - pode remover se quiser validar apenas no service)
            PurchaseController purchaseController = new PurchaseController();
            // Pode adicionar validação aqui se quiser
        }

        // Configurar compra
        Purchase purchase = configurarCompra();

        if (abrirPagamento) {
            // Abre diálogo de pagamento
            JDialogPaymentPurchase jdPayment = new JDialogPaymentPurchase(null, true);
            jdPayment.setPurchase(purchase);
            jdPayment.setVisible(true);

            // Obtém a resposta
            Purchase compraSalva = jdPayment.getResponse();

            if (compraSalva != null) {
                response = true;
                JOptionPane.showMessageDialog(this,
                        "Compra registrada com sucesso!\nNúmero: " + compraSalva.getInvoiceNumber(),
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }
        } else {
            // Salvar compra sem pagamento
            PurchaseController purchaseController = new PurchaseController();
            Purchase compraSalva = purchaseController.criarCompra(purchase);

            if (compraSalva != null && compraSalva.getId() != null) {
                response = true;
                JOptionPane.showMessageDialog(this,
                        "Compra registrada com sucesso!\nNúmero: " + compraSalva.getInvoiceNumber(),
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar compra",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "Erro ao finalizar compra: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    /**
     * Valida dados da compra - CORRIGIDO
     */
    private boolean validarDadosCompra() {
        // Verificar fornecedor
        if (jComboBoxListFornecedor.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Verificar produtos
        if (itensCompra.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione pelo menos um produto",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Verificar datas
        if (jFormattedTextFieldDataCompra.getText().trim().isEmpty()
                || jFormattedTextFieldDataVencimento.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha as datas",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Configura os dados da compra - CORRIGIDO
     */
   /**
 * Configura os dados da compra - CORRIGIDO com invoiceNumber
 */
private Purchase configurarCompra() {
    Purchase purchase = new Purchase();

    try {
        // 🔹 INVOICE NUMBER DA INTERFACE
        String invoiceNumber = jTextFieldInvoiceNumber.getText().trim();
        if (!invoiceNumber.isEmpty()) {
            purchase.setInvoiceNumber(invoiceNumber);
        }
        // Se estiver vazio, o service vai gerar automaticamente
        
        purchase.setInvoiceType(DocumentType.FT);
        
        // Fornecedor
        Supplier fornecedor = (Supplier) jComboBoxListFornecedor.getSelectedItem();
        purchase.setSupplier(fornecedor);

        // Datas - CORREÇÃO: Converter String para java.sql.Date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date dataCompraUtil = sdf.parse(jFormattedTextFieldDataCompra.getText());
        java.sql.Date dataCompra = new java.sql.Date(dataCompraUtil.getTime());
        purchase.setDataCompra(dataCompra);

        Date dataVencUtil = sdf.parse(jFormattedTextFieldDataVencimento.getText());
        java.sql.Date dataVenc = new java.sql.Date(dataVencUtil.getTime());
        purchase.setDataVencimento(dataVenc);

        // Itens
        for (PurchaseItem item : itensCompra) {
            item.setPurchase(purchase);
        }
        purchase.setItems(new ArrayList<>(itensCompra));

        // Totais
        try {
            purchase.setTotal(new BigDecimal(jTextFieldTotal.getText()));
            purchase.setIvaTotal(new BigDecimal(jTextFieldIvaTotal.getText()));
        } catch (NumberFormatException e) {
            purchase.setTotal(BigDecimal.ZERO);
            purchase.setIvaTotal(BigDecimal.ZERO);
        }

        // Observações
        purchase.setDescricao(jTextAreaNote.getText());

        // Status inicial
        purchase.setStockStatus(StockStatus.PENDENTE);
        purchase.setPaymentStatus(PaymentStatus.PENDENTE);

    } catch (Exception e) {
        System.err.println("Erro ao configurar compra: " + e.getMessage());
        throw new RuntimeException("Erro ao configurar compra: " + e.getMessage(), e);
    }

    return purchase;
}

    // ==========================================================
    // 🔹 MÉTODOS AUXILIARES
    // ==========================================================
    private void limparCamposProduto() {
        jTextFieldCodeProd.setText("");
        jTextFieldNameProdSeletec.setText("");
        jTextFieldQtdProduct.setText("1");
    }

    private PurchaseItem encontrarItemNaCompra(Integer productId) {
        for (PurchaseItem item : itensCompra) {
            if (item.getProduct().getId().equals(productId)) {
                return item;
            }
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
        jButtonAddProduct = new javax.swing.JButton();
        jFormattedTextFieldDataFabrico = new javax.swing.JFormattedTextField();
        jFormattedTextFieldDataExpiracao = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableListProd = new javax.swing.JTable();
        jTextFieldFilterListProdu = new javax.swing.JTextField();
        jButtonAddProdSeleted = new javax.swing.JButton();
        jButtonRemoveProdPurchaseItem = new javax.swing.JButton();
        jTextFieldNameProdSeletec = new javax.swing.JTextField();
        jTextFieldCodeProd = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jButtonFinishAndPay = new javax.swing.JButton();
        jTextFieldInvoiceNumber = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabelTitle.setText("Formulario de Compra");

        try {
            jFormattedTextFieldDataVencimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/20##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel6.setText("data vencimento");

        jLabel7.setText("Data compra");

        try {
            jFormattedTextFieldDataCompra.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/20##")));
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

        jButtonAddProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Plus.png"))); // NOI18N
        jButtonAddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddProductActionPerformed(evt);
            }
        });

        try {
            jFormattedTextFieldDataFabrico.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/20##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jFormattedTextFieldDataExpiracao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/20##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel10.setText("Data de Expiracao");

        jLabel4.setText("Data de Fabrico");

        jTableListProd.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableListProd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableListProdMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableListProd);

        jTextFieldFilterListProdu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldFilterListProduKeyReleased(evt);
            }
        });

        jButtonAddProdSeleted.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Plus.png"))); // NOI18N
        jButtonAddProdSeleted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddProdSeletedActionPerformed(evt);
            }
        });

        jButtonRemoveProdPurchaseItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Trash Can.png"))); // NOI18N
        jButtonRemoveProdPurchaseItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveProdPurchaseItemActionPerformed(evt);
            }
        });

        jTextFieldNameProdSeletec.setEditable(false);

        jTextFieldCodeProd.setEditable(false);

        jLabel11.setText("Procurar produto");

        jButtonFinishAndPay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Done.png"))); // NOI18N
        jButtonFinishAndPay.setText("Finalizar e Pagar");
        jButtonFinishAndPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFinishAndPayActionPerformed(evt);
            }
        });

        jLabel12.setText("Invoice Number");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(50, 50, 50))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jComboBoxListFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jFormattedTextFieldDataCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel1)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(9, 9, 9)
                                        .addComponent(jLabelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(40, 40, 40)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jFormattedTextFieldDataVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 42, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldIvaTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldInvoiceNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12))
                                .addContainerGap())))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jTextFieldFilterListProdu, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButtonAddProdSeleted, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel2)
                                            .addGap(211, 211, 211))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(jTextFieldCodeProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTextFieldNameProdSeletec)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel8)
                                        .addComponent(jTextFieldQtdProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jLabel11)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButtonRemoveProdPurchaseItem))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(0, 31, Short.MAX_VALUE))
                                    .addComponent(jFormattedTextFieldDataFabrico, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel10)
                                    .addComponent(jFormattedTextFieldDataExpiracao, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(193, 193, 193))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButtonFinishAndPay, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextFieldTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButtonFinishPurchase, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTitle)
                    .addComponent(jLabel12))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldInvoiceNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9))
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldCodeProd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jFormattedTextFieldDataFabrico, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jFormattedTextFieldDataExpiracao, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jButtonAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldNameProdSeletec)
                                    .addComponent(jTextFieldQtdProduct, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonAddProdSeleted, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldFilterListProdu)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel5)
                        .addComponent(jButtonRemoveProdPurchaseItem)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jTextFieldIvaTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonFinishPurchase)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonFinishAndPay, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(9, Short.MAX_VALUE))
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
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(916, 477));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jFormattedTextFieldDataCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextFieldDataCompraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextFieldDataCompraActionPerformed

    private void jButtonAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddProductActionPerformed
        // TODO add your handling code here:
        adicionarProduto(); // Agora chama o método consolidado
    }//GEN-LAST:event_jButtonAddProductActionPerformed

    private void jButtonFinishPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFinishPurchaseActionPerformed
        // TODO add your handling code here:
        finalizarCompra(false);
    }//GEN-LAST:event_jButtonFinishPurchaseActionPerformed

    private void jTextFieldFilterListProduKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldFilterListProduKeyReleased
        // TODO add your handling code here:
        filtrarProdutos();
    }//GEN-LAST:event_jTextFieldFilterListProduKeyReleased

    private void jTableListProdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableListProdMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            preencherCamposProdutoSelecionado();
        }
    }//GEN-LAST:event_jTableListProdMouseClicked

    private void jButtonAddProdSeletedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddProdSeletedActionPerformed
        // TODO add your handling code here:
        selecionarProduto();
    }//GEN-LAST:event_jButtonAddProdSeletedActionPerformed

    private void jButtonRemoveProdPurchaseItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveProdPurchaseItemActionPerformed
        // TODO add your handling code here:
        removerProduto();
    }//GEN-LAST:event_jButtonRemoveProdPurchaseItemActionPerformed

    private void jButtonFinishAndPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFinishAndPayActionPerformed
        // TODO add your handling code here:
        finalizarCompra(true);
    }//GEN-LAST:event_jButtonFinishAndPayActionPerformed

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
    private javax.swing.JButton jButtonAddProdSeleted;
    private javax.swing.JButton jButtonAddProduct;
    private javax.swing.JButton jButtonFinishAndPay;
    private javax.swing.JButton jButtonFinishPurchase;
    private javax.swing.JButton jButtonRemoveProdPurchaseItem;
    private javax.swing.JComboBox jComboBoxListFornecedor;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataCompra;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataExpiracao;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataFabrico;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataVencimento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTableListProd;
    private javax.swing.JTable jTablePurchaseItems;
    private javax.swing.JTextArea jTextAreaNote;
    private javax.swing.JTextField jTextFieldCodeProd;
    private javax.swing.JTextField jTextFieldFilterListProdu;
    private javax.swing.JTextField jTextFieldInvoiceNumber;
    private javax.swing.JTextField jTextFieldIvaTotal;
    private javax.swing.JTextField jTextFieldNameProdSeletec;
    private javax.swing.JTextField jTextFieldQtdProduct;
    private javax.swing.JTextField jTextFieldTotal;
    // End of variables declaration//GEN-END:variables
}
