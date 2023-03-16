package com.zhulang.waveedu.share.controller;

import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.share.po.SiteType;
import com.zhulang.waveedu.share.service.SiteTypeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 飒沓流星
 * @create 2023/3/12 11:22
 */
@RestController
@RequestMapping("/site-type")
public class SiteTypeController {
    @Resource
    private SiteTypeService siteTypeService;

    @PostMapping("/add")
    public Result addSite(@Validated @RequestBody SiteType siteType) {
        return siteTypeService.addSiteType(siteType);

    }
    @PostMapping("/del")
    public Result removeSiteById(@Validated @RequestParam(value = "id") Long id) {
        return siteTypeService.removeSiteTypeById(id);

    }
    @GetMapping("/get")
    public Result getSite(@Validated @RequestParam(value = "id") Long id) {
        return siteTypeService.getSiteTypeById(id);

    }
    @PutMapping("/update")
    public Result updateSite(@Validated @RequestBody SiteType siteType) {
        return siteTypeService.modifySiteTypeById(siteType);

    }
    @GetMapping("/getall")
    public Result getSiteAll() {
        return siteTypeService.getSiteTypeall();
    }
    @GetMapping("/get/site")
    public Result getTypeSite(@RequestParam(value = "id") Long id) {
        return siteTypeService.getSizeByType(id);
    }
}
