/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.okutonda.okudpdv.views.pdv;

import com.okutonda.okudpdv.views.shift.JDialogCloseShift;
import com.okutonda.okudpdv.views.shift.JDialogOpenShift;
import com.okutonda.okudpdv.controllers.OrderController;
import com.okutonda.okudpdv.controllers.ProductController;
import com.okutonda.okudpdv.controllers.ShiftController;
import com.okutonda.okudpdv.dao.ClientDao;
import com.okutonda.okudpdv.dao.ProductDao;
import com.okutonda.okudpdv.utilities.UtilDate;
import com.okutonda.okudpdv.utilities.Utilities;
import com.okutonda.okudpdv.models.Clients;
import com.okutonda.okudpdv.models.Order;
import com.okutonda.okudpdv.models.Product;
import com.okutonda.okudpdv.models.ProductOrder;
import com.okutonda.okudpdv.utilities.ShiftSession;
import com.okutonda.okudpdv.utilities.UserSession;
import com.okutonda.okudpdv.utilities.Util;
import com.okutonda.okudpdv.views.ScreenMain;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kenny
 */
public class ScreenPdv extends javax.swing.JFrame {

    JDialogOrder jdialogOrder;
    UserSession session;
    ShiftSession shiftSession;
    OrderController orderController;
    ProductController productController;
    List<ProductOrder> listProductOrder;
    Clients clientSelected;

    ShiftController shiftController = new ShiftController();
    double subTotal, total;

    /**
     * Creates new form ScreenPdv
     */
    public ScreenPdv() {
        initComponents();
        this.setExtendedState(ScreenPdv.MAXIMIZED_BOTH);
        session = UserSession.getInstance();
        shiftSession = ShiftSession.getInstance();
        //orderController = new OrderController();
        productController = new ProductController();
        listProductOrder = new ArrayList<>();
        clientSelected = new Clients();
        shiftController.getShiftSession();
    }

    public String[] splitString(String str) {
        // Verifica se a string está vazia
        if (str.isEmpty()) {
            return null; // Retorna null se a string estiver vazia
        }
        // Usar regex para separar a string em 'X' ou 'x'
        String[] parts = str.split("(?i)x", 2); // (?i) torna a regex case-insensitive

        // Retorna o array, que terá a string inteira se o delimitador não for encontrado
        return parts.length == 1 ? new String[]{str} : parts;
    }

    public void listProduts() {
        List<Product> list = productController.get("WHERE stock_total>0 and status='1'");
        DefaultTableModel data = (DefaultTableModel) jTableProducts.getModel();
        data.setNumRows(0);
        for (Product c : list) {
            data.addRow(new Object[]{
                c.getId(),
                c.getBarcode(),
                c.getDescription(),
                c.getPrice(),
                c.getStockTotal()
            }
            );
        }
    }

    public void listProdutsOrder() {
        DefaultTableModel data = (DefaultTableModel) jTableProductsInvoice.getModel();
        data.setNumRows(0);
        for (ProductOrder c : listProductOrder) {
            data.addRow(new Object[]{
                c.getProduct().getId(),
                c.getCode(),
                c.getDescription(),
                c.getPrice(),
                c.getQty(),
                productController.CalculateTotalProduct(c.getProduct(), c.getQty())
//                c.getQty() * c.getPrice()
            }
            );
        }
    }

    public void filterListProducts(String txt) {
        ProductDao cDao = new ProductDao();
        List<Product> list = cDao.filter(txt);
        DefaultTableModel data = (DefaultTableModel) jTableProducts.getModel();
        data.setNumRows(0);
        for (Product c : list) {
            data.addRow(new Object[]{
                c.getId(),
                c.getBarcode(),
                c.getDescription(),
                c.getPrice(),
                c.getStockTotal(),
                c.getSupplier().getName(),});
        }
    }

//    public void listClients() {
//        ClientDao dao = new ClientDao();
//        List<Clients> list = dao.list("");
//        jComboBoxClients.removeAllItems();
//        for (Clients item : list) {
//            jComboBoxClients.addItem(item);
//        }
//    }
//    public void seletedClient() {
////        String name = jComboBoxClients.getSelectedItem().toString();
////        ClientDao cDao = new ClientDao();
////        clientSelected = cDao.searchFromName(name);
////        if (clientSelected.getName() != null) {
////            jTextFieldNifClient.setText(clientSelected.getNif());
////            jLabelNameClienteSelected.setText(clientSelected.getName());
////        } else {
////            JOptionPane.showMessageDialog(null, "Cliente nao encontrado!");
////        }
//    }
    public void backDashboard() {
        new ScreenMain().setVisible(true);
        this.dispose();
    }

