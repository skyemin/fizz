package com.skye.lock.service;


import com.skye.lock.core.key.LockKey;

/**
 * 锁服务
 * @author TanRq
 *
 */
public interface LockService {
	
	/**
	 * 添加key
	 * @param lockKey
	 */
	public void setLockKey(LockKey lockKey);
	/**
	 * 加锁
	 */
	public void lock() throws Exception;
	
	/**
	 * 解锁
	 */
	public void release();

}
