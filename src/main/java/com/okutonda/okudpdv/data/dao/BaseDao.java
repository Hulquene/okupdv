/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.connection.DatabaseProvider;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.apache.poi.ss.formula.functions.T;

/**
 * Base genérica para DAOs — centraliza operações SQL comuns. Classe base para
 * os DAOs — centraliza a execução de queries e updates.
 *
 * @param <T> Tipo de entidade manipulada pelo DAO
 * @author Hulquene
 */
public abstract class BaseDao<T> implements GenericDao<T> {

    protected List<T> executeQuery(String sql, Function<ResultSet, T> mapper, Object... params) {
        List<T> results = new ArrayList<>();
        try (Connection conn = DatabaseProvider.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

            setParams(pst, params);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    T obj = mapper.apply(rs);
                    if (obj != null) {
                        results.add(obj);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao executar query: " + e.getMessage());
        }
        return results;
    }

    protected T findOne(String sql, Function<ResultSet, T> mapper, Object... params) {
        List<T> list = executeQuery(sql, mapper, params);
        return list.isEmpty() ? null : list.get(0);
    }

    protected boolean executeUpdate(String sql, Object... params) {
        try (Connection conn = DatabaseProvider.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

            setParams(pst, params);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao executar update: " + e.getMessage());
            return false;
        }
    }

    private void setParams(PreparedStatement pst, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            pst.setObject(i + 1, params[i]);
        }
    }

    /**
     * Executa uma query que retorna um único inteiro (ex: COUNT, SUM, etc.)
     */
    protected int executeScalarInt(String sql, Object... params) {
        try (var conn = DatabaseProvider.getConnection(); var pst = conn.prepareStatement(sql)) {

            // define parâmetros dinamicamente
            for (int i = 0; i < params.length; i++) {
                pst.setObject(i + 1, params[i]);
            }

            try (var rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1); // pega a primeira coluna do primeiro resultado
                }
            }

        } catch (Exception e) {
            System.err.println("[DB] Erro em executeScalarInt: " + e.getMessage());
        }
        return 0; // valor padrão se falhar ou não encontrar nada
    }

}
