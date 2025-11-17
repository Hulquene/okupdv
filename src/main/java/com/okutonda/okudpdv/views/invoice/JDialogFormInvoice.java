/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.okutonda.okudpdv.views.invoice;

import com.okutonda.okudpdv.controllers.ClientController;
import com.okutonda.okudpdv.controllers.InvoiceController;
import com.okutonda.okudpdv.controllers.ProductController;
import com.okutonda.okudpdv.data.entities.Clients;
import com.okutonda.okudpdv.data.entities.Invoices;
import com.okutonda.okudpdv.data.entities.Payment;
import com.okutonda.okudpdv.data.entities.PaymentMode;
import com.okutonda.okudpdv.data.entities.PaymentStatus;
import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.ProductSales;
import com.okutonda.okudpdv.data.entities.ProductType;
import com.okutonda.okudpdv.data.entities.User;
import com.okutonda.okudpdv.helpers.UserSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author hr
 */
public class JDialogFormInvoice extends javax.swing.JDialog {

    Boolean response = false;
    private final InvoiceController invoiceController;
    private final ClientController clientController;
    private final ProductController productController;
    private final UserSession userSession;

    private List<ProductSales> produtosFatura;
    private Invoices faturaAtual;
    private DefaultTableModel tableModelProdutosFatura;
    private DefaultTableModel tableModelListaProdutos;
    private TableRowSorter<DefaultTableModel> rowSorter;

    /**
     * Creates new form JDialogFormInvoice
     */
    public JDialogFormInvoice(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.invoiceController = new InvoiceController();
        this.clientController = new ClientController();
        this.productController = new ProductController();
        this.userSession = UserSession.getInstance();
        this.produtosFatura = new ArrayList<>();
        this.faturaAtual = new Invoices();

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
        inicializarTabelaProdutosFatura();
        inicializarTabelaListaProdutos();

        // Configurar datas padrão
        configurarDatasPadrao();

        // Carregar combobox
        carregarCombobox();

        // Configurar vendedor atual
        configurarVendedor();

        // Configurar filtro de produtos
        configurarFiltroProdutos();
    }

