package com.zhulang.waveedu.edu.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.constant.MessageSdkSendErrorTypeConstants;
import com.zhulang.waveedu.common.constant.RabbitConstants;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.po.*;
import com.zhulang.waveedu.edu.dao.LessonClassProgramHomeworkMapper;
import com.zhulang.waveedu.edu.query.programhomeworkquery.HomeworkIsPublishAndEndTimeAndHomeworkIdQuery;
import com.zhulang.waveedu.edu.query.programhomeworkquery.StuDetailHomeworkInfoQuery;
import com.zhulang.waveedu.edu.query.programhomeworkquery.StuSimpleHomeworkInfoQuery;
import com.zhulang.waveedu.edu.query.programhomeworkquery.TchSimpleHomeworkInfoQuery;
import com.zhulang.waveedu.edu.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.ModifyProgramHomeworkVO;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.PublishProgramHomeworkVO;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.SaveProgramHomeworkVO;
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
import java.util.stream.Collectors;

/**
 * <p>
 * 编程作业表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-12
 */
@Service
@Slf4j
public class LessonClassProgramHomeworkServiceImpl extends ServiceImpl<LessonClassProgramHomeworkMapper, LessonClassProgramHomework> implements LessonClassProgramHomeworkService {
    @Resource
    private LessonClassService lessonClassService;
    @Resource
    private LessonClassProgramHomeworkMapper lessonClassProgramHomeworkMapper;
    @Resource
    private ProgramHomeworkStuConditionService programHomeworkStuConditionService;
    @Resource
    private MessageSdkSendErrorLogService messageSdkSendErrorLogService;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private LessonClassStuService lessonClassStuService;

    @Override
    public Result saveHomework(SaveProgramHomeworkVO saveProgramHomeworkVO) {
        Long userId = UserHolderUtils.getUserId();
        // 1.校验身份
        if (!lessonClassService.existsByUserIdAndClassId(userId, saveProgramHomeworkVO.getClassId())) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 2.设置属性
        LessonClassProgramHomework homework = new LessonClassProgramHomework();
        homework.setClassId(saveProgramHomeworkVO.getClassId());
        homework.setCreatorId(userId);
        homework.setTitle(saveProgramHomeworkVO.getTitle());
        // 3.保存
        lessonClassProgramHomeworkMapper.insert(homework);
        // 4.返回作业Id
        return Result.ok(homework.getId());
    }

    @Override
    public Result modifyInfo(ModifyProgramHomeworkVO modifyProgramHomeworkVO) {
        // 1.校验标题
        if (modifyProgramHomeworkVO.getTitle() != null && StrUtil.isBlank(modifyProgramHomeworkVO.getTitle())) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效标题");
        }
        // 2.修改信息
        lessonClassProgramHomeworkMapper.update(null, new LambdaUpdateWrapper<LessonClassProgramHomework>()
                .eq(LessonClassProgramHomework::getId, modifyProgramHomeworkVO.getHomeworkId())
                .eq(LessonClassProgramHomework::getCreatorId, UserHolderUtils.getUserId())
                .set(modifyProgramHomeworkVO.getTitle() != null, LessonClassProgramHomework::getTitle, modifyProgramHomeworkVO.getTitle())
                .set(modifyProgramHomeworkVO.getEndTime() != null, LessonClassProgramHomework::getEndTime, modifyProgramHomeworkVO.getEndTime()));
        // 3.返回
        return Result.ok();
    }

    @Override
    public Integer getIsPublishByHomeworkIdAndCreatorId(Integer homeworkId, Long creatorId) {
        return lessonClassProgramHomeworkMapper.selectIsPublishByHomeworkIdAndCreatorId(homeworkId, creatorId);
    }

    @Override
    public void updateNumById(Integer homeworkId) {
        lessonClassProgramHomeworkMapper.updateNumById(homeworkId);
    }

    @Override
