package com.skye.lock.core.key.strategy;


import com.skye.lock.annotation.Key;
import com.skye.lock.core.key.LockKey;
import com.skye.lock.core.key.RedisLockKey;
import com.skye.lock.enumeration.LockScheme;
import com.skye.lock.exception.KeyBuilderException;

import java.lang.reflect.Method;

/**
 * @BelongsPackage: io.gitee.tooleek.lock.spring.boot.core.strategy
 * @Author: zsx
 * @CreateTime: 2019-04-17 10:00
 * @Description: 参数锁处理
 */
public class ParameterKeyStrategy extends KeyStrategy {

	public ParameterKeyStrategy(LockScheme scheme, String className, String methodName, Method realMethod,
								Object[] args) {
		super(scheme, className, methodName, realMethod, args);
	}

	@Override
	public LockKey.Builder generateBuilder() throws KeyBuilderException {
		
		LockKey.Builder keyBuilder;
		keyBuilder = RedisLockKey.newRedisKeyBuilder();
		for (int i = 0; i < realMethod.getParameters().length; i++) {
			if (realMethod.getParameters()[i].isAnnotationPresent(Key.class)) {
				keyBuilder.appendKey(wrapKeyTag(args[i].toString()));
			}
		}
		return keyBuilder;
	}

}
