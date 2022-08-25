package com.edu.eduservice.pojo.vo;

import lombok.Data;

/**
 * @CreateTime: 2022-08-18
 * 条件查询讲师信息对象
 */
@Data
public class QueryEduTeacher {
    Integer level;
    String name;
    String startTime;
    String endTime;
}
