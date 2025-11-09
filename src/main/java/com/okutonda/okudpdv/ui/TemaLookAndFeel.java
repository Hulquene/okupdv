/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.ui;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

/**
 *
 * @author hr
 */
public class TemaLookAndFeel {

    private TemaLookAndFeel() {
    }

    public static void setupFlatLaf() {
        // Tema corporativo moderno
        FlatLightLaf.setup();
    }

    /**
     * Aplica tema corporativo moderno para TODO O SISTEMA
     */
    public static void aplicarUIManagerBasico() {
        // ---- FONTS MODERNAS E SUTIS ----
        FontUIResource fontPequeno = new FontUIResource("Segoe UI", Font.PLAIN, 12);
        FontUIResource fontPrincipal = new FontUIResource("Segoe UI", Font.PLAIN, 13);
        FontUIResource fontTitulo = new FontUIResource("Segoe UI", Font.BOLD, 13);
        FontUIResource fontHeader = new FontUIResource("Segoe UI", Font.BOLD, 15);

        // Font padrão para todos os componentes
        UIManager.put("defaultFont", fontPrincipal);

        // ===== CONFIGURAÇÕES GLOBAIS =====
        // 🔥 FUNDOS COM HIERARQUIA CLARA
        put("Panel.background", TemaCores.BG_MAIN);           // Fundo PRINCIPAL de toda a aplicação
        put("Panel.secondary.background", TemaCores.BG_SECONDARY); // Fundo secundário (sidebars)
        put("Panel.card.background", TemaCores.BG_CARD);      // Cards/conteúdo em BRANCO PURO

        // 🔥 TEXTOS COM CONTRASTE ADEQUADO
        put("Label.foreground", TemaCores.TEXT_PRIMARY);      // Texto PRINCIPAL em preto suave
        put("Component.foreground", TemaCores.TEXT_PRIMARY);
        put("Component.accentColor", TemaCores.PRIMARY);

        // Fontes globais específicas
        put("Label.font", fontPrincipal);
        put("Button.font", fontPrincipal);
        put("TextField.font", fontPrincipal);
        put("ComboBox.font", fontPrincipal);
        put("CheckBox.font", fontPrincipal);
        put("RadioButton.font", fontPrincipal);
        put("ToggleButton.font", fontPrincipal);
        put("TabbedPane.font", fontPrincipal);
        put("Menu.font", fontPrincipal);
        put("MenuItem.font", fontPrincipal);

        // Cantos arredondados modernos
        put("Component.arc", 6);
        put("Button.arc", 6);
        put("TextComponent.arc", 4);

        // ===== BOTÕES =====
        put("Button.background", TemaCores.BUTTON_PRIMARY);
        put("Button.foreground", Color.WHITE);
        put("Button.border", new javax.swing.border.EmptyBorder(8, 14, 8, 14));
        put("Button.focusedBackground", TemaCores.PRIMARY_DARK);
        put("Button.hoverBackground", TemaCores.PRIMARY_DARK);
        put("Button.pressedBackground", TemaCores.PRIMARY_DARK);

        // Botões secundários
        put("Button.secondary.background", TemaCores.BUTTON_SECONDARY);
        put("Button.secondary.foreground", Color.WHITE);

        // ===== BOTÕES TOGGLE =====
        put("ToggleButton.background", TemaCores.BG_CARD);
        put("ToggleButton.foreground", TemaCores.TEXT_PRIMARY);
        put("ToggleButton.border", new javax.swing.border.EmptyBorder(8, 12, 8, 12));
        put("ToggleButton.selectedBackground", TemaCores.PRIMARY);
        put("ToggleButton.selectedForeground", Color.WHITE);
        put("ToggleButton.hoverBackground", TemaCores.HOVER);

        // ===== CAMPOS DE TEXTO =====
        put("TextField.background", TemaCores.INPUT_BACKGROUND);
        put("TextField.foreground", TemaCores.TEXT_PRIMARY);
        put("TextField.border", new javax.swing.border.CompoundBorder(
                new javax.swing.border.LineBorder(TemaCores.INPUT_BORDER, 1, true),
                new javax.swing.border.EmptyBorder(6, 10, 6, 10)
        ));
        put("TextField.caretForeground", TemaCores.PRIMARY);
        put("TextField.selectionBackground", TemaCores.SELECTED);
        put("TextField.selectionForeground", TemaCores.TEXT_PRIMARY);

        put("PasswordField.background", TemaCores.INPUT_BACKGROUND);
        put("PasswordField.foreground", TemaCores.TEXT_PRIMARY);
        put("PasswordField.border", new javax.swing.border.CompoundBorder(
                new javax.swing.border.LineBorder(TemaCores.INPUT_BORDER, 1, true),
                new javax.swing.border.EmptyBorder(6, 10, 6, 10)
        ));

        // ===== COMBOBOX =====
        put("ComboBox.background", TemaCores.INPUT_BACKGROUND);
        put("ComboBox.foreground", TemaCores.TEXT_PRIMARY);
        put("ComboBox.border", new javax.swing.border.LineBorder(TemaCores.INPUT_BORDER, 1, true));
        put("ComboBox.buttonHoverArrowColor", TemaCores.PRIMARY);
        put("ComboBox.buttonArrowColor", TemaCores.TEXT_MUTED);
        put("ComboBox.selectionBackground", TemaCores.SELECTED);
        put("ComboBox.selectionForeground", TemaCores.TEXT_PRIMARY);

        // ===== TABELAS =====
        put("Table.background", TemaCores.BG_CARD);
        put("Table.foreground", TemaCores.TEXT_PRIMARY);
        put("Table.gridColor", TemaCores.BORDER_SECONDARY);
        put("Table.selectionBackground", TemaCores.SELECTED);
        put("Table.selectionForeground", TemaCores.TEXT_PRIMARY);
        put("Table.font", fontPrincipal);

        // Header de tabela
        put("TableHeader.background", TemaCores.TABLE_HEADER);
        put("TableHeader.foreground", Color.WHITE);
        put("TableHeader.font", fontTitulo);
        put("TableHeader.cellBorder", new javax.swing.border.EmptyBorder(8, 6, 8, 6));

        // ===== TABBED PANE =====
        put("TabbedPane.background", TemaCores.BG_MAIN);
        put("TabbedPane.foreground", TemaCores.TEXT_PRIMARY);
        put("TabbedPane.contentAreaColor", TemaCores.BG_CARD);
        put("TabbedPane.underlineColor", TemaCores.PRIMARY);
        put("TabbedPane.selectedBackground", TemaCores.BG_CARD);
        put("TabbedPane.selectedForeground", TemaCores.PRIMARY);
        put("TabbedPane.hoverColor", TemaCores.HOVER);

        // ===== SCROLL BARS =====
        put("ScrollBar.thumb", new Color(173, 181, 189));
        put("ScrollBar.thumbHover", new Color(134, 142, 150));
        put("ScrollBar.thumbPressed", new Color(73, 80, 87));
        put("ScrollBar.track", new Color(241, 243, 245));
        put("ScrollBar.width", 10);

        // ===== SCROLL PANE =====
        put("ScrollPane.background", TemaCores.BG_MAIN);
        put("ScrollPane.border", new javax.swing.border.LineBorder(TemaCores.BORDER_PRIMARY, 1, true));
        put("Viewport.background", TemaCores.BG_CARD);

        // ===== TOOLBAR =====
        put("ToolBar.background", TemaCores.BG_TOOLBAR);
        put("ToolBar.foreground", TemaCores.TEXT_PRIMARY);
        put("ToolBar.border", BorderFactory.createLineBorder(TemaCores.BORDER_PRIMARY, 1));

        // ===== MENUS =====
        put("MenuBar.background", TemaCores.BG_HEADER);
        put("MenuBar.foreground", Color.WHITE);
        put("MenuBar.border", new javax.swing.border.EmptyBorder(2, 0, 2, 0));

        put("Menu.background", TemaCores.BG_CARD);
        put("Menu.foreground", TemaCores.TEXT_PRIMARY);
        put("Menu.selectionBackground", TemaCores.SELECTED);
        put("Menu.selectionForeground", TemaCores.TEXT_PRIMARY);

        put("MenuItem.background", TemaCores.BG_CARD);
        put("MenuItem.foreground", TemaCores.TEXT_PRIMARY);
        put("MenuItem.selectionBackground", TemaCores.SELECTED);
        put("MenuItem.selectionForeground", TemaCores.TEXT_PRIMARY);

        // ===== PROGRESS BAR =====
        put("ProgressBar.background", new Color(233, 236, 239));
        put("ProgressBar.foreground", TemaCores.PRIMARY);
        put("ProgressBar.border", new javax.swing.border.EmptyBorder(1, 1, 1, 1));

        // ===== SEPARATORS =====
        put("Separator.background", TemaCores.BORDER_PRIMARY);
        put("Separator.foreground", TemaCores.BORDER_PRIMARY);

        // ===== TOOLTIPS =====
        put("ToolTip.background", new Color(52, 58, 64));
        put("ToolTip.foreground", Color.WHITE);
        put("ToolTip.border", new javax.swing.border.EmptyBorder(6, 10, 6, 10));
        put("ToolTip.font", fontPequeno);

        // Velocidade de animação (suave)
        put("Component.animationSpeed", 200);
    }

