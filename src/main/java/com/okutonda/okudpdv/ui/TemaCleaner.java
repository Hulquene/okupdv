/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.ui;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.*;
/**
 *
 * @author hr
 */
public class TemaCleaner {
    private TemaCleaner() {}

    /** Limpa overrides do GUI Builder (cores manuais) para herdar do UIManager. */
    public static void clearBuilderOverrides(Container root) {
        if (root == null) return;
        clearRec(root);
    }

    private static void clearRec(Component c) {
        if (c instanceof JComponent jc) {
            // apenas se a cor NÃO é do UIManager (ou seja, foi setada manualmente)
            if (!(jc.getBackground() instanceof UIResource)) jc.setBackground(null);
            if (!(jc.getForeground() instanceof UIResource)) jc.setForeground(null);
        }
        if (c instanceof Container cont) {
            for (Component child : cont.getComponents()) clearRec(child);
        }
    }
}
