package com.zhulang.waveedu.share.vo;

import com.zhulang.waveedu.share.po.Site;
import com.zhulang.waveedu.share.po.SiteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 飒沓流星
 * @create 2023/3/16 14:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeAndSite {
    private SiteType siteType;
    private List<Site> site;
}