    /**
     * Aplica configurações de fonte específicas para diferentes tamanhos
     */
    public static void aplicarFontesPersonalizadas() {
        // Fontes para diferentes cenários
        Font pequeno = new Font("Segoe UI", Font.PLAIN, 11);
        Font normal = new Font("Segoe UI", Font.PLAIN, 13);
        Font negrito = new Font("Segoe UI", Font.BOLD, 13);
        Font titulo = new Font("Segoe UI", Font.BOLD, 14);
        Font grande = new Font("Segoe UI", Font.BOLD, 16);

        // Configurações específicas
        put("Button.small.font", pequeno);
        put("Button.large.font", grande);
        put("Label.small.font", pequeno);
        put("Label.large.font", grande);
        put("Table.small.font", pequeno);
        put("Panel.title.font", titulo);
    }

    /**
     * Aplica tema compacto para telas com muitos dados
     */
    public static void aplicarTemaCompacto() {
        // Fontes menores para densidade de informação
        put("Table.font", new Font("Segoe UI", Font.PLAIN, 11));
        put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 11));
        put("Tree.font", new Font("Segoe UI", Font.PLAIN, 11));
        put("List.font", new Font("Segoe UI", Font.PLAIN, 11));

        // Espaçamentos reduzidos
        put("Table.rowHeight", 20);
        put("Table.intercellSpacing", new java.awt.Dimension(1, 1));
    }

    /**
     * Aplica tema espaçoso para telas de foco
     */
    public static void aplicarTemaEspacoso() {
        // Fontes maiores para melhor legibilidade
        put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));

        // Mais espaçamento
