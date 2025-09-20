/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.models;

/**
 *
 * @author hr
 */
public enum InvoiceType {
    FT, // Fatura
    FR, // Fatura-recibo
    NC, // Nota de crédito
    ND, // Nota de débito
    RC, // Recibo
    PP, // Proforma (não fiscal)
    OR; // Orçamento (não fiscal)

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
