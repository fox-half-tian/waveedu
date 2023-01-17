package com.zhulang.waveedu.basic.service;

import com.zhulang.waveedu.common.entity.Result;

/**
 * @author 狐狸半面添
 * @create 2023-01-17 23:39
 */
public interface SmsService {
    /**
     * 发送用于登录与注册的验证码
     *
     * @param phone 手机号
     * @return 发送情况
     */
    Result sendLoginCode(String phone);
}
