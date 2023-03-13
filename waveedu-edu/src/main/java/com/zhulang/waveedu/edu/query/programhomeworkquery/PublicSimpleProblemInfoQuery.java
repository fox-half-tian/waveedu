package com.zhulang.waveedu.edu.query.programhomeworkquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-03-12 14:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicSimpleProblemInfoQuery {
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

    /**
     * 作者类型
     */
    private Integer authorType;

    /**
     * 作者id
     */
    private Long authorId;
    /**
     * 作者昵称
     */
    private String authorName;
    /**
     * 作者头像
     */
    private String authorIcon;
}
