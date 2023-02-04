package com.zhulang.waveedu.basic.controller;

import com.zhulang.waveedu.basic.service.UserService;
import com.zhulang.waveedu.basic.vo.LogoffVO;
import com.zhulang.waveedu.basic.vo.PhoneCodeVO;
import com.zhulang.waveedu.basic.vo.PhonePasswordVO;
import com.zhulang.waveedu.basic.vo.UpdatePwdVO;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * User登录，注册，推出登录，注销的控制器
 *
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

    /**
     * 通过密码方式进行登录
     *
     * @param phonePasswordVO 手机号——密码
     * @return 验证结果
     */
    @PostMapping("/login/pwd")
    public Result loginByPassword(@Validated @RequestBody PhonePasswordVO phonePasswordVO){
        return userService.loginByPassword(phonePasswordVO);
    }

    /**
     * 用户退出登录
     *
     * @return 退出状况
     */
    @PostMapping("/logout")
    public Result logout(){
        return userService.logout(UserHolderUtils.getUserId());
    }

    /**
     * 通过手机号进行验证，完成用户注销
     *
     * @param logoffVO 验证码 + 注销原因
     * @return 注销状况
     */
    @PostMapping("/logoff")
    public Result logoff(@Validated @RequestBody LogoffVO logoffVO){
        return userService.logoff(logoffVO);
    }

    /**
     * 修改密码
     *
     * @param updatePwdVO 两个密码+code
     * @return 修改情况
     */
    @PutMapping("/updatePwd")
    public Result updatePwd(@Validated @RequestBody UpdatePwdVO updatePwdVO){
        return userService.updatePwd(updatePwdVO);
    }
}
