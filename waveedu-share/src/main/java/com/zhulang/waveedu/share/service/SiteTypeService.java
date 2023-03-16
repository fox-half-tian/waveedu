package com.zhulang.waveedu.share.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.share.po.SiteType;

/**
 * @author 飒沓流星
 * @create 2023/3/12 11:30
 */
public interface SiteTypeService extends IService<SiteType> {
    /**
     * 添加站点分类
     * @return 添加结果
     */
    Result addSiteType(SiteType siteType);

    /**
     * 通过id删站点分类
     * @return 删除结果
     */
    Result removeSiteTypeById(Long id);

    /**
     * 通过id查询站点分类
     * @param id
     * @return 查询结果
     */
    Result getSiteTypeById(Long id);

    /**
     * 修改站点分类
     * @param siteType
     * @return 修改结果
     */
    Result modifySiteTypeById(SiteType siteType);

    Result getSiteTypeall();

    Result getSizeByType(Long id);
}
