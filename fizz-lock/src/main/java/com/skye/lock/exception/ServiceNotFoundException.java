package com.skye.lock.exception;

/**
 * 没有找到相应的锁服务实现类
 *
 * @author TanRq
 */
public class ServiceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -8199483743071016533L;

    public ServiceNotFoundException() {
        super("没有找到相应的锁服务实现类");
    }

}
