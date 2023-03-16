package com.zhulang.waveedu.share.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.share.dao.siteApplyMapper;
import com.zhulang.waveedu.share.dao.siteMapper;
import com.zhulang.waveedu.share.dao.siteTopicMapper;
import com.zhulang.waveedu.share.dao.siteTypeMapper;
import com.zhulang.waveedu.share.po.site;
import com.zhulang.waveedu.share.po.siteApply;
import com.zhulang.waveedu.share.po.siteTopic;
import com.zhulang.waveedu.share.po.siteType;
import com.zhulang.waveedu.share.service.siteService;
import com.zhulang.waveedu.share.service.siteTopicService;
import com.zhulang.waveedu.share.vo.TopicAndType;
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
public class siteTopicServiceImpl extends ServiceImpl<siteTopicMapper, siteTopic> implements siteTopicService {
    @Resource
    private siteTopicMapper siteTopicMapper;
    @Resource
    private siteTypeMapper siteTypeMapper;
    @Resource
    private siteMapper siteMapper;
    @Override
    public Result addSiteTopic(siteTopic siteTopic0) {
        int i = siteTopicMapper.insert(siteTopic0);
        LambdaQueryWrapper<siteTopic> siteTopicWrapper = new LambdaQueryWrapper<>();
        siteTopicWrapper.eq(siteTopic::getName,siteTopic0.getName() );


        siteTopic s = siteTopicMapper.selectOne(siteTopicWrapper);
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
        siteTopic siteTopic = siteTopicMapper.selectById(id);
        if(siteTopic!=null){
            return Result.ok(siteTopic);
        }else{
            return Result.error("传入有误");
        }
    }

    @Override
    public Result modifySiteTopicById(siteTopic siteTopic) {

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
        LambdaQueryWrapper<siteTopic> siteTopicWrapper = new LambdaQueryWrapper<>();
        siteTopicWrapper.gt(siteTopic::getId,0);
        List<siteTopic> siteTopics = siteTopicMapper.selectList(siteTopicWrapper);
        for(siteTopic s:siteTopics){
            LambdaQueryWrapper<siteType> siteTypeWrapper = new LambdaQueryWrapper<>();
            siteTypeWrapper.eq(siteType::getTopicId,s.getId());
            List<siteType> siteTypes = siteTypeMapper.selectList(siteTypeWrapper);
            List<String> names=new ArrayList<>();
            for(siteType st:siteTypes){
                names.add(st.getName());
            }
            TopicAndType t=new TopicAndType();
            t.setId(s.getId());
            t.setName(s.getName());
            t.setTypeName(names);
            Rlist.add(t);
        }

        return Result.ok(Rlist);
    }

    @Override
    public Result getTypeByTopicid(Long id) {
        LambdaQueryWrapper<siteType> siteTypeWrapper = new LambdaQueryWrapper<>();
        siteTypeWrapper.eq(siteType::getTopicId,id);
        List<siteType> siteTypes = siteTypeMapper.selectList(siteTypeWrapper);
        return Result.ok(siteTypes);
    }

    @Override
    public Result getTypeAndSiteByTopicid(Long id) {
        List<TypeAndSite> TSL=new ArrayList<>();
        LambdaQueryWrapper<siteType> siteTypeWrapper = new LambdaQueryWrapper<>();
        siteTypeWrapper.eq(siteType::getTopicId,id);
        List<siteType> siteTypes = siteTypeMapper.selectList(siteTypeWrapper);
        for(siteType st:siteTypes){
            TypeAndSite tas=new TypeAndSite();
            tas.setSiteType(st);
            LambdaQueryWrapper<site> siteWrapper = new LambdaQueryWrapper<>();
            siteWrapper.eq(site::getTypeId,st.getId());
            List<site> sites = siteMapper.selectList(siteWrapper);
            tas.setSite(sites);
            TSL.add(tas);
        }

        return Result.ok(TSL);
    }
}
