package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.StockMovement;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StockMovementDao {

    private final Class<StockMovement> entityClass = StockMovement.class;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public Optional<StockMovement> findById(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            StockMovement entity = session.find(StockMovement.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar StockMovement por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<StockMovement> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<StockMovement> cq = cb.createQuery(StockMovement.class);
            Root<StockMovement> root = cq.from(StockMovement.class);
            cq.select(root).orderBy(cb.desc(root.get("createdAt")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os StockMovements: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public StockMovement save(StockMovement stockMovement) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(stockMovement);
            tx.commit();

            System.out.println("✅ StockMovement salvo: " + stockMovement.getProduct().getDescription());

            // Se for entrada de compra, atualiza o item correspondente
            if ("COMPRA".equalsIgnoreCase(stockMovement.getOrigin())) {
                atualizarEntradaItemCompra(stockMovement);
            }

            return stockMovement;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar StockMovement: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar StockMovement", e);
        }
    }

    public StockMovement update(StockMovement stockMovement) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            StockMovement merged = session.merge(stockMovement);
            tx.commit();

            System.out.println("✅ StockMovement atualizado: " + stockMovement.getId());
            return merged;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar StockMovement: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar StockMovement", e);
        }
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            StockMovement stockMovement = session.find(StockMovement.class, id);
            if (stockMovement != null) {
                session.remove(stockMovement);
            }

            tx.commit();
            System.out.println("✅ StockMovement removido ID: " + id);

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover StockMovement: " + e.getMessage());
            throw new RuntimeException("Erro ao remover StockMovement", e);
        }
    }

    // ==========================================================
    // 🔹 CONSULTAS DE STOCK
    // ==========================================================
    /**
     * 🔹 MÉTODO ADICIONADO: Calcula o stock atual de um produto
     */
    public Integer getStockAtual(Integer productId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
            Root<StockMovement> root = cq.from(StockMovement.class);

            cq.select(cb.sum(root.get("quantity")))
                    .where(cb.equal(root.get("product").get("id"), productId));

            Integer stock = session.createQuery(cq).getSingleResult();
            return stock != null ? stock : 0;

        } catch (Exception e) {
            System.err.println("Erro ao calcular stock atual: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Método alternativo em inglês
     */
    public Integer getCurrentStock(Integer productId) {
        return getStockAtual(productId);
    }

    /**
     * Calcula apenas as entradas de stock
     */
    public Integer getTotalEntradas(Integer productId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
            Root<StockMovement> root = cq.from(StockMovement.class);

            cq.select(cb.sum(root.get("quantity")))
                    .where(cb.and(
                            cb.equal(root.get("product").get("id"), productId),
                            cb.gt(root.get("quantity"), 0)
                    ));

            Integer entradas = session.createQuery(cq).getSingleResult();
            return entradas != null ? entradas : 0;

        } catch (Exception e) {
            System.err.println("Erro ao calcular total de entradas: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Calcula apenas as saídas de stock
     */
    public Integer getTotalSaidas(Integer productId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
            Root<StockMovement> root = cq.from(StockMovement.class);

            cq.select(cb.sum(root.get("quantity")))
                    .where(cb.and(
                            cb.equal(root.get("product").get("id"), productId),
                            cb.lt(root.get("quantity"), 0)
                    ));

            Integer saidas = session.createQuery(cq).getSingleResult();
            return saidas != null ? saidas : 0;

        } catch (Exception e) {
            System.err.println("Erro ao calcular total de saídas: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Verifica se há stock suficiente
     */
    public boolean hasStockSuficiente(Integer productId, Integer quantidadeRequerida) {
        Integer stockAtual = getStockAtual(productId);
        return stockAtual >= quantidadeRequerida;
    }

    // ==========================================================
    // 🔹 OUTRAS CONSULTAS
    // ==========================================================
    public List<StockMovement> findByProductId(Integer productId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<StockMovement> cq = cb.createQuery(StockMovement.class);
            Root<StockMovement> root = cq.from(StockMovement.class);

            cq.select(root)
                    .where(cb.equal(root.get("product").get("id"), productId))
                    .orderBy(cb.desc(root.get("createdAt")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar StockMovements por produto: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<StockMovement> findByType(String type) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<StockMovement> cq = cb.createQuery(StockMovement.class);
            Root<StockMovement> root = cq.from(StockMovement.class);

            cq.select(root)
                    .where(cb.equal(root.get("type"), type))
                    .orderBy(cb.desc(root.get("createdAt")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar StockMovements por tipo: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<StockMovement> findByOrigin(String origin) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<StockMovement> cq = cb.createQuery(StockMovement.class);
            Root<StockMovement> root = cq.from(StockMovement.class);

            cq.select(root)
                    .where(cb.equal(root.get("origin"), origin))
                    .orderBy(cb.desc(root.get("createdAt")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar StockMovements por origem: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<StockMovement> filter(String text) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<StockMovement> cq = cb.createQuery(StockMovement.class);
            Root<StockMovement> root = cq.from(StockMovement.class);

            String likePattern = "%" + text + "%";

            Predicate notesPredicate = cb.like(root.get("notes"), likePattern);
            Predicate reasonPredicate = cb.like(root.get("reason"), likePattern);
            Predicate productPredicate = cb.like(root.get("product").get("description"), likePattern);

            cq.select(root)
                    .where(cb.or(notesPredicate, reasonPredicate, productPredicate))
                    .orderBy(cb.desc(root.get("createdAt")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar StockMovements: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<StockMovement> findByReferenceId(Integer referenceId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<StockMovement> cq = cb.createQuery(StockMovement.class);
            Root<StockMovement> root = cq.from(StockMovement.class);

            cq.select(root)
                    .where(cb.equal(root.get("referenceId"), referenceId))
                    .orderBy(cb.desc(root.get("createdAt")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar StockMovements por referência: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Atualiza entrada de item de compra (método auxiliar)
     */
    private void atualizarEntradaItemCompra(StockMovement movimento) {
        System.out.println("🔄 Atualização de entrada de compra para: "
                + movimento.getProduct().getDescription());
        // Implementação específica conforme sua estrutura de banco
    }
}
