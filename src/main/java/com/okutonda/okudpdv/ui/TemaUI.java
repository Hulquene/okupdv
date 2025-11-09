/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;

/**
 *
 * @author hr
 */
public class TemaUI {

    private TemaUI() {
    }

    /* ============================
       BOTÕES (JButton)
       ============================ */
    public static void aplicarBotaoPadrao(JButton b) {
        baseButton(b);
        b.setBackground(TemaCores.BUTTON_SECONDARY);
        b.setForeground(Color.WHITE);
    }

    public static void aplicarBotaoPrimario(JButton b) {
        baseButton(b);
        b.setBackground(TemaCores.BUTTON_PRIMARY);
        b.setForeground(Color.WHITE);
    }

    public static void aplicarBotaoSucesso(JButton b) {
        baseButton(b);
        b.setBackground(TemaCores.BUTTON_SUCCESS);
        b.setForeground(Color.WHITE);
    }

    public static void aplicarBotaoAviso(JButton b) {
        baseButton(b);
        b.setBackground(TemaCores.WARNING);
        b.setForeground(Color.WHITE);
    }

    public static void aplicarBotaoErro(JButton b) {
        baseButton(b);
        b.setBackground(TemaCores.BUTTON_DANGER);
        b.setForeground(Color.WHITE);
    }

    public static void aplicarBotaoAtivo(JButton b) {
        baseButton(b);
        b.setBackground(TemaCores.PRIMARY);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
    }

    /**
     * Botão para ações de navegação (como no PDV)
     */
    public static void aplicarBotaoNavegacao(JButton b, boolean ativo) {
        baseButton(b);
        if (ativo) {
            b.setBackground(TemaCores.PRIMARY);
            b.setForeground(Color.WHITE);
            b.setBorder(new LineBorder(TemaCores.PRIMARY_DARK, 1, true));
        } else {
            b.setBackground(TemaCores.BG_SECONDARY);
            b.setForeground(TemaCores.TEXT_PRIMARY);
            b.setBorder(new LineBorder(TemaCores.BORDER_PRIMARY, 1, true));
        }
    }

    /**
     * Variante "fantasma" (apenas contorno)
     */
    public static void aplicarBotaoOutline(JButton b, Color cor) {
        baseButton(b);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setForeground(cor);
        b.setBorder(new LineBorder(cor, 2, true));
        b.setBackground(null);
    }

    /**
     * Variante "texto" (sem contorno nem fundo)
     */
    public static void aplicarBotaoTexto(JButton b, Color corTexto) {
        baseButton(b);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setBorder(new EmptyBorder(6, 10, 6, 10));
        b.setForeground(corTexto);
        b.setBackground(null);
    }

    private static void baseButton(AbstractButton b) {
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(8, 14, 8, 14));
        b.setContentAreaFilled(true);
        b.setOpaque(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // dicas extras para FlatLaf
        b.putClientProperty("JButton.buttonType", "roundRect");
    }

    /* ============================
       LABELS (JLabel)
       ============================ */
    public static void aplicarTitulo(JLabel l) {
        l.setForeground(TemaCores.TEXT_PRIMARY);
        l.setFont(l.getFont().deriveFont(Font.BOLD, Math.max(18f, l.getFont().getSize2D())));
    }

    public static void aplicarSubtitulo(JLabel l) {
        l.setForeground(TemaCores.TEXT_SECONDARY);
        l.setFont(l.getFont().deriveFont(Font.PLAIN, Math.max(14f, l.getFont().getSize2D())));
    }

    public static void aplicarLabelPadrao(JLabel l) {
        l.setForeground(TemaCores.TEXT_PRIMARY);
        l.setFont(l.getFont().deriveFont(Font.PLAIN));
    }

    public static void aplicarLabelSucesso(JLabel l) {
        l.setForeground(TemaCores.SUCCESS);
        l.setFont(l.getFont().deriveFont(Font.BOLD));
    }

    public static void aplicarLabelAviso(JLabel l) {
        l.setForeground(TemaCores.WARNING);
        l.setFont(l.getFont().deriveFont(Font.BOLD));
    }

    public static void aplicarLabelErro(JLabel l) {
        l.setForeground(TemaCores.ERROR);
        l.setFont(l.getFont().deriveFont(Font.BOLD));
    }

    public static void aplicarLabelInfo(JLabel l) {
        l.setForeground(TemaCores.INFO);
        l.setFont(l.getFont().deriveFont(Font.BOLD));
    }

    /* ============================
       TEXT FIELDS (JTextField/JPasswordField)
       ============================ */
    public static void aplicarCampoTexto(JTextField f) {
        baseField(f);
        f.setBackground(TemaCores.INPUT_BACKGROUND);
        f.setForeground(TemaCores.TEXT_PRIMARY);
        f.setBorder(new LineBorder(TemaCores.INPUT_BORDER, 1, true));
    }

    public static void aplicarCampoSucesso(JTextField f) {
        aplicarCampoTexto(f);
        f.setBorder(new LineBorder(TemaCores.SUCCESS, 2, true));
    }

    public static void aplicarCampoAviso(JTextField f) {
        aplicarCampoTexto(f);
        f.setBorder(new LineBorder(TemaCores.WARNING, 2, true));
    }

    public static void aplicarCampoErro(JTextField f) {
        aplicarCampoTexto(f);
        f.setBorder(new LineBorder(TemaCores.ERROR, 2, true));
    }

