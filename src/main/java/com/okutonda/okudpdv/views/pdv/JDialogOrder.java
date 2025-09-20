/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.okutonda.okudpdv.views.pdv;

import com.okutonda.okudpdv.controllers.OrderController;
import com.okutonda.okudpdv.controllers.PaymentController;
import com.okutonda.okudpdv.controllers.PaymentModeController;
import com.okutonda.okudpdv.controllers.ShiftController;
import com.okutonda.okudpdv.controllers.UserController;
import com.okutonda.okudpdv.models.Order;
import com.okutonda.okudpdv.models.Payment;
import com.okutonda.okudpdv.models.PaymentMode;
import com.okutonda.okudpdv.models.PaymentStatus;
import com.okutonda.okudpdv.models.ProductOrder;
import com.okutonda.okudpdv.utilities.ShiftSession;
import com.okutonda.okudpdv.utilities.UtilDate;
import com.okutonda.okudpdv.utilities.UtilSales;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kenny
 */
public class JDialogOrder extends javax.swing.JDialog {

    Order order;
    OrderController orderController;
    PaymentController paymentController;
    PaymentModeController paymentModeController;
    UserController userController;
    ShiftController shiftController;
    ShiftSession shiftSession;
    Boolean response = false;
    Boolean statusClose = false;
    List<Payment> listPayment;

// ===== Pagamentos Dinâmicos (usar na tua JDialogOrder) =====
    private BigDecimal totalPedido = BigDecimal.ZERO;
    private final java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0.00");

