package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.CommonHomeworkStuAnswer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.edu.vo.homeworkvo.HomeworkAnswerVO;
import com.zhulang.waveedu.edu.vo.homeworkvo.MarkHomeworkVO;

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


    /**
     * 获取学生的作业情况
     *
     * @param homeworkId 作业id
     * @param stuId 学生id
     * @return 情况
     */
    Result getStuHomeworkAnswers(Integer homeworkId, Long stuId);

    /**
     * 创建者给学生的探究类作业批阅分数
     *
     * @param stuId 学生id
     * @param comment 教师评价
     * @param innerMarkList 问题id + 分数
     * @return 是否成功
     */
    Result markHomework(Long stuId, String comment, List<MarkHomeworkVO.InnerMark> innerMarkList);
}
