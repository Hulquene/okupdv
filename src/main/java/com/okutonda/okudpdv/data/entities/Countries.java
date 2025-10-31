package com.okutonda.okudpdv.data.entities;

import com.okutonda.okudpdv.data.dao.CountryDao;
import jakarta.persistence.*;
import java.util.*;

/**
 * Countries como "enum virtual" com cache estático
 */
@Entity
@Table(name = "countries") // Nome da tabela no banco
public class Countries {

    private static List<Countries> CACHE = null;
    private static final Object LOCK = new Object();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "iso2", length = 2, unique = true)
    private String iso2;

    @Column(name = "iso3", length = 3, unique = true)
    private String iso3;

    @Column(name = "short_name", length = 100)
    private String short_name;

    @Column(name = "long_name", length = 100)
    private String long_name;

    @Column(name = "un_member", length = 3)
    private String un_member;

    @Column(name = "numcode", length = 10)
    private String numcode;

    @Column(name = "calling_code", length = 10)
    private String calling_code;

    @Column(name = "cctld", length = 10)
    private String cctld;

    // ==========================================================
    // 🔹 CACHE ESTÁTICO - Funciona como Enum
    // ==========================================================
    /**
     * Carrega todos os países em cache (uma vez por execução)
     */
    public static void loadCache() {
        synchronized (LOCK) {
            if (CACHE == null) {
                CountryDao dao = new CountryDao();
                CACHE = dao.findAll();
                System.out.println("✅ Cache de países carregado: " + CACHE.size() + " países");
            }
        }
    }

    /**
     * Limpa o cache (útil para testes ou atualizações)
     */
    public static void clearCache() {
        synchronized (LOCK) {
            CACHE = null;
        }
    }

    /**
     * Retorna todos os países (como se fosse enum.values())
     */
    public static List<Countries> getAll() {
        if (CACHE == null) {
            loadCache();
        }
        return Collections.unmodifiableList(CACHE);
    }

    /**
     * Busca por ISO2 (como se fosse enum.valueOf())
     */
    public static Countries getByIso2(String iso2) {
        if (CACHE == null) {
            loadCache();
        }
        return CACHE.stream()
                .filter(c -> c.getIso2() != null && c.getIso2().equalsIgnoreCase(iso2))
                .findFirst()
                .orElse(null);
    }

    /**
     * Busca por ISO3
     */
    public static Countries getByIso3(String iso3) {
        if (CACHE == null) {
            loadCache();
        }
        return CACHE.stream()
                .filter(c -> c.getIso3() != null && c.getIso3().equalsIgnoreCase(iso3))
                .findFirst()
                .orElse(null);
    }

    /**
     * Busca por nome (fuzzy match)
     */
    public static Countries getByName(String name) {
        if (CACHE == null) {
            loadCache();
        }
        if (name == null) {
            return null;
        }

        String nameLower = name.toLowerCase();
        return CACHE.stream()
                .filter(c -> c.getLong_name() != null
                && (c.getLong_name().toLowerCase().contains(nameLower)
                || (c.getShort_name() != null && c.getShort_name().toLowerCase().contains(nameLower))))
                .findFirst()
                .orElse(null);
    }

    /**
     * Países mais comuns (para dropdowns)
     */
    public static List<Countries> getCommonCountries() {
        if (CACHE == null) {
            loadCache();
        }

        // Países mais usados em Angola
        String[] commonCodes = {"AO", "PT", "BR", "US", "GB", "FR", "ES", "ZA", "CN"};

        return Arrays.stream(commonCodes)
                .map(Countries::getByIso2)
                .filter(Objects::nonNull)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Filtra países por nome
     */
    public static List<Countries> filter(String search) {
        if (CACHE == null) {
            loadCache();
        }

        if (search == null || search.trim().isEmpty()) {
            return getAll();
        }

        String searchLower = search.toLowerCase();
        return CACHE.stream()
                .filter(c -> (c.getLong_name() != null && c.getLong_name().toLowerCase().contains(searchLower))
                || (c.getShort_name() != null && c.getShort_name().toLowerCase().contains(searchLower))
                || (c.getIso2() != null && c.getIso2().toLowerCase().contains(searchLower))
                || (c.getIso3() != null && c.getIso3().toLowerCase().contains(searchLower)))
                .collect(java.util.stream.Collectors.toList());
    }

    // ==========================================================
    // 🔹 GETTERS/SETTERS
    // ==========================================================
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public String getUn_member() {
        return un_member;
    }

    public void setUn_member(String un_member) {
        this.un_member = un_member;
    }

    public String getNumcode() {
        return numcode;
    }

    public void setNumcode(String numcode) {
        this.numcode = numcode;
    }

    public String getCalling_code() {
        return calling_code;
    }

    public void setCalling_code(String calling_code) {
        this.calling_code = calling_code;
    }

    public String getCctld() {
        return cctld;
    }

    public void setCctld(String cctld) {
        this.cctld = cctld;
    }

    @Override
    public String toString() {
        return long_name + " (" + iso2 + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Countries country = (Countries) obj;
        return id == country.id || (iso2 != null && iso2.equals(country.iso2));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, iso2);
    }
}
