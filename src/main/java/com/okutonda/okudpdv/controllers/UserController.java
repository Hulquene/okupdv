package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.UserDao;
import com.okutonda.okudpdv.data.entities.User;
import com.okutonda.okudpdv.helpers.UserSession;
import java.util.List;
import java.util.Optional;

/**
 * Controlador de Usuários com Hibernate.
 *
 * Responsável por aplicar regras de negócio e delegar ao UserDaoHibernate as
 * operações de persistência.
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
        Optional<User> userOpt = dao.login(email, password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            session.clearSession();
            session.setUser(user);
            return user;
        }
        return null;
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
     * Cria ou atualiza um usuário. Se o id for null → cria. Se o id for > 0 →
     * atualiza.
     */
    public User save(User user, Integer id) {
        try {
            User savedUser;

            if (id == null || id == 0) {
                // Criar novo usuário
                savedUser = dao.save(user);
            } else {
                // Atualizar usuário existente
                user.setId(id);
                savedUser = dao.update(user);
            }

            System.out.println("✅ Usuário salvo com sucesso: " + savedUser.getName());
            return savedUser;

        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar usuário: " + e.getMessage());
            return null;
        }
    }

    /**
     * Exclui um usuário pelo ID.
     */
    public boolean deleteById(Integer id) {
        try {
            dao.delete(id);
            System.out.println("✅ Usuário removido ID: " + id);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erro ao remover usuário: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca um usuário pelo ID.
     */
    public User getById(Integer id) {
        Optional<User> userOpt = dao.findById(id);
        return userOpt.orElse(null);
    }

    /**
     * Lista todos os usuários.
     */
    public List<User> getAll() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar usuários: " + e.getMessage());
            return List.of(); // Retorna lista vazia em caso de erro
        }
    }

    /**
     * Pesquisa usuários com base em texto (nome, email, endereço).
     */
    public List<User> filter(String txt) {
        try {
            return dao.filter(txt);
        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar usuários: " + e.getMessage());
            return List.of();
        }
    }

    // =====================
    // FUNÇÕES ESPECÍFICAS
    // =====================
    /**
     * Atualiza o código de gerente (Manager Code) apenas se o usuário logado
     * for o mesmo.
     */
    public boolean updateManagerCode(String code, Integer id) {
        if (session.getUser() == null) {
            System.err.println("❌ Nenhum usuário logado");
            return false;
        }
        if (!session.getUser().getId().equals(id)) {
            System.err.println("❌ Usuário não autorizado para esta operação");
            return false;
        }

        try {
            boolean success = dao.updateManagerCode(id, code);
            if (success) {
                // Atualiza também na sessão
                session.getUser().setCode(code);
                System.out.println("✅ Código de manager atualizado");
            }
            return success;
        } catch (Exception e) {
            System.err.println("❌ Erro ao atualizar código de manager: " + e.getMessage());
            return false;
        }
    }

    /**
     * Atualiza a senha do usuário logado, mediante senha antiga correta.
     */
    public boolean updatePassword(String oldPassword, String newPassword, Integer id) {
        User logged = session.getUser();
        if (logged == null || !logged.getId().equals(id)) {
            System.err.println("❌ Usuário não autorizado");
            return false;
        }

        // Verificar senha antiga
        if (!logged.getPassword().equals(oldPassword)) {
            System.err.println("❌ Senha antiga incorreta");
            return false;
        }

        try {
            boolean success = dao.updatePassword(id, newPassword);
            if (success) {
                // Atualiza na sessão
                logged.setPassword(newPassword);
                System.out.println("✅ Senha atualizada com sucesso");
            }
            return success;
        } catch (Exception e) {
            System.err.println("❌ Erro ao atualizar senha: " + e.getMessage());
            return false;
        }
    }

    /**
     * Valida se o código de gestor informado é válido e ativo.
     */
    public boolean validateManagerCode(String code) {
        Optional<User> userOpt = dao.validateManagerCode(code);
        return userOpt.isPresent() && userOpt.get().getStatus() == 1;
    }

    /**
     * Busca usuário por NIF.
     */
    public User getByNif(String nif) {
        Optional<User> userOpt = dao.findByNif(nif);
        return userOpt.orElse(null);
    }

    /**
     * Verifica se email já existe (para validação de duplicados).
     */
    public boolean emailExists(String email) {
        // Podemos implementar um método específico ou usar o filtro
        List<User> users = filter(email);
        return users.stream().anyMatch(user -> email.equals(user.getEmail()));
    }
}

