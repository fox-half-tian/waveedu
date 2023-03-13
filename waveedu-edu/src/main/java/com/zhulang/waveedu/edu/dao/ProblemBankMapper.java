package com.zhulang.waveedu.edu.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.edu.po.ProblemBank;
import com.zhulang.waveedu.edu.query.programhomeworkquery.ProblemDetailInfoQuery;
import com.zhulang.waveedu.edu.query.programhomeworkquery.PublicSimpleProblemInfoQuery;
import com.zhulang.waveedu.edu.query.programhomeworkquery.SelfSimpleProblemInfoQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 * 编程问题题库表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-11
 */
public interface ProblemBankMapper extends BaseMapper<ProblemBank> {

    /**
     * 获取自己的题库列表信息
     *
     * @param authorId 作者id
     * @return 列表信息
     */
    List<SelfSimpleProblemInfoQuery> selectSelfProblemInfoList(@Param("authorId") Long authorId);

    /**
     * 获取公开题库列表信息
     *
     * @return 列表信息
     */
    List<PublicSimpleProblemInfoQuery> selectPublicProblemInfoList();

    /**
     * 获取某个问题的详细信息
     *
     * @param problemId 问题id
     * @return 详细信息
     */
    ProblemDetailInfoQuery selectSelfOrPublicProblemDetailInfo(@Param("problemId") Integer problemId);
}
