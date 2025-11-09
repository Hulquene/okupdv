package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.Order;
import com.okutonda.okudpdv.data.entities.PaymentStatus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class OrderDao {

    private final Class<Order> entityClass = Order.class;

    // ==========================================================
    // 🔹 CRUD PADRÃO
    // ==========================================================
    public Optional<Order> findById(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            Order entity = session.find(Order.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar Order por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Order> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Order> cq = cb.createQuery(Order.class);
            Root<Order> root = cq.from(Order.class);
            cq.select(root).orderBy(cb.desc(root.get("datecreate")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os Orders: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Order save(Order order) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        System.out.println("ID order:" + order);
        try {
            tx = session.beginTransaction();
            session.persist(order);
            tx.commit();

            System.out.println("✅ Order salvo: " + order.getPrefix() + "/" + order.getNumber());
            return order;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar Order: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar Order", e);
        }
    }

    public Order update(Order order) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Order merged = session.merge(order);
            tx.commit();

            System.out.println("✅ Order atualizado: " + order.getPrefix() + "/" + order.getNumber());
            return merged;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar Order: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar Order", e);
        }
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Order order = session.find(Order.class, id);
            if (order != null) {
                session.remove(order);
            }

            tx.commit();
            System.out.println("✅ Order removido ID: " + id);

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover Order: " + e.getMessage());
            throw new RuntimeException("Erro ao remover Order", e);
        }
    }

    // ==========================================================
    // 🔹 CONSULTAS CUSTOM
    // ==========================================================
    public Optional<Order> findByNumber(Integer number) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Order> cq = cb.createQuery(Order.class);
            Root<Order> root = cq.from(Order.class);

            cq.select(root).where(cb.equal(root.get("number"), number));

            return session.createQuery(cq).uniqueResultOptional();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Order por número: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Order> filter(String text) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Order> cq = cb.createQuery(Order.class);
            Root<Order> root = cq.from(Order.class);

            String likePattern = "%" + text + "%";

            Predicate prefixPredicate = cb.like(root.get("prefix"), likePattern);
            Predicate notePredicate = cb.like(root.get("note"), likePattern);
            Predicate clientPredicate = cb.like(root.get("client").get("name"), likePattern);

            cq.select(root)
                    .where(cb.or(prefixPredicate, notePredicate, clientPredicate))
                    .orderBy(cb.desc(root.get("datecreate")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar Orders: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Order> filterByDate(LocalDate from, LocalDate to) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Order> cq = cb.createQuery(Order.class);
            Root<Order> root = cq.from(Order.class);

            cq.select(root)
                    .where(cb.between(root.get("datecreate"), from.toString(), to.toString()))
                    .orderBy(cb.asc(root.get("datecreate")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar Orders por data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Order> findByClientId(Integer clientId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Order> cq = cb.createQuery(Order.class);
            Root<Order> root = cq.from(Order.class);

            cq.select(root)
                    .where(cb.equal(root.get("client").get("id"), clientId))
                    .orderBy(cb.desc(root.get("datecreate")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Orders por cliente: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Order> findBySellerId(Integer sellerId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Order> cq = cb.createQuery(Order.class);
            Root<Order> root = cq.from(Order.class);

            cq.select(root)
                    .where(cb.equal(root.get("seller").get("id"), sellerId))
                    .orderBy(cb.desc(root.get("datecreate")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Orders por vendedor: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Order> findByStatus(Integer status) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Order> cq = cb.createQuery(Order.class);
            Root<Order> root = cq.from(Order.class);

            cq.select(root)
                    .where(cb.equal(root.get("status"), status))
                    .orderBy(cb.desc(root.get("datecreate")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Orders por status: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Obtém o próximo número de ordem
     */
    public Integer getNextNumber() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
            Root<Order> root = cq.from(Order.class);

            cq.select(cb.coalesce(cb.max(root.get("number")), 0));

            Integer maxNumber = session.createQuery(cq).getSingleResult();
            return maxNumber + 1;

        } catch (Exception e) {
            System.err.println("Erro ao obter próximo número: " + e.getMessage());
            return 1;
        }
    }

    /**
     * Calcula o total de vendas em um período
     */
    public BigDecimal calculateTotalSalesByPeriod(LocalDate from, LocalDate to) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
            Root<Order> root = cq.from(Order.class);

            cq.select(cb.sum(root.get("total")))
                    .where(cb.and(
                            cb.between(root.get("datecreate"), from.toString(), to.toString()),
                            cb.equal(root.get("status"), PaymentStatus.PAGO) // Usar enum diretamente
                    ));

            BigDecimal total = session.createQuery(cq).getSingleResult();
            return total != null ? total : BigDecimal.ZERO;

        } catch (Exception e) {
            System.err.println("Erro ao calcular total de vendas: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    /**
     * Conta pedidos por status
     */
    public Long countByStatus(PaymentStatus status) { // Mudar para PaymentStatus
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Order> root = cq.from(Order.class);

            cq.select(cb.count(root))
                    .where(cb.equal(root.get("status"), status)); // Usar enum

            return session.createQuery(cq).getSingleResult();

        } catch (Exception e) {
            System.err.println("Erro ao contar pedidos por status: " + e.getMessage());
            return 0L;
        }
    }

    public <T> T executeInTransaction(Function<Session, T> function) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            T result = function.apply(session);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Erro na transação", e);
        }
    }
}
