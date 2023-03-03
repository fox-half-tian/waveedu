package com.zhulang.waveedu.common.constant;

/**
 * @author 狐狸半面添
 * @create 2023-02-28 22:06
 */
public class MessageSdkSendErrorTypeConstants {
    /**
     * 普通作业发布命令发送到交换机失败
     */
    public static final Integer COMMON_HOMEWORK_PUBLISH_SEND_ERROR = 0;
    /**
     * 消息从交换机到队列错发送失败
     */
    public static final Integer EXCHANGE_TO_QUEUE_SEND_ERROR = 1;
    /**
     * 普通作业学生答案校验命令发送到交换机失败
     */
    public static final Integer COMMON_HOMEWORK_STU_ANSWER_VERIFY_SEND_ERROR = 2;
}
