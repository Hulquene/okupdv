/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.okutonda.okudpdv.views.setting;

import com.okutonda.okudpdv.views.products.JDialogReasonTaxes;
import com.okutonda.okudpdv.controllers.CountryController;
import com.okutonda.okudpdv.controllers.OptionController;
import com.okutonda.okudpdv.controllers.ShiftController;
import com.okutonda.okudpdv.controllers.TaxeController;
import com.okutonda.okudpdv.data.entities.Options;
import com.okutonda.okudpdv.data.entities.PaymentMode;
import com.okutonda.okudpdv.data.entities.Shift;
import com.okutonda.okudpdv.helpers.Utilities;
import com.okutonda.okudpdv.data.entities.Taxes;
import com.okutonda.okudpdv.helpers.CompanySession;
import com.okutonda.okudpdv.helpers.Util;
import com.okutonda.okudpdv.helpers.ExportHelper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kenny
 */
public final class JDialogSetting extends javax.swing.JDialog {

    CompanySession companySession = CompanySession.getInstance();
    OptionController optionController = new OptionController();
    CountryController countryController = new CountryController();
    TaxeController taxesController = new TaxeController();

    /**
     * Creates new form JDialogSetting
     */
    public JDialogSetting(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
//        listTaxes();
//        listBox();
    }

    public void listPaymentModes() {
////        ClientDao cDao = new ClientDao();
////        List<PaymentModes> list = paymentModes.getAll();
//        PaymentMode[] list = PaymentMode.getActiveModes();
//        // ou
////PaymentMode[] list = PaymentMode.values();
//
////        jTableClients.setModel(new DefaultTableModel);
//        DefaultTableModel data = (DefaultTableModel) jTablePaymentModes.getModel();
////        data.setM
//        data.setNumRows(0);
//        for (PaymentMode c : list) {
//            data.addRow(new Object[]{
//                c.getId(),
//                c.getName(),
//                c.getDescription(),
//                c.getCode(),
//                c.getStatus(),
//                c.getIsDefault()
//            });
//        }
        PaymentMode[] list = PaymentMode.getActiveModes();
// ou
// PaymentMode[] list = PaymentMode.values();

        DefaultTableModel data = (DefaultTableModel) jTablePaymentModes.getModel();
        data.setNumRows(0);

        for (PaymentMode mode : list) {
            data.addRow(new Object[]{
                mode.ordinal() + 1, // ID sequencial (ou use algo mais significativo)
                mode.getDescricao(), // Nome/descrição
                mode.getCodigo(), // Código (CC, CD, NU, etc.)
                mode.toString(), // Descrição completa
                "Ativo", // Status (fixo para enum)
                mode == PaymentMode.NU ? "Sim" : "Não" // Padrão (Numerário como default)
            });
        }

//        // Definir colunas do table model
//        String[] columnNames = {"ID", "Descrição", "Código", "Tipo", "Status", "Padrão"};
//        DefaultTableModel data = new DefaultTableModel(columnNames, 0);
//        jTablePaymentModes.setModel(data);
//
//// Preencher dados
//        PaymentMode[] list = PaymentMode.getActiveModes();
//        for (PaymentMode mode : list) {
//            data.addRow(new Object[]{
//                mode.ordinal() + 1,
//                mode.getDescricao(),
//                mode.getCodigo(),
//                "Sistema",
//                "Ativo",
//                mode == PaymentMode.NU ? "Sim" : "Não"
//            });
//        }
    }

    public void listBox() {
////        ClientDao cDao = new ClientDao();
//        List<Box> list = boxController.findAll();
////        jTableClients.setModel(new DefaultTableModel);
//        DefaultTableModel data = (DefaultTableModel) jTableBox.getModel();
////        data.setM
//        data.setNumRows(0);
//        for (Box c : list) {
//            data.addRow(new Object[]{
//                c.getId(),
//                c.getName(),
//                c.getStatus(),
//                ""
//            });
//        }
// Substitua o BoxController pelo ShiftController
        ShiftController shiftController = new ShiftController();
        List<Shift> list = shiftController.listarTodos();

        DefaultTableModel data = (DefaultTableModel) jTableBox.getModel();
        data.setNumRows(0);

        for (Shift shift : list) {
            data.addRow(new Object[]{
                shift.getId(),
                shift.getCode(), // Código do turno
                shift.getUser().getName(), // Nome do usuário
                formatarValor(shift.getGrantedAmount()), // Valor abertura
                formatarValor(shift.getIncurredAmount()), // Valor vendas
                formatarValor(shift.getCurrentBalance()), // Saldo atual
                shift.getStatus(), // Status (open/closed)
                shift.getDateOpen(), // Data abertura
                shift.getDateClose() // Data fechamento (pode ser null)
            });
        }

//        ShiftController shiftController = new ShiftController();
//        List<Shift> list = shiftController.listarTodos();
//
//        DefaultTableModel data = (DefaultTableModel) jTableBox.getModel();
//        data.setNumRows(0);
//
//        for (Shift shift : list) {
//            data.addRow(new Object[]{
//                shift.getId(),
//                shift.getCode() != null ? shift.getCode() : "N/A",
//                shift.getUser() != null ? shift.getUser().getName() : "N/A",
//                formatarValor(shift.getGrantedAmount()),
//                formatarValor(shift.getIncurredAmount()),
//                formatarValor(shift.getCurrentBalance()),
//                shift.getStatus() != null ? shift.getStatus() : "N/A",
//                shift.getDateOpen() != null ? shift.getDateOpen() : "N/A",
//                shift.getDateClose() != null ? shift.getDateClose() : "Em aberto"
//            });
//        }
    }