//        put("Button.border", new javax.swing.border.EmptyBorder(12, 20, 12, 20));
////        put("TextField.border", new javax.swing.border.CompoundBorder(
////                new javax.swing.border.LineBorder(TemaCores.BORDER, 1, true),
////                new javax.swing.border.EmptyBorder(10, 14, 10, 14)
//        ));
    }

    // ========== MÉTODOS PUT SOBRECARREGADOS ==========
    // Util: aplica Color -> ColorUIResource
    private static void put(String key, Color c) {
        if (c == null) {
            return;
        }
        UIManager.put(key, new ColorUIResource(c));
    }

    // Util: aplica Border
    private static void put(String key, javax.swing.border.Border border) {
        if (border != null) {
            UIManager.put(key, border);
        }
    }

    // Util: aplica Font
    private static void put(String key, Font font) {
        if (font != null) {
            UIManager.put(key, new FontUIResource(font));
        }
    }

    // Aplica valores inteiros
    private static void put(String key, int value) {
        UIManager.put(key, value);
    }

    // Aplica valores booleanos
    private static void put(String key, boolean value) {
        UIManager.put(key, value);
    }

    // Aplica valores genéricos Object
    private static void put(String key, Object value) {
        if (value != null) {
            UIManager.put(key, value);
        }
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

    /**
     * Aplica tema específico para relatórios e visualização de dados
     */
    public static void aplicarTemaRelatorios() {
        // Cores específicas para gráficos e relatórios
        put("Table.alternateRowColor", new Color(248, 249, 250));
        put("Table.sortIconColor", TemaCores.PRIMARY);
        put("Table.showHorizontalLines", true);
        put("Table.showVerticalLines", false);
    }

    /**
     * Método flexível para aplicar configurações específicas
     */
    public static void aplicarConfiguracao(String tipo) {
        switch (tipo.toLowerCase()) {
            case "compacto":
                aplicarTemaCompacto();
                break;
            case "espacoso":
                aplicarTemaEspacoso();
                break;
            case "relatorios":
                aplicarTemaRelatorios();
                break;
            case "fontes-personalizadas":
                aplicarFontesPersonalizadas();
                break;
            default:
                aplicarUIManagerBasico();
        }
    }
}
