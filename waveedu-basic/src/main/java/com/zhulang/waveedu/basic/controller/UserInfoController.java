package com.zhulang.waveedu.basic.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.basic.po.UserInfo;
import com.zhulang.waveedu.basic.service.UserInfoService;
import com.zhulang.waveedu.basic.vo.UpdateUserInfoVO;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 飒沓流星
 * @create 2023/1/28 22:38
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    /**
     * 通过id获取本用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/get/self/info")
    public Result getSelfInfo() {
        return userInfoService.getUserInfoById(UserHolderUtils.getUserId());
    }

    /**
     * 通过id来获取其他用户的信息
     *
     * @param id 传入的是id
     * @return 用户信息
     */
    @GetMapping("/get/info/id")
    public Result getUserInfoByid(@RequestParam("id") Long id) {
        return userInfoService.getUserInfoById(id);
    }

    /**
     * 修改本用户信息
     *
     * @param updateUserInfoVO 需要修改的信息
     * @return 修改结果
     */
    @PutMapping("/modify")
    public Result modifyUserInfo(@Validated @RequestBody UpdateUserInfoVO updateUserInfoVO) {
        // 拿到本用户的id，加进去
        updateUserInfoVO.setId(UserHolderUtils.getUserId());
        return userInfoService.modifyUserInfo(updateUserInfoVO);
    }

    /**
     * 获取用户自己的头像和姓名
     *
     * @return 头像+姓名
     */
    @GetMapping("/get/self/simpleInfo")
    public Result getSelfSimpleInfo() {
        return userInfoService.getSelfSimpleInfo();
    }
}
