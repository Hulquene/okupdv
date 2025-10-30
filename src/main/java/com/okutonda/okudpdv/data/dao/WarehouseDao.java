package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.Warehouse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WarehouseDao {

    private final Class<Warehouse> entityClass = Warehouse.class;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public Optional<Warehouse> findById(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            Warehouse entity = session.find(Warehouse.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar Warehouse por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Warehouse> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Warehouse> cq = cb.createQuery(Warehouse.class);
            Root<Warehouse> root = cq.from(Warehouse.class);
            cq.select(root).orderBy(cb.asc(root.get("name")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os Warehouses: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Warehouse save(Warehouse warehouse) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(warehouse);
            tx.commit();

            System.out.println("✅ Warehouse salvo: " + warehouse.getName());
            return warehouse;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar Warehouse: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar Warehouse", e);
        }
    }

    public Warehouse update(Warehouse warehouse) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Warehouse merged = session.merge(warehouse);
            tx.commit();

            System.out.println("✅ Warehouse atualizado: " + warehouse.getName());
            return merged;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar Warehouse: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar Warehouse", e);
        }
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Warehouse warehouse = session.find(Warehouse.class, id);
            if (warehouse != null) {
                session.remove(warehouse);
            }

            tx.commit();
            System.out.println("✅ Warehouse removido ID: " + id);

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover Warehouse: " + e.getMessage());
            throw new RuntimeException("Erro ao remover Warehouse", e);
        }
    }

    // ==========================================================
    // 🔹 Consultas adicionais
    // ==========================================================
    public List<Warehouse> filter(String text) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Warehouse> cq = cb.createQuery(Warehouse.class);
            Root<Warehouse> root = cq.from(Warehouse.class);

            String likePattern = "%" + text + "%";

            Predicate namePredicate = cb.like(root.get("name"), likePattern);
            Predicate locationPredicate = cb.like(root.get("location"), likePattern);
            Predicate descriptionPredicate = cb.like(root.get("description"), likePattern);

            cq.select(root)
                    .where(cb.or(namePredicate, locationPredicate, descriptionPredicate))
                    .orderBy(cb.asc(root.get("name")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar Warehouses: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Warehouse> findActive() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Warehouse> cq = cb.createQuery(Warehouse.class);
            Root<Warehouse> root = cq.from(Warehouse.class);

            cq.select(root)
                    .where(cb.equal(root.get("status"), 1))
                    .orderBy(cb.asc(root.get("name")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Warehouses ativos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean existsByName(String name) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Warehouse> root = cq.from(Warehouse.class);

            cq.select(cb.count(root))
                    .where(cb.equal(root.get("name"), name));

            Long count = session.createQuery(cq).getSingleResult();
            return count > 0;

        } catch (Exception e) {
            System.err.println("Erro ao verificar nome do Warehouse: " + e.getMessage());
            return false;
        }
    }

    /**
     * Ativa/desativa um warehouse
     */
    public boolean toggleWarehouseStatus(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Optional<Warehouse> warehouseOpt = findById(id);
            if (warehouseOpt.isPresent()) {
                Warehouse warehouse = warehouseOpt.get();
                warehouse.setStatus(warehouse.getStatus() == 1 ? 0 : 1);
                session.merge(warehouse);
            }

            tx.commit();
            System.out.println("✅ Status do Warehouse atualizado: " + id);
            return true;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao alterar status do Warehouse: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca warehouse por localização
     */
    public List<Warehouse> findByLocation(String location) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Warehouse> cq = cb.createQuery(Warehouse.class);
            Root<Warehouse> root = cq.from(Warehouse.class);

            cq.select(root)
                    .where(cb.equal(root.get("location"), location))
                    .orderBy(cb.asc(root.get("name")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Warehouses por localização: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
