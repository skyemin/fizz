package com.skye.cache.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
@NoArgsConstructor
@AllArgsConstructor
public class CacheMassage implements Serializable {
    private static final long serialVersionUID = -3574997636829868400L;

    private String cacheName;
    private CacheMsgType type;  //标识更新或删除操作
    private Object key;
    private Object value;
    private String msgSource;   //源主机标识，用来避免重复操作
}
