package com.zhulang.waveedu.share.controller;


import com.zhulang.waveedu.share.service.ResourceService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 资源分析表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-19
 */
@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Resource
    private ResourceService resourceService;
}
