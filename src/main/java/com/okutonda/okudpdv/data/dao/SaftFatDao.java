package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.ExportSaftFat;
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

    private final Class<ExportSaftFat> entityClass = ExportSaftFat.class;

    // ==========================================================
    // 🔹 INSERT
    // ==========================================================
    public ExportSaftFat insertExport(LocalDate start, LocalDate end, String path, String status, String notes) {
        return insertExport(start, end, path, status, notes, null);
    }

    public ExportSaftFat insertExport(LocalDate start, LocalDate end, String path, String status, String notes, User exportedBy) {
        ExportSaftFat export = new ExportSaftFat(start, end, path, status);
        export.setNotes(notes);
        export.setUser(exportedBy);

        return save(export);
    }

    public ExportSaftFat save(ExportSaftFat export) {
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

    public ExportSaftFat update(ExportSaftFat export) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ExportSaftFat merged = session.merge(export);
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
    public List<ExportSaftFat> getAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ExportSaftFat> cq = cb.createQuery(ExportSaftFat.class);
            Root<ExportSaftFat> root = cq.from(ExportSaftFat.class);
            cq.select(root).orderBy(cb.desc(root.get("createdAt")), cb.desc(root.get("id")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os exports SAF-T: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Optional<ExportSaftFat> findById(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            ExportSaftFat entity = session.find(ExportSaftFat.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar ExportSaftFat por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<ExportSaftFat> filter(String text) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ExportSaftFat> cq = cb.createQuery(ExportSaftFat.class);
            Root<ExportSaftFat> root = cq.from(ExportSaftFat.class);

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

    public List<ExportSaftFat> filterByCreatedAt(LocalDate from, LocalDate to) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ExportSaftFat> cq = cb.createQuery(ExportSaftFat.class);
            Root<ExportSaftFat> root = cq.from(ExportSaftFat.class);

            cq.select(root)
                    .where(cb.between(root.get("createdAt").as(LocalDate.class), from, to))
                    .orderBy(cb.desc(root.get("createdAt")), cb.desc(root.get("id")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar exports SAF-T por created_at: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<ExportSaftFat> filterByPeriod(LocalDate from, LocalDate to) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ExportSaftFat> cq = cb.createQuery(ExportSaftFat.class);
            Root<ExportSaftFat> root = cq.from(ExportSaftFat.class);

            // WHERE period_start <= ? AND period_end >= ?
            Predicate startPredicate = cb.lessThanOrEqualTo(root.get("periodStart"), to);
            Predicate endPredicate = cb.greaterThanOrEqualTo(root.get("periodEnd"), from);

            cq.select(root)
                    .where(cb.and(startPredicate, endPredicate))
                    .orderBy(cb.desc(root.get("periodStart")), cb.desc(root.get("id")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar exports SAF-T por período: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<ExportSaftFat> findByUserId(Long userId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ExportSaftFat> cq = cb.createQuery(ExportSaftFat.class);
            Root<ExportSaftFat> root = cq.from(ExportSaftFat.class);

            cq.select(root)
                    .where(cb.equal(root.get("user").get("id"), userId))
                    .orderBy(cb.desc(root.get("createdAt")), cb.desc(root.get("id")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar exports SAF-T por usuário: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<ExportSaftFat> findByStatus(String status) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ExportSaftFat> cq = cb.createQuery(ExportSaftFat.class);
            Root<ExportSaftFat> root = cq.from(ExportSaftFat.class);

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

            ExportSaftFat export = session.find(ExportSaftFat.class, id);
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
            Root<ExportSaftFat> root = cq.from(ExportSaftFat.class);

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
    public Optional<ExportSaftFat> findLastExport() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ExportSaftFat> cq = cb.createQuery(ExportSaftFat.class);
            Root<ExportSaftFat> root = cq.from(ExportSaftFat.class);

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
            Root<ExportSaftFat> root = cq.from(ExportSaftFat.class);

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
    public List<ExportSaftFat> findOverlappingExports(LocalDate start, LocalDate end) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ExportSaftFat> cq = cb.createQuery(ExportSaftFat.class);
            Root<ExportSaftFat> root = cq.from(ExportSaftFat.class);

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
