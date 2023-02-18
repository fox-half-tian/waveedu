package com.zhulang.waveedu.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhulang.waveedu.basic.po.Logoff;
import com.zhulang.waveedu.basic.query.LogoffRecordOverEndTimeQuery;

/**
 * @author 狐狸半面添
 * @create 2023-01-19 13:58
 */
public interface LogoffMapper extends BaseMapper<Logoff> {
    /**
     * 获取一条超过了截止日期的记录
     *
     * @return 记录：用户id + 电话 + 申请注销的时间 + 截止时间 + 注销原因
     */
    LogoffRecordOverEndTimeQuery getOneOfOverEndTime();
}
