package com.skye.lock.exception;

/**
 * 未知订阅操作的负载均衡模式类型
 *
 * @author 54lxb
 * @version 1.1.0
 * @apiNote 知识改变命运，技术改变世界
 * @since 2018-12-23 15:32
 */
public class UnknownSubscriptionModeException extends RuntimeException {

    private static final long serialVersionUID = -2357364241653230393L;

    public UnknownSubscriptionModeException() {
        super("未知订阅操作的负载均衡模式类型");
    }

}
