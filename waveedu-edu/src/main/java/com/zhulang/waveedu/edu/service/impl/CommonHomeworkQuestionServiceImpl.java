package com.zhulang.waveedu.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.common.util.WaveStrUtils;
import com.zhulang.waveedu.edu.po.CommonHomeworkQuestion;
import com.zhulang.waveedu.edu.dao.CommonHomeworkQuestionMapper;
import com.zhulang.waveedu.edu.po.LessonClassCommonHomework;
import com.zhulang.waveedu.edu.query.homeworkquery.HomeworkIdAndTypeQuery;
import com.zhulang.waveedu.edu.query.homeworkquery.StuHomeworkStatusQuery;
import com.zhulang.waveedu.edu.service.CommonHomeworkQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.service.LessonClassCommonHomeworkService;
import com.zhulang.waveedu.edu.vo.homeworkvo.ModifyCommonHomeworkQuestionVO;
import com.zhulang.waveedu.edu.vo.homeworkvo.SaveCommonHomeworkQuestionVO;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 普通作业表的题目表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-02-28
 */
@Service
public class CommonHomeworkQuestionServiceImpl extends ServiceImpl<CommonHomeworkQuestionMapper, CommonHomeworkQuestion> implements CommonHomeworkQuestionService {
    @Resource
    private CommonHomeworkQuestionMapper commonHomeworkQuestionMapper;
    @Resource
    private LessonClassCommonHomeworkService lessonClassCommonHomeworkService;

