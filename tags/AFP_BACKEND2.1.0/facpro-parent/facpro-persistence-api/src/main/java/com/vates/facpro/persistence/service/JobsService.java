package com.vates.facpro.persistence.service;

/**
 * @author
 * 
 */
public interface JobsService {

	void sendExpiredNotification();
	
	void sendDailyNotification();
	
	void sendBeforeExpireNotification();
	
	void sendAuthorizationNotification();
	
	void deleteArchivo();
	
	void closeOcs();
}
