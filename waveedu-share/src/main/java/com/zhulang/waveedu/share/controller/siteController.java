package com.zhulang.waveedu.share.controller;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.common.util.UserHolderUtils;
import com.zhulang.waveedu.share.po.site;
import com.zhulang.waveedu.share.service.siteService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 飒沓流星
 * @create 2023/3/12 11:21
 */
@RestController
@RequestMapping("/site")
public class siteController {
    @Resource
    private siteService siteService;

    @PostMapping("/add")
    public Result addSite(@Validated @RequestBody site site) {
        return siteService.addSite(site);

    }
    @PostMapping("/del")
    public Result removeSiteById(@Validated @RequestParam(value = "id") Long id) {
        return siteService.removeSiteById(id);

    }
    @GetMapping("/get/id")
    public Result getSite(@Validated @RequestParam(value = "id") Long id) {
        return siteService.getSiteById(id);

    }
    @PutMapping("/update")
    public Result updateSite(@Validated @RequestBody site site) {
        return siteService.modifySiteById(site);
    }
    @GetMapping("/get/approved")
    public Result getSiteapproved() {
        return siteService.getSiteApproved();
    }

}
