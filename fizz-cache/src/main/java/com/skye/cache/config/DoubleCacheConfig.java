package com.skye.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
@Data
@Component
@ConfigurationProperties(prefix = "doublecache")
public class DoubleCacheConfig {
    private Boolean allowNull = true;
    private Integer init = 100;
    private Integer max = 1000;
    private Long expireAfterWrite ;
    private Long expireAfterAccess;
    private Long refreshAfterWrite;
    private Long redisExpire;
}
