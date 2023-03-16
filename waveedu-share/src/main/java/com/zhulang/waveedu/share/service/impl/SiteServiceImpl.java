package com.zhulang.waveedu.share.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.share.dao.SiteApplyMapper;
import com.zhulang.waveedu.share.dao.SiteMapper;
import com.zhulang.waveedu.share.po.Site;
import com.zhulang.waveedu.share.po.SiteApply;
import com.zhulang.waveedu.share.service.SiteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 飒沓流星
 * @create 2023/3/12 11:51
 */
@Service
public class SiteServiceImpl extends ServiceImpl<SiteMapper, Site> implements SiteService {
    @Resource
    private SiteMapper siteMapper;
    @Resource
    private SiteApplyMapper siteApplyMapper;
    Integer s=0;
    @Override
    public Result addSite(Site site0) {
        // 统一在修改的时候再排序
        site0.setSort(s);
        s++;
        int i = siteMapper.insert(site0);
        LambdaQueryWrapper<Site> siteWrapper = new LambdaQueryWrapper<>();
        siteWrapper.eq(Site::getSiteUrl,site0.getSiteUrl() );
        siteWrapper.eq(Site::getPictureUrl,site0.getPictureUrl());
        siteWrapper.eq(Site::getUserId,site0.getUserId());

        Site s = siteMapper.selectOne(siteWrapper);
        if(i>0){
            return Result.ok(s);
        }else {
            return Result.error("传入有误");
        }
    }

    @Override
    public Result removeSiteById(Long id) {
        int i = siteMapper.deleteById(id);
        if(i>0){
            return Result.ok("删除成功");
        }else {
            return Result.error("传入有误");
        }
    }

    @Override
    public Result getSiteById(Long id) {
        Site site = siteMapper.selectById(id);
        if(site!=null){
            return Result.ok(site);
        }else{
            return Result.error("传入有误");
        }

    }

    @Override
    public Result modifySiteById(Site site1) {
        Site site0 = siteMapper.selectById(site1.getId());
        Integer sort = site1.getSort();
        if(sort>site0.getSort()){
            LambdaQueryWrapper<Site> siteWrapper = new LambdaQueryWrapper<>();
            siteWrapper.le(Site::getSort,sort).gt(Site::getSort,site0.getSort());
            List<Site> siteList = siteMapper.selectList(siteWrapper);
            for(Site s:siteList){
                s.setSort(s.getSort()-1);
                siteMapper.updateById(s);
            }
        } else if (sort<site0.getSort()) {
            LambdaQueryWrapper<Site> siteWrapper = new LambdaQueryWrapper<>();
            siteWrapper.lt(Site::getSort,site0.getSort()).ge(Site::getSort,sort);
            List<Site> siteList = siteMapper.selectList(siteWrapper);
            for(Site s:siteList){
                s.setSort(s.getSort()+1);
                siteMapper.updateById(s);
            }
        }
        int update = siteMapper.updateById(site1);
        if(update>0){
            return Result.ok("修改成功");
        }else {
            return Result.error("传入有误");
        }

    }

    @Override
    public Result getSiteApproved() {
        LambdaQueryWrapper<SiteApply> siteApplyWrapper = new LambdaQueryWrapper<>();
        siteApplyWrapper.eq(SiteApply::getStatus,1);
        List<SiteApply> siteApplyList = siteApplyMapper.selectList(siteApplyWrapper);
        List<Site> list=new ArrayList<>();
        for(SiteApply s:siteApplyList){
            list.add(siteMapper.selectById(s.getSiteId()));
        }
        return Result.ok(list);
    }
}
