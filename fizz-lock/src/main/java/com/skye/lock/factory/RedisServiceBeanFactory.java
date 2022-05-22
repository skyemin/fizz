package com.skye.lock.factory;


import com.skye.lock.enumeration.LockType;
import com.skye.lock.exception.ServiceNotFoundException;
import com.skye.lock.service.LockService;
import com.skye.lock.service.impl.redis.*;
import com.skye.lock.util.SpringUtil;

import java.util.EnumMap;

public class RedisServiceBeanFactory implements ServiceBeanFactory {

	private static EnumMap<LockType, Class<?>> serviceMap = new EnumMap<>(LockType.class);

	static {
		serviceMap.put(LockType.REENTRANT, ReentrantLockServiceImpl.class);
		serviceMap.put(LockType.FAIR, FairLockServiceImpl.class);
		serviceMap.put(LockType.MULTI, MultiLockServiceImpl.class);
		serviceMap.put(LockType.READ, ReadLockServiceImpl.class);
		serviceMap.put(LockType.RED, RedLockServiceImpl.class);
		serviceMap.put(LockType.WRITE, WriteLockServiceImpl.class);
	}
	
	/**
	 * 根据锁类型获取相应的服务处理类
	 * 
	 * @param lockType
	 * @return
	 * @throws ServiceNotFoundException
	 */
	@Override
	public LockService getService(Object lockType) {
		LockService lockService = (LockService) SpringUtil.getBean(serviceMap.get(lockType));
		if (lockService == null) {
			throw new ServiceNotFoundException();
		}
		return lockService;
	}
}
