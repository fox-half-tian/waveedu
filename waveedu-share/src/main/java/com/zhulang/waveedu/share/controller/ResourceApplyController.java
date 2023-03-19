package com.zhulang.waveedu.share.controller;


import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.share.service.ResourceApplyService;
import com.zhulang.waveedu.share.vo.ResourceApproveVO;
import com.zhulang.waveedu.share.vo.SaveResourceApplyVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * <p>
 * 资源分享申请表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-19
 */
@RestController
@RequestMapping("/resource-apply")
public class ResourceApplyController {
    @Resource
    private ResourceApplyService resourceApplyService;

    /**
     * 用户申请
     *
     * @param saveResourceApplyVO 资源信息
     * @return 申请id
     */
    @PostMapping("/save")
    public Result saveApply(@Validated @RequestBody SaveResourceApplyVO saveResourceApplyVO){
        return resourceApplyService.saveApply(saveResourceApplyVO);
    }

    /**
     * 管理员审批
     *
     * @param resourceApproveVO 审批信息
     * @return 审批结果
     */
    @PostMapping("/approve")
    public Result approve(@Validated @RequestBody ResourceApproveVO resourceApproveVO){
        return resourceApplyService.approve(resourceApproveVO);
    }

    /**
     * 管理员获取未审批的信息列表
     *
     * @return 信息列表
     */
    @GetMapping("/getNoApproveList")
    public Result getNoApproveList(){
        return resourceApplyService.getNoApproveList();
    }

}
