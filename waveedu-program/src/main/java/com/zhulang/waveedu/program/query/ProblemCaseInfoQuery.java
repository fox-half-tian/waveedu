package com.zhulang.waveedu.program.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-03-12 15:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemCaseInfoQuery {
    /**
     * 案例id
     */
    private Integer caseId;
    /**
     * 输入
     */
    private String input;
    /**
     * 输出
     */
    private String output;
}
