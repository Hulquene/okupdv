/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.okutonda.okudpdv.views.purchases;

import com.okutonda.okudpdv.controllers.PurchaseController;
import com.okutonda.okudpdv.controllers.PurchasePaymentController;
import com.okutonda.okudpdv.data.entities.PaymentMode;
import com.okutonda.okudpdv.data.entities.PaymentStatus;
import com.okutonda.okudpdv.data.entities.Purchase;
import com.okutonda.okudpdv.data.entities.PurchasePayment;
import com.okutonda.okudpdv.data.entities.User;
import com.okutonda.okudpdv.helpers.UserSession;
import com.okutonda.okudpdv.helpers.Util;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hr
 */
public class JDialogPaymentPurchase extends javax.swing.JDialog {

    private Purchase purchase;
    private Purchase response = null;
    private final DecimalFormat df = new DecimalFormat("#,##0.00");
    private final PurchasePaymentController paymentController = new PurchasePaymentController();
    private final PurchaseController purchaseController = new PurchaseController();
    private final UserSession userSession = UserSession.getInstance();

    private List<PurchasePayment> pagamentos = new ArrayList<>();
    private BigDecimal totalCompra = BigDecimal.ZERO;
    private BigDecimal totalRegistado = BigDecimal.ZERO;
    private BigDecimal totalPendente = BigDecimal.ZERO;

    /**
     * Creates new form JDialogPayPurchase
     */
    public JDialogPaymentPurchase(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        inicializarComponentes();
    }

    public Purchase getResponse() {
        return response;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
        carregarDadosCompra();
        atualizarTotais();
    }

    private void inicializarComponentes() {
        // Configurar combobox de modos de pagamento
        jComboBoxPaymentMode.removeAllItems();
        for (PaymentMode mode : PaymentMode.getActiveModes()) {
            jComboBoxPaymentMode.addItem(mode.getDescricao() + " (" + mode.getCodigo() + ")");
        }

        // Configurar combobox de status
        jComboBoxPaymentStatus.removeAllItems();
        for (PaymentStatus status : PaymentStatus.values()) {
            jComboBoxPaymentStatus.addItem(status.getDescricao());
        }

        // Configurar data atual
        jFormattedTextFieldPaymentDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        // Configurar tabela
        configurarTabela();
    }

    private void carregarDadosCompra() {
        if (purchase != null) {
            totalCompra = purchase.getTotal() != null ? purchase.getTotal() : BigDecimal.ZERO;

            jTextFieldTotalPurchase.setText(df.format(totalCompra));
            jLabelTitle.setText("Pagamento da Compra " + purchase.getInvoiceNumber());

            if (purchase.getSupplier() != null) {
                jLabelSupplierName.setText(purchase.getSupplier().getName());
                jLabelSupplierNif.setText(purchase.getSupplier().getNif() != null ? purchase.getSupplier().getNif() : "N/A");
            }

//            if (purchase.getNote() != null) {
//                jTextAreaNote.setText(purchase.getNote());
//            }
            // Carregar pagamentos existentes
            carregarPagamentosExistentes();
        }
    }

    private void carregarPagamentosExistentes() {
        if (purchase != null && purchase.getId() != null) {
            List<PurchasePayment> pagamentosExistentes = paymentController.listarPorCompra(purchase.getId());
            pagamentos.clear();
            pagamentos.addAll(pagamentosExistentes);
            atualizarTabelaPagamentos();
        }
    }

    private void configurarTabela() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Modo", "Status", "Valor", "Data", "Referência"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        jTablePayments.setModel(model);

