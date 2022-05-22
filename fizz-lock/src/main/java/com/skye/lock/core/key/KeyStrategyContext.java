package com.skye.lock.core.key;


import com.skye.lock.core.key.strategy.KeyStrategy;

public class KeyStrategyContext {
	
	private KeyStrategy keyStrategy;
	

	public KeyStrategyContext(KeyStrategy keyStrategy) {
		this.keyStrategy=keyStrategy;
	}
	
	public LockKey.Builder generateBuilder() {
		return this.keyStrategy.generateBuilder();
	}
	
}
