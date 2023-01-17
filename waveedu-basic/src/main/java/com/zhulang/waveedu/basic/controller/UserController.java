package com.zhulang.waveedu.basic.controller;

import com.zhulang.waveedu.basic.service.UserService;
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

//    @PostMapping("/login/code")
//    public Result loginByCode(){
//
//    }

}
