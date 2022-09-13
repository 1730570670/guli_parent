package com.edu.eduservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.commonutils.R;
import com.edu.eduservice.pojo.EduCourse;
import com.edu.eduservice.pojo.EduCourseDescription;
import com.edu.eduservice.pojo.vo.CourseObject;
import com.edu.eduservice.service.EduCourseDescriptionService;
import com.edu.eduservice.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @CreateTime: 2022-08-26
 */
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
@Transactional//开启事务管理
public class EduCourseController {

    //描述
    @Autowired
    private EduCourseDescriptionService descriptionService;

    @Autowired
    private EduCourseService eduCourseService;

    /**
     * 添加课程 添加描述
     * @param courseObject
     * @return
     */
    @PostMapping("/saveCourse")
    public R saveCourse(@RequestBody CourseObject courseObject){
        //获取到相关信息
        String title = courseObject.getTitle();//课程标题
        String name = courseObject.getName();//讲师id
        Integer lessonNum = courseObject.getLessonNum();//课程总数
        String oneSubject = courseObject.getOneSubject();//一级分类id
        String cover = courseObject.getCover();
        String twoSubject = courseObject.getTwoSubject();//二级分类id
        String description = courseObject.getDescription();//描述
        double price = courseObject.getPrice();//价格

        //1:添加课程信息(EduCourse)
        EduCourse eduCourse = new EduCourse();
        eduCourse.setTeacherId(name);//设置讲师ID
        eduCourse.setSubjectId(twoSubject);//设置一级分类ID
        eduCourse.setSubjectParentId(oneSubject);//设置二级分类ID
        eduCourse.setTitle(title);//设置标题
        eduCourse.setPrice(BigDecimal.valueOf(price));//设置价格
        eduCourse.setLessonNum(lessonNum);//设置课程数量
        eduCourse.setCover(cover);//设置讲师头像
        //添加到数据库
        boolean isSaveEduCourse = eduCourseService.save(eduCourse);

        //2:添加描述(EduCourseDescription)
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(eduCourse.getId());//添加简介ID(eduCourse添加成功储存ID)
        eduCourseDescription.setDescription(description);//设置描述
        boolean isSaveDescription = descriptionService.save(eduCourseDescription);

        if(!isSaveEduCourse || !isSaveDescription){
            return R.error().message("添加失败");
        }
        return R.ok().message("添加成功!").data("id",eduCourse.getId());
    }


    /**
     * 根据课程ID查询信息
     * @param id
     * @return
     */
    @GetMapping("/searchByIdCourse/{id}")
    public R searchByIdCourse(@PathVariable String id){
        LambdaQueryWrapper<EduCourse> eduCourseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        eduCourseLambdaQueryWrapper.eq(EduCourse::getId,id);//查询具体Course信息
        EduCourse eduCourse = eduCourseService.getOne(eduCourseLambdaQueryWrapper);//查询信息

        LambdaQueryWrapper<EduCourseDescription> eduDe = new LambdaQueryWrapper<>();
        eduDe.eq(EduCourseDescription::getId,id);//查询具体Description信息
        EduCourseDescription description = descriptionService.getOne(eduDe);//查询信息

        //两个都不为空
        if(eduCourse!=null && description!=null){
            return R.ok().data("eduCourse",eduCourse).data("description",description);
        }
        return R.error().message("无法找到数据!");
    }


    /**
     * 根据ID修改课程信息,以及课程描述
     * @param courseObject
     * @return
     */
    @PostMapping("/updateByIdCourse/{id}")
    public R updateByIdCourse(@RequestBody CourseObject courseObject, @PathVariable String id){
        //获取到相关信息
        String title = courseObject.getTitle();//课程标题
        String name = courseObject.getName();//讲师id
        Integer lessonNum = courseObject.getLessonNum();//课程总数
        String oneSubject = courseObject.getOneSubject();//一级分类id
        String cover = courseObject.getCover();
        String twoSubject = courseObject.getTwoSubject();//二级分类id
        String description = courseObject.getDescription();//描述
        double price = courseObject.getPrice();//价格

        //修改Course

        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setTeacherId(name);//设置讲师ID
        eduCourse.setSubjectId(twoSubject);//设置一级分类ID
        eduCourse.setSubjectParentId(oneSubject);//设置二级分类ID
        eduCourse.setTitle(title);//设置标题
        eduCourse.setPrice(BigDecimal.valueOf(price));//设置价格
        eduCourse.setLessonNum(lessonNum);//设置课程数量
        eduCourse.setCover(cover);//设置讲师头像
        boolean isEduCourse = eduCourseService.updateById(eduCourse);


        //修改描述
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(id);//设置ID
        eduCourseDescription.setDescription(description);//设置新描述
        boolean isEduCourseDescription = descriptionService.updateById(eduCourseDescription);

        if(isEduCourse && isEduCourseDescription){
            return R.ok().message("修改成功!");
        }

        return R.error().message("修改失败!");
    }


    /**
     * 分页查询课程信息
     * @return
     */
    @GetMapping("/searchCourse/{pageNum}")
    public R searchCourse(@PathVariable Integer pageNum){
        Page<EduCourse> eduCoursePage = new Page<>(pageNum,10);
        Page<EduCourse> page = eduCourseService.page(eduCoursePage);
        return R.ok().data("item",eduCoursePage);
    }

    /**
     * 根据课程ID发布课程信息
     * @param id
     * @param status 课程状态 0:Draft未发布  1:Normal已发布
     * @return
     */
    @PutMapping("/publishCourse/{id}/{status}")
    public R publishCourse(@PathVariable String id, @PathVariable String status){
        if(status.equals("0")){
            LambdaUpdateWrapper<EduCourse> eduCourseLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            eduCourseLambdaUpdateWrapper.set(EduCourse::getStatus,"Draft");
            eduCourseLambdaUpdateWrapper.eq(EduCourse::getId,id);
            boolean update = eduCourseService.update(eduCourseLambdaUpdateWrapper);
            if(update){
                 return R.ok().message("成功下架!");
            }
        }else if (status.equals("1")){
            LambdaUpdateWrapper<EduCourse> eduCourseLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            eduCourseLambdaUpdateWrapper.set(EduCourse::getStatus,"Normal");
            eduCourseLambdaUpdateWrapper.eq(EduCourse::getId,id);
            boolean update = eduCourseService.update(eduCourseLambdaUpdateWrapper);
            if(update){
                return R.ok().message("成功发布!");
            }
        }else{
            return R.error().message("参数有误!");
        }
        return R.error().message("发布失败!");
    }
}
