package com.zhulang.waveedu.share.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.share.dao.SiteMapper;
import com.zhulang.waveedu.share.dao.SiteTypeMapper;
import com.zhulang.waveedu.share.po.Site;
import com.zhulang.waveedu.share.po.SiteType;
import com.zhulang.waveedu.share.service.SiteTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 飒沓流星
 * @create 2023/3/12 11:52
 */
@Service
public class SiteTypeServiceImpl extends ServiceImpl<SiteTypeMapper, SiteType> implements SiteTypeService {
    
    @Resource
    private SiteTypeMapper siteTypeMapper;
    @Resource
    private SiteMapper siteMapper;
    @Override
    public Result addSiteType(SiteType siteType0) {
        int i = siteTypeMapper.insert(siteType0);
        LambdaQueryWrapper<SiteType> siteTypeWrapper = new LambdaQueryWrapper<>();
        siteTypeWrapper.eq(SiteType::getName,siteType0.getName() );


        SiteType s = siteTypeMapper.selectOne(siteTypeWrapper);
        if(i>0){
            return Result.ok(s);
        }else {
            return Result.error("传入有误");
        }
    }

    @Override
    public Result removeSiteTypeById(Long id) {
        int i = siteTypeMapper.deleteById(id);
        if(i>0){
            return Result.ok("删除成功");
        }else {
            return Result.error("传入有误");
        }
    }

    @Override
    public Result getSiteTypeById(Long id) {
        SiteType siteType = siteTypeMapper.selectById(id);
        if(siteType!=null){
            return Result.ok(siteType);
        }else{
            return Result.error("传入有误");
        }
    }

    @Override
    public Result modifySiteTypeById(SiteType siteType) {
        int update = siteTypeMapper.updateById(siteType);
        if(update>0){
            return Result.ok("修改成功");
        }else {
            return Result.error("传入有误");
        }
    }
    public Result getSiteTypeall() {
        LambdaQueryWrapper<SiteType> siteTypeWrapper = new LambdaQueryWrapper<>();
        siteTypeWrapper.gt(SiteType::getId,0);
        List<SiteType> siteTypes = siteTypeMapper.selectList(siteTypeWrapper);
        return Result.ok(siteTypes);
    }

    @Override
    public Result getSizeByType(Long id) {
        LambdaQueryWrapper<Site> siteWrapper = new LambdaQueryWrapper<>();
        siteWrapper.eq(Site::getTypeId,id);
        List<Site> sites = siteMapper.selectList(siteWrapper);
        return Result.ok(sites);
    }
}