    /**
     * Formata valores monetários
     */
    private String formatarValor(BigDecimal valor) {
        if (valor == null) {
            return "0.00 AOA";
        }
        return String.format("%.2f AOA", valor);
    }

    public void listTaxes() {
//        ClientDao cDao = new ClientDao();
        List<Taxes> list = taxesController.listarTodas();
//        jTableClients.setModel(new DefaultTableModel);
        DefaultTableModel data = (DefaultTableModel) jTableTaxes.getModel();
//        data.setM
        data.setNumRows(0);
        for (Taxes c : list) {
            data.addRow(new Object[]{
                c.getId(),
                c.getName(),
                c.getPercetage(),
                c.getCode(),
                c.getIsDefault()
            });
        }
    }

    public void filterListTaxes(String txt) {
        List<Taxes> list = taxesController.filtrar(txt);
        DefaultTableModel data = (DefaultTableModel) jTableTaxes.getModel();
        data.setNumRows(0);
        for (Taxes c : list) {
            data.addRow(new Object[]{
                c.getId(),
                c.getName(),
                c.getPercetage(),
                c.getCode(),
                c.getIsDefault()
            });
        }
    }

    /**
     * Função para abrir/fechar turno Centraliza a lógica de gestão de turnos
     */
    private void salvarTurno() {
        // Validação básica
        if (jTextFieldBoxName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Campo nome do turno inválido!!");
            return;
        }

        // Obtém ID (0 para novo turno, >0 para fechar turno)
        int id = jTextFieldBoxId.getText().isEmpty() ? 0 : Integer.parseInt(jTextFieldBoxId.getText());

        // Determina ação: abrir ou fechar turno
        boolean sucesso;
        if (id == 0) {
            sucesso = abrirTurno();
        } else {
            sucesso = fecharTurno(id);
        }

        if (sucesso) {
            // Limpa formulário e atualiza lista
            new Utilities().clearScreen(jPanelBoxForm);
            listBox(); // Ou listShifts() se renomeou o método
        }
    }

