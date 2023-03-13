package com.zhulang.waveedu.basic.dao;

import com.zhulang.waveedu.basic.po.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.basic.query.AdminIdAndPasswordAndStatusQuery;
import com.zhulang.waveedu.basic.query.CommonAdminInfoQuery;
import com.zhulang.waveedu.common.entity.Result;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 管理员表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-11
 */
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 根据用户名获取信息
     *
     * @param username 用户名
     * @return id + 密码 + 状态（是否被禁用）
     */
    AdminIdAndPasswordAndStatusQuery selectIdAndPasswordAndStatusByUsername(@Param("username") String username);

    /**
     * 获取管理员的状态
     *
     * @param adminId 管理员id
     * @return 状态：0-禁用，1-启用
     */
    Integer getStatusByAdminId(@Param("adminId") Long adminId);

    /**
     * 修改管理员状态
     *
     * @param adminId 管理员id
     * @param newStatus 新状态
     */
    void updateStatusByAdmin(@Param("adminId") Long adminId,@Param("newStatus") Integer newStatus);

    /**
     * 查询所有的普通管理员
     *
     * @return 列表信息
     */
    List<CommonAdminInfoQuery> selectAllCommonAdminInfoList();
}
