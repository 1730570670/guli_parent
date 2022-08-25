package com.edu.eduservice.pojo.vo;

import com.edu.eduservice.pojo.EduSubject;
import lombok.Data;


import java.util.List;

/**
 * @CreateTime: 2022-08-24
 * 查询一级分类,二级分类对象
 */
@Data
public class SubjectObject {
    private String id;
    private String title;
    private List<EduSubject> subject;
}
