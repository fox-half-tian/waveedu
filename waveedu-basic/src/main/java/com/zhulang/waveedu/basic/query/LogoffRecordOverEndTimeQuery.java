package com.zhulang.waveedu.basic.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 超过注销恢复截止日期的封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-18 23:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogoffRecordOverEndTimeQuery {
    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 电话
     */
    private String phone;
    /**
     * 注销时间
     */
    private LocalDateTime logoffTime;
    /**
     * 截止时间
     */
    private LocalDateTime endTime;
    /**
     * 注销原因
     */
    private String reason;
}
