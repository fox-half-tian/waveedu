package com.zhulang.waveedu.edu.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.po.LessonClassProgramHomework;
import com.zhulang.waveedu.edu.dao.LessonClassProgramHomeworkMapper;
import com.zhulang.waveedu.edu.po.ProgramHomeworkProblem;
import com.zhulang.waveedu.edu.po.ProgramHomeworkProblemCase;
import com.zhulang.waveedu.edu.service.LessonClassProgramHomeworkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.service.LessonClassService;
import com.zhulang.waveedu.edu.service.ProgramHomeworkProblemCaseService;
import com.zhulang.waveedu.edu.service.ProgramHomeworkProblemService;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.ModifyProgramHomeworkVO;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.SaveProgramHomeworkVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 编程作业表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-12
 */
@Service
public class LessonClassProgramHomeworkServiceImpl extends ServiceImpl<LessonClassProgramHomeworkMapper, LessonClassProgramHomework> implements LessonClassProgramHomeworkService {
    @Resource
    private LessonClassService lessonClassService;
    @Resource
    private LessonClassProgramHomeworkMapper lessonClassProgramHomeworkMapper;
    @Resource
    private ProgramHomeworkProblemService programHomeworkProblemService;
    @Resource
    private ProgramHomeworkProblemCaseService programHomeworkProblemCaseService;

    @Override
    public Result saveHomework(SaveProgramHomeworkVO saveProgramHomeworkVO) {
        Long userId = UserHolderUtils.getUserId();
        // 1.校验身份
        if (!lessonClassService.existsByUserIdAndClassId(userId, saveProgramHomeworkVO.getClassId())) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 2.设置属性
        LessonClassProgramHomework homework = new LessonClassProgramHomework();
        homework.setClassId(saveProgramHomeworkVO.getClassId());
        homework.setCreatorId(userId);
        homework.setTitle(saveProgramHomeworkVO.getTitle());
        // 3.保存
        lessonClassProgramHomeworkMapper.insert(homework);
        // 4.返回作业Id
        return Result.ok(homework.getId());
    }

    @Override
    public Result modifyInfo(ModifyProgramHomeworkVO modifyProgramHomeworkVO) {
        // 1.校验标题
        if (modifyProgramHomeworkVO.getTitle() != null && StrUtil.isBlank(modifyProgramHomeworkVO.getTitle())) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效标题");
        }
        // 2.修改信息
        lessonClassProgramHomeworkMapper.update(null, new LambdaUpdateWrapper<LessonClassProgramHomework>()
                .eq(LessonClassProgramHomework::getId, modifyProgramHomeworkVO.getHomeworkId())
                .eq(LessonClassProgramHomework::getCreatorId, UserHolderUtils.getUserId())
                .set(modifyProgramHomeworkVO.getTitle() != null, LessonClassProgramHomework::getTitle, modifyProgramHomeworkVO.getTitle())
                .set(modifyProgramHomeworkVO.getEndTime() != null, LessonClassProgramHomework::getEndTime, modifyProgramHomeworkVO.getEndTime()));
        // 3.返回
        return Result.ok();
    }

    @Override
    public Integer getIsPublishByHomeworkIdAndCreatorId(Integer homeworkId, Long creatorId) {
        return lessonClassProgramHomeworkMapper.selectIsPublishByHomeworkIdAndCreatorId(homeworkId,creatorId);
    }

    @Override
    public void updateNumById(Integer homeworkId) {
        lessonClassProgramHomeworkMapper.updateNumById(homeworkId);
    }

    @Override
//    @Transactional(rollbackFor = Exception.class)
    public Result removeHomework(Integer homeworkId) {
        // 1.校验权限
        if (!existsByHomeworkIdAndCreatorId(homeworkId,UserHolderUtils.getUserId())){
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(),HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 2.删除作业
        lessonClassProgramHomeworkMapper.deleteById(homeworkId);
        // 3.返回
        return Result.ok();

//        programHomeworkProblemService.remove(new LambdaQueryWrapper<ProgramHomeworkProblem>()
//                .eq(ProgramHomeworkProblem::getHomeworkId,homeworkId));
//        programHomeworkProblemCaseService.remove(new LambdaQueryWrapper<ProgramHomeworkProblemCase>()
//                .eq(ProgramHomeworkProblemCase::getHomeworkId,homeworkId));

    }

    public boolean existsByHomeworkIdAndCreatorId(Integer homeworkId,Long creatorId){
        return lessonClassProgramHomeworkMapper.existsByHomeworkIdAndCreatorId(homeworkId,creatorId)!=null;
    }
}
