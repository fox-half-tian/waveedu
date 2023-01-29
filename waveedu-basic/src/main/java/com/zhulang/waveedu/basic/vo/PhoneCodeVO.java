package com.zhulang.waveedu.basic.vo;

import com.zhulang.waveedu.common.util.RegexUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 接收手机号+验证码的登录对象
 *
 * @author 狐狸半面添
 * @create 2023-01-18 17:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneCodeVO {
    /**
     * 手机号
     */
    @NotBlank(message = "手机号为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.PHONE_REGEX, message = "手机号格式错误")
    private String phone;
    /**
     * 验证码
     */
    @NotBlank(message = "验证码为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.VERIFY_CODE_REGEX, message = "验证码格式错误")
    private String code;
}
