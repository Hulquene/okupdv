/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.okutonda.okudpdv.views.report;

import com.okutonda.okudpdv.controllers.OrderController;
import com.okutonda.okudpdv.controllers.ProductController;
import com.okutonda.okudpdv.controllers.ProductOrderController;
import com.okutonda.okudpdv.controllers.ReportController;
import com.okutonda.okudpdv.controllers.UserController;
import com.okutonda.okudpdv.data.entities.Order;
import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.ProductOrder;
import com.okutonda.okudpdv.data.entities.User;
import com.okutonda.okudpdv.utilities.UtilSales;
import com.okutonda.okudpdv.views.export.JDialogExportSaftFat;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kenny
 */
public final class JPanelReport extends javax.swing.JPanel {

    UserController sellerController = new UserController();
    ProductController productController = new ProductController();
    ProductOrderController productOrderController = new ProductOrderController();
    OrderController orderController = new OrderController();
//    ShiftController shiftController = new ShiftController();
    ReportController reportController = new ReportController();

    /**
     * Creates new form JPanelReport
     */
    public JPanelReport() {
        initComponents();
        listOrder();
        listOrderSeller();
        listSalesProducts();
//        listShifts();
        listButSalesProducts();

    }

//    public void listShifts() {
//        List<Shift> list = shiftController.get("");
//        DefaultTableModel data = (DefaultTableModel) jTableHistoryShift.getModel();
//        data.setNumRows(0);
//        for (Shift c : list) {
//            data.addRow(new Object[]{
//                c.getId(),
//                c.getCode(),
//                c.getUser().getName(),
//                c.getGrantedAmount(),
//                c.getIncurredAmount(),
//                c.getClosingAmount(),
//                c.getDateOpen(),
//                c.getDateClose(),
//                c.getStatus()
//            });
//        }
//    }
    public void listSalesProducts() {
        List<ProductOrder> list = productOrderController.get("");
        DefaultTableModel data = (DefaultTableModel) jTableReportSalesOrderProduct.getModel();
        data.setNumRows(0);
        for (ProductOrder c : list) {
            data.addRow(new Object[]{
                c.getId(),
                c.getDate(),
                "Descricao",
                c.getDescription(),
                c.getQty(),
                c.getPrice(),
                c.getTaxePercentage(),
                c.getPrice().multiply(BigDecimal.valueOf(c.getQty()))
//                (c.getQty() * c.getPrice())
            });
        }
    }

//    public void listProducts() {
//        List<Product> list = productController.get("");
//        DefaultTableModel data = (DefaultTableModel) jTableReportSalesOrderProduct.getModel();
//        data.setNumRows(0);
//        for (Product c : list) {
//            data.addRow(new Object[]{
//                c.getId(),
//                c.getType(),
//                c.getDescription(),
//                c.getPrice(),
//                c.getPurchasePrice(),
//                c.getStockTotal(),
//                c.getTaxe().getPercetage(),
//                c.getSupplier().getName()
//            });
//        }
//    }
    public void listButSalesProducts() {
        List<ProductOrder> list = productOrderController.get("");
        DefaultTableModel data = (DefaultTableModel) jTableReportButSalesProduct.getModel();
        data.setNumRows(0);

        for (ProductOrder c : list) {
            data.addRow(new Object[]{
                c.getId(),
                c.getCode(),
                c.getDescription(),
                c.getQty(),
                c.getPrice(),
                c.getTaxePercentage(),
                c.getPrice().multiply(BigDecimal.valueOf(c.getQty()))
//                (c.getQty() * c.getPrice())
            });
        }
    }

    public void filterListProduct(String txt, String type) {
        List<Product> list = productController.listForPDV(txt);
        DefaultTableModel data = (DefaultTableModel) jTableReportSalesOrderProduct.getModel();
        data.setNumRows(0);
        for (Product c : list) {
            data.addRow(new Object[]{
                c.getId(),
                c.getType(),
                c.getDescription(),
                c.getPrice(),
                c.getPurchasePrice(),
                c.getCurrentStock(),
                c.getTaxe().getPercetage(),
                c.getGroup().getName()
            });
        }
    }

    public void listOrder() {
        List<Order> list = orderController.get();
        DefaultTableModel data = (DefaultTableModel) jTableReportSalesOrders.getModel();
        data.setNumRows(0);
        for (Order c : list) {
            data.addRow(new Object[]{
                c.getId(),
                c.getSeller().getName(),
                UtilSales.FormatedNumberPrefix(c.getId(), c.getPrefix()),
                c.getDatecreate(),
                c.getTotalTaxe(),
                c.getTotal()
            }
            );
        }
    }

