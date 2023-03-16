package com.zhulang.waveedu.messagesdk.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.messagesdk.dao.ProgramHomeworkStuConditionMapper;
import com.zhulang.waveedu.messagesdk.po.ProgramHomeworkStuCondition;
import com.zhulang.waveedu.messagesdk.service.ProgramHomeworkStuConditionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 编程作业的学生情况表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-16
 */
@Service
public class ProgramHomeworkStuConditionServiceImpl extends ServiceImpl<ProgramHomeworkStuConditionMapper, ProgramHomeworkStuCondition> implements ProgramHomeworkStuConditionService {
    @Resource
    private ProgramHomeworkStuConditionMapper programHomeworkStuConditionMapper;

    @Override
    public void saveStuInfoList(Integer homeworkId, Long classId) {
        programHomeworkStuConditionMapper.insertStuInfoList(homeworkId,classId);
    }
}
