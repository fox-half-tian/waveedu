package com.zhulang.waveedu.judge.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.judge.dto.ProblemLimitInfoDTO;
import com.zhulang.waveedu.judge.po.ProgramHomeworkProblem;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 编程作业表的题目表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-12
 */
public interface ProgramHomeworkProblemMapper extends BaseMapper<ProgramHomeworkProblem> {

    /**
     * 获取问题的限制信息：内存限制，时间限制，栈限制
     *
     * @param id 问题id
     * @return 信息
     */
    ProblemLimitInfoDTO selectProblemLimitInfo(@Param("id") Integer id);

    /**
     * 查询问题所有案例的信息
     *
     * @param problemId 问题id
     * @return 信息列表
     */
    List<HashMap<String, Object>> selectProblemCases(@Param("problemId") Integer problemId);
}