    public static void aplicarCampoDesabilitado(JTextField f) {
        baseField(f);
        f.setEnabled(false);
        f.setBackground(TemaCores.BG_SECONDARY);
        f.setForeground(TemaCores.TEXT_MUTED);
        f.setBorder(new LineBorder(TemaCores.BORDER_SECONDARY, 1, true));
    }

    /**
     * Para JPasswordField também funciona
     */
    public static void aplicarCampoTexto(JPasswordField f) {
        f.setBackground(TemaCores.INPUT_BACKGROUND);
        f.setForeground(TemaCores.TEXT_PRIMARY);
        f.setBorder(new LineBorder(TemaCores.INPUT_BORDER, 1, true));
        f.putClientProperty("JComponent.roundRect", true);
        f.setCaretColor(TemaCores.PRIMARY);
    }

    private static void baseField(JTextField f) {
        f.putClientProperty("JComponent.roundRect", true);
        f.setCaretColor(TemaCores.PRIMARY);
    }

    /* ============================
       PAINÉIS (JPanel) — 4 presets
       ============================ */
    /**
     * Card branco com borda (para forms, conteúdo)
     */
    public static void aplicarPainelCard(JPanel p) {
        p.setOpaque(true);
        p.setBackground(TemaCores.BG_CARD);
        p.setBorder(new javax.swing.border.CompoundBorder(
                new LineBorder(TemaCores.BORDER_PRIMARY, 1, true),
                new EmptyBorder(12, 12, 12, 12)
        ));
        p.putClientProperty("JComponent.roundRect", true);
    }

    /**
     * Painel secundário (cinza claro para sidebars, áreas secundárias)
     */
    public static void aplicarPainelSecundario(JPanel p) {
        p.setOpaque(true);
        p.setBackground(TemaCores.BG_SECONDARY);
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        p.putClientProperty("JComponent.roundRect", true);
    }

    /**
     * Painel principal (fundo da aplicação)
     */
    public static void aplicarPainelPrincipal(JPanel p) {
        p.setOpaque(true);
        p.setBackground(TemaCores.BG_MAIN);
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    /**
     * Header/Bar: faixa colorida para títulos/área de topo
     */
    public static void aplicarPainelHeader(JPanel p, Color cor) {
        p.setOpaque(true);
        p.setBackground(cor != null ? cor : TemaCores.BG_HEADER);
        p.setBorder(new EmptyBorder(8, 12, 8, 12));
        p.putClientProperty("JComponent.roundRect", true);
    }

    /* ============================
       TABELAS (JTable)
       ============================ */
    public static void aplicarTabelaPadrao(JTable tabela) {
        tabela.setBackground(TemaCores.BG_CARD);
        tabela.setForeground(TemaCores.TEXT_PRIMARY);
        tabela.setGridColor(TemaCores.BORDER_SECONDARY);
        tabela.setSelectionBackground(TemaCores.SELECTED);
        tabela.setSelectionForeground(TemaCores.TEXT_PRIMARY);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Configurar header
        JTableHeader header = tabela.getTableHeader();
        header.setBackground(TemaCores.TABLE_HEADER);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    /* ============================
       UTILIDADES
       ============================ */
    /**
     * Aplica família de estilos de "ações CRUD" rapidamente
     */
    public static void aplicarAcoesCRUD(JButton btnNovo, JButton btnSalvar, JButton btnEditar, JButton btnExcluir) {
        if (btnNovo != null) {
            aplicarBotaoPrimario(btnNovo);
        }
        if (btnSalvar != null) {
            aplicarBotaoSucesso(btnSalvar);
        }
        if (btnEditar != null) {
            aplicarBotaoAviso(btnEditar);
        }
        if (btnExcluir != null) {
            aplicarBotaoErro(btnExcluir);
        }
    }

    /**
     * Aplica tema para grupo de botões de navegação
     */
    public static void aplicarGrupoBotoesNavegacao(JButton[] botoes, int indiceAtivo) {
        for (int i = 0; i < botoes.length; i++) {
            aplicarBotaoNavegacao(botoes[i], i == indiceAtivo);
        }
    }

    /**
     * Aplica estilo para toolbars
     */
    public static void aplicarToolbar(JToolBar toolbar) {
        toolbar.setBackground(TemaCores.BG_TOOLBAR);
        toolbar.setForeground(TemaCores.TEXT_PRIMARY);
        toolbar.setBorder(BorderFactory.createLineBorder(TemaCores.BORDER_PRIMARY, 1));
        toolbar.setFloatable(false);
    }

    /**
     * Aplica estilo para campos de total (como no PDV)
     */
    public static void aplicarCampoTotal(JTextField campo) {
        campo.setBackground(TemaCores.BG_CARD);
        campo.setForeground(TemaCores.ERROR);
        campo.setFont(campo.getFont().deriveFont(Font.BOLD, 16f));
        campo.setHorizontalAlignment(JTextField.RIGHT);
        campo.setBorder(new LineBorder(TemaCores.BORDER_PRIMARY, 1, true));
    }

    // No TemaUI.java, adicione:
    public static void aplicarBotaoSidebar(JToggleButton botao, boolean ativo) {
        if (ativo) {
            botao.setBackground(TemaCores.PRIMARY);
            botao.setForeground(Color.WHITE);
            botao.setContentAreaFilled(true);
            botao.setBorderPainted(false);
            botao.setFont(botao.getFont().deriveFont(Font.BOLD));
        } else {
            botao.setBackground(null);
            botao.setForeground(TemaCores.TEXT_PRIMARY);
            botao.setContentAreaFilled(false);
            botao.setBorderPainted(false);
            botao.setFont(botao.getFont().deriveFont(Font.PLAIN));
        }
        botao.setFocusPainted(false);
    }
}
