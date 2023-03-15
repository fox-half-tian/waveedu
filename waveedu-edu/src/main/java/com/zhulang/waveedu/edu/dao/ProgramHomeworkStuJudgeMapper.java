package com.zhulang.waveedu.edu.dao;

import com.zhulang.waveedu.edu.po.ProgramHomeworkStuJudge;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.edu.query.programhomeworkquery.SimpleSubmitRecordQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 编程作业的学生判题表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-14
 */
public interface ProgramHomeworkStuJudgeMapper extends BaseMapper<ProgramHomeworkStuJudge> {

    /**
     * 获取该学生的某个问题的提交信息列表
     *
     * @param stuId 学生id
     * @param problemId 问题id
     * @return 信息列表
     */
    List<SimpleSubmitRecordQuery> selectAllSubmitRecords(@Param("stuId") Long stuId, @Param("problemId") Integer problemId);
}
