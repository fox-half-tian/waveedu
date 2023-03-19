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
}
