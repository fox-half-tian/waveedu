package com.zhulang.waveedu.edu.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查看班级的学生信息的封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-25 17:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassStuInfoQuery {
    /**
     * 学生Id
     */
    private Long stuId;
    /**
     * 学生头像
     */
    private String stuIcon;
    /**
     * 学生姓名
     */
    private String stuName;
    /**
     * 学生身份-院校名
     */
    private String collegeName;
    /**
     * 学生身份-学号或工号
     */
    private String number;
    /**
     * 学生身份-0代表学生，1代表教师
     */
    private Integer type;
    /**
     * 手机号
     */
    private String stuPhone;
}
