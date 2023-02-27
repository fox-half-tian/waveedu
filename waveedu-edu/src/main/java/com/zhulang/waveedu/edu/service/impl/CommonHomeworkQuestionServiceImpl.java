package com.zhulang.waveedu.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.CommonHomeworkQuestion;
import com.zhulang.waveedu.edu.dao.CommonHomeworkQuestionMapper;
import com.zhulang.waveedu.edu.po.LessonClassCommonHomework;
import com.zhulang.waveedu.edu.service.CommonHomeworkQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.service.LessonClassCommonHomeworkService;
import com.zhulang.waveedu.edu.vo.homework.SaveCommonHomeworkQuestionVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        // 1.查询作业的信息：type + is_publish
        Map<String, Object> map = lessonClassCommonHomeworkService.getMap(new LambdaQueryWrapper<LessonClassCommonHomework>()
                .eq(LessonClassCommonHomework::getId, saveCommonHomeworkQuestionVO.getCommonHomeworkId())
                .select(LessonClassCommonHomework::getIsPublish, LessonClassCommonHomework::getType));
        // 1.1 为空说明不存在该作业信息
        if (map == null || map.isEmpty()) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "作业信息不存在");
        }
        // 1.2 如果已经发布则不能再修改
        if ((Integer) map.get("isPublish") == 1) {
            return Result.error(HttpStatus.HTTP_INFO_REFUSE.getCode(), "作业已发布，无法添加");
        }
        // 1.3 如果作业类型为1并且题目类型不为4，或者，作业类型为0并且题目类型为4则操作失败
        // 获取作业类型
        int homeworkType = (Integer) map.get("type");
        // 获取题目类型
        int questionType = saveCommonHomeworkQuestionVO.getType();
        if ((homeworkType == 1 && questionType != 4) || (homeworkType == 0 && questionType == 4)) {
            return Result.error(HttpStatus.HTTP_ILLEGAL_OPERATION.getCode(),HttpStatus.HTTP_ILLEGAL_OPERATION.getValue());
        }

        // 2.属性转换
        CommonHomeworkQuestion question = BeanUtil.copyProperties(saveCommonHomeworkQuestionVO, CommonHomeworkQuestion.class);
        // 2.根据问题类型校验答案的格式
        return null;
    }

    /**
     * 根据问题类型校验答案的格式
     *
     * @param questionType 问题类型
     * @param questionDesc  问题描述
     * @param answer 答案
     * @throws Exception 抛出异常说明验证失败
     */
    public void verifyAnswerFormat(Integer questionType,String questionDesc,String answer) throws Exception{
        switch (questionType){
            case 0:
                // 单选
//                JSON.parseObject()

        }
    }
}
