package com.zhulang.waveedu.share.dao;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.share.po.Resources;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.share.query.ResourceShowInfoQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资源分析表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-19
 */
public interface ResourceMapper extends BaseMapper<Resources> {

    /**
     * 获取某个资源的详细信息
     *
     * @param id 资源id
     * @return 信息
     */
    ResourceShowInfoQuery selectResourceInfo(@Param("id") Integer id);

    /**
     * 获取资源信息列表
     *
     * @param id 资源id
     * @return 列表信息
     */
    List<ResourceShowInfoQuery> selectResourceInfoList(@Param("id") Integer id,@Param("limitNum")Integer limitNum);
}
