package com.vates.eng.ha.persistence.service;

import java.io.Serializable;
import java.util.List;

import com.vates.eng.ha.persistence.paging.Page;

/**
 * The Interface BaseService.
 * 
 * @param <T>
 *            the generic type
 * @param <ID>
 *            the generic type
 */
public interface BaseService<T, ID extends Serializable> {

    /**
     * Creates the.
     * 
     * @param entity
     *            the entity
     * @return the t
     */
    public T create(T entity);

    /**
     * Delete.
     * 
     * @param id
     *            the id
     */
    public void delete(ID id);

    /**
     * Delete.
     * 
     * @param entity
     *            the entity
     */
    public void delete(T entity);

    /**
     * Find.
     * 
     * @param id
     *            the id
     * @return the t
     */
    public T find(ID id);

    /**
     * Find all.
     * 
     * @return the iterable
     */
    public List<T> findAll();

    /**
     * Update.
     * 
     * @param entity
     *            the entity
     * @return the t
     */
    public T update(T entity);
    
    /**
     * Find by page.
     *
     * @param number the page number
     * @param size the size
     * @return the page
     */
    public Page<T> findByPage(int number, int size);

}
