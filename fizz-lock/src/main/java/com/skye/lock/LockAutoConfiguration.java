package com.skye.lock;

import com.skye.lock.config.LockConfig;
import com.skye.lock.config.RedisSchemeConfig;
import com.skye.lock.config.redis.*;
import com.skye.lock.constant.LoadBalancerTypeConstant;
import com.skye.lock.constant.LockCommonConstant;
import com.skye.lock.constant.SubReadModeTypeConstant;
import com.skye.lock.core.LockInterceptor;
import com.skye.lock.enumeration.ServerPattern;
import com.skye.lock.exception.UnknownLoadBalancerException;
import com.skye.lock.exception.UnknownReadModeException;
import com.skye.lock.exception.UnknownSubscriptionModeException;
import com.skye.lock.factory.FactoryBean;
import com.skye.lock.factory.RedisServiceBeanFactory;
import com.skye.lock.service.impl.redis.*;
import com.skye.lock.store.RedisPatternStore;
import com.skye.lock.util.SpringUtil;
import com.skye.lock.util.ValidateUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.*;
import org.redisson.connection.balancer.LoadBalancer;
import org.redisson.connection.balancer.RandomLoadBalancer;
import org.redisson.connection.balancer.RoundRobinLoadBalancer;
import org.redisson.connection.balancer.WeightedRoundRobinBalancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 自动装配
 *
 * @author TanRq
 */
@Configuration
@EnableConfigurationProperties(LockConfig.class)
@Import({ LockInterceptor.class })
public class LockAutoConfiguration {

	@Autowired
	private LockConfig lockConfig;

	/**
	 * 创建redissonClient
	 *
	 * @return RedissonClient
	 */
	@Bean(name = "lockRedissonClient")
	@ConditionalOnProperty(prefix = "lock-config", name = "lock-scheme", havingValue = "redis")
	public RedissonClient redissonClientForConf() throws URISyntaxException {
		return redissonClient();
	}

	@Bean(name = "lockRedissonClient")
	@ConditionalOnProperty(prefix = "lock-config", name = "lock-scheme", matchIfMissing = true)
	public RedissonClient redissonClientForDef() throws URISyntaxException {
		return redissonClient();
	}

	private RedissonClient redissonClient() throws URISyntaxException {
		RedisSchemeConfig redisSchemeConfig = lockConfig.getRedis();
		Config config = new Config();
		ServerPattern serverPattern = RedisPatternStore.getServerPattern(redisSchemeConfig.getPattern());

		if (serverPattern == ServerPattern.SINGLE) {
			SingleServerConfig singleServerConfig = config.useSingleServer();
			initSingleConfig(singleServerConfig);
		}
		if (serverPattern == ServerPattern.CLUSTER) {
			ClusterServersConfig clusterConfig = config.useClusterServers();
			initClusterConfig(clusterConfig);
		}
		if (serverPattern == ServerPattern.MASTER_SLAVE) {
			MasterSlaveServersConfig masterSlaveConfig = config.useMasterSlaveServers();
			initMasterSlaveConfig(masterSlaveConfig);
		}
		if (serverPattern == ServerPattern.REPLICATED) {
			ReplicatedServersConfig replicatedServersConfig = config.useReplicatedServers();
			initReplicatedServersConfig(replicatedServersConfig);
		}
		if (serverPattern == ServerPattern.SENTINEL) {
			SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
			initSentinelServersConfig(sentinelServersConfig);
		}
		return Redisson.create(config);
	}


	@Bean
	public FactoryBean serviceBeanFactory() {
		return new FactoryBean();
	}

	@Bean
	public RedisServiceBeanFactory redisServiceBeanFactory() {
		return new RedisServiceBeanFactory();
	}

	@Bean
	public SpringUtil springUtil() {
		return new SpringUtil();
	}

	@Bean
	@Scope("prototype")
	public ReentrantLockServiceImpl reentrantLockServiceImpl() {
		return new ReentrantLockServiceImpl();
	}

	@Bean
	@Scope("prototype")
	public FairLockServiceImpl fairLockServiceImpl() {
		return new FairLockServiceImpl();
	}

	@Bean
	@Scope("prototype")
	public MultiLockServiceImpl multiLockServiceImpl() {
		return new MultiLockServiceImpl();
	}

