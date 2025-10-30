package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.ProductOrder;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductOrderDao {

    private final Class<ProductOrder> entityClass = ProductOrder.class;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    
    public Optional<ProductOrder> findById(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            ProductOrder entity = session.find(ProductOrder.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar ProductOrder por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<ProductOrder> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ProductOrder> cq = cb.createQuery(ProductOrder.class);
            Root<ProductOrder> root = cq.from(ProductOrder.class);
            cq.select(root);
            
            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os ProductOrders: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ProductOrder save(ProductOrder productOrder) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(productOrder);
            tx.commit();
            
            System.out.println("✅ ProductOrder salvo para order: " + productOrder.getOrderId());
            return productOrder;
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar ProductOrder: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar ProductOrder", e);
        }
    }

    public ProductOrder update(ProductOrder productOrder) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ProductOrder merged = session.merge(productOrder);
            tx.commit();
            
            System.out.println("✅ ProductOrder atualizado: " + productOrder.getId());
            return merged;
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar ProductOrder: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar ProductOrder", e);
        }
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            ProductOrder productOrder = session.find(ProductOrder.class, id);
            if (productOrder != null) {
                session.remove(productOrder);
            }
            
            tx.commit();
            System.out.println("✅ ProductOrder removido ID: " + id);
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover ProductOrder: " + e.getMessage());
            throw new RuntimeException("Erro ao remover ProductOrder", e);
        }
    }

    // ==========================================================
    // 🔹 CONSULTAS ESPECÍFICAS
    // ==========================================================
    
    public List<ProductOrder> findByOrderId(Integer orderId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ProductOrder> cq = cb.createQuery(ProductOrder.class);
            Root<ProductOrder> root = cq.from(ProductOrder.class);
            
            cq.select(root).where(cb.equal(root.get("orderId"), orderId));
            
            return session.createQuery(cq).getResultList();
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar ProductOrders por orderId: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<ProductOrder> filter(String text) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ProductOrder> cq = cb.createQuery(ProductOrder.class);
            Root<ProductOrder> root = cq.from(ProductOrder.class);
            
            String likePattern = "%" + text + "%";
            
            cq.select(root).where(cb.like(root.get("description"), likePattern));
            
            return session.createQuery(cq).getResultList();
            
        } catch (Exception e) {
            System.err.println("Erro ao filtrar ProductOrders: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Salva múltiplos ProductOrders em lote (para melhor performance)
     */
    public void saveBatch(List<ProductOrder> productOrders) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            for (int i = 0; i < productOrders.size(); i++) {
                session.persist(productOrders.get(i));
                
                // Flush a cada 20 registros para evitar memory overflow
                if (i % 20 == 0) {
                    session.flush();
                    session.clear();
                }
            }
            
            tx.commit();
            System.out.println("✅ " + productOrders.size() + " ProductOrders salvos em lote");
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar ProductOrders em lote: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar ProductOrders em lote", e);
        }
    }

    /**
     * Remove todos os ProductOrders de um pedido
     */
    public boolean deleteByOrderId(Integer orderId) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            int deleted = session.createMutationQuery(
                "DELETE FROM ProductOrder WHERE orderId = :orderId")
                .setParameter("orderId", orderId)
                .executeUpdate();
            
            tx.commit();
            System.out.println("✅ " + deleted + " ProductOrders removidos do order: " + orderId);
            return deleted > 0;
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover ProductOrders por orderId: " + e.getMessage());
            return false;
        }
    }

    /**
     * Calcula o total de um pedido
     */
    public Double calculateOrderTotal(Integer orderId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            // Usando HQL para calcular o total
            String hql = """
                SELECT SUM(po.qty * po.price) 
                FROM ProductOrder po 
                WHERE po.orderId = :orderId
            """;
            
            Double total = session.createQuery(hql, Double.class)
                .setParameter("orderId", orderId)
                .uniqueResult();
            
            return total != null ? total : 0.0;
            
        } catch (Exception e) {
            System.err.println("Erro ao calcular total do pedido: " + e.getMessage());
            return 0.0;
        }
    }
}