    /**
     * Abre um novo turno
     *
     * @return true se o turno foi aberto com sucesso
     */
    private boolean abrirTurno() {
        try {
            // Obtém valor de abertura
            BigDecimal valorAbertura = BigDecimal.ZERO;
            try {
                if (!jTextFieldBoxName.getText().isEmpty()) {
                    valorAbertura = new BigDecimal(jTextFieldBoxName.getText());
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Valor de abertura inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Abre o turno
            ShiftController shiftController = new ShiftController();
            Shift turnoAberto = shiftController.abrirTurno(valorAbertura);

            if (turnoAberto != null) {
                JOptionPane.showMessageDialog(null,
                        "Turno aberto com sucesso!\nCódigo: " + turnoAberto.getCode(),
                        "Turno Aberto", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }

            return false;

        } catch (Exception e) {
            System.err.println("❌ Erro ao abrir turno: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Erro ao abrir turno: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Fecha um turno existente
     *
     * @param id ID do turno a ser fechado
     * @return true se o turno foi fechado com sucesso
     */
    private boolean fecharTurno(int id) {
        try {
            // Obtém valor de fechamento
            BigDecimal valorFechamento = BigDecimal.ZERO;
            try {
                if (!jTextFieldBoxName.getText().isEmpty()) {
                    valorFechamento = new BigDecimal(jTextFieldBoxName.getText());
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Valor de fechamento inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Busca o turno
            ShiftController shiftController = new ShiftController();
            Shift turno = shiftController.buscarPorId(id);

            if (turno == null) {
                JOptionPane.showMessageDialog(null, "Turno não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Fecha o turno
            Shift turnoFechado = shiftController.fecharTurno(valorFechamento);

            if (turnoFechado != null) {
                // Calcula diferença para mensagem
                BigDecimal diferenca = turnoFechado.getDifference();
                String mensagem = diferenca.compareTo(BigDecimal.ZERO) >= 0
                        ? "Sobra: " + diferenca + " AOA"
                        : "Falta: " + diferenca.abs() + " AOA";

                JOptionPane.showMessageDialog(null,
                        "Turno fechado com sucesso!\n" + mensagem,
                        "Turno Fechado", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }

            return false;

        } catch (Exception e) {
            System.err.println("❌ Erro ao fechar turno: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Erro ao fechar turno: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

//    public void listComboCountries() {
//
//        List<Countries> list = countryController.get("");
//        jComboBoxOptionCountryCompany.removeAllItems();
//        for (Countries item : list) {
//            jComboBoxOptionCountryCompany.addItem(item);
//        }
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldOptionNameCompany = new javax.swing.JTextField();
        jTextFieldOptionNifCompany = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldOptionCityCompany = new javax.swing.JTextField();
        jTextFieldOptionPhoneCompany = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldOptionAddressCompany = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldOptionEmailCompany = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldOptionCountryCompany = new javax.swing.JTextField();
        jButtonOptionSaveCompany = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableBox = new javax.swing.JTable();
        jButtonEditBox = new javax.swing.JButton();
        jButtonDeleteBox = new javax.swing.JButton();
        jTextFieldSearchBox = new javax.swing.JTextField();
        jPanelBoxForm = new javax.swing.JPanel();
        jTextFieldBoxName = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jButtonSaveBox = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jTextFieldBoxId = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTablePaymentModes = new javax.swing.JTable();
        jTextFieldFormPaymentSearchTable = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jButtonFormPaymentDelete = new javax.swing.JButton();
        jButtonFormPAymentEdit = new javax.swing.JButton();
        jPanelPaymentModesForm = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jTextFieldPaymentModeId = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jTextFieldPaymentModesName = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jTextFieldPaymentModesDescription = new javax.swing.JTextField();
        jTextFieldPaymentModeCode = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jComboBoxPaymentModesStatus = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        jComboBoxPaymentModesDefault = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        jButtonFormPaymentAdd = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableTaxes = new javax.swing.JTable();
        jButtonReasonTaxes = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextFieldTaxeSearchTable = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButtonTaxeDelete = new javax.swing.JButton();
        jButtonTaxeEdit = new javax.swing.JButton();
        jPanelTaxeForm = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldTaxeId = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldTaxeName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldTaxePerc = new javax.swing.JTextField();
        jTextFieldTaxeCode = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jComboBoxTaxeIsDefault = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jButtonTaxeAdd = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jButtonExportDatabaseInExcel = new javax.swing.JButton();
        jButtonExportDatabaseInCSV = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabelTimeKey = new javax.swing.JLabel();
        jLabelDateLicence = new javax.swing.JLabel();
        jLabelKeyLicence = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jButtonViewLicence = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jTabbedPane1.setBackground(new java.awt.Color(204, 204, 255));
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });
        jTabbedPane1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTabbedPane1FocusGained(evt);
            }
        });
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });
        jTabbedPane1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTabbedPane1KeyPressed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));

        jPanel4.setBackground(new java.awt.Color(204, 204, 255));

        jLabel7.setText("Nome Empresa");

        jLabel8.setText("Numero de Identificacação Fiscal (NIF)");

        jLabel9.setText("Pais");

        jLabel10.setText("Provincia");

        jLabel11.setText("Endereço");

        jLabel13.setText("Email");

        jLabel12.setText("Telefone");

        jTextFieldOptionCountryCompany.setEnabled(false);

        jButtonOptionSaveCompany.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonOptionSaveCompany.setText("Salvar");
        jButtonOptionSaveCompany.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionSaveCompanyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jTextFieldOptionAddressCompany)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel9)
                                        .addComponent(jLabel7)
                                        .addComponent(jTextFieldOptionNameCompany)
                                        .addComponent(jTextFieldOptionCountryCompany))
                                    .addComponent(jTextFieldOptionCityCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 152, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(jTextFieldOptionEmailCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldOptionPhoneCompany, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                                    .addComponent(jLabel12))))
                        .addGap(38, 38, 38))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jTextFieldOptionNifCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(231, 231, 231)
                .addComponent(jButtonOptionSaveCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jTextFieldOptionAddressCompany, jTextFieldOptionCityCompany, jTextFieldOptionEmailCompany, jTextFieldOptionNameCompany, jTextFieldOptionNifCompany, jTextFieldOptionPhoneCompany});

        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldOptionNifCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldOptionEmailCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldOptionNameCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldOptionPhoneCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldOptionCountryCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldOptionCityCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldOptionAddressCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addComponent(jButtonOptionSaveCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jTextFieldOptionAddressCompany, jTextFieldOptionCityCompany, jTextFieldOptionEmailCompany, jTextFieldOptionNameCompany, jTextFieldOptionNifCompany, jTextFieldOptionPhoneCompany});

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Informacao da Empresa", jPanel2);

        jPanel8.setBackground(new java.awt.Color(204, 204, 255));

        jTableBox.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Status", "Operador"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTableBox);

        jButtonEditBox.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonEditBox.setText("Editar");
        jButtonEditBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditBoxActionPerformed(evt);
            }
        });

        jButtonDeleteBox.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonDeleteBox.setText("Excluir");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel25.setText("Formulario do Caixa");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel26.setText("Nome do caixa");

        jButtonSaveBox.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonSaveBox.setText("Guardar");
        jButtonSaveBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveBoxActionPerformed(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel27.setText("ID");

        jTextFieldBoxId.setEnabled(false);

        javax.swing.GroupLayout jPanelBoxFormLayout = new javax.swing.GroupLayout(jPanelBoxForm);
        jPanelBoxForm.setLayout(jPanelBoxFormLayout);
        jPanelBoxFormLayout.setHorizontalGroup(
            jPanelBoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBoxFormLayout.createSequentialGroup()
                .addGroup(jPanelBoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelBoxFormLayout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(jLabel25))
                    .addGroup(jPanelBoxFormLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanelBoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButtonSaveBox, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelBoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldBoxName, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanelBoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextFieldBoxId, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))))))
                .addContainerGap(85, Short.MAX_VALUE))
        );
        jPanelBoxFormLayout.setVerticalGroup(
            jPanelBoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBoxFormLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel25)
                .addGap(18, 18, 18)
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldBoxId, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldBoxName, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonSaveBox, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanelBoxForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jTextFieldSearchBox, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)
                        .addComponent(jButtonEditBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDeleteBox))
                    .addComponent(jScrollPane3))
                .addGap(27, 27, 27))
        );

        jPanel8Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButtonDeleteBox, jButtonEditBox});

        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonDeleteBox)
                    .addComponent(jButtonEditBox)
                    .addComponent(jTextFieldSearchBox, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelBoxForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE))
                .addContainerGap(66, Short.MAX_VALUE))
        );

        jPanel8Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonDeleteBox, jButtonEditBox, jTextFieldSearchBox});

        jTabbedPane1.addTab("Caixa", jPanel8);

        jPanel9.setBackground(new java.awt.Color(204, 204, 255));

        jPanel10.setBackground(new java.awt.Color(204, 204, 255));

        jTablePaymentModes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Percentagem", "code", "Padao", "Padrão"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        jScrollPane4.setViewportView(jTablePaymentModes);

        jTextFieldFormPaymentSearchTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldFormPaymentSearchTableKeyReleased(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel18.setText("Cadastrar Forma de pagamento");

        jButtonFormPaymentDelete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonFormPaymentDelete.setText("Apagar");
        jButtonFormPaymentDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFormPaymentDeleteActionPerformed(evt);
            }
        });

        jButtonFormPAymentEdit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonFormPAymentEdit.setText("Editar");
        jButtonFormPAymentEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFormPAymentEditActionPerformed(evt);
            }
        });

        jPanelPaymentModesForm.setBackground(new java.awt.Color(204, 204, 255));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setText("ID");

        jTextFieldPaymentModeId.setEditable(false);

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setText("Nome");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setText("Descrição");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setText("Codigo");

        jComboBoxPaymentModesStatus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jComboBoxPaymentModesStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ativo", "inativo" }));
        jComboBoxPaymentModesStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPaymentModesStatusActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel24.setText("Padrao");

        jComboBoxPaymentModesDefault.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jComboBoxPaymentModesDefault.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "nao", "sim" }));
        jComboBoxPaymentModesDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPaymentModesDefaultActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel28.setText("Status");

        jButtonFormPaymentAdd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonFormPaymentAdd.setText("Salvar");
        jButtonFormPaymentAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFormPaymentAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelPaymentModesFormLayout = new javax.swing.GroupLayout(jPanelPaymentModesForm);
        jPanelPaymentModesForm.setLayout(jPanelPaymentModesFormLayout);
        jPanelPaymentModesFormLayout.setHorizontalGroup(
            jPanelPaymentModesFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPaymentModesFormLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanelPaymentModesFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPaymentModesFormLayout.createSequentialGroup()
                        .addGap(0, 24, Short.MAX_VALUE)
                        .addComponent(jComboBoxPaymentModesStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxPaymentModesDefault, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(169, 169, 169))
                    .addGroup(jPanelPaymentModesFormLayout.createSequentialGroup()
                        .addGroup(jPanelPaymentModesFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButtonFormPaymentAdd, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanelPaymentModesFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel20)
                                .addComponent(jLabel22)
                                .addGroup(jPanelPaymentModesFormLayout.createSequentialGroup()
                                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(103, 103, 103)
                                    .addComponent(jLabel23))
                                .addGroup(jPanelPaymentModesFormLayout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addComponent(jLabel28)
                                    .addGap(58, 58, 58)
                                    .addComponent(jLabel24))
                                .addGroup(jPanelPaymentModesFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextFieldPaymentModesDescription, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelPaymentModesFormLayout.createSequentialGroup()
                                        .addComponent(jTextFieldPaymentModeId, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextFieldPaymentModeCode, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jTextFieldPaymentModesName, javax.swing.GroupLayout.Alignment.LEADING))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanelPaymentModesFormLayout.setVerticalGroup(
            jPanelPaymentModesFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPaymentModesFormLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanelPaymentModesFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPaymentModesFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldPaymentModeId, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldPaymentModeCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldPaymentModesName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22)
                .addGap(4, 4, 4)
                .addComponent(jTextFieldPaymentModesDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelPaymentModesFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPaymentModesFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxPaymentModesStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxPaymentModesDefault, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(jButtonFormPaymentAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanelPaymentModesForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(jTextFieldFormPaymentSearchTable, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(123, 123, 123)
                        .addComponent(jButtonFormPAymentEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonFormPaymentDelete))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonFormPaymentDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonFormPAymentEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(125, 125, 125))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldFormPaymentSearchTable, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelPaymentModesForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64))))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 84, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Forma de Pagamento", jPanel9);

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));

        jTableTaxes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Percentagem", "code", "Padao"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableTaxes);

        jButtonReasonTaxes.setText("Listar Razao do Imposto");
        jButtonReasonTaxes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReasonTaxesActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setText("Pesquisar");

        jTextFieldTaxeSearchTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldTaxeSearchTableKeyReleased(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Formulario de Imposto");

        jButtonTaxeDelete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonTaxeDelete.setText("Apagar");
        jButtonTaxeDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTaxeDeleteActionPerformed(evt);
            }
        });

        jButtonTaxeEdit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonTaxeEdit.setText("Editar");
        jButtonTaxeEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTaxeEditActionPerformed(evt);
            }
        });

        jPanelTaxeForm.setBackground(new java.awt.Color(204, 204, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("ID");

        jTextFieldTaxeId.setEditable(false);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Nome");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Percentagem");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Codigo");

        jComboBoxTaxeIsDefault.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nao", "Sim" }));

        jLabel6.setText("Padrao");

        jButtonTaxeAdd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonTaxeAdd.setText("Salvar");
        jButtonTaxeAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTaxeAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelTaxeFormLayout = new javax.swing.GroupLayout(jPanelTaxeForm);
        jPanelTaxeForm.setLayout(jPanelTaxeFormLayout);
        jPanelTaxeFormLayout.setHorizontalGroup(
            jPanelTaxeFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTaxeFormLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanelTaxeFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldTaxeName, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addGroup(jPanelTaxeFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jTextFieldTaxePerc, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanelTaxeFormLayout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addGap(108, 108, 108)
                            .addComponent(jLabel6)
                            .addGap(70, 70, 70)))
                    .addComponent(jTextFieldTaxeId, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelTaxeFormLayout.createSequentialGroup()
                        .addComponent(jTextFieldTaxeCode, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelTaxeFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonTaxeAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxTaxeIsDefault, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanelTaxeFormLayout.setVerticalGroup(
            jPanelTaxeFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTaxeFormLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldTaxeId, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldTaxeName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(4, 4, 4)
                .addComponent(jTextFieldTaxePerc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelTaxeFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelTaxeFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldTaxeCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxTaxeIsDefault, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButtonTaxeAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jLabel1))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonReasonTaxes, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanelTaxeForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jTextFieldTaxeSearchTable, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonTaxeEdit)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonTaxeDelete)
                        .addGap(4, 4, 4))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButtonTaxeEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButtonTaxeDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextFieldTaxeSearchTable, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButtonReasonTaxes, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelTaxeForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(59, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Imposto", jPanel3);

        jPanel6.setBackground(new java.awt.Color(204, 204, 255));

        jButtonExportDatabaseInExcel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonExportDatabaseInExcel.setText("Exportar database Excel Excel");
        jButtonExportDatabaseInExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportDatabaseInExcelActionPerformed(evt);
            }
        });

        jButtonExportDatabaseInCSV.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonExportDatabaseInCSV.setText("Exportar database para CSV");
        jButtonExportDatabaseInCSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportDatabaseInCSVActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonExportDatabaseInCSV, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonExportDatabaseInExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(671, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jButtonExportDatabaseInExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jButtonExportDatabaseInCSV, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(381, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Backup", jPanel6);

        jLabel21.setText("jLabel21");

        jLabelTimeKey.setText("jLabel20");

        jLabelDateLicence.setText("jLabel19");

        jLabelKeyLicence.setText("jLabel18");

        jLabel14.setText("jLabel14");

        jLabel15.setText("jLabel15");

        jLabel16.setText("jLabel16");

        jLabel17.setText("jLabel17");

        jButtonViewLicence.setText("Licença");
        jButtonViewLicence.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewLicenceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17))
                        .addGap(45, 45, 45)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelKeyLicence)
                            .addComponent(jLabel21)
                            .addComponent(jLabelTimeKey)
                            .addComponent(jLabelDateLicence)))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jButtonViewLicence, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(501, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jButtonViewLicence, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(98, 98, 98)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabelKeyLicence))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabelDateLicence))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabelTimeKey))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel21))
                .addContainerGap(204, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 113, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 62, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("About", jPanel7);

        jScrollPane2.setViewportView(jTabbedPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 903, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 567, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(919, 587));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonReasonTaxesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReasonTaxesActionPerformed
        // TODO add your handling code here:
        new JDialogReasonTaxes(null, rootPaneCheckingEnabled).setVisible(true);
    }//GEN-LAST:event_jButtonReasonTaxesActionPerformed

    private void jButtonTaxeEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTaxeEditActionPerformed
        // TODO add your handling code here:

        int value = 0;
        try {
            value = (int) jTableTaxes.getValueAt(jTableTaxes.getSelectedRow(), 0);
//            System.out.println("jTableTaxes id:" + value);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Selecione um jTableTaxes na tabela!!", "Atencao", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (value > 0) {
                jTextFieldTaxeId.setText(jTableTaxes.getValueAt(jTableTaxes.getSelectedRow(), 0).toString());
                jTextFieldTaxeName.setText(jTableTaxes.getValueAt(jTableTaxes.getSelectedRow(), 1).toString());
                jTextFieldTaxePerc.setText(jTableTaxes.getValueAt(jTableTaxes.getSelectedRow(), 2).toString());
                jTextFieldTaxeCode.setText(jTableTaxes.getValueAt(jTableTaxes.getSelectedRow(), 3).toString());
                jComboBoxTaxeIsDefault.setSelectedIndex((int) jTableTaxes.getValueAt(jTableTaxes.getSelectedRow(), 4));

            }
        }
    }//GEN-LAST:event_jButtonTaxeEditActionPerformed

    private void jButtonTaxeAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTaxeAddActionPerformed
        // TODO add your handling code here:
        Taxes cModel = new Taxes();
        if (jTextFieldTaxeCode.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Campo Codigo invalido!!");
        } else if (jTextFieldTaxeName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Campo Descrição invalido!!");
        } else if (jTextFieldTaxePerc.getText().isEmpty() || Util.isValidDouble(jTextFieldTaxePerc.getText()) == false) {
            JOptionPane.showMessageDialog(null, "Campo Percetage invalido!!");
        } else {
            cModel.setName(jTextFieldTaxeName.getText());
            cModel.setCode(jTextFieldTaxeCode.getText());
            cModel.setPercetage(Util.convertToBigDecimal(jTextFieldTaxePerc.getText()));
            cModel.setIsDefault(jComboBoxTaxeIsDefault.getSelectedIndex());
        }
        if (cModel != null) {
            int id = jTextFieldTaxeId.getText().isEmpty() == true ? 0 : Integer.parseInt(jTextFieldTaxeId.getText());
            boolean response;
            if (id == 0) {
                Taxes taxSalva = taxesController.save(cModel);
                response = (taxSalva != null);
                if (response) {
                    JOptionPane.showMessageDialog(null, "Taxes salvo com Sucesso!!");
                    listTaxes();
                }
            } else {
                cModel.setId(id);
                Taxes taxSalva = taxesController.save(cModel);
                response = (taxSalva != null);
                if (response) {
                    JOptionPane.showMessageDialog(null, "Taxes Atualizado com Sucesso!!");
                    listTaxes();
                }
            }
            new Utilities().clearScreen(jPanelTaxeForm);
        }

    }//GEN-LAST:event_jButtonTaxeAddActionPerformed

    private void jTextFieldTaxeSearchTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldTaxeSearchTableKeyReleased
        // TODO add your handling code here:
        String txt = jTextFieldTaxeSearchTable.getText();
        if (!txt.isEmpty()) {
            filterListTaxes(txt);
        } else {
            listTaxes();
        }
    }//GEN-LAST:event_jTextFieldTaxeSearchTableKeyReleased

    private void jButtonTaxeDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTaxeDeleteActionPerformed
        // TODO add your handling code here:
        jTextFieldTaxeId.setText(jTableTaxes.getValueAt(jTableTaxes.getSelectedRow(), 0).toString());

        int id = Integer.parseInt(jTableTaxes.getValueAt(jTableTaxes.getSelectedRow(), 0).toString());
        Taxes tax = taxesController.getById(id);
