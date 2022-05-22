package com.skye.lock.service.impl.redis;

import com.skye.lock.core.key.LockKey;
import com.skye.lock.core.key.RedisLockKey;
import com.skye.lock.exception.LockFailException;
import com.skye.lock.service.LockService;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 联锁操作服务
 * @author TanRq
 *
 */
public class MultiLockServiceImpl implements LockService {

	@Qualifier("lockRedissonClient")
	@Autowired
	private RedissonClient lockRedissonClient;
	
	private RedisLockKey lockKey;
	
	private RedissonMultiLock lock;
	
	@Override
	public void setLockKey(LockKey lockKey) {
		this.lockKey=(RedisLockKey) lockKey;
	}

	@Override
	public void lock() throws Exception {
		RLock[] lockList = new RLock[lockKey.getKeyList().size()];
		for(int i=0;i<lockKey.getKeyList().size();i++) {
			lockList[i]=lockRedissonClient.getLock(lockKey.getKeyList().get(i));
		}
		
		lock=new RedissonMultiLock(lockList);
		
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
		lock.unlock();
	}

}