    /**
     * Creates new form JDialogOrder
     */
    public JDialogOrder(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        orderController = new OrderController();
        paymentController = new PaymentController();
        paymentModeController = new PaymentModeController();
        listPayment = new ArrayList<>();
        userController = new UserController();
        shiftController = new ShiftController();
        shiftSession = ShiftSession.getInstance();

        // Atalhos globais
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        // F12 → Finalizar Venda
        im.put(KeyStroke.getKeyStroke("F12"), "finalizarVenda");
        am.put("finalizarVenda", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalizarVenda();
            }
        });
    }

    private void finalizarVenda() {
        // evita duplo clique
        jButtonSaveOrder.setEnabled(false);

        try {
            // 1) Garantir que recalculamos a lista de pagamentos a partir da UI
            rebuildPaymentsFromUI();

            // 2) Validar restante
            BigDecimal valorRestante = parse(jTextFieldValorRestante.getText()).setScale(2, RoundingMode.HALF_UP);
            if (valorRestante.compareTo(BigDecimal.ZERO) > 0) {
                JOptionPane.showMessageDialog(this, "VALOR EM FALTA: " + format(valorRestante), "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 3) Validar que tem ao menos um pagamento > 0
            if (listPayment == null || listPayment.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione pelo menos um método de pagamento.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 4) Soma pagamentos e calcular troco
            BigDecimal totalPedido = parse(jTextFieldTotalOrder.getText()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal somaPag = listPayment.stream()
                    .map(Payment::getTotal)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal troco = somaPag.subtract(totalPedido);
            if (troco.compareTo(BigDecimal.ZERO) < 0) {
                troco = BigDecimal.ZERO;
            }

            // 5) Preencher order com valores de pagamento
            order.setPayTotal(somaPag.doubleValue());
            order.setAmountReturned(troco.doubleValue());
            order.setNote(jTextPaneNote.getText()); // se tiver observação

            // 6) Completar metadados opcionais dos pagamentos (cliente, operador, datas)
            String nowIso = UtilDate.getFormatDataNow();
            for (Payment p : listPayment) {
                p.setClient(order.getClient());
                p.setUser(order.getSeller());
                p.setDate(nowIso);
                p.setDescription(p.getPaymentMode().name()); // opcional
            }

            // 7) Persistir: order + itens + pagamentos (numeração/hash no controller)
            Order salvo = orderController.criarEFinalizarComPagamentos(order, listPayment);
            if (salvo != null && salvo.getId() > 0) {
                JOptionPane.showMessageDialog(this, "Venda gravada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                this.response = true;  // se o diálogo usa getResponse()
                this.statusClose = true;
                UtilSales.print(order);
//             try {
//            // TODO add your handling code here:
//                UtilSales.PrintOrderTicket(order);
//            } catch (PrinterException ex) {
//                Logger.getLogger(JDialogOrder.class.getName()).log(Level.SEVERE, null, ex);
//            }

                dispose();             // fecha o diálogo
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao gravar a venda.", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao gravar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            jButtonSaveOrder.setEnabled(true);
        }
    }

    public void listProdutsOrder() {
        DefaultTableModel data = (DefaultTableModel) jTableProducts.getModel();
        data.setNumRows(0);
        for (ProductOrder c : order.getProducts()) {
            data.addRow(new Object[]{
                c.getProduct().getId(),
                c.getCode(),
                c.getDescription(),
                c.getPrice(),
                c.getTaxePercentage(),
                c.getQty(),
                //                c.getQty() * c.getPrice()
                c.getPrice().multiply(BigDecimal.valueOf(c.getQty()))
            }
            );
        }
    }

    public Boolean getResponse() {
        return response;
    }

    public void setOrder(Order order) {
        this.order = order;
        totalPedido = (order.getTotal() == null) ? BigDecimal.ZERO : new BigDecimal(order.getTotal().toString());

//        DecimalFormat df = new DecimalFormat("#,##0.00");
        Double sub = order.getSubTotal();
        Double tax = order.getTotalTaxe();
        Double tot = order.getTotal();

        jTextFieldSubTotal.setText(sub != null ? df.format(sub) : "0,00");
        jTextFieldImposto.setText(tax != null ? df.format(tax) : "0,00");
        jTextFieldTotalOrder.setText(tot != null ? df.format(tot) : "0,00");

        jLabelSeller.setText(order.getSeller().getName());
        jLabelClientName.setText(order.getClient().getName());
        jLabelClientNif.setText(order.getClient().getNif());
        jTextPaneNote.setText(order.getNote());
//        System.out.println("foi chamado...");
        // inicia a gestão dinâmica de pagamento
        setupPagamentoUI();
    }

    public void cancelOrder() {
        JPasswordField passwordFild = new JPasswordField(10);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Entre com o codigo do Supervisor: "));
        panel.add(passwordFild);
        int option = JOptionPane.showConfirmDialog(null, panel, "Senha", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            char[] password = passwordFild.getPassword();
            String paString = new String(password);
            if (!paString.isEmpty()) {
                statusClose = userController.validateManager(paString);
                System.out.println("codigo do supervisor:" + statusClose);
                if (statusClose == true) {
                    dispose();
                }
            }
        }
    }

    private void rebuildPaymentsFromUI() {
        List<Payment> nova = new ArrayList<>();

        if (jCheckBoxNumerario.isSelected()) {
            BigDecimal v = parse(jTextFieldPayNumerario.getText()).setScale(2, RoundingMode.HALF_UP);
            if (v.signum() > 0) {
                Payment p = new Payment();
                p.setPaymentMode(PaymentMode.NUMERARIO);
                p.setStatus(PaymentStatus.SUCCESS);
                p.setTotal(v);
                p.setCurrency("AOA");
                nova.add(p);
            }
        }
        if (jCheckBoxMulticaixa.isSelected()) {
            BigDecimal v = parse(jTextFieldPayMulticaixa.getText()).setScale(2, RoundingMode.HALF_UP);
            if (v.signum() > 0) {
                Payment p = new Payment();
                p.setPaymentMode(PaymentMode.MULTICAIXA);
                p.setStatus(PaymentStatus.SUCCESS);
                p.setTotal(v);
                p.setCurrency("AOA");
                // p.setReference(jTextFieldNSU.getText()); // se tiver
                nova.add(p);
            }
        }
        if (jCheckBoxTransferencia.isSelected()) {
            BigDecimal v = parse(jTextFieldPayTransferencia.getText()).setScale(2, RoundingMode.HALF_UP);
            if (v.signum() > 0) {
                Payment p = new Payment();
                p.setPaymentMode(PaymentMode.TRANSFERENCIA);
                p.setStatus(PaymentStatus.SUCCESS);
                p.setTotal(v);
                p.setCurrency("AOA");
                // p.setReference(jTextFieldRefBancaria.getText());
                nova.add(p);
            }
        }
        if (jCheckBoxOutros.isSelected()) {
            BigDecimal v = parse(jTextFieldPayOutros.getText()).setScale(2, RoundingMode.HALF_UP);
            if (v.signum() > 0) {
                Payment p = new Payment();
                p.setPaymentMode(PaymentMode.OUTROS);
                p.setStatus(PaymentStatus.SUCCESS);
                p.setTotal(v);
                p.setCurrency("AOA");
                nova.add(p);
            }
        }

        this.listPayment = nova; // substitui a lista inteira conforme estado atual da UI
    }

    // Chama isto no setOrder(order) depois de setar jTextFieldTotalOrder:
    private void setupPagamentoUI() {
        // estado inicial
        jCheckBoxNumerario.setSelected(true);
        jCheckBoxMulticaixa.setSelected(false);
        jCheckBoxTransferencia.setSelected(false);
        jCheckBoxOutros.setSelected(false);

        jTextFieldPayNumerario.setText(format(totalPedido));
        jTextFieldPayMulticaixa.setText("");
        jTextFieldPayTransferencia.setText("");
        jTextFieldPayOutros.setText("");

        syncEnabledFromSelection();
        recalcPagamento();
//
        addSelectionListeners();
        addAmountListeners();

        // botão de “Calcular Parcelas”
//        jButtonCalcularParcelas.addActionListener(e -> {
//            distribuirParcelasIguais();
//            recalcPagamento();
//        });
    }

    private void addSelectionListeners() {
//        System.out.println("addSelectionListeners");
        java.awt.event.ItemListener il = e -> {
            syncEnabledFromSelection();
            // quando ativar um método vazio, sugerir restante
            sugerirRestanteNoMetodoAtivado();
            recalcPagamento();
        };
        jCheckBoxNumerario.addItemListener(il);
        jCheckBoxMulticaixa.addItemListener(il);
        jCheckBoxTransferencia.addItemListener(il);
        jCheckBoxOutros.addItemListener(il);
    }

    private void addAmountListeners() {
//        System.out.println("addAmountListeners");
        javax.swing.event.DocumentListener doc = new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                recalcPagamento();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                recalcPagamento();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                recalcPagamento();
            }
        };
        jTextFieldPayNumerario.getDocument().addDocumentListener(doc);
        jTextFieldPayMulticaixa.getDocument().addDocumentListener(doc);
        jTextFieldPayTransferencia.getDocument().addDocumentListener(doc);
        jTextFieldPayOutros.getDocument().addDocumentListener(doc);

    }

    private void syncEnabledFromSelection() {
//        System.out.println("syncEnabledFromSelection 1");
        jTextFieldPayNumerario.setEnabled(jCheckBoxNumerario.isSelected());
        jTextFieldPayMulticaixa.setEnabled(jCheckBoxMulticaixa.isSelected());
        jTextFieldPayTransferencia.setEnabled(jCheckBoxTransferencia.isSelected());
        jTextFieldPayOutros.setEnabled(jCheckBoxOutros.isSelected());

        if (!jCheckBoxNumerario.isSelected()) {
            jTextFieldPayNumerario.setText("");
        }
        if (!jCheckBoxMulticaixa.isSelected()) {
            jTextFieldPayMulticaixa.setText("");
        }
        if (!jCheckBoxTransferencia.isSelected()) {
            jTextFieldPayTransferencia.setText("");
        }
        if (!jCheckBoxOutros.isSelected()) {
            jTextFieldPayOutros.setText("");
        }
    }

    private void sugerirRestanteNoMetodoAtivado() {
        System.out.println("sugerirRestanteNoMetodoAtivado");
        BigDecimal restante = calcRestante();
        if (restante.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        if (jCheckBoxNumerario.isSelected() && isEmpty(jTextFieldPayNumerario)) {
            jTextFieldPayNumerario.setText(format(restante));
            return;
        }
        if (jCheckBoxMulticaixa.isSelected() && isEmpty(jTextFieldPayMulticaixa)) {
            jTextFieldPayMulticaixa.setText(format(restante));
            return;
        }
        if (jCheckBoxTransferencia.isSelected() && isEmpty(jTextFieldPayTransferencia)) {
            jTextFieldPayTransferencia.setText(format(restante));
            return;
        }
        if (jCheckBoxOutros.isSelected() && isEmpty(jTextFieldPayOutros)) {
            jTextFieldPayOutros.setText(format(restante));
        }
    }

    private void recalcPagamento() {
//        System.out.println("recalcPagamento");
        BigDecimal soma = BigDecimal.ZERO;
//
//        listPayment = null;
//        listPayment = new ArrayList<>();
//        Payment pay;
//        System.out.println("jTextFieldPayNumerario " + jTextFieldPayNumerario.getText());

        if (jCheckBoxNumerario.isSelected()) {
            soma = soma.add(parse(jTextFieldPayNumerario.getText()));
//            pay = new Payment();
////            pay.setPaymentMode(paymentMode);
////            String value = jTextFieldPayNumerario.getText();
//            pay.setTotal(parseDouble(jTextFieldPayNumerario.getText()));
//            updatePaymentToList(pay, "numerario");
        }
        if (jCheckBoxMulticaixa.isSelected()) {
            soma = soma.add(parse(jTextFieldPayMulticaixa.getText()));
//            pay = new Payment();
////            pay.setPaymentMode(paymentMode);
//            pay.setTotal(parseDouble(jTextFieldPayMulticaixa.getText()));
//            updatePaymentToList(pay, "Multicaixa");
        }
        if (jCheckBoxTransferencia.isSelected()) {
            soma = soma.add(parse(jTextFieldPayTransferencia.getText()));
//            pay = new Payment();
////            pay.setPaymentMode(paymentMode);
//            pay.setTotal(parseDouble(jTextFieldPayTransferencia.getText()));
//            updatePaymentToList(pay, "Transferencia");
        }
        if (jCheckBoxOutros.isSelected()) {
            soma = soma.add(parse(jTextFieldPayOutros.getText()));
//            pay = new Payment();
////            pay.setPaymentMode(paymentMode);
//            pay.setTotal(parseDouble(jTextFieldPayOutros.getText()));
//            updatePaymentToList(pay, "Outros");
        }

        BigDecimal troco = soma.subtract(totalPedido);
        if (troco.compareTo(BigDecimal.ZERO) < 0) {
            troco = BigDecimal.ZERO;
        }

        BigDecimal restante = totalPedido.subtract(soma);
        if (restante.compareTo(BigDecimal.ZERO) < 0) {
            restante = BigDecimal.ZERO;
        }
//        System.out.println("soma" + soma);
//        System.out.println("totalPedido" + totalPedido);
//        System.out.println("restante" + restante);
        jTextFieldTroco.setText(format(troco));
        jTextFieldValorRestante.setText(format(restante));
        rebuildPaymentsFromUI();
    }

    private BigDecimal calcRestante() {
        System.out.println("calcRestante");
        BigDecimal soma = BigDecimal.ZERO;
        if (jCheckBoxNumerario.isSelected()) {
            soma = soma.add(parse(jTextFieldPayNumerario.getText()));
        }
        if (jCheckBoxMulticaixa.isSelected()) {
            soma = soma.add(parse(jTextFieldPayMulticaixa.getText()));
        }
        if (jCheckBoxTransferencia.isSelected()) {
            soma = soma.add(parse(jTextFieldPayTransferencia.getText()));
        }
        if (jCheckBoxOutros.isSelected()) {
            soma = soma.add(parse(jTextFieldPayOutros.getText()));
        }
        BigDecimal restante = totalPedido.subtract(soma);
        return (restante.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : restante;
    }

    /**
     * Divide o VALOR RESTANTE igualmente pelos métodos selecionados. Se já
     * existir algum valor digitado, ele é sobrescrito.
     */
    private void distribuirParcelasIguais() {
        System.out.println("distribuirParcelasIguais");
        java.util.List<javax.swing.JCheckBox> checks = java.util.Arrays.asList(
                jCheckBoxNumerario, jCheckBoxMulticaixa, jCheckBoxTransferencia, jCheckBoxOutros
        );
        java.util.List<javax.swing.JTextField> fields = java.util.Arrays.asList(
                jTextFieldPayNumerario, jTextFieldPayMulticaixa, jTextFieldPayTransferencia, jTextFieldPayOutros
        );

        // quais estão selecionados?
        java.util.List<javax.swing.JTextField> ativos = new java.util.ArrayList<>();
        for (int i = 0; i < checks.size(); i++) {
            if (checks.get(i).isSelected()) {
//                System.out.println("checks: " + checks.get(i).getText());
                ativos.add(fields.get(i));
            }
        }

        if (ativos.isEmpty()) {
            return;
        }

        // valor a repartir = restante (se zero, tenta repartir o total)
        BigDecimal restante = calcRestante();
//        System.out.println("calcRestante: " + restante);

        BigDecimal base = (restante.compareTo(BigDecimal.ZERO) > 0) ? restante : totalPedido;

        BigDecimal qtd = new BigDecimal(ativos.size());
        BigDecimal parte = base.divide(qtd, 2, java.math.RoundingMode.DOWN);
        BigDecimal acumulado = BigDecimal.ZERO;

        // limpa todos selecionados e reparte
        for (int i = 0; i < ativos.size(); i++) {
            javax.swing.JTextField tf = ativos.get(i);
            BigDecimal valor = (i == ativos.size() - 1) ? base.subtract(acumulado) : parte;
            tf.setText(format(valor));
            acumulado = acumulado.add(valor);
        }
    }

// ===== Helpers =====
    private boolean isEmpty(javax.swing.JTextField tf) {
        String s = (tf.getText() == null) ? "" : tf.getText().trim();
        return s.isEmpty();
    }

    private BigDecimal parse(String s) {
        if (s == null) {
            return BigDecimal.ZERO;
        }
        s = s.trim();
        if (s.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // 🔑 remove caracteres “estranhos” (espaços, U+FFFD, etc.)
        s = s.replaceAll("[^0-9,.-]", "");

        // normaliza decimal
        if (s.contains(",") && s.contains(".")) {
            // caso "10.000,50"
            s = s.replace(".", "").replace(",", ".");
        } else if (s.contains(",")) {
            s = s.replace(",", ".");
        }

        try {
            return new BigDecimal(s);
        } catch (Exception e) {
            System.err.println("Falha ao parsear valor: " + s);
            return BigDecimal.ZERO;
        }
    }

    private String format(BigDecimal v) {
        if (v == null) {
            return "0,00";
        }
        return df.format(v);
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabelSeller = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableProducts = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPaneNote = new javax.swing.JTextPane();
        jButtonSaveOrder = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabelClientNif = new javax.swing.JLabel();
        jLabelClientName = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jTextFieldPayTransferencia = new javax.swing.JTextField();
        jTextFieldPayOutros = new javax.swing.JTextField();
        jTextFieldValorRestante = new javax.swing.JTextField();
        jTextFieldPayNumerario = new javax.swing.JTextField();
        jTextFieldPayMulticaixa = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextFieldTroco = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jCheckBoxNumerario = new javax.swing.JCheckBox();
        jCheckBoxMulticaixa = new javax.swing.JCheckBox();
        jCheckBoxTransferencia = new javax.swing.JCheckBox();
        jCheckBoxOutros = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jTextFieldSubTotal = new javax.swing.JTextField();
        jTextFieldDesconto = new javax.swing.JTextField();
        jTextFieldTotalOrder = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldImposto = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jButtonCalcularParcelas = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 102));
        jLabel1.setText("DETALHES DA FATURA");

        jLabelSeller.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelSeller.setForeground(new java.awt.Color(0, 0, 102));
        jLabelSeller.setText("Nome do vendedor");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 102));
        jLabel3.setText("Vendedor:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 172, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelSeller, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(200, 200, 200))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabelSeller)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Lista de produtos");

        jTableProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Codigo", "Nome", "Preço", "Imposto", "Qtd", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableProducts);

        jScrollPane2.setViewportView(jTextPaneNote);

        jButtonSaveOrder.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonSaveOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/printer_8139457.png"))); // NOI18N
        jButtonSaveOrder.setText("FINALIZAR F12");
        jButtonSaveOrder.setToolTipText("FINALIZAR FATURA");
        jButtonSaveOrder.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonSaveOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveOrderActionPerformed(evt);
            }
        });

        jButtonClose.setForeground(new java.awt.Color(255, 0, 0));
        jButtonClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Cancel.png"))); // NOI18N
        jButtonClose.setText("Cancelar");
        jButtonClose.setToolTipText("Cancelar a Fatura");
        jButtonClose.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Observacao");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabelClientNif.setBackground(new java.awt.Color(255, 255, 255));
        jLabelClientNif.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelClientNif.setForeground(new java.awt.Color(0, 0, 102));
        jLabelClientNif.setText("Nif do cliente");

        jLabelClientName.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelClientName.setForeground(new java.awt.Color(0, 0, 102));
        jLabelClientName.setText("Nome do cliente");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 102));
        jLabel4.setText("Cliente:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 102));
        jLabel5.setText("NIF:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelClientNif, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelClientName, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelClientName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelClientNif))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextFieldPayTransferencia.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTextFieldPayTransferencia.setText("0.00");

        jTextFieldPayOutros.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTextFieldPayOutros.setText("0.00");

        jTextFieldValorRestante.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jTextFieldValorRestante.setText("0.00");
        jTextFieldValorRestante.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        jTextFieldValorRestante.setEnabled(false);
        jTextFieldValorRestante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldValorRestanteActionPerformed(evt);
            }
        });

        jTextFieldPayNumerario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTextFieldPayNumerario.setText("0.00");
        jTextFieldPayNumerario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldPayNumerarioActionPerformed(evt);
            }
        });
        jTextFieldPayNumerario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldPayNumerarioKeyReleased(evt);
            }
        });

        jTextFieldPayMulticaixa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTextFieldPayMulticaixa.setText("0.00");
        jTextFieldPayMulticaixa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldPayMulticaixaActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Valor Restante");

        jTextFieldTroco.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTextFieldTroco.setText("0.00");
        jTextFieldTroco.setEnabled(false);
        jTextFieldTroco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldTrocoActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("Troco");

        jCheckBoxNumerario.setText("Numerario");

        jCheckBoxMulticaixa.setText("Multicaixa");

        jCheckBoxTransferencia.setText("Transferência Bancária");

        jCheckBoxOutros.setText("Outros");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxMulticaixa)
                    .addComponent(jCheckBoxTransferencia)
                    .addComponent(jCheckBoxNumerario)
                    .addComponent(jCheckBoxOutros)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldTroco)
                    .addComponent(jTextFieldPayTransferencia)
                    .addComponent(jTextFieldPayOutros)
                    .addComponent(jTextFieldValorRestante)
                    .addComponent(jTextFieldPayMulticaixa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                    .addComponent(jTextFieldPayNumerario, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldPayNumerario, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxNumerario))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldPayMulticaixa, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxMulticaixa, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldPayTransferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxTransferencia))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldPayOutros, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxOutros))
                .addGap(19, 19, 19)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldValorRestante, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldTroco, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addContainerGap())
        );

        jTextFieldSubTotal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTextFieldSubTotal.setText("0.00");
        jTextFieldSubTotal.setEnabled(false);

        jTextFieldDesconto.setBackground(new java.awt.Color(255, 255, 153));
        jTextFieldDesconto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTextFieldDesconto.setText("0.00");

        jTextFieldTotalOrder.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jTextFieldTotalOrder.setForeground(new java.awt.Color(0, 0, 102));
        jTextFieldTotalOrder.setText("0.00");
        jTextFieldTotalOrder.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        jTextFieldTotalOrder.setEnabled(false);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Sub-Total");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("Desconto");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setText("Total a Pagar");

        jTextFieldImposto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTextFieldImposto.setText("0.00");
        jTextFieldImposto.setEnabled(false);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setText("Total imposto");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addComponent(jLabel13)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextFieldTotalOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel15)
                                .addComponent(jLabel12))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextFieldDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldImposto, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(21, 21, 21))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldImposto, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextFieldDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jTextFieldTotalOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Pagamento");

        jButtonCalcularParcelas.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonCalcularParcelas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Puzzle.png"))); // NOI18N
        jButtonCalcularParcelas.setText("Calcular Parcelas");
        jButtonCalcularParcelas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonCalcularParcelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCalcularParcelasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonClose)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel14)
                            .addGap(167, 167, 167)
                            .addComponent(jButtonCalcularParcelas)
                            .addGap(85, 85, 85))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonSaveOrder, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel9))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jButtonCalcularParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonSaveOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonClose, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 864, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(864, 569));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        listProdutsOrder();
        jLabelSeller.setText(order.getSeller().getName());
        jLabelClientName.setText(order.getClient().getName());
        jLabelClientNif.setText(order.getClient().getNif());
        jTextPaneNote.setText(order.getNote());
    }//GEN-LAST:event_formWindowActivated

    private void jButtonSaveOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveOrderActionPerformed

        finalizarVenda();
    }//GEN-LAST:event_jButtonSaveOrderActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        // TODO add your handling code here:
        cancelOrder();
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        if (statusClose == false) {
            this.setVisible(true);
        }
        System.out.println("Close tela");
