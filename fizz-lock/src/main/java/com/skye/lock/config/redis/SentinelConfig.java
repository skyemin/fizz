package com.skye.lock.config.redis;


import com.skye.lock.constant.LoadBalancerTypeConstant;
import com.skye.lock.constant.SubReadModeTypeConstant;

public class SentinelConfig {
	/**
	 * DNS监控间隔，单位：毫秒；用-1来禁用该功能。
	 */
	private long dnsMonitoringInterval = 5000;
	/**
	 * 主服务器的名称
	 */
	private String masterName;
	/**
	 * 哨兵节点地址，多个节点可以一次性批量添加。
	 */
	private String sentinelAddresses;
	/**
	 * 读取操作的负载均衡模式
	 */
	private String readMode = SubReadModeTypeConstant.SLAVE;
	/**
	 * 订阅操作的负载均衡模式
	 */
	private String subMode = SubReadModeTypeConstant.SLAVE;
	/**
	 * 负载均衡算法类的选择，默认：轮询调度算法
	 */
	private String loadBalancer = LoadBalancerTypeConstant.ROUND_ROBIN_LOAD_BALANCER;
	/**
	 * 默认权重值，当负载均衡算法是权重轮询调度算法时该属性有效
	 */
	private int defaultWeight = 0;
	/**
	 * 权重值设置，格式为 host1:port1,权重值1;host2:port2,权重值2 当负载均衡算法是权重轮询调度算法时该属性有效
	 */
	private String weightMaps;
	/**
	 * 从节点发布和订阅连接的最小空闲连接数
	 */
	private int subscriptionConnectionMinimumIdleSize = 1;
	/**
	 * 从节点发布和订阅连接池大小
	 */
	private int subscriptionConnectionPoolSize = 50;
	/**
	 * 从节点最小空闲连接数
	 */
	private int slaveConnectionMinimumIdleSize = 32;
	/**
	 * 从节点连接池大小
	 */
	private int slaveConnectionPoolSize = 64;
	/**
	 * 主节点最小空闲连接数
	 */
	private int masterConnectionMinimumIdleSize = 32;
	/**
	 * 主节点连接池大小
	 */
	private int masterConnectionPoolSize = 64;
	/**
	 * 连接空闲超时，单位：毫秒
	 */
	private int idleConnectionTimeout = 10000;
	/**
	 * 连接超时，单位：毫秒
	 */
	private int connectTimeout = 10000;
	/**
	 * 命令等待超时，单位：毫秒
	 */
	private int timeout = 3000;
	/**
	 * 命令失败重试次数
	 */
	private int retryAttempts = 3;
	/**
	 * 命令重试发送时间间隔，单位：毫秒
	 */
	private int retryInterval = 1500;
	/**
	 * 数据库编号
	 */
	private int database = 0;
	/**
	 * 密码，用于节点身份验证的密码
	 */
	private String password;
	/**
	 * 单个连接最大订阅数量
	 */
	private int subscriptionsPerConnection = 5;

	public long getDnsMonitoringInterval() {
		return dnsMonitoringInterval;
	}

	public void setDnsMonitoringInterval(long dnsMonitoringInterval) {
		this.dnsMonitoringInterval = dnsMonitoringInterval;
	}

	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public String getSentinelAddresses() {
		return sentinelAddresses;
	}

	public void setSentinelAddresses(String sentinelAddresses) {
		this.sentinelAddresses = sentinelAddresses;
	}

	public String getReadMode() {
		return readMode;
	}

	public void setReadMode(String readMode) {
		this.readMode = readMode;
	}

	public String getSubMode() {
		return subMode;
	}

	public void setSubMode(String subMode) {
		this.subMode = subMode;
	}

	public String getLoadBalancer() {
		return loadBalancer;
	}

	public void setLoadBalancer(String loadBalancer) {
		this.loadBalancer = loadBalancer;
	}

	public int getDefaultWeight() {
		return defaultWeight;
	}

	public void setDefaultWeight(int defaultWeight) {
		this.defaultWeight = defaultWeight;
	}

	public String getWeightMaps() {
		return weightMaps;
	}

	public void setWeightMaps(String weightMaps) {
		this.weightMaps = weightMaps;
	}

	public int getSubscriptionConnectionMinimumIdleSize() {
		return subscriptionConnectionMinimumIdleSize;
	}

	public void setSubscriptionConnectionMinimumIdleSize(int subscriptionConnectionMinimumIdleSize) {
		this.subscriptionConnectionMinimumIdleSize = subscriptionConnectionMinimumIdleSize;
	}

	public int getSubscriptionConnectionPoolSize() {
		return subscriptionConnectionPoolSize;
	}

	public void setSubscriptionConnectionPoolSize(int subscriptionConnectionPoolSize) {
		this.subscriptionConnectionPoolSize = subscriptionConnectionPoolSize;
	}

	public int getSlaveConnectionMinimumIdleSize() {
		return slaveConnectionMinimumIdleSize;
	}

	public void setSlaveConnectionMinimumIdleSize(int slaveConnectionMinimumIdleSize) {
		this.slaveConnectionMinimumIdleSize = slaveConnectionMinimumIdleSize;
	}

	public int getSlaveConnectionPoolSize() {
		return slaveConnectionPoolSize;
	}

	public void setSlaveConnectionPoolSize(int slaveConnectionPoolSize) {
		this.slaveConnectionPoolSize = slaveConnectionPoolSize;
	}

	public int getMasterConnectionMinimumIdleSize() {
		return masterConnectionMinimumIdleSize;
	}

	public void setMasterConnectionMinimumIdleSize(int masterConnectionMinimumIdleSize) {
		this.masterConnectionMinimumIdleSize = masterConnectionMinimumIdleSize;
	}

	public int getMasterConnectionPoolSize() {
		return masterConnectionPoolSize;
	}

	public void setMasterConnectionPoolSize(int masterConnectionPoolSize) {
		this.masterConnectionPoolSize = masterConnectionPoolSize;
	}

	public int getIdleConnectionTimeout() {
		return idleConnectionTimeout;
	}

	public void setIdleConnectionTimeout(int idleConnectionTimeout) {
		this.idleConnectionTimeout = idleConnectionTimeout;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getRetryAttempts() {
		return retryAttempts;
	}

	public void setRetryAttempts(int retryAttempts) {
		this.retryAttempts = retryAttempts;
	}

	public int getRetryInterval() {
		return retryInterval;
	}

	public void setRetryInterval(int retryInterval) {
		this.retryInterval = retryInterval;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getSubscriptionsPerConnection() {
		return subscriptionsPerConnection;
	}

	public void setSubscriptionsPerConnection(int subscriptionsPerConnection) {
		this.subscriptionsPerConnection = subscriptionsPerConnection;
	}
}
