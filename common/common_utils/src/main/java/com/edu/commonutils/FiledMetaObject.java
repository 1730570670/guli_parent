package com.edu.commonutils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @CreateTime: 2022-08-19
 */
@Slf4j
@Component
public class FiledMetaObject implements MetaObjectHandler {

    //插入时间
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("gmtCreate", new Date());
        metaObject.setValue("gmtModified",new Date());
        log.info(metaObject.toString());
    }

    // 更新时间
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("gmtModified",new Date());
    }
}
