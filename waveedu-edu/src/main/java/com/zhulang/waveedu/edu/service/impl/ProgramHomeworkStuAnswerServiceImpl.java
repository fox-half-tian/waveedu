package com.zhulang.waveedu.edu.service.impl;

import com.zhulang.waveedu.edu.po.ProgramHomeworkStuAnswer;
import com.zhulang.waveedu.edu.dao.ProgramHomeworkStuAnswerMapper;
import com.zhulang.waveedu.edu.service.ProgramHomeworkStuAnswerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 编程作业的学生正确回答表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-15
 */
@Service
public class ProgramHomeworkStuAnswerServiceImpl extends ServiceImpl<ProgramHomeworkStuAnswerMapper, ProgramHomeworkStuAnswer> implements ProgramHomeworkStuAnswerService {
    @Resource
    private ProgramHomeworkStuAnswerMapper programHomeworkStuAnswerMapper;

    @Override
    public Integer getIdByUserIdAndProblemId(Long stuId, Integer problemId) {
        return programHomeworkStuAnswerMapper.selectIdByUserIdAndProblemId(stuId,problemId);
    }
}
