package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.ExpenseCategory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExpenseCategoryDao {

    private final Class<ExpenseCategory> entityClass = ExpenseCategory.class;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public Optional<ExpenseCategory> findById(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            ExpenseCategory entity = session.find(ExpenseCategory.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar ExpenseCategory por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<ExpenseCategory> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ExpenseCategory> cq = cb.createQuery(ExpenseCategory.class);
            Root<ExpenseCategory> root = cq.from(ExpenseCategory.class);
            cq.select(root).orderBy(cb.asc(root.get("name")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os ExpenseCategory: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ExpenseCategory save(ExpenseCategory category) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(category);
            tx.commit();

            System.out.println("✅ ExpenseCategory salvo: " + category.getName());
            return category;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar ExpenseCategory: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar ExpenseCategory", e);
        }
    }

    public ExpenseCategory update(ExpenseCategory category) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ExpenseCategory merged = session.merge(category);
            tx.commit();

            System.out.println("✅ ExpenseCategory atualizado: " + category.getName());
            return merged;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar ExpenseCategory: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar ExpenseCategory", e);
        }
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            ExpenseCategory category = session.find(ExpenseCategory.class, id);
            if (category != null) {
                session.remove(category);
            }

            tx.commit();
            System.out.println("✅ ExpenseCategory removido ID: " + id);

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover ExpenseCategory: " + e.getMessage());
            throw new RuntimeException("Erro ao remover ExpenseCategory", e);
        }
    }

    // ==========================================================
    // 🔹 Métodos específicos
    // ==========================================================
    public Optional<ExpenseCategory> findByName(String name) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ExpenseCategory> cq = cb.createQuery(ExpenseCategory.class);
            Root<ExpenseCategory> root = cq.from(ExpenseCategory.class);

            cq.select(root).where(cb.equal(root.get("name"), name));

            return session.createQuery(cq).uniqueResultOptional();

        } catch (Exception e) {
            System.err.println("Erro ao buscar ExpenseCategory por nome: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<ExpenseCategory> filter(String text) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ExpenseCategory> cq = cb.createQuery(ExpenseCategory.class);
            Root<ExpenseCategory> root = cq.from(ExpenseCategory.class);

            String likePattern = "%" + text + "%";

            Predicate namePredicate = cb.like(root.get("name"), likePattern);
            Predicate descriptionPredicate = cb.like(root.get("description"), likePattern);

            cq.select(root)
                    .where(cb.or(namePredicate, descriptionPredicate))
                    .orderBy(cb.asc(root.get("name")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar ExpenseCategory: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<ExpenseCategory> findByStatus(Integer status) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ExpenseCategory> cq = cb.createQuery(ExpenseCategory.class);
            Root<ExpenseCategory> root = cq.from(ExpenseCategory.class);

            cq.select(root)
                    .where(cb.equal(root.get("status"), status))
                    .orderBy(cb.asc(root.get("name")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar ExpenseCategory por status: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Verifica se já existe uma categoria com o mesmo nome
     */
    public boolean existsByName(String name) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<ExpenseCategory> root = cq.from(ExpenseCategory.class);

            cq.select(cb.count(root))
                    .where(cb.equal(root.get("name"), name));

            Long count = session.createQuery(cq).getSingleResult();
            return count > 0;

        } catch (Exception e) {
            System.err.println("Erro ao verificar existência de ExpenseCategory: " + e.getMessage());
            return false;
        }
    }
}
