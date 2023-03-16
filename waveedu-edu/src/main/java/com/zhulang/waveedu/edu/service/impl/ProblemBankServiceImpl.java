package com.zhulang.waveedu.edu.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.dao.ProblemBankMapper;
import com.zhulang.waveedu.edu.po.ProblemBank;
import com.zhulang.waveedu.edu.po.ProgramHomeworkProblem;
import com.zhulang.waveedu.edu.po.ProgramHomeworkProblemCase;
import com.zhulang.waveedu.edu.query.programhomeworkquery.ProblemCaseInfoQuery;
import com.zhulang.waveedu.edu.query.programhomeworkquery.ProblemDetailInfoQuery;
import com.zhulang.waveedu.edu.query.programhomeworkquery.ProblemImportInfoQuery;
import com.zhulang.waveedu.edu.service.*;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.SaveImportHomeworkVO;
import org.apache.ibatis.annotations.Case;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 编程问题题库表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-11
 */
@Service
public class ProblemBankServiceImpl extends ServiceImpl<ProblemBankMapper, ProblemBank> implements ProblemBankService {
    @Resource
    private ProblemBankMapper problemBankMapper;
    @Resource
    private ProblemBankCaseService problemBankCaseService;
    @Resource
    private LessonClassProgramHomeworkService lessonClassProgramHomeworkService;
    @Resource
    private ProgramHomeworkProblemService programHomeworkProblemService;
    @Resource
    private ProgramHomeworkProblemCaseService programHomeworkProblemCaseService;

    @Override
    public Result getSelfProblemInfoList() {
        return Result.ok(problemBankMapper.selectSelfProblemInfoList(UserHolderUtils.getUserId()));

    }

    @Override
    public Result getPublicProblemInfoList() {
        return Result.ok(problemBankMapper.selectPublicProblemInfoList());
    }

    @Override
    public Result getSelfOrPublicProblemDetailInfo(Integer problemId) {
        // 1.校验格式
        if (problemId < 1000) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "问题id格式错误");
        }
        // 2.获取问题信息
        ProblemDetailInfoQuery questionInfo = problemBankMapper.selectSelfOrPublicProblemDetailInfo(problemId);
        if (questionInfo == null) {
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "权限不足或问题不存在");
        }
        // 3.获取示例测试
        List<ProblemCaseInfoQuery> caseList = problemBankCaseService.getProblemCaseInfoByProblemId(problemId);

        // 4.将测试案例加入到问题信息中
        questionInfo.setProblemCaseInfoQueryList(caseList);

        // 5.返回
        return Result.ok(questionInfo);
    }

    @Override
    public Result saveImportHomework(SaveImportHomeworkVO saveImportHomeworkVO) {
        Long userId = UserHolderUtils.getUserId();
        // 1.校验权限与作业状态
        Integer status = lessonClassProgramHomeworkService.getIsPublishByHomeworkIdAndCreatorId(saveImportHomeworkVO.getHomeworkId(), userId);
        if (status == null) {
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(), HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        if (status != 0) {
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "只允许修改未发布的作业信息");
        }
        // 2.查询信息
        List<ProblemImportInfoQuery> problemImportInfoQueries = problemBankMapper.selectNeedImportProblemsInfoList(saveImportHomeworkVO.getProblemIds(), userId);
        // 3.导入到作业
        ((ProblemBankService) AopContext.currentProxy()).importProblemToHomework(problemImportInfoQueries, saveImportHomeworkVO.getHomeworkId());
        // 4.返回
        return Result.ok();

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importProblemToHomework(List<ProblemImportInfoQuery> problemImportInfoQueries,Integer homeworkId){
        for (ProblemImportInfoQuery problem : problemImportInfoQueries) {
            ProgramHomeworkProblem homeworkProblem = BeanUtil.copyProperties(problem, ProgramHomeworkProblem.class);
            homeworkProblem.setHomeworkId(homeworkId);
            // 添加到问题表
            programHomeworkProblemService.save(homeworkProblem);
            List<ProgramHomeworkProblemCase> caseList = BeanUtil.copyToList(problem.getCaseList(), ProgramHomeworkProblemCase.class);
            caseList.forEach(c ->{
                c.setProblemId(homeworkProblem.getId());
            });
            // 添加到案例表
            programHomeworkProblemCaseService.saveBatch(caseList);
        }
        // 修改题目数量
        lessonClassProgramHomeworkService.updateNumById(homeworkId);
    }
}