	@Bean
	@Scope("prototype")
	public RedLockServiceImpl redLockServiceImpl() {
		return new RedLockServiceImpl();
	}

	@Bean
	@Scope("prototype")
	public ReadLockServiceImpl readLockServiceImpl() {
		return new ReadLockServiceImpl();
	}

	@Bean
	@Scope("prototype")
	public WriteLockServiceImpl writeLockServiceImpl() {
		return new WriteLockServiceImpl();
	}



	/**
	 * 初始化单机模式参数
	 *
	 * @param singleServerConfig 单机模式配置
	 */
	private void initSingleConfig(SingleServerConfig singleServerConfig) throws URISyntaxException {
		RedisSchemeConfig redisSchemeConfig = lockConfig.getRedis();
		SingleConfig singleConfig = redisSchemeConfig.getSingleServer();
		singleServerConfig.setAddress(String.format("%s%s%s%s", LockCommonConstant.REDIS_URL_PREFIX,
				singleConfig.getAddress(), LockCommonConstant.COLON, singleConfig.getPort()));
		singleServerConfig.setClientName(redisSchemeConfig.getClientName());
		singleServerConfig.setConnectionMinimumIdleSize(singleConfig.getConnMinIdleSize());
		singleServerConfig.setConnectionPoolSize(singleConfig.getConnPoolSize());
		singleServerConfig.setConnectTimeout(singleConfig.getConnTimeout());
		singleServerConfig.setDatabase(singleConfig.getDatabase());
		singleServerConfig.setDnsMonitoringInterval(singleConfig.getDnsMonitoringInterval());
		singleServerConfig.setIdleConnectionTimeout(singleConfig.getIdleConnTimeout());
		singleServerConfig.setKeepAlive(singleConfig.isKeepAlive());
		singleServerConfig.setPassword(singleConfig.getPassword());
		singleServerConfig.setRetryAttempts(singleConfig.getRetryAttempts());
		singleServerConfig.setRetryInterval(singleConfig.getRetryInterval());
		singleServerConfig.setSslEnableEndpointIdentification(redisSchemeConfig.isSslEnableEndpointIdentification());
		if (redisSchemeConfig.getSslKeystore() != null) {
			singleServerConfig.setSslKeystore(new URI(redisSchemeConfig.getSslKeystore()));
		}
		if (redisSchemeConfig.getSslKeystorePassword() != null) {
			singleServerConfig.setSslKeystorePassword(redisSchemeConfig.getSslKeystorePassword());
		}
		singleServerConfig.setSslProvider(
				LockCommonConstant.JDK.equalsIgnoreCase(redisSchemeConfig.getSslProvider()) ? SslProvider.JDK
						: SslProvider.OPENSSL);
	}

	/**
	 * 初始化集群模式参数
	 *
	 * @param clusterServerConfig 集群模式配置
	 */
	private void initClusterConfig(ClusterServersConfig clusterServerConfig) {
		RedisSchemeConfig redisSchemeConfig = lockConfig.getRedis();
		ClusterConfig clusterConfig = redisSchemeConfig.getClusterServer();
		String[] addressArr = clusterConfig.getNodeAddresses().split(LockCommonConstant.COMMA);
		Arrays.asList(addressArr).forEach(address -> clusterServerConfig
				.addNodeAddress(String.format("%s%s", LockCommonConstant.REDIS_URL_PREFIX, address)));
		clusterServerConfig.setScanInterval(clusterConfig.getScanInterval());

		ReadMode readMode = getReadMode(clusterConfig.getReadMode());
		ValidateUtil.notNull(readMode, UnknownReadModeException.class, "未知读取操作的负载均衡模式类型");
		clusterServerConfig.setReadMode(readMode);

		SubscriptionMode subscriptionMode = getSubscriptionMode(clusterConfig.getSubMode());
		ValidateUtil.notNull(subscriptionMode, UnknownSubscriptionModeException.class, "未知订阅操作的负载均衡模式类型");
		clusterServerConfig.setSubscriptionMode(subscriptionMode);

		LoadBalancer loadBalancer = getLoadBalancer(clusterConfig.getLoadBalancer(), clusterConfig.getWeightMaps(),
				clusterConfig.getDefaultWeight());
		ValidateUtil.notNull(loadBalancer, UnknownLoadBalancerException.class, "未知负载均衡算法类型");
		clusterServerConfig.setLoadBalancer(loadBalancer);

		clusterServerConfig.setSubscriptionConnectionMinimumIdleSize(clusterConfig.getSubConnMinIdleSize());
		clusterServerConfig.setSubscriptionConnectionPoolSize(clusterConfig.getSubConnPoolSize());
		clusterServerConfig.setSlaveConnectionMinimumIdleSize(clusterConfig.getSlaveConnMinIdleSize());
		clusterServerConfig.setSlaveConnectionPoolSize(clusterConfig.getSlaveConnPoolSize());
		clusterServerConfig.setMasterConnectionMinimumIdleSize(clusterConfig.getMasterConnMinIdleSize());
		clusterServerConfig.setMasterConnectionPoolSize(clusterConfig.getMasterConnPoolSize());
		clusterServerConfig.setIdleConnectionTimeout(clusterConfig.getIdleConnTimeout());
		clusterServerConfig.setConnectTimeout(clusterConfig.getConnTimeout());
		clusterServerConfig.setTimeout(clusterConfig.getTimeout());
		clusterServerConfig.setRetryAttempts(clusterConfig.getRetryAttempts());
		clusterServerConfig.setRetryInterval(clusterConfig.getRetryInterval());
		clusterServerConfig.setPassword(clusterConfig.getPassword());
		clusterServerConfig.setSubscriptionsPerConnection(clusterConfig.getSubPerConn());
		clusterServerConfig.setClientName(redisSchemeConfig.getClientName());
	}

