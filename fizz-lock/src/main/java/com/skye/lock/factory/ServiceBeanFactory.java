package com.skye.lock.factory;


import com.skye.lock.service.LockService;

public interface ServiceBeanFactory {
	
	public LockService getService(Object lockType);

}
