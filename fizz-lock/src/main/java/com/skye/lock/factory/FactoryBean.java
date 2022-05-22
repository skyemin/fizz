package com.skye.lock.factory;


import com.skye.lock.enumeration.LockScheme;
import com.skye.lock.util.SpringUtil;

import java.util.EnumMap;

/**
 * 工厂Bean
 * 
 * @author TanRq
 *
 */
public class FactoryBean {

	private static EnumMap<LockScheme, Class<?>> factoryMap = new EnumMap<>(LockScheme.class);

	static {
		factoryMap.put(LockScheme.REDIS, RedisServiceBeanFactory.class);
	}
	
	public ServiceBeanFactory getFactory(LockScheme scheme) {
		return (ServiceBeanFactory) SpringUtil.getBean(factoryMap.get(scheme));
	}
	
}
