/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.okutonda.okudpdv.views.invoice;

import com.okutonda.okudpdv.controllers.InvoiceController;
import com.okutonda.okudpdv.controllers.PaymentController;
import com.okutonda.okudpdv.data.entities.DocumentType;
import com.okutonda.okudpdv.data.entities.Invoices;
import com.okutonda.okudpdv.data.entities.Payment;
import com.okutonda.okudpdv.data.entities.PaymentMode;
import com.okutonda.okudpdv.data.entities.PaymentStatus;
import com.okutonda.okudpdv.data.entities.User;
import com.okutonda.okudpdv.helpers.UserSession;
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
public class JDialogPaymentInvoice extends javax.swing.JDialog {

    private Invoices invoice;
    private Invoices response = null;
    private final DecimalFormat df = new DecimalFormat("#,##0.00");
    private final PaymentController paymentController = new PaymentController();
    private final InvoiceController invoiceController = new InvoiceController();
    private final UserSession userSession = UserSession.getInstance();

    private List<Payment> pagamentos = new ArrayList<>();
    private BigDecimal totalFatura = BigDecimal.ZERO;
    private BigDecimal totalRegistado = BigDecimal.ZERO;
    private BigDecimal totalPendente = BigDecimal.ZERO;

    /**
     * Creates new form JDialogPaymentInvoice
     */
    public JDialogPaymentInvoice(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        inicializarComponentes();
    }

    public Invoices getResponse() {
        return response;
    }

    public void setInvoice(Invoices inv) {
        this.invoice = inv;
        carregarDadosFatura();
        atualizarTotais();
    }

    private void inicializarComponentes() {
        // Configurar combobox de modos de pagamento
        jComboBoxMoePayment.removeAllItems();
        for (PaymentMode mode : PaymentMode.getActiveModes()) {
            jComboBoxMoePayment.addItem(mode.getDescricao() + " (" + mode.getCodigo() + ")");
        }

        // Configurar combobox de status
        jComboBoxStatusPayment.removeAllItems();
        for (PaymentStatus status : PaymentStatus.values()) {
            jComboBoxStatusPayment.addItem(status.getDescricao());
        }

        // Configurar data atual
        jFormattedTextField1.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        // Configurar tabela
        configurarTabela();
    }

    private void carregarDadosFatura() {
        if (invoice != null) {
            totalFatura = invoice.getTotal() != null ? invoice.getTotal() : BigDecimal.ZERO;

            jTextFieldTotalInvoice.setText(df.format(totalFatura));
            jLabel1.setText("Pagamento da Fatura " + invoice.getPrefix() + "/" + invoice.getNumber());

            if (invoice.getClient() != null) {
                jLabelClientName.setText(invoice.getClient().getName());
                jLabelClientNif.setText(invoice.getClient().getNif() != null ? invoice.getClient().getNif() : "N/A");
            }

            if (invoice.getSeller() != null) {
                jLabelSeller.setText(invoice.getSeller().getName());
            }

            if (invoice.getNote() != null) {
                jTextPaneNote.setText(invoice.getNote());
            }

            // Carregar pagamentos existentes
            carregarPagamentosExistentes();
        }
    }