	/**
	 * 初始化哨兵模式参数
	 *
	 * @param sentinelServersConfig 哨兵模式配置
	 * @throws URISyntaxException URISyntaxException
	 */
	private void initSentinelServersConfig(SentinelServersConfig sentinelServersConfig) throws URISyntaxException {
		RedisSchemeConfig redisSchemeConfig = lockConfig.getRedis();
		SentinelConfig sentinelConfig = redisSchemeConfig.getSentinelServer();
		String[] addressArr = sentinelConfig.getSentinelAddresses().split(LockCommonConstant.COMMA);
		Arrays.asList(addressArr).forEach(address -> sentinelServersConfig
				.addSentinelAddress(String.format("%s%s", LockCommonConstant.REDIS_URL_PREFIX, address)));

		ReadMode readMode = getReadMode(sentinelConfig.getReadMode());
		ValidateUtil.notNull(readMode, UnknownReadModeException.class, "未知读取操作的负载均衡模式类型");
		sentinelServersConfig.setReadMode(readMode);

		SubscriptionMode subscriptionMode = getSubscriptionMode(sentinelConfig.getSubMode());
		ValidateUtil.notNull(subscriptionMode, UnknownSubscriptionModeException.class, "未知订阅操作的负载均衡模式类型");
		sentinelServersConfig.setSubscriptionMode(subscriptionMode);

		LoadBalancer loadBalancer = getLoadBalancer(sentinelConfig.getLoadBalancer(), sentinelConfig.getWeightMaps(),
				sentinelConfig.getDefaultWeight());
		ValidateUtil.notNull(loadBalancer, UnknownLoadBalancerException.class, "未知负载均衡算法类型");
		sentinelServersConfig.setLoadBalancer(loadBalancer);

		sentinelServersConfig.setMasterName(sentinelConfig.getMasterName());
		sentinelServersConfig.setDatabase(sentinelConfig.getDatabase());
		sentinelServersConfig.setSlaveConnectionPoolSize(sentinelConfig.getSlaveConnectionPoolSize());
		sentinelServersConfig.setMasterConnectionPoolSize(sentinelConfig.getMasterConnectionPoolSize());
		sentinelServersConfig.setSubscriptionConnectionPoolSize(sentinelConfig.getSubscriptionConnectionPoolSize());
		sentinelServersConfig.setSlaveConnectionMinimumIdleSize(sentinelConfig.getSlaveConnectionMinimumIdleSize());
		sentinelServersConfig.setMasterConnectionMinimumIdleSize(sentinelConfig.getMasterConnectionMinimumIdleSize());
		sentinelServersConfig
				.setSubscriptionConnectionMinimumIdleSize(sentinelConfig.getSubscriptionConnectionMinimumIdleSize());
		sentinelServersConfig.setDnsMonitoringInterval(sentinelConfig.getDnsMonitoringInterval());
		sentinelServersConfig.setSubscriptionsPerConnection(sentinelConfig.getSubscriptionsPerConnection());
		sentinelServersConfig.setPassword(sentinelConfig.getPassword());
		sentinelServersConfig.setRetryAttempts(sentinelConfig.getRetryAttempts());
		sentinelServersConfig.setRetryInterval(sentinelConfig.getRetryInterval());
		sentinelServersConfig.setTimeout(sentinelConfig.getTimeout());
		sentinelServersConfig.setConnectTimeout(sentinelConfig.getConnectTimeout());
		sentinelServersConfig.setIdleConnectionTimeout(sentinelConfig.getIdleConnectionTimeout());
		setLockSslConfigAndClientName(sentinelServersConfig);
	}

