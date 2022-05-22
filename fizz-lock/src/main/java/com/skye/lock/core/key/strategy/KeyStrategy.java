package com.skye.lock.core.key.strategy;


import com.skye.lock.constant.LockCommonConstant;
import com.skye.lock.core.key.LockKey;
import com.skye.lock.enumeration.LockScheme;
import com.skye.lock.exception.KeyBuilderException;

import java.lang.reflect.Method;

/**
 * @BelongsPackage: io.gitee.tooleek.lock.spring.boot.core.strategy
 * @Author: zsx
 * @CreateTime: 2019-04-17 10:01
 * @Description:
 */
public abstract class KeyStrategy {

	protected LockScheme scheme;
	protected String className;
	protected String methodName;
	protected Method realMethod;
	protected Object[] args;

	public KeyStrategy(LockScheme scheme, String className, String methodName, Method realMethod, Object[] args) {
		this.scheme = scheme;
		this.className = className;
		this.methodName = methodName;
		this.realMethod = realMethod;
		this.args = args;
	}

	public String getSimpleClassName() {
		return this.className.split("\\.")[this.className.split("\\.").length - 1];
	}

	/**
	 * 包装锁的key标记
	 * 
	 * @param valTag
	 * @return
	 */
	public String wrapKeyTag(String valTag) {
		return getSimpleClassName() + LockCommonConstant.KEY_SPLIT_MARK + this.methodName
				+ LockCommonConstant.KEY_SPLIT_MARK + valTag;
	}

	public abstract LockKey.Builder generateBuilder() throws KeyBuilderException;

}