//        JOptionPane.showMessageDialog(null, "Cliente :" + client.getName());
        int sair = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja Deletar," + tax.getName() + "?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (sair == JOptionPane.YES_OPTION) {
            if (taxesController.delete(id)) {
                JOptionPane.showMessageDialog(null, "Imposto excluido com Sucesso!!");
                listTaxes();
            }
        }
    }//GEN-LAST:event_jButtonTaxeDeleteActionPerformed

    private void jButtonOptionSaveCompanyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionSaveCompanyActionPerformed
        // TODO add your handling code here:
        List<Options> listOptionCompany = new ArrayList<>();
        if (!jTextFieldOptionNameCompany.getText().isEmpty()) {
//            optionController.add(new Options("companyName", jTextFieldOptionNameCompany.getText(), "1"));
            listOptionCompany.add(new Options("companyName", jTextFieldOptionNameCompany.getText(), "1"));
        }
        if (!jTextFieldOptionNifCompany.getText().isEmpty()) {
//            optionController.add(new Options("companyNif", jTextFieldOptionNifCompany.getText(), "1"));
            listOptionCompany.add(new Options("companyNif", jTextFieldOptionNifCompany.getText(), "1"));
        }
        if (!jTextFieldOptionCityCompany.getText().isEmpty()) {
            listOptionCompany.add(new Options("companyCity", jTextFieldOptionCityCompany.getText(), "1"));
        }

        if (!jTextFieldOptionAddressCompany.getText().isEmpty()) {
            listOptionCompany.add(new Options("companyAddress", jTextFieldOptionAddressCompany.getText(), "1"));
        }
        if (!jTextFieldOptionPhoneCompany.getText().isEmpty()) {
            listOptionCompany.add(new Options("companyPhone", jTextFieldOptionPhoneCompany.getText(), "1"));
        }
        if (!jTextFieldOptionEmailCompany.getText().isEmpty()) {
            listOptionCompany.add(new Options("companyEmail", jTextFieldOptionEmailCompany.getText(), "1"));
        }

        listOptionCompany.add(
                new Options("country", jTextFieldOptionCountryCompany.getText(), "1"));

