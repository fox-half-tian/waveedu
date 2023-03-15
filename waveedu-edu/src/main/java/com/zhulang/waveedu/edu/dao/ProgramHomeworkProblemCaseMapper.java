package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.ProgramHomeworkProblemCase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.edu.query.programhomeworkquery.ProblemCaseInfoQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 编程作业问题测试实例表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-13
 */
public interface ProgramHomeworkProblemCaseMapper extends BaseMapper<ProgramHomeworkProblemCase> {

    /**
     * 根据 案例id 和 创建者id 查询对应作业的发布状况
     *
     * @param caseId 案例id
     * @param creatorId 创建者id
     * @return 0-未发布，1-已发布，2-发布中
     */
    Integer selectIsPublishByProblemIdAndCreatorId(@Param("caseId") Integer caseId, @Param("creatorId") Long creatorId);

    /**
     * 获取问题的案例信息
     *
     * @param problemId 问题id
     * @return 信息列表
     */
    List<ProblemCaseInfoQuery> selectProblemCaseList(@Param("problemId") Integer problemId);
}
