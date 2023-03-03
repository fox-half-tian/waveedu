package com.zhulang.waveedu.edu.query.homeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-03-04 2:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkIdAndTypeQuery {
    /**
     * 问题id
     */
    private Integer homeworkId;
    /**
     * 问题类型
     */
    private Integer homeworkType;
}
