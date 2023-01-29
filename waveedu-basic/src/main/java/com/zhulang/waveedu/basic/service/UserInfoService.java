package com.zhulang.waveedu.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.basic.po.User;
import com.zhulang.waveedu.basic.po.UserInfo;
import com.zhulang.waveedu.common.entity.Result;


/**
 * @author 飒沓流星
 * @create 2023-01-28 17:24
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 通过id获取用户基本信息
     * @param id
     * @return 用户信息
     */
    Result GetUserInfoById(Long id);

    /**
     * 修改指定id的用户信息
     * @param userInfo
     * @return 修改结果
     */
    Result modifyUserInfoByUserInfo(UserInfo userInfo);

/*
    /**
     * 通过用户名来获取id
     * @param name
     * @return id
    Result getIdByName(String name);
*/
}