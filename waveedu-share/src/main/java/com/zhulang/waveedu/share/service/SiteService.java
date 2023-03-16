package com.zhulang.waveedu.share.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.share.po.Site;

/**
 * @author 飒沓流星
 * @create 2023/3/12 11:29
 */
public interface SiteService extends IService<Site> {
    /**
     * 添加站点申请
     * @return 添加结果
     */
    Result addSite(Site site);

    /**
     * 通过id删站点申请
     * @return 删除结果
     */
    Result removeSiteById(Long id);

    /**
     * 通过id查询站点申请
     * @param id
     * @return 查询结果
     */
    Result getSiteById(Long id);

    /**
     * 修改站点申请
     * @param site
     * @return 修改结果
     */
    Result modifySiteById(Site site);

    Result getSiteApproved();
}
