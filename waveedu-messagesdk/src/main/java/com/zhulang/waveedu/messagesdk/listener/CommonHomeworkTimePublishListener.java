package com.zhulang.waveedu.messagesdk.listener;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zhulang.waveedu.common.constant.RabbitConstants;
import com.zhulang.waveedu.messagesdk.po.LessonClassCommonHomework;
import com.zhulang.waveedu.messagesdk.service.LessonClassCommonHomeworkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * 工作模式 todo 需要修改
 * 普通作业定时发布的监听器
 *
 * @author 狐狸半面添
 * @create 2023-02-28 21:28
 */
@Component
@Slf4j
public class CommonHomeworkTimePublishListener {
    @Resource
    private LessonClassCommonHomeworkService lessonClassCommonHomeworkService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @RabbitListener(queues = RabbitConstants.COMMON_HOMEWORK_PUBLISH_DELAYED_QUEUE_NAME)
    public void listenerCommonHomeworkPublishQueue(HashMap<String, Object> map) throws Exception {
        Integer id = (Integer) map.get("commonHomeworkId");
        LocalDateTime startTime =  LocalDateTime.parse((String)map.get("startTime"),formatter);
        // 如果预发布状态，并且时间正是该延迟消息设置的开始时间，则设置状态为已发布
//        if(lessonClassCommonHomeworkService.existsByIdAndStartTimeAndIsPublish(id,startTime,2)){
//            lessonClassCommonHomeworkService.update(new LambdaUpdateWrapper<LessonClassCommonHomework>()
//                    .eq(LessonClassCommonHomework::getId,id)
//                    .set(LessonClassCommonHomework::getIsPublish,1));
//        }
        lessonClassCommonHomeworkService.update(new LambdaUpdateWrapper<LessonClassCommonHomework>()
                .eq(LessonClassCommonHomework::getId, id)
                .eq(LessonClassCommonHomework::getStartTime, startTime)
                // 如果状态是0，说明状态改为了未发布，那就没必要修改状态
                // 如果状态是1，说明状态已经是已发布，则没必要修改状态了
                // 只有是预发布的作业才可以修改状态为已发布
                .eq(LessonClassCommonHomework::getIsPublish, 2)
                .set(LessonClassCommonHomework::getIsPublish, 1));
    }

    /**
     * 测试
     */
    @RabbitListener(queues = "work.queue")
    public void listenerWorkQueue1(Integer message) throws Exception {
        log.info("监听器 1 接收到的消息：{}", message);
    }
}
