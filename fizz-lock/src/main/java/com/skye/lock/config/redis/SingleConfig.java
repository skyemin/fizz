package com.skye.lock.config.redis;

public class SingleConfig {
	/**
	 * 节点地址
	 */
	private String address;
	/**
	 * 节点端口
	 */
	private int port;
	/**
	 * 发布和订阅连接的最小空闲连接数
	 */
	private int subConnMinIdleSize = 1;
	/**
	 * 发布和订阅连接池大小
	 */
	private int subConnPoolSize = 50;
	/**
	 * 最小空闲连接数
	 */
	private int connMinIdleSize = 32;
	/**
	 * 连接池大小
	 */
	private int connPoolSize = 64;
	/**
	 * 是否启用DNS监测
	 */
	private boolean dnsMonitoring = false;
	/**
	 * DNS监测时间间隔，单位：毫秒，该配置需要dnsMonitoring设为true
	 */
	private int dnsMonitoringInterval = 5000;
	/**
	 * 连接空闲超时，单位：毫秒
	 */
	private int idleConnTimeout = 10000;
	/**
	 *
	 */
	private boolean keepAlive = false;
	/**
	 * 连接超时，单位：毫秒
	 */
	private int connTimeout = 10000;
	/**
	 * 命令等待超时，单位：毫秒
	 */
	private int timeout = 3000;
	/**
	 * 命令失败重试次数 如果尝试达到 retryAttempts（命令失败重试次数）
	 * 仍然不能将命令发送至某个指定的节点时，将抛出错误。如果尝试在此限制之内发送成功，则开始启用 timeout（命令等待超时） 计时。
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
	 * 密码
	 */
	private String password;
	/**
	 * 单个连接最大订阅数量
	 */
	private int subPerConn = 5;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
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

	public int getConnMinIdleSize() {
		return connMinIdleSize;
	}

	public void setConnMinIdleSize(int connMinIdleSize) {
		this.connMinIdleSize = connMinIdleSize;
	}

	public int getConnPoolSize() {
		return connPoolSize;
	}

	public void setConnPoolSize(int connPoolSize) {
		this.connPoolSize = connPoolSize;
	}

	public boolean isDnsMonitoring() {
		return dnsMonitoring;
	}

	public void setDnsMonitoring(boolean dnsMonitoring) {
		this.dnsMonitoring = dnsMonitoring;
	}

	public int getDnsMonitoringInterval() {
		return dnsMonitoringInterval;
	}

	public void setDnsMonitoringInterval(int dnsMonitoringInterval) {
		this.dnsMonitoringInterval = dnsMonitoringInterval;
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

	public int getSubPerConn() {
		return subPerConn;
	}

	public void setSubPerConn(int subPerConn) {
		this.subPerConn = subPerConn;
	}

	public boolean isKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}
}
