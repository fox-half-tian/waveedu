package com.zhulang.waveedu.judge.entity;

import lombok.Data;

/**
 * @author 狐狸半面添
 * @create 2023-03-14 20:17
 */
@Data
public class JudgeResult {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 错误信息
     */
    private String errMsg;
    /**
     * 运行时间，ms
     */
    private Integer time;
    /**
     * 运行内存，kb
     */
    private Integer memory;
}
