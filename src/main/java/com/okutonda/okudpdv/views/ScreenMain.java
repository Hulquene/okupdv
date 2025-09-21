/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.okutonda.okudpdv.views;

import com.okutonda.okudpdv.views.report.JPanelReport;
import com.okutonda.okudpdv.views.products.JPanelProduct;
import com.okutonda.okudpdv.views.setting.JDialogSetting;
import com.okutonda.okudpdv.views.dashboard.JPanelDashboard;
import com.okutonda.okudpdv.views.pdv.ScreenPdv;
import com.okutonda.okudpdv.views.clients.JPanelClient;
import com.okutonda.okudpdv.views.sales.JPanelSales;
import com.okutonda.okudpdv.views.users.JPanelUser;
import com.okutonda.okudpdv.views.login.ScreenLogin;
import com.formdev.flatlaf.FlatLightLaf;
import com.okutonda.okudpdv.controllers.AdminRoot;
import com.okutonda.okudpdv.controllers.UserController;
import com.okutonda.okudpdv.ui.TemaCleaner;
import com.okutonda.okudpdv.ui.TemaCores;
import com.okutonda.okudpdv.ui.TemaUI;
import com.okutonda.okudpdv.utilities.CompanySession;
import com.okutonda.okudpdv.utilities.JpanelLoader;
import com.okutonda.okudpdv.utilities.UserSession;
import com.okutonda.okudpdv.views.finance.JPanelFinance;
import com.okutonda.okudpdv.views.inventory.JPanelInventory;
import com.okutonda.okudpdv.views.purchases.JPanelPurchases;
import com.okutonda.okudpdv.views.stock.JPanelStockMovement;
import com.okutonda.okudpdv.views.suport.JDialogValidateLicence;
import com.okutonda.okudpdv.views.users.JDialogProfile;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author kenny
 */
public final class ScreenMain extends javax.swing.JFrame {

    JpanelLoader jpload = new JpanelLoader();
    UserSession session;
    CompanySession companySession;
//    ShiftSession shiftSession;
    UserController userController;
//    ShiftController shiftController = new ShiftController();
    AdminRoot root = new AdminRoot();

    public void roles() {
        switch (session.getUser().getProfile()) {
            case "admin":
                break;
            case "user":
                this.jToggleButtonUsers.setEnabled(false);
                this.jToggleButtonUsers.setEnabled(false);
                this.jToggleButtonSettings.setEnabled(false);
                this.jToggleButtonSettings.setEnabled(false);
                break;
            case "seller":
                this.jToggleButtonUsers.setEnabled(false);
//                this.jToggleButtonSideBarSupplier.setEnabled(false);
                this.jToggleButtonSettings.setEnabled(false);
                break;
            case "manager":
                this.jToggleButtonUsers.setEnabled(false);
//                this.jToggleButtonSideBarSupplier.setEnabled(false);
//                this.jButtonOpenPDV.setEnabled(false);
                this.jToggleButtonSettings.setEnabled(false);
                break;
            default:
                break;
        }
    }

    /**
     * Creates new form ScreenMain
     */
    public ScreenMain() {
        initComponents();
        applyTheme();
        userController = new UserController();
        session = UserSession.getInstance();
        companySession = CompanySession.getInstance();

        if (session != null || session.getUser().getStatus() == 1) {
            this.setExtendedState(ScreenMain.MAXIMIZED_BOTH);
            roles();
            JPanelDashboard pDashboard = new JPanelDashboard();
            jpload.jPanelLoader(jPanelContent, pDashboard);
            jLabelNameCompany.setText(companySession.getName());
            jLabelNameUserLogin.setText(session.getUser().getName());
            int itemCount = jComboBoxOptionsDash.getItemCount();
            String itemValue = jComboBoxOptionsDash.getItemAt(itemCount - 1);
            String name = session.getUser().getName();
            if (itemValue == null ? name != null : !itemValue.equals(name)) {
                jComboBoxOptionsDash.addItem(name);
                jComboBoxOptionsDash.setToolTipText(name);
                jComboBoxOptionsDash.setSelectedItem(name);
            }
        }
    }

