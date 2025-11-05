/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.okutonda.okudpdv.views.stock;

import com.okutonda.okudpdv.controllers.ProductController;
import com.okutonda.okudpdv.controllers.StockMovementController;
import com.okutonda.okudpdv.controllers.SupplierController;
import com.okutonda.okudpdv.controllers.WarehouseController;
import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.StockMovement;
import com.okutonda.okudpdv.data.entities.Supplier;
import com.okutonda.okudpdv.data.entities.Warehouse;
import com.okutonda.okudpdv.dtos.ProductStockReport;
import com.okutonda.okudpdv.services.StockReportService;
import com.okutonda.okudpdv.views.products.JDialogFormProduct;
import com.okutonda.okudpdv.views.warehouse.JDialogWarehouse;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kenny
 */
public final class JPanelStockMovement extends javax.swing.JPanel {

    ProductController productController = new ProductController();
    SupplierController supplierController = new SupplierController();
    StockMovementController stockMovementController = new StockMovementController();
    WarehouseController warehouseController = new WarehouseController();
    StockReportService stockReportService = new StockReportService(); // ✅ NOVO SERVICE

//    ProductController productController = new ProductController();
//    SupplierController supplierController = new SupplierController();
//    StockMovementController stockMovementController = new StockMovementController();
//    WarehouseController warehouseController = new WarehouseController();
    /**
     * Creates new form JPanelStock
     */
    public JPanelStockMovement() {
        initComponents();

        // Inicializar todas as tabelas e componentes
        System.out.println("testeteste");
        inicializarComponentes();
        configurarListeners();

//        loadCombobox();
    }

    /**
     * Função principal que inicializa todas as tabelas e componentes Esta
     * função deve ser chamada no construtor
     */
    private void inicializarComponentes() {
        // Inicializar todas as tabelas
        inicializarTabelaStockProdutos();
        inicializarTabelaMovimentosStock();
        inicializarTabelaAlertasStock();

        // Carregar dados iniciais
        carregarDadosIniciais();
        // Configurar comboboxes
        carregarComboboxes();
    }

