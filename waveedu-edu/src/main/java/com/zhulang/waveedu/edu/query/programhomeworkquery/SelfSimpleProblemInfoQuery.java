package com.zhulang.waveedu.edu.query.programhomeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 狐狸半面添
 * @create 2023-03-12 14:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelfSimpleProblemInfoQuery {
    /**
     * 题目id
     */
    private Integer id;
    /**
     * 题目标题
     */
    private String title;

    /**
     * 难度
     */
    private Integer difficulty;

    /**
     * 题目描述
     */
    private String description;
}
