package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.Purchase;
import com.okutonda.okudpdv.data.entities.DocumentType;
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

public class PurchaseDao {

    private final Class<Purchase> entityClass = Purchase.class;

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    public Optional<Purchase> findById(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            Purchase entity = session.find(Purchase.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar Purchase por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Purchase> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Purchase> cq = cb.createQuery(Purchase.class);
            Root<Purchase> root = cq.from(Purchase.class);
            cq.select(root).orderBy(cb.desc(root.get("dataCompra")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os Purchases: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Purchase save(Purchase purchase) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(purchase);
            tx.commit();

            System.out.println("✅ Purchase salvo: " + purchase.getInvoiceNumber());
            return purchase;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar Purchase: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar Purchase", e);
        }
    }

    public Purchase update(Purchase purchase) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Purchase merged = session.merge(purchase);
            tx.commit();

            System.out.println("✅ Purchase atualizado: " + purchase.getInvoiceNumber());
            return merged;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar Purchase: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar Purchase", e);
        }
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Purchase purchase = session.find(Purchase.class, id);
            if (purchase != null) {
                session.remove(purchase);
            }

            tx.commit();
            System.out.println("✅ Purchase removido ID: " + id);

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover Purchase: " + e.getMessage());
            throw new RuntimeException("Erro ao remover Purchase", e);
        }
    }

    // ==========================================================
    // 🔹 Métodos específicos
    // ==========================================================
    public Optional<Purchase> findByInvoiceNumber(String invoiceNumber) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Purchase> cq = cb.createQuery(Purchase.class);
            Root<Purchase> root = cq.from(Purchase.class);

            cq.select(root).where(cb.equal(root.get("invoiceNumber"), invoiceNumber));

            return session.createQuery(cq).uniqueResultOptional();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Purchase por número de fatura: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Purchase> filter(String text) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Purchase> cq = cb.createQuery(Purchase.class);
            Root<Purchase> root = cq.from(Purchase.class);

            String likePattern = "%" + text + "%";

            Predicate invoicePredicate = cb.like(root.get("invoiceNumber"), likePattern);
            Predicate descricaoPredicate = cb.like(root.get("descricao"), likePattern);
            Predicate supplierPredicate = cb.like(root.get("supplier").get("name"), likePattern);

            cq.select(root)
                    .where(cb.or(invoicePredicate, descricaoPredicate, supplierPredicate))
                    .orderBy(cb.desc(root.get("dataCompra")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar Purchases: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Purchase> filterByDate(LocalDate from, LocalDate to) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Purchase> cq = cb.createQuery(Purchase.class);
            Root<Purchase> root = cq.from(Purchase.class);

            cq.select(root)
                    .where(cb.between(root.get("dataCompra"),
                            java.sql.Date.valueOf(from),
                            java.sql.Date.valueOf(to)))
                    .orderBy(cb.asc(root.get("dataCompra")), cb.asc(root.get("id")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar Purchases por data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Purchase> findBySupplier(Integer supplierId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Purchase> cq = cb.createQuery(Purchase.class);
            Root<Purchase> root = cq.from(Purchase.class);

            cq.select(root)
                    .where(cb.equal(root.get("supplier").get("id"), supplierId))
                    .orderBy(cb.desc(root.get("dataCompra")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Purchases por fornecedor: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Purchase> findByInvoiceType(DocumentType invoiceType) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Purchase> cq = cb.createQuery(Purchase.class);
            Root<Purchase> root = cq.from(Purchase.class);

            cq.select(root)
                    .where(cb.equal(root.get("invoiceType"), invoiceType))
                    .orderBy(cb.desc(root.get("dataCompra")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Purchases por tipo de fatura: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Purchase> findByStatus(String status) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Purchase> cq = cb.createQuery(Purchase.class);
            Root<Purchase> root = cq.from(Purchase.class);

            cq.select(root)
                    .where(cb.equal(root.get("status"), status))
                    .orderBy(cb.desc(root.get("dataCompra")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Purchases por status: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Obtém o próximo número de fatura - VERSÃO CORRIGIDA
     */
    public Integer getNextInvoiceNumber() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            // Usando Native Query como fallback seguro
            String sql = "SELECT COALESCE(MAX(CAST(invoice_number AS UNSIGNED)), 0) + 1 FROM purchases";

            Object result = session.createNativeQuery(sql).uniqueResult();

            if (result instanceof Number) {
                return ((Number) result).intValue();
            } else if (result instanceof String) {
                try {
                    return Integer.parseInt((String) result);
                } catch (NumberFormatException e) {
                    return 1;
                }
            }

            return 1;

        } catch (Exception e) {
            System.err.println("Erro ao obter próximo número de fatura: " + e.getMessage());
            return 1;
        }
    }

    /**
     * Calcula o total de compras por período
     */
    public Double calculateTotalByPeriod(LocalDate from, LocalDate to) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Double> cq = cb.createQuery(Double.class);
            Root<Purchase> root = cq.from(Purchase.class);

            cq.select(cb.sum(root.get("total")))
                    .where(cb.between(root.get("dataCompra"),
                            java.sql.Date.valueOf(from),
                            java.sql.Date.valueOf(to)));

            Double total = session.createQuery(cq).getSingleResult();
            return total != null ? total : 0.0;

        } catch (Exception e) {
            System.err.println("Erro ao calcular total de compras: " + e.getMessage());
            return 0.0;
        }
    }

    // No PurchaseDao - adicione este método
    public Purchase findByIdWithItems(Integer compraId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            // Usando JOIN FETCH para carregar os itens junto com a compra
            String hql = "SELECT DISTINCT p FROM Purchase p "
                    + "LEFT JOIN FETCH p.items i "
                    + "LEFT JOIN FETCH i.product "
                    + "WHERE p.id = :compraId";

            return session.createQuery(hql, Purchase.class)
                    .setParameter("compraId", compraId)
                    .uniqueResult();

        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar compra com itens: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar compra com itens", e);
        }
    }
}
