package com.zhulang.waveedu.program.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.program.constant.AuthorTypeConstants;
import com.zhulang.waveedu.program.dao.ProblemBankMapper;
import com.zhulang.waveedu.program.po.ProblemBank;
import com.zhulang.waveedu.program.po.ProblemBankCase;
import com.zhulang.waveedu.program.query.ProblemCaseInfoQuery;
import com.zhulang.waveedu.program.query.ProblemDetailInfoQuery;
import com.zhulang.waveedu.program.service.ProblemBankCaseService;
import com.zhulang.waveedu.program.service.ProblemBankService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.program.vo.ModifyProblemVO;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static com.zhulang.waveedu.program.constant.AuthorTypeConstants.ADMIN;
import static com.zhulang.waveedu.program.constant.AuthorTypeConstants.USER;

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

    @Override
    public Result saveProblem(String title, Integer authorType) {
        // 1.校验格式
        if (StrUtil.isBlank(title)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效标题");
        }
        if (title.length() > 255) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "标题最多255字");
        }
        // 2.设置信息
        ProblemBank problemBank = new ProblemBank();
        // 作者id
        problemBank.setAuthorId(UserHolderUtils.getUserId());
        // 作者类型
        problemBank.setAuthorType(authorType);
        // 设置标题
        problemBank.setTitle(title);

        // 3.保存信息到数据库
        problemBankMapper.insert(problemBank);
        // 4.返回题目信息
        return Result.ok(problemBankMapper.selectById(problemBank.getId()));
    }

    @Override
    public Result modifyProblem(ModifyProblemVO modifyProblemVO, Integer authorType) {
        // 0.校验格式
        if (modifyProblemVO.getTitle() != null && StrUtil.isBlank(modifyProblemVO.getTitle())) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "无效标题");
        }
        // 1.对象转换
        ProblemBank problemBank = BeanUtil.copyProperties(modifyProblemVO, ProblemBank.class);
        // 2.保存
        int updateCount = problemBankMapper.update(problemBank, new LambdaQueryWrapper<ProblemBank>()
                .eq(ProblemBank::getId, problemBank.getId())
                .eq(ProblemBank::getAuthorType, authorType)
                .eq(authorType == USER, ProblemBank::getAuthorId, UserHolderUtils.getUserId()));
        // 3.返回
        return updateCount != 0 ? Result.ok() : Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "未找到问题信息");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result removeProblem(Integer problemId, int authorType) {
        // 1.校验格式
        if (problemId < 1000) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "问题id格式错误");
        }
        // 2.删除题库表数据
        int deleteCount = problemBankMapper.delete(new LambdaQueryWrapper<ProblemBank>()
                .eq(ProblemBank::getId, problemId)
                .eq(ProblemBank::getAuthorType, authorType)
                .eq(authorType == USER, ProblemBank::getAuthorId, UserHolderUtils.getUserId()));
        if (deleteCount == 0) {
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "权限不足或题目不存在");
        }
        // 3.删除实例表数据
        problemBankCaseService.remove(new LambdaQueryWrapper<ProblemBankCase>()
                .eq(ProblemBankCase::getProblemId, problemId));
        // 4.返回
        return Result.ok();
    }

    @Override
    public Result getProblemList(int authorType) {
        if (authorType == USER) {
            return Result.ok(problemBankMapper.selectUserSimpleProblemList(UserHolderUtils.getUserId()));
        } else {
            return Result.ok(problemBankMapper.selectAdminSimpleProblemList());
        }
    }

    @Override
    public Result getProblemDetailInfo(Integer problemId, int authorType) {
        // 1.校验格式
        if (problemId < 1000) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "问题id格式错误");
        }
        // 2.获取问题信息
        ProblemDetailInfoQuery questionInfo = problemBankMapper.selectProblemDetailInfo(problemId, authorType, UserHolderUtils.getUserId());
        if (questionInfo==null){
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(),"权限不足或问题不存在");
        }
        // 3.获取示例测试
        List<ProblemCaseInfoQuery> caseList = problemBankCaseService.getProblemCaseInfoByProblemId(problemId);

        // 4.将测试案例加入到问题信息中
        questionInfo.setProblemCaseInfoQueryList(caseList);

        // 5.返回
        return Result.ok(questionInfo);
    }
}
