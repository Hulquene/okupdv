/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.okutonda.okudpdv.views.export;

import com.okutonda.okudpdv.controllers.SaftController;
import com.okutonda.okudpdv.data.entities.SaftFat;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author hr
 */
public class JDialogExportSaftFat extends javax.swing.JDialog {

    SaftController exportSaftFat = new SaftController();
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;

    /**
     * Creates new form JDialogExportSaftFat
     */
    public JDialogExportSaftFat(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initializeDates();
        initializeTable();
        initializeFilter();
        listExpotSaftFat();
    }

    /**
     * Inicializa as datas com valores padrão
     */
    private void initializeDates() {
        try {
            // Data inicial: primeiro dia do mês atual
            LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
            // Data final: último dia do mês atual
            LocalDate lastDayOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

            DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            jFormattedTextFieldDateStart.setText(firstDayOfMonth.format(displayFormat));
            jFormattedTextFieldDateEnd.setText(lastDayOfMonth.format(displayFormat));

        } catch (Exception e) {
            System.err.println("Erro ao inicializar datas: " + e.getMessage());
            // Valores fallback
            jFormattedTextFieldDateStart.setText("01/" + String.format("%02d", LocalDate.now().getMonthValue()) + "/" + LocalDate.now().getYear());
            jFormattedTextFieldDateEnd.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
    }

    /**
     * Inicializa a tabela com configurações
     */
    private void initializeTable() {
        // Define o modelo da tabela
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Período", "Ficheiro", "Exportado por", "Criado em", "Estado", "Notas"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna todas as células não editáveis
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return Long.class;    // ID
                    case 1:
                        return String.class;  // Período
                    case 2:
                        return String.class;  // Ficheiro
                    case 3:
                        return String.class;  // Exportado por
                    case 4:
                        return String.class;  // Criado em
                    case 5:
                        return String.class;  // Estado
                    case 6:
                        return String.class;  // Notas
                    default:
                        return Object.class;
                }
            }
        };

        jTableExporSaftFat.setModel(tableModel);

        // Configura o row sorter para filtro
        rowSorter = new TableRowSorter<>(tableModel);
        jTableExporSaftFat.setRowSorter(rowSorter);

        // Ajusta a largura das colunas
        jTableExporSaftFat.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        jTableExporSaftFat.getColumnModel().getColumn(1).setPreferredWidth(120); // Período
        jTableExporSaftFat.getColumnModel().getColumn(2).setPreferredWidth(150); // Ficheiro
        jTableExporSaftFat.getColumnModel().getColumn(3).setPreferredWidth(100); // Exportado por
        jTableExporSaftFat.getColumnModel().getColumn(4).setPreferredWidth(120); // Criado em
        jTableExporSaftFat.getColumnModel().getColumn(5).setPreferredWidth(80);  // Estado
        jTableExporSaftFat.getColumnModel().getColumn(6).setPreferredWidth(200); // Notas
    }

    /**
     * Inicializa o sistema de filtro
     */
    private void initializeFilter() {
        jTextField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                applyFilter(jTextField1.getText().trim());
            }
        });
    }

    /**
     * Aplica filtro na tabela
     */
    private void applyFilter(String filterText) {
        if (filterText == null || filterText.trim().isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            try {
                // Filtra em múltiplas colunas
                rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + filterText, 1, 2, 3, 5, 6));
            } catch (Exception e) {
                // Em caso de regex inválido, usa filtro simples
                rowSorter.setRowFilter(null);
            }
        }
    }

    public void listExpotSaftFat() {
        List<SaftFat> list = exportSaftFat.get();
        loadListExpotSaftFat(list);
    }

    public void filterListExpotSaftFat(String txt) {
        List<SaftFat> list = exportSaftFat.filter(txt);
        loadListExpotSaftFat(list);
    }

    public void loadListExpotSaftFat(List<SaftFat> list) {
        // Limpa a tabela
        tableModel.setRowCount(0);

        // Formato para datas
        DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter datetimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (SaftFat export : list) {
            String period = export.getPeriodStart().format(displayFormat) + " a "
                    + export.getPeriodEnd().format(displayFormat);

            // Extrai apenas o nome do arquivo do caminho completo
            String fileName = extractFileName(export.getFilePath());

            String user = export.getUser() != null ? export.getUser().getName() : "Sistema";
            String created = export.getCreatedAt() != null
                    ? export.getCreatedAt().format(datetimeFormat) : "";
            String status = export.getStatus() != null ? export.getStatus() : "";
            String notes = export.getNotes() != null ? export.getNotes() : "";

            tableModel.addRow(new Object[]{
                export.getId(),
                period,
                fileName,
                user,
                created,
                status,
                notes
            });
        }

        // Aplica o filtro atual se houver texto no campo de pesquisa
        if (jTextField1.getText() != null && !jTextField1.getText().trim().isEmpty()) {
            applyFilter(jTextField1.getText().trim());
        }

        // Atualiza o status com a contagem
        updateStatusLabel(list.size());
    }

    /**
     * Extrai apenas o nome do arquivo do caminho completo
     */
    private String extractFileName(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return "N/A";
        }
        try {
            java.nio.file.Path path = java.nio.file.Paths.get(filePath);
            return path.getFileName().toString();
        } catch (Exception e) {
            // Fallback: pega a última parte após a última barra
            int lastSeparator = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
            return lastSeparator >= 0 ? filePath.substring(lastSeparator + 1) : filePath;
        }
    }

    /**
     * Atualiza o label de status com a contagem de registros
     */
    private void updateStatusLabel(int count) {
        jLabel1.setText("Total de registros: " + count + " | Formato data: dd/MM/yyyy");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableExporSaftFat = new javax.swing.JTable();
        jFormattedTextFieldDateStart = new javax.swing.JFormattedTextField();
        jFormattedTextFieldDateEnd = new javax.swing.JFormattedTextField();
        jButtonGerarFile = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButtonApagarFile = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jTableExporSaftFat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Data de inicio", "data de fim", "Status", "Data de Criação"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableExporSaftFat);

        try {
            jFormattedTextFieldDateStart.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/20##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextFieldDateStart.setToolTipText("Data de inicio do ficheiro");
        jFormattedTextFieldDateStart.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jFormattedTextFieldDateStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextFieldDateStartActionPerformed(evt);
            }
        });

        try {
            jFormattedTextFieldDateEnd.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/20##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextFieldDateEnd.setToolTipText("Data de fim do ficheiro");
        jFormattedTextFieldDateEnd.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jButtonGerarFile.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonGerarFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Document.png"))); // NOI18N
        jButtonGerarFile.setText("Gerar");
        jButtonGerarFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGerarFileActionPerformed(evt);
            }
        });

        jTextField1.setToolTipText("Pesquisar");

        jButtonApagarFile.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonApagarFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Trash Can.png"))); // NOI18N
        jButtonApagarFile.setText("Apagar");
        jButtonApagarFile.setBorderPainted(false);
        jButtonApagarFile.setContentAreaFilled(false);
        jButtonApagarFile.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonApagarFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonApagarFileActionPerformed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("As datas devem estar nesse formato dd/MM/yyyy");

        jLabel2.setText("Data Inicial");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel3.setText("Gerar Novo arquivo Fiscal");

        jLabel4.setText("Data Final");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Pesquisar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(154, 154, 154)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jFormattedTextFieldDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jFormattedTextFieldDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButtonGerarFile))))
                            .addComponent(jLabel3)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(123, 123, 123)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonApagarFile, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 634, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextFieldDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextFieldDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonGerarFile, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonApagarFile, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jTabbedPane1.addTab("SAF-T Faturação", jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 646, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 445, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Preferencias", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 646, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(674, 500));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonGerarFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGerarFileActionPerformed
        // TODO add your handling code here:
        try {
            // Valida as datas
            if (jFormattedTextFieldDateStart.getText() == null || jFormattedTextFieldDateStart.getText().trim().isEmpty()
                    || jFormattedTextFieldDateEnd.getText() == null || jFormattedTextFieldDateEnd.getText().trim().isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Por favor, preencha ambas as datas.");
                return;
            }

            var fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            var start = java.time.LocalDate.parse(jFormattedTextFieldDateStart.getText().trim(), fmt);
            var end = java.time.LocalDate.parse(jFormattedTextFieldDateEnd.getText().trim(), fmt);

            if (end.isBefore(start)) {
                javax.swing.JOptionPane.showMessageDialog(this, "Data final não pode ser anterior à inicial.");
                return;
            }

            String defaultName = String.format("SAFT_AO_%s_%s.xml",
                    start.format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE),
                    end.format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE));

            javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
            fc.setSelectedFile(new java.io.File(defaultName));
            fc.setDialogTitle("Salvar arquivo SAF-T");

            int result = fc.showSaveDialog(this);
            if (result != javax.swing.JFileChooser.APPROVE_OPTION) {
                return;
            }

            java.nio.file.Path output = fc.getSelectedFile().toPath();

            // Adiciona extensão .xml se não tiver
            if (!output.toString().toLowerCase().endsWith(".xml")) {
                output = java.nio.file.Paths.get(output.toString() + ".xml");
            }

            // chama o controller
            long exportId = new SaftController().export(start, end, output);

            javax.swing.JOptionPane.showMessageDialog(this,
                    "SAF-T gerado com sucesso!\nID export: " + exportId + "\nFicheiro: " + output.getFileName());
            listExpotSaftFat();
        } catch (java.time.format.DateTimeParseException ex) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Formato de data inválido. Use o formato dd/MM/yyyy.\nExemplo: 01/01/2024");
        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }//GEN-LAST:event_jButtonGerarFileActionPerformed

    private void jFormattedTextFieldDateStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextFieldDateStartActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextFieldDateStartActionPerformed

    private void jButtonApagarFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonApagarFileActionPerformed
        // TODO add your handling code here:
        // Implementação do botão apagar
        int selectedRow = jTableExporSaftFat.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Por favor, selecione um registro para apagar.");
            return;
        }

        // Converte para modelo real (considerando o filtro)
        int modelRow = jTableExporSaftFat.convertRowIndexToModel(selectedRow);
        Long exportId = (Long) tableModel.getValueAt(modelRow, 0);

        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja apagar este registro?\nID: " + exportId,
                "Confirmar exclusão",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            try {
                exportSaftFat.delete(exportId);
                javax.swing.JOptionPane.showMessageDialog(this, "Registro apagado com sucesso!");
                listExpotSaftFat();
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Erro ao apagar registro: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_jButtonApagarFileActionPerformed

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
            java.util.logging.Logger.getLogger(JDialogExportSaftFat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogExportSaftFat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogExportSaftFat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogExportSaftFat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogExportSaftFat dialog = new JDialogExportSaftFat(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonApagarFile;
    private javax.swing.JButton jButtonGerarFile;
    private javax.swing.JFormattedTextField jFormattedTextFieldDateEnd;
    private javax.swing.JFormattedTextField jFormattedTextFieldDateStart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableExporSaftFat;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