    /**
     * Funções de inicialização para construção das tabelas Cada função é
     * totalmente responsável pela construção da sua tabela
     */
// ==========================================================
// 🔹 INICIALIZAÇÃO DA TABELA DE STOCK DE PRODUTOS
// ==========================================================
    private void inicializarTabelaStockProdutos() {
        jTableStockProducts.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID", "Código", "Código Barras", "Produto", "Stock Atual",
                    "Stock Mínimo", "Preço Venda", "Preço Compra", "Grupo", "Tipo",
                    "Total Entradas", "Total Saídas", "Status", "Última Movimentação"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class,
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class,
                java.lang.Double.class, java.lang.Double.class, java.lang.String.class,
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class,
                java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false, false, false,
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        // Configurar largura das colunas
        jTableStockProducts.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        jTableStockProducts.getColumnModel().getColumn(1).setPreferredWidth(80);   // Código
        jTableStockProducts.getColumnModel().getColumn(2).setPreferredWidth(120);  // Código Barras
        jTableStockProducts.getColumnModel().getColumn(3).setPreferredWidth(200);  // Produto
        jTableStockProducts.getColumnModel().getColumn(4).setPreferredWidth(80);   // Stock Atual
        jTableStockProducts.getColumnModel().getColumn(5).setPreferredWidth(80);   // Stock Mínimo
        jTableStockProducts.getColumnModel().getColumn(6).setPreferredWidth(90);   // Preço Venda
        jTableStockProducts.getColumnModel().getColumn(7).setPreferredWidth(90);   // Preço Compra
        jTableStockProducts.getColumnModel().getColumn(8).setPreferredWidth(120);  // Grupo
        jTableStockProducts.getColumnModel().getColumn(9).setPreferredWidth(80);   // Tipo
        jTableStockProducts.getColumnModel().getColumn(10).setPreferredWidth(90);  // Total Entradas
        jTableStockProducts.getColumnModel().getColumn(11).setPreferredWidth(90);  // Total Saídas
        jTableStockProducts.getColumnModel().getColumn(12).setPreferredWidth(50); // Status
        jTableStockProducts.getColumnModel().getColumn(13).setPreferredWidth(120); // Última Movimentação

        // Configurar renderizador para status (opcional)
//        jTableStockProducts.getColumnModel().getColumn(12).setCellRenderer(new DefaultTableCellRenderer() {
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

// ==========================================================
// 🔹 INICIALIZAÇÃO DA TABELA DE MOVIMENTOS DE STOCK
// ==========================================================
    private void inicializarTabelaMovimentosStock() {
        jTableStockMovement.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID", "Produto", "Quantidade", "Tipo", "Origem", "Motivo", "Usuário", "Data/Hora"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class,
                java.lang.String.class, java.lang.String.class, java.lang.String.class,
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        // Configurar largura das colunas
        jTableStockMovement.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        jTableStockMovement.getColumnModel().getColumn(1).setPreferredWidth(200);  // Produto
        jTableStockMovement.getColumnModel().getColumn(2).setPreferredWidth(80);   // Quantidade
        jTableStockMovement.getColumnModel().getColumn(3).setPreferredWidth(80);   // Tipo
        jTableStockMovement.getColumnModel().getColumn(4).setPreferredWidth(100);  // Origem
        jTableStockMovement.getColumnModel().getColumn(5).setPreferredWidth(150);  // Motivo
        jTableStockMovement.getColumnModel().getColumn(6).setPreferredWidth(120);  // Usuário
        jTableStockMovement.getColumnModel().getColumn(7).setPreferredWidth(120);  // Data/Hora
    }

// ==========================================================
// 🔹 INICIALIZAÇÃO DA TABELA DE ALERTAS DE STOCK MÍNIMO
// ==========================================================
    private void inicializarTabelaAlertasStock() {
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID", "Produto", "Stock Atual", "Stock Mínimo", "Diferença",
                    "Status", "Última Movimentação"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class,
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        // Configurar largura das colunas
        jTable3.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        jTable3.getColumnModel().getColumn(1).setPreferredWidth(200);  // Produto
        jTable3.getColumnModel().getColumn(2).setPreferredWidth(80);   // Stock Atual
        jTable3.getColumnModel().getColumn(3).setPreferredWidth(80);   // Stock Mínimo
        jTable3.getColumnModel().getColumn(4).setPreferredWidth(80);   // Diferença
        jTable3.getColumnModel().getColumn(5).setPreferredWidth(100);  // Status
        jTable3.getColumnModel().getColumn(6).setPreferredWidth(120);  // Última Movimentação
    }

    /**
     * Carrega os dados iniciais nas tabelas
     */
    private void carregarDadosIniciais() {
        // Carregar stock de produtos
        carregarStockProdutos();

        // Carregar movimentos de stock
        carregarMovimentosStock();

        // Carregar transferências (se houver dados)
        carregarTransferencias();

        // Carregar alertas de stock mínimo
        carregarAlertasStockMinimo();
    }

    /**
     * Configura listeners para eventos
     */
    private void configurarListeners() {
        // Exemplo: Double-click para editar produto
        jTableStockProducts.addMouseListener(new java.awt.event.MouseAdapter() {
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
        int selectedRow = jTableStockProducts.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(null,
                    "Selecione um produto na tabela!!",
                    "Atenção",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Integer productId = (Integer) jTableStockProducts.getValueAt(selectedRow, 0);
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
        carregarDadosIniciais();
    }

    /**
     * Carrega os comboboxes com dados
     */
    private void carregarComboboxes() {
        carregarComboboxFornecedores();
        carregarComboboxArmazens();
    }

// ==========================================================
// 🔹 FUNÇÕES ESPECÍFICAS DE CARREGAMENTO
// ==========================================================
    private void carregarStockProdutos() {
        StockMovementController stockController = new StockMovementController();

        // Carrega relatório completo
        List<ProductStockReport> relatorios = stockController.getRelatorioStockCompletoReport();

        System.out.println("repo" + relatorios);
        System.out.println("Relatórios carregados: " + relatorios.size());

        // Debug para ver os dados
        for (ProductStockReport relatorio : relatorios) {
            System.out.println("Produto: " + relatorio.getDescription()
                    + ", Stock: " + relatorio.getCurrentStock()
                    + ", Status: " + relatorio.getStatusStock());
        }

        carregarDadosTabelaStockProdutos(relatorios);
    }

    private void carregarMovimentosStock() {
        List<StockMovement> movimentos = stockMovementController.listarTodos();
        carregarDadosTabelaMovimentosStock(movimentos);
    }

    private void carregarTransferencias() {
        // Implementar quando tiver controller de transferências
        // List<Transferencia> transferencias = transferenciaController.listarTodos();
        // carregarDadosTabelaTransferencias(transferencias);
    }

    private void carregarAlertasStockMinimo() {
        StockMovementController stockController = new StockMovementController();
        List<ProductStockReport> produtosComAlerta = stockController.getProdutosComStockBaixo();
        carregarDadosTabelaAlertasStock(produtosComAlerta);
    }

    public void carregarDadosTabelaAlertasStock(List<ProductStockReport> relatorios) {
        DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
        model.setRowCount(0); // Limpar tabela

        for (ProductStockReport relatorio : relatorios) {
            int stockAtual = relatorio.getCurrentStock() != null ? relatorio.getCurrentStock() : 0;
            int stockMinimo = relatorio.getMinStock() != null ? relatorio.getMinStock() : 0;
            int diferenca = stockAtual - stockMinimo;
            String status = diferenca <= 0 ? "CRÍTICO" : "ATENÇÃO";

            // Formatar data da última movimentação
            String ultimaAtualizacao = "N/A";
            if (relatorio.getUltimaMovimentacao() != null) {
                ultimaAtualizacao = relatorio.getUltimaMovimentacao().toString();
                // Ou formatação mais bonita: 
                // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                // ultimaAtualizacao = relatorio.getUltimaMovimentacao().format(formatter);
            }

            model.addRow(new Object[]{
                relatorio.getProductId(),
                relatorio.getDescription(),
                stockAtual,
                stockMinimo,
                diferenca,
                status,
                ultimaAtualizacao
            });
        }

        // Atualizar contador de alertas
        atualizarContadorAlertas(relatorios.size());
    }

    private void atualizarContadorAlertas(int totalAlertas) {
        // Se você tiver um label para mostrar o total de alertas
        // lblTotalAlertas.setText("Alertas: " + totalAlertas);

        if (totalAlertas > 0) {
            System.out.println("⚠️ " + totalAlertas + " produtos com stock baixo/crítico");
        } else {
            System.out.println("✅ Nenhum alerta de stock");
        }
    }

    private void carregarComboboxFornecedores() {
        List<Supplier> fornecedores = supplierController.listarTodos();
        jComboBoxSunpplierHistoryInput.removeAllItems();
        for (Supplier fornecedor : fornecedores) {
            jComboBoxSunpplierHistoryInput.addItem(fornecedor);
        }
    }

    private void carregarComboboxArmazens() {
        List<Warehouse> armazens = warehouseController.listarTodos();
        jComboBoxWarehauseTela1.removeAllItems();
        for (Warehouse armazem : armazens) {
            jComboBoxWarehauseTela1.addItem(armazem);
        }
    }
    // ==========================================================
// 🔹 FUNÇÕES PARA CARREGAR DADOS NAS TABELAS
// ==========================================================

    /**
     * Carrega dados na tabela de stock de produtos
     */
    public void carregarDadosTabelaStockProdutos(List<ProductStockReport> relatorios) {
        DefaultTableModel model = (DefaultTableModel) jTableStockProducts.getModel();
        model.setRowCount(0); // Limpar tabela

        System.out.println("teste1");

        for (ProductStockReport relatorio : relatorios) {
//            System.out.println("Stock: " + relatorio.getCurrentStock());

            model.addRow(new Object[]{
                relatorio.getProductId(), // ID do produto
                relatorio.getCode(), // Código
                relatorio.getBarcode(), // Código de barras
                relatorio.getDescription(), // Descrição
                relatorio.getCurrentStock(), // Stock atual ← CORRIGIDO!
                relatorio.getMinStock(), // Stock mínimo
                relatorio.getPrice(), // Preço de venda
                relatorio.getPurchasePrice(), // Preço de compra
                relatorio.getGroupName(), // Nome do grupo
                relatorio.getProductType(), // Tipo do produto
                relatorio.getTotalEntradas(), // Total de entradas
                relatorio.getTotalSaidas(), // Total de saídas
                relatorio.getStatus(),// Status
                relatorio.getUltimaMovimentacao() // Última movimentação
            });
        }
    }
//    public void carregarDadosTabelaStockProdutos(List<Product> produtos) {
//        DefaultTableModel model = (DefaultTableModel) jTableStockProducts.getModel();
//        model.setRowCount(0); // Limpar tabela
//
//        for (Product produto : produtos) {
//            System.out.println("f1:" + produto.getCurrentStock());
//            model.addRow(new Object[]{
//                produto.getId(),
//                produto.getCode(),
//                produto.getBarcode(),
//                produto.getDescription(),
//                produto.getCurrentStock(),
//                produto.getMinStock(),
//                produto.getPrice(),
//                produto.getPurchasePrice(),
//                (produto.getGroup() != null ? produto.getGroup().getName() : ""),
//                produto.getType()
//            });
//        }
//    }

    /**
     * Carrega dados na tabela de movimentos de stock
     */
    public void carregarDadosTabelaMovimentosStock(List<StockMovement> movimentos) {
        DefaultTableModel model = (DefaultTableModel) jTableStockMovement.getModel();
        model.setRowCount(0); // Limpar tabela

        for (StockMovement movimento : movimentos) {
            model.addRow(new Object[]{
                movimento.getId(),
                (movimento.getProduct() != null ? movimento.getProduct().getDescription() : ""),
                movimento.getQuantity(),
                movimento.getType(),
                movimento.getOrigin(),
                movimento.getReason(),
                (movimento.getUser() != null ? movimento.getUser().getName() : ""),
                (movimento.getCreatedAt() != null ? movimento.getCreatedAt().toString() : "")
            });
        }
    }

    /**
     * Carrega dados na tabela de alertas de stock mínimo
     */
//    public void carregarDadosTabelaAlertasStock(List<Product> produtos) {
//        DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
//        model.setRowCount(0); // Limpar tabela
//
//        for (Product produto : produtos) {
//            int stockAtual = produto.getCurrentStock() != null ? produto.getCurrentStock() : 0;
//            int stockMinimo = produto.getMinStock() != null ? produto.getMinStock() : 0;
//            int diferenca = stockAtual - stockMinimo;
//            String status = diferenca <= 0 ? "CRÍTICO" : "ATENÇÃO";
//
//            model.addRow(new Object[]{
//                produto.getId(),
//                produto.getDescription(),
//                stockAtual,
//                stockMinimo,
//                diferenca,
//                status,
//                "N/A" // Você pode adicionar campo de última atualização se existir
//            });
//        }
//    }
    /**
     * Atualiza todas as tabelas relacionadas a stock após uma operação Executa
     * em background para não travar a interface
     */
    private void atualizarTodasTabelasStock() {
        // Mostrar indicador de carregamento se necessário
        // setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Atualizar todas as tabelas
                carregarStockProdutos();
                carregarMovimentosStock();
                carregarAlertasStockMinimo();
                return null;
            }

            @Override
            protected void done() {
                // Restaurar cursor normal
                // setCursor(Cursor.getDefaultCursor());

                // Opcional: Mostrar mensagem de atualização concluída
                System.out.println("✅ Todas as tabelas de stock atualizadas");
            }
        }.execute();
    }

    /**
     * Função para pesquisar produtos com filtros avançados
     */
    private void pesquisarProdutosComFiltros() {
        try {
            String texto = jTextFieldTxtSearchInventory.getText();
            Supplier fornecedor = (Supplier) jComboBoxSunpplierHistoryInput.getSelectedItem();
            Warehouse armazem = (Warehouse) jComboBoxWarehauseTela1.getSelectedItem();

            StockMovementController stockController = new StockMovementController();

            // Pesquisa os produtos
            List<Product> produtosFiltrados = productController.listForPDV(texto);

            // Converte para ProductStockReport
            List<ProductStockReport> relatorios = stockController.gerarRelatorioParaProdutos(produtosFiltrados);

            System.out.println("🔍 " + relatorios.size() + " produtos encontrados");
            carregarDadosTabelaStockProdutos(relatorios);

        } catch (Exception e) {
            System.err.println("❌ Erro ao pesquisar produtos: " + e.getMessage());
//            showError("Erro ao pesquisar: " + e.getMessage());
        }
    }

    /**
     * Função para recarregar dados quando abrir/fechar diálogos
     */
    private void recarregarDadosAposOperacao() {
        SwingUtilities.invokeLater(() -> {
            carregarStockProdutos();
            carregarMovimentosStock();
        });
    }
////    public void screanListProducts() {
////        jTabbedPaneProduct.setSelectedIndex(0);
////        listProducts();
////    }
//    public void loadCombobox() {
//        List<Supplier> listS = supplierController.listarTodos();
//        jComboBoxSunpplierHistoryInput.removeAllItems();
//        for (Supplier item : listS) {
//            jComboBoxSunpplierHistoryInput.addItem(item);
//        }
//
//        List<Warehouse> listW = warehouseController.listarTodos();
//        jComboBoxWarehauseTela1.removeAllItems();
//        for (Warehouse item : listW) {
//            jComboBoxWarehauseTela1.addItem(item);
//        }
//
//    }
//
//    public void loadListStockProducts(List<Product> list) {
//        if (list == null) {
//            list = productController.listForInventory();
//        }
//        DefaultTableModel data = (DefaultTableModel) jTableStockProducts.getModel();
//        data.setNumRows(0);
//        for (Product c : list) {
//            data.addRow(new Object[]{
//                c.getId(),
//                c.getCode(),
//                c.getBarcode(),
//                c.getDescription(),
//                c.getCurrentStock(), // ✅ stock atual calculado
//                c.getMinStock(), // ✅ stock mínimo
//                c.getPrice(),
//                c.getPurchasePrice(),
//                (c.getGroup() != null ? c.getGroup().getName() : ""),
//                c.getType()
//            });
//        }
//    }
//
//    public void filterListProductInventory(String txt) {
//        List<Product> list = productController.listForPDV(txt);
//        loadListStockProducts(list);
//    }
//
//    public void loadListStock(List<StockMovement> list) {
//        DefaultTableModel data = (DefaultTableModel) jTableStockMovement.getModel();
//        data.setNumRows(0);
//
//        System.out.println(list);
////         if (list == null) {
////            list = productController.getForInventory();
////        }
//        for (StockMovement m : list) {
//            data.addRow(new Object[]{
//                m.getId(),
//                (m.getProduct() != null ? m.getProduct().getDescription() : ""), // Produto
//                m.getQuantity(),
//                m.getType(), // IN / OUT / AJUSTE
//                m.getReason(), // Motivo
//                (m.getUser() != null ? m.getUser().getName() : ""), // Usuário responsável
//                m.getCreatedAt() // Data/Hora
//            });
//        }
//    }
//
//    /**
//     * Lista todos os movimentos (auditoria completa)
//     */
//    public void listStockMovement() {
//        List<StockMovement> list = stockMovementController.listarTodos();
//
//        loadListStock(list);
//    }
//
//    /**
//     * Lista apenas os movimentos de 1 produto específico
//     */
//    public void listStockByProduct(int productId) {
//        List<StockMovement> list = stockMovementController.listarPorProduto(productId);
//        loadListStock(list);
//    }
////    public void loadListStock(List<Stock> list) {
//////        List<Product> list = productController.getProducts();
////        DefaultTableModel data = (DefaultTableModel) jTableStocks.getModel();
////        data.setNumRows(0);
////        for (Stock c : list) {
////            data.addRow(new Object[]{
////                c.getId(),
////                c.getQty(),
////                //                c.getPurchase().getDescription(),
////                "",
////                c.getDescription(),
////                c.getUser().getName(),
////                c.getType()
////            });
////        }
////    }
////    
////    public void listStock() {
//////        List<Product> list = productController.getProducts();
////        List<Stock> list = stockController.get("");
////        loadListStock(list);
////    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableStockProducts = new javax.swing.JTable();
        jTextFieldTxtSearchInventory = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButtonFormPurchase = new javax.swing.JButton();
        jComboBoxSunpplierHistoryInput = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboBoxWarehauseTela1 = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jFormattedTextField7 = new javax.swing.JFormattedTextField();
        jFormattedTextField8 = new javax.swing.JFormattedTextField();
        jLabel17 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableStockMovement = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jButtonStockMovementPurchase = new javax.swing.JButton();
        jButtonStockMovementSales = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jFormattedTextField9 = new javax.swing.JFormattedTextField();
        jFormattedTextField10 = new javax.swing.JFormattedTextField();
        jLabel19 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1720, 1080));
        setMinimumSize(new java.awt.Dimension(700, 600));
        setPreferredSize(new java.awt.Dimension(976, 622));

