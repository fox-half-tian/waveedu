package com.zhulang.waveedu.basic.controller;


import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.zhulang.waveedu.basic.service.AdminService;
import com.zhulang.waveedu.basic.vo.AdminLoginVO;
import com.zhulang.waveedu.basic.vo.AdminModifyInfoVO;
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
     * @return 登录状况，成功则返回token与身份
     */
    @PostMapping("/login")
    public Result login(@Validated @RequestBody AdminLoginVO adminLoginVO){
        return adminService.login(adminLoginVO.getUsername(),adminLoginVO.getPassword());
    }


    /**
     * 保存/添加管理员信息
     * 只允许超级管理员操作
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

    /**
     * 修改自身信息
     *
     * @param adminModifyInfoVO 信息：头像，昵称
     * @return 修改状况
     */
    @PutMapping("/modify/selfInfo")
    public Result modifySelfInfo(@Validated @RequestBody AdminModifyInfoVO adminModifyInfoVO){
        return adminService.modifySelfInfo(adminModifyInfoVO);
    }

    /**
     * 启用或禁用普通管理员账号
     * 只允许超级管理员进行操作
     *
     * @param object 管理员id
     * @return 修改后的状态
     */
    @PutMapping("/switch/status")
    public Result switchStatus(@RequestBody JSONObject object){
        return adminService.switchStatus(Long.parseLong(object.getString("adminId")));
    }

    /**
     * 获取所有普通管理员信息列表
     * 只允许超级管理员操作
     *
     * @return 管理员信息
     */
    @GetMapping("/get/allCommonAdminInfoList")
    public Result getAllCommonAdminInfoList(){
        return adminService.getAllCommonAdminInfoList();
    }

    /**
     * 删除管理员账号
     * 只允许超级管理员操作
     *
     * @param adminId 管理员id
     * @return 删除状况
     */
    @DeleteMapping("/remove/adminAccount")
    public Result removeAdminAccount(@RequestParam("adminId")Long adminId){
        return adminService.removeAdminAccount(adminId);
    }

}
