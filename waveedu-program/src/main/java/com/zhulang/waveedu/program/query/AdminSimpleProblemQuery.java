package com.zhulang.waveedu.program.query;

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
public class AdminSimpleProblemQuery {
    /**
     * 题目id
     */
    private Integer id;
    /**
     * 题目标题
     */
    private String title;
    /**
     * 是否公开
     */
    private Integer isPublic;
    /**
     * 难度
     */
    private Integer difficulty;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 作者id
     */
    private Long authorId;
    /**
     * 作者昵称
     */
    private String authorName;
}
