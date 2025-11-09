/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.okutonda.okudpdv.views.payments;

import com.okutonda.okudpdv.controllers.FinanceController;
import com.okutonda.okudpdv.data.entities.Expense;
import com.okutonda.okudpdv.data.entities.Payment;
import com.okutonda.okudpdv.ui.TemaUI;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hr
 */
public class JPanelFluxoBox extends javax.swing.JPanel {

    private FinanceController financeController = new FinanceController();
    private DefaultTableModel tableModelReceitas;
    private DefaultTableModel tableModelDespesas;

    /**
     * Creates new form JPanelFluxoBox
     */
    public JPanelFluxoBox() {
        initComponents();
        applyTheme();
        initTableModels();
        setupDateFields();
        listReceitas();
        listDespesas();
    }

    private void applyTheme() {
//        TemaCleaner.clearBuilderOverrides(getContentPane());
        // Painel de fundo da janela
//        jPanelSidebar.setBackground(TemaCores.BG_LIGHT);

//        TemaUI.aplicarTitulo(jLabelJpanelSelected);
//        jPanelSidebar.setBackground(TemaCores.PRIMARY);
        // Card do login
//        TemaUI.aplicarPainelHeader(jPanelSidebar, TemaCores.PRIMARY);
        // Título
//        TemaUI.aplicarTitulo(jLabelNameCompany);
//        jLabelNameCompany.setForeground(TemaCores.PRIMARY);
        // Labels
//        jLabel1.setForeground(TemaCores.TEXT_DARK);   // "Email:"
//        jLabel2.setForeground(TemaCores.TEXT_DARK);   // "Senha:"
        // Campos
//        TemaUI.aplicarCampoTexto(jTextFieldEmail);
//        TemaUI.aplicarCampoTexto(jPasswordFieldPassword);

        // Botões
//        TemaUI.aplicarBotaoPrimario(jButton1);
//        TemaUI.aplicarBotaoPrimario(jButton2);
//        TemaUI.aplicarBotaoPrimario(jButton3);
//        TemaUI.aplicarBotaoPrimario(jButton4);
//        TemaUI.aplicarBotaoPrimario(jButtonInventoryReport);

//        TemaUI.aplicarBotaoPrimario(jButtonLogin);
//        jButtonSuport.setForeground(TemaCores.TEXT_GRAY);
//        jButtonAbout.setForeground(TemaCores.TEXT_GRAY);
//        jButtonInstall.setForeground(TemaCores.PRIMARY);
//        jButtonCloseScreen.setForeground(TemaCores.ERROR);
        // Status de BD (cor dinâmica) — chama depois de testar a conexão
//        updateDbStatusLabel(this.conn != null);
        // Borda superior/rodapé (opcional)
        // getRootPane().setBorder(new javax.swing.border.MatteBorder(0, 0, 2, 0, TemaCores.PRIMARY));
        // Se o GUI Builder deixou cores hardcoded em initComponents,
        // isso aqui sobrescreve. Se puder, remova as cores fixas no builder.
    }

