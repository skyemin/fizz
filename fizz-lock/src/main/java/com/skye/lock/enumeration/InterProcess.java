package com.skye.lock.enumeration;

/**
 * zookeeper锁类型
 *
 * @author TanRq
 */
public enum InterProcess {

	/**
	 * 分布式可重入排它锁
	 */
	MUTEX,
	/**
	 * 分布式排它锁
	 */
	SEMAPHORE_MUTEX,
	/**
	 * 分布式读锁
	 */
	READ,
	/**
	 * 分布式写锁
	 */
	WRITE,
	/**
	 * 将多个锁作为单个实体管理的容器
	 */
	MULTI

}
