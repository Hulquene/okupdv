/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.okutonda.okudpdv;

import com.formdev.flatlaf.FlatLightLaf;
import com.okutonda.okudpdv.controllers.AdminRoot;
import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import com.okutonda.okudpdv.jdbc.DatabaseBootProbe;
import static com.okutonda.okudpdv.jdbc.DatabaseBootProbe.AppState.DB_NOT_FOUND;
import static com.okutonda.okudpdv.jdbc.DatabaseBootProbe.AppState.DB_UNREACHABLE;
import static com.okutonda.okudpdv.jdbc.DatabaseBootProbe.AppState.INSTALL_INCOMPLETE;
import static com.okutonda.okudpdv.jdbc.DatabaseBootProbe.AppState.READY;
import static com.okutonda.okudpdv.jdbc.DatabaseBootProbe.AppState.SCHEMA_MISSING;
import com.okutonda.okudpdv.views.install.ScreenInstall;
import com.okutonda.okudpdv.views.login.ScreenLogin;
import java.awt.Toolkit;
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
// 1) Look & Feel (FlatLaf)
        try {
            // Forma 1 (recomendada pelo FlatLaf):
            FlatLightLaf.setup();

//            Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
//                if (!(event instanceof java.awt.event.WindowEvent we)) {
//                    return;
//                }
//                if (we.getID() == java.awt.event.WindowEvent.WINDOW_OPENED) {
//                    java.awt.Window w = we.getWindow();
//                    if (w instanceof java.awt.Container root) {
//                        com.okutonda.okudpdv.ui.TemaCleaner.clearBuilderOverrides(root);
//                        SwingUtilities.updateComponentTreeUI(w);
//                    }
//                }
//            }, java.awt.AWTEvent.WINDOW_EVENT_MASK);

            // Forma 2 (equivalente; usa UMA das duas):
//             UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Falhou inicializar o Look & Feel FlatLaf: " + ex.getMessage());
        }

        // 2) Aplicar tema global (UI defaults) — DEPOIS do LaF
        try {
            com.okutonda.okudpdv.ui.TemaLookAndFeel.aplicarUIManagerBasico();
        } catch (Exception ex) {
            System.err.println("Falhou aplicar TemaLookAndFeel: " + ex.getMessage());
        }

        // 3) Fluxo de inicialização da app (Swing thread)
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
                }
                case DB_NOT_FOUND, SCHEMA_MISSING, INSTALL_INCOMPLETE -> {
                    new ScreenInstall().setVisible(true);
                }
                case READY -> {
                    var conn = ConnectionDatabase.getConnect();
                    if (conn == null) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Falha inesperada ao ligar após verificação. Tenta novamente.",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE
                        );
                        System.exit(1);
                        return;
                    }
//                    new ScreenLogin().setVisible(true);
                    new ScreenLogin().setVisible(true);
                }
            }
        });

//        // (look & feel etc.)
//
//        // Configura o Look and Feel para o estilo do sistema operacional
//        try {
//            UIManager.setLookAndFeel(new FlatLightLaf());
//        } catch (UnsupportedLookAndFeelException ex) {
//            System.err.println("Failed to initialize LaF");
//        }
//        SwingUtilities.invokeLater(() -> {
//            DatabaseBootProbe.AppState state = DatabaseBootProbe.detect();
//            System.out.println("DEBUG → variável state: " + state);
//            switch (state) {
//                case DB_UNREACHABLE -> {
//                    JOptionPane.showMessageDialog(
//                            null,
//                            """
//                Não foi possível comunicar com o servidor de base de dados.
//
//                Verifica:
//                • Se o serviço MySQL/MariaDB está ATIVO
//                • Host/porta corretos (ex.: localhost:3306 ou a porta publicada no Docker)
//                • Firewall/antivírus
//
//                Depois abre novamente a aplicação.
//                """,
//                            "BD inativa",
//                            JOptionPane.ERROR_MESSAGE
//                    );
//                    new ScreenLogin().setVisible(true);
////                    System.exit(1); // encerra a aplicação
//                }
//                case DB_NOT_FOUND, SCHEMA_MISSING, INSTALL_INCOMPLETE -> {
//                    // Servidor responde → podes criar BD/tabelas/seed via instalador
//                    new ScreenInstall().setVisible(true);
//                }
//                case READY -> {
//                    // Tudo ok → segue o fluxo normal
//                    Connection conn = ConnectionDatabase.getConnect();
//                    if (conn == null) {
//                        // Proteção extra (não devia acontecer após READY)
//                        JOptionPane.showMessageDialog(
//                                null,
//                                "Falha inesperada ao ligar após verificação. Tenta novamente.",
//                                "Erro",
//                                JOptionPane.ERROR_MESSAGE
//                        );
//                        System.exit(1);
//                        return;
//                    }
////                    AdminRoot root = new AdminRoot();
////                    if ("1".equals(root.getStatusSoftware())) {
//                    new ScreenLogin().setVisible(true);
////                    } else {
////                        JOptionPane.showMessageDialog(
////                                null,
////                                "O sistema está inativo",
////                                "Atenção",
////                                JOptionPane.ERROR_MESSAGE
////                        );
////                        new ScreenAbout().setVisible(true);
//////                        new ScreenInstall().setVisible(true);
////                    }
//                }
//            }
//        });
    }
}