//        listOptionCompany.add(
//                new Options("companyCountry", jComboBoxOptionCountryCompany.getSelectedItem().toString(), "1"));
        for (Options options : listOptionCompany) {
            optionController.saveOption(options);
        }

        JOptionPane.showMessageDialog(null, "Salvo com sucesso!!");
    }//GEN-LAST:event_jButtonOptionSaveCompanyActionPerformed

    private void jTabbedPane1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabbedPane1KeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_jTabbedPane1KeyPressed

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        // TODO add your handling code here:;
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jTabbedPane1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabbedPane1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane1FocusGained

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        listBox();
        listTaxes();
        listPaymentModes();
//        listComboCountries();

        jTextFieldOptionNameCompany.setText(optionController.getOptionValue("companyName"));
        jTextFieldOptionNifCompany.setText(optionController.getOptionValue("companyNif"));
        jTextFieldOptionCityCompany.setText(optionController.getOptionValue("companyCity"));
        jTextFieldOptionPhoneCompany.setText(optionController.getOptionValue("companyPhone"));
        jTextFieldOptionAddressCompany.setText(optionController.getOptionValue("companyAddress"));
        jTextFieldOptionEmailCompany.setText(optionController.getOptionValue("companyEmail"));
        jTextFieldOptionCountryCompany.setText(optionController.getOptionValue("country"));