	/**
	 * 初始化云托管模式参数
	 *
	 * @param replicatedServersConfig 云托管模式配置
	 * @throws URISyntaxException URISyntaxException
	 */
	private void initReplicatedServersConfig(ReplicatedServersConfig replicatedServersConfig)
			throws URISyntaxException {
		RedisSchemeConfig redisSchemeConfig = lockConfig.getRedis();
		ReplicatedConfig replicatedConfig = redisSchemeConfig.getReplicatedServer();

		String[] addressArr = replicatedConfig.getNodeAddresses().split(LockCommonConstant.COMMA);
		Arrays.asList(addressArr).forEach(address -> replicatedServersConfig
				.addNodeAddress(String.format("%s%s", LockCommonConstant.REDIS_URL_PREFIX, address)));
		ReadMode readMode = getReadMode(replicatedConfig.getReadMode());
		ValidateUtil.notNull(readMode, UnknownReadModeException.class, "未知读取操作的负载均衡模式类型");
		replicatedServersConfig.setReadMode(readMode);

		SubscriptionMode subscriptionMode = getSubscriptionMode(replicatedConfig.getSubscriptionMode());
		ValidateUtil.notNull(subscriptionMode, UnknownSubscriptionModeException.class, "未知订阅操作的负载均衡模式类型");
		replicatedServersConfig.setSubscriptionMode(subscriptionMode);

		LoadBalancer loadBalancer = getLoadBalancer(replicatedConfig.getLoadBalancer(),
				replicatedConfig.getWeightMaps(), replicatedConfig.getDefaultWeight());
		ValidateUtil.notNull(loadBalancer, UnknownLoadBalancerException.class, "未知负载均衡算法类型");
		replicatedServersConfig.setLoadBalancer(loadBalancer);

		replicatedServersConfig.setScanInterval(replicatedConfig.getScanInterval());
		replicatedServersConfig.setDatabase(replicatedConfig.getDatabase());
		replicatedServersConfig.setSlaveConnectionPoolSize(replicatedConfig.getSlaveConnectionPoolSize());
		replicatedServersConfig.setMasterConnectionPoolSize(replicatedConfig.getMasterConnectionPoolSize());
		replicatedServersConfig.setSubscriptionConnectionPoolSize(replicatedConfig.getSubscriptionConnectionPoolSize());
		replicatedServersConfig.setSlaveConnectionMinimumIdleSize(replicatedConfig.getSlaveConnectionMinimumIdleSize());
		replicatedServersConfig
				.setMasterConnectionMinimumIdleSize(replicatedConfig.getMasterConnectionMinimumIdleSize());
		replicatedServersConfig
				.setSubscriptionConnectionMinimumIdleSize(replicatedConfig.getSubscriptionConnectionMinimumIdleSize());
		replicatedServersConfig.setDnsMonitoringInterval(replicatedConfig.getDnsMonitoringInterval());
		replicatedServersConfig.setSubscriptionsPerConnection(replicatedConfig.getSubscriptionsPerConnection());
		replicatedServersConfig.setPassword(replicatedConfig.getPassword());
		replicatedServersConfig.setRetryAttempts(replicatedConfig.getRetryAttempts());
		replicatedServersConfig.setRetryInterval(replicatedConfig.getRetryInterval());
		replicatedServersConfig.setTimeout(replicatedConfig.getTimeout());
		replicatedServersConfig.setConnectTimeout(replicatedConfig.getConnectTimeout());
		replicatedServersConfig.setIdleConnectionTimeout(replicatedConfig.getIdleConnectionTimeout());

		setLockSslConfigAndClientName(replicatedServersConfig);
	}

