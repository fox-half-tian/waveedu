package com.zhulang.waveedu.share.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.share.dao.siteMapper;
import com.zhulang.waveedu.share.dao.siteTopicMapper;
import com.zhulang.waveedu.share.dao.siteTypeMapper;
import com.zhulang.waveedu.share.po.site;
import com.zhulang.waveedu.share.po.siteTopic;
import com.zhulang.waveedu.share.po.siteType;
import com.zhulang.waveedu.share.service.siteTypeService;
import com.zhulang.waveedu.share.vo.TypeAndSite;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 飒沓流星
 * @create 2023/3/12 11:52
 */
@Service
public class siteTypeServiceImpl extends ServiceImpl<siteTypeMapper, siteType> implements siteTypeService {
    
    @Resource
    private siteTypeMapper siteTypeMapper;
    @Resource
    private com.zhulang.waveedu.share.dao.siteMapper siteMapper;
    @Override
    public Result addSiteType(siteType siteType0) {
        int i = siteTypeMapper.insert(siteType0);
        LambdaQueryWrapper<siteType> siteTypeWrapper = new LambdaQueryWrapper<>();
        siteTypeWrapper.eq(siteType::getName,siteType0.getName() );


        siteType s = siteTypeMapper.selectOne(siteTypeWrapper);
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
        siteType siteType = siteTypeMapper.selectById(id);
        if(siteType!=null){
            return Result.ok(siteType);
        }else{
            return Result.error("传入有误");
        }
    }

    @Override
    public Result modifySiteTypeById(siteType siteType) {
        int update = siteTypeMapper.updateById(siteType);
        if(update>0){
            return Result.ok("修改成功");
        }else {
            return Result.error("传入有误");
        }
    }
    public Result getSiteTypeall() {
        LambdaQueryWrapper<siteType> siteTypeWrapper = new LambdaQueryWrapper<>();
        siteTypeWrapper.gt(siteType::getId,0);
        List<siteType> siteTypes = siteTypeMapper.selectList(siteTypeWrapper);
        return Result.ok(siteTypes);
    }

    @Override
    public Result getSizeByType(Long id) {
        LambdaQueryWrapper<site> siteWrapper = new LambdaQueryWrapper<>();
        siteWrapper.eq(site::getTypeId,id);
        List<site> sites = siteMapper.selectList(siteWrapper);
        return Result.ok(sites);
    }
}
