package com.zhulang.waveedu.edu.query.commonhomeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-03-04 17:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkNoCommitStuInfoQuery {
    /**
     * 学生姓名
     */
    private String stuName;
    /**
     * 学生id
     */
    private String stuId;
}
