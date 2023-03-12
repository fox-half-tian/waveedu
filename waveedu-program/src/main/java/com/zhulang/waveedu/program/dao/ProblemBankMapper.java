package com.zhulang.waveedu.program.dao;

import com.zhulang.waveedu.program.po.ProblemBank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.program.query.AdminSimpleProblemQuery;
import com.zhulang.waveedu.program.query.UserSimpleProblemQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 编程问题题库表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-11
 */
public interface ProblemBankMapper extends BaseMapper<ProblemBank> {

    /**
     * 用户获取自己创建的问题列表
     *
     * @param authorId 作者id
     * @return 列表信息
     */
    List<UserSimpleProblemQuery> selectUserSimpleProblemList(@Param("authorId") Long authorId);

    /**
     * 管理员获取官方的问题列表
     *
     * @return 列表信息
     */
    List<AdminSimpleProblemQuery> selectAdminSimpleProblemList();
}
