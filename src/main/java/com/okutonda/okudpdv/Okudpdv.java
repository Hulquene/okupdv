/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.okutonda.okudpdv;

import com.formdev.flatlaf.FlatLightLaf;
import com.okutonda.okudpdv.data.config.HibernateConfig;
import com.okutonda.okudpdv.views.install.ScreenInstall;
import com.okutonda.okudpdv.views.login.ScreenLogin;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import com.okutonda.okudpdv.data.connection.DatabaseBootProbe;
import com.okutonda.okudpdv.data.connection.DatabaseProvider;
import com.okutonda.okudpdv.data.entities.Countries;

/**
 *
 * @author kenny
 */
public class Okudpdv {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Countries.loadCache();
        // 1) Look & Feel (FlatLaf)
        try {
            FlatLightLaf.setup();
        } catch (Exception ex) {
            System.err.println("Falhou inicializar o Look & Feel FlatLaf: " + ex.getMessage());
        }

        // 2) Aplicar tema global
        try {
            com.okutonda.okudpdv.ui.TemaLookAndFeel.aplicarUIManagerBasico();
        } catch (Exception ex) {
            System.err.println("Falhou aplicar TemaLookAndFeel: " + ex.getMessage());
        }

        // Garante que a UI Swing inicia na thread de interface
        SwingUtilities.invokeLater(() -> {
            try {
                // 🔍 Diagnóstico inicial do banco (SEU CÓDIGO ATUAL)
                DatabaseBootProbe.AppState state = DatabaseBootProbe.detect();
                System.out.println("DEBUG → Estado do BD: " + state);

                switch (state) {
                    case DB_UNREACHABLE -> {
                        JOptionPane.showMessageDialog(null, "❌ Não foi possível comunicar com o servidor de base de dados.", "Base de dados inativa", JOptionPane.ERROR_MESSAGE);
                        new ScreenLogin().setVisible(true);
                    }

                    case DB_NOT_FOUND, SCHEMA_MISSING, INSTALL_INCOMPLETE -> {
                        JOptionPane.showMessageDialog(null, "⚙️ O sistema precisa ser instalado ou atualizado.", "Instalação necessária", JOptionPane.INFORMATION_MESSAGE);
                        new ScreenInstall().setVisible(true);
                    }

                    case READY -> {
                        // 🔥 NOVA VERIFICAÇÃO: Testar Hibernate também
                        boolean hibernateOk = testHibernateConnection();

                        if (hibernateOk) {
                            System.out.println("✅ [Hibernate] Conexão validada com sucesso!");
                            new ScreenLogin().setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Hibernate não conseguiu conectar. Usando fallback para JDBC.",
                                    "Aviso", JOptionPane.WARNING_MESSAGE);
                            // Fallback para seu sistema atual
                            try (var conn = DatabaseProvider.getConnection()) {
                                new ScreenLogin().setVisible(true);
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Erro ao conectar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                                System.exit(1);
                            }
                        }
                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro inesperado: " + e.getMessage(), "Erro Fatal", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                System.exit(1);
            }
        });

        // Shutdown hook para fechar Hibernate adequadamente
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("🔄 Encerrando aplicação...");
            HibernateConfig.shutdown();
        }));
    }

    private static boolean testHibernateConnection() {
        try {
            return HibernateConfig.testConnection();
        } catch (Exception e) {
            System.err.println("❌ Hibernate connection test failed: " + e.getMessage());
            return false;
        }
    }
