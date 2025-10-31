package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.PurchaseItem;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PurchaseItemDao {

    private final Class<PurchaseItem> entityClass = PurchaseItem.class;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public Optional<PurchaseItem> findById(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            PurchaseItem entity = session.find(PurchaseItem.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar PurchaseItem por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<PurchaseItem> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<PurchaseItem> cq = cb.createQuery(PurchaseItem.class);
            Root<PurchaseItem> root = cq.from(PurchaseItem.class);
            cq.select(root).orderBy(cb.asc(root.get("id")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os PurchaseItems: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public PurchaseItem save(PurchaseItem item) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(item);
            tx.commit();

            System.out.println("✅ PurchaseItem salvo: ID " + item.getId());
            return item;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar PurchaseItem: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar PurchaseItem", e);
        }
    }

    public PurchaseItem update(PurchaseItem item) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            PurchaseItem merged = session.merge(item);
            tx.commit();

            System.out.println("✅ PurchaseItem atualizado: ID " + item.getId());
            return merged;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar PurchaseItem: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar PurchaseItem", e);
        }
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            PurchaseItem item = session.find(PurchaseItem.class, id);
            if (item != null) {
                session.remove(item);
            }

            tx.commit();
            System.out.println("✅ PurchaseItem removido ID: " + id);

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover PurchaseItem: " + e.getMessage());
            throw new RuntimeException("Erro ao remover PurchaseItem", e);
        }
    }

    // ==========================================================
    // 🔹 Métodos específicos
    // ==========================================================
    public List<PurchaseItem> findByPurchase(Integer purchaseId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<PurchaseItem> cq = cb.createQuery(PurchaseItem.class);
            Root<PurchaseItem> root = cq.from(PurchaseItem.class);

            cq.select(root)
                    .where(cb.equal(root.get("purchase").get("id"), purchaseId))
                    .orderBy(cb.asc(root.get("id")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar PurchaseItems por compra: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<PurchaseItem> findByProduct(Integer productId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<PurchaseItem> cq = cb.createQuery(PurchaseItem.class);
            Root<PurchaseItem> root = cq.from(PurchaseItem.class);

            cq.select(root)
                    .where(cb.equal(root.get("product").get("id"), productId))
                    .orderBy(cb.desc(root.get("purchase").get("dataCompra")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar PurchaseItems por produto: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<PurchaseItem> findByEntradaStatus(String status) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<PurchaseItem> cq = cb.createQuery(PurchaseItem.class);
            Root<PurchaseItem> root = cq.from(PurchaseItem.class);

            cq.select(root)
                    .where(cb.equal(root.get("entradaStatus"), status))
                    .orderBy(cb.asc(root.get("purchase").get("dataCompra")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar PurchaseItems por status de entrada: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<PurchaseItem> findPendingStockEntry() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<PurchaseItem> cq = cb.createQuery(PurchaseItem.class);
            Root<PurchaseItem> root = cq.from(PurchaseItem.class);

            Predicate pendingPredicate = cb.or(
                    cb.equal(root.get("entradaStatus"), "NAO_INICIADO"),
                    cb.and(
                            cb.equal(root.get("entradaStatus"), "PARCIAL"),
                            cb.gt(
                                    cb.diff(root.get("quantidade"), root.get("quantidadeEntrada")),
                                    0
                            )
                    )
            );

            cq.select(root)
                    .where(pendingPredicate)
                    .orderBy(cb.asc(root.get("purchase").get("dataCompra")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar PurchaseItems pendentes de entrada: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
