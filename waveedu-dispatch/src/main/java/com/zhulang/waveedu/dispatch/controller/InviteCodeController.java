package com.zhulang.waveedu.dispatch.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.entity.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对邀请码进行统一处理
 *
 * @author 狐狸半面添
 * @create 2023-02-17 1:22
 */
@RestController
@RequestMapping("/invite-code")
public class InviteCodeController {
    /**
     * 对邀请码进行统一处理
     *
     * @param object 邀请码
     * @return 校验结果
     */
    @PostMapping("/identity")
    public Result identity(@RequestBody JSONObject object){
        return null;
    }
}