    private void applyTheme() {
        TemaCleaner.clearBuilderOverrides(getContentPane());
        // Painel de fundo da janela
        jPanelSidebar.setBackground(TemaCores.BG_LIGHT);
//        jPanelSidebar.setBackground(TemaCores.PRIMARY);
        // Card do login
//        TemaUI.aplicarPainelHeader(jPanelSidebar, TemaCores.PRIMARY);
        // Título

        TemaUI.aplicarTitulo(jLabelNameUserLogin);
//        TemaUI.aplicarTitulo(jLabelNameCompany);
//        jLabelNameCompany.setForeground(TemaCores.PRIMARY);
        // Labels
//        jLabel1.setForeground(TemaCores.TEXT_DARK);   // "Email:"
//        jLabel2.setForeground(TemaCores.TEXT_DARK);   // "Senha:"
        // Campos
//        TemaUI.aplicarCampoTexto(jTextFieldEmail);
//        TemaUI.aplicarCampoTexto(jPasswordFieldPassword);
        // Botões
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

    private void iniciarRelogio() {
        Timer timer = new Timer(1000, e -> {
            // Atualiza a cada 1 segundo (1000 ms)
            String horaAtual = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
            jLabelDateTime.setText(horaAtual);
        });
        timer.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupSideBar = new javax.swing.ButtonGroup();
        jPanelSidebar = new javax.swing.JPanel();
        jToggleButtonUsers = new javax.swing.JToggleButton();
        jToggleButtonSideBarDashboard = new javax.swing.JToggleButton();
        jToggleButtonSideBarClient = new javax.swing.JToggleButton();
        jToggleButtonSideBarProduct = new javax.swing.JToggleButton();
        jToggleButtonSideBarSales = new javax.swing.JToggleButton();
        jToggleButtonSideBarPayment = new javax.swing.JToggleButton();
        jToggleButtonSideBarReport = new javax.swing.JToggleButton();
        jToggleButtonSideBarDashboard1 = new javax.swing.JToggleButton();
        jLabelNameCompany = new javax.swing.JLabel();
        jToggleButtonSettings = new javax.swing.JToggleButton();
        jToggleButtonSideBarPurchases = new javax.swing.JToggleButton();
        jLabelNameUserLogin = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanelContent = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabelDateTime = new javax.swing.JLabel();
        jLabelNamePanelOpen = new javax.swing.JLabel();
        jComboBoxOptionsDash = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Principal");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setForeground(new java.awt.Color(242, 242, 242));
        setMinimumSize(new java.awt.Dimension(800, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanelSidebar.setBackground(new java.awt.Color(204, 204, 255));
        jPanelSidebar.setMinimumSize(new java.awt.Dimension(170, 600));
        jPanelSidebar.setPreferredSize(new java.awt.Dimension(180, 700));
        jPanelSidebar.setRequestFocusEnabled(false);

        buttonGroupSideBar.add(jToggleButtonUsers);
        jToggleButtonUsers.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jToggleButtonUsers.setForeground(new java.awt.Color(0, 0, 102));
        jToggleButtonUsers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Male User_5.png"))); // NOI18N
        jToggleButtonUsers.setText("Staff");
        jToggleButtonUsers.setBorderPainted(false);
        jToggleButtonUsers.setContentAreaFilled(false);
        jToggleButtonUsers.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jToggleButtonUsers.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButtonUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonUsersActionPerformed(evt);
            }
        });

        buttonGroupSideBar.add(jToggleButtonSideBarDashboard);
        jToggleButtonSideBarDashboard.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jToggleButtonSideBarDashboard.setForeground(new java.awt.Color(0, 0, 102));
        jToggleButtonSideBarDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/dashboard-interface_5715555.png"))); // NOI18N
        jToggleButtonSideBarDashboard.setSelected(true);
        jToggleButtonSideBarDashboard.setText("Dashboard");
        jToggleButtonSideBarDashboard.setBorderPainted(false);
        jToggleButtonSideBarDashboard.setContentAreaFilled(false);
        jToggleButtonSideBarDashboard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jToggleButtonSideBarDashboard.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButtonSideBarDashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonSideBarDashboardActionPerformed(evt);
            }
        });

        buttonGroupSideBar.add(jToggleButtonSideBarClient);
        jToggleButtonSideBarClient.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jToggleButtonSideBarClient.setForeground(new java.awt.Color(0, 0, 102));
        jToggleButtonSideBarClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Male User_2.png"))); // NOI18N
        jToggleButtonSideBarClient.setText("Entidades");
        jToggleButtonSideBarClient.setBorderPainted(false);
        jToggleButtonSideBarClient.setContentAreaFilled(false);
        jToggleButtonSideBarClient.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jToggleButtonSideBarClient.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButtonSideBarClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonSideBarClientActionPerformed(evt);
            }
        });

        buttonGroupSideBar.add(jToggleButtonSideBarProduct);
        jToggleButtonSideBarProduct.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jToggleButtonSideBarProduct.setForeground(new java.awt.Color(0, 0, 102));
        jToggleButtonSideBarProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Box.png"))); // NOI18N
        jToggleButtonSideBarProduct.setText("Inventário");
        jToggleButtonSideBarProduct.setBorderPainted(false);
        jToggleButtonSideBarProduct.setContentAreaFilled(false);
        jToggleButtonSideBarProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jToggleButtonSideBarProduct.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButtonSideBarProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonSideBarProductActionPerformed(evt);
            }
        });

        buttonGroupSideBar.add(jToggleButtonSideBarSales);
        jToggleButtonSideBarSales.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jToggleButtonSideBarSales.setForeground(new java.awt.Color(0, 0, 102));
        jToggleButtonSideBarSales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/shop_6895262.png"))); // NOI18N
        jToggleButtonSideBarSales.setText("Vendas");
        jToggleButtonSideBarSales.setBorderPainted(false);
        jToggleButtonSideBarSales.setContentAreaFilled(false);
        jToggleButtonSideBarSales.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jToggleButtonSideBarSales.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButtonSideBarSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonSideBarSalesActionPerformed(evt);
            }
        });

        buttonGroupSideBar.add(jToggleButtonSideBarPayment);
        jToggleButtonSideBarPayment.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jToggleButtonSideBarPayment.setForeground(new java.awt.Color(0, 0, 102));
        jToggleButtonSideBarPayment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/report_1321938.png"))); // NOI18N
        jToggleButtonSideBarPayment.setText("Financeiro");
        jToggleButtonSideBarPayment.setBorderPainted(false);
        jToggleButtonSideBarPayment.setContentAreaFilled(false);
        jToggleButtonSideBarPayment.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jToggleButtonSideBarPayment.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButtonSideBarPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonSideBarPaymentActionPerformed(evt);
            }
        });

        buttonGroupSideBar.add(jToggleButtonSideBarReport);
        jToggleButtonSideBarReport.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jToggleButtonSideBarReport.setForeground(new java.awt.Color(0, 0, 102));
        jToggleButtonSideBarReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Document.png"))); // NOI18N
        jToggleButtonSideBarReport.setText("Relatorio");
        jToggleButtonSideBarReport.setBorderPainted(false);
        jToggleButtonSideBarReport.setContentAreaFilled(false);
        jToggleButtonSideBarReport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jToggleButtonSideBarReport.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButtonSideBarReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonSideBarReportActionPerformed(evt);
            }
        });

        buttonGroupSideBar.add(jToggleButtonSideBarDashboard1);
        jToggleButtonSideBarDashboard1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jToggleButtonSideBarDashboard1.setForeground(new java.awt.Color(0, 0, 102));
        jToggleButtonSideBarDashboard1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/cashier_4429519.png"))); // NOI18N
        jToggleButtonSideBarDashboard1.setText("Faturação");
        jToggleButtonSideBarDashboard1.setBorderPainted(false);
        jToggleButtonSideBarDashboard1.setContentAreaFilled(false);
        jToggleButtonSideBarDashboard1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jToggleButtonSideBarDashboard1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButtonSideBarDashboard1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonSideBarDashboard1ActionPerformed(evt);
            }
        });

        jLabelNameCompany.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabelNameCompany.setForeground(new java.awt.Color(0, 0, 102));
        jLabelNameCompany.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelNameCompany.setText("LOGO");

        buttonGroupSideBar.add(jToggleButtonSettings);
        jToggleButtonSettings.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jToggleButtonSettings.setForeground(new java.awt.Color(0, 0, 102));
        jToggleButtonSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Settings.png"))); // NOI18N
        jToggleButtonSettings.setText("Configurações");
        jToggleButtonSettings.setBorderPainted(false);
        jToggleButtonSettings.setContentAreaFilled(false);
        jToggleButtonSettings.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jToggleButtonSettings.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButtonSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonSettingsActionPerformed(evt);
            }
        });

        buttonGroupSideBar.add(jToggleButtonSideBarPurchases);
        jToggleButtonSideBarPurchases.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jToggleButtonSideBarPurchases.setForeground(new java.awt.Color(0, 0, 102));
        jToggleButtonSideBarPurchases.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/shop_6895262.png"))); // NOI18N
        jToggleButtonSideBarPurchases.setText("Compras");
        jToggleButtonSideBarPurchases.setBorderPainted(false);
        jToggleButtonSideBarPurchases.setContentAreaFilled(false);
        jToggleButtonSideBarPurchases.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jToggleButtonSideBarPurchases.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButtonSideBarPurchases.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonSideBarPurchasesActionPerformed(evt);
            }
        });

        jLabelNameUserLogin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelNameUserLogin.setText("Nome Usuario Logado");

        javax.swing.GroupLayout jPanelSidebarLayout = new javax.swing.GroupLayout(jPanelSidebar);
        jPanelSidebar.setLayout(jPanelSidebarLayout);
        jPanelSidebarLayout.setHorizontalGroup(
            jPanelSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSidebarLayout.createSequentialGroup()
                .addGroup(jPanelSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToggleButtonSideBarDashboard, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleButtonSideBarClient, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleButtonSideBarProduct, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleButtonSideBarDashboard1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleButtonSideBarSales, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleButtonSideBarPayment, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleButtonSideBarReport, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleButtonUsers, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleButtonSettings, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                    .addComponent(jToggleButtonSideBarPurchases, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelSidebarLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelNameCompany, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabelNameUserLogin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelSidebarLayout.setVerticalGroup(
            jPanelSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSidebarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelNameCompany)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelNameUserLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jToggleButtonSideBarDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButtonSideBarDashboard1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButtonSideBarClient, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButtonSideBarProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButtonSideBarSales, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButtonSideBarPurchases, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButtonSideBarPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButtonSideBarReport, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButtonUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButtonSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelSidebarLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jToggleButtonSideBarClient, jToggleButtonSideBarDashboard, jToggleButtonSideBarPayment, jToggleButtonSideBarProduct, jToggleButtonSideBarReport, jToggleButtonSideBarSales, jToggleButtonUsers});

        jScrollPane2.setPreferredSize(new java.awt.Dimension(600, 600));

        jPanelContent.setBackground(new java.awt.Color(255, 255, 255));
        jPanelContent.setMinimumSize(new java.awt.Dimension(700, 600));
        jPanelContent.setPreferredSize(new java.awt.Dimension(700, 600));

        javax.swing.GroupLayout jPanelContentLayout = new javax.swing.GroupLayout(jPanelContent);
        jPanelContent.setLayout(jPanelContentLayout);
        jPanelContentLayout.setHorizontalGroup(
            jPanelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 836, Short.MAX_VALUE)
        );
        jPanelContentLayout.setVerticalGroup(
            jPanelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 720, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(jPanelContent);

        jLabelDateTime.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelDateTime.setForeground(new java.awt.Color(0, 0, 102));
        jLabelDateTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelDateTime.setText("Data e hora");

        jLabelNamePanelOpen.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelNamePanelOpen.setForeground(new java.awt.Color(0, 0, 102));
        jLabelNamePanelOpen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelNamePanelOpen.setText("DASHBOARD");

        jComboBoxOptionsDash.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jComboBoxOptionsDash.setForeground(new java.awt.Color(0, 0, 102));
        jComboBoxOptionsDash.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Perfil", "Configuração", "Sair" }));
        jComboBoxOptionsDash.setToolTipText("Usuario");
        jComboBoxOptionsDash.setAutoscrolls(true);
        jComboBoxOptionsDash.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jComboBoxOptionsDash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxOptionsDashActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBoxOptionsDash, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelDateTime, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelNamePanelOpen, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxOptionsDash, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDateTime, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNamePanelOpen, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelSidebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 838, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelSidebar, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(1040, 776));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        //Verificar as sessao do usuario
        if (session.getUser().getStatus() == 0) {
            new ScreenLogin().setVisible(true);
            this.dispose();
        } else {
            if (root.getStatusLicence() == false) {
                JDialogValidateLicence dialogLicence = new JDialogValidateLicence(this, rootPaneCheckingEnabled);
                dialogLicence.setVisible(true);
            } else {
//                Date date = new Date();
//                DateFormat dateF = DateFormat.getDateInstance(DateFormat.SHORT);
//                jLabelDateTime.setText(dateF.format(date));
                iniciarRelogio();
            }

        }
    }//GEN-LAST:event_formWindowActivated

    private void jToggleButtonSideBarClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonSideBarClientActionPerformed
        // TODO add your handling code here:
        JPanelClient pClient = new JPanelClient();
        jpload.jPanelLoader(jPanelContent, pClient);
        jLabelNamePanelOpen.setText("CLIENTES");

    }//GEN-LAST:event_jToggleButtonSideBarClientActionPerformed

    private void jToggleButtonSideBarProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonSideBarProductActionPerformed
        // TODO add your handling code here:
