package com.skye.lock.enumeration;


public enum LockScheme {

	REDIS("redis"), ZOOKEEPER("zookeeper");

	private String scheme;

	LockScheme(String scheme) {

	}
}
