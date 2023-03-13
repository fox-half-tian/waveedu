package com.zhulang.waveedu.judge.controller;

import com.zhulang.waveedu.judge.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @Author: Himit_ZH
 * @Date: 2020/12/3 20:12
 * @Description:
 */
@RestController
public class SystemConfigController {

    @Autowired
    private SystemConfigService systemConfigService;

    @RequestMapping("/get-sys-config")
    public HashMap<String,Object> getSystemConfig(){
        return systemConfigService.getSystemConfig();
    }
}