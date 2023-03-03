package com.zhulang.waveedu.messagesdk.listener;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zhulang.waveedu.common.constant.RabbitConstants;
import com.zhulang.waveedu.messagesdk.po.LessonClassCommonHomework;
import com.zhulang.waveedu.messagesdk.service.CommonHomeworkQuestionService;
import com.zhulang.waveedu.messagesdk.service.LessonClassCommonHomeworkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * 工作模式 + 延迟队列
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
    @Resource
    private CommonHomeworkQuestionService commonHomeworkQuestionService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @RabbitListener(queues = RabbitConstants.COMMON_HOMEWORK_PUBLISH_DELAYED_QUEUE_NAME)
    public void listenerCommonHomeworkPublishQueue(HashMap<String, Object> map){
        Integer id = (Integer) map.get("commonHomeworkId");
        LocalDateTime startTime = LocalDateTime.parse((String) map.get("startTime"), formatter);


        // 如果预发布状态，并且时间正是该延迟消息设置的开始时间，则设置状态为已发布
        // 如果状态是0，说明状态改为了未发布，那就没必要修改状态
        // 如果状态是1，说明状态已经是已发布，则没必要修改状态了
        // 只有是预发布的作业才可以修改状态为已发布
        if (lessonClassCommonHomeworkService.existsByIdAndStartTimeAndIsPublish(id, startTime, 2)) {
            // 查询到作业的总分数
            Integer totalScore = commonHomeworkQuestionService.getTotalScoreByCommonHomeworkId(id);
            // 保存分数，并修改状态
            lessonClassCommonHomeworkService.update(new LambdaUpdateWrapper<LessonClassCommonHomework>()
                    .eq(LessonClassCommonHomework::getId, id)
                    .set(LessonClassCommonHomework::getIsPublish, 1)
                    .set(LessonClassCommonHomework::getTotalScore, totalScore)
            );
        }
//        lessonClassCommonHomeworkService.update(new LambdaUpdateWrapper<LessonClassCommonHomework>()
//                .eq(LessonClassCommonHomework::getId, id)
//                .eq(LessonClassCommonHomework::getStartTime, startTime)
//
//                .eq(LessonClassCommonHomework::getIsPublish, 2)
//                .set(LessonClassCommonHomework::getIsPublish, 1));
    }

    /**
     * 测试
     */
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = "work.queue"),
//            // 交换机 durable 默认就是 true
//            exchange = @Exchange(name = "work.exchange", type = ExchangeTypes.DIRECT, durable = "true"),
//            key = "work.key"))
    @RabbitListener(queues = "work.queue")
    public void listenerWorkQueue1(Message message) throws Exception {
        log.info("监听器 1 接收到的消息：{}", new String(message.getBody()));
    }
}
