package com.zhulang.waveedu.basic.vo;

import com.zhulang.waveedu.common.util.RegexUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 接收手机号+密码的登录对象
 *
 * @author 狐狸半面添
 * @create 2023-01-19 21:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhonePasswordVO {
    /**
     * 手机号
     */
    @NotBlank(message = "手机号为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.PHONE_REGEX, message = "手机号或密码错误")
    private String phone;
    /**
     * 密码
     */
    @NotBlank(message = "密码为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.PASSWORD_REGEX, message = "手机号或密码错误")
    private String password;
}
