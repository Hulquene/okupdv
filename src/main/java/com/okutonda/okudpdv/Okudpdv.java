/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.okutonda.okudpdv;

import com.formdev.flatlaf.FlatLightLaf;
import com.okutonda.okudpdv.controllers.AdminRoot;
import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.jdbc.DatabaseBootProbe;
import com.okutonda.okudpdv.views.install.ScreenInstall;
import com.okutonda.okudpdv.views.login.ScreenLogin;
import java.sql.Connection;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author kenny
 */
public class Okudpdv {

    public static void main(String[] args) {

        System.out.println("Hello World!");
        // (look & feel etc.)

        // Configura o Look and Feel para o estilo do sistema operacional
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to initialize LaF");
        }
        SwingUtilities.invokeLater(() -> {
            DatabaseBootProbe.AppState state = DatabaseBootProbe.detect();
            System.out.println("DEBUG → variável state: " + state);
            switch (state) {
                case DB_UNREACHABLE -> {
                    JOptionPane.showMessageDialog(
                            null,
                            """
                Não foi possível comunicar com o servidor de base de dados.

                Verifica:
                • Se o serviço MySQL/MariaDB está ATIVO
                • Host/porta corretos (ex.: localhost:3306 ou a porta publicada no Docker)
                • Firewall/antivírus

                Depois abre novamente a aplicação.
                """,
                            "BD inativa",
                            JOptionPane.ERROR_MESSAGE
                    );
                    new ScreenLogin().setVisible(true);
//                    System.exit(1); // encerra a aplicação
                }
                case DB_NOT_FOUND, SCHEMA_MISSING, INSTALL_INCOMPLETE -> {
                    // Servidor responde → podes criar BD/tabelas/seed via instalador
                    new ScreenInstall().setVisible(true);
                }
                case READY -> {
                    // Tudo ok → segue o fluxo normal
                    Connection conn = ConnectionDatabase.getConnect();
                    if (conn == null) {
                        // Proteção extra (não devia acontecer após READY)
                        JOptionPane.showMessageDialog(
                                null,
                                "Falha inesperada ao ligar após verificação. Tenta novamente.",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE
                        );
                        System.exit(1);
                        return;
                    }
//                    AdminRoot root = new AdminRoot();
//                    if ("1".equals(root.getStatusSoftware())) {
                    new ScreenLogin().setVisible(true);
//                    } else {
//                        JOptionPane.showMessageDialog(
//                                null,
//                                "O sistema está inativo",
//                                "Atenção",
//                                JOptionPane.ERROR_MESSAGE
//                        );
//                        new ScreenAbout().setVisible(true);
////                        new ScreenInstall().setVisible(true);
//                    }
                }
            }
        });

//        System.out.println("database connection: " + conn);
//        // Executar o código Swing na thread de eventos do Swing
//        SwingUtilities.invokeLater(() -> {
//            if (conn != null) {
//                AdminRoot root = new AdminRoot();
//                if (root.getStatusSoftware().equals("1")) {
////                      // Cria e exibe a janela
//                    new ScreenLogin().setVisible(true);
////                      frame.jLabelStatusBdConect.setText(text);
////                      frame.setVisible(true);
//
//                } else {
//                    JOptionPane.showMessageDialog(null, "O sistema esta inativo", "Atenção", JOptionPane.ERROR_MESSAGE);
//                    new ScreenInstall().setVisible(true);
//                }
//            } else {
////                JOptionPane.showMessageDialog(null, "O sistema esta inativo", "Atenção", JOptionPane.ERROR_MESSAGE);
//                new ScreenInstall().setVisible(true);
//            }
//        });
    }
}
