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
    private static boolean isPopulated = false;

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
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            // Primeiro verifica se existem países
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Countries> cq = cb.createQuery(Countries.class);
            Root<Countries> root = cq.from(Countries.class);
            cq.select(root).orderBy(cb.asc(root.get("long_name")));

            List<Countries> countries = session.createQuery(cq).getResultList();

            // Se não houver países, popula a tabela
            if (countries.isEmpty() && !isPopulated) {
                System.out.println("🔄 Tabela countries vazia. Populando com dados padrão...");
                countries = populateDefaultCountries(session);
                isPopulated = true;
            }

            tx.commit();
            System.out.println("✅ Countries carregados: " + countries.size());
            return countries;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao buscar todos os Countries: " + e.getMessage());

            // Fallback para dados estáticos
            return getStaticCountries();
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
    // 🔹 População Automática de Dados
    // ==========================================================
    /**
     * Popula a tabela com países padrão
     */
    private List<Countries> populateDefaultCountries(Session session) {
        List<Countries> defaultCountries = createDefaultCountries();

        for (Countries country : defaultCountries) {
            session.persist(country);
        }

        session.flush(); // Garante que os dados são persistidos
        System.out.println("✅ " + defaultCountries.size() + " países inseridos na tabela");

        return defaultCountries;
    }

    /**
     * Cria lista de países padrão
     */
    private List<Countries> createDefaultCountries() {
        List<Countries> countries = new ArrayList<>();

        String[][] countryData = {
            // ISO2, ISO3, Nome Curto, Nome Longo, Código Chamada, ccTLD
            {"AO", "AGO", "Angola", "República de Angola", "+244", ".ao"},
            {"PT", "PRT", "Portugal", "República Portuguesa", "+351", ".pt"},
            {"BR", "BRA", "Brasil", "República Federativa do Brasil", "+55", ".br"},
            {"US", "USA", "EUA", "Estados Unidos da América", "+1", ".us"},
            {"GB", "GBR", "Reino Unido", "Reino Unido da Grã-Bretanha", "+44", ".uk"},
            {"FR", "FRA", "França", "República Francesa", "+33", ".fr"},
            {"ES", "ESP", "Espanha", "Reino de Espanha", "+34", ".es"},
            {"ZA", "ZAF", "África do Sul", "República da África do Sul", "+27", ".za"},
            {"CN", "CHN", "China", "República Popular da China", "+86", ".cn"},
            {"MZ", "MOZ", "Moçambique", "República de Moçambique", "+258", ".mz"},
            {"CV", "CPV", "Cabo Verde", "República de Cabo Verde", "+238", ".cv"},
            {"ST", "STP", "São Tomé", "República Democrática de São Tomé e Príncipe", "+239", ".st"},
            {"GW", "GNB", "Guiné-Bissau", "República da Guiné-Bissau", "+245", ".gw"},
            {"GQ", "GNQ", "Guiné Equatorial", "República da Guiné Equatorial", "+240", ".gq"},
            {"NA", "NAM", "Namíbia", "República da Namíbia", "+264", ".na"},
            {"ZW", "ZWE", "Zimbábue", "República do Zimbábue", "+263", ".zw"},
            {"BW", "BWA", "Botswana", "República do Botswana", "+267", ".bw"},
            {"CD", "COD", "Congo", "República Democrática do Congo", "+243", ".cd"},
            {"CG", "COG", "Congo", "República do Congo", "+242", ".cg"},
            {"GH", "GHA", "Gana", "República do Gana", "+233", ".gh"},
            {"KE", "KEN", "Quénia", "República do Quénia", "+254", ".ke"},
            {"NG", "NGA", "Nigéria", "República Federal da Nigéria", "+234", ".ng"},
            {"SN", "SEN", "Senegal", "República do Senegal", "+221", ".sn"},
            {"TZ", "TZA", "Tanzânia", "República Unida da Tanzânia", "+255", ".tz"},
            {"UG", "UGA", "Uganda", "República do Uganda", "+256", ".ug"}
        };

        int id = 1;
        for (String[] data : countryData) {
            Countries country = new Countries();
            country.setId(id++);
            country.setIso2(data[0]);
            country.setIso3(data[1]);
            country.setShort_name(data[2]);
            country.setLong_name(data[3]);
            country.setCalling_code(data[4]);
            country.setCctld(data[5]);
            country.setUn_member("yes"); // Assume que são membros da ONU
            countries.add(country);
        }

        return countries;
    }

    /**
     * Fallback para dados estáticos (sem banco)
     */
    private List<Countries> getStaticCountries() {
        System.out.println("⚠️  Usando dados estáticos de países");
        return createDefaultCountries();
    }

    /**
     * Força a repopulação da tabela (útil para testes)
     */
    public void forceRepopulation() {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            // Limpa a tabela
            session.createMutationQuery("DELETE FROM Countries").executeUpdate();

            // Popula novamente
            populateDefaultCountries(session);

            tx.commit();
            isPopulated = true;
            System.out.println("✅ Tabela countries repovoada com sucesso");

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao repopular countries: " + e.getMessage());
        }
    }

    // ==========================================================
    // 🔹 Métodos Específicos (mantidos da versão anterior)
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

    public List<Countries> findCommonCountries() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Countries> cq = cb.createQuery(Countries.class);
            Root<Countries> root = cq.from(Countries.class);

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

    public boolean existsByIso2(String iso2) {
        return findByIso2(iso2).isPresent();
    }

    public boolean existsByIso3(String iso3) {
        return findByIso3(iso3).isPresent();
    }
}
