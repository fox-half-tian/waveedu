package com.zhulang.waveedu.program.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.program.constant.AuthorTypeConstants;
import com.zhulang.waveedu.program.po.ProblemBank;
import com.zhulang.waveedu.program.po.ProblemBankCase;
import com.zhulang.waveedu.program.dao.ProblemBankCaseMapper;
import com.zhulang.waveedu.program.query.ProblemIdAndAuthorIdAndAuthorTypeQuery;
import com.zhulang.waveedu.program.service.ProblemBankCaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.program.service.ProblemBankService;
import com.zhulang.waveedu.program.vo.SaveProblemCaseVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.zhulang.waveedu.program.constant.AuthorTypeConstants.USER;

/**
 * <p>
 * 编程问题题库测试实例表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-12
 */
@Service
public class ProblemBankCaseServiceImpl extends ServiceImpl<ProblemBankCaseMapper, ProblemBankCase> implements ProblemBankCaseService {
    @Resource
    private ProblemBankCaseMapper problemBankCaseMapper;
    @Resource
    private ProblemBankService problemBankService;

    @Override
    public Result saveCase(SaveProblemCaseVO saveProblemCaseVO, Integer authorType) {
        // 1.属性转换
        ProblemBankCase problemBankCase = BeanUtil.copyProperties(saveProblemCaseVO, ProblemBankCase.class);
        // 2.查询是否存在问题
        long count = problemBankService.count(new LambdaQueryWrapper<ProblemBank>()
                .eq(ProblemBank::getId, problemBankCase.getProblemId())
                .eq(ProblemBank::getAuthorType, authorType)
                .eq(authorType == USER, ProblemBank::getAuthorId, UserHolderUtils.getUserId()));
        if (count == 0) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "未找到问题信息");
        }
        // 3.添加记录
        problemBankCaseMapper.insert(problemBankCase);
        // 4.返回
        return Result.ok(problemBankCase.getId());
    }

    @Override
    public Result removeCase(Integer caseId, int authorType) {
        // 0.校验格式
        if (caseId < 1000) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "案例id格式错误");
        }
        // 1.查询实例对应的问题id，作者id，作者身份
        ProblemIdAndAuthorIdAndAuthorTypeQuery queryInfo = problemBankCaseMapper.selectProblemIdAndAuthorIdAndAuthorType(caseId);
        // 2.为空说明问题或案例不存在
        if (queryInfo == null) {
            return Result.error(HttpStatus.HTTP_INFO_NOT_EXIST.getCode(), "问题或案例信息不存在");
        }
        // 3.根据身份进行处理
        // 3.1 如果当前是用户进行接口调用，并且这个案例对应问题的作者身份是用户，则需要判断作者id是否为本人
        if (queryInfo.getAuthorType() == USER && authorType == USER) {
            if (queryInfo.getAuthorId().longValue() != UserHolderUtils.getUserId()) {
                return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(),HttpStatus.HTTP_FORBIDDEN.getValue());
            }
        }else if(queryInfo.getAuthorType() != authorType){
            // 3.2 如果当前身份与问题的作者身份不一致
            return Result.error(HttpStatus.HTTP_FORBIDDEN.getCode(),HttpStatus.HTTP_FORBIDDEN.getValue());
        }
        // 4.移除案例
        problemBankCaseMapper.deleteById(caseId);
        // 5.返回
        return Result.ok();
    }
}
