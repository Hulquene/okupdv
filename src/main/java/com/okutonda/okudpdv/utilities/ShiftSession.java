/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.utilities;

import com.okutonda.okudpdv.data.entities.Shift;
import com.okutonda.okudpdv.data.entities.User;

/**
 *
 * @author kenny
 */
public class ShiftSession {

    private static ShiftSession instance;
    private Shift shift;
    private User seller;
//    private Double shiftValue;
//    private int shiftStatus;
//    private String userProfile;
//    private int userId;
//    private int userStatus;

    private ShiftSession() {
        // Construtor privado para evitar instanciamento externo
    }

    public static ShiftSession getInstance() {
        if (instance == null) {
            instance = new ShiftSession();
        }
        return instance;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

//    
//    
//    public int getShiftStatus() {
//        return shiftStatus;
//    }
//
//    public void setShiftStatus(int shiftStatus) {
//        this.shiftStatus = shiftStatus;
//    }
//
//    public Double getShiftValue() {
//        return shiftValue;
//    }
//
//    public void setShiftValue(Double shiftValue) {
//        this.shiftValue = shiftValue;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
//
//    public int getUserId() {
//        return userId;
//    }
//
//    public String getUserProfile() {
//        return userProfile;
//    }
//
//    public void setUserProfile(String userProfile) {
//        this.userProfile = userProfile;
//    }
//
//    public int getUserStatus() {
//        return userStatus;
//    }
//
//    public void setUserStatus(int userStatus) {
//        this.userStatus = userStatus;
//    }

    // Método para limpar a sessão
    public void clearSession() {
        shift = null;
        seller = null;
//        shiftValue = null;
//        userProfile = null;
//        userId = 0;
//        userStatus = 0;
//        shiftStatus = 0;
    }

    // Método para evitar clonagem da instância
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning of this object is not allowed");
    }
}
