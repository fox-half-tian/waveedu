package com.zhulang.waveedu.judge.dto;

import com.zhulang.waveedu.judge.entity.judge.Judge;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: Himit_ZH
 * @Date: 2021/2/4 22:29
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ToJudgeDTO implements Serializable {

    private static final long serialVersionUID = 999L;

    /**
     * 问题id
     */
    private Integer problemId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 语言
     */
    private String language;

    /**
     * 代码
     */
    private String code;


}