//        this.setVisible(true);
    }//GEN-LAST:event_formWindowClosed

    private void jTextFieldValorRestanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldValorRestanteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldValorRestanteActionPerformed

    private void jTextFieldPayMulticaixaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldPayMulticaixaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldPayMulticaixaActionPerformed

    private void jTextFieldPayNumerarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldPayNumerarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldPayNumerarioActionPerformed

    private void jTextFieldTrocoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldTrocoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldTrocoActionPerformed

    private void jTextFieldPayNumerarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPayNumerarioKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextFieldPayNumerarioKeyReleased

    private void jButtonCalcularParcelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCalcularParcelasActionPerformed
        // TODO add your handling code here:
//        System.out.println("jButtonCalcularParcelasActionPerformed: ");
        distribuirParcelasIguais();
        recalcPagamento();
    }//GEN-LAST:event_jButtonCalcularParcelasActionPerformed

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
            java.util.logging.Logger.getLogger(JDialogOrder.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogOrder.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogOrder.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogOrder.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogOrder dialog = new JDialogOrder(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonCalcularParcelas;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonSaveOrder;
    private javax.swing.JCheckBox jCheckBoxMulticaixa;
    private javax.swing.JCheckBox jCheckBoxNumerario;
    private javax.swing.JCheckBox jCheckBoxOutros;
    private javax.swing.JCheckBox jCheckBoxTransferencia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelClientName;
    private javax.swing.JLabel jLabelClientNif;
    private javax.swing.JLabel jLabelSeller;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableProducts;
    private javax.swing.JTextField jTextFieldDesconto;
    private javax.swing.JTextField jTextFieldImposto;
    private javax.swing.JTextField jTextFieldPayMulticaixa;
    private javax.swing.JTextField jTextFieldPayNumerario;
    private javax.swing.JTextField jTextFieldPayOutros;
    private javax.swing.JTextField jTextFieldPayTransferencia;
    private javax.swing.JTextField jTextFieldSubTotal;
    private javax.swing.JTextField jTextFieldTotalOrder;
    private javax.swing.JTextField jTextFieldTroco;
    private javax.swing.JTextField jTextFieldValorRestante;
    private javax.swing.JTextPane jTextPaneNote;
    // End of variables declaration//GEN-END:variables
}