    public void listOrderSeller() {
        List<Order> list = orderController.get();
        DefaultTableModel data = (DefaultTableModel) jTableReportSalesOrderSeller.getModel();
        data.setNumRows(0);
        for (Order c : list) {
            data.addRow(new Object[]{
                c.getId(),
                c.getSeller().toString(),
                UtilSales.FormatedNumberPrefix(c.getId(), c.getPrefix()),
                c.getDatecreate(),
                c.getSubTotal(),
                c.getTotal()
            });
        }
    }

    public void filterListOrderSeller(int idSeller) {
        List<Order> list = orderController.getOrderSeller(0);
        DefaultTableModel data = (DefaultTableModel) jTableReportSalesOrderSeller.getModel();
        data.setNumRows(0);
        for (Order c : list) {
            data.addRow(new Object[]{
                c.getId(),
                c.getSeller().getName(),
                UtilSales.FormatedNumberPrefix(c.getId(), c.getPrefix()),
                c.getDatecreate(),
                c.getTotalTaxe(),
                c.getTotal()
            });
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

        jScrollPane7 = new javax.swing.JScrollPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableReportSalesOrderProduct = new javax.swing.JTable();
        jComboBoxReportSalesOrderProduct = new javax.swing.JComboBox();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jFormattedTextField4 = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableReportButSalesProduct = new javax.swing.JTable();
        jTextFieldReportSalesOrderProductNameSearch1 = new javax.swing.JTextField();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jTextFieldReportSalesOrderProductNameSearch2 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableReportSalesOrderSeller = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jComboBoxReportSalesOrderSeller = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jFormattedTextField7 = new javax.swing.JFormattedTextField();
        jFormattedTextField8 = new javax.swing.JFormattedTextField();
        jComboBoxReportSalesOrderProduct2 = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableReportSalesOrders = new javax.swing.JTable();
        jButtonExportOrderAllExcell = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jComboBoxReportSalesOrderProduct1 = new javax.swing.JComboBox();
        jFormattedTextField5 = new javax.swing.JFormattedTextField();
        jFormattedTextField6 = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButtonExpSaftFact = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(900, 600));
        setPreferredSize(new java.awt.Dimension(900, 600));

        jTabbedPane2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel6.setBackground(new java.awt.Color(204, 204, 255));

        jTableReportSalesOrderProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "id", "Data", "Descrção", "Produto", "Quatidade", "Preço", "Imposto", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTableReportSalesOrderProduct);

        jComboBoxReportSalesOrderProduct.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jComboBoxReportSalesOrderProductAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jButton8.setText("XML");
        jButton8.setEnabled(false);

        jButton9.setText("PDF");
        jButton9.setEnabled(false);

        jButton7.setText("Excell");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jFormattedTextField3.setText("jFormattedTextField3");

        jFormattedTextField4.setText("jFormattedTextField3");

        jLabel3.setText("jLabel3");

        jLabel4.setText("jLabel3");

        jLabel5.setText("jLabel3");

