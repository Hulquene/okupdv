package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.Options;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Optional;

public class OptionsDao {

    private final Class<Options> entityClass = Options.class;

    // ==================================
    // 🔹 CRUD Básico
    // ==================================
    public Optional<Options> findById(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            Options entity = session.find(Options.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar Options por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Options> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Options> cq = cb.createQuery(Options.class);
            Root<Options> root = cq.from(Options.class);
            cq.select(root);

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todas as Options: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Options save(Options options) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(options);
            tx.commit();

            System.out.println("✅ Options salva: " + options.getName());
            return options;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar Options: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar Options", e);
        }
    }

    public Options update(Options options) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Options merged = session.merge(options);
            tx.commit();

            System.out.println("✅ Options atualizada: " + options.getName());
            return merged;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar Options: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar Options", e);
        }
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Options options = session.find(Options.class, id);
            if (options != null) {
                session.remove(options);
            }

            tx.commit();
            System.out.println("✅ Options removida ID: " + id);

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover Options: " + e.getMessage());
            throw new RuntimeException("Erro ao remover Options", e);
        }
    }

    // ==================================
    // 🔹 Métodos Específicos
    // ==================================
    public Optional<Options> findByName(String name) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Options> cq = cb.createQuery(Options.class);
            Root<Options> root = cq.from(Options.class);

            cq.select(root).where(cb.equal(root.get("name"), name));

            return session.createQuery(cq).uniqueResultOptional();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Options por nome: " + e.getMessage());
            return Optional.empty();
        }
    }

    public String findValueByName(String name) {
        return findByName(name)
                .map(Options::getValue)
                .orElse(null);
    }

    public boolean saveOrUpdate(Options options) {
        Optional<Options> existing = findByName(options.getName());

        if (existing.isPresent()) {
            // Atualiza o ID do objeto existente e faz update
            options.setId(existing.get().getId());
            update(options);
            return true;
        } else {
            save(options);
            return true;
        }
    }
}

//package com.okutonda.okudpdv.data.dao;
//
//import com.okutonda.okudpdv.data.entities.Options;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.List;
//
///**
// * DAO responsável por gerir as opções de configuração do sistema.
// *
// * Usa a infraestrutura genérica BaseDao + pool de conexões.
// *
// * @author …
// */
//public class OptionsDao extends BaseDao<Options> {
//    // ✅ Construtor padrão (usa conexão do pool automaticamente)
//
//    public OptionsDao() {
//        // não precisa chamar super(), ele já existe por padrão
//    }
//
//    // ✅ Construtor alternativo (usa conexão externa — transação)
//    public OptionsDao(java.sql.Connection externalConn) {
//        super(externalConn);
//    }
//
//    // 🔹 Mapeamento de ResultSet → objeto Options
//    private Options map(ResultSet rs) {
//        try {
//            Options obj = new Options();
//            obj.setId(rs.getInt("id"));
//            obj.setName(rs.getString("name"));
//            obj.setValue(rs.getString("value"));
//            obj.setStatus(rs.getString("status"));
//            return obj;
//        } catch (SQLException e) {
//            System.err.println("[DB] Erro ao mapear Options: " + e.getMessage());
//            return null;
//        }
//    }
//
//    // ==================================
//    // 🔹 Implementação CRUD padrão
//    // ==================================
//    @Override
//    public boolean add(Options obj) {
//        String sql = "INSERT INTO options (name, value, status) VALUES (?, ?, ?)";
//        return executeUpdate(sql, obj.getName(), obj.getValue(), obj.getStatus());
//    }
//
//    @Override
//    public boolean update(Options obj) {
//        String sql = "UPDATE options SET value=?, status=? WHERE name=?";
//        return executeUpdate(sql, obj.getValue(), obj.getStatus(), obj.getName());
//    }
//
//    @Override
//    public boolean delete(int id) {
//        return executeUpdate("DELETE FROM options WHERE id=?", id);
//    }
//
//    @Override
//    public Options findById(int id) {
//        return findOne("SELECT * FROM options WHERE id=?", this::map, id);
//    }
//
//    @Override
//    public List<Options> findAll() {
//        return executeQuery("SELECT * FROM options", this::map);
//    }
//
//    // ==================================
//    // 🔹 Métodos específicos do módulo
//    // ==================================
//    /**
//     * Busca uma opção pelo nome
//     */
//    public Options findByName(String name) {
//        return findOne("SELECT * FROM options WHERE name=?", this::map, name);
//    }
//
//    /**
//     * Retorna o valor da opção (ou null se não existir)
//     */
//    public String findValueByName(String name) {
//        Options opt = findByName(name);
//        return (opt != null) ? opt.getValue() : null;
//    }
//
//    /**
//     * Cria ou atualiza automaticamente (upsert)
//     */
//    public boolean saveOrUpdate(Options opt) {
//        Options existing = findByName(opt.getName());
//        if (existing != null) {
//            return update(opt);
//        } else {
//            return add(opt);
//        }
//    }
//}
