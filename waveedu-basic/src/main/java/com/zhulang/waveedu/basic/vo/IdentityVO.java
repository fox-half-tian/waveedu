package com.zhulang.waveedu.basic.vo;

import com.zhulang.waveedu.common.util.RegexUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
     * 院校 name
     */
    @NotBlank(message = "院校名为空")
    private String collegeName;

    /**
     * 教师邀请码
     */
    private String tchCode;

    /**
     * 身份类型 1为教师/0为学生
     */
    @NotNull
    @Range(min = 0,max = 1,message = "type参数有误，应为0或1")
    private Integer type;

    /**
     * 用户学号/工号
     */
    @NotBlank(message = "学号或工号为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.NUMBER_REGEX, message = "最大长度为16")
    private String number;
}
