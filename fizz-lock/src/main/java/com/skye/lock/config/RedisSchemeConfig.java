package com.skye.lock.config;


import com.skye.lock.config.redis.*;

public class RedisSchemeConfig {
	/**
	 * 分布式锁的运行模式,默认使用单机模式 ,Redis支持的模式：单机、集群、云托管、哨兵、主从
	 */
	private String pattern;
	/** 单机模式配置 */
	private SingleConfig singleServer;
	/** 集群模式配置 */
	private ClusterConfig clusterServer;
	/** 云托管模式配置 */
	private ReplicatedConfig replicatedServer;
	/** 哨兵模式配置 */
	private SentinelConfig sentinelServer;
	/** 主从模式配置 */
	private MasterSlaveConfig masterSlaveServer;

	/** 客户端名称 */
	private String clientName;
	/** 启用SSL终端识别 */
	private Boolean sslEnableEndpointIdentification;
	/** SSL实现方式，确定采用哪种方式（JDK或OPENSSL）来实现SSL连接 */
	private String sslProvider;
	/** SSL信任证书库路径 */
	private String sslTruststore;
	/** SSL信任证书库密码 */
	private String sslTruststorePassword;
	/** SSL钥匙库路径 */
	private String sslKeystore;
	/** SSL钥匙库密码 */
	private String sslKeystorePassword;

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public boolean isSslEnableEndpointIdentification() {
		return sslEnableEndpointIdentification;
	}

	public void setSslEnableEndpointIdentification(boolean sslEnableEndpointIdentification) {
		this.sslEnableEndpointIdentification = sslEnableEndpointIdentification;
	}

	public String getSslProvider() {
		return sslProvider;
	}

	public void setSslProvider(String sslProvider) {
		this.sslProvider = sslProvider;
	}

	public String getSslTruststore() {
		return sslTruststore;
	}

	public void setSslTruststore(String sslTruststore) {
		this.sslTruststore = sslTruststore;
	}

	public String getSslTruststorePassword() {
		return sslTruststorePassword;
	}

	public void setSslTruststorePassword(String sslTruststorePassword) {
		this.sslTruststorePassword = sslTruststorePassword;
	}

	public String getSslKeystore() {
		return sslKeystore;
	}

	public void setSslKeystore(String sslKeystore) {
		this.sslKeystore = sslKeystore;
	}

	public String getSslKeystorePassword() {
		return sslKeystorePassword;
	}

	public void setSslKeystorePassword(String sslKeystorePassword) {
		this.sslKeystorePassword = sslKeystorePassword;
	}

	public SingleConfig getSingleServer() {
		return singleServer;
	}

	public void setSingleServer(SingleConfig singleServer) {
		this.singleServer = singleServer;
	}

	public ClusterConfig getClusterServer() {
		return clusterServer;
	}

	public void setClusterServer(ClusterConfig clusterServer) {
		this.clusterServer = clusterServer;
	}

	public ReplicatedConfig getReplicatedServer() {
		return replicatedServer;
	}

	public void setReplicatedServer(ReplicatedConfig replicatedServer) {
		this.replicatedServer = replicatedServer;
	}

	public SentinelConfig getSentinelServer() {
		return sentinelServer;
	}

	public void setSentinelServer(SentinelConfig sentinelServer) {
		this.sentinelServer = sentinelServer;
	}

	public MasterSlaveConfig getMasterSlaveServer() {
		return masterSlaveServer;
	}

	public void setMasterSlaveServer(MasterSlaveConfig masterSlaveServer) {
		this.masterSlaveServer = masterSlaveServer;
	}

}
