package com.skye.lock.core.key;


import com.skye.lock.constant.LockCommonConstant;

import java.util.ArrayList;
import java.util.List;

public class LockKey {
	
	private List<String> keyList=new ArrayList<>();
	
	protected LockKey(List<String> keyList) {
		this.keyList=keyList;
	}
	
	public List<String> getKeyList() {
		return keyList;
	}
	
	public static class Builder {
		
		protected List<String> keyList=new ArrayList<>();
		
		public Builder appendKey(String key) {
			keyList.add(LockCommonConstant.KEY_PREFIX + key);
			return this;
		}
		
		public boolean isEmptyKeys() {
			return this.keyList.isEmpty();
		}
	}
}
