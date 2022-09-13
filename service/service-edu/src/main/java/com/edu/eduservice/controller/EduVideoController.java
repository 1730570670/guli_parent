package com.edu.eduservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.edu.commonutils.R;
import com.edu.eduservice.pojo.EduVideo;
import com.edu.eduservice.service.EduVideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @CreateTime: 2022-08-28
 */
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
@Slf4j
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    /**
     * 添加小节信息
     * @param eduVideo
     * @return
     */
    @PostMapping("/saveVideo")
    public R saveVideo(@RequestBody EduVideo eduVideo){
        log.info(eduVideo.toString());
        boolean isSave = videoService.save(eduVideo);
        if(isSave){
            return R.ok().message("添加成功!");
        }
        return R.error().message("添加失败!");
    }

    /**
     * 根据ID查询小节具体信息
     * @param id
     * @return
     */
    @GetMapping("searchVideoById/{id}")
    public R searchVideoById(@PathVariable String id){
        LambdaQueryWrapper<EduVideo> eduVideoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        eduVideoLambdaQueryWrapper.eq(EduVideo::getId,id);
        EduVideo eduVideo = videoService.getOne(eduVideoLambdaQueryWrapper);

        if(eduVideo!=null){
            return R.ok().data("item",eduVideo);
        }
        return R.error().message("查询失败");
    }

    /**
     * 根据ID修改小节信息
     * @param eduVideo
     * @return
     */
    @PutMapping("/updateVideoById")
    public R updateVideoById(@RequestBody EduVideo eduVideo){
        boolean isUpdate = videoService.updateById(eduVideo);
        if (isUpdate){
            return R.ok().message("修改成功!");
        }
        return R.error().message("修改失败!");
    }


    /**
     * 根据ID删除小节信息
     * @param id
     * @return
     */
    @DeleteMapping("/deleteVideoById/{id}")
    public R deleteByIdVideo(@PathVariable String id){
        boolean removeById = videoService.removeById(id);
        if(removeById){//删除成功
            return R.ok().message("删除成功!");
        }
        return R.error().message("删除失败!");
    }
}
