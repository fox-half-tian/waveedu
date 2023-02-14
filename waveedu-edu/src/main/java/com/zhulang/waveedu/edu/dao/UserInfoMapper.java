package com.zhulang.waveedu.edu.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.edu.po.UserInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @author 狐狸半面添
 * @create 2023-01-19 0:10
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     * 根据用户id获取头像
     *
     * @param userId 用户id
     * @return 头像
     */
    String selectIconByUserId(@Param("userId")Long userId);
}
