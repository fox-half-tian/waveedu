package com.zhulang.waveedu.share.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.share.dao.SiteMapper;
import com.zhulang.waveedu.share.po.Site;
import com.zhulang.waveedu.share.po.SiteApply;
import com.zhulang.waveedu.share.service.SiteApplyService;
import com.zhulang.waveedu.share.service.SiteService;
import com.zhulang.waveedu.share.vo.ApplySiteVo;
import com.zhulang.waveedu.share.vo.ApproveApplyVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 飒沓流星
 * @create 2023/3/12 11:22
 */
@RestController
@RequestMapping("/site-apply")
public class SiteApplyController {
    @Resource
    private SiteApplyService siteApplyService;

    @Resource
    private SiteService siteService;
    @Resource
    private SiteMapper siteMapper;
    private Integer s=50;
    @PostMapping("/add")
    public Result addSite(@Validated @RequestBody ApplySiteVo applySiteVo) {
        return siteApplyService.addSiteApply(applySiteVo);

    }
    @DeleteMapping("/del")
    public Result removeSiteById(@RequestParam(value = "id")Long id) {
        return siteApplyService.removeSiteApplyById(id);

    }
    @GetMapping("/get")
    public Result getSite(@RequestParam(value = "id") Long id) {
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
        if(approveApplyVO.getApprove()==1){
            // 通过
            sa.setStatus(1);
            sa.setAdminId(UserHolderUtils.getUserId());
            Site site0=new Site();
            site0.setSiteUrl(sa.getSiteUrl());
            site0.setPictureUrl(sa.getPictureUrl());
            site0.setName(sa.getName());
            site0.setTypeId(sa.getTypeId());
            site0.setIntroduce(sa.getIntroduce());
            site0.setUserId(sa.getUserId());
            site0.setIdentity(0);
            site0.setSort(s++);
            int i = siteMapper.insert(site0);
            siteApplyService.modifySiteApplyById(sa);
            LambdaQueryWrapper<Site> siteWrapper = new LambdaQueryWrapper<>();
            siteWrapper.eq(Site::getSiteUrl,site0.getSiteUrl() );
            siteWrapper.eq(Site::getPictureUrl,site0.getPictureUrl());
            siteWrapper.eq(Site::getUserId,site0.getUserId());

            Site s = siteMapper.selectOne(siteWrapper);
            return Result.ok(s);
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

    @GetMapping("/get/self")
    public Result getSite() {
        Long userId = UserHolderUtils.getUserId();
        return siteApplyService.getSiteApplyByUserId(userId);
    }
}
