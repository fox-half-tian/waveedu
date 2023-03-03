package com.zhulang.waveedu.messagesdk.listener;

import com.zhulang.waveedu.common.constant.RabbitConstants;
import com.zhulang.waveedu.messagesdk.po.CommonHomeworkStuAnswer;
import com.zhulang.waveedu.messagesdk.query.StuQuestionVerifyInfoQuery;
import com.zhulang.waveedu.messagesdk.service.CommonHomeworkStuAnswerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 工作模式
 *
 * @author 狐狸半面添
 * @create 2023-03-04 3:21
 */
@Component
@Slf4j
public class CommonHomeworkStuAnswerVerifyListener {
    @Resource
    private CommonHomeworkStuAnswerService commonHomeworkStuAnswerService;

    /**
     * 基于 RabbitListener 注解创建交换机、队列的时候，如果发现没有该交换机与队列的时候会自动创建
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(RabbitConstants.COMMON_HOMEWORK_STU_ANSWER_VERIFY_QUEUE_NAME),
            exchange = @Exchange(name = RabbitConstants.COMMON_HOMEWORK_STU_ANSWER_VERIFY_EXCHANGE_NAME, type = ExchangeTypes.DIRECT),
            key = RabbitConstants.COMMON_HOMEWORK_STU_ANSWER_VERIFY_ROUTING_KEY))
    public void listenerCommonHomeworkStuAnswerVerifyQueue(HashMap<String, Object> map) {
        Integer homeworkId = (Integer) map.get("homeworkId");
        Long stuId = (Long) map.get("stuId");
        // 1.根据作业id和学生id查询到所有自己答案与问题参考答案、分值的信息
        List<StuQuestionVerifyInfoQuery> verifyInfoList = commonHomeworkStuAnswerService.getStuQuestionVerifyInfoList(homeworkId, stuId);
        if (verifyInfoList == null) {
            throw new RuntimeException();
        }
        // 2.遍历，对不同类型的问题进行处理
        Integer score;
        CommonHomeworkStuAnswer commonHomeworkStuAnswer;
        for (StuQuestionVerifyInfoQuery info : verifyInfoList) {
            switch (info.getQuestionType()) {
                case 0:
                case 3:
                    // 单选题与判断题，直接比较
                    if (info.getStuAnswer().equals(info.getSuggestAnswer())) {
                        // 相等则满分
                        score = info.getFullScore();
                    } else {
                        // 不相等就是0分
                        score = 0;
                    }
                    break;
                case 1:
                    // 多选题
                    break;
                case 2:
                    // 填空题
                    break;
                default:
                    // 错误日志
                    throw new RuntimeException();
            }
        }

    }
}
