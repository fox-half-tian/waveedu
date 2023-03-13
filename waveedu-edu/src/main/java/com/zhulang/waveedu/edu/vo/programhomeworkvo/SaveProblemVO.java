package com.zhulang.waveedu.edu.vo.programhomeworkvo;

import com.zhulang.waveedu.common.valid.SnowIdValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 狐狸半面添
 * @create 2023-03-12 22:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveProblemVO {
    /**
     * 作业id
     */
    @NotNull(message = "作业id不允许为空")
    private Integer homeworkId;

    /**
     * 作业标题
     */
    @NotBlank
    @Length(min = 1,max = 255,message = "问题标题最多255字")
    private String title;

}
