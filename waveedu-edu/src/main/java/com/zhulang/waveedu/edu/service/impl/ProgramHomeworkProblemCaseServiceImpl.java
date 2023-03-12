package com.zhulang.waveedu.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.po.ProgramHomeworkProblemCase;
import com.zhulang.waveedu.edu.dao.ProgramHomeworkProblemCaseMapper;
import com.zhulang.waveedu.edu.service.ProgramHomeworkProblemCaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.edu.service.ProgramHomeworkProblemService;
import com.zhulang.waveedu.edu.vo.programhomeworkvo.SaveCaseVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 编程作业问题测试实例表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-13
 */
@Service
public class ProgramHomeworkProblemCaseServiceImpl extends ServiceImpl<ProgramHomeworkProblemCaseMapper, ProgramHomeworkProblemCase> implements ProgramHomeworkProblemCaseService {

    @Resource
    private ProgramHomeworkProblemCaseMapper programHomeworkProblemCaseMapper;
    @Resource
    private ProgramHomeworkProblemService programHomeworkProblemService;

    @Override
    public Result saveCase(SaveCaseVO saveCaseVO) {
        // 1.校验身份与作业状况
        Result result = programHomeworkProblemService.verifyIdentityHomeworkStatus(saveCaseVO.getProblemId(), UserHolderUtils.getUserId());
        if (result != null) {
            return result;
        }
        // 2.属性拷贝
        ProgramHomeworkProblemCase problemCase = BeanUtil.copyProperties(saveCaseVO, ProgramHomeworkProblemCase.class);
        // 3.保存信息
        programHomeworkProblemCaseMapper.insert(problemCase);
        // 4.返回id
        return Result.ok(problemCase.getId());
    }

    @Override
    public Result removeCase(Integer caseId) {
        // 1.验证身份和发布状态
        Result result = verifyIdentityHomeworkStatus(caseId, UserHolderUtils.getUserId());
        if (result != null) {
            return result;
        }
        // 2.移除案例
        programHomeworkProblemCaseMapper.deleteById(caseId);
        // 3.返回
        return Result.ok();
    }

    @Override
    public Result verifyIdentityHomeworkStatus(Integer caseId, Long creatorId) {
        Integer status = programHomeworkProblemCaseMapper.selectIsPublishByProblemIdAndCreatorId(caseId, creatorId);
        if (status == null) {
            return Result.error(HttpStatus.HTTP_INFO_REFUSE.getCode(), "未找到题目或作业信息");
        }
        if (status != 0) {
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "只允许修改未发布的作业题目信息");
        }
        return null;
    }
}
