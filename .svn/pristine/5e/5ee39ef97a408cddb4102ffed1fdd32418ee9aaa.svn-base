package com.vates.facpro.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.Notification;

@Repository
@Transactional(readOnly = false)
public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findById(Long id);
	
	List<Notification> findByType(Integer type);

}
