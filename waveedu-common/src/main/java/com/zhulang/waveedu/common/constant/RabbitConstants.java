package com.zhulang.waveedu.common.constant;

/**
 * @author 狐狸半面添
 * @create 2023-02-28 21:07
 */
public class RabbitConstants {
    /**
     * 延迟队列
     */
    public static final String COMMON_HOMEWORK_PUBLISH_DELAYED_QUEUE_NAME = "common.homework.publish.delayed.queue";
    /**
     * 延迟交换机
     */
    public static final String COMMON_HOMEWORK_PUBLISH_DELAYED_EXCHANGE_NAME = "common.homework.publish.delayed.exchange";
    /**
     * routingKey
     */
    public static final String COMMON_HOMEWORK_PUBLISH_ROUTING_KEY = "publish";
}
