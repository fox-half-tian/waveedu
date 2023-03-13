package com.zhulang.waveedu.basic.vo;

import com.zhulang.waveedu.common.util.RegexUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

/**
 * @author 狐狸半面添
 * @create 2023-03-11 18:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminModifyInfoVO {
    /**
     * 头像
     */
    @Pattern(regexp = RegexUtils.RegexPatterns.IMAGE_REGEX,message = "图片链接格式错误")
    private String icon;
    /**
     * 昵称
     */
    @Length(min = 1,max = 24,message = "昵称设置在1-24字之间")
    private String nickName;
}
