package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.entities.Order;
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

public class OrderDao {

    private final Class<Order> entityClass = Order.class;

    // ==========================================================
    // 🔹 CRUD PADRÃO
    // ==========================================================
    public Optional<Order> findById(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            Order entity = session.find(Order.class, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            System.err.println("Erro ao buscar Order por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Order> findAll() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Order> cq = cb.createQuery(Order.class);
            Root<Order> root = cq.from(Order.class);
            cq.select(root).orderBy(cb.desc(root.get("datecreate")));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os Orders: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Order save(Order order) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(order);
            tx.commit();

            System.out.println("✅ Order salvo: " + order.getPrefix() + "/" + order.getNumber());
            return order;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao salvar Order: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar Order", e);
        }
    }

    public Order update(Order order) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Order merged = session.merge(order);
            tx.commit();

            System.out.println("✅ Order atualizado: " + order.getPrefix() + "/" + order.getNumber());
            return merged;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao atualizar Order: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar Order", e);
        }
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Order order = session.find(Order.class, id);
            if (order != null) {
                session.remove(order);
            }

            tx.commit();
            System.out.println("✅ Order removido ID: " + id);

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro ao remover Order: " + e.getMessage());
            throw new RuntimeException("Erro ao remover Order", e);
        }
    }

    // ==========================================================
    // 🔹 CONSULTAS CUSTOM
    // ==========================================================
    public Optional<Order> findByNumber(Integer number) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Order> cq = cb.createQuery(Order.class);
            Root<Order> root = cq.from(Order.class);

            cq.select(root).where(cb.equal(root.get("number"), number));

            return session.createQuery(cq).uniqueResultOptional();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Order por número: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Order> filter(String text) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Order> cq = cb.createQuery(Order.class);
            Root<Order> root = cq.from(Order.class);

            String likePattern = "%" + text + "%";

            Predicate prefixPredicate = cb.like(root.get("prefix"), likePattern);
            Predicate notePredicate = cb.like(root.get("note"), likePattern);
            Predicate clientPredicate = cb.like(root.get("client").get("name"), likePattern);

            cq.select(root)
                    .where(cb.or(prefixPredicate, notePredicate, clientPredicate))
                    .orderBy(cb.desc(root.get("datecreate")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar Orders: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Order> filterByDate(LocalDate from, LocalDate to) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Order> cq = cb.createQuery(Order.class);
            Root<Order> root = cq.from(Order.class);

            cq.select(root)
                    .where(cb.between(root.get("datecreate"), from.toString(), to.toString()))
                    .orderBy(cb.asc(root.get("datecreate")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao filtrar Orders por data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Order> findByClientId(Integer clientId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Order> cq = cb.createQuery(Order.class);
            Root<Order> root = cq.from(Order.class);

            cq.select(root)
                    .where(cb.equal(root.get("client").get("id"), clientId))
                    .orderBy(cb.desc(root.get("datecreate")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Orders por cliente: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Order> findBySellerId(Integer sellerId) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Order> cq = cb.createQuery(Order.class);
            Root<Order> root = cq.from(Order.class);

            cq.select(root)
                    .where(cb.equal(root.get("seller").get("id"), sellerId))
                    .orderBy(cb.desc(root.get("datecreate")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Orders por vendedor: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Order> findByStatus(Integer status) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Order> cq = cb.createQuery(Order.class);
            Root<Order> root = cq.from(Order.class);

            cq.select(root)
                    .where(cb.equal(root.get("status"), status))
                    .orderBy(cb.desc(root.get("datecreate")));

            return session.createQuery(cq).getResultList();

        } catch (Exception e) {
            System.err.println("Erro ao buscar Orders por status: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Obtém o próximo número de ordem
     */
    public Integer getNextNumber() {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
            Root<Order> root = cq.from(Order.class);

            cq.select(cb.coalesce(cb.max(root.get("number")), 0));

            Integer maxNumber = session.createQuery(cq).getSingleResult();
            return maxNumber + 1;

        } catch (Exception e) {
            System.err.println("Erro ao obter próximo número: " + e.getMessage());
            return 1;
        }
    }

    /**
     * Calcula o total de vendas em um período
     */
    public Double calculateTotalSalesByPeriod(LocalDate from, LocalDate to) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Double> cq = cb.createQuery(Double.class);
            Root<Order> root = cq.from(Order.class);

            cq.select(cb.sum(root.get("total")))
                    .where(cb.and(
                            cb.between(root.get("datecreate"), from.toString(), to.toString()),
                            cb.equal(root.get("status"), 2) // Status de pedido concluído
                    ));

            Double total = session.createQuery(cq).getSingleResult();
            return total != null ? total : 0.0;

        } catch (Exception e) {
            System.err.println("Erro ao calcular total de vendas: " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * Conta pedidos por status
     */
    public Long countByStatus(Integer status) {
        Session session = HibernateUtil.getCurrentSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Order> root = cq.from(Order.class);

            cq.select(cb.count(root))
                    .where(cb.equal(root.get("status"), status));

            return session.createQuery(cq).getSingleResult();

        } catch (Exception e) {
            System.err.println("Erro ao contar pedidos por status: " + e.getMessage());
            return 0L;
        }
    }
}

//package com.okutonda.okudpdv.data.dao;
//
//import com.okutonda.okudpdv.data.entities.*;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.List;
//
///**
// * DAO responsável pela gestão das encomendas/pedidos (Order).
// *
// * Totalmente compatível com o DatabaseProvider (HikariCP) e BaseDao. Pode ser
// * usado tanto isoladamente como dentro de transações partilhadas.
// *
// * @author Hulquene
// */
//public class OrderDao extends BaseDao<Order> {
//
//    // ✅ Construtor padrão (usa conexão do pool automaticamente)
//    public OrderDao() {
//        // não precisa chamar super(), ele já existe por padrão
//    }
//
//    // ✅ Construtor alternativo (usa conexão externa — transação)
//    public OrderDao(java.sql.Connection externalConn) {
//        super(externalConn);
//    }
//
//    // ==========================================================
//    // 🔹 MAPEAMENTO SQL → OBJETO
//    // ==========================================================
//    private Order map(ResultSet rs) {
//        try {
//            Order o = new Order();
//
//            o.setId(rs.getInt("id"));
//            o.setStatus(rs.getInt("status"));
//            o.setDatecreate(rs.getString("datecreate"));
//            o.setNumber(rs.getInt("number"));
//            o.setPrefix(rs.getString("prefix"));
//            o.setTotal(rs.getDouble("total"));
//            o.setSubTotal(rs.getDouble("sub_total"));
//            o.setTotalTaxe(rs.getDouble("total_taxe"));
//            o.setPayTotal(rs.getDouble("pay_total"));
//            o.setAmountReturned(rs.getDouble("amount_returned"));
//            o.setHash(rs.getString("hash"));
//            o.setYear(rs.getInt("year"));
//            o.setKey(rs.getString("key"));
//            o.setNote(rs.getString("note"));
//
//            try {
//                ClientDao cDao = new ClientDao();
//                UserDao uDao = new UserDao();
//                o.setClient(cDao.findById(rs.getInt("client_id")));
//                o.setSeller(uDao.findById(rs.getInt("user_id")));
//            } catch (Exception e) {
//                System.err.println("[OrderDao] Aviso: erro ao carregar relações -> " + e.getMessage());
//            }
//
//            return o;
//        } catch (SQLException e) {
//            System.err.println("[OrderDao] Erro ao mapear Order: " + e.getMessage());
//            return null;
//        }
//    }
//
//    // ==========================================================
//    // 🔹 CRUD PADRÃO
//    // ==========================================================
//    @Override
//    public boolean add(Order o) {
//        String sql = """
//            INSERT INTO orders 
//            (status,datecreate,number,prefix,total,sub_total,total_taxe,pay_total,
//             amount_returned,hash,client_id,user_id,year,`key`,note)
//            VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
//        """;
//        return executeUpdate(sql,
//                o.getStatus(),
//                o.getDatecreate(),
//                o.getNumber(),
//                o.getPrefix(),
//                safe(o.getTotal()),
//                safe(o.getSubTotal()),
//                safe(o.getTotalTaxe()),
//                safe(o.getPayTotal()),
//                safe(o.getAmountReturned()),
//                o.getHash(),
//                o.getClient() != null ? o.getClient().getId() : 0,
//                o.getSeller() != null ? o.getSeller().getId() : 0,
//                o.getYear(),
//                o.getKey(),
//                o.getNote());
//    }
//
//    @Override
//    public boolean update(Order o) {
//        String sql = """
//            UPDATE orders 
//               SET status=?, datecreate=?, number=?, prefix=?, total=?, sub_total=?, 
//                   total_taxe=?, pay_total=?, amount_returned=?, hash=?, client_id=?, 
//                   user_id=?, year=?, `key`=?, note=? 
//             WHERE id=?
//        """;
//        return executeUpdate(sql,
//                o.getStatus(),
//                o.getDatecreate(),
//                o.getNumber(),
//                o.getPrefix(),
//                safe(o.getTotal()),
//                safe(o.getSubTotal()),
//                safe(o.getTotalTaxe()),
//                safe(o.getPayTotal()),
//                safe(o.getAmountReturned()),
//                o.getHash(),
//                o.getClient() != null ? o.getClient().getId() : 0,
//                o.getSeller() != null ? o.getSeller().getId() : 0,
//                o.getYear(),
//                o.getKey(),
//                o.getNote(),
//                o.getId());
//    }
//
//    @Override
//    public boolean delete(int id) {
//        return executeUpdate("DELETE FROM orders WHERE id=?", id);
//    }
//
//    @Override
//    public Order findById(int id) {
//        return findOne("SELECT * FROM orders WHERE id=?", this::map, id);
//    }
//
//    @Override
//    public List<Order> findAll() {
//        return executeQuery("SELECT * FROM orders ORDER BY datecreate DESC", this::map);
//    }
//
//    // ==========================================================
//    // 🔹 CONSULTAS CUSTOM
//    // ==========================================================
//    public Order findByNumber(int number) {
//        return findOne("SELECT * FROM orders WHERE number=?", this::map, number);
//    }
//
//    public List<Order> list(String where) {
//        String sql = "SELECT * FROM orders " + (where != null ? where : "");
//        return executeQuery(sql, this::map);
//    }
//
//    public List<Order> filterDate(LocalDate from, LocalDate to, String where) {
//        String sql = """
//            SELECT * FROM orders 
//            WHERE DATE(datecreate) BETWEEN ? AND ? 
//        """ + (where != null ? where : "") + " ORDER BY datecreate ASC";
//        return executeQuery(sql, this::map, from.toString(), to.toString());
//    }
//
//    public int getNextNumber() {
//        Integer num = executeScalarInt("SELECT MAX(number) FROM orders");
//        return (num == null) ? 1 : num + 1;
//    }
//
//    private static Double safe(Double v) {
//        return (v == null) ? 0d : v;
//    }
//}
///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.okutonda.okudpdv.data.dao;
//
//import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
//import com.okutonda.okudpdv.data.entities.Clients;
//import com.okutonda.okudpdv.data.entities.Order;
//import com.okutonda.okudpdv.data.entities.ProductOrder;
//import com.okutonda.okudpdv.data.entities.User;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import javax.swing.JOptionPane;
//
///**
// *
// * @author kenny
// */
//public class OrderDao {
//
//    private final Connection conn;
//    PreparedStatement pst = null;
//    ResultSet rs = null;
//
//    public OrderDao() {
//        this.conn = ConnectionDatabase.getConnect();
//    }
//
//    public OrderDao(Connection externalConn) { // para transação
//        this.conn = externalConn;
//    }
//
//    public boolean add(Order obj) {
//        try {
//            String sql = "INSERT INTO orders (status,datecreate,number,prefix,total,sub_total,total_taxe,pay_total,amount_returned,hash,client_id,user_id,year,`key`,note) "
//                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//            pst = this.conn.prepareStatement(sql);
//            pst.setInt(1, obj.getStatus());
//            pst.setString(2, obj.getDatecreate());
//            pst.setInt(3, obj.getNumber());
//            pst.setString(4, obj.getPrefix());
//            pst.setDouble(5, obj.getTotal() != null ? obj.getTotal() : 0d);
//            pst.setDouble(6, obj.getSubTotal() != null ? obj.getSubTotal() : 0d);
//            pst.setDouble(7, obj.getTotalTaxe() != null ? obj.getTotalTaxe() : 0d);       // <-- NOVO
//            pst.setDouble(8, obj.getPayTotal() != null ? obj.getPayTotal() : 0d);
//            pst.setDouble(9, obj.getAmountReturned() != null ? obj.getAmountReturned() : 0d);
//            pst.setString(10, obj.getHash());
//            pst.setInt(11, obj.getClient().getId());
//            pst.setInt(12, obj.getSeller().getId());
//            pst.setInt(13, obj.getYear());
//            pst.setString(14, obj.getKey());                                              // <-- NOVO
//            pst.setString(15, obj.getNote());                                             // <-- NOVO
//            pst.execute();
//            return true;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao salvar orders: " + e.getMessage());
//        }
//        return false;
//    }
//
//    public boolean edit(Order obj) {
//        try {
//            String sql = "UPDATE orders SET status=?,datecreate=?,number=?,prefix=?,total=?,sub_total=?,total_taxe=?,pay_total=?,amount_returned=?,hash=?,client_id=?,user_id=?,year=?,`key`=?,note=? WHERE id=?";
//            pst = this.conn.prepareStatement(sql);
//            pst.setInt(1, obj.getStatus());
//            pst.setString(2, obj.getDatecreate());
//            pst.setInt(3, obj.getNumber());
//            pst.setString(4, obj.getPrefix());
//            pst.setDouble(5, obj.getTotal() != null ? obj.getTotal() : 0d);
//            pst.setDouble(6, obj.getSubTotal() != null ? obj.getSubTotal() : 0d);
//            pst.setDouble(7, obj.getTotalTaxe() != null ? obj.getTotalTaxe() : 0d);       // <-- NOVO
//            pst.setDouble(8, obj.getPayTotal() != null ? obj.getPayTotal() : 0d);
//            pst.setDouble(9, obj.getAmountReturned() != null ? obj.getAmountReturned() : 0d);
//            pst.setString(10, obj.getHash());
//            pst.setInt(11, obj.getClient().getId());
//            pst.setInt(12, obj.getSeller().getId());
//            pst.setInt(13, obj.getYear());
//            pst.setString(14, obj.getKey());                                              // <-- NOVO
//            pst.setString(15, obj.getNote());                                             // <-- NOVO
//            pst.setInt(16, obj.getId());
//            pst.execute();
//            return true;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao atualizar Order: " + e.getMessage());
//        }
//        return false;
//    }
//
//    public void delete(int id) {
//        try {
//            // 1 passo
//            String sql = "DELETE FROM orders WHERE id =?";
//            pst = conn.prepareStatement(sql);
//            pst.setInt(1, id);
//            pst.execute();
//            JOptionPane.showMessageDialog(null, "Order excluido com Sucesso!!");
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao excluir Order: " + e.getMessage());
//        }
//    }
//
//    public Order getId(int id) {
//        try {
//            // 1 passo
//            String sql = "SELECT * FROM orders WHERE id =? ";
//            pst = conn.prepareStatement(sql);
//            pst.setInt(1, id);
//            rs = pst.executeQuery();
//            Order obj = new Order();
//            if (rs.next()) {
//                obj = formatObject(rs);
//            }
//            return obj;
//            // 2 passo
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Order: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public Order getFromNumber(int number) {
//        try {
//            // 1 passo
//            String sql = "SELECT * FROM orders WHERE number =? ";
//            pst = conn.prepareStatement(sql);
//            pst.setInt(1, number);
//            rs = pst.executeQuery();
//            Order obj = new Order();
//            if (rs.next()) {
//                obj = formatObject(rs);
//            }
//            return obj;
//            // 2 passo
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao fazer consulta do Order: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public List<Order> list(String where) {
//        List<Order> list = new ArrayList<>();
//        try {
//            String sql = "SELECT * FROM orders " + where;
//            pst = this.conn.prepareStatement(sql);
//            rs = pst.executeQuery();
//            Order obj;// = new Order();
//            while (rs.next()) {
//                obj = formatObj(rs);
//                list.add(obj);
//            }
//            return list;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de Order11: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public List<Order> filterDate(LocalDate dateFrom, LocalDate dateTo, String where) {
//        List<Order> list = new ArrayList<>();
//        try {
//            String sql = "SELECT * FROM orders WHERE created_at between ? and ? " + where;
//            PreparedStatement ptmt = this.conn.prepareStatement(sql);
//            ptmt.setString(1, dateFrom.toString() + " 00:00:00");
//            ptmt.setString(2, dateTo.toString() + " 23:59:59");
//            rs = ptmt.executeQuery();
//
//            System.out.println("date:" + dateFrom + " 00:00:00");
//            System.out.println("date:" + dateTo + " 23:59:00");
//
//            Order obj;// = new Order();
//            while (rs.next()) {
//                obj = formatObj(rs);
//                list.add(obj);
//            }
//            return list;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao pegar filterDate de Order: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public List<Order> filter(String txt, String where) {
//        List<Order> list = new ArrayList<>();
//        try {
//            String sql = "SELECT * FROM orders WHERE prod.description LIKE ? " + where;
//            PreparedStatement ptmt = this.conn.prepareStatement(sql);
//            ptmt.setString(1, "%" + txt + "%");
//            rs = ptmt.executeQuery();
//
//            Order obj;// = new Order();
//            while (rs.next()) {
//                obj = formatObj(rs);
//                list.add(obj);
//            }
//            return list;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao pegar lista de Order: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public int getNextNumberOrder() {
//        try {
//            int nextNumber = 1;
//            String sql = "SELECT max(number)as number FROM orders";
//            PreparedStatement ptmt = this.conn.prepareStatement(sql);
//            rs = ptmt.executeQuery();
//
////            Order obj = new Order();
//            while (rs.next()) {
////                obj.setNumber();
//                nextNumber = rs.getInt("number") + 1;
//            }
//            return nextNumber;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao pegar o proximo id: " + e.getMessage());
//        }
//        return 0;
//    }
//
//    public int getLastIdOrder() {
//        try {
//            int id = 0;
//            String sql = "SELECT max(id) id FROM orders";
//            pst = this.conn.prepareStatement(sql);
//            rs = pst.executeQuery();
////            Order obj = new Order();
//            while (rs.next()) {
////                obj.setId(rs.getInt("id"));
//                id = rs.getInt("id");
//            }
//            return id;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao pegar o ultimo insert id: " + e.getMessage());
//        }
//        return 0;
//    }
//
//    public String getPreviousHash() {
//        try {
//            String previousHash = null;
//            String sql = "SELECT max(id) as id FROM orders";
//            pst = this.conn.prepareStatement(sql);
//            rs = pst.executeQuery();
//
////            Order obj = new Order();
//            while (rs.next()) {
////                obj.setHash(rs.getString("hash"));
//                previousHash = rs.getString("hash");
//            }
//            return previousHash;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao pegar o hash id: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public Order formatObject(ResultSet rs) {
//        try {
//            Order obj = new Order();
//            Clients client;// = new Clients();
//            ClientDao clientDao = new ClientDao();
//            client = clientDao.findById(rs.getInt("client_id"));
//            User seller;// = new User();
//            UserDao sellerDao = new UserDao();
//            seller = sellerDao.findById(rs.getInt("user_id"));
//
//            List<ProductOrder> products;
//            ProductOrderDao pDao = new ProductOrderDao();
//            products = pDao.listProductFromOrderId(rs.getInt("id"));
//
////            Payment payment = new Payment();
//            obj.setId(rs.getInt("id"));
//            obj.setStatus(rs.getInt("status"));
//            obj.setDatecreate(rs.getString("datecreate"));
//            obj.setNumber(rs.getInt("number"));
//            obj.setPrefix(rs.getString("prefix"));
//            obj.setClient(client);
//            obj.setSeller(seller);
//            obj.setProducts(products);
//            obj.setTotal(rs.getDouble("total"));
//            obj.setSubTotal(rs.getDouble("sub_total"));
//            obj.setAmountReturned(rs.getDouble("amount_returned"));
//            obj.setPayTotal(rs.getDouble("pay_total"));
//            obj.setHash(rs.getString("hash"));
//            obj.setYear(rs.getInt("year"));
//            obj.setTotalTaxe(rs.getDouble("total_taxe"));
//            obj.setKey(rs.getString("key"));
//            obj.setNote(rs.getString("note"));
//            return obj;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao formatar obj Order: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public Order formatObj(ResultSet rs) {
//        try {
//            Order obj = new Order();
//            Clients client;// = new Clients();
//            ClientDao clientDao = new ClientDao();
//            client = clientDao.findById(rs.getInt("client_id"));
//            User seller;// = new User();
//            UserDao sellerDao = new UserDao();
//            seller = sellerDao.findById(rs.getInt("user_id"));
//
////            List<ProductOrder> products;
////            ProductOrderDao pDao = new ProductOrderDao();
////            products = pDao.listProductFromOrderId(rs.getInt("id"));
////            Payment payment = new Payment();
//            obj.setId(rs.getInt("id"));
//            obj.setStatus(rs.getInt("status"));
//            obj.setDatecreate(rs.getString("datecreate"));
//            obj.setNumber(rs.getInt("number"));
//            obj.setPrefix(rs.getString("prefix"));
//            obj.setClient(client);
//            obj.setSeller(seller);
////            obj.setProducts(products);
//            obj.setTotal(rs.getDouble("total"));
//            obj.setSubTotal(rs.getDouble("sub_total"));
//            obj.setAmountReturned(rs.getDouble("amount_returned"));
//            obj.setPayTotal(rs.getDouble("pay_total"));
//            obj.setHash(rs.getString("hash"));
//            obj.setYear(rs.getInt("year"));
//
//            obj.setTotalTaxe(rs.getDouble("total_taxe"));
//            obj.setKey(rs.getString("key"));
//            obj.setNote(rs.getString("note"));
//            return obj;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Erro ao formatar obj Order: " + e.getMessage());
//        }
//        return null;
//    }
//
//}
