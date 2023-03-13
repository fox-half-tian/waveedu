package com.zhulang.waveedu.edu.vo.commonhomeworkvo;

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
 * @create 2023-03-08 21:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishPlusCommonHomeworkVO {
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

    /**
     * 难度：0表示简单，1表示中等，2表示困难
     */
    @Range(min = 0,max = 2,message = "难度选择格式错误")
    private Integer difficulty;

    /**
     * 截止时间
     */
    @NotNull(message = "请先设置截止时间")
    @Future(message = "截止时间必须是一个未来的时间")
    private LocalDateTime endTime;

    /**
     * 完成作业后是否开启解析，0表示不开启，1表示开启，默认0
     */
    @Range(min = 0,max = 1,message = "解析策略参数格式错误")
    private Integer isCompleteAfterExplain;

    /**
     * 时间截止后是否开启解析，0表示不开启，1表示开启，默认1
     */
    @Range(min = 0,max = 1,message = "解析策略参数格式错误")
    private Integer isEndAfterExplain;
}