	/**
	 * 初始化主从模式参数
	 *
	 * @param masterSlaveServersConfig 主从模式配置
	 * @throws URISyntaxException URISyntaxException
	 */
	private void initMasterSlaveConfig(MasterSlaveServersConfig masterSlaveServersConfig) throws URISyntaxException {
		RedisSchemeConfig redisSchemeConfig = lockConfig.getRedis();
		MasterSlaveConfig masterSlaveConfig = redisSchemeConfig.getMasterSlaveServer();
		masterSlaveServersConfig.setMasterAddress(
				String.format("%s%s", LockCommonConstant.REDIS_URL_PREFIX, masterSlaveConfig.getMasterAddress()));
		String[] addressArr = masterSlaveConfig.getSlaveAddresses().split(LockCommonConstant.COMMA);

		Arrays.asList(addressArr).forEach(address -> {
			masterSlaveServersConfig
					.addSlaveAddress(String.format("%s%s", LockCommonConstant.REDIS_URL_PREFIX, address));
		});

		ReadMode readMode = getReadMode(masterSlaveConfig.getReadMode());
		ValidateUtil.notNull(readMode, UnknownReadModeException.class, "未知读取操作的负载均衡模式类型");
		masterSlaveServersConfig.setReadMode(readMode);

		SubscriptionMode subscriptionMode = getSubscriptionMode(masterSlaveConfig.getSubMode());
		ValidateUtil.notNull(subscriptionMode, UnknownSubscriptionModeException.class, "未知订阅操作的负载均衡模式类型");
		masterSlaveServersConfig.setSubscriptionMode(subscriptionMode);

		LoadBalancer loadBalancer = getLoadBalancer(masterSlaveConfig.getLoadBalancer(),
				masterSlaveConfig.getWeightMaps(), masterSlaveConfig.getDefaultWeight());
		ValidateUtil.notNull(loadBalancer, UnknownLoadBalancerException.class, "未知负载均衡算法类型");
		masterSlaveServersConfig.setLoadBalancer(loadBalancer);

		masterSlaveServersConfig.setDatabase(masterSlaveConfig.getDatabase());
		masterSlaveServersConfig.setSlaveConnectionPoolSize(masterSlaveConfig.getSlaveConnectionPoolSize());
		masterSlaveServersConfig.setMasterConnectionPoolSize(masterSlaveConfig.getMasterConnectionPoolSize());
		masterSlaveServersConfig
				.setSubscriptionConnectionPoolSize(masterSlaveConfig.getSubscriptionConnectionPoolSize());
		masterSlaveServersConfig
				.setSlaveConnectionMinimumIdleSize(masterSlaveConfig.getSlaveConnectionMinimumIdleSize());
		masterSlaveServersConfig
				.setMasterConnectionMinimumIdleSize(masterSlaveConfig.getMasterConnectionMinimumIdleSize());
		masterSlaveServersConfig
				.setSubscriptionConnectionMinimumIdleSize(masterSlaveConfig.getSubscriptionConnectionMinimumIdleSize());
		masterSlaveServersConfig.setDnsMonitoringInterval(masterSlaveConfig.getDnsMonitoringInterval());
		masterSlaveServersConfig.setSubscriptionsPerConnection(masterSlaveConfig.getSubscriptionsPerConnection());
		masterSlaveServersConfig.setPassword(masterSlaveConfig.getPassword());
		masterSlaveServersConfig.setRetryAttempts(masterSlaveConfig.getRetryAttempts());
		masterSlaveServersConfig.setRetryInterval(masterSlaveConfig.getRetryInterval());
		masterSlaveServersConfig.setTimeout(masterSlaveConfig.getTimeout());
		masterSlaveServersConfig.setConnectTimeout(masterSlaveConfig.getConnectTimeout());
		masterSlaveServersConfig.setIdleConnectionTimeout(masterSlaveConfig.getIdleConnectionTimeout());
		setLockSslConfigAndClientName(masterSlaveServersConfig);
	}

