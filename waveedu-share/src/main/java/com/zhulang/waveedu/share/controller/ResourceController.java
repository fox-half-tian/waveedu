package com.zhulang.waveedu.share.controller;


import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.share.service.ResourceService;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 用户获取自己所有的资源列表
     *
     * @return 资源列表
     */
    @GetMapping("/getSelfResourcesList")
    public Result getSelfResourcesList(){
        return resourceService.getSelfResourcesList();
    }

    /**
     * 删除自己的某个资源
     *
     * @param resourceId 资源id
     * @return 删除状况
     */
    @DeleteMapping("/remove")
    public Result removeResource(@RequestParam("resourceId")Integer resourceId){
        return resourceService.removeResource(resourceId);
    }

    /**
     * 获取某个资源的信息
     *
     * @param resourceId 资源id
     * @return 资源信息
     */
    @GetMapping("/getResourceInfo")
    public Result getResourceInfo(@RequestParam("resourceId")Integer resourceId){
        return resourceService.getResourceInfo(resourceId);
    }

    /**
     * 获取资源信息列表
     *
     * @param resourceId 资源id
     * @return 列表信息
     */
    @GetMapping("/getResourceInfoList")
    public Result getResourceInfoList(@RequestParam(value = "resourceId",required = false)Integer resourceId){
        return resourceService.getResourceInfoList(resourceId);
    }

    /**
     * 资源下载，下载次数+1
     *
     * @param object 资源id
     * @return 资源路径
     */
    @PostMapping("/download")
    public Result download(@RequestBody JSONObject object){
        try {
            return resourceService.download(Integer.parseInt(object.getString("resourceId")));
        } catch (NumberFormatException e) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(),"资源id格式错误");
        }
    }

}
