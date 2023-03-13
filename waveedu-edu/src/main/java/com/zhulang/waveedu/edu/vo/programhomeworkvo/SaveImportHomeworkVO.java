package com.zhulang.waveedu.edu.vo.programhomeworkvo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author 狐狸半面添
 * @create 2023-03-13 17:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveImportHomeworkVO {
    @NotNull(message = "作业id不允许为空")
    @Min(value = 1000,message = "作业id格式错误")
    private Integer homeworkId;

    @NotNull(message = "问题id不允许为空")
    @Size(min = 1,message = "至少导入一个问题")
    private List<Integer> problemIds;
}
