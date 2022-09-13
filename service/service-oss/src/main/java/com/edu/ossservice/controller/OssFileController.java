package com.edu.ossservice.controller;

import com.edu.commonutils.R;
import com.edu.ossservice.service.OssFileService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * @CreateTime: 2022-08-22
 * oss服务层
 */
@RestController
@RequestMapping("/ossservice/oss")
@CrossOrigin
@Slf4j
public class OssFileController {

    @Autowired
    private OssFileService ossFileService;


    /**
     * 文件的上传
     *
     * @param file
     * @return imageUrl
     * @throws IOException
     */
    @ApiOperation("文件的上传")
    @PostMapping("/upload")
    public R fileUpload(@ApiParam(name = "file", value = "前端组件的Name", required = true)
                                    MultipartFile file) throws IOException {
        String url = ossFileService.fileUpload(file);
        if ("20001".equals(url)) {
            //判断上传是否失败(200001:自定义Service中的失败字符串)
            return R.error().message("头像上传失败!");
        }
        //成功返回
        return R.ok().data("imageUrl", url);
    }

    /**
     * 视频的上传
     *
     * @param file
     * @return imageUrl
     * @throws IOException
     */
    @ApiOperation("视频的上传")
    @PostMapping("/videoUpload")
    public R videoFileUpload(@ApiParam(name = "file", value = "前端组件的Name", required = true)
                                MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        if(!".mp4".equals(substring)){
            return R.error().message("只能上传mp4格式");
        }
        String url = ossFileService.videoFileUpload(file);
        if ("20001".equals(url)) {
            //判断上传是否失败(200001:自定义Service中的失败字符串)
            return R.error().message("头像上传失败!");
        }
        //成功返回
        return R.ok().data("videoUrl", url);
    }

    /**
     * 视频的上传
     *
     * @param fileUrl
     * @throws IOException
     */
    @ApiOperation("文件的删除")
    @PostMapping("/deleteFile")
    public R deleteFileUpload(String fileUrl) {
        String substring = fileUrl.substring(fileUrl.indexOf("video"));
        ossFileService.deleteFile(substring);
        //成功返回
        return R.ok().message("删除成功!");
    }

}
