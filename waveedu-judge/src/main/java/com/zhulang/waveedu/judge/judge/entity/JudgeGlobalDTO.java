package com.zhulang.waveedu.judge.judge.entity;

import cn.hutool.json.JSONObject;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: Himit_ZH
 * @Date: 2022/1/3 11:53
 * @Description: 一次评测全局通用的传输实体类
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
public class JudgeGlobalDTO implements Serializable {

    private static final long serialVersionUID = 888L;

    /**
     * 当前评测题目的id
     */
    private Integer problemId;

    /**
     * 用户程序在沙盒编译后对应内存文件的id，运行时需要传入
     */
    private String userFileId;

    /**
     * 用户程序代码文件的内容
     */
    private String userFileContent;

    /**
     * 判题沙盒评测程序的最大实际时间，一般为题目最大限制时间+200ms
     */
    private Long testTime;

    /**
     * 当前题目评测的最大时间限制 ms
     */
    private Long maxTime;

    /**
     * 当前题目评测的最大空间限制 mb
     */
    private Long maxMemory;

    /**
     * 当前题目评测的最大栈空间限制 mb
     */
    private Integer maxStack;

    /**
     * 评测数据json内容
     */
    private JSONObject testCaseInfo;

    /**
     * 普通评测的命令配置
     */
    private LanguageConfig runConfig;


    /**
     * 是否需要自动移除评测数据的行末空格
     */
    private Boolean removeEolBlank;
}