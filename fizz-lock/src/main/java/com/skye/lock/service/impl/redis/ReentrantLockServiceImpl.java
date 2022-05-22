package com.skye.lock.service.impl.redis;

import com.skye.lock.core.key.LockKey;
import com.skye.lock.core.key.RedisLockKey;
import com.skye.lock.exception.LockFailException;
import com.skye.lock.service.LockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 可重入锁加锁服务
 * @author TanRq
 *
 */
public class ReentrantLockServiceImpl implements LockService {
	
	@Qualifier("lockRedissonClient")
	@Autowired
	private RedissonClient lockRedissonClient;
	
	private RedisLockKey lockKey;
	
	private RLock lock;
	
	@Override
	public void setLockKey(LockKey lockKey) {
		this.lockKey=(RedisLockKey) lockKey;
	}

	@Override
	public void lock() throws Exception {
		
		this.lock = lockRedissonClient.getLock(lockKey.getKeyList().get(0));
		
		if(lockKey.getLeaseTime()==-1&&lockKey.getWaitTime()==-1) {
			lock.lock();
			return;
		}
		if(lockKey.getLeaseTime()!=-1&&lockKey.getWaitTime()==-1) {
			lock.lock(lockKey.getLeaseTime(),lockKey.getTimeUnit());
			return;
		}
		if(lockKey.getLeaseTime()!=-1&&lockKey.getWaitTime()!=-1) {
			boolean isLock = lock.tryLock(lockKey.getWaitTime(),lockKey.getLeaseTime(),lockKey.getTimeUnit());
			if(!isLock) {
				throw new LockFailException("加锁失败");
			}
			return;
		}
		
		lock.lock();
	}

	@Override
	public void release() {
		//this.lock.unlock();
		this.lock.forceUnlock();
	}

	

}