        jLabel16.setText("Exportar dados por:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 885, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxReportSalesOrderProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jFormattedTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(161, 161, 161)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jButton8)
                                .addGap(18, 18, 18)
                                .addComponent(jButton9)
                                .addGap(18, 18, 18)
                                .addComponent(jButton7))
                            .addComponent(jLabel16))))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBoxReportSalesOrderProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jFormattedTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton9)
                        .addComponent(jButton7)))
                .addGap(9, 9, 9)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jComboBoxReportSalesOrderProduct, jFormattedTextField3, jFormattedTextField4});

        jPanel6Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton7, jButton8, jButton9});

        jTabbedPane2.addTab("Produtos", jPanel6);

        jPanel9.setBackground(new java.awt.Color(204, 204, 255));

        jTableReportButSalesProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "id", "Codigo", "Produto", "Quantidade", "Preço", "Imposto", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
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
        jScrollPane6.setViewportView(jTableReportButSalesProduct);

        jTextFieldReportSalesOrderProductNameSearch1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldReportSalesOrderProductNameSearch1KeyReleased(evt);
            }
        });

        jButton13.setText("XML");
        jButton13.setEnabled(false);

        jButton14.setText("PDF");
        jButton14.setEnabled(false);

        jButton15.setText("Excell");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jTextFieldReportSalesOrderProductNameSearch2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldReportSalesOrderProductNameSearch2KeyReleased(evt);
            }
        });

        jLabel1.setText("jLabel1");

        jLabel2.setText("jLabel1");

        jLabel15.setText("Exportar dados por:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 883, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldReportSalesOrderProductNameSearch2, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldReportSalesOrderProductNameSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(377, 377, 377)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jButton14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton15)))))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel9Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jTextFieldReportSalesOrderProductNameSearch1, jTextFieldReportSalesOrderProductNameSearch2});

        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldReportSalesOrderProductNameSearch2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldReportSalesOrderProductNameSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton14)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 448, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jPanel9Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jTextFieldReportSalesOrderProductNameSearch1, jTextFieldReportSalesOrderProductNameSearch2});

        jPanel9Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton13, jButton14, jButton15});

        jTabbedPane2.addTab("Produtos mais Vendidos", jPanel9);

        jPanel5.setBackground(new java.awt.Color(204, 204, 255));

        jTableReportSalesOrderSeller.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Vendedor", "Fatura", "Data", "Imposto", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class
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
        jScrollPane1.setViewportView(jTableReportSalesOrderSeller);

        jButton1.setText("Excel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jComboBoxReportSalesOrderSeller.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jComboBoxReportSalesOrderSellerAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jButton2.setText("XML");
        jButton2.setEnabled(false);

        jButton3.setText("PDF");
        jButton3.setEnabled(false);

        jLabel9.setText("jLabel9");

        jFormattedTextField7.setText("jFormattedTextField3");

        jFormattedTextField8.setText("jFormattedTextField3");

        jComboBoxReportSalesOrderProduct2.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jComboBoxReportSalesOrderProduct2AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jLabel10.setText("jLabel3");

        jLabel11.setText("jLabel3");

        jLabel12.setText("jLabel3");

        jLabel13.setText("Exportar por:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jComboBoxReportSalesOrderSeller, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxReportSalesOrderProduct2, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jFormattedTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel13))))
                .addGap(0, 16, Short.MAX_VALUE))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jFormattedTextField7, jFormattedTextField8});

        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9)
                            .addComponent(jLabel12)
                            .addComponent(jLabel11))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxReportSalesOrderSeller, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxReportSalesOrderProduct2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                            .addComponent(jButton2)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(13, 13, 13)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jComboBoxReportSalesOrderSeller, jFormattedTextField7, jFormattedTextField8});

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton1, jButton3});

        jTabbedPane2.addTab("Atividades de Vendas Vendedor", jPanel5);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        jTableReportSalesOrders.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Vendedor", "Fatura", "Data", "Tota de Imposto", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class
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
        jScrollPane4.setViewportView(jTableReportSalesOrders);

        jButtonExportOrderAllExcell.setText("Excell");
        jButtonExportOrderAllExcell.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportOrderAllExcellActionPerformed(evt);
            }
        });

        jButton11.setText("PDF");
        jButton11.setEnabled(false);

        jButton12.setText("XML");
        jButton12.setEnabled(false);

        jComboBoxReportSalesOrderProduct1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jComboBoxReportSalesOrderProduct1AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jFormattedTextField5.setText("jFormattedTextField3");

        jFormattedTextField6.setText("jFormattedTextField3");

        jLabel6.setText("jLabel3");

        jLabel7.setText("jLabel3");

        jLabel8.setText("jLabel3");

        jLabel14.setText("Exportar dados por:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 883, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxReportSalesOrderProduct1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jFormattedTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton11)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonExportOrderAllExcell, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(160, 160, 160)
                                .addComponent(jLabel14)
                                .addGap(0, 158, Short.MAX_VALUE)))))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton11, jButton12, jButtonExportOrderAllExcell});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jLabel7)
                        .addComponent(jLabel8))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel14)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxReportSalesOrderProduct1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonExportOrderAllExcell))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jComboBoxReportSalesOrderProduct1, jFormattedTextField5, jFormattedTextField6});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton11, jButton12, jButtonExportOrderAllExcell});

        jTabbedPane2.addTab("Atividades de Faturas FR", jPanel1);

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));

        jButtonExpSaftFact.setText("Ficheiro SAFT de Faturação");
        jButtonExpSaftFact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExpSaftFactActionPerformed(evt);
            }
        });

        jLabel17.setText("Gere o ficheiro XML com dados de faturação");

        jLabel18.setText("no formato exigido pela AGT.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonExpSaftFact, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(692, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jButtonExpSaftFact, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addContainerGap(474, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Exportações", jPanel2);

        jScrollPane7.setViewportView(jTabbedPane2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxReportSalesOrderSellerAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jComboBoxReportSalesOrderSellerAncestorAdded
        // TODO add your handling code here:
        List<User> list = sellerController.getAll();
        jComboBoxReportSalesOrderSeller.removeAllItems();
        for (User item : list) {
            jComboBoxReportSalesOrderSeller.addItem(item);
        }
    }//GEN-LAST:event_jComboBoxReportSalesOrderSellerAncestorAdded

    private void jComboBoxReportSalesOrderProductAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jComboBoxReportSalesOrderProductAncestorAdded
        // TODO add your handling code here:
        List<Product> list = productController.listAll();
        jComboBoxReportSalesOrderProduct.removeAllItems();
        for (Product item : list) {
            jComboBoxReportSalesOrderProduct.addItem(item);
        }
    }//GEN-LAST:event_jComboBoxReportSalesOrderProductAncestorAdded

    private void jTextFieldReportSalesOrderProductNameSearch1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldReportSalesOrderProductNameSearch1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldReportSalesOrderProductNameSearch1KeyReleased

    private void jTextFieldReportSalesOrderProductNameSearch2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldReportSalesOrderProductNameSearch2KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldReportSalesOrderProductNameSearch2KeyReleased

    private void jComboBoxReportSalesOrderProduct1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jComboBoxReportSalesOrderProduct1AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxReportSalesOrderProduct1AncestorAdded

    private void jComboBoxReportSalesOrderProduct2AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jComboBoxReportSalesOrderProduct2AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxReportSalesOrderProduct2AncestorAdded

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:

