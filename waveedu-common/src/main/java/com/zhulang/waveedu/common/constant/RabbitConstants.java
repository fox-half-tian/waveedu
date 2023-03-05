package com.zhulang.waveedu.common.constant;

/**
 * @author 狐狸半面添
 * @create 2023-02-28 21:07
 */
public class RabbitConstants {
    /**
     * 错误交换机
     */
    public static final String ERROR_EXCHANGE = "error.exchange";
    /**
     * 错误队列
     */
    public static final String ERROR_QUEUE = "error.queue";
    /**
     * 错误路由
     */
    public static final String ERROR_ROUTING_KEY = "error.key";

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
    /**
     * 笔记目录删除工作队列
     */
    public static final String NOTE_DIR_DEL_QUEUE_NAME=  "note.dir.del.queue";
    /**
     * 笔记目录删除交换机
     */
    public static final String NOTE_DIR_DEL_EXCHANGE_NAME =  "note.dir.del.exchange";
    /**
     * 笔记目录删除交换机到工作队列的路由
     */
    public static final String NOTE_DIR_DEL_QUEUE_ROUTING_KEY = "del";
}
