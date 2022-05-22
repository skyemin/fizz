package com.skye.lock.core.key.strategy;


import com.skye.lock.core.key.LockKey;
import com.skye.lock.core.key.RedisLockKey;
import com.skye.lock.enumeration.LockScheme;
import com.skye.lock.exception.KeyBuilderException;

import java.lang.reflect.Method;

public class ClassKeyStrategy extends KeyStrategy {

	public ClassKeyStrategy(LockScheme scheme, String className, String methodName, Method realMethod, Object[] args) {
		super(scheme,className, methodName, realMethod, args);
	}

	@Override
	public LockKey.Builder generateBuilder() throws KeyBuilderException {
		LockKey.Builder keyBuilder;
		keyBuilder = RedisLockKey.newRedisKeyBuilder();
		keyBuilder.appendKey(wrapKeyTag(new StringBuilder(getSimpleClassName()).append(".").append(methodName).toString()));
		
		return keyBuilder;
	}

}
