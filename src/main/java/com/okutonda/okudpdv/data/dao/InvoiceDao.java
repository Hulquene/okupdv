package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.Invoices;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.criteria.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvoiceDao {

    private final Class<Invoices> entityClass = Invoices.class;

    // CRUD Básico
    public Optional<Invoices> findById(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            Invoices entity = session.find(Invoices.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar Invoice por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Invoices> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Invoices> cq = cb.createQuery(Invoices.class);
            Root<Invoices> root = cq.from(Invoices.class);
            cq.select(root).orderBy(cb.desc(root.get("issueDate")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar todas as Invoices: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Invoices save(Invoices invoice) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(invoice);
            tx.commit();
            System.out.println("✅ Invoice salva: " + invoice.getPrefix() + "/" + invoice.getNumber());
            return invoice;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            System.err.println("❌ Erro ao salvar Invoice: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar Invoice", e);
        }
    }

    public Invoices update(Invoices invoice) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Invoices merged = session.merge(invoice);
            tx.commit();
            System.out.println("✅ Invoice atualizada: " + invoice.getPrefix() + "/" + invoice.getNumber());
            return merged;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            System.err.println("❌ Erro ao atualizar Invoice: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar Invoice", e);
        }
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Invoices invoice = session.find(Invoices.class, id);
            if (invoice != null) {
                session.remove(invoice);
            }
            tx.commit();
            System.out.println("✅ Invoice removida ID: " + id);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            System.err.println("❌ Erro ao remover Invoice: " + e.getMessage());
            throw new RuntimeException("Erro ao remover Invoice", e);
        }
    }

    // Consultas específicas
    public List<Invoices> findByPrefix(String prefix) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Invoices> cq = cb.createQuery(Invoices.class);
            Root<Invoices> root = cq.from(Invoices.class);

            cq.select(root)
                    .where(cb.equal(root.get("prefix"), prefix))
                    .orderBy(cb.desc(root.get("issueDate")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar Invoices por prefixo: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Invoices> findByStatus(Integer status) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Invoices> cq = cb.createQuery(Invoices.class);
            Root<Invoices> root = cq.from(Invoices.class);

            cq.select(root)
                    .where(cb.equal(root.get("status"), status))
                    .orderBy(cb.desc(root.get("issueDate")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar Invoices por status: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Invoices> filterByDate(LocalDate from, LocalDate to) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Invoices> cq = cb.createQuery(Invoices.class);
            Root<Invoices> root = cq.from(Invoices.class);

            cq.select(root)
                    .where(cb.between(root.get("issueDate"), from.toString(), to.toString()))
                    .orderBy(cb.asc(root.get("issueDate")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar Invoices por data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Invoices> findByClientId(Integer clientId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Invoices> cq = cb.createQuery(Invoices.class);
            Root<Invoices> root = cq.from(Invoices.class);

            cq.select(root)
                    .where(cb.equal(root.get("client").get("id"), clientId))
                    .orderBy(cb.desc(root.get("issueDate")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar Invoices por cliente: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Invoices> findBySellerId(Integer sellerId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Invoices> cq = cb.createQuery(Invoices.class);
            Root<Invoices> root = cq.from(Invoices.class);

            cq.select(root)
                    .where(cb.equal(root.get("seller").get("id"), sellerId))
                    .orderBy(cb.desc(root.get("issueDate")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar Invoices por vendedor: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Integer getNextNumber(String prefix) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
            Root<Invoices> root = cq.from(Invoices.class);

            cq.select(cb.coalesce(cb.max(root.get("number")), 0))
                    .where(cb.equal(root.get("prefix"), prefix));

            Integer maxNumber = session.createQuery(cq).getSingleResult();
            return maxNumber + 1;
        } catch (Exception e) {
            System.err.println("❌ Erro ao obter próximo número: " + e.getMessage());
            return 1;
        }
    }

    public List<Invoices> findPendentes() {
        return findByStatus(1); // Pendentes
    }

    public List<Invoices> findEmitidas() {
        return findByStatus(2); // Emitidas
    }

    public List<Invoices> findPagas() {
        return findByStatus(3); // Pagas
    }

    public List<Invoices> findAnuladas() {
        return findByStatus(4); // Anuladas
    }

    public List<Invoices> findComVencimentoProximo(LocalDate data) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Invoices> cq = cb.createQuery(Invoices.class);
            Root<Invoices> root = cq.from(Invoices.class);

            cq.select(root)
                    .where(cb.and(
                            cb.lessThanOrEqualTo(root.get("dueDate"), data.toString()),
                            cb.equal(root.get("status"), 2) // Apenas emitidas
                    ))
                    .orderBy(cb.asc(root.get("dueDate")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar Invoices com vencimento próximo: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Invoices> filter(String texto) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Invoices> cq = cb.createQuery(Invoices.class);
            Root<Invoices> root = cq.from(Invoices.class);

            String likePattern = "%" + texto + "%";

            Predicate prefixPredicate = cb.like(root.get("prefix"), likePattern);
            Predicate notePredicate = cb.like(root.get("note"), likePattern);
            Predicate clientPredicate = cb.like(root.get("client").get("name"), likePattern);

            cq.select(root)
                    .where(cb.or(prefixPredicate, notePredicate, clientPredicate))
                    .orderBy(cb.desc(root.get("issueDate")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar Invoices: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Double calculateTotalSalesByPeriod(LocalDate from, LocalDate to) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Double> cq = cb.createQuery(Double.class);
            Root<Invoices> root = cq.from(Invoices.class);

            cq.select(cb.sum(root.get("total")))
                    .where(cb.and(
                            cb.between(root.get("issueDate"), from.toString(), to.toString()),
                            cb.equal(root.get("status"), 3) // Status de invoice paga
                    ));

            Double total = session.createQuery(cq).getSingleResult();
            return total != null ? total : 0.0;
        } catch (Exception e) {
            System.err.println("❌ Erro ao calcular total de vendas: " + e.getMessage());
            return 0.0;
        }
    }

    public Long countByStatus(Integer status) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Invoices> root = cq.from(Invoices.class);

            cq.select(cb.count(root))
                    .where(cb.equal(root.get("status"), status));

            return session.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            System.err.println("❌ Erro ao contar invoices por status: " + e.getMessage());
            return 0L;
        }
    }
}