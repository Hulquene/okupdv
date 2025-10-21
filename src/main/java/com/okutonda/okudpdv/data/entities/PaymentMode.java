/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.entities;

/**
 *
 * @author hr
 */
//public enum PaymentMode {
//    NUMERARIO, MULTICAIXA, TRANSFERENCIA, OUTROS
//}
public enum PaymentMode {
    NUMERARIO,     // pagamento em dinheiro
    MULTICAIXA,    // TPA / cartão
    TRANSFERENCIA, // transferência bancária
    OUTROS         // cheque, crédito, etc.
}