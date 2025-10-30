package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.GroupsProduct;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupsProductDao {

    private final Class<GroupsProduct> entityClass = GroupsProduct.class;

    // ========================
    // 🔹 CRUD
    // ========================
    
    public Optional<GroupsProduct> findById(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            GroupsProduct entity = session.find(GroupsProduct.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar GroupsProduct por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<GroupsProduct> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<GroupsProduct> cq = cb.createQuery(GroupsProduct.class);
            Root<GroupsProduct> root = cq.from(GroupsProduct.class);
            cq.select(root);
            
            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os GroupsProduct: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public GroupsProduct save(GroupsProduct group) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(group);
            tx.commit();
            
            System.out.println("✅ GroupsProduct salvo: " + group.getName());
            return group;
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar GroupsProduct: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar GroupsProduct", e);
        }
    }

    public GroupsProduct update(GroupsProduct group) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            GroupsProduct merged = session.merge(group);
            tx.commit();
            
            System.out.println("✅ GroupsProduct atualizado: " + group.getName());
            return merged;
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar GroupsProduct: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar GroupsProduct", e);
        }
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            GroupsProduct group = session.find(GroupsProduct.class, id);
            if (group != null) {
                session.remove(group);
            }
            
            tx.commit();
            System.out.println("✅ GroupsProduct removido ID: " + id);
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover GroupsProduct: " + e.getMessage());
            throw new RuntimeException("Erro ao remover GroupsProduct", e);
        }
    }

    // ========================
    // 🔹 Métodos específicos
    // ========================
    
    public Optional<GroupsProduct> findByCode(String code) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<GroupsProduct> cq = cb.createQuery(GroupsProduct.class);
            Root<GroupsProduct> root = cq.from(GroupsProduct.class);
            
            cq.select(root).where(cb.equal(root.get("code"), code));
            
            return session.createQuery(cq).uniqueResultOptional();
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar GroupsProduct por código: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<GroupsProduct> filter(String text) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<GroupsProduct> cq = cb.createQuery(GroupsProduct.class);
            Root<GroupsProduct> root = cq.from(GroupsProduct.class);
            
            String likePattern = "%" + text + "%";
            
            Predicate namePredicate = cb.like(root.get("name"), likePattern);
            Predicate codePredicate = cb.like(root.get("code"), likePattern);
            
            cq.select(root).where(cb.or(namePredicate, codePredicate));
            
            return session.createQuery(cq).getResultList();
            
        } catch (Exception e) {
            System.err.println("Erro ao filtrar GroupsProduct: " + e.getMessage());
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