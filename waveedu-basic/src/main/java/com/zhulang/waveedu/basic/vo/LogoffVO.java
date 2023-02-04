package com.zhulang.waveedu.basic.vo;

import com.zhulang.waveedu.common.util.RegexUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 注销时的 验证码+原因 封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-04 17:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogoffVO {
    /**
     * 验证码
     */
    @NotBlank(message = "验证码为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.VERIFY_CODE_REGEX, message = "验证码格式错误")
    private String code;
    /**
     * 注销原因，最多255个字符
     */
    @Pattern(regexp = RegexUtils.RegexPatterns.LOGOFF_REASON_REGEX, message = "注销原因最多255字")
    private String reason;
}