        // Ajustar largura das colunas
        jTablePayments.getColumnModel().getColumn(0).setPreferredWidth(100);
        jTablePayments.getColumnModel().getColumn(1).setPreferredWidth(80);
        jTablePayments.getColumnModel().getColumn(2).setPreferredWidth(80);
        jTablePayments.getColumnModel().getColumn(3).setPreferredWidth(80);
        jTablePayments.getColumnModel().getColumn(4).setPreferredWidth(120);
    }

    private void atualizarTabelaPagamentos() {
        DefaultTableModel model = (DefaultTableModel) jTablePayments.getModel();
        model.setRowCount(0);

        for (PurchasePayment pagamento : pagamentos) {
            model.addRow(new Object[]{
                pagamento.getMetodo().getDescricao(),
                pagamento.getStatus().getDescricao(),
                df.format(pagamento.getValorPago()),
                pagamento.getDataPagamento(),
                pagamento.getReferencia()
            });
        }
    }

    private void adicionarPagamento() {
        try {
            // Validar dados
            if (!validarDadosPagamento()) {
                return;
            }

            // Obter modo de pagamento selecionado
            String modoSelecionado = (String) jComboBoxPaymentMode.getSelectedItem();
            PaymentMode paymentMode = obterPaymentModeFromString(modoSelecionado);

            // Obter status selecionado
            String statusSelecionado = (String) jComboBoxPaymentStatus.getSelectedItem();
            PaymentStatus paymentStatus = obterPaymentStatusFromString(statusSelecionado);

            // Obter valor
            BigDecimal valor = parseBigDecimal(jTextFieldPaymentValue.getText());

            // Obter data
            String data = jFormattedTextFieldPaymentDate.getText();

            // 🔹 CORREÇÃO: Calcular total atualizado incluindo o novo pagamento
            BigDecimal totalPagamentosComNovo = totalRegistado.add(valor);

            // 🔹 CORREÇÃO: Validar se o NOVO TOTAL excede o valor da compra
            if (totalPagamentosComNovo.compareTo(totalCompra) > 0) {
                BigDecimal saldoPermitido = totalCompra.subtract(totalRegistado);
                JOptionPane.showMessageDialog(this,
                        "Valor excede o saldo pendente!\n"
                        + "Saldo permitido: " + df.format(saldoPermitido) + " AOA\n"
                        + "Valor tentado: " + df.format(valor) + " AOA",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Criar pagamento
            PurchasePayment pagamento = new PurchasePayment();
            pagamento.setDescricao("Pagamento compra " + purchase.getInvoiceNumber());
            pagamento.setValorPago(valor);
            pagamento.setMetodo(paymentMode);
            pagamento.setStatus(paymentStatus);
            pagamento.setDataPagamento(Util.parseData(data));
            pagamento.setReferencia(gerarReferenciaPagamento());
            pagamento.setPurchaseId(purchase.getId());

            User usuarioLogado = userSession.getUser();
            if (usuarioLogado != null) {
                pagamento.setUser(usuarioLogado);
            }

            // Adicionar à lista
            pagamentos.add(pagamento);

            // Atualizar interface
            atualizarTabelaPagamentos();
            atualizarTotais();
            limparCamposPagamento();

            JOptionPane.showMessageDialog(this,
                    "Pagamento adicionado com sucesso!\n"
                    + "Saldo restante: " + df.format(totalPendente) + " AOA",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao adicionar pagamento: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void removerPagamento() {
        int selectedRow = jTablePayments.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um pagamento para remover",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja remover este pagamento?",
                "Confirmar Remoção",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            pagamentos.remove(selectedRow);
            atualizarTabelaPagamentos();
            atualizarTotais();
        }
    }

    private void atualizarTotais() {
        totalRegistado = BigDecimal.ZERO;

        for (PurchasePayment pagamento : pagamentos) {
            if (pagamento.getValorPago() != null) {
                totalRegistado = totalRegistado.add(pagamento.getValorPago());
            }
        }

        totalPendente = totalCompra.subtract(totalRegistado);

        jTextFieldTotalPaid.setText(df.format(totalRegistado));
        jTextFieldTotalPending.setText(df.format(totalPendente));

        // Atualizar cores baseadas no status
        if (totalPendente.compareTo(BigDecimal.ZERO) == 0) {
            jTextFieldTotalPending.setBackground(new java.awt.Color(200, 255, 200)); // Verde claro
        } else if (totalPendente.compareTo(BigDecimal.ZERO) > 0) {
            jTextFieldTotalPending.setBackground(new java.awt.Color(255, 255, 200)); // Amarelo
        } else {
            jTextFieldTotalPending.setBackground(new java.awt.Color(255, 200, 200)); // Vermelho claro
        }
    }

    // NO MÉTODO finalizarPagamentos(), ATUALIZE:
    private void finalizarPagamentos() {
        try {
            if (pagamentos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Adicione pelo menos um pagamento antes de finalizar",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 🔹 VERIFICAR SE A COMPRA JÁ TEM ID (já foi salva)
            if (purchase.getId() == null) {
                System.out.println("⚠️  Compra ainda não foi salva no banco...");

                // Se a compra não tem ID, precisamos salvá-la primeiro
                // Garantir que tem invoiceNumber
                if (purchase.getInvoiceNumber() == null) {
                    Integer nextNumber = purchaseController.obterProximoNumeroFatura();
                    purchase.setInvoiceNumber("COMP/" + java.time.LocalDate.now().getYear() + "/" + String.format("%04d", nextNumber));
                }
            }

            // 🔹 CALCULAR STATUS BASEADO NOS PAGAMENTOS
            BigDecimal totalPagamentosEfetivos = calcularTotalPagamentosEfetivos();
            PaymentStatus statusFinal;

            if (totalPagamentosEfetivos.compareTo(totalCompra) >= 0) {
                statusFinal = PaymentStatus.PAGO;
                System.out.println("✅ Compra será marcada como PAGA");
            } else if (totalPagamentosEfetivos.compareTo(BigDecimal.ZERO) > 0) {
                statusFinal = PaymentStatus.PARCIAL;
                System.out.println("🟡 Compra será marcada como PARCIAL");
            } else {
                statusFinal = PaymentStatus.PENDENTE;
                System.out.println("🟠 Compra será marcada como PENDENTE");
            }

            // Atualizar status da compra
            purchase.setPaymentStatus(statusFinal);

            // 🔹 SALVAR/ATUALIZAR COMPRA COM PAGAMENTOS
            response = purchaseController.atualizarCompraComPagamentos(purchase, pagamentos);

            if (response != null) {
                String statusMsg = String.format(
                        "Compra finalizada com sucesso!\n"
                        + "Número: %s\n"
                        + "Total: %s AOA\n"
                        + "Pago: %s AOA\n"
                        + "Status: %s",
                        response.getInvoiceNumber(),
                        df.format(totalCompra),
                        df.format(totalRegistado),
                        response.getPaymentStatus().getDescricao()
                );

                JOptionPane.showMessageDialog(this, statusMsg, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao finalizar compra: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

// ADICIONE ESTE MÉTODO SE NÃO EXISTIR:
    private BigDecimal calcularTotalPagamentosEfetivos() {
        return pagamentos.stream()
                .filter(p -> p.getStatus() == PaymentStatus.PAGO || p.getStatus() == PaymentStatus.PARCIAL)
                .map(PurchasePayment::getValorPago)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ==========================================================
    // 🔹 MÉTODOS AUXILIARES
    // ==========================================================
    private boolean validarDadosPagamento() {
        // Validar valor
        BigDecimal valor;
        try {
            valor = parseBigDecimal(jTextFieldPaymentValue.getText());
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this,
                        "O valor do pagamento deve ser maior que zero",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Valor do pagamento inválido",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar data
        String data = jFormattedTextFieldPaymentDate.getText();
        if (data == null || data.trim().isEmpty() || data.length() != 10) {
            JOptionPane.showMessageDialog(this,
                    "Data do pagamento inválida",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private PaymentMode obterPaymentModeFromString(String texto) {
        if (texto != null) {
            // Extrair código entre parênteses
            int start = texto.indexOf("(");
            int end = texto.indexOf(")");
            if (start != -1 && end != -1) {
                String codigo = texto.substring(start + 1, end).trim();
                return PaymentMode.fromCodigo(codigo);
            }
        }
        return PaymentMode.NU;
    }

    private PaymentStatus obterPaymentStatusFromString(String texto) {
        if (texto != null) {
            for (PaymentStatus status : PaymentStatus.values()) {
                if (status.getDescricao().equals(texto)) {
                    return status;
                }
            }
        }
        return PaymentStatus.PAGO;
    }

    private BigDecimal parseBigDecimal(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            // Remover formatação e converter
            String limpo = texto.replace(".", "").replace(",", ".");
            return new BigDecimal(limpo);
        } catch (Exception e) {
            throw new RuntimeException("Valor inválido: " + texto);
        }
    }

    private String gerarReferenciaPagamento() {
        return "PAY-PUR-" + purchase.getInvoiceNumber() + "-" + System.currentTimeMillis();
    }

    private void limparCamposPagamento() {
        jTextFieldPaymentValue.setText("0.00");
        jFormattedTextFieldPaymentDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        jComboBoxPaymentMode.setSelectedIndex(0);
        jComboBoxPaymentStatus.setSelectedItem(PaymentStatus.PAGO.getDescricao());
    }
//
//    /**
//     * Define a compra a ser paga
//     */
//    public void setCompra(Purchase compra) {
//        this.compra = compra;
//        carregarDadosCompra();
//    }
//
//    /**
//     * Retorna se o pagamento foi processado
//     */
//    public boolean isPagamentoProcessado() {
//        return pagamentoProcessado;
//    }
//
//    /**
//     * Carrega os dados da compra na interface
//     */
//    private void carregarDadosCompra() {
//        if (compra != null) {
//            // Preenche os campos com os dados da compra
//            jTextFieldCompraId.setText(String.valueOf(compra.getId()));
//            jTextFieldNumeroFatura.setText(compra.getInvoiceNumber());
//            jTextFieldFornecedor.setText(compra.getSupplier().getName());
//            jTextFieldTotal.setText(String.valueOf(compra.getTotalAmount()));
//            jTextFieldData.setText(compra.getPurchaseDate().toString());
//
//            // Aqui você pode adicionar mais campos conforme necessário
//        }
//    }
//
//    /**
//     * Processa o pagamento da compra
//     */
//    private void processarPagamento() {
//        try {
//            // Aqui você implementa a lógica de pagamento
//            // Exemplo: integração com gateway de pagamento, registro no banco, etc.
//
//            // Simulação de processamento bem-sucedido
//            boolean pagamentoSucesso = true; // Substitua pela lógica real
//
//            if (pagamentoSucesso) {
//                // Atualiza o status da compra para PAGA
//                PurchaseController purchaseController = new PurchaseController();
//                boolean atualizado = purchaseController.atualizarStatusCompra(compra.getId(), "PAGA");
//
//                if (atualizado) {
//                    pagamentoProcessado = true;
//                    JOptionPane.showMessageDialog(this,
//                            "Pagamento processado com sucesso!",
//                            "Sucesso",
//                            JOptionPane.INFORMATION_MESSAGE);
//                    this.dispose();
//                } else {
//                    JOptionPane.showMessageDialog(this,
//                            "Erro ao atualizar status da compra!",
//                            "Erro",
//                            JOptionPane.ERROR_MESSAGE);
//                }
//            } else {
//                JOptionPane.showMessageDialog(this,
//                        "Falha no processamento do pagamento!",
//                        "Erro",
//                        JOptionPane.ERROR_MESSAGE);
//            }
//
//        } catch (Exception e) {
//            System.err.println("❌ Erro ao processar pagamento: " + e.getMessage());
//            JOptionPane.showMessageDialog(this,
//                    "Erro ao processar pagamento: " + e.getMessage(),
//                    "Erro",
//                    JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    // Método chamado pelo botão de confirmar pagamento
//    private void jButtonConfirmarPagamentoActionPerformed(java.awt.event.ActionEvent evt) {
//        processarPagamento();
//    }
//
//    // Método chamado pelo botão de cancelar
//    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {
//        pagamentoProcessado = false;
//        this.dispose();
//    }

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
        jTablePayments = new javax.swing.JTable();
        jComboBoxPaymentMode = new javax.swing.JComboBox<>();
        jTextFieldPaymentValue = new javax.swing.JTextField();
        jButtonAddPaymentToTable = new javax.swing.JButton();
        jButtonFinishInvoice = new javax.swing.JButton();
        jLabelTitle = new javax.swing.JLabel();
        jComboBoxPaymentStatus = new javax.swing.JComboBox<>();
        jFormattedTextFieldPaymentDate = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldTotalPurchase = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldTotalPending = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldTotalPaid = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabelSupplierNif = new javax.swing.JLabel();
        jLabelSeller = new javax.swing.JLabel();
        jLabelSupplierName = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaNote = new javax.swing.JTextPane();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTablePayments.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTablePayments);

        jComboBoxPaymentMode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextFieldPaymentValue.setText("0.00");

        jButtonAddPaymentToTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Done.png"))); // NOI18N
        jButtonAddPaymentToTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddPaymentToTableActionPerformed(evt);
            }
        });

        jButtonFinishInvoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Done.png"))); // NOI18N
        jButtonFinishInvoice.setText("Finalizar Fatura");
        jButtonFinishInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFinishInvoiceActionPerformed(evt);
            }
        });

        jLabelTitle.setText("Pagamento da Fatura FT");

        jComboBoxPaymentStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        try {
            jFormattedTextFieldPaymentDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/20##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel2.setText("Status");

        jLabel3.setText("Data");

        jLabel4.setText("Modo");

        jLabel5.setText("Valor");

        jTextFieldTotalPurchase.setEditable(false);
        jTextFieldTotalPurchase.setText("0.00");

        jLabel6.setText("Valor Total Fatura");

        jTextFieldTotalPending.setEditable(false);
        jTextFieldTotalPending.setText("0.00");

        jLabel7.setText("Valor Pedente");

        jTextFieldTotalPaid.setEditable(false);
        jTextFieldTotalPaid.setText("0.00");

        jLabel8.setText("Total Registado");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Trash Can.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabelSupplierNif.setText("ClientNif");

        jLabelSeller.setText("Seller");

        jLabelSupplierName.setText("ClientName");

        jScrollPane2.setViewportView(jTextAreaNote);

        jLabel9.setText("Observacao");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane1)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jComboBoxPaymentStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jFormattedTextFieldPaymentDate, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton1))))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6)
                                .addComponent(jTextFieldTotalPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addGap(0, 75, Short.MAX_VALUE))
                                .addComponent(jTextFieldTotalPaid))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextFieldTotalPending, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7))
                            .addGap(115, 115, 115)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButtonAddPaymentToTable, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jComboBoxPaymentMode, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5)
                                .addComponent(jTextFieldPaymentValue, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jButtonFinishInvoice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabelSeller)
                            .addComponent(jLabelSupplierNif)
                            .addComponent(jLabelSupplierName))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(289, 289, 289)
                .addComponent(jLabelTitle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelTitle)
                        .addGap(23, 23, 23)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxPaymentMode, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldPaymentValue, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabelSeller)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelSupplierName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelSupplierNif)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonAddPaymentToTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jFormattedTextFieldPaymentDate)
                        .addComponent(jComboBoxPaymentStatus))
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldTotalPending, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldTotalPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonFinishInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldTotalPaid, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
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

        setSize(new java.awt.Dimension(857, 453));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAddPaymentToTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddPaymentToTableActionPerformed
        // TODO add your handling code here:
        adicionarPagamento();
    }//GEN-LAST:event_jButtonAddPaymentToTableActionPerformed

    private void jButtonFinishInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFinishInvoiceActionPerformed
        // TODO add your handling code here:
        finalizarPagamentos();
    }//GEN-LAST:event_jButtonFinishInvoiceActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        removerPagamento();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(JDialogPaymentPurchase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogPaymentPurchase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogPaymentPurchase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogPaymentPurchase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogPaymentPurchase dialog = new JDialogPaymentPurchase(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonAddPaymentToTable;
    private javax.swing.JButton jButtonFinishInvoice;
    private javax.swing.JComboBox<String> jComboBoxPaymentMode;
    private javax.swing.JComboBox<String> jComboBoxPaymentStatus;
    private javax.swing.JFormattedTextField jFormattedTextFieldPaymentDate;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelSeller;
    private javax.swing.JLabel jLabelSupplierName;
    private javax.swing.JLabel jLabelSupplierNif;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTablePayments;
    private javax.swing.JTextPane jTextAreaNote;
    private javax.swing.JTextField jTextFieldPaymentValue;
    private javax.swing.JTextField jTextFieldTotalPaid;
    private javax.swing.JTextField jTextFieldTotalPending;
    private javax.swing.JTextField jTextFieldTotalPurchase;
    // End of variables declaration//GEN-END:variables
}