	/**
	 * 根据用户的配置类型设置对应的LoadBalancer
	 *
	 * @param loadBalancerType   负载均衡算法类名
	 * @param customerWeightMaps 权重值设置，当负载均衡算法是权重轮询调度算法时该属性有效
	 * @param defaultWeight      默认权重值，当负载均衡算法是权重轮询调度算法时该属性有效
	 * @return LoadBalancer OR NULL
	 */
	private LoadBalancer getLoadBalancer(String loadBalancerType, String customerWeightMaps, int defaultWeight) {
		if (LoadBalancerTypeConstant.RANDOM_LOAD_BALANCER.equals(loadBalancerType)) {
			return new RandomLoadBalancer();
		}
		if (LoadBalancerTypeConstant.ROUND_ROBIN_LOAD_BALANCER.equals(loadBalancerType)) {
			return new RoundRobinLoadBalancer();
		}
		if (LoadBalancerTypeConstant.WEIGHTED_ROUND_ROBIN_BALANCER.equals(loadBalancerType)) {
			Map<String, Integer> weights = new HashMap<>(16);
			String[] weightMaps = customerWeightMaps.split(LockCommonConstant.SEMICOLON);
			Arrays.asList(weightMaps)
					.forEach(weightMap -> weights.put(
							LockCommonConstant.REDIS_URL_PREFIX + weightMap.split(LockCommonConstant.COMMA)[0],
							Integer.parseInt(weightMap.split(LockCommonConstant.COMMA)[1])));
			return new WeightedRoundRobinBalancer(weights, defaultWeight);
		}
		return null;
	}

	/**
	 * 根据readModeType返回ReadMode
	 *
	 * @param readModeType 读取操作的负载均衡模式类型
	 * @return ReadMode OR NULL
	 */
	private ReadMode getReadMode(String readModeType) {
		if (SubReadModeTypeConstant.SLAVE.equals(readModeType)) {
			return ReadMode.SLAVE;
		}
		if (SubReadModeTypeConstant.MASTER.equals(readModeType)) {
			return ReadMode.MASTER;
		}
		if (SubReadModeTypeConstant.MASTER_SLAVE.equals(readModeType)) {
			return ReadMode.MASTER_SLAVE;
		}
		return null;
	}

	/**
	 * 根据subscriptionModeType返回SubscriptionMode
	 *
	 * @param subscriptionModeType 订阅操作的负载均衡模式类型
	 * @return SubscriptionMode OR NULL
	 */
	private SubscriptionMode getSubscriptionMode(String subscriptionModeType) {
		if (SubReadModeTypeConstant.SLAVE.equals(subscriptionModeType)) {
			return SubscriptionMode.SLAVE;
		}
		if (SubReadModeTypeConstant.MASTER.equals(subscriptionModeType)) {
			return SubscriptionMode.MASTER;
		}
		return null;
	}

	/**
	 * 设置SSL配置
	 *
	 * @param lockAutoConfig lockAutoConfig
	 * @param                <T> lockAutoConfig
	 * @throws URISyntaxException URISyntaxException
	 */
	@SuppressWarnings("rawtypes")
	private <T extends BaseMasterSlaveServersConfig> void setLockSslConfigAndClientName(T lockAutoConfig)
			throws URISyntaxException {
		LockConfig lockConfig = SpringUtil.getBean(LockConfig.class);
		RedisSchemeConfig redisSchemeConfig = lockConfig.getRedis();
		lockAutoConfig.setClientName(redisSchemeConfig.getClientName());
		lockAutoConfig.setSslEnableEndpointIdentification(redisSchemeConfig.isSslEnableEndpointIdentification());
		if (redisSchemeConfig.getSslKeystore() != null) {
			lockAutoConfig.setSslKeystore(new URI(redisSchemeConfig.getSslKeystore()));
		}
		if (redisSchemeConfig.getSslKeystorePassword() != null) {
			lockAutoConfig.setSslKeystorePassword(redisSchemeConfig.getSslKeystorePassword());
		}
		if (redisSchemeConfig.getSslTruststore() != null) {
			lockAutoConfig.setSslTruststore(new URI(redisSchemeConfig.getSslTruststore()));
		}
		if (redisSchemeConfig.getSslTruststorePassword() != null) {
			lockAutoConfig.setSslTruststorePassword(redisSchemeConfig.getSslTruststorePassword());
		}
		lockAutoConfig.setSslProvider(
				LockCommonConstant.JDK.equalsIgnoreCase(redisSchemeConfig.getSslProvider()) ? SslProvider.JDK
						: SslProvider.OPENSSL);
	}

}
