package com.zhulang.waveedu.basic.service;

import com.zhulang.waveedu.basic.po.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.basic.vo.AdminModifyInfoVO;
import com.zhulang.waveedu.common.entity.Result;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-11
 */
public interface AdminService extends IService<Admin> {

    /**
     * 管理员登录
     *
     * @param username 账号
     * @param password 密码
     * @return 登录状况，成功则返回token
     */
    Result login(String username, String password);

    /**
     * 保存/添加管理员信息
     * 只允许超级管理员操作
     *
     * @return 管理员id
     */
    Result saveAdmin();

    /**
     * 获取自身的简单信息：昵称+头像
     *
     * @return 昵称 + 头像
     */
    Result getSelfSimpleInfo();

    /**
     * 修改自身信息
     *
     * @param adminModifyInfoVO 信息：头像，昵称
     * @return 修改状况
     */
    Result modifySelfInfo(AdminModifyInfoVO adminModifyInfoVO);

    /**
     * 启用或禁用管理员
     * 只允许超级管理员进行操作
     *
     * @param adminId 管理员id
     * @return 修改后的状态
     */
    Result switchStatus(Long adminId);

    /**
     * 获取所有普通管理员信息列表
     * 只允许超级管理员操作
     *
     * @return 管理员信息
     */
    Result getAllCommonAdminInfoList();

    /**
     * 删除管理员账号
     * 只允许超级管理员操作
     *
     * @param adminId 管理员id
     * @return 删除状况
     */
    Result removeAdminAccount(Long adminId);
}
