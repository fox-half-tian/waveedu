package com.zhulang.waveedu.edu.vo.programhomeworkvo;

import com.zhulang.waveedu.common.valid.SnowIdValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-12 22:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyProgramHomeworkVO {
    /**
     * 作业id
     */
    @NotNull(message = "作业id不允许为空")
    private Integer homeworkId;

    /**
     * 作业标题
     */
    @Length(min = 1,max = 64,message = "作业标题最多64字")
    private String title;

    /**
     * 截止时间
     */
    @Future(message = "截止时间必须是一个未来的时间")
    private LocalDateTime endTime;

}
