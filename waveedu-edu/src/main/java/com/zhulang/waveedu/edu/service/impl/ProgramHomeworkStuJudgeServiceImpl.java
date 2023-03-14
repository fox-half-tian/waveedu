package com.zhulang.waveedu.edu.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.dao.ProgramHomeworkStuJudgeMapper;
import com.zhulang.waveedu.edu.po.ProgramHomeworkStuJudge;
import com.zhulang.waveedu.edu.service.ProgramHomeworkStuJudgeService;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.SubmitCodeVO;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 编程作业的学生判题表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-14
 */
@Service
public class ProgramHomeworkStuJudgeServiceImpl extends ServiceImpl<ProgramHomeworkStuJudgeMapper, ProgramHomeworkStuJudge> implements ProgramHomeworkStuJudgeService {

    @Override
    public Result submit(SubmitCodeVO submitCodeVO) {
        return null;
    }
}