//        jComboBoxOptionCountryCompany.setSelectedItem(optionController.getValueOptions("companyCountry"));

        jLabelDateLicence.setText(companySession.getDateKeyLicence());
        jLabelTimeKey.setText(companySession.getTime());
        jLabelKeyLicence.setText(companySession.getKeyLicence());
//        jLabelDateLicence.setText(companySession.getDateKeyLicence());
    }//GEN-LAST:event_formWindowActivated

    private void jButtonExportDatabaseInExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportDatabaseInExcelActionPerformed
        // TODO add your handling code here:
        ExportHelper utlDB = new ExportHelper();
        utlDB.exportAllTablesToExcel();

    }//GEN-LAST:event_jButtonExportDatabaseInExcelActionPerformed

    private void jButtonExportDatabaseInCSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportDatabaseInCSVActionPerformed
        // TODO add your handling code here:
        ExportHelper utlDB = new ExportHelper();
        utlDB.exportAllTablesToCSV();
    }//GEN-LAST:event_jButtonExportDatabaseInCSVActionPerformed

    private void jButtonViewLicenceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewLicenceActionPerformed
        // TODO add your handling code here:
//        JDialogValidateLicence jdialogValidateLicence = new JDialogValidateLicence(null, true);
//        jdialogValidateLicence.setVisible(true);
    }//GEN-LAST:event_jButtonViewLicenceActionPerformed

    private void jTextFieldFormPaymentSearchTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldFormPaymentSearchTableKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldFormPaymentSearchTableKeyReleased

    private void jButtonFormPaymentDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFormPaymentDeleteActionPerformed
        // TODO add your handling code here:
