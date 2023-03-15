package com.zhulang.waveedu.edu.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.constant.RedisConstants;
import com.zhulang.waveedu.common.entity.JudgeResult;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.RedisLockUtils;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.dao.ProgramHomeworkStuJudgeMapper;
import com.zhulang.waveedu.edu.po.ProgramHomeworkStuAnswer;
import com.zhulang.waveedu.edu.po.ProgramHomeworkStuJudge;
import com.zhulang.waveedu.edu.query.programhomeworkquery.HomeworkIsPublishAndEndTimeQuery;
import com.zhulang.waveedu.edu.service.LessonClassProgramHomeworkService;
import com.zhulang.waveedu.edu.service.LessonClassService;
import com.zhulang.waveedu.edu.service.ProgramHomeworkStuAnswerService;
import com.zhulang.waveedu.edu.service.ProgramHomeworkStuJudgeService;
import com.zhulang.waveedu.edu.utils.LanguageSupportUtils;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.SubmitCodeVO;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

import static com.zhulang.waveedu.common.constant.CommonConstants.REQUEST_HEADER_TOKEN;

/**
 * <p>
 * 编程作业的学生判题表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-14
 */
@Service
public class ProgramHomeworkStuJudgeServiceImpl extends ServiceImpl<ProgramHomeworkStuJudgeMapper, ProgramHomeworkStuJudge> implements ProgramHomeworkStuJudgeService {
    @Resource
    private ProgramHomeworkStuJudgeMapper programHomeworkStuJudgeMapper;
    @Resource
    private RedisLockUtils redisLockUtils;
    @Resource
    private RestTemplate restTemplate;
    @Value("${judge.address}")
    private String address;
    @Resource
    private ProgramHomeworkStuAnswerService programHomeworkStuAnswerService;
    @Resource
    private LessonClassProgramHomeworkService lessonClassProgramHomeworkService;

    @Override
    public Result submit(SubmitCodeVO submitCodeVO, HttpServletRequest request) {
        // 1.判断语言是否支持
        if (!LanguageSupportUtils.isSupport(submitCodeVO.getLanguage())) {
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "暂不支持该语言判题");
        }
        // 判断作业状态与截止时间
        HomeworkIsPublishAndEndTimeQuery queryInfo = lessonClassProgramHomeworkService.getIsPublishAndEndTimeByProblemId(submitCodeVO.getProblemId());
        if (queryInfo == null) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "问题或作业信息不存在");
        }
        if (queryInfo.getIsPublish() != 1) {
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "作业尚未发布，提交无效");
        }

        // 2.redis锁控制
        Long userId = UserHolderUtils.getUserId();
        boolean lock = redisLockUtils.tryLock(RedisConstants.LOCK_JUDGE_KEY + userId, RedisConstants.LOCK_JUDGE_TTL);
        if (!lock) {
            return Result.error(HttpStatus.HTTP_TRY_AGAIN_LATER.getCode(), "您已有代码在判题中，请勿频繁操作");
        }
        try {
            // 3.判题机判题
            HttpHeaders headers = new HttpHeaders();
            // todo 配置网关后这里将可能不再需要处理
            headers.add(REQUEST_HEADER_TOKEN, request.getHeader(REQUEST_HEADER_TOKEN));
            HttpEntity<JSONObject> entity = new HttpEntity<>(JSONObject.parseObject(JSON.toJSONString(submitCodeVO)), headers);
            JudgeResult judgeResult = restTemplate
                    .exchange(address, HttpMethod.POST, entity, JudgeResult.class)
                    .getBody();
            if (judgeResult == null) {
                return Result.error();
            }
            // 4.判题完成，更新数据库记录
            ((ProgramHomeworkStuJudgeService) AopContext.currentProxy()).updateRecord(judgeResult,
                    userId,
                    submitCodeVO.getCode(),
                    submitCodeVO.getProblemId(),
                    submitCodeVO.getLanguage(),
                    LocalDateTime.now().isAfter(queryInfo.getEndTime())
            );
            // 5.返回判题结果
            return Result.ok(judgeResult);
        } finally {
            // 释放锁
            redisLockUtils.unlock(RedisConstants.LOCK_JUDGE_KEY + userId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRecord(JudgeResult judgeResult,
                             Long userId,
                             String languageCode,
                             Integer problemId,
                             String language,
                             boolean isEnd
    ) {
        // 如果状态码是0并且没有到截止时间，说明成功，则需要需要修改答案表
        if (judgeResult.getCode() == 0 && !isEnd) {
            // 查看答案表中是否存在，如果存在，则替换，不存在，则增加
            Integer answerId = programHomeworkStuAnswerService.getIdByUserIdAndProblemId(userId, problemId);
            if (answerId != null) {
                // 修改 运行时间，运行内存，语言，代码
                ProgramHomeworkStuAnswer programHomeworkStuAnswer = new ProgramHomeworkStuAnswer();
                programHomeworkStuAnswer.setTime(judgeResult.getTime());
                programHomeworkStuAnswer.setMemory(judgeResult.getMemory());
                programHomeworkStuAnswer.setLanguage(language);
                programHomeworkStuAnswer.setCode(languageCode);
                programHomeworkStuAnswer.setId(answerId);
                // 修改数据库信息
                programHomeworkStuAnswerService.updateById(programHomeworkStuAnswer);
            } else {
                // 新增
                // 信息配置
                ProgramHomeworkStuAnswer programHomeworkStuAnswer = new ProgramHomeworkStuAnswer();
                programHomeworkStuAnswer.setTime(judgeResult.getTime());
                programHomeworkStuAnswer.setMemory(judgeResult.getMemory());
                programHomeworkStuAnswer.setLanguage(language);
                programHomeworkStuAnswer.setCode(languageCode);
                programHomeworkStuAnswer.setProblemId(problemId);
                programHomeworkStuAnswer.setStuId(userId);
                // 插入记录
                programHomeworkStuAnswerService.save(programHomeworkStuAnswer);
            }
        }
        // 将此记录加入到判题记录中
        ProgramHomeworkStuJudge programHomeworkStuJudge = new ProgramHomeworkStuJudge();
        programHomeworkStuJudge.setStatus(judgeResult.getCode());
        programHomeworkStuJudge.setErrorMessage(judgeResult.getErrMsg());
        programHomeworkStuJudge.setTime(judgeResult.getTime());
        programHomeworkStuJudge.setMemory(judgeResult.getMemory());
        programHomeworkStuJudge.setLanguage(language);
        programHomeworkStuJudge.setCode(languageCode);
        programHomeworkStuJudge.setProblemId(problemId);
        programHomeworkStuJudge.setStuId(userId);
        programHomeworkStuJudgeMapper.insert(programHomeworkStuJudge);
    }
}
