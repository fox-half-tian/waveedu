package com.zhulang.waveedu.share.controller;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.share.po.Site;
import com.zhulang.waveedu.share.service.SiteService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 飒沓流星
 * @create 2023/3/12 11:21
 */
@RestController
@RequestMapping("/site")
public class SiteController {
    @Resource
    private SiteService siteService;

    @PostMapping("/add")
    public Result addSite(@Validated @RequestBody Site site) {
        return siteService.addSite(site);
    }

    @PostMapping("/del")
    public Result removeSiteById(@RequestParam(value = "id") Long id) {
        return siteService.removeSiteById(id);

    }

    @GetMapping("/get/id")
    public Result getSite(@Validated @RequestParam(value = "id") Long id) {
        return siteService.getSiteById(id);

    }
    @PutMapping("/update")
    public Result updateSite(@Validated @RequestBody Site site) {
        return siteService.modifySiteById(site);
    }

    @GetMapping("/get/approved")
    public Result getSiteApproved() {
        return siteService.getSiteApproved();
    }

}