////         jTextFieldTaxeId.setText(jTableTaxes.getValueAt(jTableTaxes.getSelectedRow(), 0).toString());
//
//        int id = Integer.parseInt(jTablePaymentModes.getValueAt(jTablePaymentModes.getSelectedRow(), 0).toString());
////        Taxes tax = taxesController.getId(id);
//        PaymentModes paymentMode = paymentModes.getById(id);
////        JOptionPane.showMessageDialog(null, "Cliente :" + client.getName());
//        int sair = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja Deletar," + paymentMode.getName() + "?", "Atenção", JOptionPane.YES_NO_OPTION);
//        if (sair == JOptionPane.YES_OPTION) {
//            if (paymentModes.delete(id)) {
//                JOptionPane.showMessageDialog(null, "payment Mode excluido com Sucesso!!");
////                listTaxes();
//                listPaymentModes();
//            }
//        }
    }//GEN-LAST:event_jButtonFormPaymentDeleteActionPerformed

    private void jButtonFormPAymentEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFormPAymentEditActionPerformed
        // TODO add your handling code here:
        int value = 0;
        try {
            value = (int) jTablePaymentModes.getValueAt(jTablePaymentModes.getSelectedRow(), 0);
//            System.out.println("jTablePaymentModes id:" + value);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Selecione um jTablePaymentModes na tabela!!", "Atencao", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (value > 0) {
                jTextFieldPaymentModeId.setText(jTablePaymentModes.getValueAt(jTablePaymentModes.getSelectedRow(), 0).toString());
                jTextFieldPaymentModesName.setText(jTablePaymentModes.getValueAt(jTablePaymentModes.getSelectedRow(), 1).toString());
                jTextFieldPaymentModesDescription.setText(jTablePaymentModes.getValueAt(jTablePaymentModes.getSelectedRow(), 2).toString());
                jTextFieldPaymentModeCode.setText(jTablePaymentModes.getValueAt(jTablePaymentModes.getSelectedRow(), 3).toString());
                jComboBoxPaymentModesStatus.setSelectedItem(jTablePaymentModes.getValueAt(jTablePaymentModes.getSelectedRow(), 4));
                jComboBoxPaymentModesDefault.setSelectedItem(jTablePaymentModes.getValueAt(jTablePaymentModes.getSelectedRow(), 4));
            }
        }
    }//GEN-LAST:event_jButtonFormPAymentEditActionPerformed

    private void jButtonFormPaymentAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFormPaymentAddActionPerformed
        // TODO add your handling code here:

//        PaymentModes cModel = new PaymentModes();
//        if (jTextFieldPaymentModesName.getText().isEmpty()) {
//            JOptionPane.showMessageDialog(null, "Campo Nome invalido!!");
//        } else if (jTextFieldPaymentModesDescription.getText().isEmpty()) {
//            JOptionPane.showMessageDialog(null, "Campo Descrição invalido!!");
//        } else if (jTextFieldPaymentModeCode.getText().isEmpty()) {
//            JOptionPane.showMessageDialog(null, "Campo Codigo invalido!!");
//        } else {
//            cModel.setName(jTextFieldPaymentModesName.getText());
//            cModel.setDescription(jTextFieldPaymentModesDescription.getText());
//            cModel.setCode(jTextFieldPaymentModeCode.getText());
//
////            cModel.setStatus((String) jComboBoxPaymentModesStatus.getSelectedItem());
////            cModel.setIsDefault((String) jComboBoxPaymentModesDefault.getSelectedItem());
//            String statusPayment = (String) jComboBoxPaymentModesStatus.getSelectedItem();
//            String defaultPayment = (String) jComboBoxPaymentModesDefault.getSelectedItem();
//
//            if ("ativo".equals(statusPayment)) {
//                cModel.setStatus(1);
//            } else {
//                cModel.setStatus(0);
//            }
//            if ("sim".equals(defaultPayment)) {
//                cModel.setIsDefault(1);
//            } else {
//                cModel.setIsDefault(0);
//            }
//
//            if (cModel != null) {
//                int id = jTextFieldPaymentModeId.getText().isEmpty() == true ? 0 : Integer.parseInt(jTextFieldPaymentModeId.getText());
//                boolean response;
//                if (id == 0) {
//                    response = paymentModes.save(cModel);
//                    if (response) {
//                        JOptionPane.showMessageDialog(null, "Taxes salvo com Sucesso!!");
//                        listTaxes();
//                    }
//                } else {
//                    response = paymentModes.save(cModel);
//                    if (response) {
//                        JOptionPane.showMessageDialog(null, "taxes Atualizado com Sucesso!!");
//                        listTaxes();
//                    }
//                }
//                new Utilities().clearScreen(jPanelPaymentModesForm);
//            }
//        }
    }//GEN-LAST:event_jButtonFormPaymentAddActionPerformed

    private void jButtonSaveBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveBoxActionPerformed
        // TODO add your handling code here:
        salvarTurno();
