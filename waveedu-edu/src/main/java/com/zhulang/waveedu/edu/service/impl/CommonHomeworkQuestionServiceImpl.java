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
import com.zhulang.waveedu.edu.service.CommonHomeworkQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.service.LessonClassCommonHomeworkService;
import com.zhulang.waveedu.edu.vo.homework.SaveCommonHomeworkQuestionVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
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
        if (map.get("creator_id").equals(UserHolderUtils.getUserId())) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 1.3 如果已经发布则不能再修改
        if ((Integer) map.get("is_publish") == 1) {
            return Result.error(HttpStatus.HTTP_INFO_REFUSE.getCode(), "作业已发布，无法添加");
        }
        // 1.4 如果作业类型为1并且题目类型不为4，或者，作业类型为0并且题目类型为4则操作失败
        // 获取作业类型
        int homeworkType = (Integer) map.get("type");
        // 获取题目类型
        int questionType = saveCommonHomeworkQuestionVO.getType();
        if ((homeworkType == 1 && questionType != 4) || (homeworkType == 0 && questionType == 4)) {
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
        // 5.保存
        commonHomeworkQuestionMapper.insert(question);
        // 6.返回题目Id
        return Result.ok(question.getId());
    }

    @Override
    public Result delQuestion(Integer questionId) {
        // 1.判断 questionId 格式
        if (questionId < 1) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "题目id格式错误");
        }
        // 2.查看问题是否发布
        Map<String, Object> map = commonHomeworkQuestionMapper.selectHomeworkIsPublishAndCreatorIdById(questionId);
        if (map == null || map.isEmpty()) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "未查询到题目或作业信息");
        }

        if ((Integer) map.get("is_publish") == 1) {
            return Result.error(HttpStatus.HTTP_ILLEGAL_OPERATION.getCode(), "作业已发布，无法修改题目");
        }

        // 3.查看是否为创建者
        if (map.get("creator_id").equals(UserHolderUtils.getUserId())){
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 4.删除题目
        commonHomeworkQuestionMapper.deleteById(questionId);
        // 5.返回
        return Result.ok();
    }

    /**
     * 根据问题类型校验答案的格式
     *
     * @param questionType 问题类型
     * @param questionDesc 问题描述
     * @param answer       答案
     * @throws Exception 抛出异常说明验证失败
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
