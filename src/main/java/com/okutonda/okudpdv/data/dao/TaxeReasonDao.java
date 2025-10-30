package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.ReasonTaxes;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaxeReasonDao {

    private final Class<ReasonTaxes> entityClass = ReasonTaxes.class;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    
    public Optional<ReasonTaxes> findById(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            ReasonTaxes entity = session.find(ReasonTaxes.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar ReasonTaxes por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<ReasonTaxes> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ReasonTaxes> cq = cb.createQuery(ReasonTaxes.class);
            Root<ReasonTaxes> root = cq.from(ReasonTaxes.class);
            cq.select(root).orderBy(cb.asc(root.get("code")));
            
            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os ReasonTaxes: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ReasonTaxes save(ReasonTaxes reasonTax) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(reasonTax);
            tx.commit();
            
            System.out.println("✅ ReasonTaxes salvo: " + reasonTax.getCode());
            return reasonTax;
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar ReasonTaxes: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar ReasonTaxes", e);
        }
    }

    public ReasonTaxes update(ReasonTaxes reasonTax) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ReasonTaxes merged = session.merge(reasonTax);
            tx.commit();
            
            System.out.println("✅ ReasonTaxes atualizado: " + reasonTax.getCode());
            return merged;
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar ReasonTaxes: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar ReasonTaxes", e);
        }
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            ReasonTaxes reasonTax = session.find(ReasonTaxes.class, id);
            if (reasonTax != null) {
                session.remove(reasonTax);
            }
            
            tx.commit();
            System.out.println("✅ ReasonTaxes removido ID: " + id);
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover ReasonTaxes: " + e.getMessage());
            throw new RuntimeException("Erro ao remover ReasonTaxes", e);
        }
    }

    // ==========================================================
    // 🔹 Consultas personalizadas
    // ==========================================================
    
    public Optional<ReasonTaxes> findByCode(String code) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ReasonTaxes> cq = cb.createQuery(ReasonTaxes.class);
            Root<ReasonTaxes> root = cq.from(ReasonTaxes.class);
            
            cq.select(root).where(cb.equal(root.get("code"), code));
            
            return session.createQuery(cq).uniqueResultOptional();
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar ReasonTaxes por código: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<ReasonTaxes> searchByDescription(String text) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ReasonTaxes> cq = cb.createQuery(ReasonTaxes.class);
            Root<ReasonTaxes> root = cq.from(ReasonTaxes.class);
            
            String likePattern = "%" + text + "%";
            
            Predicate descriptionPredicate = cb.like(root.get("description"), likePattern);
            Predicate reasonPredicate = cb.like(root.get("reason"), likePattern);
            
            cq.select(root)
              .where(cb.or(descriptionPredicate, reasonPredicate))
              .orderBy(cb.asc(root.get("code")));
            
            return session.createQuery(cq).getResultList();
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar ReasonTaxes por descrição: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<ReasonTaxes> filter(String text) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ReasonTaxes> cq = cb.createQuery(ReasonTaxes.class);
            Root<ReasonTaxes> root = cq.from(ReasonTaxes.class);
            
            String likePattern = "%" + text + "%";
            
            Predicate codePredicate = cb.like(root.get("code"), likePattern);
            Predicate descriptionPredicate = cb.like(root.get("description"), likePattern);
            Predicate reasonPredicate = cb.like(root.get("reason"), likePattern);
            Predicate standardPredicate = cb.like(root.get("standard"), likePattern);
            
            cq.select(root)
              .where(cb.or(codePredicate, descriptionPredicate, reasonPredicate, standardPredicate))
              .orderBy(cb.asc(root.get("code")));
            
            return session.createQuery(cq).getResultList();
            
        } catch (Exception e) {
            System.err.println("Erro ao filtrar ReasonTaxes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Busca razões fiscais por padrão (standard)
     */
    public List<ReasonTaxes> findByStandard(String standard) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ReasonTaxes> cq = cb.createQuery(ReasonTaxes.class);
            Root<ReasonTaxes> root = cq.from(ReasonTaxes.class);
            
            cq.select(root)
              .where(cb.equal(root.get("standard"), standard))
              .orderBy(cb.asc(root.get("code")));
            
            return session.createQuery(cq).getResultList();
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar ReasonTaxes por padrão: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Verifica se um código já existe
     */
    public boolean codeExists(String code) {
        return findByCode(code).isPresent();
    }
}