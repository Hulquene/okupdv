/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import javax.swing.border.LineBorder;

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
        b.setBackground(TemaCores.SECONDARY);
        b.setForeground(Color.WHITE);
    }

    public static void aplicarBotaoPrimario(JButton b) {
        baseButton(b);
        b.setBackground(TemaCores.PRIMARY);
        b.setForeground(Color.WHITE);
    }

    public static void aplicarBotaoSucesso(JButton b) {
        baseButton(b);
        b.setBackground(TemaCores.SUCCESS);
        b.setForeground(Color.WHITE);
    }

    public static void aplicarBotaoAviso(JButton b) { // warning
        baseButton(b);
        b.setBackground(TemaCores.WARNING);
        b.setForeground(Color.WHITE);
    }

    public static void aplicarBotaoErro(JButton b) {
        baseButton(b);
        b.setBackground(TemaCores.ERROR);
        b.setForeground(Color.WHITE);
    }

    public static void aplicarBotaoAtivo(JButton b) {
        baseButton(b);
        b.setBackground(TemaCores.PRIMARY);   // cor de ativo
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
    }

    /**
     * Variante “fantasma” (apenas contorno)
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
     * Variante “texto” (sem contorno nem fundo)
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
        // dicas extras para FlatLaf
        b.putClientProperty("JButton.buttonType", "roundRect"); // cantos arredondados
    }

    /* ============================
       LABELS (JLabel)
       ============================ */
    public static void aplicarTitulo(JLabel l) {
        l.setForeground(TemaCores.TEXT_DARK);
        l.setFont(l.getFont().deriveFont(Font.BOLD, Math.max(18f, l.getFont().getSize2D())));
    }

    public static void aplicarSubtitulo(JLabel l) {
        l.setForeground(TemaCores.TEXT_GRAY);
        l.setFont(l.getFont().deriveFont(Font.PLAIN, Math.max(14f, l.getFont().getSize2D())));
    }

    public static void aplicarLabelPadrao(JLabel l) {
        l.setForeground(TemaCores.TEXT_DARK);
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

    /* ============================
       TEXT FIELDS (JTextField/JPasswordField)
       ============================ */
    public static void aplicarCampoTexto(JTextField f) {
        baseField(f);
        f.setBackground(Color.WHITE);
        f.setForeground(TemaCores.TEXT_DARK);
        f.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
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
        f.setBackground(new Color(245, 245, 245));
        f.setForeground(TemaCores.TEXT_GRAY);
        f.setBorder(new LineBorder(new Color(230, 230, 230), 1, true));
    }

    /**
     * para JPasswordField também funciona
     */
    public static void aplicarCampoTexto(JPasswordField f) {
        f.setBackground(Color.WHITE);
        f.setForeground(TemaCores.TEXT_DARK);
        f.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        f.putClientProperty("JComponent.roundRect", true);
        f.setCaretColor(TemaCores.PRIMARY);
    }

    private static void baseField(JTextField f) {
        f.putClientProperty("JComponent.roundRect", true); // FlatLaf cantos arredondados
        f.setCaretColor(TemaCores.PRIMARY);
    }

    /* ============================
       PAINÉIS (JPanel) — 3 presets
       ============================ */
    /**
     * Card claro com sombra leve (para forms, caixas)
     */
    public static void aplicarPainelCard(JPanel p) {
        p.setOpaque(true);
        p.setBackground(Color.WHITE);
        p.setBorder(new javax.swing.border.CompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(12, 12, 12, 12)
        ));
        p.putClientProperty("JComponent.roundRect", true);
    }

    /**
     * Seção/Surface: cinza claro, bom para separar blocos
     */
    public static void aplicarPainelSurface(JPanel p) {
        p.setOpaque(true);
        p.setBackground(TemaCores.BG_LIGHT);
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        p.putClientProperty("JComponent.roundRect", true);
    }

    /**
     * Header/Bar: faixa colorida para títulos/área de topo
     */
    public static void aplicarPainelHeader(JPanel p, Color cor) {
        p.setOpaque(true);
        p.setBackground(cor != null ? cor : TemaCores.PRIMARY);
        p.setBorder(new EmptyBorder(8, 12, 8, 12));
        p.putClientProperty("JComponent.roundRect", true);
    }

    /* ============================
       UTILIDADES
       ============================ */
    /**
     * Aplica família de estilos de “ações CRUD” rapidamente
     */
    public static void aplicarAcoesCRUD(JButton btnNovo, JButton btnSalvar, JButton btnEditar, JButton btnExcluir) {
        if (btnNovo != null) {
            aplicarBotaoPadrao(btnNovo);
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
}