    public void calculTotal() {
        total = subTotal = 0;
        for (ProductOrder productOrder : listProductOrder) {
            subTotal = productOrder.getProduct().getPrice() * productOrder.getQty();
            total += productOrder.getProduct().getPrice() * productOrder.getQty();
        }
        
        jTextFieldTotalInvoice.setText(String.valueOf(total));
        jTextFieldPayClient.setText(String.valueOf(total));
    }

    public void clearOrder() {
        listProductOrder.clear();
        listProdutsOrder();
        calculTotal();
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
        jLabelNameUserSeller = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabelDateTime = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jButtonPainel = new javax.swing.JButton();
        jButtonCloseShift = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jTextFieldPayClient = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldTotalInvoice = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldTotalChange = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jButtonFinishInvoice = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableProductsInvoice = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        jButtonRemoveAllProd = new javax.swing.JButton();
        jButtonRemoveProd = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldNifClient = new javax.swing.JTextField();
        jButtonPesquisarCompany = new javax.swing.JButton();
        jLabelNameClienteSelected = new javax.swing.JLabel();
        jPanelSelectedProduct = new javax.swing.JPanel();
        jTextFieldSearchProducts = new javax.swing.JTextField();
        jButtonSearchProduct = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldBarCodeProductSelect = new javax.swing.JTextField();
        jTextFieldQtdProductsSelected = new javax.swing.JTextField();
        jButtonAddProductInvoice = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableProducts = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 0, 51));

        jLabelNameUserSeller.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabelNameUserSeller.setForeground(new java.awt.Color(255, 255, 255));
        jLabelNameUserSeller.setText("Usuario");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("PDV");

        jLabelDateTime.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDateTime.setText("Data e hora");

        jToolBar1.setRollover(true);
        jToolBar1.setMargin(new java.awt.Insets(0, 5, 0, 5));

        jButtonPainel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonPainel.setText("Painel");
        jButtonPainel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPainelActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonPainel);

        jButtonCloseShift.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonCloseShift.setText("Fechar caixa");
        jButtonCloseShift.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseShiftActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonCloseShift);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(jLabelNameUserSeller)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelDateTime, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(jLabel14)
                    .addContainerGap(1080, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabelNameUserSeller))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabelDateTime, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel14)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel10.setBackground(new java.awt.Color(0, 0, 51));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jTextFieldPayClient.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        jTextFieldPayClient.setText("0.00");
        jTextFieldPayClient.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldPayClientKeyReleased(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Valor Pago");

        jTextFieldTotalInvoice.setEditable(false);
        jTextFieldTotalInvoice.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jTextFieldTotalInvoice.setForeground(new java.awt.Color(204, 0, 0));
        jTextFieldTotalInvoice.setText("0.00");
        jTextFieldTotalInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldTotalInvoiceActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setText("Total a Pagar");

        jTextFieldTotalChange.setEditable(false);
        jTextFieldTotalChange.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        jTextFieldTotalChange.setText("0.00");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel13.setText("Diferença");

        jButtonFinishInvoice.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButtonFinishInvoice.setText("Imprimir FR");
        jButtonFinishInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFinishInvoiceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel13)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel9)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextFieldPayClient)
                            .addComponent(jTextFieldTotalInvoice, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                            .addComponent(jTextFieldTotalChange)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonFinishInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jTextFieldPayClient, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldTotalInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)))
                    .addComponent(jLabel1))
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextFieldTotalChange, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonFinishInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTableProductsInvoice.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTableProductsInvoice.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Codigo", "Produto", "Preço", "Qtd", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
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
        jScrollPane1.setViewportView(jTableProductsInvoice);
        if (jTableProductsInvoice.getColumnModel().getColumnCount() > 0) {
            jTableProductsInvoice.getColumnModel().getColumn(0).setMinWidth(30);
            jTableProductsInvoice.getColumnModel().getColumn(0).setPreferredWidth(40);
            jTableProductsInvoice.getColumnModel().getColumn(0).setMaxWidth(50);
            jTableProductsInvoice.getColumnModel().getColumn(1).setMinWidth(80);
            jTableProductsInvoice.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTableProductsInvoice.getColumnModel().getColumn(1).setMaxWidth(120);
            jTableProductsInvoice.getColumnModel().getColumn(2).setMinWidth(200);
            jTableProductsInvoice.getColumnModel().getColumn(2).setPreferredWidth(250);
            jTableProductsInvoice.getColumnModel().getColumn(2).setMaxWidth(300);
            jTableProductsInvoice.getColumnModel().getColumn(4).setMinWidth(40);
            jTableProductsInvoice.getColumnModel().getColumn(4).setPreferredWidth(50);
            jTableProductsInvoice.getColumnModel().getColumn(4).setMaxWidth(60);
        }

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel17.setText("Fatura");

        jButtonRemoveAllProd.setText("Remover Tudo");
        jButtonRemoveAllProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveAllProdActionPerformed(evt);
            }
        });

        jButtonRemoveProd.setText("Remover");
        jButtonRemoveProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveProdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonRemoveAllProd)
                .addGap(18, 18, 18)
                .addComponent(jButtonRemoveProd)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jButtonRemoveAllProd, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonRemoveProd, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(0, 0, 51));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Localizar o Cliente por NIF");

        jTextFieldNifClient.setAutoscrolls(false);

        jButtonPesquisarCompany.setText("Add");
        jButtonPesquisarCompany.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPesquisarCompanyActionPerformed(evt);
            }
        });

        jLabelNameClienteSelected.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabelNameClienteSelected.setText("Consumidor final");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jTextFieldNifClient, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonPesquisarCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jLabelNameClienteSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldNifClient, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonPesquisarCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNameClienteSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        jPanelSelectedProduct.setBackground(new java.awt.Color(255, 255, 255));

        jTextFieldSearchProducts.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSearchProductsKeyReleased(evt);
            }
        });

        jButtonSearchProduct.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonSearchProduct.setText("Produtos");
        jButtonSearchProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchProductActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Selecionar Produto");

        jTextFieldBarCodeProductSelect.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jTextFieldBarCodeProductSelect.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldBarCodeProductSelectKeyPressed(evt);
            }
        });

        jTextFieldQtdProductsSelected.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTextFieldQtdProductsSelected.setText("1");
        jTextFieldQtdProductsSelected.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldQtdProductsSelectedKeyReleased(evt);
            }
        });

        jButtonAddProductInvoice.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonAddProductInvoice.setText("ADD");
        jButtonAddProductInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddProductInvoiceActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("QTD X Codigo de Barra");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("QTD");

        javax.swing.GroupLayout jPanelSelectedProductLayout = new javax.swing.GroupLayout(jPanelSelectedProduct);
        jPanelSelectedProduct.setLayout(jPanelSelectedProductLayout);
        jPanelSelectedProductLayout.setHorizontalGroup(
            jPanelSelectedProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSelectedProductLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanelSelectedProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSelectedProductLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelSelectedProductLayout.createSequentialGroup()
                        .addGroup(jPanelSelectedProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelSelectedProductLayout.createSequentialGroup()
                                .addGroup(jPanelSelectedProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldBarCodeProductSelect)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanelSelectedProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelSelectedProductLayout.createSequentialGroup()
                                        .addComponent(jTextFieldQtdProductsSelected)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButtonAddProductInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel10)))
                            .addGroup(jPanelSelectedProductLayout.createSequentialGroup()
                                .addComponent(jTextFieldSearchProducts)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonSearchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        jPanelSelectedProductLayout.setVerticalGroup(
            jPanelSelectedProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSelectedProductLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSelectedProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel10))
                .addGap(1, 1, 1)
                .addGroup(jPanelSelectedProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldQtdProductsSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAddProductInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldBarCodeProductSelect, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
                .addGap(7, 7, 7)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelSelectedProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldSearchProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSearchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTableProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Codigo de Barra", "Produto", "Preço", "Estoque"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class
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
        jTableProducts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableProductsMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableProducts);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanelSelectedProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelSelectedProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        setSize(new java.awt.Dimension(1256, 599));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
