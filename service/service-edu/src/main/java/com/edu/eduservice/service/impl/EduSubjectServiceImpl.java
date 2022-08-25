package com.edu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.commonutils.R;
import com.edu.eduservice.listen.ExcelObjectListen;
import com.edu.eduservice.pojo.EduSubject;
import com.edu.eduservice.pojo.excel.ExcelSubject;
import com.edu.eduservice.service.EduSubjectService;
import com.edu.eduservice.mapper.EduSubjectMapper;
import com.edu.servicebase.handler.GlobalExceptionHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author 1730570670
 * @description 针对表【edu_subject(课程科目)】的数据库操作Service实现
 * @createDate 2022-08-23 10:25:13
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject>
        implements EduSubjectService {

    @Override
    public boolean excelSubject(MultipartFile file,EduSubjectService eduSubjectService) {
        try{
            EasyExcel.read(file.getInputStream(), ExcelSubject.class, new ExcelObjectListen(eduSubjectService)).sheet().doRead();
            return true;
        }catch (Exception e){
            return false;
        }
    }

}




