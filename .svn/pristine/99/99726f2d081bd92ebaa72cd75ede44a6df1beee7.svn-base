package com.vates.facpro.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.Kanav;

@Repository
@Transactional(readOnly = true)
public interface KanavRepository extends JpaRepository<Kanav, Long>{

	Kanav findById(Long id);
}
