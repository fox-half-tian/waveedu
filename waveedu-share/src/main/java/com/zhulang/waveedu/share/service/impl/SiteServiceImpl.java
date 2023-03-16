package com.zhulang.waveedu.share.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.share.dao.SiteApplyMapper;
import com.zhulang.waveedu.share.dao.SiteMapper;
import com.zhulang.waveedu.share.po.Site;
import com.zhulang.waveedu.share.po.SiteApply;
import com.zhulang.waveedu.share.po.SiteTopic;
import com.zhulang.waveedu.share.service.SiteService;
import com.zhulang.waveedu.share.vo.AddSiteVO;
import com.zhulang.waveedu.share.vo.TopicAndType;
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
    Integer s=101;
    @Override
    public Result addSite(AddSiteVO addSiteVO) {
        // 统一在修改的时候再排序
        Site site0=new Site();
        site0.setSort(s);
        s++;
        site0.setSiteUrl(addSiteVO.getSiteUrl());
        site0.setPictureUrl(addSiteVO.getPictureUrl());
        site0.setName(addSiteVO.getName());
        site0.setTypeId(addSiteVO.getTypeId());
        site0.setIntroduce(addSiteVO.getIntroduce());
        site0.setUserId(UserHolderUtils.getUserId());
        // 管理员加的
        site0.setIdentity(1);
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
    public Result getSiteall() {
        LambdaQueryWrapper<Site> siteWrapper = new LambdaQueryWrapper<>();
        siteWrapper.gt(Site::getId,0);
        List<Site> list = siteMapper.selectList(siteWrapper);
        return Result.ok(list);
    }
}
