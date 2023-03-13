package com.zhulang.waveedu.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.po.ProgramHomeworkProblem;
import com.zhulang.waveedu.edu.dao.ProgramHomeworkProblemMapper;
import com.zhulang.waveedu.edu.po.ProgramHomeworkProblemCase;
import com.zhulang.waveedu.edu.query.programhomeworkquery.TchSimpleHomeworkProblemInfoQuery;
import com.zhulang.waveedu.edu.service.LessonClassProgramHomeworkService;
import com.zhulang.waveedu.edu.service.ProgramHomeworkProblemCaseService;
import com.zhulang.waveedu.edu.service.ProgramHomeworkProblemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.ModifyProblemVO;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.SaveProblemVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.naming.spi.ObjectFactory;
import java.util.List;

/**
 * <p>
 * 编程作业表的题目表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-12
 */
@Service
public class ProgramHomeworkProblemServiceImpl extends ServiceImpl<ProgramHomeworkProblemMapper, ProgramHomeworkProblem> implements ProgramHomeworkProblemService {
    @Resource
    private ProgramHomeworkProblemMapper programHomeworkProblemMapper;
    @Resource
    private LessonClassProgramHomeworkService lessonClassProgramHomeworkService;
    @Resource
    private ProgramHomeworkProblemCaseService programHomeworkProblemCaseService;

    @Override
    public Result saveProblem(SaveProblemVO saveProblemVO) {
        Long userId = UserHolderUtils.getUserId();
        // 1.校验身份与作业状况
        Integer status = lessonClassProgramHomeworkService.getIsPublishByHomeworkIdAndCreatorId(saveProblemVO.getHomeworkId(), userId);
        if (status == null) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        if (status != 0) {
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "只允许修改未发布的作业信息");
        }
        // 2.属性设置
        ProgramHomeworkProblem problem = BeanUtil.copyProperties(saveProblemVO, ProgramHomeworkProblem.class);
        // 3.保存
        programHomeworkProblemMapper.insert(problem);
        // 4.添加作业题数
        lessonClassProgramHomeworkService.updateNumById(saveProblemVO.getHomeworkId());
        // 4.返回问题id
        return Result.ok(problem.getId());
    }

    @Override
    public Result modifyProblem(ModifyProblemVO modifyProblemVO) {
        // 1.校验身份与发布状况
        Result result = verifyIdentityHomeworkStatus(modifyProblemVO.getId(), UserHolderUtils.getUserId());
        if (result != null) {
            return result;
        }
        // 2.校验标题
        if (modifyProblemVO.getTitle() != null && StrUtil.isBlank(modifyProblemVO.getTitle())) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效标题");
        }
        // 3.属性拷贝
        ProgramHomeworkProblem problem = BeanUtil.copyProperties(modifyProblemVO, ProgramHomeworkProblem.class);
        // 4.修改信息
        programHomeworkProblemMapper.updateById(problem);

        // 5.返回
        return Result.ok();
    }

    @Override
    public Result verifyIdentityHomeworkStatus(Integer problemId, Long creatorId) {
        Integer status = programHomeworkProblemMapper.selectIsPublishByProblemIdAndCreatorId(problemId, creatorId);
        if (status == null) {
            return Result.error(HttpStatus.HTTP_INFO_REFUSE.getCode(), "未找到题目或作业信息");
        }
        if (status != 0) {
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "只允许修改未发布的作业题目信息");
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result removeProblem(Integer problemId) {
        // 0.校验数据格式
        if (problemId < 1000) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "问题id格式错误");
        }
        // 1.校验身份与发布状况
        Result result = verifyIdentityHomeworkStatus(problemId, UserHolderUtils.getUserId());
        if (result != null) {
            return result;
        }
        // 2.删除信息
        programHomeworkProblemMapper.deleteById(problemId);
        programHomeworkProblemCaseService.remove(new LambdaQueryWrapper<ProgramHomeworkProblemCase>()
                .eq(ProgramHomeworkProblemCase::getProblemId, problemId));
        // 3.修改题目数量
        lessonClassProgramHomeworkService.updateNumById(programHomeworkProblemMapper.selectHomeworkIdByProblemId(problemId));
        // 3.返回
        return Result.ok();
    }

    @Override
    public Result tchGetHomeworkProblemList(Integer homeworkId) {
        // 1.校验身份
        if (!lessonClassProgramHomeworkService.existsByHomeworkIdAndCreatorId(homeworkId,UserHolderUtils.getUserId())){
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(),HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 2.获取列表信息
        List<TchSimpleHomeworkProblemInfoQuery> infoList = programHomeworkProblemMapper.selectTchSimpleHomeworkProblemInfoList(homeworkId);
        // 3.返回
        return Result.ok(infoList);
    }

}
