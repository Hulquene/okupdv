package com.okutonda.okudpdv.data.config;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernateUtil {
    
    private static final ThreadLocal<Session> threadSession = new ThreadLocal<>();
    private static final ThreadLocal<Transaction> threadTransaction = new ThreadLocal<>();

    public static Session openSession() {
        return HibernateConfig.getSessionFactory().openSession();
    }

    public static Session getCurrentSession() {
        Session session = threadSession.get();
        
        if (session == null || !session.isOpen()) {
            session = openSession();
            threadSession.set(session);
        }
        
        return session;
    }

    public static void beginTransaction() {
        Transaction tx = threadTransaction.get();
        if (tx == null) {
            Session session = getCurrentSession();
            tx = session.beginTransaction();
            threadTransaction.set(tx);
        }
    }

    public static void commitTransaction() {
        Transaction tx = threadTransaction.get();
        if (tx != null && tx.isActive()) {
            tx.commit();
        }
        threadTransaction.remove();
    }

    public static void rollbackTransaction() {
        Transaction tx = threadTransaction.get();
        if (tx != null && tx.isActive()) {
            tx.rollback();
        }
        threadTransaction.remove();
    }

    public static void closeSession() {
        Session session = threadSession.get();
        if (session != null && session.isOpen()) {
            session.close();
        }
        threadSession.remove();
        threadTransaction.remove();
    }
}