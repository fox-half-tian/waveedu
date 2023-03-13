package com.zhulang.waveedu.edu.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.edu.dao.ProblemBankMapper;
import com.zhulang.waveedu.edu.po.ProblemBank;
import com.zhulang.waveedu.edu.service.ProblemBankService;
import com.zhulang.waveedu.edu.service.ProgramHomeworkProblemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    @Override
    public Result getSelfProblemInfoList() {
        return Result.ok(problemBankMapper.selectSelfProblemInfoList(UserHolderUtils.getUserId()));

    }

    @Override
    public Result getPublicProblemInfoList() {
        return Result.ok(problemBankMapper.selectPublicProblemInfoList());
    }
}
