package com.zhulang.waveedu.share.service;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.share.po.Resources;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 资源分析表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-19
 */
public interface ResourceService extends IService<Resources> {

    /**
     * 用户获取自己所有的资源列表
     *
     * @return 资源列表
     */
    Result getSelfResourcesList();

    /**
     * 删除自己的某个资源
     *
     * @param resourceId 资源id
     * @return 删除状况
     */
    Result removeResource(Integer resourceId);


    /**
     * 获取某个资源的信息
     *
     * @param resourceId 资源id
     * @return 资源信息
     */
    Result getResourceInfo(Integer resourceId);

    /**
     * 获取资源信息列表
     *
     * @param resourceId 资源id
     * @return 列表信息
     */
    Result getResourceInfoList(Integer resourceId);
}
