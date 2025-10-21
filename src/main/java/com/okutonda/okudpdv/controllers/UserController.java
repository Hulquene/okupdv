package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.UserDao;
import com.okutonda.okudpdv.data.entities.User;
import com.okutonda.okudpdv.utilities.UserSession;
import java.util.List;

/**
 * Controlador de Usuários.
 *
 * Responsável por aplicar regras de negócio e delegar ao UserDao as operações
 * de persistência.
 *
 * @author Hul…
 */
public class UserController {

    private final UserDao dao;
    private final UserSession session = UserSession.getInstance();

    public UserController() {
        this.dao = new UserDao();
    }

    // =====================
    // AUTENTICAÇÃO
    // =====================
    /**
     * Realiza o login e inicializa a sessão do usuário.
     */
    public User login(String email, String password) {
        User user = dao.login(email, password);
        if (user != null) {
            session.clearSession();
            session.setUser(user);
        }
        return user;
    }

    /**
     * Termina a sessão atual do usuário.
     */
    public boolean logout() {
        session.clearSession();
        return true;
    }

    // =====================
    // OPERAÇÕES CRUD
    // =====================
    /**
     * Cria ou atualiza um usuário. Se o id for 0 → cria. Se o id for > 0 →
     * atualiza.
     */
    public User save(User user, int id) {
        boolean status;
        if (id == 0) {
            status = dao.add(user);
        } else {
            user.setId(id);
            status = dao.update(user);
        }

        if (status) {
            return dao.findByNif(user.getNif());
        }
        return null;
    }

    /**
     * Exclui um usuário pelo ID.
     */
    public boolean deleteById(int id) {
        return dao.delete(id);
    }

    /**
     * Busca um usuário pelo ID.
     */
    public User getById(int id) {
        return dao.findById(id);
    }

    /**
     * Lista todos os usuários.
     */
    public List<User> getAll() {
        return dao.findAll();
    }

    /**
     * Pesquisa usuários com base em texto (nome, email, endereço).
     */
    public List<User> filter(String txt) {
        return dao.filter(txt);
    }

    // =====================
    // FUNÇÕES ESPECÍFICAS
    // =====================
    /**
     * Atualiza o código de gerente (Manager Code) apenas se o usuário logado
     * for o mesmo.
     */
    public boolean updateManagerCode(String code, int id) {
        if (session.getUser() == null) {
            return false;
        }
        if (session.getUser().getId() != id) {
            return false;
        }

        return dao.updateManagerCode(id, code);
    }

    /**
     * Atualiza a senha do usuário logado, mediante senha antiga correta.
     */
    public boolean updatePassword(String oldPassword, String newPassword, int id) {
        User logged = session.getUser();
        if (logged == null || logged.getId() != id) {
            return false;
        }

        if (logged.getPassword() != null && logged.getPassword().equals(oldPassword)) {
            boolean ok = dao.updatePassword(id, newPassword);
            if (ok) {
                logged.setPassword(newPassword);
            }
            return ok;
        }
        return false;
    }

    /**
     * Valida se o código de gestor informado é válido e ativo.
     */
    public boolean validateManagerCode(String code) {
        User user = dao.validateManagerCode(code);
        return user != null && user.getStatus() == 1;
    }
}

///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.okutonda.okudpdv.controllers;
//
//import com.okutonda.okudpdv.data.dao.UserDao;
//import com.okutonda.okudpdv.utilities.UserSession;
//import com.okutonda.okudpdv.data.entities.User;
//import java.util.List;
//
///**
// *
// * @author kenny
// */
//public class UserController {
//
//    UserDao dao;
//    UserSession session = UserSession.getInstance();
//
//    public UserController() {
//        this.dao = new UserDao();
//    }
//
//    public User login(String email, String password) {
//        User user = dao.login(email, password);
//        if (user != null) {
//            session.clearSession();
//            session.setUser(user);
//        }
//        return user;
//    }
//
//    public Boolean logout() {
//        session.clearSession();
//        return true;
//    }
//
//    public User add(User user, int id) {
//        if (session.getUser().getId() == id) {
//            return null;
//        }
//
//        boolean status;
//        if (id == 0) {
//            status = dao.add(user);
//        } else {
//            status = dao.edit(user, id);
//        }
//        if (status == true) {
//            User responde = dao.searchFromNif(user.getNif());
//            return responde;
//        }
//        return null;
//    }
//
//    public boolean updateCodeManager(String code, int id) {
//        if (session.getUser().getId() == id) {
//            boolean status = dao.updateCodeManager(code, id);
//            return status;
//        }
//        return false;
//    }
//
//    public boolean updatePassword(String newPassword, String oldPassword, int id) {
//        if (session.getUser().getId() == id && (session.getUser().getPassword() == null ? oldPassword == null : session.getUser().getPassword().equals(oldPassword))) {
//            boolean status = dao.updatePassword(newPassword, id);
//            return status;
//        }
//        return false;
//    }
//    //    public User add(User user) {
//    //        boolean status = dao.add(user);
//    //        if (status == true) {
//    //            User responde = dao.searchFromName(user.getName());
//    //            return responde;
//    //        }
//    //        return null;
//    //    }
//
//    public User getId(int id) {
//        return dao.getId(id);
//    }
//
//    public User getName(String name) {
//        return dao.searchFromName(name);
//    }
//
//    public Boolean validateManager(String code) {
//        User user = dao.validateManagerCode(code);
////        System.out.println("hhh:" + user);
//        return user.getStatus() == 1;
//    }
//
//    public Boolean deleteId(int id) {
//        return dao.delete(id);
//    }
//
//    public List<User> get(String where) {
//        return dao.list(where);
//    }
//
//    public List<User> filter(String txt) {
//        return dao.filter(txt);
//    }
//}