    /**
     * Inicializa os modelos da tabela
     */
    private void initTableModels() {
        // Modelo para receitas
        tableModelReceitas = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Data", "Número", "Cliente", "Conta", "Tipo", "Valor", "Forma Pagamento"
                }
        ) {
            Class[] types = new Class[]{
                String.class, String.class, String.class, String.class, String.class, Double.class, String.class
            };
            boolean[] canEdit = new boolean[]{false, false, false, false, false, false, false};

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        jTableFinanceCashFlowReceitas.setModel(tableModelReceitas);

        // Modelo para despesas
        tableModelDespesas = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Data", "Nº Doc", "Descrição", "Fornecedor", "Categoria", "Conta", "Valor", "Forma Pagamento", "Usuário", "Notas"
                }
        ) {
            Class[] types = new Class[]{
                String.class, String.class, String.class, String.class, String.class,
                String.class, Double.class, String.class, String.class, String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false, false, false
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
        jTableFinanceCashFlowDespesas.setModel(tableModelDespesas);
    }

    /**
     * Configura as datas iniciais
     */
    private void setupDateFields() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Receitas
        jFormattedTextField9.setText(firstDayOfMonth.format(formatter));
        jFormattedTextField10.setText(today.format(formatter));

        // Despesas
        jFormattedTextField11.setText(firstDayOfMonth.format(formatter));
        jFormattedTextField12.setText(today.format(formatter));
    }

    /**
     * Lista receitas
     */
    public void listReceitas() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateFrom = LocalDate.parse(jFormattedTextField9.getText(), formatter);
            LocalDate dateTo = LocalDate.parse(jFormattedTextField10.getText(), formatter);

            List<Payment> receitas = financeController.getReceitas(dateFrom, dateTo);
            loadListReceitas(receitas);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar receitas: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carrega receitas na tabela
     */
    public void loadListReceitas(List<Payment> list) {
        tableModelReceitas.setRowCount(0);

        for (Payment p : list) {
            tableModelReceitas.addRow(new Object[]{
                p.getDate(),
                p.getPrefix() + "-" + p.getNumber(),
                (p.getClient() != null ? p.getClient().getName() : ""),
                "Caixa Loja",
                "Receita",
                p.getTotal().doubleValue(),
                p.getPaymentMode() != null ? p.getPaymentMode().getDescricao() : ""
            });
        }

        adjustTableColumnsReceitas();
    }

    /**
     * Ajusta colunas da tabela de receitas
     */
    private void adjustTableColumnsReceitas() {
        jTableFinanceCashFlowReceitas.getColumnModel().getColumn(0).setPreferredWidth(80);  // Data
        jTableFinanceCashFlowReceitas.getColumnModel().getColumn(1).setPreferredWidth(100); // Número
        jTableFinanceCashFlowReceitas.getColumnModel().getColumn(2).setPreferredWidth(150); // Cliente
        jTableFinanceCashFlowReceitas.getColumnModel().getColumn(3).setPreferredWidth(100); // Conta
        jTableFinanceCashFlowReceitas.getColumnModel().getColumn(4).setPreferredWidth(80);  // Tipo
        jTableFinanceCashFlowReceitas.getColumnModel().getColumn(5).setPreferredWidth(80);  // Valor
        jTableFinanceCashFlowReceitas.getColumnModel().getColumn(6).setPreferredWidth(100); // Forma Pagamento
    }

    /**
     * Lista despesas
     */
    public void listDespesas() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateFrom = LocalDate.parse(jFormattedTextField11.getText(), formatter);
            LocalDate dateTo = LocalDate.parse(jFormattedTextField12.getText(), formatter);

            List<Expense> despesas = financeController.getDespesas(dateFrom, dateTo);
            loadListDespesas(despesas);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar despesas: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carrega despesas na tabela
     */
    public void loadListDespesas(List<Expense> list) {
        tableModelDespesas.setRowCount(0);

        for (Expense e : list) {
            tableModelDespesas.addRow(new Object[]{
                e.getDate(),
                (e.getId() != null ? "DP-" + e.getId() : ""),
                e.getDescription(),
                (e.getSupplier() != null ? e.getSupplier().getName() : ""),
                (e.getCategory() != null ? e.getCategory().getName() : ""),
                "Caixa Loja",
                e.getTotal().doubleValue(),
                e.getMode(),
                (e.getUser() != null ? e.getUser().getName() : ""),
                e.getNotes()
            });
        }

        adjustTableColumnsDespesas();
    }

    /**
     * Ajusta colunas da tabela de despesas
     */
    private void adjustTableColumnsDespesas() {
        jTableFinanceCashFlowDespesas.getColumnModel().getColumn(0).setPreferredWidth(80);  // Data
        jTableFinanceCashFlowDespesas.getColumnModel().getColumn(1).setPreferredWidth(80);  // Nº Doc
        jTableFinanceCashFlowDespesas.getColumnModel().getColumn(2).setPreferredWidth(150); // Descrição
        jTableFinanceCashFlowDespesas.getColumnModel().getColumn(3).setPreferredWidth(120); // Fornecedor
        jTableFinanceCashFlowDespesas.getColumnModel().getColumn(4).setPreferredWidth(100); // Categoria
        jTableFinanceCashFlowDespesas.getColumnModel().getColumn(5).setPreferredWidth(80);  // Conta
        jTableFinanceCashFlowDespesas.getColumnModel().getColumn(6).setPreferredWidth(80);  // Valor
        jTableFinanceCashFlowDespesas.getColumnModel().getColumn(7).setPreferredWidth(100); // Forma Pagamento
        jTableFinanceCashFlowDespesas.getColumnModel().getColumn(8).setPreferredWidth(100); // Usuário
        jTableFinanceCashFlowDespesas.getColumnModel().getColumn(9).setPreferredWidth(150); // Notas
    }

    /**
     * Filtra receitas
     */
    public void filterReceitas() {
        if (!validarDatasReceitas()) {
            return;
        }
        listReceitas();
    }

    /**
     * Filtra despesas
     */
    public void filterDespesas() {
        if (!validarDatasDespesas()) {
            return;
        }
        listDespesas();
    }

    /**
     * Valida datas das receitas
     */
    private boolean validarDatasReceitas() {
        String dataInicio = jFormattedTextField9.getText();
        String dataFim = jFormattedTextField10.getText();

        if (dataInicio == null || dataInicio.trim().isEmpty()
                || dataFim == null || dataFim.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha ambas as datas para filtrar",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateFrom = LocalDate.parse(dataInicio, formatter);
            LocalDate dateTo = LocalDate.parse(dataFim, formatter);

            if (dateFrom.isAfter(dateTo)) {
                JOptionPane.showMessageDialog(this,
                        "Data de início não pode ser maior que data de fim",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }

            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Formato de data inválido. Use DD/MM/AAAA",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Valida datas das despesas
     */
    private boolean validarDatasDespesas() {
        String dataInicio = jFormattedTextField11.getText();
        String dataFim = jFormattedTextField12.getText();

        if (dataInicio == null || dataInicio.trim().isEmpty()
                || dataFim == null || dataFim.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha ambas as datas para filtrar",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateFrom = LocalDate.parse(dataInicio, formatter);
            LocalDate dateTo = LocalDate.parse(dataFim, formatter);

            if (dateFrom.isAfter(dateTo)) {
                JOptionPane.showMessageDialog(this,
                        "Data de início não pode ser maior que data de fim",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }

            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Formato de data inválido. Use DD/MM/AAAA",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Exporta receitas para Excel
     */
    private void exportarReceitasExcel() {
        // TODO: Implementar exportação para Excel
        JOptionPane.showMessageDialog(this,
                "Funcionalidade de exportação para Excel será implementada em breve",
                "Exportar",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Exporta despesas para Excel
     */
    private void exportarDespesasExcel() {
        // TODO: Implementar exportação para Excel
        JOptionPane.showMessageDialog(this,
                "Funcionalidade de exportação para Excel será implementada em breve",
                "Exportar",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableFinanceCashFlowReceitas = new javax.swing.JTable();
        jButtonExportHistoryShiftExcell1 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jComboBoxFlowShiftFilter1 = new javax.swing.JComboBox<>();
        jFormattedTextField9 = new javax.swing.JFormattedTextField();
        jFormattedTextField10 = new javax.swing.JFormattedTextField();
        jButton2 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jComboBoxFlowShiftFromSeller = new javax.swing.JComboBox();
        jLabel25 = new javax.swing.JLabel();
        jComboBoxFlowShifitManager = new javax.swing.JComboBox();
        jLabel26 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableFinanceCashFlowDespesas = new javax.swing.JTable();
        jButtonExportHistoryShiftExcell2 = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        jComboBoxFlowShiftFilter2 = new javax.swing.JComboBox<>();
        jFormattedTextField11 = new javax.swing.JFormattedTextField();
        jFormattedTextField12 = new javax.swing.JFormattedTextField();
        jButton3 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jComboBoxFlowShiftFromSeller1 = new javax.swing.JComboBox();
        jLabel34 = new javax.swing.JLabel();
        jComboBoxFlowShifitManager1 = new javax.swing.JComboBox();
        jLabel35 = new javax.swing.JLabel();

        jTableFinanceCashFlowReceitas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Codigo", "Vendedor", "Valor de Abertura", "Valor de fecho", "Total vendido", "Data de Abertura", "Data de Fecho", "Estado", "Caixa"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        jScrollPane6.setViewportView(jTableFinanceCashFlowReceitas);

        jButtonExportHistoryShiftExcell1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonExportHistoryShiftExcell1.setText("Excel");
        jButtonExportHistoryShiftExcell1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportHistoryShiftExcell1ActionPerformed(evt);
            }
        });

        jLabel22.setText("Exportar por:");

        jComboBoxFlowShiftFilter1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "Hoje", "Semana", "Mes", "Vencidas" }));
        jComboBoxFlowShiftFilter1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFlowShiftFilter1ActionPerformed(evt);
            }
        });

        try {
            jFormattedTextField9.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField9ActionPerformed(evt);
            }
        });
        jFormattedTextField9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFormattedTextField9KeyPressed(evt);
            }
        });

        try {
            jFormattedTextField10.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField10ActionPerformed(evt);
            }
        });
        jFormattedTextField10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFormattedTextField10KeyPressed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setText("Filtrar receita");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel16.setText("Da data");

        jLabel23.setText("Ate a data");

        jLabel24.setText("Filtar por");

        jLabel25.setText("Por Funcionario");

        jLabel26.setText("Por Gestor");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jComboBoxFlowShiftFilter1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(144, 144, 144)
                                .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jComboBoxFlowShiftFromSeller, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxFlowShifitManager, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                .addGap(120, 120, 120)
                                .addComponent(jLabel22))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 141, Short.MAX_VALUE)
                                .addComponent(jButtonExportHistoryShiftExcell1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane6))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonExportHistoryShiftExcell1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBoxFlowShiftFromSeller, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBoxFlowShifitManager, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextField10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jFormattedTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBoxFlowShiftFilter1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 996, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 557, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTabbedPane2.addTab("Entradas (Receitas)", jPanel6);

        jTableFinanceCashFlowDespesas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Codigo", "Vendedor", "Valor de Abertura", "Valor de fecho", "Total vendido", "Data de Abertura", "Data de Fecho", "Estado", "Caixa"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        jScrollPane7.setViewportView(jTableFinanceCashFlowDespesas);

        jButtonExportHistoryShiftExcell2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonExportHistoryShiftExcell2.setText("Excel");
        jButtonExportHistoryShiftExcell2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportHistoryShiftExcell2ActionPerformed(evt);
            }
        });

        jLabel31.setText("Exportar por:");

        jComboBoxFlowShiftFilter2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "Hoje", "Semana", "Mes", "Vencidas" }));
        jComboBoxFlowShiftFilter2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFlowShiftFilter2ActionPerformed(evt);
            }
        });

        try {
            jFormattedTextField11.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextField11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField11ActionPerformed(evt);
            }
        });
        jFormattedTextField11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFormattedTextField11KeyPressed(evt);
            }
        });

        try {
            jFormattedTextField12.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextField12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField12ActionPerformed(evt);
            }
        });
        jFormattedTextField12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFormattedTextField12KeyPressed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setText("Filtar despesa");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel17.setText("Da data");

        jLabel32.setText("Ate a data");

        jLabel33.setText("Filtar por");

        jLabel34.setText("Por Funcionario");

        jLabel35.setText("Por Gestor");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)
                                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jFormattedTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBoxFlowShiftFilter2, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(150, 150, 150)
                                .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jComboBoxFlowShiftFromSeller1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)))
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxFlowShifitManager1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(120, 120, 120)
                                .addComponent(jLabel31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonExportHistoryShiftExcell2, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane7))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonExportHistoryShiftExcell2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBoxFlowShiftFromSeller1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBoxFlowShifitManager1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextField12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxFlowShiftFilter2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jFormattedTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 514, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76))
        );

        jTabbedPane2.addTab("Saídas (Despesas)", jPanel7);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 561, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonExportHistoryShiftExcell1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportHistoryShiftExcell1ActionPerformed
        // TODO add your handling code here:
        exportarReceitasExcel();
    }//GEN-LAST:event_jButtonExportHistoryShiftExcell1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        filterReceitas();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButtonExportHistoryShiftExcell2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportHistoryShiftExcell2ActionPerformed
        // TODO add your handling code here:
        exportarDespesasExcel();
    }//GEN-LAST:event_jButtonExportHistoryShiftExcell2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        filterDespesas();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jComboBoxFlowShiftFilter1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFlowShiftFilter1ActionPerformed
        // TODO add your handling code here:
        filterReceitas();
    }//GEN-LAST:event_jComboBoxFlowShiftFilter1ActionPerformed

    private void jComboBoxFlowShiftFilter2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFlowShiftFilter2ActionPerformed
        // TODO add your handling code here:
        filterDespesas();
    }//GEN-LAST:event_jComboBoxFlowShiftFilter2ActionPerformed

    private void jFormattedTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField9ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jFormattedTextField9ActionPerformed

    private void jFormattedTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField10ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jFormattedTextField10ActionPerformed

    private void jFormattedTextField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField11ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jFormattedTextField11ActionPerformed

    private void jFormattedTextField12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField12ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jFormattedTextField12ActionPerformed

    private void jFormattedTextField12KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField12KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filterDespesas();
        }
    }//GEN-LAST:event_jFormattedTextField12KeyPressed

    private void jFormattedTextField11KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField11KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filterDespesas();
        }
    }//GEN-LAST:event_jFormattedTextField11KeyPressed

    private void jFormattedTextField9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField9KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filterReceitas();
        }
    }//GEN-LAST:event_jFormattedTextField9KeyPressed

    private void jFormattedTextField10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField10KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filterReceitas();
        }
    }//GEN-LAST:event_jFormattedTextField10KeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButtonExportHistoryShiftExcell1;
    private javax.swing.JButton jButtonExportHistoryShiftExcell2;
    private javax.swing.JComboBox jComboBoxFlowShifitManager;
    private javax.swing.JComboBox jComboBoxFlowShifitManager1;
    private javax.swing.JComboBox<String> jComboBoxFlowShiftFilter1;
    private javax.swing.JComboBox<String> jComboBoxFlowShiftFilter2;
    private javax.swing.JComboBox jComboBoxFlowShiftFromSeller;
    private javax.swing.JComboBox jComboBoxFlowShiftFromSeller1;
    private javax.swing.JFormattedTextField jFormattedTextField10;
    private javax.swing.JFormattedTextField jFormattedTextField11;
    private javax.swing.JFormattedTextField jFormattedTextField12;
    private javax.swing.JFormattedTextField jFormattedTextField9;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTableFinanceCashFlowDespesas;
    private javax.swing.JTable jTableFinanceCashFlowReceitas;
    // End of variables declaration//GEN-END:variables
}
