package com.zhulang.waveedu.basic.dao;

import com.zhulang.waveedu.basic.po.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.basic.query.AdminIdAndPasswordAndStatusQuery;
import org.apache.ibatis.annotations.Param;

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
}
