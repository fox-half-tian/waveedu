package com.zhulang.waveedu.basic.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.basic.service.IdentityService;
import com.zhulang.waveedu.basic.vo.IdentityVO;
import com.zhulang.waveedu.basic.vo.UpdateUserInfoVO;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 飒沓流星
 * @create 2023-02-04 17:35
 */
@RestController
@RequestMapping("/identity")
public class IdentityController {
    @Resource
    private IdentityService identityService;

    /**
     * 添加改用户的身份信息
     * @param identityVO
     * @return
     */
    @PostMapping("/add")
    public Result addIdentity(@Validated @RequestBody IdentityVO identityVO){
        // 拿到本用户的id，加进去
        identityVO.setUserId(UserHolderUtils.getUserId());
        return identityService.addIdentity(identityVO);
    }

    /**
     * 删除该用户的身份信息
     * @return
     */
    @PostMapping("/delete")
    public Result deleteIdentityByUserId(){
        Long id=UserHolderUtils.getUserId();
        return identityService.removeIdentityUserId(id);
    }

    /**
     * 查询到该id的用户身份信息
     * @param id 传入的是id
     * @return 查询结果(包含身份信息)
     */
    @GetMapping("/get/id")
    public Result getIdentityByUserId(@RequestParam("id") Long id){
        return identityService.getIdentityUserId(id);
    }

    @PutMapping("/modify")
    public Result modifyUserInfo(@Validated @RequestBody IdentityVO identityVO) {
        // 拿到本用户的id，加进去
        identityVO.setUserId(UserHolderUtils.getUserId());
        return identityService.modifyIdentity(identityVO);
    }
}
