package com.zhulang.waveedu.basic.vo;

import com.zhulang.waveedu.common.util.RegexUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 修改密码时的VO
 *
 * @author 狐狸半面添
 * @create 2023-01-29 22:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePwdVO {
    /**
     * 第一次输入的密码
     */
    @NotBlank(message = "输入密码为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.PASSWORD_REGEX, message = "密码格式错误，应为8-16位的数字或字母")
    private String firPassword;
    /**
     * 第二次输入的密码（确认密码）
     */
    @NotBlank(message = "输入密码为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.PASSWORD_REGEX, message = "密码格式错误，应为8-16位的数字或字母")
    private String secPassword;
    /**
     * 图片验证码字符
     */
    @NotBlank(message = "验证码为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.CAPTCHA_CODE_REGEX, message = "验证码格式错误")
    private String code;
}
