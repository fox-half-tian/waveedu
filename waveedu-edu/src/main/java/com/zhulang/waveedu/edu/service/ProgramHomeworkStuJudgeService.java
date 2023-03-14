package com.zhulang.waveedu.edu.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.ProgramHomeworkStuJudge;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.SubmitCodeVO;

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
     * @return 判题结果
     */
    Result submit(SubmitCodeVO submitCodeVO);
}
