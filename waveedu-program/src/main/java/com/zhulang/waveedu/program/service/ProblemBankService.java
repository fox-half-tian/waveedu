package com.zhulang.waveedu.program.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.program.po.ProblemBank;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.program.vo.ModifyProblemVO;

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
     * 创建题目
     *
     * @param title 标题
     * @param authorType 作者类型：0-普通用户，1-管理员
     * @return 题目信息
     */
    Result saveProblem(String title, Integer authorType);

    /**
     * 修改题目
     *
     * @param modifyProblemVO 题目信息
     * @param authorType 作者类型：0-普通用户，1-管理员
     * @return 修改状况
     */
    Result modifyProblem(ModifyProblemVO modifyProblemVO,Integer authorType);

    /**
     * 删除题目
     *
     * @param problemId 题目id
     * @param authorType 作者类型：0-普通用户，1-管理员
     * @return 删除状况
     */
    Result removeProblem(Integer problemId, int authorType);

    /**
     * 获取题目列表
     *
     * @param authorType 作者类型：0-普通用户，1-管理员
     * @return 列表信息
     */
    Result getProblemList(int authorType);

    /**
     * 获取问题的详细信息
     *
     * @param problemId 问题id
     * @param authorType 作者类型：0-普通用户，1-管理员
     * @return 详细信息
     */
    Result getProblemDetailInfo(Integer problemId, int authorType);
}
