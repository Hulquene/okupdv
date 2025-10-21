/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.okutonda.okudpdv.data.dao;

import java.util.List;

/**
 * Interface padrão para operações CRUD.
 *
 * @param <T> Tipo da entidade (ex: User, Client, Product)
 * @author Hul…
 */
public interface GenericDao<T> {

    boolean add(T entity);

    boolean update(T entity);

    boolean delete(int id);

    T findById(int id);

    List<T> findAll();
}
