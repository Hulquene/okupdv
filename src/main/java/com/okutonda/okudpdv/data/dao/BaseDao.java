//package com.okutonda.okudpdv.data.dao;
//
//import com.okutonda.okudpdv.data.connection.DatabaseProvider;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Function;
//
///**
// * Base genérica para DAOs — centraliza operações SQL comuns.
// *
// * Compatível com HikariCP e conexões externas (transações).
// *
// * @param <T> Tipo de entidade manipulada pelo DAO
// * @author Hulquene
// */
//public abstract class BaseDao<T> implements GenericDao<T>, AutoCloseable  {
//
//    protected Connection conn; // pode vir do pool ou de fora (transação)
//
//    // 🔹 Construtor padrão: usa conexão do pool automaticamente
//    public BaseDao() {
//        try {
//            this.conn = DatabaseProvider.getConnection();
//        } catch (SQLException e) {
//            throw new RuntimeException("[DB] Erro ao obter conexão do pool: " + e.getMessage(), e);
//        }
//    }
//
//    // 🔹 Construtor alternativo: usa conexão externa (transacional)
//    public BaseDao(Connection externalConn) {
//        this.conn = externalConn;
//    }
//
//    // ==========================================================
//    // 🔹 MÉTODOS GENÉRICOS
//    // ==========================================================
//    protected List<T> executeQuery(String sql, Function<ResultSet, T> mapper, Object... params) {
//        List<T> results = new ArrayList<>();
//        boolean closeAfter = false;
//
//        try {
//            Connection c = this.conn != null ? this.conn : DatabaseProvider.getConnection();
//            System.out.println("this.conn= " + this.conn);
//            if (this.conn == null) {
//                closeAfter = true;
//            }
//
//            try (PreparedStatement pst = c.prepareStatement(sql)) {
//                setParams(pst, params);
//                try (ResultSet rs = pst.executeQuery()) {
//                    while (rs.next()) {
//                        T obj = mapper.apply(rs);
//                        if (obj != null) {
//                            results.add(obj);
//                        }
//                    }
//                }
//            }
//
//            if (closeAfter) {
//                c.close(); // devolve ao pool se foi criada aqui
//            }
//        } catch (SQLException e) {
//            System.err.println("[DB] Erro em executeQuery: " + e.getMessage());
//        }
//
//        return results;
//    }
//
//    protected T findOne(String sql, Function<ResultSet, T> mapper, Object... params) {
//        List<T> list = executeQuery(sql, mapper, params);
//        return list.isEmpty() ? null : list.get(0);
//    }
//
//    protected boolean executeUpdate(String sql, Object... params) {
//        boolean closeAfter = false;
//
//        try {
//            Connection c = this.conn != null ? this.conn : DatabaseProvider.getConnection();
//            if (this.conn == null) {
//                closeAfter = true;
//            }
//
//            try (PreparedStatement pst = c.prepareStatement(sql)) {
//                setParams(pst, params);
//                pst.executeUpdate();
//            }
//
//            if (closeAfter) {
//                c.close();
//            }
//            return true;
//
//        } catch (SQLException e) {
//            System.err.println("[DB] Erro em executeUpdate: " + e.getMessage());
//            return false;
//        }
//    }
//
//    protected int executeScalarInt(String sql, Object... params) {
//        boolean closeAfter = false;
//
//        try {
//            Connection c = this.conn != null ? this.conn : DatabaseProvider.getConnection();
//            if (this.conn == null) {
//                closeAfter = true;
//            }
//
//            try (PreparedStatement pst = c.prepareStatement(sql)) {
//                setParams(pst, params);
//                try (ResultSet rs = pst.executeQuery()) {
//                    if (rs.next()) {
//                        return rs.getInt(1);
//                    }
//                }
//            }
//
//            if (closeAfter) {
//                c.close();
//            }
//
//        } catch (SQLException e) {
//            System.err.println("[DB] Erro em executeScalarInt: " + e.getMessage());
//        }
//        return 0;
//    }
//
//    private void setParams(PreparedStatement pst, Object... params) throws SQLException {
//        for (int i = 0; i < params.length; i++) {
//            pst.setObject(i + 1, params[i]);
//        }
//    }
//
//    @Override
//    public void close() {
//        System.err.println("chegou aqui ");
//        if (this.conn != null) {
//            try {
//                this.conn.close();
//                System.out.println("[DB] Conexão devolvida ao pool (" + this.getClass().getSimpleName() + ")");
//            } catch (SQLException e) {
//                System.err.println("[DB] Erro ao fechar conexão: " + e.getMessage());
//            }
//        }
//    }
//}
