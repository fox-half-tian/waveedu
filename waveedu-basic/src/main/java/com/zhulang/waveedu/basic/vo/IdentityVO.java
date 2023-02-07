package com.zhulang.waveedu.basic.vo;

import com.zhulang.waveedu.common.util.RegexUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

/**
 * @author 飒沓流星
 * @create 2023/2/4 16:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentityVO {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 院校id
     */
    private String collegeName;

    /**
     * 身份类型 1为教师/0为学生
     */
    private Integer type;

    /**
     * 用户学号/工号
     */
    @Pattern(regexp = RegexUtils.RegexPatterns.NUMBER_REGEX, message = "最大长度为16")
    private String number;

    /**
     * 是否删除 1删除 0未删除
     */
    private Integer isDeleted;
}
