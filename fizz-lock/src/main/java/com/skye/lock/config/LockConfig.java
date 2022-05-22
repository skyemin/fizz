package com.skye.lock.config;

import com.skye.lock.config.redis.*;
import com.skye.lock.constant.LockCommonConstant;
import com.skye.lock.enumeration.ServerPattern;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 锁配置文件
 *
 * @author TanRq
 */
@Component
@ConfigurationProperties(prefix = "lock-config")
public class LockConfig {

	/**
	 * 锁的实现方式：redis或者zookeeper,默认redis
	 */
	private String lockScheme = "redis";

	/**
	 * 分布式锁的运行模式,默认使用单机模式 Redis支持的模式：单机、集群、云托管、哨兵、主从
	 */
	private String pattern = ServerPattern.SINGLE.getPattern();

	private RedisSchemeConfig redis;

	private SingleConfig singleServer;

	private ClusterConfig clusterServer;

	private ReplicatedConfig replicatedServer;

	private SentinelConfig sentinelServer;

	private MasterSlaveConfig masterSlaveServer;
	/** 客户端名称 */
	private String clientName = LockCommonConstant.LOCK;
	/** 启用SSL终端识别 */
	private Boolean sslEnableEndpointIdentification = true;
	/** SSL实现方式，确定采用哪种方式（JDK或OPENSSL）来实现SSL连接 */
	private String sslProvider = LockCommonConstant.JDK;
	/** SSL信任证书库路径 */
	private String sslTruststore;
	/** SSL信任证书库密码 */
	private String sslTruststorePassword;
	/** SSL钥匙库路径 */
	private String sslKeystore;
	/** SSL钥匙库密码 */
	private String sslKeystorePassword;

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

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
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

	public String getLockScheme() {
		return lockScheme;
	}

	public void setLockScheme(String lockScheme) {
		this.lockScheme = lockScheme;
	}

	public RedisSchemeConfig getRedis() {
		if (this.redis == null) {
			RedisSchemeConfig redisConfig = new RedisSchemeConfig();
			BeanUtils.copyProperties(this, redisConfig);
			this.redis = redisConfig;
		}
		return redis;
	}

	public void setRedis(RedisSchemeConfig redis) {
		this.redis = redis;
	}

}
