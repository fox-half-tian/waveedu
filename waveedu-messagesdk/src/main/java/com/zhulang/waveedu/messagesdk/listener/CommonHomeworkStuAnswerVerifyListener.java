package com.zhulang.waveedu.messagesdk.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zhulang.waveedu.common.constant.RabbitConstants;
import com.zhulang.waveedu.common.util.WaveStrUtils;
import com.zhulang.waveedu.messagesdk.po.CommonHomeworkStuAnswer;
import com.zhulang.waveedu.messagesdk.po.CommonHomeworkStuScore;
import com.zhulang.waveedu.messagesdk.query.StuQuestionVerifyInfoQuery;
import com.zhulang.waveedu.messagesdk.service.CommonHomeworkStuAnswerService;
import com.zhulang.waveedu.messagesdk.service.CommonHomeworkStuScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    @Resource
    private CommonHomeworkStuScoreService commonHomeworkStuScoreService;
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 基于 RabbitListener 注解创建交换机、队列的时候，如果发现没有该交换机与队列的时候会自动创建
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(RabbitConstants.COMMON_HOMEWORK_STU_ANSWER_VERIFY_QUEUE_NAME),
            // 默认是 路由模式 Direct交换机
            exchange = @Exchange(name = RabbitConstants.COMMON_HOMEWORK_STU_ANSWER_VERIFY_EXCHANGE_NAME),
            key = RabbitConstants.COMMON_HOMEWORK_STU_ANSWER_VERIFY_ROUTING_KEY))
    public void listenerCommonHomeworkStuAnswerVerifyQueue(HashMap<String, Object> map) {
        Integer homeworkId = (Integer) map.get("homeworkId");
        Long stuId = Long.parseLong(map.get("stuId").toString());
        // 1.根据作业id和学生id查询到所有自己答案与问题参考答案、分值的信息
        List<StuQuestionVerifyInfoQuery> verifyInfoList = commonHomeworkStuAnswerService.getStuQuestionVerifyInfoList(homeworkId, stuId);
        if (verifyInfoList == null || verifyInfoList.isEmpty()) {
            throw new RuntimeException("根据作业id和学生id未查询到自己所有答案与问题参考答案、分值的信息");
        }
        // 2.遍历，对不同类型的问题进行处理，获取分数
        List<CommonHomeworkStuAnswer> commonHomeworkStuAnswers = new ArrayList<>(verifyInfoList.size());
        int allScore = 0;
        for (StuQuestionVerifyInfoQuery info : verifyInfoList) {
            int score = 0;
            try {
                switch (info.getQuestionType()) {
                    case 0:
                    case 3:
                        // 单选题与判断题，直接比较
                        if (info.getStuAnswer().equals(info.getSuggestAnswer())) {
                            // 相等则满分
                            score = info.getFullScore();
                        }
                        // 不相等就是0分，有默认为0
                        break;
                    case 1:
                        // 多选题
                        if (info.getStuAnswer().equals(info.getSuggestAnswer())) {
                            // 相等则满分
                            score = info.getFullScore();
                        } else {
                            String[] stuAnswers = WaveStrUtils.strSplitToArr(info.getStuAnswer(), ",");
                            String[] suggestAnswers = WaveStrUtils.strSplitToArr(info.getSuggestAnswer(), ",");
                            if (stuAnswers.length < suggestAnswers.length) {
                                // 设置一个标识，自己的答案在参考答案中是否存在
                                boolean flag;
                                // 每题的分数
                                int singleScore = info.getFullScore() / suggestAnswers.length;
                                // 遍历自己所有的答案
                                for (String stuAnswer : stuAnswers) {
                                    // 默认自己的这个答案在参考答案中不存在
                                    flag = false;
                                    // 拿这自己这个答案去与 参考答案中的每个答案进行比较
                                    for (String suggestAnswer : suggestAnswers) {
                                        // 如果相等说明存在
                                        if (stuAnswer.equals(suggestAnswer)) {
                                            // 分数增加
                                            score += singleScore;
                                            // 标识为该答案存在
                                            flag = true;
                                            // 退出内循环
                                            break;
                                        }
                                    }
                                    // 如果自己的这个答案在参考答案中不存在，则记为0分
                                    if (!flag) {
                                        score = 0;
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    case 2:
                        // 填空题
                        if (info.getStuAnswer().equalsIgnoreCase(info.getSuggestAnswer())) {
                            score = info.getFullScore();
                        } else {
                            ArrayList stuAnswers = JSON.parseObject(info.getStuAnswer(), ArrayList.class);
                            ArrayList suggestAnswers = JSON.parseObject(info.getSuggestAnswer(), ArrayList.class);
                            if (stuAnswers.size() != suggestAnswers.size()) {
                                throw new RuntimeException("普通作业的填空题的学生答案与参考答案数量不一致，作业id为 " + homeworkId
                                        + "，答案id为" + info.getAnswerId() + "，学生答案为 " + info.getSuggestAnswer() + "，参考答案为：" + suggestAnswers);
                            }
                            // 每个题的分数
                            int singleScore = info.getFullScore() / suggestAnswers.size();
                            // 每个进行比较
                            for (int i = 0; i < suggestAnswers.size(); i++) {
                                // 如果相等，就加分
                                if (stuAnswers.get(i).toString().equalsIgnoreCase(suggestAnswers.get(i).toString())) {
                                    score += singleScore;
                                }
                            }
                        }
                        break;
                    default:
                        // 错误日志，但这种情况几乎不会发生，在添加或修改题目时已经进行了校验判断
                        throw new RuntimeException("与作业类型不匹配的题目，作业id为 " + homeworkId + "，问题类型为 " + info.getQuestionType());
                }
            } catch (Exception e) {
                // 日志记录
                log.error("系统检测学生普通作业时发生异常：{}" + e.getMessage());
                // 发送到错误交换机
//                HashMap<String, Object> errorMap = new HashMap<>(1);
                map.put("errorMessage", "系统检测学生普通作业时发生异常：{}" + e.getMessage());
                rabbitTemplate.convertAndSend(RabbitConstants.ERROR_EXCHANGE, RabbitConstants.ERROR_ROUTING_KEY, map);
                score = 0;
            }
            allScore += score;
            // 3.将数据添加到集合中
            CommonHomeworkStuAnswer stuAnswer = new CommonHomeworkStuAnswer();
            stuAnswer.setId(info.getAnswerId());
            stuAnswer.setScore(score);
            commonHomeworkStuAnswers.add(stuAnswer);
        }
        // 4.批量插入
        commonHomeworkStuAnswerService.updateBatchById(commonHomeworkStuAnswers);
        // 5.修改总分数以及状态
        commonHomeworkStuScoreService.update(new LambdaUpdateWrapper<CommonHomeworkStuScore>()
                .eq(CommonHomeworkStuScore::getHomeworkId, homeworkId)
                .eq(CommonHomeworkStuScore::getStuId, stuId)
                .set(CommonHomeworkStuScore::getStatus, 1)
                .set(CommonHomeworkStuScore::getScore, allScore));
    }
}
