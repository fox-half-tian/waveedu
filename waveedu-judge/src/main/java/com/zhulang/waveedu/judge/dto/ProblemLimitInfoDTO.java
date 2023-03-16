package com.zhulang.waveedu.judge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-03-13 23:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemLimitInfoDTO {
    /**
     * 问题id
     */
    private Integer id;
    /**
     * 时间限制，单位ms
     */
    private Integer timeLimit;

    /**
     * 内存限制，单位mb
     */
    private Integer memoryLimit;

    /**
     * 栈限制，单位mb
     */
    private Integer stackLimit;
}
