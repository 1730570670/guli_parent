package com.edu.eduservice.service;

import com.edu.commonutils.R;
import com.edu.eduservice.pojo.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.eduservice.pojo.excel.ExcelSubject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
* @author 1730570670
* @description 针对表【edu_subject(课程科目)】的数据库操作Service
* @createDate 2022-08-23 10:25:13
*/
public interface EduSubjectService extends IService<EduSubject> {
    boolean excelSubject(MultipartFile file, EduSubjectService eduSubjectService);
}
