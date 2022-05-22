package com.skye.lock.core.key;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedisLockKey extends LockKey {

	private long leaseTime = -1;
	private long waitTime = -1;
	private TimeUnit timeUnit = TimeUnit.SECONDS;

	private RedisLockKey(List<String> keyList, long leaseTime, long waitTime, TimeUnit timeUnit) {
		super(keyList);
		this.leaseTime = leaseTime;
		this.waitTime = waitTime;
		this.timeUnit = timeUnit;
	}

	public long getLeaseTime() {
		return leaseTime;
	}

	public long getWaitTime() {
		return waitTime;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
	
	public static RedisKeyBuilder newRedisKeyBuilder() {
		return new RedisKeyBuilder();
	}

	public static class RedisKeyBuilder extends Builder {

		private long leaseTime = -1;
		private long waitTime = -1;
		private TimeUnit timeUnit = TimeUnit.SECONDS;

		public RedisKeyBuilder leaseTime(long leaseTime) {
			this.leaseTime = leaseTime;
			return this;
		}

		public RedisKeyBuilder waitTime(long waitTime) {
			this.waitTime = waitTime;
			return this;
		}

		public RedisKeyBuilder timeUnit(TimeUnit timeUnit) {
			this.timeUnit = timeUnit;
			return this;
		}

		public RedisLockKey build() {
			return new RedisLockKey(keyList, leaseTime, waitTime, timeUnit);
		}
	}
}
