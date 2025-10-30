package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.Product;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDao {

    private final Class<Product> entityClass = Product.class;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public Optional<Product> findById(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            Product entity = session.find(Product.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar Product por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Product> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
            Root<Product> root = cq.from(Product.class);
            cq.select(root);

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os Products: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Product save(Product product) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(product);
            tx.commit();

            System.out.println("✅ Product salvo: " + product.getDescription());
            return product;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar Product: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar Product", e);
        }
    }

    public Product update(Product product) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Product merged = session.merge(product);
            tx.commit();

            System.out.println("✅ Product atualizado: " + product.getDescription());
            return merged;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar Product: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar Product", e);
        }
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Product product = session.find(Product.class, id);
            if (product != null) {
                session.remove(product);
            }

            tx.commit();
            System.out.println("✅ Product removido ID: " + id);

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover Product: " + e.getMessage());
            throw new RuntimeException("Erro ao remover Product", e);
        }
    }

    // ==========================================================
    // 🔹 MÉTODOS ESPECÍFICOS
    // ==========================================================
    public Optional<Product> findByBarcode(String barcode) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
            Root<Product> root = cq.from(Product.class);

            cq.select(root).where(cb.equal(root.get("barcode"), barcode));

            return session.createQuery(cq).uniqueResultOptional();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Product por barcode: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Product> findByDescription(String description) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
            Root<Product> root = cq.from(Product.class);

            cq.select(root).where(cb.equal(root.get("description"), description));

            return session.createQuery(cq).uniqueResultOptional();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Product por descrição: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Product> filter(String text) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
            Root<Product> root = cq.from(Product.class);

            String likePattern = "%" + text + "%";

            Predicate descriptionPredicate = cb.like(root.get("description"), likePattern);
            Predicate barcodePredicate = cb.like(root.get("barcode"), likePattern);
            Predicate codePredicate = cb.like(root.get("code"), likePattern);

            cq.select(root).where(cb.or(descriptionPredicate, barcodePredicate, codePredicate));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar Products: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Product> findActive() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
            Root<Product> root = cq.from(Product.class);

            cq.select(root).where(cb.equal(root.get("status"), 1));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Products ativos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Product> findByType(String type) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
            Root<Product> root = cq.from(Product.class);

            cq.select(root).where(cb.equal(root.get("type"), type));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Products por tipo: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Busca produtos para PDV (ativos, tipo produto, com stock) Nota: A lógica
     * de stock precisa ser implementada separadamente
     */
    public List<Product> findForPDV(String filter) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
            Root<Product> root = cq.from(Product.class);

            // Condições base
            Predicate statusPredicate = cb.equal(root.get("status"), 1);
            Predicate typePredicate = cb.equal(root.get("type"), "product");

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(statusPredicate);
            predicates.add(typePredicate);

            // Filtro opcional
            if (filter != null && !filter.trim().isEmpty()) {
                String likePattern = "%" + filter + "%";
                Predicate filterPredicate = cb.or(
                        cb.like(root.get("description"), likePattern),
                        cb.like(root.get("barcode"), likePattern),
                        cb.like(root.get("code"), likePattern)
                );
                predicates.add(filterPredicate);
            }

            cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Products para PDV: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Verifica se um código de barras já existe
     */
    public boolean barcodeExists(String barcode) {
        return findByBarcode(barcode).isPresent();
    }

    /**
     * Verifica se um código de produto já existe
     */
    public boolean codeExists(String code) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Product> root = cq.from(Product.class);

            cq.select(cb.count(root)).where(cb.equal(root.get("code"), code));

            Long count = session.createQuery(cq).getSingleResult();
            return count > 0;

        } catch (Exception e) {
            System.err.println("Erro ao verificar código: " + e.getMessage());
            return false;
        }
    }
}
