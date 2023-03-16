package com.zhulang.waveedu.program.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-03-12 1:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemIdAndAuthorIdAndAuthorTypeQuery {
    /**
     * 问题id
     */
    private Integer problemId;
    /**
     * 作者id
     */
    private Long authorId;
    /**
     * 作者身份
     */
    private Integer authorType;
}
