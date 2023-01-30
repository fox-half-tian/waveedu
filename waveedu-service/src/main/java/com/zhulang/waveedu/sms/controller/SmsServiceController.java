package com.zhulang.waveedu.sms.controller;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.sms.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 短信服务
 *
 * @author 狐狸半面添
 * @create 2023-01-17 23:37
 */
@RestController
@RequestMapping("/sms")
public class SmsServiceController {
    @Resource
    private UserService userService;

    /**
     * 发送用于登录与注册的验证码
     *
     * @param phone 手机号
     * @return 发送情况
     */
    @PostMapping("/user/sendLoginCode")
    public Result sendUserLoginCode(@RequestParam("phone") String phone) {
        return userService.sendUserLoginCode(phone);
    }

    /**
     * 发送用于用户注销的验证码
     *
     * @return 发送情况
     */
    @PostMapping("/user/sendLogoffCode")
    public Result sendUserLogoffCode(){
        return userService.sendUserLogoffCode();
    }
}