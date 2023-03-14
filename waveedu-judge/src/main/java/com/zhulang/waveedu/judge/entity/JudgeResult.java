package com.zhulang.waveedu.judge.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author 狐狸半面添
 * @create 2023-03-14 20:17
 */
@Data
public class JudgeResult {
    private Integer code;
    private String errMsg;
    private Integer time;
    private Integer memory;
}
