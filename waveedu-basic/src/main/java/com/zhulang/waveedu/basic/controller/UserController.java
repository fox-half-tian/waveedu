package com.zhulang.waveedu.basic.controller;

import com.zhulang.waveedu.basic.service.UserService;
import com.zhulang.waveedu.basic.vo.PhoneCodeVO;
import com.zhulang.waveedu.basic.vo.PhonePasswordVO;
import com.zhulang.waveedu.common.entity.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 狐狸半面添
 * @create 2023-01-17 23:14
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 通过手机验证码方式进行登录&注册
     *
     * @param phoneCodeVO 手机号+验证码
     * @return 验证结果
     */
    @PostMapping("/login/code")
    public Result loginByCode(@Validated @RequestBody PhoneCodeVO phoneCodeVO){
        return userService.loginByCode(phoneCodeVO);
    }

    @PostMapping("/login/pwd")
    public Result loginByPassword(@Validated @RequestBody PhonePasswordVO phonePasswordVO){
        return userService.loginByPassword(phonePasswordVO);
    }

}
