package com.zhulang.waveedu.basic.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhulang.waveedu.common.util.RegexUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

/**
 * @author 狐狸半面添
 * @create 2023-01-31 0:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInfoVO {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户名
     */
    @Pattern(regexp = RegexUtils.RegexPatterns.NAME_REGEX, message = "用户名格式错误，应为2-24位的汉字、字母或数字")
    private String name;
    /**
     * 用户头像
     */
    @Pattern(regexp = RegexUtils.RegexPatterns.IMAGE_REGEX, message = "错误的图片链接")
    private String icon;
    /**
     * 用户个性签名
     */
    @Pattern(regexp = RegexUtils.RegexPatterns.SIGNATURE_REGEX, message = "只允许64字内的签名")
    private String signature;
    /**
     * 性别：男或女
     */
    @Pattern(regexp = RegexUtils.RegexPatterns.SEX_REGEX, message = "性别只允许男或女")
    private String sex;
    /**
     * 出生
     */
    @Past(message = "只允许设置过去的时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate born;
}
