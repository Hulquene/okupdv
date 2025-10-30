package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.Payment;
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

public class PaymentDao {

    private final Class<Payment> entityClass = Payment.class;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public Optional<Payment> findById(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            Payment entity = session.find(Payment.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar Payment por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Payment> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Payment> cq = cb.createQuery(Payment.class);
            Root<Payment> root = cq.from(Payment.class);
            cq.select(root).orderBy(cb.desc(root.get("date")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os Payments: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Payment save(Payment payment) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(payment);
            tx.commit();

            System.out.println("✅ Payment salvo: " + payment.getReference());
            return payment;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar Payment: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar Payment", e);
        }
    }

    public Payment update(Payment payment) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Payment merged = session.merge(payment);
            tx.commit();

            System.out.println("✅ Payment atualizado: " + payment.getReference());
            return merged;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar Payment: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar Payment", e);
        }
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Payment payment = session.find(Payment.class, id);
            if (payment != null) {
                session.remove(payment);
            }

            tx.commit();
            System.out.println("✅ Payment removido ID: " + id);

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover Payment: " + e.getMessage());
            throw new RuntimeException("Erro ao remover Payment", e);
        }
    }

    // ==========================================================
    // 🔹 Métodos específicos
    // ==========================================================
    public Optional<Payment> findByReference(String reference) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Payment> cq = cb.createQuery(Payment.class);
            Root<Payment> root = cq.from(Payment.class);

            cq.select(root).where(cb.equal(root.get("reference"), reference));

            return session.createQuery(cq).uniqueResultOptional();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Payment por referência: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Payment> filter(String text) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Payment> cq = cb.createQuery(Payment.class);
            Root<Payment> root = cq.from(Payment.class);

            String likePattern = "%" + text + "%";

            Predicate descriptionPredicate = cb.like(root.get("description"), likePattern);
            Predicate datePredicate = cb.like(root.get("date"), likePattern);
            Predicate prefixPredicate = cb.like(root.get("prefix"), likePattern);
            Predicate referencePredicate = cb.like(root.get("reference"), likePattern);

            cq.select(root)
                    .where(cb.or(descriptionPredicate, datePredicate, prefixPredicate, referencePredicate))
                    .orderBy(cb.desc(root.get("date")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar Payments: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Payment> filterByDate(LocalDate from, LocalDate to) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Payment> cq = cb.createQuery(Payment.class);
            Root<Payment> root = cq.from(Payment.class);

            cq.select(root)
                    .where(cb.between(root.get("date"), from.toString(), to.toString()))
                    .orderBy(cb.asc(root.get("date")), cb.asc(root.get("id")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar Payments por data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Payment> findByInvoiceId(Integer invoiceId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Payment> cq = cb.createQuery(Payment.class);
            Root<Payment> root = cq.from(Payment.class);

            cq.select(root)
                    .where(cb.equal(root.get("invoiceId"), invoiceId))
                    .orderBy(cb.desc(root.get("date")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Payments por invoiceId: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Payment> findByStatus(Payment.PaymentStatus status) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Payment> cq = cb.createQuery(Payment.class);
            Root<Payment> root = cq.from(Payment.class);

            cq.select(root)
                    .where(cb.equal(root.get("status"), status))
                    .orderBy(cb.desc(root.get("date")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Payments por status: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Payment> findByPaymentMode(Payment.PaymentMode paymentMode) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Payment> cq = cb.createQuery(Payment.class);
            Root<Payment> root = cq.from(Payment.class);

            cq.select(root)
                    .where(cb.equal(root.get("paymentMode"), paymentMode))
                    .orderBy(cb.desc(root.get("date")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Payments por modo de pagamento: " + e.getMessage());
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
            Root<Payment> root = cq.from(Payment.class);

            cq.select(cb.sum(root.get("total")))
                    .where(cb.and(
                            cb.between(root.get("date"), from.toString(), to.toString()),
                            cb.equal(root.get("status"), Payment.PaymentStatus.SUCCESS)
                    ));

            Double total = session.createQuery(cq).getSingleResult();
            return total != null ? total : 0.0;

        } catch (Exception e) {
            System.err.println("Erro ao calcular total de pagamentos: " + e.getMessage());
            return 0.0;
        }
    }
}
