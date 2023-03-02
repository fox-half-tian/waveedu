package com.zhulang.waveedu.edu.query.chapterquery;

import com.zhulang.waveedu.edu.query.sectionquery.SectionNameInfoQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 获取章节与小节列表的封装类
 *
 * @author 狐狸半面添
 * @create 2023-02-16 19:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChapterNameInfoWithSectionListQuery {
    /**
     * 章节id
     */
    private Integer chapterId;
    /**
     * 章节名
     */
    private String chapterName;
    /**
     * 小节列表：小节id + 小节name
     */
    private List<SectionNameInfoQuery> sectionList;
}
