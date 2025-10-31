package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.Shift;
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

public class ShiftDao {

    private final Class<Shift> entityClass = Shift.class;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public Optional<Shift> findById(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            Shift entity = session.find(Shift.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar Shift por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Shift> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Shift> cq = cb.createQuery(Shift.class);
            Root<Shift> root = cq.from(Shift.class);
            cq.select(root).orderBy(cb.desc(root.get("dateOpen")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os Shifts: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Shift save(Shift shift) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(shift);
            tx.commit();

            System.out.println("✅ Shift salvo: " + shift.getCode());
            return shift;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar Shift: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar Shift", e);
        }
    }

    public Shift update(Shift shift) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Shift merged = session.merge(shift);
            tx.commit();

            System.out.println("✅ Shift atualizado: " + shift.getCode());
            return merged;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar Shift: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar Shift", e);
        }
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Shift shift = session.find(Shift.class, id);
            if (shift != null) {
                session.remove(shift);
            }

            tx.commit();
            System.out.println("✅ Shift removido ID: " + id);

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover Shift: " + e.getMessage());
            throw new RuntimeException("Erro ao remover Shift", e);
        }
    }

    // ==========================================================
    // 🔹 Métodos específicos
    // ==========================================================
    public Optional<Shift> findByCode(String code) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Shift> cq = cb.createQuery(Shift.class);
            Root<Shift> root = cq.from(Shift.class);

            cq.select(root).where(cb.equal(root.get("code"), code));

            return session.createQuery(cq).uniqueResultOptional();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Shift por código: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Shift> findByHash(String hash) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Shift> cq = cb.createQuery(Shift.class);
            Root<Shift> root = cq.from(Shift.class);

            cq.select(root).where(cb.equal(root.get("hash"), hash));

            return session.createQuery(cq).uniqueResultOptional();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Shift por hash: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Shift> findLastOpenShiftByUser(Integer userId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Shift> cq = cb.createQuery(Shift.class);
            Root<Shift> root = cq.from(Shift.class);

            Predicate userPredicate = cb.equal(root.get("user").get("id"), userId);
            Predicate statusPredicate = cb.equal(root.get("status"), "open");

            cq.select(root)
                    .where(cb.and(userPredicate, statusPredicate))
                    .orderBy(cb.desc(root.get("dateOpen")));

            return session.createQuery(cq).setMaxResults(1).uniqueResultOptional();

        } catch (Exception e) {
            System.err.println("Erro ao buscar último Shift aberto do usuário: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Shift> filter(String text) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Shift> cq = cb.createQuery(Shift.class);
            Root<Shift> root = cq.from(Shift.class);

            String likePattern = "%" + text + "%";

            Predicate codePredicate = cb.like(root.get("code"), likePattern);
            Predicate statusPredicate = cb.like(root.get("status"), likePattern);
            Predicate userPredicate = cb.like(root.get("user").get("name"), likePattern);

            cq.select(root)
                    .where(cb.or(codePredicate, statusPredicate, userPredicate))
                    .orderBy(cb.desc(root.get("dateOpen")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar Shifts: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Shift> findByUser(Integer userId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Shift> cq = cb.createQuery(Shift.class);
            Root<Shift> root = cq.from(Shift.class);

            cq.select(root)
                    .where(cb.equal(root.get("user").get("id"), userId))
                    .orderBy(cb.desc(root.get("dateOpen")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Shifts por usuário: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Shift> findByStatus(String status) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Shift> cq = cb.createQuery(Shift.class);
            Root<Shift> root = cq.from(Shift.class);

            cq.select(root)
                    .where(cb.equal(root.get("status"), status))
                    .orderBy(cb.desc(root.get("dateOpen")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Shifts por status: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Shift> findByDateRange(LocalDate startDate, LocalDate endDate) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Shift> cq = cb.createQuery(Shift.class);
            Root<Shift> root = cq.from(Shift.class);

            cq.select(root)
                    .where(cb.between(root.get("dateOpen"), startDate.toString(), endDate.toString()))
                    .orderBy(cb.asc(root.get("dateOpen")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Shifts por período: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Atualiza apenas o valor incorrido (incurred_amount)
     */
    public boolean updateIncurredAmount(Double newValue, Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Shift shift = session.find(Shift.class, id);
            if (shift != null) {
                shift.setIncurredAmount(newValue);
                session.merge(shift);
            }

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar incurred_amount: " + e.getMessage());
            return false;
        }
    }

    /**
     * Fecha o turno com dados de fechamento
     */
    public boolean closeShift(Double closingAmount, String status, String dateClose, Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Shift shift = session.find(Shift.class, id);
            if (shift != null) {
                shift.setClosingAmount(closingAmount);
                shift.setStatus(status);
                shift.setDateClose(dateClose);
                session.merge(shift);
            }

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao fechar shift: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gera código único para o turno - VERSÃO DEFINITIVA
     */
    public String generateShiftCode() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            // Método 1: Tenta pelo maior código numérico
            try {
                String sql = "SELECT MAX(CAST(SUBSTRING(code, 7) AS UNSIGNED)) FROM shift WHERE code LIKE 'SHIFT-%'";
                Integer maxNumber = (Integer) session.createNativeQuery(sql).uniqueResult();
                int nextNumber = (maxNumber != null ? maxNumber : 0) + 1;
                return String.format("SHIFT-%06d", nextNumber);
            } catch (Exception e1) {
                // Método 2: Fallback - usa o próximo ID
                String sql = "SELECT COALESCE(MAX(id), 0) + 1 FROM shift";
                Integer nextId = ((Number) session.createNativeQuery(sql).uniqueResult()).intValue();
                return String.format("SHIFT-%06d", nextId);
            }

        } catch (Exception e) {
            System.err.println("Erro ao gerar código do shift: " + e.getMessage());
            // Método 3: Fallback final - timestamp
            return "SHIFT-" + System.currentTimeMillis();
        }
    }
}
