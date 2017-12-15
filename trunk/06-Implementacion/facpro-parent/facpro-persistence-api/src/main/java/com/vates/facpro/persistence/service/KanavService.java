package com.vates.facpro.persistence.service;

import com.vates.facpro.persistence.domain.Kanav;

/**
 * @author Manuel Cabrera
 * 
 */
public interface KanavService extends BaseService<Kanav, Long> {

	Kanav findById(long id);
	

}