//        List<ProductOrder> listProd = new ArrayList<>();
        List<List<String>> data = new ArrayList<>();
        data.add(List.of("ID", "Data", "Descricao", "Produto", "QTD", "Preço", "Imposto", "Total"));
        for (int i = 0; i < jTableReportSalesOrderProduct.getRowCount(); i++) {
            data.add(List.of(
                    jTableReportSalesOrderProduct.getValueAt(i, 0).toString(),
                    jTableReportSalesOrderProduct.getValueAt(i, 1).toString(),
                    jTableReportSalesOrderProduct.getValueAt(i, 2).toString(),
                    jTableReportSalesOrderProduct.getValueAt(i, 3).toString(),
                    jTableReportSalesOrderProduct.getValueAt(i, 4).toString(),
                    jTableReportSalesOrderProduct.getValueAt(i, 5).toString(),
                    jTableReportSalesOrderProduct.getValueAt(i, 6).toString(),
                    jTableReportSalesOrderProduct.getValueAt(i, 7).toString()
            ));
//            ProductOrder prod = new ProductOrder();
//            prod.setId((int) jTableReportSalesOrderProduct.getValueAt(i, 0));
//            prod.setDate((String) jTableReportSalesOrderProduct.getValueAt(i, 1));
////            prod.setDate((String) jTableReportSalesOrderProduct.getValueAt(i, 2));
//            prod.setDescription((String) jTableReportSalesOrderProduct.getValueAt(i, 3));
//            prod.setQty((int) jTableReportSalesOrderProduct.getValueAt(i, 4));
//            prod.setPrice((Double) jTableReportSalesOrderProduct.getValueAt(i, 5));
//            prod.setTaxeCode((String) jTableReportSalesOrderProduct.getValueAt(i, 6));
////            prod.setStockTotal((int) jTableReportSalesOrderProduct.getValueAt(i, 7));
//
//            listProd.add(prod);
        }

        reportController.salesProductsArrayListToExcell(data);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
