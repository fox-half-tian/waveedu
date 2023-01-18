package com.zhulang.waveedu.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.basic.po.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author 狐狸半面添
 * @create 2023-01-18 22:17
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询该手机号的用户id
     *
     * @param phone 手机号
     * @return 用户id
     */
    Long selectIdByPhone(@Param("phone") String phone);
}
