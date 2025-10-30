package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.Clients;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDao {

    private final Class<Clients> entityClass = Clients.class;

    // ====================================
    // 🔹 CRUD Básico
    // ====================================
    public Optional<Clients> findById(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            Clients entity = session.find(Clients.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar Client por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Clients> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Clients> cq = cb.createQuery(Clients.class);
            Root<Clients> root = cq.from(Clients.class);
            cq.select(root);

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os Clients: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Clients save(Clients client) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(client);
            tx.commit();

            System.out.println("✅ Client salvo: " + client.getName());
            return client;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar Client: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar Client", e);
        }
    }

    public Clients update(Clients client) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Clients merged = session.merge(client);
            tx.commit();

            System.out.println("✅ Client atualizado: " + client.getName());
            return merged;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar Client: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar Client", e);
        }
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Clients client = session.find(Clients.class, id);
            if (client != null) {
                session.remove(client);
            }

            tx.commit();
            System.out.println("✅ Client removido ID: " + id);

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover Client: " + e.getMessage());
            throw new RuntimeException("Erro ao remover Client", e);
        }
    }

    // ====================================
    // 🔹 Métodos específicos
    // ====================================
    public Optional<Clients> findByName(String name) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Clients> cq = cb.createQuery(Clients.class);
            Root<Clients> root = cq.from(Clients.class);

            cq.select(root).where(cb.equal(root.get("name"), name));

            return session.createQuery(cq).uniqueResultOptional();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Client por nome: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Clients> findByNif(String nif) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Clients> cq = cb.createQuery(Clients.class);
            Root<Clients> root = cq.from(Clients.class);

            cq.select(root).where(cb.equal(root.get("nif"), nif));

            return session.createQuery(cq).uniqueResultOptional();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Client por NIF: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Clients> getDefaultClient() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Clients> cq = cb.createQuery(Clients.class);
            Root<Clients> root = cq.from(Clients.class);

            cq.select(root).where(cb.equal(root.get("isDefault"), 1));

            return session.createQuery(cq).uniqueResultOptional();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Client padrão: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Clients> filter(String text) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Clients> cq = cb.createQuery(Clients.class);
            Root<Clients> root = cq.from(Clients.class);

            String likePattern = "%" + text + "%";

            Predicate namePredicate = cb.like(root.get("name"), likePattern);
            Predicate nifPredicate = cb.like(root.get("nif"), likePattern);
            Predicate cityPredicate = cb.like(root.get("city"), likePattern);

            cq.select(root).where(cb.or(namePredicate, nifPredicate, cityPredicate));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar Clients: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Define um cliente como padrão e remove o padrão anterior
     */
    public boolean setDefaultClient(Integer clientId) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            // Remove padrão atual
            session.createMutationQuery("UPDATE Clients SET isDefault = 0 WHERE isDefault = 1")
                    .executeUpdate();

            // Define novo padrão
            session.createMutationQuery("UPDATE Clients SET isDefault = 1 WHERE id = :id")
                    .setParameter("id", clientId)
                    .executeUpdate();

            tx.commit();
            System.out.println("✅ Cliente padrão definido: " + clientId);
            return true;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao definir cliente padrão: " + e.getMessage());
            return false;
        }
    }
}
