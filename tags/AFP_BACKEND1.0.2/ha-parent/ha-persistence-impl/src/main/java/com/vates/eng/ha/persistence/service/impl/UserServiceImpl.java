package com.vates.eng.ha.persistence.service.impl;

import javax.inject.Inject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.eng.ha.persistence.domain.User;
import com.vates.eng.ha.persistence.paging.PageConverter;
import com.vates.eng.ha.persistence.repository.UserRepository;
import com.vates.eng.ha.persistence.service.UserService;

/**
 * @author
 *
 */
@Repository("userService")
@Transactional(propagation = Propagation.NESTED)
public class UserServiceImpl extends AbstractBaseService<User, Long> implements UserService {

    @Inject
    private UserRepository repository;

    @Inject
    private PageConverter<User> pageConverter;

    @Override
    @Transactional(readOnly = true)
    public User find(String login, String password, String name) {
        return repository.findByLoginIgnoringCaseAndPasswordAndRealmName(login, password, name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.persistence.service.impl.AbstractBaseService#getRepository()
     */
    @Override
    protected JpaRepository<User, Long> getRepository() {
        
        return repository;
    
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.persistence.service.impl.AbstractBaseService#getPageConverter()
     */
    @Override
    protected PageConverter<User> getPageConverter() {

        return pageConverter;

    }

}
