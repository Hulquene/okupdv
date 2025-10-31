//
//public class DatabaseBootProbe {
//
//    public enum AppState {
//        DB_UNREACHABLE,
//        DB_NOT_FOUND,
//        SCHEMA_MISSING,
//        INSTALL_INCOMPLETE,
//        READY
//    }
//
//    public static AppState detect() {
//        try {
//            // 🔥 AGORA USA HIBERNATE para todas as verificações
//            return detectWithHibernate();
//
//        } catch (Exception e) {
//            System.err.println("❌ Hibernate detection failed: " + e.getMessage());
//
//            // Fallback para JDBC se Hibernate falhar completamente
//            return detectWithJDBC();
//        }
//    }
//
//    private static AppState detectWithHibernate() {
//        try {
//            // 1. Testar conexão básica do Hibernate
//            if (!HibernateConfig.testConnection()) {
//                return AppState.DB_UNREACHABLE;
//            }
//
//            // 2. Verificar se tabelas essenciais existem
//            if (!checkEssentialTablesExist()) {
//                return AppState.SCHEMA_MISSING;
//            }
//
//            // 3. Verificar se dados mínimos existem
//            if (!checkMinimumDataExists()) {
//                return AppState.INSTALL_INCOMPLETE;
//            }
//
//            return AppState.READY;
//
//        } catch (Exception e) {
//            System.err.println("❌ Hibernate detection error: " + e.getMessage());
//            return AppState.DB_UNREACHABLE;
//        }
//    }
//
//    private static boolean checkEssentialTablesExist() {
//        try {
//            Session session = HibernateUtil.getCurrentSession();
//
//            // Verificar se tabela options existe (usando HQL)
//            Long optionsCount = session.createQuery(
//                    "SELECT COUNT(o) FROM Options o", Long.class)
//                    .getSingleResult();
//
//            // Se chegou aqui, a tabela existe
//            return true;
//
//        } catch (Exception e) {
//            System.err.println("❌ Essential tables check failed: " + e.getMessage());
//            return false;
//        }
//    }
//
//    private static boolean checkMinimumDataExists() {
//        try {
//            Session session = HibernateUtil.getCurrentSession();
//
//            // Verificar se existe pelo menos 1 usuário
//            Long userCount = session.createQuery(
//                    "SELECT COUNT(u) FROM User u", Long.class)
//                    .getSingleResult();
//
//            // Verificar se existe configuração básica
//            Long optionsCount = session.createQuery(
//                    "SELECT COUNT(o) FROM Options o WHERE o.name = 'companyName'", Long.class)
//                    .getSingleResult();
//
//            return userCount > 0 && optionsCount > 0;
//
//        } catch (Exception e) {
//            System.err.println("❌ Minimum data check failed: " + e.getMessage());
//            return false;
//        }
//    }
//
//    // 🔁 FALLBACK: Método JDBC original (mantido para compatibilidade)
//    private static AppState detectWithJDBC() {
//        // ... seu código JDBC original aqui
//        String urlServer = "jdbc:mysql://" + ConnectionConfig.getHost() + ":" + ConnectionConfig.getPort() + "/"
//                + "?useUnicode=true&characterEncoding=UTF-8"
//                + "&useSSL=false&allowPublicKeyRetrieval=true"
//                + "&serverTimezone=UTC&connectTimeout=5000&socketTimeout=5000";
//
//        try (Connection cServer = DriverManager.getConnection(urlServer, ConnectionConfig.getUser(), ConnectionConfig.getPassword())) {
//            // ... resto do código JDBC
//            return AppState.READY; // simplificado
//        } catch (SQLException e) {
//            return AppState.DB_UNREACHABLE;
//        }
//    }
//}
