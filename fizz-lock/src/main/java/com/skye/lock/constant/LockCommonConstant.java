package com.skye.lock.constant;

public interface LockCommonConstant {
	/**
	 * 默认客户端名字
	 */
	String LOCK = "Tooleek:Lock";
	/**
	 * 默认SSL实现方式：JDK
	 */
	String JDK = "JDK";
	/**
	 * 逗号
	 */
	String COMMA = ",";
	/**
	 * 冒号
	 */
	String COLON = ":";
	/**
	 * 斜线
	 */
	String OBLIQUE = "/";
	/**
	 * 分号
	 */
	String SEMICOLON = ";";
	/**
	 * redis默认URL前缀
	 */
	String REDIS_URL_PREFIX = "redis://";
	/**
	 * 锁的key的分隔符
	 */
	String KEY_SPLIT_MARK = ":";
	/**
	 * 锁的前缀
	 */
	String KEY_PREFIX = "tooleek" + KEY_SPLIT_MARK + "lock" + KEY_SPLIT_MARK + "key" + KEY_SPLIT_MARK;

}
