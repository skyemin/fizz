package com.skye.spi;

import com.skye.spi.extension.ExtensionLoader;

import java.nio.charset.StandardCharsets;
import java.util.ServiceLoader;

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
 * @date Created in 2022年04月24日 14:44
 * @since 1.0
 */
public class Main {

    public static void main(String[] args) {
        dubboSpi();
    }

    public static void jdkSpi(){
        //会寻找所有包下的META-INF
        ServiceLoader<Compresser> serviceLoader = ServiceLoader.load(Compresser.class);
        for (Compresser service : serviceLoader) {
            System.out.println(service.getClass().getClassLoader());
            byte[] compress = service.compress("Hello".getBytes(StandardCharsets.UTF_8));
            System.out.println(new String(compress));
            byte[] decompress = service.decompress("Hello".getBytes(StandardCharsets.UTF_8));
            System.out.println(new String(decompress));
        }
    }
    public static void dubboSpi(){
        Compresser service = ExtensionLoader.getExtensionLoader(Compresser.class).getExtension("gzip");
        byte[] compress = service.compress("Hello".getBytes(StandardCharsets.UTF_8));
        System.out.println(new String(compress));
    }
}
