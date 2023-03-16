package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.ProgramHomeworkStuAnswer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 编程作业的学生正确回答表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-15
 */
public interface ProgramHomeworkStuAnswerMapper extends BaseMapper<ProgramHomeworkStuAnswer> {

    /**
     * 根据问题id和用户id查询是否存在正确答案
     *
     * @param stuId 用户id
     * @param problemId 问题id
     * @return 正确答案id
     */
    Integer selectIdByUserIdAndProblemId(@Param("stuId") Long stuId, @Param("problemId") Integer problemId);

    /**
     * 查询是否做了这个题
     *
     * @param stuId 学生id
     * @param problemId 问题id
     * @return null-没做，不为空-做了
     */
    Integer existsByStuIdAndProblemId(@Param("stuId") Long stuId, @Param("problemId") Integer problemId);
}