//        Box cModel = new Box();
//        if (jTextFieldBoxName.getText().isEmpty()) {
//            JOptionPane.showMessageDialog(null, "Campo Nome do caixa invalido!!");
//        } else {
//            cModel.setName(jTextFieldBoxName.getText());
////            cModel.setCode(jTextFieldTaxeCode.getText());
////            cModel.setPercetage(Util.convertToDouble(jTextFieldTaxePerc.getText()));
////            cModel.setIsDefault(jComboBoxTaxeIsDefault.getSelectedIndex());
//            if (cModel != null) {
//                int id = jTextFieldBoxId.getText().isEmpty() == true ? 0 : Integer.parseInt(jTextFieldBoxId.getText());
//                boolean response;
//                if (id == 0) {
//                    response = boxController.save(cModel);
//                    if (response) {
//                        JOptionPane.showMessageDialog(null, "Caixa salvo com Sucesso!!");
//                        listBox();
//                    }
//                } else {
//                    cModel.setId(id);
//                    response = boxController.save(cModel);
//                    if (response) {
//                        JOptionPane.showMessageDialog(null, "Caixa Atualizado com Sucesso!!");
//                        listBox();
//                    }
//                }
//                new Utilities().clearScreen(jPanelBoxForm);
//            }
//        }

    }//GEN-LAST:event_jButtonSaveBoxActionPerformed

    private void jButtonEditBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditBoxActionPerformed
        // TODO add your handling code here:

        int value = 0;
        try {
            value = (int) jTableBox.getValueAt(jTableBox.getSelectedRow(), 0);
//            System.out.println("jTableBox id:" + value);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Selecione um jTableBox na tabela!!", "Atencao", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (value > 0) {
                jTextFieldBoxId.setText(jTableBox.getValueAt(jTableBox.getSelectedRow(), 0).toString());
                jTextFieldBoxName.setText(jTableBox.getValueAt(jTableBox.getSelectedRow(), 1).toString());

            }
        }
    }//GEN-LAST:event_jButtonEditBoxActionPerformed

    private void jComboBoxPaymentModesStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPaymentModesStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxPaymentModesStatusActionPerformed

    private void jComboBoxPaymentModesDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPaymentModesDefaultActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxPaymentModesDefaultActionPerformed

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
            java.util.logging.Logger.getLogger(JDialogSetting.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogSetting.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogSetting.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogSetting.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogSetting dialog = new JDialogSetting(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonDeleteBox;
    private javax.swing.JButton jButtonEditBox;
    private javax.swing.JButton jButtonExportDatabaseInCSV;
    private javax.swing.JButton jButtonExportDatabaseInExcel;
    private javax.swing.JButton jButtonFormPAymentEdit;
    private javax.swing.JButton jButtonFormPaymentAdd;
    private javax.swing.JButton jButtonFormPaymentDelete;
    private javax.swing.JButton jButtonOptionSaveCompany;
    private javax.swing.JButton jButtonReasonTaxes;
    private javax.swing.JButton jButtonSaveBox;
    private javax.swing.JButton jButtonTaxeAdd;
    private javax.swing.JButton jButtonTaxeDelete;
    private javax.swing.JButton jButtonTaxeEdit;
    private javax.swing.JButton jButtonViewLicence;
    private javax.swing.JComboBox<String> jComboBoxPaymentModesDefault;
    private javax.swing.JComboBox<String> jComboBoxPaymentModesStatus;
    private javax.swing.JComboBox<String> jComboBoxTaxeIsDefault;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelDateLicence;
    private javax.swing.JLabel jLabelKeyLicence;
    private javax.swing.JLabel jLabelTimeKey;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelBoxForm;
    private javax.swing.JPanel jPanelPaymentModesForm;
    private javax.swing.JPanel jPanelTaxeForm;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableBox;
    private javax.swing.JTable jTablePaymentModes;
    private javax.swing.JTable jTableTaxes;
    private javax.swing.JTextField jTextFieldBoxId;
    private javax.swing.JTextField jTextFieldBoxName;
    private javax.swing.JTextField jTextFieldFormPaymentSearchTable;
    private javax.swing.JTextField jTextFieldOptionAddressCompany;
    private javax.swing.JTextField jTextFieldOptionCityCompany;
    private javax.swing.JTextField jTextFieldOptionCountryCompany;
    private javax.swing.JTextField jTextFieldOptionEmailCompany;
    private javax.swing.JTextField jTextFieldOptionNameCompany;
    private javax.swing.JTextField jTextFieldOptionNifCompany;
    private javax.swing.JTextField jTextFieldOptionPhoneCompany;
    private javax.swing.JTextField jTextFieldPaymentModeCode;
    private javax.swing.JTextField jTextFieldPaymentModeId;
    private javax.swing.JTextField jTextFieldPaymentModesDescription;
    private javax.swing.JTextField jTextFieldPaymentModesName;
    private javax.swing.JTextField jTextFieldSearchBox;
    private javax.swing.JTextField jTextFieldTaxeCode;
    private javax.swing.JTextField jTextFieldTaxeId;
    private javax.swing.JTextField jTextFieldTaxeName;
    private javax.swing.JTextField jTextFieldTaxePerc;
    private javax.swing.JTextField jTextFieldTaxeSearchTable;
    // End of variables declaration//GEN-END:variables
}
