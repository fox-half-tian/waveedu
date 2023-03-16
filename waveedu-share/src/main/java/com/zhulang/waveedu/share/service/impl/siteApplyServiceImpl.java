package com.zhulang.waveedu.share.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.share.dao.siteApplyMapper;
import com.zhulang.waveedu.share.po.siteApply;
import com.zhulang.waveedu.share.service.siteApplyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 飒沓流星
 * @create 2023/3/12 11:52
 */
@Service
public class siteApplyServiceImpl extends ServiceImpl<siteApplyMapper, siteApply> implements siteApplyService {
    @Resource
    private siteApplyMapper siteApplyMapper;


    @Override
    public Result addSiteApply(siteApply siteApply0) {
        siteApply0.setStatus(0);
        int i = siteApplyMapper.insert(siteApply0);
        LambdaQueryWrapper<siteApply> siteApplyWrapper = new LambdaQueryWrapper<>();
        siteApplyWrapper.eq(siteApply::getSiteId,siteApply0.getSiteId() );
        siteApplyWrapper.eq(siteApply::getIsDeleted,0);
        siteApply s = siteApplyMapper.selectOne(siteApplyWrapper);
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
        siteApply siteApply = siteApplyMapper.selectById(id);
        if(siteApply!=null){
            return Result.ok(siteApply);
        }else{
            return Result.error("传入有误");
        }
    }

    @Override
    public Result modifySiteApplyById(siteApply siteApply) {
        int update = siteApplyMapper.updateById(siteApply);
        if(update>0){
            return Result.ok("修改成功");
        }else {
            return Result.error("传入有误");
        }
    }

    @Override
    public Result getSiteApplyByAdminId() {


        LambdaQueryWrapper<siteApply> siteApplyWrapper = new LambdaQueryWrapper<>();
        siteApplyWrapper.eq(siteApply::getStatus, 0);
        List<siteApply> siteApplyList = siteApplyMapper.selectList(siteApplyWrapper);
        if (siteApplyList == null) {
            return Result.error( HttpStatus.HTTP_NOT_FOUND.getCode(),"未找到管理员需要批准的申请");
        }else{
            return Result.ok(siteApplyList);
        }

    }



}
