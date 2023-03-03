package com.zhulang.waveedu.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.constant.MessageSdkSendErrorTypeConstants;
import com.zhulang.waveedu.common.constant.RabbitConstants;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.po.CommonHomeworkStuAnswer;
import com.zhulang.waveedu.edu.dao.CommonHomeworkStuAnswerMapper;
import com.zhulang.waveedu.edu.po.CommonHomeworkStuScore;
import com.zhulang.waveedu.edu.po.LessonClassCommonHomework;
import com.zhulang.waveedu.edu.po.MessageSdkSendErrorLog;
import com.zhulang.waveedu.edu.query.homeworkquery.HomeworkIdAndTypeQuery;
import com.zhulang.waveedu.edu.service.CommonHomeworkQuestionService;
import com.zhulang.waveedu.edu.service.CommonHomeworkStuAnswerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.service.CommonHomeworkStuScoreService;
import com.zhulang.waveedu.edu.service.MessageSdkSendErrorLogService;
import com.zhulang.waveedu.edu.vo.homeworkvo.HomeworkAnswerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 普通作业表的学生回答表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-04
 */
@Service
@Slf4j
public class CommonHomeworkStuAnswerServiceImpl extends ServiceImpl<CommonHomeworkStuAnswerMapper, CommonHomeworkStuAnswer> implements CommonHomeworkStuAnswerService {
    @Resource
    private CommonHomeworkStuAnswerMapper commonHomeworkStuAnswerMapper;
    @Resource
    private CommonHomeworkQuestionService commonHomeworkQuestionService;
    @Resource
    private CommonHomeworkStuScoreService commonHomeworkStuScoreService;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private MessageSdkSendErrorLogService messageSdkSendErrorLogService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result verifyAnswers(List<HomeworkAnswerVO> homeworkAnswerVos) {
        // todo 查询截止时间
        // 1.查询作业的类型与作业的id
        HomeworkIdAndTypeQuery info = commonHomeworkQuestionService.getHomeworkIdAndTypeById(homeworkAnswerVos.get(0).getQuestionId());
        if (info == null) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "作业或问题信息不存在");
        }
        Long userId = UserHolderUtils.getUserId();
        // 2.判断该学生对于该作业的问题解答是否已经存在
        if (commonHomeworkStuAnswerMapper.exists(new LambdaQueryWrapper<CommonHomeworkStuAnswer>()
                .eq(CommonHomeworkStuAnswer::getHomeworkId, info.getHomeworkId())
                .eq(CommonHomeworkStuAnswer::getStuId, userId))) {
            return Result.error(HttpStatus.HTTP_REPEAT_SUCCESS_OPERATE.getCode(), "已提交，请勿重复操作");
        }

        // 3.将答案信息存储
        CommonHomeworkStuAnswer stuAnswer;
        List<CommonHomeworkStuAnswer> stuAnswers = new ArrayList<>(homeworkAnswerVos.size());
        for (HomeworkAnswerVO answerVO : homeworkAnswerVos) {
            stuAnswer = BeanUtil.copyProperties(answerVO, CommonHomeworkStuAnswer.class);
            stuAnswer.setStuId(UserHolderUtils.getUserId());
            stuAnswers.add(stuAnswer);
        }
        // 批量加入到数据库
        commonHomeworkStuAnswerMapper.insertBatchSomeColumn(stuAnswers);
        // 4.添加学生作业分数表信息
        CommonHomeworkStuScore commonHomeworkStuScore = new CommonHomeworkStuScore();
        commonHomeworkStuScore.setHomeworkId(info.getHomeworkId());
        commonHomeworkStuScore.setStuId(userId);
        commonHomeworkStuScoreService.save(commonHomeworkStuScore);

        // 5.如果作业类型是探究型，则直接返回
        if (info.getHomeworkType() == 0) {
            return Result.ok();
        }

        // 6.如果作业类型为其它，则交给rabbitmq工作队列进行校验处理
        // 6.1 创建一个消息回调对象，需要指定一个唯一的id，因为每一个消息发送成功或失败都需要回调，用于区分是哪一个消息
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        // 6.2 注册回调函数
        correlationData.getFuture().addCallback(new SuccessCallback<CorrelationData.Confirm>() {

            /**
             * 回调实际：消息发送到交换机，成功或者失败的时候都会调用
             *
             * @param result the result
             */
            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                // 获取本次发送消息的结果
                boolean ack = result.isAck();
                if (!ack) {
                    log.error("普通作业学生答案校验的消息没有到达交换机，原因是：{}", result.getReason());
                    // 消息没有到达交换机，则需要重发，重发三次之后如果还是失败，那么这个消息需要存储到数据库中做兜底工作
                    MessageSdkSendErrorLog messageSdkSendErrorLog = new MessageSdkSendErrorLog();
                    // 设置错误信息
                    messageSdkSendErrorLog.setErrorMsg(result.getReason());
                    // /设置发送者
                    messageSdkSendErrorLog.setSender(userId + "");
                    // 设置消息内容
                    messageSdkSendErrorLog.setContent(homeworkAnswerVos.toString());
                    // 设置消息备注
                    messageSdkSendErrorLog.setRemark("普通作业学生答案校验命令发送到交换机失败");
                    // 设置消息类型
                    messageSdkSendErrorLog.setType(MessageSdkSendErrorTypeConstants.COMMON_HOMEWORK_STU_ANSWER_VERIFY_SEND_ERROR);
                    // 保存错误信息
                    messageSdkSendErrorLogService.save(messageSdkSendErrorLog);
                    // 批量删除学生答案表所有该学生的对应问题id的信息
                    List<Integer> questionIds = new ArrayList<>(homeworkAnswerVos.size());
                    for (HomeworkAnswerVO homeworkAnswerVo : homeworkAnswerVos) {
                        questionIds.add(homeworkAnswerVo.getQuestionId());
                    }
                    commonHomeworkStuAnswerMapper.deleteBatchByStuIdAndQuestionIds(userId, questionIds);
                }
            }
        }, new FailureCallback() {
            /**
             * 这个方法回调的时机：
             * 消息到达交换机，但是交换机的消息到队列的时候，队列没有给消息回执的时候调用
             * 这种情况发生很少
             *
             * @param ex the failure
             */
            @Override
            public void onFailure(Throwable ex) {
                log.error("普通作业学生答案校验命令队列没有给消息回执原因：{}" + ex.getMessage());
            }
        });
        // 6.3 设置发送的内容
        HashMap<String, Object> sendMap = new HashMap<>(2);
        sendMap.put("homeworkId", info.getHomeworkId());
        sendMap.put("stuId", userId);
        // 6.4 异步发送到延迟队列
        rabbitTemplate.convertAndSend(RabbitConstants.COMMON_HOMEWORK_STU_ANSWER_VERIFY_EXCHANGE_NAME,
                RabbitConstants.COMMON_HOMEWORK_STU_ANSWER_VERIFY_ROUTING_KEY,
                sendMap,
                correlationData
        );
        // 返回ok
        return Result.ok();
    }
}