//        List<ProductOrder> listProd = new ArrayList<>();

        List<List<String>> data = new ArrayList<>();
        data.add(List.of("ID", "Codigo", "Vendedor", "Produto", "QTD", "Preço", "Imposto", "Ttal"));
        for (int i = 0; i < jTableReportButSalesProduct.getRowCount(); i++) {
            data.add(List.of(
                    jTableReportButSalesProduct.getValueAt(i, 0).toString(),
                    jTableReportButSalesProduct.getValueAt(i, 1).toString(),
                    jTableReportButSalesProduct.getValueAt(i, 2).toString(),
                    jTableReportButSalesProduct.getValueAt(i, 3).toString(),
                    jTableReportButSalesProduct.getValueAt(i, 4).toString(),
                    jTableReportButSalesProduct.getValueAt(i, 5).toString(),
                    jTableReportButSalesProduct.getValueAt(i, 6).toString(),
                    jTableReportButSalesProduct.getValueAt(i, 7).toString()
            ));

//            ProductOrder prod = new ProductOrder();
//            prod.setId((int) jTableReportButSalesProduct.getValueAt(i, 0));
//            prod.setDate((String) jTableReportButSalesProduct.getValueAt(i, 1));
////            prod.setDate((String) jTableReportButSalesProduct.getValueAt(i, 2));
//            prod.setDescription((String) jTableReportButSalesProduct.getValueAt(i, 3));
//            prod.setQty((int) jTableReportButSalesProduct.getValueAt(i, 4));
//            prod.setPrice((Double) jTableReportButSalesProduct.getValueAt(i, 5));
//            prod.setTaxeCode((String) jTableReportButSalesProduct.getValueAt(i, 6));
////            prod.setStockTotal((int) jTableReportButSalesProduct.getValueAt(i, 7));
//
//            listProd.add(prod);
        }

        reportController.salesProductsSellerArrayListToExcell(data);
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
//        List<Order> listOrders = new ArrayList<>();

        List<List<String>> data = new ArrayList<>();
        data.add(List.of("ID", "Vendedor", "Fatura", "Data", "SubTotal", "Total"));
        for (int i = 0; i < jTableReportSalesOrderSeller.getRowCount(); i++) {
            data.add(List.of(
                    jTableReportSalesOrderSeller.getValueAt(i, 0).toString(),
                    jTableReportSalesOrderSeller.getValueAt(i, 1).toString(),
                    jTableReportSalesOrderSeller.getValueAt(i, 2).toString(),
                    jTableReportSalesOrderSeller.getValueAt(i, 3).toString(),
                    jTableReportSalesOrderSeller.getValueAt(i, 4).toString(),
                    jTableReportSalesOrderSeller.getValueAt(i, 5).toString()
            ));

//            Order order = new Order();
//            order.setId((int) jTableReportSalesOrderSeller.getValueAt(i, 0));
////            order.setSeller((User) jTableReportSalesOrderSeller.getValueAt(i, 1));
////            order.setSeller((User) jTableReportSalesOrderSeller.getValueAt(i, 1));
//            order.setPrefix((String) jTableReportButSalesProduct.getValueAt(i, 2));
//            order.setDatecreate((String) jTableReportSalesOrderSeller.getValueAt(i, 3));
//            order.setSubTotal((Double) jTableReportSalesOrderSeller.getValueAt(i, 4));
//            order.setTotal((Double) jTableReportSalesOrderSeller.getValueAt(i, 5));
//            listOrders.add(order);
        }
        reportController.salesOrderSellerArrayListToExcell(data);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButtonExportOrderAllExcellActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportOrderAllExcellActionPerformed
        // TODO add your handling code here:
        List<List<String>> data = new ArrayList<>();
        data.add(List.of("ID", "Vendedor", "Fatura", "Data", "SubTotal", "Total"));
        for (int i = 0; i < jTableReportSalesOrders.getRowCount(); i++) {
            data.add(List.of(
                    jTableReportSalesOrders.getValueAt(i, 0).toString(),
                    jTableReportSalesOrders.getValueAt(i, 1).toString(),
                    jTableReportSalesOrders.getValueAt(i, 2).toString(),
                    jTableReportSalesOrders.getValueAt(i, 3).toString(),
                    jTableReportSalesOrders.getValueAt(i, 4).toString(),
                    jTableReportSalesOrders.getValueAt(i, 5).toString()
            ));
        }
        reportController.salesOrderArrayListToExcell(data);
    }//GEN-LAST:event_jButtonExportOrderAllExcellActionPerformed

    private void jButtonExpSaftFactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExpSaftFactActionPerformed
        // TODO add your handling code here:
        new JDialogExportSaftFat(null, true).setVisible(true);
    }//GEN-LAST:event_jButtonExpSaftFactActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonExpSaftFact;
    private javax.swing.JButton jButtonExportOrderAllExcell;
    private javax.swing.JComboBox jComboBoxReportSalesOrderProduct;
    private javax.swing.JComboBox jComboBoxReportSalesOrderProduct1;
    private javax.swing.JComboBox jComboBoxReportSalesOrderProduct2;
    private javax.swing.JComboBox jComboBoxReportSalesOrderSeller;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JFormattedTextField jFormattedTextField4;
    private javax.swing.JFormattedTextField jFormattedTextField5;
    private javax.swing.JFormattedTextField jFormattedTextField6;
    private javax.swing.JFormattedTextField jFormattedTextField7;
    private javax.swing.JFormattedTextField jFormattedTextField8;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTableReportButSalesProduct;
    private javax.swing.JTable jTableReportSalesOrderProduct;
    private javax.swing.JTable jTableReportSalesOrderSeller;
    private javax.swing.JTable jTableReportSalesOrders;
    private javax.swing.JTextField jTextFieldReportSalesOrderProductNameSearch1;
    private javax.swing.JTextField jTextFieldReportSalesOrderProductNameSearch2;
    // End of variables declaration//GEN-END:variables
}
