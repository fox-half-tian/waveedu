package com.zhulang.waveedu.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.basic.po.UserInfo;
import com.zhulang.waveedu.basic.vo.UpdateUserInfoVO;
import com.zhulang.waveedu.common.entity.Result;


/**
 * @author 飒沓流星
 * @create 2023-01-28 17:24
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 通过id获取用户基本信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    Result getUserInfoById(Long id);

    /**
     * 修改指定id的用户信息
     *
     * @param updateUserInfoVO 需要修改的信息
     * @return 修改结果
     */
    Result modifyUserInfo(UpdateUserInfoVO updateUserInfoVO);

    /**
     * 获取用户自己的头像和姓名
     *
     * @return 头像+姓名
     */
    Result getSelfSimpleInfo();

/*
    /**
     * 通过用户名来获取id
     * @param name
     * @return id
    Result getIdByName(String name);
*/
}