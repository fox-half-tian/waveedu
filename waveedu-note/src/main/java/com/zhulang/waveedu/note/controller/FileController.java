package com.zhulang.waveedu.note.controller;


import com.alibaba.fastjson.JSONObject;
import com.zhulang.waveedu.common.constant.HttpStatus;
import com.zhulang.waveedu.common.entity.Result;
import com.zhulang.waveedu.note.service.FileService;
import com.zhulang.waveedu.note.vo.SaveDirVO;
import com.zhulang.waveedu.note.vo.SaveFileVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 笔记的文件表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-03-05
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Resource
    private FileService fileService;

    /**
     * 保存&创建文件
     *
     * @param saveFileVO 文件信息：父目录，文件名，文件类型
     * @return 文件Id
     */
    @PostMapping("/saveFile")
    public Result saveFile(@Validated @RequestBody SaveFileVO saveFileVO) {
        return fileService.saveFile(saveFileVO);
    }

    /**
     * 创建目录
     *
     * @param saveDirVO 目录信息：父目录 + 目录名
     * @return 目录id
     */
    @PostMapping("/saveDir")
    public Result saveDir(@Validated @RequestBody SaveDirVO saveDirVO) {
        return fileService.saveDir(saveDirVO);
    }

    /**
     * 修改文件名（目录名）
     *
     * @param fileId   文件id
     * @param fileName 文件名
     * @return 修改状况
     */
    @PutMapping("/modifyName")
    public Result modifyName(@RequestParam("fileId") Integer fileId,
                             @RequestParam("fileName") String fileName) {
        return fileService.modifyName(fileId, fileName);
    }

    /**
     * 删除文件或目录
     *
     * @param object 文件id
     * @return 删除状况
     */
    @DeleteMapping("/remove")
    public Result remove(@RequestBody JSONObject object) {
        try {
            return fileService.remove(Integer.parseInt(object.getString("fileId")));
        } catch (NumberFormatException e) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "文件id格式错误");
        }
    }

    /**
     * 获取该id文件夹下的文件列表信息
     *
     * @param parentId 父id
     * @return 列表信息：文件名 + 是否为目录 + 类型 + id
     */
    @GetMapping("/getListByParentId")
    public Result getListByParentId(@RequestParam("parentId")Integer parentId){
        return fileService.getListByParentId(parentId);
    }

    /**
     * 获取该文件id所在目录下的文件列表
     *
     * @param childId 文件id
     * @return 列表信息
     */
    @GetMapping("/getFileAtDirUnderList")
    public Result getFileAtDirUnderList(@RequestParam("childId")Integer childId){
        return fileService.getFileAtDirUnderList(childId);
    }

    /**
     * 获取该id文件夹下的所有目录信息
     *
     * @param parentId 父id
     * @return 列表信息：目录名 + id
     */
    @GetMapping("/getDirListByParentId")
    public Result getDirListByParentId(@RequestParam("parentId")Integer parentId){
        return fileService.getDirListByParentId(parentId);
    }

    /**
     * 获取该文件id所在目录下的目录列表
     *
     * @param childId 文件id
     * @return 列表信息
     */
    @GetMapping("/getFileAtDirUnderDirList")
    public Result getFileAtDirUnderDirList(@RequestParam("childId")Integer childId){
        return fileService.getFileAtDirUnderDirList(childId);
    }

    /**
     * 将当前文件或目录移动到某个目录下
     *
     * @param fromFileId 需要移动的文件或目录
     * @param toDirId 移动到的目录
     * @return 移动状况
     */
    @PutMapping("/modifyFileLocation")
    public Result modifyFileLocation(@RequestParam("fromFileId")Integer fromFileId,
                                     @RequestParam("toDirId")Integer toDirId){
        return fileService.modifyFileLocation(fromFileId,toDirId);
    }
}