    private void carregarPagamentosExistentes() {
        if (invoice != null && invoice.getId() != null) {
            List<Payment> pagamentosExistentes = paymentController.getByInvoiceId(invoice.getId());
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

        for (Payment pagamento : pagamentos) {
            model.addRow(new Object[]{
                pagamento.getPaymentMode().getDescricao(),
                pagamento.getStatus().getDescricao(),
                df.format(pagamento.getTotal()),
                pagamento.getDate(),
                pagamento.getReference()
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
            String modoSelecionado = (String) jComboBoxMoePayment.getSelectedItem();
            PaymentMode paymentMode = obterPaymentModeFromString(modoSelecionado);

            // Obter status selecionado
            String statusSelecionado = (String) jComboBoxStatusPayment.getSelectedItem();
            PaymentStatus paymentStatus = obterPaymentStatusFromString(statusSelecionado);

            // Obter valor
            BigDecimal valor = parseBigDecimal(jTextFieldValuePayment.getText());

            // Obter data
            String data = jFormattedTextField1.getText();

            // Criar pagamento
            Payment pagamento = new Payment();
            pagamento.setDescription("Pagamento fatura " + invoice.getPrefix() + "/" + invoice.getNumber());
            pagamento.setTotal(valor);
            pagamento.setPaymentMode(paymentMode);
            pagamento.setStatus(paymentStatus);
            pagamento.setDate(data);
            pagamento.setReference(gerarReferenciaPagamento());
            pagamento.setInvoiceId(invoice.getId());
            pagamento.setInvoiceType(DocumentType.FT);

            User usuarioLogado = userSession.getUser();
            if (usuarioLogado != null) {
                pagamento.setUser(usuarioLogado);
            }

            if (invoice.getClient() != null) {
                pagamento.setClient(invoice.getClient());
            }

            // Adicionar à lista
            pagamentos.add(pagamento);

            // Atualizar interface
            atualizarTabelaPagamentos();
            atualizarTotais();
            limparCamposPagamento();

            JOptionPane.showMessageDialog(this, "Pagamento adicionado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao adicionar pagamento: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
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

        for (Payment pagamento : pagamentos) {
            if (pagamento.getTotal() != null) {
                totalRegistado = totalRegistado.add(pagamento.getTotal());
            }
        }

        totalPendente = totalFatura.subtract(totalRegistado);

        jTextField1.setText(df.format(totalRegistado));
        jTextFieldTotalPedente.setText(df.format(totalPendente));

        // Atualizar cores baseadas no status
        if (totalPendente.compareTo(BigDecimal.ZERO) == 0) {
            jTextFieldTotalPedente.setBackground(new java.awt.Color(200, 255, 200)); // Verde claro
        } else if (totalPendente.compareTo(BigDecimal.ZERO) > 0) {
            jTextFieldTotalPedente.setBackground(new java.awt.Color(255, 255, 200)); // Amarelo
        } else {
            jTextFieldTotalPedente.setBackground(new java.awt.Color(255, 200, 200)); // Vermelho claro
        }
    }

    private void finalizarFatura() {
        try {
            if (pagamentos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Adicione pelo menos um pagamento antes de finalizar",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 🔹 REGRAS DE NEGÓCIO: Verificar status antes de salvar
            BigDecimal totalPagamentosEfetivos = calcularTotalPagamentosEfetivos();

            if (totalPagamentosEfetivos.compareTo(totalFatura) >= 0) {
                // Fatura será marcada como PAGA automaticamente
                System.out.println("✅ Fatura será marcada como PAGA");
            } else if (totalPagamentosEfetivos.compareTo(BigDecimal.ZERO) > 0) {
                // Fatura será marcada como PARCIAL automaticamente
                System.out.println("🟡 Fatura será marcada como PARCIAL");
            } else {
                // Fatura será marcada como PENDENTE automaticamente
                System.out.println("🟠 Fatura será marcada como PENDENTE");
            }

            // Salvar fatura com pagamentos (status será aplicado automaticamente)
            response = invoiceController.criarFaturaComProdutosEPagamentos(
                    invoice,
                    invoice.getProducts(),
                    pagamentos
            );

            if (response != null) {
                String statusMsg = String.format(
                        "Fatura finalizada com sucesso!\n"
                        + "Total: %s AOA\n"
                        + "Pago: %s AOA\n"
                        + "Pendente: %s AOA\n"
                        + "Status: %s",
                        df.format(totalFatura),
                        df.format(totalRegistado),
                        df.format(totalPendente),
                        response.getStatus().getDescricao()
                );

                JOptionPane.showMessageDialog(this, statusMsg, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao finalizar fatura: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private BigDecimal calcularTotalPagamentosEfetivos() {
        return pagamentos.stream()
                .filter(p -> p.getStatus() == PaymentStatus.PAGO || p.getStatus() == PaymentStatus.PARCIAL)
                .map(Payment::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ==========================================================
    // 🔹 MÉTODOS AUXILIARES
    // ==========================================================
    private boolean validarDadosPagamento() {
        // Validar valor
        BigDecimal valor;
        try {
            valor = parseBigDecimal(jTextFieldValuePayment.getText());
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
        String data = jFormattedTextField1.getText();
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
        return "PAY-" + invoice.getPrefix() + "-" + invoice.getNumber() + "-" + System.currentTimeMillis();
    }

    private void limparCamposPagamento() {
        jTextFieldValuePayment.setText("0.00");
        jFormattedTextField1.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        jComboBoxMoePayment.setSelectedIndex(0);
        jComboBoxStatusPayment.setSelectedItem(PaymentStatus.PAGO.getDescricao());
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
        jTablePayments = new javax.swing.JTable();
        jComboBoxMoePayment = new javax.swing.JComboBox<>();
        jTextFieldValuePayment = new javax.swing.JTextField();
        jButtonAddPaymentToTable = new javax.swing.JButton();
        jButtonFinishInvoice = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxStatusPayment = new javax.swing.JComboBox<>();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldTotalInvoice = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldTotalPedente = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabelClientNif = new javax.swing.JLabel();
        jLabelSeller = new javax.swing.JLabel();
        jLabelClientName = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPaneNote = new javax.swing.JTextPane();
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

        jComboBoxMoePayment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextFieldValuePayment.setText("0.00");

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

        jLabel1.setText("Pagamento da Fatura FT");

        jComboBoxStatusPayment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        try {
            jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/20##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel2.setText("Status");

        jLabel3.setText("Data");

        jLabel4.setText("Modo");

        jLabel5.setText("Valor");

        jTextFieldTotalInvoice.setEditable(false);
        jTextFieldTotalInvoice.setText("0.00");

        jLabel6.setText("Valor Total Fatura");

        jTextFieldTotalPedente.setEditable(false);
        jTextFieldTotalPedente.setText("0.00");

        jLabel7.setText("Valor Pedente");

        jTextField1.setEditable(false);
        jTextField1.setText("0.00");

        jLabel8.setText("Total Registado");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Trash Can.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabelClientNif.setText("ClientNif");

        jLabelSeller.setText("Seller");

        jLabelClientName.setText("ClientName");

        jScrollPane2.setViewportView(jTextPaneNote);

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
                                .addComponent(jComboBoxStatusPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton1))))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6)
                                .addComponent(jTextFieldTotalInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addGap(0, 75, Short.MAX_VALUE))
                                .addComponent(jTextField1))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextFieldTotalPedente, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7))
                            .addGap(115, 115, 115)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButtonAddPaymentToTable, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jComboBoxMoePayment, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5)
                                .addComponent(jTextFieldValuePayment, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jButtonFinishInvoice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabelSeller)
                            .addComponent(jLabelClientNif)
                            .addComponent(jLabelClientName))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(289, 289, 289)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addGap(23, 23, 23)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxMoePayment, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldValuePayment, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabelSeller)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelClientName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelClientNif)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonAddPaymentToTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jFormattedTextField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                            .addComponent(jComboBoxStatusPayment, javax.swing.GroupLayout.Alignment.LEADING)))
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldTotalPedente, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldTotalInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonFinishInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        setSize(new java.awt.Dimension(840, 484));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonFinishInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFinishInvoiceActionPerformed
        // TODO add your handling code here:
        finalizarFatura();
    }//GEN-LAST:event_jButtonFinishInvoiceActionPerformed

    private void jButtonAddPaymentToTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddPaymentToTableActionPerformed
        // TODO add your handling code here:
        adicionarPagamento();
    }//GEN-LAST:event_jButtonAddPaymentToTableActionPerformed

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
            java.util.logging.Logger.getLogger(JDialogPaymentInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogPaymentInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogPaymentInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogPaymentInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogPaymentInvoice dialog = new JDialogPaymentInvoice(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox<String> jComboBoxMoePayment;
    private javax.swing.JComboBox<String> jComboBoxStatusPayment;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelClientName;
    private javax.swing.JLabel jLabelClientNif;
    private javax.swing.JLabel jLabelSeller;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTablePayments;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextFieldTotalInvoice;
    private javax.swing.JTextField jTextFieldTotalPedente;
    private javax.swing.JTextField jTextFieldValuePayment;
    private javax.swing.JTextPane jTextPaneNote;
    // End of variables declaration//GEN-END:variables
}