//        System.out.println("hhh" + shiftSession.getShift());
        if (shiftSession.getShift() == null) {
            new JDialogOpenShift(this, rootPaneCheckingEnabled).setVisible(true);
            if (shiftSession.getShift() == null) {
                backDashboard();
            }
        } else {
            listProduts();
//            listClients();
            String date = UtilDate.getDateNow();
            jLabelDateTime.setText(date);
            jLabelNameUserSeller.setText(shiftSession.getSeller().getName());
        }
    }//GEN-LAST:event_formWindowActivated

    private void jButtonPesquisarCompanyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPesquisarCompanyActionPerformed
        // TODO add your handling code here:
        String nif = jTextFieldNifClient.getText();
        ClientDao cDao = new ClientDao();
        clientSelected = cDao.searchFromNif(nif);
        if (clientSelected.getName() != null) {
            jTextFieldNifClient.setText(clientSelected.getNif());
            jLabelNameClienteSelected.setText(clientSelected.getName());
        } else {
            JOptionPane.showMessageDialog(null, "Cliente nao encontrado!");
        }
    }//GEN-LAST:event_jButtonPesquisarCompanyActionPerformed

    private void jButtonAddProductInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddProductInvoiceActionPerformed
        // TODO add your handling code here:
        String str = jTextFieldBarCodeProductSelect.getText();

