package com.skye.cache.msg;

import com.skye.common.utils.SpringContextUtil;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
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
public class MessageSourceUtil {
    public static String getMsgSource() throws UnknownHostException {
        String host = InetAddress.getLocalHost().getHostAddress();
        Environment env = SpringContextUtil.getBean(Environment.class);
        String port = env.getProperty("server.port");
        return host+":"+port;
    }
}
