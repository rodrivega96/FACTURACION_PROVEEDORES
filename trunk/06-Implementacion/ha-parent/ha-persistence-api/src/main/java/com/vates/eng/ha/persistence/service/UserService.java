package com.vates.eng.ha.persistence.service;

import com.vates.eng.ha.persistence.domain.User;

/**
 * @author 
 *
 */
public interface UserService extends BaseService<User, Long> {

    public User find(String login, String password, String name);

}
