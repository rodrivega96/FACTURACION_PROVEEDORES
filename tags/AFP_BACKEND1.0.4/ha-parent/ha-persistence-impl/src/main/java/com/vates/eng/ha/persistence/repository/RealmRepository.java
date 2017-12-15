package com.vates.eng.ha.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.eng.ha.persistence.domain.Realm;

/**
 * @author 
 *
 */
@Repository
@Transactional(readOnly = true)
public interface RealmRepository extends JpaRepository<Realm, Long> {

}
