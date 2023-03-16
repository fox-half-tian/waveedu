package com.zhulang.waveedu.share.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.share.po.SiteApply;
import com.zhulang.waveedu.share.vo.ApplySiteVo;

/**
 * @author 飒沓流星
 * @create 2023/3/12 11:29
 */

public interface SiteApplyService extends IService<SiteApply> {
    /**
     * 添加站点
     * @return 添加结果
     */
    Result addSiteApply(ApplySiteVo applySiteVo);

    /**
     * 通过id删站点
     * @return 删除结果
     */
    Result removeSiteApplyById(Long id);

    /**
     * 通过id查询站点
     * @param id
     * @return 查询结果
     */
    Result getSiteApplyById(Long id);

    /**
     * 修改站点
     * @param siteApply
     * @return 修改结果
     */
    Result modifySiteApplyById(SiteApply siteApply);

    Result getSiteApplyByAdminId();

    //Result applySiteApplyByAdminId(approveApplyVO approveApplyVO);

}
