package com.skye.lock.annotation;


import com.skye.lock.enumeration.LockType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;


@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface RLock {

	/**
	 * 锁类型
	 * @return
	 */
	LockType lockType() default LockType.REENTRANT;
	/**
	 * 加锁时间，超过该时长自动解锁，默认单位为：秒
	 * @return
	 */
	long leaseTime() default -1;
	/**
	 * 等待锁时间，默认单位：秒
	 * @return
	 */
	long waitTime() default -1;
	/**
	 * 锁时长单位
	 * @return
	 */
	TimeUnit timeUnit() default TimeUnit.SECONDS;
	
}
