package com.edu.eduservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.edu.commonutils.R;
import com.edu.eduservice.pojo.EduChapter;
import com.edu.eduservice.pojo.EduVideo;
import com.edu.eduservice.service.EduChapterService;
import com.edu.eduservice.service.EduVideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @CreateTime: 2022-08-26
 */
@RestController
@RequestMapping("/eduservice/chapter")
@Transactional//开启事务
@CrossOrigin
@Slf4j
public class EduChapterController {

    @Autowired
    private EduChapterService eduChapterService;
    @Autowired
    private EduVideoService videoService;

    /**
     * 根据ID查询章节信息
     * @param id
     * @return
     */
    @GetMapping("/chapterList/{id}")
    public R chapterList(@PathVariable String id) {
        LambdaQueryWrapper<EduChapter> eduChapterLambdaQueryWrapper = new LambdaQueryWrapper<>();//创建条件构造器
        //查询该ID拥有的章节,并升序处理
        eduChapterLambdaQueryWrapper.eq(EduChapter::getCourseId, id);
        eduChapterLambdaQueryWrapper.orderByAsc(EduChapter::getSort);
        List<EduChapter> list = eduChapterService.list(eduChapterLambdaQueryWrapper);
        for(int i=0;i<list.size();i++){
            LambdaQueryWrapper<EduVideo> eduVideoLambdaQueryWrapper = new LambdaQueryWrapper<>();//条件构造器
            eduVideoLambdaQueryWrapper.eq(EduVideo::getChapterId,list.get(i).getId());//设置ChapterID
            eduVideoLambdaQueryWrapper.eq(EduVideo::getCourseId,list.get(i).getCourseId());//设置CourseID
            eduVideoLambdaQueryWrapper.orderByAsc(EduVideo::getSort);//升序
            List<EduVideo> listVideo = videoService.list(eduVideoLambdaQueryWrapper);
            list.get(i).setVideos(listVideo);//设置信息

        }
        if (list != null) {
            return R.ok().data("item", list);
        }
        return R.error().message("查询失败!");
    }

    /**
     * 添加章节信息
     * @param eduChapter
     * @return
     */
    @PostMapping("/saveChapter")
    public R saveChapter(@RequestBody EduChapter eduChapter) {

        boolean save = eduChapterService.save(eduChapter);
        log.info(eduChapter.toString());
        if (save) {//添加成功
            return R.ok().message("添加成功!");
        }
        return R.error().message("添加失败!");
    }

    /**
     * 更新章节信息
     * @param eduChapter
     * @return
     */
    @PutMapping("/updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){
        boolean isUpdate = eduChapterService.updateById(eduChapter);
        if(isUpdate){
            return R.ok().message("修改成功!");
        }
        return R.error().message("修改失败!");
    }

    /**
     * 根据查询章节具体信息
     * @param id
     * @return
     */
    @GetMapping("/searchChapterById/{id}")
    public R searchChapterById(@PathVariable String id){
        LambdaQueryWrapper<EduChapter> eduChapterLambdaQueryWrapper = new LambdaQueryWrapper<>();
        eduChapterLambdaQueryWrapper.eq(EduChapter::getId,id);
        EduChapter one = eduChapterService.getOne(eduChapterLambdaQueryWrapper);
        if(one!=null){
            return R.ok().data("item",one);
        }
        return R.error();
    }

    @DeleteMapping("deleteChapterById/{id}")
    public R deleteChapterById(@PathVariable String id){
        System.out.println(id);
        // 删除视频小节
        LambdaQueryWrapper<EduVideo> eduVideoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        eduVideoLambdaQueryWrapper.eq(EduVideo::getChapterId,id);
        boolean remove = videoService.remove(eduVideoLambdaQueryWrapper);
        if(remove){//视频删除成功在实行删除章节
            boolean isChapter = eduChapterService.removeById(id);
            if(isChapter){
                return R.ok().message("删除成功!");
            }
        }
        return R.error().message("删除失败!");
    }
}
