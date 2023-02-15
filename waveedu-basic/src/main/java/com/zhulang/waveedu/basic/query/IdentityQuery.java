package com.zhulang.waveedu.basic.query;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 飒沓流星
 * @create 2023/2/7 20:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentityQuery {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 院校id
     */
    private Integer collegeId;
    /**
     * 院校名称
     */
    private String collegeName;
    /**
     * 身份类型，0代表学生，1代表老师
     */
    private Integer type;
    /**
     * 学号/工号，最大16长度
     */
    private String number;
}