//    public static void main(String[] args) {
//
//        System.out.println("Hello World!");
//// 1) Look & Feel (FlatLaf)
//        try {
//            // Forma 1 (recomendada pelo FlatLaf):
//            FlatLightLaf.setup();
//
//        } catch (Exception ex) {
//            System.err.println("Falhou inicializar o Look & Feel FlatLaf: " + ex.getMessage());
//        }
//
//        // 2) Aplicar tema global (UI defaults) — DEPOIS do LaF
//        try {
//            com.okutonda.okudpdv.ui.TemaLookAndFeel.aplicarUIManagerBasico();
//        } catch (Exception ex) {
//            System.err.println("Falhou aplicar TemaLookAndFeel: " + ex.getMessage());
//        }
//
//        // Garante que a UI Swing inicia na thread de interface
//        SwingUtilities.invokeLater(() -> {
//            try {
//                // 🔍 Diagnóstico inicial do banco
//                DatabaseBootProbe.AppState state = DatabaseBootProbe.detect();
//                System.out.println("DEBUG → Estado do BD: " + state);
//
//                switch (state) {
//                    case DB_UNREACHABLE -> {
//                        JOptionPane.showMessageDialog(
//                                null,
//                                """
//                                ❌ Não foi possível comunicar com o servidor de base de dados.
//
//                                Verifica:
//                                • Se o serviço MySQL/MariaDB está ATIVO
//                                • Host/porta corretos (ex.: localhost:3306 ou porta publicada no Docker)
//                                • Firewall/antivírus
//
//                                Depois abre novamente a aplicação.
//                                """,
//                                "Base de dados inativa",
//                                JOptionPane.ERROR_MESSAGE
//                        );
//                        // Mostra tela de login mesmo assim, se quiser que continue offline
//                        new ScreenLogin().setVisible(true);
//                    }
//
//                    case DB_NOT_FOUND, SCHEMA_MISSING, INSTALL_INCOMPLETE -> {
//                        JOptionPane.showMessageDialog(
//                                null,
//                                """
//                                ⚙️ O sistema precisa ser instalado ou atualizado.
//
//                                A base de dados foi detectada, mas parece incompleta.
//                                """,
//                                "Instalação necessária",
//                                JOptionPane.INFORMATION_MESSAGE
//                        );
//                        new ScreenInstall().setVisible(true);
//                    }
//
//                    case READY -> {
//                        // Conexão via pool de conexões (HikariCP)
//                        try (var conn = DatabaseProvider.getConnection()) {
//                            if (conn == null || conn.isClosed()) {
//                                JOptionPane.showMessageDialog(
//                                        null,
//                                        "Falha inesperada ao conectar após verificação. Tenta novamente.",
//                                        "Erro",
//                                        JOptionPane.ERROR_MESSAGE
//                                );
//                                System.exit(1);
//                                return;
//                            }
//                            System.out.println("[DB] Conexão validada com sucesso!");
//                            new ScreenLogin().setVisible(true);
//                        } catch (Exception e) {
//                            JOptionPane.showMessageDialog(
//                                    null,
//                                    "Erro ao conectar ao banco: " + e.getMessage(),
//                                    "Erro de conexão",
//                                    JOptionPane.ERROR_MESSAGE
//                            );
//                            e.printStackTrace();
//                            System.exit(1);
//                        }
//                    }
//                }
//
//            } catch (Exception e) {
//                JOptionPane.showMessageDialog(
//                        null,
//                        "Erro inesperado ao iniciar o sistema: " + e.getMessage(),
//                        "Erro Fatal",
//                        JOptionPane.ERROR_MESSAGE
//                );
//                e.printStackTrace();
//                System.exit(1);
//            }
//        });
//
//        // 3) Fluxo de inicialização da app (Swing thread)
////        SwingUtilities.invokeLater(() -> {
//////             AppState state = DatabaseBootProbe.detect();
////            DatabaseBootProbe.AppState state = DatabaseBootProbe.detect();
////            System.out.println("DEBUG → variável state: " + state);
////
////            switch (state) {
////                case DB_UNREACHABLE -> {
////                    JOptionPane.showMessageDialog(
////                            null,
////                            """
////                        Não foi possível comunicar com o servidor de base de dados.
////
////                        Verifica:
////                        • Se o serviço MySQL/MariaDB está ATIVO
////                        • Host/porta corretos (ex.: localhost:3306 ou a porta publicada no Docker)
////                        • Firewall/antivírus
////
////                        Depois abre novamente a aplicação.
////                        """,
////                            "BD inativa",
////                            JOptionPane.ERROR_MESSAGE
////                    );
////                    new ScreenLogin().setVisible(true);
////                }
////                case DB_NOT_FOUND, SCHEMA_MISSING, INSTALL_INCOMPLETE -> {
////                    new ScreenInstall().setVisible(true);
////                }
////                case READY -> {
////                    var conn = ConnectionDatabase.getConnect();
////                    if (conn == null) {
////                        JOptionPane.showMessageDialog(
////                                null,
////                                "Falha inesperada ao ligar após verificação. Tenta novamente.",
////                                "Erro",
////                                JOptionPane.ERROR_MESSAGE
////                        );
////                        System.exit(1);
////                        return;
////                    }
//////                    new ScreenLogin().setVisible(true);
////                    new ScreenLogin().setVisible(true);
////                }
////            }
////        });
//    }
}
