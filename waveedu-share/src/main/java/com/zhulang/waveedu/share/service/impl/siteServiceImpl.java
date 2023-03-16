package com.zhulang.waveedu.share.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.share.dao.siteApplyMapper;
import com.zhulang.waveedu.share.dao.siteMapper;
import com.zhulang.waveedu.share.po.site;
import com.zhulang.waveedu.share.po.siteApply;
import com.zhulang.waveedu.share.service.siteApplyService;
import com.zhulang.waveedu.share.service.siteService;
import org.aspectj.weaver.ast.Var;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.w3c.dom.ranges.Range;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 飒沓流星
 * @create 2023/3/12 11:51
 */
@Service
public class siteServiceImpl extends ServiceImpl<siteMapper, site> implements siteService {
    @Resource
    private siteMapper siteMapper;
    @Resource
    private siteApplyMapper siteApplyMapper;
    Integer s=0;
    @Override
    public Result addSite(site site0) {
        // 统一在修改的时候再排序
        site0.setSort(s);
        s++;
        int i = siteMapper.insert(site0);
        LambdaQueryWrapper<site> siteWrapper = new LambdaQueryWrapper<>();
        siteWrapper.eq(site::getSiteUrl,site0.getSiteUrl() );
        siteWrapper.eq(site::getPictureUrl,site0.getPictureUrl());
        siteWrapper.eq(site::getUserId,site0.getUserId());

        site s = siteMapper.selectOne(siteWrapper);
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
        site site = siteMapper.selectById(id);
        if(site!=null){
            return Result.ok(site);
        }else{
            return Result.error("传入有误");
        }

    }

    @Override
    public Result modifySiteById(site site1) {
        site site0 = siteMapper.selectById(site1.getId());
        Integer sort = site1.getSort();
        if(sort>site0.getSort()){
            LambdaQueryWrapper<site> siteWrapper = new LambdaQueryWrapper<>();
            siteWrapper.le(site::getSort,sort).gt(site::getSort,site0.getSort());
            List<site> siteList = siteMapper.selectList(siteWrapper);
            for(site s:siteList){
                s.setSort(s.getSort()-1);
                siteMapper.updateById(s);
            }
        } else if (sort<site0.getSort()) {
            LambdaQueryWrapper<site> siteWrapper = new LambdaQueryWrapper<>();
            siteWrapper.lt(site::getSort,site0.getSort()).ge(site::getSort,sort);
            List<site> siteList = siteMapper.selectList(siteWrapper);
            for(site s:siteList){
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
        LambdaQueryWrapper<siteApply> siteApplyWrapper = new LambdaQueryWrapper<>();
        siteApplyWrapper.eq(siteApply::getStatus,1);
        List<siteApply> siteApplyList = siteApplyMapper.selectList(siteApplyWrapper);
        List<site> list=new ArrayList<>();
        for(siteApply s:siteApplyList){
            list.add(siteMapper.selectById(s.getSiteId()));
        }
        return Result.ok(list);
    }
}
