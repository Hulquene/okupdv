/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.ui;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.*;

/**
 * Limpa overrides do GUI Builder de forma mais agressiva
 */
public class TemaCleaner {

    private TemaCleaner() {
    }

    /**
     * Limpa overrides do GUI Builder de forma mais completa
     */
    public static void clearBuilderOverrides(Container root) {
        if (root == null) {
            return;
        }
        clearRec(root, 0);
    }

    private static void clearRec(Component c, int depth) {
        if (depth > 20) {
            return; // Prevenir recursão infinita
        }
        if (c instanceof JComponent jc) {
            // Limpar cores manuais
            if (!(jc.getBackground() instanceof UIResource)) {
                jc.setBackground(null);
                jc.setOpaque(true); // 🔥 Garantir que o fundo seja visível
            }
            if (!(jc.getForeground() instanceof UIResource)) {
                jc.setForeground(null);
            }

            // Limpar bordas manuais
            if (!(jc.getBorder() instanceof UIResource)) {
                jc.setBorder(null);
            }

            // Forçar componentes específicos a serem opacos
            if (jc instanceof JPanel || jc instanceof JLabel) {
                jc.setOpaque(true);
            }
        }

        // Recursão para filhos
        if (c instanceof Container cont) {
            for (Component child : cont.getComponents()) {
                clearRec(child, depth + 1);
            }
        }
    }

    /**
     * Método mais agressivo para componentes problemáticos
     */
    public static void forceTheme(Component c) {
        if (c instanceof JComponent jc) {
            jc.setBackground(null);
            jc.setForeground(null);
            jc.setBorder(null);
            jc.setOpaque(true);
            jc.updateUI(); // 🔥 Forçar atualização do UI
        }
    }
}
