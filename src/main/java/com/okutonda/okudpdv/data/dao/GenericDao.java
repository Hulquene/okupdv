package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class GenericDao<T, ID extends Serializable> {

    private final Class<T> entityClass;

    public GenericDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public Optional<T> findById(ID id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            T entity = session.find(entityClass, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar " + entityClass.getSimpleName() + " por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<T> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(entityClass);
            Root<T> root = cq.from(entityClass);
            cq.select(root);

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os " + entityClass.getSimpleName() + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public T save(T entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();

            System.out.println("✅ " + entityClass.getSimpleName() + " salvo: " + entity);
            return entity;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar " + entityClass.getSimpleName() + ": " + e.getMessage());
            throw new RuntimeException("Erro ao salvar entidade", e);
        }
    }

    public void delete(T entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.remove(entity);
            tx.commit();

            System.out.println("✅ " + entityClass.getSimpleName() + " removido: " + entity);

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover " + entityClass.getSimpleName() + ": " + e.getMessage());
            throw new RuntimeException("Erro ao remover entidade", e);
        }
    }
}


//
///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
// */
//package com.okutonda.okudpdv.data.dao;
//
//import java.util.List;
//
///**
// * Interface padrão para operações CRUD.
// *
// * @param <T> Tipo da entidade (ex: User, Client, Product)
// * @author Hul…
// */
//public interface GenericDao<T> {
//
//    boolean add(T entity);
//
//    boolean update(T entity);
//
//    boolean delete(int id);
//
//    T findById(int id);
//
//    List<T> findAll();
//}
