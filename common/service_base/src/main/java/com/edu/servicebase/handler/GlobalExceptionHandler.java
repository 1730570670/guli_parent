package com.edu.servicebase.handler;

import com.edu.commonutils.R;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @CreateTime: 2022-08-15
 * 全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 数据库发生约束异常(讲师名字存在重复)  返回json数据
     * @param e
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    public R duplicateKeyExceptionHandler(Exception e){
        e.printStackTrace();
        return R.error().message("已存在此讲师!");
    }
    /**
     * 全局异常处理  返回json数据
     * @param e
     * @return R
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R exceptionHandler(Exception e){
        e.printStackTrace();
        return R.error().message("请求发生了错误!");
    }
}
