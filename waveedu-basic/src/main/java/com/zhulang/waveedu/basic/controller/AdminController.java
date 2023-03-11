package com.zhulang.waveedu.basic.controller;


import com.zhulang.waveedu.basic.service.AdminService;
import com.zhulang.waveedu.basic.vo.AdminLoginVO;
import com.zhulang.waveedu.common.entity.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-11
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminService adminService;

    /**
     * 管理员登录
     *
     * @param adminLoginVO 账号 + 密码
     * @return 登录状况，成功则返回token
     */
    @PostMapping("/login")
    public Result login(@Validated @RequestBody AdminLoginVO adminLoginVO){
        return adminService.login(adminLoginVO.getUsername(),adminLoginVO.getPassword());
    }


    /**
     * 保存/添加管理员信息
     *
     * @return 管理员id
     */
    @PostMapping("/saveAdmin")
    public Result saveAdmin(){
        return adminService.saveAdmin();
    }

    /**
     * 获取自身的简单信息：昵称+头像
     *
     * @return 昵称 + 头像
     */
    @GetMapping("/get/selfSimpleInfo")
    public Result getSelfSimpleInfo(){
        return adminService.getSelfSimpleInfo();
    }


}
