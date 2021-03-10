package com.github.flying.jeelite.modules.mq.api;

/**
 * 消息发送服务，用来向交换机发送消息
 */
public interface SenderService {

    /**
     * 测试广播模式.
     *
     */
    void broadcast(String p);

    /**
     * 测试Direct模式.
     *
     */
    void direct(String p);

    /**
     * 测试Topic模式1.
     *
     */
    void topic1(String p);

    /**
     * 测试Topic模式2.
     *
     */
    void topic2(String p);

}