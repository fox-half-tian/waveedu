package com.zhulang.waveedu.share.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.share.dao.SiteApplyMapper;
import com.zhulang.waveedu.share.po.SiteApply;
import com.zhulang.waveedu.share.service.SiteApplyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 飒沓流星
 * @create 2023/3/12 11:52
 */
@Service
public class SiteApplyServiceImpl extends ServiceImpl<SiteApplyMapper, SiteApply> implements SiteApplyService {
    @Resource
    private SiteApplyMapper siteApplyMapper;


    @Override
    public Result addSiteApply(SiteApply siteApply0) {
        siteApply0.setStatus(0);
        int i = siteApplyMapper.insert(siteApply0);
        LambdaQueryWrapper<SiteApply> siteApplyWrapper = new LambdaQueryWrapper<>();
        siteApplyWrapper.eq(SiteApply::getSiteId,siteApply0.getSiteId() );
        siteApplyWrapper.eq(SiteApply::getIsDeleted,0);
        SiteApply s = siteApplyMapper.selectOne(siteApplyWrapper);
        if(i>0){
            return Result.ok(s);
        }else {
            return Result.error("传入有误");
        }
    }

    @Override
    public Result removeSiteApplyById(Long id) {
        int i = siteApplyMapper.deleteById(id);
        if(i>0){
            return Result.ok("删除成功");
        }else {
            return Result.error("传入有误");
        }
    }

    @Override
    public Result getSiteApplyById(Long id) {
        SiteApply siteApply = siteApplyMapper.selectById(id);
        if(siteApply!=null){
            return Result.ok(siteApply);
        }else{
            return Result.error("传入有误");
        }
    }

    @Override
    public Result modifySiteApplyById(SiteApply siteApply) {
        int update = siteApplyMapper.updateById(siteApply);
        if(update>0){
            return Result.ok("修改成功");
        }else {
            return Result.error("传入有误");
        }
    }

    @Override
    public Result getSiteApplyByAdminId() {


        LambdaQueryWrapper<SiteApply> siteApplyWrapper = new LambdaQueryWrapper<>();
        siteApplyWrapper.eq(SiteApply::getStatus, 0);
        List<SiteApply> siteApplyList = siteApplyMapper.selectList(siteApplyWrapper);
        if (siteApplyList == null) {
            return Result.error( HttpStatus.HTTP_NOT_FOUND.getCode(),"未找到管理员需要批准的申请");
        }else{
            return Result.ok(siteApplyList);
        }

    }



}
