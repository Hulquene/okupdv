package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.Expense;
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

public class ExpenseDao {

    private final Class<Expense> entityClass = Expense.class;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public Optional<Expense> findById(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            Expense entity = session.find(Expense.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar Expense por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Expense> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Expense> cq = cb.createQuery(Expense.class);
            Root<Expense> root = cq.from(Expense.class);
            cq.select(root).orderBy(cb.desc(root.get("date")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os Expenses: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Expense save(Expense expense) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(expense);
            tx.commit();

            System.out.println("✅ Expense salvo: " + expense.getDescription());
            return expense;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar Expense: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar Expense", e);
        }
    }

    public Expense update(Expense expense) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Expense merged = session.merge(expense);
            tx.commit();

            System.out.println("✅ Expense atualizado: " + expense.getDescription());
            return merged;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar Expense: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar Expense", e);
        }
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Expense expense = session.find(Expense.class, id);
            if (expense != null) {
                session.remove(expense);
            }

            tx.commit();
            System.out.println("✅ Expense removido ID: " + id);

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover Expense: " + e.getMessage());
            throw new RuntimeException("Erro ao remover Expense", e);
        }
    }

    // ==========================================================
    // 🔹 Métodos específicos
    // ==========================================================
    public List<Expense> filter(String text) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Expense> cq = cb.createQuery(Expense.class);
            Root<Expense> root = cq.from(Expense.class);

            String likePattern = "%" + text + "%";

            Predicate descriptionPredicate = cb.like(root.get("description"), likePattern);
            Predicate datePredicate = cb.like(root.get("date"), likePattern);
            Predicate referencePredicate = cb.like(root.get("reference"), likePattern);

            cq.select(root)
                    .where(cb.or(descriptionPredicate, datePredicate, referencePredicate))
                    .orderBy(cb.desc(root.get("date")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar Expenses: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Expense> filterByDate(LocalDate from, LocalDate to) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Expense> cq = cb.createQuery(Expense.class);
            Root<Expense> root = cq.from(Expense.class);

            cq.select(root)
                    .where(cb.between(root.get("date"), from.toString(), to.toString()))
                    .orderBy(cb.asc(root.get("date")), cb.asc(root.get("id")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar Expenses por data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Expense> findByCategory(Integer categoryId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Expense> cq = cb.createQuery(Expense.class);
            Root<Expense> root = cq.from(Expense.class);

            cq.select(root)
                    .where(cb.equal(root.get("category").get("id"), categoryId))
                    .orderBy(cb.desc(root.get("date")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Expenses por categoria: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Expense> findBySupplier(Integer supplierId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Expense> cq = cb.createQuery(Expense.class);
            Root<Expense> root = cq.from(Expense.class);

            cq.select(root)
                    .where(cb.equal(root.get("supplier").get("id"), supplierId))
                    .orderBy(cb.desc(root.get("date")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Expenses por fornecedor: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Expense> findByStatus(Integer status) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Expense> cq = cb.createQuery(Expense.class);
            Root<Expense> root = cq.from(Expense.class);

            cq.select(root)
                    .where(cb.equal(root.get("status"), status))
                    .orderBy(cb.desc(root.get("date")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Expenses por status: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Expense> findByPaymentMode(PaymentMode paymentMode) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Expense> cq = cb.createQuery(Expense.class);
            Root<Expense> root = cq.from(Expense.class);

            cq.select(root)
                    .where(cb.equal(root.get("mode"), paymentMode))
                    .orderBy(cb.desc(root.get("date")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Expenses por modo de pagamento: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Obtém o próximo número para despesa
     */
    public Integer getNextNumber() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
            Root<Expense> root = cq.from(Expense.class);

            cq.select(cb.coalesce(cb.max(root.get("number")), 0));

            Integer maxNumber = session.createQuery(cq).getSingleResult();
            return maxNumber + 1;

        } catch (Exception e) {
            System.err.println("Erro ao obter próximo número de despesa: " + e.getMessage());
            return 1;
        }
    }

    /**
     * Calcula o total de despesas por período
     */
    public Double calculateTotalByPeriod(LocalDate from, LocalDate to) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Double> cq = cb.createQuery(Double.class);
            Root<Expense> root = cq.from(Expense.class);

            cq.select(cb.sum(root.get("total")))
                    .where(cb.between(root.get("date"), from.toString(), to.toString()));

            Double total = session.createQuery(cq).getSingleResult();
            return total != null ? total : 0.0;

        } catch (Exception e) {
            System.err.println("Erro ao calcular total de despesas: " + e.getMessage());
            return 0.0;
        }
    }
}
