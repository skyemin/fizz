package com.skye.lock.core.key.strategy;

import com.skye.lock.annotation.Key;
import com.skye.lock.core.key.LockKey;
import com.skye.lock.core.key.RedisLockKey;
import com.skye.lock.enumeration.LockScheme;
import com.skye.lock.exception.KeyBuilderException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @BelongsPackage: io.gitee.tooleek.lock.spring.boot.core.strategy
 * @Author: zsx
 * @CreateTime: 2019-04-17 10:00
 * @Description: 属性锁处理
 */
public class PropertiesKeyStrategy extends KeyStrategy {

	public PropertiesKeyStrategy(LockScheme scheme, String className, String methodName, Method realMethod,
								 Object[] args) {
		super(scheme, className, methodName, realMethod, args);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public LockKey.Builder generateBuilder() throws KeyBuilderException {
		LockKey.Builder keyBuilder;
		keyBuilder = RedisLockKey.newRedisKeyBuilder();
		for (int i = 0; i < args.length; i++) {
			Object obj = args[i];
			Class objectClass = obj.getClass();
			Field[] fields = objectClass.getDeclaredFields();
			for (Field field : fields) {
				if (null != field.getAnnotation(Key.class)) {
					field.setAccessible(true);
					try {
						keyBuilder.appendKey(wrapKeyTag(field.get(obj).toString()));
					} catch (IllegalArgumentException e) {
						throw new KeyBuilderException("生成builder失败", e);
					} catch (IllegalAccessException e) {
						throw new KeyBuilderException("生成builder失败", e);
					}
				}
			}
		}
		return keyBuilder;
	}

}
