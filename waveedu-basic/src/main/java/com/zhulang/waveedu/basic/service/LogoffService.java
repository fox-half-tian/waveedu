package com.zhulang.waveedu.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhulang.waveedu.basic.po.Logoff;
import com.zhulang.waveedu.basic.query.LogoffRecordOverEndTimeQuery;

/**
 * @author 狐狸半面添
 * @create 2023-01-19 14:00
 */
public interface LogoffService extends IService<Logoff> {
    /**
     * 获取一条超过了截止日期的记录
     *
     * @return 超过了截止日期的记录
     */
    LogoffRecordOverEndTimeQuery getOneOfOverEndTime();
}
