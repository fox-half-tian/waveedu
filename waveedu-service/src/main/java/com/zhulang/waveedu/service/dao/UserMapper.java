package com.zhulang.waveedu.service.dao;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.service.po.User;

/**
 * @author 狐狸半面添
 * @create 2023-01-28 13:30
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 通过 id 查询手机号
     *
     * @param id 用户id
     * @return 手机号
     */
    String selectPhoneById(@Param("id") Long id);
}
