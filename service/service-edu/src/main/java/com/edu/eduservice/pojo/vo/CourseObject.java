package com.edu.eduservice.pojo.vo;

import lombok.Data;

/**
 * @CreateTime: 2022-08-26
 * 填写科恒基本信息VO类
 */
@Data
public class CourseObject {
    private String title;//课程标题
    private String name;//讲师姓名
    private Integer lessonNum;//总课时
    private String oneSubject;//一级分类
    private String twoSubject;//二级分类
    private String description;//秒速
    private double price;//价格
}
