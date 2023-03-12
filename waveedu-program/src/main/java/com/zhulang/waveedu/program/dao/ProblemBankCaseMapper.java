package com.zhulang.waveedu.program.dao;

import com.zhulang.waveedu.program.po.ProblemBankCase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.program.query.ProblemCaseInfoQuery;
import com.zhulang.waveedu.program.query.ProblemIdAndAuthorIdAndAuthorTypeQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 编程问题题库测试实例表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-12
 */
public interface ProblemBankCaseMapper extends BaseMapper<ProblemBankCase> {

    /**
     * 根据案例id 查询 问题id，作者id，作者身份
     *
     * @param caseId 案例id
     * @return 问题id，作者id，作者身份
     */
    ProblemIdAndAuthorIdAndAuthorTypeQuery selectProblemIdAndAuthorIdAndAuthorType(@Param("caseId") Integer caseId);

    /**
     * 获取问题的实例测试列表
     *
     * @param problemId 问题id
     * @return 测试列表信息
     */
    List<ProblemCaseInfoQuery> selectProblemCaseInfoByProblemId(@Param("problemId") Integer problemId);
}
