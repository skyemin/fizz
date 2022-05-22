package com.skye.lock.config.redis;


import com.skye.lock.constant.LoadBalancerTypeConstant;
import com.skye.lock.constant.SubReadModeTypeConstant;

public class ClusterConfig {

	/**
	 * 集群节点地址
	 */
	private String nodeAddresses;
	/**
	 * 集群扫描间隔时间
	 */
	private int scanInterval = 1000;
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
	private int subConnMinIdleSize = 1;
	/**
	 * 从节点发布和订阅连接池大小
	 */
	private int subConnPoolSize = 50;
	/**
	 * 从节点最小空闲连接数
	 */
	private int slaveConnMinIdleSize = 32;
	/**
	 * 从节点连接池大小
	 */
	private int slaveConnPoolSize = 64;
	/**
	 * 主节点最小空闲连接数
	 */
	private int masterConnMinIdleSize = 32;
	/**
	 * 主节点连接池大小
	 */
	private int masterConnPoolSize = 64;
	/**
	 * 连接空闲超时，单位：毫秒
	 */
	private int idleConnTimeout = 10000;
	/**
	 * 连接超时，单位：毫秒
	 */
	private int connTimeout = 10000;
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
	 * 密码
	 */
	private String password;
	/**
	 * 单个连接最大订阅数量
	 */
	private int subPerConn = 5;

	public String getNodeAddresses() {
		return nodeAddresses;
	}

	public void setNodeAddresses(String nodeAddresses) {
		this.nodeAddresses = nodeAddresses;
	}

	public int getScanInterval() {
		return scanInterval;
	}

	public void setScanInterval(int scanInterval) {
		this.scanInterval = scanInterval;
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

	public int getSubConnMinIdleSize() {
		return subConnMinIdleSize;
	}

	public void setSubConnMinIdleSize(int subConnMinIdleSize) {
		this.subConnMinIdleSize = subConnMinIdleSize;
	}

	public int getSubConnPoolSize() {
		return subConnPoolSize;
	}

	public void setSubConnPoolSize(int subConnPoolSize) {
		this.subConnPoolSize = subConnPoolSize;
	}

	public int getSlaveConnMinIdleSize() {
		return slaveConnMinIdleSize;
	}

	public void setSlaveConnMinIdleSize(int slaveConnMinIdleSize) {
		this.slaveConnMinIdleSize = slaveConnMinIdleSize;
	}

	public int getSlaveConnPoolSize() {
		return slaveConnPoolSize;
	}

	public void setSlaveConnPoolSize(int slaveConnPoolSize) {
		this.slaveConnPoolSize = slaveConnPoolSize;
	}

	public int getMasterConnMinIdleSize() {
		return masterConnMinIdleSize;
	}

	public void setMasterConnMinIdleSize(int masterConnMinIdleSize) {
		this.masterConnMinIdleSize = masterConnMinIdleSize;
	}

	public int getMasterConnPoolSize() {
		return masterConnPoolSize;
	}

	public void setMasterConnPoolSize(int masterConnPoolSize) {
		this.masterConnPoolSize = masterConnPoolSize;
	}

	public int getIdleConnTimeout() {
		return idleConnTimeout;
	}

	public void setIdleConnTimeout(int idleConnTimeout) {
		this.idleConnTimeout = idleConnTimeout;
	}

	public int getConnTimeout() {
		return connTimeout;
	}

	public void setConnTimeout(int connTimeout) {
		this.connTimeout = connTimeout;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getSubPerConn() {
		return subPerConn;
	}

	public void setSubPerConn(int subPerConn) {
		this.subPerConn = subPerConn;
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
}
