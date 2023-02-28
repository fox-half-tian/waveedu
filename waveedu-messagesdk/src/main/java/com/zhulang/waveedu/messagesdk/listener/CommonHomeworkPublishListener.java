package com.zhulang.waveedu.messagesdk.listener;

import com.zhulang.waveedu.common.constant.RabbitConstants;
import com.zhulang.waveedu.messagesdk.service.CommonHomeworkStuScoreService;
import com.zhulang.waveedu.messagesdk.service.LessonClassCommonHomeworkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 工作模式 todo 需要修改
 * 普通作业发布的监听器
 *
 * @author 狐狸半面添
 * @create 2023-02-28 21:28
 */
@Component
@Slf4j
public class CommonHomeworkPublishListener {
    @Resource
    private CommonHomeworkStuScoreService commonHomeworkStuScoreService;
    @Resource
    private LessonClassCommonHomeworkService lessonClassCommonHomeworkService;

    @RabbitListener(queues = RabbitConstants.COMMON_HOMEWORK_PUBLISH_QUEUE)
    public void listenerCommonHomeworkPublishQueue(Integer commonHomeworkId) throws Exception {

    }

    /**
     * 测试
     */
    @RabbitListener(queues = "work.queue")
    public void listenerWorkQueue1(Integer message) throws Exception {
        log.info("监听器 1 接收到的消息：{}", message);
    }
}
