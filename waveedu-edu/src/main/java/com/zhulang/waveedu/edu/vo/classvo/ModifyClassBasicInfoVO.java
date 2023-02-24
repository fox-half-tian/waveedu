package com.zhulang.waveedu.edu.vo.classvo;

import com.zhulang.waveedu.common.util.RegexUtils;
import com.zhulang.waveedu.common.valid.SnowIdValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 修改班级基本信息的视图类
 *
 * @author 狐狸半面添
 * @create 2023-02-24 21:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyClassBasicInfoVO {
    /**
     * 班级名，不超过24字
     */
    @Pattern(regexp = RegexUtils.RegexPatterns.LESSON_CLASS_NAME_REGEX,message = "班级名不超过24字")
    private String name;

    /**
     * 班级封面
     */
    @Pattern(regexp = RegexUtils.RegexPatterns.IMAGE_REGEX,message = "图片链接错误")
    private String cover;

    /**
     * 是否开启结课
     */
    @Range(min = 0,max = 1,message = "参数错误")
    private Integer isEndClass;

    /**
     * 是否禁止加入班级
     */
    @Range(min = 0,max = 1,message = "参数错误")
    private Integer isForbidJoin;

    /**
     * 班级id
     */
    @SnowIdValidate
    private Long id;
}
