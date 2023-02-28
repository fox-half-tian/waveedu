package com.zhulang.waveedu.edu.vo.homework;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-01 1:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishCommonHomeworkVO {
    /**
     * 作业id
     */
    @NotNull(message = "作业id不允许为空")
    @Min(value = 1,message = "作业id格式错误")
    private Integer commonHomeworkId;
    /**
     * 是否定时发布，0表示立即发布，1表示定时发布
     */
    @NotNull(message = "发布状况不允许为空")
    @Range(min = 0,max = 1,message = "发布状况格式错误")
    private Integer isRegularTime;
    /**
     * 定时发布的时间，这个时间不能超过距离当前24天
     */
    @Future(message = "发布时间必须是一个未来的时间")
    private LocalDateTime startTime;
}
