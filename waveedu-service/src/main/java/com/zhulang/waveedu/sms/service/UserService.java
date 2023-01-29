package com.zhulang.waveedu.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.sms.po.User;

/**
 * @author 狐狸半面添
 * @create 2023-01-17 23:39
 */
public interface UserService extends IService<User> {
    /**
     * 发送用于登录与注册的验证码
     *
     * @param phone 手机号
     * @return 发送情况
     */
    Result sendUserLoginCode(String phone);

    /**
     * 发送用于用户注销的验证码
     *
     * @return 发送情况
     */
    Result sendUserLogoffCode();
}
