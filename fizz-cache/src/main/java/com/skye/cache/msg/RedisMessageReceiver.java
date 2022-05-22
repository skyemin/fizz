package com.skye.cache.msg;

import com.skye.cache.cache.DoubleCache;
import com.skye.cache.cache.DoubleCacheManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.net.UnknownHostException;

/**
 * <p></p>
 * <p>
 * <PRE>
 * <BR>    修改记录
 * <BR>-----------------------------------------------
 * <BR>    修改日期         修改人          修改内容
 * </PRE>
 *
 * @author skye
 * @version 1.0
 * @date Created in 2022年04月24日 14:35
 * @since 1.0
 */
@Slf4j
@AllArgsConstructor
public class RedisMessageReceiver {
    private final RedisTemplate redisTemplate;
    private final DoubleCacheManager manager;

    //接收通知，进行处理
    public void receive(String message) throws UnknownHostException {
        CacheMassage msg = (CacheMassage) redisTemplate
                .getValueSerializer().deserialize(message.getBytes());
        log.info(msg.toString());

        //如果是本机发出的消息，那么不进行处理
        if (msg.getMsgSource().equals(MessageSourceUtil.getMsgSource())){
            log.info("收到本机发出的消息，不做处理");
            return;
        }

        DoubleCache cache = (DoubleCache) manager.getCache(msg.getCacheName());
        if (msg.getType()== CacheMsgType.UPDATE) {
            cache.updateL1Cache(msg.getKey(),msg.getValue());
            log.info("更新本地缓存");
        }

        if (msg.getType()== CacheMsgType.DELETE) {
            log.info("删除本地缓存");
            cache.evictL1Cache(msg.getKey());
        }
    }
}
