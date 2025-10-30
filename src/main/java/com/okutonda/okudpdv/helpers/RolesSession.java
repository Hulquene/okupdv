/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.helpers;

/**
 *
 * @author kenny
 */
public class RolesSession {
 private static RolesSession instance;
    private RolesSession() {
        // Construtor privado para evitar instanciamento externo
    }

    public static RolesSession getInstance() {
        if (instance == null) {
            instance = new RolesSession();
        }
        return instance;
    }
}
