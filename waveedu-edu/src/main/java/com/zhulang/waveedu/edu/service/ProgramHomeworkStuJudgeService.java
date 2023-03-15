package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.JudgeResult;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.ProgramHomeworkStuJudge;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.SubmitCodeVO;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 编程作业的学生判题表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-14
 */
public interface ProgramHomeworkStuJudgeService extends IService<ProgramHomeworkStuJudge> {

    /**
     * 判题操作
     *
     * @param submitCodeVO 提交代码信息
     * @param request 请求
     * @return 判题结果
     */
    Result submit(SubmitCodeVO submitCodeVO, HttpServletRequest request);

    /**
     * 根据提交的信息修改数据库
     *
     * @param judgeResult 判题结果：状态码，错误信息，运行时间，运行内存
     * @param userId 用户id
     * @param languageCode 代码
     * @param problemId 问题id
     * @param language 语言
     * @param homeworkId 作业id
     * @param isEnd 是否到了截止时间，true-已截止，false-为截止
     */
    void updateRecord(JudgeResult judgeResult,
                      Long userId,
                      String languageCode,
                      Integer problemId,
                      String language,
                      Integer homeworkId,
                      boolean isEnd
    );
}
