//package com.okutonda.okudpdv.data.dao;
//
//import com.okutonda.okudpdv.data.config.HibernateUtil;
//import com.okutonda.okudpdv.data.entities.PaymentModes;
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.Predicate;
//import jakarta.persistence.criteria.Root;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class PaymentModeDao {
//
//    private final Class<PaymentModes> entityClass = PaymentModes.class;
//
//    // ==========================================================
//    // 🔹 CRUD
//    // ==========================================================
//    public Optional<PaymentModes> findById(Integer id) {
//        Session session = HibernateUtil.getCurrentSession();
//        try {
//            PaymentModes entity = session.find(PaymentModes.class, id);
//            return Optional.ofNullable(entity);
//        } catch (Exception e) {
//            System.err.println("Erro ao buscar PaymentModes por ID: " + e.getMessage());
//            return Optional.empty();
//        }
//    }
//
//    public List<PaymentModes> findAll() {
//        Session session = HibernateUtil.getCurrentSession();
//        try {
//            CriteriaBuilder cb = session.getCriteriaBuilder();
//            CriteriaQuery<PaymentModes> cq = cb.createQuery(PaymentModes.class);
//            Root<PaymentModes> root = cq.from(PaymentModes.class);
//            cq.select(root);
//
//            return session.createQuery(cq).getResultList();
//        } catch (Exception e) {
//            System.err.println("Erro ao buscar todos os PaymentModes: " + e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }
//
//    public PaymentModes save(PaymentModes paymentMode) {
//        Session session = HibernateUtil.getCurrentSession();
//        Transaction tx = null;
//        try {
//            tx = session.beginTransaction();
//            session.persist(paymentMode);
//            tx.commit();
//
//            System.out.println("✅ PaymentMode salvo: " + paymentMode.getName());
//            return paymentMode;
//
//        } catch (Exception e) {
//            if (tx != null && tx.isActive()) {
//                tx.rollback();
//            }
//            System.err.println("❌ Erro ao salvar PaymentMode: " + e.getMessage());
//            throw new RuntimeException("Erro ao salvar PaymentMode", e);
//        }
//    }
//
//    public PaymentModes update(PaymentModes paymentMode) {
//        Session session = HibernateUtil.getCurrentSession();
//        Transaction tx = null;
//        try {
//            tx = session.beginTransaction();
//            PaymentModes merged = session.merge(paymentMode);
//            tx.commit();
//
//            System.out.println("✅ PaymentMode atualizado: " + paymentMode.getName());
//            return merged;
//
//        } catch (Exception e) {
//            if (tx != null && tx.isActive()) {
//                tx.rollback();
//            }
//            System.err.println("❌ Erro ao atualizar PaymentMode: " + e.getMessage());
//            throw new RuntimeException("Erro ao atualizar PaymentMode", e);
//        }
//    }
//
//    public void delete(Integer id) {
//        Session session = HibernateUtil.getCurrentSession();
//        Transaction tx = null;
//        try {
//            tx = session.beginTransaction();
//
//            PaymentModes paymentMode = session.find(PaymentModes.class, id);
//            if (paymentMode != null) {
//                session.remove(paymentMode);
//            }
//
//            tx.commit();
//            System.out.println("✅ PaymentMode removido ID: " + id);
//
//        } catch (Exception e) {
//            if (tx != null && tx.isActive()) {
//                tx.rollback();
//            }
//            System.err.println("❌ Erro ao remover PaymentMode: " + e.getMessage());
//            throw new RuntimeException("Erro ao remover PaymentMode", e);
//        }
//    }
//
//    // ==========================================================
//    // 🔹 MÉTODOS ESPECÍFICOS
//    // ==========================================================
//    public Optional<PaymentModes> findByName(String name) {
//        Session session = HibernateUtil.getCurrentSession();
//        try {
//            CriteriaBuilder cb = session.getCriteriaBuilder();
//            CriteriaQuery<PaymentModes> cq = cb.createQuery(PaymentModes.class);
//            Root<PaymentModes> root = cq.from(PaymentModes.class);
//
//            cq.select(root).where(cb.equal(root.get("name"), name));
//
//            return session.createQuery(cq).uniqueResultOptional();
//
//        } catch (Exception e) {
//            System.err.println("Erro ao buscar PaymentMode por nome: " + e.getMessage());
//            return Optional.empty();
//        }
//    }
//
//    public Optional<PaymentModes> findByCode(String code) {
//        Session session = HibernateUtil.getCurrentSession();
//        try {
//            CriteriaBuilder cb = session.getCriteriaBuilder();
//            CriteriaQuery<PaymentModes> cq = cb.createQuery(PaymentModes.class);
//            Root<PaymentModes> root = cq.from(PaymentModes.class);
//
//            cq.select(root).where(cb.equal(root.get("code"), code));
//
//            return session.createQuery(cq).uniqueResultOptional();
//
//        } catch (Exception e) {
//            System.err.println("Erro ao buscar PaymentMode por código: " + e.getMessage());
//            return Optional.empty();
//        }
//    }
//
//    public Optional<PaymentModes> findDefault() {
//        Session session = HibernateUtil.getCurrentSession();
//        try {
//            CriteriaBuilder cb = session.getCriteriaBuilder();
//            CriteriaQuery<PaymentModes> cq = cb.createQuery(PaymentModes.class);
//            Root<PaymentModes> root = cq.from(PaymentModes.class);
//
//            cq.select(root).where(cb.equal(root.get("isDefault"), 1));
//
//            return session.createQuery(cq).uniqueResultOptional();
//
//        } catch (Exception e) {
//            System.err.println("Erro ao buscar PaymentMode padrão: " + e.getMessage());
//            return Optional.empty();
//        }
//    }
//
//    public List<PaymentModes> filter(String text) {
//        Session session = HibernateUtil.getCurrentSession();
//        try {
//            CriteriaBuilder cb = session.getCriteriaBuilder();
//            CriteriaQuery<PaymentModes> cq = cb.createQuery(PaymentModes.class);
//            Root<PaymentModes> root = cq.from(PaymentModes.class);
//
//            String likePattern = "%" + text + "%";
//
//            Predicate namePredicate = cb.like(root.get("name"), likePattern);
//            Predicate codePredicate = cb.like(root.get("code"), likePattern);
//            Predicate descriptionPredicate = cb.like(root.get("description"), likePattern);
//
//            cq.select(root).where(cb.or(namePredicate, codePredicate, descriptionPredicate));
//
//            return session.createQuery(cq).getResultList();
//
//        } catch (Exception e) {
//            System.err.println("Erro ao filtrar PaymentModes: " + e.getMessage());
//            return new ArrayList<>();
//        }
//    }
//
//    public List<PaymentModes> findActive() {
//        Session session = HibernateUtil.getCurrentSession();
//        try {
//            CriteriaBuilder cb = session.getCriteriaBuilder();
//            CriteriaQuery<PaymentModes> cq = cb.createQuery(PaymentModes.class);
//            Root<PaymentModes> root = cq.from(PaymentModes.class);
//
//            cq.select(root).where(cb.equal(root.get("status"), 1));
//
//            return session.createQuery(cq).getResultList();
//
//        } catch (Exception e) {
//            System.err.println("Erro ao buscar PaymentModes ativos: " + e.getMessage());
//            return new ArrayList<>();
//        }
//    }
//
//    /**
//     * Define um modo de pagamento como padrão e remove o padrão anterior
//     */
//    public boolean setDefaultPaymentMode(Integer paymentModeId) {
//        Session session = HibernateUtil.getCurrentSession();
//        Transaction tx = null;
//        try {
//            tx = session.beginTransaction();
//
//            // Remove padrão atual
//            session.createMutationQuery("UPDATE PaymentModes SET isDefault = 0 WHERE isDefault = 1")
//                    .executeUpdate();
//
//            // Define novo padrão
//            session.createMutationQuery("UPDATE PaymentModes SET isDefault = 1 WHERE id = :id")
//                    .setParameter("id", paymentModeId)
//                    .executeUpdate();
//
//            tx.commit();
//            System.out.println("✅ PaymentMode padrão definido: " + paymentModeId);
//            return true;
//
//        } catch (Exception e) {
//            if (tx != null && tx.isActive()) {
//                tx.rollback();
//            }
//            System.err.println("❌ Erro ao definir PaymentMode padrão: " + e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * Verifica se nome já existe
//     */
//    public boolean nameExists(String name) {
//        return findByName(name).isPresent();
//    }
//
//    /**
//     * Verifica se código já existe
//     */
//    public boolean codeExists(String code) {
//        return findByCode(code).isPresent();
//    }
//}
