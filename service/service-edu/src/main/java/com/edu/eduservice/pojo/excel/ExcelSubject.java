package com.edu.eduservice.pojo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @CreateTime: 2022-08-23
 */
@Data
public class ExcelSubject {


    @ExcelProperty(value = "一级分类",index = 0)
    private String title;
    @ExcelProperty(value = "二级分类",index = 1)
    private String subject;


}
