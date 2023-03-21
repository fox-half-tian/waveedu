package com.zhulang.waveedu.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.basic.po.College;
import com.zhulang.waveedu.common.entity.Result;

/**
 * @author 狐狸半面添
 * @create 2023-02-03 0:24
 */
public interface CollegeService extends IService<College> {
    /**
     * 模糊查询院校
     *
     * @param name 模糊名
     * @return 院校结果
     */
    Result getCollegesByLike(String name);

    /**
     * 获取当前页数据
     *
     * @param pageNum 页数
     * @param recordNum 记录数
     * @return 数据信息列表
     */
    Result getPageRecords(Integer pageNum, Integer recordNum);

    /**
     * 修改教师邀请码
     *
     * @param collegeId 院校id
     * @return 新的邀请码
     */
    Result modifyTchCode(Integer collegeId);

    /**
     * 通过院校name获取院校的详细信息
     *
     * @param name 院校名
     * @return 详细信息
     */
    Result getCollegeInfo(String name);
}
