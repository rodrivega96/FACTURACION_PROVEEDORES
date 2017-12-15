package com.vates.facpro.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.Manager;

@Repository
@Transactional(readOnly = true)
public interface ManagerRepository extends JpaRepository<Manager, Long> {

	List<Manager> findByNombreIsLikeIgnoreCase(String nombre);

}