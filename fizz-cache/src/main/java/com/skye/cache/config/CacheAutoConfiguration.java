package com.skye.cache.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skye.cache.cache.DoubleCacheManager;
import com.skye.cache.msg.RedisMessageReceiver;
import com.skye.common.utils.SpringContextUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableConfigurationProperties(DoubleCacheConfig.class)
public class CacheAutoConfiguration extends CachingConfigurerSupport {

    public static final String TOPIC="cache.msg";


    @Bean
    //@Order(Ordered.HIGHEST_PRECEDENCE)
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }
    //@ConditionalOnMissingClass("org.springframework.data.redis.core.RedisTemplate")
    @Bean
    public RedisTemplate redisTemplate111(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<Object, Object> redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // json序列化设置
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer
                = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        om.activateDefaultTyping(om.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,//类名序列化到json串中
                JsonTypeInfo.As.WRAPPER_ARRAY);

//        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
//        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        jackson2JsonRedisSerializer.setObjectMapper(om);

        //String类型的序列化
        RedisSerializer<?> stringSerializer = new StringRedisSerializer();

        redisTemplate.setKeySerializer(stringSerializer);// key采用String序列化方式
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);// value序列化
        redisTemplate.setHashKeySerializer(stringSerializer);// Hash key采用String序列化方式
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);// Hash value序列化
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    @Bean
    public DoubleCacheManager cacheManager(RedisTemplate<Object,Object> redisTemplate111,
                                           DoubleCacheConfig doubleCacheConfig){
        return new DoubleCacheManager(redisTemplate111,doubleCacheConfig);
    }
    @Bean
    public RedisMessageReceiver receiver(RedisTemplate<Object, Object> redisTemplate111, DoubleCacheManager doubleCacheManager) {
        return new RedisMessageReceiver(redisTemplate111, doubleCacheManager);
    }

    @Bean
    RedisMessageListenerContainer container(MessageListenerAdapter listenerAdapter,
                                            RedisConnectionFactory redisConnectionFactory){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(TOPIC));
        return container;
    }

    @Bean
    MessageListenerAdapter adapter(RedisMessageReceiver receiver){
        return new MessageListenerAdapter(receiver,"receive");
    }
}
