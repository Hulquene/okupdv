/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.ui;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.Window;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

/**
 *
 * @author hr
 */
public class TemaLookAndFeel {

    private TemaLookAndFeel() {
    }

    public static void setupFlatLaf() {
        // Ativa o FlatLaf (podes trocar por FlatDarkLaf.setup() se quiser tema escuro)
        FlatLightLaf.setup();
    }

    /**
     * Aplica **todas** as chaves globais de UI. Chamar DEPOIS do LaF.
     */
    public static void aplicarUIManagerBasico() {
        // ---- Paleta base
        put("Panel.background", TemaCores.BG_LIGHT);
        put("Label.foreground", TemaCores.TEXT_DARK);

        // Accent do FlatLaf (foco, seleção, controles)
        put("Component.accentColor", TemaCores.PRIMARY);

        // Canto arredondado padrão (FlatLaf)
        UIManager.put("Component.arc", 12);
        UIManager.put("Button.arc", 12);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("ProgressBar.arc", 8);

        // ---- Botões
        put("Button.background", TemaCores.PRIMARY);
        put("Button.foreground", Color.WHITE);
        put("Button.focusedBackground", TemaCores.PRIMARY);
        put("Button.hoverBackground", mix(TemaCores.PRIMARY, Color.WHITE, 0.12));
        put("Button.pressedBackground", mix(TemaCores.PRIMARY, Color.BLACK, 0.10));

        // Toggle / Check / Radio
        put("ToggleButton.background", TemaCores.PRIMARY);
        put("ToggleButton.foreground", Color.WHITE);
        put("CheckBox.icon[filled]CheckmarkColor", TemaCores.PRIMARY);
        put("RadioButton.icon[filled]SelectionEnabledColor", TemaCores.PRIMARY);

        UIManager.put("CheckBox.icon.selectedBackground", TemaCores.PRIMARY);
        UIManager.put("CheckBox.icon.hoverBorderColor", TemaCores.HIGHLIGHT);

        UIManager.put("RadioButton.icon.selectedBackground", TemaCores.PRIMARY);
        UIManager.put("RadioButton.icon.hoverBorderColor", TemaCores.HIGHLIGHT);

        UIManager.put("ToggleButton.selectedBackground", TemaCores.PRIMARY.darker());
        UIManager.put("ToggleButton.hoverBackground", TemaCores.PRIMARY.brighter());

        UIManager.put("Component.animationSpeed", 200); // mais lento e visível

        // ---- Campos de texto
        put("TextField.background", Color.WHITE);
        put("TextField.foreground", TemaCores.TEXT_DARK);
        put("TextField.caretForeground", TemaCores.PRIMARY);
        put("TextComponent.selectionBackground", TemaCores.HIGHLIGHT);
        put("TextComponent.selectionForeground", TemaCores.TEXT_DARK);

        // ---- ComboBox
        put("ComboBox.buttonHoverArrowColor", TemaCores.PRIMARY);
        put("ComboBox.buttonArrowColor", TemaCores.SECONDARY);
        put("ComboBox.selectionBackground", TemaCores.HIGHLIGHT);
        put("ComboBox.selectionForeground", TemaCores.TEXT_DARK);

        // ---- JTable
        put("Table.background", Color.WHITE);
        put("Table.foreground", TemaCores.TEXT_DARK);
        put("Table.selectionBackground", TemaCores.HIGHLIGHT);
        put("Table.selectionForeground", TemaCores.TEXT_DARK);
        put("TableHeader.background", TemaCores.SECONDARY);
        put("TableHeader.foreground", Color.WHITE);

        // ---- JTabbedPane
        put("TabbedPane.background", TemaCores.BG_LIGHT);
        put("TabbedPane.foreground", TemaCores.TEXT_DARK);
        put("TabbedPane.underlineColor", TemaCores.PRIMARY);
        UIManager.put("TabbedPane.selectedBackground", new ColorUIResource(Color.WHITE));

        // ---- JScrollBar
        put("ScrollBar.thumb", mix(TemaCores.SECONDARY, Color.WHITE, 0.6));
        put("ScrollBar.track", mix(TemaCores.BG_LIGHT, Color.WHITE, 0.5));

        // ---- JOptionPane
        put("OptionPane.background", TemaCores.BG_LIGHT);
        put("OptionPane.messageForeground", TemaCores.TEXT_DARK);
        put("Panel.background", TemaCores.BG_LIGHT); // garante fundo nos dialogs

        // ---- Menu / MenuItem
        put("MenuBar.background", Color.WHITE);
        put("MenuBar.foreground", TemaCores.TEXT_DARK);
        put("Menu.foreground", TemaCores.TEXT_DARK);
        put("MenuItem.selectionBackground", TemaCores.HIGHLIGHT);
        put("MenuItem.selectionForeground", TemaCores.TEXT_DARK);

        // ---- ProgressBar
        put("ProgressBar.background", mix(TemaCores.BG_LIGHT, Color.WHITE, 0.5));
        put("ProgressBar.foreground", TemaCores.PRIMARY);
    }

    // Util: aplica Color -> ColorUIResource
    private static void put(String key, Color c) {
        if (c == null) {
            return;
        }
        UIManager.put(key, new ColorUIResource(c));
    }

    // Mistura de cores simples (para hovers/sombras)
    private static Color mix(Color a, Color b, double fracB) {
        double fracA = 1.0 - fracB;
        int r = (int) Math.round(a.getRed() * fracA + b.getRed() * fracB);
        int g = (int) Math.round(a.getGreen() * fracA + b.getGreen() * fracB);
        int b2 = (int) Math.round(a.getBlue() * fracA + b.getBlue() * fracB);
        return new Color(r, g, b2);
    }

    /**
     * Atualiza TODAS as janelas já abertas (caso mude tema em runtime).
     */
    public static void atualizarUINasJanelasAbertas() {
        for (Window w : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(w);
            w.repaint();
        }
    }
//    public static void aplicarUIManagerBasico() {
//        UIManager.put("Panel.background",      TemaCores.BG_LIGHT);
//        UIManager.put("Label.foreground",      TemaCores.TEXT_DARK);
//        UIManager.put("Button.background",     TemaCores.PRIMARY);
//        UIManager.put("Button.foreground",     Color.WHITE);
//        UIManager.put("TextField.background",  Color.WHITE);
//        UIManager.put("TextField.foreground",  TemaCores.TEXT_DARK);
//        UIManager.put("Table.background",      Color.WHITE);
//        UIManager.put("Table.foreground",      TemaCores.TEXT_DARK);
//        UIManager.put("Table.selectionBackground", TemaCores.HIGHLIGHT);
//        UIManager.put("Table.selectionForeground", TemaCores.TEXT_DARK);
//    }
}