        jTabbedPane3.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jTabbedPane3AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));

        jTableStockProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Codigo", "Codigo de Barra", "Produto", "Stock atual", "Stock mínimo", "Preço venda", "Preço Compra", "Armazem", "Tipo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableStockProducts);

        jTextFieldTxtSearchInventory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldTxtSearchInventoryKeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Objetivo: Permitir visualizar o stock disponível de cada produto, em cada armazém (se existir mais de um).");

        jButtonFormPurchase.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonFormPurchase.setText("Entrada de Mercadoria");
        jButtonFormPurchase.setEnabled(false);
        jButtonFormPurchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFormPurchaseActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Selecione o Fornecedor");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Pesquisar");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Selecione o Armazem");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(187, 187, 187)
                        .addComponent(jLabel5)
                        .addGap(57, 57, 57)
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 619, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jTextFieldTxtSearchInventory, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBoxSunpplierHistoryInput, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(5, 5, 5)
                                        .addComponent(jComboBoxWarehauseTela1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButtonFormPurchase)))
                                .addGap(0, 288, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jComboBoxSunpplierHistoryInput, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonFormPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxWarehauseTela1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jTextFieldTxtSearchInventory))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jTabbedPane3.addTab("Consulta de stock atual por produto", jPanel2);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        jTextField2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Pesquisar  por motivo");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setText("Da data");

        try {
            jFormattedTextField7.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("20##-##-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jFormattedTextField8.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("20##-##-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setText("Ate a data");

        jButton4.setText("Filtrar");

        jTableStockMovement.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Quantidade", "Producto", "Motivo", "Usuario", "Data"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTableStockMovement);

        jButton1.setText("Movimento de Estoque");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Armazem");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Objetivo: Permitir visualizar o stock disponível de cada produto, em cada armazém (se existir mais de um).");

        jButtonStockMovementPurchase.setText("Compra");
        jButtonStockMovementPurchase.setEnabled(false);
        jButtonStockMovementPurchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStockMovementPurchaseActionPerformed(evt);
            }
        });

        jButtonStockMovementSales.setText("Venda");
        jButtonStockMovementSales.setEnabled(false);
        jButtonStockMovementSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStockMovementSalesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 619, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(93, 93, 93)
                                .addComponent(jLabel16)
                                .addGap(43, 43, 43)
                                .addComponent(jLabel17)
                                .addGap(195, 195, 195))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addComponent(jButtonStockMovementPurchase)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonStockMovementSales)))
                .addContainerGap(130, Short.MAX_VALUE))
            .addComponent(jScrollPane4)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jFormattedTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonStockMovementPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonStockMovementSales, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton4, jFormattedTextField7, jFormattedTextField8, jTextField2});

        jTabbedPane3.addTab("Movimentos de stock (entrada/saída)", jPanel1);

        jPanel4.setBackground(new java.awt.Color(204, 204, 255));

        jTextField3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Pesquisar  por motivo");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setText("Da data");

        try {
            jFormattedTextField9.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jFormattedTextField10.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel19.setText("Ate a data");

        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton5.setText("Filtrar");

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Quantidade", "Producto", "Motivo", "Usuario", "Data"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jTable3);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Objetivo: Permitir visualizar o stock disponível de cada produto, em cada armazém (se existir mais de um).");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jFormattedTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 619, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(468, Short.MAX_VALUE))
            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jFormattedTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jTabbedPane3.addTab("Alertas de stock mínimo", jPanel4);

        jScrollPane1.setViewportView(jTabbedPane3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1095, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPane3AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jTabbedPane3AncestorAdded
        // TODO add your handling code here:
        //        listStockMovement();
        //        listStockByProduct(PROPERTIES);
    }//GEN-LAST:event_jTabbedPane3AncestorAdded

    private void jButtonStockMovementSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStockMovementSalesActionPerformed
        // TODO add your handling code here:
        try {
            JDialogStockMovementSales jdForm = new JDialogStockMovementSales(null, true);
            jdForm.setVisible(true);

            if (jdForm.getResponse() == true) {
                JOptionPane.showMessageDialog(null, "Saída de stock por venda realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                // Atualizar dados em background
                SwingUtilities.invokeLater(() -> {
                    atualizarTodasTabelasStock();
                });
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao processar movimento de venda: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Erro ao processar movimento de stock: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonStockMovementSalesActionPerformed

    private void jButtonStockMovementPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStockMovementPurchaseActionPerformed
        // TODO add your handling code here:
        try {
            JDialogStockMovementPurchase jdForm = new JDialogStockMovementPurchase(null, true);
            jdForm.setVisible(true);

            if (jdForm.getResponse() == true) {
                JOptionPane.showMessageDialog(null, "Entrada de stock por compra realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                // Atualizar dados em background para não travar a UI
                SwingUtilities.invokeLater(() -> {
                    atualizarTodasTabelasStock();
                });
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao processar movimento de compra: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Erro ao processar movimento de stock: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonStockMovementPurchaseActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        new JDialogWarehouse(null, true).setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        JDialogFormStockMovement jdForm = new JDialogFormStockMovement(null, true);
        jdForm.setVisible(true);

        if (jdForm.getResponse() == true) {
            JOptionPane.showMessageDialog(null, "Movimento de estoque registrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            atualizarTodasTabelasStock();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButtonFormPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFormPurchaseActionPerformed
        // TODO add your handling code here:
        JDialogFormEntryProdPurchase formPurchase = new JDialogFormEntryProdPurchase(null, true);
        formPurchase.setVisible(true);

        if (formPurchase.getResponse() == true) {
            JOptionPane.showMessageDialog(null, "Entrada de mercadoria realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            atualizarTodasTabelasStock();
        }
    }//GEN-LAST:event_jButtonFormPurchaseActionPerformed

    private void jTextFieldTxtSearchInventoryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldTxtSearchInventoryKeyReleased
        // TODO add your handling code here:
        String txt = jTextFieldTxtSearchInventory.getText().trim();

        Timer timer = new Timer(300, e -> {
            pesquisarProdutosComFiltros(); // Usa o método corrigido
        });
        timer.setRepeats(false);
        timer.start();
    }//GEN-LAST:event_jTextFieldTxtSearchInventoryKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButtonFormPurchase;
    private javax.swing.JButton jButtonStockMovementPurchase;
    private javax.swing.JButton jButtonStockMovementSales;
    private javax.swing.JComboBox jComboBoxSunpplierHistoryInput;
    private javax.swing.JComboBox jComboBoxWarehauseTela1;
    private javax.swing.JFormattedTextField jFormattedTextField10;
    private javax.swing.JFormattedTextField jFormattedTextField7;
    private javax.swing.JFormattedTextField jFormattedTextField8;
    private javax.swing.JFormattedTextField jFormattedTextField9;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTableStockMovement;
    private javax.swing.JTable jTableStockProducts;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextFieldTxtSearchInventory;
    // End of variables declaration//GEN-END:variables
}
