package com.zhulang.waveedu.share.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author 狐狸半面添
 * @create 2023-03-19 12:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceApproveVO {
    /**
     * 申请id
     */
    @NotNull(message = "申请id不允许为空")
    private Integer id;
    /**
     * 是否审批通过
     */
    @NotNull(message = "审批状态不允许为空")
    @Range(min = 1,max = 2,message = "审批状态格式错误")
    private Integer status;

    /**
     * 备注
     */
    @Length(max = 255,message = "备注最多255字")
    private String mark;
}
