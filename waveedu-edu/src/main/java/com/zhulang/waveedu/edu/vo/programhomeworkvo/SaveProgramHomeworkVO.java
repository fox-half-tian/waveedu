package com.zhulang.waveedu.edu.vo.programhomeworkvo;

import com.zhulang.waveedu.common.valid.SnowIdValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author 狐狸半面添
 * @create 2023-03-12 22:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveProgramHomeworkVO {
    /**
     * 班级id
     */
    @SnowIdValidate(message = "班级id格式错误")
    private Long classId;

    /**
     * 作业标题
     */
    @NotBlank
    @Length(min = 1,max = 64,message = "作业标题最多64字")
    private String title;

}
