/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.dao.UserDao;
import com.okutonda.okudpdv.utilities.UserSession;
import com.okutonda.okudpdv.models.User;
import java.util.List;

/**
 *
 * @author kenny
 */
public class UserController {

    UserDao dao;
    UserSession session = UserSession.getInstance();

    public UserController() {
        this.dao = new UserDao();
    }

    public User login(String email, String password) {
        User user = dao.login(email, password);
        if (user != null) {
            System.out.println("UserControllher");
            System.out.println(user.getStatus());
            System.out.println(user);

            session.clearSession();
            session.setUser(user);
        }
        return user;
    }

    public Boolean logout() {
        session.clearSession();
        return true;
    }

    public User add(User user, int id) {
        boolean status;
//        System.out.println("use"+user.getName());
        if (id == 0) {
            status = dao.add(user);
        } else {
            status = dao.edit(user, id);
        }
        if (status == true) {
            User responde = dao.searchFromNif(user.getNif());
            return responde;
        }
        return null;
    }

//    public User add(User user) {
//        boolean status = dao.add(user);
//        if (status == true) {
//            User responde = dao.searchFromName(user.getName());
//            return responde;
//        }
//        return null;
//    }
    public User getId(int id) {
        return dao.getId(id);
    }

    public User getName(String name) {
        return dao.searchFromName(name);
    }

    public Boolean validateManager(String code) {
        User user = dao.validateManagerCode(code);
        System.out.println("hhh:" + user);
        if (user.getStatus().equals("ativo") == true) {
            return true;
        }
        return false;
    }

    public Boolean deleteId(int id) {
        return dao.delete(id);
    }

    public List<User> get(String where) {
        return dao.list(where);
    }

    public List<User> filter(String txt) {
        return dao.filter(txt);
    }
}
