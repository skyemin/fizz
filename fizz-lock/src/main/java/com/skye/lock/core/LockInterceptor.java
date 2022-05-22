package com.skye.lock.core;

import com.skye.lock.annotation.Key;
import com.skye.lock.annotation.RLock;
import com.skye.lock.core.key.KeyStrategyContext;
import com.skye.lock.core.key.LockKey;
import com.skye.lock.core.key.RedisLockKey;
import com.skye.lock.core.key.strategy.*;
import com.skye.lock.enumeration.LockScheme;
import com.skye.lock.factory.FactoryBean;
import com.skye.lock.service.LockService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 锁拦截器
 *
 * @author TanRq
 */
@Order(1)
@Aspect
@Component
public class LockInterceptor {

	Logger logger = LoggerFactory.getLogger(LockInterceptor.class);

	@Autowired
	private FactoryBean factoryBean;

	private ThreadLocal<Map<String, LockService>> localLockServiceMap = new ThreadLocal<>();

	/**
	 * 获取LockService的Key,用于存放在localLockServiceMap中
	 * 
	 * @param className  类名
	 * @param methodName 方法名
	 * @return
	 */
	private String getLockServiceKey(String className, String methodName) {
		String simpleClassName = className.split("\\.")[className.split("\\.").length - 1];
		return simpleClassName + "." + methodName;
	}

	@Around(value = "@annotation(com.skye.lock.annotation.RLock)")
	public Object lockHandle(ProceedingJoinPoint joinPoint) throws Throwable {

		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method targetMethod = methodSignature.getMethod();
		Method realMethod = joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(),
				targetMethod.getParameterTypes());

		LockScheme scheme = getLockScheme(realMethod);

		Object[] args = joinPoint.getArgs();

		String className = joinPoint.getTarget().getClass().getName();
		String methodName = methodSignature.getName();

		KeyStrategy keyStrategy = getKeyStrategy(scheme, className, methodName, realMethod, args);

		LockKey.Builder keyBuilder = new KeyStrategyContext(keyStrategy).generateBuilder();

		LockKey lockKey = null;
		if (keyBuilder instanceof RedisLockKey.RedisKeyBuilder) {
			lockKey = buildRedisLockKey(realMethod, (RedisLockKey.RedisKeyBuilder) keyBuilder);
		}
		LockService lockService = getLockService(scheme, realMethod);
		lockService.setLockKey(lockKey);

		String lockServiceKey = getLockServiceKey(className, methodName);
		Map<String, LockService> lockServiceMap = localLockServiceMap.get();
		if (lockServiceMap == null) {
			lockServiceMap = new HashMap<>();
			localLockServiceMap.set(lockServiceMap);
		}
		lockServiceMap.put(lockServiceKey, lockService);

		lockService.lock();

		logger.debug("============================================================");
		logger.debug("===> 执行线程：[{}]  加锁任务：开始",Thread.currentThread().getName());
		logger.debug("===> 执行线程：[{}]  执行操作：加锁",Thread.currentThread().getName());
		logger.debug("===> 执行线程：[{}]  加锁方法：{}",Thread.currentThread().getName(),lockServiceKey);
		
		return joinPoint.proceed();
	}

	private LockService getLockService(LockScheme scheme, Method realMethod) {
		if (scheme == LockScheme.REDIS) {
			if (null != realMethod.getAnnotation(RLock.class)) {
				RLock lock = realMethod.getAnnotation(RLock.class);
				return factoryBean.getFactory(scheme).getService(lock.lockType());
			}
		}
		return null;
	}

	private LockScheme getLockScheme(Method realMethod) {
		if (null != realMethod.getAnnotation(RLock.class)) {
			return LockScheme.REDIS;
		}
		return LockScheme.ZOOKEEPER;
	}


	private RedisLockKey buildRedisLockKey(Method realMethod, RedisLockKey.RedisKeyBuilder keyBuilder) {
		RLock rlock = realMethod.getAnnotation(RLock.class);
		return keyBuilder.leaseTime(rlock.leaseTime()).waitTime(rlock.waitTime()).timeUnit(rlock.timeUnit()).build();
	}

	private KeyStrategy getKeyStrategy(LockScheme scheme, String className, String methodName, Method realMethod,
			Object[] args) {
		// 参数锁
		for (int i = 0; i < realMethod.getParameters().length; i++) {
			if (realMethod.getParameters()[i].isAnnotationPresent(Key.class)) {
				return new ParameterKeyStrategy(scheme, className, methodName, realMethod, args);
			}
		}
		// 方法锁
		if (null != realMethod.getAnnotation(Key.class)) {
			return new MethodKeyStrategy(scheme, className, methodName, realMethod, args);
		}
		// 属性锁
		for (int i = 0; i < args.length; i++) {
			Object obj = args[i];
			if(obj==null) {continue;}
			@SuppressWarnings("rawtypes")
			Class objectClass = obj.getClass();
			Field[] fields = objectClass.getDeclaredFields();
			for (Field field : fields) {
				if (null != field.getAnnotation(Key.class)) {
					return new PropertiesKeyStrategy(scheme, className, methodName, realMethod, args);
				}
			}
		}
		// 类名和方法名作为key的锁
		return new ClassKeyStrategy(scheme, className, methodName, realMethod, args);
	}

	@AfterReturning(value = "@annotation(com.skye.lock.annotation.RLock)")
	public void afterReturning(JoinPoint joinPoint) {
		release(joinPoint);
	}

	@AfterThrowing(value = "@annotation(com.skye.lock.annotation.RLock)")
	public void afterThrowing(JoinPoint joinPoint) {
		release(joinPoint);
	}

	/**
	 * 释放 锁和LockService
	 * 
	 * @param joinPoint
	 */
	private void release(JoinPoint joinPoint) {
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = ((MethodSignature) joinPoint.getSignature()).getName();

		String lockServiceKey = getLockServiceKey(className, methodName);
		Map<String, LockService> lockServiceMap = localLockServiceMap.get();

		logger.debug("===> 执行线程：[{}]  执行操作：解锁",Thread.currentThread().getName());
		logger.debug("===> 执行线程：[{}]  解锁方法：{}",Thread.currentThread().getName(),lockServiceKey);
		lockServiceMap.get(lockServiceKey).release();

		logger.debug("===> 执行线程：[{}]  释放资源：Service",Thread.currentThread().getName());
		lockServiceMap.remove(lockServiceKey);

		if (lockServiceMap.isEmpty()) {
			logger.debug("===> 执行线程：[{}]  释放资源：ServiceMap",Thread.currentThread().getName());
			localLockServiceMap.remove();
		}
		
		logger.debug("===> 执行线程：[{}]  加锁任务：结束",Thread.currentThread().getName());
		logger.debug("============================================================");
		// 原始方法
		// localLockService.get().release();
		// localLockService.remove();
	}

}