//        JPanelProduct pProduct = new JPanelProduct();
//        jpload.jPanelLoader(jPanelContent, pProduct);
//        jLabelNamePanelOpen.setText("PRODUTO");
        JPanelInventory pInventory = new JPanelInventory();
        jpload.jPanelLoader(jPanelContent, pInventory);
        jLabelNamePanelOpen.setText("INVENTARIO");
    }//GEN-LAST:event_jToggleButtonSideBarProductActionPerformed

    private void jToggleButtonSideBarPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonSideBarPaymentActionPerformed
        // TODO add your handling code here:
        JPanelFinance pFinanec = new JPanelFinance();
        jpload.jPanelLoader(jPanelContent, pFinanec);
        jLabelNamePanelOpen.setText("FINANCEIRO");
//        JPanelPayment pPayment = new JPanelPayment();
//        jpload.jPanelLoader(jPanelContent, pPayment);
//        jLabelNamePanelOpen.setText("PAGAMENTOS");
    }//GEN-LAST:event_jToggleButtonSideBarPaymentActionPerformed

    private void jToggleButtonSideBarSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonSideBarSalesActionPerformed
        // TODO add your handling code here:
        JPanelSales pSales = new JPanelSales();
        jpload.jPanelLoader(jPanelContent, pSales);
        jLabelNamePanelOpen.setText("VENDAS");
    }//GEN-LAST:event_jToggleButtonSideBarSalesActionPerformed

    private void jToggleButtonSideBarReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonSideBarReportActionPerformed
        // TODO add your handling code here:
        JPanelReport pReport = new JPanelReport();
        jpload.jPanelLoader(jPanelContent, pReport);
        jLabelNamePanelOpen.setText("RELATORIOS");
    }//GEN-LAST:event_jToggleButtonSideBarReportActionPerformed

    private void jToggleButtonSideBarDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonSideBarDashboardActionPerformed
        // TODO add your handling code here:
        JPanelDashboard pDashboard = new JPanelDashboard();
        jpload.jPanelLoader(jPanelContent, pDashboard);
        jLabelNamePanelOpen.setText("DASHBOARD");
    }//GEN-LAST:event_jToggleButtonSideBarDashboardActionPerformed

    private void jToggleButtonUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonUsersActionPerformed
        // TODO add your handling code here:
        JPanelUser pUser = new JPanelUser();
        jpload.jPanelLoader(jPanelContent, pUser);
        jLabelNamePanelOpen.setText("USUARIO");
    }//GEN-LAST:event_jToggleButtonUsersActionPerformed

    private void jToggleButtonSideBarDashboard1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonSideBarDashboard1ActionPerformed
        // TODO add your handling code here:
        int sair = JOptionPane.showConfirmDialog(null, "Abrir o PDV ?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (sair == JOptionPane.YES_OPTION) {
            new ScreenPdv().setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_jToggleButtonSideBarDashboard1ActionPerformed

    private void jComboBoxOptionsDashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxOptionsDashActionPerformed
        // TODO add your handling code here:
        String option = (String) jComboBoxOptionsDash.getSelectedItem();

//        || session.getUser().getName().equals(option)
        if ("Perfil".equals(option)) {
            new JDialogProfile(this, rootPaneCheckingEnabled).setVisible(true);
        } else if ("Configuração".equals(option)) {
            new JDialogSetting(this, rootPaneCheckingEnabled).setVisible(true);
        } else if ("Sair".equals(option)) {
            int sair = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja sair?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (sair == JOptionPane.YES_OPTION) {
                this.dispose();
                if (userController.logout()) {
                    new ScreenLogin().setVisible(true);
                }
            }
        }
    }//GEN-LAST:event_jComboBoxOptionsDashActionPerformed

    private void jToggleButtonSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonSettingsActionPerformed
        // TODO add your handling code here:
        new JDialogSetting(this, rootPaneCheckingEnabled).setVisible(true);
    }//GEN-LAST:event_jToggleButtonSettingsActionPerformed

    private void jToggleButtonSideBarPurchasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonSideBarPurchasesActionPerformed
        // TODO add your handling code here:
        JPanelPurchases pPurchases = new JPanelPurchases();
        jpload.jPanelLoader(jPanelContent, pPurchases);
        jLabelNamePanelOpen.setText("COMPRAS");
    }//GEN-LAST:event_jToggleButtonSideBarPurchasesActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Windows".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ScreenMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ScreenMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ScreenMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ScreenMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to initialize LaF");
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ScreenMain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupSideBar;
    private javax.swing.JComboBox<String> jComboBoxOptionsDash;
    private javax.swing.JLabel jLabelDateTime;
    private javax.swing.JLabel jLabelNameCompany;
    private javax.swing.JLabel jLabelNamePanelOpen;
    private javax.swing.JLabel jLabelNameUserLogin;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelContent;
    private javax.swing.JPanel jPanelSidebar;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JToggleButton jToggleButtonSettings;
    public javax.swing.JToggleButton jToggleButtonSideBarClient;
    public javax.swing.JToggleButton jToggleButtonSideBarDashboard;
    public javax.swing.JToggleButton jToggleButtonSideBarDashboard1;
    public javax.swing.JToggleButton jToggleButtonSideBarPayment;
    public javax.swing.JToggleButton jToggleButtonSideBarProduct;
    public javax.swing.JToggleButton jToggleButtonSideBarPurchases;
    public javax.swing.JToggleButton jToggleButtonSideBarReport;
    public javax.swing.JToggleButton jToggleButtonSideBarSales;
    public javax.swing.JToggleButton jToggleButtonUsers;
    // End of variables declaration//GEN-END:variables
}
