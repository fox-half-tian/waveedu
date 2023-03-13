package com.zhulang.waveedu.edu.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.edu.po.ProblemBank;

/**
 * <p>
 * 编程问题题库表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-11
 */
public interface ProblemBankService extends IService<ProblemBank> {
    /**
     * 展示我的题库列表
     *
     * @return 题库列表
     */
    Result getSelfProblemInfoList();
}
