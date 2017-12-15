package com.vates.eng.ha.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.eng.ha.persistence.domain.User;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByLoginIgnoringCaseAndPasswordAndRealmName(String login, String password, String name);

}
