package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.PurchasePayment;
import com.okutonda.okudpdv.data.entities.PaymentMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PurchasePaymentDao {

    private final Class<PurchasePayment> entityClass = PurchasePayment.class;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public Optional<PurchasePayment> findById(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            PurchasePayment entity = session.find(PurchasePayment.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar PurchasePayment por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<PurchasePayment> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<PurchasePayment> cq = cb.createQuery(PurchasePayment.class);
            Root<PurchasePayment> root = cq.from(PurchasePayment.class);
            cq.select(root).orderBy(cb.desc(root.get("dataPagamento")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os PurchasePayments: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public PurchasePayment save(PurchasePayment payment) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(payment);
            tx.commit();

            System.out.println("✅ PurchasePayment salvo: " + payment.getReferencia());
            return payment;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar PurchasePayment: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar PurchasePayment", e);
        }
    }

    public PurchasePayment update(PurchasePayment payment) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            PurchasePayment merged = session.merge(payment);
            tx.commit();

            System.out.println("✅ PurchasePayment atualizado: " + payment.getReferencia());
            return merged;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar PurchasePayment: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar PurchasePayment", e);
        }
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            PurchasePayment payment = session.find(PurchasePayment.class, id);
            if (payment != null) {
                session.remove(payment);
            }

            tx.commit();
            System.out.println("✅ PurchasePayment removido ID: " + id);

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover PurchasePayment: " + e.getMessage());
            throw new RuntimeException("Erro ao remover PurchasePayment", e);
        }
    }

    // ==========================================================
    // 🔹 Métodos específicos
    // ==========================================================
    public List<PurchasePayment> findByPurchase(Integer purchaseId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<PurchasePayment> cq = cb.createQuery(PurchasePayment.class);
            Root<PurchasePayment> root = cq.from(PurchasePayment.class);

            cq.select(root)
                    .where(cb.equal(root.get("purchase").get("id"), purchaseId))
                    .orderBy(cb.asc(root.get("dataPagamento")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar PurchasePayments por compra: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<PurchasePayment> filterByDate(LocalDate from, LocalDate to) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<PurchasePayment> cq = cb.createQuery(PurchasePayment.class);
            Root<PurchasePayment> root = cq.from(PurchasePayment.class);

            cq.select(root)
                    .where(cb.between(root.get("dataPagamento"),
                            java.sql.Date.valueOf(from),
                            java.sql.Date.valueOf(to)))
                    .orderBy(cb.asc(root.get("dataPagamento")), cb.asc(root.get("id")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar PurchasePayments por data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<PurchasePayment> findByPaymentMode(PaymentMode paymentMode) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<PurchasePayment> cq = cb.createQuery(PurchasePayment.class);
            Root<PurchasePayment> root = cq.from(PurchasePayment.class);

            cq.select(root)
                    .where(cb.equal(root.get("metodo"), paymentMode))
                    .orderBy(cb.desc(root.get("dataPagamento")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar PurchasePayments por modo de pagamento: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Calcula o total de pagamentos por período
     */
    public Double calculateTotalByPeriod(LocalDate from, LocalDate to) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Double> cq = cb.createQuery(Double.class);
            Root<PurchasePayment> root = cq.from(PurchasePayment.class);

            cq.select(cb.sum(root.get("valorPago")))
                    .where(cb.between(root.get("dataPagamento"),
                            java.sql.Date.valueOf(from),
                            java.sql.Date.valueOf(to)));

            Double total = session.createQuery(cq).getSingleResult();
            return total != null ? total : 0.0;

        } catch (Exception e) {
            System.err.println("Erro ao calcular total de pagamentos: " + e.getMessage());
            return 0.0;
        }
    }
}