    @Override
    public Result saveQuestion(SaveCommonHomeworkQuestionVO saveCommonHomeworkQuestionVO) {
        // 1.查询作业的信息：type + is_publish + creator_id
        Map<String, Object> map = lessonClassCommonHomeworkService.getMap(new LambdaQueryWrapper<LessonClassCommonHomework>()
                .eq(LessonClassCommonHomework::getId, saveCommonHomeworkQuestionVO.getCommonHomeworkId())
                .select(LessonClassCommonHomework::getIsPublish, LessonClassCommonHomework::getType, LessonClassCommonHomework::getCreatorId));
        // 1.1 为空说明不存在该作业信息
        if (map == null || map.isEmpty()) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "作业信息不存在");
        }
        // 1.2 如果不是创建者说明权限不足
        if (!map.get("creatorId").toString().equals(UserHolderUtils.getUserId().toString())) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 1.3 如果已经发布则不能再修改
        if ((Integer) map.get("isPublish") != 0) {
            return Result.error(HttpStatus.HTTP_INFO_REFUSE.getCode(), "作业已发布，无法添加");
        }
        // 1.4 如果作业类型为1并且题目类型不为4，或者，作业类型为0并且题目类型为4则操作失败
        // 获取作业类型
        int homeworkType = (Integer) map.get("type");
        // 获取题目类型
        int questionType = saveCommonHomeworkQuestionVO.getType();
        if ((homeworkType == 1 && questionType == 4) || (homeworkType == 0 && questionType != 4)) {
            return Result.error(HttpStatus.HTTP_ILLEGAL_OPERATION.getCode(), HttpStatus.HTTP_ILLEGAL_OPERATION.getValue());
        }

        // 2.属性转换
        CommonHomeworkQuestion question = BeanUtil.copyProperties(saveCommonHomeworkQuestionVO, CommonHomeworkQuestion.class);
        // 3.根据问题类型校验答案的格式
        try {
            verifyAnswerFormat(question.getType(), question.getProblemDesc(), question.getAnswer());
        } catch (Exception e) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "题目格式错误");
        }
        // 4.将无效的答案和解析设为空串
        if (!StringUtils.hasText(question.getAnswer())) {
            question.setAnswer("");
        }
        if (!StringUtils.hasText(question.getAnalysis())) {
            question.setAnalysis("");
        }
        // 5.保存问题并修改总分数
        ((CommonHomeworkQuestionService) AopContext.currentProxy()).saveQuestionAndModifyTotalScore(question);
        // 6.返回题目Id
        return Result.ok(question.getId());
    }

    /**
     * 保存问题并修改总分数
     *
     * @param question 问题信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveQuestionAndModifyTotalScore(CommonHomeworkQuestion question) {
        // 问题保存
        commonHomeworkQuestionMapper.insert(question);
        // 修改作业表的总分数
        lessonClassCommonHomeworkService.modifyTotalScore(question.getCommonHomeworkId());
    }

    @Override
    public Result delQuestion(Integer questionId) {
        // 1.判断 questionId 格式
        if (questionId < 1) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "题目id格式错误");
        }
        // 2.查看问题是否发布
        Map<String, Object> map = commonHomeworkQuestionMapper.selectHomeworkIsPublishAndCreatorIdAndCommonHomeworkIdById(questionId);
        if (map == null || map.isEmpty()) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "未查询到题目或作业信息");
        }

        if ((Integer) map.get("isPublish") != 0) {
            return Result.error(HttpStatus.HTTP_ILLEGAL_OPERATION.getCode(), "作业已发布，无法修改题目");
        }

        // 3.查看是否为创建者
        if (!map.get("creatorId").toString().equals(UserHolderUtils.getUserId().toString())) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 4.删除题目
        ((CommonHomeworkQuestionService) AopContext.currentProxy()).removeQuestionAndModifyTotalScore(questionId, Integer.parseInt(map.get("homeworkId").toString()));

        // 5.返回
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeQuestionAndModifyTotalScore(Integer questionId, Integer homeworkId) {
        // 问题删除
        commonHomeworkQuestionMapper.deleteById(questionId);
        // 修改作业表的总分数
        lessonClassCommonHomeworkService.modifyTotalScore(homeworkId);
    }

    @Override
    public Result modifyQuestion(ModifyCommonHomeworkQuestionVO modifyCommonHomeworkQuestionVO) {
        // 1.查询作业的信息：type + is_publish + creator_id
        Map<String, Object> map = commonHomeworkQuestionMapper.selectHomeworkIsPublishAndCreatorIdAndTypeAndHomeworkIdById(modifyCommonHomeworkQuestionVO.getId());

        // 1.1 为空说明不存在该作业信息
        if (map == null || map.isEmpty()) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "作业信息不存在");
        }
        // 1.2 如果不是创建者说明权限不足
        if (!map.get("creatorId").toString().equals(UserHolderUtils.getUserId().toString())) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 1.3 如果已经发布则不能再修改
        if ((Integer) map.get("isPublish") != 0) {
            return Result.error(HttpStatus.HTTP_INFO_REFUSE.getCode(), "作业已发布，无法添加");
        }
        // 1.4 如果作业类型为1并且题目类型不为4，或者，作业类型为0并且题目类型为4则操作失败
        // 获取作业类型
        int homeworkType = (Integer) map.get("type");
        // 获取题目类型
        int questionType = modifyCommonHomeworkQuestionVO.getType();
        if ((homeworkType == 1 && questionType == 4) || (homeworkType == 0 && questionType != 4)) {
            return Result.error(HttpStatus.HTTP_ILLEGAL_OPERATION.getCode(), HttpStatus.HTTP_ILLEGAL_OPERATION.getValue());
        }

        // 2.属性转换
        CommonHomeworkQuestion question = BeanUtil.copyProperties(modifyCommonHomeworkQuestionVO, CommonHomeworkQuestion.class);
        // 3.根据问题类型校验答案的格式
        try {
            verifyAnswerFormat(question.getType(), question.getProblemDesc(), question.getAnswer());
        } catch (Exception e) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "题目格式错误");
        }
        // 4.将无效的答案和解析设为空串
        if (!StringUtils.hasText(question.getAnswer())) {
            question.setAnswer("");
        }
        if (!StringUtils.hasText(question.getAnalysis())) {
            question.setAnalysis("");
        }
        // 5.修改题目
        ((CommonHomeworkQuestionService) AopContext.currentProxy()).modifyQuestionAndModifyTotalScore(question, Integer.parseInt(map.get("homeworkId").toString()));
        // 6.返回
        return Result.ok();
    }

    /**
     * 修改问题并修改总分数
     *
     * @param question 问题信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyQuestionAndModifyTotalScore(CommonHomeworkQuestion question, Integer homeworkId) {
        // 问题保存
        commonHomeworkQuestionMapper.updateById(question);
        // 修改作业表的总分数
        lessonClassCommonHomeworkService.modifyTotalScore(homeworkId);

    }

    @Override
    public Result getStuHomeworkQuestionListInfo(Integer homeworkId) {
        // 1.校验格式
        if (homeworkId < 1) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "作业id格式错误");
        }
        // 2.校验是否为班级学生
        Long userId = UserHolderUtils.getUserId();
        if (!lessonClassCommonHomeworkService.isClassStuByIdAndStuId(homeworkId, userId)) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 3.获取作业状态与作业策略
        StuHomeworkStatusQuery statusInfo = lessonClassCommonHomeworkService.getStuHomeworkStatus(homeworkId, userId);
        if (statusInfo == null) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 4.根据状态与策略查询不同的信息
        // 规定：0-进行中（未提交且未过截止日期），1-已截止（未提交且过了截止日期），2-批阅中（已提交且处于批阅中）
        //      3-已批阅（已提交并且已批阅）
        HashMap<String, Object> resultMap = new HashMap<>(2);
        if (statusInfo.getStatus() == null) {
            // 4.1 说明没有提交
            if (LocalDateTime.now().isBefore(statusInfo.getEndTime())) {
                // 如果现在还没过截止时间，则状态为 进行中
                // 如果当前的时间在截止时间之前，则只查询题目信息
                resultMap.put("status", 0);
                resultMap.put("questions", commonHomeworkQuestionMapper.selectHomeworkQuestionSimpleInfoList(homeworkId));
            } else {
                // 说明是未提交且已经截止
                resultMap.put("status", 1);
                // 根据是否开启截止后提交判断获取的信息
                Integer isEndAfterExplain = statusInfo.getIsEndAfterExplain();
//                resultMap.put("isEndAfterExplain", isEndAfterExplain);
                if (isEndAfterExplain == 0) {
                    // 未开启
                    resultMap.put("questions", commonHomeworkQuestionMapper.selectHomeworkQuestionSimpleInfoList(homeworkId));
                } else {
                    // 开启 --> 需要去获取问题的答案与解析
                    resultMap.put("questions", commonHomeworkQuestionMapper.selectHomeworkQuestionDetailInfoList(homeworkId));
                }
            }
        } else {
            // 3.2 说明已经提交
            if (statusInfo.getStatus() == 0) {
                // 如果是批阅中
                resultMap.put("status", 2);
                if (statusInfo.getIsCompleteAfterExplain() == 1 ||
                        (LocalDateTime.now().isAfter(statusInfo.getEndTime())) && statusInfo.getIsEndAfterExplain() == 1) {
                    // 如果允许完成作业后查看或者 时间已经截止了并且允许时间截止后查看 作业解析
                    // 获取： 问题id,问题类型，问题描述，问题参考答案，问题解析，问题满分，学生答案
                    resultMap.put("questions",commonHomeworkQuestionMapper.selectQuestionDetailAndSelfAnswerWithoutScore(homeworkId,userId));

                }else{
                    // 获取问题情况以及自己的答案
                    // 获取： 问题id,问题类型，问题描述，问题满分，学生答案
                    resultMap.put("questions",commonHomeworkQuestionMapper.selectQuestionSimpleAndSelfAnswerWithoutScore(homeworkId,userId));
                }
            } else {
                // 如果已批阅
                resultMap.put("status", 3);
                if (statusInfo.getIsCompleteAfterExplain() == 1 ||
                        (LocalDateTime.now().isAfter(statusInfo.getEndTime())) && statusInfo.getIsEndAfterExplain() == 1) {
                    // 如果允许完成作业后查看或者 时间已经截止了并且允许时间截止后查看
                    // 获取： 问题id,问题类型，问题描述，问题参考答案，问题解析，问题满分，学生答案，学生该题分数
                    resultMap.put("questions",commonHomeworkQuestionMapper.selectQuestionDetailAndSelfAnswerWithScore(homeworkId,userId));

                }else{
                    // 获取问题情况以及自己的答案
                    // 获取：问题id,问题类型，问题描述，问题满分，学生答案，学生该题分数
                    resultMap.put("questions",commonHomeworkQuestionMapper.selectQuestionSimpleAndSelfAnswerWithScore(homeworkId,userId));
                }
            }
        }
        return Result.ok(resultMap);
    }

    @Override
    public HomeworkIdAndTypeQuery getHomeworkIdAndTypeById(Integer questionId) {
        return commonHomeworkQuestionMapper.selectHomeworkIdAndTypeById(questionId);
    }

    @Override
    public Integer getTmpTotalScoreByCommonHomeworkId(Integer homeworkId) {
        return commonHomeworkQuestionMapper.selectTotalScoreByCommonHomeworkId(homeworkId);
    }

    @Override
    public Result getTmpTotalScore(Integer homeworkId) {
        if (homeworkId < 1) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "作业id格式错误");
        }
        return Result.ok(commonHomeworkQuestionMapper.selectTotalScoreByCommonHomeworkId(homeworkId));
    }

    @Override
    public Result getTchHomeworkQuestionListInfo(Integer homeworkId, Integer pattern) {
        // 校验格式
        if (homeworkId < 1) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "作业Id格式错误");
        }
        if (pattern != 0 && pattern != 1) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "模式格式错误");
        }

        // 1.校验是否为作业班级创建者
        if (!lessonClassCommonHomeworkService.existsByIdAndUserId(homeworkId, UserHolderUtils.getUserId())) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 2.根据模式查询不同的信息并返回
        if (pattern == 0) {
            return Result.ok(commonHomeworkQuestionMapper.selectHomeworkQuestionSimpleInfoList(homeworkId));
        } else {
            return Result.ok(commonHomeworkQuestionMapper.selectHomeworkQuestionDetailInfoList(homeworkId));
        }
    }

    /**
     * 根据问题类型校验答案的格式
     *
     * @param questionType 问题类型
     * @param questionDesc 问题描述
     * @param answer       答案
     */
    public void verifyAnswerFormat(Integer questionType, String questionDesc, String answer) {
        switch (questionType) {
            case 0:
                // 单选：第一个元素时题目，后面的元素是选项
                // answer只允许数字，满足范围[1,集合大小-1]
                ArrayList singleList = JSON.parseObject(questionDesc, ArrayList.class);
                int choice = Integer.parseInt(answer);
                // 满足 总共的选项 大于等于 choice
                if (choice < 1 || choice > singleList.size() - 1) {
                    throw new RuntimeException();
                }
                break;
            case 1:
                // 多选：第一个元素时题目，后面的元素是选项
                // answer是逗号拼接的答案
                ArrayList multipartList = JSON.parseObject(questionDesc, ArrayList.class);
                String[] answers = WaveStrUtils.strSplitToArr(answer, ",");
                int choiceNum = multipartList.size() - 1;
                for (String select : answers) {
                    int intSelect = Integer.parseInt(select);
                    if (intSelect < 1 || intSelect > choiceNum) {
                        throw new RuntimeException();
                    }
                }
                break;
            case 2:
                // 填空：answer解析后的数量必须大于等于1
                if (JSON.parseObject(answer, ArrayList.class).isEmpty()) {
                    throw new RuntimeException();
                }
                break;
            case 3:
                // 判断：answer只允许是0或1，0表示正确，1表示错误
                int judge = Integer.parseInt(answer);
                if (judge != 0 && judge != 1) {
                    throw new RuntimeException();
                }
            case 4:
                // 探究题
                break;
            default:
                throw new RuntimeException();
        }
    }
}