    /**
     * Inicializa a tabela de produtos da fatura
     */
    private void inicializarTabelaProdutosFatura() {
        String[] colunas = {"ID", "Código", "Descrição", "Preço", "Qtd", "Total", "Imposto"};
        tableModelProdutosFatura = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabela não editável
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return Integer.class;
                    case 1:
                        return String.class;
                    case 2:
                        return String.class;
                    case 3:
                        return BigDecimal.class;
                    case 4:
                        return Integer.class;
                    case 5:
                        return BigDecimal.class;
                    case 6:
                        return BigDecimal.class;
                    default:
                        return Object.class;
                }
            }
        };

        jTableProdutsInvoice.setModel(tableModelProdutosFatura);

        // Configurar largura das colunas
        jTableProdutsInvoice.getColumnModel().getColumn(0).setPreferredWidth(50);
        jTableProdutsInvoice.getColumnModel().getColumn(1).setPreferredWidth(80);
        jTableProdutsInvoice.getColumnModel().getColumn(2).setPreferredWidth(200);
        jTableProdutsInvoice.getColumnModel().getColumn(3).setPreferredWidth(80);
        jTableProdutsInvoice.getColumnModel().getColumn(4).setPreferredWidth(50);
        jTableProdutsInvoice.getColumnModel().getColumn(5).setPreferredWidth(80);
        jTableProdutsInvoice.getColumnModel().getColumn(6).setPreferredWidth(80);
    }

    /**
     * Inicializa a tabela de lista de produtos
     */
    private void inicializarTabelaListaProdutos() {
        String[] colunas = {"Código", "Descrição", "Tipo"};
        tableModelListaProdutos = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabela não editável
            }
        };

        jTableListProdutos.setModel(tableModelListaProdutos);

        // Configurar largura das colunas
        jTableListProdutos.getColumnModel().getColumn(0).setPreferredWidth(80);
        jTableListProdutos.getColumnModel().getColumn(1).setPreferredWidth(200);
        jTableListProdutos.getColumnModel().getColumn(2).setPreferredWidth(80);
    }

    /**
     * Configura filtro para a tabela de produtos
     */
    private void configurarFiltroProdutos() {
        rowSorter = new TableRowSorter<>(tableModelListaProdutos);
        jTableListProdutos.setRowSorter(rowSorter);
    }

    /**
     * Filtra produtos na tabela
     */
    private void filtrarProdutos() {
        String textoFiltro = jTextFieldFilterListProdutos.getText().trim();

        if (textoFiltro.length() == 0) {
            rowSorter.setRowFilter(null);
        } else {
            try {
                // Filtra por código ou descrição (ignorando case)
                RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("(?i)" + textoFiltro, 0, 1);
                rowSorter.setRowFilter(rf);
            } catch (Exception e) {
                System.err.println("Erro ao filtrar produtos: " + e.getMessage());
            }
        }
    }

    /**
     * Carrega produtos na tabela de lista
     */
    private void carregarProdutosNaTabela() {
        try {
            tableModelListaProdutos.setRowCount(0);
            List<Product> produtos = productController.listAll();

            for (Product produto : produtos) {
                if (produto.getStatus().isActive()) {
                    tableModelListaProdutos.addRow(new Object[]{
                        produto.getCode(),
                        produto.getDescription(),
                        produto.getType() != null ? produto.getType().getDescription() : "Produto"
                    });
                }
            }

            jLabel9.setText("Produtos disponíveis: " + tableModelListaProdutos.getRowCount());

        } catch (Exception e) {
            System.err.println("Erro ao carregar produtos: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar produtos: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Preenche campos do produto quando selecionado na tabela
     */
    private void preencherCamposProdutoSelecionado() {
        int selectedRow = jTableListProdutos.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        try {
            int modelRow = jTableListProdutos.convertRowIndexToModel(selectedRow);

            String codigo = (String) tableModelListaProdutos.getValueAt(modelRow, 0);
            String descricao = (String) tableModelListaProdutos.getValueAt(modelRow, 1);
            String tipo = (String) tableModelListaProdutos.getValueAt(modelRow, 2);

            jTextFieldCodeProd.setText(codigo);
            jTextFieldNameProd.setText(descricao);

            // Buscar produto completo para obter o preço
            Product produto = productController.getByCode(codigo);
            if (produto != null && produto.getPrice() != null) {
                jTextFieldPriceProd.setText(produto.getPrice().toString());
            }
            // ✅ VERIFICAÇÃO REFORÇADA PARA SERVIÇOS
            if (tipo.equals(ProductType.SERVICE.getDescription())) {
                jTextFieldPriceProd.setEditable(true);
            } else {
                jTextFieldPriceProd.setEditable(false);
            }

            // Focar no campo de quantidade para facilitar a entrada
            jTextFieldQtdProd.requestFocus();
            jTextFieldQtdProd.selectAll();

        } catch (Exception e) {
            System.err.println("Erro ao preencher campos do produto: " + e.getMessage());
        }
    }

    /**
     * Seleciona produto da tabela (para uso do botão)
     */
    private void selecionarProduto() {
        int selectedRow = jTableListProdutos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um produto da lista primeiro",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        preencherCamposProdutoSelecionado();
    }

    /**
     * Adiciona produto selecionado à fatura
     */
    private void adicionarProduto() {
        try {
            // Verificar se há produto selecionado
            if (jTextFieldCodeProd.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Selecione um produto da lista primeiro",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Obter código do produto selecionado
            String codigoProduto = jTextFieldCodeProd.getText().trim();
            Product produto = productController.getByCode(codigoProduto);

            if (produto == null) {
                JOptionPane.showMessageDialog(this,
                        "Produto não encontrado: " + codigoProduto,
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obter quantidade
            int quantidade = 1; // Valor padrão
            try {
                String qtdTexto = jTextFieldQtdProd.getText().trim();
                if (!qtdTexto.isEmpty()) {
                    quantidade = Integer.parseInt(qtdTexto);
                    if (quantidade <= 0) {
                        JOptionPane.showMessageDialog(this,
                                "Quantidade deve ser maior que zero",
                                "Aviso",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Quantidade inválida. Use um número inteiro.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar se produto já existe na fatura
            ProductSales produtoExistente = encontrarProdutoNaFatura(produto.getId());

            if (produtoExistente != null) {
                // Produto já existe - incrementar quantidade
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Produto '" + produto.getDescription() + "' já está na fatura.\n"
                        + "Deseja incrementar a quantidade em " + quantidade + " unidades?",
                        "Produto Duplicado",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    produtoExistente.setQty(produtoExistente.getQty() + quantidade);

                    // Atualizar preço se foi fornecido um novo
                    BigDecimal precoPersonalizado = obterPrecoPersonalizado();
                    if (precoPersonalizado != null) {
                        produtoExistente.setPrice(precoPersonalizado);
                    }

                    // Atualizar interface
                    atualizarTabelaProdutosFatura();
                    calcularTotais();

                    limparCamposProduto();
                    return;
                } else {
                    return; // Usuário cancelou
                }
            }

            // Produto novo - adicionar à fatura
            ProductSales productSales = new ProductSales();
            productSales.setProduct(produto);
            productSales.setDescription(produto.getDescription());
            productSales.setCode(produto.getCode());

            // Usar preço personalizado se fornecido, senão usar preço do produto
            BigDecimal precoPersonalizado = obterPrecoPersonalizado();
            if (precoPersonalizado != null) {
                productSales.setPrice(precoPersonalizado);
            } else {
                productSales.setPrice(produto.getPrice());
            }

            productSales.setQty(quantidade);

            // Configurar impostos
            if (produto.getTaxe() != null) {
                productSales.setTaxePercentage(produto.getTaxe().getPercetage());
                productSales.setTaxeCode(produto.getTaxe().getCode());
                productSales.setTaxeName(produto.getTaxe().getName());
            }

            if (produto.getReasonTaxe() != null) {
                productSales.setReasonCode(produto.getReasonTaxe().getCode());
                productSales.setReasonTax(produto.getReasonTaxe().getReason());
            }

            // Adicionar à lista
            produtosFatura.add(productSales);

            // Atualizar tabela
            atualizarTabelaProdutosFatura();

            // Calcular totais
            calcularTotais();

            // Limpar campos
            limparCamposProduto();

            JOptionPane.showMessageDialog(this,
                    "Produto adicionado com sucesso! Quantidade: " + quantidade,
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao adicionar produto: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Remove produto selecionado da fatura
     */
    private void removerProduto() {
        int selectedRow = jTableProdutsInvoice.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um produto para remover",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int modelRow = jTableProdutsInvoice.convertRowIndexToModel(selectedRow);
            Integer productId = (Integer) tableModelProdutosFatura.getValueAt(modelRow, 0);

            // Remover da lista
            produtosFatura.removeIf(ps -> ps.getProduct().getId().equals(productId));

            // Atualizar tabela e totais
            atualizarTabelaProdutosFatura();
            calcularTotais();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao remover produto: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Atualiza a tabela de produtos da fatura
     */
    private void atualizarTabelaProdutosFatura() {
        tableModelProdutosFatura.setRowCount(0);

        for (ProductSales ps : produtosFatura) {
            BigDecimal totalItem = ps.getPrice().multiply(BigDecimal.valueOf(ps.getQty()));
            BigDecimal impostoItem = BigDecimal.ZERO;

            if (ps.getTaxePercentage() != null && ps.getTaxePercentage().compareTo(BigDecimal.ZERO) > 0) {
                impostoItem = totalItem.multiply(ps.getTaxePercentage())
                        .divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
            }

            tableModelProdutosFatura.addRow(new Object[]{
                ps.getProduct().getId(),
                ps.getCode(),
                ps.getDescription(),
                ps.getPrice(),
                ps.getQty(),
                totalItem,
                impostoItem
            });
        }
    }

    /**
     * Configura datas padrão
     */
    private void configurarDatasPadrao() {
        String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String dataVencimento = LocalDate.now().plusDays(30).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        jFormattedTextFieldDateStart.setText(dataAtual);
        jFormattedTextFieldDateEnd.setText(dataVencimento);
    }

    /**
     * Carrega combobox com dados
     */
    private void carregarCombobox() {
        try {
            // Carregar clientes
            carregarClientes();

            // Carregar produtos na tabela
            carregarProdutosNaTabela();

            // Configurar moeda
            jComboBoxCurrency.removeAllItems();
            jComboBoxCurrency.addItem("AOA");
            jComboBoxCurrency.addItem("USD");
            jComboBoxCurrency.addItem("EUR");
            jComboBoxCurrency.setSelectedItem("AOA");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar dados: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carrega lista de clientes
     */
    private void carregarClientes() {
        try {
            jComboBoxListClients.removeAllItems();
            List<Clients> clientes = clientController.getAll();

            for (Clients cliente : clientes) {
                jComboBoxListClients.addItem(cliente.getName() + " - " + cliente.getNif());
            }

            if (jComboBoxListClients.getItemCount() > 0) {
                jComboBoxListClients.setSelectedIndex(0);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar clientes: " + e.getMessage());
        }
    }

    /**
     * Configura vendedor atual
     */
    private void configurarVendedor() {
        User usuario = userSession.getUser();
        if (usuario != null) {
            jLabelNameSeller.setText(usuario.getName());
            faturaAtual.setSeller(usuario);
        }
    }

    /**
     * Carrega dados iniciais
     */
    private void carregarDadosIniciais() {
        // Nada específico por enquanto - pode ser usado para carregar uma fatura existente
    }

    /**
     * MÉTODO AUXILIAR: Obter preço personalizado do campo jTextFieldPriceProd
     */
    private BigDecimal obterPrecoPersonalizado() {
        try {
            String precoTexto = jTextFieldPriceProd.getText().trim();
            if (!precoTexto.isEmpty()) {
                BigDecimal preco = new BigDecimal(precoTexto.replace(",", "."));
                if (preco.compareTo(BigDecimal.ZERO) >= 0) {
                    return preco;
                }
            }
        } catch (NumberFormatException e) {
            // Ignora erro - usa preço padrão do produto
        }
        return null;
    }

    /**
     * MÉTODO AUXILIAR: Limpar campos de produto após adicionar
     */
    private void limparCamposProduto() {
        jTextFieldCodeProd.setText("");
        jTextFieldNameProd.setText("");
        jTextFieldPriceProd.setText("");
        jTextFieldQtdProd.setText("1");
    }

    /**
     * MÉTODO AUXILIAR: Encontrar produto na lista da fatura
     */
    private ProductSales encontrarProdutoNaFatura(Integer productId) {
        for (ProductSales ps : produtosFatura) {
            if (ps.getProduct().getId().equals(productId)) {
                return ps;
            }
        }
        return null;
    }

    /**
     * Calcula totais da fatura
     */
    private void calcularTotais() {
        BigDecimal subTotal = BigDecimal.ZERO;
        BigDecimal totalImpostos = BigDecimal.ZERO;
        BigDecimal desconto = BigDecimal.ZERO;

        try {
            // Calcular desconto
            String textoDesconto = jTextFieldDesconto.getText().trim();
            if (!textoDesconto.isEmpty()) {
                desconto = new BigDecimal(textoDesconto);
                if (desconto.compareTo(BigDecimal.ZERO) < 0) {
                    desconto = BigDecimal.ZERO;
                    jTextFieldDesconto.setText("0");
                }
            }

            // Calcular subtotal e impostos
            for (ProductSales ps : produtosFatura) {
                BigDecimal totalItem = ps.getPrice().multiply(BigDecimal.valueOf(ps.getQty()));
                subTotal = subTotal.add(totalItem);

                if (ps.getTaxePercentage() != null && ps.getTaxePercentage().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal impostoItem = totalItem.multiply(ps.getTaxePercentage())
                            .divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
                    totalImpostos = totalImpostos.add(impostoItem);
                }
            }

            // Calcular total
            BigDecimal total = subTotal.add(totalImpostos).subtract(desconto);

            // Atualizar labels
            jLabelSubTotal.setText(subTotal.toString());
            jLabelTotalTaxe.setText(totalImpostos.toString());
            jLabelDesconto.setText(desconto.toString());
            jLabelTotal.setText(total.toString());

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor de desconto inválido",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            jTextFieldDesconto.setText("0");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao calcular totais: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cria pagamento automático (fallback)
     */
    private List<Payment> criarPagamentoAutomatico() {
        Payment pagamento = new Payment();
        pagamento.setTotal(faturaAtual.getTotal());
        pagamento.setPaymentMode(PaymentMode.NU);
        pagamento.setDescription("Pagamento automático");
        pagamento.setReference("AUTO-" + System.currentTimeMillis());
        pagamento.setStatus(PaymentStatus.PAGO);
        return List.of(pagamento);
    }

    /**
     * Salva a fatura
     */
    private void salvarFatura(boolean marcarComoPaga) {
        try {
            // Validar dados básicos
            if (!validarDadosFatura()) {
                return;
            }

            // Configurar fatura
            configurarFatura();
            Invoices faturaSalva = null;

            if (marcarComoPaga) {
                // Abre diálogo de pagamento
                JDialogPaymentInvoice jdPayment = new JDialogPaymentInvoice(null, true);
                jdPayment.setInvoice(faturaAtual);
                jdPayment.setVisible(true);

                // Obtém a resposta (fatura salva com pagamentos)
                faturaSalva = jdPayment.getResponse();
            } else {
                // Cria pagamento automático
                List<Payment> pagamentos = criarPagamentoAutomatico();
                faturaAtual.setStatus(PaymentStatus.PAGO);

                faturaSalva = invoiceController.criarFaturaComProdutosEPagamentos(
                        faturaAtual,
                        faturaAtual.getProducts(),
                        pagamentos
                );
            }

            if (faturaSalva != null) {
                response = true;
                JOptionPane.showMessageDialog(this,
                        "Fatura salva com sucesso!\nNúmero: " + faturaSalva.getPrefix() + "/" + faturaSalva.getNumber(),
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar fatura",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar fatura: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Valida dados da fatura
     */
    private boolean validarDadosFatura() {
        // Verificar cliente
        if (jComboBoxListClients.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Verificar produtos
        if (produtosFatura.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione pelo menos um produto",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Verificar datas
        if (jFormattedTextFieldDateStart.getText().trim().isEmpty()
                || jFormattedTextFieldDateEnd.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha as datas",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Configura os dados da fatura
     */
    private void configurarFatura() {
        // Cliente
        String clienteSelecionado = (String) jComboBoxListClients.getSelectedItem();
        String nifCliente = clienteSelecionado.split(" - ")[1];
        Clients cliente = clientController.getByNif(nifCliente);
        faturaAtual.setClient(cliente);

        // Datas
        faturaAtual.setIssueDate(jFormattedTextFieldDateStart.getText());
        faturaAtual.setDueDate(jFormattedTextFieldDateEnd.getText());

        // Produtos
        faturaAtual.setProducts(produtosFatura);

        // Totais
        faturaAtual.setSubTotal(new BigDecimal(jLabelSubTotal.getText()));
        faturaAtual.setTotalTaxe(new BigDecimal(jLabelTotalTaxe.getText()));
        faturaAtual.setTotal(new BigDecimal(jLabelTotal.getText()));

        // Desconto
        String textoDesconto = jTextFieldDesconto.getText().trim();
        if (!textoDesconto.isEmpty()) {
            faturaAtual.setDiscount(new BigDecimal(textoDesconto));
        }

        // Observações
        faturaAtual.setNote(jTextArea1.getText());

        // Prefixo (FT para fatura)
        faturaAtual.setPrefix("FT");
    }

    /**
     * Método para recarregar lista de produtos
     */
    private void recarregarListaProdutos() {
        try {
            carregarProdutosNaTabela();
            JOptionPane.showMessageDialog(this,
                    "Lista de produtos recarregada com sucesso!",
                    "Lista Recarregada",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao recarregar lista de produtos: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableProdutsInvoice = new javax.swing.JTable();
        jButtonRemoveProdTable = new javax.swing.JButton();
        jComboBoxListClients = new javax.swing.JComboBox<>();
        jButtonSave = new javax.swing.JButton();
        jFormattedTextFieldDateStart = new javax.swing.JFormattedTextField();
        jFormattedTextFieldDateEnd = new javax.swing.JFormattedTextField();
        jComboBoxCurrency = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabelSubTotal = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jTextFieldDesconto = new javax.swing.JTextField();
        jLabelNameSeller = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButtonAddProdTable = new javax.swing.JButton();
        jButtonSaveAndPay = new javax.swing.JButton();
        jLabelTotalTaxe = new javax.swing.JLabel();
        jLabelListTypeTaxe = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabelDesconto = new javax.swing.JLabel();
        jTextFieldQtdProd = new javax.swing.JTextField();
        jTextFieldPriceProd = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableListProdutos = new javax.swing.JTable();
        jTextFieldFilterListProdutos = new javax.swing.JTextField();
        jTextFieldNameProd = new javax.swing.JTextField();
        jTextFieldCodeProd = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jButtonSelecionarProd = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        jTableProdutsInvoice.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTableProdutsInvoice);

        jButtonRemoveProdTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Trash Can.png"))); // NOI18N
        jButtonRemoveProdTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveProdTableActionPerformed(evt);
            }
        });

        jComboBoxListClients.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButtonSave.setText("Save Fatura");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        try {
            jFormattedTextFieldDateStart.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/20##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jFormattedTextFieldDateEnd.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jComboBoxCurrency.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jLabel1.setText("Selecione o Clienet");

        jLabel2.setText("Data da Fatura");

        jLabel3.setText("Data vencimento");

        jLabel4.setText("Moeda");

        jLabel5.setText("Adicionar produto");

        jLabel6.setText("Nota do Admin");

        jLabelSubTotal.setText("SubTotal");

        jLabelTotal.setText("Total");

        jTextFieldDesconto.setText("0");
        jTextFieldDesconto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldDescontoKeyReleased(evt);
            }
        });

        jLabelNameSeller.setText("Vendedor");

        jLabel10.setText("Desconto");

        jButtonAddProdTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Done.png"))); // NOI18N
        jButtonAddProdTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddProdTableActionPerformed(evt);
            }
        });

        jButtonSaveAndPay.setText("Save e Pagar");
        jButtonSaveAndPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveAndPayActionPerformed(evt);
            }
        });

        jLabelTotalTaxe.setText("Total Imposto");

        jLabelListTypeTaxe.setText("Lista Tipo imposto");

        jLabel7.setText("Total");

        jLabel8.setText("SubTotal");

        jLabel11.setText("Desconto");

        jLabelDesconto.setText("Desconto");

        jTextFieldQtdProd.setText("1");

        jTextFieldPriceProd.setText("0.00");

        jTableListProdutos.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableListProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableListProdutosMouseClicked(evt);
            }
        });
        jTableListProdutos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTableListProdutosKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(jTableListProdutos);

        jTextFieldFilterListProdutos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldFilterListProdutosKeyReleased(evt);
            }
        });

        jTextFieldNameProd.setEditable(false);

        jTextFieldCodeProd.setEditable(false);

        jLabel9.setText("Pesquisar Produtos e servicos");

        jLabel12.setText("Selecione o produto");

        jButtonSelecionarProd.setText("Add");
        jButtonSelecionarProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelecionarProdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jFormattedTextFieldDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jFormattedTextFieldDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBoxCurrency, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(42, 42, 42)
                                        .addComponent(jLabel4))))
                            .addComponent(jComboBoxListClients, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextFieldCodeProd, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextFieldNameProd, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextFieldFilterListProdutos)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonSelecionarProd)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextFieldPriceProd, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldQtdProd, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonAddProdTable, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButtonRemoveProdTable)))))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButtonSaveAndPay))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel11)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabelDesconto))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel8)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabelSubTotal))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel7)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabelTotal))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabelListTypeTaxe)
                                            .addGap(64, 64, 64)
                                            .addComponent(jLabelTotalTaxe))))
                                .addGap(52, 52, 52))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelNameSeller)
                        .addGap(230, 230, 230))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabelNameSeller))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBoxListClients, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jFormattedTextFieldDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextFieldDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxCurrency, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jButtonAddProdTable, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButtonRemoveProdTable, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextFieldQtdProd)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextFieldCodeProd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextFieldPriceProd, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldNameProd, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelSubTotal)
                            .addComponent(jLabel8))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jLabelDesconto))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelTotalTaxe)
                            .addComponent(jLabelListTypeTaxe))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelTotal)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonSaveAndPay, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldFilterListProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(jButtonSelecionarProd))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(70, 70, 70))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 960, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 501, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(978, 509));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAddProdTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddProdTableActionPerformed
        // TODO add your handling code here:
        adicionarProduto();
    }//GEN-LAST:event_jButtonAddProdTableActionPerformed

    private void jButtonRemoveProdTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveProdTableActionPerformed
        // TODO add your handling code here:
        removerProduto();
    }//GEN-LAST:event_jButtonRemoveProdTableActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        // TODO add your handling code here:
        salvarFatura(false);
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonSaveAndPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveAndPayActionPerformed
        // TODO add your handling code here:
        salvarFatura(true);
    }//GEN-LAST:event_jButtonSaveAndPayActionPerformed

    private void jTextFieldDescontoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDescontoKeyReleased
        // TODO add your handling code here:
        calcularTotais();
    }//GEN-LAST:event_jTextFieldDescontoKeyReleased

    private void jTableListProdutosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableListProdutosKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_jTableListProdutosKeyReleased

    private void jTextFieldFilterListProdutosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldFilterListProdutosKeyReleased
        // TODO add your handling code here:
        filtrarProdutos();
    }//GEN-LAST:event_jTextFieldFilterListProdutosKeyReleased

    private void jTableListProdutosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableListProdutosMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            preencherCamposProdutoSelecionado();
        }
    }//GEN-LAST:event_jTableListProdutosMouseClicked

    private void jButtonSelecionarProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelecionarProdActionPerformed
        // TODO add your handling code here:
        selecionarProduto();
    }//GEN-LAST:event_jButtonSelecionarProdActionPerformed

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
            java.util.logging.Logger.getLogger(JDialogFormInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogFormInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogFormInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogFormInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogFormInvoice dialog = new JDialogFormInvoice(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonAddProdTable;
    private javax.swing.JButton jButtonRemoveProdTable;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSaveAndPay;
    private javax.swing.JButton jButtonSelecionarProd;
    private javax.swing.JComboBox<String> jComboBoxCurrency;
    private javax.swing.JComboBox<String> jComboBoxListClients;
    private javax.swing.JFormattedTextField jFormattedTextFieldDateEnd;
    private javax.swing.JFormattedTextField jFormattedTextFieldDateStart;
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
    private javax.swing.JLabel jLabelDesconto;
    private javax.swing.JLabel jLabelListTypeTaxe;
    private javax.swing.JLabel jLabelNameSeller;
    private javax.swing.JLabel jLabelSubTotal;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JLabel jLabelTotalTaxe;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTableListProdutos;
    private javax.swing.JTable jTableProdutsInvoice;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextFieldCodeProd;
    private javax.swing.JTextField jTextFieldDesconto;
    private javax.swing.JTextField jTextFieldFilterListProdutos;
    private javax.swing.JTextField jTextFieldNameProd;
    private javax.swing.JTextField jTextFieldPriceProd;
    private javax.swing.JTextField jTextFieldQtdProd;
    // End of variables declaration//GEN-END:variables
}
