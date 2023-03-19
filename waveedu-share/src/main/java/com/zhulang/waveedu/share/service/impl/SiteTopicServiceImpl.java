package com.zhulang.waveedu.share.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.share.dao.SiteMapper;
import com.zhulang.waveedu.share.dao.SiteTopicMapper;
import com.zhulang.waveedu.share.dao.SiteTypeMapper;
import com.zhulang.waveedu.share.po.Site;
import com.zhulang.waveedu.share.po.SiteTopic;
import com.zhulang.waveedu.share.po.SiteType;
import com.zhulang.waveedu.share.service.SiteTopicService;
import com.zhulang.waveedu.share.vo.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 飒沓流星
 * @create 2023/3/12 11:52
 */
@Service
public class SiteTopicServiceImpl extends ServiceImpl<SiteTopicMapper, SiteTopic> implements SiteTopicService {
    @Resource
    private SiteTopicMapper siteTopicMapper;
    @Resource
    private SiteTypeMapper siteTypeMapper;
    @Resource
    private SiteMapper siteMapper;
    @Override
    public Result addSiteTopic(SiteTopic siteTopic0) {
        int i = siteTopicMapper.insert(siteTopic0);
        LambdaQueryWrapper<SiteTopic> siteTopicWrapper = new LambdaQueryWrapper<>();
        siteTopicWrapper.eq(SiteTopic::getName,siteTopic0.getName() );


        SiteTopic s = siteTopicMapper.selectOne(siteTopicWrapper);
        if(i>0){
            return Result.ok(s);
        }else {
            return Result.error("传入有误");
        }
    }

    @Override
    public Result removeSiteTopicById(Long id) {
        int i = siteTopicMapper.deleteById(id);
        if(i>0){
            return Result.ok("删除成功");
        }else {
            return Result.error("传入有误");
        }
    }

    @Override
    public Result getSiteTopicById(Long id) {
        SiteTopic siteTopic = siteTopicMapper.selectById(id);
        if(siteTopic!=null){
            return Result.ok(siteTopic);
        }else{
            return Result.error("传入有误");
        }
    }

    @Override
    public Result modifySiteTopicById(SiteTopic siteTopic) {

        int update = siteTopicMapper.updateById(siteTopic);
        if(update>0){
            return Result.ok("修改成功");
        }else {
            return Result.error("传入有误");
        }
    }

    @Override
    public Result getSiteTopicall() {
        List<TopicAndType> Rlist=new ArrayList<>();
        LambdaQueryWrapper<SiteTopic> siteTopicWrapper = new LambdaQueryWrapper<>();
        siteTopicWrapper.gt(SiteTopic::getId,0);
        List<SiteTopic> siteTopics = siteTopicMapper.selectList(siteTopicWrapper);
        for(SiteTopic s:siteTopics){
            LambdaQueryWrapper<SiteType> siteTypeWrapper = new LambdaQueryWrapper<>();
            siteTypeWrapper.eq(SiteType::getTopicId,s.getId());
            List<SiteType> siteTypes = siteTypeMapper.selectList(siteTypeWrapper);
            List<TypeNameAndId> nai=new ArrayList<>();
            for(SiteType st:siteTypes){
                TypeNameAndId tni=new TypeNameAndId();
                tni.setId(st.getId());
                tni.setName(st.getName());
                nai.add(tni);
            }
            TopicAndType t=new TopicAndType();
            t.setId(s.getId());
            t.setName(s.getName());
            t.setTypeNameAndId(nai);
            Rlist.add(t);
        }

        return Result.ok(Rlist);
    }

    @Override
    public Result getTypeByTopicid(Long id) {
        LambdaQueryWrapper<SiteType> siteTypeWrapper = new LambdaQueryWrapper<>();
        siteTypeWrapper.eq(SiteType::getTopicId,id);
        List<SiteType> siteTypes = siteTypeMapper.selectList(siteTypeWrapper);
        return Result.ok(siteTypes);
    }

    @Override
    public Result getTypeAndSiteByTopicid(Long id) {
        List<TypeAndSite> TSL=new ArrayList<>();
        LambdaQueryWrapper<SiteType> siteTypeWrapper = new LambdaQueryWrapper<>();
        siteTypeWrapper.eq(SiteType::getTopicId,id);
        List<SiteType> siteTypes = siteTypeMapper.selectList(siteTypeWrapper);
        for(SiteType st:siteTypes){
            TypeAndSite tas=new TypeAndSite();
            tas.setSiteType(st);
            LambdaQueryWrapper<Site> siteWrapper = new LambdaQueryWrapper<>();
            siteWrapper.eq(Site::getTypeId,st.getId());
            List<Site> sites = siteMapper.selectList(siteWrapper);
            tas.setSite(sites);
            TSL.add(tas);
        }

        return Result.ok(TSL);
    }

    @Override
    public Result getAnything() {
        List<TopicAll> Rlist=new ArrayList<>();

        LambdaQueryWrapper<SiteTopic> siteTopicWrapper = new LambdaQueryWrapper<>();
        siteTopicWrapper.gt(SiteTopic::getId,0);
        List<SiteTopic> siteTopics = siteTopicMapper.selectList(siteTopicWrapper);

        for(SiteTopic s:siteTopics){
            TopicAll topicAll=new TopicAll();
            topicAll.setTopic(s);
            LambdaQueryWrapper<SiteType> siteTypeWrapper = new LambdaQueryWrapper<>();
            siteTypeWrapper.eq(SiteType::getTopicId,s.getId());
            List<SiteType> siteTypes = siteTypeMapper.selectList(siteTypeWrapper);

            List<TypeAll> nai=new ArrayList<>();
            for(SiteType st:siteTypes){
                TypeAll tni=new TypeAll();
                tni.setSiteType(st);
                LambdaQueryWrapper<Site> siteWrapper = new LambdaQueryWrapper<>();
                siteWrapper.eq(Site::getTypeId,st.getId());
                List<Site> sites = siteMapper.selectList(siteWrapper);
                tni.setSites(sites);
                nai.add(tni);
            }
            topicAll.setTypes(nai);
            Rlist.add(topicAll);
        }
        return Result.ok(Rlist);
    }
}