//    @Transactional(rollbackFor = Exception.class)
    public Result removeHomework(Integer homeworkId) {
        // 1.校验权限
        if (!existsByHomeworkIdAndCreatorId(homeworkId, UserHolderUtils.getUserId())) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 2.删除作业
        lessonClassProgramHomeworkMapper.deleteById(homeworkId);
        // 3.返回
        return Result.ok();

//        programHomeworkProblemService.remove(new LambdaQueryWrapper<ProgramHomeworkProblem>()
//                .eq(ProgramHomeworkProblem::getHomeworkId,homeworkId));
//        programHomeworkProblemCaseService.remove(new LambdaQueryWrapper<ProgramHomeworkProblemCase>()
//                .eq(ProgramHomeworkProblemCase::getHomeworkId,homeworkId));

    }

    @Override
    public boolean existsByHomeworkIdAndCreatorId(Integer homeworkId, Long creatorId) {
        return lessonClassProgramHomeworkMapper.existsByHomeworkIdAndCreatorId(homeworkId, creatorId) != null;
    }

    @Override
    public Result tchGetHomeworkInfoList(Long classId, Integer status) {
        // 1.校验数据格式
        if (RegexUtils.isSnowIdInvalid(classId)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "班级id格式错误");
        }
        if (status != null && (status < 0 || status > 3)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "作业状态格式错误");
        }
        // 2.校验身份
        if (!lessonClassService.existsByUserIdAndClassId(UserHolderUtils.getUserId(), classId)) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 3.获取列表信息
        List<TchSimpleHomeworkInfoQuery> infoList;
        if (status == null) {
            infoList = lessonClassProgramHomeworkMapper.selectTchHomeworkInfoList(classId, status);
            infoList.forEach(info -> {
                if (info.getStatus() == 1 && LocalDateTime.now().isAfter(info.getEndTime())) {
                    info.setStatus(3);
                }
            });
        } else if (status == 3) {
            infoList = lessonClassProgramHomeworkMapper.selectTchHomeworkInfoList(classId, 1);
            LocalDateTime now = LocalDateTime.now();
            infoList = infoList.stream().filter(info -> now.isAfter(info.getEndTime())).collect(Collectors.toList());
            infoList.forEach(info -> {
                info.setStatus(3);
            });
        } else {
            infoList = lessonClassProgramHomeworkMapper.selectTchHomeworkInfoList(classId, status);
        }
        return Result.ok(infoList);
    }

    @Override
    public Result tchGetHomeworkDetailInfo(Integer homeworkId) {
        // 1.校验格式
        if (homeworkId < 1000) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "作业id格式错误");
        }
        // 2.校验身份
        if (!existsByHomeworkIdAndCreatorId(homeworkId, UserHolderUtils.getUserId())) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 3.获取信息
        LessonClassProgramHomework homework = lessonClassProgramHomeworkMapper.selectById(homeworkId);
        if (homework == null) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "作业不存在");
        }
        // 4.判断状态
        if (homework.getIsPublish() == 1) {
            if (homework.getEndTime().isBefore(LocalDateTime.now())) {
                homework.setIsPublish(3);
            }
        }
        // 5.返回
        return Result.ok(homework);
    }

    @Override
    public Result publish(PublishProgramHomeworkVO publishProgramHomeworkVO) {
        // 1.校验创建者，发布状况
        Map<String, Object> map = this.getMap(new LambdaQueryWrapper<LessonClassProgramHomework>()
                .eq(LessonClassProgramHomework::getId, publishProgramHomeworkVO.getHomeworkId())
                .select(LessonClassProgramHomework::getIsPublish, LessonClassProgramHomework::getCreatorId, LessonClassProgramHomework::getEndTime, LessonClassProgramHomework::getClassId));
        // 1.1 是否为创建者
        if (!map.get("creatorId").toString().equals(UserHolderUtils.getUserId().toString())) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 1.2 判断是否设置了截止时间
        LocalDateTime endTime = (LocalDateTime) map.get("endTime");
        if (endTime == null) {
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "请先设置作业截止时间");
        }
        // 如果现在的时间在截止时间之后或者开始时间在截止时间之后
        if (LocalDateTime.now().isAfter(endTime) || (publishProgramHomeworkVO.getStartTime() != null && publishProgramHomeworkVO.getStartTime().isAfter(endTime))) {
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "截止时间必须在开始时间之后，请重新设置截止时间");
        }
        // 1.2 如果是已经发布了，就不能发布
        if ((Integer) map.get("isPublish") == 1) {
            return Result.error(HttpStatus.HTTP_REPEAT_SUCCESS_OPERATE.getCode(), "作业已发布，请勿重复操作");
        }
        // 2.查询题目数量，如果为0则无法发布
        int count = lessonClassProgramHomeworkMapper.selectNumById(publishProgramHomeworkVO.getHomeworkId());
        if (count == 0) {
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "请先添加至少一道题目");
        }

        // 3.如果是定时发布就加入到延迟队列
        if (publishProgramHomeworkVO.getIsRegularTime() == 1) {
            // 3.1 判断是否设置定时时间
            if (publishProgramHomeworkVO.getStartTime() == null) {
                return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "未设置定时发布时间");
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
                        log.error("发布编程作业任务的消息没有到达交换机，原因是：{}", result.getReason());
                        if (count == 3) {
                            // 消息没有到达交换机，则需要重发，重发三次之后如果还是失败，那么这个消息需要存储到数据库中做兜底工作
                            MessageSdkSendErrorLog messageSdkSendErrorLog = new MessageSdkSendErrorLog();
                            // 设置错误信息
                            messageSdkSendErrorLog.setErrorMsg(result.getReason());
                            // /设置发送者
                            messageSdkSendErrorLog.setSender(UserHolderUtils.getUserId() + "");
                            // 设置消息内容
                            messageSdkSendErrorLog.setContent(publishProgramHomeworkVO.getHomeworkId() + "");
                            // 设置消息备注
                            messageSdkSendErrorLog.setRemark("发送到普通作业预发布交换机错误");
                            // 设置消息类型
                            messageSdkSendErrorLog.setType(MessageSdkSendErrorTypeConstants.COMMON_HOMEWORK_PUBLISH_SEND_ERROR);
                            // 保存错误信息
                            messageSdkSendErrorLogService.save(messageSdkSendErrorLog);
                            // 修改作业状态为0-未发布
                            lessonClassProgramHomeworkMapper.update(null, new LambdaUpdateWrapper<LessonClassProgramHomework>()
                                    .eq(LessonClassProgramHomework::getId, publishProgramHomeworkVO.getHomeworkId())
                                    .set(LessonClassProgramHomework::getIsPublish, 0));
                        } else {
                            // 重发
                            count++;
                            // 设置发送的内容
                            HashMap<String, Object> sendMap = new HashMap<>(2);
                            sendMap.put("programHomeworkId", publishProgramHomeworkVO.getHomeworkId());
                            sendMap.put("startTime", publishProgramHomeworkVO.getStartTime());
                            sendMap.put("classId", map.get("classId").toString());
                            // 设置发送消息的延迟时长（单位 ms）
                            int delayedTime = (int) Duration.between(LocalDateTime.now(), publishProgramHomeworkVO.getStartTime()).toMillis();
                            // 异步发送到消息队列
                            // todo 有bug，在交换机不存在情况下无法重发三次
//                            correlationData.setId(UUID.randomUUID().toString());
                            rabbitTemplate.convertAndSend(RabbitConstants.PROGRAM_HOMEWORK_PUBLISH_DELAYED_QUEUE_NAME,
                                    RabbitConstants.PROGRAM_HOMEWORK_PUBLISH_ROUTING_KEY,
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
                    log.error("发送到编程作业预发布交换机失败原因：{}" + ex.getMessage());
                }
            });

            // 3.4 设置发送的内容
            HashMap<String, Object> sendMap = new HashMap<>(2);
            sendMap.put("programHomeworkId", publishProgramHomeworkVO.getHomeworkId());
            sendMap.put("startTime", publishProgramHomeworkVO.getStartTime());
            sendMap.put("classId", map.get("classId").toString());

            // 3.5 设置发送消息的延迟时长（单位 ms）
            long delayedTime = Duration.between(LocalDateTime.now(), publishProgramHomeworkVO.getStartTime()).toMillis();
            if (delayedTime > 2073600000) {
                return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "定时不能距离当前超过24天");
            }

            // 3.6 异步发送到延迟队列
            rabbitTemplate.convertAndSend(RabbitConstants.PROGRAM_HOMEWORK_PUBLISH_DELAYED_EXCHANGE_NAME,
                    RabbitConstants.PROGRAM_HOMEWORK_PUBLISH_ROUTING_KEY,
                    sendMap,
                    msg -> {
                        msg.getMessageProperties().setDelay((int) delayedTime);
                        return msg;
                    },
                    correlationData
            );
            // 3.7 将时间保存到数据库，并修改状态为 定时发布中
            this.update(new LambdaUpdateWrapper<LessonClassProgramHomework>()
                    .eq(LessonClassProgramHomework::getId, publishProgramHomeworkVO.getHomeworkId())
                    .set(LessonClassProgramHomework::getStartTime, publishProgramHomeworkVO.getStartTime())
                    .set(LessonClassProgramHomework::getIsPublish, 2));
            return Result.ok();
        }

        // 4.如果不是定时发布，修改发布状态为已发布，设置发布时间为当前时间
        this.update(new LambdaUpdateWrapper<LessonClassProgramHomework>()
                .eq(LessonClassProgramHomework::getId, publishProgramHomeworkVO.getHomeworkId())
                .set(LessonClassProgramHomework::getStartTime, LocalDateTime.now())
                .set(LessonClassProgramHomework::getIsPublish, 1)
        );
        // 5.添加学生信息到 condition 表中
        programHomeworkStuConditionService.saveStuInfoList(publishProgramHomeworkVO.getHomeworkId(), Long.parseLong(map.get("classId").toString()));

        // 6.返回
        return Result.ok();

    }

    @Override
    public HomeworkIsPublishAndEndTimeAndHomeworkIdQuery getIsPublishAndEndTimeAndHomeworkIdByProblemId(Integer problemId) {
        return lessonClassProgramHomeworkMapper.selectIsPublishAndEndTimeAndHomeworkIdAndHomeworkIdByProblemId(problemId);
    }

    @Override
    public Result stuGetHomeworkSimpleListInfo(Long classId) {
        Long userId = UserHolderUtils.getUserId();
        // 1.校验身份
        if (!lessonClassStuService.existsByClassIdAndUserId(classId, userId)) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), "未找到您在该班级中的学生信息");
        }
        // 2.获取信息
        List<StuSimpleHomeworkInfoQuery> infoList = lessonClassProgramHomeworkMapper.selectStuHomeworkSimpleInfoList(userId, classId);
        // 3.设置作业状态
        for (StuSimpleHomeworkInfoQuery info : infoList) {
            // 如果问题数量等于已完成数量，说明已完成作业
            if (Objects.equals(info.getCompleteNum(), info.getProblemNum())) {
                info.setStatus(0);
            } else if (LocalDateTime.now().isAfter(info.getEndTime())) {
                // 如果当前时间在截止时间之后，说明作业已截止
                info.setStatus(1);
            } else {
                // 否则就是 进行中
                info.setStatus(2);
            }
        }
        // 4.返回
        return Result.ok(infoList);

    }

    @Override
    public Result stuGetHomeworkDetailInfo(Integer homeworkId) {
        // 1.校验格式（身份不校验了）
        if (homeworkId < 1000) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "作业Id格式错误");
        }
        // 2.查询作业信息
        StuDetailHomeworkInfoQuery infoQuery = lessonClassProgramHomeworkMapper.selectStuHomeworkDetailInfo(homeworkId);
        if (infoQuery == null) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "未找到相关作业信息");
        }
        if (Objects.equals(infoQuery.getCompleteNum(), infoQuery.getProblemNum())) {
            // 完成数量等于问题数量
            infoQuery.setHomeworkStatus(0);
        } else if (LocalDateTime.now().isAfter(infoQuery.getEndTime())) {
            // 如果已经截止
            infoQuery.setHomeworkStatus(1);
        } else {
            // 说明还在进行中，并未完成
            infoQuery.setHomeworkStatus(2);
        }
        // 3.查询问题与自己的回答信息
        List<StuDetailHomeworkInfoQuery.InnerProblemInfo> innerProblemInfos = lessonClassProgramHomeworkMapper.selectAnswerProblemSimpleInfolist(homeworkId, UserHolderUtils.getUserId());
        if (innerProblemInfos == null || innerProblemInfos.isEmpty()) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "未找到相关问题信息");
        }
        for (StuDetailHomeworkInfoQuery.InnerProblemInfo innerProblemInfo : innerProblemInfos) {
            if (innerProblemInfo.getCompleteTime() == null) {
                innerProblemInfo.setStatus(0);
            } else {
                innerProblemInfo.setStatus(1);
            }
        }
        // 4.装载属性
        infoQuery.setProblemInfoList(innerProblemInfos);
        // 5.返回
        return Result.ok(infoQuery);
    }

    @Override
    public Integer getNumById(Integer id) {
        return lessonClassProgramHomeworkMapper.selectNumById(id);
    }
}
