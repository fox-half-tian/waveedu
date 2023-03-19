package com.zhulang.waveedu.share.dao;

import com.zhulang.waveedu.share.po.Resources;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.share.query.SelfResourceApplyingQuery;
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
     * 获取自己正在申请中的资源信息列表
     *
     * @param userId 用户id
     * @return 信息列表
     */
    List<SelfResourceApplyingQuery> selectSelfApplyingList(@Param("userId") Long userId);
}
