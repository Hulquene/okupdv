/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.ui;

import java.awt.Color;

/**
 * Paleta de cores universal para todo o sistema com hierarquia visual clara
 */
public class TemaCores {

    private TemaCores() {
    }

    // ===== CORES PRIMÁRIAS =====
    public static final Color PRIMARY = new Color(0, 102, 204);       // Azul corporativo principal
    public static final Color PRIMARY_DARK = new Color(0, 82, 164);   // Azul escuro (hover/active)
    public static final Color PRIMARY_LIGHT = new Color(77, 148, 255); // Azul claro (highlight)
    
    // ===== CORES DE STATUS =====
    public static final Color SUCCESS = new Color(40, 167, 69);       // Verde - sucesso/confirmação
    public static final Color WARNING = new Color(255, 153, 0);       // Laranja - alerta/atenção  
    public static final Color ERROR = new Color(220, 53, 69);         // Vermelho - erro/perigo
    public static final Color INFO = new Color(23, 162, 184);         // Azul claro - informação
    
    // ===== CORES DE FUNDO (HIERARQUIA DEFINIDA) =====
    public static final Color BG_MAIN = new Color(242, 244, 246);     // 🔥 Fundo principal da aplicação
    public static final Color BG_SECONDARY = new Color(248, 249, 250); // 🔥 Fundo secundário (sidebars)
    public static final Color BG_CARD = Color.WHITE;                  // 🔥 Fundo de cards/conteúdo
    public static final Color BG_HEADER = new Color(52, 58, 64);      // Cabeçalhos escuros
    public static final Color BG_TOOLBAR = new Color(233, 236, 239);  // Toolbars
    
    // ===== CORES DE TEXTO =====
    public static final Color TEXT_PRIMARY = new Color(33, 37, 41);   // 🔥 Texto principal (PRETO SUAVE)
    public static final Color TEXT_SECONDARY = new Color(73, 80, 87); // 🔥 Texto secundário
    public static final Color TEXT_MUTED = new Color(108, 117, 125);  // Texto menos importante
    public static final Color TEXT_LIGHT = new Color(173, 181, 189);  // Texto desabilitado
    public static final Color TEXT_WHITE = Color.WHITE;               // Texto sobre fundos escuros
    
    // ===== CORES DE BORDA =====
    public static final Color BORDER_PRIMARY = new Color(206, 212, 218); // 🔥 Bordas principais
    public static final Color BORDER_SECONDARY = new Color(222, 226, 230); // Bordas sutis
    public static final Color BORDER_LIGHT = new Color(233, 236, 239);   // Bordas muito sutis
    
    // ===== CORES DE INTERAÇÃO =====
    public static final Color HOVER = new Color(245, 247, 250);       // Cor ao passar mouse
    public static final Color SELECTED = new Color(224, 242, 255);    // Cor de seleção
    public static final Color FOCUS = new Color(77, 148, 255);        // Cor de foco
    
    // ===== CORES FUNCIONAIS =====
    public static final Color BUTTON_PRIMARY = PRIMARY;               // Botões primários
    public static final Color BUTTON_SECONDARY = new Color(108, 117, 125); // Botões secundários
    public static final Color BUTTON_SUCCESS = SUCCESS;               // Botões de sucesso
    public static final Color BUTTON_DANGER = ERROR;                  // Botões perigosos
    
    public static final Color TABLE_HEADER = new Color(52, 58, 64);   // Cabeçalho de tabelas
    public static final Color TABLE_ROW_EVEN = Color.WHITE;           // Linha par da tabela
    public static final Color TABLE_ROW_ODD = new Color(248, 249, 250); // Linha ímpar
    
    public static final Color INPUT_BACKGROUND = Color.WHITE;         // Fundo de inputs
    public static final Color INPUT_BORDER = BORDER_PRIMARY;          // Borda de inputs
}