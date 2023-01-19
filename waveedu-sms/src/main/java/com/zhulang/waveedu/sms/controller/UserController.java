package com.zhulang.waveedu.sms.controller;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.sms.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户短信服务的控制器
 *
 * @author 狐狸半面添
 * @create 2023-01-17 23:37
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 发送用于登录与注册的验证码
     *
     * @param phone 手机号
     * @return 发送情况
     */
    @PostMapping("/login")
    public Result sendLoginCode(@RequestParam("phone") String phone) {
        return userService.sendLoginCode(phone);
    }
}
