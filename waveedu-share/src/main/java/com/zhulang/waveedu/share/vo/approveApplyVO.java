package com.zhulang.waveedu.share.vo;

import com.zhulang.waveedu.common.valid.SnowIdValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 飒沓流星
 * @create 2023/3/14 13:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class approveApplyVO {
    private Long applyId;
    private Integer approve;

}
