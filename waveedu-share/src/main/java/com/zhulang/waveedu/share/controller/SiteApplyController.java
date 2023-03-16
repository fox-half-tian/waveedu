package com.zhulang.waveedu.share.controller;

import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.share.po.Site;
import com.zhulang.waveedu.share.po.SiteApply;
import com.zhulang.waveedu.share.service.SiteApplyService;
import com.zhulang.waveedu.share.service.SiteService;
import com.zhulang.waveedu.share.vo.ApproveApplyVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 飒沓流星
 * @create 2023/3/12 11:22
 */
@RestController
@RequestMapping("/site/apply")
public class SiteApplyController {
    @Resource
    private SiteApplyService siteApplyService;

    @Resource
    private SiteService siteService;

    @PostMapping("/add")
    public Result addSite(@Validated @RequestBody SiteApply siteApply) {
        return siteApplyService.addSiteApply(siteApply);

    }
    @PostMapping("/del")
    public Result removeSiteById(@Validated @RequestParam(value = "id")Long id) {
        return siteApplyService.removeSiteApplyById(id);

    }
    @GetMapping("/get")
    public Result getSite(@Validated @RequestParam(value = "id") Long id) {
        return siteApplyService.getSiteApplyById(id);

    }
    @PutMapping("/update")
    public Result updateSite(@Validated @RequestBody SiteApply siteApply) {
        return siteApplyService.modifySiteApplyById(siteApply);
    }
    @PostMapping("/approve")
    public Result approveSiteApply(@Validated @RequestBody ApproveApplyVO approveApplyVO) {
        SiteApply sa = siteApplyService.getById(approveApplyVO.getApplyId());
        if(sa==null){
            return Result.error(HttpStatus.HTTP_NOT_FOUND.getCode(),"未找到该申请");
        }
        Site st = siteService.getById(sa.getSiteId());
        if(approveApplyVO.getApprove()==1){
            // 通过
            sa.setStatus(1);
            return siteApplyService.modifySiteApplyById(sa);
        }else{
            // 拒绝
            sa.setStatus(2);
            return siteApplyService.modifySiteApplyById(sa);
        }
    }
    @GetMapping("/get/approve")
    public Result getSiteApplyByAdminId(){
        return siteApplyService.getSiteApplyByAdminId();
    }
}