//         String str = "12X3333333444"; // Exemplo com delimitador
        // String str = "123456789"; // Exemplo sem delimitador
        String[] result = splitString(str);

        System.out.println("Resultado:");

        // Acessando os valores do array por índice
        String barcode = "";
        int qtd = 1;
        if (result == null) {
            System.out.println("Nenhum valor (string vazia)");
            JOptionPane.showMessageDialog(null, "Selecione um produto da lista!!");
        } else {
            if (result.length == 1) {
                barcode = result[0];
                qtd = Integer.parseInt(jTextFieldQtdProductsSelected.getText());
                System.out.println("Parte 0: " + result[0]); // Apenas a parte inteira
            } else {
                qtd = Integer.parseInt(result[0]);
//                barcode = result[0];
                barcode = result[1];
                System.out.println("Parte 0: " + result[0]); // Antes do delimitador
                System.out.println("Parte 1: " + result[1]); // Depois do delimitador

            }
//        }
//
//        System.out.println("codigo: " + barcode);
//
//        if (barcode.isEmpty() != true) {
            Product prod = productController.getFromBarCode(barcode);
//            
//            System.out.println("product encontrado1:" + prod.getDescription());
            if (prod.getDescription() != null) {
//                System.out.println("product encontrado2:" + prod.getDescription());
                if (prod.getStockTotal() >= qtd && qtd > 0) {
                    ProductOrder prodOrder = new ProductOrder();
                    prodOrder.setProduct(prod);
                    prodOrder.setDescription(prod.getDescription());
                    prodOrder.setTaxePercentage(prod.getTaxe().getPercetage());
                    prodOrder.setTaxeCode(prod.getTaxe().getCode());
                    prodOrder.setTaxeName(prod.getTaxe().getName());
                    prodOrder.setReasonTax(prod.getReasonTaxe().getReason());
                    prodOrder.setReasonCode(prod.getReasonTaxe().getCode());
                    prodOrder.setCode(prod.getCode());
                    prodOrder.setPrice(prod.getPrice());
                    prodOrder.setQty(qtd);

                    for (ProductOrder productOrder : listProductOrder) {
                        if (productOrder.getProduct().getId() == prodOrder.getProduct().getId()) {
                            prodOrder.setQty(prodOrder.getQty() + productOrder.getQty());
                            listProductOrder.remove(productOrder);
                            break;
                        }
                    }
//                    System.out.println(prodOrder);
                    listProductOrder.add(prodOrder);
                    listProdutsOrder();
                    calculTotal();
                    new Utilities().clearScreen(jPanelSelectedProduct);
                    jTextFieldQtdProductsSelected.setText("1");
//                    jTextFieldBarCodeProductSelect.setText("1X");

                } else if (qtd <= 0) {
                    JOptionPane.showMessageDialog(null, "Quantidade invalida!! Verifica a Quantidade de produto!", "Atenção", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Estoque insuficiente!", "Atenção", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Não foi possivel adicionar Produto ao carrinho. verifica O Codigo de barra!!", "Atenção", JOptionPane.ERROR_MESSAGE);
            }

        }
//        else {
//            JOptionPane.showMessageDialog(null, "Selecione um produto da lista!!");
//        }
    }//GEN-LAST:event_jButtonAddProductInvoiceActionPerformed

    private void jTextFieldBarCodeProductSelectKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldBarCodeProductSelectKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String barCode = jTextFieldBarCodeProductSelect.getText();
            Product pModel;
            pModel = productController.getFromBarCode(barCode);
            if (pModel.getDescription() == null) {
                JOptionPane.showMessageDialog(null, "Produto nao encontrado!");
            }
            System.out.println("clicado!!");
        }

    }//GEN-LAST:event_jTextFieldBarCodeProductSelectKeyPressed

    private void jTextFieldSearchProductsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearchProductsKeyReleased
        // TODO add your handling code here:
        String txt = jTextFieldSearchProducts.getText();
        if (!txt.isEmpty()) {
            filterListProducts(txt);
        } else {
            listProduts();
        }
    }//GEN-LAST:event_jTextFieldSearchProductsKeyReleased

    private void jTextFieldPayClientKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPayClientKeyReleased
        // TODO add your handling code here:
        Double payClient = Double.valueOf(jTextFieldPayClient.getText());
        Double totalInvoice = Double.valueOf(jTextFieldTotalInvoice.getText());
        Double totalChange = payClient - totalInvoice;
        jTextFieldTotalChange.setText(String.valueOf(totalChange));
    }//GEN-LAST:event_jTextFieldPayClientKeyReleased

    private void jButtonFinishInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFinishInvoiceActionPerformed
        // TODO add your handling code here:

        if (!listProductOrder.isEmpty()) {
            Double totalPay = Double.valueOf(jTextFieldPayClient.getText());
            Double changeInvoice = Double.valueOf(jTextFieldTotalChange.getText());
            if (total > 0 && total >= changeInvoice && changeInvoice >= 0) {
                try {
                    if (clientSelected != null) {
                        ClientDao clientDao = new ClientDao();
                        clientSelected = clientDao.getClientDefault();
                    }
                    Order order = new Order();
//                    String prefix = UtilSales.getPrefix("order");
//                    int number = orderController.getNextNumber();
//                    String numberOrder = prefix + number;
//                    String date = UtilDate.getFormatDataNow();
//                    String hash = UtilSaft.appGenerateHashInvoice(date, date, numberOrder, String.valueOf(total), "");

                    order.setStatus(1);
                    order.setYear(UtilDate.getYear());
//                    order.setDatecreate(date);
                    order.setTotal(total);
                    order.setSubTotal(subTotal);
                    order.setTotalTaxe(subTotal);
                    order.setPayTotal(totalPay);
                    order.setAmountReturned(changeInvoice);
//                    order.setNumber(number);
//                    order.setPrefix(UtilSales.getPrefix("order"));
//                order.setHash(UtilSaft.appGenerateHashInvoice(date, date, numberOrder, String.valueOf(total), ""));
//                    order.setHash(hash);

                    order.setClient(clientSelected);
                    order.setSeller(shiftSession.getSeller());
                    order.setProducts(listProductOrder);

                    JDialogOrder jdOrder = new JDialogOrder(this, true);
                    jdOrder.setOrder(order);
                    jdOrder.setVisible(true);

                    Boolean respo = jdOrder.getResponse();

                    if (respo == true) {
                        JOptionPane.showMessageDialog(null, "Venda efetuada com sucesso!!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                        clearOrder();
                        listProduts();
//                        helpUtil.clearScreen(jPanel3);
//                        helpUtil.clearScreen(jPanel3);
//                        order = null;

                    } else {
                        JOptionPane.showMessageDialog(null, "Venda nao cadastrada!!", "Atenção", JOptionPane.ERROR);
                    }
//                Order result = orderController.add(order);
//                if (result != null) {
//                    total = 0.0;
//                    subTotal = 0.0;
//                    JOptionPane.showMessageDialog(null, "Venda efetuada com sucesso!! Num: " + result.getId(), "Atenção", JOptionPane.INFORMATION_MESSAGE);
//                } else {
//                    JOptionPane.showMessageDialog(null, "Venda nao cadastrada!!", "Atenção", JOptionPane.ERROR);
//                }
                } catch (Exception ex) {
                    Logger.getLogger(ScreenPdv.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Valor Pago insuficiente!", "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Adicione producto para fazer uma venda", "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButtonFinishInvoiceActionPerformed

    private void jButtonRemoveProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveProdActionPerformed
        // TODO add your handling code here:
        int value = 0;
        try {
            value = (int) jTableProductsInvoice.getValueAt(jTableProductsInvoice.getSelectedRow(), 0);
            System.out.println("jTableProductsInvoice id:" + value);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Selecione um Products na tabela!!", "Atencao", JOptionPane.ERROR_MESSAGE);
        } finally {
//            System.out.println("jTableUsers id:" + value);
            if (value > 0) {
                for (ProductOrder productOrder : listProductOrder) {
                    if (productOrder.getProduct().getId() == value) {
                        listProductOrder.remove(productOrder);
                        break;
                    }
                }
                listProdutsOrder();
                calculTotal();
            }
        }
    }//GEN-LAST:event_jButtonRemoveProdActionPerformed

    private void jButtonRemoveAllProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveAllProdActionPerformed
        // TODO add your handling code here:
        listProductOrder.clear();
        listProdutsOrder();
        calculTotal();
    }//GEN-LAST:event_jButtonRemoveAllProdActionPerformed

    private void jButtonPainelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPainelActionPerformed
        // TODO add your handling code here:
        int yes = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja ir ao Dashboard??", "Atenção", JOptionPane.YES_NO_OPTION);
        if (yes == JOptionPane.YES_OPTION) {
            backDashboard();
        }
    }//GEN-LAST:event_jButtonPainelActionPerformed

    private void jButtonCloseShiftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseShiftActionPerformed
        // TODO add your handling code here:
        new JDialogCloseShift(this, rootPaneCheckingEnabled).setVisible(true);
    }//GEN-LAST:event_jButtonCloseShiftActionPerformed

    private void jTextFieldQtdProductsSelectedKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldQtdProductsSelectedKeyReleased
        // TODO add your handling code here:
        if (Util.checkIsNumber(jTextFieldQtdProductsSelected.getText()) == false) {
            JOptionPane.showMessageDialog(null, "Insira um numero valido!");
        }
    }//GEN-LAST:event_jTextFieldQtdProductsSelectedKeyReleased

    private void jTableProductsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableProductsMouseClicked
        // TODO add your handling code here:
        jTextFieldBarCodeProductSelect.setText((String) jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 1));
        //        jTextFieldNameProductSelected.setText(jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 2).toString());
        //        jTextFieldPriceProductSelect.setText(jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 3).toString());
        //        jTextFieldStock.setText(jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 4).toString());
    }//GEN-LAST:event_jTableProductsMouseClicked

    private void jButtonSearchProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchProductActionPerformed
        // TODO add your handling code here:
        JDialogProductSearch jdP = new JDialogProductSearch(this, true);
        jdP.setVisible(true);
    }//GEN-LAST:event_jButtonSearchProductActionPerformed

    private void jTextFieldTotalInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldTotalInvoiceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldTotalInvoiceActionPerformed

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ScreenPdv.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ScreenPdv.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ScreenPdv.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ScreenPdv.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ScreenPdv().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddProductInvoice;
    private javax.swing.JButton jButtonCloseShift;
    private javax.swing.JButton jButtonFinishInvoice;
    private javax.swing.JButton jButtonPainel;
    private javax.swing.JButton jButtonPesquisarCompany;
    private javax.swing.JButton jButtonRemoveAllProd;
    private javax.swing.JButton jButtonRemoveProd;
    private javax.swing.JButton jButtonSearchProduct;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelDateTime;
    private javax.swing.JLabel jLabelNameClienteSelected;
    private javax.swing.JLabel jLabelNameUserSeller;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanelSelectedProduct;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTableProducts;
    private javax.swing.JTable jTableProductsInvoice;
    private javax.swing.JTextField jTextFieldBarCodeProductSelect;
    private javax.swing.JTextField jTextFieldNifClient;
    private javax.swing.JTextField jTextFieldPayClient;
    private javax.swing.JTextField jTextFieldQtdProductsSelected;
    private javax.swing.JTextField jTextFieldSearchProducts;
    private javax.swing.JTextField jTextFieldTotalChange;
    private javax.swing.JTextField jTextFieldTotalInvoice;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
