/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.okutonda.okudpdv;

import com.formdev.flatlaf.FlatLightLaf;
import com.okutonda.okudpdv.controllers.AdminRoot;
import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
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

        Connection conn = ConnectionDatabase.getConnect();
//        var softwareStatus = "0";
//        this.conn = new ConnectDB().connect();
//        if (conn != null) {
//            AdminRoot root = new AdminRoot();
//            softwareStatus = root.getStatusSoftware().getValue();
//            System.out.println("Status Software: " + softwareStatus);
////            jLabelStatusBdConect.setText("Conectado");
//        }

        // Defina a data e hora para disparar o evento
//        LocalDateTime eventDateTime = LocalDateTime.of(2024, 8, 24, 8, 11);
//
//        // Converta LocalDateTime para Date
//        Date eventDate = Date.from(eventDateTime.atZone(ZoneId.from(eventDateTime).systemDefault()).toInstant());
//
//        // Calcule o tempo de atraso até a data do evento
//        long delay = eventDate.getTime() - System.currentTimeMillis();
//
//        // Crie um ScheduledExecutorService
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//        // Defina a tarefa a ser executada
//        Runnable eventTask = new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("Evento disparado em: " + LocalDateTime.now());
//                // Coloque aqui o código que deseja executar no evento
//            }
//        };
//
//        // Agende a tarefa para ser executada após o atraso calculado
//        scheduler.schedule(eventTask, delay, TimeUnit.MILLISECONDS);
//
//        System.out.println("Evento agendado para: " + eventDateTime);
// **********************************************************************************
        // Configura o Look and Feel para o estilo do sistema operacional
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to initialize LaF");
        }

        // Executar o código Swing na thread de eventos do Swing
        SwingUtilities.invokeLater(() -> {
            if (conn != null) {
                AdminRoot root = new AdminRoot();
                if (root.getStatusSoftware().equals("1")) {
//                      // Cria e exibe a janela
                    new ScreenLogin().setVisible(true);
//                      frame.jLabelStatusBdConect.setText(text);
//                      frame.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(null, "O sistema esta inativo", "Atenção", JOptionPane.ERROR_MESSAGE);
                    new ScreenInstall().setVisible(true);
                }
            } else {
//                JOptionPane.showMessageDialog(null, "O sistema esta inativo", "Atenção", JOptionPane.ERROR_MESSAGE);
                new ScreenInstall().setVisible(true);
            }
        });
    }
}
