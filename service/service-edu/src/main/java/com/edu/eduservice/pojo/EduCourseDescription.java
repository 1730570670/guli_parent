package com.edu.eduservice.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 课程简介
 *
 * @TableName edu_course_description
 */
@TableName(value = "edu_course_description")
@Data
public class EduCourseDescription implements Serializable {
    /**
     * 课程ID
     */
    @TableId
    private String id;

    /**
     * 课程简介
     */
    private String description;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModified;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}