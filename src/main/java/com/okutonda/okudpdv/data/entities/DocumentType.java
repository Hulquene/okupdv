/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.entities;

/**
 *
 * @author hr
 */
public enum DocumentType {
    FT("FT", "Fatura"),
    FR("FR", "Fatura-recibo"),
    NC("NC", "Nota de crédito"),
    ND("ND", "Nota de débito"),
    RC("RC", "Recibo"),
    PP("PP", "Proforma"),
    OR("OR", "Orçamento");

    private final String codigo;
    private final String descricao;

    DocumentType(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    // ✅ NOVO: Getter para código
    public String getCodigo() {
        return codigo;
    }

    // ✅ NOVO: Getter para descrição
    public String getDescricao() {
        return descricao;
    }

    // Método para converter string para DocumentType
    public static DocumentType fromString(String value) {
        if (value == null) return FT;
        
        switch (value.toUpperCase()) {
            case "FT": return FT;
            case "FR": return FR;
            case "NC": return NC;
            case "ND": return ND;
            case "RC": return RC;
            case "PP": return PP;
            case "OR": return OR;
            default: return FT; // valor padrão
        }
    }

    // ✅ NOVO: Método para converter do código
    public static DocumentType fromCodigo(String codigo) {
        if (codigo == null) return FT;
        
        String codigoUpper = codigo.trim().toUpperCase();
        for (DocumentType type : values()) {
            if (type.codigo.equals(codigoUpper)) {
                return type;
            }
        }
        return FT; // valor padrão
    }

    // Método para obter o prefixo (string do enum)
    public String getPrefix() {
        return this.codigo;
    }

    // ✅ NOVO: Método para verificar se é documento fiscal
    public boolean isDocumentoFiscal() {
        return this == FT || this == FR || this == NC || this == ND || this == RC;
    }

    // ✅ NOVO: Método para verificar se é válido para SAF-T
    public boolean isValidoParaSAFT() {
        return this == FT || this == FR || this == NC || this == ND || this == RC;
    }

    @Override
    public String toString() {
        return descricao;
    }
}