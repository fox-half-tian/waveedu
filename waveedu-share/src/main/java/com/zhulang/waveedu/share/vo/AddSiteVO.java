package com.zhulang.waveedu.share.vo;

import com.zhulang.waveedu.common.util.RegexUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

/**
 * @author 飒沓流星
 * @create 2023/3/16 21:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddSiteVO {

    private String name;

    private String introduce;

    private Long typeId;

    @Pattern(regexp = RegexUtils.RegexPatterns.IMAGE_REGEX,message = "图片链接有误")
    private String pictureUrl;

    private String siteUrl;
}
