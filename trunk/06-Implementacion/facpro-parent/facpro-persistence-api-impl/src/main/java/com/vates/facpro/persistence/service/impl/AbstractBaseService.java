package com.vates.facpro.persistence.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.paging.Page;
import com.vates.facpro.persistence.paging.PageConverter;
import com.vates.facpro.persistence.service.BaseService;

/**
 * It defines an abstract implementation in order to be used as a base for CRUD services. This implementation is backed by Spring Data JPA
 * repositories.
 * 
 * @author Gaston Napoli
 *
 * @param <T>
 *            Entity datatype.
 * @param <ID>
 *            Entity ID datatype.
 */
@Transactional(propagation = Propagation.NESTED)
@NoRepositoryBean
public abstract class AbstractBaseService<T, ID extends Serializable> implements BaseService<T, ID> {

    protected abstract JpaRepository<T, ID> getRepository();

    protected abstract PageConverter<T> getPageConverter();

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.facpro.persistence.service.BaseService#create(java.lang.Object)
     */
    @Override
    public T create(T entity) {

        return getRepository().saveAndFlush(entity);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.facpro.persistence.service.BaseService#delete(java.io.Serializable)
     */
    @Override
    public void delete(ID id) {

        getRepository().delete(id);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.facpro.persistence.service.BaseService#delete(java.lang.Object)
     */
    @Override
    public void delete(T entity) {

        getRepository().delete(entity);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.facpro.persistence.service.BaseService#find(java.io.Serializable)
     */
    @Override
    @Transactional(readOnly = true)
    public T find(ID id) {

        return getRepository().findOne(id);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.facpro.persistence.service.BaseService#findAll()
     */
    @Override
    public List<T> findAll() {

        return getRepository().findAll();

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.facpro.persistence.service.BaseService#update(java.lang.Object)
     */
    @Override
    public T update(T entity) {

        return getRepository().save(entity);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.facpro.persistence.service.BaseService#findByPage(int, int)
     */
    @Override
    @Transactional(readOnly = true)
    public Page<T> findByPage(int number, int size) {

        Pageable pageable = new PageRequest(number, size);

        Page<T> page = getPageConverter().convertFrom(getRepository().findAll(pageable));

        return page;

    }
    
	@Override
	public  Object getUserDto(CacheManager cacheManager, String sessionId) {
		Object object = null;
		boolean loaded = false;
		int count = 0;
		while (!loaded && count <= 10) {
			try {
				count++;
				object = ((org.springframework.cache.CacheManager)cacheManager).getCache("defaultCache").get(sessionId)
						.get();
				loaded = true;
			} catch (Exception e) {

			}
		}
		return object;
	}

}
