package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.Countries;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CountryDao {

    private final Class<Countries> entityClass = Countries.class;

    // ==========================================================
    // 🔹 CRUD Básico
    // ==========================================================
    public Optional<Countries> findById(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            Countries entity = session.find(Countries.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar Country por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Countries> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Countries> cq = cb.createQuery(Countries.class);
            Root<Countries> root = cq.from(Countries.class);
            cq.select(root).orderBy(cb.asc(root.get("long_name")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os Countries: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Countries save(Countries country) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(country);
            tx.commit();

            System.out.println("✅ Country salvo: " + country.getLong_name());
            return country;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar Country: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar Country", e);
        }
    }

    public Countries update(Countries country) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Countries merged = session.merge(country);
            tx.commit();

            System.out.println("✅ Country atualizado: " + country.getLong_name());
            return merged;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar Country: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar Country", e);
        }
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Countries country = session.find(Countries.class, id);
            if (country != null) {
                session.remove(country);
            }

            tx.commit();
            System.out.println("✅ Country removido ID: " + id);

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover Country: " + e.getMessage());
            throw new RuntimeException("Erro ao remover Country", e);
        }
    }

    // ==========================================================
    // 🔹 Métodos Específicos
    // ==========================================================
    public Optional<Countries> findByIso2(String iso2) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Countries> cq = cb.createQuery(Countries.class);
            Root<Countries> root = cq.from(Countries.class);

            cq.select(root).where(cb.equal(root.get("iso2"), iso2));

            return session.createQuery(cq).uniqueResultOptional();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Country por ISO2: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Countries> findByIso3(String iso3) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Countries> cq = cb.createQuery(Countries.class);
            Root<Countries> root = cq.from(Countries.class);

            cq.select(root).where(cb.equal(root.get("iso3"), iso3));

            return session.createQuery(cq).uniqueResultOptional();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Country por ISO3: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Countries> findByName(String name) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Countries> cq = cb.createQuery(Countries.class);
            Root<Countries> root = cq.from(Countries.class);

            String likePattern = "%" + name + "%";
            Predicate namePredicate = cb.like(root.get("long_name"), likePattern);
            Predicate shortNamePredicate = cb.like(root.get("short_name"), likePattern);

            cq.select(root).where(cb.or(namePredicate, shortNamePredicate));

            return session.createQuery(cq).uniqueResultOptional();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Country por nome: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Countries> filter(String text) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Countries> cq = cb.createQuery(Countries.class);
            Root<Countries> root = cq.from(Countries.class);

            String likePattern = "%" + text + "%";

            Predicate iso2Predicate = cb.like(root.get("iso2"), likePattern);
            Predicate iso3Predicate = cb.like(root.get("iso3"), likePattern);
            Predicate longNamePredicate = cb.like(root.get("long_name"), likePattern);
            Predicate shortNamePredicate = cb.like(root.get("short_name"), likePattern);
            Predicate callingCodePredicate = cb.like(root.get("calling_code"), likePattern);

            cq.select(root)
                    .where(cb.or(iso2Predicate, iso3Predicate, longNamePredicate, shortNamePredicate, callingCodePredicate))
                    .orderBy(cb.asc(root.get("long_name")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar Countries: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Busca países por código de chamada
     */
    public List<Countries> findByCallingCode(String callingCode) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Countries> cq = cb.createQuery(Countries.class);
            Root<Countries> root = cq.from(Countries.class);

            cq.select(root)
                    .where(cb.equal(root.get("calling_code"), callingCode))
                    .orderBy(cb.asc(root.get("long_name")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Countries por código de chamada: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Retorna países membros da ONU
     */
    public List<Countries> findUnMemberCountries() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Countries> cq = cb.createQuery(Countries.class);
            Root<Countries> root = cq.from(Countries.class);

            cq.select(root)
                    .where(cb.equal(root.get("un_member"), "yes"))
                    .orderBy(cb.asc(root.get("long_name")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar países membros da ONU: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Busca países por domínio de topo (ccTLD)
     */
    public Optional<Countries> findByCcTld(String cctld) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Countries> cq = cb.createQuery(Countries.class);
            Root<Countries> root = cq.from(Countries.class);

            cq.select(root).where(cb.equal(root.get("cctld"), cctld));

            return session.createQuery(cq).uniqueResultOptional();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Country por ccTLD: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Retorna países mais comuns (para dropdowns)
     */
    public List<Countries> findCommonCountries() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Countries> cq = cb.createQuery(Countries.class);
            Root<Countries> root = cq.from(Countries.class);

            // Países mais comuns no contexto angolano
            String[] commonCodes = {"AO", "PT", "BR", "US", "GB", "FR", "ES", "ZA", "CN"};

            List<Predicate> predicates = new ArrayList<>();
            for (String code : commonCodes) {
                predicates.add(cb.equal(root.get("iso2"), code));
            }

            cq.select(root)
                    .where(cb.or(predicates.toArray(new Predicate[0])))
                    .orderBy(cb.asc(root.get("long_name")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar países comuns: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Verifica se um código ISO2 já existe
     */
    public boolean existsByIso2(String iso2) {
        return findByIso2(iso2).isPresent();
    }

    /**
     * Verifica se um código ISO3 já existe
     */
    public boolean existsByIso3(String iso3) {
        return findByIso3(iso3).isPresent();
    }
}
