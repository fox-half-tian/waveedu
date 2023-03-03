package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.CommonHomeworkStuAnswer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.edu.vo.homeworkvo.HomeworkAnswerVO;

import java.util.List;

/**
 * <p>
 * 普通作业表的学生回答表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-04
 */
public interface CommonHomeworkStuAnswerService extends IService<CommonHomeworkStuAnswer> {

    /**
     * 验证题目答案，学生可调用
     *
     * @param homeworkAnswerVO 题目id + 学生答案
     * @return 验证状况
     */
    Result verifyAnswers(List<HomeworkAnswerVO> homeworkAnswerVO);
}
