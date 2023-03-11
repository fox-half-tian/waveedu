package com.zhulang.waveedu.program.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.program.po.ProblemBankCase;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.program.vo.SaveProblemCaseVO;

/**
 * <p>
 * 编程问题题库测试实例表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-12
 */
public interface ProblemBankCaseService extends IService<ProblemBankCase> {

    /**
     * 添加测试案例
     *
     * @param saveProblemCaseVO 案例信息
     * @param authorType 作者身份
     * @return 案例id
     */
    Result saveCase(SaveProblemCaseVO saveProblemCaseVO, Integer authorType);
}
