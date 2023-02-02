package com.zhulang.waveedu.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.basic.po.College;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 狐狸半面添
 * @create 2023-02-02 3:16
 */
public interface CollegeMapper extends BaseMapper<College> {
    /**
     * 右模糊查询 name
     *
     * @param name 模糊名
     * @return 所有右模糊匹配的大学名称
     */
    List<String> selectNamesLikeName(@Param("name")String name);
}
