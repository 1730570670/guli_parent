package com.edu.eduservice.controller;

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

    @PostMapping("/test")
    public R test(@RequestBody CourseObject courseObject){
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
        eduCourse.setSubjectId(oneSubject);//设置一级分类ID
        eduCourse.setSubjectParentId(twoSubject);//设置二级分类ID
        eduCourse.setTitle(title);//设置标题
        eduCourse.setPrice(BigDecimal.valueOf(price));//设置价格
        eduCourse.setLessonNum(lessonNum);//设置课程数量
        eduCourse.setCover(cover);//设置讲师头像
        //添加到数据库
        boolean isSaveEduCourse = eduCourseService.save(eduCourse);
        System.out.println(isSaveEduCourse);
        //2:添加描述(EduCourseDescription)
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setDescription(description);
        boolean isSaveDescription = descriptionService.save(eduCourseDescription);

        if(!isSaveEduCourse || !isSaveDescription){
            return R.error().message("添加失败");
        }
        return R.ok().message("添加成功!");
    }

}
