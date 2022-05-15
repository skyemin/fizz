package com.skye.spi;

import java.nio.charset.StandardCharsets;

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
 * @date Created in 2022年04月24日 14:36
 * @since 1.0
 */
//@SPIExtension("zip")
public class ZipCompresser implements Compresser{

    @Override
    public byte[] compress(byte[] bytes) {
        return "compress by Zip".getBytes(StandardCharsets.UTF_8);
    }
    @Override
    public byte[] decompress(byte[] bytes) {
        return "decompress by Zip".getBytes(StandardCharsets.UTF_8);
    }
}
