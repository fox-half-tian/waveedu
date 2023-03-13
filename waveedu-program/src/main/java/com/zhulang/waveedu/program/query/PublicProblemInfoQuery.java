package com.zhulang.waveedu.program.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-03-13 10:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicProblemInfoQuery {
    /**
     * 问题id
     */
    private Integer problemId;
    /**
     * 问题标题
     */
    private String title;
    /**
     * 作者类型
     */
    private Integer authorType;
    /**
     * 作者id
     */
    private Long authorId;
    /**
     * 难度
     */
    private Integer difficulty;
    /**
     * 问题描述
     */
    private String description;
    /**
     * 作者名称
     */
    private String authorName;
    /**
     * 作者头像
     */
    private String authorIcon;
}
