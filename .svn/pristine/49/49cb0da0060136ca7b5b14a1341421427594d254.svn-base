package com.vates.facpro.persistence.service;

import java.io.Serializable;
import java.util.List;

import com.vates.facpro.persistence.paging.Page;

/**
 * It defines a base interface for services implementing CRUD functionalities on entities.
 * 
 * @param <T>
 *            Entity datatype.
 * @param <ID>
 *            Entity ID datatype.
 */
public interface BaseService<T, ID extends Serializable> {

    /**
     * It creates
     * @param entity
     *            the entity
     * @return the t
     */
    T create(T entity);

    /**
     * 
     * @param id
     *            the id
     */
    void delete(ID id);

    /**
     * 
     * @param entity
     *            the entity
     */
    void delete(T entity);

    /**
     * 
     * @param id
     *            the id
     * @return the t
     */
    T find(ID id);

    /**
     * 
     * @return the iterable
     */
    List<T> findAll();

    /**
     * 
     * @param entity
     *            the entity
     * @return the t
     */
    T update(T entity);

    /**
     * Find by page.
     *
     * @param number
     *            the page number
     * @param size
     *            the size
     * @return the page
     */
    Page<T> findByPage(int number, int size);

}
