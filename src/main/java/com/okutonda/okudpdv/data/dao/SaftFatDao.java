package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.SaftFat;
import com.okutonda.okudpdv.data.entities.User;
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

public class SaftFatDao {

    private final Class<SaftFat> entityClass = SaftFat.class;

    // ==========================================================
    // 🔹 INSERT E UPDATE
    // ==========================================================
    public SaftFat insertExport(LocalDate start, LocalDate end, String path, String status, String notes) {
        return insertExport(start, end, path, status, notes, null);
    }

    public SaftFat insertExport(LocalDate start, LocalDate end, String path, String status, String notes, User exportedBy) {
        SaftFat export = new SaftFat(start, end, path, status);
        export.setNotes(notes);
        export.setUser(exportedBy);

        return save(export);
    }

    public SaftFat save(SaftFat export) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(export);
            tx.commit();

            System.out.println("✅ Export SAF-T salvo: " + export.getPeriodStart() + " to " + export.getPeriodEnd());
            return export;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar export SAF-T: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar export SAF-T", e);
        }
    }

    public SaftFat update(SaftFat export) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            SaftFat merged = session.merge(export);
            tx.commit();

            System.out.println("✅ Export SAF-T atualizado: " + export.getId());
            return merged;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar export SAF-T: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar export SAF-T", e);
        }
    }

    // ==========================================================
    // 🔹 SELECTS
    // ==========================================================
    public List<SaftFat> getAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<SaftFat> cq = cb.createQuery(SaftFat.class);
            Root<SaftFat> root = cq.from(SaftFat.class);
            cq.select(root).orderBy(cb.desc(root.get("createdAt")), cb.desc(root.get("id")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os exports SAF-T: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Optional<SaftFat> findById(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            SaftFat entity = session.find(SaftFat.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar ExportSaftFat por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<SaftFat> filter(String text) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<SaftFat> cq = cb.createQuery(SaftFat.class);
            Root<SaftFat> root = cq.from(SaftFat.class);

            String likePattern = "%" + (text != null ? text.trim() : "") + "%";

            Predicate filePathPredicate = cb.like(root.get("filePath"), likePattern);
            Predicate statusPredicate = cb.like(root.get("status"), likePattern);
            Predicate notesPredicate = cb.like(root.get("notes"), likePattern);
            Predicate userPredicate = cb.like(root.get("user").get("name"), likePattern);

            cq.select(root)
                    .where(cb.or(filePathPredicate, statusPredicate, notesPredicate, userPredicate))
                    .orderBy(cb.desc(root.get("createdAt")), cb.desc(root.get("id")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar exports SAF-T: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<SaftFat> filterByCreatedAt(LocalDate from, LocalDate to) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<SaftFat> cq = cb.createQuery(SaftFat.class);
            Root<SaftFat> root = cq.from(SaftFat.class);

            cq.select(root)
                    .where(cb.between(root.get("createdAt").as(LocalDate.class), from, to))
                    .orderBy(cb.desc(root.get("createdAt")), cb.desc(root.get("id")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar exports SAF-T por created_at: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<SaftFat> filterByPeriod(LocalDate from, LocalDate to) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<SaftFat> cq = cb.createQuery(SaftFat.class);
            Root<SaftFat> root = cq.from(SaftFat.class);

            // WHERE period_start = ? AND period_end = ?
            Predicate startPredicate = cb.equal(root.get("periodStart"), from);
            Predicate endPredicate = cb.equal(root.get("periodEnd"), to);

            cq.select(root)
                    .where(cb.and(startPredicate, endPredicate))
                    .orderBy(cb.desc(root.get("createdAt")), cb.desc(root.get("id")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar exports SAF-T por período: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<SaftFat> findByUserId(Long userId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<SaftFat> cq = cb.createQuery(SaftFat.class);
            Root<SaftFat> root = cq.from(SaftFat.class);

            cq.select(root)
                    .where(cb.equal(root.get("user").get("id"), userId))
                    .orderBy(cb.desc(root.get("createdAt")), cb.desc(root.get("id")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar exports SAF-T por usuário: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<SaftFat> findByStatus(String status) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<SaftFat> cq = cb.createQuery(SaftFat.class);
            Root<SaftFat> root = cq.from(SaftFat.class);

            cq.select(root)
                    .where(cb.equal(root.get("status"), status))
                    .orderBy(cb.desc(root.get("createdAt")), cb.desc(root.get("id")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar exports SAF-T por status: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ==========================================================
    // 🔹 DELETE
    // ==========================================================
    public void delete(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            SaftFat export = session.find(SaftFat.class, id);
            if (export != null) {
                session.remove(export);
            }

            tx.commit();
            System.out.println("✅ Export SAF-T removido ID: " + id);

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover export SAF-T: " + e.getMessage());
            throw new RuntimeException("Erro ao remover export SAF-T", e);
        }
    }

    // ==========================================================
    // 🔹 MÉTODOS UTILITÁRIOS
    // ==========================================================
    /**
     * Verifica se já existe export para um período
     */
    public boolean existsForPeriod(LocalDate start, LocalDate end) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<SaftFat> root = cq.from(SaftFat.class);

            Predicate startPredicate = cb.equal(root.get("periodStart"), start);
            Predicate endPredicate = cb.equal(root.get("periodEnd"), end);

            cq.select(cb.count(root))
                    .where(cb.and(startPredicate, endPredicate));

            Long count = session.createQuery(cq).getSingleResult();
            return count > 0;

        } catch (Exception e) {
            System.err.println("Erro ao verificar existência de export para período: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca o último export realizado
     */
    public Optional<SaftFat> findLastExport() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<SaftFat> cq = cb.createQuery(SaftFat.class);
            Root<SaftFat> root = cq.from(SaftFat.class);

            cq.select(root).orderBy(cb.desc(root.get("createdAt")));

            return session.createQuery(cq).setMaxResults(1).uniqueResultOptional();

        } catch (Exception e) {
            System.err.println("Erro ao buscar último export SAF-T: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Conta exports por status
     */
    public Long countByStatus(String status) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<SaftFat> root = cq.from(SaftFat.class);

            cq.select(cb.count(root))
                    .where(cb.equal(root.get("status"), status));

            return session.createQuery(cq).getSingleResult();

        } catch (Exception e) {
            System.err.println("Erro ao contar exports por status: " + e.getMessage());
            return 0L;
        }
    }

    /**
     * Busca exports com período sobreposto
     */
    public List<SaftFat> findOverlappingExports(LocalDate start, LocalDate end) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<SaftFat> cq = cb.createQuery(SaftFat.class);
            Root<SaftFat> root = cq.from(SaftFat.class);

            // WHERE (period_start <= end) AND (period_end >= start)
            Predicate startOverlap = cb.lessThanOrEqualTo(root.get("periodStart"), end);
            Predicate endOverlap = cb.greaterThanOrEqualTo(root.get("periodEnd"), start);

            cq.select(root)
                    .where(cb.and(startOverlap, endOverlap))
                    .orderBy(cb.desc(root.get("periodStart")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar exports sobrepostos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
