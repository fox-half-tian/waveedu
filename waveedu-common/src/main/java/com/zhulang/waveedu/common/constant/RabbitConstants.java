package com.zhulang.waveedu.common.constant;

/**
 * @author 狐狸半面添
 * @create 2023-02-28 21:07
 */
public class RabbitConstants {
    /**
     * 普通作业发布延迟队列
     */
    public static final String COMMON_HOMEWORK_PUBLISH_DELAYED_QUEUE_NAME = "common.homework.publish.delayed.queue";
    /**
     * 普通作业发布延迟交换机
     */
    public static final String COMMON_HOMEWORK_PUBLISH_DELAYED_EXCHANGE_NAME = "common.homework.publish.delayed.exchange";
    /**
     * 普通作业发布延迟交换机到延迟队列的路由
     */
    public static final String COMMON_HOMEWORK_PUBLISH_ROUTING_KEY = "publish";
    /**
     * 普通作业学生答案校验队列
     */
    public static final String COMMON_HOMEWORK_STU_ANSWER_VERIFY_QUEUE_NAME=  "common.homework.stu.answer.verify.queue";
    /**
     * 普通作业学生答案校验交换机
     */
    public static final String COMMON_HOMEWORK_STU_ANSWER_VERIFY_EXCHANGE_NAME=  "common.homework.stu.answer.verify.exchange";
    /**
     * 普通作业学生答案校验交换机到延迟队列的路由
     */
    public static final String COMMON_HOMEWORK_STU_ANSWER_VERIFY_ROUTING_KEY = "verify";
}
