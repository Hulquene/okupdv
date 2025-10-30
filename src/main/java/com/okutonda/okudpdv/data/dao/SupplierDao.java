package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.Supplier;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SupplierDao {

    private final Class<Supplier> entityClass = Supplier.class;

    // ==========================================================
    // 🔹 CRUD Básico
    // ==========================================================
    
    public Optional<Supplier> findById(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            Supplier entity = session.find(Supplier.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar Supplier por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Supplier> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Supplier> cq = cb.createQuery(Supplier.class);
            Root<Supplier> root = cq.from(Supplier.class);
            cq.select(root).orderBy(cb.asc(root.get("name")));
            
            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os Suppliers: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Supplier save(Supplier supplier) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(supplier);
            tx.commit();
            
            System.out.println("✅ Supplier salvo: " + supplier.getName());
            return supplier;
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar Supplier: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar Supplier", e);
        }
    }

    public Supplier update(Supplier supplier) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Supplier merged = session.merge(supplier);
            tx.commit();
            
            System.out.println("✅ Supplier atualizado: " + supplier.getName());
            return merged;
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar Supplier: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar Supplier", e);
        }
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            Supplier supplier = session.find(Supplier.class, id);
            if (supplier != null) {
                session.remove(supplier);
            }
            
            tx.commit();
            System.out.println("✅ Supplier removido ID: " + id);
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover Supplier: " + e.getMessage());
            throw new RuntimeException("Erro ao remover Supplier", e);
        }
    }

    // ==========================================================
    // 🔹 Consultas adicionais
    // ==========================================================
    
    public Optional<Supplier> findByName(String name) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Supplier> cq = cb.createQuery(Supplier.class);
            Root<Supplier> root = cq.from(Supplier.class);
            
            cq.select(root).where(cb.equal(root.get("name"), name));
            
            return session.createQuery(cq).uniqueResultOptional();
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar Supplier por nome: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Supplier> findByNif(String nif) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Supplier> cq = cb.createQuery(Supplier.class);
            Root<Supplier> root = cq.from(Supplier.class);
            
            cq.select(root).where(cb.equal(root.get("nif"), nif));
            
            return session.createQuery(cq).uniqueResultOptional();
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar Supplier por NIF: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Supplier> filter(String text) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Supplier> cq = cb.createQuery(Supplier.class);
            Root<Supplier> root = cq.from(Supplier.class);
            
            String likePattern = "%" + text + "%";
            
            Predicate namePredicate = cb.like(root.get("name"), likePattern);
            Predicate nifPredicate = cb.like(root.get("nif"), likePattern);
            Predicate cityPredicate = cb.like(root.get("city"), likePattern);
            Predicate emailPredicate = cb.like(root.get("email"), likePattern);
            
            cq.select(root)
              .where(cb.or(namePredicate, nifPredicate, cityPredicate, emailPredicate))
              .orderBy(cb.asc(root.get("name")));
            
            return session.createQuery(cq).getResultList();
            
        } catch (Exception e) {
            System.err.println("Erro ao filtrar Suppliers: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean existsByNif(String nif) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Supplier> root = cq.from(Supplier.class);
            
            cq.select(cb.count(root))
              .where(cb.equal(root.get("nif"), nif));
            
            Long count = session.createQuery(cq).getSingleResult();
            return count > 0;
            
        } catch (Exception e) {
            System.err.println("Erro ao verificar NIF: " + e.getMessage());
            return false;
        }
    }

    public List<Supplier> findActive() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Supplier> cq = cb.createQuery(Supplier.class);
            Root<Supplier> root = cq.from(Supplier.class);
            
            cq.select(root)
              .where(cb.equal(root.get("status"), 1))
              .orderBy(cb.asc(root.get("name")));
            
            return session.createQuery(cq).getResultList();
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar Suppliers ativos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Busca suppliers por grupo
     */
    public List<Supplier> findByGroup(Integer groupId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Supplier> cq = cb.createQuery(Supplier.class);
            Root<Supplier> root = cq.from(Supplier.class);
            
            cq.select(root)
              .where(cb.equal(root.get("groupId"), groupId))
              .orderBy(cb.asc(root.get("name")));
            
            return session.createQuery(cq).getResultList();
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar Suppliers por grupo: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}