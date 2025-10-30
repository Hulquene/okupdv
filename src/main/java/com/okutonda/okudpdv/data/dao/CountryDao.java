package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.data.entities.Countries;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO responsável pela gestão de países (Countries).
 *
 * Compatível com BaseDao e HikariCP. Fornece operações CRUD básicas e consultas
 * filtradas.
 *
 * @author Hulquene
 */
public class CountryDao extends BaseDao<Countries> {

    public CountryDao() {
        super(); // utiliza o pool de conexões via DatabaseProvider
    }

    public CountryDao(java.sql.Connection externalConn) {
        super(externalConn); // permite uso transacional externo
    }

    // ==========================================================
    // 🔹 Mapeamento ResultSet → Entidade
    // ==========================================================
    private Countries map(ResultSet rs) {
        try {
            Countries c = new Countries();
            c.setId(rs.getInt("id"));
            c.setIso2(rs.getString("iso2"));
            c.setIso3(rs.getString("iso3"));
            c.setShort_name(rs.getString("short_name"));
            c.setLong_name(rs.getString("long_name"));
            c.setUn_member(rs.getString("un_member"));
            c.setNumcode(rs.getString("numcode"));
            c.setCalling_code(rs.getString("calling_code"));
            c.setCctld(rs.getString("cctld"));
            return c;
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao mapear Countries: " + e.getMessage());
            return null;
        }
    }

    // ==========================================================
    // 🔹 CRUD
    // ==========================================================
    @Override
    public boolean add(Countries c) {
        String sql = """
            INSERT INTO countries (iso2, iso3, short_name, long_name, un_member, numcode, calling_code, cctld)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        return executeUpdate(sql,
                c.getIso2(),
                c.getIso3(),
                c.getShort_name(),
                c.getLong_name(),
                c.getUn_member(),
                c.getNumcode(),
                c.getCalling_code(),
                c.getCctld());
    }

    @Override
    public boolean update(Countries c) {
        String sql = """
            UPDATE countries
               SET iso2=?, iso3=?, short_name=?, long_name=?, un_member=?, numcode=?, calling_code=?, cctld=?
             WHERE id=?
        """;
        return executeUpdate(sql,
                c.getIso2(),
                c.getIso3(),
                c.getShort_name(),
                c.getLong_name(),
                c.getUn_member(),
                c.getNumcode(),
                c.getCalling_code(),
                c.getCctld(),
                c.getId());
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM countries WHERE id=?", id);
    }

    @Override
    public Countries findById(int id) {
        String sql = "SELECT * FROM countries WHERE id=?";
        return findOne(sql, this::map, id);
    }

    @Override
    public List<Countries> findAll() {
        String sql = "SELECT * FROM countries ORDER BY long_name ASC";
        return executeQuery(sql, this::map);
    }

    // ==========================================================
    // 🔹 Consultas adicionais
    // ==========================================================
    public List<Countries> filter(String txt) {
        String like = "%" + txt + "%";
        String sql = """
            SELECT * FROM countries
             WHERE short_name LIKE ? OR long_name LIKE ? OR iso2 LIKE ? OR iso3 LIKE ?
          ORDER BY long_name ASC
        """;
        return executeQuery(sql, this::map, like, like, like, like);
    }

    public Countries findByIso2(String iso2) {
        String sql = "SELECT * FROM countries WHERE iso2=?";
        return findOne(sql, this::map, iso2);
    }

    public Countries findByName(String name) {
        String sql = "SELECT * FROM countries WHERE long_name LIKE ? LIMIT 1";
        return findOne(sql, this::map, "%" + name + "%");
    }
}
