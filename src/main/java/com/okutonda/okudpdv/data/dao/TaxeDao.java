package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.Taxes;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaxeDao {

    private final Class<Taxes> entityClass = Taxes.class;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    
    public Optional<Taxes> findById(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            Taxes entity = session.find(Taxes.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar Taxes por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Taxes> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Taxes> cq = cb.createQuery(Taxes.class);
            Root<Taxes> root = cq.from(Taxes.class);
            cq.select(root).orderBy(cb.asc(root.get("name")));
            
            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os Taxes: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Taxes save(Taxes tax) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(tax);
            tx.commit();
            
            System.out.println("✅ Taxes salvo: " + tax.getName());
            return tax;
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar Taxes: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar Taxes", e);
        }
    }

    public Taxes update(Taxes tax) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Taxes merged = session.merge(tax);
            tx.commit();
            
            System.out.println("✅ Taxes atualizado: " + tax.getName());
            return merged;
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar Taxes: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar Taxes", e);
        }
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            Taxes tax = session.find(Taxes.class, id);
            if (tax != null) {
                session.remove(tax);
            }
            
            tx.commit();
            System.out.println("✅ Taxes removido ID: " + id);
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover Taxes: " + e.getMessage());
            throw new RuntimeException("Erro ao remover Taxes", e);
        }
    }

    // ==========================================================
    // 🔹 Consultas personalizadas
    // ==========================================================
    
    public Optional<Taxes> findByCode(String code) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Taxes> cq = cb.createQuery(Taxes.class);
            Root<Taxes> root = cq.from(Taxes.class);
            
            cq.select(root).where(cb.equal(root.get("code"), code));
            
            return session.createQuery(cq).uniqueResultOptional();
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar Taxes por código: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Taxes> filter(String text) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Taxes> cq = cb.createQuery(Taxes.class);
            Root<Taxes> root = cq.from(Taxes.class);
            
            String likePattern = "%" + text + "%";
            
            Predicate namePredicate = cb.like(root.get("name"), likePattern);
            Predicate codePredicate = cb.like(root.get("code"), likePattern);
            Predicate percentagePredicate = cb.like(root.get("percentage").as(String.class), likePattern);
            
            cq.select(root)
              .where(cb.or(namePredicate, codePredicate, percentagePredicate))
              .orderBy(cb.asc(root.get("name")));
            
            return session.createQuery(cq).getResultList();
            
        } catch (Exception e) {
            System.err.println("Erro ao filtrar Taxes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Optional<Taxes> findDefaultTax() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Taxes> cq = cb.createQuery(Taxes.class);
            Root<Taxes> root = cq.from(Taxes.class);
            
            cq.select(root).where(cb.equal(root.get("isDefault"), 1));
            
            return session.createQuery(cq).uniqueResultOptional();
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar Tax padrão: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Define um imposto como padrão e remove o padrão anterior
     */
    public boolean setDefaultTax(Integer taxId) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            // Remove padrão atual
            session.createMutationQuery("UPDATE Taxes SET isDefault = 0 WHERE isDefault = 1")
                  .executeUpdate();
            
            // Define novo padrão
            session.createMutationQuery("UPDATE Taxes SET isDefault = 1 WHERE id = :id")
                  .setParameter("id", taxId)
                  .executeUpdate();
            
            tx.commit();
            System.out.println("✅ Tax padrão definido: " + taxId);
            return true;
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao definir tax padrão: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica se um código já existe
     */
    public boolean codeExists(String code) {
        return findByCode(code).isPresent();
    }

    /**
     * Busca impostos ativos (status implícito - todos são considerados ativos)
     */
    public List<Taxes> findActive() {
        return findAll(); // Todos os impostos são considerados ativos
    }
}