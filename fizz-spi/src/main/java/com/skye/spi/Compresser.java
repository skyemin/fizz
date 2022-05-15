package com.skye.spi;

import com.skye.spi.annotation.SPI;

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
@SPI
public interface Compresser {

    byte[] compress(byte[] bytes);
    byte[] decompress(byte[] bytes);
}
