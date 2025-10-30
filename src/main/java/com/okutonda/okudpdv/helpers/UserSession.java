/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.helpers;

import com.okutonda.okudpdv.data.entities.User;

/**
 *
 * @author kenny
 */
public class UserSession {

    private static UserSession instance;
    private User user;
//    private String username;
//    private String userProfile;
//    private int userId;
//    private int userStatus;

    private UserSession() {
        // Construtor privado para evitar instanciamento externo
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getUsername() {
//        return username;
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
        this.user = null;
//        username = null;
//        userId = 0;
//        username = null;
//        userProfile = null;
//        userStatus = 0;
    }

    // Método para evitar clonagem da instância
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning of this object is not allowed");
    }
}
