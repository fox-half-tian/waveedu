package com.zhulang.waveedu.chat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.chat.pojo.BasicUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 阿东
 * @date 2023/3/10 [13:44]
 */
@Mapper
public interface BasicUserMapper extends BaseMapper<BasicUserInfo> {
    /**
     * 获取用户基本信息
     * @param userId 该用户的id
     * @return 用户信息
     */
    List<BasicUserInfo> getBasicUserInfo(@Param("userId")Long userId);
}
