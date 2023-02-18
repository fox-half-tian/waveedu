package com.zhulang.waveedu.basic.component;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zhulang.waveedu.basic.po.Logoff;
import com.zhulang.waveedu.basic.po.LogoffInfo;
import com.zhulang.waveedu.basic.query.LogoffRecordOverEndTimeQuery;
import com.zhulang.waveedu.basic.service.LogoffInfoService;
import com.zhulang.waveedu.basic.service.LogoffService;
import com.zhulang.waveedu.basic.service.UserService;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author 狐狸半面添
 * @create 2023-02-18 22:21
 */
@Component
public class XxlJobScheduledTasks {
    @Resource
    private LogoffService logoffService;
    @Resource
    private LogoffInfoService logoffInfoService;
    @Resource
    private UserService userService;
    @Resource
    private XxlJobScheduledTasks proxy = (XxlJobScheduledTasks)AopContext.currentProxy();

    /**
     * 检查注销后可恢复的截止期限
     * 如果超过了截止日期，则将信息放到`basic_logoff_info`表中
     * 规定每隔1小时执行一次该任务
     */
    @XxlJob("demoJobHandler")
    public void logoffCheckExpire() {
        while (true) {
            // 1.每次查询一条超过了截止时间的记录
            LogoffRecordOverEndTimeQuery query = logoffService.getOneOfOverEndTime();
            // 2.如果查询不到就返回
            if (query == null) {
                return;
            }

            // 3.如果查询到了，将注销用户信息放入到 `basic_logoff_info` 中，并修改user表的记录状态（逻辑删除）
            proxy.saveLogoffInfoAndUpdateUserStatus(query);
        }
    }


    /**
     * 将注销用户信息放入到 `basic_logoff_info` 中，并修改user表的记录状态（逻辑删除）
     *
     * @param query 注销用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveLogoffInfoAndUpdateUserStatus(LogoffRecordOverEndTimeQuery query){
        // 1.转换
        LogoffInfo logoffInfo = BeanUtil.copyProperties(query, LogoffInfo.class);
        // 2.保存记录到 `basic_logoff_info` 中
        logoffInfoService.save(logoffInfo);
        // 3.修改 user表的用户记录状态->逻辑删除
        userService.removeById(query.getUserId());
    }

}
