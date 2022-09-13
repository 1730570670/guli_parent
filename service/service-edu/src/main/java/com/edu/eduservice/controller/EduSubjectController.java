package com.edu.eduservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.edu.commonutils.R;
import com.edu.eduservice.pojo.EduSubject;
import com.edu.eduservice.pojo.vo.SubjectObject;
import com.edu.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @CreateTime: 2022-08-23a
 * excel管理
 */
@RestController
@RequestMapping("/eduservice/excel")
@CrossOrigin
@Api(description = "excel文件管理")
public class EduSubjectController {

    @Autowired
    private EduSubjectService eduSubjectService;



    /**
     * 上传excel文件添加分类(一级标题,二级标题)
     *
     * @param file
     * @return
     */
    @ApiOperation("上传excel文件添加分类")
    @PostMapping("/upload")
    public R readSubject(MultipartFile file) {
        //判断文件是否以xlsx或xls结尾
        //获取文件的后缀名
        String sufix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        if (!sufix.equals(".xlsx") && !sufix.equals(".xls")) {
            //文件类型不匹配
            return R.error().message("文件类型必须是excel,且为xlsx或xls后缀!");
        }
        //文件类型匹配,进行添加操作
        boolean isUpload = eduSubjectService.excelSubject(file, eduSubjectService);
        if (isUpload) {//上传成功
            return R.ok().message("上传成功!");
        }
        return R.error().message("上传失败!");
    }

    /**
     * 查询分类列表信息
     *
     * @return
     */
    @ApiOperation("查询一级分类以及二级分类")
    @GetMapping("/subjectList")
    public R subjectList() {
        LambdaQueryWrapper<EduSubject> queryWrapper = new LambdaQueryWrapper<>();//条件构造器
        queryWrapper.eq(EduSubject::getParentId, "0");
        List<EduSubject> eduSubjects = eduSubjectService.list(queryWrapper);
        //创建List保存实体对象
        List<SubjectObject> list = new ArrayList<>();
        for (int i = 0; i < eduSubjects.size(); i++) {
            SubjectObject subjectObject = new SubjectObject();//创建实体对象
            subjectObject.setId(eduSubjects.get(i).getId());//设置对应的ID
            subjectObject.setTitle(eduSubjects.get(i).getTitle());//设置对应的标题
            //根据以及标题的id查询属于一级标题的二级标题
            LambdaQueryWrapper<EduSubject> subjectQuery = new LambdaQueryWrapper<>();
            subjectQuery.eq(EduSubject::getParentId, eduSubjects.get(i).getId());//将title与parentID进行对比
            List<EduSubject> subjectList = eduSubjectService.list(subjectQuery);//查询一级标题下的二级标题
            subjectObject.setSubject(subjectList);
            list.add(subjectObject);
        }
        return R.ok().data("item", list);
    }

}
