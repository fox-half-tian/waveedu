package com.zhulang.waveedu.edu.service.impl;

import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.po.CommonHomeworkStuScore;
import com.zhulang.waveedu.edu.dao.CommonHomeworkStuScoreMapper;
import com.zhulang.waveedu.edu.service.CommonHomeworkStuScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.service.LessonClassCommonHomeworkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 普通作业表的学生成绩表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-01
 */
@Service
public class CommonHomeworkStuScoreServiceImpl extends ServiceImpl<CommonHomeworkStuScoreMapper, CommonHomeworkStuScore> implements CommonHomeworkStuScoreService {
    @Resource
    private CommonHomeworkStuScoreMapper commonHomeworkStuScoreMapper;
    @Resource
    private LessonClassCommonHomeworkService lessonClassCommonHomeworkService;

    @Override
    public Result getHomeworksCheckTaskList(Long classId, Integer status) {
        // 1.校验格式
        if (RegexUtils.isSnowIdInvalid(classId)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "班级id格式错误");
        }
        if (status != 0 && status != 1) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "状态格式错误");
        }
        // 2.根据
        return null;
    }

    @Override
    public Result getHomeworkStuConditionList(Integer homeworkId, Integer status) {
        // 1.校验格式
        if (homeworkId < 1) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "作业id格式错误");
        }
        if (status<0||status>3){
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "状态格式错误");
        }
        // 2.校验身份
        Long userId = UserHolderUtils.getUserId();
        if (!lessonClassCommonHomeworkService.existsByIdAndUserId(homeworkId,userId)){
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(),HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 3.根据status获取信息
        switch (status){
            case 0:
                // 未提交
                return Result.ok(commonHomeworkStuScoreMapper.selectHomeworkNoCommitStuInfoListByHomeworkId(homeworkId));
            case 1:
                // 批阅中：学生id，学生姓名，提交时间
                return Result.ok(commonHomeworkStuScoreMapper.selectHomeworkNoCheckStuInfoListByHomeworkId(homeworkId));
            case 2:
                // 已批阅 学生id,学生姓名，提交时间，分数
                return Result.ok(commonHomeworkStuScoreMapper.selectCheckedStuInfoListByHomeworkId(homeworkId));
            case 3:
                // 所有：学生id,学生姓名，提交时间，分数，状态 0-待批阅，1-已批阅，2-未提交
                return Result.ok(commonHomeworkStuScoreMapper.selectHomeworkAllStuInfoListByHomeworkId(homeworkId));
            default:
                return Result.error();
        }
    }
}
