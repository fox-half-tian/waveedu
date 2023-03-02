package com.zhulang.waveedu.edu.query.sectionquery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-02-16 19:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionNameInfoQuery {
    /**
     * 小节id
     */
    private Integer sectionId;
    /**
     * 小节名
     */
    private String sectionName;
}
