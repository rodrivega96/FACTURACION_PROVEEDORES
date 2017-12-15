package com.vates.eng.ha.persistence.service.impl;

import javax.inject.Inject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.eng.ha.persistence.domain.Realm;
import com.vates.eng.ha.persistence.paging.PageConverter;
import com.vates.eng.ha.persistence.repository.RealmRepository;
import com.vates.eng.ha.persistence.service.RealmService;

/**
 * @author
 * 
 */
@Repository("realmService")
@Transactional(propagation = Propagation.NESTED)
public class RealmServiceImpl extends AbstractBaseService<Realm, Long> implements RealmService {

    @Inject
    private RealmRepository repository;

    @Inject
    private PageConverter<Realm> pageConverter;

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.persistence.service.impl.AbstractBaseService#getRepository()
     */
    @Override
    protected JpaRepository<Realm, Long> getRepository() {

        return repository;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.persistence.service.impl.AbstractBaseService#getPageConverter()
     */
    @Override
    protected PageConverter<Realm> getPageConverter() {

        return pageConverter;

    }

}
