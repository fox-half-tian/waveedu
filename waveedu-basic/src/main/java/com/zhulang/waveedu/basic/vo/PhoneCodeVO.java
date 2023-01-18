package com.zhulang.waveedu.basic.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
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
    @Pattern(regexp = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$", message = "非法手机号")
    private String phone;
    /**
     * 验证码
     */
    @NotBlank(message = "验证码为空")
    @Pattern(regexp = "^\\d{6}$", message = "非法验证码")
    private String code;
}