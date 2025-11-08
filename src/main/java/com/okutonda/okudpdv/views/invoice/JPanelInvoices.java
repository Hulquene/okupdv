/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.okutonda.okudpdv.views.invoice;

import com.okutonda.okudpdv.controllers.InvoiceController;
import com.okutonda.okudpdv.data.entities.Invoices;
import com.okutonda.okudpdv.helpers.PrintHelper;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author hr
 */
public class JPanelInvoices extends javax.swing.JPanel {

    private final InvoiceController invoiceController;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;

    /**
     * Creates new form JPanelInvoices
     */
    public JPanelInvoices() {
        initComponents();
        this.invoiceController = new InvoiceController();
        inicializarTabela();
        carregarDadosIniciais();
        configurarFiltros();
    }

    /**
     * Inicializa o modelo da tabela
     */
    private void inicializarTabela() {
        // Definir modelo da tabela
        String[] colunas = {
            "ID", "Número", "Cliente", "Data", "Total", "Status", "Tipo", "Vendedor"
        };

        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabela não editável
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return Integer.class; // ID
                    case 1:
                        return String.class;  // Número
                    case 2:
                        return String.class;  // Cliente
                    case 3:
                        return String.class;  // Data
                    case 4:
                        return Double.class;  // Total
                    case 5:
                        return String.class;  // Status
                    case 6:
                        return String.class;  // Tipo
                    case 7:
                        return String.class;  // Vendedor
                    default:
                        return Object.class;
                }
            }
        };

        jTableInvoices.setModel(tableModel);

        // Configurar sorter para filtros
        rowSorter = new TableRowSorter<>(tableModel);
        jTableInvoices.setRowSorter(rowSorter);

        // Configurar largura das colunas
        jTableInvoices.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        jTableInvoices.getColumnModel().getColumn(1).setPreferredWidth(100); // Número
        jTableInvoices.getColumnModel().getColumn(2).setPreferredWidth(200); // Cliente
        jTableInvoices.getColumnModel().getColumn(3).setPreferredWidth(100); // Data
        jTableInvoices.getColumnModel().getColumn(4).setPreferredWidth(100); // Total
        jTableInvoices.getColumnModel().getColumn(5).setPreferredWidth(100); // Status
        jTableInvoices.getColumnModel().getColumn(6).setPreferredWidth(80);  // Tipo
        jTableInvoices.getColumnModel().getColumn(7).setPreferredWidth(150); // Vendedor
    }

    /**
     * Carrega dados iniciais na tabela
     */
    private void carregarDadosIniciais() {
        limparTabela();

        try {
            List<Invoices> invoices = invoiceController.listarTodas();

            for (Invoices invoice : invoices) {
                adicionarLinhaTabela(invoice);
            }

            atualizarStatusCarregamento("Carregadas " + invoices.size() + " faturas");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar faturas: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adiciona uma linha na tabela
     */
    private void adicionarLinhaTabela(Invoices invoice) {
        String status = getStatusDescricao(invoice.getStatus());
        String tipo = invoice.getPrefix();
        String vendedor = invoice.getSeller() != null ? invoice.getSeller().getName() : "N/A";
        String cliente = invoice.getClient() != null ? invoice.getClient().getName() : "N/A";
        String numeroCompleto = invoice.getPrefix() + "/" + invoice.getNumber();

        tableModel.addRow(new Object[]{
            invoice.getId(),
            numeroCompleto,
            cliente,
            invoice.getIssueDate(),
            invoice.getTotal().doubleValue(),
            status,
            tipo,
            vendedor
        });
    }

    /**
     * Limpa a tabela
     */
    private void limparTabela() {
        tableModel.setRowCount(0);
    }

    /**
     * Configura filtros de data
     */
    private void configurarFiltros() {
        // Definir datas padrão (últimos 30 dias)
        LocalDate dataFim = LocalDate.now();
        LocalDate dataInicio = dataFim.minusDays(30);

        jFormattedTextFieldDateStartFT.setText(formatarData(dataInicio));
        jFormattedTextFieldDateFinishFT.setText(formatarData(dataFim));

        // Adicionar listener para Enter nos campos de data
        jFormattedTextFieldDateStartFT.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    filtrarPorData();
                }
            }
        });

        jFormattedTextFieldDateFinishFT.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    filtrarPorData();
                }
            }
        });
    }

    /**
     * Filtra as faturas por data
     */
    private void filtrarPorData() {
        try {
            LocalDate dataInicio = parseData(jFormattedTextFieldDateStartFT.getText());
            LocalDate dataFim = parseData(jFormattedTextFieldDateFinishFT.getText());

            if (dataInicio == null || dataFim == null) {
                JOptionPane.showMessageDialog(this,
                        "Datas inválidas. Use o formato DD/MM/AAAA",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (dataInicio.isAfter(dataFim)) {
                JOptionPane.showMessageDialog(this,
                        "Data inicial não pode ser maior que data final",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Invoices> invoices = invoiceController.listarPorPeriodo(dataInicio, dataFim);
            atualizarTabela(invoices);
            atualizarStatusCarregamento("Filtradas " + invoices.size() + " faturas por período");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao filtrar por data: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Filtra a tabela por texto
     */
    private void filtrarPorTexto(String texto) {
        if (texto.trim().isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            try {
                RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("(?i)" + texto, 1, 2, 5, 6, 7);
                rowSorter.setRowFilter(rf);
            } catch (Exception e) {
                // Ignora erros de regex inválido
            }
        }
    }

    /**
     * Atualiza a tabela com uma lista de faturas
     */
    private void atualizarTabela(List<Invoices> invoices) {
        limparTabela();
        for (Invoices invoice : invoices) {
            adicionarLinhaTabela(invoice);
        }
    }

    /**
     * Obtém a fatura selecionada na tabela
     */
    private Invoices getFaturaSelecionada() {
        int selectedRow = jTableInvoices.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma fatura da tabela",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }

        int modelRow = jTableInvoices.convertRowIndexToModel(selectedRow);
        Integer faturaId = (Integer) tableModel.getValueAt(modelRow, 0);

        return invoiceController.buscarPorId(faturaId);
    }

    /**
     * Imprime a fatura selecionada
     */
    private void imprimirFatura() {
        Invoices fatura = getFaturaSelecionada();
        if (fatura == null) {
            return;
        }

        try {
            boolean sucesso = PrintHelper.printInvoiceWithDialog(fatura);
            if (sucesso) {
                JOptionPane.showMessageDialog(this,
                        "Fatura impressa com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao imprimir fatura: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gera nota de crédito para a fatura selecionada
     */
    private void gerarNotaCredito() {
        Invoices fatura = getFaturaSelecionada();
        if (fatura == null) {
            return;
        }

        // TODO: Implementar lógica de geração de nota de crédito
        JOptionPane.showMessageDialog(this,
                "Funcionalidade de nota de crédito em desenvolvimento",
                "Em Desenvolvimento",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Visualiza detalhes da fatura selecionada
     */
    private void visualizarFatura() {
        Invoices fatura = getFaturaSelecionada();
        if (fatura == null) {
            return;
        }

        // TODO: Implementar visualização de detalhes
        JOptionPane.showMessageDialog(this,
                "Visualizando fatura: " + fatura.getPrefix() + "/" + fatura.getNumber()
                + "\nCliente: " + (fatura.getClient() != null ? fatura.getClient().getName() : "N/A")
                + "\nTotal: " + fatura.getTotal()
                + "\nStatus: " + getStatusDescricao(fatura.getStatus()),
                "Detalhes da Fatura",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Atualiza o status de carregamento
     */
    private void atualizarStatusCarregamento(String mensagem) {
        // Pode ser implementado com uma status bar se necessário
        System.out.println("ℹ️ " + mensagem);
    }

    // ==========================================================
    // 🔹 MÉTODOS UTILITÁRIOS
    // ==========================================================
    private String getStatusDescricao(Integer status) {
        if (status == null) {
            return "Desconhecido";
        }
        switch (status) {
            case 1:
                return "Pendente";
            case 2:
                return "Emitida";
            case 3:
                return "Paga";
            case 4:
                return "Anulada";
            default:
                return "Desconhecido";
        }
    }

    private String formatarData(LocalDate data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return data.format(formatter);
    }

    private LocalDate parseData(String textoData) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(textoData, formatter);
        } catch (Exception e) {
            return null;
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

        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableInvoices = new javax.swing.JTable();
        jFormattedTextFieldDateStartFT = new javax.swing.JFormattedTextField();
        jFormattedTextFieldDateFinishFT = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButtonFilterOrderFT = new javax.swing.JButton();
        jButtonPrintFT = new javax.swing.JButton();
        jButtonGenerateNoteCreditFT = new javax.swing.JButton();
        jButtonViewOrder2 = new javax.swing.JButton();
        jButtonNewInvoice = new javax.swing.JButton();
        jTextFieldFiltText = new javax.swing.JTextField();

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jTableInvoices.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(jTableInvoices);

        try {
            jFormattedTextFieldDateStartFT.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/20##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextFieldDateStartFT.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        try {
            jFormattedTextFieldDateFinishFT.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/20##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextFieldDateFinishFT.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Data Inicio:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Data Fim:");

        jButtonFilterOrderFT.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonFilterOrderFT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Search.png"))); // NOI18N
        jButtonFilterOrderFT.setText("Filtrar");
        jButtonFilterOrderFT.setBorderPainted(false);
        jButtonFilterOrderFT.setContentAreaFilled(false);
        jButtonFilterOrderFT.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonFilterOrderFT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFilterOrderFTActionPerformed(evt);
            }
        });

        jButtonPrintFT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/printer_8139457.png"))); // NOI18N
        jButtonPrintFT.setText("Imprimir");
        jButtonPrintFT.setContentAreaFilled(false);
        jButtonPrintFT.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonPrintFT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrintFTActionPerformed(evt);
            }
        });

        jButtonGenerateNoteCreditFT.setBackground(new java.awt.Color(153, 255, 255));
        jButtonGenerateNoteCreditFT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Document.png"))); // NOI18N
        jButtonGenerateNoteCreditFT.setText("Gerar Nota de Credito");
        jButtonGenerateNoteCreditFT.setContentAreaFilled(false);
        jButtonGenerateNoteCreditFT.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonGenerateNoteCreditFT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenerateNoteCreditFTActionPerformed(evt);
            }
        });

        jButtonViewOrder2.setBackground(new java.awt.Color(255, 255, 102));
        jButtonViewOrder2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Binoculars.png"))); // NOI18N
        jButtonViewOrder2.setText("Ver");
        jButtonViewOrder2.setContentAreaFilled(false);
        jButtonViewOrder2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonViewOrder2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewOrder2ActionPerformed(evt);
            }
        });

        jButtonNewInvoice.setText("Novo");
        jButtonNewInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewInvoiceActionPerformed(evt);
            }
        });

        jTextFieldFiltText.setText("jTextField1");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextFieldDateStartFT, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jFormattedTextFieldDateFinishFT, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jTextFieldFiltText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                                .addComponent(jButtonFilterOrderFT, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonNewInvoice)
                                .addGap(59, 59, 59)
                                .addComponent(jButtonViewOrder2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonGenerateNoteCreditFT)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonPrintFT))))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextFieldDateStartFT, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextFieldDateFinishFT, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonFilterOrderFT, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonPrintFT, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonGenerateNoteCreditFT, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonViewOrder2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonNewInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldFiltText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1110, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 535, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 535, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonFilterOrderFTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFilterOrderFTActionPerformed
        // TODO add your handling code here:
        filtrarPorData();
    }//GEN-LAST:event_jButtonFilterOrderFTActionPerformed

    private void jButtonPrintFTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrintFTActionPerformed
        // TODO add your handling code here:
        imprimirFatura();
    }//GEN-LAST:event_jButtonPrintFTActionPerformed

    private void jButtonGenerateNoteCreditFTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenerateNoteCreditFTActionPerformed
        // TODO add your handling code here:
        gerarNotaCredito();
    }//GEN-LAST:event_jButtonGenerateNoteCreditFTActionPerformed

    private void jButtonViewOrder2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewOrder2ActionPerformed
        // TODO add your handling code here:
        visualizarFatura();
    }//GEN-LAST:event_jButtonViewOrder2ActionPerformed

    private void jButtonNewInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewInvoiceActionPerformed
        // TODO add your handling code here:
        JDialogFormInvoice formInvoice = new JDialogFormInvoice(null, true);
        formInvoice.setVisible(true);

        if (formInvoice.getResponse()) {
            JOptionPane.showMessageDialog(null, "Fatura realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarDadosIniciais(); // Recarrega a tabela após nova fatura
        }

    }//GEN-LAST:event_jButtonNewInvoiceActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonFilterOrderFT;
    public javax.swing.JButton jButtonGenerateNoteCreditFT;
    private javax.swing.JButton jButtonNewInvoice;
    private javax.swing.JButton jButtonPrintFT;
    private javax.swing.JButton jButtonViewOrder2;
    private javax.swing.JFormattedTextField jFormattedTextFieldDateFinishFT;
    private javax.swing.JFormattedTextField jFormattedTextFieldDateStartFT;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTableInvoices;
    private javax.swing.JTextField jTextFieldFiltText;
    // End of variables declaration//GEN-END:variables
}
