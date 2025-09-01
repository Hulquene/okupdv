/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.okutonda.okudpdv.jdbc;

import java.awt.HeadlessException;
import java.sql.Connection;
import javax.swing.JOptionPane;

/**
 *
 * @author kenny
 */
public class TestConnectDB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Connection conn = ConnectionDatabase.getConnect();
        
//        try {
//            new ConnectDB().connect();
//            JOptionPane.showMessageDialog(null, "teste de conect: Connectado com sucesso");
//        } catch (HeadlessException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao se conectar: " + e.getMessage());
//        }
        
//         try {
//            new ConnectDB().connect();
//            JOptionPane.showMessageDialog(null, "teste de conect: Connectado com sucesso");
//        } catch (HeadlessException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao se conectar: " + e.getMessage());
//        }
    }
    
}