//package com.okutonda.okudpdv.controllers;
//
//import com.okutonda.okudpdv.data.dao.UserDao;
//import com.okutonda.okudpdv.data.entities.User;
//import com.okutonda.okudpdv.helpers.UserSession;
//import java.util.List;
//
///**
// * Controlador de Usuários.
// *
// * Responsável por aplicar regras de negócio e delegar ao UserDao as operações
// * de persistência.
// *
// * @author Hul…
// */
//public class UserController {
//    
//    private final UserDao dao;
//    private final UserSession session = UserSession.getInstance();
//    
//    public UserController() {
//        this.dao = new UserDao();
//    }
//
//    // =====================
//    // AUTENTICAÇÃO
//    // =====================
//    /**
//     * Realiza o login e inicializa a sessão do usuário.
//     */
//    public User login(String email, String password) {
//        User user = dao.login(email, password);
//        if (user != null) {
//            session.clearSession();
//            session.setUser(user);
//        }
//        dao.close();
//        return user;
//    }
//
//    /**
//     * Termina a sessão atual do usuário.
//     */
//    public boolean logout() {
//        session.clearSession();
//        return true;
//    }
//
//    // =====================
//    // OPERAÇÕES CRUD
//    // =====================
//    /**
//     * Cria ou atualiza um usuário. Se o id for 0 → cria. Se o id for > 0 →
//     * atualiza.
//     */
//    public User save(User user, int id) {
//        boolean status;
//        if (id == 0) {
//            status = dao.add(user);
//        } else {
//            user.setId(id);
//            status = dao.update(user);
//        }
//        
//        if (status) {
//            return dao.findByNif(user.getNif());
//        }
//        return null;
//    }
//
//    /**
//     * Exclui um usuário pelo ID.
//     */
//    public boolean deleteById(int id) {
//        return dao.delete(id);
//    }
//
//    /**
//     * Busca um usuário pelo ID.
//     */
//    public User getById(int id) {
//        return dao.findById(id);
//    }
//
//    /**
//     * Lista todos os usuários.
//     */
//    public List<User> getAll() {
//        List<User> lista = dao.findAll();
//        dao.close();
//        return lista;
////        return dao.findAll();
//    }
//
//    /**
//     * Pesquisa usuários com base em texto (nome, email, endereço).
//     */
//    public List<User> filter(String txt) {
//        List<User> lista = dao.filter(txt);
//        dao.close();
//        return lista;
////        return dao.filter(txt);
//    }
//
//    // =====================
//    // FUNÇÕES ESPECÍFICAS
//    // =====================
//    /**
//     * Atualiza o código de gerente (Manager Code) apenas se o usuário logado
//     * for o mesmo.
//     */
//    public boolean updateManagerCode(String code, int id) {
//        if (session.getUser() == null) {
//            return false;
//        }
//        if (session.getUser().getId() != id) {
//            return false;
//        }
//        
//        return dao.updateManagerCode(id, code);
//    }
//
//    /**
//     * Atualiza a senha do usuário logado, mediante senha antiga correta.
//     */
//    public boolean updatePassword(String oldPassword, String newPassword, int id) {
//        User logged = session.getUser();
//        if (logged == null || logged.getId() != id) {
//            return false;
//        }
//        
//        if (logged.getPassword() != null && logged.getPassword().equals(oldPassword)) {
//            boolean ok = dao.updatePassword(id, newPassword);
//            if (ok) {
//                logged.setPassword(newPassword);
//            }
//            return ok;
//        }
//        return false;
//    }
//
//    /**
//     * Valida se o código de gestor informado é válido e ativo.
//     */
//    public boolean validateManagerCode(String code) {
//        User user = dao.validateManagerCode(code);
//        return user != null && user.getStatus() == 1;
//    }
//}
//
/////*
//// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
//// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
//// */
////package com.okutonda.okudpdv.controllers;
////
////import com.okutonda.okudpdv.data.dao.UserDao;
////import com.okutonda.okudpdv.utilities.UserSession;
////import com.okutonda.okudpdv.data.entities.User;
////import java.util.List;
////
/////**
//// *
//// * @author kenny
//// */
////public class UserController {
////
////    UserDao dao;
////    UserSession session = UserSession.getInstance();
////
////    public UserController() {
////        this.dao = new UserDao();
////    }
////
////    public User login(String email, String password) {
////        User user = dao.login(email, password);
////        if (user != null) {
////            session.clearSession();
////            session.setUser(user);
////        }
////        return user;
////    }
////
////    public Boolean logout() {
////        session.clearSession();
////        return true;
////    }
////
////    public User add(User user, int id) {
////        if (session.getUser().getId() == id) {
////            return null;
////        }
////
////        boolean status;
////        if (id == 0) {
////            status = dao.add(user);
////        } else {
////            status = dao.edit(user, id);
////        }
////        if (status == true) {
////            User responde = dao.searchFromNif(user.getNif());
////            return responde;
////        }
////        return null;
////    }
////
////    public boolean updateCodeManager(String code, int id) {
////        if (session.getUser().getId() == id) {
////            boolean status = dao.updateCodeManager(code, id);
////            return status;
////        }
////        return false;
////    }
////
////    public boolean updatePassword(String newPassword, String oldPassword, int id) {
////        if (session.getUser().getId() == id && (session.getUser().getPassword() == null ? oldPassword == null : session.getUser().getPassword().equals(oldPassword))) {
////            boolean status = dao.updatePassword(newPassword, id);
////            return status;
////        }
////        return false;
////    }
////    //    public User add(User user) {
////    //        boolean status = dao.add(user);
////    //        if (status == true) {
////    //            User responde = dao.searchFromName(user.getName());
////    //            return responde;
////    //        }
////    //        return null;
////    //    }
////
////    public User getId(int id) {
////        return dao.getId(id);
////    }
////
////    public User getName(String name) {
////        return dao.searchFromName(name);
////    }
////
////    public Boolean validateManager(String code) {
////        User user = dao.validateManagerCode(code);
//////        System.out.println("hhh:" + user);
////        return user.getStatus() == 1;
////    }
////
////    public Boolean deleteId(int id) {
////        return dao.delete(id);
////    }
////
////    public List<User> get(String where) {
////        return dao.list(where);
////    }
////
////    public List<User> filter(String txt) {
////        return dao.filter(txt);
////    }
////}
