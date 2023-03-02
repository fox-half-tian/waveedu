package com.zhulang.waveedu.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.constant.MessageSdkSendErrorTypeConstants;
import com.zhulang.waveedu.common.constant.RabbitConstants;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.po.CommonHomeworkQuestion;
import com.zhulang.waveedu.edu.po.LessonClass;
import com.zhulang.waveedu.edu.po.LessonClassCommonHomework;
import com.zhulang.waveedu.edu.dao.LessonClassCommonHomeworkMapper;
import com.zhulang.waveedu.edu.po.MessageSdkSendErrorLog;
import com.zhulang.waveedu.edu.query.homeworkquery.TchHomeworkSimpleInfoQuery;
import com.zhulang.waveedu.edu.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.vo.homeworkvo.ModifyCommonHomeworkVo;
import com.zhulang.waveedu.edu.vo.homeworkvo.PublishCommonHomeworkVO;
import com.zhulang.waveedu.edu.vo.homeworkvo.SaveCommonHomeworkVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 课程班级的普通作业表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-27
 */
@Service
@Slf4j
public class LessonClassCommonHomeworkServiceImpl extends ServiceImpl<LessonClassCommonHomeworkMapper, LessonClassCommonHomework> implements LessonClassCommonHomeworkService {
    @Resource
    private LessonClassCommonHomeworkMapper lessonClassCommonHomeworkMapper;
    @Resource
    private LessonClassService lessonClassService;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private MessageSdkSendErrorLogService messageSdkSendErrorLogService;
    @Resource
    private CommonHomeworkQuestionService commonHomeworkQuestionService;

    @Override
    public Result saveHomework(SaveCommonHomeworkVO saveCommonHomeworkVO) {
        Long userId = UserHolderUtils.getUserId();
        // 1.判断是否为班级创建者
        if (!lessonClassService.existsByUserIdAndClassId(userId, saveCommonHomeworkVO.getLessonClassId())) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 2.属性转换
        LessonClassCommonHomework homework = BeanUtil.copyProperties(saveCommonHomeworkVO, LessonClassCommonHomework.class);
        // 3.添加创建者
        homework.setCreatorId(userId);
        // 3.插入数据
        lessonClassCommonHomeworkMapper.insert(homework);
        // 5.返回作业id
        return Result.ok(homework.getId());
    }

    @Override
    public Result publish(PublishCommonHomeworkVO publishCommonHomeworkVO) {
        // 1.校验创建者，发布状况
        Map<String, Object> map = this.getMap(new LambdaQueryWrapper<LessonClassCommonHomework>()
                .eq(LessonClassCommonHomework::getId, publishCommonHomeworkVO.getCommonHomeworkId())
                .select(LessonClassCommonHomework::getIsPublish, LessonClassCommonHomework::getCreatorId));
        // 1.1 是否为创建者
        if (!map.get("creatorId").toString().equals(UserHolderUtils.getUserId().toString())) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 1.2 如果是已经发布了，就不能发布
        if ((Integer) map.get("isPublish") == 1) {
            return Result.error(HttpStatus.HTTP_REPEAT_SUCCESS_OPERATE.getCode(), "作业已发布，请勿重复操作");
        }
        // 2.查询题目数量，如果为0则无法发布
        long count = commonHomeworkQuestionService.count(new LambdaQueryWrapper<CommonHomeworkQuestion>()
                .eq(CommonHomeworkQuestion::getCommonHomeworkId, publishCommonHomeworkVO.getCommonHomeworkId()));
        if (count == 0) {
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "请先添加至少一道题目");
        }

