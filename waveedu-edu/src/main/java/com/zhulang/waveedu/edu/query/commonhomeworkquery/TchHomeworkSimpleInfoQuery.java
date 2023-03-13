package com.zhulang.waveedu.edu.query.commonhomeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author 狐狸半面添
 * @create 2023-03-02 10:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TchHomeworkSimpleInfoQuery {
    /**
     * 作业id
     */
    private Integer id;
    /**
     * 作业标题
     */
    private String title;
    /**
     * 作业状态：0-未发布，1-已发布，2-预发布，3-已截止
     */
    private Integer status;
}
