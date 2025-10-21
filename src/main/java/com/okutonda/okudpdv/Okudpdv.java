/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.okutonda.okudpdv;

import com.formdev.flatlaf.FlatLightLaf;
//import com.okutonda.okudpdv.controllers.AdminRoot;
//import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
//import com.okutonda.okudpdv.jdbc.DatabaseBootProbe;
//import static com.okutonda.okudpdv.jdbc.DatabaseBootProbe.AppState.DB_NOT_FOUND;
//import static com.okutonda.okudpdv.jdbc.DatabaseBootProbe.AppState.DB_UNREACHABLE;
//import static com.okutonda.okudpdv.jdbc.DatabaseBootProbe.AppState.INSTALL_INCOMPLETE;
//import static com.okutonda.okudpdv.jdbc.DatabaseBootProbe.AppState.READY;
//import static com.okutonda.okudpdv.jdbc.DatabaseBootProbe.AppState.SCHEMA_MISSING;
import com.okutonda.okudpdv.views.install.ScreenInstall;
import com.okutonda.okudpdv.views.login.ScreenLogin;
//import java.awt.Toolkit;
//import java.sql.Connection;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;

//import com.okutonda.okudpdv.data.connection.DatabaseBootProbe;
//import com.okutonda.okudpdv.data.connection.DatabaseBootProbe.AppState;
import com.okutonda.okudpdv.data.connection.DatabaseBootProbe;
//import com.okutonda.okudpdv.data.connection.DatabaseBootProbe.AppState;
import com.okutonda.okudpdv.data.connection.DatabaseProvider;
//import com.okutonda.okudpdv.ui.ScreenInstall;
//import com.okutonda.okudpdv.ui.ScreenLogin;
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

        } catch (Exception ex) {
            System.err.println("Falhou inicializar o Look & Feel FlatLaf: " + ex.getMessage());
        }

        // 2) Aplicar tema global (UI defaults) — DEPOIS do LaF
        try {
            com.okutonda.okudpdv.ui.TemaLookAndFeel.aplicarUIManagerBasico();
        } catch (Exception ex) {
            System.err.println("Falhou aplicar TemaLookAndFeel: " + ex.getMessage());
        }

        // Garante que a UI Swing inicia na thread de interface
        SwingUtilities.invokeLater(() -> {
            try {
                // 🔍 Diagnóstico inicial do banco
                DatabaseBootProbe.AppState state = DatabaseBootProbe.detect();
                System.out.println("DEBUG → Estado do BD: " + state);

                switch (state) {
                    case DB_UNREACHABLE -> {
                        JOptionPane.showMessageDialog(
                                null,
                                """
                                ❌ Não foi possível comunicar com o servidor de base de dados.

                                Verifica:
                                • Se o serviço MySQL/MariaDB está ATIVO
                                • Host/porta corretos (ex.: localhost:3306 ou porta publicada no Docker)
                                • Firewall/antivírus

                                Depois abre novamente a aplicação.
                                """,
                                "Base de dados inativa",
                                JOptionPane.ERROR_MESSAGE
                        );
                        // Mostra tela de login mesmo assim, se quiser que continue offline
                        new ScreenLogin().setVisible(true);
                    }

                    case DB_NOT_FOUND, SCHEMA_MISSING, INSTALL_INCOMPLETE -> {
                        JOptionPane.showMessageDialog(
                                null,
                                """
                                ⚙️ O sistema precisa ser instalado ou atualizado.

                                A base de dados foi detectada, mas parece incompleta.
                                """,
                                "Instalação necessária",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        new ScreenInstall().setVisible(true);
                    }

                    case READY -> {
                        // Conexão via pool de conexões (HikariCP)
                        try (var conn = DatabaseProvider.getConnection()) {
                            if (conn == null || conn.isClosed()) {
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Falha inesperada ao conectar após verificação. Tenta novamente.",
                                        "Erro",
                                        JOptionPane.ERROR_MESSAGE
                                );
                                System.exit(1);
                                return;
                            }
                            System.out.println("[DB] Conexão validada com sucesso!");
                            new ScreenLogin().setVisible(true);
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Erro ao conectar ao banco: " + e.getMessage(),
                                    "Erro de conexão",
                                    JOptionPane.ERROR_MESSAGE
                            );
                            e.printStackTrace();
                            System.exit(1);
                        }
                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Erro inesperado ao iniciar o sistema: " + e.getMessage(),
                        "Erro Fatal",
                        JOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
                System.exit(1);
            }
        });

        // 3) Fluxo de inicialização da app (Swing thread)
//        SwingUtilities.invokeLater(() -> {
////             AppState state = DatabaseBootProbe.detect();
//            DatabaseBootProbe.AppState state = DatabaseBootProbe.detect();
//            System.out.println("DEBUG → variável state: " + state);
//
//            switch (state) {
//                case DB_UNREACHABLE -> {
//                    JOptionPane.showMessageDialog(
//                            null,
//                            """
//                        Não foi possível comunicar com o servidor de base de dados.
//
//                        Verifica:
//                        • Se o serviço MySQL/MariaDB está ATIVO
//                        • Host/porta corretos (ex.: localhost:3306 ou a porta publicada no Docker)
//                        • Firewall/antivírus
//
//                        Depois abre novamente a aplicação.
//                        """,
//                            "BD inativa",
//                            JOptionPane.ERROR_MESSAGE
//                    );
//                    new ScreenLogin().setVisible(true);
//                }
//                case DB_NOT_FOUND, SCHEMA_MISSING, INSTALL_INCOMPLETE -> {
//                    new ScreenInstall().setVisible(true);
//                }
//                case READY -> {
//                    var conn = ConnectionDatabase.getConnect();
//                    if (conn == null) {
//                        JOptionPane.showMessageDialog(
//                                null,
//                                "Falha inesperada ao ligar após verificação. Tenta novamente.",
//                                "Erro",
//                                JOptionPane.ERROR_MESSAGE
//                        );
//                        System.exit(1);
//                        return;
//                    }
////                    new ScreenLogin().setVisible(true);
//                    new ScreenLogin().setVisible(true);
//                }
//            }
//        });
    }
}
