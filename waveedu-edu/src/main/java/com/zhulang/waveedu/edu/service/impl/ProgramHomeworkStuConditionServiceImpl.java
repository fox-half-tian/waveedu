package com.zhulang.waveedu.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.po.LessonClassProgramHomework;
import com.zhulang.waveedu.edu.po.ProgramHomeworkStuCondition;
import com.zhulang.waveedu.edu.dao.ProgramHomeworkStuConditionMapper;
import com.zhulang.waveedu.edu.query.programhomeworkquery.HomeworkStuCompleteInfoQuery;
import com.zhulang.waveedu.edu.service.LessonClassProgramHomeworkService;
import com.zhulang.waveedu.edu.service.ProgramHomeworkStuConditionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
    @Resource
    private LessonClassProgramHomeworkService lessonClassProgramHomeworkService;

    @Override
    public void saveStuInfoList(Integer homeworkId, Long classId) {
        programHomeworkStuConditionMapper.insertStuInfoList(homeworkId, classId);
    }

    @Override
    public Result tchGetStuCompleteCondition(Integer homeworkId) {
        // 1.校验格式
        if (homeworkId < 1000) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "作业Id格式错误");
        }
        // 2.查询题目总数量以及截止时间
        LessonClassProgramHomework homeworkInfo = lessonClassProgramHomeworkService.getOne(new LambdaQueryWrapper<LessonClassProgramHomework>()
                .eq(LessonClassProgramHomework::getId, homeworkId)
                .eq(LessonClassProgramHomework::getIsPublish, 1)
                .eq(LessonClassProgramHomework::getCreatorId, UserHolderUtils.getUserId()));
        if (homeworkInfo == null) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "未找到学生的作业信息或拒绝获取");
        }
        LocalDateTime endTime = homeworkInfo.getEndTime();
        Integer problemNum = homeworkInfo.getNum();
        // 3.查询学生信息
        List<HomeworkStuCompleteInfoQuery> infoList = programHomeworkStuConditionMapper.selectHomeworkStuCompleteInfoList(homeworkId);
        // 4.处理状态
        LocalDateTime now = LocalDateTime.now();
        for (HomeworkStuCompleteInfoQuery info : infoList) {
            if (Objects.equals(problemNum, info.getCompleteNum())) {
                // 说明已完成
                info.setStatus(0);
            }else if(now.isAfter(endTime)){
                // 当前时间在截止时间之后，说明已截止
                info.setStatus(2);
            }else{
                // 进行中
                info.setStatus(1);
            }
        }
        // 5.返回
        HashMap<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("problemNum",problemNum);
        resultMap.put("stuCondition",infoList);
        return Result.ok(resultMap);
    }
}
