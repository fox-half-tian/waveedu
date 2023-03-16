package com.zhulang.waveedu.edu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.edu.po.ProblemBankCase;
import com.zhulang.waveedu.edu.query.programhomeworkquery.ProblemCaseInfoQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 编程问题题库测试实例表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-13
 */
public interface ProblemBankCaseMapper extends BaseMapper<ProblemBankCase> {

    /**
     * 获取问题的实例测试列表
     *
     * @param problemId 问题id
     * @return 测试列表信息
     */
    List<ProblemCaseInfoQuery> selectProblemCaseInfoByProblemId(@Param("problemId") Integer problemId);
}
