package com.vates.eng.ha.persistence.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.eng.ha.persistence.paging.Page;
import com.vates.eng.ha.persistence.paging.PageConverter;
import com.vates.eng.ha.persistence.service.BaseService;

/**
 * @author
 *
 * @param <T>
 * @param <ID>
 */
@Transactional(propagation = Propagation.NESTED)
@NoRepositoryBean
public abstract class AbstractBaseService<T, ID extends Serializable> implements BaseService<T, ID> {

    protected abstract JpaRepository<T, ID> getRepository();

    protected abstract PageConverter<T> getPageConverter();

    @Override
    public T create(T entity) {
        return getRepository().saveAndFlush(entity);
    }

    @Override
    public void delete(ID id) {
        getRepository().delete(id);

    }

    @Override
    public void delete(T entity) {
        getRepository().delete(entity);

    }

    @Override
    @Transactional(readOnly = true)
    public T find(ID id) {
        return getRepository().findOne(id);
    }

    @Override
    public List<T> findAll() {
        return getRepository().findAll();
    }

    @Override
    public T update(T entity) {
        return getRepository().save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> findByPage(int number, int size) {
        Pageable pageable = new PageRequest(number, size);
        Page<T> page = getPageConverter().convertFrom(getRepository().findAll(pageable));

        return page;
    }

}