        // 3.如果是定时发布就加入到延迟队列
        if (publishCommonHomeworkVO.getIsRegularTime() == 1) {
            // 3.1 判断是否设置定时时间
            if (publishCommonHomeworkVO.getStartTime() == null) {
                return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "未设置发布时间");
            }
            // 3.2 创建一个消息回调对象，需要指定一个唯一的id，因为每一个消息发送成功或失败都需要回调，用于区分是哪一个消息
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            // 3.3 注册回调函数
            correlationData.getFuture().addCallback(new SuccessCallback<CorrelationData.Confirm>() {
                private int count = 0;

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
                        log.error("发布普通作业任务的消息没有到达交换机，原因是：{}", result.getReason());
                        if (count == 3) {
                            // 消息没有到达交换机，则需要重发，重发三次之后如果还是失败，那么这个消息需要存储到数据库中做兜底工作
                            MessageSdkSendErrorLog messageSdkSendErrorLog = new MessageSdkSendErrorLog();
                            // 设置错误信息
                            messageSdkSendErrorLog.setErrorMsg(result.getReason());
                            // /设置发送者
                            messageSdkSendErrorLog.setSender(UserHolderUtils.getUserId() + "");
                            // 设置消息内容
                            messageSdkSendErrorLog.setContent(publishCommonHomeworkVO.getCommonHomeworkId() + "");
                            // 设置消息备注
                            messageSdkSendErrorLog.setRemark("发送到普通作业预发布交换机错误");
                            // 设置消息类型
                            messageSdkSendErrorLog.setType(MessageSdkSendErrorTypeConstants.COMMON_HOMEWORK_PUBLISH_SEND_ERROR);
                            // 保存错误信息
                            messageSdkSendErrorLogService.save(messageSdkSendErrorLog);
                            // 修改作业状态为0-未发布
                            lessonClassCommonHomeworkMapper.update(null, new LambdaUpdateWrapper<LessonClassCommonHomework>()
                                    .eq(LessonClassCommonHomework::getId, publishCommonHomeworkVO.getCommonHomeworkId())
                                    .set(LessonClassCommonHomework::getIsPublish, 0));
                        } else {
                            // 重发
                            count++;
                            // 设置发送的内容
                            HashMap<String, Object> sendMap = new HashMap<>(2);
                            sendMap.put("commonHomeworkId", publishCommonHomeworkVO.getCommonHomeworkId());
                            sendMap.put("startTime", publishCommonHomeworkVO.getStartTime());
                            // 设置发送消息的延迟时长（单位 ms）
                            int delayedTime = (int) Duration.between(LocalDateTime.now(), publishCommonHomeworkVO.getStartTime()).toMillis();
                            // 异步发送到消息队列 todo 有bug，在交换机不存在情况下无法重发三次
//                            correlationData.setId(UUID.randomUUID().toString());
                            rabbitTemplate.convertAndSend(RabbitConstants.COMMON_HOMEWORK_PUBLISH_DELAYED_EXCHANGE_NAME,
                                    RabbitConstants.COMMON_HOMEWORK_PUBLISH_ROUTING_KEY,
                                    sendMap,
                                    msg -> {
                                        msg.getMessageProperties().setDelay(delayedTime);
                                        return msg;
                                    }, correlationData
                            );
                        }

                    }
                }
            }, new FailureCallback() {
                /**
                 * 这个方法回调的时机：
                 * 消息到达交换机，但是交换机的消息到队列的时候，队列没有给消息绘制的时候调用
                 * 这种情况发生很少
                 *
                 * @param ex the failure
                 */
                @Override
                public void onFailure(Throwable ex) {
                    log.error("发送到普通作业预发布交换机失败原因：{}" + ex.getMessage());
                }
            });

            // 3.4 设置发送的内容
            HashMap<String, Object> sendMap = new HashMap<>(2);
            sendMap.put("commonHomeworkId", publishCommonHomeworkVO.getCommonHomeworkId());
            sendMap.put("startTime", publishCommonHomeworkVO.getStartTime());

            // 3.5 设置发送消息的延迟时长（单位 ms）
            long delayedTime = Duration.between(LocalDateTime.now(), publishCommonHomeworkVO.getStartTime()).toMillis();
            if (delayedTime > 2073600000) {
                return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "定时不能距离当前超过24天");
            }

            // 3.6 异步发送到延迟队列
            rabbitTemplate.convertAndSend(RabbitConstants.COMMON_HOMEWORK_PUBLISH_DELAYED_EXCHANGE_NAME,
                    RabbitConstants.COMMON_HOMEWORK_PUBLISH_ROUTING_KEY,
                    sendMap,
                    msg -> {
                        msg.getMessageProperties().setDelay((int) delayedTime);
                        return msg;
                    },
                    correlationData
            );
            // 3.7 将时间保存到数据库，并修改状态为 定时发布中
            this.update(new LambdaUpdateWrapper<LessonClassCommonHomework>()
                    .eq(LessonClassCommonHomework::getId, publishCommonHomeworkVO.getCommonHomeworkId())
                    .set(LessonClassCommonHomework::getStartTime, publishCommonHomeworkVO.getStartTime())
                    .set(LessonClassCommonHomework::getIsPublish, 2));
            return Result.ok();
        }

        // 4.如果不是定时发布，就获取总分数，并修改发布状态为已发布，设置发布时间为当前时间
        Integer totalScore =
                commonHomeworkQuestionService.getTmpTotalScoreByCommonHomeworkId(publishCommonHomeworkVO.getCommonHomeworkId());
        this.update(new LambdaUpdateWrapper<LessonClassCommonHomework>()
                .eq(LessonClassCommonHomework::getId, publishCommonHomeworkVO.getCommonHomeworkId())
                .set(LessonClassCommonHomework::getStartTime, LocalDateTime.now())
                .set(LessonClassCommonHomework::getIsPublish, 1)
                .set(LessonClassCommonHomework::getTotalScore, totalScore)
        );

        // 5.返回
        return Result.ok();
    }

    @Override
    public Result modifyInfo(ModifyCommonHomeworkVo modifyCommonHomeworkVo) {
        // 1.校验是否为创建者
        Long creatorId = lessonClassCommonHomeworkMapper.selectCreatorIdById(modifyCommonHomeworkVo.getId());
        if (creatorId == null) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "作业信息不存在");
        }
        if (!creatorId.equals(UserHolderUtils.getUserId())) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 2.作业标题如果传，则需校验格式
        String title = modifyCommonHomeworkVo.getTitle();
        if (StrUtil.isBlank(title)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "作业标题不允许为空");
        }
        modifyCommonHomeworkVo.setTitle(title.trim());
        // 3.修改数据库信息
        lessonClassCommonHomeworkMapper.updateById(BeanUtil.copyProperties(modifyCommonHomeworkVo, LessonClassCommonHomework.class));
        // 4.返回
        return Result.ok();
    }

    @Override
    public Result modifyCancelPreparePublish(Integer homeworkId) {
        // 1.校验是否为创建者
        Long creatorId = lessonClassCommonHomeworkMapper.selectCreatorIdById(homeworkId);
        if (creatorId == null) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "作业信息不存在");
        }
        if (!creatorId.equals(UserHolderUtils.getUserId())) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 2.将状况为预发布的作业修改为未发布
        int recordCount = lessonClassCommonHomeworkMapper.update(null, new LambdaUpdateWrapper<LessonClassCommonHomework>()
                .eq(LessonClassCommonHomework::getId, homeworkId)
                .eq(LessonClassCommonHomework::getIsPublish, 2)
                .set(LessonClassCommonHomework::getIsPublish, 0));
        if (recordCount == 0) {
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "只允许将预发布的作业修改为未发布");
        }
        // 3.返回
        return Result.ok();
    }

    @Override
    public Result getTchHomeworkDetailListInfo(Long classId, Integer isPublish) {
        // 1.校验是否为创建者
        if (!lessonClassService.existsByUserIdAndClassId(UserHolderUtils.getUserId(), classId)) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 2.查询班级总人数
        Map<String, Object> map = lessonClassService.getMap(new LambdaQueryWrapper<LessonClass>()
                .eq(LessonClass::getId, classId)
                .select(LessonClass::getNum));
        if (map == null || map.isEmpty()) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "班级信息不存在");
        }
        // 3.查询作业详情
        List<LessonClassCommonHomework> listInfo = lessonClassCommonHomeworkMapper
                .selectList(new LambdaQueryWrapper<LessonClassCommonHomework>()
                        .eq(LessonClassCommonHomework::getLessonClassId, classId)
                        .eq(isPublish != null, LessonClassCommonHomework::getIsPublish, isPublish)
                        .orderByDesc(LessonClassCommonHomework::getCreateTime)
                );
        // 4.返回
        map.put("homeworkInfoList", listInfo);
        return Result.ok(map);
    }

    @Override
    public Result getTchHomeworkSimpleListInfo(Long classId, Integer isPublish) {
        // 0.格式校验
        if (RegexUtils.isSnowIdInvalid(classId) || (isPublish != null && (isPublish < 0 || isPublish > 2))) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), HttpStatus.HTTP_BAD_REQUEST.getValue());
        }
        // 1.校验是否为创建者
        if (!lessonClassService.existsByUserIdAndClassId(UserHolderUtils.getUserId(), classId)) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 2.查询
        List<TchHomeworkSimpleInfoQuery> listInfo = lessonClassCommonHomeworkMapper.getTchHomeworkSimpleListInfo(classId, isPublish);
        // 3.返回
        return Result.ok(listInfo);
    }

    @Override
    public boolean existsByIdAndUserId(Integer id, Long userId) {
        return lessonClassCommonHomeworkMapper.existsByIdAndUserId(id, userId) != null;
    }
}
