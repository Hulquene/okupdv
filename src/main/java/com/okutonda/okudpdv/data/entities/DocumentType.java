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
    FT, // Fatura
    FR, // Fatura-recibo
    NC, // Nota de crédito
    ND, // Nota de débito
    RC, // Recibo
    PP, // Proforma (não fiscal)
    OR; // Orçamento (não fiscal)// Orçamento (não fiscal)

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

    // Método para obter o prefixo (string do enum)
    public String getPrefix() {
        return this.name();
    }

    @Override
    public String toString() {
        switch (this) {
            case FT:
                return "Fatura";
            case FR:
                return "Fatura-Recibo";
            case NC:
                return "Nota de Crédito";
            case ND:
                return "Nota de Débito";
            case RC:
                return "Recibo";
            case PP:
                return "Proforma";
            case OR:
                return "Orçamento";
            default:
                return name();
        }
    }
}