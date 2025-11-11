//package com.okutonda.okudpdv.helpers;
//
//import com.okutonda.okudpdv.data.config.HibernateUtil;
//import com.okutonda.okudpdv.data.dao.OptionsDaoHibernate;
//import com.okutonda.okudpdv.data.dao.UserDaoHibernate;
//import com.okutonda.okudpdv.data.entities.Options;
//import com.okutonda.okudpdv.data.entities.User;
//
//public class TestMigration {
//    
//    public static void main(String[] args) {
//        try {
//            System.out.println("🧪 Testando migração para Hibernate...");
//            
//            // Testar Options
//            OptionsDaoHibernate optionsDao = new OptionsDaoHibernate();
//            
//            // Buscar todas as options
//            var allOptions = optionsDao.findAll();
//            System.out.println("✅ Options encontradas: " + allOptions.size());
//            
//            // Testar Users
//            UserDaoHibernate userDao = new UserDaoHibernate();
//            
//            // Buscar todos os users
//            var allUsers = userDao.findAll();
//            System.out.println("✅ Users encontrados: " + allUsers.size());
//            
//            // Testar login
//            userDao.login("admin@exemplo.com", "senha123")
//                  .ifPresentOrElse(
//                    user -> System.out.println("✅ Login bem-sucedido: " + user.getName()),
//                    () -> System.out.println("❌ Login falhou")
//                  );
//            
//        } catch (Exception e) {
//            System.err.println("❌ Erro no teste: " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            HibernateUtil.closeSession();
//        }
//    }
//}