package com.zhulang.waveedu.edu.vo.homeworkvo;

import com.zhulang.waveedu.common.valid.SnowIdValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author 狐狸半面添
 * @create 2023-03-04 23:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkHomeworkVO {
    /**
     * 学生id
     */
    @SnowIdValidate(message = "学生id格式错误")
    private Long stuId;
    /**
     * 教师评价
     */
    @Length(min = 0, max = 512, message = "评价最大字数为512字")
    private String comment;

    @Valid
    @NotNull(message = "未检测到分数数据")
    @Size(min = 1, message = "未检测到分数数据")
    private List<InnerMark> innerMarkList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InnerMark {
        /**
         * 问题id
         */
        @NotNull(message = "问题id不允许为空")
        @Min(value = 1, message = "问题Id格式错误")
        private Integer questionId;
        /**
         * 分数
         */
        @NotNull(message = "分数不允许为空")
        @Min(value = 0, message = "分数最小为0分")
        private Integer score;
    }
}
